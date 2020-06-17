/*
 * FatturaUtils.java
 *
 * Created on 11 octombrie 2004, 12:46
 */
package it.myrent.ee.api.utils;

import it.aessepi.myrentcs.utils.ContrattoUtils;
import it.aessepi.myrentcs.utils.FatturaContrattoAdapter;
import it.aessepi.myrentcs.utils.Fatturazione;
import it.aessepi.myrentcs.utils.FatturazioneFactory;
import it.myrent.ee.api.exception.FatturaVuotaException;
import it.myrent.ee.api.preferences.Preferenze;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.TariffaNonValidaException;
import it.myrent.ee.db.*;
import it.myrent.ee.db.MROldContrattoNoleggio;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author  jamess
 */
public class FatturaUtils {
 private static MROldFonteCommissione fontecommissione;
    private static double calcolaImponibileRighe(List righeFattura) {
        double totaleImponibileRighe = 0.0;
        for (int i = 0; i < righeFattura.size(); i++) {
            MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(i);
            /*
             * modifica per jarkko
             */
            if (tmpRiga.getTempoKm() || tmpRiga.getTempoExtra()) {
                /*
                 * fine jarko
                 */
                totaleImponibileRighe = MathUtils.round(totaleImponibileRighe + tmpRiga.getTotaleImponibileRiga().doubleValue());
            }
        }
        return totaleImponibileRighe;
    }

    /** Creates a new instance of FatturaUtils */
    public FatturaUtils() {
    }
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");
    public static final String UNITA_MISURA_GIORNI = bundle.getString("FatturaUtils.msgGG");
    public static final String UNITA_MISURA_KM = bundle.getString("FatturaUtils.msgKM");
    public static final String DESCRIZIONE_GIORNI_TARIFFA = bundle.getString("FatturaUtils.msgListino0GiorniTariffa");
    public static final String DESCRIZIONE_GIORNI_EXTRA = bundle.getString("FatturaUtils.msgListino0GiorniExtra");
    public static final String DESCRIZIONE_COMBUSTIBILE_MANCANTE = bundle.getString("FatturaUtils.msgMaterialeConsumo0");
    private static Log log = LogFactory.getLog(FatturaUtils.class);
    private static final String EARLY_BIRD_31_OTTOBRE = "EB_31OCTOBER";
    private static final String EARLY_BIRD_31_DICEMBRE = "EB_31DECEMBER";
    private static final String EARLY_BIRD_28_FEBBRAIO = "EB_28FEBRUARY";
    private static final String EARLY_BIRD_31_MARZO = "EB_31MARCH";
    private static final String LONG_RENTAL_15_21 = "LR_15_21";
    private static final String LONG_RENTAL_22_28 = "LR_22_28";
    private static final String LONG_RENTAL_29_35 = "LR_29_35";
    private static final String LONG_RENTAL_36_42 = "LR_36_42";
    private static final String LONG_RENTAL_43_OVER = "LR_43_OVER";
    /*added by Andrea*/
    private static Date lastReadSottoContiTime = null;
    private static MROldPianoDeiConti sottocontoValue = null;
    /*ritardo massimo*/
    private static Date lastReadRitardoMassimoTime = null;
    private static Integer ritardoMassimoValue = null;
    /*soloTempoChilometri*/
    private static Date lastReadTempoChilometriTime = null;
    private static Boolean tempoChilometriValue = null;

    private static MROldPianoDeiConti leggiCodiceSottoconto(Session sx) throws HibernateException {
        return leggiSottoconto(sx, new Integer(4), new Integer(10), new Integer(6));
    }

    private static MROldPianoDeiConti leggiCodiceSottocontoCarburante(Session sx) throws HibernateException {
        return leggiSottoconto(sx, new Integer(4), new Integer(10), new Integer(3));
    }

    public static MROldPianoDeiConti leggiSottoconto(Session sx, Integer mastro, Integer conto, Integer sottoconto) throws HibernateException {
        MROldPianoDeiConti retValue = null;
	/*added by Andrea*/
        /*if first time*/
        if(lastReadSottoContiTime == null){
        Query querySottoconto = sx.createQuery(
                "select s from MROldSottoconto s where " + //NOI18N
                "s.codiceMastro = :mastro and " + // NOI18N
                "s.codiceConto = :conto and " + // NOI18N
                "s.codiceSottoconto = :sottoconto"); // NOI18N
            querySottoconto.setParameter("mastro", mastro); //NOI18N
            querySottoconto.setParameter("conto", conto); //NOI18N
            querySottoconto.setParameter("sottoconto", sottoconto); //NOI18N
            retValue = (MROldPianoDeiConti) querySottoconto.uniqueResult();
            sottocontoValue = retValue;
            lastReadSottoContiTime = new Date();
            return retValue;
        }
        /*if too old*/
        Date now = new Date();
        if((now.getTime() - lastReadSottoContiTime.getTime()) > 120000){
            Query querySottoconto = sx.createQuery(
                "select s from MROldSottoconto s where " + //NOI18N
                "s.codiceMastro = :mastro and " + // NOI18N
                "s.codiceConto = :conto and " + // NOI18N
                "s.codiceSottoconto = :sottoconto"); // NOI18N
            querySottoconto.setParameter("mastro", mastro); //NOI18N
            querySottoconto.setParameter("conto", conto); //NOI18N
            querySottoconto.setParameter("sottoconto", sottoconto); //NOI18N
            retValue = (MROldPianoDeiConti) querySottoconto.uniqueResult();
            sottocontoValue = retValue;
            lastReadSottoContiTime = new Date();
            return retValue;
        }
        /*if old value is good*/
        return sottocontoValue;
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

    private static MROldImportoTariffa trovaMinimoStagione(MROldStagioneTariffa stagioneTariffa) {
        MROldImportoTariffa returnValue = null;
        if (stagioneTariffa.getImporti() != null) {
            for (MROldImportoTariffa importoStagione : stagioneTariffa.getImporti()) {
                if (returnValue == null || returnValue.getMinimo().compareTo(importoStagione.getMinimo()) > 0) {
                    returnValue = importoStagione;
                }
            }
        }
        return returnValue;
    }

    public static MROldImportoTariffa trovaMinimoTariffa(MROldTariffa tariffa) {
        if (tariffa.getStagioni().size() > 0) {
            return trovaMinimoStagione(tariffa.getStagioni().iterator().next());
        }
        return null;
    }

    private static MROldImportoExtraPrepay trovaMinimoExtraPrepay(MROldTariffa tariffa) {
        MROldImportoExtraPrepay returnValue = null;
        if (tariffa.getImportiExtraPrepay() != null) {
            Iterator it = tariffa.getImportiExtraPrepay().iterator();
            while (it.hasNext()) {
                MROldImportoExtraPrepay importoExtraPrepay = (MROldImportoExtraPrepay) it.next();
                if (returnValue == null || returnValue.getMinimo().compareTo(importoExtraPrepay.getMinimo()) > 0) {
                    returnValue = importoExtraPrepay;
                }
            }
        }
        return returnValue;
    }

    /**
     * Trova l'importo della tariffa con la massima durata minima che non supera
     * la durata del noleggio. Se la durata del noleggio e' minore di tutte le durate
     * minime degli importi della tariffa, oppure se la tariffa non contiene
     * alcun importo, il valore di ritorno e' null.
     * @param stagioneTariffa La tariffa contenente gli importi e le durate.
     * @param durataTotaleNoleggio La durata del noleggio,
     * da non superare con la durata minima della tariffa.
     * @return l'importo trovato, o null se non ci sono importi validi
     * per la durata specificata.
     */
    private static MROldImportoTariffa trovaImportoStagione(MROldStagioneTariffa stagioneTariffa, double durataTotaleNoleggio) {
        MROldImportoTariffa returnValue = null;
        if (stagioneTariffa.getImporti() != null) {
            for (MROldImportoTariffa importoStagione : stagioneTariffa.getImporti()) {
                if (importoStagione.getMinimo().doubleValue() <= durataTotaleNoleggio) {
                    if (returnValue == null || returnValue.getMinimo().compareTo(importoStagione.getMinimo()) < 0) {
                        returnValue = importoStagione;
                    }
                }
            }
        }
        return returnValue;
    }

    /**
     * Trova l'importo extra prepay della tariffa con la massima durata minima,
     * che non supera la durata extra del prepagato.
     * Se la durata extra del prepagato e' minore di tutte le durate
     * minime degli importi extra prepay della tariffa, oppure se la tariffa
     * non contiene alcun importo extra prepay, il valore di ritorno e' null.
     * @param tariffa La tariffa contenente gli importi extra prepay e le durate.
     * @param durataExtraPrepay La durata del superamento del prepagato
     * da non superare con la durata minima della tariffa.
     * @return l'importo extra prepay trovato, o null se non ci sono
     * importi extra prepay validi per la durata specificata.
     */
    private static MROldImportoExtraPrepay trovaImportoExtraPrepay(MROldTariffa tariffa, double durataExtraPrepay) {
        MROldImportoExtraPrepay returnValue = null;
        if (tariffa.getImportiExtraPrepay() != null) {
            Iterator it = tariffa.getImportiExtraPrepay().iterator();
            while (it.hasNext()) {
                MROldImportoExtraPrepay importoExtraPrepay = (MROldImportoExtraPrepay) it.next();
                if (importoExtraPrepay.getMinimo().doubleValue() <= durataExtraPrepay) {
                    if (returnValue == null || returnValue.getMinimo().compareTo(importoExtraPrepay.getMinimo()) < 0) {
                        returnValue = importoExtraPrepay;
                    }
                }
            }
        }
        return returnValue;
    }

    /**
     * Calcola l'importo giornaliero scorporando l'iva e gli optionals inclusi nell'<code>importoTariffa</code>
     * dato il numero di giorni di riferimento.
     *@param tariffa la tariffa di riferimento
     *@param importoTariffa l'importo complessivo della tariffa
     *@param numeroGiorni il numero di giorni al quale e' riferito l'<code>importoTariffa</code>
     *@return l'importo giornaliero del noleggio.
     */
    private static double calcolaImportoGiornaliero(Session sx, MROldTariffa tariffa, boolean ivaInclusa, double aliquotaIva, double importoTariffa, double numeroGiorni, boolean giornoExtra, boolean extraPrePay) throws HibernateException {
        if (Preferenze.getListiniImportiGiornalieri(sx)) {
            importoTariffa *= numeroGiorni;
        }
        List optionalsInclusi = new ArrayList();
        double oneriAeroportualiIvaApplicabile = 0.0, oneriAeroportualiSenzaIva = 0.0, x = 0.0, s = 0.0, si = 0.0, sp = 0.0, spi = 0.0, iva = (ivaInclusa ? aliquotaIva : 0.0);
        if (giornoExtra == false) {
            if (tariffa.getOptionalsTariffa() != null) {
                Iterator it = tariffa.getOptionalsTariffa().values().iterator();
                while (it.hasNext()) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                    if (Boolean.TRUE.equals(optionalTariffa.getIncluso()) && Boolean.TRUE.equals(optionalTariffa.getSelezionato())) {
                        if (giornoExtra && optionalTariffa.getOptional().getImportoFisso()) {
                            optionalsInclusi.add(optionalTariffa);
                        } else {
                            if (!Boolean.TRUE.equals(optionalTariffa.getOptional().getDepositoCauzionale())) {
                                optionalsInclusi.add(optionalTariffa);
                            }
                        }
                    }
                }
            }
        }
        if (optionalsInclusi.size() > 0) {
            //Calcoliamo i vari parametri.
            for (int i = 0; i < optionalsInclusi.size(); i++) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) optionalsInclusi.get(i);
                if (!Boolean.TRUE.equals(optionalTariffa.getOptional().getKmExtra())) {
                    double importo = optionalTariffa.getImporto().doubleValue();
                    if (Boolean.TRUE.equals(optionalTariffa.getOptional().getSoggIvaImporto())) {
                        if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoPercentuale())) {
                            if (optionalTariffa.getOptional().getOneriAeroportuali() || optionalTariffa.getOptional().getOneriFeroviari()) {
                                oneriAeroportualiIvaApplicabile += importo / 100;
                            } else {
                                spi += importo / 100.0;
                            }
                        } else if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoGiornaliero())) {
                            si += (importo * numeroGiorni);
                        } else if (extraPrePay){
                            // non fa niente in questo caso
                            si += importo;
                        } else {
                            si += importo;
                        }
                    } else {
                        if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoPercentuale())) {
                            if (optionalTariffa.getOptional().getOneriAeroportuali() || optionalTariffa.getOptional().getOneriFeroviari()) {
                                oneriAeroportualiSenzaIva += importo;
                            } else {
                                sp += importo / 100.0;
                            }
                        } else if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoGiornaliero())) {
                            s += (importo * numeroGiorni);
                        } else if (extraPrePay){
                            // non fa niente in questo caso
                            si += importo;
                        } else {
                            s += importo;
                        }
                    }
                }
            }
        }
        //TODO - sbagliato - x = (importoTariffa - (s + si)*(1.0+sp*spi) - iva*(si + s * spi + si * spi)) / (iva+1.0) / (1.0 + spi + sp) / numeroGiorni;
        x = (importoTariffa - si * (1.0 + spi + sp + iva * spi + iva) + s * (1.0 + spi + sp + iva * spi)) / (1.0 + spi + sp + iva * spi + iva) / numeroGiorni;

//        System.out.println("Vecchio valore calcolato: " + x);

        x = FormulaCalcoloNoleggio.calcolaImportoGiornalieroOldWay(importoTariffa, numeroGiorni, s, si, sp + oneriAeroportualiSenzaIva, spi + oneriAeroportualiIvaApplicabile, iva);

//        System.out.println("Vecchio valore calcolato con nuovo metodo: " + x);

        double oneriAeroportuali, totaleQuotePercentualiSenzaIvaApplicabile, totaleQuotePercentualiConIvaApplicabile, totaleOptionalFissiEGiornalieriSenzaIvaApplicabile, totaleOptionalFissiEGiornalieriConIvaApplicabile;


        totaleOptionalFissiEGiornalieriConIvaApplicabile = si;
        System.out.println("totaleOptionalFissiEGiornalieriConIvaApplicabile: " + totaleOptionalFissiEGiornalieriConIvaApplicabile);

        totaleOptionalFissiEGiornalieriSenzaIvaApplicabile = s;
        System.out.println("totaleOptionalFissiEGiornalieriSenzaIvaApplicabile: " + totaleOptionalFissiEGiornalieriSenzaIvaApplicabile);


        totaleQuotePercentualiConIvaApplicabile = spi;
//        System.out.println("totaleQuotePercentualiConIvaApplicabile: " + totaleQuotePercentualiConIvaApplicabile);


        totaleQuotePercentualiSenzaIvaApplicabile = sp;
//        System.out.println("totaleQuotePercentualiSenzaIvaApplicabile: " + totaleQuotePercentualiSenzaIvaApplicabile);

        oneriAeroportuali = oneriAeroportualiIvaApplicabile + oneriAeroportualiSenzaIva;
//        System.out.println("oneriAeroportuali: " + oneriAeroportuali);


        x = FormulaCalcoloNoleggio.calcolaImportoGiornalieroNewWay(importoTariffa, oneriAeroportuali,
                totaleQuotePercentualiSenzaIvaApplicabile, totaleQuotePercentualiConIvaApplicabile,
                totaleOptionalFissiEGiornalieriSenzaIvaApplicabile, totaleOptionalFissiEGiornalieriConIvaApplicabile,
                iva, numeroGiorni);

        System.out.println("Nuovo costo giornaliero calcolato: " + x);

        return x;
    }

    /**
     * Calcola l'importo giornaliero scorporando l'iva e gli optionals inclusi nell'<code>importoTariffa</code>
     * dato il numero di giorni di riferimento.
     *@param tariffa la tariffa di riferimento
     *@param importoTariffa l'importo complessivo della tariffa
     *@param numeroGiorni il numero di giorni al quale e' riferito l'<code>importoTariffa</code>
     *@return l'importo giornaliero del noleggio.
     */
    private static double calcolaImportoGiornaliero(Session sx, Map optionalsTariffa, boolean ivaInclusa, double aliquotaIva, double importoTariffa, double numeroGiorni,boolean extraPrePay) throws HibernateException {
        if (Preferenze.getListiniImportiGiornalieri(sx)) {
            importoTariffa *= numeroGiorni;
        }
        List optionalsInclusi = new ArrayList();
        double oneriAeroportualiIvaApplicabile = 0.0, oneriAeroportualiSenzaIva = 0.0;
        double x = 0.0, s = 0.0, si = 0.0, sp = 0.0, spi = 0.0, iva = ivaInclusa ? aliquotaIva : 0.0;

        if (optionalsTariffa != null) {
            Iterator it = optionalsTariffa.values().iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                if (Boolean.TRUE.equals(optionalTariffa.getIncluso()) && Boolean.TRUE.equals(optionalTariffa.getSelezionato())) {
                    optionalsInclusi.add(optionalTariffa);
                }
            }
        }
        if (optionalsInclusi.size() > 0) {
            //Calcoliamo i vari parametri.
            for (int i = 0; i < optionalsInclusi.size(); i++) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) optionalsInclusi.get(i);
                if (!Boolean.TRUE.equals(optionalTariffa.getOptional().getKmExtra())) {
                    double importo = optionalTariffa.getImporto().doubleValue();
                    if (Boolean.TRUE.equals(optionalTariffa.getOptional().getSoggIvaImporto())) {
                        if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoPercentuale())) {
                            if (optionalTariffa.getOptional().getOneriAeroportuali() || optionalTariffa.getOptional().getOneriFeroviari()) {
                                oneriAeroportualiIvaApplicabile += importo / 100;
                            } else {
                                spi += importo / 100.0;
                            }
                        } else if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoGiornaliero())) {
                            si += (importo * numeroGiorni);
                        } else if (extraPrePay){
                            // non fa niente in questo caso
                        } else {
                            si += importo;
                        }
                    } else {
                        if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoPercentuale())) {
                            if (optionalTariffa.getOptional().getOneriAeroportuali() || optionalTariffa.getOptional().getOneriFeroviari()) {
                                oneriAeroportualiSenzaIva += importo;
                            } else {
                                sp += importo / 100.0;
                            }
                        } else if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoGiornaliero())) {
                            s += (importo * numeroGiorni);
                        } else if (extraPrePay){
                            // non fa niente in questo caso
                        } else {
                            si += importo;
                        }
                    }
                }
            }
        }
        //TODO - sbagliato - x = (importoTariffa - (s + si)*(1.0+sp*spi) - iva*(si + s * spi + si * spi)) / (iva+1.0) / (1.0 + spi + sp) / numeroGiorni;
        x = (importoTariffa - si * (1.0 + spi + sp + iva * spi + iva) + s * (1.0 + spi + sp + iva * spi)) / (1.0 + spi + sp + iva * spi + iva) / numeroGiorni;

//        System.out.println("Vecchio valore calcolato: " + x);

        x = FormulaCalcoloNoleggio.calcolaImportoGiornalieroOldWay(importoTariffa, numeroGiorni, s, si, sp + oneriAeroportualiSenzaIva, spi + oneriAeroportualiIvaApplicabile, iva);

//        System.out.println("Vecchio valore calcolato con nuovo metodo: " + x);

        double oneriAeroportuali, totaleQuotePercentualiSenzaIvaApplicabile, totaleQuotePercentualiConIvaApplicabile,
                totaleOptionalFissiEGiornalieriSenzaIvaApplicabile, totaleOptionalFissiEGiornalieriConIvaApplicabile;


        totaleOptionalFissiEGiornalieriConIvaApplicabile = si;
//        System.out.println("totaleOptionalFissiEGiornalieriConIvaApplicabile: " + totaleOptionalFissiEGiornalieriConIvaApplicabile);

        totaleOptionalFissiEGiornalieriSenzaIvaApplicabile = s;
//        System.out.println("totaleOptionalFissiEGiornalieriSenzaIvaApplicabile: " + totaleOptionalFissiEGiornalieriSenzaIvaApplicabile);


        totaleQuotePercentualiConIvaApplicabile = spi;
//        System.out.println("totaleQuotePercentualiConIvaApplicabile: " + totaleQuotePercentualiConIvaApplicabile);


        totaleQuotePercentualiSenzaIvaApplicabile = sp;
//        System.out.println("totaleQuotePercentualiSenzaIvaApplicabile: " + totaleQuotePercentualiSenzaIvaApplicabile);

        oneriAeroportuali = oneriAeroportualiIvaApplicabile + oneriAeroportualiSenzaIva;
