/*
 * TariffeUtils.java
 *
 * Created on 30 iunie 2005, 15:11
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package it.myrent.ee.api.utils;

import grails.util.Holders;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.beanutil.ScontoComparator;
import it.myrent.ee.db.*;
import it.myrent.ee.db.MROldValiditaListinoFonte;
import java.awt.Component;
import java.text.DecimalFormat;
import java.util.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;

/**
 *
 * @author jamess
 */
public class TariffeUtils {

    /** Creates a new instance of TariffeUtils */
    private TariffeUtils() {
    }
    private static final Log log = LogFactory.getLog(TariffeUtils.class);

    /**
     * Cerca tra i listini della fonte commissionabile per trovare i listini per coprire il periodo richiesto.
     * @param sx La sessione al database
     * @param dataPrenotazione La data della prenotazione.
     * @param dataInizioNoleggio La data di inizio del noleggio.
     * @param dataFineNoleggio La data di fine del noleggio.
     * @return Un array di listini validi.
     * @throws org.hibernate.HibernateException Se qualcosa va storto...
     */
    private static List<MROldValiditaListinoFonte> cercaListini(
            Component parent,
            Session sx,
            MROldFonteCommissione fonte,
            Date dataPrenotazione,
            Date dataInizioNoleggio,
            Date dataFineNoleggio,
            MROldSede locationUscita,
            boolean silent,
            MROldGruppo gruppo) throws HibernateException {
        Date inizioStagione = FormattedDate.extractDate(dataInizioNoleggio);
        Date fineStagione = FormattedDate.extractDate(dataFineNoleggio);


        //*********************************************************************************************//
        List<MROldValiditaListinoFonte> validita = new ArrayList<MROldValiditaListinoFonte>();
        List<MROldValiditaListinoFonte> filtroListini = new ArrayList<MROldValiditaListinoFonte>();

        /***
         *  Modified for isVirtual by Mauro 2016-02-14
         *

         **/


        Set<MROldValiditaListinoFonte> listaListiniTrovati=null;

        if ((fonte.getIsvirtual()!=null)&&fonte.getIsvirtual() && fonte.getMasterReservation()!=null)
            listaListiniTrovati=fonte.getMasterReservation().getListini();
        else
            listaListiniTrovati = fonte.getListini();


        /****************************************************************************************/

        ArrayList<MROldValiditaListinoFonte> tmpLista = new ArrayList<MROldValiditaListinoFonte>();

        tmpLista.addAll(listaListiniTrovati);

        Collections.sort(tmpLista);

        /*for (int i = 0; i < tmpLista.size(); i++) {
            MROldValiditaListinoFonte v = tmpLista.get(i);
        }*/

        Iterator<MROldValiditaListinoFonte> validitaListiniFonte = tmpLista.iterator();

        boolean completato = false;
        while (!completato && validitaListiniFonte.hasNext()) {
            MROldValiditaListinoFonte validitaListinoFonte = validitaListiniFonte.next();
            if (!validitaListinoFonte.inOfferta(dataPrenotazione) || !validitaListinoFonte.inStagione(inizioStagione)){
                continue;
            }

            if (locationUscita != null) {

                Integer restrictedLocation=validitaListinoFonte.getListino().getSede()!=null?validitaListinoFonte.getListino().getSede().getId():null;
                Integer applicableLocation=validitaListinoFonte.getApplicableLocation()!=null?validitaListinoFonte.getApplicableLocation().getId():null;

                if (restrictedLocation == null && applicableLocation==null||

                        ((restrictedLocation!=null && restrictedLocation.equals(locationUscita.getId()))
                                && applicableLocation ==null) ||

                        ((restrictedLocation!=null && restrictedLocation.equals(locationUscita.getId()))
                                && (applicableLocation!=null && applicableLocation.equals(locationUscita.getId()))) ||

                        (restrictedLocation==null
                                && (applicableLocation!=null && applicableLocation.equals(locationUscita.getId())))

                        )
                {

                    /**************************************************************************************/
                    boolean gruppoTrovato = false;
                    validitaListinoFonte = (MROldValiditaListinoFonte) sx.get(MROldValiditaListinoFonte.class, validitaListinoFonte.getId());


                    Boolean isListinoWeb = validitaListinoFonte.getListino().getIsCalendar();
                    if (isListinoWeb!=null && isListinoWeb) {
                        SQLQuery query=null;
                        //SQLQuery query = sx.createSQLQuery("select count(*) as num from tariffe_listini as t where t.id_gruppo = " + gruppo.getId() + " and t.location_id = " + locationUscita.getId() + " and t.date = '" + FormattedDate.extractDate(dataInizioNoleggio) + "'");
                            query = sx.createSQLQuery("select count(*) as num from tariffe_listini as t where t.listini_id = " + validitaListinoFonte.getListino().getId() + " and t.id_gruppo = " + gruppo.getId() + " and t.location_id = " + locationUscita.getId() + " and t.date = '" + FormattedDate.extractDate(dataInizioNoleggio) + "'");

                        query.addScalar("num", IntegerType.INSTANCE);
                        //int groupQuery = ((Long) sx.createQuery("select count(*) from tariffe_listini as t where t.id_gruppo = " + gruppo.getId() + " and t.location_id = " + locationUscita.getId() + " and t.date = '" + dataInizioNoleggio + "'").uniqueResult()).intValue();
                        List<Integer> result = query.list();
                        if (result.get(0) > 0) {
                            gruppoTrovato = true;
                        }
                    }
                    else{
                        List<MROldDurata> durateQuery = (List<MROldDurata>)sx.createQuery("select d from MROldDurata d where d.listino.id = :listinoId").
                                setParameter("listinoId", validitaListinoFonte.getListino().getId()).list();
                        //MROldListino listino = (MROldListino) sx.get(MROldListino.class, validitaListinoFonte.getListino().getId());
                        //List durateListino = listino.getDurate();
                        if (durateQuery != null) {
                            List<MROldDurata> durate = new ArrayList<MROldDurata>();
                            durate.addAll(durateQuery);
                            int i=0;
                            while (!gruppoTrovato && durate.size()>i) {
                                MROldDurata durata = (MROldDurata) durate.get(i);
                                i++;
                                SQLQuery query =null;
                                    query = sx.createSQLQuery("select count(*) as num from tariffe_listini as t where t.id_durata =" + durata.getId() + " and t.id_gruppo =" + gruppo.getId());
                                query.addScalar("num", IntegerType.INSTANCE);
                                //int groupQuery = ((Long) sx.createQuery("select count(*) from tariffe_listini as t where t.id_gruppo = " + gruppo.getId() + " and t.location_id = " + locationUscita.getId() + " and t.date = '" + dataInizioNoleggio + "'").uniqueResult()).intValue();
                                List<Integer> result = query.list();
                                if (result.get(0) > 0) {
                                    gruppoTrovato = true;
                                }
                                /*
                                * changed for new listini
                                */
                                /*if(durata != null){
                                    Map tariffe = durata.getTariffe();
                                    Set gruppiTariffe = tariffe.keySet();
                                    if (gruppiTariffe != null) {
                                        Iterator it = gruppiTariffe.iterator();
                                        while (it.hasNext() && !gruppoTrovato) {
                                            MROldGruppo g = (MROldGruppo) it.next();
                                            if (gruppo.equals(g)) {
                                                gruppoTrovato = true;

                                            }
                                        }
                                    }
                                }*/
                            }
                        }
                    }



                    /*****/

                    if (validitaListinoFonte.inOfferta(dataPrenotazione) && validitaListinoFonte.inStagione(inizioStagione) && gruppoTrovato) {
                        validita.add(validitaListinoFonte);
                        if (validitaListinoFonte.inStagione(fineStagione) || !fonte.getMultistagione()) {
                            completato = true;
                        } else {
                            inizioStagione = FormattedDate.add(validitaListinoFonte.getFineStagione(), Calendar.DAY_OF_MONTH, 1);
                        }
                    }
                }
            }
        }

        if (!completato) {
            validita.clear();
        }

        return validita;
    }

    /**
     * Verifica se le stagioni della tariffa ricoprono completamente il periodo di noleggio.
     * @param tariffa La tariffa della prenotazione/contratto.
     * @param dataInizioNoleggio La data di inizio del noleggio.
     * @param dataFineNoleggio La data di fine del noleggio.
     * @return Un array di listini validi.
     * @throws org.hibernate.HibernateException Se qualcosa va storto...
     */
    public static boolean controllaCoperturaStagioniMROldTariffa(
            MROldTariffa tariffa,
            Date dataInizioNoleggio,
            Date dataFineNoleggio) {
        Date inizioStagione = FormattedDate.extractDate(dataInizioNoleggio);
        Date fineStagione = FormattedDate.extractDate(dataFineNoleggio);
        boolean coperturaPeriodo = false;
        Iterator<MROldStagioneTariffa> stagioni = tariffa.getStagioni().iterator();
        while (!coperturaPeriodo && stagioni.hasNext()) {
            MROldStagioneTariffa stagione = stagioni.next();
            if (stagione.inStagione(inizioStagione)) {
                if (!stagione.getMultistagione() || stagione.inStagione(fineStagione)) {
                    coperturaPeriodo = true;
                } else {
                    inizioStagione = FormattedDate.add(stagione.getFine(), Calendar.DAY_OF_MONTH, 1);
                }
            }
        }
        return coperturaPeriodo;
    }


