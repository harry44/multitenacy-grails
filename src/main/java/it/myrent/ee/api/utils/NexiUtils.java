package it.myrent.ee.api.utils;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.factory.PrimanotaFactory;
import it.myrent.ee.db.*;
import org.grails.web.json.JSONArray;
import org.grails.web.json.JSONObject;
import org.hibernate.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Shivangani on 9/24/2018.
 */
public class NexiUtils {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");

    public static Map importTransaction(Session sx, String authCode, String description, Double authAmt, String dataTransazione, MROldDeviceConfiguration deviceConfiguration, MROldContrattoNoleggio contratto){
        Map transactionalMap = new HashMap();
        try{
            String requestUrl="";

            MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, MROldSetting.IS_NEXI_TEST);
            boolean isNexiTest = Boolean.valueOf(setting.getValue());
            if(isNexiTest){
                requestUrl = "https://coll-ecommerce.nexi.it/" + "ecomm/api/contratti/creazioneDaPosFisico";
            }else{
                requestUrl = "https://ecommerce.nexi.it/" + "ecomm/api/contratti/creazioneDaPosFisico";
            }
            String apiKey =deviceConfiguration.getApiKey(); // Alias fornito da Nexi
            String chiaveSegreta = deviceConfiguration.getSecretkey();// Chiave segreta fornita da Nexi
            String numeroContratto = ((int )(Math.random() * 50 + 1))+""+contratto.getPrefisso() + "" + contratto.getId();
            String idPOSFisico =deviceConfiguration.getPhysicalIDPOS(); // ID del POS fisico
            String codiceAutorizzazione =authCode; // Codice dia autorizzazione della transazione
            Integer importoTemp = new Double(authAmt*100).intValue();
            String importo = importoTemp.toString();
            String stan = "";
            String descrizione ="";//desc.getTextFieldValue();
            String mail = "";//contratto.getCliente().getEmail()!=null ? contratto.getCliente().getEmail():"test@gmail.com";
            long timeStamp = new Date().getTime();
            String stringaMac = "apiKey=" + apiKey
                    + "numeroContratto=" + numeroContratto
                    + "idPOSFisico=" + idPOSFisico
                    + "codiceAutorizzazione=" + codiceAutorizzazione
                    + "stan=" + stan
                    + "importo=" + importo
                    + "descrizione=" + descrizione
                    + "mail=" + mail
                    + "timeStamp=" + timeStamp
                    + chiaveSegreta;
            String macCalculated = hashMac(stringaMac);
            JSONObject contratt = new JSONObject();
            contratt.put("numeroContratto", numeroContratto);
            contratt.put("idPOSFisico", idPOSFisico);
            contratt.put("codiceAutorizzazione", codiceAutorizzazione);
            contratt.put("dataTransazione", dataTransazione);
            contratt.put("importo", importo);
            JSONObject json = new JSONObject();
            json.put("apiKey", apiKey);
            json.put("contratto", contratt);
            json.put("timeStamp", timeStamp);
            json.put("mac", macCalculated);
            String result = sendRequest(requestUrl, json);
            JSONObject jobject = new JSONObject(result);

            if (jobject.has("esito") && jobject.getString("esito").equalsIgnoreCase("ok")) {
                MROldNexiTransactionDeposit nexitransaction = storeNexiTrxDeposit(sx, result, authCode, authAmt.toString(), dataTransazione,description, true);
                if(nexitransaction!=null){
                    nexitransaction.setContratto(contratto);
                    contratto.setNexiDeposit(nexitransaction);

                    sx.saveOrUpdate(nexitransaction);
                    sx.saveOrUpdate(contratto);
                    transactionalMap.put("msg",bundle.getString("MRRentaAgreement.cardToken.label"));
                    transactionalMap.put("color","Green");
                    transactionalMap.put("nexiId", nexitransaction.getId());
                    transactionalMap.put("nexitransaction",nexitransaction.toString());
                } else {
                    transactionalMap.put("msg", bundle.getString("MRRentaAgreement.somethingwentwrong.label"));
                    transactionalMap.put("color","Red");
                }
            } else {
                if(jobject.has("errore")){
                    transactionalMap.put("msg", jobject.getJSONObject("errore").get("messaggio"));
                    transactionalMap.put("color","Red");
                }else{
                    transactionalMap.put("msg", bundle.getString("MRRentaAgreement.payErr.cardNotToken.label"));
                    transactionalMap.put("color","Red");
                }
            }
        } catch(Exception ex){
            transactionalMap.put("msg", bundle.getString("MRRentaAgreement.somethingwentwrong.label"));
            transactionalMap.put("color","Red");
        }
        return transactionalMap;

    }

    private static String hashMac(String stringaMac) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] in = digest.digest(stringaMac.getBytes("UTF-8"));

        final StringBuilder builder = new StringBuilder();
        for (byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
    public static void cashTransaction(Session sx, String amountCharge, User userImpl, MROldContrattoNoleggio contratto, MROldDeviceConfiguration deviceConfiguration, boolean isTokenFromCash) {

        MROldPagamento pagamentoVPOS = null;
        if (isTokenFromCash) {
            pagamentoVPOS = deviceConfiguration.getPaymentToken();
        } else {
            pagamentoVPOS = deviceConfiguration.getPayment();
        }

        MROldPrimanota primanotaIncasso = PrimanotaFactory.creaPrimanotaIncassoCliente(
                sx,
                userImpl.getSedeOperativa()!=null?userImpl.getSedeOperativa():contratto.getSedeUscita(), //sede
                contratto,
                pagamentoVPOS,
                null, //garanzia
                Double.parseDouble(amountCharge),
                userImpl);

        MROldNumerazione numerazionePrimenote = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, userImpl);
        Integer numeroRegistrazionePrimenote = NumerazioniUtils.aggiornaProgressivo(sx, numerazionePrimenote, FormattedDate.annoCorrente(), 1);
        primanotaIncasso.setNumerazione(numerazionePrimenote);
        primanotaIncasso.setNumeroRegistrazione(numeroRegistrazionePrimenote);

        sx.saveOrUpdate(primanotaIncasso);


    }

    public static Map immediateCharge(Session sx, Integer importo, String authCode, String authAmount, String txnDescript, String dataTransazione, MROldDeviceConfiguration deviceConfiguration, MROldContrattoNoleggio contratto, MROldNexiTransactionDeposit nexiTransactionDeposit, User user){
        Map transactionalMap = new HashMap();
        try{
            String requestUrl = "";
            MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, MROldSetting.IS_NEXI_TEST);
            boolean isNexiTest = Boolean.valueOf(setting.getValue());
            if(isNexiTest){
                requestUrl = "https://coll-ecommerce.nexi.it/ecomm/api/recurring/pagamentoRicorrente";
            } else {
                requestUrl = "https://ecommerce.nexi.it/ecomm/api/recurring/pagamentoRicorrente";
            }

            String APIKEY = deviceConfiguration.getChargeApiKey();// "payment_3444168"; // Alias fornito da Nexi
            // Modified by Mauro for Production
            //APIKEY="payment_3550007";
            // Modified by Mauro for Production
            String CHIAVESEGRETA = deviceConfiguration.getChargeSecretKey();//"CsT830052L63QHNd1E351uh73272Q23h175650k9wU28T7EU1Hd6l156N5I2oBY6U7OW7kP34282C5965r8V0hpG72ojq5B58896G4Q6oXGc36a6z3Tn6J271B4N33p45C28369j7E025O2245GK7T5p1MNN5T25S05UJxCKH0TMc98fBQ66M2NxRDzrR66c7RG2K367D4xiV54X9kY592K5E3V1X1U01AO85P3n4z28eJIL13t8Ww3P28eg24y2";// Chiave segreta fornita da Nexi
            //CHIAVESEGRETA = "C8535Q64HxA60hnj6aKTvi95Wf59224T5S1480e8";
            String numContratto = nexiTransactionDeposit.getNumeroContratto();
            String divisa = "978";
            String scadenza = nexiTransactionDeposit.getScadenzaCarta();
            String codiceGruppo = deviceConfiguration.getCodiceGruppo();//"GRUPPOTEST";

            //Modified by Mauro for production
            //codiceGruppo="NOLEGGIARE";

            long timeStamp = System.currentTimeMillis();//date.getTime();
            String stringaMac = "apiKey=" + APIKEY
                    + "numeroContratto=" + numContratto
                    + "codiceTransazione=" + dataTransazione
                    + "importo=" + importo.toString()
                    + "divisa=" + divisa
                    + "scadenza=" + scadenza
                    + "timeStamp=" + timeStamp
                    + CHIAVESEGRETA;
            System.out.println(stringaMac);

            String macCalculated = hashMac(stringaMac);
            System.out.println("Request Mac: " + macCalculated);

            JSONObject jsonParametriAggiuntivi = new JSONObject();
            jsonParametriAggiuntivi.put("mail", contratto.getCliente().getEmail() != null ? contratto.getCliente().getEmail() : "test@gmail.com");
            jsonParametriAggiuntivi.put("nome", contratto.getCliente().getNome());
            jsonParametriAggiuntivi.put("cognome", contratto.getCliente().getCognome());

            JSONObject json = new JSONObject();
            json.put("numeroContratto", numContratto);
            json.put("codiceTransazione", dataTransazione);
            json.put("importo", importo.toString());
            json.put("scadenza", scadenza);
            json.put("codiceGruppo", codiceGruppo);
            json.put("divisa", divisa);
            json.put("apiKey", APIKEY);
            //json.put("parametriAggiuntivi", jsonParametriAggiuntivi);
            json.put("timeStamp", timeStamp);
            json.put("mac", macCalculated);

            String resultTemp = sendRequest(requestUrl, json);

            JSONObject jobject = new JSONObject(resultTemp);
            if (jobject.has("esito") && jobject.getString("esito").equalsIgnoreCase("ok")) {
                MROldNexiTransactionDeposit nexitransaction = storeNexiTrxDeposit(sx, resultTemp, authCode, authAmount, dataTransazione,txnDescript, false);
                if(nexitransaction!=null){
                    nexitransaction.setContratto(contratto);
                    contratto.setNexiDeposit(nexitransaction);

                    sx.saveOrUpdate(nexitransaction);
                    sx.saveOrUpdate(contratto);
                    transactionalMap.put("msg",bundle.getString("MRRentaAgreement.amountDebit.label"));
                    transactionalMap.put("color","Green");
                    cashTransaction(sx, importo.toString(), user, contratto, deviceConfiguration, false);
                    MROldPaymentReceipt scontrino = creaPaymentReceipt(sx, contratto, json, jobject, MROldPaymentReceipt.TYPE_CHARGE);
                    transactionalMap.put("payRecId",scontrino.getId());
                    transactionalMap.put("msg",bundle.getString("MRRentaAgreement.amountDebit.cashBookCreated.label"));
                } else {
                    transactionalMap.put("msg", bundle.getString("MRRentaAgreement.somethingwentwrong.label"));
                }
            } else {
                if (jobject.has("errore")) {
                    transactionalMap.put("msg", jobject.getJSONObject("errore").get("messaggio"));
                    transactionalMap.put("color","Red");
                } else {
                    transactionalMap.put("msg", "Payment Error");
                    transactionalMap.put("color","Red");
                }
            }

        } catch(Exception ex){
            transactionalMap.put("msg", bundle.getString("MRRentaAgreement.somethingwentwrong.label"));
        }
        return transactionalMap;
    }


    private static MROldNexiTransactionDeposit storeNexiTrxDeposit(Session sx, String responseString, String authCode, String authAmount, String dataTransazione, String txnDescript, boolean isDeposit){
        try{
            JSONObject jobject = new JSONObject(responseString);
            if (jobject.has("esito") && jobject.getString("esito").equalsIgnoreCase("ok")) {
                MROldNexiTransactionDeposit nexitransaction = new MROldNexiTransactionDeposit();
                nexitransaction.setAuthCode(authCode);
                nexitransaction.setAuthAmount(authAmount);
                nexitransaction.setDate(dataTransazione);
                nexitransaction.setDescription(txnDescript);
                nexitransaction.setIsDeposit(isDeposit);
                if (jobject.has("esito")) {
                    nexitransaction.setEsito(jobject.get("esito").toString());
                }
                if (jobject.has("idOperazione")) {
                    nexitransaction.setIdOperazione(jobject.get("idOperazione").toString());
                }
                if (jobject.has("timeStamp")) {
                    nexitransaction.setTimeStamp(jobject.get("timeStamp").toString());
                }
                if (jobject.has("mac")) {
                    nexitransaction.setMac(jobject.get("mac").toString());
                }
                if(isDeposit){
                    if (jobject.getJSONObject("infoContratto").has("numeroMerchant")) {
                        nexitransaction.setNumeroMerchant(jobject.getJSONObject("infoContratto").get("numeroMerchant").toString());
                    }
                    if (jobject.getJSONObject("infoContratto").has("codiceGruppo")) {
                        nexitransaction.setCodiceGruppo(jobject.getJSONObject("infoContratto").get("codiceGruppo").toString());
                    }
                    if (jobject.getJSONObject("infoContratto").has("hashPan")) {
                        nexitransaction.setHashPan(jobject.getJSONObject("infoContratto").get("hashPan").toString());
                    }
                    if (jobject.getJSONObject("infoContratto").has("tipoCarta")) {
                        nexitransaction.setTipoCarta(jobject.getJSONObject("infoContratto").get("tipoCarta").toString());
                    }
                    if (jobject.getJSONObject("infoContratto").has("statoPrimoPag")) {
                        nexitransaction.setStatoPrimoPag(jobject.getJSONObject("infoContratto").get("statoPrimoPag").toString());
                    }
                    if (jobject.getJSONObject("infoContratto").has("dataAttivazione")) {
                        nexitransaction.setDataAttivazione(jobject.getJSONObject("infoContratto").get("dataAttivazione").toString());
                    }
                    if (jobject.getJSONObject("infoContratto").has("numeroContratto")) {
                        nexitransaction.setNumeroContratto(jobject.getJSONObject("infoContratto").get("numeroContratto").toString());
                    }

                    if (jobject.getJSONObject("infoContratto").has("scadenzaCarta")) {
                        nexitransaction.setScadenzaCarta(jobject.getJSONObject("infoContratto").get("scadenzaCarta").toString());
                    }

                    if (jobject.getJSONObject("infoContratto").has("codTrans")) {
                        nexitransaction.setCodTrans(jobject.getJSONObject("infoContratto").get("codTrans").toString().replaceAll("POS: ",""));
                        System.out.println("Codice Transazione: "+jobject.getJSONObject("infoContratto").get("codTrans").toString());
                    }
                    if (jobject.getJSONObject("infoContratto").has("codiceFiscale")) {
                        nexitransaction.setCodiceFiscale(jobject.getJSONObject("infoContratto").get("codiceFiscale").toString());
                    }
                } else {
                    if (jobject.has("codiceAutorizzazione")) {
                        nexitransaction.setCodiceAutorizzazione(jobject.get("codiceAutorizzazione").toString());
                    }
                    if (jobject.has("data")) {
                        nexitransaction.setDate(jobject.get("data").toString());
                    }
                    if (jobject.has("ora")) {
                        nexitransaction.setOra(jobject.get("ora").toString());
                    }
                    if (jobject.has("regione")) {
                        nexitransaction.setRegione(jobject.get("regione").toString());
                    }
                    if (jobject.has("nazione")) {
                        nexitransaction.setNazione(jobject.get("nazione").toString());
                    }

                    if (jobject.has("tipoProdotto")) {
                        nexitransaction.setTipoProdotto(jobject.get("tipoProdotto").toString());
                    }
                    if (jobject.has("ppo")) {
                        nexitransaction.setPpo(jobject.get("ppo").toString());
                    }

                    if (jobject.has("codiceConvenzione")) {
                        nexitransaction.setCodiceConvenzione(jobject.get("codiceConvenzione").toString());
                    }
                    if (jobject.has("brand")) {
                        nexitransaction.setBrand(jobject.get("brand").toString());
                    }
                    if (jobject.has("tipoTransazione")) {
                        nexitransaction.setTipoTransazione(jobject.get("tipoTransazione").toString());
                    }
                }
                sx.saveOrUpdate(nexitransaction);
                return nexitransaction;
            } else {
                return null;
            }
        } catch (Exception ex){
            return null;
        }
    }

    public static Map getTransactionCancelList(Session sx, MROldDeviceConfiguration deviceConfiguration, String startDate, String endDate){
        Map transactionResult = new HashMap();
        List transactionlist = new ArrayList();
        try{
            String requestUrl = "";
            MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, MROldSetting.IS_NEXI_TEST);
            boolean isNexiTest = Boolean.valueOf(setting.getValue());
            if(isNexiTest){
                requestUrl = "https://coll-servizi.cartasi.it/PerseoService/rest/PerseoWS/transazioni";
            } else {
                requestUrl = "https://servizi.cartasi.it/PerseoService/rest/PerseoWS/transazioni";
            }
            String idCliente = deviceConfiguration.getIdCliente();
            String idRichiesta = "1";
            String codEseSia = deviceConfiguration.getCodEseSia();
            String codiceAbi = deviceConfiguration.getCodiceAbi();
            String operazione_richiesta = "STORNO_PRE";
            String timeStamp = "" + System.currentTimeMillis();
            String idTerminale = deviceConfiguration.getPhysicalIDPOS();
            String chiaveSegreta = deviceConfiguration.getFetchSecretKey(); // this should be the right one
            String stringaMac = "idRichiesta=" + idRichiesta + "timeStamp=" + timeStamp
                    + "idCliente=" + idCliente + "codEseSia=" + codEseSia + "codiceAbi=" + codiceAbi + "idTerminale=" + deviceConfiguration.getPhysicalIDPOS() + "daData=" + startDate + "aData=" + endDate + chiaveSegreta;
            System.out.println(stringaMac);
            String macCalculated = hashMacFetchTransaction(stringaMac);
            JSONObject json = new JSONObject();
            json.put("idCliente", idCliente);
            json.put("idRichiesta", idRichiesta);
            json.put("timeStamp", timeStamp);
            json.put("mac", macCalculated);
            json.put("codEseSia", codEseSia);
            json.put("codiceAbi", codiceAbi);
            json.put("idTerminale", idTerminale);
            json.put("daData", startDate);
            json.put("aData", endDate);
            json.put("operazione_richiesta", operazione_richiesta);

            String resultTemp = sendRequest(requestUrl, json);

            JSONObject jobject = new JSONObject(resultTemp);
            if (jobject.has("esito") && jobject.getString("esito").equalsIgnoreCase("ok")) {
                if (jobject.has("transazioni")) {
                    transactionlist.addAll(parseNexiListJson(jobject));
                    transactionResult.put("transList", transactionlist);
                    transactionResult.put("msg", "Transaction List :- " + jobject.getString("numMovimenti"));
                    transactionResult.put("color","Green");
                } else {
                    try {
                        transactionResult.put("msg", "Transaction List has :- " + jobject.getString("numMovimenti"));
                        transactionResult.put("color","Green");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        transactionResult.put("msg", "Transaction List: No movement available");
                        transactionResult.put("color","Red");
                    }
                }
            } else {
                if (jobject.has("errore") && jobject.getJSONObject("errore").has("messaggio")) {
                    transactionResult.put("msg", jobject.getJSONObject("errore").get("messaggio"));
                    transactionResult.put("color","Red");
                } else if (jobject.has("errore") && jobject.getJSONObject("errore").has("descr")) {
                    transactionResult.put("msg", jobject.getJSONObject("errore").get("descr"));
                    transactionResult.put("color","Red");
                }  else {
                    transactionResult.put("msg", "Payment Error");
                    transactionResult.put("color","Red");
                }
            }

        } catch (Exception ex){
            transactionResult.put("msg", bundle.getString("MRRentaAgreement.somethingwentwrong.label"));
            transactionResult.put("color","Red");
        }
        return transactionResult;
    }

    public static Map getTransactionList(Session sx, MROldDeviceConfiguration deviceConfiguration, String startDate, String endDate){
        Map transactionResult = new HashMap();
        List transactionlist = new ArrayList();
        try{
            String requestUrl = "";
            MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, MROldSetting.IS_NEXI_TEST);
            boolean isNexiTest = Boolean.valueOf(setting.getValue());
            if(isNexiTest){
                requestUrl = "https://coll-servizi.cartasi.it/PerseoService/rest/PerseoWS/transazioni";
            } else {
                requestUrl = "https://servizi.cartasi.it/PerseoService/rest/PerseoWS/transazioni";
            }
            String idCliente = deviceConfiguration.getIdCliente();
            String idRichiesta = "1";
            String codEseSia = deviceConfiguration.getCodEseSia();
            String codiceAbi = deviceConfiguration.getCodiceAbi();
            String operazione_richiesta = "NOTIFICA";
            String timeStamp = "" + System.currentTimeMillis();
            String idTerminale = deviceConfiguration.getPhysicalIDPOS();
            String chiaveSegreta = deviceConfiguration.getFetchSecretKey(); // this should be the right one
            String stringaMac = "idRichiesta=" + idRichiesta + "timeStamp=" + timeStamp
                    + "idCliente=" + idCliente + "codEseSia=" + codEseSia + "codiceAbi=" + codiceAbi + "idTerminale=" + deviceConfiguration.getPhysicalIDPOS() + "daData=" + startDate + "aData=" + endDate + chiaveSegreta;
            System.out.println(stringaMac);
            String macCalculated = hashMacFetchTransaction(stringaMac);
            JSONObject json = new JSONObject();
            json.put("idCliente", idCliente);
            json.put("idRichiesta", idRichiesta);
            json.put("timeStamp", timeStamp);
            json.put("mac", macCalculated);
            json.put("codEseSia", codEseSia);
            json.put("codiceAbi", codiceAbi);
            json.put("idTerminale", idTerminale);
            json.put("daData", startDate);
            json.put("aData", endDate);
            json.put("operazione_richiesta", operazione_richiesta);

            String resultTemp = sendRequest(requestUrl, json);

            JSONObject jobject = new JSONObject(resultTemp);
            transactionResult.put("jobject", jobject);
            if (jobject.has("esito") && jobject.getString("esito").equalsIgnoreCase("ok")) {
                if (jobject.has("transazioni")) {
                    transactionlist.addAll(parseNexiListJson(jobject));
                    transactionResult.put("transList", transactionlist);
                    transactionResult.put("msg", "Transaction List :- " + jobject.getString("numMovimenti"));
                    transactionResult.put("color","Green");
                } else {
                    try {
                        transactionResult.put("msg", "Transaction List has :- " + jobject.getString("numMovimenti"));
                        transactionResult.put("color","Green");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        transactionResult.put("msg", "Transaction List: No movement available");
                        transactionResult.put("color","Red");
                    }
                }
            } else {
                if (jobject.has("errore") && jobject.getJSONObject("errore").has("messaggio")) {
                    transactionResult.put("msg", jobject.getJSONObject("errore").get("messaggio"));
                    transactionResult.put("color","Red");
                } else if (jobject.has("errore") && jobject.getJSONObject("errore").has("descr")) {
                    transactionResult.put("msg", jobject.getJSONObject("errore").get("descr"));
                    transactionResult.put("color","Red");
                }  else {
                    transactionResult.put("msg", "Payment Error");
                    transactionResult.put("color","Red");
                }
            }

        } catch (Exception ex){
            transactionResult.put("msg", bundle.getString("MRRentaAgreement.somethingwentwrong.label"));
            transactionResult.put("color","Red");
        }
        return transactionResult;

    }

    private static String hashMacFetchTransaction(String stringaMac) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] in = digest.digest(stringaMac.getBytes("UTF-8"));

        return bytesToHex(in);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static List parseNexiListJson(JSONObject jsonObject) {
        List<MROldNexiTransaction> list = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("transazioni");
            for (int i = 0; i < jsonArray.length(); i++) {
                MROldNexiTransaction nexiTransaction = new MROldNexiTransaction();
                JSONObject object = jsonArray.getJSONObject(i);
                nexiTransaction.setIdPrs(object.getString("idPrs"));
                nexiTransaction.setIdCol(object.getString("idCol"));
                nexiTransaction.setCodAbi(object.getString("codAbi"));
                nexiTransaction.setEsercenteSia(object.getString("esercenteSia"));
                nexiTransaction.setStabilimentoSia(object.getString("stabilimentoSia"));
                nexiTransaction.setNumeroCassa(object.getString("numeroCassa"));

                nexiTransaction.setIdTerminale(object.getString("idTerminale"));
                nexiTransaction.setNumeroTransazione(object.getString("numeroTransazione"));
                try {
                    nexiTransaction.setDataOra(new Date(object.getLong("dataOra")).toString());
                } catch (Exception ex) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        Date date = sdf.parse(object.getString("dataOra"));
                        String dateStr = date.toString();
                        nexiTransaction.setDataOra(dateStr);
                    } catch (Exception exx) {
                        exx.printStackTrace();
                    }
                }
                try {
                    nexiTransaction.setDataTransazione(new Date(object.getLong("dataTransazione")).toString());
                } catch (Exception ex) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = sdf.parse(object.getString("dataTransazione"));
                        String dateStr = date.toString();
                        nexiTransaction.setDataTransazione(dateStr);
                    } catch (Exception exx) {
                        exx.printStackTrace();
                    }
                }
                try {
                    nexiTransaction.setDataValuta(new Date(object.getLong("dataValuta")).toString());
                } catch (Exception ex) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = sdf.parse(object.getString("dataValuta"));
                        String dateStr = date.toString();
                        nexiTransaction.setDataValuta(dateStr);
                    } catch (Exception exx) {
                        exx.printStackTrace();
                    }
                }
                nexiTransaction.setCodAutorizzazione(object.getString("codAutorizzazione"));
                nexiTransaction.setSegno(object.getString("segno"));
                nexiTransaction.setDivisa(object.getString("divisa"));
                nexiTransaction.setImporto(object.getDouble("importo"));
                nexiTransaction.setEsito(object.getString("esito"));
                nexiTransaction.setDescrizioneEsito(object.getString("descrizioneEsito"));
                nexiTransaction.setCodCompagnia(object.getString("codCompagnia"));
                nexiTransaction.setDesCompagnia(object.getString("desCompagnia"));
                nexiTransaction.setPanCarta(object.getString("panCarta"));
                nexiTransaction.setCodiceConvenzione(object.getString("codiceConvenzione"));
                nexiTransaction.setTipoOperazione(object.getString("tipoOperazione"));
                nexiTransaction.setAbiCarta(object.getString("abiCarta"));
                nexiTransaction.setContatorePos(object.getString("contatorePos"));
                nexiTransaction.setAutorizzazioneSelfService(object.getBoolean("autorizzazioneSelfService"));
                nexiTransaction.setTipoTransazione(object.getString("tipoTransazione"));
                nexiTransaction.setRrn(object.getString("rrn"));
                //nexiTransaction.setOraTransazione(new Date(object.getLong("oraTransazione")).toString());
                list.add(nexiTransaction);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String sendRequest(String requestUrl, JSONObject json){
        String resultTemp = "";
        try{
            StringBuffer serverUrl = new StringBuffer();
            serverUrl.append(requestUrl);
            URL toGo = new URL(serverUrl.toString());
            HttpURLConnection conn = (HttpURLConnection) toGo.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(json.toString());
            bw.close();
            System.out.print("request params:::"+json);
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                resultTemp += output;
            }
            System.out.print("response:::"+resultTemp);
            br.close();
            conn.disconnect();
        } catch (Exception ex) {

        }
        return resultTemp;

    }

    public static Map chargeNexiTxnCancel(Session sx, MROldNexiTransaction nexiTransaction, String chargeAmount, MROldDeviceConfiguration deviceConfiguration, User user, MROldContrattoNoleggio contrattoNoleggio){
        Map transactionResult = new HashMap();
        try{
            String requestUrl = "";
            MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, MROldSetting.IS_NEXI_TEST);
            boolean isNexiTest = Boolean.valueOf(setting.getValue());
            if(isNexiTest){
                requestUrl = "https://coll-servizi.cartasi.it/PerseoService/rest/PerseoWS/stornoPreauth";
            } else {
                requestUrl = "https://servizi.cartasi.it/PerseoService/rest/PerseoWS/stornoPreauth";
            }
            String idPrs = nexiTransaction.getIdPrs();
            String importo = chargeAmount;
            String idRichiesta = "1";
            String timeStamp = "" + System.currentTimeMillis();
            String idCliente = deviceConfiguration.getIdCliente();
            String chiaveSegreta = deviceConfiguration.getFetchSecretKey();
            String macString = "idRichiesta=" + idRichiesta + "timeStamp=" + timeStamp
                    + "idCliente=" + idCliente + "idPrs=" + idPrs + chiaveSegreta;
            System.out.println(macString);
            String macCalculatedForCharge = hashMacFetchTransaction(macString);

            JSONObject jsonCharge = new JSONObject();
            jsonCharge.put("idCliente", idCliente);
            jsonCharge.put("idRichiesta", idRichiesta);
            jsonCharge.put("timeStamp", timeStamp);
            jsonCharge.put("mac", macCalculatedForCharge);
            jsonCharge.put("idPrs", idPrs);
            jsonCharge.put("importo", importo);
            String resultStr = sendRequest(requestUrl, jsonCharge);
            JSONObject chargeJSON = new JSONObject(resultStr);
            if (chargeJSON.has("esito") && chargeJSON.getString("esito").equalsIgnoreCase("ok")) {
                sx.saveOrUpdate(nexiTransaction);
                transactionResult.put("msg", bundle.getString("MRRentaAgreement.succRef.label"));
                transactionResult.put("color","Green");
            } else {
                if (chargeJSON.has("errore") && chargeJSON.getJSONObject("errore").has("descr")) {
                    transactionResult.put("msg", chargeJSON.getJSONObject("errore").get("descr"));
                    transactionResult.put("color","Red");
                } else {
                    transactionResult.put("msg", bundle.getString("MRRentaAgreement.payErr.label"));
                    transactionResult.put("color","Red");
                }
            }
        } catch (Exception ex){
            transactionResult.put("msg", bundle.getString("MRRentaAgreement.somethingwentwrong.label"));
            transactionResult.put("color","Red");
        }
        return transactionResult;
    }

    public static Map chargeNexiTxn(Session sx, MROldNexiTransaction nexiTransaction, String chargeAmount, MROldDeviceConfiguration deviceConfiguration, User user, MROldContrattoNoleggio contrattoNoleggio, JSONObject jobject){
        Map transactionResult = new HashMap();
        try{
            String requestUrl = "";
            MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, MROldSetting.IS_NEXI_TEST);
            boolean isNexiTest = Boolean.valueOf(setting.getValue());
            if(isNexiTest){
                requestUrl = "https://coll-servizi.cartasi.it/PerseoService/rest/PerseoWS/notifica";
            } else {
                requestUrl = "https://servizi.cartasi.it/PerseoService/rest/PerseoWS/notifica";
            }
            String idPrs = nexiTransaction.getIdPrs();
            String importo = chargeAmount;
            String idRichiesta = "1";
            String timeStamp = "" + System.currentTimeMillis();
            String idCliente = deviceConfiguration.getIdCliente();
            String chiaveSegreta = deviceConfiguration.getFetchSecretKey();
            String macString = "idRichiesta=" + idRichiesta + "timeStamp=" + timeStamp
                    + "idCliente=" + idCliente + "idPrs=" + idPrs + chiaveSegreta;
            System.out.println(macString);
            String macCalculatedForCharge = hashMacFetchTransaction(macString);

            JSONObject jsonCharge = new JSONObject();
            jsonCharge.put("idCliente", idCliente);
            jsonCharge.put("idRichiesta", idRichiesta);
            jsonCharge.put("timeStamp", timeStamp);
            jsonCharge.put("mac", macCalculatedForCharge);
            jsonCharge.put("idPrs", idPrs);
            jsonCharge.put("importo", importo);
            String resultStr = sendRequest(requestUrl, jsonCharge);
            JSONObject chargeJSON = new JSONObject(resultStr);
            if (chargeJSON.has("esito") && chargeJSON.getString("esito").equalsIgnoreCase("ok")) {
                sx.saveOrUpdate(nexiTransaction);
                transactionResult.put("msg", bundle.getString("MRRentaAgreement.succChrg.label"));
                transactionResult.put("color","Green");
                cashTransaction(sx, chargeAmount, user, contrattoNoleggio, deviceConfiguration,true);
                MROldPaymentReceipt scontrino = creaPaymentReceipt(sx, contrattoNoleggio, jsonCharge, jobject, MROldPaymentReceipt.TYPE_CHARGE_FROM_AUTH);
                transactionResult.put("payRecId",scontrino.getId());
                transactionResult.put("msg", bundle.getString("MRRentaAgreement.succChrg.cashBookCreated.label"));
                transactionResult.put("color","Green");
            } else {
                if (chargeJSON.has("errore") && chargeJSON.getJSONObject("errore").has("descr")) {
                    transactionResult.put("msg", chargeJSON.getJSONObject("errore").get("descr"));
                    transactionResult.put("color","Red");
                } else {
                    transactionResult.put("msg", bundle.getString("MRRentaAgreement.payErr.label"));
                    transactionResult.put("color","Red");
                }
            }
        } catch (Exception ex){
            transactionResult.put("msg", bundle.getString("MRRentaAgreement.somethingwentwrong.label"));
            transactionResult.put("color","Red");
        }
        return transactionResult;
    }

    public static MROldPaymentReceipt creaPaymentReceipt (Session sx, MROldContrattoNoleggio contratto, JSONObject request, JSONObject response, Integer type) {

        MROldPaymentReceipt scontrino = new MROldPaymentReceipt();
        try {
            //DATI RECUPERABILI ALLO STESSO MODO SIA IN TYPE_CHARGE CHE IN TYPE_CHARGE_FROM_AUTH (REFUND NON ANCORA IMPLEMENTATO)
            if (contratto != null) {
                scontrino.setContratto(contratto);
            }
            scontrino.setType(type);

            if (Objects.equals(type, MROldPaymentReceipt.TYPE_CHARGE)) { //DATI PRESENTI SOLO IN TYPE_CHARGE
                if (request.has("numeroContratto") && request.get("numeroContratto").toString() != null) {
                    scontrino.setToken(request.get("numeroContratto").toString());
                }
                if (response.has("brand") && response.get("brand").toString() != null) {
                    scontrino.setCardType(response.get("brand").toString());
                }
                if (request.has("codiceTransazione") && request.get("codiceTransazione").toString() != null) {
                    scontrino.setTransactionNumber(request.get("codiceTransazione").toString());
                }
                if (request.has("scadenza") && request.get("scadenza").toString() != null) {
                    scontrino.setCardExpiration(request.get("scadenza").toString());
                }
                if (request.has("importo") && request.get("importo").toString() != null) {
                    String importoStr = request.get("importo").toString();
                    Double importo = Double.parseDouble(importoStr);
                    importo = importo/100;
                    scontrino.setAmount(importo);
                }
                if (response.has("idOperazione") && response.get("idOperazione").toString() != null) {
                    scontrino.setOperationId(response.get("idOperazione").toString());
                }
                if (response.has("codiceAutorizzazione") && response.get("codiceAutorizzazione").toString() != null) {
                    scontrino.setAuthCode(response.get("codiceAutorizzazione").toString());
                }
                if (response.has("data") && response.has("ora") && response.get("data").toString() != null && response.get("ora").toString() != null) {
                    String timestampStr = response.get("data").toString() + "-" + response.get("ora").toString();
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
                    Date timestamp = df.parse(timestampStr);
                    scontrino.setTimestamp(timestamp);
                }
                if (response.has("esito") && response.get("esito").toString() != null) {
                    scontrino.setResult(response.get("esito").toString());
                }
            } else if (Objects.equals(type, MROldPaymentReceipt.TYPE_CHARGE_FROM_AUTH)) { //DATI PRESENTI SOLO IN TYPE_CHARGE_FROM_AUTH
                if (response.has("esito") && response.get("esito").toString() != null) {
                    scontrino.setResult(response.get("esito").toString());
                }
                if (request.has("importo") && request.get("importo").toString() != null) {
                    String importoStr = request.get("importo").toString();
                    Double importo = Double.parseDouble(importoStr);
                    scontrino.setAmount(importo);
                }
                if (request.has("timeStamp") && request.get("timeStamp").toString() != null) {
                    long timestampLong = Long.parseLong(request.get("timeStamp").toString());
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(timestampLong);
                    Date timestampDate = cal.getTime();
                    scontrino.setTimestamp(timestampDate);
                }
                String idPrs = "";
                if (request.has("idPrs") && request.get("idPrs").toString() != null) {
                    idPrs = request.get("idPrs").toString();
                    scontrino.setPrsId(idPrs);
                }
                if (idPrs != null && !idPrs.isEmpty()) {
                    if (response.has("transazioni") && response.get("transazioni").toString() != null) {
                        JSONArray transazioni = (JSONArray) response.get("transazioni");
                        for (int i = 0; i < transazioni.length(); i++) {
                            JSONObject transazione = (JSONObject) transazioni.get(i);
                            if (transazione.has("idPrs") && transazione.get("idPrs").toString().equals(idPrs)) {
                                if (transazione.has("codAutorizzazione") && transazione.get("codAutorizzazione").toString() != null) {
                                    scontrino.setAuthCode(transazione.get("codAutorizzazione").toString());
                                }
                                if (transazione.has("numeroTransazione") && transazione.get("numeroTransazione").toString() != null) {
                                    scontrino.setTransactionNumber(transazione.get("numeroTransazione").toString());
                                }
                                if (transazione.has("panCarta") && transazione.get("panCarta").toString() != null) {
                                    scontrino.setCardPan(transazione.get("panCarta").toString());
                                }
                                if (transazione.has("idTerminale") && transazione.get("idTerminale").toString() != null) {
                                    scontrino.setTerminalId(transazione.get("idTerminale").toString());
                                }
                            }
                        }
                    }
                }

            } else if (Objects.equals(type, MROldPaymentReceipt.TYPE_REFUND)) { //DATI PRESENTI SOLO IN TYPE_REFUND (NON ANCORA IMPLEMENTATO)


            }

            sx.saveOrUpdate(scontrino);



        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return scontrino;

    }
}