//        System.out.println("oneriAeroportuali: " + oneriAeroportuali);


        x = FormulaCalcoloNoleggio.calcolaImportoGiornalieroNewWay(importoTariffa, oneriAeroportuali,
                totaleQuotePercentualiSenzaIvaApplicabile, totaleQuotePercentualiConIvaApplicabile,
                totaleOptionalFissiEGiornalieriSenzaIvaApplicabile, totaleOptionalFissiEGiornalieriConIvaApplicabile,
                iva, numeroGiorni);

//        System.out.println("Nuovo costo giornaliero calcolato: " + x);

        return x;
    }

     /**
     * Calcola il costo del prepagato.
     * Attenzione: il periodo tra <code>inizio</coed> e <code>fine</code> deve avere esattamente <code>giorniVoucher</code> giorni.
     */
    public static List calcolaRighePrepagato(
            Session sx,
            MROldTariffa tariffa,
            Date inizio,
            Date fine,
            Double scontoPercentuale,
            Integer giorniVoucher, MROldFonteCommissione fonte) throws HibernateException, TariffaNonValidaException {
        fontecommissione=fonte;
        List righe = new ArrayList();

        double giorniPrepagati = 0.0;
        if (giorniVoucher != null) {
            giorniPrepagati = giorniVoucher.doubleValue();
        }

        MROldImportoTariffa minimoTariffa = trovaMinimoTariffa(tariffa);
        if (minimoTariffa == null) {
            throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgTariffaNonContieneImportiGruppoPeriodo"));
        } else if (minimoTariffa.getMinimo() > giorniVoucher) {
            throw new TariffaNonValidaException(MessageFormat.format(bundle.getString("FatturaUtils.msgNoleggioPrepagatoInferiorePeriodoMinimoRichiesto0"), minimoTariffa.getMinimo()));
        }

        fine = FormattedDate.add(inizio, Calendar.DAY_OF_MONTH, (int) giorniPrepagati);

        Date dataInizio = FormattedDate.extractDate(inizio);
        Date oraInizio = FormattedDate.extractTime(inizio);
        Date dataFine = FormattedDate.extractDate(fine);
        Date oraFine = FormattedDate.extractTime(fine);

        for (MROldStagioneTariffa stagione : tariffa.getStagioni()) {
            Date inizioStagione = null, fineStagione = null;
            if (stagione.inStagione(dataInizio) && !dataInizio.equals(dataFine)) {
                inizioStagione = FormattedDate.createTimestamp(dataInizio, oraInizio);
                if (!stagione.getMultistagione() || stagione.inStagione(dataFine)) {
                    fineStagione = FormattedDate.createTimestamp(dataFine, oraFine);
                } else {
                    fineStagione = FormattedDate.createTimestamp(FormattedDate.add(stagione.getFine(), Calendar.DAY_OF_MONTH, 1), oraInizio);
                    dataInizio = FormattedDate.add(stagione.getFine(), Calendar.DAY_OF_MONTH, 1);
                }
            } else {
                continue;
            }

            if (inizioStagione != null && fineStagione != null) {

                Double giorniStagione = FormattedDate.numeroGiorni(inizioStagione, fineStagione, true);

                MROldPianoDeiConti pianoDeiConti = leggiCodiceSottoconto(sx);
                MROldCodiciIva codiceIva = (MROldCodiciIva) sx.get(MROldCodiciIva.class, stagione.getCodiceIva().getId());

                //Troviamo l'importo che va bene per questo numero di giorni.
                MROldImportoTariffa importoStagione = trovaImportoStagione(stagione, giorniStagione);

                //FIXME: Da far vedere all'utente che qui bisogna rispettare un periodo minimo di noleggio!
                if (importoStagione == null) {
                    importoStagione = trovaMinimoStagione(stagione);
                    if (importoStagione == null) {
                        throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgTariffaNonContieneImportiGruppoPeriodo"));
                    }
//                    } else {
//                        giorniStagione = importoStagione.getMinimo().doubleValue();
//                    }
                }

                //I giorni base sono limitati al massimo della tariffa.
                double giorniBase = Math.min(importoStagione.getMassimo().doubleValue(), giorniStagione);
                //I giorni extra sono quelli che superano il massimo della tariffa.
                double giorniExtra = Math.max(0.0, giorniStagione - importoStagione.getMassimo().doubleValue());

                double importoGiornaliero, importoExtra;
                if (importoStagione.getImportoFisso().equals(Boolean.FALSE)) {
                    importoGiornaliero = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importoStagione.getImportoBase().doubleValue(),
                            importoStagione.getMinimo().doubleValue(),
                            false,
                            false);
                    importoExtra = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importoStagione.getImportoExtra().doubleValue(),
                            1.0,
                            true,
                            false);
                } else {
                    importoGiornaliero = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importoStagione.getImportoBase().doubleValue(),
                            giorniBase,
                            false,
                            false);
                    importoExtra = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importoStagione.getImportoExtra().doubleValue(),
                            1.0,
                            true,
                            false);
                }

                String descrizioneTariffa = stagione.getDescrizione();

                MROldRigaDocumentoFiscale primaRiga = creaRigaNoleggio(descrizioneTariffa, giorniBase, importoGiornaliero, 0.0, codiceIva, pianoDeiConti);

                if (primaRiga != null) {
                    righe.add(primaRiga);
                    System.out.println("dati prima riga: " + primaRiga.getQuantita() + " " + primaRiga.getTotaleImponibileRiga());
                }

                MROldRigaDocumentoFiscale secondaRiga = creaRigaGiorniExtra(descrizioneTariffa, giorniExtra, importoExtra, 0.0, codiceIva, pianoDeiConti);
                if (secondaRiga != null) {
                    System.out.println("dati seconda riga: " + secondaRiga.getQuantita() + " " + secondaRiga.getTotaleImponibileRiga());
                    righe.add(secondaRiga);
                }
            }
        }

        return aggiungiRigheOptionalsPrepagato(sx, righe, tariffa, giorniPrepagati, scontoPercentuale, inizio);
    }

    public static List calcolaRighe(
            Session sx,
            MROldContrattoNoleggio contratto,
            MROldTariffa tariffa,
            Date inizio,
            Date fine,
            Integer km,
            Integer litri,
            MROldCarburante carburante,
            Double scontoPercentuale,
            Integer giorniVoucher,
            MROldSede pickupLocation
            ) throws HibernateException, TariffaNonValidaException {

System.out.println("METODO: calcolaRighe");
        List righe = new ArrayList();
        if (contratto != null && contratto.getId() != null) {
            contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contratto.getId());
        } else {
            contratto = null;
        }
        MROldPianoDeiConti pianoDeiConti = leggiCodiceSottoconto(sx);

        if (scontoPercentuale == null) {
            scontoPercentuale = 0.0;
        }


        Map totaliCarburanti = new HashMap();
        double totaleKm = 0.0;

        double giorniPrepagati = 0.0;
        if (giorniVoucher != null) {
            giorniPrepagati = giorniVoucher;
        }
        //Arrotondiamo per eccesso, imputando almeno 1 giorno. Non teniamo conto dell'ora legale.
        //Ritardo massimo 59 minuti.
        double giorniEsattiSenzaRitardo = FormattedDate.numeroGiorni(inizio, fine, true);

        Integer ritardoMassimoConsentito = ritardoMassimoConsentito(sx);

        double giorniEsatti = FormattedDate.numeroGiorni(inizio, FormattedDate.add(fine, Calendar.MINUTE, -ritardoMassimoConsentito.intValue()), true);
        double giorniInteriPerEccesso = Math.ceil(giorniEsatti);
        double giorniInteriPerDiffetto = Math.floor(giorniEsatti);

        double durataNoleggioPerEccesso = Math.max(1.0, giorniInteriPerEccesso);

        /***
        * FATTA PERSONALIZZAZIONE ESCLUSIVA PER TOURING CARS ( JARKKO )
        */
        Boolean isTouringCars = new Boolean(ResourceBundle.getBundle("messages", Locale.getDefault()).getString("is_touringcars"));
        /*
         * Andrea is_not_day24h
         */
        Boolean isNotDay24H = new Boolean(ResourceBundle.getBundle("messages", Locale.getDefault()).getString("is_not_day24h"));
        if (Boolean.TRUE.equals(isTouringCars) || Boolean.TRUE.equals(isNotDay24H)) {
                
                Date tmpDataInizio = FormattedDate.extractDate(inizio);
                Date tmpOraInizio = FormattedDate.extractTime(inizio);
                Date tmpDataFine = FormattedDate.extractDate(fine);
                Date tmpOraFine = FormattedDate.extractTime(fine);

                //se la data di fine Ã¨ maggiore della data di inzio, allora abbiamo almeno due giorni di prepgato

                if ((tmpDataFine.getTime() > tmpDataInizio.getTime()) && (tmpOraFine.getTime() <= tmpOraInizio.getTime())) {
                    //se Ã¨ cosi, allora aggiunge un giorno al prepagato
                    durataNoleggioPerEccesso++;
                }

            }
         /***
         * FINE PERSONALIZZAZIONE ESCLUSIVA PER TOURING CARS ( JARKKO )
         */


        boolean prepagato = (giorniPrepagati > 0);
        boolean prepagatoExtra = (prepagato && durataNoleggioPerEccesso > giorniPrepagati);

        boolean mezzaGiornata = false;
        boolean giornataRidotta = false;
        boolean notteExtra = false;
        boolean giornoFestivo = false;
        double giorniFestivi = 0;

        System.out.println("prepagatoExtra "+prepagatoExtra);

        if (prepagatoExtra) {
            aggiungiRighePrepagatoExtra(
                    sx,
                    righe, tariffa,
                    durataNoleggioPerEccesso,
                    giorniPrepagati, scontoPercentuale,
                    pianoDeiConti);
        } else if (!prepagato) {
            Iterator<MROldOptional> optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = optionals.next();
                if (optional.getTempoExtra()) {
                    MROldOptionalTariffa optionalTariffa = tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato() || optionalTariffa.getSelezionatoRientro()) {
                        notteExtra = notteExtra | optional.getNotteExtra();
                        giornataRidotta = giornataRidotta | optional.getGiornataRidotta();
                        mezzaGiornata = mezzaGiornata | optional.getMezzaGiornata();
                        giornoFestivo = giornoFestivo | optional.getGiornoFestivo();
                        if (optional.getGiornoFestivo() && optionalTariffa.getQuantita() != null) {
                            giorniFestivi += optionalTariffa.getQuantita();
                        }
                    }
                }
            }

            if (giornataRidotta || mezzaGiornata || notteExtra || giornoFestivo) {
                if (giornataRidotta && giorniEsattiSenzaRitardo - giorniFestivi >= 1) {
                    throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgGiornataRidottaNonValida"));
                } else if (giornataRidotta && (mezzaGiornata || notteExtra)) {
                    throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgGiornataRidottaInsiemeMezzaGiornataNotteExtra"));
                } else if (giorniEsattiSenzaRitardo <= 1 && (mezzaGiornata || notteExtra)) {
                    throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgMezzaGiornataNotteExtraNonValide"));
                } else if (giorniFestivi > giorniInteriPerEccesso) {
                    throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgGiorniFestiviSuperanoGiorniNoleggio"));
                }
                double giorniNoleggio;
                if (!giornataRidotta && !mezzaGiornata && !notteExtra) {
                    giorniNoleggio = giorniInteriPerEccesso - giorniFestivi;
                } else {
                    giorniNoleggio = giorniInteriPerDiffetto - giorniFestivi;
                }
                if (giorniNoleggio > 0) {
                    // Abbiamo solo i giorni festivi.
                    aggiungiRigheNonPrepagato(sx,
                            righe, tariffa, inizio, fine,
                            giorniNoleggio, scontoPercentuale,
                            pianoDeiConti);
                }

            } else {
                // Aggiungiamo il numero di giorni arrotondato per eccesso, minimo uno.
                aggiungiRigheNonPrepagato(sx,
                        righe, tariffa, inizio, fine,
                        durataNoleggioPerEccesso, scontoPercentuale,
                        pianoDeiConti);
            }
        }

        //FIXME Da chiedere se addebitare anche qui il noleggio contabile.


        List righeOptionals = aggiungiRigheOptionals(sx, righe, tariffa, scontoPercentuale, durataNoleggioPerEccesso, giorniPrepagati, totaleKm, totaliCarburanti, inizio, pickupLocation);


        return righeOptionals;

    }





    private static void aggiungiRigheNonPrepagato(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Date inizio,
            Date fine,
            double giorniNoleggio,
            double scontoPercentuale,
            MROldPianoDeiConti contoRicavo) throws TariffaNonValidaException {
        System.out.println("METODO: aggiungiRigheNonPrepagato");
        fine = FormattedDate.add(inizio, Calendar.DAY_OF_MONTH, (int) giorniNoleggio);

        Date dataInizio = FormattedDate.extractDate(inizio);
        Date oraInizio = FormattedDate.extractTime(inizio);
        Date dataFine = FormattedDate.extractDate(fine);
        Date oraFine = FormattedDate.extractTime(fine);

        MROldImportoTariffa minimoTariffa = trovaMinimoTariffa(tariffa);
        if (minimoTariffa == null) {
            throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgTariffaNonContieneImportiGruppoPeriodo"));
        } else if (minimoTariffa.getMinimo() > giorniNoleggio) {
            throw new TariffaNonValidaException(MessageFormat.format(bundle.getString("FatturaUtils.msgNoleggioInferiorePeriodoMinimoRichiesto0"), minimoTariffa.getMinimo()));
        }


        for (MROldStagioneTariffa stagione : tariffa.getStagioni()) {
            Date inizioStagione = null, fineStagione = null;
            if (stagione.inStagione(dataInizio) && !dataInizio.equals(dataFine)) {
                inizioStagione = FormattedDate.createTimestamp(dataInizio, oraInizio);
                if (!stagione.getMultistagione() || stagione.inStagione(dataFine)) {
                    fineStagione = FormattedDate.createTimestamp(dataFine, oraFine);
                } else {
                    //Per poter misurare correttamente la durata del periodo aggiungiamo un giorno a fine stagione.
                    fineStagione = FormattedDate.createTimestamp(FormattedDate.add(stagione.getFine(), Calendar.DAY_OF_MONTH, 1), oraInizio);
                    dataInizio = FormattedDate.add(stagione.getFine(), Calendar.DAY_OF_MONTH, 1);
                }
            } else {
                continue;
            }

            if (inizioStagione != null && fineStagione != null) {

                Double giorniStagione = FormattedDate.numeroGiorni(inizioStagione, fineStagione, true);

                MROldImportoTariffa importStagione = trovaImportoStagione(stagione, giorniStagione);
                if (importStagione == null) {
                    importStagione = trovaMinimoStagione(stagione);
                }
                if (importStagione == null) {
                    throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgTariffaNonContieneImportiGruppoPeriodo"));
                }
                String descrizioneTariffa = stagione.getDescrizione();
                MROldCodiciIva codiceIva = (MROldCodiciIva) sx.get(MROldCodiciIva.class, stagione.getCodiceIva().getId());

                double giorniBase = Math.min(importStagione.getMassimo().doubleValue(), giorniStagione);
                double giorniExtra = Math.max(0.0, giorniStagione - importStagione.getMassimo().doubleValue());
                double importoGiornaliero = 0.0,
                        importoExtra = 0.0;

                if (importStagione.getImportoFisso().equals(Boolean.FALSE)) {
                    importoGiornaliero = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importStagione.getImportoBase().doubleValue(),
                            importStagione.getMinimo().doubleValue(),
                            false,
                            false);
                    importoExtra = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importStagione.getImportoExtra().doubleValue(),
                            1.0,
                            true,
                            false);
                } else {
                    importoGiornaliero = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importStagione.getImportoBase().doubleValue(),
                            giorniBase,
                            false,
                            false);
                    importoExtra = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importStagione.getImportoExtra().doubleValue(),
                            1.0,
                            true,
                            false);
                }
                MROldRigaDocumentoFiscale primaRiga = creaRigaNoleggio(
                        descrizioneTariffa,
                        giorniBase,
                        importoGiornaliero,
                        scontoPercentuale,
                        codiceIva,
                        contoRicavo);

                if (primaRiga != null) {
                    righeFattura.add(primaRiga);
                }

                MROldRigaDocumentoFiscale secondaRiga = creaRigaGiorniExtra(
                        descrizioneTariffa,
                        giorniExtra,
                        importoExtra,
                        scontoPercentuale,
                        codiceIva,
                        contoRicavo);
                if (secondaRiga != null) {
                    righeFattura.add(secondaRiga);
                }
            }
        }
    }

    private static void aggiungiRighePrepagatoExtra(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            double giorniNoleggio,
            double giorniPrepagati,
            double scontoPercentuale,
            MROldPianoDeiConti contoRicavo) throws TariffaNonValidaException {

        ImportoGiornaliero importoTariffa = null;
        String descrizioneTariffa = null;

        System.out.println("METODO: aggiungiRighePrepagatoExtra");


        if (tariffa.getImportiExtraPrepay() == null || tariffa.getImportiExtraPrepay().isEmpty()) {
            System.out.println("MROldTariffa non valida exception");
            throw new TariffaNonValidaException(
                    bundle.getString("FatturaUtils.msgTariffaExtraPrepayMancante"));
        } else {
            importoTariffa = trovaImportoExtraPrepay(tariffa, giorniNoleggio - giorniPrepagati);
            if (importoTariffa == null) {
                importoTariffa = trovaMinimoExtraPrepay(tariffa);
                if (importoTariffa == null) {
                    throw new TariffaNonValidaException(
                            bundle.getString("FatturaUtils.msgTariffaExtraPrepayNonContieneImportiGruppoPeriodo"));
                } else {
                    giorniNoleggio = giorniPrepagati + importoTariffa.getMinimo().doubleValue();
                }
            }
            descrizioneTariffa = tariffa.getDescrizione();
        }

        MROldCodiciIva codiceIva = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
        MROldCodiciIva codiceIvaExtraPrepay = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaExtraPrepay().getId());

        double giorniBase = Math.min(importoTariffa.getMassimo().doubleValue(), giorniNoleggio - giorniPrepagati);
        double giorniExtra = Math.max(0.0, giorniNoleggio - giorniPrepagati - importoTariffa.getMassimo().doubleValue());
        double importoGiornaliero = 0.0, importoExtra = 0.0;

        if (importoTariffa.getImportoFisso().equals(Boolean.FALSE)) {
            importoGiornaliero = calcolaImportoGiornaliero(sx,
                    tariffa.getOptionalsTariffa(),
                    tariffa.getIvaInclusaExtraPrepay(),
                    codiceIvaExtraPrepay.getAliquota(),
                    importoTariffa.getImportoBase().doubleValue(),
                    importoTariffa.getMinimo().doubleValue(),
                    false);
            importoExtra = calcolaImportoGiornaliero(
                    sx,
                    tariffa.getOptionalsTariffa(),
                    tariffa.getIvaInclusaExtraPrepay(),
                    codiceIvaExtraPrepay.getAliquota(),
                    importoTariffa.getImportoExtra().doubleValue(),
                    1.0,
                    false);
        } else {
            importoGiornaliero = calcolaImportoGiornaliero(
                    sx,
                    tariffa.getOptionalsTariffa(),
                    tariffa.getIvaInclusaExtraPrepay(),
                    codiceIvaExtraPrepay.getAliquota(),
                    importoTariffa.getImportoBase().doubleValue(),
                    giorniBase,
                    false);
            importoExtra = calcolaImportoGiornaliero(
                    sx,
                    tariffa.getOptionalsTariffa(),
                    tariffa.getIvaInclusaExtraPrepay(),
                    codiceIvaExtraPrepay.getAliquota(),
                    importoTariffa.getImportoExtra().doubleValue(),
                    1.0,
                    false);
        }

        MROldRigaDocumentoFiscale primaRiga = creaRigaNoleggio(
                descrizioneTariffa,
                giorniBase,
                importoGiornaliero,
                scontoPercentuale,
                codiceIva,
                contoRicavo);

        if (primaRiga != null) {
            righeFattura.add(primaRiga);
        }

        MROldRigaDocumentoFiscale secondaRiga = creaRigaGiorniExtra(
                descrizioneTariffa,
                giorniExtra,
                importoExtra,
                scontoPercentuale,
                codiceIva,
                contoRicavo);
        if (secondaRiga != null) {
            righeFattura.add(secondaRiga);
        }
    }

    private static MROldRigaDocumentoFiscale creaRigaNoleggio(
            String descrizioneTariffa,
            double giorniBase,
            double importoGiornaliero,
            double scontoPercentuale,
            MROldCodiciIva codiceIva,
            MROldPianoDeiConti contoRicavo) {

        MROldRigaDocumentoFiscale primaRiga = null;
        //Prima riga. Giorni previsti.
        double quantita = giorniBase, prezzo = importoGiornaliero, imponibile = 0.0, iva = 0.0, aliquota = 0.0, totale = 0.0;
        //double giorniPrepagatiRestanti = Math.max(giorniPrepagati - giorniBase, 0.0);
        String descrizione, unitaMisura;
        if (quantita > 0) {
            descrizione = MessageFormat.format(DESCRIZIONE_GIORNI_TARIFFA, descrizioneTariffa);
            unitaMisura = UNITA_MISURA_GIORNI;
            quantita = MathUtils.round(quantita, 5);
            prezzo = MathUtils.round(prezzo, 5);
            //FIXME Qui veniva inserito lo sconto percentuale.
            imponibile = MathUtils.round(quantita * prezzo * (100.0 - scontoPercentuale) * 0.01, 5);
            //imponibile = MathUtils.round(quantita * prezzo, 5);
            aliquota = codiceIva.getAliquota();
            iva = MathUtils.round(imponibile * aliquota, 5);
            totale = imponibile + iva;

            primaRiga = new MROldRigaDocumentoFiscale();
            primaRiga.setDescrizione(descrizione);
            primaRiga.setUnitaMisura(unitaMisura);
            primaRiga.setQuantita(new Double(quantita));
            primaRiga.setPrezzoUnitario(new Double(prezzo));
            primaRiga.setTotaleImponibileRiga(new Double(imponibile));
            primaRiga.setTotaleIvaRiga(new Double(iva));
            primaRiga.setCodiceIva(codiceIva);
            primaRiga.setSconto(scontoPercentuale);
            primaRiga.setScontoFisso(new Double(0));
            primaRiga.setCodiceSottoconto(contoRicavo);
            primaRiga.setTotaleRiga(new Double(totale));

            primaRiga.setTempoKm(Boolean.TRUE);
            primaRiga.setTempoExtra(Boolean.FALSE);
            primaRiga.setFranchigia(Boolean.FALSE);
        }
        return primaRiga;
    }

    private static MROldRigaDocumentoFiscale creaRigaGiorniExtra(
            String descrizioneTariffa,
            double giorniExtra,
            double importoExtra,
            double scontoPercentuale,
            MROldCodiciIva codiceIva,
            MROldPianoDeiConti contoRicavo) {

        MROldRigaDocumentoFiscale secondaRiga = null;

        //Seconda riga. Eventuale superamento.
        double quantita = giorniExtra;
        double prezzo = importoExtra;
        double imponibile = 0.0;
        double iva = 0.0;
        double aliquota = 0.0;
        double totale = 0.0;

        String descrizione, unitaMisura;

        if (giorniExtra > 0) {
            quantita = MathUtils.round(quantita, 5);
            prezzo = MathUtils.round(prezzo, 5);
            imponibile = MathUtils.round(quantita * prezzo * (100.0 - scontoPercentuale) * 0.01, 5);
            aliquota = codiceIva.getAliquota().doubleValue();
            iva = MathUtils.round(imponibile * aliquota, 5);
            totale = imponibile + iva;
            descrizione = MessageFormat.format(DESCRIZIONE_GIORNI_EXTRA, descrizioneTariffa);
            unitaMisura = UNITA_MISURA_GIORNI;

            secondaRiga = new MROldRigaDocumentoFiscale();
            secondaRiga.setDescrizione(descrizione);
            secondaRiga.setUnitaMisura(unitaMisura);
            secondaRiga.setQuantita(new Double(quantita));
            secondaRiga.setPrezzoUnitario(new Double(prezzo));
            secondaRiga.setTotaleImponibileRiga(new Double(imponibile));
            secondaRiga.setTotaleIvaRiga(new Double(iva));
            secondaRiga.setCodiceIva(codiceIva);
            secondaRiga.setSconto(scontoPercentuale);
            secondaRiga.setScontoFisso(new Double(0));
            secondaRiga.setCodiceSottoconto(contoRicavo);
            secondaRiga.setTotaleRiga(new Double(totale));

            secondaRiga.setTempoKm(Boolean.FALSE);
            secondaRiga.setTempoExtra(Boolean.TRUE);
            secondaRiga.setFranchigia(Boolean.FALSE);
        }

        return secondaRiga;
    }

    private static List aggiungiRigheOptionals(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Double scontoPercentuale,
            double totaleGiorni,
            double giorniVoucher,
            double totaleKm,
            Map totaliCarburanti,
            Date dataInizio,
            MROldSede pickupLocation) {

        aggiungiRigheOptionalsTempoExtra(sx, righeFattura, tariffa, scontoPercentuale);
        Iterator carburanti = totaliCarburanti.keySet().iterator();
        while (carburanti.hasNext()) {
            MROldCarburante carburante = (MROldCarburante) carburanti.next();
            carburante = (MROldCarburante) sx.get(MROldCarburante.class, carburante.getId());
            Integer litri = (Integer) totaliCarburanti.get(carburante);
            aggiungiRigaCarburante(sx, righeFattura, tariffa, litri, carburante);
        }
        // OPTIONALS USCITA

        System.out.println("aggiungiRigheOptionalsNormale");

        boolean extraPrePay = giorniVoucher > 0 ? true : false;

        aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, totaleKm, false,null);

        // Per applicare gli oneri agli optionals aggiunti al rientro
        // OPTIONALS RIENTRO - CON ONERI
        //aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, totaleKm, true, extraPrePay);

        // ONERI
        aggiungiRigheOptionalsPercentuali(sx, righeFattura, tariffa, true);

        // Per non applicare gli oneri agli optionals aggiunti al rientro
        // OPTIONALS RIENTRO - SENZA ONERI
        //aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, totaleKm, true);

        // FRANCHIGIE
        aggiungiRigheOptionalsFranchigie(sx, righeFattura, tariffa, true);
        return righeFattura;
    }

    private static List aggiungiRigheOptionalsFranchigie(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            boolean extraPrePay)
            throws HibernateException {

        if (tariffa.getOptionalsTariffa() != null) {

            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());

            if (tariffa.getCodiceIvaExtraPrepay()!= null && extraPrePay)
                codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaExtraPrepay().getId());

            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());

            //Aggiungiamo le franchigie in caso di danno.
            Iterator it = tariffa.getOptionalsTariffa().values().iterator();

            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalTariffa.getOptional();
                if (optionalTariffa.getSelezionatoFranchigia() && optionalTariffa.getFranchigia() != null) {

                    double quantita = 1.0;

                    double prezzo = optionalTariffa.getFranchigia();

                    MROldPianoDeiConti contoContabileFranchigia = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabileFranchigia().getId());

                    MROldRigaDocumentoFiscale riga = creaRigaOptional(
                            optional,
                            quantita,
                            prezzo,
                            null,
                            false,
                            true,
                            codiceIvaImponibile,
                            codiceIvaNonSoggetto,
                            contoContabileFranchigia);

                    if (riga != null) {
                        righeFattura.add(riga);
                    }
                }
            }
        }
        return righeFattura;
    }

    /*Added by Saurabh*/
    /**
     * Legge dal database tutte le fatture, note credito e ricevute fiscali non prepagate, emesse per il contratto,
     * escludendo le fatture di acconto.
     * @param sx
     * @param contrattoNoleggio Il contratto in causa.
     * @return
     */
    public static List<MROldDocumentoFiscale> leggiDocumentiFiscaliEmessi(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        return sx.createQuery(
                "select d from MROldDocumentoFiscale d "
                        + "where d.class <> MROldFatturaAcconto "
                        + "and d.contratto.id = :contratto "
                        + "and d.prepagato = :false ").
                setParameter("contratto", contrattoNoleggio.getId()).
                setParameter("false", Boolean.FALSE).
                list();
    }
    public static List<MROldDocumentoFiscale> leggiFattureAcconto(Session sx, MROldPrenotazione p) {
        MROldContrattoNoleggio contratto = null;

        if (p != null && p.getId() != null) {
            MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, p.getCommissione().getId());

            if (commissione.getContratto() != null) {
                contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, commissione.getContratto().getId());
            }
        }
        Query query = sx.createQuery("select d from MROldDocumentoFiscale d "
                + "where d.class = MROldFatturaAcconto "
                + (contratto != null ? "and d.contratto.id = :contratto " : "")
                + "and d.prenotazione.id = :prenotazione "
                + "and d.prepagato = :false ");

        query.setParameter("prenotazione", p.getId());
        query.setParameter("false", Boolean.FALSE);

        if (contratto != null) {
            query.setParameter("contratto", contratto.getId());
        }

        return query.list();
    }
    public static List<MROldPrimanota> leggiIncassiPrenotazione(Session sx, MROldPrenotazione p) {
        List<MROldPrimanota> lista = null;

        lista = sx.createQuery("SELECT p FROM MROldPrimanota p WHERE p.prenotazione.id = :id").setParameter("id", p.getId()).list();

        return lista;
    }
    public static void calcolaTotaleNoleggioPPay(Session sx, MROldContrattoNoleggio c) throws TariffaNonValidaException {

        return;
    }

    //
    private static List aggiungiRigheOptionalsPrepagato(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            double giorniVoucher,
            double scontoPercentuale,
            Date inizio) {
        // OPTIONALS INCLUSI
        aggiungiRigheOptionalsNonPercentualiPrepagato(sx, righeFattura, tariffa, giorniVoucher, Boolean.TRUE);

        aggiungiRigheOptionalsNonPercentualiPrepagato(sx, righeFattura, tariffa, giorniVoucher, Boolean.FALSE);

        // Per applicare gli oneri agli optionals non inclusi nella tariffa
        // OPTIONALS NON INCLUSI - CON ONERI
//        aggiungiRigheOptionalsNonPercentualiPrepagato(sx, righeFattura, tariffa, giorniVoucher, Boolean.FALSE);

        // ONERI
        aggiungiRigheOptionalsPercentuali(sx, righeFattura, tariffa, Boolean.TRUE);

        //PER JARKO
        //OPTIONALS EARLY BIRD

        // Mauro - Commentato... prova per vedere se viene ancora doppio... per jarkko, early bird
        //aggiungiRigheOptionalsEarlyBird(sx, righeFattura, tariffa, inizio);

        // Mauro - Commentato... prova per vedere se viene ancora doppio... per jarkko, long rental
        //OPTIONALS LONG RENT
        //aggiungiRigheOptionalsLongRental(sx, righeFattura, tariffa, giorniVoucher);

        Iterator it = righeFattura.iterator();
        while (it.hasNext()) {
            MROldRigaDocumentoFiscale r = (MROldRigaDocumentoFiscale) it.next();
            System.out.println("METODO aggiungiRigheOptionalsPrepagato: descrizoione " + r.getDescrizione() + " - imponibile= " + r.getTotaleImponibileRiga() + " - quantita= " + r.getQuantita());
        }



        return righeFattura;
    }

    private static List aggiungiRigheOptionalsNonPercentualiPrepagato(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            double giorniVoucher,
            Boolean incluso) {

        if (tariffa.getOptionalsTariffa() != null) {

            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());

            Iterator it = tariffa.getOptionalsTariffa().values().iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalTariffa.getOptional();

                //escludiamo i deposito cauzionali
                if (optional != null && optional.getDepositoCauzionale() != null && Boolean.TRUE.equals(optional.getDepositoCauzionale())) {
                    ;
                } else {
                    if ((( // Optionals inclusi se richiesti
                            incluso && optionalTariffa.getIncluso() && (!optional.getGuidatoreAggiuntivo() || optionalTariffa.getSelezionato()))
                            || ( // Optionals non inclusi e selezionati se richiesti.
                            !incluso
                            && !optionalTariffa.getIncluso()
                            && optionalTariffa.getSelezionato()
                            && optionalTariffa.getPrepagato()))
                            && !optional.getImportoPercentuale()
                            && !optional.getKmExtra()
                            && !optional.getImportoKm()
                            && !optional.getTempoExtra()) {


                        double prezzo = optionalTariffa.getImporto();

                        System.out.println("aggiungiRigheOptionalsNonPercentualiPrepagato prezzo optional: " + prezzo);

                        double quantita = 0.0;
                        double molteplicita = 1.0;
                        if (optionalTariffa.getQuantita() != null) {
                            molteplicita = optionalTariffa.getQuantita();
                        }

                        //Qui aggiungiamo gli altri optionals.
                        if (optional.getImportoGiornaliero().equals(Boolean.TRUE)) {
                            quantita = giorniVoucher;
                        } else {
                            quantita = molteplicita;
                            molteplicita = 1.0;
                        }

                        //Limitiamo al massimo addebitabile, se impostato.
                        //calcola gli eventuali giorni minimo
                        if (optional.getAddebitoMinimo() != null) {
                            if (Boolean.TRUE.equals(Preferenze.getOptionalMinimoGiorni(sx))) {
                                quantita = optional.getAddebitoMinimo();
                            } else {
                                quantita = Math.min(optional.getAddebitoMinimo().doubleValue(), quantita);
                            }

                            if (quantita < giorniVoucher) {
                                quantita = giorniVoucher;
                            }
                        }
                        //Attenti ai km, dove il massimo viene moltiplicato per il numero di giorni.
                        if (optional.getAddebitoMassimo() != null) {
                            quantita = Math.min(quantita, optional.getAddebitoMassimo().doubleValue());
                        }

                        MROldPianoDeiConti contoRicavo = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabile().getId());
                        //Add the check condition if is not included and not oneri areoportuali
                        //check if the reservation source has the property
                        //then you can understund if setPrepagato to FALSE or doesn't change nothing
                        if(fontecommissione.getCliente() != null &&  fontecommissione.getIsNotBookingOptionalPrepaid()!=null){
                            if(!fontecommissione.getIsNotBookingOptionalPrepaid() || optionalTariffa.getIncluso()){
                                optionalTariffa.setPrepagato(true);
                            }
                            else{
                                optionalTariffa.setPrepagato(false);
                            }
                        }
                        else{
                            if(fontecommissione.getCliente() != null){
                                optionalTariffa.setPrepagato(true);
                            }
                        }

                        for (int i = 0; i < molteplicita; i++) {
                            MROldRigaDocumentoFiscale riga = creaRigaOptional(
                                    optional,
                                    quantita,
                                    prezzo,
                                    null,
                                    false,
                                    false,
                                    codiceIvaImponibile,
                                    codiceIvaNonSoggetto,
                                    contoRicavo);
                            if (riga != null) {
                                righeFattura.add(riga);
                            }
                        }
                    }
                }
            }
        }
        return righeFattura;
    }

    /**
     * Crea la riga della fattura per un optional.
     * @param optional L'optional da usare
     * @param quantita La quantita' di questo optional
     * @param prezzo Il prezzo del singolo optional.
     * @param scontoPercentuale Lo sconto da applicare. Richiesto se scontabile.
     * @param scontabile Indica se questo optional e' scontabile o no.
     * Se scontabile, lo sconto non puo' essere null
     * @param franchigia Indica se l'importo da addebitare e' quello della franchigia.
     * @param codiceIvaImponibile Il codice IVA da usare se l'optional e' sooggetto ad IVA
     * @param codiceIvaNonSoggetto Il codice IVA da usare se l'optional non e' soggetto ad IVA
     * @param contoRicavo Il conto contabile di ricavo.
     * @return
     */
    private static MROldRigaDocumentoFiscale creaRigaOptional(
            MROldOptional optional,
            Double quantita,
            Double prezzo,
            Double scontoPercentuale,
            Boolean scontabile,
            Boolean franchigia,
            MROldCodiciIva codiceIvaImponibile,
            MROldCodiciIva codiceIvaNonSoggetto,
            MROldPianoDeiConti contoRicavo) {
        MROldRigaDocumentoFiscale riga = null;

        double imponibile = 0.0, iva = 0.0, aliquota = 0.0, totale = 0.0;

        if (quantita != 0.0) {
            MROldCodiciIva codiceIva;
            if (franchigia) {
                if (optional.getSoggIvaDanno()) {
                    codiceIva = codiceIvaImponibile;
                } else {
                    codiceIva = codiceIvaNonSoggetto;
                }
            } else {
                if (optional.getSoggIvaImporto()) {
                    codiceIva = codiceIvaImponibile;
                } else {
                    codiceIva = codiceIvaNonSoggetto;
                }
            }

            prezzo = MathUtils.round(prezzo, 5);

            if (scontabile) {
                imponibile = MathUtils.round(quantita * prezzo * (100.0 - scontoPercentuale.doubleValue()) * 0.01, 5);
            } else {
                imponibile = MathUtils.round(quantita * prezzo, 5);
            }
           aliquota = codiceIva.getAliquota().doubleValue();

           System.out.println("creaRigaOptional codiceIva= " + aliquota);

           if (optional.getSoggIvaImporto()) {
                            prezzo = prezzo - (1*aliquota);
                        }
           if( optional.getDescrizione().contains("AGREEMENTDISCOUNT")){
               iva = MathUtils.round(0, 5);
           }
           else{
               iva = MathUtils.round(imponibile * aliquota, 5);
           }
            totale = imponibile + iva;

            riga = new MROldRigaDocumentoFiscale();
            if (franchigia) {
                if (optional.getFranchigia()) {
                    if (optional.getDanni()) {
                        riga.setDescrizione(bundle.getString("FatturaUtils.msgAddebitoFranchigiaDanni"));
                    } else {
                        riga.setDescrizione(bundle.getString("FatturaUtils.msgAddebitoFranchigiaFurto"));
                    }
                } else {
                    riga.setDescrizione(MessageFormat.format(bundle.getString("FatturaUtils.msg0AccessorioDanneggiatoMancante"), optional.getDescrizione()));
                }
            } else {
                riga.setDescrizione(optional.getDescrizione());
            }
            if (franchigia) {
                riga.setUnitaMisura(null);
            } else {
                riga.setUnitaMisura(optional.getUnitaMisura());
            }

            riga.setQuantita(new Double(quantita));
            riga.setPrezzoUnitario(new Double(prezzo));
            if (scontabile) {
                riga.setSconto(scontoPercentuale);
            } else {
                riga.setSconto(new Double(0));
            }
            riga.setScontoFisso(new Double(0));
            riga.setTotaleImponibileRiga(new Double(imponibile));
            riga.setTotaleIvaRiga(new Double(iva));
            riga.setCodiceIva(codiceIva);
            riga.setCodiceSottoconto(contoRicavo);
            riga.setTotaleRiga(new Double(totale));

            riga.setOptional(optional);
            riga.setTempoKm(Boolean.FALSE);
            riga.setTempoExtra(Boolean.FALSE);
            riga.setFranchigia(franchigia);

        }

        return riga;
    }

    private static List aggiungiRigheOptionalsTempoExtra(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Double scontoPercentuale) {

        if (tariffa.getOptionalsTariffa() != null) {

            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());

            Iterator it = tariffa.getOptionalsTariffa().values().iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalTariffa.getOptional();

                if (optional.getTempoExtra()
                        && (optionalTariffa.getSelezionato()
                        || optionalTariffa.getSelezionatoRientro())) {

                    boolean scontabile = true;

                    double quantita = 1.0;

                    double molteplicita = 1.0;
                    if (optionalTariffa.getQuantita() != null) {
                        molteplicita = optionalTariffa.getQuantita();
                    }

                    quantita = molteplicita;
                    molteplicita = 1.0;

                    double prezzo = optionalTariffa.getImporto();

                    MROldPianoDeiConti contoRicavo = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabile().getId());

                    for (int i = 0; i < molteplicita; i++) {
                        MROldRigaDocumentoFiscale riga = creaRigaOptionalTempoExtra(
                                optional,
                                quantita,
                                prezzo,
                                scontoPercentuale,
                                scontabile,
                                codiceIvaImponibile,
                                codiceIvaNonSoggetto,
                                contoRicavo);

                        if (riga != null) {
                            righeFattura.add(riga);
                        }
                    }
                }
            }
        }
        return righeFattura;
    }

    /**
     * Crea la riga della fattura per un optional di tempo extra.
     * @param optional L'optional da usare
     * @param quantita La quantita' di questo optional
     * @param prezzo Il prezzo del singolo optional.
     * @param scontoPercentuale Lo sconto da applicare. Richiesto se scontabile.
     * @param scontabile Indica se questo optional e' scontabile o no.
     * Se scontabile, lo sconto non puo' essere null
     * @param codiceIvaImponibile Il codice IVA da usare se l'optional e' sooggetto ad IVA
     * @param codiceIvaNonSoggetto Il codice IVA da usare se l'optional non e' soggetto ad IVA
     * @param contoRicavo Il conto contabile di ricavo.
     * @return
     */
    private static MROldRigaDocumentoFiscale creaRigaOptionalTempoExtra(
            MROldOptional optional,
            Double quantita,
            Double prezzo,
            Double scontoPercentuale,
            Boolean scontabile,
            MROldCodiciIva codiceIvaImponibile,
            MROldCodiciIva codiceIvaNonSoggetto,
            MROldPianoDeiConti contoRicavo) {
        MROldRigaDocumentoFiscale riga = null;

        double imponibile = 0.0, iva = 0.0, aliquota = 0.0, totale = 0.0;

        if (quantita > 0.0) {
            MROldCodiciIva codiceIva;
            if (Boolean.TRUE.equals(optional.getSoggIvaImporto())) {
                codiceIva = codiceIvaImponibile;
            } else {
                codiceIva = codiceIvaNonSoggetto;
            }

            prezzo = MathUtils.round(prezzo, 5);
            if (scontabile) {
                imponibile = MathUtils.round(quantita * prezzo * (100.0 - scontoPercentuale.doubleValue()) * 0.01, 5);
            } else {
                imponibile = MathUtils.round(quantita * prezzo, 5);
            }
            aliquota = codiceIva.getAliquota().doubleValue();
            iva = MathUtils.round(imponibile * aliquota, 5);
            totale = imponibile + iva;

            riga = new MROldRigaDocumentoFiscale();
            //FIXME Da impostare anche la descrizione tariffa
            riga.setDescrizione(optional.getDescrizione());
            riga.setUnitaMisura(UNITA_MISURA_GIORNI);
            riga.setQuantita(new Double(quantita));
            riga.setPrezzoUnitario(new Double(prezzo));
            if (scontabile) {
                riga.setSconto(scontoPercentuale);
            } else {
                riga.setSconto(new Double(0));
            }
            riga.setScontoFisso(new Double(0));
            riga.setTotaleImponibileRiga(new Double(imponibile));
            riga.setTotaleIvaRiga(new Double(iva));
            riga.setCodiceIva(codiceIva);
            riga.setCodiceSottoconto(contoRicavo);
            riga.setTotaleRiga(new Double(totale));

            riga.setOptional(optional);
            riga.setTempoKm(Boolean.FALSE);
            riga.setTempoExtra(Boolean.FALSE);
            riga.setFranchigia(Boolean.FALSE);

        }

        return riga;
    }


    private static List aggiungiRigheOptionalsNonPercentuali(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Double scontoPercentuale,
            double totaleGiorni,
            double giorniVoucher,
            double totaleKm,
            boolean extraPrePay,
            MROldSede pickupLocation) {

        if (tariffa.getOptionalsTariffa() != null) {


            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());

            if (tariffa.getCodiceIvaExtraPrepay()!= null && extraPrePay)
                codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaExtraPrepay().getId());

            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());

            Iterator it = tariffa.getOptionalsTariffa().values().iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalTariffa.getOptional();

                //escludiamo i deposito cauzionali
                if (optional.getDepositoCauzionale() != null && Boolean.TRUE.equals(optional.getDepositoCauzionale())) {
                    ;
                } else {
                    if ( // Gli optionals in uscita solo all'uscita
                            optionalTariffa.getSelezionato()
                            && // Escludiamo gli optionals percentuali
                            !optional.getImportoPercentuale()
                            && // Escludiamo gli optionals di tempo extra.
                            !optional.getTempoExtra()
                            && // escludiamo i prepagati
                            (!optionalTariffa.getPrepagato() || giorniVoucher == 0)
                            ) {

                        // Gli optionals delle assicurazioni base sono scontabili quando sono inclusi nella tariffa.
                        boolean scontabile =
                                optionalTariffa.getIncluso().booleanValue()
                                && optional.getAssicurazione().booleanValue()
                                && (optional.getDanni().booleanValue() || optional.getFurto().booleanValue())
                                && optional.getFranchigia().booleanValue();
                        //Oppure se abilitiamo gli sconti per gli optionals.
                        scontabile = scontabile || Preferenze.getOptionalsAbilitaSconto(sx);

                        double quantita = 0.0;
                        double molteplicita = 1.0;
                        if (optionalTariffa.getQuantita() != null) {
                            molteplicita = optionalTariffa.getQuantita();
                        }

                        if (optional.getKmExtra().equals(Boolean.TRUE) && optionalTariffa.getSelezionatoRientro()) {
                            //Qui aggiungiamo i km extra, se ci sono
                            //quantita = MathUtils.round(totaleKm - optional.getKmInclusi().doubleValue() * totaleGiorni, 5);
                            double kmCalcolati = MathUtils.round(totaleKm - optional.getKmInclusi().doubleValue() * totaleGiorni, 5);
                            quantita = kmCalcolati>=0?kmCalcolati:0.0;
                        } else {
                            //Qui aggiuingiamo gli altri optionals.
                            if (optional.getImportoGiornaliero().equals(Boolean.TRUE)) {
                                if (optionalTariffa.getIncluso().equals(Boolean.TRUE) || optionalTariffa.getPrepagato().equals(Boolean.TRUE)) {
                                    quantita = MathUtils.round(Math.max(totaleGiorni - giorniVoucher, 0.0), 5);
                                } else {
                                    quantita = MathUtils.round(totaleGiorni, 5);
                                }
                            } else if (optionalTariffa.getOptional().getImportoKm().equals(Boolean.TRUE)) {
                                quantita = MathUtils.round(totaleKm, 5);
                            } else {
                                if ((optionalTariffa.getIncluso().equals(Boolean.TRUE) || optionalTariffa.getPrepagato().equals(Boolean.TRUE)) && giorniVoucher > 0.0) {
                                    quantita = 0.0;
                                } else {
                                    // Per gli optionals a quantita 1, usiamo la molteplicita come quantita.
                                    quantita = molteplicita;
                                    molteplicita = 1.0;
                                }
                            }
                        }
                        //Limitiamo al massimo addebitabile, se impostato.
                        //Attenti ai km, dove il massimo viene moltiplicato per il numero di giorni.
                        //calcola gli eventuali giorni minimo
                        if (optional.getAddebitoMinimo() != null) {
                            if (Boolean.TRUE.equals(Preferenze.getOptionalMinimoGiorni(sx))) {
                                quantita = optional.getAddebitoMinimo();
                            } else {
                                quantita = Math.min(optional.getAddebitoMinimo().doubleValue(), quantita);
                            }

                            if (quantita < totaleGiorni) {
                                quantita = totaleGiorni;
                            }
                        }
                        if (optional.getAddebitoMassimo() != null) {
                            if (optional.getKmExtra().equals(Boolean.TRUE)) {
                                quantita = Math.min(quantita, optional.getAddebitoMassimo().doubleValue() * totaleGiorni);
                            } else {
                                quantita = Math.min(quantita, optional.getAddebitoMassimo().doubleValue());
                            }
                        }

                        MROldPianoDeiConti contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabile().getId());

                        //scorpororo l'onere aeroportuale
                        double prezzo = 0.0;

                        //aOptionalTariffa.getOptional().getOneriAeroportuali() && !aOptionalTariffa.getOptional().getOneriFeroviari()

                        /*
                         * PARTE FUNZIONANTE
                         */
                        if (!Boolean.TRUE.equals(optional.getRoadTax()) && optional.getSoggIvaImporto()) {
                            if (Preferenze.getScorporoOneriAeroportualiDallaTariffa(sx)) {
                                prezzo = optionalTariffa.getImporto();
                            } else {
                                if (pickupLocation!=null&& (pickupLocation.getAeroporto()|| pickupLocation.getFerrovia())){
                                    Double percentualeOnereAeroportuale = TariffeUtils.getPercentualeOneriAeroportuali(tariffa);
                                    prezzo = optionalTariffa.getImporto() / (percentualeOnereAeroportuale != null ? percentualeOnereAeroportuale : 1.0);
                                }
                                else
                                    prezzo = optionalTariffa.getImporto();
                            }
                        } else {
                            prezzo = optionalTariffa.getImporto();
                        }
                        /*
                         * FINE PARTE FUNZIONANTE
                         */

                        for (int i = 0; i < molteplicita; i++) {
                            MROldRigaDocumentoFiscale riga = creaRigaOptional(
                                    optional,
                                    quantita,
                                    prezzo,
                                    scontoPercentuale,
                                    scontabile,
                                    false,
                                    codiceIvaImponibile,
                                    codiceIvaNonSoggetto,
                                    contoContabile);
                            if (riga != null) {
                                righeFattura.add(riga);
                            }
                        }
                    }
                }
            }
        }
        return righeFattura;
    }

    public static Boolean calcolaOptionalsPercentualiSoloTempoKm(Session sx) {
        /*added by Andrea*/
        if(lastReadTempoChilometriTime == null){
             MROldSetting s = ( MROldSetting) sx.createQuery("select s from  MROldSetting s where s.key = :key").setParameter("key", "calcola.importo.optional.percentuali.solo.tkm").uniqueResult();

            if (s != null && !s.getValue().equals("")) {
                tempoChilometriValue = Boolean.parseBoolean(s.getValue());
                lastReadTempoChilometriTime = new Date();
                return tempoChilometriValue;
            }
            tempoChilometriValue = Boolean.TRUE;
            lastReadTempoChilometriTime = new Date();
            return Boolean.TRUE;
        }
        /*if too old*/
        Date now = new Date();
        if(lastReadTempoChilometriTime != null){
            if((now.getTime() - lastReadTempoChilometriTime.getTime()) > 120000){
                 MROldSetting s = ( MROldSetting) sx.createQuery("select s from  MROldSetting s where s.key = :key").setParameter("key", "calcola.importo.optional.percentuali.solo.tkm").uniqueResult();

                if (s != null && !s.getValue().equals("")) {
                    tempoChilometriValue = Boolean.parseBoolean(s.getValue());
                    lastReadTempoChilometriTime = new Date();
                    return tempoChilometriValue;
                }
                tempoChilometriValue = Boolean.TRUE;
                lastReadTempoChilometriTime = new Date();
                return Boolean.TRUE;
            }
        }
        /*if old value is good*/
        //da qui
        /*Setting s = (Setting) sx.createQuery("select s from Setting s where s.key = :key").setParameter("key", "calcola.importo.optional.percentuali.solo.tkm").uniqueResult();

        if (s != null && !s.getValue().equals("")) {
            return Boolean.parseBoolean(s.getValue());
        }*/
        //a qui con return
        return tempoChilometriValue;
    }

    public static Integer ritardoMassimoConsentito(Session sx) {
        /*added by Andrea*/
        if(lastReadRitardoMassimoTime == null){
            Integer ritardoMassimo = 0;
            //Setting s = (Setting) sx.createQuery("select s from Setting s where s.key = :key").setParameter("key", "contratto.ritardo_massimo_minuti").uniqueResult();
  
            /*
            Andrea
            */
            Query query = sx.createQuery("select s from MROldSetting s where s.key = :key").setParameter("key", "contratto.ritardo_massimo_minuti");
            query.setCacheable(true);
            MROldSetting s = (MROldSetting)query.uniqueResult();

            if (s != null && !s.getValue().equals("")) {
                try {
                    ritardoMassimo = Integer.parseInt(s.getValue());
                } catch (Exception e) {
                    ritardoMassimo = 0;
                }
            }
            ritardoMassimoValue = ritardoMassimo;
            lastReadRitardoMassimoTime = new Date();
            //a qui con return
            return ritardoMassimo;
        }
        /*if too old*/
        Date now = new Date();
        if((now.getTime() - lastReadRitardoMassimoTime.getTime()) > 120000){
            //da qui
            Integer ritardoMassimo = 0;
            MROldSetting s = (MROldSetting) sx.createQuery("select s from MROldSetting s where s.key = :key").setParameter("key", "contratto.ritardo_massimo_minuti").uniqueResult();

            if (s != null && !s.getValue().equals("")) {
                try {
                    ritardoMassimo = Integer.parseInt(s.getValue());
                } catch (Exception e) {
                    ritardoMassimo = 0;
                }
            }
            ritardoMassimoValue = ritardoMassimo;
            lastReadRitardoMassimoTime = new Date();
            //a qui con return
            return ritardoMassimo;
        }
        //da qui
        /*Integer ritardoMassimo = 0;
        Setting s = (Setting) sx.createQuery("select s from Setting s where s.key = :key").setParameter("key", "contratto.ritardo_massimo_minuti").uniqueResult();

        if (s != null && !s.getValue().equals("")) {
            try {
                ritardoMassimo = Integer.parseInt(s.getValue());
            } catch (Exception e) {
                ritardoMassimo = 0;
            }
        }*/
        //a qui con return
        return ritardoMassimoValue;
    }

