/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.api.utils;

import it.aessepi.utils.MathUtils;
import it.myrent.ee.db.*;
import it.myrent.ee.db.MROldClienti;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author giacomo
 */
public class ContabUtils {

    public static List<MROldPartita> leggiPartiteAperteFattureAcconto(Session sx, MROldPrenotazione prenotazione) {
        return (List<MROldPartita>) sx.createQuery("select p from it.myrent.ee.db.MROldPartita p left join p.fattura where p.prenotazione = :prenotazione and p.fattura.class = it.myrent.ee.db.MROldFatturaAcconto and p.partitaSaldo is null").
                setParameter("prenotazione", prenotazione).
                list();
    }

    public static List<MROldPartita> leggiPartiteAperteFattureAcconto(Session sx, MROldContrattoNoleggio contratto) {

        return (List<MROldPartita>) sx.createQuery("select p from it.myrent.ee.db.MROldPartita p left join p.fattura where p.contratto = :contratto and p.fattura.class = it.myrent.ee.db.MROldFatturaAcconto and p.partitaSaldo is null").
                setParameter("contratto", contratto).
                list();
    }

    public static List<MROldPartita> leggiPartiteAcconto(Session sx, MROldPartita partitaSaldo) {

        return (List<MROldPartita>) sx.createQuery("select p from MROldPartita p where p.partitaSaldo = :partitaSaldo").
                setParameter("partitaSaldo", partitaSaldo).
                list();
    }

    /**
     * Legge tutti i movimenti di incasso di un contratto non associati ad una partita.
     * @param sx La sessione al database
     * @param prenotazione Il contratto di noleggio
     * @return Una lista contenente le primenote trovate.
     */
    public static List<MROldPrimanota> leggiContropartiteAperte(Session sx, MROldPrenotazione prenotazione) {
        return (List<MROldPrimanota>) sx.createFilter(prenotazione.getPrimenote(), "where this.partita is null and this.causale.codice in (:codiciIncasso)").
                setParameterList("codiciIncasso", new Object[]{
                    MROldCausalePrimanota.INCASSO_CLIENTE,
                    MROldCausalePrimanota.INCASSO_CLIENTE_CARTA_DI_CREDITO}).
                list();
    }

    public static List<MROldPrimanota> leggiContropartiteAperte(Session sx, MROldContrattoNoleggio contratto) {

        return (List<MROldPrimanota>) sx.createFilter(contratto.getPrimenote(), "where this.partita is null and this.causale.codice in (:codiciIncasso)").
                setParameterList("codiciIncasso", new Object[]{
                        MROldCausalePrimanota.INCASSO_CLIENTE,
                        MROldCausalePrimanota.INCASSO_CLIENTE_CARTA_DI_CREDITO}).
                list();
    }

    public static MROldPianoDeiConti leggiSottoconto(Session sx, Integer mastro, Integer conto, Integer sottoconto) throws HibernateException {
        MROldPianoDeiConti retValue = null;
        Query querySottoconto = sx.createQuery(
                "select s from it.myrent.ee.db.MROldSottoconto s where " + //NOI18N
                "s.codiceMastro = :mastro and " + // NOI18N
                "s.codiceConto = :conto and " + // NOI18N
                "s.codiceSottoconto = :sottoconto"); // NOI18N
        querySottoconto.setParameter("mastro", mastro); //NOI18N
        querySottoconto.setParameter("conto", conto); //NOI18N
        querySottoconto.setParameter("sottoconto", sottoconto); //NOI18N
        retValue = (MROldPianoDeiConti) querySottoconto.uniqueResult();
        return retValue;
    }

    public static MROldCodiciIva leggiCodiceIva(Session mySession, Integer id) throws HibernateException {
        MROldCodiciIva retValue = null;
        Query queryCodiceIva = mySession.createQuery("select c from it.myrent.ee.db.MROldCodiciIva c where c.id = :id"); // NOI18N
        queryCodiceIva.setParameter("id", id); // NOI18N
        retValue = (MROldCodiciIva) queryCodiceIva.uniqueResult();
        return retValue;
    }

    public static MROldCausalePrimanota leggiCausale(Session sx, Integer codice) throws HibernateException {
        MROldCausalePrimanota retValue = null;
        Query queryCausale = sx.createQuery("select c from it.myrent.ee.db.MROldCausalePrimanota c where c.codice = :codice"); // NOI18N
        queryCausale.setParameter("codice", codice); // NOI18N
        retValue = (MROldCausalePrimanota) queryCausale.uniqueResult();
        return retValue;
    }

