/*
 * MovimentoAutoUtils.java
 *
 * Created on 16 aprilie 2007, 10:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package it.myrent.ee.api.utils;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.Parameters;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.VeicoloNonDisponibileException;
import it.myrent.ee.db.*;
import it.myrent.ee.db.MROldAffiliato;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 *
 * @author jamess
 */
public class MovimentoAutoUtils {

    private static final Log log = LogFactory.getLog(MovimentoAutoUtils.class);

    /** Creates a new instance of MovimentoAutoUtils */
    public MovimentoAutoUtils() {
    }
    public static final int SCADENZA_CONTRATTO = 0;
    public static final int SCADENZA_PROROGA = 1;
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");

    public static String toString(MROldMovimentoAuto movimento) {
        String descrizioneMovimento = ""; //NOI18N
        if (movimento != null) {
            if (movimento.getContratto() != null) {
                descrizioneMovimento += "R.A. " + MROldNumerazione.format(movimento.getContratto(). //NOI18N
                        getPrefisso(), movimento.getContratto().
                        getNumero()) + " "; //NOI18N
            } else {
                descrizioneMovimento += movimento.getCausale().
                        getDescrizione() + " "; //NOI18N
            }
            descrizioneMovimento += FormattedDate.format(movimento.getData()) + " "; //NOI18N
            if (movimento.getAutista() != null) {
                descrizioneMovimento += "(" + movimento.getAutista(). //NOI18N
                        toString() + ")"; //NOI18N
            } else if (movimento.getContratto().
                    getCliente() != null) {
                descrizioneMovimento += "(" + movimento.getContratto(). //NOI18N
                        getCliente().
                        toString() + ")"; //NOI18N
            }
        }
        return descrizioneMovimento;
    }

    /**
     * Carica dal database la causale del noleggio (con id 1)
     * @param sx La sessione da usare per caricare la causale.
     * @return la causale noleggio.
     */
    public static MROldCausaleMovimento leggiCausaleNoleggio(Session sx) throws HibernateException {
        return (MROldCausaleMovimento) sx.get(MROldCausaleMovimento.class, MROldCausaleMovimento.CAUSALE_NOLEGGIO);
    }

    /**
     * Carica dal database la causale del noleggio (con id 1)
     * @param sx La sessione da usare per caricare la causale.
     * @return la causale noleggio.
     */
    public static MROldCausaleMovimento getCausale(Session sx, String descrizione) throws HibernateException {
        if(descrizione.equals(MROldCausaleMovimento.CAUSALE_PLANNED_RENTAL)){
            MROldCausaleMovimento causaleMovimento = (MROldCausaleMovimento) sx.createQuery("select d from MROldCausaleMovimento d where d.descrizione=:descrizione").setParameter("descrizione",descrizione).setMaxResults(1).uniqueResult();
            if(causaleMovimento!=null){
                return causaleMovimento;
            } else {
                causaleMovimento = new MROldCausaleMovimento();
                causaleMovimento.setDescrizione(MROldCausaleMovimento.CAUSALE_PLANNED_RENTAL);
                sx.save(causaleMovimento);
                sx.flush();
                return causaleMovimento;
            }
        } else {
            return (MROldCausaleMovimento) sx.createQuery("select d from MROldCausaleMovimento d where d.descrizione=:descrizione").setParameter("descrizione",descrizione).setMaxResults(1).uniqueResult();
        }

    }

    /**
     * Carica dal database la causale della prenotazione (con id 2)
     * @param sx La sessione da usare per caricare la causale.
     * @return la causale prenotazione.
     */
    public static MROldCausaleMovimento leggiCausalePrenotazione(Session sx) throws HibernateException {
        return (MROldCausaleMovimento) sx.get(MROldCausaleMovimento.class, MROldCausaleMovimento.CAUSALE_PRENOTAZIONE);
    }

    public static MROldParcoVeicoli cercaVeicoloLibero(
            Session sx,
            MROldSede sedeUscita,
            MROldGruppo gruppo,
            MROldMovimentoAuto movimento,
            Date inizio,
            Date fine,
            boolean prenotazione) throws HibernateException {
        // Veicoli movimentati, unico risultato.
        List veicoli = cercaVeicoliLiberi(sx, sedeUscita, gruppo, movimento, inizio, fine, SCADENZA_CONTRATTO, true, true, prenotazione);
        if (veicoli.size() == 0) {
            //Veicoli non movimentati, unico risultato.
            veicoli = cercaVeicoliLiberi(sx, sedeUscita, gruppo, movimento, inizio, fine, SCADENZA_CONTRATTO, false, true, prenotazione);
        }
        if (veicoli.size() > 0) {
            return (MROldParcoVeicoli) veicoli.get(0);
        } else {
            return null;
        }
    }


