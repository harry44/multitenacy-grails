/*
 * ContabUtils.java
 *
 * Created on 31 mai 2006, 08:50
 *
 */
package it.aessepi.myrentcs.utils;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.beans.QueryData;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.db.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

/**
 * Classe contenente metodi statici utili per la contabilita.
 * @author jamess
 */
public class ContabUtils {

    /**
     * Questa classe non puo' essere istanziata.
     */
    private ContabUtils() {
    }
    private static final Log log = LogFactory.getLog(ContabUtils.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");

    /**
     * Verifica se una riga ha un conto IVA.<br>
     *@param <code>riga</code> Riga da verificare.
     *@return <code>true</code> se la riga non e' null ed ha un conto IVA, <code>false</code> altrimenti.
     */
    public static boolean isRigaIva(MROldRigaCausale riga) {
        return riga != null
                && riga.getConto() != null
                && riga.getConto().getCodiceMastro().equals(MROldPianoDeiConti.MASTRO_PASSIVITA)
                && riga.getConto().getCodiceConto().equals(MROldPianoDeiConti.CONTO_IVA);
    }

    public static boolean isRigaCliente(MROldRigaCausale riga) {
        return riga != null
                && riga.getConto() != null
                && riga.getConto().getCodiceMastro().equals(MROldPianoDeiConti.MASTRO_ATTIVITA)
                && riga.getConto().getCodiceConto().equals(MROldPianoDeiConti.CONTO_CLIENTI);
    }

    public static boolean isRigaFornitore(MROldRigaCausale riga) {
        return riga != null
                && riga.getConto() != null
                && riga.getConto().getCodiceMastro().equals(MROldPianoDeiConti.MASTRO_PASSIVITA)
                && riga.getConto().getCodiceConto().equals(MROldPianoDeiConti.CONTO_FORNITORI);
    }

    public static boolean isRigaDareAvere(MROldRigaCausale riga) {
        return riga != null && riga.getSegno() == null;
    }

    public static boolean isContoCassaCauzioni(MROldPianoDeiConti conto) {
        return conto != null
                && conto.getCodiceMastro().equals(conto.MASTRO_ATTIVITA)
                && conto.getCodiceConto().equals(conto.CONTO_CASSA)
                && conto.getCodiceSottoconto().equals(conto.SOTTOCONTO_CASSA_CAUZIONI);
    }

    public static boolean isContoCassa(MROldPianoDeiConti conto) {
        return conto != null
                && conto.getCodiceMastro().equals(conto.MASTRO_ATTIVITA)
                && conto.getCodiceConto().equals(conto.CONTO_CASSA);
    }

    public static boolean isContoBanca(MROldPianoDeiConti conto) {
        return conto != null
                && conto.getCodiceMastro().equals(conto.MASTRO_ATTIVITA)
                && conto.getCodiceConto().equals(conto.CONTO_BANCA);
    }

    public static boolean isContoIva(MROldPianoDeiConti conto) {
        return conto != null
                && conto.getCodiceMastro().equals(conto.MASTRO_PASSIVITA)
                && conto.getCodiceConto().equals(conto.CONTO_IVA);
    }

    public static boolean isContoIvaAcquisti(MROldPianoDeiConti conto) {
        return conto != null
                && conto.getCodiceMastro().equals(conto.MASTRO_PASSIVITA)
                && conto.getCodiceConto().equals(conto.CONTO_IVA)
                && conto.getCodiceSottoconto().equals(conto.SOTTOCONTO_IVA_ACQUISTI);
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

    public static boolean isContoSpesa(MROldPianoDeiConti conto) {
        return conto != null
                && conto.getCodiceMastro().equals(conto.MASTRO_COSTI);
    }

    public static boolean isRigaIvaAcquisti(MROldRigaCausale riga) {
        return riga != null
                && isContoIvaAcquisti(riga.getConto());
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

    public static MROldCausalePrimanota leggiCausale(Integer codice,Session mySession) {

        MROldCausalePrimanota retValue = null;
        try {
            retValue = leggiCausale(mySession, codice);

        } catch (Exception ex) {

            log.error("ContabUtils: Exception encountered while reading causale with code: " + codice + ".", ex); //NOI18N
        }
        return retValue;
    }

    public static MROldCausalePrimanota leggiCausale(Session sx, Integer codice) throws HibernateException {
        MROldCausalePrimanota retValue = null;
        Query queryCausale = sx.createQuery("select c from MROldCausalePrimanota c where c.codice = :codice"); // NOI18N
        queryCausale.setParameter("codice", codice); // NOI18N
        retValue = (MROldCausalePrimanota) queryCausale.uniqueResult();
        return retValue;
    }

    public static MROldCodiciIva leggiCodiceIva(Integer id, Session mySession) {
        MROldCodiciIva retValue = null;
        try {
            retValue = leggiCodiceIva(mySession, id);
            mySession.close();
        } catch (Exception ex) {
            log.error("ContabUtils: Exception encountered while reading codice iva with id: " + id + ".", ex); //NOI18N
        }
        return retValue;
    }

    public static MROldCodiciIva leggiCodiceIva(Session mySession, Integer id) throws HibernateException {
        MROldCodiciIva retValue = null;
        Query queryCodiceIva = mySession.createQuery("select c from MROldCodiciIva c where c.id = :id"); // NOI18N
        queryCodiceIva.setParameter("id", id); // NOI18N
        retValue = (MROldCodiciIva) queryCodiceIva.uniqueResult();
        return retValue;
    }

    private static MROldPianoDeiConti leggiSottocontoAperturaGiornaliera(Session sx) {
        return leggiSottoconto(2, 50, 6,sx);
    }

    private static MROldPianoDeiConti leggiSottocontoChiusuraGiornaliera(Session sx) {
        return leggiSottoconto(2, 50, 7,sx);
    }

    public static List leggiSottocontiDelConto(Integer mastro, Integer conto,Session mySession) {
        List retValue = null;
        try {

            retValue = leggiSottocontiDelConto(mySession, mastro, conto);
            mySession.close();
        } catch (Exception ex) {
            log.error("ContabUtils: Exception encountered while reading sottoconti with codes: " + mastro + ", " + conto + ".", ex); //NOI18N
        }
        return retValue;
    }

    public static List leggiSottocontiDelConto(Session mySession, Integer mastro, Integer conto) throws HibernateException {
        Query query = mySession.createQuery(
                "select c from MROldSottoconto c where " + //NOI18N
                "c.codiceMastro = :codiceMastro and " + //NOI18N
                "c.codiceConto = :codiceConto order by c.pattern"); //NOI18N
        query.setParameter("codiceMastro", mastro); //NOI18N
        query.setParameter("codiceConto", conto); //NOI18N
        return query.list();
    }

    public static List leggiSottocontiDelMastro(Integer mastro,Session mySession) {
        List retValue = null;
        try {
            retValue = leggiSottocontiDelMastro(mySession, mastro);
            mySession.close();
        } catch (Exception ex) {
            log.error("ContabUtils: Exception encountered while reading sottoconti with mastro: " + mastro + ".", ex); // NOI18N
        }
        return retValue;
    }

    public static List leggiSottocontiDelMastro(Session mySession, Integer mastro) {
        Query query = mySession.createQuery(
                "select c from MROldSottoconto c where " + //NOI18N
                "c.codiceMastro = :codiceMastro order by c.pattern"); // NOI18N
        query.setParameter("codiceMastro", mastro); // NOI18N
        return query.list();
    }

    /**
     * Legge un conto usando i codici mastro e conto. Non usa l'ID.
     * @param sx La sessione al database.
     * @param conto il conto dal quale prelevare i codici.
     * @return Il conto letto dal database.
     */
    public static MROldPianoDeiConti leggiConto(Session sx, MROldPianoDeiConti conto) {
        return leggiConto(sx, conto.getCodiceMastro(), conto.getCodiceConto());
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

    public static MROldPianoDeiConti leggiSottoconto(Integer mastro, Integer conto, Integer sottoconto,Session mySession) {

        MROldPianoDeiConti retValue = null;
        try {
            retValue = leggiSottoconto(mySession, mastro, conto, sottoconto);
            mySession.close();
        } catch (Exception ex) {
            log.error("ContabUtils: Exception encountered while reading account with codes: " + mastro + ", " + conto + ", " + sottoconto + ".", ex); //NOI18N
        }
        return retValue;
    }

    public static MROldPianoDeiConti leggiSottoconto(Session sx, Integer mastro, Integer conto, Integer sottoconto) throws HibernateException {
        MROldPianoDeiConti retValue = null;
        Query querySottoconto = sx.createQuery(
                "select s from MROldSottoconto s where " + //NOI18N
                "s.codiceMastro = :mastro and " + // NOI18N
                "s.codiceConto = :conto and " + // NOI18N
                "s.codiceSottoconto = :sottoconto"); // NOI18N
        querySottoconto.setParameter("mastro", mastro); //NOI18N
        querySottoconto.setParameter("conto", conto); //NOI18N
        querySottoconto.setParameter("sottoconto", sottoconto); //NOI18N
        retValue = (MROldPianoDeiConti) querySottoconto.uniqueResult();
        return retValue;
    }

    public static MROldPianoDeiConti leggiMastro(Session sx, Integer mastro) {
        return (MROldPianoDeiConti) sx.createQuery(
                "select m from Mastro m "
                + "where m.codiceMastro = :mastro").
                setParameter("mastro", mastro).
                uniqueResult();
    }

    public static MROldPianoDeiConti leggiConto(Session sx, Integer mastro, Integer conto) {

        return (MROldPianoDeiConti) sx.createQuery(
                "select c from Conto c "
                + "where c.codiceMastro = :mastro "
                + "and c.codiceConto = :conto").
                setParameter("mastro", mastro).
                setParameter("conto", conto).
                uniqueResult();
    }

    public static List leggiConti(Session sx, Integer mastro1, Integer mastro2) {
        
        return sx.createQuery("select p from MROldPianoDeiConti p where (p.codiceMastro =:mastro1 or p.codiceMastro =:mastro2) and p.codiceSottoconto !='0' order by p.pattern").
                setParameter("mastro1", mastro1).
                setParameter("mastro2", mastro2).
                list();
    }

    public static List leggiClientiMovimentati(Session sx, Integer anno) {
        return sx.createQuery(
                "select distinct r.cliente from RigaPrimanota r " + // NOI18N
                "where r.primanota.annoCompetenza = :anno"). // NOI18N
                setParameter("anno", anno). // NOI18N
                list();
    }

    public static List leggiFornitoriMovimentati(Session sx, Integer anno) {
     return sx.createQuery(
                "select distinct r.fornitore from RigaPrimanota r "
                + "where r.primanota.annoCompetenza = :anno").
                setParameter("anno", anno).
                list();
    }

    public static String numerazioneRegistro(MROldCausalePrimanota causale) {
        switch (causale.getRegistroIva().getId().intValue()) {
            case 1:
                return MROldNumerazione.ACQUISTI;
            case 2:
                if (causale.getSegnoIva().equals(causale.SEGNO_IVA_POSITIVO)) {
                    return MROldNumerazione.VENDITE;
                } else {
                    return MROldNumerazione.NOTE_CREDITO_VENDITA;
                }
            case 3:
                return MROldNumerazione.CORRISPETTIVI;
            case 4:
                return MROldNumerazione.ACQUISTI_MARGINE;
            case 5:
                return MROldNumerazione.VENDITE_MARGINE;
        }
        return null;
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

    public static Integer nuovoCodiceCausale(Integer minimo,Session mySession) {
        Integer nuovoCodice = null;
        try {
            nuovoCodice = nuovoCodiceCausale(mySession, minimo);
            mySession.close();
        } catch (Exception ex) {
            log.error("Exception encountered while looking for nuovo codice causale. Message is: " + ex.getMessage()); //NOI18N
            log.debug("Exception encountered while looking for nuovo codice causale.", ex); //NOI18N
        }
        return nuovoCodice;
    }

    public static Integer nuovoCodiceCausale(Session mySession, Integer minimo) throws HibernateException {
        Integer nuovoCodice = null;
        Query queryCodice = mySession.createQuery(
                "select c.codice from MROldCausalePrimanota as c where " + //NOI18N
                "c.codice > :minimo order by c.codice"); //NOI18N
        queryCodice.setParameter("minimo", minimo); //NOI18N
        Iterator it = queryCodice.iterate();
        Integer ultimoCodice = minimo;
        while (it.hasNext() && nuovoCodice == null) {
            Integer codice = (Integer) it.next();
            if (codice.intValue() - ultimoCodice.intValue() > 1) {
                nuovoCodice = new Integer(ultimoCodice.intValue() + 1);
            } else {
                ultimoCodice = codice;
            }
        }
        if (nuovoCodice == null) {
            nuovoCodice = new Integer(ultimoCodice.intValue() + 1);
        }
        return nuovoCodice;
    }

    /**
     * Questo metodo legge le primenote di incasso e crea una scheda contabile del cliente.
     * @return Un array contenente i seguenti valori: dare e avere saldo cliente, dare e avere saldo cassa cauzioni, dare e avere saldo cassa incassi.
     */
    public static double[] creaSchedaContabileCliente(Set aPrimenote) {
        double dareCliente = 0.0, avereCliente = 0.0, dareCauzione = 0.0, avereCauzione = 0.0, dareIncasso = 0.0, avereIncasso = 0.0;
        Iterator primenote = aPrimenote.iterator();
        while (primenote.hasNext()) {
            MROldPrimanota primanota = (MROldPrimanota) primenote.next();
            Iterator righe = primanota.getRighePrimanota().iterator();
            while (righe.hasNext()) {
                RigaPrimanota riga = (RigaPrimanota) righe.next();
                if (riga.getCliente() != null) { // Riga del cliente
                    if (riga.getSegno().equals(MROldCausalePrimanota.DARE)) {
                        dareCliente += riga.getImporto().doubleValue();
                    } else {
                        avereCliente += riga.getImporto().doubleValue();
                    }
                } else {
                    if (riga.getConto().getCodiceSottoconto().equals(new Integer(3))) {
                        // Cassa cauzioni
                        if (riga.getSegno().equals(MROldCausalePrimanota.DARE)) {
                            dareCauzione += riga.getImporto().doubleValue();
                        } else {
                            avereCauzione += riga.getImporto().doubleValue();
                        }
                    } else {
                        // Conto incasso
                        if (riga.getSegno().equals(MROldCausalePrimanota.DARE)) {
                            dareIncasso += riga.getImporto().doubleValue();
                        } else {
                            avereIncasso += riga.getImporto().doubleValue();
                        }
                    }
                }
            }
        }
        return new double[]{dareCliente, avereCliente, dareCauzione, avereCauzione, dareIncasso, avereIncasso};
    }

    public static MROldPrimanota leggiPrimanota(Session sx, Integer causalePrimanota, Integer anno) {
        return (MROldPrimanota) sx.createQuery(
                "select p from MROldPrimanota p "
                + "where p.annoCompetenza = :anno "
                + "and p.causale.codice = :causale ").
                setParameter("anno", anno).
                setParameter("causale", causalePrimanota).
                uniqueResult();
    }

    public static MROldPrimanota leggiPrimanotaCassa(Session sx, MROldSede sede) {
        return (MROldPrimanota) sx.createQuery(
                "select p from MROldPrimanota p "
                + "where p.causale.codice in (:causali) "
                + "and p.sede = :sede "
                + "order by p.dataRegistrazione desc, p.numeroRegistrazione desc").
                setParameterList("causali", new Object[]{
                        MROldCausalePrimanota.APERTURA_CASSA,
                        MROldCausalePrimanota.CHIUSURA_CASSA}).
                setParameter("sede", sede).
                setMaxResults(1).
                uniqueResult();
    }

    public static MROldPrimanota leggiPrimanotaCassa(Session sx, MROldSede sede, Date data) {
        return (MROldPrimanota) sx.createQuery(
                "select p from MROldPrimanota p "
                + "where p.causale.codice in (:causali) "
                + "and p.sede = :sede "
                + "and p.dataRegistrazione <= :data "
                + "order by p.dataRegistrazione desc, p.numeroRegistrazione desc").
                setParameterList("causali", new Object[]{
                        MROldCausalePrimanota.APERTURA_CASSA,
                        MROldCausalePrimanota.CHIUSURA_CASSA}).
                setParameter("sede", sede).
                setMaxResults(1).
                uniqueResult();
    }

    public static boolean isCassaChiusa(Session sx, MROldSede aSede) {
        MROldPrimanota primanotaCassa = leggiPrimanotaCassa(sx, aSede);
        if (primanotaCassa != null && primanotaCassa.getCausale().getCodice().equals(MROldCausalePrimanota.CHIUSURA_CASSA)) {
            return true;
        }
        return false;
    }

    

    public static Double leggiDifferenzaAverDareDiSottoconto(MROldPrimanota primanota, MROldPianoDeiConti sottoconto) {
        if (primanota == null || primanota.getRighePrimanota() == null || primanota.getRighePrimanota().size() == 0) {
            return new Double(0);
        }

        double sommaDareSottoconto = 0;
        double sommaAverSottoconto = 0;

        for (int i = 0; i < primanota.getRighePrimanota().size(); i++) {
            RigaPrimanota tmpRiga = (RigaPrimanota) primanota.getRighePrimanota().get(i);
            if (tmpRiga != null && tmpRiga.getConto().equals(sottoconto)) {
                if (tmpRiga.getSegno().booleanValue()) {
                    sommaDareSottoconto = sommaDareSottoconto
                            + tmpRiga.getImporto().doubleValue();

                } else {
                    sommaAverSottoconto = sommaAverSottoconto
                            + tmpRiga.getImporto().doubleValue();
                }
            }
        }

        return new Double(sommaAverSottoconto - sommaDareSottoconto);
    }

    public static MROldCausalePrimanota duplicaCausale(MROldCausalePrimanota causale, Integer codice, String descrizione, String registro) {
        MROldCausalePrimanota copiaCausale = new MROldCausalePrimanota();
        copiaCausale.setCausaleEsterna(causale.getCausaleEsterna());
        copiaCausale.setCodice(codice);
        copiaCausale.setDataDocRequired(causale.getDataDocRequired());
        copiaCausale.setDescrizione(descrizione);
        copiaCausale.setGestioneIvaIndetraibile(causale.getGestioneIvaIndetraibile());
        copiaCausale.setIva(causale.getIva());
        copiaCausale.setLiquidazioneIva(causale.getLiquidazioneIva());
        copiaCausale.setNumDocRequired(causale.getNumDocRequired());
        copiaCausale.setRegistroIva(duplicaRegistroIva(causale.getRegistroIva(), registro));
        copiaCausale.setSegnoIva(causale.getSegnoIva());

        copiaCausale.setRigheCausale(new ArrayList());
        Iterator righe = causale.getRigheCausale().iterator();
        while (righe.hasNext()) {
            MROldRigaCausale riga = (MROldRigaCausale) righe.next();
            copiaCausale.getRigheCausale().add(duplicaRigaCausale(riga));
        }
        return copiaCausale;
    }

    public static MROldRigaCausale duplicaRigaCausale(MROldRigaCausale riga) {
        MROldRigaCausale copiaRiga = new MROldRigaCausale();
        copiaRiga.setAutoCarica(riga.getAutoCarica());
        copiaRiga.setConto(riga.getConto());
        copiaRiga.setDataScadenza(riga.getDataScadenza());
        copiaRiga.setKmVeicolo(riga.getKmVeicolo());
        copiaRiga.setNumeroRiga(riga.getNumeroRiga());
        copiaRiga.setPagamento(riga.getPagamento());
        copiaRiga.setVeicolo(riga.getVeicolo());
        return copiaRiga;
    }

    public static MROldRegistroIva duplicaRegistroIva(MROldRegistroIva registroIva, String descrizione) {
        if (registroIva != null) {
            MROldRegistroIva copiaRegistroIva = new MROldRegistroIva();
            copiaRegistroIva.setCalcoloIva(registroIva.getCalcoloIva());
            copiaRegistroIva.setContoIva(registroIva.getContoIva());
            copiaRegistroIva.setDescrizione(descrizione);
            copiaRegistroIva.setSegnoLiquidazioneIva(registroIva.getSegnoLiquidazioneIva());
            return copiaRegistroIva;
        } else {
            return null;
        }
    }

    public static List leggiPagamentiNoleggio(Session sx, User aUser) {
        boolean enablePaymentFilter = Preferenze.getEnablePaymenyForAreaManager(sx);
        if (enablePaymentFilter && (aUser.isOperatore() || aUser.isCapoStazione())) {
            List filteredList = new ArrayList();
            Set userPayment=new HashSet();
            userPayment=aUser.getPayment();
            Iterator itr=userPayment.iterator();
            while(itr.hasNext()){
                MROldPagamento p=(MROldPagamento)itr.next();
                if((!p.getDifferito()) && (!p.getPrepagato())){
                    filteredList.add(p);
                }
            }
            String contrattoFormaPagamentoDefaultString = Preferenze.getSettingValue(sx, Preferenze.PROP_CONTRATTO_FORMA_PAGAMENTO_DEFAULT);
            Integer contrattoFormaPagamentoDefaultId = null;
            MROldPagamento contrattoFormPagamentoDefault = null;
            try {
                contrattoFormaPagamentoDefaultId = new Integer(contrattoFormaPagamentoDefaultString);
            } catch (Exception e) {
            /* not a number */
                contrattoFormaPagamentoDefaultId = null;
            }
            if (contrattoFormaPagamentoDefaultId != null) {
                contrattoFormPagamentoDefault = (MROldPagamento) sx.get(MROldPagamento.class, contrattoFormaPagamentoDefaultId);
                if(contrattoFormPagamentoDefault!=null){
                    filteredList.add(contrattoFormPagamentoDefault);
                }
            }

            return filteredList;
        } else {
            List filteredList = new ArrayList();
            filteredList.addAll(sx.createQuery(
                    "select p from MROldPagamento p " + // NOI18N
                            "where p.differito = :differito " + // NOI18N
                            "and p.prepagato = :prepagato " + // NOI18N
                            "order by p.descrizione"). //NOI18N
                    setParameter("differito", Boolean.FALSE). //NOI18N
                    setParameter("prepagato", Boolean.FALSE). // NOI18N
                    list()); //NOI18N
            String contrattoFormaPagamentoDefaultString = Preferenze.getSettingValue(sx, Preferenze.PROP_CONTRATTO_FORMA_PAGAMENTO_DEFAULT);
            Integer contrattoFormaPagamentoDefaultId = null;
            MROldPagamento contrattoFormPagamentoDefault = null;
            try {
                contrattoFormaPagamentoDefaultId = new Integer(contrattoFormaPagamentoDefaultString);
            } catch (Exception e) {
            /* not a number */
                contrattoFormaPagamentoDefaultId = null;
            }
            if (contrattoFormaPagamentoDefaultId != null) {
                contrattoFormPagamentoDefault = (MROldPagamento) sx.get(MROldPagamento.class, contrattoFormaPagamentoDefaultId);
                if(contrattoFormPagamentoDefault!=null){
                    filteredList.add(contrattoFormPagamentoDefault);
                }
            }
            return filteredList;

        }


    }

    public static List leggiPagamentiCauzione(Session sx, User aUser) {
        boolean enablePaymentFilter = Preferenze.getEnablePaymenyForAreaManager(sx);
        if (enablePaymentFilter && (aUser.isOperatore() || aUser.isCapoStazione())) {
            List filteredList = new ArrayList();
            Set userPayment=new HashSet();
            userPayment=aUser.getPayment();
            Iterator itr=userPayment.iterator();
            while(itr.hasNext()){
                MROldPagamento p=(MROldPagamento)itr.next();
                if((p.getContanti())&&(!p.getDifferito()) && (!p.getPrepagato())){
                    filteredList.add(p);
                }
            }
            String contrattoFormaPagamentoDefaultString = Preferenze.getSettingValue(sx, Preferenze.PROP_CONTRATTO_FORMA_PAGAMENTO_DEFAULT);
            Integer contrattoFormaPagamentoDefaultId = null;
            MROldPagamento contrattoFormPagamentoDefault = null;
            try {
                contrattoFormaPagamentoDefaultId = new Integer(contrattoFormaPagamentoDefaultString);
            } catch (Exception e) {
            /* not a number */
                contrattoFormaPagamentoDefaultId = null;
            }
            if (contrattoFormaPagamentoDefaultId != null) {
                contrattoFormPagamentoDefault = (MROldPagamento) sx.get(MROldPagamento.class, contrattoFormaPagamentoDefaultId);
                if(contrattoFormPagamentoDefault!=null){
                    filteredList.add(contrattoFormPagamentoDefault);
                }
            }
            return filteredList;
        } else {
            List filteredList = new ArrayList();

            filteredList.addAll(sx.createQuery(
                    "select p from MROldPagamento p "
                            + "where p.contanti = :contanti "
                            + "and p.differito = :differito "
                            + "and p.prepagato = :prepagato "
                            + "order by p.descrizione"). //NOI18N
                    setParameter("contanti", Boolean.TRUE). //NOI18N
                    setParameter("differito", Boolean.FALSE). //NOI18N
                    setParameter("prepagato", Boolean.FALSE). //NOI18N
                    list());
            String contrattoFormaPagamentoDefaultString = Preferenze.getSettingValue(sx, Preferenze.PROP_CONTRATTO_FORMA_PAGAMENTO_DEFAULT);
            Integer contrattoFormaPagamentoDefaultId = null;
            MROldPagamento contrattoFormPagamentoDefault = null;
            try {
                contrattoFormaPagamentoDefaultId = new Integer(contrattoFormaPagamentoDefaultString);
            } catch (Exception e) {
            /* not a number */
                contrattoFormaPagamentoDefaultId = null;
            }
            if (contrattoFormaPagamentoDefaultId != null) {
                contrattoFormPagamentoDefault = (MROldPagamento) sx.get(MROldPagamento.class, contrattoFormaPagamentoDefaultId);
                if(contrattoFormPagamentoDefault!=null){
                    filteredList.add(contrattoFormPagamentoDefault);
                }
            }
            return filteredList;
        }
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


        Query query = sx.createQuery("select sum(d.totaleFattura * (CASE WHEN d.class = MROldNotaCredito then (-1) else 1 END)) "
                + "from MROldDocumentoFiscale d "
                + "where d.class <> MROldFatturaAcconto "
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

//    /**
//     * Calcola il totale dei documenti fiscali di saldo emessi per questa prenotazione,
//     * sommando le note credito con segno negativo. Non vengono quindi incluse le
//     * fatture di acconto.
//     * @param sx La sessione al database
//     * @param prenotazione Il contratto per il quale si deve fare il calcolo
//     * @return L'importo totale calcolato, arrotondato a 2 decimali.
//     */
//    public static Double totaleFattureSaldo(Session sx, Prenotazione prenotazione) {
//        Number saldoFatture = (Number) sx.createQuery(
//                "select sum(d.totaleFattura * (CASE WHEN d.class = NotaCredito then (-1) else 1 END)) "
//                + "from DocumentoFiscale d "
//                + "where d.class <> FatturaAcconto "
//                + "and d.prenotazione = :prenotazione "
//                + "and d.prepagato = :prepagato").
//                setParameter("prenotazione", prenotazione).
//                setParameter("prepagato", Boolean.FALSE).
//                uniqueResult();
//        return saldoFatture != null ? MathUtils.round(saldoFatture.doubleValue()) : 0.0;
//    }

    /**
     * Calcola il totale dei documenti fiscali di acconto emessi per questo contratto.
     * @param sx La sessione al database
     * @param contratto Il contratto per il quale si deve fare il calcolo
     * @return L'importo totale calcolato, arrotondato a 2 decimali.
     */
    public static Double totaleFattureAcconto(Session sx, MROldContrattoNoleggio contratto, MROldPrenotazione prenotazione) {
        Query query = sx.createQuery("select sum(d.totaleFattura) "
                + "from MROldFatturaAcconto d "
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

//    /**
//     * Calcola il totale dei documenti fiscali di acconto emessi per questa prenotazione.
//     * @param sx La sessione al database
//     * @param prenotazione Il contratto per il quale si deve fare il calcolo
//     * @return L'importo totale calcolato, arrotondato a 2 decimali.
//     */
//    public static Double totaleFattureAcconto(Session sx, Prenotazione prenotazione) {
//        Number saldoFatture = (Number) sx.createQuery(
//                "select sum(d.totaleFattura) "
//                + "from FatturaAcconto d "
//                + "where d.prenotazione = :prenotazione "
//                + "and d.prepagato = :prepagato").
//                setParameter("prenotazione", prenotazione).
//                setParameter("prepagato", false).
//                uniqueResult();
//        return saldoFatture != null ? MathUtils.round(saldoFatture.doubleValue()) : 0.0;
//    }

    public static Double totaleIncassi(Session sx, MROldContrattoNoleggio contratto) {
        Number saldoIncassi = (Number) sx.createQuery(
                "select sum(r.importo * (CASE WHEN r.segno = :dare then (-1) else 1 END)) "
                + "from RigaPrimanota r left join r.primanota p "
                + "where r.cliente is not null "
                + "and p.contratto = :contratto "
                + "and p.causale.codice in (:codiciPagamento) ").
                setParameterList("codiciPagamento", new Object[]{
                        MROldCausalePrimanota.INCASSO_CLIENTE,
                        MROldCausalePrimanota.INCASSO_CLIENTE_CARTA_DI_CREDITO,
                        MROldCausalePrimanota.PAGAMENTO_A_CLIENTE,
                        MROldCausalePrimanota.BONIFICO_O_PAGAMENTO_CON_ASSEGNO_A_CLIENTE
                }).
                setParameter("contratto", contratto).
                setParameter("dare", MROldCausalePrimanota.DARE).
                uniqueResult();
        return saldoIncassi != null ? MathUtils.round(saldoIncassi.doubleValue()) : 0.0;
    }

    /**
     * Legge tutti i movimenti di incasso di un contratto non associati ad una partita.
     * @param sx La sessione al database
     * @param contratto Il contratto di noleggio
     * @return Una lista contenente le primenote trovate.
     */
    public static List<MROldPrimanota> leggiContropartiteAperte(Session sx, MROldContrattoNoleggio contratto) {
        
        return (List<MROldPrimanota>) sx.createFilter(contratto.getPrimenote(), "where this.partita is null and this.causale.codice in (:codiciIncasso)").
                setParameterList("codiciIncasso", new Object[]{
                        MROldCausalePrimanota.INCASSO_CLIENTE,
                        MROldCausalePrimanota.INCASSO_CLIENTE_CARTA_DI_CREDITO}).
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

    /**
     * Legge tutti i movimenti di rimborso di un contratto non associati ad una partita.
     * @param sx La sessione al database
     * @param contratto Il contratto di noleggio
     * @return Una lista contenente le primenote trovate.
     */
    public static List<MROldPrimanota> leggiContropartiteRimborsiAperte(Session sx, MROldContrattoNoleggio contratto) {
        return (List<MROldPrimanota>) sx.createFilter(contratto.getPrimenote(), "where this.partita is null and this.causale.codice in (:codiciRimborso)").
                setParameterList("codiciRimborso", new Object[]{
                        MROldCausalePrimanota.PAGAMENTO_A_CLIENTE,
                        MROldCausalePrimanota.BONIFICO_O_PAGAMENTO_CON_ASSEGNO_A_CLIENTE}).
                list();
    }

    /**
     * Legge tutte le partite (fatture) aperte di un contratto.
     * @param sx La sessione al database
     * @param contratto Il contratto di noleggio
     * @param prepagato Usato per selezionare le partite aperte di fatture prepagate, non prepagate, o tutte (se impostato a null)
     * @return Una lista contenente le partite trovate.
     */
    public static List<MROldPartita> leggiPartiteAperte(Session sx, MROldContrattoNoleggio contratto, Boolean prepagato) {
        List<MROldPartita> listaPartite = null;
        
        if(contratto == null 
                || (contratto != null && contratto.getId() == null)) {
            return null;
        }
        
        try {
            String hqlPartite = "SELECT partite "
                                + "FROM MROldContrattoNoleggio c "
                                + "WHERE c.id = :idContratto";
                    
            listaPartite = sx.createQuery(hqlPartite).setParameter("idContratto", contratto.getId()).list();
        } catch(Exception ex) {
            ex.printStackTrace();
        } 
        
        if(listaPartite == null 
                || (listaPartite != null && listaPartite.size() == 0)) {
            return listaPartite;
        }
        
        if (prepagato == null) {
            Iterator iteratorPartite = listaPartite.iterator();
            while(iteratorPartite.hasNext()) {
                MROldPartita part = (MROldPartita) iteratorPartite.next();
                if(part != null 
                        && part.getChiusa() != null && part.getChiusa() != false) {
                    listaPartite.remove(part);
                }
            }
            return listaPartite;
            //return (List<Partita>) sx.createFilter(listaPartite, "where this.chiusa = :false").
                    //setParameter("false", Boolean.FALSE).
                    //list();
        }
        
        Iterator iteratorPartite = listaPartite.iterator();
        while(iteratorPartite.hasNext()) {
            MROldPartita part = (MROldPartita) iteratorPartite.next();
            if(part != null 
                    && part.getChiusa() != null && part.getChiusa() != false
                    && part.getFattura().getPrepagato() != null && part.getFattura().getPrepagato() != prepagato) {
                listaPartite.remove(part);
            }
        }
        return listaPartite;
            
        //return (List<Partita>) sx.createFilter(listaPartite, "where this.chiusa = :false and this.fattura.prepagato = :prepagato").
                //setParameter("prepagato", prepagato).
                //setParameter("false", Boolean.FALSE).
                //list();
    }

    /**
     * Legge tutte le partite (fatture) aperte di un contratto.
     * @param sx La sessione al database
     * @param prenotazione la prenotazione
     * @param prepagato Usato per selezionare le partite aperte di fatture prepagate, non prepagate, o tutte (se impostato a null)
     * @return Una lista contenente le partite trovate.
     */
    public static List<MROldPartita> leggiPartiteAperte(Session sx, MROldPrenotazione prenotazione, Boolean prepagato) {

        if (prepagato == null) {
            return (List<MROldPartita>) sx.createFilter(prenotazione.getPartite(), "where this.chiusa = :false").
                    setParameter("false", Boolean.FALSE).
                    list();
        }
        
        return (List<MROldPartita>) sx.createFilter(prenotazione.getPartite(), "where this.chiusa = :false and this.fattura.prepagato = :prepagato").
                setParameter("prepagato", prepagato).
                setParameter("false", Boolean.FALSE).
                list();
    }

    /**
     * Legge tutte le partite
     * @param sx La sessione al database
     * @param fattura  la fattura
     * @param prepagato Usato per selezionare le partite aperte di fatture prepagate, non prepagate, o tutte (se impostato a null)
     * @return Una lista contenente le partite trovate.
     */
    public static List<MROldPartita> leggiPartiteAperte(Session sx, MROldDocumentoFiscale fattura, Boolean prepagato) {

        List lista = sx.createQuery("SELECT p from MROldPartita p where p.fattura.id = :idFattura and p.chiusa = :false").
                setParameter("idFattura", fattura.getId()).
                setParameter("false", Boolean.FALSE).
                list();

        return lista;
    
    }

    /**
     * Carica tutti i sottoconti dei conti nella lista. La lista puo' contenere Mastri, Conti o Sottoconti.
     */
    public static List leggiSottocontiDeiConti(Session sx, List conti) throws HibernateException {
        if (conti == null || conti.size() == 0) {
            return new ArrayList();
        }
        
        QueryData queryData = new QueryData();
        String queryString = "select distinct s from MROldSottoconto s where ";
        for (int iC = 0; iC < conti.size(); iC++) {
            MROldPianoDeiConti aConto = (MROldPianoDeiConti) conti.get(iC);
            queryString += (iC > 0 ? " or " : "") + " (s.codiceMastro = :codiceMastro" + iC;
            queryData.addParameter("codiceMastro" + iC, aConto.getCodiceMastro());
            if (aConto.getCodiceConto().intValue() != 0) {
                queryString += " and s.codiceConto = :codiceConto" + iC;
                queryData.addParameter("codiceConto" + iC, aConto.getCodiceConto());
                if (aConto.getCodiceSottoconto().intValue() != 0) {
                    queryString += " and s.codiceSottoconto = :codiceSottoconto" + iC;
                    queryData.addParameter("codiceSottoconto" + iC, aConto.getCodiceSottoconto());
                }
            }
            queryString += ") ";
        }
        queryString += " order by s.pattern ";
        queryData.setQueryString(queryString);
        return queryData.query(sx);
    }

    /**
     * Carica tutti i sottoconti dei conti nella lista. La lista puo' contenere Mastri, Conti o Sottoconti.
     */
    public static List leggiSottocontiDeiContiId(Session sx, List conti) throws HibernateException {
        if (conti == null || conti.size() == 0) {
            return new ArrayList();
        }

        QueryData queryData = new QueryData();
        String queryString = "select distinct s.id from MROldSottoconto s where ";
        for (int iC = 0; iC < conti.size(); iC++) {
            MROldPianoDeiConti aConto = (MROldPianoDeiConti) conti.get(iC);
            queryString += (iC > 0 ? " or " : "") + " (s.codiceMastro = :codiceMastro" + iC;
            queryData.addParameter("codiceMastro" + iC, aConto.getCodiceMastro());
            if (aConto.getCodiceConto().intValue() != 0) {
                queryString += " and s.codiceConto = :codiceConto" + iC;
                queryData.addParameter("codiceConto" + iC, aConto.getCodiceConto());
                if (aConto.getCodiceSottoconto().intValue() != 0) {
                    queryString += " and s.codiceSottoconto = :codiceSottoconto" + iC;
                    queryData.addParameter("codiceSottoconto" + iC, aConto.getCodiceSottoconto());
                }
            }
            queryString += ") ";
        }
        queryString += " order by s.pattern ";
        queryData.setQueryString(queryString);
        return queryData.query(sx);
    }

    public static MROldPartita leggiPartita(Session sx, MROldDocumentoFiscale fattura) {
         return (MROldPartita) sx.createQuery("select p from MROldPartita p where p.fattura = :fattura").
                setParameter("fattura", fattura).
                setMaxResults(1).
                uniqueResult();
    }

    public static List<MROldPartita> leggiPartiteAperteFattureAcconto(Session sx, MROldContrattoNoleggio contratto) {
        return (List<MROldPartita>) sx.createQuery("select p from MROldPartita p left join p.fattura where p.contratto = :contratto and p.fattura.class = MROldFatturaAcconto and p.partitaSaldo is null").
                setParameter("contratto", contratto).
                list();
    }

    public static List<MROldPartita> leggiPartiteAperteFattureAcconto(Session sx, MROldPrenotazione prenotazione) {

        return (List<MROldPartita>) sx.createQuery("select p from MROldPartita p left join p.fattura where p.prenotazione = :prenotazione and p.fattura.class = MROldFatturaAcconto and p.partitaSaldo is null").
                setParameter("prenotazione", prenotazione).
                list();
    }

    public static List<MROldPartita> leggiPartiteAcconto(Session sx, MROldPartita partitaSaldo) {

        return (List<MROldPartita>) sx.createQuery("select p from MROldPartita p where p.partitaSaldo = :partitaSaldo").
                setParameter("partitaSaldo", partitaSaldo).
                list();
    }

    public static MROldPartita leggiPartita(MROldDocumentoFiscale fattura,Session sx) {
        MROldPartita partita = null;
        try {
            partita = leggiPartita(sx, fattura);
        } catch (Exception ex) {
           ex.printStackTrace();
        }
        return partita;
    }

    public static MROldClienti getClientePrimanota(MROldPrimanota p) {
        return (MROldClienti) getClienteFornitorePrimanota(p, true);
    }

    public static MROldFornitori getFornitorePrimanota(MROldPrimanota p) {
        return (MROldFornitori) getClienteFornitorePrimanota(p, false);
    }

    private static MROldBusinessPartner getClienteFornitorePrimanota(MROldPrimanota p, boolean cliente) {
        if (p != null && p.getRighePrimanota() != null) {
            Iterator righePrimanota = p.getRighePrimanota().iterator();
            while (righePrimanota.hasNext()) {
                RigaPrimanota aRiga = (RigaPrimanota) righePrimanota.next();
                if (cliente) {
                    if (aRiga.getCliente() != null) {
                        return aRiga.getCliente();
                    }
                } else {
                    if (aRiga.getFornitore() != null) {
                        return aRiga.getFornitore();
                    }
                }
            }
        }
        return null;
    }
}
