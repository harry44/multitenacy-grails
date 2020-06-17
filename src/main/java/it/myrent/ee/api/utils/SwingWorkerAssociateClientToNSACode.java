package it.myrent.ee.api.utils;


import java.awt.Window;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import it.myrent.ee.db.*;
import it.myrent.ee.nsa.InsertNsa;
import it.myrent.ee.nsa.NSAhandler;
import it.myrent.ee.nsa.SearchNsa;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created by Shivangani on 10/6/2017.
 */
public class SwingWorkerAssociateClientToNSACode extends SwingWorkerAssociatorAbstract {

    private static String missingFields;
    private List<MROldContrattoNoleggio> cnList;
    SAXParserFactory factory;
    SAXParser saxParser;
    InputSource inputSource;
    MROldClienti focusCliente;
    MROldConducenti focusConducente;
    MROldContrattoNoleggio focusContrattoNoleggio;
    MROldBusinessPartner focusBusinessPartner;
    Boolean isInNSA;
    NSAhandler.NSAhandlerListener nsaListener;
    ArrayList<MROldBusinessPartner> notInsertedBParteners;
    ArrayList<MROldBusinessPartner> insertedBParteners;
    HashMap<MROldBusinessPartner, String> notInserted;
    HashMap<Integer, List<MROldBusinessPartner>> agreementBpInserted;
    private InsertResponseListener insertResponseListener;

    private String focusNsaCode;
    private static final int SINGLE_SEARCH = 0;
    private static final int SINGLE_INSERT = 1;
    private int choice = 2;
    private boolean isInserting;
    //private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/gui/archivi/Bundle");
    private boolean canInsert;
    Session sx;

    public interface InsertResponseListener {

        void isInNsa(Boolean b);
    }

    public SwingWorkerAssociateClientToNSACode(Session sx) throws ParserConfigurationException, SAXException {
        //super(parent, loadingMessage);
        cnList = new ArrayList();
        this.focusCliente = null;
        this.focusConducente = null;
        focusContrattoNoleggio = null;
        isInNSA = false;
        canInsert = false;
        isInserting = false;
        notInserted = new HashMap<MROldBusinessPartner, String>();
        insertedBParteners = new ArrayList<MROldBusinessPartner>();
        notInsertedBParteners = new ArrayList<MROldBusinessPartner>();
        agreementBpInserted = new HashMap<Integer, List<MROldBusinessPartner>>();
        this.sx = sx;

    }

    /**
     * 0 = single search 1 = single insert 2 = search in agreement between
     * initial and final dates.
     *
     * @param ch
     */
    public void setChoice(int ch, MROldBusinessPartner bp) {
        this.choice = ch;
        //bp = (MROldBusinessPartner) DatabaseUtils.refreshPersistentInstanceWithSx(HibernateBridge.startNewSession(), BusinessPartner.class, bp);
        this.focusBusinessPartner = bp;
    }

    public Boolean insertSingleElement(MROldBusinessPartner selectedBp) {

        try {
            searchSingleElement(selectedBp);
        } catch (SAXException ex) {
            ex.getLocalizedMessage();
        } catch (IOException ex) {
            ex.getLocalizedMessage();
        } catch (ParserConfigurationException ex) {
            ex.getLocalizedMessage();
        }

        if (isInNSA == false && canInsert) {
            if (selectedBp.getCliente()) {
                focusCliente = (MROldClienti) sx.get(MROldClienti.class, selectedBp.getId());
            } else if (selectedBp.getConducente()) {
                focusConducente = (MROldConducenti) sx.get(MROldConducenti.class,
                        selectedBp.getId());
            }

            try {
                insertNsa();
            } catch (Exception ex) {
                ex.printStackTrace();
                ex.getLocalizedMessage();
            }
        }

        return isInNSA;
    }

    public MROldBusinessPartner insertWithoutMultiThread(MROldBusinessPartner bp){
        focusBusinessPartner = bp;
        insertSingleElement(focusBusinessPartner);
        if (isInNSA && focusNsaCode != null && canInsert) {
            try {
                if (focusNsaCode != null) {
                    MROldBusinessPartner clienteToSave = (MROldBusinessPartner) sx.get(MROldBusinessPartner.class, focusBusinessPartner.getId());
                    clienteToSave.setCodiceNSA(focusNsaCode);
                    sx.saveOrUpdate(clienteToSave);
                    sx.flush();
                }
            } catch (HibernateException e) {
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();

            } finally {
//                if (sx != null && sx.isOpen()) {
//                    sx.close();
//                }
//                        if (insertResponseListener != null) {
//                            insertResponseListener.isInNsa(true);
//                        } else {
//
////                            JOptionPane.showMessageDialog(null, focusBusinessPartner
////                                    + " Anagrafica inserita con il Codice NSA: " + focusNsaCode);
//                        }

            }

        }
        return bp;
    }

