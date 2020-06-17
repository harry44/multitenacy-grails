/*
 * FatturaUtils.java
 *
 * Created on 11 octombrie 2004, 12:46
 */
package it.myrent.ee.api.utils;

import it.myrent.ee.api.preferences.Preferenze;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.TariffaNonValidaException;
import it.myrent.ee.db.*;
import it.myrent.ee.db.MROldContrattoNoleggio;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
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
public class FatturaUtilsOld {

    /** Creates a new instance of FatturaUtils */
    public FatturaUtilsOld() {
    }
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");
    public static final String UNITA_MISURA_GIORNI = bundle.getString("FatturaUtils.msgGG");
    public static final String UNITA_MISURA_KM = bundle.getString("FatturaUtils.msgKM");
    public static final String DESCRIZIONE_GIORNI_TARIFFA = bundle.getString("FatturaUtils.msgListino0GiorniTariffa");
    public static final String DESCRIZIONE_GIORNI_EXTRA = bundle.getString("FatturaUtils.msgListino0GiorniExtra");
    public static final String DESCRIZIONE_COMBUSTIBILE_MANCANTE = bundle.getString("FatturaUtils.msgMaterialeConsumo0");
    private static Log log = LogFactory.getLog(FatturaUtils.class);

    private static MROldPianoDeiConti leggiCodiceSottoconto(Session sx) throws HibernateException {
        return leggiSottoconto(sx, new Integer(4), new Integer(10), new Integer(6));
    }


    private static MROldPianoDeiConti leggiCodiceSottocontoCarburante(Session sx) throws HibernateException {
        return leggiSottoconto(sx, new Integer(4), new Integer(10), new Integer(3));
    }

    public static MROldPianoDeiConti leggiSottoconto(Session sx, Integer mastro, Integer conto, Integer sottoconto) throws HibernateException {
        MROldPianoDeiConti retValue = null;
        Query querySottoconto = sx.createQuery(
                "select s from Sottoconto s where " + //NOI18N
                "s.codiceMastro = :mastro and " + // NOI18N
                "s.codiceConto = :conto and " + // NOI18N
                "s.codiceSottoconto = :sottoconto"); // NOI18N
        querySottoconto.setParameter("mastro", mastro); //NOI18N
        querySottoconto.setParameter("conto", conto); //NOI18N
        querySottoconto.setParameter("sottoconto", sottoconto); //NOI18N
        retValue = (MROldPianoDeiConti) querySottoconto.uniqueResult();
        return retValue;
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
        if(tariffa.getStagioni().size() > 0) {
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
    private static double calcolaImportoGiornaliero(Session sx, MROldTariffa tariffa, boolean ivaInclusa, double aliquotaIva, double importoTariffa, double numeroGiorni,boolean giornoExtra) throws HibernateException {
        if (Preferenze.getListiniImportiGiornalieri(sx)) {
            importoTariffa *= numeroGiorni;
        }
        //TO DO - RIPORTARE BUGFIXING DAL MYRENTEE

        List optionalsInclusi = new ArrayList();
        double x = 0.0, s = 0.0, si = 0.0, sp = 0.0, spi = 0.0, iva = (ivaInclusa ? aliquotaIva : 0.0);

        if (!giornoExtra) {
            if (tariffa.getOptionalsTariffa() != null) {
                Iterator it = tariffa.getOptionalsTariffa().values().iterator();
                while (it.hasNext()) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                    if (Boolean.TRUE.equals(optionalTariffa.getIncluso()) && Boolean.TRUE.equals(optionalTariffa.getSelezionato())) {
                        optionalsInclusi.add(optionalTariffa);
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
                            spi += importo / 100.0;
                        } else if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoGiornaliero())) {
                            si += (importo * numeroGiorni);
                        } else {
                            si += importo;
                        }
                    } else {
                        if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoPercentuale())) {
                            sp += importo / 100.0;
                        } else if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoGiornaliero())) {
                            s += (importo * numeroGiorni);
                        } else {
                            s += importo;
                        }
                    }
                }
            }
        }
        //TODO - sbagliato - x = (importoTariffa - (s + si)*(1.0+sp*spi) - iva*(si + s * spi + si * spi)) / (iva+1.0) / (1.0 + spi + sp) / numeroGiorni;
        x = (importoTariffa - si * (1.0 + spi + sp + iva * spi + iva) + s * (1.0 + spi + sp + iva * spi)) / (1.0 + spi + sp + iva * spi + iva) / numeroGiorni;
        return x;
    }

