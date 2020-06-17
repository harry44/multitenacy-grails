/*
 * ImpegniUtils.java
 *
 * Created on 30 iunie 2005, 16:05
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package it.myrent.ee.api.utils;

import grails.util.Holders;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.Parameters;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.db.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.*;
/**
 *
 * @author jamess
 */
public class ImpegniUtils {

    /** Creates a new instance of ImpegniUtils */
    private ImpegniUtils() {
    }
    private static final Log log = LogFactory.getLog(ImpegniUtils.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");


    public static MROldContrattoNoleggio generaContratto(Session sx, MROldGruppo gruppo, MROldSede sede, Date inizio, Date fine) throws HibernateException {
        MROldNumerazione numerazione = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.CONTRATTI);
        Date dataOdierna = FormattedDate.formattedDate();
        Integer anno = DayUtils.getFieldFromDate(dataOdierna, Calendar.YEAR);
        Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
        String prefisso = numerazione.getPrefisso();
        if (gruppo != null) {
            gruppo = (MROldGruppo) sx.get(MROldGruppo.class, gruppo.getId());
        }

        User aUser = (User) sx.get(UserImpl.class, Parameters.getUser().getId());
        MROldAffiliato affiliato = aUser.getAffiliato();

        if (sede != null) {
            sede = (MROldSede) sx.get(MROldSede.class, sede.getId());
        } else if (aUser.getSedeOperativa() != null) {
            sede = (MROldSede) sx.get(MROldSede.class, aUser.getSedeOperativa().getId());
        }

        MROldContrattoNoleggio c = new MROldContrattoNoleggio(
                numerazione,
                prefisso,
                numero,
                dataOdierna,
                anno,
                affiliato,
                gruppo,
                sede,
                sede,
                inizio,
                fine,
                aUser);
        return c;
    }

    public static MROldPrenotazione generaPrenotazioneVeicolo(Session sx, MROldParcoVeicoli veicolo, Date inizio, Date fine, User mRUser) {

        if (veicolo != null && veicolo.getId() != null) {
            veicolo = (MROldParcoVeicoli) sx.get(MROldParcoVeicoli.class, veicolo.getId());
        }
        return generaPrenotazione(sx, veicolo, inizio, fine,  mRUser);

    }

    public static MROldPrenotazione generaPrenotazioneGruppo(Session sx, MROldParcoVeicoli veicolo, Date inizio, Date fine, User mRUser) {
        if (veicolo != null && veicolo.getId() != null) {
            veicolo = (MROldParcoVeicoli) sx.get(MROldParcoVeicoli.class, veicolo.getId());
        }
        if (veicolo != null) {
            return generaPrenotazione(sx, veicolo.getGruppo(), veicolo.getSede(), inizio, fine, mRUser);
        } else {
            return generaPrenotazione(sx, veicolo, inizio, fine, mRUser);
        }
    }

    /**
     * Crea una prenotazione con blocco della macchina (opzionale, se il veicolo non e' null)
     */
    public static MROldPrenotazione generaPrenotazione(Session sx, MROldParcoVeicoli tmpVeicolo, Date inizio, Date fine, User mRUser) throws HibernateException {
        MROldPrenotazione p;
        if (tmpVeicolo != null) {
            p = generaPrenotazione(sx, tmpVeicolo.getGruppo(), tmpVeicolo.getSede(), inizio, fine, mRUser);
            p.setMovimento(new MROldMovimentoAuto(
                    null, // MROldNumerazione
                    null, // Prefisso
                    null, // Numero
                    FormattedDate.formattedDate(), // Data
                    FormattedDate.annoCorrente(), // Anno
                    p.getAffiliato(),
                    MovimentoAutoUtils.leggiCausalePrenotazione(sx),
                    p, // MROldPrenotazione
                    null, // Contratto
                    tmpVeicolo,
                    null, // Danni
                    p.getSedeUscita(),
                    p.getSedeRientroPrevisto(),
                    p.getInizio(),
                    p.getFine(),
                    null, // Km inizio
                    null, // Combustibile uscita
                    null, // MROldAutista
                    null, // Note
                    p.getUserApertura(),
                    FormattedDate.formattedTimestamp(),
                    false));
        } else {
            p = generaPrenotazione(sx, null, null, inizio, fine, mRUser);
        }
        return p;
    }

    public static MROldPrenotazione generaPrenotazione(Session sx, MROldGruppo gruppo, MROldSede sede, Date inizio, Date fine, User user) {

        MROldNumerazione numerazione = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRENOTAZIONI, user);
        Date data = FormattedDate.formattedDate();
        Integer anno = FormattedDate.annoCorrente();
        Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
        String prefisso = numerazione.getPrefisso();


        User aUser = null;
        MROldAffiliato affiliato = null;