public static List aggiungiRigheOptionalsPercentuali(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            boolean extraPrePay)
            throws HibernateException {


        if (tariffa.getOptionalsTariffa() != null && righeFattura.size() > 0) {



            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());

            if (tariffa.getCodiceIvaExtraPrepay()!= null && extraPrePay)
                codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaExtraPrepay().getId());


            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());


            //Aggiungiamo gli optionals percentuali solo se abbiamo le righe :-)
            if (righeFattura.size() > 0) {
                //Aggiungiamo optionals percentuali.
                Iterator it = tariffa.getOptionalsTariffa().values().iterator();
                Iterator itForOneri = tariffa.getOptionalsTariffa().values().iterator();
                Iterator<MROldOptionalTariffa> itForOptionalInclusi = tariffa.getOptionalsTariffa().values().iterator();

                double totaleImponibileRighe = 0.0;
                double totaleImponibileKmInclusi = 0.0;
                double totaleTempoKm = 0;

                double totaleImponibileOptionalInclusi = 0.0;
                double totalePercentualeOptionalInclusi = 0.0;
                double aliquotaIva = 0;

                double y5 = 0.0, y4 = 0.0, y3 = 0.0, y2 = 0.0, y1 = 0.0;
                /**
                 * y5 è totaleOptionalFissiEGiornalieriConIvaApplicabile
                 * y4 è OptionalFissiEGiornalieriSenzaIvaApplicabile
                 * y3 è totaleQuotePercentualiConIvaApplicabile
                 * y2totaleQuotePercentualiSenzaIvaApplicabile
                 * y1 oneri aeroportuali
                 * y0 importo tariffa
                 */
                double totaleImponibileRigheConIvaApplicabile = 0.0;

                List<MROldOptional> optionalInclusi = new ArrayList<MROldOptional>();
                MROldOptionalTariffa aOptionalTariffa;


                while (itForOptionalInclusi.hasNext()) {
                    aOptionalTariffa = itForOptionalInclusi.next();
                    //if (aOptionalTariffa.getIncluso() && !aOptionalTariffa.getPrepagato()) {
                    if (aOptionalTariffa.getIncluso() || ((aOptionalTariffa.getSelezionato() || aOptionalTariffa.getSelezionatoRientro()) && !aOptionalTariffa.getPrepagato())) {


                        optionalInclusi.add(aOptionalTariffa.getOptional());


                        if (aOptionalTariffa.getOptional().getImportoPercentuale() && aOptionalTariffa.getOptional().getSoggIvaImporto() && !aOptionalTariffa.getOptional().getOneriAeroportuali() && !aOptionalTariffa.getOptional().getOneriFeroviari()) {
                            y3 += aOptionalTariffa.getImporto() * 0.01;
                        } else if (aOptionalTariffa.getOptional().getImportoPercentuale() && !aOptionalTariffa.getOptional().getSoggIvaImporto() && !aOptionalTariffa.getOptional().getOneriAeroportuali() && !aOptionalTariffa.getOptional().getOneriFeroviari()) {
                            y2 += aOptionalTariffa.getImporto() * 0.01;
                        } else if (aOptionalTariffa.getOptional().getImportoPercentuale() && aOptionalTariffa.getOptional().getSoggIvaImporto() && (aOptionalTariffa.getOptional().getOneriAeroportuali() || aOptionalTariffa.getOptional().getOneriFeroviari())) {
                            y1 += aOptionalTariffa.getImporto() * 0.01;
                        }


                    }


                }

                for (int i = 0; i < righeFattura.size(); i++) {
                    MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(i);
                    /*
                     * modifica per jarkko
                     */
                    if (tmpRiga.getTempoKm() || tmpRiga.getTempoExtra()) {
                        /*
                         * fine jarkko
                         */

                        totaleImponibileRighe = MathUtils.round(totaleImponibileRighe + tmpRiga.getTotaleImponibileRiga().doubleValue());
                        aliquotaIva = tmpRiga.getCodiceIva().getAliquota();
                    } else {
                        if (tmpRiga.getOptional() != null && !tmpRiga.getFranchigia() && !tmpRiga.isRigaDescrittiva() && tmpRiga.getOptional().getSoggIvaImporto()) {
                            if (optionalInclusi.contains(tmpRiga.getOptional())) {
                                if (!tmpRiga.getOptional().getImportoPercentuale() && tmpRiga.getTotaleIvaRiga() > 0) {
                                    y5 += tmpRiga.getTotaleImponibileRiga(); //totaleOptionalFissiEGiornalieriConIvaApplicabile
                                } else if (!tmpRiga.getOptional().getImportoPercentuale() && tmpRiga.getTotaleIvaRiga() == 0) {
                                    y4 += tmpRiga.getTotaleImponibileRiga(); //OptionalFissiEGiornalieriSenzaIvaApplicabile
                                }
                            }
                        }

                    }

                }

                totaleTempoKm = totaleImponibileRighe; // corrisponde a variabile d della formula


                //importoTariffa = ((1) * (y5 * (1 - y3) + totaleTempoKm) * 1 / (1 - y3)) * (1) -y4;

                double importoTariffaAnteOptionalPercentuali = ( (1) * (y5 * (1 - y3) + totaleTempoKm) * 1/ (1 - y3) ) * ( 1 ) + y4;
                double importoTariffaAnteOneri = ((1 + y1) * (y5 * (1 - y3) + totaleTempoKm) * 1 / (1 - y3)) * (1);

//                System.out.println("\n\n *** Importo tariffa Calcolato: ");

                /***
                 * Cerco MROldOptional Percentuali tranne gli oneri
                 */
                while (it.hasNext()) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();

                    /*
                     * modifica per jarkko
                     */
                    /*if (Boolean.TRUE.equals(optionalTariffa.getIncluso())) {
                    totaleImponibileRighe -= optionalTariffa.getImporto();
                    }

                     *
                     */
                    MROldOptional optional = optionalTariffa.getOptional();
//                    if (optionalTariffa.getIncluso() && optional.getImportoPercentuale() && !optional.getTempoExtra() && !optional.getOneriAeroportuali() && !optional.getOneriFeroviari()) {
                    if ((optionalTariffa.getSelezionato() || optionalTariffa.getIncluso())
                            && optional.getImportoPercentuale() && !optional.getTempoExtra()
                            && !optional.getOneriAeroportuali() && !optional.getOneriFeroviari()) {

                        double molteplicita = 1.0;
                        if (optionalTariffa.getQuantita() != null) {
                            molteplicita = optionalTariffa.getQuantita();
                        }

                        double quantita = MathUtils.round(optionalTariffa.getImporto().doubleValue() * 0.01, 5);

                        /**
                         *
                         * Bisogna assegnare il prezzo uguale a importoTariffa se l'optional è incluso
                         *
                         */
                        double prezzo = totaleImponibileRighe;

                        if (optionalTariffa.getIncluso() || optionalTariffa.getSelezionato()) {
                            // Modifica da mettere in preferenza a seconda dei due casi?
                            // 2014-08-04 - Mauro E Giacomo
                            Boolean calcolaTKM = calcolaOptionalsPercentualiSoloTempoKm(sx);
                            if (calcolaTKM) {
                                prezzo = totaleImponibileRighe;
                            } else {
                                prezzo = importoTariffaAnteOptionalPercentuali - y5;
                            }

//                            prezzo = totaleImponibileRighe;
//                        }

//                        System.out.println("\n\n *** Importo di applicazione MROldOptional Incluso: " + prezzo);

                        MROldPianoDeiConti contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabile().getId());
			//Add the check condition if is not included and not oneri areoportuali
                        //check if the reservation source has the property to asjkdasfjfajss
                        //then you can understun if setPrepagato to FALSE or doesn't change nothing
                        if(fontecommissione != null){
                            if(fontecommissione.getCliente() != null &&  fontecommissione.getIsNotBookingOptionalPrepaid()!=null){
                                if(!fontecommissione.getIsNotBookingOptionalPrepaid() || optionalTariffa.getIncluso()){
                                    optionalTariffa.setPrepagato(true);
                                }
                                else{
                                    optionalTariffa.setPrepagato(false);
                                }
                            }
                            else{
                                if(fontecommissione.getCliente() != null){
                                     optionalTariffa.setPrepagato(true);
                                }
                            }
                        }
                        for (int i = 0; i < molteplicita; i++) {
                            MROldRigaDocumentoFiscale riga = creaRigaOptional(
                                    optional,
                                    quantita,
                                    prezzo,
                                    null,
                                    false,
                                    false,
                                    codiceIvaImponibile,
                                    codiceIvaNonSoggetto,
                                    contoContabile);

                            if (riga != null) {
                                righeFattura.add(riga);
                            }
                        }
                    }
                }

                /**
                 * Mi calcolo il totale dell'imponibile con IVA Applicabile per gli oneri
                 *
                 */
                for (int i = 0; i < righeFattura.size(); i++) {
                    MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(i);
                    /*
                     * modifica per jarkko
                     */
                    if (tmpRiga.getTotaleIvaRiga() != 0.0) {
                        /*
                         * fine jarko
                         */
                        totaleImponibileRigheConIvaApplicabile += MathUtils.round(tmpRiga.getTotaleImponibileRiga().doubleValue());
                    }


                }

                }
                /***
                 * Cerco Gli Oneri tra gli optional Percentuali
                 *
                 *
                 */
                while (itForOneri.hasNext()) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) itForOneri.next();

                    MROldOptional optional = optionalTariffa.getOptional();
                    if (optionalTariffa.getSelezionato() && optional.getImportoPercentuale() && !optional.getTempoExtra()
                            && ((optional.getOneriAeroportuali() || optional.getOneriFeroviari())) ) {

                        double molteplicita = 1.0;
                        if (optionalTariffa.getQuantita() != null) {
                            molteplicita = optionalTariffa.getQuantita();
                        }

                        double quantita = 1;

                        double prezzo =  importoTariffaAnteOptionalPercentuali * MathUtils.round(optionalTariffa.getImporto().doubleValue() * 0.01, 5);

                        MROldPianoDeiConti contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabile().getId());

                        for (int i = 0; i < molteplicita; i++) {
                            MROldRigaDocumentoFiscale riga = creaRigaOptional(
                                    optional,
                                    quantita,
                                    prezzo,
                                    null,
                                    false,
                                    false,
                                    codiceIvaImponibile,
                                    codiceIvaNonSoggetto,
                                    contoContabile);

                            if (riga != null) {
                                righeFattura.add(riga);
                            }
                        }
                    }
                }



            }
        }
        return righeFattura;
    }