    /** Questo metodo cerca un veicolo libero nel periodo specificato da <code>inizio</code> e <code>fine</code>,
     * appertente al gruppo di mezzi <code>gruppo</code>, senza includere i veicoli con contratti scadenti.
     * @param mySession Sessione da usare per fare le query.
     * @param sedeUscita la sede da dove partira' il mezzo.
     * @param gruppo gruppo di appartenenza del veicolo. Puo' essere null, quindi non incluso tra i criteri di ricerca.
     * @param movimento movimento da escludere nella ricerca della disponibilita.
     * @param inizio data di inizio del periodo di riferimento (timestamp con campi anno, mese, giorno, ora, minuto).
     * @param fine data di fine del periodo di roferimento (timestamp con campi anno, mese, giorno, ora, minuto).
     * @param scadenza
     * @param veicoliMovimentati
     * @param mezzoUnico
     * @param prenotazione Se true, permette di selezionare anche veicoli impegnati.
     */
    public static List cercaVeicoliLiberi(
            Session mySession,
            MROldSede sedeUscita,
            MROldGruppo gruppo,
            MROldMovimentoAuto movimento,
            Date inizio,
            Date fine,
            int tipoScadenza,
            boolean veicoliMovimentati,
            boolean mezzoUnico,
            boolean prenotazione) throws HibernateException {
        List veicoliLiberi = new ArrayList();

        Query queryIdentifiersVeicoli = null;
        String selectString =
                "select v.id from MROldParcoVeicoli v " + //NOI18N
                "left join v.movimenti m " + //NOI18N
                "with " + //NOI18N
                "(" + //NOI18N
                "   m.fine > :oggi and " + //NOI18N
                "   m.annullato = :false and " + //NOI18N
                "   ( m.prenotazione.id is not null or m.contratto.id is not null ) " + //NOI18N
                ") " + //NOI18N
                "where v.sede.id = :idSede"; //NOI18N
        // Solo per le prenotazioni possiamo cercare veicoli non impengati.
        if (!prenotazione) {
            selectString += " and v.impegnato = :impegnato"; //NOI18N
        }
        selectString += " and v.abilitato = :true"; //NOI18N

        String gruppoString = "v.gruppo.id = :idGruppo"; //NOI18N
        String scadenzaString;
        if (tipoScadenza == SCADENZA_CONTRATTO) {
            scadenzaString = "(v.dataAcquisto is null or v.dataAcquisto <= :acquisto) and v.dataScadenzaContratto >= :scadenza"; //NOI18N        
        } else {
            scadenzaString = "(v.dataAcquisto is null or v.dataAcquisto <= :acquisto) and (v.dataProroga1 >= :scadenza or v.dataProroga2 >= :scadenza )"; //NOI18N
        }

        String movimentoString =
                "v not in " + //NOI18N
                "(" + //NOI18N
                "   select m.veicolo from MROldMovimentoAuto as m where " + //NOI18N
                "   m.annullato = :false and (" + //NOI18N
                "       (m.inizio >= :inizio and m.inizio < :fine) or " + //NOI18N
                "       (m.fine > :inizio and m.fine <= :fine) or " + //NOI18N
                "       (m.inizio < :inizio and m.fine > :fine)" + //NOI18N
                "   )" + //NOI18N
                ")"; //NOI18N
        String movimentoDaEscludereString =
                "v not in " + //NOI18N
                "(" + //NOI18N
                "   select m.veicolo from MROldMovimentoAuto as m where " + //NOI18N
                "   m.id = :idMovimento or " + //NOI18N
                "   (" + //NOI18N
                "       m.annullato = :false and " + //NOI18N
                "       (" + //NOI18N
                "           (m.inizio >= :inizio and m.inizio < :fine) or " + //NOI18N
                "           (m.fine > :inizio and m.fine <= :fine) or " + //NOI18N
                "           (m.inizio < :inizio and m.fine > :fine)" + //NOI18N
                "       )" + //NOI18N
                "   )" + //NOI18N
                ")"; //NOI18N

        String orderString =
                "group by v.id, v.km having " + //NOI18N
                (veicoliMovimentati ? "count(m) > 0 " : "count(m) = 0 ") + //NOI18N
                "order by sum(m.fine - m.inizio) desc, v.km asc"; //NOI18N
        // SOLO PER POSTGRES >= di 8.1
        //"order by sum(m.fine - greatest(:oggi, m.inizio)) desc, v.km asc"; //NOI18N
        if (gruppo != null && gruppo.getId() != null) {
            if (movimento != null && movimento.getId() != null) {
                queryIdentifiersVeicoli = mySession.createQuery(
                        selectString + " and " + //NOI18N
                        scadenzaString + " and " + //NOI18N
                        gruppoString + " and " + //NOI18N
                        movimentoDaEscludereString + " " + //NOI18N
                        orderString);
                queryIdentifiersVeicoli.setParameter("idMovimento", movimento.getId()); //NOI18N
                queryIdentifiersVeicoli.setParameter("idGruppo", gruppo.getId()); //NOI18N
            } else {
                queryIdentifiersVeicoli = mySession.createQuery(
                        selectString + " and " + //NOI18N
                        scadenzaString + " and " + //NOI18N
                        gruppoString + " and " + //NOI18N
                        movimentoString + " " + //NOI18N
                        orderString);
                queryIdentifiersVeicoli.setParameter("idGruppo", gruppo.getId()); //NOI18N
            }
        } else {
            if (movimento != null && movimento.getId() != null) {
                queryIdentifiersVeicoli = mySession.createQuery(
                        selectString + " and " + //NOI18N
                        scadenzaString + " and " + //NOI18N
                        movimentoDaEscludereString + " " + //NOI18N
                        orderString);
                queryIdentifiersVeicoli.setParameter("idMovimento", movimento.getId()); //NOI18N
            } else {
                queryIdentifiersVeicoli = mySession.createQuery(
                        selectString + " and " + //NOI18N
                        scadenzaString + " and " + //NOI18N
                        movimentoString + " " + //NOI18N
                        orderString);
            }
        }
        if (!prenotazione) {
            queryIdentifiersVeicoli.setParameter("impegnato", Boolean.FALSE); //NOI18N
        }
        queryIdentifiersVeicoli.setParameter("idSede", sedeUscita.getId()); //NOI18N
        queryIdentifiersVeicoli.setParameter("inizio", inizio); //NOI18N
        queryIdentifiersVeicoli.setParameter("fine", fine); //NOI18N
        queryIdentifiersVeicoli.setParameter("acquisto", FormattedDate.formattedDate(inizio.getTime())); //NOI18N
        queryIdentifiersVeicoli.setParameter("scadenza", FormattedDate.formattedDate(fine.getTime())); //NOI18N
        queryIdentifiersVeicoli.setParameter("oggi", FormattedDate.formattedDate()); //NOI18N
        queryIdentifiersVeicoli.setParameter("false", Boolean.FALSE); //NOI18N
        queryIdentifiersVeicoli.setParameter("true", Boolean.TRUE); //NOI18N
        if (mezzoUnico) {
            queryIdentifiersVeicoli.setMaxResults(1);
        }
        List identifiers = queryIdentifiersVeicoli.list();
        if (identifiers != null && identifiers.size() > 0) {
            Iterator id = identifiers.iterator();
            while (id.hasNext()) {
                veicoliLiberi.add(mySession.get(MROldParcoVeicoli.class, (Integer) id.next()));
            }
        }
        return veicoliLiberi;
    }

    