    private static Map<MROldDurata, MROldTariffaListino> getTariffeListiniByCalendar(Session sx, MROldValiditaListinoFonte validita, MROldGruppo gruppo, Date inizioNoleggio, MROldSede locationUscita){
        Map<MROldDurata, MROldTariffaListino> retValue = new HashMap<MROldDurata, MROldTariffaListino>();
        Date inizioNoleggioDate = FormattedDate.extractDate(inizioNoleggio);
        for (MROldDurata durata : validita.getListino().getDurate()) {
            if(durata != null){
                MROldTariffaListino tariffeListinoDurate = (MROldTariffaListino)sx.createQuery("select t from MROldTariffaListino t where t.durata = :durata and t.gruppo = :gruppo and t.location = :location and t.date = :inizioNoleggio").
                        setParameter("durata", durata).
                        setParameter("gruppo", gruppo).
                        setParameter("location", locationUscita).
                        setParameter("inizioNoleggio", inizioNoleggioDate).list().get(0);
                retValue.put(durata, tariffeListinoDurate);
            }
        }
        return retValue;
    }


    public static MROldTariffaListino getTariffeListiniByCalendar(Session sx, MROldDurata durata, MROldGruppo gruppo, Date inizioNoleggio, MROldSede locationUscita){
        MROldTariffaListino retValue = null;
        List<MROldTariffaListino> listTariffaListino = null;
        Date inizioNoleggioDate = FormattedDate.extractDate(inizioNoleggio);
        if (durata != null) {
            listTariffaListino = sx.createQuery("select t from MROldTariffaListino t where t.durata = :durata and t.gruppo = :gruppo and t.location = :location and t.date = :inizioNoleggio").
                    setParameter("durata", durata).
                    setParameter("gruppo", gruppo).
                    setParameter("location", locationUscita).
                    setParameter("inizioNoleggio", inizioNoleggioDate).list();
            if(listTariffaListino != null && !listTariffaListino.isEmpty()){
                retValue = listTariffaListino.get(0);
            }
        }
        return retValue;
    }