//    /**
//     * Calcola l'importo giornaliero scorporando l'iva e gli optionals inclusi nell'<code>importoTariffa</code>
//     * dato il numero di giorni di riferimento.
//     *@param tariffa la tariffa di riferimento
//     *@param importoTariffa l'importo complessivo della tariffa
//     *@param numeroGiorni il numero di giorni al quale e' riferito l'<code>importoTariffa</code>
//     *@return l'importo giornaliero del noleggio.
//     */
//    private static double calcolaImportoGiornaliero(Session sx, Boolean ivaInclusa, MROldCodiciIva codiceIva, double importoTariffa, double numeroGiorni) throws HibernateException {
//        double x = 0.0, s = 0.0, si = 0.0, sp = 0.0, spi = 0.0, iva = 0.0;
//        if (Boolean.TRUE.equals(ivaInclusa)) {
//            codiceIva = (MROldCodiciIva) sx.get(MROldCodiciIva.class, codiceIva.getId());
//            iva = codiceIva.getAliquota().doubleValue();
//        }
//        x = (importoTariffa - si * (1.0 + spi + sp + iva * spi + iva) + s * (1.0 + spi + sp + iva * spi)) / (1.0 + spi + sp + iva * spi + iva) / numeroGiorni;
//        return x;
//    }
    /**
     * Calcola l'importo giornaliero scorporando l'iva e gli optionals inclusi nell'<code>importoTariffa</code>
     * dato il numero di giorni di riferimento.
     *@param tariffa la tariffa di riferimento
     *@param importoTariffa l'importo complessivo della tariffa
     *@param numeroGiorni il numero di giorni al quale e' riferito l'<code>importoTariffa</code>
     *@return l'importo giornaliero del noleggio.
     */
    private static double calcolaImportoGiornaliero(Session sx, Map optionalsTariffa, boolean ivaInclusa, double aliquotaIva, double importoTariffa, double numeroGiorni) throws HibernateException {
        if (Preferenze.getListiniImportiGiornalieri(sx)) {
            importoTariffa *= numeroGiorni;
        }
        List optionalsInclusi = new ArrayList();
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
                            spi += importo / 100.0;
                        } else if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoGiornaliero())) {
                            si += (importo * numeroGiorni);
                        } else {
                            si += importo;
                        }
                    } else {
                        if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoPercentuale())) {
                            sp += importo / 100.0;
                        } else if (Boolean.TRUE.equals(optionalTariffa.getOptional().getImportoGiornaliero())) {
                            s += (importo * numeroGiorni);
                        } else {
                            s += importo;
                        }
                    }
                }
            }
        }
        //TODO - sbagliato - x = (importoTariffa - (s + si)*(1.0+sp*spi) - iva*(si + s * spi + si * spi)) / (iva+1.0) / (1.0 + spi + sp) / numeroGiorni;
        x = (importoTariffa - si * (1.0 + spi + sp + iva * spi + iva) + s * (1.0 + spi + sp + iva * spi)) / (1.0 + spi + sp + iva * spi + iva) / numeroGiorni;
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
            Integer giorniVoucher) throws HibernateException, TariffaNonValidaException {

        List righe = new ArrayList();

        double giorniPrepagati = 0.0;
        if (giorniVoucher != null) {
            giorniPrepagati = giorniVoucher.doubleValue();
        }

        MROldImportoTariffa minimoTariffa = trovaMinimoTariffa(tariffa);
        if(minimoTariffa == null) {
            throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgTariffaNonContieneImportiGruppoPeriodo"));
        } else if(minimoTariffa.getMinimo() > giorniVoucher) {
            throw new TariffaNonValidaException(MessageFormat.format(bundle.getString("FatturaUtils.msgNoleggioPrepagatoInferiorePeriodoMinimoRichiesto0"), minimoTariffa.getMinimo()));
        }


        fine = FormattedDate.add(inizio, Calendar.DAY_OF_MONTH, (int) giorniPrepagati);

        Date dataInizio = FormattedDate.extractDate(inizio);
        Date oraInizio = FormattedDate.extractTime(inizio);
        Date dataFine = FormattedDate.extractDate(fine);
        Date oraFine = FormattedDate.extractTime(fine);
//
//        if (oraFine.getTime() <= oraInizio.getTime()) {
//            if (tariffa != null && Boolean.TRUE.equals(tariffa.getOraRientroAttiva()) && tariffa.getOraRientro() != null) {
//                oraFine = tariffa.getOraRientro();
//            }
//        }

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
                            false);
                    importoExtra = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importoStagione.getImportoExtra().doubleValue(),
                            1.0,
                            true);
                } else {
                    importoGiornaliero = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importoStagione.getImportoBase().doubleValue(),
                            giorniBase,
                            false);
                    importoExtra = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importoStagione.getImportoExtra().doubleValue(),
                            1.0,
                            true);
                }

                String descrizioneTariffa = stagione.getDescrizione();

                MROldRigaDocumentoFiscale primaRiga = creaRigaNoleggio(descrizioneTariffa, giorniBase, importoGiornaliero, scontoPercentuale, codiceIva, pianoDeiConti);

                if (primaRiga != null) {
                    righe.add(primaRiga);
                }

                MROldRigaDocumentoFiscale secondaRiga = creaRigaGiorniExtra(descrizioneTariffa, giorniExtra, importoExtra, scontoPercentuale, codiceIva, pianoDeiConti);
                if (secondaRiga != null) {
                    righe.add(secondaRiga);
                }
            }
        }

        return aggiungiRigheOptionalsPrepagato(sx, righe, tariffa, giorniPrepagati, scontoPercentuale);
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
            Integer giorniVoucher) throws HibernateException, TariffaNonValidaException {

        List righe = new ArrayList();
        if (contratto != null && contratto.getId() != null) {
            contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contratto.getId());
        } else {
            contratto = null;
        }
        MROldPianoDeiConti pianoDeiConti = leggiCodiceSottoconto(sx);

        if (scontoPercentuale == null) {
            scontoPercentuale = new Double(0);
        }

        double totaleKm = 0.0;
        Map totaliCarburanti = new HashMap();

        if (contratto != null) {
            Iterator movimenti = contratto.getMovimenti().iterator();
            while (movimenti.hasNext()) {
                MROldMovimentoAuto aMovimento = (MROldMovimentoAuto) movimenti.next();
                if (!aMovimento.getAnnullato().booleanValue()) {
                    int kmMovimento = 0, litriMovimento = 0;
                    if (aMovimento.getChiuso().booleanValue()) {
                        kmMovimento = aMovimento.getKmFine().intValue() - aMovimento.getKmInizio().intValue();
                        litriMovimento = aMovimento.getCombustibileInizio().intValue() - aMovimento.getCombustibileFine().intValue();
                    } else {
                        if (km != null) {
                            kmMovimento = km.intValue() - aMovimento.getKmInizio().intValue();
                        }
                        if (litri != null) {
                            litriMovimento = aMovimento.getCombustibileInizio().intValue() - litri.intValue();
                        }
                    }

                    if (kmMovimento > 0) {
                        totaleKm += (double) kmMovimento;
                    }

                    if (litriMovimento > 0) {
                        Integer litriCarburante = (Integer) totaliCarburanti.get(aMovimento.getVeicolo().getCarburante());
                        if (litriCarburante == null) {
                            litriCarburante = new Integer(litriMovimento);
                        } else {
                            litriCarburante = new Integer(litriMovimento + litriCarburante.intValue());
                        }
                        totaliCarburanti.put(aMovimento.getVeicolo().getCarburante(), litriCarburante);
                    }

                }
            }
        } else {
            if (km != null) {
                totaleKm = km.doubleValue();
            }
            if (litri != null && carburante != null) {
                carburante = (MROldCarburante) sx.get(MROldCarburante.class, carburante.getId());
                totaliCarburanti.put(carburante, litri);
            }
        }


        double giorniPrepagati = 0.0;
        if (giorniVoucher != null) {
            giorniPrepagati = giorniVoucher.doubleValue();
        }
        //Arrotondiamo per eccesso, imputando almeno 1 giorno. Non teniamo conto dell'ora legale.
        //Ritardo massimo 59 minuti.
        double giorniEsattiSenzaRitardo = FormattedDate.numeroGiorni(inizio, fine, true);

        double giorniEsatti = FormattedDate.numeroGiorni(inizio, FormattedDate.add(fine, Calendar.MINUTE, -59), true);
        double giorniInteriPerEccesso = Math.ceil(giorniEsatti);
        double giorniInteriPerDiffetto = Math.floor(giorniEsatti);

        double durataNoleggioPerEccesso = Math.max(1.0, giorniInteriPerEccesso);

        boolean prepagato = (giorniPrepagati > 0);
        boolean prepagatoExtra = (prepagato && durataNoleggioPerEccesso > giorniPrepagati);

        boolean mezzaGiornata = false;
        boolean giornataRidotta = false;
        boolean notteExtra = false;
        boolean giornoFestivo = false;
        double giorniFestivi = 0;

        if (prepagatoExtra) {
            aggiungiRighePrepagatoExtra(
                    sx,
                    righe,
                    tariffa,
                    durataNoleggioPerEccesso,
                    giorniPrepagati,
                    scontoPercentuale,
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
                            righe,
                            tariffa,
                            inizio,
                            fine,
                            giorniNoleggio,
                            scontoPercentuale,
                            pianoDeiConti);
                }

            } else {
                // Aggiungiamo il numero di giorni arrotondato per eccesso, minimo uno.
                aggiungiRigheNonPrepagato(sx,
                        righe,
                        tariffa,
                        inizio,
                        fine,
                        durataNoleggioPerEccesso,
                        scontoPercentuale,
                        pianoDeiConti);
            }
        }

        //FIXME Da chiedere se addebitare anche qui il noleggio contabile.
        return aggiungiRigheOptionals(sx, righe, tariffa, scontoPercentuale, durataNoleggioPerEccesso, giorniPrepagati, totaleKm, totaliCarburanti);
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

        List righe = new ArrayList();

        fine = FormattedDate.add(inizio, Calendar.DAY_OF_MONTH, (int) giorniNoleggio);

        Date dataInizio = FormattedDate.extractDate(inizio);
        Date oraInizio = FormattedDate.extractTime(inizio);
        Date dataFine = FormattedDate.extractDate(fine);
        Date oraFine = FormattedDate.extractTime(fine);

        MROldImportoTariffa minimoTariffa = trovaMinimoTariffa(tariffa);
        if(minimoTariffa == null) {
            throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgTariffaNonContieneImportiGruppoPeriodo"));
        } else if(minimoTariffa.getMinimo() > giorniNoleggio) {
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
                            false);
                    importoExtra = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importStagione.getImportoExtra().doubleValue(),
                            1.0,
                            true);
                } else {
                    importoGiornaliero = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importStagione.getImportoBase().doubleValue(),
                            giorniBase,
                            false);
                    importoExtra = calcolaImportoGiornaliero(
                            sx,
                            tariffa,
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importStagione.getImportoExtra().doubleValue(),
                            1.0,
                            true);
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




        if (tariffa.getImportiExtraPrepay() == null || tariffa.getImportiExtraPrepay().isEmpty()) {
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
                    importoTariffa.getMinimo().doubleValue());
            importoExtra = calcolaImportoGiornaliero(
                    sx,
                    tariffa.getOptionalsTariffa(),
                    tariffa.getIvaInclusaExtraPrepay(),
                    codiceIvaExtraPrepay.getAliquota(),
                    importoTariffa.getImportoExtra().doubleValue(),
                    1.0);
        } else {
            importoGiornaliero = calcolaImportoGiornaliero(
                    sx,
                    tariffa.getOptionalsTariffa(),
                    tariffa.getIvaInclusaExtraPrepay(),
                    codiceIvaExtraPrepay.getAliquota(),
                    importoTariffa.getImportoBase().doubleValue(),
                    giorniBase);
            importoExtra = calcolaImportoGiornaliero(
                    sx,
                    tariffa.getOptionalsTariffa(),
                    tariffa.getIvaInclusaExtraPrepay(),
                    codiceIvaExtraPrepay.getAliquota(),
                    importoTariffa.getImportoExtra().doubleValue(),
                    1.0);
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
            Map totaliCarburanti) {
        aggiungiRigheOptionalsTempoExtra(sx, righeFattura, tariffa, scontoPercentuale);
        Iterator carburanti = totaliCarburanti.keySet().iterator();
        while (carburanti.hasNext()) {
            MROldCarburante carburante = (MROldCarburante) carburanti.next();
            carburante = (MROldCarburante) sx.get(MROldCarburante.class, carburante.getId());
            Integer litri = (Integer) totaliCarburanti.get(carburante);
            aggiungiRigaCarburante(sx, righeFattura, tariffa, litri, carburante);
        }
        // OPTIONALS USCITA
        aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, totaleKm, false);

        // Per applicare gli oneri agli optionals aggiunti al rientro
        // OPTIONALS RIENTRO - CON ONERI
        aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, totaleKm, true);

        // ONERI
        aggiungiRigheOptionalsPercentuali(sx, righeFattura, tariffa);

        // Per non applicare gli oneri agli optionals aggiunti al rientro
        // OPTIONALS RIENTRO - SENZA ONERI
        //aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, totaleKm, true);

        // FRANCHIGIE
        aggiungiRigheOptionalsFranchigie(sx, righeFattura, tariffa);
        return righeFattura;
    }

    private static List aggiungiRigheOptionalsPrepagato(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            double giorniVoucher,
            double scontoPercentuale) {
        // OPTIONALS INCLUSI
        aggiungiRigheOptionalsNonPercentualiPrepagato(sx, righeFattura, tariffa, giorniVoucher, scontoPercentuale, Boolean.TRUE);

        // Per applicare gli oneri agli optionals non inclusi nella tariffa
        // OPTIONALS NON INCLUSI - CON ONERI
        aggiungiRigheOptionalsNonPercentualiPrepagato(sx, righeFattura, tariffa, giorniVoucher, scontoPercentuale, Boolean.FALSE);

        // ONERI
        aggiungiRigheOptionalsPercentuali(sx, righeFattura, tariffa);

        // Per non applicare gli oneri agli optionals non inclusi nella tariffa
        // OPTIONALS NON INCLUSI - SENZA ONERI
        //aggiungiRigheOptionalsNonPercentualiPrepagato(sx, righeFattura, tariffa, giorniVoucher, Boolean.FALSE);

        return righeFattura;
    }

    private static List aggiungiRigheOptionalsNonPercentualiPrepagato(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            double giorniVoucher,
            double scontoPercentuale,
            Boolean incluso) {

        if (tariffa.getOptionalsTariffa() != null) {

            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());

            Iterator it = tariffa.getOptionalsTariffa().values().iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalTariffa.getOptional();
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
                    //Attenti ai km, dove il massimo viene moltiplicato per il numero di giorni.
                    if (optional.getAddebitoMassimo() != null) {
                        quantita = Math.min(quantita, optional.getAddebitoMassimo().doubleValue());
                    }

                    MROldPianoDeiConti contoRicavo = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabile().getId());

                    for (int i = 0; i < molteplicita; i++) {
                        MROldRigaDocumentoFiscale riga = creaRigaOptional(
                                optional,
                                quantita,
                                prezzo,
                                scontoPercentuale,
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

        if (quantita > 0.0) {
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
            iva = MathUtils.round(imponibile * aliquota, 5);
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
            boolean rientro) {

        if (tariffa.getOptionalsTariffa() != null) {


            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());

            Iterator it = tariffa.getOptionalsTariffa().values().iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalTariffa.getOptional();
                if ( // Gli optionals in uscita solo all'uscita
                        ((!rientro && optionalTariffa.getSelezionato())
                        || // e' quelli in rientro solo al rientro.
                        (rientro && optionalTariffa.getSelezionatoRientro()))
                        && // Escludiamo gli optionals percentuali
                        !optional.getImportoPercentuale()
                        && // Escludiamo gli optionals di tempo extra.
                        !optional.getTempoExtra()) {


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

                    if (optional.getKmExtra().equals(Boolean.TRUE)) {
                        //Qui aggiungiamo i km extra, se ci sono
                        quantita = MathUtils.round(totaleKm - optional.getKmInclusi().doubleValue() * totaleGiorni, 5);
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
                    if (optional.getAddebitoMassimo() != null) {
                        if (optional.getKmExtra().equals(Boolean.TRUE)) {
                            quantita = Math.min(quantita, optional.getAddebitoMassimo().doubleValue() * totaleGiorni);
                        } else {
                            quantita = Math.min(quantita, optional.getAddebitoMassimo().doubleValue());
                        }
                    }

                    MROldPianoDeiConti contoContabile = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, optional.getContoContabile().getId());

                    double prezzo = optionalTariffa.getImporto();

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
        return righeFattura;
    }

    private static List aggiungiRigheOptionalsPercentuali(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa)
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
                    totaleImponibileRighe = MathUtils.round(totaleImponibileRighe + tmpRiga.getTotaleImponibileRiga().doubleValue());
                }

                while (it.hasNext()) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                    MROldOptional optional = optionalTariffa.getOptional();
                    if (optionalTariffa.getSelezionato() && optional.getImportoPercentuale() && !optional.getTempoExtra()) {

                        double molteplicita = 1.0;
                        if (optionalTariffa.getQuantita() != null) {
                            molteplicita = optionalTariffa.getQuantita();
                        }

                        double quantita = MathUtils.round(optionalTariffa.getImporto().doubleValue() * 0.01, 5);
                        double prezzo = totaleImponibileRighe;

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
                if (optionalTariffa.getSelezionatoFranchigia()
                        && optionalTariffa.getFranchigia() != null) {

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
        if (
                combustibileMancante != null
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
                if (tmpRiga.isRigaDescrittiva()) {
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
        try {
            documentoFiscale.setFatturaRighe(FatturaUtils.calcolaRighe(
                    sx,
                    null,
                    prenotazione.getTariffa(),
                    prenotazione.getInizio(),
                    prenotazione.getFine(),
                    null,
                    null,
                    null,
                    prenotazione.getScontoPercentuale(),
                    prenotazione.getCommissione().getGiorniVoucher(),null));
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
}
