/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.myrentcs.utils;

import it.aessepi.utils.MathUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.BusinessRuleException;
import it.myrent.ee.api.exception.DatabaseException;
import it.myrent.ee.api.exception.FatturaVuotaException;
import it.myrent.ee.api.exception.TariffaNonValidaException;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.db.*;
import org.hibernate.Session;

import java.util.*;

/**
 *
 * @author bogdan
 */
public class FatturazionePlurimensile extends AbstractFatturazione implements Fatturazione {

    private Date inizioNoleggio;
    private Date fineNoleggio;
    public static final FatturazioneSpecification SPECIFICATION = new FatturazioneSpecification() {


        public boolean isSatisfiedBy(Date inizio, Date fine, Integer giorniVoucher) {
            if (giorniVoucher == null) {
                giorniVoucher = 0;
            }
            return giorniVoucher == 0 && FormattedDate.numeroGiorni(inizio, fine, true) > 30 && Boolean.valueOf(Preferenze.getSettingValue(null, Preferenze.PROP_CONTRATTO_FATTURAZIONE_MENSILE));
        }


        public boolean isSatisfiedBy(Session sx, Date inizio, Date fine, Integer giorniVoucher) {
            if (giorniVoucher == null) {
                giorniVoucher = 0;
            }
            return giorniVoucher == 0 && FormattedDate.numeroGiorni(inizio, fine, true) > 30 && Boolean.valueOf(Preferenze.getSettingValue(sx, Preferenze.PROP_CONTRATTO_FATTURAZIONE_MENSILE));
        }
    };