//    public static List aggiungiRigheOptionalsPercentuali(
//            Session sx,
//            List righeFattura,
//            MROldTariffa tariffa,
//            boolean extraPrePay)
//            throws HibernateException {
//
//
//        if (tariffa.getOptionalsTariffa() != null && righeFattura.size() > 0) {
//
//            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
//
//            if (tariffa.getCodiceIvaExtraPrepay()!= null &&  extraPrePay)
//                codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaExtraPrepay().getId());
//
//
//            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());
//
//
//            //Aggiungiamo gli optionals percentuali solo se abbiamo le righe :-)
//            if (righeFattura.size() > 0) {
//                //Aggiungiamo optionals percentuali.
//                Iterator it = tariffa.getOptionalsTariffa().values().iterator();
//                Iterator itForOneri = tariffa.getOptionalsTariffa().values().iterator();
//
//
//                double totaleImponibileRighe = 0.0;
//                double totaleImponibileKmInclusi = 0.0;
//                double totaleTempoKm = 0;
//
//                double totaleImponibileOptionalInclusi = 0.0;
//                double totalePercentualeOptionalInclusi = 0.0;
//                double aliquotaIva = 0;
//
//                double y5 = 0.0, y4 = 0.0, y3 = 0.0, y2 = 0.0, y1 = 0.0;
//                /**
//                 * y5 è totaleOptionalFissiEGiornalieriConIvaApplicabile
//                 * y4 è OptionalFissiEGiornalieriSenzaIvaApplicabile
//                 * y3 è totaleQuotePercentualiConIvaApplicabile
//                 * y2totaleQuotePercentualiSenzaIvaApplicabile
//                 * y1 oneri aeroportuali
//                 * y0 importo tariffa
//                 */
//                double totaleImponibileRigheConIvaApplicabile = 0.0;
//
//                List<MROldOptional> optionalInclusi = new ArrayList<MROldOptional>();
//
//                //to do calcolo optional inclusi
//
//                for (int i = 0; i < righeFattura.size(); i++) {
//                    MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(i);
//                    /*
//                     * modifica per jarkko
//                     */
//                    if (tmpRiga.getTempoKm() || tmpRiga.getTempoExtra()) {
//                        /*
//                         * fine jarko
//                         */
//
//                        totaleImponibileRighe = MathUtils.round(totaleImponibileRighe + tmpRiga.getTotaleImponibileRiga().doubleValue());
//                        aliquotaIva = tmpRiga.getCodiceIva().getAliquota();
//                    } else {
//                        if (tmpRiga.getOptional() != null && !tmpRiga.getFranchigia() && !tmpRiga.isRigaDescrittiva()) {
//                            if (optionalInclusi.contains(tmpRiga.getOptional())) {
//                                if (!tmpRiga.getOptional().getImportoPercentuale() && tmpRiga.getTotaleIvaRiga() > 0) {
//                                    y5 += tmpRiga.getTotaleImponibileRiga(); //totaleOptionalFissiEGiornalieriConIvaApplicabile
//                                } else if (!tmpRiga.getOptional().getImportoPercentuale() && tmpRiga.getTotaleIvaRiga() == 0) {
//                                    y4 += tmpRiga.getTotaleImponibileRiga(); //OptionalFissiEGiornalieriSenzaIvaApplicabile
//                                }
//                            }
//                        }
//
//                    }
//
//                }
//
//                totaleTempoKm = totaleImponibileRighe; // corrisponde a variabile d della formula
//
//
//                //importoTariffa = ((1) * (y5 * (1 - y3) + totaleTempoKm) * 1 / (1 - y3)) * (1) -y4;
//
//                double importoTariffaAnteOptionalPercentuali = ( (1) * (y5 * (1 - y3) + totaleTempoKm) * 1/ (1 - y3) ) * ( 1 ) + y4;
//                double importoTariffaAnteOneri = ((1 + y1) * (y5 * (1 - y3) + totaleTempoKm) * 1 / (1 - y3)) * (1);
//
//                System.out.println("\n\n *** Importo tariffa Calcolato: ");
//
//                /***
//                 * Cerco MROldOptional Percentuali tranne gli oneri
//                 */
//                while (it.hasNext()) {
//                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
//
//                    MROldOptional optional = optionalTariffa.getOptional();
//                    if (optionalTariffa.getIncluso() && optionalTariffa.getSelezionato() && optional.getImportoPercentuale() && !optional.getTempoExtra() && !optional.getOneriAeroportuali() && !optional.getOneriFeroviari()) {
//
//                        double molteplicita = 1.0;
//                        if (optionalTariffa.getQuantita() != null) {
//                            molteplicita = optionalTariffa.getQuantita();
//                        }
//
//                        double quantita = MathUtils.round(optionalTariffa.getImporto().doubleValue() * 0.01, 5);
//
//
//                        /**
//                         *
//                         * Bisogna assegnare il prezzo uguale a importoTariffa se l'optional è incluso
//                         *
//                         */
//                        double prezzo = totaleImponibileRighe;
//
//
//                        if (optionalTariffa.getIncluso()) {
//                            prezzo = importoTariffaAnteOptionalPercentuali -y5;
//                        }
//
//
//                        System.out.println("\n\n *** Importo di applicazione MROldOptional Incluso: " + prezzo);
//
//                        MROldPianoDeiConti contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabile().getId());
//
//                        for (int i = 0; i < molteplicita; i++) {
//                            MROldRigaDocumentoFiscale riga = creaRigaOptional(
//                                    optional,
//                                    quantita,
//                                    prezzo,
//                                    null,
//                                    false,
//                                    false,
//                                    codiceIvaImponibile,
//                                    codiceIvaNonSoggetto,
//                                    contoContabile);
//
//                            if (riga != null) {
//                                righeFattura.add(riga);
//                            }
//                        }
//                    }
//                }
//
//                /**
//                 * Mi calcolo il totale dell'imponibile con IVA Applicabile per gli oneri
//                 *
//                 */
//                for (int i = 0; i < righeFattura.size(); i++) {
//                    MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(i);
//                    /*
//                     * modifica per jarkko
//                     */
//                    if (tmpRiga.getTotaleIvaRiga() != 0.0) {
//                        /*
//                         * fine jarko
//                         */
//                        totaleImponibileRigheConIvaApplicabile += MathUtils.round(tmpRiga.getTotaleImponibileRiga().doubleValue());
//                    }
//
//
//                }
//
//
//                /***
//                 * Cerco Gli Oneri tra gli optional Percentuali
//                 *
//                 *
//                 */
//                while (itForOneri.hasNext()) {
//                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) itForOneri.next();
//
//                    MROldOptional optional = optionalTariffa.getOptional();
//                    if (optionalTariffa.getIncluso() && optionalTariffa.getSelezionato() && optional.getImportoPercentuale() && !optional.getTempoExtra() && (optional.getOneriAeroportuali() || optional.getOneriFeroviari())) {
//
//                        double molteplicita = 1.0;
//                        if (optionalTariffa.getQuantita() != null) {
//                            molteplicita = optionalTariffa.getQuantita();
//                        }
//
//                        double quantita = 1;
//
//                        double prezzo =  importoTariffaAnteOptionalPercentuali * MathUtils.round(optionalTariffa.getImporto().doubleValue() * 0.01, 5);
//
//                        MROldPianoDeiConti contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabile().getId());
//
//                        for (int i = 0; i < molteplicita; i++) {
//                            MROldRigaDocumentoFiscale riga = creaRigaOptional(
//                                    optional,
//                                    quantita,
//                                    prezzo,
//                                    null,
//                                    false,
//                                    false,
//                                    codiceIvaImponibile,
//                                    codiceIvaNonSoggetto,
//                                    contoContabile);
//
//                            if (riga != null) {
//                                righeFattura.add(riga);
//                            }
//                        }
//                    }
//                }
//
//
//
//            }
//        }
//        return righeFattura;
//    }

    private static List aggiungiRigheOptionalsFranchigie(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa)
            throws HibernateException {

        if (tariffa.getOptionalsTariffa() != null) {

            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());

            //Aggiungiamo le franchigie in caso di danno.
            Iterator it = tariffa.getOptionalsTariffa().values().iterator();

            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalTariffa.getOptional();
                if (!optionalTariffa.getIncluso() && optionalTariffa.getSelezionatoFranchigia() && optionalTariffa.getFranchigia() != null) {

                    double quantita = 1.0;

                    double prezzo = optionalTariffa.getFranchigia();

                    MROldPianoDeiConti contoContabileFranchigia = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabileFranchigia().getId());

                    MROldRigaDocumentoFiscale riga = creaRigaOptional(
                            optional,
                            quantita,
                            prezzo,
                            null,
                            false,
                            true,
                            codiceIvaImponibile,
                            codiceIvaNonSoggetto,
                            contoContabileFranchigia);

                    if (riga != null) {
                        righeFattura.add(riga);
                    }
                }
            }
        }
        return righeFattura;
    }

    private static List aggiungiRigaCarburante(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Integer combustibileMancante,
            MROldCarburante carburante) {
        if (combustibileMancante != null
                && combustibileMancante.intValue() > 0
                && carburante != null
                && carburante.getImportoUnitario() != null
                && carburante.getImportoUnitario().doubleValue() > 0) {


            MROldPianoDeiConti sottocontoCarburante = null;
            MROldCodiciIva codiceIva = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());

            if (carburante != null) {
                carburante = (MROldCarburante) sx.get(MROldCarburante.class, carburante.getId());
                if (carburante.getContoRicavo() != null) {
                    sottocontoCarburante = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, carburante.getContoRicavo().getId());
                }
            }
            if (sottocontoCarburante == null) {
                sottocontoCarburante = leggiCodiceSottocontoCarburante(sx);
            }
            double litriMancanti = combustibileMancante.doubleValue();
            double prezzoUnitario = MathUtils.round(MathUtils.scorporoIva(codiceIva.getAliquota(), carburante.getImportoUnitario().doubleValue()), 5);
            double totaleImponibile = MathUtils.round(litriMancanti * prezzoUnitario, 5);
            double totaleIva = MathUtils.round(totaleImponibile * codiceIva.getAliquota().doubleValue(), 5);
            double totaleRiga = totaleImponibile + totaleIva;

            MROldRigaDocumentoFiscale tmpRiga = new MROldRigaDocumentoFiscale();
            tmpRiga.setDescrizione(MessageFormat.format(DESCRIZIONE_COMBUSTIBILE_MANCANTE, carburante.toString()));
            tmpRiga.setUnitaMisura(carburante.getUnitaMisura());
            tmpRiga.setQuantita(MathUtils.roundDouble(litriMancanti));
            tmpRiga.setPrezzoUnitario(new Double(prezzoUnitario));
            tmpRiga.setSconto(new Double(0));
            tmpRiga.setScontoFisso(new Double(0));
            tmpRiga.setTotaleImponibileRiga(new Double(totaleImponibile));
            tmpRiga.setTotaleIvaRiga(new Double(totaleIva));

            tmpRiga.setCodiceIva(codiceIva);
            tmpRiga.setCodiceSottoconto(sottocontoCarburante);
            tmpRiga.setTotaleRiga(new Double(totaleRiga));

            tmpRiga.setTempoKm(Boolean.FALSE);
            tmpRiga.setTempoExtra(Boolean.FALSE);
            tmpRiga.setFranchigia(Boolean.FALSE);
            tmpRiga.setCarburante(carburante);

            righeFattura.add(tmpRiga);
        }
        return righeFattura;
    }

