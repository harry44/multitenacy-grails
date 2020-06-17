/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.myrentcs.utils;

import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.BusinessRuleException;
import it.myrent.ee.api.exception.DatabaseException;
import it.myrent.ee.api.exception.FatturaVuotaException;
import it.myrent.ee.api.exception.TariffaNonValidaException;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.db.*;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

/**
 *
 * @author bogdan
 */
public class FatturazioneBreveTermine extends AbstractFatturazione implements Fatturazione {

    private static Log log = LogFactory.getLog(FatturazioneBreveTermine.class);
    public static final FatturazioneSpecification SPECIFICATION = new FatturazioneSpecification() {

        public boolean isSatisfiedBy(Date inizio, Date fine, Integer giorniVoucher) {
            if(giorniVoucher == null) {
                giorniVoucher = 0;
            }
            return (giorniVoucher > 0 || FormattedDate.numeroGiorni(inizio, fine, true) <= 30 || !Boolean.valueOf(Preferenze.getSettingValue( null, Preferenze.PROP_CONTRATTO_FATTURAZIONE_MENSILE)));
        }

        public boolean isSatisfiedBy(Session sx, Date inizio, Date fine, Integer giorniVoucher) {
            if(giorniVoucher == null) {
                giorniVoucher = 0;
            }
            return (giorniVoucher > 0 || FormattedDate.numeroGiorni(inizio, fine, true) <= 31 || !Boolean.valueOf(Preferenze.getSettingValue(sx, Preferenze.PROP_CONTRATTO_FATTURAZIONE_MENSILE)) || Boolean.valueOf(Preferenze.getSettingValue(sx, "rental.monthlyRental.enable")));
        }
    };



    public FatturazioneBreveTermine(MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        super(contrattoNoleggio, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
    }

    public FatturazioneBreveTermine(MROldPrenotazione prenotazione, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        super(prenotazione, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
    }

    public FatturazioneBreveTermine(Session sx,MROldPrenotazione prenotazione, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        super(sx,prenotazione, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
    }

    public FatturazioneBreveTermine(MROldPreventivo preventivo, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        super(preventivo, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
    }


    public List<MROldRigaDocumentoFiscale> calcolaRigheProssimaFattura(Session sx) throws TariffaNonValidaException{

        /*
         * Andrea
         */
        List righeNuove = null;
        try {
            righeNuove = calcolaRighe(sx);
        } catch (TariffaNonValidaException e) {
            throw e;
        }
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
        return righeNuove;
    }

    public List<MROldRigaDocumentoFiscale> calcolaRigheProssimaFattura() throws BusinessRuleException, DatabaseException {
        return null;
    }


    // Codice da completare GTA per Noleggiare Mauro - 2015-10-05

    public List<MROldRigaDocumentoFiscale> anteprimaValoreFatture(Session sx) throws it.myrent.ee.api.exception.TariffaNonValidaException, FatturaVuotaException {

        /*
         * Andrea
         */
        List righeNuove = calcolaRighe(sx);

        return righeNuove;
    }



    public List<MROldRigaDocumentoFiscale> ricalcolaRigheUltimaFattura(Session sx, MROldDocumentoFiscale fattura) throws it.myrent.ee.api.exception.TariffaNonValidaException, FatturaVuotaException {
        List righeNuove = calcolaRighe(sx);
        if (righeNuove == null || righeNuove.isEmpty()) {
            throw new FatturaVuotaException(bundle.getString("Fatturazione.msgNessunaVoceDaFatturare"));
        }
        if (getContrattoNoleggio() != null && getContrattoNoleggio().getId() != null) {
            List righeFatturePrecedenti = FatturaUtils.leggiRigheDocumentiFiscaliPrecedenti(sx, getContrattoNoleggio(), fattura);
            if (righeFatturePrecedenti.size() > 0) {
                FatturaUtils.calcolaDifferenzaDaFatturare(righeFatturePrecedenti, righeNuove);
                if (righeNuove == null || righeNuove.isEmpty()) {
                    throw new FatturaVuotaException(bundle.getString("Fatturazione.msgTutteLeVociSonoGiaFatturate"));
                }
            }
        }
        return righeNuove;
    }

    public List<MROldRigaDocumentoFiscale> ricalcolaRigheUltimaFattura(MROldDocumentoFiscale fattura) throws BusinessRuleException, DatabaseException {
        return null;
    }

    @Override
    public List<MROldRigaDocumentoFiscale> calculateRowsSplitPayment(Session sx) {
        List<MROldRigaDocumentoFiscale> rowCalculated = null;

        try {
            rowCalculated = calcolaRighe(sx);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return rowCalculated;
    }

}
