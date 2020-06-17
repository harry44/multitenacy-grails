package it.myrent.ee.ingenico;

import java.text.SimpleDateFormat;
import java.util.*;

import it.aessepi.utils.BundleUtils;
import it.myrent.ee.api.factory.PrimanotaFactory;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.api.utils.NumerazioniUtils;
import it.myrent.ee.db.*;
import it.aessepi.utils.beans.FormattedDate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import it.myrent.ee.db.MROldNetsAnagrafica;
import it.myrent.ee.db.MROldNetsTransazione;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import it.myrent.ee.api.utils.MathUtils;
import org.hibernate.StaleObjectStateException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * Created by Shivangani on 4/3/2018.
 */
public class IngenicoUtils {

    public final static String TEST = "https://ogone.test.v-psp.com/Tokenization/HostedPage";
    public final static String PRODUCTION = "https://secure.ogone.com/Tokenization/HostedPage";
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    private static Map registerIngenicoVPOS(String panHash, Session sx, String url, Boolean localhost, String merchId, String userId) {
        String firstUri = "";
        Map result = new HashMap();
        if (panHash == null || panHash.equals("") ) {
            MROldNetsAnagrafica xpay = null;
            Query query = sx.createQuery(""
                    + "SELECT x "
                    + "FROM MROldNetsAnagrafica x "
                    + "WHERE inUse = true and "
                    + "x.merchantId = :merchId and x.userId = :userId")
                    .setParameter("merchId", merchId)
                    .setParameter("userId", userId)
                    .setLockMode("x", LockMode.READ);

            xpay = (MROldNetsAnagrafica) query.uniqueResult();
            String psid = xpay.getMerchantId();
            String acceptUrl = url + "/MRRentalAgreement/UrlDone";
            String exceptionUrl = url + "/MRRentalAgreement/UrlBack";
            String shaIn = xpay.getShaIn();
            if (localhost) {
                firstUri = "https://ogone.test.v-psp.com/Tokenization/HostedPage";
            } else {
                firstUri = "https://secure.ogone.com/Tokenization/HostedPage";
            }
            String storePerm = "N";
            String shaSign = "";

            String strToHash = "ACCOUNT.PSPID=" + psid + shaIn + "ALIAS.STOREPERMANENTLY=Y"+shaIn+"CARD.PAYMENTMETHOD=CREDITCARD" + shaIn + "PARAMETERS.ACCEPTURL=" + acceptUrl + shaIn + "PARAMETERS.EXCEPTIONURL=" + exceptionUrl + shaIn;
            shaSign = generateSHASign(strToHash);

            result.put("firstUri", firstUri);
            result.put("psid", psid);
            result.put("acceptUrl", acceptUrl);
            result.put("exceptionUrl", exceptionUrl);
            result.put("payMthd", "CREDITCARD");
            result.put("shaSign", shaSign);
            result.put("transMsg", "Register OK");
            result.put("transMsgColor", "Green");
            //result.put("shaSign",shaSign);
        }
        return result;
    }

