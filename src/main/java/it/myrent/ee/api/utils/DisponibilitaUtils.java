/*
 * DisponibilitaUtils.java
 *
 * Created on September 10, 2003, 4:39 PM
 */
package it.myrent.ee.api.utils;

import java.util.*;

import it.aessepi.utils.beans.FormattedDate;

import it.myrent.ee.db.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import it.myrent.ee.api.exception.VeicoloNonDisponibileException;
import it.aessepi.utils.BundleUtils;
import it.myrent.ee.db.MROldDisponibilitaVeicolo;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Transaction;

/**
 *
 * @author  mauro
 */
/*
 * DisponibilitaUtils.java
 *
 * Created on September 10, 2003, 4:39 PM
 */
public class DisponibilitaUtils {

    /** Creates a new instance of DisponibilitaUtils */
    public DisponibilitaUtils() {
    }
    private static Log log = LogFactory.getLog(DisponibilitaUtils.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");

    public static void creaDisponibilitaVeicoli(Session sx, Transaction tx) throws HibernateException {
        sx.createQuery("delete from DisponibilitaVeicolo d"). //NOI18N
                executeUpdate();

        Iterator<MROldParcoVeicoli> veicoli = (Iterator<MROldParcoVeicoli>) sx.createQuery("select v from MROldParcoVeicoli v"). //NOI18N
                iterate();
        while (veicoli.hasNext()) {
            MROldParcoVeicoli veicolo = veicoli.next();
            log.debug("Creazione disponibilita per veicolo: " + veicolo); //NOI18N

            MROldDisponibilitaVeicolo disponibilita = new MROldDisponibilitaVeicolo(
                    veicolo,
                    veicolo.getDataAcquisto(),
                    null,
                    null,
                    Boolean.TRUE,
                    Boolean.FALSE);

            Iterator<MROldMovimentoAuto> movimenti = (Iterator<MROldMovimentoAuto>) sx.createQuery(
                    "select m from MROldMovimentoAuto m where m.veicolo = :veicolo and m.annullato = :false order by m.inizio"). //NOI18N
                    setParameter("veicolo", veicolo). //NOI18N
                    setParameter("false", Boolean.FALSE). //NOI18N
                    iterate();

            while (movimenti.hasNext()) {
                MROldMovimentoAuto movimento = movimenti.next();
                disponibilita.setFine(movimento.getInizio());

                if (disponibilita.getSede() == null) {
                    disponibilita.setSede(movimento.getSedeUscita());
                }

                sx.save(disponibilita);

                disponibilita = new MROldDisponibilitaVeicolo(
                        veicolo,
                        movimento.getFine(),
                        null,
                        movimento.getSedeRientro(),
                        Boolean.FALSE,
                        Boolean.FALSE);
            }

            // Se il veicolo non ha alcun movimento, mettiamo la sede del veicolo.
            if (disponibilita.getSede() == null) {
                disponibilita.setSede(veicolo.getSede());
            }

            disponibilita.setFine(veicolo.fineDisponibilitaTimestamp());
            disponibilita.setUscitaParco(Boolean.TRUE);

            sx.save(disponibilita);
        }
    }

    public static void eliminaDisponibilitaVeicolo(Session sx, Transaction tx, MROldParcoVeicoli veicolo) {
        sx.createQuery("delete from MROldDisponibilitaVeicolo d where d.veicolo = :veicolo"). //NOI18N
                setParameter("veicolo", veicolo). //NOI18N
                executeUpdate();
    }

    public static void creaDisponibilitaVeicolo(Session sx, Transaction tx, MROldParcoVeicoli veicolo) {
        veicolo = (MROldParcoVeicoli) sx.get(MROldParcoVeicoli.class, veicolo.getId());

        log.debug("Creazione disponibilita per veicolo: " + veicolo); //NOI18N

        sx.createQuery("delete from MROldDisponibilitaVeicolo d where d.veicolo = :veicolo"). //NOI18N
                setParameter("veicolo", veicolo). //NOI18N
                executeUpdate();

        if (!veicolo.getAbilitato() && veicolo.getTarga().equals("XXX" + veicolo.getId())) { //NOI18N
            //I veicoli fittizi disabilitati non devono avere disponibilita'.
            return;
        }

        MROldDisponibilitaVeicolo disponibilita = new MROldDisponibilitaVeicolo(
                veicolo,
                veicolo.getDataAcquisto(),
                null,
                veicolo.getSede(),
                Boolean.TRUE,
                Boolean.FALSE);

        Iterator<MROldMovimentoAuto> movimenti = (Iterator<MROldMovimentoAuto>) sx.createQuery(
                "select m from MROldMovimentoAuto m where m.veicolo = :veicolo and m.annullato = :false order by m.inizio"). //NOI18N
                setParameter("veicolo", veicolo). //NOI18N
                setParameter("false", Boolean.FALSE). //NOI18N
                iterate();

        while (movimenti.hasNext()) {
            MROldMovimentoAuto movimento = movimenti.next();
            disponibilita.setFine(movimento.getInizio());

            if (disponibilita.getSede() == null) {
                disponibilita.setSede(movimento.getSedeUscita());
            }

            sx.save(disponibilita);

            disponibilita = new MROldDisponibilitaVeicolo(
                    veicolo,
                    movimento.getFine(),
                    null,
                    movimento.getSedeRientro(),
                    Boolean.FALSE,
                    Boolean.FALSE);
        }

        // Se il veicolo non ha alcun movimento, mettiamo la sede del veicolo.
        if (disponibilita.getSede() == null) {
            disponibilita.setSede(veicolo.getSede());
        }

        disponibilita.setFine(veicolo.fineDisponibilitaTimestamp());
        disponibilita.setUscitaParco(Boolean.TRUE);
        sx.save(disponibilita);
}

    public static void creaDisponibilitaVeicolo(Session sx, MROldParcoVeicoli veicolo) {
        try {
            veicolo = (MROldParcoVeicoli) sx.get(MROldParcoVeicoli.class, veicolo.getId());

            log.debug("Creazione disponibilita per veicolo: " + veicolo); //NOI18N


            sx.createQuery("delete from MROldDisponibilitaVeicolo d where d.veicolo = :veicolo"). //NOI18N
                    setParameter("veicolo", veicolo). //NOI18N
                    executeUpdate();



            if (!veicolo.getAbilitato() && veicolo.getTarga().equals("XXX" + veicolo.getId())) { //NOI18N
                //I veicoli fittizi disabilitati non devono avere disponibilita'.
                return;
            }

            MROldDisponibilitaVeicolo disponibilita = new MROldDisponibilitaVeicolo(
                    veicolo,
                    veicolo.getDataAcquisto(),
                    null,
                    null,
                    Boolean.TRUE,
                    Boolean.FALSE);

            Iterator<MROldMovimentoAuto> movimenti = (Iterator<MROldMovimentoAuto>) sx.createQuery(
                    "select m from MROldMovimentoAuto m where m.veicolo = :veicolo and m.annullato = :false order by m.inizio"). //NOI18N
                    setParameter("veicolo", veicolo). //NOI18N
                    setParameter("false", Boolean.FALSE). //NOI18N
                    iterate();

            int count = 0;

            while (movimenti.hasNext()) {
                MROldMovimentoAuto movimento = movimenti.next();
                disponibilita.setFine(movimento.getInizio());

                if (disponibilita.getSede() == null) {
                    if (count == 0) {
                        disponibilita.setSede(movimento.getVeicolo().getSede());
                    } else {
                        disponibilita.setSede(movimento.getSedeUscita());
                    }
                }

                sx.save(disponibilita);

                disponibilita = new MROldDisponibilitaVeicolo(
                        veicolo,
                        movimento.getFine(),
                        null,
                        movimento.getSedeRientro(),
                        Boolean.FALSE,
                        Boolean.FALSE);

                count++;
            }

            // Se il veicolo non ha alcun movimento, mettiamo la sede del veicolo.
            if (disponibilita.getSede() == null) {
                disponibilita.setSede(veicolo.getSede());
            }

            disponibilita.setFine(veicolo.fineDisponibilitaTimestamp());
            disponibilita.setUscitaParco(Boolean.TRUE);
            sx.save(disponibilita);
            sx.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void aggiornaDisponibilita(Session sx, Transaction tx, MROldMovimentoAuto movimentoVecchio, MROldMovimentoAuto movimentoNuovo) throws HibernateException, VeicoloNonDisponibileException {
        if (movimentoVecchio != null
                && !Boolean.TRUE.equals(movimentoVecchio.getAnnullato())
                && movimentoNuovo != null
                && !Boolean.TRUE.equals(movimentoNuovo.getAnnullato())
                && new EqualsBuilder().append(movimentoVecchio.getVeicolo(), movimentoNuovo.getVeicolo()).
                append(movimentoVecchio.getSedeUscita(), movimentoNuovo.getSedeUscita()).
                append(movimentoVecchio.getSedeRientro(), movimentoNuovo.getSedeRientro()).
                append(movimentoVecchio.getInizio().getTime(), movimentoNuovo.getInizio().getTime()).
                append(movimentoVecchio.getFine().getTime(), movimentoNuovo.getFine().getTime()).
                isEquals()) {
            // Non e'  cambiato niente. Usciamo.
        } else {
            MROldParcoVeicoli veicoloVecchio = null;
            MROldParcoVeicoli veicoloNuovo = null;

            if (movimentoVecchio != null && movimentoVecchio.getVeicolo() != null && movimentoVecchio.getVeicolo().getId() != null) {
                //veicoloVecchio = (ParcoVeicoli) sx.get(ParcoVeicoli.class, movimentoVecchio.getVeicolo().getId());
                veicoloVecchio = (MROldParcoVeicoli) sx.get( MROldParcoVeicoli.class, movimentoVecchio.getVeicolo().getId());
            }

            if (movimentoNuovo != null && movimentoNuovo.getVeicolo() != null && movimentoNuovo.getVeicolo().getId() != null) {
                //veicoloNuovo = (ParcoVeicoli) sx.get(ParcoVeicoli.class, movimentoNuovo.getVeicolo().getId());
                veicoloNuovo = (MROldParcoVeicoli) sx.get(MROldParcoVeicoli.class, movimentoNuovo.getVeicolo().getId());
            }

            //TODO Da spostare i lock dentro salvaElemento()...
            if (movimentoVecchio != null && movimentoNuovo != null) {
                if (movimentoVecchio.getVeicolo().equals(movimentoNuovo.getVeicolo())) {
                    sx.lock(veicoloVecchio, LockMode.UPGRADE);
                }
            } else if (movimentoVecchio != null) {
                sx.lock(veicoloVecchio, LockMode.UPGRADE);
            } else if (movimentoNuovo != null) {
                sx.lock(veicoloNuovo, LockMode.UPGRADE);
            }

            if (movimentoVecchio != null && !Boolean.TRUE.equals(movimentoVecchio.getAnnullato())) {
                aggiornaImpegniMovimentoEliminato(sx, tx, movimentoVecchio);
            }

            if (movimentoNuovo != null && !Boolean.TRUE.equals(movimentoNuovo.getAnnullato())) {
                aggiornaImpegniMovimentoNuovo(sx, tx, movimentoNuovo);
            }
        }
    }

    private static MROldDisponibilitaVeicolo salvaDisponibilita(Session sx, MROldDisponibilitaVeicolo aDisponibilita) {
        MROldDisponibilitaVeicolo aDisponibilitaEsistente = (MROldDisponibilitaVeicolo) sx.get(MROldDisponibilitaVeicolo.class, aDisponibilita);
        if (aDisponibilitaEsistente != null) {
            aDisponibilitaEsistente.setIngressoParco(aDisponibilita.getIngressoParco());
            aDisponibilitaEsistente.setUscitaParco(aDisponibilita.getUscitaParco());
            aDisponibilita = aDisponibilitaEsistente;
        }
        sx.save(aDisponibilita);
        return aDisponibilita;
    }

    /**
     * Aggiorna la disponibilita ed i movimenti futuri del veicolo
     * quando viene eliminato un movimento.
     * @param sx La session da usare
     * @param tx La transazione aperta
     * @param movimento Il movimento che sara' eliminato.
     * @throws org.hibernate.HibernateException Se qualcosa va storto.
     */
    public static void aggiornaImpegniMovimentoEliminato(Session sx, Transaction tx, MROldMovimentoAuto movimento) throws HibernateException, VeicoloNonDisponibileException {
        if(movimento.getVeicolo() != null) {
        MROldGruppo gruppo = (MROldGruppo) sx.get(MROldGruppo.class, movimento.getVeicolo().getGruppo().getId());

        MROldDisponibilitaVeicolo disponibilitaSx = (MROldDisponibilitaVeicolo) sx.createQuery(
                "select d from MROldDisponibilitaVeicolo d where d.veicolo = :veicolo and d.fine = :inizio"). //NOI18N
                setParameter("veicolo", movimento.getVeicolo()). //NOI18N
                setParameter("inizio", movimento.getInizio()). //NOI18N
                uniqueResult();

        MROldDisponibilitaVeicolo disponibilitaDx = (MROldDisponibilitaVeicolo) sx.createQuery(
                "select d from MROldDisponibilitaVeicolo d where d.veicolo = :veicolo and d.inizio = :fine"). //NOI18N
                setParameter("veicolo", movimento.getVeicolo()). //NOI18N
                setParameter("fine", movimento.getFine()). //NOI18N
                uniqueResult();

        if (disponibilitaDx == null || disponibilitaSx == null) {
            throw new VeicoloNonDisponibileException(bundle.getString("DisponibilitaUtils.msgDisponibilitaNonAggiornata"));
        }

        MROldSede sedeDisponibilita = null;
	
        /*
         * Andrea
         * new cases if the movement hasn't the left availability
         */
//        Date dateStartAvailabilityNew = null;
//         //Cases update movement: Availability, Movement, Availability
//        if(disponibilitaDx != null && disponibilitaSx != null){
//            dateStartAvailabilityNew =  disponibilitaSx.getInizio();
//        }
//        //Cases update movement 2: Movement1, Movement2, Availability
//        if(disponibilitaDx != null && disponibilitaSx == null){
//            dateStartAvailabilityNew = movimento.getInizio();
//        }


        //Works for both situation
        MROldDisponibilitaVeicolo disponibilitaNuova = new MROldDisponibilitaVeicolo(
                movimento.getVeicolo(),
                disponibilitaSx.getInizio(),
                disponibilitaDx.getFine(),
                null,
                disponibilitaSx.getIngressoParco(),
                disponibilitaDx.getUscitaParco());
        Date timestamp = FormattedDate.formattedTimestamp();

        /*previuos case, ds, mv, dx*/
//        if(disponibilitaSx != null){
             if (timestamp.getTime() >= disponibilitaNuova.getInizio().getTime() && timestamp.getTime() <= disponibilitaNuova.getFine().getTime()) {
                disponibilitaNuova.setSede(movimento.getVeicolo().getSede());
            } else {
                if (disponibilitaSx.getIngressoParco() && disponibilitaDx.getUscitaParco()) {
                    disponibilitaNuova.setSede(movimento.getVeicolo().getSede());
                } else if (disponibilitaSx.getIngressoParco()) {
                    disponibilitaNuova.setSede(disponibilitaDx.getSede());
                } else {
                    disponibilitaNuova.setSede(disponibilitaSx.getSede());
                }
            }
            sx.delete(disponibilitaSx);
            sx.delete(disponibilitaDx);
            sx.save(disponibilitaNuova);
//        }
//        else{
//            if (timestamp.getTime() >= disponibilitaNuova.getInizio().getTime() && timestamp.getTime() <= disponibilitaNuova.getFine().getTime()) {
//                disponibilitaNuova.setSede(movimento.getVeicolo().getSede());
//            } else {
//                disponibilitaNuova.setSede(movimento.getSedeUscita());
//            }
//            sx.delete(disponibilitaDx);
//            sx.save(disponibilitaNuova);
//        }
       

        if (!disponibilitaNuova.getUscitaParco()
                && !gruppo.getSpeciale()) {
            //Eliminiamo in modo ricorsivo i movimenti per prenotazione che non seguono la catena delle sedi.            
            // Ci fermiamo se troviamo almeno un movimento valido che non e' prenotazione.
            MROldMovimentoAuto prossimoMovimento = (MROldMovimentoAuto) sx.createQuery(
                    "select m from MROldMovimentoAuto m where m.veicolo = :veicolo and m.inizio = :fine and m.annullato = :false order by m.inizio"). //NOI18N
                    setParameter("veicolo", movimento.getVeicolo()). //NOI18N
                    setParameter("fine", disponibilitaNuova.getFine()). //NOI18N
                    setParameter("false", Boolean.FALSE). //NOI18N
                    uniqueResult();
            if (prossimoMovimento != null
                    && prossimoMovimento.getCausale().getPrenotazione()
                    && !prossimoMovimento.getSedeUscita().equals(disponibilitaNuova.getSede())) {
                sx.delete(prossimoMovimento);
//                sx.flush();
//                sx.evict(prossimoMovimento);
                aggiornaImpegniMovimentoEliminato(sx, tx, prossimoMovimento);
            }
        }
        } else {
            System.out.println("The movement does not have a vehicle to which to update the availability");
        }
    }

    public static void aggiornaImpegniMovimentoNuovo(Session sx, Transaction tx, MROldMovimentoAuto movimento) throws VeicoloNonDisponibileException {
        if (movimento.getVeicolo() != null) {
            MROldGruppo gruppo = (MROldGruppo) sx.get(MROldGruppo.class, movimento.getVeicolo().getGruppo().getId());
            //Carichiamo la disponibilita da modificare.
            MROldDisponibilitaVeicolo disponibilitaVecchia = (MROldDisponibilitaVeicolo) sx.createQuery(
                    "select d from MROldDisponibilitaVeicolo d where d.veicolo = :veicolo and d.inizio <= :inizio and d.fine >= :fine"). //NOI18N
                    setParameter("veicolo", movimento.getVeicolo()). //NOI18N
                    setParameter("inizio", movimento.getInizio()). //NOI18N
                    setParameter("fine", movimento.getFine()). //NOI18N
                    uniqueResult();

            if (disponibilitaVecchia == null) {
                throw new VeicoloNonDisponibileException(bundle.getString("DisponibilitaUtils.msgDisponibilitaNonTrovataPeriodoSelezionato"));
            }

            MROldDisponibilitaVeicolo disponibilitaSx = new MROldDisponibilitaVeicolo(
                    movimento.getVeicolo(),
                    disponibilitaVecchia.getInizio(),
                    movimento.getInizio(),
                    disponibilitaVecchia.getSede(),
                    disponibilitaVecchia.getIngressoParco(),
                    Boolean.FALSE);
            MROldDisponibilitaVeicolo disponibilitaDx = new MROldDisponibilitaVeicolo(
                    movimento.getVeicolo(),
                    movimento.getFine(),
                    disponibilitaVecchia.getFine(),
                    movimento.getSedeRientro(),
                    Boolean.FALSE,
                    disponibilitaVecchia.getUscitaParco());

            sx.delete(disponibilitaVecchia);
            sx.save(disponibilitaSx);
            sx.save(disponibilitaDx);

            if (!disponibilitaVecchia.getUscitaParco() && !gruppo.getSpeciale() && !movimento.getSedeUscita().equals(movimento.getSedeRientro())) {
                //Eliminiamo in modo ricorsivo i movimenti per prenotazione che non seguono la catena delle sedi.
                // Ci fermiamo se troviamo almeno un movimento valido che non e' prenotazione.
                MROldMovimentoAuto prossimoMovimento = (MROldMovimentoAuto) sx.createQuery(
                        "select m from MROldMovimentoAuto m where m.veicolo = :veicolo and m.inizio = :fine and m.annullato = :false order by m.inizio"). //NOI18N
                        setParameter("veicolo", movimento.getVeicolo()). //NOI18N
                        setParameter("fine", disponibilitaVecchia.getFine()). //NOI18N
                        setParameter("false", Boolean.FALSE). //NOI18N
                        uniqueResult();
                if (prossimoMovimento != null
                        && prossimoMovimento.getCausale().getPrenotazione()
                        && !prossimoMovimento.getSedeUscita().equals(disponibilitaDx.getSede())) {
                    sx.delete(prossimoMovimento);
                    sx.flush();
                    sx.evict(prossimoMovimento);
                    aggiornaImpegniMovimentoEliminato(sx, tx, prossimoMovimento);
                }
            }
        } else {
            System.out.println("The movement does not have a vehicle to which to update the availability");
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
    public static List cercaVeicoliDisponibili(
            Session mySession,
            MROldSede sedeUscita,
            MROldGruppo gruppo,
            Date inizio,
            Date fine,
            boolean veicoliMovimentati,
            boolean mezzoUnico,
            boolean gruppoSpeciale) throws HibernateException {

        List veicoliLiberi = new ArrayList();

        Query queryIdentifiersVeicoli = null;

        String selectString =
                "select d.veicolo.id from MROldDisponibilitaVeicolo d left join d.veicolo v " + //NOI18N
                        (gruppoSpeciale && sedeUscita.getRegione() != null? "left join d.sede s " : "") + //NOI18N
                        "left join v.movimenti m " + //NOI18N
                        "with " + //NOI18N
                        "(" + //NOI18N
                        "   m.fine > :oggi and " + //NOI18N
                        "   m.annullato = :false and " + //NOI18N
                        "   ( m.prenotazione.id is not null or m.contratto.id is not null ) " + //NOI18N
                        ") " + //NOI18N
                        "where " + //NOI18N
                        (gruppoSpeciale && sedeUscita.getRegione() != null? "s.regione.id = :idRegione " : "d.sede.id = :idSede ") + //NOI18N
                        "and d.inizio <= :inizio " + //NOI18N
                        "and d.fine >= :fine "; //NOI18N


        selectString += " and v.abilitato = :true "; //NOI18N

        String gruppoString = "v.gruppo.id = :idGruppo "; //NOI18N

        String orderString = "group by d.veicolo.id, v.km,d.sede.descrizione having " + //NOI18N
                (veicoliMovimentati ? "count(m) > 0 " : "count(m) = 0 ") + //NOI18N
                "order by d.sede.descrizione desc, v.km asc"; //NOI18N

        if (gruppo != null && gruppo.getId() != null) {
            queryIdentifiersVeicoli = mySession.createQuery(
                    selectString + " and " + //NOI18N
                            gruppoString +
                            orderString);
            queryIdentifiersVeicoli.setParameter("idGruppo", gruppo.getId()); //NOI18N
        } else {
            queryIdentifiersVeicoli = mySession.createQuery(selectString + orderString);
        }
        if (gruppoSpeciale && sedeUscita.getRegione() != null) {
            queryIdentifiersVeicoli.setParameter("idRegione", sedeUscita.getRegione().getId()); //NOI18N
        } else {
            queryIdentifiersVeicoli.setParameter("idSede", sedeUscita.getId()); //NOI18N
        }
        queryIdentifiersVeicoli.setParameter("inizio", inizio); //NOI18N
        queryIdentifiersVeicoli.setParameter("fine", fine); //NOI18N
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





    public static Boolean cercaVeicoloDisponibile(
            Session mySession,
            MROldParcoVeicoli veicolo,
            MROldSede sedeUscita,
            MROldGruppo gruppo,
            Date inizio,
            Date fine,
            boolean gruppoSpeciale) throws HibernateException {

        Boolean returnValue = false;
        if(veicolo != null){
            Query queryIdentifiersVeicoli = null;

            String selectString =
                    "select d.veicolo.id from MROldDisponibilitaVeicolo d left join d.veicolo v " + //NOI18N
                            (gruppoSpeciale && sedeUscita.getRegione() != null? "left join d.sede s " : "") + //NOI18N
                            "where " + //NOI18N
                            (gruppoSpeciale && sedeUscita.getRegione() != null ? "s.regione.id = :idRegione " : "d.sede.id = :idSede ") + //NOI18N
                            "and v.id = :idVeicolo " + //NOI18N
                            "and d.inizio <= :inizio " + //NOI18N
                            "and d.fine >= :fine ";


            selectString += " and v.abilitato = :true "; //NOI18N

            String gruppoString = "v.gruppo.id = :idGruppo "; //NOI18N

            String orderString =
                    "group by d.veicolo.id, v.km  "; //NOI18N
            // SOLO PER POSTGRES >= di 8.1
            //"order by sum(m.fine - greatest(:oggi, m.inizio)) desc, v.km asc";  //NOI18N
            if (gruppo != null && gruppo.getId() != null) {
                queryIdentifiersVeicoli = mySession.createQuery(
                        selectString + " and " + //NOI18N
                                gruppoString
                                + orderString);
                queryIdentifiersVeicoli.setParameter("idGruppo", gruppo.getId()); //NOI18N
            } else {
                queryIdentifiersVeicoli = mySession.createQuery(
                        selectString
                                + orderString);
            }
            if (gruppoSpeciale && sedeUscita.getRegione() != null) {
                queryIdentifiersVeicoli.setParameter("idRegione", sedeUscita.getRegione().getId()); //NOI18N
            } else {
                queryIdentifiersVeicoli.setParameter("idSede", sedeUscita.getId()); //NOI18N
            }
            queryIdentifiersVeicoli.setParameter("inizio", inizio); //NOI18N
            queryIdentifiersVeicoli.setParameter("fine", fine); //NOI18N
            queryIdentifiersVeicoli.setParameter("idVeicolo", veicolo.getId());
            queryIdentifiersVeicoli.setParameter("true", Boolean.TRUE); //NOI18N
            List identifiers = queryIdentifiersVeicoli.list();
            if (identifiers != null && identifiers.size() > 0) {
                returnValue = true;
            }
        }

        return returnValue;
    }





    
    public static MROldParcoVeicoli cercaVeicoloDisponibile(
            Session sx,
            MROldSede sedeUscita,
            MROldGruppo gruppo,
            Date inizio,
            Date fine) throws HibernateException {
        List veicoli = cercaVeicoliDisponibili(sx, sedeUscita, gruppo, inizio, fine, true, true, false);
        if (veicoli.size() == 0) {
            veicoli = cercaVeicoliDisponibili(sx, sedeUscita, gruppo, inizio, fine, false, true, false);
        }
        if (veicoli.size() > 0) {
            return (MROldParcoVeicoli) veicoli.get(0);
        } else {
            return null;
        }
    }
	//Special group true
    public static MROldParcoVeicoli cercaVeicoloDisponibileSpecialGroup(
            Session sx,
            MROldSede sedeUscita,
            MROldGruppo gruppo,
            Date inizio,
            Date fine) throws HibernateException {
        List veicoli = cercaVeicoliDisponibili(sx, sedeUscita, gruppo, inizio, fine, true, true, true);
        if (veicoli.size() == 0) {
            veicoli = cercaVeicoliDisponibili(sx, sedeUscita, gruppo, inizio, fine, false, true, true);
        }
        if (veicoli.size() > 0) {
            return (MROldParcoVeicoli) veicoli.get(0);
        } else {
            return null;
        }
    }

    public static MROldDisponibilitaVeicolo disponibilitaIniziale(Session sx, MROldParcoVeicoli veicolo) {
        return (MROldDisponibilitaVeicolo) sx.createQuery(
                "select d from MROldDisponibilitaVeicolo d where d.veicolo = :veicolo and d.ingressoParco = :true"). //NOI18N
                setParameter("veicolo", veicolo). //NOI18N
                setParameter("true", Boolean.TRUE). //NOI18N
                uniqueResult();
    }

    public static MROldDisponibilitaVeicolo disponibilitaFinale(Session sx, MROldParcoVeicoli veicolo) {
        return (MROldDisponibilitaVeicolo) sx.createQuery(
                "select d from MROldDisponibilitaVeicolo d where d.veicolo = :veicolo and d.uscitaParco = :true"). //NOI18N
                setParameter("veicolo", veicolo). //NOI18N
                setParameter("true", Boolean.TRUE). //NOI18N
                uniqueResult();
    }

    public static List selezionaVeicoloDisponibile(
            Session sx,
            MROldSede sedeUscita,
            MROldGruppo gruppo,
            Date inizio,
            Date fine) throws HibernateException {
        List veicoli = cercaVeicoliDisponibili(sx, sedeUscita, gruppo, inizio, fine, true, false, false);
        veicoli.addAll(cercaVeicoliDisponibili(sx, sedeUscita, gruppo, inizio, fine, false, false, false));

        if (veicoli.size() == 0) {
            veicoli.addAll(cercaVeicoliDisponibili(sx, sedeUscita, gruppo, inizio, fine, true, false, true));
            veicoli.addAll(cercaVeicoliDisponibili(sx, sedeUscita, gruppo, inizio, fine, false, false, true));
        }

        return veicoli;
    }

//    private static MROldParcoVeicoli selezionaVeicoloDisponibile(Session sx, Component parent, List veicoli, String title, Date inizio, Date fine) {
//        if (veicoli != null && veicoli.size() > 0) {
//            JDialogSelezioneVeicolo.seleziona(
//                    sx,
//                    parent,
//                    true,
//                    title,
//                    veicoli,
//                    inizio,
//                    fine,
//                    JDialogSelezioneVeicolo.SINGLE_OBJECT_SELECTION);
//            if (JDialogSelezioneVeicolo.getReturnStatus() == JDialogSelezioneVeicolo.RET_OK) {
//                return (MROldParcoVeicoli) JDialogSelezioneVeicolo.getSelectedElements().
//                        get(0);
//            }
//        }
//        return null;
//    }

    public static Boolean isVeicoloImpegnato(Session sx, MROldParcoVeicoli veicolo) {
        Number movimentiAperti = (Number) sx.createQuery(
                "select count(m) from MROldMovimentoAuto m " + // NOI18N
                "where m.veicolo = :veicolo " + // NOI18N
                "and m.chiuso = :false " + // NOI18N
                "and m.causale.prenotazione = :false"). // NOI18N
                setParameter("veicolo", veicolo). // NOI18N
                setParameter("false", Boolean.FALSE). // NOI18N
                uniqueResult();
        return (movimentiAperti != null && movimentiAperti.intValue() > 0);
    }

    /**
     * Aggiorna lo stato di impegno del mezzo cercando se esiste un movimento aperto.
     * @param sx Sessione al database.
     * @param veicolo Il veicolo da verificare se e' impegnato
     * @return true se lo stato di impegno del veicolo e' stato modificato, false se non e' stata fatta alcuna modifica.     *
     */
    public static boolean aggiornaVeicoloImpegnato(Session sx, MROldParcoVeicoli veicolo) {
        Boolean veicoloImpegnato = isVeicoloImpegnato(sx, veicolo);
        if (!veicoloImpegnato.equals(veicolo.getImpegnato())) {
            veicolo.setImpegnato(veicoloImpegnato);
            return true;
        }
        return false;
    }

    public static boolean checkVehicleAvailability(Session sx, Transaction tx, MROldSede sedeUscita,
                                                   MROldGruppo gruppo, Date startDate, MROldSede sedeRientro,
                                                   MROldMovimentoAuto movimento, Date endDate){

        boolean isVehicleAvailable = false;

        MROldDisponibilitaVeicolo disponibilitaSx = (MROldDisponibilitaVeicolo) sx.createQuery(
                "select d from MROldDisponibilitaVeicolo d left join d.veicolo v " +
                        "where " +
                        "d.sede.id = :idSede and " +
                        "v.gruppo.id = :idGruppo and " +
                        "d.veicolo.id = :veicolo and d.fine = :inizio and d.inizio <= :startDate"). //NOI18N
                setParameter("idSede", sedeUscita.getId()). //NOI18N
                setParameter("idGruppo", gruppo.getId()). //NOI18N
                setParameter("veicolo", movimento.getVeicolo().getId()). //NOI18N
                setParameter("inizio", movimento.getInizio()). //NOI18N
                setParameter("startDate", startDate). //NOI18N
                uniqueResult();


        MROldDisponibilitaVeicolo disponibilitaDx = (MROldDisponibilitaVeicolo) sx.createQuery(
                "select d from MROldDisponibilitaVeicolo d left join d.veicolo v " +
                        "where " +
                      //  "d.sede.id = :idSede and " +
                        "v.gruppo.id = :idGruppo and " +
                        "d.veicolo.id = :veicolo and d.inizio = :fine and d.fine >= :endDate "). //NOI18N
              //  setParameter("idSede", sedeRientro.getId()). //NOI18N
                setParameter("idGruppo", gruppo.getId()). //NOI18N
                setParameter("veicolo", movimento.getVeicolo().getId()). //NOI18N
                setParameter("fine", movimento.getFine()). //NOI18N
                setParameter("endDate", endDate). //NOI18N
                uniqueResult();

        if (disponibilitaDx == null || disponibilitaSx == null) {
            Boolean isConflict = MovimentoAutoUtils.conflicts(sx, movimento.getVeicolo(), movimento, startDate, endDate);
            boolean isVehicleFineValid = false;
            if(!isConflict){
                if(movimento.getVeicolo().getDataScadenzaContratto() != null && movimento.getVeicolo().getDataScadenzaContratto().getTime()>=FormattedDate.formattedDate(endDate.getTime()).getTime()){
                    isVehicleFineValid  = true;
                }
                if(!isVehicleFineValid){
                    if(movimento.getVeicolo().getDataProroga1() != null && movimento.getVeicolo().getDataProroga1().getTime()>=FormattedDate.formattedDate(endDate.getTime()).getTime()){
                        isVehicleFineValid  = true;
                    }
                }
                if(!isVehicleFineValid){
                    if(movimento.getVeicolo().getDataProroga2() != null && movimento.getVeicolo().getDataProroga2().getTime()>=FormattedDate.formattedDate(endDate.getTime()).getTime()){
                        isVehicleFineValid  = true;
                    }
                }
                if(isVehicleFineValid){
                    if(movimento.getVeicolo().getGruppo()==gruppo && movimento.getVeicolo().getSede()==sedeUscita && movimento.getVeicolo().getAbilitato() == Boolean.TRUE){
                        isVehicleAvailable = true;
                    }
                } else {
                    isVehicleAvailable = false;
                }
            } else {
                isVehicleAvailable = false;
            }
        } else {
            isVehicleAvailable = true;
        }
        return isVehicleAvailable;
    }

    public static boolean checkVehicleAvailabilityPR(Session sx, Transaction tx, MROldSede sedeUscita,
                                                   MROldGruppo gruppo, Date startDate, MROldSede sedeRientro,
                                                   MROldMovimentoAuto movimento, Date endDate, MROldMovimentoAuto plannedRental){

        boolean isVehicleAvailable = false;

        MROldDisponibilitaVeicolo disponibilitaSx = (MROldDisponibilitaVeicolo) sx.createQuery(
                "select d from MROldDisponibilitaVeicolo d left join d.veicolo v " +
                        "where " +
                        "d.sede.id = :idSede and " +
                        "v.gruppo.id = :idGruppo and " +
                        "d.veicolo.id = :veicolo and d.fine = :inizio and d.inizio <= :startDate"). //NOI18N
                setParameter("idSede", sedeUscita.getId()). //NOI18N
                setParameter("idGruppo", gruppo.getId()). //NOI18N
                setParameter("veicolo", movimento.getVeicolo().getId()). //NOI18N
                setParameter("inizio", movimento.getInizio()). //NOI18N
                setParameter("startDate", startDate). //NOI18N
                uniqueResult();


        MROldDisponibilitaVeicolo disponibilitaDx = (MROldDisponibilitaVeicolo) sx.createQuery(
                "select d from MROldDisponibilitaVeicolo d left join d.veicolo v " +
                        "where " +
                        //  "d.sede.id = :idSede and " +
                        "v.gruppo.id = :idGruppo and " +
                        "d.veicolo.id = :veicolo and d.inizio = :fine and d.fine >= :endDate "). //NOI18N
                //  setParameter("idSede", sedeRientro.getId()). //NOI18N
                setParameter("idGruppo", gruppo.getId()). //NOI18N
                setParameter("veicolo", movimento.getVeicolo().getId()). //NOI18N
                setParameter("fine", movimento.getFine()). //NOI18N
                setParameter("endDate", endDate). //NOI18N
                uniqueResult();

        if (disponibilitaDx == null || disponibilitaSx == null) {
            Boolean isConflict = false;
            if(plannedRental!=null){
                isConflict  = MovimentoAutoUtils.conflictsForPR(sx, movimento.getVeicolo(), movimento, startDate, endDate, plannedRental);
            } else {
                isConflict = MovimentoAutoUtils.conflicts(sx, movimento.getVeicolo(), movimento, startDate, endDate);
            }

            boolean isVehicleFineValid = false;
            if(!isConflict){
                if(movimento.getVeicolo().getDataScadenzaContratto() != null && movimento.getVeicolo().getDataScadenzaContratto().getTime()>=FormattedDate.formattedDate(endDate.getTime()).getTime()){
                    isVehicleFineValid  = true;
                }
                if(!isVehicleFineValid){
                    if(movimento.getVeicolo().getDataProroga1() != null && movimento.getVeicolo().getDataProroga1().getTime()>=FormattedDate.formattedDate(endDate.getTime()).getTime()){
                        isVehicleFineValid  = true;
                    }
                }
                if(!isVehicleFineValid){
                    if(movimento.getVeicolo().getDataProroga2() != null && movimento.getVeicolo().getDataProroga2().getTime()>=FormattedDate.formattedDate(endDate.getTime()).getTime()){
                        isVehicleFineValid  = true;
                    }
                }
                if(isVehicleFineValid){
                    if(movimento.getVeicolo().getGruppo()==gruppo && movimento.getVeicolo().getSede()==sedeUscita && movimento.getVeicolo().getAbilitato() == Boolean.TRUE){
                        isVehicleAvailable = true;
                    }
                } else {
                    isVehicleAvailable = false;
                }
            } else {
                isVehicleAvailable = false;
            }
        } else {
            isVehicleAvailable = true;
        }
        return isVehicleAvailable;
    }

    public static boolean markOtherMovementCancel(Session sx, Transaction tx, MROldMovimentoAuto oldMovimentoAuto) throws VeicoloNonDisponibileException{
        List<MROldMovimentoAuto> newMovement = sx.createQuery(
                "select m from MROldMovimentoAuto m where m.veicolo = :veicolo and m.id <>:movementId and m.annullato = :false and m.inizio <:fine and m.chiuso =:false"). //NOI18N
                setParameter("veicolo", oldMovimentoAuto.getVeicolo()). //NOI18N
                setParameter("movementId", oldMovimentoAuto.getId()). //NOI18N
                setParameter("fine", oldMovimentoAuto.getFine()). //NOI18N
                setParameter("false", Boolean.FALSE).list();
        if(newMovement.size()>0){
            for (MROldMovimentoAuto movement : newMovement){
                movement.setAnnullato(Boolean.TRUE);
                movement.setPrenotazione(null);
                movement.setContratto(null);
                sx.saveOrUpdate(movement);
                aggiornaImpegniMovimentoEliminato(sx, tx, movement);
            }
        }
        boolean isVehicleConflict = MovimentoAutoUtils.conflicts(sx, oldMovimentoAuto.getVeicolo(), oldMovimentoAuto, oldMovimentoAuto.getInizio(), oldMovimentoAuto.getFine());
        return isVehicleConflict;
    }
	
	public static MROldSede getSedeFromDis(MROldParcoVeicoli parcoVeicoli, Date inizio, Date fine){
        MROldSede sede = null;
        if(parcoVeicoli!=null){
            sede = parcoVeicoli.getSede();
            if(parcoVeicoli.getDisponibilita()!=null && parcoVeicoli.getDisponibilita().size()>0){
                List<MROldDisponibilitaVeicolo> tutteDisponibilita = new ArrayList(parcoVeicoli.getDisponibilita());
                for (MROldDisponibilitaVeicolo dispVeicolo : tutteDisponibilita) {
                    if (dispVeicolo.getInizio().getTime() <= inizio.getTime() && dispVeicolo.getFine().getTime() >= fine.getTime()) {
                        sede = dispVeicolo.getSede();
                    }
                }
            }
        }

        return sede;
    }

    public static List getDisponibiltalistFromVeh(Session sx, List vehList, Date inizio, Date fine, MROldPrenotazione prenotazione){
        List disList = new ArrayList();
        try{
            for(int i = 0; i < vehList.size(); i++) {
                MROldParcoVeicoli v = (MROldParcoVeicoli) vehList.get(i);
                MROldDisponibilitaVeicolo d = (MROldDisponibilitaVeicolo) sx.createQuery(
                        "select d from MROldDisponibilitaVeicolo d where " + //NOI18N
                                "d.inizio <= :inizio and " + //NOI18N
                                "d.fine >= :fine and " + //NOI18N
                                "d.veicolo = :veicolo"). //NOI18N
                        setParameter("veicolo", v). //NOI18N
                        setParameter("inizio", inizio). //NOI18N
                        setParameter("fine", fine). //NOI18N
                        setMaxResults(1).
                        uniqueResult();
                if(prenotazione!=null && d==null && prenotazione.getMovimento()!=null && prenotazione.getMovimento().getVeicolo()!=null && prenotazione.getMovimento().getVeicolo().getId()==v.getId()){
                    MROldDisponibilitaVeicolo d1 = (MROldDisponibilitaVeicolo) sx.createQuery(
                            "select d from MROldDisponibilitaVeicolo d where " + //NOI18N
                                    "d.fine = :inizio and " + //NOI18N
                                    "d.veicolo = :veicolo"). //NOI18N
                            setParameter("veicolo", v). //NOI18N
                            setParameter("inizio", prenotazione.getMovimento().getInizio()). //NOI18N
                            setMaxResults(1).
                            uniqueResult();
                    MROldDisponibilitaVeicolo d2 = (MROldDisponibilitaVeicolo) sx.createQuery(
                            "select d from MROldDisponibilitaVeicolo d where " + //NOI18N
                                    "d.inizio = :fine and " + //NOI18N
                                    "d.veicolo = :veicolo"). //NOI18N
                            setParameter("veicolo", v). //NOI18N
                            setParameter("fine", prenotazione.getMovimento().getFine()). //NOI18N
                            setMaxResults(1).
                            uniqueResult();
                    if(d1!=null && d2!=null && d1.getInizio().before(inizio) && d2.getFine().after(fine)){
                        d2.setInizio(d1.getInizio());
                        disList.add(d2);
                    }
                } else {
                    disList.add(d);
                }
            }
        } catch (Exception ex) {
            //Something went wrong!
        }

        return disList;
    }

}