    public String searchSingleElement(MROldBusinessPartner bp) throws SAXException, IOException, ParserConfigurationException {
        String response = "";
        if (bp != null) {
            String soapResponse = "";
            SearchNsa sn = new SearchNsa(sx);
            //bp = (MROldBusinessPartner) DatabaseUtils.refreshPersistentInstanceWithSx(HibernateBridge.startNewSession(), BusinessPartner.class, bp);

            if (Boolean.TRUE.equals(bp.getPersonaFisica()) && !Boolean.TRUE.equals(bp.getDittaIndividuale()) && bp.getCodiceFiscale() != null) {

                soapResponse = sn.processRequest("", bp.getCodiceFiscale());

            } else if (bp.getPartitaIva() != null) {
                soapResponse = sn.processRequest(bp.getPartitaIva(), "");

            }
            isInNSA = false;
            canInsert = false;
            if (soapResponse != null && !soapResponse.equals("")) {
                this.inputSource = new InputSource(new StringReader(soapResponse));
                inputSource.setEncoding("utf-8");
                this.factory = SAXParserFactory.newInstance();
                this.saxParser = factory.newSAXParser();
                setNsaListener();
                NSAhandler nsaHandler = new NSAhandler(nsaListener);
                saxParser.parse(inputSource, nsaHandler);

                if (isInNSA && focusNsaCode != null) {
                    response = focusBusinessPartner + " Codice NSA trovato nel Server: " + focusNsaCode;
                } else {
                    response = focusBusinessPartner + " Codice NSA non trovato nel Server "+ missingFields;
                }
            } else {
                response = focusBusinessPartner + " Codice fiscale o Partita Iva non presente nell'anagrafica";
            }

        }
        return response;

    }