    protected FatturazionePlurimensile(MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        super(contrattoNoleggio, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
        setInizioNoleggio(inizio);
        setFineNoleggio(fine);
    }

    public List<MROldRigaDocumentoFiscale> calcolaRigheProssimaFattura(Session sx) {
        /*
         * Andrea
         */

        setInizio(FormattedDate.add(getInizio(), Calendar.DAY_OF_MONTH, FatturaUtils.leggiTotaleGiorniNonPrepagatiFatturati(sx, getContrattoNoleggio())));
        setFine(FormattedDate.add(getInizio(), Calendar.DAY_OF_MONTH, 30));
        if (getFine().after(getFineNoleggio())) {
            setFine(getFineNoleggio());
        }
        try {
            try {
                return calcolaRigheFatturaPlurimensile(sx);
            } catch (FatturaVuotaException e) {
                e.printStackTrace();
            }
        } catch (TariffaNonValidaException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MROldRigaDocumentoFiscale> calcolaRigheProssimaFattura() throws BusinessRuleException, DatabaseException {
        return null;
    }

    public List<MROldRigaDocumentoFiscale> anteprimaValoreFatture(Session sx) throws  it.myrent.ee.api.exception.TariffaNonValidaException,  it.myrent.ee.api.exception.FatturaVuotaException {
        
        return calcolaRigheFatturaPlurimensile(sx);
    }


    public List<MROldRigaDocumentoFiscale> ricalcolaRigheUltimaFattura(Session sx, MROldDocumentoFiscale fattura) throws  it.myrent.ee.api.exception.TariffaNonValidaException, FatturaVuotaException {
        setInizio(FormattedDate.add(getInizio(), Calendar.DAY_OF_MONTH, FatturaUtils.leggiTotaleGiorniNonPrepagatiFatturePrecedenti(sx, getContrattoNoleggio(), fattura)));
        setFine(FormattedDate.add(getInizio(), Calendar.DAY_OF_MONTH, 30));
        if (getFine().after(getFineNoleggio())) {
            setFine(getFineNoleggio());
        }
        return calcolaRigheFatturaPlurimensile(sx);
    }

    public List<MROldRigaDocumentoFiscale> ricalcolaRigheUltimaFattura(MROldDocumentoFiscale fattura) throws BusinessRuleException, DatabaseException {
        return null;
    }

    @Override
    public List<MROldRigaDocumentoFiscale> calculateRowsSplitPayment(Session sx) {
        List<MROldRigaDocumentoFiscale> rowCalculated = null;

        try {
            rowCalculated = calcolaRigheFatturaPlurimensile(sx);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return rowCalculated;
    }

    private List<MROldRigaDocumentoFiscale> calcolaRigheFatturaPlurimensile(Session sx) throws  it.myrent.ee.api.exception.TariffaNonValidaException,  it.myrent.ee.api.exception.FatturaVuotaException {

         
        List righeNuove = null;
        Double noleggioNonFatturatoInMinuti = MathUtils.round((getFineNoleggio().getTime() - getInizio().getTime()) / 1000.0 / 60.0);

        Integer ritardoMassimoConsentito = Preferenze.getContrattoRitardoMassimoMinuti(sx);
        if (noleggioNonFatturatoInMinuti > ritardoMassimoConsentito.intValue()) {
            righeNuove = calcolaRighe(sx);
        }
        else{
            /*
             * Andrea
             */
            righeNuove = calcolaRighe(sx);
            if (righeNuove == null || righeNuove.isEmpty()) {
                //throw new FatturaVuotaException(bundle.getString("Fatturazione.msgNessunaVoceDaFatturare"));
            }
            if (getContrattoNoleggio() != null && getContrattoNoleggio().getId() != null) {
                List righeFatturePrecedenti = FatturaUtils.leggiRigheDocumentiFiscaliEmessi(sx, getContrattoNoleggio());
                if (righeFatturePrecedenti.size() > 0) {
                    FatturaUtils.calcolaDifferenzaDaFatturare(righeFatturePrecedenti, righeNuove);
                    if (righeNuove == null || righeNuove.isEmpty()) {
    //                    throw new FatturaVuotaException(bundle.getString("Fatturazione.msgTutteLeVociSonoGiaFatturate"));
                    }
                }
            } else if (getPrenotazione() != null && getPrenotazione().getId() != null) {
                List righeFatturePrecedenti = FatturaUtils.leggiRigheDocumentiFiscaliEmessi(sx, getPrenotazione());
                if (righeFatturePrecedenti.size() > 0) {
                    FatturaUtils.calcolaDifferenzaDaFatturare(righeFatturePrecedenti, righeNuove);
                    if (righeNuove == null || righeNuove.isEmpty()) {
    //                    throw new FatturaVuotaException(bundle.getString("Fatturazione.msgTutteLeVociSonoGiaFatturate"));
                    }
                }
            }
        }
        if (righeNuove == null || righeNuove.isEmpty()) {
            //throw new FatturaVuotaException(bundle.getString("Fatturazione.msgNessunaVoceDaFatturare"));
        }
        return righeNuove;
    }

    private boolean isFatturaFinale(Session sx) {
        Integer ritardoMassimoConsentito = Preferenze.getContrattoRitardoMassimoMinuti(sx);

        return MathUtils.round((getFineNoleggio().getTime() - getFine().getTime()) / 1000.0 / 60.0) <= ritardoMassimoConsentito.intValue();
    }

    @Override
    protected List aggiungiRigheOptionals(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Double scontoPercentuale,
            double totaleGiorni,
            double giorniVoucher,
            double totaleKm,
            Map totaliCarburanti) {
        aggiungiRigheOptionalsTempoExtra(sx, righeFattura, tariffa, scontoPercentuale);

        if (isFatturaFinale(sx)) {
            Iterator carburanti = totaliCarburanti.keySet().iterator();
            while (carburanti.hasNext()) {
                MROldCarburante carburante = (MROldCarburante) carburanti.next();
                carburante = (MROldCarburante) sx.get(MROldCarburante.class, carburante.getId());
                Integer litri = (Integer) totaliCarburanti.get(carburante);
                aggiungiRigaCarburante(sx, righeFattura, tariffa, litri, carburante, false);
            }
        }

        boolean extraPrePay = giorniVoucher > 0 ? true : false;
        // OPTIONALS USCITA
        aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, 0.0, false, extraPrePay);

        Integer ritardoMassimoConsentito = Preferenze.getContrattoRitardoMassimoMinuti(sx);

        if (isFatturaFinale(sx)) {
            double durataNoleggio = Math.max(1.0, Math.ceil(FormattedDate.numeroGiorni(getInizioNoleggio(), FormattedDate.add(getFineNoleggio(), Calendar.MINUTE, -ritardoMassimoConsentito.intValue()), true)));
            aggiungiRigheKmExtra(sx, righeFattura, tariffa, scontoPercentuale, durataNoleggio, totaleKm, false);
        }

        // Per applicare gli oneri agli optionals aggiunti al rientro
        // OPTIONALS RIENTRO - CON ONERI
        aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, 0.0, true, extraPrePay);

        if (isFatturaFinale(sx)) {
            double durataNoleggio = Math.max(1.0, Math.ceil(FormattedDate.numeroGiorni(getInizioNoleggio(), FormattedDate.add(getFineNoleggio(), Calendar.MINUTE, -ritardoMassimoConsentito.intValue()), true)));
            aggiungiRigheKmExtra(sx, righeFattura, tariffa, scontoPercentuale, durataNoleggio, totaleKm, true);
        }

        // ONERI
        aggiungiRigheOptionalsPercentuali(sx, righeFattura, tariffa, extraPrePay);

        // Per non applicare gli oneri agli optionals aggiunti al rientro
        // OPTIONALS RIENTRO - SENZA ONERI
        //aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, totaleKm, true);

        // FRANCHIGIE
        aggiungiRigheOptionalsFranchigie(sx, righeFattura, tariffa, extraPrePay);
        return righeFattura;
    }

    protected List aggiungiRigheKmExtra(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Double scontoPercentuale,
            double totaleGiorni,
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
                        && // Includiamo solo optionals di km extra
                        optional.getKmExtra().equals(Boolean.TRUE)) {

                    //Gli optionals sono scontabili se abilitiamo gli sconti per gli optionals.
                    boolean scontabile = Preferenze.getOptionalsAbilitaSconto(sx);

                    double quantita = 0.0;
                    double molteplicita = 1.0;
                    if (optionalTariffa.getQuantita() != null) {
                        molteplicita = optionalTariffa.getQuantita();
                    }

                    quantita = MathUtils.round(totaleKm - optional.getKmInclusi().doubleValue() * totaleGiorni, 5);

                    /*
                     * Andrea e Giacomo
                     */
                    if(quantita < 0.0){
                        quantita = 0.0;
                    }

                    /*
                     * Andrea, before was first check max then min
                     */
                    //Limitiamo al massimo addebitabile, se impostato.
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
                    //Ai km, il massimo viene moltiplicato per il numero di giorni.
                    if (optional.getAddebitoMassimo() != null) {
                        quantita = Math.min(quantita, optional.getAddebitoMassimo().doubleValue() * totaleGiorni);
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

    @Override
    protected boolean isVerificaMinimoTariffa(Session sx) {
        return !isFatturaFinale(sx);
    }

    @Override
    public Date getInizioPeriodo() {
        return getInizio();
    }

    @Override
    public Date getFinePeriodo() {
        return getFine();
    }

    public Date getInizioNoleggio() {
        return inizioNoleggio;
    }

    public void setInizioNoleggio(Date inizioNoleggio) {
        this.inizioNoleggio = inizioNoleggio;
    }

    public Date getFineNoleggio() {
        return fineNoleggio;
    }

    public void setFineNoleggio(Date fineNoleggio) {
        this.fineNoleggio = fineNoleggio;
    }
}