    /**
     * Scollega tutte le prenotazioni del veicolo. Per le prenotazioni
     * successive ad oggi prova ad abbinare altri veicoli.
     * Tutti i cambiamenti debbono essere resi persistenti con commit,
     * altrimenti vengono scartati alla chiusura della sessione.
     */
    public static void scollegaPrenotazioniVeicolo(Session sx, Transaction tx, MROldParcoVeicoli aVeicolo) throws HibernateException, VeicoloNonDisponibileException {
        //1. Cerchiamo tutte le prenotazioni future del mezzo, da riallocare.
        Iterator<MROldPrenotazione> prenotazioni = (Iterator<MROldPrenotazione>) sx.createQuery(
                "select m.prenotazione from MROldMovimentoAuto m where " + //NOI18N
                "m.causale.id = :prenotazione and " + //NOI18N
                "m.veicolo = :veicolo and " + //NOI18N
                "m.inizio >= :oggi " + //NOI18N
                "order by m.inizio"). //NOI18N
                setParameter("prenotazione", MROldCausaleMovimento.CAUSALE_PRENOTAZIONE). //NOI18N
                setParameter("veicolo", aVeicolo). //NOI18N
                setParameter("oggi", FormattedDate.formattedDate()). //NOI18N
                iterate();

        //2. Eliminiamo tutti i blocchi per prenotazione
        sx.createQuery("delete from MROldMovimentoAuto m where m.veicolo = :veicolo and m.causale.id = :prenotazione"). //NOI18N
                setParameter("veicolo", aVeicolo). //NOI18N
                setParameter("prenotazione", MROldCausaleMovimento.CAUSALE_PRENOTAZIONE). //NOI18N
                executeUpdate();

        //3. Ricreiamo le disponibilita'
        DisponibilitaUtils.creaDisponibilitaVeicolo(sx, tx, aVeicolo);

        //4. Abbiniamo le prenotazioni future ad altri mezzi
        MROldCausaleMovimento causalePrenotazione = leggiCausalePrenotazione(sx);
        while (prenotazioni.hasNext()) {
            MROldPrenotazione p = (MROldPrenotazione) prenotazioni.next();
            MROldParcoVeicoli v = DisponibilitaUtils.cercaVeicoloDisponibile(sx, p.getSedeUscita(), p.getGruppo(), p.getInizio(), p.getFine());
            System.out.println("targa veicolo: " + v.getTarga());
            if (v != null) {
                p.setMovimento(new MROldMovimentoAuto(
                        null, // MROldNumerazione
                        null, // Prefisso
                        null, // Numero
                        FormattedDate.formattedDate(), // Data
                        FormattedDate.annoCorrente(), // Anno
                        MROldAffiliato.getNoleggiatore(),
                        causalePrenotazione,
                        p, // MROldPrenotazione
                        null, // Contratto
                        v,
                        null, // Danni
                        p.getSedeUscita(),
                        p.getSedeRientroPrevisto(),
                        p.getInizio(),
                        p.getFine(),
                        null, // Km inizio
                        null, // Combustibile uscita
                        null, // MROldAutista
                        null, // Note
                        (User) Parameters.getUser(),
                        FormattedDate.formattedTimestamp(),
                        false));
                sx.saveOrUpdate(p);
                DisponibilitaUtils.aggiornaDisponibilita(sx, tx, null, p.getMovimento());
            }
        }
    }