    public void setInsertResponseListener(InsertResponseListener insertResponseListener) {
        this.insertResponseListener = insertResponseListener;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
//        JDialogProgressbar progressbar = super.getProgressbar();
//        progressbar.setVisible(true);
        boolean responseSingleElement = false;
        String textResponse = "";
        switch (choice) {
            case SINGLE_SEARCH:
//                progressbar.setIndeterminate(true);
                try {

                    textResponse = searchSingleElement(focusBusinessPartner);
                    if (focusNsaCode != null) {
                        MROldBusinessPartner clienteToSave = (MROldBusinessPartner) sx.get(MROldBusinessPartner.class, focusBusinessPartner.getId());
                        clienteToSave.setCodiceNSA(focusNsaCode);
                        sx.saveOrUpdate(clienteToSave);
                        sx.flush();
                        responseSingleElement = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getLocalizedMessage();
                } finally {
//                    if (sx != null && sx.isOpen()) {
//                        sx.close();
//                    }
                    //progressbar.setVisible(false);
                    //JOptionPane.showMessageDialog(null, textResponse);
                }
                return isInNSA;

            case SINGLE_INSERT:
                //progressbar.setIndeterminate(true);

                insertSingleElement(focusBusinessPartner);

                //progressbar.setVisible(false);
                if (isInNSA && focusNsaCode != null) {
                    try {
                        if (focusNsaCode != null) {
                            MROldBusinessPartner clienteToSave = (MROldBusinessPartner) sx.get(MROldBusinessPartner.class, focusBusinessPartner.getId());
                            clienteToSave.setCodiceNSA(focusNsaCode);
                            sx.saveOrUpdate(clienteToSave);
                            sx.flush();
                        }
                    } catch (HibernateException e) {
                        e.printStackTrace();
                    } finally {
//                        if (sx != null && sx.isOpen()) {
//                            sx.close();
//                        }
                        if (insertResponseListener != null) {
                            insertResponseListener.isInNsa(true);
                        } else {
//                            JOptionPane.showMessageDialog(null, focusBusinessPartner
//                                    + " Anagrafica inserita con il Codice NSA: " + focusNsaCode);
                        }

                    }

                } else {
                    if (insertResponseListener != null) {
                        insertResponseListener.isInNsa(false);
                    }
                    String missingData = "";
                    if (missingFields != null && !missingFields.equals("")) {
                        missingData = missingFields;
                        missingFields = "";
                    }

                    Iterator it = notInserted.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        if (!pair.getValue().equals("")) {
                            missingData += "\n NSA SERVER RESPONSE: " + pair.getValue().toString();
                        }
                        it.remove();
                    }
//                    JOptionPane.showMessageDialog(null, focusBusinessPartner
//                            + " Anagrafica non inserita " + "\n" + missingData);
                }
                return responseSingleElement;

        }

        Date from = super.getFrom();
        Date to = super.getTo();

        if (from != null && to != null) {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(to);
            calendar.set(Calendar.HOUR, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            to = calendar.getTime();
            try {


//                    Setting setting = (Setting) sx.get(Setting.class, "isSiaTest");
//                    Boolean isSiaTest;
//                    if (setting !=null && setting.getValue().equals("true")){
//                        isSiaTest = true;
//                    }
//                    else{
//                        isSiaTest = false;
//                    }

/////////////////////////////debug
//                Query query = sx.createQuery("select id from ContrattoNoleggio x WHERE x.fine >= :from AND x.fine <= :to ORDER BY x.fine ASC");
//                query.setDate("from", from);
//                query.setDate("to", to);
//                List<ContrattoNoleggio> lc = query.list();
//                for(ContrattoNoleggio c : lc){
//                    if(c.getNumero().equals(10711)){
//                        System.out.println("");
//                    }
//                }
////////////////////////////debug
                Query queryContrattoNoleggio = sx.createQuery("select id from MROldContrattoNoleggio x WHERE x.fine >= :from AND x.fine <= :to ORDER BY x.fine ASC");
                queryContrattoNoleggio.setDate("from", from);
                queryContrattoNoleggio.setDate("to", to);
                ArrayList<Integer> idContratti = (ArrayList<Integer>) queryContrattoNoleggio.list();

                String idToSend = "";

                idToSend = "";

                //progressbar.setMaximum(idContratti.size());

                //---------------
                for (int i = 0; i < idContratti.size(); i++) {
                    focusNsaCode = null;
                    try {
                        ArrayList<MROldBusinessPartner> bpOfAgreementInserted = new ArrayList<MROldBusinessPartner>();
                        focusCliente = null;
                        focusConducente = null;
                        if (!isStopWorking()) {
                            //progressbar.setValue(i);

                            Criteria critCont = sx.createCriteria(MROldContrattoNoleggio.class).add(Restrictions.eq("id", idContratti.get(i)));
                            critCont.setCacheable(true);
                            focusContrattoNoleggio = (MROldContrattoNoleggio) critCont.uniqueResult();
                            System.out.println("focusContrattoNoleggio.fine = " + focusContrattoNoleggio.getFine());
                            String soapResponse = "";
                            SearchNsa sn = new SearchNsa(sx);
                            if (focusContrattoNoleggio.getCliente() != null) {

                                focusCliente = (MROldClienti) sx.get(MROldClienti.class, focusContrattoNoleggio.getCliente().getId());

                                if (focusCliente != null) {
                                    if (focusCliente.getCodiceNSA() == null) {
                                        if (Boolean.TRUE.equals(focusCliente.getPersonaFisica()) && !Boolean.TRUE.equals(focusCliente.getDittaIndividuale())) {

                                            idToSend = focusCliente.getCodiceFiscale() != null ? focusCliente.getCodiceFiscale() : "";
                                            soapResponse = sn.processRequest("", idToSend);
                                        } else {
                                            idToSend = focusCliente.getPartitaIva() != null ? focusCliente.getPartitaIva() : "";
                                            soapResponse = sn.processRequest(idToSend, "");
                                        }
                                        isInNSA = false;
                                    } else {
                                        isInNSA = true;
                                    }

                                    focusConducente = null;

                                    startParsing(soapResponse);
                                    try {
                                        if (!sx.isOpen()) {
//                                                sx2 = HibernateBridge.openNewSession();
//                                            sx2 = HibernateBridge.startNewSession();
//                                            tx2 = sx2.beginTransaction();
                                        }
                                        if (focusNsaCode != null) {
                                            MROldBusinessPartner clienteToSave = (MROldBusinessPartner) sx.get(MROldBusinessPartner.class, focusCliente.getId());
                                            clienteToSave.setCodiceNSA(focusNsaCode);
                                            sx.saveOrUpdate(clienteToSave);
                                            bpOfAgreementInserted.add(clienteToSave);
                                            sx.flush();
                                        }

                                    } catch (Exception exc) {
                                        exc.printStackTrace();
                                        exc.getLocalizedMessage();
                                    }
                                }

                            }
                            if (focusContrattoNoleggio.getConducente1() != null) {
                                focusConducente = (MROldConducenti) sx.get(MROldConducenti.class,
                                        focusContrattoNoleggio.getConducente1().getId());

                                if (focusConducente != null) {

                                    if (focusConducente.getCodiceNSA() == null) {
                                        if (Boolean.TRUE.equals(focusConducente.getPersonaFisica()) && !Boolean.TRUE.equals(focusConducente.getDittaIndividuale()) ) {
                                            idToSend = focusConducente.getCodiceFiscale() != null ? focusConducente.getCodiceFiscale() : "";
                                            soapResponse = sn.processRequest("", idToSend);

                                        } else {
                                            idToSend = focusConducente.getPartitaIva() != null ? focusConducente.getPartitaIva() : "";
                                            soapResponse = sn.processRequest(idToSend, "");
                                        }
                                        isInNSA = false;
                                    } else {
                                        isInNSA = true;
                                    }

                                    focusCliente = null;
                                    startParsing(soapResponse);
                                    try {
                                        if (!sx.isOpen()) {
//                                                sx2 = HibernateBridge.openNewSession();
//                                            sx2 = HibernateBridge.startNewSession();
//                                            tx2 = sx2.beginTransaction();

                                        }
                                        if (focusNsaCode != null) {
                                            MROldBusinessPartner conducenteToSave = (MROldBusinessPartner) sx.get(MROldBusinessPartner.class, focusConducente.getId());
                                            conducenteToSave.setCodiceNSA(focusNsaCode);
//                                                Conducenti cond = (Conducenti)sx2.merge(focusConducente);
                                            sx.saveOrUpdate(conducenteToSave);
                                            bpOfAgreementInserted.add(conducenteToSave);
                                            sx.flush();
                                        }
                                    } catch (Exception exc) {
                                        exc.printStackTrace();
                                        exc.getLocalizedMessage();
                                    }

                                }

                            } else {
//                                    System.out.println("NO CONDUCENTE AND CLIENTE FOUND");
                            }

                            agreementBpInserted.put(focusContrattoNoleggio.getNumero(), bpOfAgreementInserted);

                            if (!sx.isOpen()) {
//                                sx2 = HibernateBridge.startNewSession();
//                                tx2 = sx2.beginTransaction();
                            }
//                            if (tx2 == null || !tx2.isActive()) {
//                                tx2 = sx2.beginTransaction();
//                            }
//
//                            tx2.commit();
                        }

                    } catch (Exception e) {
                        if (e instanceof HibernateException) {
                            e.getLocalizedMessage();
                            e.printStackTrace();
//                            if (tx2 != null) {
//                                tx.rollback();
//                            }
                        }
                    } finally {

//                        if (sx != null && sx.isOpen()) {
////                            sx2.close();
//                        }
//                            String actuallyInserted = "";
//                            for(BusinessPartner bp : insertedBParteners){
//
//                                    actuallyInserted += " - " +bp.toString();
//                                }
//                            System.out.println("Inserted partners: "+ actuallyInserted);

//                            String notSuccsessfullyInserted = "";
//                            Iterator it = notInserted.entrySet().iterator();
//                            while (it.hasNext()) {
//                                Map.Entry pair = (Map.Entry)it.next();
//                                notSuccsessfullyInserted += pair.getKey().toString() + " ERROR: " + pair.getValue().toString() + "///|";
//
//                            }
//                            System.out.println("Not inserted: "+ notSuccsessfullyInserted);
                        if (i == idContratti.size() - 1) {
                            for (MROldBusinessPartner bp : insertedBParteners) {
                                String codNsa = bp.getCodiceNSA() != null ? bp.getCodiceNSA() : "";
                                System.out.println("\n" + bp + " is inserted con Codice NSA: " + codNsa);
                            }
                            for (MROldBusinessPartner bp : notInserted.keySet()) {
                                String missingfields = notInserted.get(bp);

                                System.out.println(bp.toString() + " ERROR: " + missingfields);
                            }

                            for (Integer agreementNumber : agreementBpInserted.keySet()) {
                                List<MROldBusinessPartner> bps = agreementBpInserted.get(agreementNumber);
                                for (MROldBusinessPartner bbp : bps) {
                                    String nome = bbp.getNome() != null ? bbp.getNome() : "";
                                    String cognome = bbp.getCognome() != null ? bbp.getCognome() : "";
                                    String ragioneSociale = bbp.getRagioneSociale() != null ? bbp.getRagioneSociale() : "";
                                    String codiceNsa = bbp.getCodiceNSA() != null ? bbp.getCodiceNSA() : "";
                                    if(nome.equals("") && cognome.equals("")){
                                        System.out.println("EA "+agreementNumber +" " + ragioneSociale + " " + codiceNsa);
                                    }
                                    else{
                                        System.out.println("EA "+agreementNumber +" " + nome + " " + cognome + " " + codiceNsa);
                                    }


                                }
                                if (bps.size() == 0) {
                                    System.out.println("EA " + agreementNumber + " senza nessuna anagrafica NSA inserita");
                                }
                            }

                            List notIns = new ArrayList();
                            notIns.addAll(notInserted.keySet());
                            System.out.println("NUMERO DI ANAGRAFICHE INSERITE: " + insertedBParteners.size());
//                            JAnagraficaBusinessPartner jAnagraficaBPnotInserted = new JAnagraficaBusinessPartner();
//                            jAnagraficaBPnotInserted.setMinimumSize(new Dimension(1480, 1000));
//                            jAnagraficaBPnotInserted.setTitle("Anagrafiche NON importate");
//                            jAnagraficaBPnotInserted.loadValues(notIns);
//                            jAnagraficaBPnotInserted.show();

//                            JAnagraficaBusinessPartner jAnagraficaBPInserted = new JAnagraficaBusinessPartner();
//                            jAnagraficaBPInserted.setMinimumSize(new Dimension(1480, 1000));
//                            jAnagraficaBPInserted.setTitle("Anagrafiche correttamente importate");
//                            jAnagraficaBPInserted.loadValues(this.insertedBParteners);
//                            jAnagraficaBPInserted.show();
                        }

                    }
                }

            } catch (HibernateException e) {

//                if (tx != null) {
//                    tx.rollback();
//                    e.printStackTrace();
//                    e.getLocalizedMessage();
//                }
                e.printStackTrace();
                e.getLocalizedMessage();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
//                if (sx != null && sx.isOpen()) {
//                    sx.close();
//                }

            }
            return true;
        } else {
            return false;
        }
    }

    public void insertNsa() throws Exception {

        NSAutility nsaUtility = new NSAutility(sx);

        if (focusCliente != null) {
            if (focusCliente.getPersonaFisica() && focusCliente.getDittaIndividuale() != true) {

                if (focusCliente.getCodiceFiscale() != null
                        && focusCliente.getNome() != null && focusCliente.getCognome() != null) {
                    nsaUtility.fillFieldIfCliente(focusCliente);
                } else if (focusConducente != null && focusConducente.getCodiceFiscale() != null) {
                    nsaUtility.fillFieldIfConducente1(focusConducente);
                }
                nsaUtility.setTipoPersona("1");
            } else {

                if (focusCliente.getPartitaIva() != null) {
                    nsaUtility.fillFieldIfCliente(focusCliente);
                } else if (focusConducente != null && focusConducente.getPartitaIva() != null) {
                    nsaUtility.fillFieldIfConducente1(focusConducente);
                }
                nsaUtility.setTipoPersona("2");
                nsaUtility.setSesso("0");
            }
            if (focusCliente.getNazione() != null) {
                String focusNaz = focusCliente.getNazione().toUpperCase();
                if (!focusNaz.equals("ITALIA")) {
                    nsaUtility.setTipoCliente("2");
                }
            }
        } else if (focusConducente != null) {
            if (focusConducente.getPersonaFisica() && focusConducente.getDittaIndividuale() != true) {
                nsaUtility.setTipoPersona("1");
                if (focusConducente.getCodiceFiscale() != null
                        && focusConducente.getNome() != null && focusConducente.getCognome() != null) {
                    nsaUtility.fillFieldIfConducente1(focusConducente);
                } else if (focusCliente != null && focusCliente.getCodiceFiscale() != null
                        && focusCliente.getNome() != null && focusCliente.getCognome() != null) {
                    nsaUtility.fillFieldIfCliente(focusCliente);
                }
            } else {
                nsaUtility.setTipoPersona("2");
                nsaUtility.setSesso("0");
                if (focusConducente.getPartitaIva() != null) {
                    nsaUtility.fillFieldIfConducente1(focusConducente);
                } else if (focusCliente != null && focusCliente.getPartitaIva() != null) {
                    nsaUtility.fillFieldIfCliente(focusCliente);
                }
            }
            if (focusConducente.getNazione() != null) {
                String focusNaz = focusConducente.getNazione().toUpperCase();
                if (!focusNaz.equals("ITALIA")) {
                    nsaUtility.setTipoCliente("2");
                }
            }

        }
        if (focusContrattoNoleggio != null) {
            if (focusContrattoNoleggio.getInizio() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                nsaUtility.setDtPrivacy(sdf.format(focusContrattoNoleggio.getInizio()));
            }
//            nsaUtility.setTipoPrivacy(focusContrattoNoleggio.getPrivacyMessage1() ? "1" : "");
            nsaUtility.setTipoPrivacy(focusContrattoNoleggio.getPrivacyMessage2() ? "2" : "1");

        }
        else{
            Query q = sx.createQuery("from MROldContrattoNoleggio c where c.cliente = :businesspartner OR c.conducente1 = :businesspartner ORDER BY c.inizio ASC");
            if(focusCliente != null){
                q.setParameter("businesspartner", focusCliente);
            }
            else if(focusConducente != null){
                q.setParameter("businesspartner", focusConducente);
            }
            MROldContrattoNoleggio firstAgreement =(MROldContrattoNoleggio) q.setMaxResults(1).uniqueResult();
            if (firstAgreement.getInizio() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                nsaUtility.setDtPrivacy(sdf.format(firstAgreement.getInizio()));
            }
            nsaUtility.setTipoPrivacy(firstAgreement.getPrivacyMessage2() ? "2" : "1");
        }

        try {

            String nsaResponse = "";
            nsaResponse = nsaUtility.startNsa();
            if (!nsaResponse.equals("") && !nsaResponse.equals("Z")) {
                isInserting = true;
                inputSource = new InputSource(new StringReader(nsaResponse));
                inputSource.setEncoding("utf-8");
                factory = SAXParserFactory.newInstance();
                saxParser = factory.newSAXParser();
                setNsaListener();
                NSAhandler nsaHandler = new NSAhandler(nsaListener);
                saxParser.parse(inputSource, nsaHandler);
            }
//////////////this will be deleted when myrent will manage foreign clients//////////////
//            else if (nsaResponse.equals("Z")) {
//
//                missingFields = bundle.getString("SwingWorkerAssociateClientToNSACode.ForeignCostumer");
//            }
////////////////////////////////////////////////////////////////////////////////////////
            else {
                if (focusCliente != null) {
                    printMap(nsaUtility.getFieldMap());
//                    System.out.println("\n"+focusCliente + " NOT inserted "
//                            + "\nParametri mancanti: "+missingFields);
                    notInserted.put(focusCliente, missingFields);
                    notInsertedBParteners.add(focusCliente);
                } else if (focusConducente != null) {
                    printMap(nsaUtility.getFieldMap());
//                    System.out.println("\n"+focusConducente + " NOT inserted"
//                            + "\nParametri mancanti: "+missingFields);
                    notInserted.put(focusConducente, missingFields);
                    notInsertedBParteners.add(focusConducente);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setNsaListener() {
        nsaListener = new NSAhandler.NSAhandlerListener() {
            @Override
            public void nsaHandlerListener(String NSAcode, String returnedSearchStatus, String returnedDescriptionError) {
                if (returnedSearchStatus.equals("OK")) {
                    try {
                        isInNSA = true;
                        focusNsaCode = NSAcode;
                        if (focusCliente != null) {
                            focusCliente.setCodiceNSA(NSAcode);
                            if (isInserting) {
                                insertedBParteners.add(focusCliente);
                            }

                            System.out.println("\n  cliente " + focusCliente + " updated with this NSA = " + NSAcode);

                        } else if (focusConducente != null) {
                            focusConducente.setCodiceNSA(NSAcode);
                            if (isInserting) {
                                insertedBParteners.add(focusConducente);
                            }
                            System.out.println("\n conducente " + focusConducente + " updated with this NSA = " + NSAcode);
                        }

                    } catch (Exception e) {

                        e.getLocalizedMessage();
                        e.printStackTrace();
                    } finally {

                        isInserting = false;
                    }

                }else if(returnedSearchStatus != null && returnedSearchStatus.contains("NF")){

                    isInNSA = false;
                    canInsert = true;
                    if (focusCliente != null) {
                        if (isInserting) {
                            notInserted.put(focusCliente, returnedDescriptionError);
                        }
//                        System.out.println("\n  cliente "+focusCliente+" not inserted error: " + returnedDescriptionError);
                    } else if (focusConducente != null) {
                        if (isInserting) {
                            notInserted.put(focusConducente, returnedDescriptionError);
                        }
//                        System.out.println("\n conducente "+focusConducente + " not inserted error: " + returnedDescriptionError);
                    }
                    isInserting = false;
                }
                else{
                    isInNSA = false;
                    canInsert = false;
                    if (focusCliente != null) {
                        if (isInserting) {
                            notInserted.put(focusCliente, returnedDescriptionError);
                        }
//                        System.out.println("\n  cliente "+focusCliente+" not inserted error: " + returnedDescriptionError);
                    } else if (focusConducente != null) {
                        if (isInserting) {
                            notInserted.put(focusConducente, returnedDescriptionError);
                        }
//                        System.out.println("\n conducente "+focusConducente + " not inserted error: " + returnedDescriptionError);
                    }
                    missingFields = returnedDescriptionError;

                }
            }
        };

    }

    //    private String findNationISOCode(String nazione) {
//        Session sx = HibernateBridge.startNewSession();
//        NazioneISO focusN = (NazioneISO)sx.createCriteria(NazioneISO.class)
//                .add(Restrictions.eq("nome", nazione)).setMaxResults(1).uniqueResult();
//        if(focusN != null){
//            return focusN.getCodice();
//        }
//        return "";
//    }
//
//    private Comuni getComune(String citta) {
//        Session sx = HibernateBridge.startNewSession();
//        Comuni focusC = (Comuni)sx.createCriteria(Comuni.class)
//                .add(Restrictions.eq("comune", citta.toUpperCase())).setMaxResults(1).uniqueResult();
//        return focusC;
//    }
    private void startParsing(String soapResponse) throws IOException, ParserConfigurationException {
        if (soapResponse != null && !soapResponse.equals("")) {
            try {
                this.inputSource = new InputSource(new StringReader(soapResponse));
                inputSource.setEncoding("utf-8");
                this.factory = SAXParserFactory.newInstance();
                this.saxParser = factory.newSAXParser();
                setNsaListener();
                NSAhandler nsaHandler = new NSAhandler(nsaListener);
                saxParser.parse(inputSource, nsaHandler);
                if (!isInNSA && canInsert) {
                    try {
                        insertNsa();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {

                    }

                }
            } catch (SAXException ex) {

                System.out.println("soapResponse is = " + soapResponse);
                ex.getLocalizedMessage();
            }

        }
    }

    public static String getMissingFields() {
        return missingFields;
    }

    public static void printMap(Map mp) {
        missingFields = "Dati mancanti: ";
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (pair.getValue().equals("")) {
                missingFields += "\n" + pair.getKey().toString();
            }
            it.remove();
        }
    }

}

class NSAutility {

    String nominativo = "";
    String cognome = "";
    String nome = "";
    String partitaIva = "";
    String codFiscale = "";
    String dtNas = "";
    String nazNas = "";
    String provNas = "";
    String cittaNas = "";
    String sesso = "0";
    String nazioneEst = "";
    String cittaEst = "";
    String telefono = "";
    String cellulare = "";
    String email = "";
    String dtPrivacy = "01/10/2016";
    String tipoPrivacy = "1";
    String provincia = "";
    String localita = "";
    String cap = "";
    String frazione = "";
    String indirizzo = "";
    String tipoCliente = "1";
    String tipoPersona = "";

    HashMap fieldMap;
    SimpleDateFormat sdf;
    Session sx;
    //    private boolean isInserting;
    private MROldComuni fcomune;

    public NSAutility(Session sx) {
        this.sx = sx;
        fieldMap = new HashMap();
        sdf = new SimpleDateFormat("dd/MM/yyyy");

    }

    public String startNsa() {

        //Setting fake fields if city is not setted
        provincia = provincia.equals("") ? provNas : provincia;
        localita = localita.equals("") ? cittaNas : localita;
        if (cap.equals("")) {
            fcomune = getComune(cittaNas);
            if (fcomune != null) {
                cap = fcomune.getCap();
            }
        }

        if (fcomune != null) {
            nazioneEst = nazioneEst.equals("") ? findNationISOCode(fcomune.getNazione()) : nazioneEst;
        }

        indirizzo = indirizzo.equals("") ? "via" : indirizzo;

        boolean isOk = false;
        if (tipoPersona.equals("1")) {
            if (!nome.equals("") && !cognome.equals("")
                    && !codFiscale.equals("")
                    && !nazioneEst.equals("") && !sesso.equals("")
                    && !dtNas.equals("") && !nazNas.equals("")
                    && !provNas.equals("") && !cittaNas.equals("")
                    && !dtPrivacy.equals("") && !tipoPrivacy.equals("")
                    && !provincia.equals("") && !localita.equals("")
                    && !cap.equals("") && !indirizzo.equals("")
                    && !tipoCliente.equals("") && !tipoPersona.equals("")) {

                isOk = true;
            } else {

                fieldMap.put("Nome", nome);
                fieldMap.put("Cognome", cognome);
                fieldMap.put("Codice Fiscale", codFiscale);
                fieldMap.put("Codice ISO Nazione", nazioneEst);
                fieldMap.put("Sesso", sesso);
                fieldMap.put("Data di nascita", dtNas);
                fieldMap.put("Nazione di nascita", nazNas);
                fieldMap.put("Provincia di nascita", provNas);
                fieldMap.put("Citta di nascita", cittaNas);
                fieldMap.put("Data Privacy", dtPrivacy);
                fieldMap.put("Tipo Privacy", tipoPrivacy);
                fieldMap.put("Provincia", provincia);
                fieldMap.put("Localita", localita);
                fieldMap.put("CAP", cap);
                fieldMap.put("Indirizzo", indirizzo);
                fieldMap.put("Tipo di persona", tipoPersona);
                fieldMap.put("Tipo di cliente", tipoCliente);

            }

        } else if (tipoPersona.equals("2")) {
            if (!nominativo.equals("") && !partitaIva.equals("")
                    && !nazioneEst.equals("") && !sesso.equals("")
                    && !dtPrivacy.equals("") && !tipoPrivacy.equals("")
                    && !provincia.equals("") && !localita.equals("")
                    && !cap.equals("") && !indirizzo.equals("")
                    && !tipoCliente.equals("") && !tipoPersona.equals("")) {

                isOk = true;
            } else {
                fieldMap.put("Ragione sociale", nominativo);
                fieldMap.put("Partita Iva", partitaIva);
                fieldMap.put("Codice ISO Nazione", nazioneEst);
                fieldMap.put("Sesso", sesso);
                fieldMap.put("Data Privacy", dtPrivacy);
                fieldMap.put("Tipo Privacy", tipoPrivacy);
                fieldMap.put("Provincia", provincia);
                fieldMap.put("Localita", localita);
                fieldMap.put("CAP", cap);
                fieldMap.put("Indirizzo", indirizzo);
                fieldMap.put("Tipo di persona", tipoPersona);
                fieldMap.put("Tipo di cliente", tipoCliente);

            }
            codFiscale = "";
        }

        System.out.println("debug point");
        if (isOk) {

            return InsertNsa.processRequest(sx, nominativo, cognome, nome, partitaIva, codFiscale, dtNas,
                    nazNas, provNas, cittaNas, sesso, nazioneEst,
                    cittaEst, telefono, cellulare, email, dtPrivacy, tipoPrivacy,
                    provincia, localita, cap, frazione, indirizzo, tipoCliente, tipoPersona);
        } else {
            return "";
        }

    }

    public HashMap getFieldMap() {
        return fieldMap;
    }

    public void setSesso(String sesso) {
        this.sesso = sesso;
    }

    public void setTipoPrivacy(String tipoPrivacy) {
        this.tipoPrivacy = tipoPrivacy;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public void setDtPrivacy(String dtPrivacy) {
        this.dtPrivacy = dtPrivacy;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public void fillFieldIfCliente(MROldClienti focusCliente) throws Exception {

        cap = focusCliente.getCap() != null ? focusCliente.getCap() : cap;
        if (focusCliente.getCitta() != null) {
            localita = focusCliente.getCitta();
            fcomune = getComune(focusCliente.getCitta());
            if (fcomune != null) {
                cap = fcomune.getCap();
                localita = fcomune.getComune();
                provincia = fcomune.getProvincia();
                //the businesspartner live in a country that is not Italy
                if(fcomune.getCodiceStatistico().startsWith("Z")){
                    cittaEst = focusCliente.getCitta();
                    frazione = focusCliente.getCitta();
                }
            }
            else{
                cap = focusCliente.getCap();
                provincia = focusCliente.getProvincia();
                cittaEst = focusCliente.getCitta();
                frazione = focusCliente.getCitta();

            }
        }
        if (focusCliente.getNazione() != null) {
            nazioneEst = !findNationISOCode(focusCliente.getNazione()).equals("")
                    ? findNationISOCode(focusCliente.getNazione()) : nazioneEst;
        }

        nome = focusCliente.getNome() != null ? focusCliente.getNome() : nome;
        cognome = focusCliente.getCognome() != null ? focusCliente.getCognome() : cognome;
        if (focusCliente.getCodiceFiscale() != null && focusCliente.getCodiceFiscale().length() == 16) {
            codFiscale = focusCliente.getCodiceFiscale() != null ? focusCliente.getCodiceFiscale() : codFiscale;

            if(codFiscale.equals("9999999999999999")){

                sesso = focusCliente.getSesso() != null ? focusCliente.getSesso() == true ? "1" : "2" : "1";
                dtNas = focusCliente.getDataNascita() != null ? sdf.format(focusCliente.getDataNascita()) : "";
                Query findNation = sx.createQuery("from MROldNazioneISO n where n.nome = :nazioneNascita");
                findNation.setParameter("nazioneNascita", focusCliente.getNazioneNascita());
                findNation.setMaxResults(1);

                MROldNazioneISO n = (MROldNazioneISO)findNation.uniqueResult();
                String codeNation = n.getCodice();
                nazNas = codeNation;
                provNas = "AN";
                cittaNas = "Polverigi";

            }
            else{
                fillFiscalCode(focusCliente.getCodiceFiscale());
            }


        }
        partitaIva = focusCliente.getPartitaIva() != null ? focusCliente.getPartitaIva() : partitaIva;
        nominativo = focusCliente.getRagioneSociale() != null ? focusCliente.getRagioneSociale() : nominativo;
        if (nominativo.equals("")
                && Boolean.TRUE.equals(focusCliente.getPersonaFisica())
                && Boolean.TRUE.equals(focusCliente.getDittaIndividuale())) {
            nominativo = cognome + " " + nome;
        }
        email = focusCliente.getEmail() != null ? focusCliente.getEmail() : email;
        indirizzo = focusCliente.getVia() != null ? focusCliente.getVia() + " " : indirizzo;
        indirizzo += focusCliente.getNumero() != null ? focusCliente.getNumero() : "";
        setForeignFields(focusCliente);
    }

    private void fillFiscalCode(String fiscalCode) {
        if (fiscalCode != null) {
            if (!fiscalCode.equals("") ) {
                FiscalCodeCalculator fiscalCodeCalculator
                        = new FiscalCodeCalculator(fiscalCode, sx);
                sesso = fiscalCodeCalculator.getMaleOrFemale();
                dtNas = fiscalCodeCalculator.getDateOfBirth();
                nazNas = fiscalCodeCalculator.getNationality();
                provNas = fiscalCodeCalculator.getProv();
                cittaNas = fiscalCodeCalculator.getCity();
            }

        }

    }

    public void fillFieldIfConducente1(MROldConducenti focusConducente) throws Exception {
        cap = focusConducente.getCap() != null ? focusConducente.getCap() : cap;
        if (focusConducente.getCitta() != null) {
            localita = focusConducente.getCitta();
            fcomune = getComune(focusConducente.getCitta());
            if (fcomune != null) {
                cap = fcomune.getCap();
                localita = fcomune.getComune();
                provincia = fcomune.getProvincia();
                //the businesspartner live in a country that is not Italy
                if(fcomune.getCodiceStatistico()!= null && fcomune.getCodiceStatistico().startsWith("Z")){
                    cittaEst = focusConducente.getCitta();
                    frazione = focusConducente.getCitta();
                }
            }
            else{
                cap = focusConducente.getCap();
                provincia = focusConducente.getProvincia();
                cittaEst = focusConducente.getCitta();
                frazione = focusConducente.getCitta();

            }
        }
        if (focusConducente.getNazione() != null) {
            nazioneEst = !findNationISOCode(focusConducente.getNazione()).equals("")
                    ? findNationISOCode(focusConducente.getNazione()) : nazioneEst;
        }

        nome = focusConducente.getNome() != null ? focusConducente.getNome() : nome;
        cognome = focusConducente.getCognome() != null ? focusConducente.getCognome() : cognome;
        if (focusConducente.getCodiceFiscale() != null && focusConducente.getCodiceFiscale().length() == 16) {
            codFiscale = focusConducente.getCodiceFiscale() != null ? focusConducente.getCodiceFiscale() : codFiscale;
            if(codFiscale.equals("9999999999999999")){

                sesso = focusConducente.getSesso() != null ? focusConducente.getSesso() == true ? "1" : "2" : "1";
                dtNas = focusConducente.getDataNascita() != null ? sdf.format(focusConducente.getDataNascita()) : "";
                Query findNation = sx.createQuery("from MROldNazioneISO n where n.nome = :nazioneNascita");
                findNation.setParameter("nazioneNascita", focusConducente.getNazioneNascita());
                findNation.setMaxResults(1);

                MROldNazioneISO n = (MROldNazioneISO)findNation.uniqueResult();
                String codeNation = n.getCodice();
                nazNas = codeNation;
                provNas = "AN";
                cittaNas = "Polverigi";

            }
            else{
                fillFiscalCode(focusConducente.getCodiceFiscale());
            }

        }
        partitaIva = focusConducente.getPartitaIva() != null ? focusConducente.getPartitaIva() : partitaIva;
        nominativo = focusConducente.getRagioneSociale() != null ? focusConducente.getRagioneSociale() : nominativo;
        if (nominativo.equals("")
                && Boolean.TRUE.equals(focusConducente.getPersonaFisica())
                && Boolean.TRUE.equals(focusConducente.getDittaIndividuale())) {
            nominativo = cognome + " " + nome;
        }
        email = focusConducente.getEmail() != null ? focusConducente.getEmail() : email;
        indirizzo = focusConducente.getVia() != null ? focusConducente.getVia() + " " : indirizzo;
        indirizzo += focusConducente.getNumero() != null ? focusConducente.getNumero() : "";

        setForeignFields(focusConducente);

    }

    private String findNationISOCode(String nazione) {
        MROldNazioneISO focusN = (MROldNazioneISO) sx.createCriteria(MROldNazioneISO.class)
                .add(Restrictions.eq("nome", nazione)).setCacheable(true).setMaxResults(1).uniqueResult();
        if (focusN != null) {
            return focusN.getCodice();
        }
        return "";
    }

    private MROldComuni getComune(String citta) {
        MROldComuni focusC = (MROldComuni) sx.createCriteria(MROldComuni.class)
                .add(Restrictions.eq("comune", citta.toUpperCase())).setCacheable(true).setMaxResults(1).uniqueResult();
        return focusC;
    }

    private void setForeignFields(MROldBusinessPartner b) {

        if (b != null && b.getCodiceFiscale() != null
                && b.getCodiceFiscale().length() == 16
                && (b.getCodiceFiscale().substring(11, 15).startsWith("Z") || b.getCodiceFiscale().equals("9999999999999999"))) {
            if (cittaNas.equals("Polverigi")) {
                if (b.getLuogoNascita() != null) {
                    cittaNas = b.getLuogoNascita();
                }

            }

            if (provNas.equals("AN")) {
                if (b.getProvinciaNascita() != null && b.getProvinciaNascita().length() == 2) {
                    provNas = b.getProvinciaNascita();
                } else {
                    provNas = provincia;
                }

            }

            cittaEst = b.getCitta();
            frazione = b.getCitta();

        }

    }

}