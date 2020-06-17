package it.myrent.ee.api.utils;

import it.aessepi.myrentcs.utils.Fatturazione;
import it.aessepi.myrentcs.utils.FatturazioneBreveTermine;
import it.aessepi.myrentcs.utils.FatturazioneFactory;
import it.aessepi.myrentcs.utils.FatturazionePrepagato;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.DateUtils;
import static it.aessepi.utils.MathUtils.safeLongToInt;

import it.aessepi.utils.Parameters;
import it.aessepi.utils.beans.FormattedDate;
import it.aessepi.utils.db.PersistentInstance;

import java.util.*;
import java.util.concurrent.TimeUnit;

import it.myrent.ee.api.exception.TitledException;
import it.myrent.ee.api.factory.ImpostaFactory;
import it.myrent.ee.api.preferences.Preferenze;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


import it.myrent.ee.api.exception.FatturaVuotaException;
import it.myrent.ee.api.exception.TariffaNonValidaException;
import it.myrent.ee.api.factory.DocumentoFiscaleFactory;
import it.myrent.ee.db.*;
import it.myrent.ee.db.MROldDocumentoFiscale;




/**
 * Created by Shivangani on 10/6/2017.
 */
public class InvoiceEAUtils {
    private static HashSet<MROldContrattoNoleggio> wrongRatesAgreement;
    private static HashMap<PersistentInstance, List<MROldDocumentoFiscale>> r2rInvoiceMap;
    private List<Rent2Rent> rent2rentList;
    private static final String KPRE_TYPE = "KPRE";

    public InvoiceEAUtils(List<MROldContrattoNoleggio> contrattiList,List<Rent2Rent> rent2rent, Date dateTo, Date invoiceDate) {
        this.contrattiList = contrattiList;
        this.rent2rentList=rent2rent;
        this.dataTo = dateTo;
        dataFattura = invoiceDate;
        fontiAlreadyInvoiced = new HashSet<MROldFonteCommissione>();
        summaryRowsMap = new HashMap<MROldFonteCommissione, List<MROldRigaDocumentoFiscale>>();
        summaryInvoicesMap = new HashMap<MROldFonteCommissione, List<MROldDocumentoFiscale>>();
        docsAndPartite = new HashMap<String, List<MROldPartita>>();
        invoicesMap = new HashMap<PersistentInstance, List<MROldDocumentoFiscale>>();
        r2rInvoiceMap  = new HashMap<PersistentInstance, List<MROldDocumentoFiscale>>();
        wrongRatesAgreement = new HashSet<MROldContrattoNoleggio>();
    }