    /**
     * Questo metodo aggiorna i blocchi precedenti ad oggi, per prenotazione,
     * di un mezzo che viene disabilitato.<br>
     * Tutti i cambiamenti debbono essere resi persistenti con commit,
     * altrimenti vengono scartati alla chiusura della sessione.
     */
    public static void aggiornaPrenotazioniPrecedenti(Session mySession, MROldParcoVeicoli aVeicolo) throws HibernateException {
        Query queryMovimentiFuturi = mySession.createQuery(
                "select m from MROldMovimentoAuto m left join m.causale as c where " + //NOI18N
                "c.id = :causalePrenotazione and " + //NOI18N
                "m.veicolo = :veicolo and " + //NOI18N
                "m.inizio < :oggi " + //NOI18N
                "order by m.inizio"); //NOI18N
        queryMovimentiFuturi.setParameter("causalePrenotazione", MROldCausaleMovimento.CAUSALE_PRENOTAZIONE); //NOI18N
        queryMovimentiFuturi.setParameter("veicolo", aVeicolo); //NOI18N
        queryMovimentiFuturi.setParameter("oggi", FormattedDate.formattedDate()); //NOI18N
        Iterator movimenti = queryMovimentiFuturi.iterate();
        while (movimenti.hasNext()) {
            MROldMovimentoAuto m = (MROldMovimentoAuto) movimenti.next();
            if (m.getPrenotazione() != null) {
                m.getPrenotazione().
                        setMovimento(null);
                mySession.saveOrUpdate(m.getPrenotazione());
                mySession.delete(m);
            } else {
                log.debug("MovimentoAutoUtils.aggiornamentoImpegniFuturi: Trovato il movimento " + m.getId() + " con contratto. Impossibile spostarlo."); //NOI18N
            }
        }
    }

    /**
     * Questo metodo aggiorna i blocchi futuri, per prenotazione, di un 
     * mezzo che si sposta.<br>
     * Tutti i cambiamenti debbono essere resi persistenti con commit,
     * altrimenti vengono scartati alla chiusura della sessione.
     */
    public static void aggiornaPrenotazioniFuture(Session mySession, MROldMovimentoAuto aMovimento) throws HibernateException {
        Query queryMovimentiFuturi = mySession.createQuery(
                "select m from MROldMovimentoAuto m left join m.causale as c where " + //NOI18N
                "c.id = :causalePrenotazione and " + //NOI18N
                "m.veicolo = :veicolo and " + //NOI18N
                "m.inizio >= :fine and " + //NOI18N
                "m != :movimento and " + //NOI18N
                "m.annullato = :false " + //NOI18N
                "order by m.inizio"); //NOI18N
        queryMovimentiFuturi.setParameter("causalePrenotazione", MROldCausaleMovimento.CAUSALE_PRENOTAZIONE); //NOI18N
        queryMovimentiFuturi.setParameter("veicolo", aMovimento.getVeicolo()); //NOI18N
        queryMovimentiFuturi.setParameter("fine", aMovimento.getFine()); //NOI18N
        queryMovimentiFuturi.setParameter("movimento", aMovimento); //NOI18N
        queryMovimentiFuturi.setParameter("false", Boolean.FALSE); //NOI18N
        Iterator movimenti = queryMovimentiFuturi.iterate();
        while (movimenti.hasNext()) {
            MROldMovimentoAuto m = (MROldMovimentoAuto) movimenti.next();
            List veicoliLiberi = cercaVeicoliLiberi(mySession, m.getSedeUscita(), m.getVeicolo().
                    getGruppo(), m, m.getInizio(), m.getFine(), SCADENZA_CONTRATTO, true, true, true);
            if (veicoliLiberi.size() == 0) {
                veicoliLiberi = cercaVeicoliLiberi(mySession, m.getSedeUscita(), m.getVeicolo().
                        getGruppo(), m, m.getInizio(), m.getFine(), SCADENZA_CONTRATTO, false, true, true);
            }

            MROldParcoVeicoli nuovoVeicolo = null;
            if (veicoliLiberi.size() > 0) {
                nuovoVeicolo = (MROldParcoVeicoli) veicoliLiberi.get(0);
            }

            if (nuovoVeicolo != null) {
                m.setVeicolo(nuovoVeicolo);
                mySession.saveOrUpdate(m);
            } else {
                if (m.getPrenotazione() != null) {
                    m.getPrenotazione().
                            setMovimento(null);
                    mySession.saveOrUpdate(m.getPrenotazione());
                    mySession.delete(m);
                } else {
                    log.debug("MovimentoAutoUtils.aggiornamentoImpegniFuturi: Trovato il movimento " + m.getId() + " con contratto. Impossibile spostarlo."); //NOI18N
                }

            }
        }
    }