        if (user != null) {
            aUser = user;
            affiliato = aUser.getAffiliato();
        } else {
            affiliato = (MROldAffiliato) sx.get(MROldAffiliato.class, new Integer(1));
        }


        if (sede != null) {
            sede = (MROldSede) sx.get(MROldSede.class, sede.getId());
        } else if (aUser != null && aUser.getSedeOperativa() != null) {
            sede = (MROldSede) sx.get(MROldSede.class, aUser.getSedeOperativa().getId());
        }


        return new MROldPrenotazione(
                numerazione,
                prefisso,
                numero,
                data,
                anno,
                affiliato,
                gruppo,
                sede,
                sede,
                inizio,
                fine,
                aUser);


    }

    public static MROldPreventivo generaPreventivo(Session sx, MROldParcoVeicoli veicolo, Date inizio, Date fine) throws HibernateException {
        MROldSede sede = null;
        MROldGruppo gruppo = null;
        if (veicolo != null && veicolo.getId() != null) {
            veicolo = (MROldParcoVeicoli) sx.get(MROldParcoVeicoli.class, veicolo.getId());
        }

        User aUser = (User) sx.get(UserImpl.class, Parameters.getUser().getId());
        MROldAffiliato affiliato = aUser.getAffiliato();

        if (veicolo != null && veicolo.getSede() != null) {
            sede = (MROldSede) sx.get(MROldSede.class, veicolo.getSede().getId());
        } else {
            if (aUser.getSedeOperativa() != null) {
                sede = (MROldSede) sx.get(MROldSede.class, aUser.getSedeOperativa().getId());
            }
        }
        if (veicolo != null && veicolo.getGruppo() != null) {
            gruppo = (MROldGruppo) sx.get(MROldGruppo.class, veicolo.getGruppo().getId());
        }

        Date data = FormattedDate.formattedDate();
        Integer anno = FormattedDate.annoCorrente();
        MROldNumerazione numerazione = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PREVENTIVI);
        Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
        String prefisso = numerazione.getPrefisso();
        return new MROldPreventivo(
                numerazione,
                prefisso,
                numero,
                data,
                anno,
                affiliato,
                gruppo,
                sede,
                sede,
                inizio,
                fine);
    }

   

    /**
     * Creazione della prenotazione da un preventivo.
     * Il preventivo dovrebbe avere tutti i dati validi.
     * Il preventivo puo' essere anche NON persistente.
     */
    public static MROldPrenotazione generaPrenotazioneDaPreventivo(Session sx, MROldPreventivo aPreventivo) throws HibernateException {
        // FIXME Generazione prenotazione da preventivo: da aggiustare la roba lazy.
        if (aPreventivo == null) {
            return null;
        }

        MROldNumerazione numerazione = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRENOTAZIONI);
        Date data = FormattedDate.formattedDate();
        Integer anno = FormattedDate.annoCorrente();
        Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
        String prefisso = numerazione.getPrefisso();

        if (aPreventivo.getId() != null) {
            aPreventivo = (MROldPreventivo) sx.get(MROldPreventivo.class, aPreventivo.getId());
        }

        User aUser = (User) sx.get(UserImpl.class, Parameters.getUser().getId());
        MROldAffiliato affiliato = aUser.getAffiliato();

        MROldSede sedeUscita = (MROldSede) sx.get(MROldSede.class, aPreventivo.getSedeUscita().getId());
        MROldSede sedeRientroPrevisto = (MROldSede) sx.get(MROldSede.class, aPreventivo.getSedeRientroPrevisto().getId());
        MROldGruppo gruppo = (MROldGruppo) sx.get(MROldGruppo.class, aPreventivo.getGruppo().getId());

        Date inizio = aPreventivo.getInizio();
        Date fine = aPreventivo.getFine();

        MROldPrenotazione aPrenotazione = new MROldPrenotazione(
                numerazione,
                prefisso,
                numero,
                data,
                anno,
                affiliato,
                gruppo,
                sedeUscita,
                sedeRientroPrevisto,
                inizio,
                fine,
                aUser);
        if (aPreventivo.getCommissione() != null) {
            aPrenotazione.setCommissione(aPreventivo.getCommissione());
            if (aPreventivo.getCommissione().getId() == null) {
                aPrenotazione.setCommissione(aPreventivo.getCommissione());
                aPrenotazione.getCommissione().setPreventivo(null);
                aPrenotazione.getCommissione().setPrenotazione(aPrenotazione);
            }
        }

        aPrenotazione.setCliente((MROldClienti) sx.get(MROldClienti.class, aPreventivo.getCliente().getId()));
        aPrenotazione.setTariffa(new MROldTariffa(aPreventivo.getTariffa()));
        aPrenotazione.setPagamento((MROldPagamento) sx.get(MROldPagamento.class, aPreventivo.getPagamento().getId()));

        if (aPreventivo.getConducente1() != null) {
            aPrenotazione.setConducente1((MROldConducenti) sx.get(MROldConducenti.class, aPreventivo.getConducente1().getId()));
            if (aPreventivo.getConducente2() != null) {
                aPrenotazione.setConducente2((MROldConducenti) sx.get(MROldConducenti.class, aPreventivo.getConducente2().getId()));
                if (aPreventivo.getConducente3() != null) {
                    aPrenotazione.setConducente3((MROldConducenti) sx.get(MROldConducenti.class, aPreventivo.getConducente3().getId()));
                }
            }
        }
        aPrenotazione.setNote(aPreventivo.getNote());
        aPrenotazione.setScontoPercentuale(aPreventivo.getScontoPercentuale());

        return aPrenotazione;
    }

    public static MROldMovimentoAuto generaMovimentoAuto(Session sx, MROldParcoVeicoli aVeicolo, Date inizio, Date fine) throws HibernateException {
        Date data = FormattedDate.formattedDate();
        Date dataApertura = FormattedDate.formattedTimestamp();
        Integer anno = FormattedDate.annoCorrente();
        MROldSede sede = null;
        Integer km = null, combustibile = null;
        Set danniPresenti = null;

        User aUser = (User) sx.get(UserImpl.class, Parameters.getUser().getId());
        MROldAffiliato affiliato = aUser.getAffiliato();

        if (aVeicolo != null) {
            aVeicolo = (MROldParcoVeicoli) sx.get(MROldParcoVeicoli.class, aVeicolo.getId());
            if (aVeicolo.getSede() != null) {
                sede = (MROldSede) sx.get(MROldSede.class, aVeicolo.getSede().getId());
            }
            km = aVeicolo.getKm();
            combustibile = aVeicolo.getLivelloCombustibile();
            danniPresenti = new HashSet();
            Iterator danni = aVeicolo.getDanni().iterator();
            while (danni.hasNext()) {
                MROldDanno aDanno = (MROldDanno) danni.next();
                if (aDanno.getRiparato().equals(Boolean.FALSE)) {
                    danniPresenti.add(aDanno);
                }
            }
        }

        if (sede == null && aUser.getSedeOperativa() != null) {
            sede = (MROldSede) sx.get(MROldSede.class, aUser.getSedeOperativa().getId());
        }

        return new MROldMovimentoAuto(
                null, // MROldNumerazione
                null, // Prefisso
                null, // Numero
                data,
                anno,
                affiliato,
                null, // Causale
                null, // MROldPrenotazione
                null, // Contratto
                aVeicolo,
                danniPresenti,
                sede,
                sede,
                inizio,
                fine,
                km,
                combustibile,
                null, // MROldAutista
                null, // Note
                aUser,
                dataApertura,
                false);
    }

    /**
     * Legge i conducenti di un cliente. Se la preferenza di usare anche
     * conducenti non abbinati e' stata impostata, qui compaiono anche
     * conducenti senza cliente.
     * @param sx La session da usare per la query.
     * @param aCliente Il cliente per il quale cercare conducenti.
     * @throws org.hibernate.HibernateException Se qualcosa va storto col db...
     * @return La lista di conducenti come ritornata dalla query, o null se il
     * cliente e' null.
     */
    public static List leggiConducenti(Session sx, MROldClienti aCliente) throws HibernateException {
        List listaConducenti = null;
        if (aCliente != null) {
            listaConducenti =
                    sx.createQuery("select c from MROldConducenti c where c.cliente = :cliente"). //NOI18N
                    setParameter("cliente", aCliente). //NOI18N
                    list();
        }
        return listaConducenti;
    }

    /**
     *
     * @param sx
     * @param gruppo
     * @param sede
     * @param inizio
     * @param fine
     * @return
     */
    public static MROldPrenotazione generaPrenotazione(Session sx, MROldGruppo gruppo, MROldSede sede, Date inizio, Date fine) {
        MROldNumerazione numerazione = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRENOTAZIONI);

        /* ROB: uniformita' con le voci di menu del MyRentFrame */
        Date data = FormattedDate.formattedTimestamp();
        //Date data = FormattedDate.formattedDate();

        Integer anno = FormattedDate.annoCorrente();
        Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
        String prefisso = numerazione.getPrefisso();

        User aUser = (User) Parameters.getUser();
        MROldAffiliato affiliato = aUser.getAffiliato();

        if (sede == null && aUser.getSedeOperativa() != null) {
            sede =  aUser.getSedeOperativa();
        }

        return new MROldPrenotazione(
                numerazione,
                prefisso,
                numero,
                data,
                anno,
                affiliato,
                gruppo,
                sede,
                sede,
                inizio,
                fine,
                aUser);
    }
}