    public static String generateSHASign(String stringToHash) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(stringToHash.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static Map processDirectRequest(String alias, Double amount, String currency, String orderId, String merchantId, String userId, Session sx, Boolean localhost, MROldNetsTransazione selectedNetsTransazione, User aUser, String contractId, String paymentId) {

        String response = "";
        Map result = new HashMap();
        MROldNetsAnagrafica xpay = null;
        Query query = sx.createQuery(""
                + "SELECT x "
                + "FROM MROldNetsAnagrafica x "
                + "WHERE inUse = true and "
                + "x.merchantId = :merchId and x.userId = :userId")
                .setParameter("merchId", merchantId)
                .setParameter("userId", userId)
                .setLockMode("x", LockMode.READ);

        xpay = (MROldNetsAnagrafica) query.uniqueResult();
        String pwd = xpay.getToken();
        String shaIn = xpay.getShaIn();
        //Double newAmt = new Double( Math.round(amount));
        Double newAmt = new Double(MathUtils.round(amount, 2));
        Integer aa = (int) (double) (newAmt * 10 * 10);
        MROldContrattoNoleggio contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, Integer.parseInt(contractId));
        MROldPagamento pagamento = (MROldPagamento) sx.get(MROldPagamento.class, Integer.parseInt(paymentId));
        String strToHash = "";
        if (orderId.startsWith("DG")) {
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                strToHash = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn + "ECI=9" + shaIn + "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
            } else {
                strToHash = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn + "ECI=9" + shaIn + "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;
            }
//            strToHash  = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn +"ECI=9"+ shaIn+ "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFKIND=MGID"+shaIn+"USERID=" + userId + shaIn;
        } else {
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                strToHash = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn + "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
            } else {
                strToHash = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn + "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;
            }

        }
        String strHashed = generateSHASign(strToHash);
        try {
            String strUrl = "";
            if (localhost) {
                strUrl = "https://secure.ogone.com/Ncol/Test/orderdirect.asp";
            } else {
                strUrl = "https://secure.ogone.com/ncol/prod/orderdirect.asp";
            }
            URL url = new URL(strUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            BufferedWriter httpRequestBodyWriter
                    = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            if (orderId.startsWith("DG")) {
                if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                    httpRequestBodyWriter.write("ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&ECI=9" + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&ECI=9" + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                } else {
                    httpRequestBodyWriter.write("ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&ECI=9" + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&ECI=9" + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                }
            } else {
                if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                    httpRequestBodyWriter.write("ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                } else {
                    httpRequestBodyWriter.write("ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                }
            }
            httpRequestBodyWriter.close();

            Scanner httpResponseScanner = new Scanner(conn.getInputStream());
            while (httpResponseScanner.hasNextLine()) {
                response = response + httpResponseScanner.nextLine();
            }
            System.out.print(response);
            httpResponseScanner.close();
            if (conn.getResponseCode() != 200) {
                result.put("transMsgColor", "Red");
            } else {
                String statusCode = "";
                if (response.contains("\"STATUS")) {
                    String temp = response.split("\"STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                } else if (response.contains("\" STATUS")) {
                    String temp = response.split("\" STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                }
                if (statusCode.equals("9") || statusCode.equals("91")) {
                    Calendar cal = Calendar.getInstance();
                    result.put("statusCode", statusCode);
                    if (statusCode.equals("9")) {
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_SALE);
                        result.put("transMsgColor", "Green");
                        result.put("checkBtn", "N");
                        result.put("saleBtn", "N");
                        result.put("authBtn", "N");
                        result.put("captureBtn", "N");
                        result.put("cancelBtn", "N");
                        result.put("creditBtn", "Y");
                        result.put("isAuthAmt", "N");
                        result.put("authAmt1", "0.0");
                        result.put("isCaptAmt", "N");
                        result.put("captAmt1", amount);
                        result.put("captAmt", amount);
                        result.put("isCreditAmt", "Y");
                        result.put("creditAmt1", amount);
                        result.put("checkBtnCnf", "N");
                        selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                        sx.update(selectedNetsTransazione);
                        sx.flush();
                        storeSaleResponse(response, sx, selectedNetsTransazione);

                        try{
                            MROldPrimanota primanotaIncasso = PrimanotaFactory.creaPrimanotaIncassoCliente(
                                    sx,
                                    aUser.getSedeOperativa(), //sede
                                    contrattoNoleggio,
                                    pagamento,
                                    null, //garanzia
                                    newAmt,
                                    aUser);

                            MROldNumerazione numerazionePrimenote = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, aUser);
                            Integer numeroRegistrazionePrimenote = NumerazioniUtils.aggiornaProgressivo(sx, numerazionePrimenote, FormattedDate.annoCorrente(), 1);
                            primanotaIncasso.setNumerazione(numerazionePrimenote);
                            primanotaIncasso.setNumeroRegistrazione(numeroRegistrazionePrimenote);
                            sx.saveOrUpdate(primanotaIncasso);
                        } catch (StaleObjectStateException staleEx) {
                            result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                            System.out.println("##########The transaction was successful but was unable to create account entry");
                        } catch (Exception ex){
                            result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                            System.out.println("##########The transaction was successful but was unable to create account entry");
                        } catch (Throwable ex) {
                            result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                            System.out.println("##########The transaction was successful but was unable to create account entry");
                        }
                    } else {
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_SALE_WAIT);
                        result.put("transMsgColor", "Green");
                        result.put("checkBtn", "N");
                        result.put("saleBtn", "N");
                        result.put("authBtn", "N");
                        result.put("captureBtn", "N");
                        result.put("cancelBtn", "N");
                        result.put("creditBtn", "N");
                        result.put("isAuthAmt", "N");
                        result.put("authAmt1", "0.0");
                        result.put("isCaptAmt", "N");
                        result.put("captAmt1", amount);
                        result.put("captAmt", amount);
                        result.put("isCreditAmt", "N");
                        result.put("creditAmt1", amount);
                        result.put("checkBtnCnf", "Y");
                        selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                        sx.update(selectedNetsTransazione);
                        sx.flush();
                        storeSaleResponse(response, sx, selectedNetsTransazione);
                    }
                } else {
                    result.put("transMsgColor", "Red");
                }
            }
        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
        String statustr = getStatus(response);
        result.put("transMsg", statustr);
        return result;
    }

    public static void storeSaleResponse(String response, Session sx, MROldNetsTransazione selectedNetsTransazione) {
        Map result = new HashMap();
        if (response != "" && response != null) {
            //String statusCode =
            String statusCode = "";
            String NCERROR = "";
            String nccErrorPlus = "";
            String acceptance = "";
            String payId = "";
            String payIdSub = "";
            if (response.contains("\"STATUS")) {
                String temp = response.split("\"STATUS=\"")[1];
                statusCode = temp.split("\"")[0];
            } else if (response.contains("\" STATUS")) {
                String temp = response.split("\" STATUS=\"")[1];
                statusCode = temp.split("\"")[0];
            }
            //if(actProces=="SALE"){
            Double amount = 0.0;
            if (response.contains("amount")) {
                String temp = response.split("amount=\"")[1];
                temp = temp.split("\"")[0];
                if (temp != "") {
                    amount = Double.parseDouble(temp);
                }
            }
            if (response.contains("NCERROR")) {
                String temp = response.split("NCERROR=\"")[1];
                NCERROR = temp.split("\"")[0];
            }
            if (response.contains("NCERRORPLUS")) {
                String temp = response.split("NCERRORPLUS=\"")[1];
                nccErrorPlus = temp.split("\"")[0];
            }
            if (response.contains("ACCEPTANCE")) {
                String temp = response.split("ACCEPTANCE=\"")[1];
                acceptance = temp.split("\"")[0];
            }
            if (response.contains("PAYID")) {
                String temp = response.split("PAYID=\"")[1];
                payId = temp.split("\"")[0];
            }
            if (response.contains("PAYIDSUB")) {
                String temp = response.split("PAYIDSUB=\"")[1];
                payIdSub = temp.split("\"")[0];
            }
            Calendar cal = Calendar.getInstance();
            MROldNetsOperationCall operationCall = new MROldNetsOperationCall();
            if (statusCode.equals("9")) {
                operationCall.setType(MROldNetsOperationCall.SALE_CALL);
            } else {
                operationCall.setType(MROldNetsOperationCall.SALE_CALL_WAIT);
            }
            operationCall.setAmount(amount);
            operationCall.setDate(cal.getTime());
            operationCall.setStatus(statusCode);
            operationCall.setNccError(NCERROR);
            operationCall.setNccErrorPlus(nccErrorPlus);
            operationCall.setAcceptance(acceptance);
            operationCall.setPayId(payId);
            operationCall.setTransazioneNets(selectedNetsTransazione);
            operationCall.setPayIdSub(payIdSub);
            sx.saveOrUpdate(operationCall);
            sx.flush();
            //}
        }
    }

    public static Map processAuthRequest(String alias, Double amount, String currency, String orderId, String merchantId, String userId, Session sx, Boolean localhost, MROldNetsTransazione selectedNetsTransazione) {

        String response = "";
        Map result = new HashMap();
        MROldNetsAnagrafica xpay = null;
        Query query = sx.createQuery(""
                + "SELECT x "
                + "FROM MROldNetsAnagrafica x "
                + "WHERE inUse = true and "
                + "x.merchantId = :merchId and x.userId = :userId")
                .setParameter("merchId", merchantId)
                .setParameter("userId", userId)
                .setLockMode("x", LockMode.READ);

        xpay = (MROldNetsAnagrafica) query.uniqueResult();
        String pwd = xpay.getToken();
        String shaIn = xpay.getShaIn();
        Double newAmt = new Double(MathUtils.round(amount, 2));
        Integer aa = (int) (double) (newAmt * 10 * 10);
        String strToHash = "";
        if (orderId.startsWith("DG")) {
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                strToHash = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn + "ECI=9" + shaIn + "OPERATION=RES" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
            } else {
                strToHash = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn + "ECI=9" + shaIn + "OPERATION=RES" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;
            }
        } else {
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                strToHash = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn + "OPERATION=RES" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
            } else {
                strToHash = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn + "OPERATION=RES" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;
            }
        }
        String strHashed = generateSHASign(strToHash);
        String statusCode = "";
        try {
            String strUrl = "";
            if (localhost) {
                strUrl = "https://secure.ogone.com/Ncol/Test/orderdirect.asp";
            } else {
                strUrl = "https://secure.ogone.com/ncol/prod/orderdirect.asp";
            }
            URL url = new URL(strUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            BufferedWriter httpRequestBodyWriter
                    = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            if (orderId.startsWith("DG")) {
                if (xpay.getRefId().equals("")|| xpay.getRefId() == null) {
                    httpRequestBodyWriter.write("ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&ECI=9" + "&OPERATION=RES&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request::::" +"ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&ECI=9" + "&OPERATION=RES&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                } else {
                    httpRequestBodyWriter.write("ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&ECI=9" + "&OPERATION=RES&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request::::" +"ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&ECI=9" + "&OPERATION=RES&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                }
            } else {
                if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                    httpRequestBodyWriter.write("ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&OPERATION=RES&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request::::"+"ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&OPERATION=RES&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                } else {
                    httpRequestBodyWriter.write("ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&OPERATION=RES&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request::::"+"ALIAS=" + alias + "&AMOUNT=" + aa + "&CURRENCY=" + currency + "&OPERATION=RES&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                }

            }
            httpRequestBodyWriter.close();

            Scanner httpResponseScanner = new Scanner(conn.getInputStream());
            while (httpResponseScanner.hasNextLine()) {
                response = response + httpResponseScanner.nextLine();
            }
            System.out.print(response);
            httpResponseScanner.close();
            if (conn.getResponseCode() != 200) {
                result.put("transMsgColor", "Red");
            } else {
                if (response.contains("\"STATUS")) {
                    String temp = response.split("\"STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                } else if (response.contains("\" STATUS")) {
                    String temp = response.split("\" STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                }
                if (statusCode.equals("5") || statusCode.equals("51")) {
                    Calendar cal = Calendar.getInstance();
                    if (statusCode.equals("5")) {
                        result.put("transMsgColor", "Green");
                        result.put("captureBtn", "Y");
                        result.put("cancelBtn", "Y");
                        result.put("saleBtn", "N");
                        result.put("authBtn", "N");

                        result.put("isCaptAmt", "Y");
                        result.put("captAmt1", amount);

                        result.put("isAuthAmt", "N");
                        result.put("authAmt", amount);
                        result.put("checkBtnCnf", "N");
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_AUTHORIZED);
                    } else {
                        result.put("transMsgColor", "Green");
                        result.put("captureBtn", "N");
                        result.put("cancelBtn", "N");
                        result.put("saleBtn", "N");
                        result.put("authBtn", "N");

                        result.put("isCaptAmt", "N");
                        result.put("captAmt1", amount);

                        result.put("isAuthAmt", "N");
                        result.put("authAmt", amount);
                        result.put("checkBtnCnf", "Y");
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_AUTHORIZED_WAIT);

                    }

                    selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                    selectedNetsTransazione.setAmountAuth(amount);
                    sx.update(selectedNetsTransazione);
                    sx.flush();
//                    if (!statusCode.equals("5")) {
                        storeWaitRequest(selectedNetsTransazione.getState(), response, sx, selectedNetsTransazione);
//                    }
                } else {
                    result.put("transMsgColor", "Red");
                }
            }
        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
        String statustr = getStatus(response);

        result.put("transMsg", statustr);
        return result;
    }

    public static Map processCaptRequest(String alias, Double amount, String currency, String orderId, String merchantId, String userId, Session sx, Boolean localhost, MROldNetsTransazione selectedNetsTransazione, User aUser, String contractId, String paymentId, Double creditAmt, Double capturedAmt, Double authdAmt) {

        String response = "";
        Map result = new HashMap();
        MROldNetsAnagrafica xpay = null;
        Query query = sx.createQuery(""
                + "SELECT x "
                + "FROM MROldNetsAnagrafica x "
                + "WHERE inUse = true and "
                + "x.merchantId = :merchId and x.userId = :userId")
                .setParameter("merchId", merchantId)
                .setParameter("userId", userId)
                .setLockMode("x", LockMode.READ);

        xpay = (MROldNetsAnagrafica) query.uniqueResult();

        String pwd = xpay.getToken();
        String shaIn = xpay.getShaIn();
        //Double newAmt = new Double( Math.round(amount));
        Double newAmt = new Double(MathUtils.round(amount, 2));
        Integer aa = (int) (double) (newAmt * 10 * 10);
        MROldContrattoNoleggio contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, Integer.parseInt(contractId));
        MROldPagamento pagamento = (MROldPagamento) sx.get(MROldPagamento.class, Integer.parseInt(paymentId));
        //String strToHash = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn + "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + shaIn+ "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
        String strToHash = "";
//        Query query1 = sx.createQuery(""
//                + "SELECT x "
//                + "FROM MROldNetsOperationCall x "
//                + "WHERE x.transazioneNets = :transazioneNets ")
//                .setParameter("transazioneNets", selectedNetsTransazione);
//
//        MROldNetsOperationCall xpay1 = (MROldNetsOperationCall) query1.uniqueResult();
        if (orderId.startsWith("DG")) {
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                strToHash = "AMOUNT=" + aa + shaIn + "ECI=9" + shaIn + "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
            } else {
                strToHash = "AMOUNT=" + aa + shaIn + "ECI=9" + shaIn + "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;
            }
        } else {
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                strToHash = "AMOUNT=" + aa + shaIn + "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;

            } else {
                strToHash = "AMOUNT=" + aa + shaIn + "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;

            }
        }
        String strHashed = generateSHASign(strToHash);
        try {
            String strUrl = "";
            if (localhost) {
                strUrl = "https://ogone.test.v-psp.com/ncol/test/maintenancedirect.asp";
            } else {
                strUrl = "https://secure.ogone.com/ncol/prod/maintenancedirect.asp";
            }
            URL url = new URL(strUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            BufferedWriter httpRequestBodyWriter
                    = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            if (orderId.startsWith("DG")) {
                if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                    httpRequestBodyWriter.write("AMOUNT=" + aa + "&ECI=9" + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"AMOUNT=" + aa + "&ECI=9" + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                } else {
                    httpRequestBodyWriter.write("AMOUNT=" + aa + "&ECI=9" + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"AMOUNT=" + aa + "&ECI=9" + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                }
            } else {
                if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                    httpRequestBodyWriter.write("AMOUNT=" + aa + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"AMOUNT=" + aa + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                } else {
                    httpRequestBodyWriter.write("AMOUNT=" + aa + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"AMOUNT=" + aa + "&OPERATION=SAL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                }
            }
            httpRequestBodyWriter.close();

            Scanner httpResponseScanner = new Scanner(conn.getInputStream());
            while (httpResponseScanner.hasNextLine()) {
                response = response + httpResponseScanner.nextLine();
            }
            System.out.print(response);
            httpResponseScanner.close();
            if (conn.getResponseCode() != 200) {
                result.put("transMsgColor", "Red");
            } else {
                String statusCode = "";
                if (response.contains("\"STATUS")) {
                    String temp = response.split("\"STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                } else if (response.contains("\" STATUS")) {
                    String temp = response.split("\" STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                }
                if (statusCode.equals("9") || statusCode.equals("91")) {
                    Calendar cal = Calendar.getInstance();
                    result.put("statusCode", statusCode);
                    if (statusCode.equals("9")) {
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_CAPTURE);
                        if (authdAmt - (amount + capturedAmt) == 0) {
                            result.put("isCaptAmt", "N");
                            result.put("captureBtn", "N");
//                            selectedNetsTransazione.setState(MROldNetsTransazione.STATE_SALE);
                        }
                        result.put("transMsg", "CAPTURE OK");
                        result.put("transMsgColor", "Green");
                        result.put("cancelBtn", "N");
                        result.put("creditBtn", "Y");
                        result.put("isCreditAmt", "Y");
                        result.put("creditAmt1", amount + creditAmt);
                        result.put("captAmt", amount + capturedAmt);
                        result.put("captAmt1", authdAmt - (amount + capturedAmt));
                        result.put("checkBtnCnf", "N");
                        selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                        sx.update(selectedNetsTransazione);
                        sx.flush();
                        storeCaptResponse(response, sx, selectedNetsTransazione);
                        try{
                            MROldPrimanota primanotaIncasso = PrimanotaFactory.creaPrimanotaIncassoCliente(
                                    sx,
                                    aUser.getSedeOperativa(), //sede
                                    contrattoNoleggio,
                                    pagamento,
                                    null, //garanzia
                                    newAmt,
                                    aUser);
                            MROldNumerazione numerazionePrimenote = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, aUser);
                            Integer numeroRegistrazionePrimenote = NumerazioniUtils.aggiornaProgressivo(sx, numerazionePrimenote, FormattedDate.annoCorrente(), 1);
                            primanotaIncasso.setNumerazione(numerazionePrimenote);
                            primanotaIncasso.setNumeroRegistrazione(numeroRegistrazionePrimenote);
                            sx.saveOrUpdate(primanotaIncasso);
                        } catch (StaleObjectStateException staleEx) {
                            result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                            System.out.println("##########The transaction was successful but was unable to create account entry");
                        } catch (Exception ex){
                            result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                            System.out.println("##########The transaction was successful but was unable to create account entry");
                        } catch (Throwable ex) {
                            result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                            System.out.println("##########The transaction was successful but was unable to create account entry");
                        }
                    } else {
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_CAPTURE_WAIT);
                        if (authdAmt - (amount + capturedAmt) == 0) {
                            result.put("isCaptAmt", "N");
                            result.put("captureBtn", "N");
//                            selectedNetsTransazione.setState(MROldNetsTransazione.STATE_SALE_WAIT);
                        }
                        result.put("transMsg", "CAPTURE OK");
                        result.put("transMsgColor", "Green");
                        result.put("cancelBtn", "N");
                        result.put("creditBtn", "N");
                        result.put("isCreditAmt", "N");
                        result.put("creditAmt1", amount + creditAmt);
                        result.put("captAmt", amount + capturedAmt);
                        result.put("captAmt1", authdAmt - (amount + capturedAmt));
                        result.put("checkBtnCnf", "Y");
                        selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                        sx.update(selectedNetsTransazione);
                        sx.flush();
                        storeCaptResponse(response, sx, selectedNetsTransazione);
                    }


                } else {
                    result.put("transMsgColor", "Red");
                }
            }
        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
        String statustr = getStatus(response);
        result.put("transMsg", statustr);
        return result;
    }

    public static void storeCaptResponse(String response, Session sx, MROldNetsTransazione selectedNetsTransazione) {
        Map result = new HashMap();
        if (response != "" && response != null) {
            //String statusCode =
            String statusCode = "";
            String NCERROR = "";
            String nccErrorPlus = "";
            String acceptance = "";
            String payId = "";
            if (response.contains("\"STATUS")) {
                String temp = response.split("\"STATUS=\"")[1];
                statusCode = temp.split("\"")[0];
            }
            //if(actProces=="SALE"){
            Double amount = 0.0;
            if (response.contains("amount")) {
                String temp = response.split("amount=\"")[1];
                temp = temp.split("\"")[0];
                if (temp != "") {
                    amount = Double.parseDouble(temp);
                }
            }
            if (response.contains("NCERROR")) {
                String temp = response.split("NCERROR=\"")[1];
                NCERROR = temp.split("\"")[0];
            }
            if (response.contains("NCERRORPLUS")) {
                String temp = response.split("NCERRORPLUS=\"")[1];
                nccErrorPlus = temp.split("\"")[0];
            }
            if (response.contains("ACCEPTANCE")) {
                String temp = response.split("ACCEPTANCE=\"")[1];
                acceptance = temp.split("\"")[0];
            }
            if (response.contains("PAYID")) {
                String temp = response.split("PAYID=\"")[1];
                payId = temp.split("\"")[0];
            }
            String payIdSub = "";
            if (response.contains("PAYIDSUB")) {
                String temp = response.split("PAYIDSUB=\"")[1];
                payIdSub = temp.split("\"")[0];
            }
            Calendar cal = Calendar.getInstance();
            MROldNetsOperationCall operationCall = new MROldNetsOperationCall();
            if (statusCode.equals("9")) {
                operationCall.setType(MROldNetsOperationCall.CAPTURE_CALL);
            } else {
                operationCall.setType(MROldNetsOperationCall.CAPTURE_CALL_WAIT);
            }
            operationCall.setAmount(amount);
            operationCall.setDate(cal.getTime());
            operationCall.setStatus(statusCode);
            operationCall.setNccError(NCERROR);
            operationCall.setNccErrorPlus(nccErrorPlus);
            operationCall.setAcceptance(acceptance);
            operationCall.setPayId(payId);
            operationCall.setPayIdSub(payIdSub);
            operationCall.setTransazioneNets(selectedNetsTransazione);
            sx.saveOrUpdate(operationCall);
            sx.flush();
            //}
        }
    }

    public static Map processCreditRequest(Double amount, String orderId, String merchantId, String userId, Session sx, Boolean localhost, MROldNetsTransazione selectedNetsTransazione, User aUser, String contractId, String paymentId, Double captdAmount, Double creditedAmt, Double creditLimit) {

        String response = "";
        Map result = new HashMap();
        MROldNetsAnagrafica xpay = null;
        Query query = sx.createQuery(""
                + "SELECT x "
                + "FROM MROldNetsAnagrafica x "
                + "WHERE inUse = true and "
                + "x.merchantId = :merchId and x.userId = :userId")
                .setParameter("merchId", merchantId)
                .setParameter("userId", userId)
                .setLockMode("x", LockMode.READ);

        xpay = (MROldNetsAnagrafica) query.uniqueResult();


        query = sx.createQuery(""
                + "SELECT x "
                + "FROM MROldNetsOperationCall x "
                + "WHERE x.status = :status and "
                + "x.transazioneNets.id = :idTrans")
//                .setParameter("type", getState(selectedNetsTransazione))
                .setParameter("status", "9")
                .setParameter("idTrans", selectedNetsTransazione.getId())
                .setLockMode("x", LockMode.READ);

        MROldNetsOperationCall opCall = (MROldNetsOperationCall) query.list().get(0);
        String pwd = xpay.getToken();
        String shaIn = xpay.getShaIn();
        //Double newAmt = new Double( Math.round(amount));
        Double newAmt = new Double(MathUtils.round(amount, 2));
        Integer aa = (int) (double) (newAmt * 10 * 10);
        MROldContrattoNoleggio contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, Integer.parseInt(contractId));
        MROldPagamento pagamento = (MROldPagamento) sx.get(MROldPagamento.class, Integer.parseInt(paymentId));
        //String strToHash = "ALIAS=" + alias + shaIn + "AMOUNT=" + aa + shaIn + "CURRENCY=" + currency + shaIn + "OPERATION=SAL" + shaIn + "ORDERID=" + orderId + shaIn + shaIn+ "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
        String strToHash = "";
        if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
            strToHash = "AMOUNT=" + aa + shaIn + "OPERATION=RFD" + shaIn + "ORDERID=" + orderId + shaIn + "PAYID=" + opCall.getPayId() + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
        } else {
            strToHash = "AMOUNT=" + aa + shaIn + "OPERATION=RFD" + shaIn + "ORDERID=" + orderId + shaIn + "PAYID=" + opCall.getPayId() + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;
        }
        String strHashed = generateSHASign(strToHash);
        try {
            String strUrl = "";
            if (localhost) {
                strUrl = "https://ogone.test.v-psp.com/ncol/test/maintenancedirect.asp";
            } else {
                strUrl = "https://secure.ogone.com/ncol/prod/maintenancedirect.asp";
            }
            URL url = new URL(strUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            BufferedWriter httpRequestBodyWriter
                    = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                httpRequestBodyWriter.write("AMOUNT=" + aa + "&OPERATION=RFD&ORDERID=" + orderId + "&PAYID=" + opCall.getPayId()+ "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                System.out.println("Request:::"+"AMOUNT=" + aa + "&OPERATION=RFD&ORDERID=" + orderId + "&PAYID=" + opCall.getPayId()+ "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
            } else {
                httpRequestBodyWriter.write("AMOUNT=" + aa + "&OPERATION=RFD&ORDERID=" + orderId + "&PAYID=" + opCall.getPayId()+ "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                System.out.println("Request:::"+"AMOUNT=" + aa + "&OPERATION=RFD&ORDERID=" + orderId + "&PAYID=" + opCall.getPayId()+ "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
            }

            httpRequestBodyWriter.close();

            Scanner httpResponseScanner = new Scanner(conn.getInputStream());
            while (httpResponseScanner.hasNextLine()) {
                response = response + httpResponseScanner.nextLine();
            }
            System.out.print(response);
            httpResponseScanner.close();
            if (conn.getResponseCode() != 200) {
                result.put("transMsgColor", "Red");
            } else {
                String statusCode = "";
                if (response.contains("\"STATUS")) {
                    String temp = response.split("\"STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                } else if (response.contains("\" STATUS")) {
                    String temp = response.split("\" STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                }
                if (statusCode.equals("8") || statusCode.equals("81")) {
                    Calendar cal = Calendar.getInstance();
                    if (statusCode.equals("8")) {
                        result.put("transMsgColor", "Green");
                        System.out.println("Credit OK!");
                        result.put("creditAmt", creditedAmt + amount);

                        double newCreditAmount = creditLimit - amount;
                        result.put("creditAmt1", newCreditAmount);
                        result.put("creditLmt1", newCreditAmount);
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_CREDITED);
                        if (creditedAmt.equals(captdAmount)) {
                            result.put("isCreditAmt", "N");
                            result.put("creditBtn", "N");
                            selectedNetsTransazione.setState(MROldNetsTransazione.STATE_FULL_CREDITED);
                        }
                        result.put("checkBtnCnf", "N");
                        selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                        sx.update(selectedNetsTransazione);
                        sx.flush();
                        storeCreditResponse(response, sx, selectedNetsTransazione);
                        try{
                            MROldPrimanota primanotaRimborso = PrimanotaFactory.creaPrimanotaRimborsoCliente(
                                    sx,
                                    aUser.getSedeOperativa(), //sede
                                    contrattoNoleggio,
                                    pagamento,
                                    amount,
                                    aUser);

                            MROldNumerazione numerazionePrimenote = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, aUser);
                            Integer numeroRegistrazionePrimenote = NumerazioniUtils.aggiornaProgressivo(sx, numerazionePrimenote, FormattedDate.annoCorrente(), 1);
                            primanotaRimborso.setNumerazione(numerazionePrimenote);
                            primanotaRimborso.setNumeroRegistrazione(numeroRegistrazionePrimenote);
                            sx.saveOrUpdate(primanotaRimborso);
                        } catch (StaleObjectStateException staleEx) {
                            result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                            System.out.println("##########The transaction was successful but was unable to create account entry");
                        } catch (Exception ex){
                            result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                            System.out.println("##########The transaction was successful but was unable to create account entry");
                        } catch (Throwable ex) {
                            result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                            System.out.println("##########The transaction was successful but was unable to create account entry");
                        }
                    } else {
                        result.put("transMsgColor", "Green");
                        System.out.println("Credit OK!");
                        result.put("creditAmt", creditedAmt + amount);

                        double newCreditAmount = creditLimit - amount;
                        result.put("creditAmt1", newCreditAmount);
                        result.put("creditLmt1", newCreditAmount);
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_CREDITED_WAIT);
                        if (creditedAmt.equals(captdAmount)) {
                            result.put("isCreditAmt", "N");
                            result.put("creditBtn", "N");
//                            selectedNetsTransazione.setState(MROldNetsTransazione.STATE_FULL_CREDITED_WAIT);
                        }
                        result.put("checkBtnCnf", "Y");
                        selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                        sx.update(selectedNetsTransazione);
                        sx.flush();
                        storeCreditResponse(response, sx, selectedNetsTransazione);
                    }
                } else {
                    result.put("transMsgColor", "Red");
                }
            }
        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
        String statustr = getStatus(response);
        result.put("transMsg", statustr);
        return result;
    }

    public static void storeCreditResponse(String response, Session sx, MROldNetsTransazione selectedNetsTransazione) {
        Map result = new HashMap();
        if (response != "" && response != null) {
            //String statusCode =
            String statusCode = "";
            String NCERROR = "";
            String nccErrorPlus = "";
            String acceptance = "";
            String payId = "";
            if (response.contains("\"STATUS")) {
                String temp = response.split("\"STATUS=\"")[1];
                statusCode = temp.split("\"")[0];
            }
            //if(actProces=="SALE"){
            Double amount = 0.0;
            if (response.contains("amount")) {
                String temp = response.split("amount=\"")[1];
                temp = temp.split("\"")[0];
                if (temp != "") {
                    amount = Double.parseDouble(temp);
                }
            }
            if (response.contains("NCERROR")) {
                String temp = response.split("NCERROR=\"")[1];
                NCERROR = temp.split("\"")[0];
            }
            if (response.contains("NCERRORPLUS")) {
                String temp = response.split("NCERRORPLUS=\"")[1];
                nccErrorPlus = temp.split("\"")[0];
            }
            if (response.contains("ACCEPTANCE")) {
                String temp = response.split("ACCEPTANCE=\"")[1];
                acceptance = temp.split("\"")[0];
            }
            if (response.contains("PAYID")) {
                String temp = response.split("PAYID=\"")[1];
                payId = temp.split("\"")[0];
            }
            String payIdSub = "";
            if (response.contains("PAYIDSUB")) {
                String temp = response.split("PAYIDSUB=\"")[1];
                payIdSub = temp.split("\"")[0];
            }
            Calendar cal = Calendar.getInstance();
            MROldNetsOperationCall operationCall = new MROldNetsOperationCall();
            if (statusCode.equals("8")) {
                operationCall.setType(MROldNetsOperationCall.CREDIT_CALL);
            } else {
                operationCall.setType(MROldNetsOperationCall.CREDIT_CALL_WAIT);
            }
            operationCall.setAmount(amount);
            operationCall.setDate(cal.getTime());
            operationCall.setStatus(statusCode);
            operationCall.setNccError(NCERROR);
            operationCall.setNccErrorPlus(nccErrorPlus);
            operationCall.setAcceptance(acceptance);
            operationCall.setPayId(payId);
            operationCall.setPayIdSub(payIdSub);
            operationCall.setTransazioneNets(selectedNetsTransazione);
            sx.saveOrUpdate(operationCall);
            sx.flush();
            //}
        }
    }


    public static Map processCancelRequest(String orderId, Double amount, String merchantId, String userId, Session sx, Boolean localhost, MROldNetsTransazione selectedNetsTransazione) {

        String response = "";
        Map result = new HashMap();
        MROldNetsAnagrafica xpay = null;
        Query query = sx.createQuery(""
                + "SELECT x "
                + "FROM MROldNetsAnagrafica x "
                + "WHERE inUse = true and "
                + "x.merchantId = :merchId and x.userId = :userId")
                .setParameter("merchId", merchantId)
                .setParameter("userId", userId)
                .setLockMode("x", LockMode.READ);

        xpay = (MROldNetsAnagrafica) query.uniqueResult();
//        query = sx.createQuery(""
//                + "SELECT x "
//                + "FROM MROldNetsOperationCall x "
//                + "WHERE x.status = :status and "
//                + "x.transazioneNets.id = :idTrans")
//                .setParameter("status", "5")
//                .setParameter("idTrans", selectedNetsTransazione.getId())
//                .setLockMode("x", LockMode.READ);
//
//        MROldNetsOperationCall opCall = (MROldNetsOperationCall) query.list().get(0);
        String pwd = xpay.getToken();
        String shaIn = xpay.getShaIn();
        //Double newAmt = new Double( Math.round(amount));
        Double newAmt = new Double(MathUtils.round(amount, 2));
        Integer aa = (int) (double) (newAmt * 10 * 10);
        String strToHash = "";
        if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
            strToHash = "AMOUNT=" + aa + shaIn + "OPERATION=DEL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
        } else {
            strToHash = "AMOUNT=" + aa + shaIn + "OPERATION=DEL" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;
        }
        String strHashed = generateSHASign(strToHash);
        String statusCode = "";
        try {
            String strUrl = "";
            if (localhost) {
                strUrl = "https://ogone.test.v-psp.com/ncol/test/maintenancedirect.asp";
            } else {
                strUrl = "https://secure.ogone.com/ncol/prod/maintenancedirect.asp";
            }
            URL url = new URL(strUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            BufferedWriter httpRequestBodyWriter
                    = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                httpRequestBodyWriter.write("AMOUNT=" + aa + "&OPERATION=DEL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                System.out.println("Request::::" + "AMOUNT=" + aa + "&OPERATION=DEL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
            } else {
                httpRequestBodyWriter.write("AMOUNT=" + aa + "&OPERATION=DEL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                System.out.println("Request::::" + "AMOUNT=" + aa + "&OPERATION=DEL&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
            }
            httpRequestBodyWriter.close();

            Scanner httpResponseScanner = new Scanner(conn.getInputStream());
            while (httpResponseScanner.hasNextLine()) {
                response = response + httpResponseScanner.nextLine();
            }
            System.out.print(response);
            httpResponseScanner.close();
            if (conn.getResponseCode() != 200) {
                result.put("transMsgColor", "Red");
            } else {
                if (response.contains("\"STATUS")) {
                    String temp = response.split("\"STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                } else if (response.contains("\" STATUS")) {
                    String temp = response.split("\" STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                }
                if (statusCode.equals("6") || statusCode.equals("61")) {
                    Calendar cal = Calendar.getInstance();
                    result.put("transMsg", "ANNUL OK");
                    result.put("transMsgColor", "Green");
                    result.put("transId", "");
                    result.put("statusCode", statusCode);
                    //initializeButtons();
                    System.out.println("Annul OK!");
                    if (statusCode.equals("6")) {
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_ANNULL);
                        result.put("checkBtnCnf", "N");
                    } else {
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_ANNULL_WAIT);
                        result.put("checkBtnCnf", "Y");
                    }


                    selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                    sx.update(selectedNetsTransazione);
                    sx.flush();
                    if (!statusCode.equals("6")) {
                        storeWaitRequest(MROldNetsTransazione.STATE_ANNULL_WAIT, response, sx, selectedNetsTransazione);
                    }
                } else {
                    result.put("transMsgColor", "Red");
                }
            }
        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
        String statustr = getStatus(response);
        result.put("transMsg", statustr);
        return result;
    }

    public static String getStatus(String response) {
        Integer code = 0;
        if (response.contains("\" STATUS")) {
            String temp = response.split("\" STATUS=\"")[1];
            temp = temp.split("\"")[0];
            code = Integer.parseInt(temp);
        } else if (response.contains("\"STATUS")) {
            String temp = response.split("\"STATUS=\"")[1];
            temp = temp.split("\"")[0];
            code = Integer.parseInt(temp);
        }
        String errorMsg = "";
        if (response.contains("\" NCERRORPLUS")) {
            String temp = response.split("\" NCERRORPLUS=\"")[1];
            if((!temp.equals("")) && temp!=null){
                errorMsg = temp.split("\"")[0];
            }
        } else if (response.contains("\"NCERRORPLUS")) {
            String temp = response.split("\"NCERRORPLUS=\"")[1];
            if((!temp.equals("")) && temp!=null){
                errorMsg = temp.split("\"")[0];
            }
        }
        String status = "";
        switch (code) {
            case 0:
                status = "Invalid or incomplete";
                if(!errorMsg.equals("") && !errorMsg.equals("!")){
                    status =  errorMsg;
                }
                break;
            case 1:
                status = "Cancelled by customer";
                break;
            case 2:
                status = "Authorisation refused";
                break;
            case 4:
                status = "Order stored";
                break;
            case 40:
                status = "Stored waiting external result";
                break;
            case 41:
                status = "Waiting for client payment ";
                break;
            case 46:
                status = "Waiting authentication";
                break;
            case 5:
                status = "Authorised";
                break;
            case 50:
                status = "Authorized waiting external result ";
                break;
            case 51:
                status = "Authorisation waiting";
                break;
            case 52:
                status = "Authorisation not known";
                break;
            case 55:
                status = "Standby";
                break;
            case 56:
                status = "Ok with scheduled payments ";
                break;
            case 59:
                status = "Authorization to be requested manually";
                break;
            case 57:
                status = "Not OK with scheduled payments";
                break;
            case 6:
                status = "Authorised and cancelled";
                break;
            case 61:
                status = "Author. deletion waiting";
                break;
            case 62:
                status = "Author. deletion uncertain";
                break;
            case 63:
                status = "Author. deletion refused";
                break;
            case 64:
                status = "Authorised and cancelled ";
                break;
            case 7:
                status = "The payment has been cancelled/deleted";
                break;
            case 71:
                status = "Waiting for payment cancellation/deletion";
                break;
            case 72:
                status = "Payment deletion uncertain";
                break;
            case 73:
                status = "Payment deletion refused";
                break;
            case 74:
                status = "Payment deleted ";
                break;
            case 8:
                status = "The payment has been refunded";
                break;
            case 81:
                status = "Waiting for refund of the payment";
                break;
            case 82:
                status = "Refund uncertain ";
                break;
            case 83:
                status = "Refund refused ";
                break;
            case 84:
                status = "Refund";
                break;
            case 85:
                status = "Refund handled by merchant";
                break;
            case 9:
                status = "The payment has been accepted.";
                break;
            case 91:
                status = "Payment processing: The data capture will be processed offline.";
                break;
            case 92:
                status = "Payment uncertain";
                break;
            case 93:
                status = "Payment refused";
                break;
            case 94:
                status = "Refund declined by the acquirer";
                break;
            case 95:
                status = "Payment handled by merchant";
                break;
            case 96:
                status = "Refund reversed";
                break;
            case 99:
                status = "Being processed ";
                break;
            default:
                System.out.println("Number not exists");
                break;
        }
        return status;
    }

    public static Map processCnfRequest(String requestType, String contractId, Session sx, User aUser, MROldNetsTransazione selectedNetsTransazione, Boolean host, String merchantId, String userId, Double captdAmt, Double authdAmt, Double creditdAmt) {

        double amount = 0.0;
        String statusCode = "";
        String resStr = "";
        boolean isCnf = false;
        String payId = "";
        String payIdSub = "";
        String state = "";
        Map result = new HashMap();
        if (requestType.equals(MROldNetsTransazione.STATE_AUTHORIZED_WAIT)) {
            Map result1 = getCnfStatus(selectedNetsTransazione, host, merchantId, userId, sx);
            statusCode = result1.get("code").toString();
            String tempAmt = result1.get("amount").toString();
            amount = Double.parseDouble(tempAmt);
            payId = result1.get("payId").toString();
            payIdSub = result1.get("payIdSub").toString();
            resStr = result1.get("resStr").toString();
            if (statusCode.equals("5")) {
                isCnf = true;
                selectedNetsTransazione.setState(MROldNetsTransazione.STATE_AUTHORIZED);
                sx.saveOrUpdate(selectedNetsTransazione);
                sx.flush();
                result.put("captureBtn", "Y");
                result.put("cancelBtn", "Y");
                result.put("saleBtn", "N");
                result.put("authBtn", "N");

                result.put("isCaptAmt", "Y");
                result.put("captAmt1", amount);

                result.put("isAuthAmt", "N");
                result.put("authAmt", amount);
                result.put("checkBtnCnf", "N");
            }
        } else if (requestType.equals(MROldNetsTransazione.STATE_ANNULL_WAIT)) {
            Map result1 = getCnfStatus(selectedNetsTransazione, host, merchantId, userId, sx);
            statusCode = result1.get("code").toString();
            if (statusCode.equals("6")) {
                isCnf = true;
                selectedNetsTransazione.setState(MROldNetsTransazione.STATE_ANNULL);
                sx.saveOrUpdate(selectedNetsTransazione);
                sx.flush();
            }
            String tempAmt = result1.get("amount").toString();
            amount = Double.parseDouble(tempAmt);
            payId = result1.get("payId").toString();
            payIdSub = result1.get("payIdSub").toString();
            resStr = result1.get("resStr").toString();
            if(isCnf){
                Query query1 = sx.createQuery(""
                        + "SELECT x "
                        + "FROM MROldNetsOperationCall x "
                        + "WHERE x.transazioneNets = :transazioneNets and "
                        + "x.payId = :payId and x.status = :status and x.type = :type")
                        .setParameter("transazioneNets", selectedNetsTransazione)
                        .setParameter("payId", payId)
                        .setParameter("status", "")
                        .setParameter("type", MROldNetsTransazione.STATE_ANNULL_WAIT);
                List temp = query1.list();
                if(temp.size()>0){
                    Iterator itr = temp.iterator();
                    while(itr.hasNext()){
                        MROldNetsOperationCall op = (MROldNetsOperationCall)itr.next();
                        if(op.getPayIdSub().equals(payIdSub)){
                            op.setStatus(statusCode);
                            op.setType(MROldNetsTransazione.STATE_ANNULL);
                            op.setAmount(amount);
                            sx.saveOrUpdate(op);
                            sx.flush();
                        }
                    }
                }
            }
        } else if (requestType.equals(MROldNetsTransazione.STATE_CAPTURE_WAIT) || requestType.equals("CAPTUR_WAIT")) {
            Map result1 = getCnfStatus(selectedNetsTransazione, host, merchantId, userId, sx);
            statusCode = result1.get("code").toString();
            if (statusCode.equals("9")) {
                isCnf = true;
//                selectedNetsTransazione.setState(MROldNetsTransazione.STATE_CAPTUR);
//                sx.saveOrUpdate(selectedNetsTransazione);
//                sx.flush();
                state = MROldNetsOperationCall.CAPTURE_CALL;
                result.put("cancelBtn", "N");
                result.put("creditBtn", "Y");
                result.put("isCreditAmt", "Y");
            }
            String tempAmt = result1.get("amount").toString();
            amount = Double.parseDouble(tempAmt);
            payId = result1.get("payId").toString();
            payIdSub = result1.get("payIdSub").toString();
            resStr = result1.get("resStr").toString();
        } else if (requestType.equals(MROldNetsTransazione.STATE_FULL_CREDITED_WAIT)) {
            Map result1 = getCnfStatus(selectedNetsTransazione, host, merchantId, userId, sx);
            statusCode = result1.get("code").toString();
            if (statusCode.equals("8")) {
                isCnf = true;
//                selectedNetsTransazione.setState(MROldNetsTransazione.STATE_FULL_CREDITED);
//                sx.saveOrUpdate(selectedNetsTransazione);
//                sx.flush();
                state = MROldNetsOperationCall.CREDIT_CALL;
            }
            String tempAmt = result1.get("amount").toString();
            amount = Double.parseDouble(tempAmt);
            payId = result1.get("payId").toString();
            payIdSub = result1.get("payIdSub").toString();
            resStr = result1.get("resStr").toString();
        } else if (requestType.equals(MROldNetsTransazione.STATE_SALE_WAIT)) {
            Map result1 = getCnfStatus(selectedNetsTransazione, host, merchantId, userId, sx);
            statusCode = result1.get("code").toString();
            if (statusCode.equals("9")) {
                isCnf = true;
                selectedNetsTransazione.setState(MROldNetsTransazione.STATE_SALE);
                sx.saveOrUpdate(selectedNetsTransazione);
                sx.flush();
                state = MROldNetsOperationCall.SALE_CALL;
                result.put("checkBtn", "N");
                result.put("saleBtn", "N");
                result.put("authBtn", "N");
                result.put("captureBtn", "N");
                result.put("cancelBtn", "N");
                result.put("creditBtn", "Y");
                result.put("isAuthAmt", "Y");
                result.put("authAmt1", "0.0");
                result.put("isCaptAmt", "N");
                result.put("isCreditAmt", "Y");
            }
            String tempAmt = result1.get("amount").toString();
            amount = Double.parseDouble(tempAmt);
            payId = result1.get("payId").toString();
            payIdSub = result1.get("payIdSub").toString();
            resStr = result1.get("resStr").toString();
        } else if (requestType.equals(MROldNetsTransazione.STATE_CREDITED_WAIT)) {
            Map result1 = getCnfStatus(selectedNetsTransazione, host, merchantId, userId, sx);
            statusCode = result1.get("code").toString();
            if (statusCode.equals("8")) {
                isCnf = true;
                //selectedNetsTransazione.setState(MROldNetsTransazione.STATE_CREDITED);
                //sx.saveOrUpdate(selectedNetsTransazione);
                //sx.flush();
                state = MROldNetsOperationCall.CREDIT_CALL;
            }
            String tempAmt = result1.get("amount").toString();
            amount = Double.parseDouble(tempAmt);
            payId = result1.get("payId").toString();
            payIdSub = result1.get("payIdSub").toString();
            resStr = result1.get("resStr").toString();
            //result.put("creditAmt", captdAmt-(creditdAmt+amount));

            //double newCreditAmount = creditLimit - amount;
            //result.put("creditdAmt1", creditdAmt+amount);
            if(authdAmt-(creditdAmt+amount)>0){
                result.put("creditBtn", "Y");
                result.put("isCreditAmt", "Y");
            }
            //result.put("creditLmt1", newCreditAmount);
        }

        boolean isCnfList = true;
        if (isCnf) {
            MROldContrattoNoleggio contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, Integer.parseInt(contractId));
            if (statusCode.equals("9") && state.equals(MROldNetsOperationCall.CAPTURE_CALL)) {
                MROldNetsOperationCall xpay = null;
                Query query = sx.createQuery(""
                        + "SELECT x "
                        + "FROM MROldNetsOperationCall x "
                        + "WHERE x.transazioneNets = :transazioneNets and "
                        + "x.payId = :payId and x.status = :status and x.type = :type")
                        .setParameter("transazioneNets", selectedNetsTransazione)
                        .setParameter("payId", payId)
                        .setParameter("status", "")
                        .setParameter("type", MROldNetsOperationCall.CAPTURE_CALL_WAIT);

                //xpay = (MROldNetsOperationCall) query.uniqueResult();
                List<MROldNetsOperationCall> temp = query.list();
                if(temp.size()==0){
                    query = sx.createQuery(""
                            + "SELECT x "
                            + "FROM MROldNetsOperationCall x "
                            + "WHERE x.transazioneNets = :transazioneNets and "
                            + "x.payId = :payId and x.status = :status and x.type = :type")
                            .setParameter("transazioneNets", selectedNetsTransazione)
                            .setParameter("payId", payId)
                            .setParameter("status", "")
                            .setParameter("type", MROldNetsOperationCall.SALE_CALL_WAIT);
                    temp = query.list();
                }
                if(temp.size()>1){
                    Iterator itr = temp.iterator();
                    while(itr.hasNext()){
                        MROldNetsOperationCall op = (MROldNetsOperationCall)itr.next();
                        if(op.getPayIdSub().equals(payIdSub)){
                            op.setStatus(statusCode);
                            op.setType(MROldNetsOperationCall.CAPTURE_CALL);
                            op.setAmount(amount);
                            sx.saveOrUpdate(op);
                            sx.flush();
                        }
                    }
                    isCnfList = false;
                } else {
                    xpay = temp.get(0);
                    xpay.setStatus(statusCode);
                    xpay.setType(MROldNetsOperationCall.CAPTURE_CALL);
                    xpay.setAmount(amount);
                    sx.saveOrUpdate(xpay);
                    selectedNetsTransazione.setState(MROldNetsTransazione.STATE_CAPTURE);
                    sx.saveOrUpdate(selectedNetsTransazione);
                    sx.flush();
                }
                try{
                    MROldPrimanota primanotaIncasso = PrimanotaFactory.creaPrimanotaIncassoCliente(
                            sx,
                            aUser.getSedeOperativa(), //sede
                            contrattoNoleggio,
                            contrattoNoleggio.getPagamento(),
                            null, //garanzia
                            amount,
                            aUser);
                    MROldNumerazione numerazionePrimenote = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, aUser);
                    Integer numeroRegistrazionePrimenote = NumerazioniUtils.aggiornaProgressivo(sx, numerazionePrimenote, FormattedDate.annoCorrente(), 1);
                    primanotaIncasso.setNumerazione(numerazionePrimenote);
                    primanotaIncasso.setNumeroRegistrazione(numeroRegistrazionePrimenote);
                    sx.saveOrUpdate(primanotaIncasso);
                } catch (StaleObjectStateException staleEx) {
                    result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                    System.out.println("##########The transaction was successful but was unable to create account entry");
                } catch (Exception ex){
                    result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                    System.out.println("##########The transaction was successful but was unable to create account entry");
                } catch (Throwable ex) {
                    result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                    System.out.println("##########The transaction was successful but was unable to create account entry");
                }
            } else if (statusCode.equals("8") && state.equals(MROldNetsOperationCall.CREDIT_CALL)) {
                MROldNetsOperationCall xpay = null;
                Query query = sx.createQuery(""
                        + "SELECT x "
                        + "FROM MROldNetsOperationCall x "
                        + "WHERE x.transazioneNets = :transazioneNets and "
                        + "x.payId = :payId and x.status = :status and x.type = :type")
                        .setParameter("transazioneNets", selectedNetsTransazione)
                        .setParameter("payId", payId)
                        .setParameter("status", "")
                        .setParameter("type", MROldNetsOperationCall.CREDIT_CALL_WAIT);

                //xpay = (MROldNetsOperationCall) query.uniqueResult();
                List<MROldNetsOperationCall> temp = query.list();
                if(temp.size()>1){
                    Iterator itr = temp.iterator();
                    while(itr.hasNext()){
                        MROldNetsOperationCall op = (MROldNetsOperationCall)itr.next();
                        if(op.getPayIdSub().equals(payIdSub)){
                            op.setStatus(statusCode);
                            op.setType(MROldNetsOperationCall.CREDIT_CALL);
                            op.setAmount(amount);
                            sx.saveOrUpdate(op);
                            sx.flush();
                        }
                    }
                    isCnfList = false;
                } else {
                    xpay = temp.get(0);
                    xpay.setStatus(statusCode);
                    xpay.setType(MROldNetsOperationCall.CREDIT_CALL);
                    xpay.setAmount(amount);
                    sx.saveOrUpdate(xpay);
                    if (requestType.equals(MROldNetsTransazione.STATE_FULL_CREDITED_WAIT)) {
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_FULL_CREDITED);
                    } else if(requestType.equals(MROldNetsTransazione.STATE_CREDITED_WAIT)){
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_CREDITED);
                    }
                    sx.saveOrUpdate(selectedNetsTransazione);
                    sx.flush();
                }
//                xpay.setStatus(statusCode);
//                xpay.setType(MROldNetsOperationCall.CREDIT_CALL);
//                xpay.setAmount(amount);
//                sx.saveOrUpdate(xpay);
//                sx.flush();
                try{
                    MROldPrimanota primanotaRimborso = PrimanotaFactory.creaPrimanotaRimborsoCliente(
                            sx,
                            aUser.getSedeOperativa(), //sede
                            contrattoNoleggio,
                            contrattoNoleggio.getPagamento(),
                            amount,
                            aUser);

                    MROldNumerazione numerazionePrimenote = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, aUser);
                    Integer numeroRegistrazionePrimenote = NumerazioniUtils.aggiornaProgressivo(sx, numerazionePrimenote, FormattedDate.annoCorrente(), 1);
                    primanotaRimborso.setNumerazione(numerazionePrimenote);
                    primanotaRimborso.setNumeroRegistrazione(numeroRegistrazionePrimenote);
                    sx.saveOrUpdate(primanotaRimborso);
                } catch (StaleObjectStateException staleEx) {
                    result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                    System.out.println("##########The transaction was successful but was unable to create account entry");
                } catch (Exception ex){
                    result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                    System.out.println("##########The transaction was successful but was unable to create account entry");
                } catch (Throwable ex) {
                    result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                    System.out.println("##########The transaction was successful but was unable to create account entry");
                }
            } else if (statusCode.equals("9") && state.equals(MROldNetsOperationCall.SALE_CALL)) {
                MROldNetsOperationCall xpay = null;
                Query query = sx.createQuery(""
                        + "SELECT x "
                        + "FROM MROldNetsOperationCall x "
                        + "WHERE x.transazioneNets = :transazioneNets and "
                        + "x.payId = :payId and x.status = :status and x.type = :type")
                        .setParameter("transazioneNets", selectedNetsTransazione)
                        .setParameter("payId", payId)
                        .setParameter("status" , "")
                        .setParameter("type", MROldNetsOperationCall.SALE_CALL_WAIT);

                //xpay = (MROldNetsOperationCall) query.uniqueResult();
                List<MROldNetsOperationCall> temp = query.list();
                if (temp.size() == 0) {
                    query = sx.createQuery(""
                            + "SELECT x "
                            + "FROM MROldNetsOperationCall x "
                            + "WHERE x.transazioneNets = :transazioneNets and "
                            + "x.payId = :payId and x.type = :type")
                            .setParameter("transazioneNets", selectedNetsTransazione)
                            .setParameter("payId", payId)
                            .setParameter("type", MROldNetsOperationCall.CAPTURE_CALL_WAIT);

                    temp = query.list();
                }


                if(temp.size()>1){
                    Iterator itr = temp.iterator();
                    while(itr.hasNext()){
                        MROldNetsOperationCall op = (MROldNetsOperationCall)itr.next();
                        if(op.getPayIdSub().equals(payIdSub)){
                            op.setStatus(statusCode);
                            op.setType(MROldNetsOperationCall.SALE_CALL);
                            op.setAmount(amount);
                            sx.saveOrUpdate(op);
                            sx.flush();
                        }
                    }
                    isCnfList = false;
                } else {
                    xpay = temp.get(0);
                    xpay.setStatus(statusCode);
                    xpay.setType(MROldNetsOperationCall.SALE_CALL);
                    xpay.setAmount(amount);
                    sx.saveOrUpdate(xpay);
                    sx.flush();
                }


//                xpay.setStatus(statusCode);
//                xpay.setType(MROldNetsOperationCall.SALE_CALL);
//                xpay.setAmount(amount);
//                sx.saveOrUpdate(xpay);
//                sx.flush();
                try{
                    MROldPrimanota primanotaIncasso = PrimanotaFactory.creaPrimanotaIncassoCliente(
                            sx,
                            aUser.getSedeOperativa(), //sede
                            contrattoNoleggio,
                            contrattoNoleggio.getPagamento(),
                            null, //garanzia
                            amount,
                            aUser);
                    MROldNumerazione numerazionePrimenote = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, aUser);
                    Integer numeroRegistrazionePrimenote = NumerazioniUtils.aggiornaProgressivo(sx, numerazionePrimenote, FormattedDate.annoCorrente(), 1);
                    primanotaIncasso.setNumerazione(numerazionePrimenote);
                    primanotaIncasso.setNumeroRegistrazione(numeroRegistrazionePrimenote);
                    sx.saveOrUpdate(primanotaIncasso);
                } catch (StaleObjectStateException staleEx) {
                    result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                    System.out.println("##########The transaction was successful but was unable to create account entry");
                } catch (Exception ex){
                    result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                    System.out.println("##########The transaction was successful but was unable to create account entry");
                } catch (Throwable ex) {
                    result.put("transMsg1", bundle.getString("Ingenico.msgTransactionSucessAccountEntryFail"));
                    System.out.println("##########The transaction was successful but was unable to create account entry");
                }
            }
        }
        result.put("statusCode", statusCode);
        result.put("resStr", resStr);
        if(isCnfList){
            result.put("checkCnf", "Y");
        } else {
            result.put("checkCnf", "N");
        }
        return result;
    }

    public static Map getCnfStatus(MROldNetsTransazione selectedNetsTransazione, Boolean host, String merchantId, String userId, Session sx) {

        String statusCode = "";
        String response = "";
        Double amount = 0.0;
        String payId = "";
        Map result = new HashMap();
        MROldNetsAnagrafica xpay = null;
        Query query = sx.createQuery(""
                + "SELECT x "
                + "FROM MROldNetsAnagrafica x "
                + "WHERE inUse = true and "
                + "x.merchantId = :merchId and x.userId = :userId")
                .setParameter("merchId", merchantId)
                .setParameter("userId", userId)
                .setLockMode("x", LockMode.READ);

        xpay = (MROldNetsAnagrafica) query.uniqueResult();
        String pwd = xpay.getToken();
        String shaIn = xpay.getShaIn();
        Query query1 = sx.createQuery(""
                + "SELECT x "
                + "FROM MROldNetsOperationCall x "
                + "WHERE x.transazioneNets = :transazioneNets and "
                + "x.status = :status")
                .setParameter("transazioneNets", selectedNetsTransazione)
                .setParameter("status", "")
                .setMaxResults(1)
                .setLockMode("x", LockMode.READ);
//        Double newAmt = new Double(MathUtils.round(amount, 2));
//        Integer aa = (int) (double) (newAmt * 10 * 10);
        MROldNetsOperationCall opCall = (MROldNetsOperationCall) query1.uniqueResult();
        String strToHash = "";
        if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
            strToHash = "PAYID=" + opCall.getPayId() + shaIn + "PAYIDSUB=" + opCall.getPayIdSub() + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
        } else {
            strToHash = "PAYID=" + opCall.getPayId() + shaIn + "PAYIDSUB=" + opCall.getPayIdSub() + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;
        }
        String strHashed = generateSHASign(strToHash);
        String payIdSub = "";
        try {
            String strUrl = "";
            if (host) {
                strUrl = "https://ogone.test.v-psp.com/ncol/test/querydirect.asp";
            } else {
                strUrl = "https://secure.ogone.com/ncol/prod/querydirect.asp";
            }
            URL url = new URL(strUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            BufferedWriter httpRequestBodyWriter
                    = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                httpRequestBodyWriter.write("PAYID=" + opCall.getPayId() + "&PAYIDSUB=" + opCall.getPayIdSub() + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                System.out.println("Request:::"+"PAYID=" + opCall.getPayId() + "&PAYIDSUB=" + opCall.getPayIdSub() + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
            } else {
                httpRequestBodyWriter.write("PAYID=" + opCall.getPayId() + "&PAYIDSUB=" + opCall.getPayIdSub() + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                System.out.println("Request:::"+"PAYID=" + opCall.getPayId() + "&PAYIDSUB=" + opCall.getPayIdSub() + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
            }
            httpRequestBodyWriter.close();

            Scanner httpResponseScanner = new Scanner(conn.getInputStream());
            while (httpResponseScanner.hasNextLine()) {
                response = response + httpResponseScanner.nextLine();
            }
            System.out.print(response);
            httpResponseScanner.close();
            if (response.contains("\"STATUS")) {
                String temp = response.split("\"STATUS=\"")[1];
                statusCode = temp.split("\"")[0];
            } else if (response.contains("\" STATUS")) {
                String temp = response.split("\" STATUS=\"")[1];
                statusCode = temp.split("\"")[0];
            }
            if (response.contains("amount")) {
                String temp = response.split("amount=\"")[1];
                temp = temp.split("\"")[0];
                if (temp != "") {
                    amount = Double.parseDouble(temp);
                }
            }
            if (response.contains("PAYID")) {
                String temp = response.split("PAYID=\"")[1];
                payId = temp.split("\"")[0];
            }
            if (response.contains("PAYIDSUB")) {
                String temp = response.split("PAYIDSUB=\"")[1];
                payIdSub = temp.split("\"")[0];
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String resStr = getStatus(response);

        result.put("code", statusCode);
        result.put("amount", amount);
        result.put("payId", payId);
        result.put("payIdSub", payIdSub);
        result.put("resStr", resStr);


        return result;
    }

    public static Map processRenewAuth(String alias, Double amount, String orderId, String merchantId, String userId, Session sx, Boolean localhost, MROldNetsTransazione selectedNetsTransazione) {

        String response = "";
        Map result = new HashMap();
        MROldNetsAnagrafica xpay = null;
        Query query = sx.createQuery(""
                + "SELECT x "
                + "FROM MROldNetsAnagrafica x "
                + "WHERE inUse = true and "
                + "x.merchantId = :merchId and x.userId = :userId")
                .setParameter("merchId", merchantId)
                .setParameter("userId", userId)
                .setLockMode("x", LockMode.READ);

        xpay = (MROldNetsAnagrafica) query.uniqueResult();

        String pwd = xpay.getToken();
        String shaIn = xpay.getShaIn();
        //Double newAmt = new Double( Math.round(amount));
        Double newAmt = new Double(MathUtils.round(amount, 2));
        Integer aa = (int) (double) (newAmt * 10 * 10);
        String strToHash = "";
//        Query query1 = sx.createQuery(""
//                + "SELECT x "
//                + "FROM MROldNetsOperationCall x "
//                + "WHERE x.transazioneNets = :transazioneNets ")
//                .setParameter("transazioneNets", selectedNetsTransazione);
//
//        MROldNetsOperationCall xpay1 = (MROldNetsOperationCall) query1.uniqueResult();
        if (orderId.startsWith("DG")) {
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                strToHash = "AMOUNT=" + aa + shaIn + "ECI=9" + shaIn + "OPERATION=REN" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;
            } else {
                strToHash = "AMOUNT=" + aa + shaIn + "ECI=9" + shaIn + "OPERATION=REN" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;
            }
        } else {
            if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                strToHash = "AMOUNT=" + aa + shaIn + "OPERATION=REN" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "USERID=" + userId + shaIn;

            } else {
                strToHash = "AMOUNT=" + aa + shaIn + "OPERATION=REN" + shaIn + "ORDERID=" + orderId + shaIn + "PSPID=" + merchantId + shaIn + "PSWD=" + pwd + shaIn + "REFID=" + xpay.getRefId() + shaIn + "REFKIND=MGID" + shaIn + "USERID=" + userId + shaIn;

            }
        }
        String strHashed = generateSHASign(strToHash);
        try {
            String strUrl = "";
            if (localhost) {
                strUrl = "https://ogone.test.v-psp.com/ncol/test/maintenancedirect.asp";
            } else {
                strUrl = "https://secure.ogone.com/ncol/prod/maintenancedirect.asp";
            }
            URL url = new URL(strUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            BufferedWriter httpRequestBodyWriter
                    = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            if (orderId.startsWith("DG")) {
                if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                    httpRequestBodyWriter.write("AMOUNT=" + aa + "&ECI=9" + "&OPERATION=REN&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"AMOUNT=" + aa + "&ECI=9" + "&OPERATION=REN&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                } else {
                    httpRequestBodyWriter.write("AMOUNT=" + aa + "&ECI=9" + "&OPERATION=REN&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"AMOUNT=" + aa + "&ECI=9" + "&OPERATION=REN&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                }
            } else {
                if (xpay.getRefId().equals("") || xpay.getRefId() == null) {
                    httpRequestBodyWriter.write("AMOUNT=" + aa + "&OPERATION=REN&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"AMOUNT=" + aa + "&OPERATION=REN&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&USERID=" + userId + "&SHASIGN=" + strHashed);
                } else {
                    httpRequestBodyWriter.write("AMOUNT=" + aa + "&OPERATION=REN&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                    System.out.println("Request:::"+"AMOUNT=" + aa + "&OPERATION=REN&ORDERID=" + orderId + "&PSPID=" + merchantId + "&PSWD=" + pwd + "&REFID=" + xpay.getRefId() + "&REFKIND=MGID&USERID=" + userId + "&SHASIGN=" + strHashed);
                }
            }
            httpRequestBodyWriter.close();

            Scanner httpResponseScanner = new Scanner(conn.getInputStream());
            while (httpResponseScanner.hasNextLine()) {
                response = response + httpResponseScanner.nextLine();
            }
            System.out.print(response);
            httpResponseScanner.close();
            if (conn.getResponseCode() != 200) {
                result.put("transMsgColor", "Red");
            } else {
                String statusCode = "";
                if (response.contains("\"STATUS")) {
                    String temp = response.split("\"STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                } else if (response.contains("\" STATUS")) {
                    String temp = response.split("\" STATUS=\"")[1];
                    statusCode = temp.split("\"")[0];
                }
                if (statusCode.equals("5") || statusCode.equals("51")) {
                    Calendar cal = Calendar.getInstance();

                    if (statusCode.equals("5")) {
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_AUTHORIZED);

                        result.put("transMsgColor", "Green");
                        result.put("captureBtn", "Y");
                        result.put("cancelBtn", "Y");
                        result.put("saleBtn", "N");
                        result.put("authBtn", "N");

                        result.put("isCaptAmt", "Y");
                        result.put("captAmt1", amount);

                        result.put("isAuthAmt", "N");
                        result.put("authAmt", amount);
                        result.put("checkBtnCnf", "N");
                    } else {
                        result.put("transMsgColor", "Green");
                        result.put("captureBtn", "N");
                        result.put("cancelBtn", "N");
                        result.put("saleBtn", "N");
                        result.put("authBtn", "N");

                        result.put("isCaptAmt", "N");
                        result.put("captAmt1", amount);

                        result.put("isAuthAmt", "N");
                        result.put("authAmt", amount);
                        result.put("checkBtnCnf", "Y");
                        selectedNetsTransazione.setState(MROldNetsTransazione.STATE_AUTHORIZED_WAIT);
                    }

                    selectedNetsTransazione.setLastUpdateDate(cal.getTime());
                    selectedNetsTransazione.setAmountAuth(amount);
                    sx.update(selectedNetsTransazione);
                    sx.flush();
                } else {
                    result.put("transMsgColor", "Red");
                    result.put("transMsg", "Auth Wait Error");
                }
            }
        } catch (MalformedURLException mex) {
            mex.printStackTrace();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
        String statustr = getStatus(response);
        result.put("transMsg", statustr);
        return result;
    }

    public static void storeWaitRequest(String statusType, String response, Session sx, MROldNetsTransazione selectedNetsTransazione) {
        String statusCode = "";
        String NCERROR = "";
        String nccErrorPlus = "";
        String acceptance = "";
        Double amount = 0.0;
        String payId = "";
        if (response.contains("PAYID")) {
            String temp = response.split("PAYID=\"")[1];
            payId = temp.split("\"")[0];
        }
        String payIdSub = "";
        if (response.contains("PAYIDSUB")) {
            String temp = response.split("PAYIDSUB=\"")[1];
            payIdSub = temp.split("\"")[0];
        }
        if (response.contains("\"STATUS")) {
            String temp = response.split("\"STATUS=\"")[1];
            statusCode = temp.split("\"")[0];
        }
        if(payIdSub.equals("")){
            payIdSub = "0";
        }
        Calendar cal = Calendar.getInstance();
        MROldNetsOperationCall operationCall = new MROldNetsOperationCall();
        operationCall.setType(statusType);
        operationCall.setAmount(amount);
        operationCall.setDate(cal.getTime());
        operationCall.setStatus(statusCode);
        operationCall.setNccError(NCERROR);
        operationCall.setNccErrorPlus(nccErrorPlus);
        operationCall.setAcceptance(acceptance);
        operationCall.setPayId(payId);
        operationCall.setTransazioneNets(selectedNetsTransazione);
        operationCall.setPayIdSub(payIdSub);
        sx.saveOrUpdate(operationCall);
        sx.flush();
    }

    public static String getState(MROldNetsTransazione netsTransazione){
        String state = "";
        if(netsTransazione.getState().equals("CREDITED")){
            state = "CREDIT";
        } else if(netsTransazione.getState().equals("CREDITED_WAIT")){
            state = "CREDIT_WAIT";
        } else if(netsTransazione.getState().equals("STATE_FULL_CREDITED_WAIT")){
            state = "CREDIT_WAIT";
        } else {
            state = netsTransazione.getState();
        }
        return state;

    }

    public static List getDepositList(String contractId, Session sx){
        List res = new ArrayList();
        boolean vPosAbilitato = Preferenze.getIngenicoAbilitato(sx);

        MROldContrattoNoleggio contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, Integer.parseInt(contractId));
        if (!vPosAbilitato) {
            return res;
        } else if (contrattoNoleggio.getMovimento().getSedeUscita().getMerchantId() == null || contrattoNoleggio.getMovimento().getSedeUscita().getMerchantId().equals("")) {
            return res;
        } else {
            Map resMap = new HashMap();
            List<MROldNetsTransazione> transactionList = (List<MROldNetsTransazione>) sx.createQuery(""
                    + "FROM MROldNetsTransazione t "
                    + "WHERE 1 = 1 "
                    + "AND t.contratto = :contrattoNoleggio and t.shaSign is not null "
                    + "AND t.merchantId is not null and t.merchantId = :merchantId "
                    + "ORDER BY t.startTransactionDate DESC")
                    .setParameter("contrattoNoleggio", contrattoNoleggio)
                    .setParameter("merchantId", contrattoNoleggio.getMovimento().getSedeUscita().getMerchantId())
            .list();
            SimpleDateFormat formatDateTimeSweden = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //DESEncrypter dencrypter = new DESEncrypter("utenti_myrent");
            boolean isAuth = false;
            Double authTotal = 0.0;
            Double chargeTotal = 0.0;
            for (int i = 0; i < transactionList.size(); i++) {
                Map transMap = new HashMap();
                String trans = "Order - " + formatDateTimeSweden.format(transactionList.get(i).getStartTransactionDate()) + " - " + transactionList.get(i).getIdTransazione();
                //String decStr = dencrypter.decrypt(transactionList.get(i).getIdTransazione())
                transMap.put("transId", transactionList.get(i).getIdTransazione());
                MROldNetsTransazione netsTransazione = (MROldNetsTransazione) transactionList.get(i);
                transMap.put("authAmt","0");
                transMap.put("captAmt","0");
                transMap.put("refundAmt","0");
                transMap.put("isAuthRev","N");
                transMap.put("payId","");
                transMap.put("cardNum",transactionList.get(i).getCardNumber());
                transMap.put("cardType",transactionList.get(i).getCardType());
                if(netsTransazione.getState() != null && netsTransazione.getState().equals(MROldNetsTransazione.FLEX_CHECKOUT)){
                    transMap.put("isRegister","Y");
                    isAuth = false;
                } else {
                    if (netsTransazione.getState() != null && netsTransazione.getState().contains("WAIT")){
                        transMap.put("isWait","Y");
                    }
                    if(netsTransazione.getState() != null &&  (netsTransazione.getState().equals(MROldNetsTransazione.STATE_SALE) || netsTransazione.getState().equals(MROldNetsTransazione.STATE_SALE_WAIT))){
                        List<MROldNetsOperationCall> temp = sx.createQuery("select op from MROldNetsOperationCall op where op.transazioneNets.id = :transId").setParameter("transId", netsTransazione.getId()).list();
                        if(temp.size()>0){
                            transMap.put("captAmt",temp.get(0).getAmount());
                            transMap.put("payId",temp.get(0).getPayId());
                            chargeTotal += temp.get(0).getAmount();
                        }

                        isAuth = false;
                    } else if(netsTransazione.getState() != null && (netsTransazione.getState().equals(MROldNetsTransazione.STATE_AUTHORIZED) || netsTransazione.getState().equals(MROldNetsTransazione.STATE_AUTHORIZED_WAIT))){
                        transMap.put("authAmt",netsTransazione.getAmountAuth());
                        authTotal += netsTransazione.getAmountAuth();
                                isAuth = true;
                    } else if(netsTransazione.getState() != null && (netsTransazione.getState().equals(MROldNetsTransazione.STATE_CAPTURE) || netsTransazione.getState().equals(MROldNetsTransazione.STATE_CAPTURE_WAIT))){
                        isAuth = false;
                        List temp = sx.createQuery("select op from MROldNetsOperationCall op where op.transazioneNets.id = :transId").setParameter("transId", netsTransazione.getId()).list();
                        if(temp.size()>0){
                            Iterator itr = temp.iterator();
                            Double authAmt = 0.0;
                            Double captAmt = 0.0;
                            while(itr.hasNext()){
                                MROldNetsOperationCall op = (MROldNetsOperationCall)itr.next();
                                transMap.put("payId",op.getPayId());
                                if(op.getType().equals(MROldNetsTransazione.STATE_AUTHORIZED)){
                                    authAmt += netsTransazione.getAmountAuth();
                                } else if(op.getType().equals(MROldNetsTransazione.STATE_CAPTURE) || op.getType().equals(MROldNetsTransazione.STATE_CAPTURE_WAIT)){
                                    captAmt += op.getAmount();
                                    chargeTotal += op.getAmount();
                                }
                            }
                            transMap.put("authAmt",authAmt);
                            transMap.put("captAmt",captAmt);
                        }
                    } else if(netsTransazione.getState() != null && (netsTransazione.getState().equals(MROldNetsTransazione.STATE_CREDITED) || netsTransazione.getState().equals(MROldNetsTransazione.STATE_CREDITED_WAIT))){
                        isAuth = false;
                        List temp = sx.createQuery("select op from MROldNetsOperationCall op where op.transazioneNets.id = :transId").setParameter("transId", netsTransazione.getId()).list();
                        if(temp.size()>0){
                            Iterator itr = temp.iterator();
                            Double authAmt = 0.0;
                            Double captAmt = 0.0;
                            Double creditAmt = 0.0;
                            while(itr.hasNext()){
                                MROldNetsOperationCall op = (MROldNetsOperationCall)itr.next();
                                transMap.put("payId",op.getPayId());
                                if(op.getType().equals(MROldNetsTransazione.STATE_AUTHORIZED)){
                                    authAmt += netsTransazione.getAmountAuth();
                                } else if(op.getType().equals(MROldNetsTransazione.STATE_CAPTURE) || op.getType().equals(MROldNetsOperationCall.SALE_CALL) || op.getType().equals(MROldNetsTransazione.STATE_CAPTURE_WAIT)){
                                    captAmt += op.getAmount();
                                    chargeTotal += op.getAmount();
                                } else if(op.getType().equals(MROldNetsOperationCall.CREDIT_CALL) || op.getType().equals(MROldNetsOperationCall.CREDIT_CALL_WAIT)){
                                    creditAmt += op.getAmount();
                                    chargeTotal -= op.getAmount();
                                }
                            }
                            transMap.put("authAmt",authAmt);
                            transMap.put("captAmt",captAmt);
                            transMap.put("refundAmt",creditAmt);
                        }
                    } else if (netsTransazione.getState() != null &&  (netsTransazione.getState().equals(MROldNetsTransazione.STATE_ANNULL) || netsTransazione.getState().equals(MROldNetsTransazione.STATE_ANNULL_WAIT))){
                        isAuth = false;
                        transMap.put("authAmt",netsTransazione.getAmountAuth());
                        transMap.put("isAuthRev","Y");
                    }
                }
                if(transMap.get("isAuthRev")=="Y"){
                    continue;
                } else if(Double.parseDouble(transMap.get("authAmt").toString())>0 && Double.parseDouble(transMap.get("captAmt").toString())==0){
                    resMap.put("amount", transMap.get("authAmt"));
                    resMap.put("transId", transMap.get("transId"));
                    resMap.put("payId", transMap.get("payId"));
                    resMap.put("cardNum", transMap.get("cardNum"));
                    resMap.put("cardType", transMap.get("cardType"));
                } else {
                    continue;
                }
                res.add(resMap);
                if(res.size()==1){
                    return res;
                }

            }



            return res;
        }


    }


    public static List getChargeList(String contractId, Session sx){
        List res = new ArrayList();
        boolean vPosAbilitato = Preferenze.getIngenicoAbilitato(sx);

        MROldContrattoNoleggio contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, Integer.parseInt(contractId));
        if (!vPosAbilitato) {
            return res;
        } else if (contrattoNoleggio.getMovimento().getSedeUscita().getMerchantId() == null || contrattoNoleggio.getMovimento().getSedeUscita().getMerchantId().equals("")) {
            return res;
        } else {
            Map resMap = new HashMap();
            List<MROldNetsTransazione> transactionList = (List<MROldNetsTransazione>) sx.createQuery(""
                    + "FROM MROldNetsTransazione t "
                    + "WHERE 1 = 1 "
                    + "AND t.contratto = :contrattoNoleggio and t.shaSign is not null "
                    + "AND t.merchantId is not null and t.merchantId = :merchantId "
                    + "ORDER BY t.startTransactionDate DESC")
                    .setParameter("contrattoNoleggio", contrattoNoleggio)
                    .setParameter("merchantId", contrattoNoleggio.getMovimento().getSedeUscita().getMerchantId())
            .list();
            SimpleDateFormat formatDateTimeSweden = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //DESEncrypter dencrypter = new DESEncrypter("utenti_myrent");
            boolean isAuth = false;
            Iterator itr = transactionList.iterator();
            while(itr.hasNext()){
                MROldNetsTransazione netsTransazione = (MROldNetsTransazione) itr.next();
                Map transMap = new HashMap();
                transMap.put("transId", netsTransazione.getIdTransazione());

                transMap.put("authAmt","0");
                transMap.put("captAmt","0");
                transMap.put("refundAmt","0");
                transMap.put("isAuthRev","N");
                transMap.put("payId","");
                transMap.put("cardNum",netsTransazione.getCardNumber());
                transMap.put("cardType",netsTransazione.getCardType());
                if(netsTransazione.getState() != null && netsTransazione.getState().equals(MROldNetsTransazione.FLEX_CHECKOUT)){
                    transMap.put("isRegister","Y");
                    isAuth = false;
                } else {
                    if (netsTransazione.getState() != null &&  netsTransazione.getState().contains("WAIT")){
                        transMap.put("isWait","Y");
                    }
                    if(netsTransazione.getState() != null && (netsTransazione.getState().equals(MROldNetsTransazione.STATE_SALE) || netsTransazione.getState().equals(MROldNetsTransazione.STATE_SALE_WAIT))){
                        List<MROldNetsOperationCall> temp = sx.createQuery("select op from MROldNetsOperationCall op where op.transazioneNets.id = :transId").setParameter("transId", netsTransazione.getId()).list();
                        if(temp.size()>0){
                            transMap.put("captAmt",temp.get(0).getAmount());
                            transMap.put("payId",temp.get(0).getPayId());
                        }
                        isAuth = false;
                    } else if (netsTransazione.getState() != null && (netsTransazione.getState().equals(MROldNetsTransazione.STATE_AUTHORIZED) || netsTransazione.getState().equals(MROldNetsTransazione.STATE_AUTHORIZED_WAIT))){
                        transMap.put("authAmt",netsTransazione.getAmountAuth());
                                isAuth = true;
                    } else if (netsTransazione.getState() != null && netsTransazione.getState().equals(MROldNetsTransazione.STATE_CAPTURE) || netsTransazione.getState().equals(MROldNetsTransazione.STATE_CAPTURE_WAIT)){
                        isAuth = false;
                        List temp = sx.createQuery("select op from MROldNetsOperationCall op where op.transazioneNets.id = :transId").setParameter("transId", netsTransazione.getId()).list();
                        if(temp.size()>0){
                            Iterator itr1 = temp.iterator();
                            Double authAmt = 0.0;
                            Double captAmt = 0.0;
                            while(itr1.hasNext()){
                                MROldNetsOperationCall op = (MROldNetsOperationCall)itr1.next();
                                transMap.put("payId",op.getPayId());
                                if(op.getType().equals(MROldNetsTransazione.STATE_AUTHORIZED)){
                                    authAmt += netsTransazione.getAmountAuth();
                                } else if(op.getType().equals(MROldNetsTransazione.STATE_CAPTURE) || op.getType().equals(MROldNetsTransazione.STATE_CAPTURE_WAIT)){
                                    captAmt += op.getAmount();
                                }
                            }
                            transMap.put("authAmt",authAmt);
                            transMap.put("captAmt",captAmt);
                        }
                    } else if(netsTransazione.getState() != null && netsTransazione.getState().equals(MROldNetsTransazione.STATE_CREDITED) || netsTransazione.getState().equals(MROldNetsTransazione.STATE_CREDITED_WAIT)){
                        isAuth = false;
                        List temp = sx.createQuery("select op from MROldNetsOperationCall op where op.transazioneNets.id = :transId").setParameter("transId", netsTransazione.getId()).list();
                        if(temp.size()>0){
                            Iterator itr1 = temp.iterator();
                            Double authAmt = 0.0;
                            Double captAmt = 0.0;
                            Double creditAmt = 0.0;
                            while(itr1.hasNext()){
                                MROldNetsOperationCall op = (MROldNetsOperationCall)itr1.next();
                                transMap.put("payId",op.getPayId());
                                if(op.getType().equals(MROldNetsTransazione.STATE_AUTHORIZED)){
                                    authAmt += netsTransazione.getAmountAuth();
                                } else if(op.getType().equals(MROldNetsTransazione.STATE_CAPTURE) || op.getType().equals(MROldNetsOperationCall.SALE_CALL) || op.getType().equals(MROldNetsTransazione.STATE_CAPTURE_WAIT)){
                                    captAmt += op.getAmount();
                                } else if(op.getType().equals(MROldNetsOperationCall.CREDIT_CALL) || op.getType().equals(MROldNetsOperationCall.CREDIT_CALL_WAIT)){
                                    creditAmt += op.getAmount();
                                }
                            }
                            transMap.put("authAmt",authAmt);
                            transMap.put("captAmt",captAmt);
                            transMap.put("refundAmt",creditAmt);
                        }
                    } else if (netsTransazione.getState() != null && (netsTransazione.getState().equals(MROldNetsTransazione.STATE_ANNULL) || netsTransazione.getState().equals(MROldNetsTransazione.STATE_ANNULL_WAIT))){
                        isAuth = false;
                        transMap.put("authAmt",netsTransazione.getAmountAuth());
                        transMap.put("isAuthRev","Y");
                    }
                }

                if(Double.parseDouble(transMap.get("captAmt").toString())>0){
                    Double temp = Double.parseDouble(transMap.get("captAmt").toString()) - Double.parseDouble(transMap.get("refundAmt").toString());
                    resMap.put("amount", temp);
                    resMap.put("transId", transMap.get("transId"));
                    resMap.put("payId", transMap.get("payId"));
                    resMap.put("cardNum", transMap.get("cardNum"));
                    resMap.put("cardType", transMap.get("cardType"));
                } else {
                    continue;
                }
                res.add(resMap);
                if(res.size()==2){
                    return res;
                }

            }
        }



        return res;
    }
}