    /**
     * Verifica se esistono altri movimenti del mezzo nel periodo inserito.
     * @param sx La sessione da usare per fare la query.
     * @param aVeicolo Il veicolo per il quale viene fatta la ricerca - not null.
     * @param movimentoDaEscludere Movimento da escludere nella ricerca (opzionale, puo' essere null).
     * @param inizio Data di inizio del periodo verificato (timestamp) - not null.
     * @param fine Data di fine del periodo verificato (timestamp) - not null.
     * @throws org.hibernate.HibernateException se succede qualcosa di storto col db...
     * @return true se esistono altri movimenti del mezzo per lo stesso periodo.
     */
    public static boolean conflicts(Session sx, MROldParcoVeicoli aVeicolo, MROldMovimentoAuto movimentoDaEscludere, Date inizio, Date fine) throws HibernateException {
        boolean conflictsExist = false;
        Query queryConflicts = null;
        if (movimentoDaEscludere != null && movimentoDaEscludere.getId() != null) {
            queryConflicts = sx.createQuery("select count(m.id) from MROldMovimentoAuto m where " + //NOI18N
                    "m.veicolo.id = :idVeicolo and " + //NOI18N
                    "m.id <> :idMovimento and " + //NOI18N
                    "m.annullato = :false and " + //NOI18N
                    "(" + //NOI18N
                    "(m.inizio >= :inizio and m.inizio < :fine) or" + //NOI18N
                    "(m.fine > :inizio and m.fine <= :fine) or" + //NOI18N
                    "(m.inizio < :inizio and m.fine > :fine)" + //NOI18N
                    ")"); //NOI18N
            queryConflicts.setParameter("idMovimento", movimentoDaEscludere.getId()); //NOI18N
        } else {
             queryConflicts = sx.createQuery("select count(m.id) from MROldMovimentoAuto m where " + //NOI18N
                    "m.veicolo.id = :idVeicolo and " + //NOI18N
                    "m.annullato = :false and " + //NOI18N
                    "(" + //NOI18N
                    "(m.inizio >= :inizio and m.inizio < :fine) or" + //NOI18N
                    "(m.fine > :inizio and m.fine <= :fine) or" + //NOI18N
                    "(m.inizio < :inizio and m.fine > :fine)" + //NOI18N
                    ")"); //NOI18N
        }

        queryConflicts.setParameter("idVeicolo", aVeicolo !=null ? aVeicolo.getId():null); //NOI18N
        queryConflicts.setParameter("inizio", inizio); //NOI18N
        queryConflicts.setParameter("fine", fine); //NOI18N
        queryConflicts.setParameter("false", Boolean.FALSE); //NOI18N
        int conflicts = ((Number) queryConflicts.uniqueResult()).intValue();
        conflictsExist =
                (conflicts > 0);
        return conflictsExist;
    }

    /**
     * Verifica se esistono altri movimenti del mezzo nel periodo inserito.
     * @param sx La sessione da usare per fare la query.
     * @param aVeicolo Il veicolo per il quale viene fatta la ricerca - not null.
     * @param movimentoDaEscludere Movimento da escludere nella ricerca (opzionale, puo' essere null).
     * @param inizio Data di inizio del periodo verificato (timestamp) - not null.
     * @param fine Data di fine del periodo verificato (timestamp) - not null.
     * @throws org.hibernate.HibernateException se succede qualcosa di storto col db...
     * @return true se esistono altri movimenti del mezzo per lo stesso periodo.
     */
    public static boolean conflictsForPR(Session sx, MROldParcoVeicoli aVeicolo, MROldMovimentoAuto movimentoDaEscludere, Date inizio, Date fine, MROldMovimentoAuto plannedRental) throws HibernateException {
        boolean conflictsExist = false;
        Query queryConflicts = null;
        if (movimentoDaEscludere != null && movimentoDaEscludere.getId()!=null && plannedRental!=null && plannedRental.getId()!=null) {
            queryConflicts = sx.createQuery("select m from MROldMovimentoAuto m where " + //NOI18N
                    "m.veicolo.id = :idVeicolo and " + //NOI18N
                    "m.id <> :mov1Id and m.id <> :mov2Id and " + //NOI18N
                    "m.annullato = :false and " + //NOI18N
                    "(" + //NOI18N
                    "(m.inizio >= :inizio and m.inizio < :fine) or" + //NOI18N
                    "(m.fine > :inizio and m.fine <= :fine) or" + //NOI18N
                    "(m.inizio < :inizio and m.fine > :fine)" + //NOI18N
                    ")"); //NOI18N
            queryConflicts.setParameter("mov1Id", movimentoDaEscludere.getId()).setParameter("mov2Id", plannedRental.getId()); //NOI18N
        } else if (movimentoDaEscludere != null && movimentoDaEscludere.getId()!=null) {
            queryConflicts = sx.createQuery("select m from MROldMovimentoAuto m where " + //NOI18N
                    "m.veicolo.id = :idVeicolo and " + //NOI18N
                    "m.id <> :mov1Id and " + //NOI18N
                    "m.annullato = :false and " + //NOI18N
                    "(" + //NOI18N
                    "(m.inizio >= :inizio and m.inizio < :fine) or" + //NOI18N
                    "(m.fine > :inizio and m.fine <= :fine) or" + //NOI18N
                    "(m.inizio < :inizio and m.fine > :fine)" + //NOI18N
                    ")"); //NOI18N
            queryConflicts.setParameter("mov1Id", movimentoDaEscludere.getId()); //NOI18N
        } else {
            queryConflicts = sx.createQuery("select m from MROldMovimentoAuto m where " + //NOI18N
                    "m.veicolo.id = :idVeicolo and " + //NOI18N
                    "m.annullato = :false and " + //NOI18N
                    "(" + //NOI18N
                    "(m.inizio >= :inizio and m.inizio < :fine) or" + //NOI18N
                    "(m.fine > :inizio and m.fine <= :fine) or" + //NOI18N
                    "(m.inizio < :inizio and m.fine > :fine)" + //NOI18N
                    ")"); //NOI18N
        }

        queryConflicts.setParameter("idVeicolo", aVeicolo !=null ? aVeicolo.getId():null); //NOI18N
        queryConflicts.setParameter("inizio", inizio); //NOI18N
        queryConflicts.setParameter("fine", fine); //NOI18N
        queryConflicts.setParameter("false", Boolean.FALSE); //NOI18N
        List confList = queryConflicts.list();
        if(confList!=null && confList.size()>0){
            Set confSet = new HashSet();
            Iterator itr = confList.iterator();
            while(itr.hasNext()) {
                MROldMovimentoAuto mov = (MROldMovimentoAuto)itr.next();
                if(mov.getChiuso() && mov.getCausale().getDescrizione().equals(MROldCausaleMovimento.CAUSALE_PLANNED_RENTAL)){
                    continue;
                } else {
                    return true;
                }
            }
            conflictsExist = (confSet.size()>0);
        } else {
            conflictsExist = false;
        }
//        conflictsExist = (conflicts > 0);
        return conflictsExist;
    }