//    private static List aggiungiRigheOptionalsGenericDiscount(
//            Session sx,
//            MROldContrattoNoleggio contratto,
//            List righeFattura,
//            MROldTariffa tariffa,
//            Date inizio)
//            throws HibernateException {
//
//
//        if (contratto == null){
//            System.out.println("OptionalGenericDiscount: Contratto is null");
//            return null;
//        }
//
//        if (contratto.getCommissione() == null)
//            return null;
//        else if (contratto.getCommissione().getFonteCommissione()==null){
//                System.out.println("Fonte MROldCommissione is null, idCommissione = "+contratto.getCommissione().getId().toString());
//                return null;
//        }
//        else if (contratto.getCommissione().getFonteCommissione().getPromemoria() == null ){
//                System.out.println("Promemoria in MROldFonteCommissione is null, MROldFonteCommissione = "+contratto.getCommissione().getFonteCommissione().getCodice());
//                return null;
//        }
//
//
//        String annotazioni = contratto.getCommissione().getFonteCommissione().getPromemoria();
//        String[] strOptionals = null;
//        if (annotazioni != null) {
//            strOptionals = annotazioni.split(" #");
//        }
//
//        System.out.println ("*** MROldOptional Inclusi presi da Fonte Commissionabile con REGEX (spazio cancelletto): #");
//        System.out.println(strOptionals);
//
//        Iterator it = tariffa.getOptionalsTariffa().values().iterator();
//
//        Double quantita;
//        MROldPianoDeiConti contoContabile;
//        MROldOptional optional;
//
//        MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
//        MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());
//
//        if (strOptionals != null) {
//            while (it.hasNext()) {
//                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
//                optional = optionalTariffa.getOptional();
//
//                for (String strCode : strOptionals) {
//                    if (optional != null && optional.getCodice().equals(strCode)) {
//                        System.out.println("  Identificato codice da REGEX fonte commissionabile: "+ optional.getCodice());
//
//                        quantita = MathUtils.round(optional.getImporto().doubleValue() * 0.01, 5);
//                        contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabile().getId());
//                        double prezzo = calcolaImponibileRighe(righeFattura);
//
//                        System.out.println("  Prezzo/Imponibile su cui calcolare l'optional: "+new Double(prezzo).toString());
//
//                        MROldRigaDocumentoFiscale riga = creaRigaOptional(
//                                optional,
//                                quantita,
//                                prezzo,
//                                null,
//                                false,
//                                false,
//                                codiceIvaImponibile,
//                                codiceIvaNonSoggetto,
//                                contoContabile);
//
//                        if (riga != null) {
//                            righeFattura.add(riga);
//                            System.out.println("  Aggiungo Riga");
//
//                        }
//                        optionalTariffa.setSelezionato(Boolean.TRUE);
//                        System.out.println("  Seleziono l'optional");
//                        sx.saveOrUpdate(optionalTariffa);
//                        System.out.println("  MROldOptional Generic Discount salvato");
//
//
//                    }
//                }
//            }
//
//        }
//        return righeFattura;
//    }



        /**
         * aggiunge gli optionals early bird - fatto per jarkko
         * @param sx
         * @param righeFattura
         * @param tariffa
         * @param inizio
         * @return
         * @throws HibernateException
         */
        /**
         * aggiunge gli optionals early bird - fatto per jarkko
         * @param sx
         * @param righeFattura
         * @param tariffa
         * @param inizio
         * @return
         * @throws HibernateException
         */
        /**
         * aggiunge gli optionals early bird - fatto per jarkko
         * @param sx
         * @param righeFattura
         * @param tariffa
         * @param inizio
         * @return
         * @throws HibernateException
         */
    private static List aggiungiRigheOptionalsEarlyBird(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Date inizio)
            throws HibernateException {


        if (tariffa.getOptionalsTariffa() != null && righeFattura.size() > 0) {

            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());


            //Aggiungiamo gli optionals percentuali solo se abbiamo le righe :-)
            if (righeFattura.size() > 0) {
                //Aggiungiamo optionals percentuali.
                Iterator it = tariffa.getOptionalsTariffa().values().iterator();
                double totaleImponibileRighe = 0.0;
                totaleImponibileRighe = calcolaImponibileRighe(righeFattura);

                boolean optionalEsiastente = false;
                //optionals early bird
                MROldOptional earlyBird31Ottobre = null;
                MROldOptional earlyBird31Dicembre = null;
                MROldOptional earlybird28Febbraio = null;
                MROldOptional earlyBird31Marzo = null;

                MROldOptional tmpOptional = null;
                double prezzo = totaleImponibileRighe;
                double quantita = 0.0;
                MROldPianoDeiConti contoContabile = null;

                while (it.hasNext()) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();

                    MROldOptional optional = optionalTariffa.getOptional();

                    if (optional.getCodice().equals(EARLY_BIRD_31_OTTOBRE)) {
                        earlyBird31Ottobre = optional;
                        optionalEsiastente = true;
                    } else if (optional.getCodice().equals(EARLY_BIRD_31_DICEMBRE)) {
                        earlyBird31Dicembre = optional;
                        optionalEsiastente = true;
                    } else if (optional.getCodice().equals(EARLY_BIRD_28_FEBBRAIO)) {
                        earlybird28Febbraio = optional;
                        optionalEsiastente = true;
                    } else if (optional.getCodice().equals(EARLY_BIRD_31_MARZO)) {
                        earlyBird31Marzo = optional;
                        optionalEsiastente = true;
                        //break;
                    }
                }

                if (optionalEsiastente) {
                    //determina lòa data di prenotazione e la data di inizio prenotazione per vedere lo sconto da apolicare
                    //per prima cosa verifica se la data della prenotazione e la data di inizio prenotazione sono dello stesso anno
                    Integer annoStipulaPrenotazione = FormattedDate.annoCorrente();
                    Integer annoInizioPrenotazione = FormattedDate.annoCorrente(inizio);

                    //se l'anno di stipula è diverso allora faremo i controlli tra i primi due sconti
                    //altrimenti tra gli altri due
                    Date dataStipula = new Date(); // data di quando si fa la prenotazione

                    if (annoStipulaPrenotazione - annoInizioPrenotazione < 0) {

                        Date ottobre31 = FormattedDate.fineMese(Calendar.OCTOBER, annoStipulaPrenotazione);
                        Date dicembre31 = FormattedDate.fineMese(Calendar.DECEMBER, annoStipulaPrenotazione);

                        if (dataStipula.getTime() < ottobre31.getTime()) {
                            if (earlyBird31Ottobre != null) {
                                quantita = MathUtils.round(earlyBird31Ottobre.getImporto().doubleValue() * 0.01, 5);
                                contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, earlyBird31Ottobre.getContoContabile().getId());
                                tmpOptional = (MROldOptional) sx.get(MROldOptional.class, earlyBird31Ottobre.getId());
                            }
                        } else if (dataStipula.getTime() > ottobre31.getTime() && dataStipula.getTime() <= dicembre31.getTime()) {
                            if (earlyBird31Dicembre != null) {
                                quantita = MathUtils.round(earlyBird31Dicembre.getImporto().doubleValue() * 0.01, 5);
                                contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, earlyBird31Dicembre.getContoContabile().getId());
                                tmpOptional = (MROldOptional) sx.get(MROldOptional.class, earlyBird31Dicembre.getId());
                            }
                        }
                    } else {
                        Date febbraio28 = FormattedDate.fineMese(Calendar.FEBRUARY, annoStipulaPrenotazione);
                        Date marzo31 = FormattedDate.fineMese(Calendar.MARCH, annoStipulaPrenotazione);

                        if (dataStipula.getTime() < febbraio28.getTime()) {
                            if (earlybird28Febbraio != null) {
                                quantita = MathUtils.round(earlybird28Febbraio.getImporto().doubleValue() * 0.01, 5);
                                contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, earlybird28Febbraio.getContoContabile().getId());
                                tmpOptional = (MROldOptional) sx.get(MROldOptional.class, earlybird28Febbraio.getId());
                            }
                        } else if (dataStipula.getTime() > febbraio28.getTime() && dataStipula.getTime() <= marzo31.getTime()) {
                            //if (dataStipula.getTime() <= marzo31.getTime()) {
                            if (earlyBird31Marzo != null) {
                                quantita = MathUtils.round(earlyBird31Marzo.getImporto().doubleValue() * 0.01, 5);
                                contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, earlyBird31Marzo.getContoContabile().getId());
                                tmpOptional = (MROldOptional) sx.get(MROldOptional.class, earlyBird31Marzo.getId());
                            }
                        }
                    }
                }


                if (tmpOptional != null) {
                    it = tariffa.getOptionalsTariffa().values().iterator();
                    MROldOptionalTariffa optionalTariffa = null;
                    while (it.hasNext()) {
                        optionalTariffa = (MROldOptionalTariffa) it.next();
                        MROldOptional optional = optionalTariffa.getOptional();

                        //MROldOptional tmpOptional = (MROldOptional) sx.get(MROldOptional.class, tmpOptional.getId());

                        if (optional.getId().equals(tmpOptional.getId())) {
                            optionalTariffa.setSelezionato(Boolean.TRUE);
                            sx.saveOrUpdate(optionalTariffa);
                            sx.saveOrUpdate(tariffa);
                            break;
                        } else {
                            optionalTariffa = null;
                        }
//                        if (optional.getId().equals(tmpOptional.getId())) {
//                            optionalTariffa.setSelezionato(Boolean.TRUE);
//                            sx.saveOrUpdate(optionalTariffa);
//                            break;
//                        }
                    }


                    if (optionalTariffa != null) {
                        MROldRigaDocumentoFiscale riga = creaRigaOptional(
                                optionalTariffa.getOptional(),
                                quantita,
                                prezzo,
                                null,
                                false,
                                false,
                                codiceIvaImponibile,
                                codiceIvaNonSoggetto,
                                contoContabile);

                        if (riga != null) {
                            righeFattura.add(riga);
                        }
                    }
                }
            }
        }
        return righeFattura;
    }

    /**
     * aggiunge gli optionals long rental - fatto per jarkko
     * @param sx
     * @param righeFattura
     * @param tariffa
     * @param totaleGiorni
     * @param giorniVoucher
     * @return
     * @throws HibernateException
     */
    private static List aggiungiRigheOptionalsLongRental(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            double giorniVoucher)
            throws HibernateException {


        if (tariffa.getOptionalsTariffa() != null && righeFattura.size() > 0) {

            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());


            //Aggiungiamo gli optionals percentuali solo se abbiamo le righe :-)
            if (righeFattura.size() > 0) {
                //Aggiungiamo optionals percentuali.
                Iterator it = tariffa.getOptionalsTariffa().values().iterator();
                double totaleImponibileRighe = 0.0;
                for (int i = 0; i < righeFattura.size(); i++) {
                    MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(i);
                    /*
                     * modifica per jarkko
                     */
                    if (tmpRiga.getTempoKm() || tmpRiga.getTempoExtra()) {
                        /*
                         * fine jarko
                         */
                        totaleImponibileRighe = MathUtils.round(totaleImponibileRighe + tmpRiga.getTotaleImponibileRiga().doubleValue());
                    }
                }

                boolean optionalEssistente = false;
                //optionals early bird
                MROldOptional longRental15_21 = null;
                MROldOptional longRental22_28 = null;
                MROldOptional longRental29_35 = null;
                MROldOptional longRental36_42 = null;
                MROldOptional longRental43_Over = null;

                while (it.hasNext()) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                    MROldOptional optional = optionalTariffa.getOptional();

                    System.out.println("codice = " + optional.getCodice());

                    if (optional.getCodice().equals(LONG_RENTAL_15_21)) {
                        longRental15_21 = (MROldOptional) sx.get(MROldOptional.class, optional.getId());
                        optionalEssistente = true;
                    } else if (optional.getCodice().equals(LONG_RENTAL_22_28)) {
                        longRental22_28 = (MROldOptional) sx.get(MROldOptional.class, optional.getId());
                        optionalEssistente = true;
                    } else if (optional.getCodice().equals(LONG_RENTAL_29_35)) {
                        longRental29_35 = (MROldOptional) sx.get(MROldOptional.class, optional.getId());
                        optionalEssistente = true;
                    } else if (optional.getCodice().equals(LONG_RENTAL_36_42)) {
                        longRental36_42 = (MROldOptional) sx.get(MROldOptional.class, optional.getId());
                        optionalEssistente = true;
                    } else if (optional.getCodice().equals(LONG_RENTAL_43_OVER)) {
                        longRental43_Over = (MROldOptional) sx.get(MROldOptional.class, optional.getId());
                        optionalEssistente = true;
                    }
                }


                double prezzo = totaleImponibileRighe;
                double quantita = 0.0;
                MROldPianoDeiConti contoContabile = null;
                MROldOptional tmpOptional = null;

                if (optionalEssistente) {
                    if (giorniVoucher >= 15.0 && giorniVoucher <= 21.0) {
                        quantita = MathUtils.round(longRental15_21.getImporto().doubleValue() * 0.01, 5);
                        contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, longRental15_21.getContoContabile().getId());
                        tmpOptional = (MROldOptional) sx.get(MROldOptional.class, longRental15_21.getId());
                    } else if (giorniVoucher >= 22.0 && giorniVoucher <= 28.0) {
                        quantita = MathUtils.round(longRental22_28.getImporto().doubleValue() * 0.01, 5);
                        contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, longRental22_28.getContoContabile().getId());
                        tmpOptional = (MROldOptional) sx.get(MROldOptional.class, longRental22_28.getId());
                    } else if (giorniVoucher >= 29.0 && giorniVoucher <= 35.0) {
                        quantita = MathUtils.round(longRental29_35.getImporto().doubleValue() * 0.01, 5);
                        contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, longRental29_35.getContoContabile().getId());
                        tmpOptional = (MROldOptional) sx.get(MROldOptional.class, longRental29_35.getId());
                    } else if (giorniVoucher >= 36.0 && giorniVoucher <= 42.0) {
                        quantita = MathUtils.round(longRental36_42.getImporto().doubleValue() * 0.01, 5);
                        contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, longRental36_42.getContoContabile().getId());
                        tmpOptional = (MROldOptional) sx.get(MROldOptional.class, longRental36_42.getId());
                    } else if (giorniVoucher >= 43.0) {
                        quantita = MathUtils.round(longRental43_Over.getImporto().doubleValue() * 0.01, 5);
                        contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, longRental43_Over.getContoContabile().getId());
                        tmpOptional = (MROldOptional) sx.get(MROldOptional.class, longRental43_Over.getId());
                    }
                }

                if (tmpOptional != null) {
                    MROldRigaDocumentoFiscale riga = creaRigaOptional(
                            tmpOptional,
                            quantita,
                            prezzo,
                            null,
                            false,
                            false,
                            codiceIvaImponibile,
                            codiceIvaNonSoggetto,
                            contoContabile);

                    if (riga != null) {
                        righeFattura.add(riga);
                    }


                    it = tariffa.getOptionalsTariffa().values().iterator();
                    while (it.hasNext()) {
                        MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                        MROldOptional optional = optionalTariffa.getOptional();

                        if (optional.getId().equals(tmpOptional.getId())) {
                            optionalTariffa.setSelezionato(Boolean.TRUE);
                            sx.saveOrUpdate(optionalTariffa);
                            break;
                        }
//                        MROldOptional tmpOptional = (MROldOptional) sx.get(MROldOptional.class, tmpOptionalTariffa.getOptional().getId());
//
//                        if (optional.getId().equals(tmpOptional.getId())) {
//                            optionalTariffa.setSelezionato(Boolean.TRUE);
//                            sx.saveOrUpdate(optionalTariffa);
//                            break;
//                        }
                    }
                }
            }
        }
        return righeFattura;
    }

    public static String esisteEarlyBird(Session sx, MROldTariffa tariffa, Date inizio) throws HibernateException {
        if (tariffa.getOptionalsTariffa() != null) {
            //Aggiungiamo gli optionals percentuali solo se abbiamo le righe :-)
            //Aggiungiamo optionals percentuali.
            Iterator it = tariffa.getOptionalsTariffa().values().iterator();

            boolean optionalEsiastente = false;
            //optionals early bird
            MROldOptional earlyBird31Ottobre = null;
            MROldOptional earlyBird31Dicembre = null;
            MROldOptional earlybird28Febbraio = null;
            MROldOptional earlyBird31Marzo = null;

            MROldOptional tmpOptional = null;

            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();

                MROldOptional optional = optionalTariffa.getOptional();

                if (optional.getCodice().equals(EARLY_BIRD_31_OTTOBRE)) {
                    earlyBird31Ottobre = optional;
                    optionalEsiastente = true;
                } else if (optional.getCodice().equals(EARLY_BIRD_31_DICEMBRE)) {
                    earlyBird31Dicembre = optional;
                    optionalEsiastente = true;
                } else if (optional.getCodice().equals(EARLY_BIRD_28_FEBBRAIO)) {
                    earlybird28Febbraio = optional;
                    optionalEsiastente = true;
                } else if (optional.getCodice().equals(EARLY_BIRD_31_MARZO)) {
                    earlyBird31Marzo = optional;
                    optionalEsiastente = true;
                }
            }

            if (optionalEsiastente) {
                //determina lòa data di prenotazione e la data di inizio prenotazione per vedere lo sconto da apolicare
                //per prima cosa verifica se la data della prenotazione e la data di inizio prenotazione sono dello stesso anno
                Integer annoStipulaPrenotazione = FormattedDate.annoCorrente();
                Integer annoInizioPrenotazione = FormattedDate.annoCorrente(inizio);

                //se l'anno di stipula è diverso allora faremo i controlli tra i primi due sconti
                //altrimenti tra gli altri due
                Date dataStipula = new Date(); // data di quando si fa la prenotazione

                if (annoStipulaPrenotazione - annoInizioPrenotazione < 0) {

                    Date ottobre31 = FormattedDate.fineMese(Calendar.OCTOBER, annoStipulaPrenotazione);
                    Date dicembre31 = FormattedDate.fineMese(Calendar.DECEMBER, annoStipulaPrenotazione);

                    if (dataStipula.getTime() < ottobre31.getTime()) {
                        if (earlyBird31Ottobre != null) {
                            tmpOptional = (MROldOptional) sx.get(MROldOptional.class, earlyBird31Ottobre.getId());
                        }
                    } else if (dataStipula.getTime() > ottobre31.getTime() && dataStipula.getTime() <= dicembre31.getTime()) {
                        if (earlyBird31Dicembre != null) {
                            tmpOptional = (MROldOptional) sx.get(MROldOptional.class, earlyBird31Dicembre.getId());
                        }
                    }
                } else {
                    Date febbraio28 = FormattedDate.fineMese(Calendar.FEBRUARY, annoStipulaPrenotazione);
                    Date marzo31 = FormattedDate.fineMese(Calendar.MARCH, annoStipulaPrenotazione);

                    if (dataStipula.getTime() < febbraio28.getTime()) {
                        if (earlybird28Febbraio != null) {
                            tmpOptional = (MROldOptional) sx.get(MROldOptional.class, earlybird28Febbraio.getId());
                        }
                    } else if (dataStipula.getTime() > febbraio28.getTime() && dataStipula.getTime() <= marzo31.getTime()) {
                        //if (dataStipula.getTime() <= marzo31.getTime()) {
                        if (earlyBird31Marzo != null) {
                            tmpOptional = (MROldOptional) sx.get(MROldOptional.class, earlyBird31Marzo.getId());
                        }
                    }
                }

                if (tmpOptional != null) {
                    return tmpOptional.getCodice();
                }
            }

        }

        return null;
    }

    public static String esisteLongRental(Session sx, MROldTariffa tariffa, double giorniVoucher) throws HibernateException {
        if (tariffa.getOptionalsTariffa() != null) {

            //Aggiungiamo gli optionals percentuali solo se abbiamo le righe :-)
            //Aggiungiamo optionals percentuali.
            Iterator it = tariffa.getOptionalsTariffa().values().iterator();

            boolean optionalEssistente = false;
            //optionals early bird
            MROldOptional longRental15_21 = null;
            MROldOptional longRental22_28 = null;
            MROldOptional longRental29_35 = null;
            MROldOptional longRental36_42 = null;
            MROldOptional longRental43_Over = null;

            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalTariffa.getOptional();

                if (optional.getCodice().equals(LONG_RENTAL_15_21)) {
                    longRental15_21 = (MROldOptional) sx.get(MROldOptional.class, optional.getId());
                    optionalEssistente = true;
                } else if (optional.getCodice().equals(LONG_RENTAL_22_28)) {
                    longRental22_28 = (MROldOptional) sx.get(MROldOptional.class, optional.getId());
                    optionalEssistente = true;
                } else if (optional.getCodice().equals(LONG_RENTAL_29_35)) {
                    longRental29_35 = (MROldOptional) sx.get(MROldOptional.class, optional.getId());
                    optionalEssistente = true;
                } else if (optional.getCodice().equals(LONG_RENTAL_36_42)) {
                    longRental36_42 = (MROldOptional) sx.get(MROldOptional.class, optional.getId());
                    optionalEssistente = true;
                } else if (optional.getCodice().equals(LONG_RENTAL_43_OVER)) {
                    longRental43_Over = (MROldOptional) sx.get(MROldOptional.class, optional.getId());
                    optionalEssistente = true;
                }
            }

            MROldOptional tmpOptional = null;

            if (optionalEssistente) {
                if (giorniVoucher >= 15.0 && giorniVoucher <= 21.0) {
                    tmpOptional = (MROldOptional) sx.get(MROldOptional.class, longRental15_21.getId());
                } else if (giorniVoucher >= 22.0 && giorniVoucher <= 28.0) {
                    tmpOptional = (MROldOptional) sx.get(MROldOptional.class, longRental22_28.getId());
                } else if (giorniVoucher >= 29.0 && giorniVoucher <= 35.0) {
                    tmpOptional = (MROldOptional) sx.get(MROldOptional.class, longRental29_35.getId());
                } else if (giorniVoucher >= 36.0 && giorniVoucher <= 42.0) {
                    tmpOptional = (MROldOptional) sx.get(MROldOptional.class, longRental36_42.getId());
                } else if (giorniVoucher >= 43.0) {
                    tmpOptional = (MROldOptional) sx.get(MROldOptional.class, longRental43_Over.getId());
                }

                if (tmpOptional != null) {
                    return tmpOptional.getCodice();
                }
            }
        }

        return null;
    }

    public static Object[] creaCastellettoIva(List righeDocumentoFiscale) {

        HashMap map = new HashMap();
        if (righeDocumentoFiscale != null) {
            Iterator it = righeDocumentoFiscale.iterator();
            while (it.hasNext()) {
                MROldRigaDocumento tmpRiga = (MROldRigaDocumento) it.next();
                if (tmpRiga.getCodiceIva() != null) {
                    CastellettoIva castelletto = null;
                    if (!map.containsKey(tmpRiga.getCodiceIva())) {
                        map.put(tmpRiga.getCodiceIva(), new CastellettoIva(tmpRiga.getCodiceIva()));
                    }
                    castelletto = (CastellettoIva) map.get(tmpRiga.getCodiceIva());

                    double imponibile = castelletto.getImponibile().doubleValue() + tmpRiga.getTotaleImponibileRiga().doubleValue();

                    castelletto.setImponibile(new Double(imponibile));
                }
            }
        }

        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            CastellettoIva castelletto = (CastellettoIva) it.next();
            castelletto.setImponibile(MathUtils.roundDouble(castelletto.getImponibile().doubleValue()));
            castelletto.setImposta(MathUtils.roundDouble(castelletto.getImponibile().doubleValue() * castelletto.getCodiceIva().getAliquota().doubleValue()));
        }

        return map.values().toArray();
    }

    /**
     *Calcola i totali delle righe.
     *@return Array di Double contenente:
     *<br> 0 - imponibile scontato
     *<br> 1 - iva
     *<br> 2 - imponibile scontato + iva = totale
     *<br> 3 - imponibile non scontato
     */
    public static Double[] calcolaImportiRigheFattura(List righeFattura) {
        double tmpTotaleIva = 0.0, tmpTotaleImponibile = 0.0;
        Object[] castelletto = creaCastellettoIva(righeFattura);
        for (int i = 0; i < castelletto.length; i++) {
            CastellettoIva aCastelletto = (CastellettoIva) castelletto[i];
            tmpTotaleImponibile = tmpTotaleImponibile + aCastelletto.getImponibile().doubleValue();
            tmpTotaleIva = tmpTotaleIva + aCastelletto.getImposta().doubleValue();
        }
        double tmpTotaleDocumento = tmpTotaleImponibile + tmpTotaleIva;
        double imponibileScontato = 0;
        if (righeFattura != null && righeFattura.size() > 0) {
            for (int iR = 0; iR < righeFattura.size(); iR++) {
                MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(iR);
                if (!tmpRiga.isRigaDescrittiva()) {
                    imponibileScontato = imponibileScontato + tmpRiga.getTotaleImponibileRiga().doubleValue();
                }
            }
        }
        Double importi[] = new Double[]{null, null, null, null};
        importi[0] = MathUtils.roundDouble(imponibileScontato);
        importi[1] = MathUtils.roundDouble(tmpTotaleIva);
        importi[2] = MathUtils.roundDouble(tmpTotaleDocumento);
        importi[3] = MathUtils.roundDouble(tmpTotaleImponibile);
        return importi;
    }

    /**
     *Una riga puo essere scontata se:
     *<br> 1. sconto != null
     *<br> 2. imponibile != null e imponibile >= sconto
     *<br> 3. codice iva != null
     */
    private static boolean isScontabile(MROldRigaDocumentoFiscale tmpRiga, Double sconto) {
        return sconto != null && isScontabile(tmpRiga, sconto.doubleValue());
    }

    /**
     *Una riga puo essere scontata se:
     *<br> 1. imponibile != null e imponibile >= sconto
     *<br> 2. codice iva != null
     */
    private static boolean isScontabile(MROldRigaDocumentoFiscale tmpRiga, double sconto) {
        return tmpRiga.getTotaleImponibileRiga() != null && tmpRiga.getCodiceIva() != null && tmpRiga.getTotaleImponibileRiga().doubleValue() > sconto && sconto > 0;
    }

    private static void applicaScontoRiga(MROldRigaDocumentoFiscale tmpRiga, Double sconto) {
        applicaScontoRiga(tmpRiga, sconto.doubleValue());
    }

    private static void applicaScontoRiga(MROldRigaDocumentoFiscale tmpRiga, double sconto) {
        tmpRiga.setTotaleImponibileRiga(MathUtils.roundDouble(tmpRiga.getTotaleImponibileRiga().doubleValue() - sconto, 5));
        tmpRiga.setTotaleIvaRiga(MathUtils.roundDouble(tmpRiga.getCodiceIva().getAliquota().doubleValue() * tmpRiga.getTotaleImponibileRiga().doubleValue(), 5));
        tmpRiga.setTotaleRiga(new Double(tmpRiga.getTotaleImponibileRiga().doubleValue() + tmpRiga.getTotaleIvaRiga().doubleValue()));
        if (tmpRiga.getScontoFisso() != null) {
            sconto = sconto + tmpRiga.getScontoFisso().doubleValue();
        }
        tmpRiga.setScontoFisso(new Double(sconto));
    }

    public static List applicaScontoPesatoPerRiga(List righeFattura, Double sconto) {
        if (righeFattura != null && sconto != null) {
            double totaleImponibile = calcolaImportiRigheFattura(righeFattura)[0].doubleValue();
            double quota = sconto.doubleValue() / totaleImponibile;
            double resto = sconto.doubleValue();
            int index = 0;
            while (resto > 0 && index < righeFattura.size()) {
                MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(index++);
                if (tmpRiga.getTotaleImponibileRiga() != null) {
                    double scontoProporzionale = tmpRiga.getTotaleImponibileRiga().doubleValue() * quota;
                    //scontoProporzionale < imponibile riga.
                    double scontoApplicato;
                    if (scontoProporzionale > resto) {
                        scontoApplicato = resto;
                    } else {
                        double arrotondamento;
                        if (scontoProporzionale != Math.floor(scontoProporzionale)) {
                            // Lo sconto proporzionale non e' intero. Proviamo ad arrotondarlo per eccesso.
                            arrotondamento = Math.floor(scontoProporzionale) + 1d;
                        } else {
                            // MROldSconto proporzionale intero. Andiamo avanti cosi.
                            arrotondamento = scontoProporzionale;
                        }
                        //Minimizziamo al resto.
                        if (arrotondamento > resto) {
                            arrotondamento = resto;
                        }

                        if (isScontabile(tmpRiga, arrotondamento)) {
                            // MROldSconto proporzionale intero o riga scontabile dello sconto proporzionale 'arrotondato per eccesso.
                            scontoApplicato = arrotondamento;
                        } else {
                            // Arrotondiamo per diffetto.
                            double scontoProporzionaleIntero = Math.floor(scontoProporzionale);
                            if (isScontabile(tmpRiga, scontoProporzionaleIntero)) {
                                scontoApplicato = scontoProporzionaleIntero;
                            } else if (isScontabile(tmpRiga, scontoProporzionaleIntero - 1.0)) {
                                scontoApplicato = scontoProporzionaleIntero - 1.0;
                            } else {
                                scontoApplicato = -1;
                            }
                        }
                    }
                    if (scontoApplicato > 0) {
                        applicaScontoRiga(tmpRiga, scontoApplicato);
                        resto = resto - scontoApplicato;
                    }
                }
            }
            if (resto > 0) {
                applicaScontoMassimoPerRiga(righeFattura, new Double(resto));
            }
        }
        return righeFattura;
    }

    public static List applicaScontoMassimoPerRiga(List righeFattura, Double sconto) {
        if (righeFattura != null && sconto != null) {
            double resto = sconto.doubleValue();
            int index = 0;
            while (resto > 0 && index < righeFattura.size()) {
                MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(index++);
                if (tmpRiga.getTotaleImponibileRiga() != null && tmpRiga.getCodiceIva() != null) {
                    if (isScontabile(tmpRiga, resto)) {
                        applicaScontoRiga(tmpRiga, resto);
                        resto = 0;
                    } else if (isScontabile(tmpRiga, tmpRiga.getTotaleImponibileRiga().doubleValue() - 1.0)) {
                        resto = resto - (tmpRiga.getTotaleImponibileRiga().doubleValue() - 1.0);
                        applicaScontoRiga(tmpRiga, tmpRiga.getTotaleImponibileRiga().doubleValue() - 1.0);
                    }
                }
            }
        }
        return righeFattura;
    }

    private static Double calcolaScontoRigheFattura(List righeFattura) {
        Double sconto = null;
        if (righeFattura != null && righeFattura.size() > 0) {
            double tmpSconto = 0;
            double imponibile = 0;
            for (int iR = 0; iR < righeFattura.size(); iR++) {
                MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(iR);
                imponibile = tmpRiga.getPrezzoUnitario().doubleValue() * tmpRiga.getQuantita().doubleValue();
                if (tmpRiga.getSconto() != null && !tmpRiga.getSconto().equals(new Double(0))) {
                    tmpSconto = tmpSconto + imponibile * (tmpRiga.getSconto().doubleValue() * 0.01);
                }
            }
            sconto = MathUtils.roundDouble(tmpSconto);
        }
        return sconto;
    }

    private static List estraiPercentualiScontoRigheFattura(List righeFattura) {
        ArrayList percentuali = null;
        if (righeFattura != null && righeFattura.size() > 0) {
            percentuali = new ArrayList();
            for (int iR = 0; iR < righeFattura.size(); iR++) {
                MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFattura.get(iR);
                if (tmpRiga.getSconto() != null && !tmpRiga.getSconto().equals(new Double(0)) && !percentuali.contains(tmpRiga.getSconto())) {
                    percentuali.add(tmpRiga.getSconto());
                }
            }
            percentuali.trimToSize();
        }
        return percentuali;
    }

    public static MROldDocumentoFiscale creaAnteprimaDocumentoFiscale(Session sx, Integer idContratto) {
        MROldContrattoNoleggio contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
        MROldDocumentoFiscale documentoFiscale = new MROldDocumentoFiscale();
        try {
            documentoFiscale.setFatturaRighe(FatturaUtils.calcolaRighe(
                    sx,
                    contrattoNoleggio,
                    contrattoNoleggio.getTariffa(),
                    contrattoNoleggio.getInizio(),
                    contrattoNoleggio.getFine(),
                    null,
                    null,
                    null,
                    contrattoNoleggio.getScontoTariffa(),
                    contrattoNoleggio.getCommissione().getGiorniVoucher(),null));
            for (int i = 0; i < documentoFiscale.getFatturaRighe().size(); i++) {
                MROldRigaDocumentoFiscale riga = (MROldRigaDocumentoFiscale) documentoFiscale.getFatturaRighe().get(i);
                riga.setFattura(documentoFiscale);
            }
            Double[] totali = calcolaImportiRigheFattura(documentoFiscale.getFatturaRighe());
            documentoFiscale.setTotaleImponibile(totali[0]);
            documentoFiscale.setTotaleIva(totali[1]);
            documentoFiscale.setTotaleFattura(totali[2]);
        } catch (TariffaNonValidaException tex) {
            documentoFiscale.setFatturaRighe(new ArrayList());
            documentoFiscale.setTotaleImponibile(0.0);
            documentoFiscale.setTotaleIva(0.0);
            documentoFiscale.setTotaleFattura(0.0);
        }
        return documentoFiscale;
    }

    public static void calcolaDifferenzaDaFatturareOld(List righeFatturePrecedenti, List righeFatturaTotale) {
        Iterator totali = righeFatturaTotale.iterator();
        List righeNuove = new ArrayList();
        while (totali.hasNext()) {
            MROldRigaDocumentoFiscale aRigaTotale = (MROldRigaDocumentoFiscale) totali.next();
            List righePrecedenti = new ArrayList();
            Iterator precedenti = righeFatturePrecedenti.iterator();
            while (precedenti.hasNext()) {
                MROldRigaDocumentoFiscale aRigaPrecedente = (MROldRigaDocumentoFiscale) precedenti.next();
                if (new EqualsBuilder().append(aRigaTotale.getTempoKm(), aRigaPrecedente.getTempoKm()).
                        append(aRigaTotale.getTempoExtra(), aRigaPrecedente.getTempoExtra()).
                        append(aRigaTotale.getOptional(), aRigaPrecedente.getOptional()).
                        append(aRigaTotale.getFranchigia(), aRigaPrecedente.getFranchigia()).
                        append(aRigaTotale.getCarburante(), aRigaPrecedente.getCarburante()).
                        isEquals()) {
                    //TODO Aggiungere una preferenza per i rimborsi, o gestire con note credito il rimborso delle voci non usufruite.
                    righePrecedenti.add(aRigaPrecedente);
                }
            }
            Double[] totaliRighePrecedenti = calcolaImportiRigheFattura(righePrecedenti);
            Double imponibilePrecedente = totaliRighePrecedenti[0];
            Double imponibileTotale = MathUtils.round(aRigaTotale.getTotaleImponibileRiga());
            if (imponibilePrecedente >= imponibileTotale) {
                totali.remove();
            } else if (imponibilePrecedente < aRigaTotale.getTotaleImponibileRiga()) {
                Object[] castelletto = creaCastellettoIva(righePrecedenti);
                for (int i = 0; i < castelletto.length; i++) {
                    CastellettoIva iva = (CastellettoIva) castelletto[i];
                    if (iva.getImponibile() != 0.0) {
                        MROldRigaDocumentoFiscale aRigaSconto = new MROldRigaDocumentoFiscale();
                        aRigaSconto.setCarburante(aRigaTotale.getCarburante());
                        aRigaSconto.setCodiceIva(iva.getCodiceIva());
                        aRigaSconto.setCodiceSottoconto(aRigaTotale.getCodiceSottoconto());
                        aRigaSconto.setDescrizione(aRigaTotale.getDescrizione() + " (ADDEBITI PRECEDENTI)");
                        aRigaSconto.setFranchigia(aRigaTotale.getFranchigia());
                        aRigaSconto.setOptional(aRigaTotale.getOptional());
                        aRigaSconto.setPrezzoUnitario(0.0);
                        aRigaSconto.setQuantita(0.0);
                        aRigaSconto.setSconto(0.0);
                        aRigaSconto.setScontoFisso(iva.getImponibile());
                        aRigaSconto.setTempoExtra(aRigaTotale.getTempoExtra());
                        aRigaSconto.setTempoKm(aRigaTotale.getTempoKm());
                        aRigaSconto.setTotaleImponibileRiga(-iva.getImponibile());
                        aRigaSconto.setTotaleIvaRiga(-iva.getImposta());
                        aRigaSconto.setTotaleRiga(-iva.getImponibile() - iva.getImposta());
                        aRigaSconto.setUnitaMisura(aRigaTotale.getUnitaMisura());
                        righeNuove.add(aRigaSconto);
                    }
                }
            }
        }
        righeFatturaTotale.addAll(righeNuove);
    }

    private static boolean equalsTipoRiga(MROldRigaDocumentoFiscale riga1, MROldRigaDocumentoFiscale riga2) {
        return new EqualsBuilder().append(riga1.getTempoKm() || riga1.getTempoExtra(), riga2.getTempoKm() || riga2.getTempoExtra()).
                append(riga1.getOptional(), riga2.getOptional()).
                append(riga1.getFranchigia(), riga2.getFranchigia()).
                append(riga1.getCarburante(), riga2.getCarburante()).
                isEquals();
    }

    private static boolean isRigaSconto(MROldRigaDocumentoFiscale riga) {
        return riga.getScontoFisso() != null
                && riga.getScontoFisso() > 0
                && riga.getPrezzoUnitario() != null
                && riga.getPrezzoUnitario() == 0.0;
    }

    public static Double quantitaFatturataPrecedentemente(MROldRigaDocumentoFiscale rigaFattura, List<MROldRigaDocumentoFiscale> righeFatturePrecedenti) {
        Double quantitaFatturata = 0.0;
        Iterator<MROldRigaDocumentoFiscale> precedenti = righeFatturePrecedenti.iterator();
        while (precedenti.hasNext()) {
            MROldRigaDocumentoFiscale rigaPrecedente = precedenti.next();
            if (equalsTipoRiga(rigaFattura, rigaPrecedente)) {
                if (rigaPrecedente.getTotaleRiga() < 0 && rigaPrecedente.getPrezzoUnitario() == 0.0) {
                    quantitaFatturata -= rigaPrecedente.getQuantita();
                } else if (rigaPrecedente.getFattura().getTipoDocumento().equals(MROldDocumentoFiscale.NC)) {
                    quantitaFatturata -= rigaPrecedente.getQuantita();
                } else {
                    quantitaFatturata += rigaPrecedente.getQuantita();
                }
                precedenti.remove();
            }
        }
        return quantitaFatturata;
    }

    private static void aggiornaTotaliRiga(MROldRigaDocumentoFiscale riga) {
        Double quantita = MathUtils.round(riga.getQuantita(), 5);
        Double prezzo = MathUtils.round(riga.getPrezzoUnitario(), 5);
        Double scontoPercentuale = MathUtils.round(riga.getSconto(), 5);
        Double imponibile = MathUtils.round(quantita * prezzo * (100.0 - scontoPercentuale) * 0.01, 5);
        Double aliquota = riga.getCodiceIva().getAliquota();
        Double iva = MathUtils.round(imponibile * aliquota, 5);
        Double totale = imponibile + iva;

        riga.setTotaleImponibileRiga(new Double(imponibile));
        riga.setTotaleIvaRiga(new Double(iva));
        riga.setTotaleRiga(new Double(totale));
    }

    public static void calcolaDifferenzaDaFatturare(List<MROldRigaDocumentoFiscale> righeFattureEmesse, List<MROldRigaDocumentoFiscale> righeDaFatturare, List<MROldRigaDocumentoFiscale> righeDaStornare) {
        ArrayList righePrecedenti = new ArrayList(righeFattureEmesse);
        int i = 0;
        while (i < righeDaFatturare.size()) {
            MROldRigaDocumentoFiscale aRigaTotale = (MROldRigaDocumentoFiscale) righeDaFatturare.get(i);
            if (aRigaTotale.getQuantita() != null && aRigaTotale.getQuantita() > 0) {
                Double quantitaFatturata = quantitaFatturataPrecedentemente(aRigaTotale, righePrecedenti);
                if (quantitaFatturata > 0) {
                    int j = i;
                    while (j < righeDaFatturare.size() && quantitaFatturata > 0) {
                        MROldRigaDocumentoFiscale riga = righeDaFatturare.get(j);
                        if (equalsTipoRiga(aRigaTotale, riga)) {
                            if (riga.getQuantita() > quantitaFatturata) {
                                riga.setQuantita(MathUtils.round(riga.getQuantita() - quantitaFatturata, 5));
                                aggiornaTotaliRiga(riga);
                                quantitaFatturata = 0.0;
                            } else if (riga.getQuantita() <= quantitaFatturata) {
                                quantitaFatturata = MathUtils.round(quantitaFatturata - riga.getQuantita(), 5);
                                righeDaFatturare.remove(j);
                                if (j == i) {
                                    i--;
                                    //i-th row was removed. i must not increment.
                                }
                                //j-th row was removed. j must not increment.s
                                continue;
                            }
                        }
                        j++;
                    }
                }
            }
            i++;
        }
    }

    public static void calcolaDifferenzaDaFatturare(List<MROldRigaDocumentoFiscale> righeFatturePrecedenti1, List<MROldRigaDocumentoFiscale> righeFatturaTotale) {
        ArrayList righePrecedenti = new ArrayList(righeFatturePrecedenti1);
        int i = 0;
        while (i < righeFatturaTotale.size()) {
            MROldRigaDocumentoFiscale aRigaTotale = (MROldRigaDocumentoFiscale) righeFatturaTotale.get(i);
            if (aRigaTotale.getQuantita() != null && aRigaTotale.getQuantita() > 0) {
                Double quantitaFatturata = quantitaFatturataPrecedentemente(aRigaTotale, righePrecedenti);
                if (quantitaFatturata > 0) {
                    int j = i;
                    while (j < righeFatturaTotale.size() && quantitaFatturata > 0) {
                        MROldRigaDocumentoFiscale riga = righeFatturaTotale.get(j);
                        if (equalsTipoRiga(aRigaTotale, riga)) {
                            if (riga.getQuantita() > quantitaFatturata) {
                                riga.setQuantita(MathUtils.round(riga.getQuantita() - quantitaFatturata, 5));
                                aggiornaTotaliRiga(riga);
                                quantitaFatturata = 0.0;
                            } else if (riga.getQuantita() <= quantitaFatturata) {
                                quantitaFatturata = MathUtils.round(quantitaFatturata - riga.getQuantita(), 5);
                                righeFatturaTotale.remove(j);
                                if (j == i) {
                                    i--;
                                    //i-th row was removed. i must not increment.
                                }
                                //j-th row was removed. j must not increment.s
                                continue;
                            }
                        }
                        j++;
                    }
                }
            }
            i++;
        }
    }

    public static List calcolaDifferenzaDaFatturare(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) throws TariffaNonValidaException {
        List righeNuove = FatturaUtils.calcolaRighe(
                sx,
                contrattoNoleggio,
                tariffa,
                inizio,
                fine,
                km,
                litri,
                carburante,
                scontoPercentuale,
                giorniVoucher,null);
        if (contrattoNoleggio != null && contrattoNoleggio.getId() != null && righeNuove.size() > 0) {
            List righeFatturePrecedenti = leggiRigheDocumentiFiscaliEmessi(sx, contrattoNoleggio);
            if (righeFatturePrecedenti.size() > 0) {
                FatturaUtils.calcolaDifferenzaDaFatturare(righeFatturePrecedenti, righeNuove);
            }
        }
        return righeNuove;
    }

    public static MROldDocumentoFiscale creaAnteprimaDocumentoFiscaleDaPrenotazione(Session sx, Integer idPrenotazione) {
        MROldPrenotazione prenotazione = (MROldPrenotazione) sx.get(MROldPrenotazione.class, idPrenotazione);
        MROldDocumentoFiscale documentoFiscale = new MROldDocumentoFiscale();

        Date tmpFine = prenotazione.getFine();
        Date tmpDataFine = FormattedDate.extractDate(prenotazione.getFine());

        if (Boolean.TRUE.equals(prenotazione.getTariffa().getOraRientroAttiva()) && prenotazione.getTariffa().getOraRientro() != null) {
            tmpFine = FormattedDate.createTimestamp(tmpDataFine, prenotazione.getTariffa().getOraRientro());
        }


        try {
            documentoFiscale.setFatturaRighe(FatturaUtils.calcolaRighe(
                    sx,
                    null,
                    prenotazione.getTariffa(),
                    prenotazione.getInizio(),
                    tmpFine,
                    null,
                    null,
                    null,
                    prenotazione.getScontoPercentuale(),
                    prenotazione.getCommissione().getGiorniVoucher(),
                    null));

            for (int i = 0; i < documentoFiscale.getFatturaRighe().size(); i++) {
                MROldRigaDocumentoFiscale riga = (MROldRigaDocumentoFiscale) documentoFiscale.getFatturaRighe().get(i);
                riga.setFattura(documentoFiscale);
            }
            Double[] totali = calcolaImportiRigheFattura(documentoFiscale.getFatturaRighe());
            documentoFiscale.setTotaleImponibile(totali[0]);
            documentoFiscale.setTotaleIva(totali[1]);
            documentoFiscale.setTotaleFattura(totali[2]);
        } catch (Exception tex) {
            documentoFiscale.setFatturaRighe(new ArrayList());
            documentoFiscale.setTotaleImponibile(0.0);
            documentoFiscale.setTotaleIva(0.0);
            documentoFiscale.setTotaleFattura(0.0);
        }
        return documentoFiscale;
    }

        public static List<MROldRigaDocumentoFiscale> creaRigheNotaCredito(Session sx, MROldDocumentoFiscale notaCredito, List<MROldRigaDocumentoFiscale> righeFatture) {
        List<MROldRigaDocumentoFiscale> righeNotaCredito = new ArrayList<MROldRigaDocumentoFiscale>();
        Iterator<MROldRigaDocumentoFiscale> precedenti = righeFatture.iterator();
        while (precedenti.hasNext()) {
            MROldRigaDocumentoFiscale rigaFattura = (MROldRigaDocumentoFiscale) sx.get(MROldRigaDocumentoFiscale.class, precedenti.next().getId());
            MROldRigaDocumentoFiscale rigaNotaCredito = new MROldRigaDocumentoFiscale();
            rigaNotaCredito.setCarburante(rigaFattura.getCarburante());
            rigaNotaCredito.setCodiceIva(rigaFattura.getCodiceIva());
            rigaNotaCredito.setCodiceSottoconto(rigaFattura.getCodiceSottoconto());
            rigaNotaCredito.setDescrizione(rigaFattura.getDescrizione());
            rigaNotaCredito.setFattura(notaCredito);
            rigaNotaCredito.setFranchigia(rigaNotaCredito.getFranchigia());
            rigaNotaCredito.setNumeroRigaFattura(righeNotaCredito.size());
            rigaNotaCredito.setOptional(rigaFattura.getOptional());
            rigaNotaCredito.setPrezzoUnitario(rigaFattura.getPrezzoUnitario());
            rigaNotaCredito.setQuantita(rigaFattura.getQuantita());
            rigaNotaCredito.setSconto(rigaFattura.getSconto());
            rigaNotaCredito.setScontoFisso(rigaFattura.getScontoFisso());
            rigaNotaCredito.setTempoExtra(rigaFattura.getTempoExtra());
            rigaNotaCredito.setTempoKm(rigaFattura.getTempoKm());
            rigaNotaCredito.setTotaleImponibileRiga(rigaFattura.getTotaleImponibileRiga());
            rigaNotaCredito.setTotaleIvaRiga(rigaFattura.getTotaleIvaRiga());
            rigaNotaCredito.setTotaleRiga(rigaFattura.getTotaleRiga());
            righeNotaCredito.add(rigaNotaCredito);
        }
        return righeNotaCredito;
    }

    /**
     * Legge dal database tutte le righe di tutti i documenti fiscali non prepagati emessi per questo contratto.
     * @param sx
     * @param contrattoNoleggio Il contratto in causa.
     * @return
     */
    public static List<MROldRigaDocumentoFiscale> leggiRigheDocumentiFiscaliEmessi(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        return sx.createQuery(
                "select r from MROldRigaDocumentoFiscale r "
                + "where r.fattura.contratto = :contratto "
                + "and r.fattura.prepagato = :false ").
                setParameter("contratto", contrattoNoleggio).
                setParameter("false", Boolean.FALSE).
                list();
    }

    /**
     * Legge dal database tutte le righe di tutte le FATTURE emesse per questo contratto, escludendo quindi ricevute fiscali e note credito.
     * @param sx
     * @param contrattoNoleggio Il contratto in causa.
     * @return
     */
    public static List<MROldRigaDocumentoFiscale> leggiRigheFattureEmesse(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        return sx.createQuery(
                "select r from MROldRigaDocumentoFiscale r "
                + "where r.fattura.class = Fattura "
                + "and r.fattura.contratto = :contratto "
                + "and r.fattura.prepagato = :false").
                setParameter("contratto", contrattoNoleggio).
                setParameter("false", Boolean.FALSE).
                list();
    }

    /**
     * Legge dal database tutte le righe di tutti i documenti fiscali non prepagati emessi per questo contratto eccetto l'ultimo.
     * @param sx
     * @param contrattoNoleggio Il contratto in causa.
     * @param fattura L'ultima fattura emessa, da escludere nella ricerca.
     * @return
     */
    public static List<MROldRigaDocumentoFiscale> leggiRigheDocumentiFiscaliPrecedenti(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldDocumentoFiscale fattura) {
        return sx.createQuery(
                "select r from MROldRigaDocumentoFiscale r "
                + "where r.fattura.contratto = :contratto "
                + "and r.fattura.prepagato = :false "
                + "and r.fattura <> :fattura ").
                setParameter("contratto", contrattoNoleggio).
                setParameter("fattura", fattura).
                setParameter("false", Boolean.FALSE).
                list();
    }

     public static MROldCausalePrimanota leggiCausale(Session sx, Integer codice) throws HibernateException {
        MROldCausalePrimanota retValue = null;
        Query queryCausale = sx.createQuery("select c from MROldCausalePrimanota c where c.codice = :codice"); // NOI18N
        queryCausale.setParameter("codice", codice); // NOI18N
        retValue = (MROldCausalePrimanota) queryCausale.uniqueResult();
        return retValue;
    }
    public static void calcolaTotaleNoleggioNoPPay(Session sx, MROldPrenotazione p) throws TariffaNonValidaException {

        return;
    }

    /**
     * Crea una lista di righe da passare al motore di stampa
     */
    public static List creaStampeDocumentiFiscali(Session sx, List documentiFiscali, boolean cartaBianca) throws HibernateException {
        List stampe = null;
        if (documentiFiscali != null && documentiFiscali.size() > 0) {
            stampe = new ArrayList();
            Iterator it = documentiFiscali.listIterator();
            if (cartaBianca) {
                while (it.hasNext()) {
                    MROldDocumentoFiscale myFattura = (MROldDocumentoFiscale) it.next();
                    if(myFattura.getId()!=null){
                        myFattura = (MROldDocumentoFiscale) sx.get(MROldDocumentoFiscale.class, myFattura.getId());
                    }
                    if (myFattura != null && myFattura.getFatturaRighe() != null) {
                        int righe = myFattura.getFatturaRighe().size();
                        for (int i = 0; i < righe; i++) {
                            MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) myFattura.getFatturaRighe().get(i);
                            if (tmpRiga != null) {
                                tmpRiga.setFattura(myFattura);
                                tmpRiga.setNumeroRigaFattura(new Integer(i));
                                stampe.add(tmpRiga);
                            }
                        }
                    }
                }
            } else {
                while (it.hasNext()) {
                    MROldDocumentoFiscale myFattura = (MROldDocumentoFiscale) it.next();
                    //myFattura = (MROldDocumentoFiscale) sx.get(MROldDocumentoFiscale.class, myFattura.getId());
                    stampe.add(new FatturaContrattoAdapter(myFattura));
                }
            }
        }
        return stampe;
    }


    public static List<List<MROldRigaDocumentoFiscale>> calculateRowsUtils(Session sx, Rent2Rent r2r, Date startDate, Date endDate) {

        //sx = HibernateBridge.refreshSessionSX(sx);

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


    public static List<List<MROldRigaDocumentoFiscale>> calculateRowsUtils(Session sx,
                                                                      MROldContrattoNoleggio agreement, MROldTariffa rateToApply, Integer giorniVoucher, boolean withAlreadyInvoiceOdd) {


        List<List<MROldRigaDocumentoFiscale>> rows = new ArrayList<List<MROldRigaDocumentoFiscale>>();
        List defaultRows = new ArrayList();
        List prePaidRows = new ArrayList();

        Integer kmToApply = agreement.getMovimento().getKmFine();

        MROldTariffa pickedRate = null;
        if (rateToApply != null) {
            pickedRate = rateToApply;
        } else {
            pickedRate = agreement.getTariffa();
        }

//        agreement = (ContrattoNoleggio) DatabaseUtils.refreshPersistentInstanceWithSx
//        (sx, ContrattoNoleggio.class, agreement);
        Date inizio = agreement.getMovimento().getInizio();
        Date fine = agreement.getFine();
        Double aSconto = agreement.getScontoTariffa() != null ? agreement.getScontoTariffa() : null;

        Integer aGiorni = null;
        if (giorniVoucher != null && giorniVoucher > 0) {
            aGiorni = giorniVoucher;
        } else if (agreement.getCommissione() != null && agreement.getCommissione().getGiorniVoucher() != null) {
            aGiorni = agreement.getCommissione().getGiorniVoucher();
        }
        MROldMovimentoAuto aMovimento = agreement.getMovimento();
        Integer stateOfFuel = 0;
        if (aMovimento.getCombustibileFine() != null) {
            stateOfFuel = aMovimento.getCombustibileFine();
        }

        MROldCarburante typeOfFuel = aMovimento.getVeicolo().getCarburante();

        try {
            Fatturazione fatturaBreveTermine = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(sx,
                    agreement,
                    pickedRate,
                    inizio,
                    fine,
                    kmToApply,
                    stateOfFuel,
                    typeOfFuel,
                    aSconto,
                    aGiorni);
            if(withAlreadyInvoiceOdd){
                defaultRows = fatturaBreveTermine.calcolaRigheProssimaFattura(sx);
            }
            else{
                defaultRows = fatturaBreveTermine.anteprimaValoreFatture(sx);
            }
        } catch (FatturaVuotaException fex) {
            fex.printStackTrace();
            defaultRows = new ArrayList();
        } catch (TariffaNonValidaException ex) {
            ex.printStackTrace();
        }
        if (aGiorni != null) {
            try {
                Fatturazione fatturaPrePay = FatturazioneFactory.PREPAGATO_FACTORY.getFatturazione(sx,
                        agreement,
                        pickedRate,
                        inizio,
                        fine,
                        0.0, //scontoPercentuale
                        aGiorni);

                if(withAlreadyInvoiceOdd){
                    prePaidRows = fatturaPrePay.calcolaRigheProssimaFattura(sx);
                }
                else{
                    prePaidRows = fatturaPrePay.anteprimaValoreFatture(sx);
                }

            } catch (FatturaVuotaException fvex) {
                prePaidRows = new ArrayList();
            } catch (TariffaNonValidaException ex) {
                ex.printStackTrace();
            }
        }

        rows.add(defaultRows);
        rows.add(prePaidRows);

        return rows;
    }

    /**
     * it cleans the list from duplicate rows
     *
     * @param lista
     * @return
     */
    protected static List cleanRighe(List<MROldRigaDocumentoFiscale> lista) {
        HashSet<MROldRigaDocumentoFiscale> cleaner = new HashSet<MROldRigaDocumentoFiscale>();
        cleaner.addAll(lista);
        lista = new ArrayList();
        for (MROldRigaDocumentoFiscale r : cleaner) {
            lista.add(r);
        }
        return lista;
    }

    protected static List removeEmptyCharges(List<MROldRigaDocumentoFiscale> lista) {
        Iterator<MROldRigaDocumentoFiscale> r = lista.iterator();
        while (r.hasNext()) {
            MROldRigaDocumentoFiscale s = r.next();
            if (s.getTotaleRiga() == 0.0) {
                r.remove();
            }
        }
        return lista;
    }

    public static List creaStampeDocumentiFiscaliEA(Session sx, List documentiFiscali, boolean cartaBianca) throws HibernateException {
        List stampe = new ArrayList();
        if (documentiFiscali != null && documentiFiscali.size() > 0) {

            Iterator it = documentiFiscali.listIterator();
            if (cartaBianca) {
                while (it.hasNext()) {
                    MROldDocumentoFiscale myFattura = (MROldDocumentoFiscale) it.next();
                    myFattura = (MROldDocumentoFiscale) sx.get(MROldDocumentoFiscale.class, myFattura.getId());
                    if(myFattura.getFatturaRighe() == null){
                        myFattura.setFatturaRighe(new ArrayList());
                        MROldRigaDocumentoFiscale rigaDummy0 = new MROldRigaDocumentoFiscale();
                        rigaDummy0.setTotaleRiga(0.0);
                        rigaDummy0.setTotaleIvaRiga(0.0);
                        rigaDummy0.setTotaleImponibileRiga(0.0);
                        myFattura.getFatturaRighe().add(rigaDummy0);
                    }

                    if (myFattura != null && myFattura.getFatturaRighe() != null) {
                        List<MROldRigaDocumentoFiscale> righeFatturaPerStampa = new ArrayList<MROldRigaDocumentoFiscale>();

//

                        MROldRigaDocumentoFiscale nomeCognome = new MROldRigaDocumentoFiscale();
                        nomeCognome.setDescrizione("Cliente: "+myFattura.getCliente().toString());
                        MROldRigaDocumentoFiscale numeroContratto = new MROldRigaDocumentoFiscale();
                        numeroContratto.setDescrizione("CDS: EA "+myFattura.getContratto().getNumero());
                        righeFatturaPerStampa.add(nomeCognome);
                        righeFatturaPerStampa.add(numeroContratto);

                        if (myFattura.getTipoDocumento().equals(MROldDocumentoFiscale.FT)) {
                            righeFatturaPerStampa.addAll(creaDescrizioneFatturaSaldo(myFattura));
                            righeFatturaPerStampa = accorpaRigheGiorniExtraConGiorniNoleggio(righeFatturaPerStampa);
                            righeFatturaPerStampa.addAll(accorpaRigheFattureAccontiPerStampaFatturaSaldo(sx, myFattura));
                        }
                        righeFatturaPerStampa.addAll(myFattura.getFatturaRighe());
                        for (int i = 0; i < righeFatturaPerStampa.size(); i++) {
                            MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) righeFatturaPerStampa.get(i);
                            if(tmpRiga == null){
                                continue;
                            }
                            tmpRiga.setFattura(myFattura);
                            tmpRiga.setNumeroRigaFattura(i);
                            stampe.add(tmpRiga);
                        }
                    }
                }
            } else {
                while (it.hasNext()) {
                    MROldDocumentoFiscale myFattura = (MROldDocumentoFiscale) it.next();
                    myFattura = (MROldDocumentoFiscale) sx.get(MROldDocumentoFiscale.class, myFattura.getId());
                    stampe.add(new FatturaContrattoAdapter(myFattura));
                }
            }
        }
        return stampe;
    }

    private static List<MROldRigaDocumentoFiscale> creaDescrizioneFatturaSaldo(MROldDocumentoFiscale myFattura) {
        List<MROldRigaDocumentoFiscale> response = new ArrayList<MROldRigaDocumentoFiscale>();
        //first name and surname//
//        RigaDocumentoFiscale nomeCognome = new RigaDocumentoFiscale();
//        nomeCognome.setDescrizione("Cliente: "+myFattura.getCliente().toString());
//        RigaDocumentoFiscale numeroContratto = new RigaDocumentoFiscale();
        MROldRigaDocumentoFiscale dataInizioDataFine = new MROldRigaDocumentoFiscale();
        MROldRigaDocumentoFiscale numeroOrdineEGruppoRichiesto = new MROldRigaDocumentoFiscale();
        List<MROldRigaDocumentoFiscale> targheVeicoli = new ArrayList();

        if(myFattura.getContratto() != null){
            String numeroOrdine = "";
            Locale currentLocale = Locale.getDefault();
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, currentLocale);

            MROldContrattoNoleggio contratto = myFattura.getContratto();
            MROldFonteCommissione fonte =  contratto.getCommissione().getFonteCommissione();
            numeroOrdine = ContrattoUtils.transformCodiceVoucherToNumOrd(contratto.getCommissione().getCodiceVoucher() != null ? contratto.getCommissione().getCodiceVoucher() : "", fonte.getRentalType().getDescription());

//            numeroContratto.setDescrizione("CDS: EA"+myFattura.getContratto().getNumero());

            dataInizioDataFine.setDescrizione("Data inizio: "+df.format(contratto.getInizio()) + "    Data fine: " + df.format(contratto.getFine()));
            numeroOrdineEGruppoRichiesto.setDescrizione("Numero Ordine: "+numeroOrdine + " Gruppo Richiesto: "+contratto.getGruppo1());

            for(MROldMovimentoAuto m : (Set<MROldMovimentoAuto>) contratto.getMovimenti()){
                MROldRigaDocumentoFiscale veicolo = new MROldRigaDocumentoFiscale();
                veicolo.setDescrizione("Targa: " + m.getVeicolo().getTarga());
                targheVeicoli.add(veicolo);
            }
        }