    public List<MROldDocumentoFiscale> makeAllRent2RentInvoices(Session sx,Integer userid) {

        //Session sx = HibernateBridge.startNewSession();
        int i = 0;
        int cont = 0;

        if (rent2rentList!=null)
            System.out.println("contratti rent2rent = " + rent2rentList.size());

//        if(listenerProgressBar != null && rent2rentList!=null){
//            listenerProgressBar.update(0);
//            listenerProgressBar.setVisible(true);
//            listenerProgressBar.setMax(rent2rentList.size());
//            listenerProgressBar.setText("Caricando i contratti...");
//        }

        for (Rent2Rent focusR2R : rent2rentList){
            if (focusR2R != null) {
                cont++;
//                if(listenerProgressBar != null && !listenerProgressBar.getIsStopped()){
//                    listenerProgressBar.update(cont);
//                }
//                else{
//                    return null;
//                }

                focusR2R = (Rent2Rent)sx.get(Rent2Rent.class, focusR2R.getId());

                try {
                    r2rMonthlyBilling (sx, focusR2R, dataTo,userid);
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }


        System.out.println("contratti processati = " + cont);


//        if(listenerProgressBar != null){
//            listenerProgressBar.setVisible(false);
//        }
        try{
//            printResults();
        }
        catch(Exception e){
            e.printStackTrace();
        }


        // return getInvoicesR2RMapValues();
        return getInvoicesMapValues();

    }



    private List<MROldRigaDocumentoFiscale> r2rMonthlyBilling(Session sx, Rent2Rent rent2rent, Date toDate,Integer userid) throws TariffaNonValidaException, FatturaVuotaException {
        Date lastDay = rent2rent.getLastBilledDate();
        Boolean billed30Days = rent2rent.getIs30DaysInvoice();
        Date startDate, endDate;

        //Caso classico . fatturazione a fine mese
        if (billed30Days!=null && !billed30Days) {
            if (lastDay == null) {
                startDate = rent2rent.getRaStartDate();
                Calendar calLastDayOfTheMonth = new GregorianCalendar();
                calLastDayOfTheMonth.setTime(startDate);
                calLastDayOfTheMonth.set(Calendar.DATE, calLastDayOfTheMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate = calLastDayOfTheMonth.getTime();

            } else {

                Calendar calLastBilledDay = new GregorianCalendar();
                calLastBilledDay.setTime(lastDay);

                Calendar calLastDayOfTheMonth = new GregorianCalendar();
                calLastDayOfTheMonth.setTime(lastDay);

                Calendar calStartDate = new GregorianCalendar();
                calStartDate.setTime(lastDay);
                calStartDate.add(Calendar.DATE, 1);

                int lastBilledDay = calLastBilledDay.get(Calendar.DAY_OF_MONTH);
                int lastDayOfMonth = calLastDayOfTheMonth.getActualMaximum(Calendar.DATE);

                startDate = calStartDate.getTime();

                if ((startDate.getTime() >= rent2rent.getRaEndDate().getTime() && rent2rent.getLastBillingDate() == null)
                        || (rent2rent.getLastBillingDate() != null && startDate.getTime() >= rent2rent.getLastBillingDate().getTime())) {
                    return null; // Ad esempio: abbiamo fatturato fino al 31 Marzo e il contratto finiva il 31 Marzo -> non deve fare niente
                }
                if (lastDayOfMonth == lastBilledDay) {
                    //fatturiamo il prox mese
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(lastDay);
                    cal.add(Calendar.DATE, 1);
                    Date nextMonthFirstDay = cal.getTime();
                    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    endDate = cal.getTime();

                    if (rent2rent.getLastBillingDate() != null && rent2rent.getLastBillingDate().getTime() < endDate.getTime()) {
                        endDate = rent2rent.getLastBillingDate();
                    }

                } else {
                    //fatturare il rateo
                    endDate = calLastDayOfTheMonth.getTime();
                    if (rent2rent.getLastBillingDate() != null && rent2rent.getLastBillingDate().getTime() < endDate.getTime()) {
                        endDate = rent2rent.getLastBillingDate();
                    }

                }

            }
        } else {
            if (lastDay == null) {
                startDate = rent2rent.getRaStartDate();
                Calendar calNexMonthDayLessOne = new GregorianCalendar();
                calNexMonthDayLessOne.setTime(startDate);
                calNexMonthDayLessOne.add(Calendar.MONTH, 1);
                endDate = calNexMonthDayLessOne.getTime();

            } else {

                Calendar calLastBilledDay = new GregorianCalendar();
                calLastBilledDay.setTime(lastDay);

                Calendar calNexMonthDayLessOne = new GregorianCalendar();
                calNexMonthDayLessOne.setTime(lastDay);
                calNexMonthDayLessOne.add(Calendar.MONTH, 1);

                Calendar calStartDate = new GregorianCalendar();
                calStartDate.setTime(lastDay);
                calStartDate.add(Calendar.DATE, 1);

                int lastBilledDay = calLastBilledDay.get(Calendar.DAY_OF_MONTH);
                int lastNexMonthDayLessOne = calNexMonthDayLessOne.get(Calendar.DATE);

                startDate = calStartDate.getTime();

                if ((startDate.getTime() >= rent2rent.getRaEndDate().getTime() && rent2rent.getLastBillingDate() == null)
                        || (rent2rent.getLastBillingDate() != null && startDate.getTime() >= rent2rent.getLastBillingDate().getTime())) {
                    return null; // Ad esempio: abbiamo fatturato fino al 31 Marzo e il contratto finiva il 31 Marzo -> non deve fare niente
                }
                if ((lastNexMonthDayLessOne +1) == lastBilledDay) {
                    //fatturiamo il prox mese
                    endDate = calNexMonthDayLessOne.getTime();

                    if (rent2rent.getLastBillingDate() != null && rent2rent.getLastBillingDate().getTime() < endDate.getTime()) {
                        endDate = rent2rent.getLastBillingDate();
                    }

                } else {
                    //fatturare il rateo
                    endDate = calNexMonthDayLessOne.getTime();
                    if (rent2rent.getLastBillingDate() != null && rent2rent.getLastBillingDate().getTime() < endDate.getTime()) {
                        endDate = rent2rent.getLastBillingDate();
                    }

                }

            }
        }


        //FonteCommissione fonte = (FonteCommissione) DatabaseUtils.refreshPersistentInstanceWithSx(sx, FonteCommissione.class, contratto.getCommissione().getFonteCommissione());
        List<List<MROldRigaDocumentoFiscale>> rows = FatturaUtils.calculateRowsUtils(sx, rent2rent, startDate, endDate);
        HashMap<String, List<MROldRigaDocumentoFiscale>> hsm = new HashMap<String, List<MROldRigaDocumentoFiscale>>();
        hsm.put((KPRE_TYPE), rows.get(0));

        if(hsm.get(KPRE_TYPE).size() > 0){
            if (rent2rent.getVehicle() != null) {
                System.out.println("numero contratto KPRE con righe KPRE = " + rent2rent.getExportContractNumber() + " " + rent2rent.getVehicle().getTarga());
            }
        }
        ArrayList <MROldDocumentoFiscale>createdInvoices = new ArrayList<MROldDocumentoFiscale>();
        invoiceManager(rent2rent, sx, hsm, dataFattura, startDate, endDate,userid);






        return null;
    }

    public static List<List<MROldRigaDocumentoFiscale>> calculateRowsUtils(Session sx, Rent2Rent r2r, Date startDate, Date endDate) {

       // sx = HibernateBridge.refreshSessionSX(sx);

        List<List<MROldRigaDocumentoFiscale>> rows = new ArrayList<List<MROldRigaDocumentoFiscale>>();
        List defaultRows = new ArrayList<MROldRigaDocumentoFiscale>();



        if (startDate != null && endDate != null) {

            try {
                Fatturazione fatturaBreveTermine = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(sx,
                        new MROldPreventivo(),
                        null,
                        startDate,
                        endDate,
                        null,
                        null,
                        null,
                        null,
                        null);
                defaultRows = fatturaBreveTermine.calcolaRigheProssimaFatturaR2R(sx, r2r, startDate, endDate);

            } catch (FatturaVuotaException fex) {
                fex.printStackTrace();
                defaultRows = new ArrayList();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Iterator itr=defaultRows.iterator();
            MROldRigaDocumentoFiscale row=null;

            while (itr.hasNext()){
                row = (MROldRigaDocumentoFiscale)itr.next();
                row.setDescrizione(row.getDescrizione().replaceAll(" - Giorni tariffa", ""));
                if (row.getQuantita().equals(1))
                    row.setUnitaMisura("MESE");
            }

            rows.add(defaultRows);


        }

        return rows;
    }



    private Boolean invoiceManager(Rent2Rent r2r,
                                   Session sx, Map<String, List<MROldRigaDocumentoFiscale>> mapRighe, Date data,
                                   Date inizioFatturazione, Date fineFatturazione,Integer userid) {

        User aUserInvoice = (User) sx.get(UserImpl.class,userid);
        MROldSede sedeOperativa = aUserInvoice.getSedeOperativa();
        List<MROldRigaDocumentoFiscale> righeKPRE = new ArrayList<MROldRigaDocumentoFiscale>();

        if (mapRighe.get(KPRE_TYPE) != null) {
            righeKPRE.addAll(mapRighe.get(KPRE_TYPE));
        }
        if (r2r != null) {
            System.out.println("invoice manager r2r = contratto " + r2r.getExportContractNumber() +" "+r2r.getVehicle().getTarga());
        }

        boolean response = false;
        if ((r2r.getVehicle() != null && r2r.getCustomer() != null))  {
            invoicesMap.entrySet();
            List<MROldDocumentoFiscale> fattureList = invoicesMap.get(r2r);
            if (fattureList == null) {
                fattureList = new ArrayList<MROldDocumentoFiscale>();
            }

            ///CREAZIONE FATTURA EXTRA PREPAY
            if (righeKPRE != null && righeKPRE.size() > 0) {
                MROldDocumentoFiscale focusDoc = null;
                righeKPRE = FatturaUtils.cleanRighe(righeKPRE);

                boolean found= false;
                Iterator itr = null;


                MROldDocumentoFiscale df=null;


                for (List<MROldDocumentoFiscale> testDocs : invoicesMap.values()) {
                    if (testDocs!=null &&
                            testDocs.size()>0)
                        itr = testDocs.iterator();

                    while (itr!=null && itr.hasNext() && found==false){
                        df= (MROldDocumentoFiscale) itr.next();
                        if (df.getCliente().equals(r2r.getCustomer()))
                            found = true;

                    }
                }
                List<MROldDocumentoFiscale> tempList = r2rInvoiceMap.get(r2r);
                if (tempList == null) {
                    tempList = new ArrayList<MROldDocumentoFiscale>();
                }

                MROldDocumentoFiscale tempDoc = createInvoice(righeKPRE, r2r, sedeOperativa, findPaymentForAgreement(sx, r2r), data, false, inizioFatturazione, fineFatturazione,sx,aUserInvoice);

                tempList.add(tempDoc);
                r2rInvoiceMap.put(r2r, tempList);

                if (!found) {
                    focusDoc = createInvoice(righeKPRE, r2r, sedeOperativa, findPaymentForAgreement(sx, r2r), data, false, inizioFatturazione, fineFatturazione,sx,aUserInvoice);
                } else {
                    if (df != null) {
                        List<MROldRigaDocumentoFiscale> newRigheKPRE = new ArrayList<MROldRigaDocumentoFiscale>();
                        newRigheKPRE.addAll(righeKPRE);

                        df = updateInvoice(sx, r2r, newRigheKPRE,df);

                    }
                    //df.getFatturaRighe().addAll(righeKPRE);
                }
                if (focusDoc!=null)
                    fattureList.add(focusDoc);


            }


            if(fattureList != null && fattureList.size() > 0){

                invoicesMap.put(r2r, fattureList);

            }


        }
        return response;
    }

    private static MROldDocumentoFiscale createInvoice(List<MROldRigaDocumentoFiscale> righe, Rent2Rent r2r,
                                                       MROldSede sedeOperativa, MROldPagamento pagamento, Date date,
                                                  boolean prepagato, Date inizioFatturazione, Date fineFatturazione,Session sx,User user) {
//        Session sx = HibernateBridge.startNewSession();
//        if (r2r != null) {
//            righe = FatturaUtils.removeEmptyCharges(righe);
//        }

        MROldDocumentoFiscale aFattura = null;
        if (righe != null && righe.size() > 0) {
            MROldNumerazione numerazioneSelezionata = null;
            int indexFattura = 0;

            MROldNumerazione numerazioneRegistrazione = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE,user);
            MROldNumerazione numerazioneProtocollo = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.VENDITE,user);
            MROldNumerazione numerazioneProtocolloProforma = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PROFORMA,user);

            MROldNumerazione numerazionePartite = NumerazioniUtils.getNumerazione(sx, user.getAffiliato(), MROldNumerazione.PARTITE,user);

            Integer primoNumeroRegistrazione = NumerazioniUtils.aggiornaProgressivo(
                    sx,
                    numerazioneRegistrazione,
                    FormattedDate.annoCorrente(date),
                    1);


            Integer primoNumeroPartita = NumerazioniUtils.aggiornaProgressivo(
                    sx,
                    numerazionePartite,
                    0,
                    1);

            createInvoiceCounter++;
            System.out.println("createInvoiceCounter = " + createInvoiceCounter);
//            if (createInvoiceCounter == 53) {
//                System.out.println("");
//            }


            Integer primoNumeroProtocollo = NumerazioniUtils.aggiornaProgressivo(
                    sx,
                    numerazioneProtocollo,
                    FormattedDate.annoCorrente(date),
                    1);

            aFattura = createFattura(sx,
                    null,
                    null,
                    sedeOperativa,
                    pagamento,
                    righe,
                    date,
                    numerazioneProtocollo,
                    numerazioneRegistrazione,
                    numerazionePartite,
                    primoNumeroPartita,
                    primoNumeroRegistrazione,
                    primoNumeroProtocollo,
                    prepagato,
                    indexFattura,
                    inizioFatturazione,
                    fineFatturazione, r2r);

            Integer iAnno = 2017;

            if(aFattura != null){
                if (aFattura.getAnno() == null || (aFattura.getAnno() != null && aFattura.getAnno().equals(0))) {
                    iAnno = DayUtils.getFieldFromDate(date, Calendar.YEAR);
                }
                aFattura = (MROldDocumentoFiscale)checkNumerazioneDoppelGanger(sx,"MROldDocumentoFiscale",  aFattura, numerazioneProtocollo, iAnno);
            }

        }
//    public void checkNumerazioneDoppelGanger(String clasz, String numero, String prefisso, ProgessivoInterface p, Numerazione numerazione, Integer anno)


        return aFattura;
    }

    private static MROldDocumentoFiscale createFattura(Session sx,
                                                       MROldContrattoNoleggio contratto,
                                                       MROldFonteCommissione fonte,
                                                       MROldSede sedeOperativa,
                                                       MROldPagamento pagamento,
                                                  List<MROldRigaDocumentoFiscale> righe,
                                                  Date date,
                                                       MROldNumerazione numerazioneProtocollo,
                                                       MROldNumerazione numerazioneRegistrazione,
                                                       MROldNumerazione numerazionePartite,
                                                  Integer primoNumeroPartita,
                                                  Integer primoNumeroRegistrazione,
                                                  Integer primoNumeroProtocollo ,
                                                  Boolean prepagato,
                                                  Integer indexFattura,
                                                  Date inizioFatturazione,
                                                  Date fineFatturazione, Rent2Rent r2r) {

        MROldPartita associatedPartita = null;
        //fonte = (MROldFonteCommissione)sx.get(MROldFonteCommissione.class, fonte.getId());

        MROldDocumentoFiscale aFattura = null;
        for (int i = 0; i < righe.size(); i++) {

            try {
                if (aFattura == null) {
                    try {
                        aFattura = DocumentoFiscaleFactory.newIntestazioneFattura(
                                sx,
                                //contratto,
                                r2r,
                                date,
                                numerazioneProtocollo,
                                primoNumeroProtocollo + i,
                                prepagato);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }
                if (aFattura != null) {
                    //sx = HibernateBridge.startNewSession();
//                    for(RigaDocumentoFiscale r : righe){
//                        System.out.println("CreateFattura - codice sottoconto" + r.getCodiceSottoconto());
//                        System.out.println("CreateFattura - veicolo" + r.getVeicolo());
//                    }
                    aFattura.setFatturaRighe(righe);


                    try {
//                        if(contratto != null){
//                            aFattura.setContratto(contratto);
//                        }
//                        if(pagamento != null && contratto!=null){
//                            if (fonte!=null && fonte.getCliente()!=null && fonte.getCliente().getPagamento()!=null&& prepagato!=null && prepagato)
//                                aFattura.setPagamento(fonte.getCliente().getPagamento());
//                            else
//                                aFattura.setPagamento(pagamento);
//                        }
                        if (r2r != null) {
                            aFattura.setRent2rent(r2r);
                            aFattura.setContractExportCode(r2r.getExportContractNumber());

                            if (r2r.getPagamento() != null) {
                                aFattura.setPagamento(r2r.getPagamento());
                            } else if (r2r.getCustomer().getPagamento() != null) {
                                aFattura.setPagamento(r2r.getCustomer().getPagamento());
                            } else {
                                aFattura.setPagamento(pagamento);
                            }
                        }

                        Map<MROldPartita, MROldDocumentoFiscale> newDocAndPart = null;
                        newDocAndPart = DocumentoFiscaleFactory.newFatturaAutomaticaSaldoAndPartita(sx, aFattura, sedeOperativa, prepagato,
                                numerazioneRegistrazione,
                                primoNumeroRegistrazione + righe.size(),
                                numerazionePartite, primoNumeroPartita + indexFattura, r2r.getVehicle());

                        for(MROldDocumentoFiscale d: newDocAndPart.values()){
                            aFattura = d;
                        }
                        for(MROldPartita p : newDocAndPart.keySet()){
                            associatedPartita = p;
                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(aFattura != null){
            aFattura.setPrepagato(prepagato);
            aFattura.setData(date);
            aFattura.setInizioFatturazione(inizioFatturazione);
            aFattura.setFineFatturazione(fineFatturazione);

            if (r2r!=null){
                aFattura.setCliente(r2r.getCustomer());
            }

            if (contratto!=null)
                aFattura.setContratto(contratto);
            if (r2r!=null)
                aFattura.setRent2rent(r2r);

            insertPartitaInMap(aFattura, associatedPartita);
        }
        return aFattura;

    }


    public static MROldPagamento findPaymentForAgreement(Session sx, Rent2Rent r2r) {
//        if(sx == null || sx != null && !sx.isOpen()){
//            sx = HibernateBridge.startNewSession();
//        }
        MROldPagamento pagamento = null;
        if(r2r != null){
            if(r2r.getCustomer()!=null && r2r.getCustomer().getPagamento()!=null){ //vista fattura
                pagamento = (MROldPagamento) sx.get(MROldPagamento.class, r2r.getCustomer().getPagamento().getId());
            }
            else{
                pagamento = (MROldPagamento) sx.get(MROldPagamento.class, 303);


            }

        }
        else{
            pagamento = (MROldPagamento) sx.get(MROldPagamento.class, 1);
        }
        return pagamento;
    }

    private static MROldDocumentoFiscale updateInvoice(Session sx,
                                                  Rent2Rent r2r,
                                                  List<MROldRigaDocumentoFiscale> righe,
                                                       MROldDocumentoFiscale oldInvoice) {

        oldInvoice.getFatturaRighe().addAll(righe);
        ImpostaFactory imposteFattura = new ImpostaFactory(oldInvoice.getFatturaRighe());
        oldInvoice.setFatturaImposte(imposteFattura.getImposte());

        oldInvoice.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        oldInvoice.setTotaleIva(imposteFattura.getTotaleImposta());

        oldInvoice.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

        return oldInvoice;
    }


    public static HashMap<PersistentInstance, List<MROldDocumentoFiscale>> getR2RInvoicesMap() {
        return r2rInvoiceMap;
    }

    public List<MROldDocumentoFiscale> addAllInovicesKPREVirtualAndMade(Session sx){
        List<MROldDocumentoFiscale> result = new ArrayList<MROldDocumentoFiscale>();
        for(Rent2Rent r2r : rent2rentList){
            List<MROldDocumentoFiscale> fattureList = r2rInvoiceMap.get(r2r);
            if (fattureList == null) {
                fattureList = new ArrayList<MROldDocumentoFiscale>();
            }
            r2r = (Rent2Rent) sx.get(Rent2Rent.class, r2r.getId());

            List<MROldDocumentoFiscale> doc = null;
            doc = caricaDocumentiFiscali(r2r,sx);

            if(doc != null && doc.size() > 0){
                fattureList.addAll(doc);
                invoicesMap.put(r2r, fattureList);
                result = fattureList;
            }
        }
        return result;

    }
    private static List<MROldDocumentoFiscale> caricaDocumentiFiscali(Rent2Rent r2r,Session sx) {
        List<MROldDocumentoFiscale> fatture = null;
        //Session sx = sx;
        try {
            //sx = HibernateBridge.startNewSession();

            Query qx = sx.createQuery(
                    "select x from MROldDocumentoFiscale x where " + //NOI18N
                            "x.rent2rent = :r2r  " + //NOI18N
                            "order by x.data desc, x.numero desc"); //NOI18N
            qx.setParameter("r2r", r2r); //NOI18N
           // sx = HibernateBridge.startNewSession();
            fatture = (List<MROldDocumentoFiscale>) qx.list();

        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return fatture;
    }


    private static void insertPartitaInMap(MROldDocumentoFiscale aFattura, MROldPartita associatedPartita) {
        if(docsAndPartite == null){
            docsAndPartite = new HashMap<String, List<MROldPartita>>();
        }
        List<MROldPartita> partiteList = docsAndPartite.get(aFattura.getNumero()+"-"+aFattura.getTipoDocumento());
        if (partiteList == null) {
            partiteList = new ArrayList<MROldPartita>();
        }
        partiteList.add(associatedPartita);
        docsAndPartite.put(aFattura.getNumero()+"-"+aFattura.getTipoDocumento(), partiteList);
    }



    private List<MROldContrattoNoleggio> contrattiList;
    private HashSet<MROldFonteCommissione> fontiAlreadyInvoiced;
    private HashMap<MROldFonteCommissione, List<MROldRigaDocumentoFiscale>> summaryRowsMap;
    private HashMap<MROldFonteCommissione, List<MROldDocumentoFiscale>> summaryInvoicesMap;
    private static HashMap<PersistentInstance, List<MROldDocumentoFiscale>> invoicesMap;
    private static final String CO_TYPE = "CO";
    private static final String CLI_TYPE = "CLI";
    private static int createInvoiceCounter;
    //    private int invoiceManagerCounter;
    private int invoiceManagerCounterMain;
    private int contrattiNull;
    private Date dataFattura;
    private Date dataTo;
    private Boolean onlyExtraPrepay = false;
    protected static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");
    //private ProgressBarListener listenerProgressBar;
    private static Map<String, List<MROldPartita>> docsAndPartite;

    public InvoiceEAUtils(List<MROldContrattoNoleggio> contrattiList, Date dateTo, Date invoiceDate) {
        this.contrattiList = contrattiList;
        this.dataTo = dateTo;
        dataFattura = invoiceDate;
        fontiAlreadyInvoiced = new HashSet<MROldFonteCommissione>();
        summaryRowsMap = new HashMap<MROldFonteCommissione, List<MROldRigaDocumentoFiscale>>();
        summaryInvoicesMap = new HashMap<MROldFonteCommissione, List<MROldDocumentoFiscale>>();
        docsAndPartite = new HashMap<String, List<MROldPartita>>();
        invoicesMap = new HashMap<PersistentInstance, List<MROldDocumentoFiscale>>();
        wrongRatesAgreement = new HashSet<MROldContrattoNoleggio>();
    }

    public void setOnlyExtraPrepay(Boolean onlyExtraPrepay) {
        this.onlyExtraPrepay = onlyExtraPrepay;
    }



//    public void setListenerProgressBar(ProgressBarListener listenerProgressBar) {
//        this.listenerProgressBar = listenerProgressBar;
//    }

    private Map<String, List<MROldRigaDocumentoFiscale>> calculateRighe(Session sx, MROldContrattoNoleggio contratto) {

        Map<String, List<MROldRigaDocumentoFiscale>> mapRighe = new HashMap<String, List<MROldRigaDocumentoFiscale>>();

        List<MROldRigaDocumentoFiscale> listRiga = new ArrayList<MROldRigaDocumentoFiscale>();
        if (contratto != null && contratto.getMovimento() != null && contratto.getMovimento().getId() != null) {
            try {
                if (contratto.getCommissione().getPrepagato() && contratto.getCommissione().getGiorniVoucher()
                        != null && contratto.getCommissione().getGiorniVoucher() > 0) {

                    listRiga = createRighePrePayAuto(sx, contratto);

                    if (listRiga != null) {
                        mapRighe.put(CO_TYPE, listRiga);
                    }

                }

                listRiga = createRigheCustomerAuto(sx, contratto);

                if (listRiga != null) {
                    mapRighe.put(CLI_TYPE, listRiga);
                }

            } catch (Exception ex) {
                ex.printStackTrace();

                mapRighe = null;
            }


        }
        return mapRighe;
    }

    private List<MROldRigaDocumentoFiscale> monthlyBilling(Session sx, MROldContrattoNoleggio contratto, Date toDate, User aUser) throws TariffaNonValidaException, FatturaVuotaException {

        MROldFonteCommissione fonte = contratto.getCommissione().getFonteCommissione();

        Date startInvoice = contratto.getInizio();
        if(contratto.getBilledTill() != null){
            startInvoice = contratto.getBilledTill();
        }
        Date maxEndInvoice = contratto.getFine();
        if (toDate.compareTo(maxEndInvoice) < 0) {
            maxEndInvoice = toDate;
        }
        if (fonte != null && fonte.getIsMonthlyBilled() && contratto.getCommissione()!= null
                && contratto.getCommissione().getGiorniVoucher() != null && contratto.getCommissione().getGiorniVoucher() > 0) {

            ////create normal invoice for the extra prepay rows
            List<List<MROldRigaDocumentoFiscale>> rows = FatturaUtils.calculateRowsUtils(sx, contratto,
                    contratto.getTariffa(), contratto.getCommissione().getGiorniVoucher(), true);
            HashMap<String, List<MROldRigaDocumentoFiscale>> hsm = new HashMap<String, List<MROldRigaDocumentoFiscale>>();
            hsm.put(CLI_TYPE, rows.get(0));

            if(contratto.getNumero() == 193 || contratto.getNumero() == 172){
                System.out.println("");
            }
            if(hsm.get(CLI_TYPE).size() > 0){
                System.out.println("numero contratto monthly con righe cli = " +contratto.getNumero());
            }
            invoiceManager(contratto, sx, hsm, dataFattura, fonte, contratto.getInizio(), contratto.getFine(), aUser);
            ////
            while (startInvoice.compareTo(maxEndInvoice) < 0) {
                //contratto = (MROldContrattoNoleggio) DatabaseUtils.refreshPersistentInstanceWithSx(sx, MROldContrattoNoleggio.class, contratto);

                Date endInvoice = DateUtils.addMonths(startInvoice, 1);

                //Controlla se la fine del contratto e piu vicina del mese
                if (endInvoice.compareTo(contratto.getFine()) > 0) {

                    endInvoice = contratto.getFine();


                }
                if(endInvoice.compareTo(maxEndInvoice) > 0){
                    endInvoice = maxEndInvoice;
                }

                Integer daysToInvoice = 0;
                boolean isLastInvoice = false;

                daysToInvoice = safeLongToInt(TimeUnit.MILLISECONDS.toDays(endInvoice.getTime() - startInvoice.getTime()));

                //calcolo dei giorni finali con integrazione del ritardo massimo
                //days calculation for the final invoice with the max time permitted before adding another day
                if(endInvoice.compareTo(contratto.getFine()) == 0){

                    Integer ritardoMassimoMinuti = Preferenze.getContrattoRitardoMassimoMinuti(sx);
                    daysToInvoice = new Integer((int) Math.max(1.0, Math.ceil(
                            FormattedDate.numeroGiorni(startInvoice, FormattedDate.add(endInvoice,
                                    Calendar.MINUTE, - ritardoMassimoMinuti.intValue()), true))));
                    isLastInvoice = true;
                }
                ////////////////////////////////////////////////////////////////////////////////////////////



                Calendar startDate = Calendar.getInstance();
                startDate.setTime(contratto.getInizio());


                System.out.println("inizio " + startInvoice);
                System.out.println("dateNewInvoice " + endInvoice);
                if(daysToInvoice != null){
                    invoiceManager(contratto, sx, calculateInvoiceMonthly(sx, contratto, daysToInvoice, isLastInvoice), this.dataFattura, fonte, startInvoice, endInvoice, aUser);
                    startInvoice = endInvoice;
                }
            }
        }
        return null;

    }

    private Boolean invoiceManager(MROldContrattoNoleggio contratto,
                                   Session sx, Map<String, List<MROldRigaDocumentoFiscale>> mapRighe, Date data, MROldFonteCommissione focusFonte,
                                   Date inizioFatturazione, Date fineFatturazione, User aUserInvoice) {

        MROldSede sedeOperativa = aUserInvoice.getSedeOperativa();
        List<MROldRigaDocumentoFiscale> righeCliente = new ArrayList<MROldRigaDocumentoFiscale>();
        List<MROldRigaDocumentoFiscale> righePrePay = new ArrayList<MROldRigaDocumentoFiscale>();
        if (mapRighe.get(CLI_TYPE) != null) {
            righeCliente.addAll(mapRighe.get(CLI_TYPE));
        }

        if (mapRighe.get(CO_TYPE) != null) {
            righePrePay.addAll(mapRighe.get(CO_TYPE));
        }
        if (contratto != null) {
            System.out.println("invoice manager numero contratto = " + contratto.getNumero());
        }
        MROldFonteCommissione fonte = contratto.getCommissione().getFonteCommissione();

        boolean response = false;
        if (Boolean.TRUE.equals(contratto.getChiuso() || (Boolean.TRUE.equals(fonte.getIsMonthlyBilled())
                && (contratto.getBilledTill() == null || contratto.getBilledTill() != null
                && contratto.getBilledTill().compareTo(this.dataTo) < 0)))
                && !Boolean.TRUE.equals(contratto.getIsCancelled())) {

            List<MROldDocumentoFiscale> fattureList = invoicesMap.get(contratto);
            if (fattureList == null) {
                fattureList = new ArrayList<MROldDocumentoFiscale>();
            }

            List<MROldDocumentoFiscale> fattureListFonte = invoicesMap.get(focusFonte);
            if (fattureListFonte == null) {
                fattureListFonte = new ArrayList<MROldDocumentoFiscale>();
            }

            ///CREAZIONE FATTURA EXTRA PREPAY
            if (righeCliente != null && righeCliente.size() > 0) {
                MROldDocumentoFiscale focusDoc = null;
                righeCliente = FatturaUtils.cleanRighe(righeCliente);

                focusDoc = createInvoice(sx, righeCliente, contratto, null,sedeOperativa,findPaymentForAgreement(sx, contratto), data, false, inizioFatturazione, fineFatturazione, aUserInvoice);

                if(focusDoc != null){
                    fattureListFonte.add(focusDoc);
                    fattureList.add(focusDoc);
                }
            }
            ///CREAZIONE FATTURA PREPAY
            if (righePrePay != null && righePrePay.size() > 0 && !onlyExtraPrepay) {
                MROldDocumentoFiscale focusDoc = null;
                righePrePay = FatturaUtils.cleanRighe(righePrePay);

                focusDoc = createInvoice(sx, righePrePay, contratto, null,sedeOperativa,findPaymentForAgreement(sx, contratto), data, true, inizioFatturazione, fineFatturazione, aUserInvoice);
                if(focusDoc != null){

                    fattureListFonte.add(focusDoc);
                    fattureList.add(focusDoc);
                }
            }

            if(fattureList != null && fattureList.size() > 0){

                invoicesMap.put(contratto, fattureList);

                if(Boolean.TRUE.equals(focusFonte.getIsSummaryinvoice())){
                    invoicesMap.put(focusFonte, fattureListFonte);
                }
            }


        }
        return response;
    }

    public static Map<String, List<MROldPartita>> getDocsAndPartite() {
        return docsAndPartite;
    }



    private Map<String, List<MROldRigaDocumentoFiscale>> calculateInvoiceMonthly(Session sx, MROldContrattoNoleggio contratto, Integer daysToInvoice, boolean lastInvoice) {
        Map<String, List<MROldRigaDocumentoFiscale>> mapRighe = new HashMap<String, List<MROldRigaDocumentoFiscale>>();
        List<List<MROldRigaDocumentoFiscale>> rows = FatturaUtils.calculateRowsUtils(sx, contratto,
                contratto.getTariffa(), contratto.getCommissione().getGiorniVoucher(), true);
        Double totalDaysAgreement = FormattedDate.numeroGiorni(contratto.getInizio(), contratto.getFine(), true);
//        totalDaysAgreement = MathUtils.round(totalDaysAgreement, 5);

        ResourceBundle bundleFatt = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");
        ResourceBundle bundleDb = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
        String labelOptionalUnitaMisura = bundleDb.getString("Optional.UnitaMisuraGG");
        String labelGiorniExtra = bundleFatt.getString("FatturaUtils.msgListino0GiorniExtra");
        String labelGiorniTariffa = bundleFatt.getString("FatturaUtils.msgListino0GiorniTariffa");
        labelGiorniExtra = bundleFatt.getString("FatturaUtils.msgListino0GiorniExtra").split("-")[1];
        labelGiorniTariffa = bundleFatt.getString("FatturaUtils.msgListino0GiorniTariffa").split("-")[1];
//        System.out.println("giorni extra bundle = " + labelGiorniExtra);
//        System.out.println("giorni tariffa bundle = " + labelGiorniTariffa);

//        for (int iB = 0; iB < rows.size(); iB++) {
        List<MROldRigaDocumentoFiscale> listRighe = rows.get(1);
        if (listRighe != null) {
            MROldRigaDocumentoFiscale giorniExtra = null;
            MROldRigaDocumentoFiscale giorniTariffa = null;
//                        MROldRigaDocumentoFiscale km = null;
//                        MROldRigaDocumentoFiscale carburanteMancante = null;

            for (MROldRigaDocumentoFiscale focusRow : listRighe) {

                if (focusRow.getDescrizione() != null) {

                    if (focusRow.getDescrizione().contains(labelGiorniExtra)) {

                        giorniExtra = focusRow;
                    } else if (focusRow.getDescrizione().contains(labelGiorniTariffa)) {

                        giorniTariffa = focusRow;
                    }
//                                else if(focusRow.getDescrizione().equals(bundleFatt.getString("FatturaUtils.msgMaterialeConsumo0"))){
//                                    carburanteMancante = focusRow;
//                                }
                }

            }

            MROldRigaDocumentoFiscale noMoreExtraDaysForMonthlyBilling = null;
            if (giorniTariffa != null) {
                noMoreExtraDaysForMonthlyBilling = giorniTariffa;
                listRighe.remove(giorniTariffa);

                if (giorniExtra != null) {
                    listRighe.remove(giorniExtra);
                    noMoreExtraDaysForMonthlyBilling.setQuantita(giorniExtra.getQuantita() + giorniTariffa.getQuantita());
                    noMoreExtraDaysForMonthlyBilling.setTotaleRiga(giorniExtra.getTotaleRiga() + giorniTariffa.getTotaleRiga());
                    noMoreExtraDaysForMonthlyBilling.setTotaleImponibileRiga(giorniExtra.getTotaleImponibileRiga() + giorniTariffa.getTotaleImponibileRiga());
                    noMoreExtraDaysForMonthlyBilling.setTotaleIvaRiga(giorniExtra.getTotaleIvaRiga() + giorniTariffa.getTotaleIvaRiga());
                }
                totalDaysAgreement = noMoreExtraDaysForMonthlyBilling.getQuantita();
                listRighe.add(noMoreExtraDaysForMonthlyBilling);
            }

            List<MROldRigaDocumentoFiscale> newListRigheDefault = new ArrayList<MROldRigaDocumentoFiscale>();
            boolean isGiorniNoleggio = false;
            for (MROldRigaDocumentoFiscale rdf : listRighe) {

                MROldRigaDocumentoFiscale newRiga = rdf;

                //divide ogni riga 'giorni noleggio' per il numero di giorni da fatturare
                //it splits every rental days rows for the days number to invoice
                if (rdf.getUnitaMisura() != null && rdf.getUnitaMisura().equals(labelOptionalUnitaMisura)
                    //                            && rdf.getQuantita() == totalDaysAgreement
                        ) {
                    Double quantita = 1.0 * daysToInvoice;
                    newRiga.setQuantita(quantita);
                    Double totaleRighe = (rdf.getTotaleRiga() / totalDaysAgreement) * daysToInvoice;
                    Double totaleImponibile = (rdf.getTotaleImponibileRiga() / totalDaysAgreement) * daysToInvoice;
                    Double totaleIva = (rdf.getTotaleIvaRiga() / totalDaysAgreement) * daysToInvoice;
                    newRiga.setTotaleRiga(totaleRighe);
                    newRiga.setTotaleImponibileRiga(totaleImponibile);
                    newRiga.setTotaleIvaRiga(totaleIva);
                    isGiorniNoleggio = true;
                }


                ///////////////////////////////////////////////////////////////////////
                if(isGiorniNoleggio || lastInvoice){
                    newListRigheDefault.add(newRiga);
                }
                isGiorniNoleggio = false;

            }

            mapRighe.put(CO_TYPE, newListRigheDefault);
        }

        return mapRighe;
    }

    private static Map<MROldPartita, MROldDocumentoFiscale> createInvoiceOperatore(Session sx, List<MROldRigaDocumentoFiscale> righe, MROldContrattoNoleggio contratto,
                                                        MROldSede sedeOperativa, MROldPagamento pagamento, Date date, boolean prepagato,
                                                        Date inizioFatturazione, Date fineFatturazione, Integer numberToApply, User aUser) throws TitledException{
        Map<MROldPartita, MROldDocumentoFiscale> docAndPartita = null;
        righe = FatturaUtils.removeEmptyCharges(righe);
        MROldFonteCommissione fonte = contratto.getCommissione().getFonteCommissione();


        MROldDocumentoFiscale aFattura = null;
        if (righe != null && righe.size() > 0) {
//            MROldNumerazione numerazioneSelezionata = null;
            int indexFattura = 0;

            MROldNumerazione numerazioneRegistrazione = null;
            MROldNumerazione numerazioneProtocollo = null;
            MROldNumerazione numerazionePartite = null;

            Integer primoNumeroProtocollo = null; //fatture invoice
            Integer primoNumeroRegistrazione = null; // primenote
            Integer primoNumeroPartita = null; //partite


            if(!"Z".equals(fonte.getRentalType().getDescription())){
                //normal enumeration logic (that will result in assigning EA - 10000000 enumeration
                numerazioneRegistrazione = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, aUser);
                numerazioneProtocollo = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.VENDITE, aUser);
                numerazionePartite = NumerazioniUtils.getNumerazione(sx, aUser.getAffiliato(), MROldNumerazione.PARTITE);

            }
            else{
                //logic for Z enumeration
                numerazioneRegistrazione  = NumerazioniUtils.findOrCreateNumerazioneSede(sx, sedeOperativa, MROldNumerazione.PRIMENOTE);
                numerazionePartite  = NumerazioniUtils.findOrCreateNumerazioneSede(sx, sedeOperativa, MROldNumerazione.PARTITE);
                numerazioneProtocollo = NumerazioniUtils.findOrCreateNumerazioneSede(sx, sedeOperativa, MROldNumerazione.VENDITE);
            }

            if(numberToApply != null){
                primoNumeroProtocollo = numberToApply;
            }
            else{
                primoNumeroProtocollo = NumerazioniUtils.aggiornaProgressivo(
                        sx,
                        numerazioneProtocollo,
                        FormattedDate.annoCorrente(date),
                        1);
            }

            primoNumeroRegistrazione = NumerazioniUtils.aggiornaProgressivo(
                    sx,
                    numerazioneRegistrazione,
                    FormattedDate.annoCorrente(date),
                    1);

            primoNumeroPartita = NumerazioniUtils.aggiornaProgressivo(
                    sx,
                    numerazionePartite,
                    0,
                    1);


            try {
                docAndPartita = createFatturaOperatore(sx,
                        contratto,
                        fonte,
                        sedeOperativa,
                        pagamento,
                        righe,
                        date,
                        numerazioneProtocollo,
                        numerazioneRegistrazione,
                        numerazionePartite,
                        primoNumeroPartita,
                        primoNumeroRegistrazione,
                        primoNumeroProtocollo,
                        prepagato,
                        indexFattura,
                        inizioFatturazione,
                        fineFatturazione,
                        aUser);
            } catch (TitledException tex) {
                throw tex;
            }

        }

        return docAndPartita;
    }

    private static MROldDocumentoFiscale createInvoice(Session sx, List<MROldRigaDocumentoFiscale> righe, MROldContrattoNoleggio contratto,
                                                  MROldFonteCommissione fonte,MROldSede sedeOperativa, MROldPagamento pagamento, Date date,
                                                  boolean prepagato, Date inizioFatturazione, Date fineFatturazione, User user) {
        if (fonte == null && contratto != null) {
            righe = FatturaUtils.removeEmptyCharges(righe);
            fonte = contratto.getCommissione().getFonteCommissione();
        }

        MROldDocumentoFiscale aFattura = null;
        if (righe != null && righe.size() > 0) {
            MROldNumerazione numerazioneSelezionata = null;
            int indexFattura = 0;

            MROldNumerazione numerazioneRegistrazione = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE, user);
            MROldNumerazione numerazioneProtocollo = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.VENDITE, user);
            MROldNumerazione numerazioneProtocolloProforma = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PROFORMA, user);
            MROldNumerazione numerazionePartite = NumerazioniUtils.getNumerazione(sx, user.getAffiliato(), MROldNumerazione.PARTITE);

            Integer primoNumeroRegistrazione = NumerazioniUtils.aggiornaProgressivo(
                    sx,
                    numerazioneRegistrazione,
                    FormattedDate.annoCorrente(date),
                    1);


            Integer primoNumeroPartita = NumerazioniUtils.aggiornaProgressivo(
                    sx,
                    numerazionePartite,
                    0,
                    1);

            createInvoiceCounter++;
            System.out.println("createInvoiceCounter = " + createInvoiceCounter);
//            if (createInvoiceCounter == 53) {
//                System.out.println("");
//            }

            if (fonte.getCliente() != null && fonte.getCliente().getPartitaIva() != null
                    && fonte.getCliente().getPartitaIva().equals("11989340150") && prepagato) {

                Integer primoNumeroProtocolloProforma = NumerazioniUtils.aggiornaProgressivo(
                        sx,
                        numerazioneProtocolloProforma,
                        FormattedDate.annoCorrente(date),
                        1);

                aFattura = createFatturaProforma(sx,
                        contratto,
                        fonte,
                        righe,
                        date,
                        numerazioneProtocolloProforma,
                        numerazionePartite,
                        primoNumeroPartita,
                        primoNumeroProtocolloProforma,
                        prepagato,
                        indexFattura,
                        inizioFatturazione,
                        fineFatturazione);

                Integer iAnno = 2017;


                if(aFattura != null){
                    if (aFattura.getAnno() == null || (aFattura.getAnno() != null && aFattura.getAnno().equals(0))) {
                        iAnno = DayUtils.getFieldFromDate(date, Calendar.YEAR);
                    }
                    aFattura = (MROldDocumentoFiscale) checkNumerazioneDoppelGanger(sx, "MROldDocumentoFiscale",  aFattura, numerazioneProtocolloProforma, iAnno);
                }


            } else {
                Integer primoNumeroProtocollo = NumerazioniUtils.aggiornaProgressivo(
                        sx,
                        numerazioneProtocollo,
                        FormattedDate.annoCorrente(date),
                        1);

                aFattura = createFattura(sx,
                        contratto,
                        fonte,
                        sedeOperativa,
                        pagamento,
                        righe,
                        date,
                        numerazioneProtocollo,
                        numerazioneRegistrazione,
                        numerazionePartite,
                        primoNumeroPartita,
                        primoNumeroRegistrazione,
                        primoNumeroProtocollo,
                        prepagato,
                        indexFattura,
                        inizioFatturazione,
                        fineFatturazione,
                        user);

                Integer iAnno = 2017;

                if(aFattura != null){
                    if (aFattura.getAnno() == null || (aFattura.getAnno() != null && aFattura.getAnno().equals(0))) {
                        iAnno = DayUtils.getFieldFromDate(date, Calendar.YEAR);
                    }
                    aFattura = (MROldDocumentoFiscale)checkNumerazioneDoppelGanger(sx, "MROldDocumentoFiscale",  aFattura, numerazioneProtocollo, iAnno);
                }
            }
        }
//    public void checkNumerazioneDoppelGanger(String clasz, String numero, String prefisso, ProgessivoInterface p, MROldNumerazione numerazione, Integer anno)


        return aFattura;
    }

//    /**
//     * Used for extraprepay invoices
//     * @param righe
//     * @param contratto
//     * @param date
//     * @param prepagato
//     * @param inizioFatturazione
//     * @param fineFatturazione
//     * @return
//     */
//    private static MROldDocumentoFiscale createInvoice(List<MROldRigaDocumentoFiscale> righe, MROldContrattoNoleggio contratto,MROldSede sedeOperativa, MROldPagamento pagamento, Date date,
//            boolean prepagato, Date inizioFatturazione, Date fineFatturazione) {
//       Session sx = HibernateBridge.startNewSession();
//       MROldFonteCommissione fonte = (MROldFonteCommissione) DatabaseUtils.refreshPersistentInstanceWithSx(sx,
//               MROldFonteCommissione.class, contratto.getCommissione().getFonteCommissione());
//
//        righe = FatturaUtils.removeEmptyCharges(righe);
//
//
//
//        MROldDocumentoFiscale aFattura = null;
//        if (righe != null && righe.size() > 0) {
//            int indexFattura = 0;
//
//            MROldNumerazione numerazioneRegistrazione = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE);
//            MROldNumerazione numerazioneProtocollo = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.VENDITE);
//            MROldNumerazione numerazionePartite = NumerazioniUtils.getNumerazione(sx, MROldAffiliato.getNoleggiatore(), MROldNumerazione.PARTITE);
//
//            Integer primoNumeroRegistrazione = NumerazioniUtils.aggiornaProgressivo(
//                    sx,
//                    numerazioneRegistrazione,
//                    FormattedDate.annoCorrente(date),
//                    1);
//            Integer primoNumeroProtocollo = NumerazioniUtils.aggiornaProgressivo(
//                    sx,
//                    numerazioneProtocollo,
//                    FormattedDate.annoCorrente(date),
//                    1);
//
//            Integer primoNumeroPartita = NumerazioniUtils.aggiornaProgressivo(
//                    sx,
//                    numerazionePartite,
//                    0,
//                    1);
//
//
//                aFattura = createFattura(sx,
//                        contratto,
//                        fonte,
//                        sedeOperativa,
//                        pagamento,
//                        righe,
//                        date,
//                        numerazioneProtocollo,
//                        numerazioneRegistrazione,
//                        numerazionePartite,
//                        primoNumeroPartita,
//                        primoNumeroRegistrazione,
//                        primoNumeroProtocollo,
//                        prepagato,
//                        indexFattura,
//                        inizioFatturazione,
//                        fineFatturazione);
//
//                Integer iAnno = 2017;
//
//                if(aFattura != null){
//                    if (aFattura.getAnno() == null || (aFattura.getAnno() != null && aFattura.getAnno().equals(0))) {
//                        iAnno = DayUtils.getFieldFromDate(date, Calendar.YEAR);
//                    }
//                    aFattura = (MROldDocumentoFiscale)checkNumerazioneDoppelGanger("MROldDocumentoFiscale",  aFattura, numerazioneProtocollo, iAnno);
//                }
//
//        }
//
//
//        return aFattura;
//    }

    public List<MROldDocumentoFiscale> getInvoicesMapValues() {
        List<MROldDocumentoFiscale> all = new ArrayList<MROldDocumentoFiscale>();
        for (List<MROldDocumentoFiscale> inMap : invoicesMap.values()) {
            all.addAll(inMap);
        }
        return all;
    }

    /**
     * This method will return the normal invoices plus the monthlybilling
     * invoices
     *
     * @return
     */
    private List<MROldDocumentoFiscale> getInvoices() {
        List<MROldDocumentoFiscale> all = new ArrayList<MROldDocumentoFiscale>();
        for (List<MROldDocumentoFiscale> inMap : invoicesMap.values()) {
            all.addAll(inMap);
        }

        return all;
    }

    public List<MROldDocumentoFiscale> getSummaryInvoices() {
        List<MROldDocumentoFiscale> sums = new ArrayList<MROldDocumentoFiscale>();
        for (List<MROldDocumentoFiscale> inSum : summaryInvoicesMap.values()) {
            sums.addAll(inSum);
        }
        return sums;
    }

    public List<MROldDocumentoFiscale> makeAllInvoices(Session sx, User aUserInvoice, User user) {

        int i = 0;
        int cont = 0;
        System.out.println("contratti trovati = " + contrattiList.size());
//        if(listenerProgressBar != null){
//            listenerProgressBar.update(0);
//            listenerProgressBar.setVisible(true);
//            listenerProgressBar.setMax(contrattiList.size());
//            listenerProgressBar.setText("Caricando i contratti...");
//        }

        for (MROldContrattoNoleggio focusContratto : contrattiList) {
            i++;
//            if(listenerProgressBar != null && !listenerProgressBar.getIsStopped()){
//                listenerProgressBar.update(i);
//            }
//            else{
//                return null;
//            }

            //first create all the invoices
            if (focusContratto != null) {
                cont++;

                MROldFonteCommissione focusFonte = focusContratto.getCommissione().getFonteCommissione();

                invoiceManagerCounterMain++;
                System.out.println("CounterMain = " + invoiceManagerCounterMain);
                if (Boolean.TRUE.equals(focusFonte.getIsMonthlyBilled())) {

                    try {
                        monthlyBilling(sx, focusContratto, dataTo, aUserInvoice);
                    } catch (TariffaNonValidaException ex) {
                        ex.printStackTrace();
                    } catch (FatturaVuotaException ex) {
                        ex.printStackTrace();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
                else {

                    try {
                        Map<String, List<MROldRigaDocumentoFiscale>> mapRighe = calculateRighe(sx, focusContratto);
                        invoiceManager(focusContratto, sx, mapRighe, dataFattura, focusFonte, focusContratto.getInizio(), focusContratto.getFine(), aUserInvoice);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }



            } else {
                contrattiNull++;
                System.out.println("contrattiNull = " + contrattiNull);
            }

        }
        System.out.println("contratti processati = " + cont);

        for (PersistentInstance pers : invoicesMap.keySet()) {
            if (pers instanceof MROldFonteCommissione) {
                MROldFonteCommissione fonte = (MROldFonteCommissione) pers;
                List<MROldRigaDocumentoFiscale> righePrePay = new ArrayList();
                List<MROldRigaDocumentoFiscale> righeCli = new ArrayList();

                //adds all the rows and then make a unique invoice for each fonte
                for (MROldDocumentoFiscale doc : invoicesMap.get(pers)) {
                    if(doc != null && Boolean.TRUE.equals(doc.getPrepagato())){
                        righePrePay.addAll(doc.getFatturaRighe());
                    }
                    else{
                        righeCli.addAll(doc.getFatturaRighe());
                    }

                }


                List<MROldDocumentoFiscale> fattureList = new ArrayList<MROldDocumentoFiscale>();
                if (righePrePay.size() > 0) {
                    MROldDocumentoFiscale focusDoc = null;
//                    righe = FatturaUtils.cleanRighe(righe);

                    try{
                        MROldSede sedeOperativa = aUserInvoice.getSedeOperativa();
                        //MROldPagamento pagamento = (MROldPagamento) sx.get(MROldPagamento.class, idNumberBonifico30gg);
                        MROldPagamento pagamento = (MROldPagamento) sx.get(MROldPagamento.class, 301);
                        focusDoc = createInvoice(sx, righePrePay, null, fonte,sedeOperativa, pagamento, dataFattura, true,
                                contrattiList.get(0).getFine(), this.dataTo, user);

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    if(focusDoc != null){
                        fattureList.add(focusDoc);
                    }
                }
                if(fattureList.size() > 0){
                    invoicesMap.put(fonte, fattureList);
                }
            }
        }
//        if(listenerProgressBar != null){
//            listenerProgressBar.setVisible(false);
//        }
        try{
//            printResults();
        }
        catch(Exception e){
            e.printStackTrace();
        }


        return getInvoicesMapValues();

    }

    public List<MROldDocumentoFiscale> addAllInovicesVirtualAndMade(Session sx, Boolean prepagato){
        List<MROldDocumentoFiscale> result = new ArrayList<MROldDocumentoFiscale>();
        for(MROldContrattoNoleggio con : contrattiList){
            List<MROldDocumentoFiscale> fattureList = invoicesMap.get(con);
            if (fattureList == null) {
                fattureList = new ArrayList<MROldDocumentoFiscale>();
            }
            MROldFonteCommissione fonte = con.getCommissione().getFonteCommissione();
            List<MROldDocumentoFiscale> doc = null;
            if(fonte != null && Boolean.TRUE.equals(fonte.getIsMonthlyBilled())){
                if(Boolean.FALSE.equals(prepagato)){
                    doc = caricaDocumentiFiscali(sx, con, prepagato);
                }
            }
            else{
                if(prepagato == null){
                    doc = caricaDocumentiFiscali(sx, con);
                }
                else{
                    doc = caricaDocumentiFiscali(sx, con, prepagato);
                }

            }
            if(doc != null && doc.size() > 0){
                fattureList.addAll(doc);
                invoicesMap.put(con, fattureList);
                result = fattureList;
            }
        }
        return result;

    }


    private static List<MROldDocumentoFiscale> caricaDocumentiFiscali(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        List<MROldDocumentoFiscale> fatture = new ArrayList<MROldDocumentoFiscale>();
        try {

            Query qx = sx.createQuery(
                    "select x from MROldDocumentoFiscale x where " + //NOI18N
                            "x.contratto = :contratto "
                            + "order by x.data desc, x.numero desc"); //NOI18N
            qx.setParameter("contratto", contrattoNoleggio); //NOI18N

            List l = qx.list();
            if(l != null && l.size() > 0){
                fatture.addAll(l);
            }

        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return fatture;
    }

    private static List<MROldDocumentoFiscale> caricaDocumentiFiscali(Session sx, MROldContrattoNoleggio contrattoNoleggio, Boolean prepagato) {
        List<MROldDocumentoFiscale> fatture = null;
        try {

            Query qx = sx.createQuery(
                    "select x from MROldDocumentoFiscale x where " + //NOI18N
                            "x.contratto = :contratto and " + //NOI18N
                            "x.prepagato = :prepagato "
                            + "order by x.data desc, x.numero desc"); //NOI18N
            qx.setParameter("contratto", contrattoNoleggio); //NOI18N
            qx.setParameter("prepagato", prepagato); //NOI18N
            fatture = (List<MROldDocumentoFiscale>) qx.list();

        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        return fatture;
    }

    public static List<MROldRigaDocumentoFiscale> createRigheCustomerAuto(Session sx, MROldContrattoNoleggio contratto) {
        List<MROldRigaDocumentoFiscale> listRiga = new ArrayList<MROldRigaDocumentoFiscale>();
        Integer litri = 0;
        MROldTariffa focusTariffa = (MROldTariffa) contratto.getTariffa();
        MROldMovimentoAuto focusMovimento = (MROldMovimentoAuto) contratto.getMovimento();
        if (focusMovimento.getCombustibileInizio() != null && focusMovimento.getCombustibileFine() != null) {
            litri = focusMovimento.getCombustibileInizio() - focusMovimento.getCombustibileFine();
        }
        litri = litri < 0 ? 0 : litri;
        MROldParcoVeicoli focusVeicolo = (MROldParcoVeicoli) focusMovimento.getVeicolo();
        FatturazioneBreveTermine fbt = new FatturazioneBreveTermine(
                contratto,
                focusTariffa,
                contratto.getInizio(),
                contratto.getFine(),
                focusMovimento.getKilometriTotaliPercorsi() != null ? focusMovimento.getKilometriTotaliPercorsi() : 0,
                litri,
                focusVeicolo.getCarburante(),
                contratto.getScontoTariffa(),
                contratto.getCommissione().getGiorniVoucher());
        try {

            if(fbt.calcolaRigheProssimaFattura(sx) != null){
                listRiga.addAll(fbt.calcolaRigheProssimaFattura(sx));
            }
        } catch (Exception e) {
            wrongRatesAgreement.add(contratto);
            e.printStackTrace();
            e.getLocalizedMessage();
        }
        return listRiga;
    }

    public static List<MROldRigaDocumentoFiscale> createRighePrePayAuto(Session sx, MROldContrattoNoleggio contratto) {
        List<MROldRigaDocumentoFiscale> listRiga = new ArrayList<MROldRigaDocumentoFiscale>();
        Integer litri = 0;
        MROldTariffa focusTariffa = contratto.getTariffa();
        MROldMovimentoAuto focusMovimento = contratto.getMovimento();
        if (focusMovimento.getCombustibileInizio() != null && focusMovimento.getCombustibileFine() != null) {
            litri = focusMovimento.getCombustibileInizio() - focusMovimento.getCombustibileFine();
        }
        litri = litri < 0 ? 0 : litri;
        MROldParcoVeicoli focusVeicolo = (MROldParcoVeicoli) focusMovimento.getVeicolo();
        FatturazionePrepagato fp = new FatturazionePrepagato(
                contratto,
                focusTariffa,
                contratto.getInizio(),
                contratto.getFine(),
                focusMovimento.getKilometriTotaliPercorsi() != null ? focusMovimento.getKilometriTotaliPercorsi() : 0,
                litri,
                focusVeicolo.getCarburante(),
                contratto.getScontoTariffa(),
                contratto.getCommissione().getGiorniVoucher());
        try {

            if(fp.calcolaRigheProssimaFattura(sx) != null){
                listRiga.addAll(fp.calcolaRigheProssimaFattura(sx));
            }
        } catch (Exception e) {
            wrongRatesAgreement.add(contratto);
            e.printStackTrace();
            e.getLocalizedMessage();
        }
        return listRiga;
    }


    public static Map<MROldPartita, MROldDocumentoFiscale> createInvoiceAuto(Session sx, MROldContrattoNoleggio contratto, MROldSede sedeoperativa, MROldPagamento pagamento,  Boolean prepagato, Integer numberToApply, User aUser) throws TitledException{

        Map<MROldPartita, MROldDocumentoFiscale> newDocAndPart = null;
        List<MROldRigaDocumentoFiscale> listRighe = new ArrayList<MROldRigaDocumentoFiscale>();

        if(prepagato){
            listRighe = createRighePrePayAuto(sx, contratto);
        } else{
            listRighe = createRigheCustomerAuto(sx, contratto);
        }
        if(listRighe != null && listRighe.size() > 0){
            listRighe = FatturaUtils.cleanRighe(listRighe);
            try{
                newDocAndPart = createInvoiceOperatore(sx, listRighe, contratto, sedeoperativa, pagamento, new Date(), prepagato, new Date(), new Date(), numberToApply, aUser);
            } catch (TitledException tex) {
                throw tex;
            }
        } else {
            throw new TitledException(bundle.getString("Fatturazione.msgNessunaVoceDaFatturare"),bundle.getString("Fatturazione.msgFatturaVuota"));
        }
        return newDocAndPart;
    }





    private static MROldDocumentoFiscale createFatturaProforma(Session sx,
                                                          MROldContrattoNoleggio contratto,
                                                          MROldFonteCommissione fonte,
                                                          List<MROldRigaDocumentoFiscale> righe,
                                                          Date date,
                                                          MROldNumerazione numerazioneProtocollo,
                                                          MROldNumerazione numerazionePartite,
                                                          Integer primoNumeroPartita,
                                                          Integer primoNumeroProtocollo ,
                                                          Boolean prepagato,
                                                          Integer indexFattura,
                                                          Date inizioFatturazione,
                                                          Date fineFatturazione) {
        MROldPartita associatedPartita = null;
        //fonte = (MROldFonteCommissione) DatabaseUtils.refreshPersistentInstanceWithSx(sx, MROldFonteCommissione.class, fonte);
        MROldDocumentoFiscale aFattura = null;
        for (int i = 0; i < righe.size(); i++) {

            try {
                if (aFattura == null) {
                    try {
                        aFattura = DocumentoFiscaleFactory.newIntestazioneFatturaProforma(
                                sx,
                                contratto,
                                date,
                                numerazioneProtocollo,
                                primoNumeroProtocollo + i,
                                prepagato);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (fonte != null && contratto != null && Boolean.TRUE.equals((fonte.getIsSummaryinvoice()))) {
                        String cliente = "";
                        if (contratto.getCliente() != null) {
                            cliente = contratto.getCliente().getNome() + " " + contratto.getCliente().getCognome();
                        } else if (contratto.getConducente1() != null) {
                            cliente = contratto.getConducente1().getNome() + " " + contratto.getConducente1().getCognome();
                        }
                        String voucher = "";
                        if (contratto.getCommissione() != null && contratto.getCommissione().getCodiceVoucher() != null) {
                            voucher = contratto.getCommissione().getCodiceVoucher();
                        }
                        String descrizione = "RA EA " + contratto.getNumero() + " VOUCHER " + voucher + " del " + contratto.getData() + " CLIENTE " + cliente ;
                        MROldRigaDocumentoFiscale rigaIntestazioneSummary = new MROldRigaDocumentoFiscale();
                        rigaIntestazioneSummary.setDescrizione(descrizione);
                        righe.add(0, rigaIntestazioneSummary);
                    }

                }
                if (aFattura != null) {
                    aFattura.setFatturaRighe(righe);
                    aFattura.setPagamento(findPaymentForAgreement(sx, contratto));

                    try {
                        Map<MROldPartita, MROldDocumentoFiscale> newDocAndPart = null;
                        newDocAndPart = DocumentoFiscaleFactory.newFatturaAutomaticaProformaAndPartita(sx, aFattura, prepagato,
                                numerazionePartite, primoNumeroPartita + indexFattura);


                        for(MROldDocumentoFiscale d: newDocAndPart.values()){
                            aFattura = d;
                        }
                        for(MROldPartita p : newDocAndPart.keySet()){
                            associatedPartita = p;
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }



                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(aFattura != null){
            aFattura.setPrepagato(prepagato);
            aFattura.setData(date);
            aFattura.setInizioFatturazione(inizioFatturazione);
            aFattura.setFineFatturazione(fineFatturazione);
            if(prepagato){
                aFattura.setCliente(fonte.getCliente());
            }
            else if(contratto != null){
                if(contratto.getInvoiceTo() != null){
                    aFattura.setCliente(contratto.getInvoiceTo());
                }
                else if(contratto.getCliente() != null){
                    aFattura.setCliente(contratto.getCliente());
                }
                else if(contratto.getConducente1() != null){
                    aFattura.setCliente(contratto.getConducente1());
                }

            }

            aFattura.setContratto(contratto);

            insertPartitaInMap(aFattura, associatedPartita);
        }

        return aFattura;
    }


    private static MROldDocumentoFiscale createFattura(Session sx,
                                                  MROldContrattoNoleggio contratto,
                                                  MROldFonteCommissione fonte,
                                                  MROldSede sedeOperativa,
                                                  MROldPagamento pagamento,
                                                  List<MROldRigaDocumentoFiscale> righe,
                                                  Date date,
                                                  MROldNumerazione numerazioneProtocollo,
                                                  MROldNumerazione numerazioneRegistrazione,
                                                  MROldNumerazione numerazionePartite,
                                                  Integer primoNumeroPartita,
                                                  Integer primoNumeroRegistrazione,
                                                  Integer primoNumeroProtocollo ,
                                                  Boolean prepagato,
                                                  Integer indexFattura,
                                                  Date inizioFatturazione,
                                                  Date fineFatturazione,
                                                       User user) {

        MROldPartita associatedPartita = null;
        MROldDocumentoFiscale aFattura = null;
        for (int i = 0; i < righe.size(); i++) {

            try {
                if (aFattura == null) {
                    try {
                        aFattura = DocumentoFiscaleFactory.newIntestazioneFattura(
                                sx,
                                contratto,
                                date,
                                numerazioneProtocollo,
                                primoNumeroProtocollo + i,
                                prepagato);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (fonte != null && contratto != null && Boolean.TRUE.equals(fonte.getIsSummaryinvoice()) && true == prepagato) {
                        String cliente = "";
                        if (contratto.getCliente() != null) {
                            cliente = contratto.getCliente().getNome() + " " + contratto.getCliente().getCognome();
                        } else if (contratto.getConducente1() != null) {
                            cliente = contratto.getConducente1().getNome() + " " + contratto.getConducente1().getCognome();
                        }
                        String voucher = "";
                        if (contratto.getCommissione() != null && contratto.getCommissione().getCodiceVoucher() != null) {
                            voucher = contratto.getCommissione().getCodiceVoucher();
                        }
                        String descrizione = "|RA EA " + contratto.getNumero() + " |VOUCHER " + voucher + " |del " + contratto.getData() + " |CLIENTE " + cliente + "|";
                        MROldRigaDocumentoFiscale rigaIntestazioneSummary = new MROldRigaDocumentoFiscale();
                        rigaIntestazioneSummary.setDescrizione(descrizione);
                        righe.add(0, rigaIntestazioneSummary);
                    }

                }
                if (aFattura != null) {
//                    for(MROldRigaDocumentoFiscale r : righe){
//                        System.out.println("CreateFattura - codice sottoconto" + r.getCodiceSottoconto());
//                        System.out.println("CreateFattura - veicolo" + r.getVeicolo());
//                    }
                    aFattura.setFatturaRighe(righe);


                    try {
                        if(contratto != null){
                            aFattura.setContratto(contratto);
                        }
                        if(pagamento != null){
                            aFattura.setPagamento(pagamento);
                        }

                        Map<MROldPartita, MROldDocumentoFiscale> newDocAndPart = null;
                        newDocAndPart = DocumentoFiscaleFactory.newFatturaAutomaticaSaldoAndPartita(sx, aFattura, sedeOperativa, prepagato,
                                numerazioneRegistrazione,
                                primoNumeroRegistrazione + righe.size(),
                                numerazionePartite, primoNumeroPartita + indexFattura, user);

                        for(MROldDocumentoFiscale d: newDocAndPart.values()){
                            aFattura = d;
                        }
                        for(MROldPartita p : newDocAndPart.keySet()){
                            associatedPartita = p;
                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(aFattura != null){
            aFattura.setPrepagato(prepagato);
            aFattura.setData(date);
            aFattura.setInizioFatturazione(inizioFatturazione);
            aFattura.setFineFatturazione(fineFatturazione);
            if(prepagato){
                aFattura.setCliente(fonte.getCliente());
            }
            else if(contratto != null){
                if(contratto.getInvoiceTo() != null){
                    aFattura.setCliente(contratto.getInvoiceTo());
                }
                else if(contratto.getCliente() != null){
                    aFattura.setCliente(contratto.getCliente());
                }
                else if(contratto.getConducente1() != null){
                    aFattura.setCliente(contratto.getConducente1());
                }

            }

            aFattura.setContratto(contratto);
            insertPartitaInMap(aFattura, associatedPartita);
        }
        return aFattura;

    }


    private static Map<MROldPartita, MROldDocumentoFiscale> createFatturaOperatore(Session sx,
                                                                         MROldContrattoNoleggio contratto,
                                                                         MROldFonteCommissione fonte,
                                                                         MROldSede sedeOperativa,
                                                                         MROldPagamento pagamento,
                                                                         List<MROldRigaDocumentoFiscale> righe,
                                                                         Date date,
                                                                         MROldNumerazione numerazioneProtocollo,
                                                                         MROldNumerazione numerazioneRegistrazione,
                                                                         MROldNumerazione numerazionePartite,
                                                                         Integer primoNumeroPartita,
                                                                         Integer primoNumeroRegistrazione,
                                                                         Integer primoNumeroProtocollo ,
                                                                         Boolean prepagato,
                                                                         Integer indexFattura,
                                                                         Date inizioFatturazione,
                                                                         Date fineFatturazione, User user) throws TitledException{

        Map<MROldPartita, MROldDocumentoFiscale> newDocAndPart = null;
        MROldPartita associatedPartita = null;
        //fonte = (MROldFonteCommissione) DatabaseUtils.refreshPersistentInstanceWithSx(sx, MROldFonteCommissione.class, fonte);
        MROldDocumentoFiscale aFattura = null;
        for (int i = 0; i < righe.size(); i++) {

            try {
                if (aFattura == null) {
                    try {
                        aFattura = DocumentoFiscaleFactory.newIntestazioneFattura(
                                sx,
                                contratto,
                                date,
                                numerazioneProtocollo,
                                primoNumeroProtocollo,
                                prepagato);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (fonte != null && contratto != null && Boolean.TRUE.equals(fonte.getIsSummaryinvoice()) && true == prepagato) {
                        String cliente = "";
                        if (contratto.getCliente() != null) {
                            cliente = contratto.getCliente().getNome() + " " + contratto.getCliente().getCognome();
                        } else if (contratto.getConducente1() != null) {
                            cliente = contratto.getConducente1().getNome() + " " + contratto.getConducente1().getCognome();
                        }
                        String voucher = "";
                        if (contratto.getCommissione() != null && contratto.getCommissione().getCodiceVoucher() != null) {
                            voucher = contratto.getCommissione().getCodiceVoucher();
                        }
                        String descrizione = "|RA EA " + contratto.getNumero() + " |VOUCHER " + voucher + " |del " + contratto.getData() + " |CLIENTE " + cliente + "|";
                        MROldRigaDocumentoFiscale rigaIntestazioneSummary = new MROldRigaDocumentoFiscale();
                        rigaIntestazioneSummary.setDescrizione(descrizione);
                        righe.add(0, rigaIntestazioneSummary);
                    }

                }
                if (aFattura != null) {
//                    for(MROldRigaDocumentoFiscale r : righe){
//                        System.out.println("CreateFattura - codice sottoconto" + r.getCodiceSottoconto());
//                        System.out.println("CreateFattura - veicolo" + r.getVeicolo());
//                    }
                    aFattura.setFatturaRighe(righe);


                    try {
                        if(contratto != null){
                            aFattura.setContratto(contratto);
                        }
                        if(pagamento != null){
                            aFattura.setPagamento(pagamento);
                        }


                        newDocAndPart = DocumentoFiscaleFactory.newFatturaAutomaticaSaldoAndPartita(sx, aFattura, sedeOperativa, prepagato,
                                numerazioneRegistrazione,
                                primoNumeroRegistrazione + righe.size(),
                                numerazionePartite, primoNumeroPartita + indexFattura, user);

                        for(MROldDocumentoFiscale d: newDocAndPart.values()){
                            aFattura = d;
                        }
                        for(MROldPartita p : newDocAndPart.keySet()){
                            associatedPartita = p;
                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (aFattura.getTotaleFattura() != null && aFattura.getTotaleFattura() < 0.03) {
            throw new TitledException(bundle.getString("PartitaFactory.msgFatturaImportoZero"), bundle.getString("PartitaFactory.msgFatturaImportoZeroTitle"));
        }
        if(aFattura != null){
            aFattura.setPrepagato(prepagato);
            aFattura.setData(date);
            aFattura.setInizioFatturazione(inizioFatturazione);
            aFattura.setFineFatturazione(fineFatturazione);
            if(prepagato){
                aFattura.setCliente(fonte.getCliente());
            }
            else if(contratto != null){
                if(contratto.getInvoiceTo() != null){
                    aFattura.setCliente(contratto.getInvoiceTo());
                }
                else if(contratto.getCliente() != null){
                    aFattura.setCliente(contratto.getCliente());
                }
                else if(contratto.getConducente1() != null){
                    aFattura.setCliente(contratto.getConducente1());
                }

            }

            aFattura.setContratto(contratto);
            Integer iAnno = 2017;
            if(aFattura != null){

                if (aFattura.getAnno() == null || (aFattura.getAnno() != null && aFattura.getAnno().equals(0))) {
                    iAnno = DayUtils.getFieldFromDate(date, Calendar.YEAR);
                }
                aFattura = (MROldDocumentoFiscale)checkNumerazioneDoppelGanger(sx, "MROldDocumentoFiscale",  aFattura, numerazioneProtocollo, iAnno);
            }
        }
        newDocAndPart.put(associatedPartita, aFattura);
        return newDocAndPart;

    }

    private void printResults(){

        System.out.println("numero contratti selezionati = " + contrattiList.size());
        Integer testNumeroFattura = 0;
        for (List<MROldDocumentoFiscale> testDoc : invoicesMap.values()) {

            for (int in = 0; in < testDoc.size(); in++) {
                testNumeroFattura++;
                System.out.println("=====================FATTURA CREATA NUMERO " + testNumeroFattura + "==============================");
                System.out.println("numero righe = " + testDoc.get(in).getFatturaRighe().size());
                System.out.println("totale righe = " + testDoc.get(in).getTotaleRighe());
                System.out.println("prepagato = " + testDoc.get(in).getPrepagato());
                MROldBusinessPartner intestatario = (MROldBusinessPartner) testDoc.get(in).getCliente();
                if (intestatario != null) {
                    System.out.println("nome cliente = " + intestatario.getCognome());
                    System.out.println("ragione sociale = " + intestatario.getRagioneSociale());
                }

                String numeroContratto = "";
                if (testDoc.get(in).getContratto() == null) {
                    System.out.println("SUMMARY INVOICE");
                } else {
                    numeroContratto = String.valueOf(testDoc.get(in).getContratto().getNumero());
                }
                System.out.println("numero contratto abbinato = " + numeroContratto);
//                    }
                System.out.println("=====================FATTURA CREATA NUMERO " + testNumeroFattura + "==============================\n");
            }
        }
    }

    public static boolean isAllAgreementBilled(Session sx, MROldContrattoNoleggio con){

        List<MROldRigaDocumentoFiscale> allRows = new ArrayList();
        allRows.addAll(InvoiceEAUtils.createRigheCustomerAuto(sx, con));
        allRows.addAll(InvoiceEAUtils.createRighePrePayAuto(sx, con));

        Double totNotInvoiced = 0.0;
        for(MROldRigaDocumentoFiscale r: allRows){
            if(r != null && r.getTotaleRiga() != null){
                totNotInvoiced += r.getTotaleRiga();
            }
        }

        if(totNotInvoiced > 0.0){

            return false;
        }
        else {

            return true;
        }

    }

    public static HashMap<PersistentInstance, List<MROldDocumentoFiscale>> getInvoicesMap() {
        return invoicesMap;
    }

    public static MROldProgressivoInterface checkNumerazioneDoppelGanger(Session sx, String clasz, MROldProgressivoInterface p, MROldNumerazione numerazione, Integer anno){
        boolean isDuplicated = true;

        while (isDuplicated) {
            String hql = "from "+clasz+" as c "
                    + " WHERE "
                    + "c.numero = :numero and "
                    + " c.prefisso = :prefisso";
            //                    + " and c.anno = :anno";

            List lista = sx.createQuery(hql)
                    .setParameter("numero", p.getNumero())
                    .setParameter("prefisso", p.getPrefisso())
                            //                    .setParameter("anno", getContratto().getAnno())
                    .list();
            if (p.getNumerazione() == null) {
                p.setNumerazione(NumerazioniUtils.getNumerazione(sx,
                        numerazione));
            }

            if ((p.getId() == null && lista.size() > 1)
                    || (p.getId() != null && lista.size() > 1)) {

                p.setNumero(NumerazioniUtils.aggiornaProgressivo(
                        sx,
                        p.getNumerazione(),
                        anno));
            } else {

                isDuplicated = false;
            }

        }
        return p;
    }

    public static HashSet<MROldContrattoNoleggio> getWrongRatesAgreement() {
        return wrongRatesAgreement;
    }

    public static MROldPagamento findPaymentForAgreement(Session sx, MROldContrattoNoleggio contratto) {
//        if(sx == null || sx != null && !sx.isOpen()){
//            sx = HibernateBridge.startNewSession();
//        }
        MROldPagamento pagamento = null;
        if(contratto != null){
            if(contratto.getTotalDue() != null && contratto.getTotalDue() > 0.0){ //vista fattura
                pagamento = (MROldPagamento) sx.get(MROldPagamento.class, 303);
            }
            else{
                Double totalCashpayed = contratto.getTotalCashPayment() != null ? contratto.getTotalCashPayment() : 0.0;
                Double totalCCpayed = contratto.getGarazia1CCPayment() != null ? contratto.getGarazia1CCPayment() : 0.0;
                totalCCpayed += contratto.getGarazia2CCPayment() != null ? contratto.getGarazia2CCPayment() : 0.0;

                if(totalCCpayed > totalCashpayed){

                    pagamento = (MROldPagamento) sx.get(MROldPagamento.class, 5);
                }
                else{
                    pagamento = (MROldPagamento) sx.get(MROldPagamento.class, 1);
                }

            }

        }
        else{
            pagamento = (MROldPagamento) sx.get(MROldPagamento.class, 1);
        }
        return pagamento;
    }
}