    public static List recuperaMovimentiGiornalieriVeicolo(
            Session sx, Integer idVeicolo, Date inizio, Date fine) throws HibernateException {
        List movimenti = null;
        Date fineModificato = FormattedDate.add(fine, GregorianCalendar.DAY_OF_MONTH, 1);

        String queryString = "select m from MROldMovimentoAuto m where " + //NOI18N
                "m.veicolo.id = :idVeicolo and " + //NOI18N
                "m.annullato = :false and " + //NOI18N
                "((m.inizio >= :inizio and m.inizio < :fine) or " + //NOI18N
                "(m.fine >= :inizio and m.fine < :fine) or " + //NOI18N
                "(m.inizio < :inizio and m.fine >= :fine)) order by m.inizio"; //NOI18N
        Query myQuery = sx.createQuery(queryString);
        myQuery.setParameter("idVeicolo", idVeicolo); //NOI18N
        myQuery.setParameter("inizio", inizio); //NOI18N
        myQuery.setParameter("fine", fineModificato); //NOI18N
        myQuery.setParameter("false", Boolean.FALSE); //NOI18N
        movimenti =
                myQuery.list();
        return movimenti;
    }

    /**
     * Carica i veicoli ed i movimenti auto con i criteri inseriti. Tutti i criteri sono opzionali, tranne inizioIntervallo e fineIntervallo.
     *@return Un array contenente i veicoli nella posizione 0 ed i movimenti auto nella posizione 1.
     */
    public static List[] caricaVeicoliAndPrenotazioni(Session sx, MROldGruppo gruppo, MROldAffiliato affiliato, List sedi, Date inizioIntervallo, Date fineIntervallo) throws HibernateException {
        List[] retValue = new List[2];
        List veicoli = null, movimenti = null;
        Query queryVeicoli = null, queryMovimenti = null;
        HashMap parameters = new HashMap();
        StringBuffer queryString = new StringBuffer("select new MROldParcoVeicoli(" + //NOI18N
                "v.id, " + //NOI18N
                "v.targa, " + //NOI18N
                "v.marca, " + //NOI18N
                "v.modello, " + //NOI18N
                "v.versione, " + //NOI18N
                "v.parcheggio, " + //NOI18N
                "v.km, " + //NOI18N
                "v.livelloCombustibile, " + //NOI18N
                "v.capacitaSerbatoio, " + //NOI18N
                "v.pulita, " + //NOI18N
                "v.impegnato, " + //NOI18N
                "v.dataAcquisto," + //NOI18N
                "v.dataVendita, " + //NOI18N
                "v.dataScadenzaContratto, " + //NOI18N
                "v.dataProroga1, " + //NOI18N
                "v.dataProroga2, " + //NOI18N
                "v.gruppo.id, " + //NOI18N
                "v.gruppo.codiceNazionale, " + //NOI18N
                "v.affiliato.id, " + //NOI18N
                "v.affiliato.tipoAffiliato, " + //NOI18N
                "v.sede.id, " + //NOI18N
                "v.sede.codice, " + //NOI18N
                "v.sede.affiliato.id) " + //NOI18N
                "from MROldParcoVeicoli as v left join v.sede as s left join v.gruppo as g where " + //NOI18N
                "v.abilitato = :abilitato and " + //NOI18N
                "(" + //NOI18N
                "(v.dataAcquisto is null and v.dataVendita is null) or " + //NOI18N
                "(v.dataAcquisto is null and v.dataVendita >= :inizioRiferimento) or " + //NOI18N
                "(v.dataVendita is null and v.dataAcquisto <= :fineRiferimento) or " + //NOI18N
                "(v.dataAcquisto is not null and v.dataVendita is not null and " + //NOI18N
                "(" + //NOI18N
                "(v.dataAcquisto between :inizioRiferimento and :fineRiferimento) or " + //NOI18N
                "(v.dataVendita between :inizioRiferimento and :fineRiferimento) or " + //NOI18N
                "(v.dataAcquisto < :inizioRiferimento and v.dataVendita > :fineRiferimento)" + //NOI18N
                ")" + //NOI18N
                ")" + //NOI18N
                ")"); //NOI18N
        if (gruppo != null) {
            queryString.append(" and v.gruppo = :gruppo "); //NOI18N
        }

        if (sedi != null && sedi.size() > 0) {
            if (sedi.size() == 1) {
                queryString.append(" and v.sede = :sede "); //NOI18N
            } else {
                queryString.append(" and v.sede in ( :sedi ) "); //NOI18N
            }

        }

        if (affiliato != null) {
            queryString.append(" and (v.affiliato = :affiliato or s.affiliato = :affiliato) "); //NOI18N
        }

        queryString.append(" order by v.gruppo.codiceNazionale, v.targa"); //NOI18N

        queryVeicoli =
                sx.createQuery(queryString.toString());

        queryVeicoli.setParameter("inizioRiferimento", inizioIntervallo); //NOI18N
        queryVeicoli.setParameter("fineRiferimento", fineIntervallo); //NOI18N

        if (gruppo != null) {
            queryVeicoli.setParameter("gruppo", gruppo); //NOI18N
        }

        if (sedi != null && sedi.size() > 0) {
            if (sedi.size() == 1) {
                queryVeicoli.setParameter("sede", sedi.get(0)); //NOI18N
            } else {
                queryVeicoli.setParameterList("sedi", sedi); //NOI18N
            }

        }

        if (affiliato != null) {
            queryVeicoli.setParameter("affiliato", affiliato); //NOI18N
        }

        queryVeicoli.setParameter("abilitato", Boolean.TRUE); //NOI18N

        veicoli =
                queryVeicoli.list();
        if (veicoli != null && veicoli.size() > 0) {
            queryMovimenti = sx.createQuery(
                    "select new MovimentoAuto(m.id, m.inizio, m.fine, m.veicolo.id, m.contratto.id, m.prenotazione.id, m.prenotazione.confermata) " + //NOI18N
                    " from MROldMovimentoAuto as m left join m.contratto left join m.prenotazione left join m.veicolo where " + //NOI18N
                    "m.veicolo in ( :veicoli ) and " + //NOI18N
                    "m.annullato = :false and " + //NOI18N
                    "(" + //NOI18N
                    "(m.inizio between :inizioRiferimento and :fineRiferimento) or " + //NOI18N
                    "(m.fine between :inizioRiferimento and :fineRiferimento) or " + //NOI18N
                    "(m.inizio < :inizioRiferimento and m.fine > :fineRiferimento)" + //NOI18N
                    ")"); //NOI18N
            queryMovimenti.setParameterList("veicoli", veicoli.toArray()); //NOI18N
            queryMovimenti.setParameter("inizioRiferimento", new Date(inizioIntervallo.getTime() - 1)); //NOI18N
            queryMovimenti.setParameter("fineRiferimento", new Date(FormattedDate.add(fineIntervallo, GregorianCalendar.DAY_OF_MONTH, 1). //NOI18N
                    getTime() - 1));
            queryMovimenti.setParameter("false", Boolean.FALSE); //NOI18N
            movimenti = queryMovimenti.list();
        }

        retValue[0] = veicoli != null ? veicoli : new ArrayList();
        retValue[1] = movimenti != null ? movimenti : new ArrayList();
        return retValue;
    }