    /**
     * Legge un sottoconto usando i codici mastro, conto, sottoconto. Non usa l'ID.
     * @param sx La sessione al database.
     * @param sottoconto il sottoconto dal quale prelevare i codici.
     * @return Il sottoconto letto dal database.
     */
    public static MROldPianoDeiConti leggiSottoconto(Session sx, MROldPianoDeiConti sottoconto) {
        return leggiSottoconto(sx, sottoconto.getCodiceMastro(), sottoconto.getCodiceConto(), sottoconto.getCodiceSottoconto());
    }

    public static boolean isRigaDescrittiva(MROldRigaDocumentoFiscale riga) {
        boolean rigaDescrittiva =
                (riga.getDescrizione() != null
                && riga.getQuantita() == null
                && riga.getPrezzoUnitario() == null
                && riga.getSconto() == null
                && riga.getScontoFisso() == null
                && riga.getCodiceIva() == null
                && riga.getCodiceSottoconto() == null);
        return rigaDescrittiva;
    }

    public static List leggiConti(Session sx, Integer mastro1, Integer mastro2) {
        return sx.createQuery("select p from it.myrent.ee.db.MROldPianoDeiConti p where (p.codiceMastro =:mastro1 or p.codiceMastro =:mastro2) and p.codiceSottoconto !='0' order by p.pattern").
                setParameter("mastro1", mastro1).
                setParameter("mastro2", mastro2).
                list();
    }

    public static List leggiClientiMovimentati(Session sx, Integer anno) {
        return sx.createQuery(
                "select distinct r.cliente from it.myrent.ee.db.MROldRigaPrimanota r " + // NOI18N
                "where r.primanota.annoCompetenza = :anno"). // NOI18N
                setParameter("anno", anno). // NOI18N
                list();
    }

    public static List leggiFornitoriMovimentati(Session sx, Integer anno) {
        return sx.createQuery(
                "select distinct r.fornitore from it.myrent.ee.db.MROldRigaPrimanota r "
                + "where r.primanota.annoCompetenza = :anno").
                setParameter("anno", anno).
                list();
    }

    public static Double leggiSumRighePrimanoteDaChiudere(Session sx, Integer anno, MROldPianoDeiConti conto, Boolean segno, MROldClienti cliente, MROldFornitori fornitore) {
        Query query = null;
        if (cliente == null && fornitore == null) {
            query = sx.createQuery("select sum(round(Riga.importo * 100.0)) / 100.0 from RigaPrimanota as Riga "
                    + " where Riga.primanota.annoCompetenza = :anno and "
                    + " Riga.segno = :segnoDare and Riga.conto = :conto");
        } else if (cliente != null) {
            query = sx.createQuery("select sum(round(Riga.importo * 100.0)) / 100.0 from RigaPrimanota as Riga "
                    + " where Riga.primanota.annoCompetenza = :anno and "
                    + " Riga.segno = :segnoDare and Riga.conto = :conto and Riga.cliente =:cliente");
            query.setParameter("cliente", cliente);
        } else if (fornitore != null) {
            query = sx.createQuery("select sum(round(Riga.importo * 100.0)) / 100.0 from RigaPrimanota as Riga "
                    + " where Riga.primanota.annoCompetenza = :anno and "
                    + " Riga.segno = :segnoDare and Riga.conto = :conto and Riga.fornitore =:fornitore");
            query.setParameter("fornitore", fornitore);
        }
        query.setParameter("conto", conto);
        query.setParameter("segnoDare", segno);
        query.setParameter("anno", anno);

        return (Double) query.uniqueResult();
    }

    public static List leggiSumRighePrimanoteDaChiudere(Session sx, Integer anno, Boolean segno, List clienti, List fornitori, List conti) {
        StringBuffer queryText = new StringBuffer();
        queryText.append(
                "select sum(round(Riga.importo * 100.0)) / 100.0 , "
                + "Riga.cliente.id, Riga.fornitore.id, Riga.conto.id "
                + "from RigaPrimanota as Riga "
                + "where Riga.primanota.annoCompetenza = :anno "
                + "and Riga.segno = :segno ");

        queryText.append("group by Riga.cliente.id, Riga.fornitore.id, Riga.conto.id");

        Query query = sx.createQuery(queryText.toString());
        query.setParameter("segno", segno);
        query.setParameter("anno", anno);

        return query.list();

    }

