package it.myrent.ee.api.utils;

import it.aessepi.myrentcs.utils.FatturazioneFactory;
import it.aessepi.utils.BundleUtils;
import it.myrent.ee.api.factory.DocumentoFiscaleFactory;
import it.myrent.ee.api.factory.ImpostaFactory;
import it.myrent.ee.api.factory.PrimanotaFactory;
import it.myrent.ee.db.*;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

/**
 * Created by Shivangani on 1/24/2018.
 */
public class SplitPaymentUtils {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");

    public static final String SELECT_SUBJECT = bundle.getString("MRRentaAgreement.splitPayment.typeSubject.selectType");
    public static final String CUSTOMER_SUBJECT = bundle.getString("MRRentaAgreement.splitPayment.typeSubject.customer");
    public static final String WORKSHOP__SUBJECT = bundle.getString("MRRentaAgreement.splitPayment.typeSubject.workshop");
    public static final String INSURANCE_SUBJECT = bundle.getString("MRRentaAgreement.splitPayment.typeSubject.insuranceCompany");

    public static final String IMPORTI_ASSOLUTO = bundle.getString("MRRentaAgreement.absolute.label");
    public static final String IMPORTI_PERCENTUALE = bundle.getString("MRRentaAgreement.percentage.label");
    public static final Boolean IMPORTI_ASSOLUTO_BOOLEAN = Boolean.FALSE;
    public static final Boolean IMPORTI_PERCENTUALE_BOOLEAN = Boolean.TRUE;

    public static Map insertNewRowSplitTable(MROldRigaDocumentoFiscale riga, Session sx, int row) {
        //INSERT LABEL TO OPTIONAL CODE
        Map righaMap = new HashMap();
        //righaMap.put("description"+String.valueOf(row), riga.getDescrizione());
        //righaMap.put("amtToUpdate"+String.valueOf(row), riga.getTotaleImponibileRiga());
        //righaMap.put("vatToUpdate"+String.valueOf(row), riga.getTotaleIvaRiga());

        righaMap.put("amtTotal" + String.valueOf(row), MathUtils.round(riga.getTotaleImponibileRiga(), 2));
        righaMap.put("vatTotal" + String.valueOf(row), MathUtils.round(riga.getTotaleIvaRiga(), 2));
        return righaMap;

    }

    public static Map setSubjectsSplitPayment(Session sx, MROldContrattoNoleggio contrattoNoleggio, List<MROldRigaDocumentoFiscale> listaRigheNormali) {

        Map result = new HashMap();
        List<MROldCustomerSplitPayment> listCustomer = contrattoNoleggio.getCustomerSplitPayment();
        if (listCustomer != null && !listCustomer.isEmpty()) {
            int i = 0;
            for (MROldCustomerSplitPayment cust : listCustomer) {
                MROldClienti customer = cust.getCliente();
                String subjectTypeNum = cust.getOrdineCliente().toString();
                String subjectNum = cust.getOrdineCliente().toString();
                if (customer.getTypeOfCustomer() == null) {
                    customer.setTypeOfCustomer(CUSTOMER_SUBJECT);
                    result.put("subType0", CUSTOMER_SUBJECT);
                    sx.save(customer);
                    sx.flush();
                }
                result.put("subType" + subjectTypeNum, customer.getTypeOfCustomer());
                result.put("sub" + subjectNum, customer.getId()); //comboPanelSubject0.initialize(customer);
                if (i == 0) {
                    result.putAll(SplitPaymentUtils.setAmtVatForSubject(cust, listaRigheNormali, true));
                } else {
                    result.putAll(SplitPaymentUtils.setAmtVatForSubject(cust, listaRigheNormali, false));
                }
                i++;
            }

        } else {
            MROldClienti cliente = contrattoNoleggio.getCliente();
            if (cliente.getTypeOfCustomer() == null || cliente.getTypeOfCustomer().equals("")) {
                cliente.setTypeOfCustomer(CUSTOMER_SUBJECT);
                sx.save(cliente);
            }
            result.put("subType0", cliente.getTypeOfCustomer());
            result.put("sub0", cliente.getId());
            MROldCustomerSplitPayment newCustomer = new MROldCustomerSplitPayment();
            newCustomer.setCliente(cliente);
            newCustomer.setContratto(contrattoNoleggio);
            newCustomer.setOrdineCliente(0);
            result.putAll(SplitPaymentUtils.setAmtVatForSubject(newCustomer, listaRigheNormali, true));
            result.put("subType1", SELECT_SUBJECT);
            result.put("sub1", null);
            result.put("subType2", SELECT_SUBJECT);
            result.put("sub2", null);
            result.put("subType4", SELECT_SUBJECT);
            result.put("sub4", null);
            result.put("subType3", SELECT_SUBJECT);
            result.put("sub3", null);
        }
        return result;
    }

    public static Map setSubjectsSplitPayment(Session sx, MROldPrenotazione prenotazione, List<MROldRigaDocumentoFiscale> listaRigheNormali) {

        Map result = new HashMap();
        List<MROldCustomerSplitPayment> listCustomer = prenotazione.getCustomerSplitPayment();
        if (listCustomer != null && !listCustomer.isEmpty()) {
            int i = 0;
            for (MROldCustomerSplitPayment cust : listCustomer) {
                MROldClienti customer = cust.getCliente();
                String subjectTypeNum = cust.getOrdineCliente().toString();
                String subjectNum = cust.getOrdineCliente().toString();
                if (customer.getTypeOfCustomer() == null) {
                    customer.setTypeOfCustomer(CUSTOMER_SUBJECT);
                    result.put("subType0", CUSTOMER_SUBJECT);
                    sx.save(customer);
                    sx.flush();
                }
                result.put("subType" + subjectTypeNum, customer.getTypeOfCustomer());
                result.put("sub" + subjectNum, customer.getId()); //comboPanelSubject0.initialize(customer);
                if (i == 0) {
                    result.putAll(SplitPaymentUtils.setAmtVatForSubject(cust, listaRigheNormali, true));
                } else {
                    result.putAll(SplitPaymentUtils.setAmtVatForSubject(cust, listaRigheNormali, false));
                }
                i++;
            }

        } else {
            MROldClienti cliente = prenotazione.getCliente();
            if (cliente.getTypeOfCustomer() == null || cliente.getTypeOfCustomer().equals("")) {
                cliente.setTypeOfCustomer(CUSTOMER_SUBJECT);
                sx.save(cliente);
            }
            result.put("subType0", cliente.getTypeOfCustomer());
            result.put("sub0", cliente.getId());
            MROldCustomerSplitPayment newCustomer = new MROldCustomerSplitPayment();
            newCustomer.setCliente(cliente);
            newCustomer.setPrenotazione(prenotazione);
            newCustomer.setOrdineCliente(0);
            result.putAll(SplitPaymentUtils.setAmtVatForSubject(newCustomer, listaRigheNormali, true));
            result.put("subType1", SELECT_SUBJECT);
            result.put("sub1", null);
            result.put("subType2", SELECT_SUBJECT);
            result.put("sub2", null);
            result.put("subType4", SELECT_SUBJECT);
            result.put("sub4", null);
            result.put("subType3", SELECT_SUBJECT);
            result.put("sub3", null);
        }
        return result;
    }

    public static Map setAmtVatForSubject(MROldCustomerSplitPayment customer, List<MROldRigaDocumentoFiscale> listaRigheNormali, boolean isFirst) {
        Map<String, ArrayList<Double>> mapRes = new HashMap<String, ArrayList<Double>>();
        ArrayList<Double> listDoublePanelAmountToUpdate = new ArrayList<Double>();
        ArrayList<Double> listDoublePanelVatToUpdate = new ArrayList<Double>();
        if (customer != null && customer.getRigheSplitPayment() != null && !customer.getRigheSplitPayment().isEmpty()) {
            for (int i = 0; i < listaRigheNormali.size(); i++) {
                boolean isFound = false;
                for (MROldRigaDocumentoFiscaleSplitPayment rowsCustomer : customer.getRigheSplitPayment()) {
                    MROldRigaDocumentoFiscale doc = listaRigheNormali.get(i);
                    if (rowsCustomer.getDescrizione().equals(doc.getDescrizione())) {
                        isFound = true;
                        listDoublePanelAmountToUpdate.add(MathUtils.round(rowsCustomer.getTotaleImponibileRiga(), 2));
                        listDoublePanelVatToUpdate.add(MathUtils.round(rowsCustomer.getTotaleIvaRiga(), 2));
                    }
                }
                if (!isFound && isFirst) {
                    listDoublePanelAmountToUpdate.add(MathUtils.round(listaRigheNormali.get(i).getTotaleImponibileRiga(), 2));
                    listDoublePanelVatToUpdate.add(MathUtils.round(listaRigheNormali.get(i).getTotaleIvaRiga(), 2));
                } else if (!isFound) {
                    listDoublePanelAmountToUpdate.add(new Double(0));
                    listDoublePanelVatToUpdate.add(new Double(0));
                }

            }
            mapRes.put(findAmtColForSub(customer.getOrdineCliente()), listDoublePanelAmountToUpdate);
            mapRes.put(findVatColForSub(customer.getOrdineCliente()), listDoublePanelVatToUpdate);
        } else {
            if (customer.getOrdineCliente() != null && customer.getOrdineCliente().equals(new Integer(0))) {
                for (int i = 0; i < listaRigheNormali.size(); i++) {
                    listDoublePanelAmountToUpdate.add(MathUtils.round(listaRigheNormali.get(i).getTotaleImponibileRiga(), 2));
                    listDoublePanelVatToUpdate.add(MathUtils.round(listaRigheNormali.get(i).getTotaleIvaRiga(), 2));
                }
                mapRes.put(findAmtColForSub(customer.getOrdineCliente()), listDoublePanelAmountToUpdate);
                mapRes.put(findVatColForSub(customer.getOrdineCliente()), listDoublePanelVatToUpdate);
            }
        }
        return mapRes;
    }