    public static boolean isUltimoMovimento(MROldMovimentoAuto movimento, Session sx) {
        Transaction tx = null;
        boolean ultimoMovimento = false;
        try {
            movimento = (MROldMovimentoAuto) sx.get(MROldMovimentoAuto.class, movimento.getId());
            Number count = (Number) sx.createQuery("select count(m) from MROldMovimentoAuto m where m.causale.prenotazione = :false and m.causale.indisponibilitaFutura = :false and m.inizio >= :fine and m.veicolo = :veicolo and m.id <> :id").
                    setParameter("false", false).
                    setParameter("fine", movimento.getFine()).
                    setParameter("veicolo", movimento.getVeicolo()).
                    setParameter("id", movimento.getId()).
                    uniqueResult();
            ultimoMovimento = (count == null || count.intValue() == 0);
            //TODO Insert your code here
        } catch (Exception ex) {
            if (tx != null) {
                try {
                    //tx.rollback();
                } catch (Exception txx) {
                }
            }
        } finally {
            if (sx != null) {
                try {
                    //sx.close();
                } catch (Exception sxx) {
                }
            }
        }
        return ultimoMovimento;
    }

    public static boolean conflicts(Session mySession , Integer idVeicolo, Integer idMovimentoDaEscludere, Date
            inizio, Date fine) {
        boolean conflictsExist = false;
        try {
            Query queryConflicts = null;
            if (idMovimentoDaEscludere != null) {
                queryConflicts = mySession.createQuery(
                        "select count(m.id) from MROldMovimentoAuto m where " + //NOI18N
                                "m.veicolo.id = :idVeicolo and " + //NOI18N
                                "m.id <> :idMovimento and " + //NOI18N
                                "m.annullato = :false and " + //NOI18N
                                "(" + //NOI18N
                                "   (m.inizio >= :inizio and m.inizio < :fine) or" + //NOI18N
                                "   (m.fine > :inizio and m.fine <= :fine) or" + //NOI18N
                                "   (m.inizio < :inizio and m.fine > :fine)" + //NOI18N
                                ")"); //NOI18N
                queryConflicts.setParameter("idMovimento", idMovimentoDaEscludere); //NOI18N
            } else {
                queryConflicts = mySession.createQuery("select count(m.id) from MROldMovimentoAuto m where " + //NOI18N
                        "m.veicolo.id = :idVeicolo and " + //NOI18N
                        "m.annullato = :false and " + //NOI18N
                        "(" + //NOI18N
                        "(m.inizio >= :inizio and m.inizio < :fine) or" + //NOI18N
                        "(m.fine > :inizio and m.fine <= :fine) or" + //NOI18N
                        "(m.inizio < :inizio and m.fine > :fine)" + //NOI18N
                        ")"); //NOI18N
            }

            queryConflicts.setParameter("idVeicolo", idVeicolo); //NOI18N
            queryConflicts.setParameter("inizio", inizio); //NOI18N
            queryConflicts.setParameter("fine", fine); //NOI18N
            queryConflicts.setParameter("false", Boolean.FALSE); //NOI18N
            int conflicts = ((Number) queryConflicts.uniqueResult()).intValue();
            conflictsExist =
                    (conflicts > 0);
        } catch (Exception ex) {
//            if (mySession != null) {
//                HibernateBridge.closeSession();
//            }
            if (log.isDebugEnabled()) {
                log.debug("MovimentoAutoUtils : Exception encountered while querying for conflicts.", ex); //NOI18N
            } else {
                log.warn("MovimentoAutoUtils : Exception encountered while querying for conflicts."); //NOI18N
            }

        }
        return conflictsExist;
    }