//        response.add(nomeCognome);
//        response.add(numeroContratto);
        response.add(dataInizioDataFine);
        response.add(numeroOrdineEGruppoRichiesto);
        response.addAll(targheVeicoli);


        return response;
    }

    private static List<MROldRigaDocumentoFiscale> accorpaRigheGiorniExtraConGiorniNoleggio(List<MROldRigaDocumentoFiscale> listRighe) {

        ResourceBundle bundleFatt = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");
        String labelGiorniExtra = bundleFatt.getString("FatturaUtils.msgListino0GiorniExtra");
        String labelGiorniTariffa = bundleFatt.getString("FatturaUtils.msgListino0GiorniTariffa");
        labelGiorniExtra = bundleFatt.getString("FatturaUtils.msgListino0GiorniExtra").split("-")[1];
        labelGiorniTariffa = bundleFatt.getString("FatturaUtils.msgListino0GiorniTariffa").split("-")[1];

        MROldRigaDocumentoFiscale giorniExtra = null;
        MROldRigaDocumentoFiscale giorniTariffa = null;

        for (MROldRigaDocumentoFiscale focusRow : listRighe) {

            if (focusRow.getDescrizione() != null) {

                if (focusRow.getDescrizione().contains(labelGiorniExtra)) {

                    giorniExtra = focusRow;
                } else if (focusRow.getDescrizione().contains(labelGiorniTariffa)) {

                    giorniTariffa = focusRow;
                }
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
            listRighe.add(noMoreExtraDaysForMonthlyBilling);
        }
        return listRighe;
    }


    public static List<MROldRigaDocumentoFiscale> accorpaRigheFattureAccontiPerStampaFatturaSaldo(Session sx, MROldDocumentoFiscale doc) {
        List<MROldRigaDocumentoFiscale> result = new ArrayList<MROldRigaDocumentoFiscale>();
//        result = mergeRowsByVATcode(findRowsAccontiForSelectedSaldoEA(doc));
        result = mergeRowsByRowDescription(findRowsAccontiForSelectedSaldoEA(sx, doc));
//        result = doc.getFatturaRighe();
        for (MROldRigaDocumentoFiscale r : result) {

            r.setTotaleRiga(-r.getTotaleRiga());
            r.setTotaleImponibileRiga(-r.getTotaleImponibileRiga());
            r.setTotaleIvaRiga(-r.getTotaleIvaRiga());


        }

        return result;
    }

    private static List<MROldRigaDocumentoFiscale> mergeRowsByRowDescription(List<MROldRigaDocumentoFiscale> rows) {
        List<MROldRigaDocumentoFiscale> result = new ArrayList<MROldRigaDocumentoFiscale>();
        if (rows != null) {
            for (MROldRigaDocumentoFiscale r : rows) {

                MROldRigaDocumentoFiscale newRiga = new MROldRigaDocumentoFiscale();
                newRiga.setTotaleRiga(r.getTotaleRiga());
                newRiga.setTotaleImponibileRiga(r.getTotaleImponibileRiga());
                newRiga.setTotaleIvaRiga(r.getTotaleIvaRiga());
                newRiga.setCodiceIva(r.getCodiceIva());
                newRiga.setDescrizione(r.getDescrizione());
                result.add(newRiga);
            }

        }


        return result;
    }

    private static List<MROldRigaDocumentoFiscale> findRowsAccontiForSelectedSaldoEA(Session sx, MROldDocumentoFiscale doc) {
        List<MROldDocumentoFiscale> resultAccontoInvoices = findAccontoInvoicesForSelectedSaldoEA(sx, doc);
        List<MROldRigaDocumentoFiscale> righeDiAcconto = new ArrayList();
        if (resultAccontoInvoices != null) {
            for (MROldDocumentoFiscale acconto : resultAccontoInvoices) {
                righeDiAcconto.addAll(acconto.getFatturaRighe());
            }
        }

        return righeDiAcconto;
    }

    public static List<MROldDocumentoFiscale> findAccontoInvoicesForSelectedSaldoEA(Session sx, MROldDocumentoFiscale doc) {

        List<MROldDocumentoFiscale> resultAccontoInvoices = new ArrayList<MROldDocumentoFiscale>();
        MROldPartita partitaSaldo = ContabUtils.leggiPartita(sx, doc);
        if (partitaSaldo != null) {
            List<MROldPartita> partiteAcconto = ContabUtils.leggiPartiteAcconto(sx, partitaSaldo);
            for (MROldPartita p : partiteAcconto) {
                resultAccontoInvoices.add(p.getFattura());
            }
        }

        return resultAccontoInvoices;
    }

    public static MROldRigaDocumentoFiscaleSplitPayment trasformaRigaDocumentoInSplitPayment(MROldRigaDocumentoFiscale riga, MROldCustomerSplitPayment customer) {
        MROldRigaDocumentoFiscaleSplitPayment result = new MROldRigaDocumentoFiscaleSplitPayment();

        try {
            result.setNumeroRigaFattura(riga.getNumeroRigaFattura());
            result.setDescrizione(riga.getDescrizione());
            result.setUnitaMisura(riga.getUnitaMisura());
            result.setQuantita(riga.getQuantita());
            result.setPrezzoUnitario(riga.getPrezzoUnitario());
            result.setSconto(riga.getSconto());
            result.setScontoFisso(riga.getScontoFisso());
            result.setTotaleImponibileRiga(riga.getTotaleImponibileRiga());
            result.setTotaleIvaRiga(riga.getTotaleIvaRiga());
            result.setTotaleRiga(riga.getTotaleRiga());
            result.setCodiceSottoconto(riga.getCodiceSottoconto());
            result.setOptional(riga.getOptional());
            result.setCodiceIva(riga.getCodiceIva());
            result.setCarburante(riga.getCarburante());
            result.setTempoKm(riga.getTempoKm());
            result.setTempoExtra(riga.getTempoExtra());
            result.setFranchigia(riga.getFranchigia());
            result.setVeicolo(riga.getVeicolo());
            result.setCustomer(customer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static MROldRigaDocumentoFiscale trasformaRigaDocumentoSplitPaymentInNormale(MROldRigaDocumentoFiscaleSplitPayment riga) {
        MROldRigaDocumentoFiscale result = new MROldRigaDocumentoFiscale();

        try {
            result.setNumeroRigaFattura(riga.getNumeroRigaFattura());
            result.setDescrizione(riga.getDescrizione());
            result.setUnitaMisura(riga.getUnitaMisura());
            result.setQuantita(riga.getQuantita());
            result.setPrezzoUnitario(riga.getPrezzoUnitario());
            result.setSconto(riga.getSconto());
            result.setScontoFisso(riga.getScontoFisso());
            result.setTotaleImponibileRiga(riga.getTotaleImponibileRiga());
            result.setTotaleIvaRiga(riga.getTotaleIvaRiga());
            result.setTotaleRiga(riga.getTotaleRiga());
            result.setCodiceSottoconto(riga.getCodiceSottoconto());
            result.setOptional(riga.getOptional());
            result.setCodiceIva(riga.getCodiceIva());
            result.setCarburante(riga.getCarburante());
            result.setTempoKm(riga.getTempoKm());
            result.setTempoExtra(riga.getTempoExtra());
            result.setFranchigia(riga.getFranchigia());
            result.setVeicolo(riga.getVeicolo());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
}