    /**
     *
     * @param sx
     * @param fonte
     * @param gruppo
     * @param aeroporto
     * @param ferrovia
     * @param conducente2
     * @param conducente3
     * @param dataNascita1
     * @param dataNascita2
     * @param dataNascita3
     * @param inizioNoleggio
     * @param fineNoleggio
     * @param dataPrenotazione
     * @param locationUscita
     * @return
     * @throws HibernateException
     */
    public static MROldTariffa creaMROldTariffa(
            Session sx,
            MROldFonteCommissione fonte,
            MROldGruppo gruppo,
            Boolean aeroporto,
            Boolean ferrovia,
            Boolean conducente2,
            Boolean conducente3,
            Date dataNascita1,
            Date dataNascita2,
            Date dataNascita3,
            Date inizioNoleggio,
            Date fineNoleggio,
            Date dataPrenotazione,
            MROldSede locationUscita)
            throws HibernateException {

        fonte = (MROldFonteCommissione) sx.load(MROldFonteCommissione.class, fonte.getId());
        gruppo = (MROldGruppo) sx.load(MROldGruppo.class, gruppo.getId());

        MROldTariffa tariffa;
        tariffa = new MROldTariffa();
        tariffa.setGruppo(gruppo);
        tariffa.setMultistagione(fonte.getMultistagione());
        StringBuilder descrizione = new StringBuilder();

        List<MROldValiditaListinoFonte> listini = cercaListini(null, sx, fonte, dataPrenotazione, inizioNoleggio, fineNoleggio, locationUscita, true, gruppo);

        for (MROldValiditaListinoFonte validita : listini) {
            /*
             * changes for new Listino
             */
            if(validita.getListino().getIsCalendar() != null){
                if(validita.getListino().getIsCalendar().equals(Boolean.TRUE)){
                    /*
                     * usare la sessione hibernate per leggere la lista degli oggetti TariffaListino filtrati,
                     * fare metodo.
                     */
                    Map<MROldDurata, MROldTariffaListino> durataTariffeListinoListMap = getTariffeListiniByCalendar(sx, validita, gruppo, inizioNoleggio, locationUscita);
                    tariffa.getStagioni().add(new MROldStagioneTariffa(validita, durataTariffeListinoListMap,fonte));
                }
                else{
                    //normal listino
                    tariffa.getStagioni().add(new MROldStagioneTariffa(validita, gruppo,fonte));
                }
            }
            else{
                tariffa.getStagioni().add(new MROldStagioneTariffa(validita, gruppo,fonte ));
            }
            if (descrizione.length() > 0) {
                descrizione.append(" / ");
            }
            descrizione.append(validita.getListino().getDescrizione());
        }


        /***
         * Andiamo ad implementare la ricarca del listino per l'eventuale Fonte Extra PrePay
         * FONTE_EXTRA_PPAY
         * set as fonte.setListinoExtraPPay the first result from cercaListini from FonteExtraPPay
         */
        List<MROldValiditaListinoFonte> listiniExtraPPay=null;

        if (fonte.getFonteExtraPPay() != null) {
            listiniExtraPPay = cercaListini(null, sx, fonte.getFonteExtraPPay(), dataPrenotazione, inizioNoleggio, fineNoleggio, locationUscita, true, gruppo);

            if (listiniExtraPPay != null && !listiniExtraPPay.isEmpty() && listiniExtraPPay.get(0) != null) {
                fonte.setListinoExtraPrepay(listiniExtraPPay.get(0).getListino());

                tariffa.setIvaInclusaExtraPrepay(fonte.getListinoExtraPrepay().getIvaInclusa());
                tariffa.setCodiceIvaExtraPrepay(fonte.getListinoExtraPrepay().getCodiceIva());

                /*
                 * changes for new listino
                 */
                if (fonte.getListinoExtraPrepay().getIsCalendar() != null && fonte.getListinoExtraPrepay().getIsCalendar().equals(Boolean.TRUE)) {
                    for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                        if (durata != null) {
                            MROldTariffaListino tariffaListino = getTariffeListiniByCalendar(sx, durata, gruppo, inizioNoleggio, locationUscita);
                            if (tariffaListino != null) {
                                tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                            }
                        }
                    }
                } else {
                    //normal listino
                    for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                        if (durata != null) {
                            MROldTariffaListino tariffaListino = durata.getTariffe().get(gruppo);
                            if (tariffaListino != null) {
                                tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                            }
                        }
                    }
                }
            }
        }

        /*
         * No FonteExtraPPay, take ListinoExtraPPay
         */
        if ((fonte.getFonteExtraPPay() == null
                || ((fonte.getFonteExtraPPay() != null) && listiniExtraPPay != null && (listiniExtraPPay.isEmpty() || listiniExtraPPay.get(0) == null)))
                && fonte.getListinoExtraPrepay() != null) {

            tariffa.setIvaInclusaExtraPrepay(fonte.getListinoExtraPrepay().getIvaInclusa());
            tariffa.setCodiceIvaExtraPrepay(fonte.getListinoExtraPrepay().getCodiceIva());
            /*
             * changes for new listino
             */
            if(fonte.getListinoExtraPrepay().getIsCalendar() != null && fonte.getListinoExtraPrepay().getIsCalendar().equals(Boolean.TRUE)){
                for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                    if (durata != null) {
                        MROldTariffaListino tariffaListino = getTariffeListiniByCalendar(sx, durata, gruppo, inizioNoleggio, locationUscita);
                        if (tariffaListino != null) {
                            tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                        }
                    }
                }
            }
            else {
                //normal listino
                for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                    MROldTariffaListino tariffaListino = durata.getTariffe().get(gruppo);
                    if (tariffaListino != null) {
                        tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                    }
                }
            }
        }

        if (descrizione.length() > 0) {
            tariffa.setDescrizione(descrizione.toString());
        } else {
            //TODO Mettere "Nessun listino" come descrizione
            tariffa.setDescrizione(fonte.getRagioneSociale());
        }


        if (listini.size() > 0) {
            tariffa.setCodiceIva(listini.get(0).getListino().getCodiceIva());
            if(listini.get(0).getListino().getCodiceIvaNonSoggetto() != null){
                tariffa.setCodiceIvaNonSoggetto(listini.get(0).getListino().getCodiceIvaNonSoggetto());
            }
            else{
                tariffa.setCodiceIvaNonSoggetto(listini.get(0).getListino().getCodiceIva());
            }
            tariffa.setDepositoContanti(listini.get(0).getListino().getDepositoContanti());
            tariffa.setDepositoSenzaAss(listini.get(0).getListino().getDepositoSenzaAss());
            tariffa.setDepositoSuperAss(listini.get(0).getListino().getDepositoSuperAss());

            tariffa.setOraRientro(listini.get(0).getListino().getOraRientro());
            tariffa.setOraRientroAttiva(listini.get(0).getListino().getOraRientroAttiva());

            tariffa.setOptionalsTariffa(new HashMap());

            /*
             * changes for rateOptional
             */
            Iterator it;
            if(listini.get(0).getListino().getOptionalRate() != null){
                MROldListino optionalRate = listini.get(0).getListino().getOptionalRate();
                it = optionalRate.getOptionalsListino().iterator();
            }
            else{
                it = listini.get(0).getListino().getOptionalsListino().iterator();
            }

            while (it.hasNext()) {
                MROldOptionalListino optionalListino = (MROldOptionalListino) it.next();
                /*if (optionalListino.getOptional() != null && optionalListino.getOptional().getAppGruppo() != null && optionalListino.getOptional().getAppGruppo().equals(Boolean.TRUE) && !optionalListino.getGruppo().equals(gruppo)) {
                    continue;
                }*/
                if (optionalListino.getOptional().
                        getAppGruppo().
                        equals(Boolean.TRUE) && !optionalListino.getGruppo().
                        equals(gruppo)) {
                    continue;
                }
                MROldOptionalTariffa optionalMROldTariffa = new MROldOptionalTariffa();
                MROldOptional optional = (MROldOptional) sx.get(MROldOptional.class, optionalListino.getOptional().
                        getId());
                optionalMROldTariffa.setOptional(optional);
                optionalMROldTariffa.setImporto(optionalListino.getImporto());
                optionalMROldTariffa.setFranchigia(optionalListino.getFranchigia());
                optionalMROldTariffa.setIncluso(optionalListino.getIncluso());
                int quantita = 0;
                if (optional.getOneriAeroportuali()) {
                    optionalMROldTariffa.setSelezionato(aeroporto);
                } else if (optional.getOneriFeroviari()) {
                    optionalMROldTariffa.setSelezionato(ferrovia);
                } else if (optional.getGuidatoreAggiuntivo()) {
                    if (conducente2) {
                        quantita++;
                    }
                    if (conducente3) {
                        quantita++;
                    }
                    optionalMROldTariffa.setSelezionato(quantita > 0);
                } else if (optional.getSupplementoEta()) {
                    Integer anniMinimo = optional.getEtaMinima();
                    Integer anniMassimo = optional.getEtaMassima();
                    if (AnagraficaUtils.verificaEtaGuidatore(dataNascita1, inizioNoleggio, anniMinimo, anniMassimo)) {
                        quantita++;
                    }
                    if (AnagraficaUtils.verificaEtaGuidatore(dataNascita2, inizioNoleggio, anniMinimo, anniMassimo)) {
                        quantita++;
                    }
                    if (AnagraficaUtils.verificaEtaGuidatore(dataNascita3, inizioNoleggio, anniMinimo, anniMassimo)) {
                        quantita++;
                    }
                    optionalMROldTariffa.setSelezionato(quantita > 0);
                } else if (optional.getRoadTax()) {
                    optionalMROldTariffa.setSelezionato(Boolean.TRUE);
                }  else {
                    optionalMROldTariffa.setSelezionato(optionalListino.getIncluso());
                }
                if (quantita == 0 && optional.getMoltiplicabile() && optionalMROldTariffa.getSelezionato()) {
                    quantita++;
                }
                if (quantita > 0) {
                    optionalMROldTariffa.setQuantita((double) quantita);
                } else {
                    optionalMROldTariffa.setQuantita(null);
                }

                optionalMROldTariffa.setPrepagato(Boolean.FALSE);
                optionalMROldTariffa.setSelezionatoRientro(Boolean.FALSE);
                optionalMROldTariffa.setSelezionatoFranchigia(Boolean.FALSE);
                tariffa.getOptionalsTariffa().put(optional, optionalMROldTariffa);
            }
        }
        return tariffa;
    }



    public static boolean verificaOptionalGenericDiscount(MROldOptional optional, MROldFonteCommissione fonte) {
        String annotazioni = fonte.getPromemoria();
        String[] strOptionals = null;
        if (annotazioni != null) {
            strOptionals = annotazioni.split(" #");
        }
        if (strOptionals != null) {
            for (String strCode : strOptionals) {
                if (optional != null && optional.getCodice().equals(strCode))
                    return true;

            }
        }
        return false;
    }


    /**
     * <p>Imposta per questa tariffa la selezione degli optionals oneri aeroportuali, ferroviari e conducente aggiuntivo
     * secondo il valore specificato dai relativi parametri, per i parametri diversi da null.</p>
     * <p>Se la tariffa e' null oppure gli optionals sono null, non succede niente.</p>
     * <br>Per esempio, gli oneri aeroportuali verranno<br>
     * - selezionati se <tt>aeroporto==Boolean.TRUE</tt>,<br>
     * - deselezionati se <tt>aeroporto==Boolean.FALSE</tt>,<br>
     * - lasciati con la selezione corrente se <tt>aeroporto==null</tt>
     * <br> Gli optionals di supplemento eta' vengono aggiornati SEMPRE.
     */
    public static void aggiornaImpostazioniMROldTariffa(MROldTariffa tariffa, Boolean aeroporto, Boolean ferrovia, Boolean conducente2, Boolean conducente3, Date dataNascita1, Date dataNascita2, Date dataNascita3, Date inizioNoleggio) {
        if (tariffa == null || tariffa.getOptionalsTariffa() == null) {
            return;
        }
        Iterator itOptional = tariffa.getOptionalsTariffa().
                keySet().
                iterator();

        while (itOptional.hasNext()) {
            MROldOptional optional = (MROldOptional) itOptional.next();
            MROldOptionalTariffa optionalMROldTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
            int quantita = 0;
            if (optional.getOneriAeroportuali()) {
                optionalMROldTariffa.setSelezionato(aeroporto);
            } else if (optional.getOneriFeroviari()) {
                optionalMROldTariffa.setSelezionato(ferrovia);
            } else if (optional.getGuidatoreAggiuntivo()) {
                if (conducente2) {
                    quantita++;
                }
                if (conducente3) {
                    quantita++;
                }
                optionalMROldTariffa.setSelezionato(quantita > 0);
            } else if (optional.getSupplementoEta()) {
                Integer anniMinimo = optional.getEtaMinima();
                Integer anniMassimo = optional.getEtaMassima();
                if (AnagraficaUtils.verificaEtaGuidatore(dataNascita1, inizioNoleggio, anniMinimo, anniMassimo)) {
                    quantita++;
                }
                if (AnagraficaUtils.verificaEtaGuidatore(dataNascita2, inizioNoleggio, anniMinimo, anniMassimo)) {
                    quantita++;
                }
                if (AnagraficaUtils.verificaEtaGuidatore(dataNascita3, inizioNoleggio, anniMinimo, anniMassimo)) {
                    quantita++;
                }
                optionalMROldTariffa.setSelezionato(quantita > 0);
            }
            if (quantita == 0 && optional.getMoltiplicabile() && optionalMROldTariffa.getSelezionato()) {
                if (optionalMROldTariffa.getQuantita() != null && optionalMROldTariffa.getQuantita().intValue() > 0) {
                    quantita = optionalMROldTariffa.getQuantita().intValue();
                } else {
                    quantita++;
                }
            }
            if (quantita > 0) {
                optionalMROldTariffa.setQuantita((double) quantita);

            } else {
                optionalMROldTariffa.setQuantita(null);
            }
        }
    }

    /**
     *
     * @param sx
     * @param fonte
     * @param gruppo
     * @param tariffa
     * @param aeroporto
     * @param ferrovia
     * @param conducente2
     * @param conducente3
     * @param dataNascita1
     * @param dataNascita2
     * @param dataNascita3
     * @param inizioNoleggio
     * @param fineNoleggio
     * @param dataPrenotazione
     * @param locationUscita
     * @return
     * @throws HibernateException
     */
    public static MROldTariffa aggiornaMROldTariffa(
            Session sx,
            MROldFonteCommissione fonte,
            MROldGruppo gruppo,
            MROldTariffa tariffa,
            Boolean aeroporto,
            Boolean ferrovia,
            Boolean conducente2,
            Boolean conducente3,
            Date dataNascita1,
            Date dataNascita2,
            Date dataNascita3,
            Date inizioNoleggio,
            Date fineNoleggio,
            Date dataPrenotazione,
            MROldSede locationUscita)
            throws HibernateException {

        try{
            if (tariffa == null) {
                tariffa = creaMROldTariffa(
                        sx,
                        fonte,
                        gruppo,
                        aeroporto,
                        ferrovia,
                        conducente2,
                        conducente3,
                        dataNascita1,
                        dataNascita2,
                        dataNascita3,
                        inizioNoleggio,
                        fineNoleggio,
                        dataPrenotazione,
                        locationUscita);
            } else {

                fonte = (MROldFonteCommissione) sx.get(MROldFonteCommissione.class, fonte.getId());
                gruppo = (MROldGruppo) sx.get(MROldGruppo.class, gruppo.getId());

                if (!gruppo.equals(tariffa.getGruppo())) {
                    tariffa.setGruppo(gruppo);
                }

                tariffa.getImportiExtraPrepay().clear();


                /***
                 * Andiamo ad implementare la ricarca del listino per l'eventuale Fonte Extra PrePay
                 * FONTE_EXTRA_PPAY
                 *
                 */
                List<MROldValiditaListinoFonte> listiniExtraPPay = null;

                if (fonte.getFonteExtraPPay() != null) {
                    listiniExtraPPay = cercaListini(null, sx, fonte.getFonteExtraPPay(), dataPrenotazione, inizioNoleggio, fineNoleggio, locationUscita, true, gruppo);

                    if (!listiniExtraPPay.isEmpty() && listiniExtraPPay.get(0) != null) {
                        fonte.setListinoExtraPrepay(listiniExtraPPay.get(0).getListino());

                        tariffa.setIvaInclusaExtraPrepay(fonte.getListinoExtraPrepay().getIvaInclusa());
                        tariffa.setCodiceIvaExtraPrepay(fonte.getListinoExtraPrepay().getCodiceIva());

                    /*
                     * changes for new listino
                     */
                        if (fonte.getListinoExtraPrepay().getIsCalendar() != null && fonte.getListinoExtraPrepay().getIsCalendar().equals(Boolean.TRUE)) {
                            for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                                if (durata != null) {
                                    MROldTariffaListino tariffaListino = getTariffeListiniByCalendar(sx, durata, gruppo, inizioNoleggio, locationUscita);
                                    if (tariffaListino != null) {
                                        tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                                    }
                                }
                            }
                        } else {
                            //normal listino
                            for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                                if (durata != null) {
                                    MROldTariffaListino tariffaListino = durata.getTariffe().get(gruppo);
                                    if (tariffaListino != null) {
                                        tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                                    }
                                }
                            }
                        }
                    }

                }


            /*
             * No FonteExtraPPay, take ListinoExtraPPay
             */
                if ((fonte.getFonteExtraPPay() == null
                        || ((fonte.getFonteExtraPPay() != null) && listiniExtraPPay != null && (listiniExtraPPay.isEmpty() || listiniExtraPPay.get(0) == null)))
                        && fonte.getListinoExtraPrepay() != null) {

                    tariffa.setIvaInclusaExtraPrepay(fonte.getListinoExtraPrepay().getIvaInclusa());
                    tariffa.setCodiceIvaExtraPrepay(fonte.getListinoExtraPrepay().getCodiceIva());

                 /*
                 * changes for new listino
                 */
                    if (fonte.getListinoExtraPrepay().getIsCalendar() != null && fonte.getListinoExtraPrepay().getIsCalendar().equals(Boolean.TRUE)) {
                        for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                            if (durata != null) {
                                MROldTariffaListino tariffaListino = getTariffeListiniByCalendar(sx, durata, gruppo, inizioNoleggio, locationUscita);
                                if (tariffaListino != null) {
                                    tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                                }
                            }
                        }
                    } else {
                        //normal listino
                        for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                            MROldTariffaListino tariffaListino = durata.getTariffe().get(gruppo);
                            if (tariffaListino != null) {
                                tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                            }
                        }
                    }
                }

                List<MROldValiditaListinoFonte> listini = cercaListini(null, sx, fonte, dataPrenotazione, inizioNoleggio, fineNoleggio, locationUscita, true, gruppo);

                tariffa.getStagioni().clear();

                StringBuilder descrizione = new StringBuilder();

                for (MROldValiditaListinoFonte validita : listini) {
                /*
                 * changes for new Listino
                 */
                    if (validita.getListino().getIsCalendar() != null) {
                        if (validita.getListino().getIsCalendar().equals(Boolean.TRUE)) {
                        /*
                         * usare la sessione hibernate per leggere la lista degli oggetti TariffaListino filtrati,
                         * fare metodo.
                         */
                            Map<MROldDurata, MROldTariffaListino> durataTariffeListinoListMap = getTariffeListiniByCalendar(sx, validita, gruppo, inizioNoleggio, locationUscita);
                            tariffa.getStagioni().add(new MROldStagioneTariffa(validita, durataTariffeListinoListMap, fonte));
                        } else {
                            //normal listino
                            tariffa.getStagioni().add(new MROldStagioneTariffa(validita, gruppo, fonte));
                        }
                    } else {
                        tariffa.getStagioni().add(new MROldStagioneTariffa(validita, gruppo, fonte ));
                    }
                    if (descrizione.length() > 0) {
                        descrizione.append(" / ");
                    }
                    descrizione.append(validita.getListino().getDescrizione());
                }

                if (descrizione.length() > 0) {
                    tariffa.setDescrizione(descrizione.toString());
                } else {
                    //TODO Mettere "Nessun listino" come descrizione
                    tariffa.setDescrizione(fonte.getRagioneSociale());
                }

                if (listini.size() > 0) {
                    MROldListino listino = listini.get(0).getListino();

                    tariffa.setCodiceIva(listino.getCodiceIva());

                    if (listini.get(0).getListino().getCodiceIvaNonSoggetto()!=null)
                        tariffa.setCodiceIvaNonSoggetto(listini.get(0).getListino().getCodiceIvaNonSoggetto());
                    else
                        tariffa.setCodiceIvaNonSoggetto(listini.get(0).getListino().getCodiceIva());

                    tariffa.setDepositoContanti(listini.get(0).getListino().getDepositoContanti());
                    tariffa.setDepositoSenzaAss(listini.get(0).getListino().getDepositoSenzaAss());
                    tariffa.setDepositoSuperAss(listini.get(0).getListino().getDepositoSuperAss());

                    tariffa.setOraRientro(listini.get(0).getListino().getOraRientro());
                    tariffa.setOraRientroAttiva(listini.get(0).getListino().getOraRientroAttiva());

                    tariffa.setOptionalsTariffa(new HashMap());

   /*
             * changes for rateOptional
             */
                    Iterator it;
                    if(listini.get(0).getListino().getOptionalRate() != null){
                        MROldListino optionalRate = listini.get(0).getListino().getOptionalRate();
                        it = optionalRate.getOptionalsListino().iterator();
                    }
                    else{
                        it = listini.get(0).getListino().getOptionalsListino().iterator();
                    }
                    while (it.hasNext()) {
                        MROldOptionalListino optionalListino = (MROldOptionalListino) it.next();
                        if (optionalListino.getOptional().
                                getAppGruppo().
                                equals(Boolean.TRUE) && !optionalListino.getGruppo().
                                equals(gruppo)) {
                            continue;
                        }
                        MROldOptionalTariffa optionalTariffa = new MROldOptionalTariffa();
                        MROldOptional optional = (MROldOptional) sx.get(MROldOptional.class, optionalListino.getOptional().
                                getId());
                        optionalTariffa.setOptional(optional);
                        optionalTariffa.setImporto(optionalListino.getImporto());
                        optionalTariffa.setFranchigia(optionalListino.getFranchigia());
                        optionalTariffa.setIncluso(optionalListino.getIncluso());
                        int quantita = 0;
                        if (optional.getOneriAeroportuali()) {
                            optionalTariffa.setSelezionato(aeroporto);
                        } else if (optional.getOneriFeroviari()) {
                            optionalTariffa.setSelezionato(ferrovia);
                        } else if (optional.getGuidatoreAggiuntivo()) {
                            if (conducente2) {
                                quantita++;
                            }
                            if (conducente3) {
                                quantita++;
                            }
                            optionalTariffa.setSelezionato(quantita > 0);
                        } else if (optional.getSupplementoEta()) {
                            Integer anniMinimo = optional.getEtaMinima();
                            Integer anniMassimo = optional.getEtaMassima();
                            if (AnagraficaUtils.verificaEtaGuidatore(dataNascita1, inizioNoleggio, anniMinimo, anniMassimo)) {
                                quantita++;
                            }
                            if (AnagraficaUtils.verificaEtaGuidatore(dataNascita2, inizioNoleggio, anniMinimo, anniMassimo)) {
                                quantita++;
                            }
                            if (AnagraficaUtils.verificaEtaGuidatore(dataNascita3, inizioNoleggio, anniMinimo, anniMassimo)) {
                                quantita++;
                            }
                            optionalTariffa.setSelezionato(quantita > 0);
                        } else if (optional.getRoadTax()) {
                            optionalTariffa.setSelezionato(Boolean.TRUE);
                        } else {
                            optionalTariffa.setSelezionato(optionalListino.getIncluso());
                        }
                        if (quantita == 0 && optional.getMoltiplicabile() && optionalTariffa.getSelezionato()) {
                            quantita++;
                        }
                        if (quantita > 0) {
                            optionalTariffa.setQuantita((double) quantita);
                        } else {
                            optionalTariffa.setQuantita(null);
                        }

                        optionalTariffa.setPrepagato(Boolean.FALSE);
                        optionalTariffa.setSelezionatoRientro(Boolean.FALSE);
                        optionalTariffa.setSelezionatoFranchigia(Boolean.FALSE);
                        tariffa.getOptionalsTariffa().put(optional, optionalTariffa);
                    }
                }
            }
        } catch (Exception ex){
            tariffa = null;
        }


        return tariffa;

    }


    private static void aggiornaOptionalsMROldTariffa(Session sx, MROldTariffa tariffa, MROldListino listino, MROldGruppo gruppo, Boolean aeroporto, Boolean ferrovia, Boolean conducente2) throws HibernateException {
        //Verifichiamo se il listino ha gli stessi optionals, ed aggiorniamo sia gli optionals che i prezzi.
        Iterator itMROldTariffa = tariffa.getOptionalsTariffa().keySet().iterator();
        while (itMROldTariffa.hasNext()) {
            MROldOptional optional = (MROldOptional) itMROldTariffa.next();
            MROldOptionalListino optionalListino;
            if (optional.getAppGruppo().equals(Boolean.TRUE)) {
                optionalListino = getImportoForOptional(listino, optional, gruppo);
            } else {
                optionalListino = getImportoForOptional(listino, optional, null);
            }
            if (optionalListino == null) {
                itMROldTariffa.remove();
            } else {
                MROldOptionalTariffa optionalMROldTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().
                        get(optional);
                optionalMROldTariffa.setFranchigia(optionalListino.getFranchigia());
                optionalMROldTariffa.setImporto(optionalListino.getImporto());
                optionalMROldTariffa.setIncluso(optionalListino.getIncluso());
                //Se ci sono degli optionals inclusi nel nuovo listino
                //che prima non erano inclusi quindi possibilmente non selezionati
                //bisogna selezionarli.
                if (optionalListino.getIncluso().equals(Boolean.TRUE) && !optionalListino.getOptional().getOneriAeroportuali().equals(Boolean.TRUE) && !optionalListino.getOptional().getOneriFeroviari().equals(Boolean.TRUE) && !optionalListino.getOptional().getGuidatoreAggiuntivo().equals(Boolean.TRUE)) {
                    optionalMROldTariffa.setSelezionato(Boolean.TRUE);
                }
            }
        }
        //Aggiungiamo gli optionals in piu' del listino.
        Iterator itListino = listino.getOptionalsListino().iterator();

        while (itListino.hasNext()) {
            MROldOptionalListino optionalListino = (MROldOptionalListino) itListino.next();
            if (optionalListino.getOptional().getAppGruppo().equals(Boolean.TRUE) && !optionalListino.getGruppo().equals(gruppo)) {
                continue;
            }
            MROldOptionalTariffa optionalMROldTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().
                    get(optionalListino.getOptional());
            if (optionalMROldTariffa == null) {
                optionalMROldTariffa = new MROldOptionalTariffa();
                MROldOptional optional = (MROldOptional) sx.get(MROldOptional.class, optionalListino.getOptional().
                        getId());
                optionalMROldTariffa.setOptional(optional);
                optionalMROldTariffa.setImporto(optionalListino.getImporto());
                optionalMROldTariffa.setFranchigia(optionalListino.getFranchigia());
                optionalMROldTariffa.setIncluso(optionalListino.getIncluso());
                if (optional.getOneriAeroportuali().equals(Boolean.TRUE)) {
                    optionalMROldTariffa.setSelezionato(aeroporto);
                } else if (optional.getOneriFeroviari().equals(Boolean.TRUE)) {
                    optionalMROldTariffa.setSelezionato(ferrovia);
                } else if (optional.getGuidatoreAggiuntivo().equals(Boolean.TRUE)) {
                    optionalMROldTariffa.setSelezionato(conducente2);
                } else {
                    optionalMROldTariffa.setSelezionato(optionalListino.getIncluso());
                }
                optionalMROldTariffa.setPrepagato(Boolean.FALSE);
                optionalMROldTariffa.setSelezionatoRientro(Boolean.FALSE);
                optionalMROldTariffa.setSelezionatoFranchigia(Boolean.FALSE);
                tariffa.getOptionalsTariffa().put(optional, optionalMROldTariffa);
            }
        }
    }


    private static MROldOptionalListino getImportoForOptional(MROldListino listino, MROldOptional optional, MROldGruppo gruppo) {
        //new listino
        MROldListino listinoToUse;
        if(listino != null && listino.getIsCalendar().equals(Boolean.TRUE)){
            listinoToUse = listino.getOptionalRate();
        }
        else{
            listinoToUse = listino;
        }
        if (optional != null && listinoToUse != null && listinoToUse.getOptionalsListino() != null) {
            Iterator it = listinoToUse.getOptionalsListino().
                    iterator();
            while (it.hasNext()) {
                MROldOptionalListino optionalListino = (MROldOptionalListino) it.next();
                if (new EqualsBuilder().append(gruppo, optionalListino.getGruppo()).
                        isEquals()
                        && new EqualsBuilder().append(optional, optionalListino.getOptional()).
                        isEquals()) {
                    return optionalListino;
                }
            }
        }
        return null;
    }


    /** Calcola il totale della cauzione. Se serve diviso per voci, usare
     * <code>calcolaDepositiCauzionali</code>
     */
    public static Double calcolaTotaleCauzione(Session sx, MROldTariffa tariffa, MROldPagamento pagamento) {
        double[] depositiCauzionali = calcolaDepositiCauzionali(sx, tariffa, pagamento);
        return MathUtils.roundDouble(depositiCauzionali[0] + depositiCauzionali[1]);
    }

    /**
     * Calcola il deposito cauzionale diviso in 2 voci: franchigie e deposito contanti.
     *
     * @return Double[] { totale franchigia (mezzo + accessori), deposito contanti }
     * @param tariffa La tariffa di riferimento
     */
    public static double[] calcolaDepositiCauzionali(MROldTariffa tariffa, MROldPagamento pagamento) {
        if (Boolean.FALSE.equals(pagamento.getDifferito()) && Boolean.FALSE.equals(pagamento.getScadenziario())) {
            return calcolaDepositiCauzionali(null, tariffa, Boolean.TRUE.equals(pagamento.getCartaCredito()) || Boolean.TRUE.equals(pagamento.getPrepagato()));
        } else {
            return new double[]{0.0, 0.0, 0.0};
        }
    }


    public static double[] calcolaDepositiCauzionali(Session sx, MROldTariffa tariffa, MROldPagamento pagamento) {
        if (Boolean.FALSE.equals(pagamento.getDifferito()) && Boolean.FALSE.equals(pagamento.getScadenziario())) {
            return calcolaDepositiCauzionali(sx, tariffa, Boolean.TRUE.equals(pagamento.getCartaCredito()) || Boolean.TRUE.equals(pagamento.getPrepagato()));
        } else {
            return new double[]{0.0, 0.0, 0.0};
        }
    }


    public static Double calcolaTotaleFranchigia(Session sx, MROldTariffa tariffa, MROldPagamento pagamento) {
        double[] depositiCauzionali = calcolaDepositiCauzionali(sx, tariffa, pagamento);
        return MathUtils.roundDouble(depositiCauzionali[0]);
    }

    /**
     * Calcola il deposito cauzionale diviso in 2 voci: franchigie e deposito contanti.
     *
     * @return Double[] { totale franchigia (mezzo + accessori), deposito contanti }
     * @param tariffa La tariffa di riferimento
     */
    private static double[] calcolaDepositiCauzionali(Session sx, MROldTariffa tariffa, boolean cartaDiCreditoOPrepagato) {
        /*if (sx == null || !sx.isOpen()) {
            sx = HibernateBridge.startNewSession();
        }*/
        double franchigiaFurto = 0.0;
        double franchigiaDanni = 0.0;
        double riduzioneFurto = 0.0;
        double riduzioneDanni = 0.0;
        double depositoSenzaAss = 0.0;
        double depositoSuperAss = 0.0;
        double depositoContanti = 0.0;
        double franchigiaAccessori = 0.0;
        double franchigiaMezzo = 0.0;

        boolean esisteOptionalCauzione = false;
        boolean optionalAssicurazione = false;

        MROldOptionalTariffa optionalTariffaDepositoCauzionale = null;

        if (tariffa.getDepositoSenzaAss() != null) {
            depositoSenzaAss = tariffa.getDepositoSenzaAss().doubleValue();
        }
        if (tariffa.getDepositoSuperAss() != null) {
            depositoSuperAss = tariffa.getDepositoSuperAss().doubleValue();
        }

        if (tariffa.getDepositoContanti() != null) {
            depositoContanti = tariffa.getDepositoContanti().doubleValue();
        }

        if (tariffa != null) {
            //verifica se esiste un optional tipo deposito cauzionale
            optionalTariffaDepositoCauzionale = getOptionalDepositoCauzionale(tariffa);

            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();

            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                optionalTariffa.getOptional().getAssicurazione();

                if (optionalTariffa.getSelezionato().equals(Boolean.TRUE)) {
                    //Abbiamo una franchigia assicurativa?
                    if (optional.getAssicurazione().equals(Boolean.TRUE) && (optional.getFurto().equals(Boolean.TRUE) || optional.getDanni().equals(Boolean.TRUE))) {
                        if (optional.getRiduzione().equals(Boolean.TRUE)) {
                            //ATTENZIONE! Qui e' possibile avere un optional di riduzione di entrambe le assicurazioni base.
                            if (optional.getDanni().equals(Boolean.TRUE)) {
                                riduzioneDanni = Math.max(riduzioneDanni, optional.getPercentuale().doubleValue() / 100.0);
                                franchigiaDanni = depositoSenzaAss * (1 - riduzioneDanni);
                            }
                            if (optional.getFurto().equals(Boolean.TRUE)) {
                                riduzioneFurto = Math.max(riduzioneFurto, optional.getPercentuale().doubleValue() / 100.0);
                                franchigiaFurto = depositoSenzaAss * (1 - riduzioneFurto);
                            }
                        } else if (optional.getFranchigia().equals(Boolean.TRUE)) {
                            if (optional.getDanni().equals(Boolean.TRUE)) {
                                if (optionalTariffa.getFranchigia() != null) {
                                    franchigiaDanni = optionalTariffa.getFranchigia();
                                } else {
                                    franchigiaDanni = 0d;
                                }
                            } else if (optional.getFurto().equals(Boolean.TRUE)) {
                                franchigiaFurto = optionalTariffa.getFranchigia();
                            }
                        }
                        optionalAssicurazione = true;
                    } else if (optional.getAccessorio().equals(Boolean.TRUE)) {
                        if (optionalTariffa.getFranchigia() != null) {
                            franchigiaAccessori += optionalTariffa.getFranchigia().doubleValue();
                        }
                    }

                    if (optionalTariffaDepositoCauzionale != null && (Boolean.TRUE.equals(optionalTariffaDepositoCauzionale.getSelezionato()) || Boolean.TRUE.equals(optionalTariffaDepositoCauzionale.getIncluso()))) {
                        if (optionalAssicurazione) {
                            esisteOptionalCauzione = true;
                            franchigiaMezzo = optionalTariffaDepositoCauzionale.getImporto();
                        }
                    }
                }
            }
        }

        if (esisteOptionalCauzione == true && (franchigiaFurto == 0.0 && franchigiaDanni == 0.0)) {
            if (!Preferenze.getOptionalDepositoCauzionale(sx)) {
                //non fa nulla in quanto franchigiaMezzo è già valorizzato
                //franchigiaMezzo = franchigiaMezzo * (1.0 - optionalTariffaDepositoCauzionale.getImporto());
            } else {
                if (depositoSenzaAss == 0.0) {
                    franchigiaMezzo = Math.max(franchigiaDanni, franchigiaFurto);
                } else {
                    franchigiaDanni = depositoSenzaAss * (1.0 - riduzioneDanni);
                    franchigiaFurto = depositoSenzaAss * (1.0 - riduzioneFurto);
                    franchigiaMezzo = Math.max(franchigiaDanni, franchigiaFurto);
                }
            }
        } else if (esisteOptionalCauzione == true && ((franchigiaFurto > 0.0 && riduzioneFurto < 1.0) || (franchigiaDanni > 0.0 && riduzioneDanni < 1.0))) {
            if (Preferenze.getOptionalDepositoCauzionale(sx)) {
                //non fa nulla in quanto franchigiaMezzo è già valorizzato
            } else {
                if (optionalAssicurazione) {
                    double importoMassimo = Math.max(riduzioneDanni, riduzioneFurto);
                    franchigiaMezzo = optionalTariffaDepositoCauzionale.getImporto() * (1.0 - importoMassimo);
                }
            }
        } else if (!esisteOptionalCauzione && franchigiaFurto == 0.0 && franchigiaDanni == 0.0) {
            //Nessuna assicurazione
            franchigiaMezzo = depositoSenzaAss;
        } else if (!esisteOptionalCauzione && (franchigiaFurto > 0.0 && riduzioneFurto < 1.0) || (franchigiaDanni > 0.0 && riduzioneDanni < 1.0)) {
            //Abbiamo una franchigia.
            if (depositoSenzaAss == 0.0) {
                franchigiaMezzo = Math.max(franchigiaDanni, franchigiaFurto);
            } else {
                franchigiaDanni = depositoSenzaAss * (1.0 - riduzioneDanni);
                franchigiaFurto = depositoSenzaAss * (1.0 - riduzioneFurto);
                franchigiaMezzo = Math.max(franchigiaDanni, franchigiaFurto);
            }

        } else if (!esisteOptionalCauzione && (franchigiaFurto > 0.0 && riduzioneFurto == 1.0) || (franchigiaDanni > 0.0 && riduzioneDanni == 1.0)) {
            //Abbiamo un abbattimento della franchigia.
            franchigiaMezzo = depositoSuperAss;
        }


        if (cartaDiCreditoOPrepagato) {
            depositoContanti = 0.0;
        }

        return new double[]{
                MathUtils.round(franchigiaMezzo + franchigiaAccessori), depositoContanti
        };


    }

    public static Set getScontiApplicabili(Session sx, Date aDate, User user) throws HibernateException {
        TreeSet scontiApplicabili = new TreeSet(new ScontoComparator());
        aDate = FormattedDate.formattedDate(aDate.getTime());
        user = (User) sx.get(UserImpl.class, user.getId());
        if (user.getScontiStraordinari() != null && user.getScontiStraordinari().
                size() > 0) {
            Iterator sconti = user.getScontiStraordinari().
                    iterator();
            while (sconti.hasNext()) {
                MROldSconto sconto = (MROldSconto) sconti.next();
                if (sconto.getInizioSconto().
                        getTime() <= aDate.getTime() && aDate.getTime() <= sconto.getFineSconto().
                        getTime()) {
                    scontiApplicabili.add(sconto);
                }
            }
        }
        return scontiApplicabili;
    }


    public static Double scontoMassimoApplicabile(Session sx, String username, Date dataPrenotazione, Date dataRitiro, Integer durata) throws HibernateException {
        Double scontoMassimo = (Double) sx.createQuery(
                "select u.scontoMassimo from UserImpl u " +
                        "where u.userName = :username ").
                setParameter("username", username).uniqueResult();

        Integer id = (Integer) sx.createQuery(
                "select u.id from UserImpl u " +
                        "where u.userName = :username ").
                setParameter("username", username).uniqueResult();


        if (scontoMassimo == null) {
            scontoMassimo = 0.0;
        }
        Date aDataPrenotazione = FormattedDate.extractDate(dataPrenotazione);
        Date aDataRitiro = FormattedDate.extractDate(dataRitiro);
        Double scontoStraordinario = (Double) sx.createQuery(
                "select max(s.percentuale) from MROldSconto s, UserImpl u "
                        + "where u.id = :id "
                        + "and s in elements(u.scontiStraordinari) "
                        + "and :dataPrenotazione between s.inizioSconto and s.fineSconto "
                        + "and :dataRiferimento between s.inizioRiferimento and s.fineRiferimento "
                        + "and s.durataMinima <=  :durata").
                setParameter("id", id).
                setParameter("dataPrenotazione", aDataPrenotazione).
                setParameter("dataRiferimento", aDataRitiro).
                setParameter("durata", durata).
                uniqueResult();


        if (scontoStraordinario != null) {
            scontoMassimo = Math.max(scontoMassimo, scontoStraordinario);
        }
        return scontoMassimo;
    }


    public static String optionalsMROldTariffaAsString(MROldTariffa tariffa) {
        return optionalsMROldTariffaAsString(tariffa, null);
    }


    public static String optionalsMROldTariffaAsString(MROldTariffa tariffa, ResourceBundle reportBundle) {
        StringBuilder optionals = new StringBuilder();
        StringBuilder franchigie = new StringBuilder();

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator it = tariffa.getOptionalsTariffa().keySet().iterator();
            while (it.hasNext()) {
                MROldOptional optional = (MROldOptional) it.next();
                MROldOptionalTariffa optionalMROldTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                if (!verificaOptional(optional, optionalMROldTariffa)) {
                    continue;
                }
                if (optional.getAssicurazione() && optional.getFranchigia()) {
                    appendOptional(franchigie, reportBundle, optional, optionalMROldTariffa);

                } else {
                    appendOptional(optionals, reportBundle, optional, optionalMROldTariffa);
                }
            }
        }
        if (franchigie.length() > 0) {
            if (optionals.length() > 0) {
                optionals.append("\n");
            }
            optionals.append("\n").append(franchigie);
        }
        return optionals.toString();
    }


    private static void appendOptional(StringBuilder currentBuffer, ResourceBundle reportBundle, MROldOptional optional, MROldOptionalTariffa optionalMROldTariffa) {
        if (currentBuffer.length() > 0) {
            currentBuffer.append("\n");
        }

        currentBuffer.append(optionalMROldTariffa.getOptional().getDescrizione());
        if (reportBundle != null && reportBundle.containsKey(optionalMROldTariffa.getOptional().getDescrizione())) {
            String descrizioneI18N = reportBundle.getString(optionalMROldTariffa.getOptional().getDescrizione());
            currentBuffer.append(" - ").append(descrizioneI18N);
        }
        if (!optionalMROldTariffa.getOptional().getImportoPercentuale()) {
            currentBuffer.append(" "); //NOI18N
            if (optional.getAssicurazione() && optional.getFranchigia() && optionalMROldTariffa.getFranchigia() > 0) {
                currentBuffer.append(new DecimalFormat("\u00A4 #,##0.00").format(optionalMROldTariffa.getFranchigia())); //NOI18N
            } else {
                currentBuffer.append(new DecimalFormat("\u00A4 #,##0.00").format(optionalMROldTariffa.getImporto())); //NOI18N
            }
        }
        if (!(optional.getAssicurazione() && optional.getFranchigia()) && (optional.getImportoGiornaliero() || optional.getImportoPercentuale() || optional.getImportoKm())) {
            currentBuffer.append(" "); //NOI18N
            currentBuffer.append(optional.getStringImporto());
        }
    }


    public static boolean verificaOptional(MROldOptional optional, MROldOptionalTariffa optionalMROldTariffa) {
        return optional.getOneWay()
                || optional.getLavaggio()
                || optional.getCarburante()
                || optional.getFuoriOrario()
                || optional.getGuidatoreAggiuntivo()
                || optional.getDescrizione().toLowerCase().matches("franchigia")
                || optional.getCodice().equals("APA")
                || optional.getCodice().equals("APS")
                || optional.getCodice().equals("PKEY")
                || optional.getCodice().equals("PDOC")
                || optional.getCodice().equals("PCASS")
                || optional.getCodice().equals("PTARGA")
                || optionalMROldTariffa.getIncluso()
                || optionalMROldTariffa.getSelezionato()
                || optionalMROldTariffa.getSelezionatoRientro();

    }


    public static MROldOptionalTariffa getOptionalDepositoCauzionale(MROldTariffa aTariffa) {
        if (aTariffa != null) {
            Iterator it = aTariffa.getOptionalsTariffa().values().iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                if (optionalTariffa != null) {
                    MROldOptional optional = optionalTariffa.getOptional();
                    if (optional != null && Boolean.TRUE.equals(optional.getDepositoCauzionale())) {
                        return optionalTariffa;
                    }
                }
            }
        }

        return null;
    }


    public static Double getPercentualeOneriAeroportuali(MROldTariffa tariffa) {
        Double valore = null;

        if (tariffa != null && !tariffa.getOptionalsTariffa().isEmpty()) {
            Iterator it = tariffa.getOptionalsTariffa().values().iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa optionalMROldTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalMROldTariffa.getOptional();
                if  ((optionalMROldTariffa.getSelezionato() || optionalMROldTariffa.getIncluso()) && Boolean.TRUE.equals(optional.getOneriAeroportuali())) {
                    valore = MathUtils.round(optionalMROldTariffa.getImporto().doubleValue() * 0.01, 5);
                    return 1 + valore;
                }
            }
        }

        return valore;
    }


    public static Double getPercentualeOneriFerroviari(MROldTariffa tariffa) {
        Double valore = null;

        if (tariffa != null && !tariffa.getOptionalsTariffa().isEmpty()) {
            Iterator it = tariffa.getOptionalsTariffa().values().iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalTariffa.getOptional();
                if  ((optionalTariffa.getSelezionato() || optionalTariffa.getIncluso()) && Boolean.TRUE.equals(optional.getOneriFeroviari())) {
                    valore = MathUtils.round(optionalTariffa.getImporto().doubleValue() * 0.01, 5);
                    return 1 + valore;
                }
            }
        }

        return valore;
    }

    /*Added by Saurabh */
    public static boolean controllaCoperturaStagioniTariffa(
            MROldTariffa tariffa,
            Date dataInizioNoleggio,
            Date dataFineNoleggio) {
        Date inizioStagione = FormattedDate.extractDate(dataInizioNoleggio);
        Date fineStagione = FormattedDate.extractDate(dataFineNoleggio);
        boolean coperturaPeriodo = false;
        Iterator<MROldStagioneTariffa> stagioni = tariffa.getStagioni().iterator();
        while (!coperturaPeriodo && stagioni.hasNext()) {
            MROldStagioneTariffa stagione = stagioni.next();
            if (stagione.inStagione(inizioStagione)) {
                if (!stagione.getMultistagione() || stagione.inStagione(fineStagione)) {
                    coperturaPeriodo = true;
                } else {
                    inizioStagione = FormattedDate.add(stagione.getFine(), Calendar.DAY_OF_MONTH, 1);
                }
            }
        }
        return coperturaPeriodo;
    }



    public static void aggiornaImpostazioniTariffa(Session sx,MROldTariffa tariffa1, Boolean aeroporto, Boolean ferrovia, Boolean conducente2, Boolean conducente3, Date dataNascita1, Date dataNascita2, Date dataNascita3, Date inizioNoleggio) {
        if (tariffa1 == null || tariffa1.getOptionalsTariffa() == null) {
            return;
        }
        //Session sx=HibernateBridge.startNewSession();
        log.info("tariffa id passed to aggiornaImpostazioniTariffa = "+tariffa1.getId());
        MROldTariffa tariffa=(MROldTariffa)sx.get(MROldTariffa.class,tariffa1.getId());
        Iterator itOptional = tariffa.getOptionalsTariffa().
                keySet().
                iterator();
         while (itOptional.hasNext()) {
            MROldOptional optional = (MROldOptional) itOptional.next();
            MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
            int quantita = 0;
            if (optional.getOneriAeroportuali()) {
                optionalTariffa.setSelezionato(aeroporto);
            } else if (optional.getOneriFeroviari()) {
                optionalTariffa.setSelezionato(ferrovia);
            } else if (optional.getGuidatoreAggiuntivo()) {
                if (conducente2) {
                    quantita++;
                }
                if (conducente3) {
                    quantita++;
                }
                optionalTariffa.setSelezionato(quantita > 0);
            } else if (optional.getSupplementoEta()) {
                Integer anniMinimo = optional.getEtaMinima();
                Integer anniMassimo = optional.getEtaMassima();
                if (AnagraficaUtils.verificaEtaGuidatore(dataNascita1, inizioNoleggio, anniMinimo, anniMassimo)) {
                    quantita++;
                }
                if (AnagraficaUtils.verificaEtaGuidatore(dataNascita2, inizioNoleggio, anniMinimo, anniMassimo)) {
                    quantita++;
                }
                if (AnagraficaUtils.verificaEtaGuidatore(dataNascita3, inizioNoleggio, anniMinimo, anniMassimo)) {
                    quantita++;
                }
                optionalTariffa.setSelezionato(quantita > 0);
            }
            if (quantita == 0 && optional.getMoltiplicabile() && optionalTariffa.getSelezionato()) {
                if (optionalTariffa.getQuantita() != null && optionalTariffa.getQuantita().intValue() > 0) {
                    quantita = optionalTariffa.getQuantita().intValue();
                } else {
                    quantita++;
                }
            }
            if (quantita > 0) {
                optionalTariffa.setQuantita((double) quantita);
            } else {
                optionalTariffa.setQuantita(null);
            }
        }
    }


    public static MROldTariffa creaTariffaForOptional(
            Session sx,
            MROldGruppo gruppo,
            List<MROldValiditaListinoFonte> listiniOpt,
            Date inizioNoleggio,
            Date fineNoleggio,
            Date dataPrenotazione)
            throws HibernateException {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -10);
        Date inizioStagione = c.getTime();
        c.setTime(new Date());
        c.add(Calendar.YEAR, 10);
        Date fineStagione  =c.getTime();

        gruppo = (MROldGruppo) sx.get(MROldGruppo.class, gruppo.getId());

        MROldTariffa tariffa = new MROldTariffa();
        tariffa.setGruppo(gruppo);
        StringBuilder descrizione = new StringBuilder();

        List<MROldValiditaListinoFonte> listini = listiniOpt;
        for (MROldValiditaListinoFonte validita : listini) {
            validita.setInizioStagione(inizioStagione);
            validita.setInizioOfferta(inizioStagione);
            validita.setFineOfferta(fineStagione);
            validita.setFineStagione(fineStagione);
            tariffa.getStagioni().add(new MROldStagioneTariffa(validita, gruppo, ""));
            if (descrizione.length() > 0) {
                descrizione.append(" / ");
            }
            descrizione.append(validita.getListino().getDescrizione());
        }


        if (descrizione.length() > 0) {
            tariffa.setDescrizione(descrizione.toString());
        } else {
            //TODO Mettere "Nessun listino" come descrizione
            tariffa.setDescrizione("TariffaOpt");
        }
        if (listini.size() > 0) {
//            tariffa.setCodiceIva(listini.get(0).getListino().getCodiceIva());

//            if (listini.get(0).getListino().getCodiceIvaNonSoggetto()!=null)
//                tariffa.setCodiceIvaNonSoggetto(listini.get(0).getListino().getCodiceIvaNonSoggetto());
//            else
//                tariffa.setCodiceIvaNonSoggetto(listini.get(0).getListino().getCodiceIva());


//            tariffa.setDepositoContanti(listini.get(0).getListino().getDepositoContanti());
//            tariffa.setDepositoSenzaAss(listini.get(0).getListino().getDepositoSenzaAss());
//            tariffa.setDepositoSuperAss(listini.get(0).getListino().getDepositoSuperAss());

//            tariffa.setOraRientro(listini.get(0).getListino().getOraRientro());
//            tariffa.setOraRientroAttiva(listini.get(0).getListino().getOraRientroAttiva());

//            tariffa.setOptionalsTariffa(new HashMap());
//            Iterator it = listini.get(0).getListino().getOptionalsListino().iterator();
//            while (it.hasNext()) {
//                OptionalListino optionalListino = (OptionalListino) it.next();
//                if (optionalListino.getOptional().
//                        getAppGruppo().
//                        equals(Boolean.TRUE) && !optionalListino.getGruppo().
//                        equals(gruppo)) {
//                    continue;
//                }
//                OptionalTariffa optionalTariffa = new OptionalTariffa();
//                Optional optional = (Optional) sx.get(Optional.class, optionalListino.getOptional().
//                        getId());
//                optionalTariffa.setOptional(optional);
//                optionalTariffa.setImporto(optionalListino.getImporto());
//                optionalTariffa.setFranchigia(optionalListino.getFranchigia());
//                optionalTariffa.setIncluso(optionalListino.getIncluso());
//                int quantita = 0;
//                if (quantita == 0 && optional.getMoltiplicabile() && optionalTariffa.getSelezionato()) {
//                    quantita++;
//                }
//                if (quantita > 0) {
//                    optionalTariffa.setQuantita((double) quantita);
//                } else {
//                    optionalTariffa.setQuantita(null);
//                }
//                optionalTariffa.setPrepagato(Boolean.FALSE);
//                optionalTariffa.setSelezionatoRientro(Boolean.FALSE);
//                optionalTariffa.setSelezionatoFranchigia(Boolean.FALSE);
//                tariffa.getOptionalsTariffa().put(optional, optionalTariffa);
//            }
        }
        return tariffa;
    }
    public static MROldTariffa creaTariffa(
            Session sx,
            MROldFonteCommissione fonte,
            MROldGruppo gruppo,
            Boolean aeroporto,
            Boolean ferrovia,
            Boolean conducente2,
            Boolean conducente3,
            Date dataNascita1,
            Date dataNascita2,
            Date dataNascita3,
            Date inizioNoleggio,
            Date fineNoleggio,
            Date dataPrenotazione,
            MROldSede locationUscita)
            throws HibernateException {

        fonte = (MROldFonteCommissione) sx.get(MROldFonteCommissione.class, fonte.getId());
        gruppo = (MROldGruppo) sx.get(MROldGruppo.class, gruppo.getId());

        MROldTariffa tariffa = new MROldTariffa();
        tariffa.setGruppo(gruppo);
        tariffa.setMultistagione(fonte.getMultistagione());
        StringBuilder descrizione = new StringBuilder();

        List<MROldValiditaListinoFonte> listini = cercaListini(null, sx, fonte, dataPrenotazione, inizioNoleggio, fineNoleggio, locationUscita, true,gruppo);

        for (MROldValiditaListinoFonte validita : listini) {
            /*
             * changes for new Listino
             */
            if(validita.getListino().getIsCalendar() != null){
                if(validita.getListino().getIsCalendar().equals(Boolean.TRUE)){
                    /*
                     * usare la sessione hibernate per leggere la lista degli oggetti TariffaListino filtrati,
                     * fare metodo.
                     */
                    //for (Durata durata : validita.getListino().getDurate()) {
                    Map<MROldDurata, MROldTariffaListino> durataTariffeListinoListMap = getTariffeListiniByCalendar(sx, validita, gruppo, inizioNoleggio, locationUscita);
                    tariffa.getStagioni().add(new MROldStagioneTariffa(validita, durataTariffeListinoListMap,fonte));
                }
                else{
                    //normal listino
                    tariffa.getStagioni().add(new MROldStagioneTariffa(validita, gruppo,fonte));
                }
            }
            else{
                tariffa.getStagioni().add(new MROldStagioneTariffa(validita, gruppo,fonte ));
            }
            if (descrizione.length() > 0) {
                descrizione.append(" / ");
            }
            descrizione.append(validita.getListino().getDescrizione());
        }

        /***
         * Andiamo ad implementare la ricarca del listino per l'eventuale Fonte Extra PrePay
         * FONTE_EXTRA_PPAY
         * set as fonte.setListinoExtraPPay the first result from cercaListini from FonteExtraPPay
         */
        List<MROldValiditaListinoFonte> listiniExtraPPay=null;

        if (fonte.getFonteExtraPPay() != null) {
            listiniExtraPPay = cercaListini(null, sx, fonte.getFonteExtraPPay(), dataPrenotazione, inizioNoleggio, fineNoleggio, locationUscita, true, gruppo);

            if (listiniExtraPPay != null && !listiniExtraPPay.isEmpty() && listiniExtraPPay.get(0) != null) {
                fonte.setListinoExtraPrepay(listiniExtraPPay.get(0).getListino());

                tariffa.setIvaInclusaExtraPrepay(fonte.getListinoExtraPrepay().getIvaInclusa());
                tariffa.setCodiceIvaExtraPrepay(fonte.getListinoExtraPrepay().getCodiceIva());

                /*
                 * changes for new listino
                 */
                if (fonte.getListinoExtraPrepay().getIsCalendar() != null && fonte.getListinoExtraPrepay().getIsCalendar().equals(Boolean.TRUE)) {
                    for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                        if (durata != null) {
                            MROldTariffaListino tariffaListino = getTariffeListiniByCalendar(sx, durata, gruppo, inizioNoleggio, locationUscita);
                            if (tariffaListino != null) {
                                tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                            }
                        }
                    }
                } else {
                    //normal listino
                    for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                        if (durata != null) {
                            MROldTariffaListino tariffaListino = durata.getTariffe().get(gruppo);
                            if (tariffaListino != null) {
                                tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                            }
                        }
                    }
                }
            }
        }

        /*
         * No FonteExtraPPay, take ListinoExtraPPay
         */
        if ((fonte.getFonteExtraPPay() == null
                || ((fonte.getFonteExtraPPay() != null) && listiniExtraPPay != null && (listiniExtraPPay.isEmpty() || listiniExtraPPay.get(0) == null)))
                && fonte.getListinoExtraPrepay() != null) {

            tariffa.setIvaInclusaExtraPrepay(fonte.getListinoExtraPrepay().getIvaInclusa());
            tariffa.setCodiceIvaExtraPrepay(fonte.getListinoExtraPrepay().getCodiceIva());
            /*
             * changes for new listino
             */
            if(fonte.getListinoExtraPrepay().getIsCalendar() != null && fonte.getListinoExtraPrepay().getIsCalendar().equals(Boolean.TRUE)){
                for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                    if (durata != null) {
                        MROldTariffaListino tariffaListino = getTariffeListiniByCalendar(sx, durata, gruppo, inizioNoleggio, locationUscita);
                        if (tariffaListino != null) {
                            tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                        }
                    }
                }
            }
            else {
                //normal listino
                for (MROldDurata durata : fonte.getListinoExtraPrepay().getDurate()) {
                    MROldTariffaListino tariffaListino = durata.getTariffe().get(gruppo);
                    if (tariffaListino != null) {
                        tariffa.getImportiExtraPrepay().add(new MROldImportoExtraPrepay(tariffa, durata, tariffaListino));
                    }
                }
            }
        }

        if (descrizione.length() > 0) {
            tariffa.setDescrizione(descrizione.toString());
        } else {
            //TODO Mettere "Nessun listino" come descrizione
            tariffa.setDescrizione(fonte.getRagioneSociale());
        }
        if (listini.size() > 0) {
            tariffa.setCodiceIva(listini.get(0).getListino().getCodiceIva());

            // Edited 2015-09-14 Mauro& Andrea

            if (listini.get(0).getListino().getCodiceIvaNonSoggetto()!=null)
                tariffa.setCodiceIvaNonSoggetto(listini.get(0).getListino().getCodiceIvaNonSoggetto());
            else
                tariffa.setCodiceIvaNonSoggetto(listini.get(0).getListino().getCodiceIva());

            //End modification

            tariffa.setDepositoContanti(listini.get(0).getListino().getDepositoContanti());
            tariffa.setDepositoSenzaAss(listini.get(0).getListino().getDepositoSenzaAss());
            tariffa.setDepositoSuperAss(listini.get(0).getListino().getDepositoSuperAss());

            tariffa.setOraRientro(listini.get(0).getListino().getOraRientro());
            tariffa.setOraRientroAttiva(listini.get(0).getListino().getOraRientroAttiva());

            tariffa.setOptionalsTariffa(new HashMap());

            /*
             * changes for rateOptional
             */
            Iterator it;
            if(listini.get(0).getListino().getOptionalRate() != null){
                MROldListino optionalRate = listini.get(0).getListino().getOptionalRate();
                it = optionalRate.getOptionalsListino().iterator();
            }
            else{
                it = listini.get(0).getListino().getOptionalsListino().iterator();
            }
            while (it.hasNext()) {
                MROldOptionalListino optionalListino = (MROldOptionalListino) it.next();
                if (optionalListino.getGruppo()== null && optionalListino.getOptional().
                        getAppGruppo().equals(Boolean.TRUE)) {
                    System.out.println("DEBUG");
                }
                if (optionalListino.getOptional().
                        getAppGruppo().
                        equals(Boolean.TRUE) && optionalListino.getGruppo() != null &&
                        !optionalListino.getGruppo().equals(gruppo)) {
                    continue;
                }
                MROldOptionalTariffa optionalTariffa = new MROldOptionalTariffa();
                MROldOptional optional = (MROldOptional) sx.get(MROldOptional.class, optionalListino.getOptional().
                        getId());
                optionalTariffa.setOptional(optional);
                optionalTariffa.setImporto(optionalListino.getImporto());
                optionalTariffa.setFranchigia(optionalListino.getFranchigia());
                optionalTariffa.setIncluso(optionalListino.getIncluso());
                int quantita = 0;
                if (optional.getOneriAeroportuali()) {
                    optionalTariffa.setSelezionato(aeroporto);
                } else if (optional.getOneriFeroviari()) {
                    optionalTariffa.setSelezionato(ferrovia);
                } else if (optional.getGuidatoreAggiuntivo()) {
                    if (conducente2) {
                        quantita++;
                    }
                    if (conducente3) {
                        quantita++;
                    }
                    optionalTariffa.setSelezionato(quantita > 0);
                } else if (optional.getSupplementoEta()) {
                    Integer anniMinimo = optional.getEtaMinima();
                    Integer anniMassimo = optional.getEtaMassima();
                    if (AnagraficaUtils.verificaEtaGuidatore(dataNascita1, inizioNoleggio, anniMinimo, anniMassimo)) {
                        quantita++;
                    }
                    if (AnagraficaUtils.verificaEtaGuidatore(dataNascita2, inizioNoleggio, anniMinimo, anniMassimo)) {
                        quantita++;
                    }
                    if (AnagraficaUtils.verificaEtaGuidatore(dataNascita3, inizioNoleggio, anniMinimo, anniMassimo)) {
                        quantita++;
                    }
                    optionalTariffa.setSelezionato(quantita > 0);
                } else if (optional.getRoadTax()) {
                    optionalTariffa.setSelezionato(Boolean.TRUE);
                } else {
                    optionalTariffa.setSelezionato(optionalListino.getIncluso());
                }
                if (quantita == 0 && optional.getMoltiplicabile() && optionalTariffa.getSelezionato()) {
                    quantita++;
                }
                if (quantita > 0) {
                    optionalTariffa.setQuantita((double) quantita);
                } else {
                    optionalTariffa.setQuantita(null);
                }

                optionalTariffa.setPrepagato(Boolean.FALSE);
                optionalTariffa.setSelezionatoRientro(Boolean.FALSE);
                optionalTariffa.setSelezionatoFranchigia(Boolean.FALSE);
                tariffa.getOptionalsTariffa().put(optional, optionalTariffa);
            }
        }
        return tariffa;
    }
}