    public static Set<MROldMovimentoAuto> getMovementsOfAgreement(Session mySession, MROldContrattoNoleggio agreement){
        Set<MROldMovimentoAuto> movimenti = new HashSet<MROldMovimentoAuto>();
        if(agreement!=null){
            Query queryMovimentiFuturi = mySession.createQuery(
                    "select m from MROldMovimentoAuto m where " + //NOI18N
                            "m.contratto.id = :agreementId"); //NOI18N
            queryMovimentiFuturi.setParameter("agreementId", agreement.getId()); //NOI18N
            Iterator itr = queryMovimentiFuturi.iterate();
            while(itr.hasNext()){
                movimenti.add((MROldMovimentoAuto)itr.next());
            }
        }
        return movimenti;
    }
    public static Set<MROldMovimentoAuto> getClosedMovementsOfAgreement(Session mySession, MROldContrattoNoleggio agreement){
        Set<MROldMovimentoAuto> movimenti = new HashSet<MROldMovimentoAuto>();
        if(agreement!=null){
            Query queryMovimentiFuturi = mySession.createQuery(
                    "select m from MROldMovimentoAuto m where " + //NOI18N
                            "m.contratto.id = :agreementId and m.chiuso = :chiuso"); //NOI18N
            queryMovimentiFuturi.setParameter("agreementId", agreement.getId()).setParameter("chiuso", true); //NOI18N
            Iterator itr = queryMovimentiFuturi.iterate();
            while(itr.hasNext()){
                movimenti.add((MROldMovimentoAuto)itr.next());
            }
        }
        return movimenti;
    }

    public static MROldMovimentoAuto findPlannedRental(Session mySession, MROldParcoVeicoli parcoVeicoli, Date startDate, Date endDate){
        MROldMovimentoAuto movimenti = null;
        if(parcoVeicoli!=null){
            try{
                movimenti = (MROldMovimentoAuto) mySession.createQuery(
                        "select m from MROldMovimentoAuto m where " + //NOI18N
                                "m.veicolo.id = :parcoVeicoliId and m.causale.id = :plannedCausaleId and " +
                                "m.inizio= :startDate and m.fine=:endDate and m.chiuso = false").
                        setParameter("parcoVeicoliId", parcoVeicoli.getId()).
                        setParameter("plannedCausaleId", getCausale(mySession, MROldCausaleMovimento.CAUSALE_PLANNED_RENTAL).getId()).
                        setParameter("startDate", startDate).
                        setParameter("endDate", endDate).setMaxResults(1).uniqueResult(); //NOI18N
            } catch (Exception ex){

            }
        }
        return movimenti;
    }





}