    public static Map checkTotalValue(Map<Integer, Double> mapDoublePanelAmount, List<Double> listDoublePanelAmountToUpdate,
                                      Map<Integer, Double> mapDoublePanelVat, List<Double> listDoublePanelVatToUpdate) {
        Map resuMap = new HashMap();
        for (Map.Entry<Integer, Double> checkValueVat : mapDoublePanelAmount.entrySet()) {
            Double sumAmount = 0.00;
            Double dpMap = checkValueVat.getValue();
            Double mapValue = dpMap;

            for (Double dp : listDoublePanelAmountToUpdate) {
                Double dpValue = dp;
                if (dp != dpMap && dpValue != null && dpValue != 0.00) {
                    sumAmount += dpValue;
                }
            }

            if (between(sumAmount, mapValue - 0.01, mapValue + 0.01)
                    || between(sumAmount, 99.99, mapValue + 100.01)) { //if (sumAmount.equals(mapValue) || sumAmount.equals(100.00)) {
                resuMap.put("amtTotalColor" + String.valueOf(checkValueVat.getKey()), "Green");
            } else {
                resuMap.put("amtTotalColor" + String.valueOf(checkValueVat.getKey()), "Red");
            }
        }

        for (Map.Entry<Integer, Double> checkValueVat : mapDoublePanelVat.entrySet()) {
            Double sumVat = 0.00;
            Double dpMap = checkValueVat.getValue();
            Double mapValue = dpMap;

            for (Double dp : listDoublePanelVatToUpdate) {
                Double dpValue = dp;
                if (dp != dpMap && dpValue != null && dpValue != 0.00) {
                    sumVat += dpValue;
                }
            }

            if (between(sumVat, mapValue - 0.01, mapValue + 0.01)
                    || between(sumVat, 99.99, mapValue + 100.01)) { //if (sumVat.equals(mapValue) || sumVat.equals(100.00)) {
                resuMap.put("amtVatColor" + String.valueOf(checkValueVat.getKey()), "Green");
            } else {
                resuMap.put("amtVatColor" + String.valueOf(checkValueVat.getKey()), "Red");
            }
        }
        return resuMap;
    }

    public static boolean between(Double i, Double minValueInclusive, Double maxValueInclusive) {
        if (i >= minValueInclusive && i <= maxValueInclusive) {
            return true;
        } else {
            return false;
        }
    }

    public static String findAmtColForSub(Integer ordineClient) {
        String col = null;
        switch (ordineClient) {
            case 0:
                col = "a";
                break;
            case 1:
                col = "c";
                break;
            case 2:
                col = "e";
                break;
            case 3:
                col = "g";
                break;
            case 4:
                col = "i";
                break;
            default:
                System.out.println("Number not exists");
                break;
        }
        return col;
    }

    public static String findVatColForSub(Integer ordineClient) {
        String col = null;
        switch (ordineClient) {
            case 0:
                col = "b";
                break;
            case 1:
                col = "d";
                break;
            case 2:
                col = "f";
                break;
            case 3:
                col = "h";
                break;
            case 4:
                col = "j";
                break;
            default:
                System.out.println("Number not exists");
                break;
        }
        return col;
    }

    public static Map setAmtForSplit(List<Double> amtTotal, List vatTotal, double amount, String splitVatText, String insColAmt, String insColVat, String custAmtCol, String custVat, List<MROldRigaDocumentoFiscale> listaRigheNormali, Boolean isOnlyKm) {

        Map mapRes = new HashMap();
        ArrayList<Double> listAmountIns = new ArrayList<Double>();
        ArrayList<Double> listAmountCust = new ArrayList<Double>();

        int i = 0;
        Iterator itr = amtTotal.iterator();
        while (itr.hasNext()) {
            if (isOnlyKm) {
                Double amt = (Double) itr.next();

                if (listaRigheNormali != null && (listaRigheNormali.get(i).getOptional() != null && listaRigheNormali.get(i).getOptional().getKmExtra())) {
                    listAmountCust.add(MathUtils.round((amt * amount), 2));
                } else {
                    if (listaRigheNormali != null && !(listaRigheNormali.get(i).getTempoExtra() || listaRigheNormali.get(i).getTempoKm())) {
                        listAmountCust.add(MathUtils.round((amt), 2));
                    } else {
                        listAmountCust.add(0.0);
                    }

                }
                if (listaRigheNormali != null && (listaRigheNormali.get(i).getOptional() != null && listaRigheNormali.get(i).getOptional().getKmExtra())) {
                    listAmountIns.add(MathUtils.round((amt * (1 - amount)), 2));
                } else {
                    if (listaRigheNormali != null && !(listaRigheNormali.get(i).getTempoExtra() || listaRigheNormali.get(i).getTempoKm())) {
                        listAmountIns.add(0.0);
                    } else {
                        listAmountIns.add(MathUtils.round((amt), 2));
                    }

                }
                i++;
            } else {
                Double amt = (Double) itr.next();
                if (listaRigheNormali != null && (listaRigheNormali.get(i).getTempoExtra() || listaRigheNormali.get(i).getTempoKm())) {
                    listAmountCust.add(MathUtils.round((amt * amount), 2));
                } else {
                    listAmountCust.add(MathUtils.round((amt), 2));
                }
                if (listaRigheNormali != null && (listaRigheNormali.get(i).getTempoExtra() || listaRigheNormali.get(i).getTempoKm())) {
                    listAmountIns.add(MathUtils.round((amt * (1 - amount)), 2));
                } else {
                    listAmountIns.add(0.0);
                }
                i++;
            }
        }

        List<Double> totalVat = new ArrayList<Double>();
        for (int j = 0; j < listAmountIns.size(); j++) {
            totalVat.add(MathUtils.round(listAmountIns.get(j) + listAmountCust.get(j), 2));
        }

        mapRes.put(insColAmt, listAmountIns);
        mapRes.put(custAmtCol, listAmountCust);
        mapRes.put("totalAmt", totalVat);
//        mapRes.put(insColVat, listVatIns);
//        mapRes.put(custVat, listVatCust);
        return mapRes;
    }

    public static Map setVatForSplit(List<Double> amtTotal, List vatTotal, String splitVatText, String insColAmt, String insColVat,
                                     String custAmtCol, String custVat, List<MROldRigaDocumentoFiscale> listaRigheNormali,
                                     ArrayList<Double> listAmountIns, ArrayList<Double> listAmountCust) {

        Map mapRes = new HashMap();

        ArrayList<Double> listVatIns = new ArrayList<Double>();
        ArrayList<Double> listVatCust = new ArrayList<Double>();

        for (int i = 0; i < listaRigheNormali.size(); i++) {
            MROldRigaDocumentoFiscale rigaNormale = listaRigheNormali.get(i);
            Double vat = 0.0;
            if (rigaNormale.getCodiceIva() != null && rigaNormale.getCodiceIva().getAliquota() != null) {
                vat = rigaNormale.getCodiceIva().getAliquota();
            }

            if (listAmountCust.size() > 0) {
                if (splitVatText.equals("noDis")) {
                    listVatCust.add(MathUtils.round((vat * listAmountCust.get(i)), 2));
                    listVatIns.add(0.0);
                } else if (splitVatText.equals("allVatCust") && listAmountIns.size()>0) {
                    listVatCust.add(MathUtils.round(((vat * listAmountCust.get(i) + vat * listAmountIns.get(i))), 2));
                    listVatIns.add(0.0);

                } else if (splitVatText.equals("eachVatOwn") && listAmountIns.size()>0 ) {
                    Double totVatRow = 0.00;
                    Double halfVatRow = 0.00;
                    boolean isKm = false;
                    if (listaRigheNormali.get(i).getOptional() != null) {
                        //kmrow
                        if (listaRigheNormali.get(i).getOptional().getKmExtra()) {
                            isKm = true;
                        }
                    }
                    if (listaRigheNormali.get(i).getTempoKm() || listaRigheNormali.get(i).getTempoExtra() || isKm) {
                        totVatRow = listaRigheNormali.get(i).getTotaleIvaRiga();
                        halfVatRow = 0.00;
                        if (totVatRow != 0.00) {
                            halfVatRow = MathUtils.roundDouble(totVatRow / 2, 2);
                        }
                        listVatCust.add(MathUtils.round((halfVatRow), 2));
                        listVatIns.add(MathUtils.round((halfVatRow), 2));
                    } else {
                        totVatRow = listaRigheNormali.get(i).getTotaleIvaRiga();
                        listVatCust.add(MathUtils.round((totVatRow), 2));
                        listVatIns.add(0.0);
                    }
//                        listVatCust.add(MathUtils.round((vat * listAmountCust.get(i)), 2));
//                        listVatIns.add(MathUtils.round((vat * listAmountIns.get(i)), 2));
                }
            }
        }
        List<Double> totalVat = new ArrayList<Double>();
        for (int i = 0; i < listVatCust.size(); i++) {
            totalVat.add(MathUtils.round(listVatCust.get(i) + listVatIns.get(i), 2));
        }


//        mapRes.put(insColAmt, listAmountIns);
//        mapRes.put(custAmtCol, listAmountCust);
        mapRes.put(insColVat, listVatIns);
        mapRes.put(custVat, listVatCust);
        mapRes.put("totalVat", totalVat);
        return mapRes;
    }