    public static boolean isContoClienti(MROldPianoDeiConti conto) {
        return conto != null
                && conto.getCodiceMastro().equals(conto.MASTRO_ATTIVITA)
                && conto.getCodiceConto().equals(conto.CONTO_CLIENTI);
    }

    public static boolean isContoFornitori(MROldPianoDeiConti conto) {
        return conto != null
                && conto.getCodiceMastro().equals(conto.MASTRO_PASSIVITA)
                && conto.getCodiceConto().equals(conto.CONTO_FORNITORI);
    }

    /**
     * Calcola il totale dei documenti fiscali di saldo emessi per questo contratto,
     * sommando le note credito con segno negativo. Non vengono quindi incluse le
     * fatture di acconto.
     * @param sx La sessione al database
     * @param contratto Il contratto per il quale si deve fare il calcolo
     * @return L'importo totale calcolato, arrotondato a 2 decimali.
     */
    public static Double totaleFattureSaldo(Session sx, MROldContrattoNoleggio contratto, MROldPrenotazione prenotazione) {

        String queryString = null;
        if (contratto != null && prenotazione != null) {
            queryString = " (d.contratto = :contratto or d.prenotazione = :prenotazione) and ";
        } else if (contratto != null && prenotazione == null) {
            queryString = " d.contratto = :contratto and ";
        } else if (contratto == null && prenotazione != null) {
            queryString = " d.prenotazione = :prenotazione and ";
        }


        Query query = sx.createQuery("select sum(d.totaleFattura * (CASE WHEN d.class = it.myrent.ee.db.MROldNotaCredito then (-1) else 1 END)) "
                + "from it.myrent.ee.db.MROldDocumentoFiscale d "
                + "where d.class <> it.myrent.ee.db.MROldFatturaAcconto "
                + "and "
                + queryString
                + "d.id is not null "
                + "and d.prepagato = :prepagato");

        query.setParameter("prepagato", false);

        if (contratto != null) {
            query.setParameter("contratto", contratto);
        }

        if (prenotazione != null) {
            query.setParameter("prenotazione", prenotazione);
        }
        Number saldoFatture = (Number) query.uniqueResult();

        return saldoFatture != null ? MathUtils.round(saldoFatture.doubleValue()) : 0.0;
    }

     public static MROldPartita leggiPartita(Session sx, MROldDocumentoFiscale fattura) {
        MROldPartita p = null;
        try {
             p = (MROldPartita) sx.createQuery("select p from it.myrent.ee.db.MROldPartita p where p.fattura = :fattura").
                setParameter("fattura", fattura).
                setMaxResults(1).
                uniqueResult();
         } catch (Exception e) {
             e.printStackTrace();
         }

        return p;
    }

     /**
     * Calcola il totale dei documenti fiscali di acconto emessi per questo contratto.
     * @param sx La sessione al database
     * @param contratto Il contratto per il quale si deve fare il calcolo
     * @return L'importo totale calcolato, arrotondato a 2 decimali.
     */
    public static Double totaleFattureAcconto(Session sx, MROldContrattoNoleggio contratto, MROldPrenotazione prenotazione) {
        Query query = sx.createQuery("select sum(d.totaleFattura) "
                + "from it.myrent.ee.db.MROldFatturaAcconto d "
                + "where d.id is not null "
                + (contratto != null?"and d.contratto = :contratto ":"")
                + (prenotazione != null ? "and d.prenotazione = :prenotazione " : "")
                + "and d.prepagato = :prepagato");

        query.setParameter("prepagato", false);

        if (contratto != null) {
            query.setParameter("contratto", contratto);
        }

        if (prenotazione != null) {
            query.setParameter("prenotazione", prenotazione);
        }

        Number saldoFatture = (Number) query.uniqueResult();

        return saldoFatture != null ? MathUtils.round(saldoFatture.doubleValue()) : 0.0;
    }

    public static boolean isRigaCliente(MROldRigaCausale riga) {
        return riga != null
                && riga.getConto() != null
                && riga.getConto().getCodiceMastro().equals(MROldPianoDeiConti.MASTRO_ATTIVITA)
                && riga.getConto().getCodiceConto().equals(MROldPianoDeiConti.CONTO_CLIENTI);
    }

}