    public static List saveSplitPay(Session sx, MROldContrattoNoleggio contrattoNoleggio, List splitList, MROldTariffa aTariffa, String splitAmtBtn, String splitVatBtn, String splitPltNum, String splitRefNum) {

        MROldClienti clientiAppoggio = null;
        List<MROldCustomerSplitPayment> listCustomer = new ArrayList<MROldCustomerSplitPayment>();
        if (contrattoNoleggio.getCustomerSplitPayment() == null) {
            contrattoNoleggio.setCustomerSplitPayment(new ArrayList<MROldCustomerSplitPayment>());
        }
        listCustomer = contrattoNoleggio.getCustomerSplitPayment();

        for (int i = 0; i < 5; i++) {
            Map splitMap = (Map) splitList.get(0);
            if (splitMap.containsKey("subType" + i)) {
                String subType = (String) splitMap.get("subType" + i);
                String sub = (String) splitMap.get("sub" + i);
//            String amt = findAmtColForSub(i);
//            String vat = findVatColForSub(i);
//            amt = (String) splitMap.get(amt+i);
//            vat = (String) splitMap.get(vat+i);
                if((!sub.equals("")) && sub !=null && (!sub.equals("null"))){
                    clientiAppoggio = (MROldClienti) sx.get(MROldClienti.class, Integer.parseInt(sub));
                    if (clientiAppoggio != null) {
                        if (subType.equals("0")) {
                            clientiAppoggio.setTypeOfCustomer(MROldClienti.NORMAL);
                        } else if (subType.equals("1")) {
                            clientiAppoggio.setTypeOfCustomer(MROldClienti.WORKSHOP);
                        } else if (subType.equals("2")) {
                            clientiAppoggio.setTypeOfCustomer(MROldClienti.INSURANCE_COMPANY);
                        }

                        SplitPaymentUtils.addCustomerToList(clientiAppoggio, i, listCustomer, contrattoNoleggio);
                        SplitPaymentUtils.cancellaRigheDaCustomer(sx, listCustomer.get(i));
                        listCustomer.get(i).setRigheSplitPayment(new ArrayList<MROldRigaDocumentoFiscaleSplitPayment>());
                    }
                }

            }
        }
        List<MROldRigaDocumentoFiscale> listaRigheNormali = new ArrayList<MROldRigaDocumentoFiscale>();
        if (listCustomer != null || listCustomer.size() > 0) {
            for (MROldCustomerSplitPayment customer : listCustomer) {
                sx.saveOrUpdate(customer);
            }
            contrattoNoleggio.setCustomerSplitPayment(listCustomer);
            sx.saveOrUpdate(contrattoNoleggio);
            sx.flush();

            Integer aGiorni = contrattoNoleggio.getCommissione().getGiorniVoucher();
            Date inizio = contrattoNoleggio.getInizio();
            Date fine = contrattoNoleggio.getFine();
            Double aSconto = contrattoNoleggio.getScontoTariffa();

            if (aTariffa != null && inizio != null && fine != null) {
                listaRigheNormali = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                        sx,
                        contrattoNoleggio,
                        aTariffa,
                        inizio,
                        fine,
                        contrattoNoleggio.getMovimento().getKmFine(),
                        contrattoNoleggio.getMovimento().getCombustibileFine(),
                        contrattoNoleggio.getMovimento().getVeicolo().getCarburante(),
                        aSconto,
                        aGiorni)
                        .calculateRowsSplitPayment(sx); //.calcolaRigheProssimaFattura(sx);
            }
            for (int i = 0; i < listaRigheNormali.size(); i++) {
                MROldRigaDocumentoFiscale rigaNormale = listaRigheNormali.get(i);
                Map splitMap = new HashMap();
                try{
                    splitMap = (Map) splitList.get(i);
                } catch (Exception ex) {
                    if(i!=0){
                        splitMap = (Map) splitList.get(0);
                        splitMap.put("amta"+i, "0");
                        splitMap.put("vatb"+i, "0");
                        splitMap.put("amtc"+i, "0");
                        splitMap.put("vatd"+i, "0");
                        splitMap.put("amte"+i, "0");
                        splitMap.put("vatf"+i, "0");
                        splitMap.put("amtg"+i, "0");
                        splitMap.put("vath"+i, "0");
                        splitMap.put("amti"+i, "0");
                        splitMap.put("vatj"+i, "0");
                    } else {
                        break;
                    }

                }

                //splitMap = (Map) splitList.get(i);
                String amt = findAmtColForSub(0);
                String vat = findVatColForSub(0);
                String amount = (String) splitMap.get("amt" + amt + i);
                String iva = (String) splitMap.get("vat" + vat + i);
                addRowDocumentoFiscaleToCustomerSplitPayment(sx, rigaNormale, listCustomer.get(0), Double.parseDouble((String) splitMap.get("amt" + amt + i)), Double.parseDouble((String) splitMap.get("vat" + vat + i)));
                if (listCustomer.size() > 1 && listCustomer.get(1) != null) {
                    amt = findAmtColForSub(1);
                    vat = findVatColForSub(1);
                    addRowDocumentoFiscaleToCustomerSplitPayment(sx, rigaNormale, listCustomer.get(1), Double.parseDouble((String) splitMap.get("amt" + amt + i)), Double.parseDouble((String) splitMap.get("vat" + vat + i)));
                    if (listCustomer.size() > 2 && listCustomer.get(2) != null) {
                        amt = findAmtColForSub(2);
                        vat = findVatColForSub(2);
                        addRowDocumentoFiscaleToCustomerSplitPayment(sx, rigaNormale, listCustomer.get(2), Double.parseDouble((String) splitMap.get("amt" + amt + i)), Double.parseDouble((String) splitMap.get("vat" + vat + i)));
                        if (listCustomer.size() > 3 && listCustomer.get(3) != null) {
                            amt = findAmtColForSub(3);
                            vat = findVatColForSub(3);
                            addRowDocumentoFiscaleToCustomerSplitPayment(sx, rigaNormale, listCustomer.get(3), Double.parseDouble((String) splitMap.get("amt" + amt + i)), Double.parseDouble((String) splitMap.get("vat" + vat + i)));
                            if (listCustomer.size() > 4 && listCustomer.get(4) != null) {
                                amt = findAmtColForSub(4);
                                vat = findVatColForSub(4);
                                addRowDocumentoFiscaleToCustomerSplitPayment(sx, rigaNormale, listCustomer.get(4), Double.parseDouble((String) splitMap.get("amt" + amt + i)), Double.parseDouble((String) splitMap.get("vat" + vat + i)));
                            }
                        }
                    }
                }
            }
            if (splitAmtBtn != "" && splitAmtBtn != null) {
                contrattoNoleggio.setSplitPaymentAmount(splitAmtBtn);
            }
            if (splitVatBtn != "" && splitVatBtn != null) {
                contrattoNoleggio.setSplitPaymentVat(splitVatBtn);
            }
            if (splitPltNum != "" && splitPltNum != null) {
                contrattoNoleggio.setSplitPaymentPlateNumber(splitPltNum);
            }
            if (splitRefNum != "" && splitRefNum != null) {
                contrattoNoleggio.setSplitPaymentInsRefNr(splitRefNum);
            }

            sx.saveOrUpdate(contrattoNoleggio);
            sx.flush();
        }
        return listaRigheNormali;
    }

    public static List saveSplitPay(Session sx, MROldPrenotazione prenotazione, List splitList, MROldTariffa aTariffa, String splitAmtBtn, String splitVatBtn, String splitPltNum, String splitRefNum) {

        MROldClienti clientiAppoggio = null;
        List<MROldCustomerSplitPayment> listCustomer = new ArrayList<MROldCustomerSplitPayment>();
        prenotazione.setSplitPayment(Boolean.TRUE);
        if (prenotazione.getCustomerSplitPayment() == null) {
            prenotazione.setCustomerSplitPayment(new ArrayList<MROldCustomerSplitPayment>());
        }
        listCustomer = prenotazione.getCustomerSplitPayment();

        for (int i = 0; i < 5; i++) {
            Map splitMap = (Map) splitList.get(0);
            if (splitMap.containsKey("subType" + i)) {
                String subType = (String) splitMap.get("subType" + i);
                String sub = (String) splitMap.get("sub" + i);
//            String amt = findAmtColForSub(i);
//            String vat = findVatColForSub(i);
//            amt = (String) splitMap.get(amt+i);
//            vat = (String) splitMap.get(vat+i);
                if((!sub.equals("")) && sub !=null && (!sub.equals("null"))){
                    clientiAppoggio = (MROldClienti) sx.get(MROldClienti.class, Integer.parseInt(sub));
                    if (clientiAppoggio != null) {
                        if (subType.equals("0")) {
                            clientiAppoggio.setTypeOfCustomer(MROldClienti.NORMAL);
                        } else if (subType.equals("1")) {
                            clientiAppoggio.setTypeOfCustomer(MROldClienti.WORKSHOP);
                        } else if (subType.equals("2")) {
                            clientiAppoggio.setTypeOfCustomer(MROldClienti.INSURANCE_COMPANY);
                        }

                        SplitPaymentUtils.addCustomerToList(clientiAppoggio, i, listCustomer, prenotazione);
                        SplitPaymentUtils.cancellaRigheDaCustomer(sx, listCustomer.get(i));
                        listCustomer.get(i).setRigheSplitPayment(new ArrayList<MROldRigaDocumentoFiscaleSplitPayment>());
                    }
                }

            }
        }
        List<MROldRigaDocumentoFiscale> listaRigheNormali = new ArrayList<MROldRigaDocumentoFiscale>();
        if (listCustomer != null || listCustomer.size() > 0) {
            for (MROldCustomerSplitPayment customer : listCustomer) {
                sx.saveOrUpdate(customer);
            }
            prenotazione.setCustomerSplitPayment(listCustomer);
            sx.saveOrUpdate(prenotazione);
            sx.flush();

            Integer aGiorni = prenotazione.getCommissione().getGiorniVoucher();
            Date inizio = prenotazione.getInizio();
            Date fine = prenotazione.getFine();
            Double aSconto = prenotazione.getScontoPercentuale();

            if (aTariffa != null && inizio != null && fine != null) {
                listaRigheNormali = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                        sx,
                        prenotazione,
                        aTariffa,
                        inizio,
                        fine,
                        (prenotazione.getMovimento()!=null)?prenotazione.getMovimento().getKmFine():null,
                        (prenotazione.getMovimento()!=null)?prenotazione.getMovimento().getCombustibileFine():null,
                        (prenotazione.getMovimento()!=null)?prenotazione.getMovimento().getVeicolo().getCarburante():null,
                        aSconto,
                        aGiorni)
                        .calculateRowsSplitPayment(sx); //.calcolaRigheProssimaFattura(sx);
            }
            for (int i = 0; i < listaRigheNormali.size(); i++) {
                MROldRigaDocumentoFiscale rigaNormale = listaRigheNormali.get(i);
                Map splitMap = new HashMap();
                try{
                    splitMap = (Map) splitList.get(i);
                } catch (Exception ex) {
                    if(i!=0){
                        splitMap = (Map) splitList.get(0);
                        splitMap.put("amta"+i, "0");
                        splitMap.put("vatb"+i, "0");
                        splitMap.put("amtc"+i, "0");
                        splitMap.put("vatd"+i, "0");
                        splitMap.put("amte"+i, "0");
                        splitMap.put("vatf"+i, "0");
                        splitMap.put("amtg"+i, "0");
                        splitMap.put("vath"+i, "0");
                        splitMap.put("amti"+i, "0");
                        splitMap.put("vatj"+i, "0");
                    } else {
                        break;
                    }

                }
                String amt = findAmtColForSub(0);
                String vat = findVatColForSub(0);
                String amount = (String) splitMap.get("amt" + amt + i);
                String iva = (String) splitMap.get("vat" + vat + i);
                addRowDocumentoFiscaleToCustomerSplitPayment(sx, rigaNormale, listCustomer.get(0), Double.parseDouble((String) splitMap.get("amt" + amt + i)), Double.parseDouble((String) splitMap.get("vat" + vat + i)));
                if (listCustomer.size() > 1 && listCustomer.get(1) != null) {
                    amt = findAmtColForSub(1);
                    vat = findVatColForSub(1);
                    addRowDocumentoFiscaleToCustomerSplitPayment(sx, rigaNormale, listCustomer.get(1), Double.parseDouble((String) splitMap.get("amt" + amt + i)), Double.parseDouble((String) splitMap.get("vat" + vat + i)));
                    if (listCustomer.size() > 2 && listCustomer.get(2) != null) {
                        amt = findAmtColForSub(2);
                        vat = findVatColForSub(2);
                        addRowDocumentoFiscaleToCustomerSplitPayment(sx, rigaNormale, listCustomer.get(2), Double.parseDouble((String) splitMap.get("amt" + amt + i)), Double.parseDouble((String) splitMap.get("vat" + vat + i)));
                        if (listCustomer.size() > 3 && listCustomer.get(3) != null) {
                            amt = findAmtColForSub(3);
                            vat = findVatColForSub(3);
                            addRowDocumentoFiscaleToCustomerSplitPayment(sx, rigaNormale, listCustomer.get(3), Double.parseDouble((String) splitMap.get("amt" + amt + i)), Double.parseDouble((String) splitMap.get("vat" + vat + i)));
                            if (listCustomer.size() > 4 && listCustomer.get(4) != null) {
                                amt = findAmtColForSub(4);
                                vat = findVatColForSub(4);
                                addRowDocumentoFiscaleToCustomerSplitPayment(sx, rigaNormale, listCustomer.get(4), Double.parseDouble((String) splitMap.get("amt" + amt + i)), Double.parseDouble((String) splitMap.get("vat" + vat + i)));
                            }
                        }
                    }
                }
            }
            if (splitAmtBtn != "" && splitAmtBtn != null) {
                prenotazione.setSplitPaymentAmount(splitAmtBtn);
            }
            if (splitVatBtn != "" && splitVatBtn != null) {
                prenotazione.setSplitPaymentVat(splitVatBtn);
            }
            if (splitPltNum != "" && splitPltNum != null) {
                prenotazione.setSplitPaymentPlateNumber(splitPltNum);
            }
            if (splitRefNum != "" && splitRefNum != null) {
                prenotazione.setSplitPaymentInsRefNr(splitRefNum);
            }

            sx.saveOrUpdate(prenotazione);
            sx.flush();
        }
        return listaRigheNormali;
    }

    public static void addCustomerToList(MROldClienti cliente, int order, List<MROldCustomerSplitPayment> listCustomer, MROldContrattoNoleggio contratto) { //Session sx, Clienti cliente, int order
        if (cliente != null) {
            try {
                MROldCustomerSplitPayment customerSplitAppoggio;

                if (listCustomer != null && listCustomer.size() > order) {
                    customerSplitAppoggio = listCustomer.get(order);
                    customerSplitAppoggio.setContratto(contratto);
                    customerSplitAppoggio.setOrdineCliente(order);
                    customerSplitAppoggio.setCliente(cliente);
                } else {
                    customerSplitAppoggio = new MROldCustomerSplitPayment();
                    customerSplitAppoggio.setContratto(contratto);
                    customerSplitAppoggio.setOrdineCliente(order);
                    customerSplitAppoggio.setCliente(cliente);
                    listCustomer.add(customerSplitAppoggio);
                }

                //listCustomer.add(customerSplitAppoggio);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void addCustomerToList(MROldClienti cliente, int order, List<MROldCustomerSplitPayment> listCustomer, MROldPrenotazione prenotazione) { //Session sx, Clienti cliente, int order
        if (cliente != null) {
            try {
                MROldCustomerSplitPayment customerSplitAppoggio;

                if (listCustomer != null && listCustomer.size() > order) {
                    customerSplitAppoggio = listCustomer.get(order);
                    customerSplitAppoggio.setPrenotazione(prenotazione);
                    customerSplitAppoggio.setOrdineCliente(order);
                    customerSplitAppoggio.setCliente(cliente);
                } else {
                    customerSplitAppoggio = new MROldCustomerSplitPayment();
                    customerSplitAppoggio.setPrenotazione(prenotazione);
                    customerSplitAppoggio.setOrdineCliente(order);
                    customerSplitAppoggio.setCliente(cliente);
                    listCustomer.add(customerSplitAppoggio);
                }

                //listCustomer.add(customerSplitAppoggio);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void cancellaRigheDaCustomer(Session sx, MROldCustomerSplitPayment customer) {
        if (customer.getRigheSplitPayment() != null) {
            for (MROldRigaDocumentoFiscaleSplitPayment rigaSplit : customer.getRigheSplitPayment()) {
                if (rigaSplit.getId() != null) {
                    sx.delete(rigaSplit);
                }
            }
        }
    }

    public static MROldDocumentoFiscale createInvoiceForCustomer(Session sx, String splitAmountType, String splitVatType, List<MROldRigaDocumentoFiscale> listaRighe, MROldAffiliato affiliato, MROldNumerazione numerazione, String prefisso, Date data, Integer anno, User aUser, MROldCustomerSplitPayment cust, MROldSede sedeOperativa, MROldContrattoNoleggio contratto) {

        MROldCodiciIva codiceIvaCustomerInsurance = null;
        if (splitVatType.equals("No distribution")) {
            if (contratto.getTariffa() != null) {
                codiceIvaCustomerInsurance = contratto.getTariffa().getCodiceIva();
            }
        } else if (splitVatType.equals("All VAT to customer")) {
            Query query = sx.createQuery(
                    "select x from MROldCodiciIva x where "
                            + //NOI18N
                            "x.codice = :codice ");
            query.setParameter("codice", "0"); //NOI18N
            codiceIvaCustomerInsurance = (MROldCodiciIva) query.list().get(0);
        } else if (splitVatType.equals("Each party pays their own VAT")) {
            if (splitAmountType.equals("12,5%")) {
                Query query = sx.createQuery(
                        "select x from MROldCodiciIva x where "
                                + //NOI18N
                                "x.codice = :codice ");
                query.setParameter("codice", "14"); //NOI18N
                codiceIvaCustomerInsurance = (MROldCodiciIva) query.list().get(0);
            } else if (splitAmountType.equals("1/3")) {
                Query query = sx.createQuery(
                        "select x from MROldCodiciIva x where "
                                + //NOI18N
                                "x.codice = :codice ");
                query.setParameter("codice", "18"); //NOI18N
                codiceIvaCustomerInsurance = (MROldCodiciIva) query.list().get(0);
            } else if (splitAmountType.equals("25%")) {
                Query query = sx.createQuery(
                        "select x from MROldCodiciIva x where "
                                + //NOI18N
                                "x.codice = :codice ");
                query.setParameter("codice", "16"); //NOI18N
                codiceIvaCustomerInsurance = (MROldCodiciIva) query.list().get(0);
            }

        }
        MROldPianoDeiConti sottoContoNoleggio = ContabUtils.leggiSottoconto(sx, new Integer(4), new Integer(10), new Integer(6));
        MROldDocumentoFiscale newFattura = null;
        try {
            Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
            newFattura = MROldDocumentoFiscale.creaDocumentoFiscale(
                    affiliato,
                    aUser,
                    MROldDocumentoFiscale.FT,
                    numerazione,
                    prefisso,
                    numero,
                    data,
                    anno);
            newFattura.setContratto((MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contratto.getId()));
            newFattura.setCliente(cust.getCliente());
            newFattura.setPagamento(contratto.getPagamento());
            newFattura.setAnnotazioni(contratto.getNote());
            if (newFattura.getFatturaRighe() != null && !newFattura.getFatturaRighe().isEmpty()) {
                newFattura.getFatturaRighe().clear();
            } else if (newFattura.getFatturaRighe() == null) {
                newFattura.setFatturaRighe(new ArrayList());
            }
            //METODO PRECEDENTE
            /*for (RigaDocumentoFiscaleSplitPayment rigaSplit : cust.getRigheSplitPayment()) {
                RigaDocumentoFiscale rigaNormal = FatturaUtils.trasformaRigaDocumentoSplitPaymentInNormale(rigaSplit);
                newFattura.getFatturaRighe().add(rigaNormal);
            }*/

            //NUOVO METODO
            /*
            if subject is customer, row from calcolaRighe
             */
            if (cust.getCliente() != null && cust.getCliente().getTypeOfCustomer().equals(MROldClienti.NORMAL)) {
                //check if there is a workshop or insurance company
                //check if there is a workshop
                boolean thereIsWorkshop = false;
                List<MROldCustomerSplitPayment> listCustomerSplit = contratto.getCustomerSplitPayment();
                for (MROldCustomerSplitPayment tempCustomerWorkshop : listCustomerSplit) {
                    if (tempCustomerWorkshop.getCliente().getTypeOfCustomer().equals(MROldClienti.WORKSHOP)) {
                        thereIsWorkshop = true;
                    }
                }
                if (thereIsWorkshop) {
                    //if customer and workshop
                    for (MROldRigaDocumentoFiscaleSplitPayment rigaSplit : cust.getRigheSplitPayment()) {
                        MROldRigaDocumentoFiscale rigaNormal = FatturaUtils.trasformaRigaDocumentoSplitPaymentInNormale(rigaSplit);
                        newFattura.getFatturaRighe().add(rigaNormal);
                    }
                } else {
                    //if customer and insurance company
                    for (MROldRigaDocumentoFiscale rowToAdd : listaRighe) {
                        newFattura.getFatturaRighe().add(rowToAdd);
                    }
                }
                for (MROldCustomerSplitPayment tempCustomer : listCustomerSplit) {
                    if (tempCustomer.getCliente().getTypeOfCustomer().equals(MROldClienti.INSURANCE_COMPANY)) {
                        //fetch the row split payment, calculate total amount, inser the correct CodiceIva, quantty from tkm
                        List<MROldRigaDocumentoFiscaleSplitPayment> insuranceCompanyRows = tempCustomer.getRigheSplitPayment();
                        Double totAmountInsuranceComany = 0.00;
                        Double quantity = 1.00;
                        if (insuranceCompanyRows != null) {
                            for (MROldRigaDocumentoFiscaleSplitPayment insuranceCompanyRow : insuranceCompanyRows) {
                                totAmountInsuranceComany = totAmountInsuranceComany + insuranceCompanyRow.getTotaleRiga();
                            }
                        }
                        totAmountInsuranceComany = totAmountInsuranceComany - (totAmountInsuranceComany * 2);
                        totAmountInsuranceComany = MathUtils.round(totAmountInsuranceComany, 2);


//                        MROldRigaDocumentoFiscale rigaRiferimento = listaRighe.get(0);
//                        MROldRigaDocumentoFiscaleSplitPayment rifaRiferimentoSplit = insuranceCompanyRows.get(0);
                        /*
                        prendere tutti i dati da quella di riferimento vedend i set che fa
                         */
                        MROldRigaDocumentoFiscale riga = null;
                        double imponibile = 0.0, iva = 0.0, aliquota = 0.0, totale = 0.0;
                        //Codice IVA insurance company, first case is the same of the customer in second case is 0
                        double prezzo = MathUtils.round(totAmountInsuranceComany, 5);
                        imponibile = MathUtils.round(1 * prezzo, 5);
                        aliquota = codiceIvaCustomerInsurance.getAliquota().doubleValue();
                        iva = MathUtils.round(imponibile * aliquota, 5);
                        totale = imponibile + iva;
                        riga = new MROldRigaDocumentoFiscale();
                        riga.setDescrizione("Paid by insurance");
                        riga.setUnitaMisura(null);
                        riga.setQuantita(quantity);
                        riga.setPrezzoUnitario(new Double(prezzo));
                        riga.setSconto(new Double(0));
                        riga.setScontoFisso(new Double(0));
                        riga.setTotaleImponibileRiga(new Double(imponibile));
                        riga.setTotaleIvaRiga(new Double(iva));
                        riga.setCodiceIva(codiceIvaCustomerInsurance);
                        //riga.setCodiceSottoconto(rigaRiferimento.getCodiceSottoconto());
                        riga.setCodiceSottoconto(sottoContoNoleggio);
                        riga.setTotaleRiga(new Double(totale));
                        riga.setTempoKm(Boolean.FALSE);
                        riga.setTempoExtra(Boolean.FALSE);
                        riga.setFranchigia(Boolean.FALSE);
                        newFattura.getFatturaRighe().add(riga);
                        if (splitVatType == "Each party pays their own VAT") {
                            MROldRigaDocumentoFiscale rigaArrotondamento = null;
                            MROldCodiciIva codiciIvaArrotondamento = null;
                            double imponibileArrotondamento = 0.0, ivaArrotondamento = 0.0, aliquotaArrotondamento = 0.0, totaleArrotondamento = 0.0;
                            /*
                            calculate check amount
                            */
                            Double totAmount = 0.00;
                            Double totAmountCustomer = 0.00;
                            Double totAmountInsurance = 0.00;
                            Double totAmountCheck = 0.00;
                            for (MROldRigaDocumentoFiscale singleRow : listaRighe) {
                                totAmount = totAmount + singleRow.getTotaleRiga();
                            }
                            totAmountCustomer = MathUtils.round(totAmount + totale, 2);
                            totAmountInsurance = MathUtils.round(totAmount - totAmountCustomer, 2);
                            totAmountCheck = MathUtils.round(totAmount - totAmountInsurance - totAmountCustomer, 2);
                            double prezzoArrotondamento = MathUtils.round(totAmountCheck, 2);
                            imponibileArrotondamento = MathUtils.round(1 * prezzoArrotondamento, 2);
                            Query query = sx.createQuery(
                                    "select x from MROldCodiciIva x where "
                                            + //NOI18N
                                            "x.codice = :codice ");
                            query.setParameter("codice", "0"); //NOI18N
                            codiciIvaArrotondamento = (MROldCodiciIva) query.list().get(0);
                            aliquotaArrotondamento = codiciIvaArrotondamento.getAliquota().doubleValue();
                            ivaArrotondamento = MathUtils.round(imponibileArrotondamento * aliquotaArrotondamento, 2);
                            totaleArrotondamento = imponibileArrotondamento + ivaArrotondamento;
                            rigaArrotondamento = new MROldRigaDocumentoFiscale();
                            rigaArrotondamento.setDescrizione("Check amount row");
                            rigaArrotondamento.setUnitaMisura(null);
                            rigaArrotondamento.setQuantita(quantity);
                            rigaArrotondamento.setPrezzoUnitario(new Double(prezzoArrotondamento));
                            rigaArrotondamento.setSconto(new Double(0));
                            rigaArrotondamento.setScontoFisso(new Double(0));
                            rigaArrotondamento.setTotaleImponibileRiga(new Double(imponibileArrotondamento));
                            rigaArrotondamento.setTotaleIvaRiga(new Double(ivaArrotondamento));
                            /*
                            the previous alquota
                            */
                            rigaArrotondamento.setCodiceIva(codiciIvaArrotondamento);
                            //riga.setCodiceSottoconto(rigaRiferimento.getCodiceSottoconto());
                            rigaArrotondamento.setCodiceSottoconto(sottoContoNoleggio);
                            rigaArrotondamento.setTotaleRiga(new Double(totaleArrotondamento));
                            rigaArrotondamento.setTempoKm(Boolean.FALSE);
                            rigaArrotondamento.setTempoExtra(Boolean.FALSE);
                            rigaArrotondamento.setFranchigia(Boolean.FALSE);
                            newFattura.getFatturaRighe().add(rigaArrotondamento);
                        }
                    }
                }
            }
            if (cust.getCliente() != null && cust.getCliente().getTypeOfCustomer().equals(MROldClienti.INSURANCE_COMPANY)) {
                //ok if only customer, first case customer and insurance company
                MROldCodiciIva codiceIva = null;
                if (splitVatType.equals("Each party pays their own VAT")) {
                    for (MROldRigaDocumentoFiscale rowToAdd : listaRighe) {
                        newFattura.getFatturaRighe().add(rowToAdd);
                    }
                    if (splitAmountType.equals("12,5%")) {
                        Query query = sx.createQuery(
                                "select x from MROldCodiciIva x where "
                                        + //NOI18N
                                        "x.codice = :codice ");
                        query.setParameter("codice", "100"); //NOI18N
                        codiceIva = (MROldCodiciIva) query.list().get(0);
                    } else if (splitAmountType.equals("1/3")) {
                        Query query = sx.createQuery(
                                "select x from MROldCodiciIva x where "
                                        + //NOI18N
                                        "x.codice = :codice ");
                        query.setParameter("codice", "375"); //NOI18N
                        codiceIva = (MROldCodiciIva) query.list().get(0);
                    } else if (splitAmountType.equals("25%")) {
                        Query query = sx.createQuery(
                                "select x from MROldCodiciIva x where "
                                        + //NOI18N
                                        "x.codice = :codice ");
                        query.setParameter("codice", "50"); //NOI18N
                        codiceIva = (MROldCodiciIva) query.list().get(0);
                    }
                } else {
                    //ok if only customer, first case customer and insurance company
                    for (MROldRigaDocumentoFiscale rowToAdd : listaRighe) {
                        Query query = sx.createQuery(
                                "select x from MROldCodiciIva x where "
                                        + //NOI18N
                                        "x.codice = :codice ");
                        query.setParameter("codice", "0"); //NOI18N
                        codiceIva = (MROldCodiciIva) query.list().get(0);
                        //normal row should be with amount without VAT and IVA 0
                        Double totAmountNoVAT = rowToAdd.getTotaleImponibileRiga();
                        rowToAdd.setTotaleIvaRiga(0.00);
                        rowToAdd.setCodiceIva(codiceIva);
                        rowToAdd.setTotaleRiga(totAmountNoVAT);
                        newFattura.getFatturaRighe().add(rowToAdd);
                    }
                }
                //check if there is an insurance company
                List<MROldCustomerSplitPayment> listCustomerSplit = contratto.getCustomerSplitPayment();
                for (MROldCustomerSplitPayment tempCustomer : listCustomerSplit) {
                    if (tempCustomer.getCliente().getTypeOfCustomer().equals(MROldClienti.NORMAL)) {
                        //fetch the row split payment, calculate total amount, inser the correct CodiceIva, quantty from tkm
                        List<MROldRigaDocumentoFiscaleSplitPayment> normalCustomerRows = tempCustomer.getRigheSplitPayment();
                        Double totAmountNormalCustomer = 0.00;
                        Double quantity = 1.00;

                        /*
                        Build row Paid by Customer with only tmk rows and VAT
                        */
                        if (normalCustomerRows != null) {
                            for (MROldRigaDocumentoFiscaleSplitPayment normalCustomerRow : normalCustomerRows) {
                                if (normalCustomerRow.getTempoExtra() || normalCustomerRow.getTempoKm() || (normalCustomerRow.getOptional() != null && normalCustomerRow.getOptional().getKmExtra())) {
                                    totAmountNormalCustomer = totAmountNormalCustomer + normalCustomerRow.getTotaleImponibileRiga();
                                }
                            }
                        }
                        totAmountNormalCustomer = totAmountNormalCustomer - (totAmountNormalCustomer * 2);
                        /*
                        prendere tutti i dati da quella di riferimento vedend i set che fa
                         */
                        MROldRigaDocumentoFiscale riga = null;
                        double imponibile = 0.0, iva = 0.0, aliquota = 0.0, totale = 0.0;
                        double prezzo = MathUtils.round(totAmountNormalCustomer, 5);
                        imponibile = MathUtils.round(1 * prezzo, 5);
                        aliquota = codiceIva.getAliquota().doubleValue();
                        iva = MathUtils.round(imponibile * aliquota, 5);
                        totale = imponibile + iva;
                        riga = new MROldRigaDocumentoFiscale();
                        riga.setDescrizione("Paid by Customer");
                        riga.setUnitaMisura(null);
                        riga.setQuantita(quantity);
                        riga.setPrezzoUnitario(new Double(prezzo));
                        riga.setSconto(new Double(0));
                        riga.setScontoFisso(new Double(0));
                        riga.setTotaleImponibileRiga(new Double(imponibile));
                        riga.setTotaleIvaRiga(new Double(iva));
                        riga.setCodiceIva(codiceIva);
                        //riga.setCodiceSottoconto(rigaRiferimento.getCodiceSottoconto());
                        riga.setCodiceSottoconto(sottoContoNoleggio);
                        riga.setTotaleRiga(new Double(totale));
                        riga.setTempoKm(Boolean.FALSE);
                        riga.setTempoExtra(Boolean.FALSE);
                        riga.setFranchigia(Boolean.FALSE);
                        newFattura.getFatturaRighe().add(riga);


                        MROldCodiciIva codiceIva2 = contratto.getTariffa().getCodiceIva();
                        List<MROldRigaDocumentoFiscaleSplitPayment> normalCustomerRows2 = tempCustomer.getRigheSplitPayment();
                        Double totAmountNormalCustomerOptional = 0.00;
                        Double quantityOpt = 1.00;
                        /*
                        Build row Paid by Customer with only optional and normal VAT
                        */
                        if (normalCustomerRows2 != null) {
                            for (MROldRigaDocumentoFiscaleSplitPayment normalCustomerRow2 : normalCustomerRows2) {
                                if (!normalCustomerRow2.getTempoExtra() && !normalCustomerRow2.getTempoKm()) {
                                    if (normalCustomerRow2.getOptional() != null && !normalCustomerRow2.getOptional().getKmExtra()) {
                                        totAmountNormalCustomerOptional = totAmountNormalCustomerOptional + normalCustomerRow2.getTotaleImponibileRiga();
                                    }
                                }
                            }
                        }
                        totAmountNormalCustomerOptional = totAmountNormalCustomerOptional - (totAmountNormalCustomerOptional * 2);
                        /*
                        prendere tutti i dati da quella di riferimento vedend i set che fa
                         */
                        MROldRigaDocumentoFiscale riga2 = null;
                        double imponibile2 = 0.0, iva2 = 0.0, aliquota2 = 0.0, totale2 = 0.0;
                        double prezzo2 = MathUtils.round(totAmountNormalCustomerOptional, 2);
                        imponibile2 = MathUtils.round(1 * prezzo2, 2);
                        aliquota2 = codiceIva2.getAliquota().doubleValue();
                        iva2 = MathUtils.round(imponibile2 * aliquota2, 2);
                        totale2 = imponibile2 + iva2;
                        riga2 = new MROldRigaDocumentoFiscale();
                        riga2.setDescrizione("Paid by Customer");
                        riga2.setUnitaMisura(null);
                        riga2.setQuantita(quantityOpt);
                        riga2.setPrezzoUnitario(new Double(prezzo2));
                        riga2.setSconto(new Double(0));
                        riga2.setScontoFisso(new Double(0));
                        riga2.setTotaleImponibileRiga(new Double(imponibile2));
                        riga2.setTotaleIvaRiga(new Double(iva2));
                        riga2.setCodiceIva(codiceIva2);
                        //riga.setCodiceSottoconto(rigaRiferimento.getCodiceSottoconto());
                        riga2.setCodiceSottoconto(sottoContoNoleggio);
                        riga2.setTotaleRiga(new Double(totale2));
                        riga2.setTempoKm(Boolean.FALSE);
                        riga2.setTempoExtra(Boolean.FALSE);
                        riga2.setFranchigia(Boolean.FALSE);
                        newFattura.getFatturaRighe().add(riga2);
                    }
                }
            }
            ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
            newFattura.setFatturaImposte(imposteFattura.getImposte());
            newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
            newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
            newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
            newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
            newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
            if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
                ScadenzaUtils.creaScadenze(newFattura);
            }
            newFattura.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contratto));
            newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));
            //???????????????????????
            //???????????????????????
            //????????????????????????
            PrimanotaFactory.newPrimanota(sx, newFattura, sedeOperativa, null, null, aUser);

            sx.save(newFattura.getPrimanota());
            sx.save(newFattura);
            newFattura = (MROldDocumentoFiscale)DocumentoFiscaleFactory.checkNumerazioneDoppelGanger(sx, "MROldDocumentoFiscale", newFattura, newFattura.getNumerazione(), newFattura.getAnno(), newFattura.getTipoDocumento());
            System.out.println("Fattura per " + cust.getCliente().getTypeOfCustomer() + " creata: " + newFattura.getPrefisso() + " - " + newFattura.getNumero());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newFattura;
    }

    public static MROldDocumentoFiscale createInvoiceForCustomer(Session sx, String splitAmountType, String splitVatType, List<MROldRigaDocumentoFiscale> listaRighe, MROldAffiliato affiliato, MROldNumerazione numerazione, String prefisso, Date data, Integer anno, User aUser, MROldCustomerSplitPayment cust, MROldSede sedeOperativa, MROldPrenotazione prenotazione) {

        MROldCodiciIva codiceIvaCustomerInsurance = null;
        if (splitVatType.equals("No distribution")) {
            if (prenotazione.getTariffa() != null) {
                codiceIvaCustomerInsurance = prenotazione.getTariffa().getCodiceIva();
            }
        } else if (splitVatType.equals("All VAT to customer")) {
            Query query = sx.createQuery(
                    "select x from MROldCodiciIva x where "
                            + //NOI18N
                            "x.codice = :codice ");
            query.setParameter("codice", "0"); //NOI18N
            codiceIvaCustomerInsurance = (MROldCodiciIva) query.list().get(0);
        } else if (splitVatType.equals("Each party pays their own VAT")) {
            if (splitAmountType.equals("12,5%")) {
                Query query = sx.createQuery(
                        "select x from MROldCodiciIva x where "
                                + //NOI18N
                                "x.codice = :codice ");
                query.setParameter("codice", "14"); //NOI18N
                codiceIvaCustomerInsurance = (MROldCodiciIva) query.list().get(0);
            } else if (splitAmountType.equals("1/3")) {
                Query query = sx.createQuery(
                        "select x from MROldCodiciIva x where "
                                + //NOI18N
                                "x.codice = :codice ");
                query.setParameter("codice", "18"); //NOI18N
                codiceIvaCustomerInsurance = (MROldCodiciIva) query.list().get(0);
            } else if (splitAmountType.equals("25%")) {
                Query query = sx.createQuery(
                        "select x from MROldCodiciIva x where "
                                + //NOI18N
                                "x.codice = :codice ");
                query.setParameter("codice", "16"); //NOI18N
                codiceIvaCustomerInsurance = (MROldCodiciIva) query.list().get(0);
            }

        }
        MROldPianoDeiConti sottoContoNoleggio = ContabUtils.leggiSottoconto(sx, new Integer(4), new Integer(10), new Integer(6));
        MROldDocumentoFiscale newFattura = null;
        try {
            Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
            newFattura = MROldDocumentoFiscale.creaDocumentoFiscale(
                    affiliato,
                    aUser,
                    MROldDocumentoFiscale.FT,
                    numerazione,
                    prefisso,
                    numero,
                    data,
                    anno);
            newFattura.setPrenotazione((MROldPrenotazione) sx.get(MROldPrenotazione.class, prenotazione.getId()));
            newFattura.setCliente(cust.getCliente());
            newFattura.setPagamento(prenotazione.getPagamento());
            newFattura.setAnnotazioni(prenotazione.getNote());
            if (newFattura.getFatturaRighe() != null && !newFattura.getFatturaRighe().isEmpty()) {
                newFattura.getFatturaRighe().clear();
            } else if (newFattura.getFatturaRighe() == null) {
                newFattura.setFatturaRighe(new ArrayList());
            }
            //METODO PRECEDENTE
            /*for (RigaDocumentoFiscaleSplitPayment rigaSplit : cust.getRigheSplitPayment()) {
                RigaDocumentoFiscale rigaNormal = FatturaUtils.trasformaRigaDocumentoSplitPaymentInNormale(rigaSplit);
                newFattura.getFatturaRighe().add(rigaNormal);
            }*/

            //NUOVO METODO
            /*
            if subject is customer, row from calcolaRighe
             */
            if (cust.getCliente() != null && cust.getCliente().getTypeOfCustomer().equals(MROldClienti.NORMAL)) {
                //check if there is a workshop or insurance company
                //check if there is a workshop
                boolean thereIsWorkshop = false;
                List<MROldCustomerSplitPayment> listCustomerSplit = prenotazione.getCustomerSplitPayment();
                for (MROldCustomerSplitPayment tempCustomerWorkshop : listCustomerSplit) {
                    if (tempCustomerWorkshop.getCliente().getTypeOfCustomer().equals(MROldClienti.WORKSHOP)) {
                        thereIsWorkshop = true;
                    }
                }
                if (thereIsWorkshop) {
                    //if customer and workshop
                    for (MROldRigaDocumentoFiscaleSplitPayment rigaSplit : cust.getRigheSplitPayment()) {
                        MROldRigaDocumentoFiscale rigaNormal = FatturaUtils.trasformaRigaDocumentoSplitPaymentInNormale(rigaSplit);
                        newFattura.getFatturaRighe().add(rigaNormal);
                    }
                } else {
                    //if customer and insurance company
                    for (MROldRigaDocumentoFiscale rowToAdd : listaRighe) {
                        newFattura.getFatturaRighe().add(rowToAdd);
                    }
                }
                for (MROldCustomerSplitPayment tempCustomer : listCustomerSplit) {
                    if (tempCustomer.getCliente().getTypeOfCustomer().equals(MROldClienti.INSURANCE_COMPANY)) {
                        //fetch the row split payment, calculate total amount, inser the correct CodiceIva, quantty from tkm
                        List<MROldRigaDocumentoFiscaleSplitPayment> insuranceCompanyRows = tempCustomer.getRigheSplitPayment();
                        Double totAmountInsuranceComany = 0.00;
                        Double quantity = 1.00;
                        if (insuranceCompanyRows != null) {
                            for (MROldRigaDocumentoFiscaleSplitPayment insuranceCompanyRow : insuranceCompanyRows) {
                                totAmountInsuranceComany = totAmountInsuranceComany + insuranceCompanyRow.getTotaleRiga();
                            }
                        }
                        totAmountInsuranceComany = totAmountInsuranceComany - (totAmountInsuranceComany * 2);
                        totAmountInsuranceComany = MathUtils.round(totAmountInsuranceComany, 2);


//                        MROldRigaDocumentoFiscale rigaRiferimento = listaRighe.get(0);
//                        MROldRigaDocumentoFiscaleSplitPayment rifaRiferimentoSplit = insuranceCompanyRows.get(0);
                        /*
                        prendere tutti i dati da quella di riferimento vedend i set che fa
                         */
                        MROldRigaDocumentoFiscale riga = null;
                        double imponibile = 0.0, iva = 0.0, aliquota = 0.0, totale = 0.0;
                        //Codice IVA insurance company, first case is the same of the customer in second case is 0
                        double prezzo = MathUtils.round(totAmountInsuranceComany, 5);
                        imponibile = MathUtils.round(1 * prezzo, 5);
                        aliquota = codiceIvaCustomerInsurance.getAliquota().doubleValue();
                        iva = MathUtils.round(imponibile * aliquota, 5);
                        totale = imponibile + iva;
                        riga = new MROldRigaDocumentoFiscale();
                        riga.setDescrizione("Paid by insurance");
                        riga.setUnitaMisura(null);
                        riga.setQuantita(quantity);
                        riga.setPrezzoUnitario(new Double(prezzo));
                        riga.setSconto(new Double(0));
                        riga.setScontoFisso(new Double(0));
                        riga.setTotaleImponibileRiga(new Double(imponibile));
                        riga.setTotaleIvaRiga(new Double(iva));
                        riga.setCodiceIva(codiceIvaCustomerInsurance);
                        //riga.setCodiceSottoconto(rigaRiferimento.getCodiceSottoconto());
                        riga.setCodiceSottoconto(sottoContoNoleggio);
                        riga.setTotaleRiga(new Double(totale));
                        riga.setTempoKm(Boolean.FALSE);
                        riga.setTempoExtra(Boolean.FALSE);
                        riga.setFranchigia(Boolean.FALSE);
                        newFattura.getFatturaRighe().add(riga);
                        if (splitVatType == "Each party pays their own VAT") {
                            MROldRigaDocumentoFiscale rigaArrotondamento = null;
                            MROldCodiciIva codiciIvaArrotondamento = null;
                            double imponibileArrotondamento = 0.0, ivaArrotondamento = 0.0, aliquotaArrotondamento = 0.0, totaleArrotondamento = 0.0;
                            /*
                            calculate check amount
                            */
                            Double totAmount = 0.00;
                            Double totAmountCustomer = 0.00;
                            Double totAmountInsurance = 0.00;
                            Double totAmountCheck = 0.00;
                            for (MROldRigaDocumentoFiscale singleRow : listaRighe) {
                                totAmount = totAmount + singleRow.getTotaleRiga();
                            }
                            totAmountCustomer = MathUtils.round(totAmount + totale, 2);
                            totAmountInsurance = MathUtils.round(totAmount - totAmountCustomer, 2);
                            totAmountCheck = MathUtils.round(totAmount - totAmountInsurance - totAmountCustomer, 2);
                            double prezzoArrotondamento = MathUtils.round(totAmountCheck, 2);
                            imponibileArrotondamento = MathUtils.round(1 * prezzoArrotondamento, 2);
                            Query query = sx.createQuery(
                                    "select x from MROldCodiciIva x where "
                                            + //NOI18N
                                            "x.codice = :codice ");
                            query.setParameter("codice", "0"); //NOI18N
                            codiciIvaArrotondamento = (MROldCodiciIva) query.list().get(0);
                            aliquotaArrotondamento = codiciIvaArrotondamento.getAliquota().doubleValue();
                            ivaArrotondamento = MathUtils.round(imponibileArrotondamento * aliquotaArrotondamento, 2);
                            totaleArrotondamento = imponibileArrotondamento + ivaArrotondamento;
                            rigaArrotondamento = new MROldRigaDocumentoFiscale();
                            rigaArrotondamento.setDescrizione("Check amount row");
                            rigaArrotondamento.setUnitaMisura(null);
                            rigaArrotondamento.setQuantita(quantity);
                            rigaArrotondamento.setPrezzoUnitario(new Double(prezzoArrotondamento));
                            rigaArrotondamento.setSconto(new Double(0));
                            rigaArrotondamento.setScontoFisso(new Double(0));
                            rigaArrotondamento.setTotaleImponibileRiga(new Double(imponibileArrotondamento));
                            rigaArrotondamento.setTotaleIvaRiga(new Double(ivaArrotondamento));
                            /*
                            the previous alquota
                            */
                            rigaArrotondamento.setCodiceIva(codiciIvaArrotondamento);
                            //riga.setCodiceSottoconto(rigaRiferimento.getCodiceSottoconto());
                            rigaArrotondamento.setCodiceSottoconto(sottoContoNoleggio);
                            rigaArrotondamento.setTotaleRiga(new Double(totaleArrotondamento));
                            rigaArrotondamento.setTempoKm(Boolean.FALSE);
                            rigaArrotondamento.setTempoExtra(Boolean.FALSE);
                            rigaArrotondamento.setFranchigia(Boolean.FALSE);
                            newFattura.getFatturaRighe().add(rigaArrotondamento);
                        }
                    }
                }
            }
            if (cust.getCliente() != null && cust.getCliente().getTypeOfCustomer().equals(MROldClienti.INSURANCE_COMPANY)) {
                //ok if only customer, first case customer and insurance company
                MROldCodiciIva codiceIva = null;
                if (splitVatType.equals("Each party pays their own VAT")) {
                    for (MROldRigaDocumentoFiscale rowToAdd : listaRighe) {
                        newFattura.getFatturaRighe().add(rowToAdd);
                    }
                    if (splitAmountType.equals("12,5%")) {
                        Query query = sx.createQuery(
                                "select x from MROldCodiciIva x where "
                                        + //NOI18N
                                        "x.codice = :codice ");
                        query.setParameter("codice", "100"); //NOI18N
                        codiceIva = (MROldCodiciIva) query.list().get(0);
                    } else if (splitAmountType.equals("1/3")) {
                        Query query = sx.createQuery(
                                "select x from MROldCodiciIva x where "
                                        + //NOI18N
                                        "x.codice = :codice ");
                        query.setParameter("codice", "375"); //NOI18N
                        codiceIva = (MROldCodiciIva) query.list().get(0);
                    } else if (splitAmountType.equals("25%")) {
                        Query query = sx.createQuery(
                                "select x from MROldCodiciIva x where "
                                        + //NOI18N
                                        "x.codice = :codice ");
                        query.setParameter("codice", "50"); //NOI18N
                        codiceIva = (MROldCodiciIva) query.list().get(0);
                    }
                } else {
                    //ok if only customer, first case customer and insurance company
                    for (MROldRigaDocumentoFiscale rowToAdd : listaRighe) {
                        Query query = sx.createQuery(
                                "select x from MROldCodiciIva x where "
                                        + //NOI18N
                                        "x.codice = :codice ");
                        query.setParameter("codice", "0"); //NOI18N
                        codiceIva = (MROldCodiciIva) query.list().get(0);
                        //normal row should be with amount without VAT and IVA 0
                        Double totAmountNoVAT = rowToAdd.getTotaleImponibileRiga();
                        rowToAdd.setTotaleIvaRiga(0.00);
                        rowToAdd.setCodiceIva(codiceIva);
                        rowToAdd.setTotaleRiga(totAmountNoVAT);
                        newFattura.getFatturaRighe().add(rowToAdd);
                    }
                }
                //check if there is an insurance company
                List<MROldCustomerSplitPayment> listCustomerSplit = prenotazione.getCustomerSplitPayment();
                for (MROldCustomerSplitPayment tempCustomer : listCustomerSplit) {
                    if (tempCustomer.getCliente().getTypeOfCustomer().equals(MROldClienti.NORMAL)) {
                        //fetch the row split payment, calculate total amount, inser the correct CodiceIva, quantty from tkm
                        List<MROldRigaDocumentoFiscaleSplitPayment> normalCustomerRows = tempCustomer.getRigheSplitPayment();
                        Double totAmountNormalCustomer = 0.00;
                        Double quantity = 1.00;

                        /*
                        Build row Paid by Customer with only tmk rows and VAT
                        */
                        if (normalCustomerRows != null) {
                            for (MROldRigaDocumentoFiscaleSplitPayment normalCustomerRow : normalCustomerRows) {
                                if (normalCustomerRow.getTempoExtra() || normalCustomerRow.getTempoKm() || (normalCustomerRow.getOptional() != null && normalCustomerRow.getOptional().getKmExtra())) {
                                    totAmountNormalCustomer = totAmountNormalCustomer + normalCustomerRow.getTotaleImponibileRiga();
                                }
                            }
                        }
                        totAmountNormalCustomer = totAmountNormalCustomer - (totAmountNormalCustomer * 2);
                        /*
                        prendere tutti i dati da quella di riferimento vedend i set che fa
                         */
                        MROldRigaDocumentoFiscale riga = null;
                        double imponibile = 0.0, iva = 0.0, aliquota = 0.0, totale = 0.0;
                        double prezzo = MathUtils.round(totAmountNormalCustomer, 5);
                        imponibile = MathUtils.round(1 * prezzo, 5);
                        aliquota = codiceIva.getAliquota().doubleValue();
                        iva = MathUtils.round(imponibile * aliquota, 5);
                        totale = imponibile + iva;
                        riga = new MROldRigaDocumentoFiscale();
                        riga.setDescrizione("Paid by Customer");
                        riga.setUnitaMisura(null);
                        riga.setQuantita(quantity);
                        riga.setPrezzoUnitario(new Double(prezzo));
                        riga.setSconto(new Double(0));
                        riga.setScontoFisso(new Double(0));
                        riga.setTotaleImponibileRiga(new Double(imponibile));
                        riga.setTotaleIvaRiga(new Double(iva));
                        riga.setCodiceIva(codiceIva);
                        //riga.setCodiceSottoconto(rigaRiferimento.getCodiceSottoconto());
                        riga.setCodiceSottoconto(sottoContoNoleggio);
                        riga.setTotaleRiga(new Double(totale));
                        riga.setTempoKm(Boolean.FALSE);
                        riga.setTempoExtra(Boolean.FALSE);
                        riga.setFranchigia(Boolean.FALSE);
                        newFattura.getFatturaRighe().add(riga);


                        MROldCodiciIva codiceIva2 = prenotazione.getTariffa().getCodiceIva();
                        List<MROldRigaDocumentoFiscaleSplitPayment> normalCustomerRows2 = tempCustomer.getRigheSplitPayment();
                        Double totAmountNormalCustomerOptional = 0.00;
                        Double quantityOpt = 1.00;
                        /*
                        Build row Paid by Customer with only optional and normal VAT
                        */
                        if (normalCustomerRows2 != null) {
                            for (MROldRigaDocumentoFiscaleSplitPayment normalCustomerRow2 : normalCustomerRows2) {
                                if (!normalCustomerRow2.getTempoExtra() && !normalCustomerRow2.getTempoKm()) {
                                    if (normalCustomerRow2.getOptional() != null && !normalCustomerRow2.getOptional().getKmExtra()) {
                                        totAmountNormalCustomerOptional = totAmountNormalCustomerOptional + normalCustomerRow2.getTotaleImponibileRiga();
                                    }
                                }
                            }
                        }
                        totAmountNormalCustomerOptional = totAmountNormalCustomerOptional - (totAmountNormalCustomerOptional * 2);
                        /*
                        prendere tutti i dati da quella di riferimento vedend i set che fa
                         */
                        MROldRigaDocumentoFiscale riga2 = null;
                        double imponibile2 = 0.0, iva2 = 0.0, aliquota2 = 0.0, totale2 = 0.0;
                        double prezzo2 = MathUtils.round(totAmountNormalCustomerOptional, 2);
                        imponibile2 = MathUtils.round(1 * prezzo2, 2);
                        aliquota2 = codiceIva2.getAliquota().doubleValue();
                        iva2 = MathUtils.round(imponibile2 * aliquota2, 2);
                        totale2 = imponibile2 + iva2;
                        riga2 = new MROldRigaDocumentoFiscale();
                        riga2.setDescrizione("Paid by Customer");
                        riga2.setUnitaMisura(null);
                        riga2.setQuantita(quantityOpt);
                        riga2.setPrezzoUnitario(new Double(prezzo2));
                        riga2.setSconto(new Double(0));
                        riga2.setScontoFisso(new Double(0));
                        riga2.setTotaleImponibileRiga(new Double(imponibile2));
                        riga2.setTotaleIvaRiga(new Double(iva2));
                        riga2.setCodiceIva(codiceIva2);
                        //riga.setCodiceSottoconto(rigaRiferimento.getCodiceSottoconto());
                        riga2.setCodiceSottoconto(sottoContoNoleggio);
                        riga2.setTotaleRiga(new Double(totale2));
                        riga2.setTempoKm(Boolean.FALSE);
                        riga2.setTempoExtra(Boolean.FALSE);
                        riga2.setFranchigia(Boolean.FALSE);
                        newFattura.getFatturaRighe().add(riga2);
                    }
                }
            }
            ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
            newFattura.setFatturaImposte(imposteFattura.getImposte());
            newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
            newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
            newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
            newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
            newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
            if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
                ScadenzaUtils.creaScadenze(newFattura);
            }
            newFattura.setContratto(PrenotazioniUtils.contrattoEsistente(sx, prenotazione));
            newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));
            //???????????????????????
            //???????????????????????
            //????????????????????????
            PrimanotaFactory.newPrimanota(sx, newFattura, sedeOperativa, null, null, aUser);

            sx.save(newFattura.getPrimanota());
            sx.save(newFattura);
            newFattura = (MROldDocumentoFiscale)DocumentoFiscaleFactory.checkNumerazioneDoppelGanger(sx, "MROldDocumentoFiscale", newFattura, newFattura.getNumerazione(), newFattura.getAnno(), newFattura.getTipoDocumento());
            System.out.println("Fattura per " + cust.getCliente().getTypeOfCustomer() + " creata: " + newFattura.getPrefisso() + " - " + newFattura.getNumero());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newFattura;
    }

    public static MROldDocumentoFiscale createInvoiceForWorkshop(Session sx, MROldAffiliato affiliato, MROldNumerazione numerazione, String prefisso, Date data, Integer anno, User aUser, MROldCustomerSplitPayment cust, MROldSede sedeOperativa, MROldContrattoNoleggio contratto) {
        MROldDocumentoFiscale newFattura = null;
        try {
            Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
            newFattura = MROldDocumentoFiscale.creaDocumentoFiscale(
                    affiliato,
                    aUser,
                    MROldDocumentoFiscale.FTP,
                    numerazione,
                    prefisso,
                    numero,
                    data,
                    anno);
            MROldCodiciIva codiceIva;
            Query query = sx.createQuery(
                    "select x from MROldCodiciIva x where "
                            + //NOI18N
                            "x.codice = :codice ");
            query.setParameter("codice", "0"); //NOI18N
            codiceIva = (MROldCodiciIva) query.list().get(0);
            newFattura.setContratto((MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contratto.getId()));
            newFattura.setCliente(cust.getCliente());
            newFattura.setPagamento(contratto.getPagamento());
            newFattura.setAnnotazioni(contratto.getNote());
            if (newFattura.getFatturaRighe() != null && !newFattura.getFatturaRighe().isEmpty()) {
                newFattura.getFatturaRighe().clear();
            } else if (newFattura.getFatturaRighe() == null) {
                newFattura.setFatturaRighe(new ArrayList());
            }

            for (MROldRigaDocumentoFiscaleSplitPayment rigaSplit : cust.getRigheSplitPayment()) {
                MROldRigaDocumentoFiscale rigaNormal = FatturaUtils.trasformaRigaDocumentoSplitPaymentInNormale(rigaSplit);
                rigaNormal.setCodiceIva(codiceIva);
                newFattura.getFatturaRighe().add(rigaNormal);
            }

            ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
            newFattura.setFatturaImposte(imposteFattura.getImposte());
            newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
            newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
            newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
            newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
            newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
            if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
                ScadenzaUtils.creaScadenze(newFattura);
            }
            newFattura.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contratto));
            newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));

            sx.save(newFattura);
            newFattura = (MROldDocumentoFiscale)DocumentoFiscaleFactory.checkNumerazioneDoppelGanger(sx, "MROldDocumentoFiscale", newFattura, newFattura.getNumerazione(), newFattura.getAnno(), newFattura.getTipoDocumento());
            System.out.println("Fattura per officina creata: " + newFattura.getPrefisso() + " - " + newFattura.getNumero());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newFattura;
    }

    public static MROldDocumentoFiscale createInvoiceForWorkshop(Session sx, MROldAffiliato affiliato, MROldNumerazione numerazione, String prefisso, Date data, Integer anno, User aUser, MROldCustomerSplitPayment cust, MROldSede sedeOperativa, MROldPrenotazione prenotazione) {
        MROldDocumentoFiscale newFattura = null;
        try {
            Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
            newFattura = MROldDocumentoFiscale.creaDocumentoFiscale(
                    affiliato,
                    aUser,
                    MROldDocumentoFiscale.FTP,
                    numerazione,
                    prefisso,
                    numero,
                    data,
                    anno);
            MROldCodiciIva codiceIva;
            Query query = sx.createQuery(
                    "select x from MROldCodiciIva x where "
                            + //NOI18N
                            "x.codice = :codice ");
            query.setParameter("codice", "0"); //NOI18N
            codiceIva = (MROldCodiciIva) query.list().get(0);
            newFattura.setPrenotazione((MROldPrenotazione) sx.get(MROldPrenotazione.class, prenotazione.getId()));
            newFattura.setCliente(cust.getCliente());
            newFattura.setPagamento(prenotazione.getPagamento());
            newFattura.setAnnotazioni(prenotazione.getNote());
            if (newFattura.getFatturaRighe() != null && !newFattura.getFatturaRighe().isEmpty()) {
                newFattura.getFatturaRighe().clear();
            } else if (newFattura.getFatturaRighe() == null) {
                newFattura.setFatturaRighe(new ArrayList());
            }

            for (MROldRigaDocumentoFiscaleSplitPayment rigaSplit : cust.getRigheSplitPayment()) {
                MROldRigaDocumentoFiscale rigaNormal = FatturaUtils.trasformaRigaDocumentoSplitPaymentInNormale(rigaSplit);
                rigaNormal.setCodiceIva(codiceIva);
                newFattura.getFatturaRighe().add(rigaNormal);
            }

            ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
            newFattura.setFatturaImposte(imposteFattura.getImposte());
            newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
            newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
            newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
            newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
            newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
            if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
                ScadenzaUtils.creaScadenze(newFattura);
            }
            newFattura.setContratto(PrenotazioniUtils.contrattoEsistente(sx, prenotazione));
            newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));

            sx.save(newFattura);
            newFattura = (MROldDocumentoFiscale)DocumentoFiscaleFactory.checkNumerazioneDoppelGanger(sx, "MROldDocumentoFiscale", newFattura, newFattura.getNumerazione(), newFattura.getAnno(), newFattura.getTipoDocumento());
            System.out.println("Fattura per officina creata: " + newFattura.getPrefisso() + " - " + newFattura.getNumero());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return newFattura;
    }

    public static void addRowDocumentoFiscaleToCustomerSplitPayment(Session sx, MROldRigaDocumentoFiscale rigaNormale, MROldCustomerSplitPayment customer, Double amount, Double iva) {
        MROldRigaDocumentoFiscaleSplitPayment rigaSplit = FatturaUtils.trasformaRigaDocumentoInSplitPayment(rigaNormale, customer);
        try {
            rigaSplit.setTotaleImponibileRiga(amount);
            rigaSplit.setTotaleIvaRiga(iva);
            rigaSplit.setTotaleRiga(amount + iva);
            customer.getRigheSplitPayment().add(rigaSplit);
            sx.saveOrUpdate(rigaSplit);
            sx.saveOrUpdate(customer);
            //sx.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
