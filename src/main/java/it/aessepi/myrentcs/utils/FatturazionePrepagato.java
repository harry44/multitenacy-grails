/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.aessepi.myrentcs.utils;

import grails.util.Holders;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.BusinessRuleException;
import it.myrent.ee.api.exception.DatabaseException;
import it.myrent.ee.api.exception.TariffaNonValidaException;
import it.myrent.ee.db.MROldDepositAndWaiverResSource;
import it.myrent.ee.db.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author bogdan
 */
public class FatturazionePrepagato extends AbstractFatturazione implements Fatturazione {

    public FatturazionePrepagato(MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        super(tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
    }

    public FatturazionePrepagato(MROldPrenotazione prenotazione, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        super(prenotazione,tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
    }

    public FatturazionePrepagato(MROldContrattoNoleggio contratto, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        super(contratto, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
    }

    public List<MROldRigaDocumentoFiscale> calcolaRigheProssimaFattura(Session sx) throws TariffaNonValidaException{
//        return calcolaRighePrepagato(sx);

        List righeNuove = null;
        try {
            righeNuove = calcolaRighePrepagato(sx);
        } catch (TariffaNonValidaException e) {
            throw new TariffaNonValidaException(e.getMessage());
            //e.printStackTrace();
        }
        if (righeNuove == null || righeNuove.isEmpty()) {
            //throw new FatturaVuotaException(bundle.getString("Fatturazione.msgNessunaVoceDaFatturare"));
        }
        if (getPrenotazione() != null && getPrenotazione().getId() != null) {
            List righeFatturePrecedenti = FatturaUtils.leggiRigheDocumentiFiscaliEmessiPrepagati(sx, getPrenotazione());
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

    public List<MROldRigaDocumentoFiscale> anteprimaValoreFatture(Session sx) throws it.myrent.ee.api.exception.TariffaNonValidaException, it.myrent.ee.api.exception.FatturaVuotaException {

        return calcolaRigheProssimaFattura(sx);
    }


    public List<MROldRigaDocumentoFiscale> ricalcolaRigheUltimaFattura(Session sx, MROldDocumentoFiscale fattura) throws it.myrent.ee.api.exception.TariffaNonValidaException, it.myrent.ee.api.exception.FatturaVuotaException {
        return calcolaRighePrepagato(sx);
    }

    public List<MROldRigaDocumentoFiscale> ricalcolaRigheUltimaFattura(MROldDocumentoFiscale fattura) throws BusinessRuleException, DatabaseException {
        return null;
    }

    /**
     * Calcola il costo del prepagato. Attenzione: il periodo tra
     * <code>inizio</coed> e <code>fine</code> deve avere esattamente
     * <code>giorniVoucher</code> giorni.
     */
    private List calcolaRighePrepagato(Session sx) throws HibernateException, it.myrent.ee.api.exception.TariffaNonValidaException {

        List righe = new ArrayList();

        double giorniPrepagati = 0.0;
        if (getGiorniVoucher() != null) {
            giorniPrepagati = getGiorniVoucher().doubleValue();
        }

        MROldImportoTariffa minimoTariffa = trovaMinimoTariffa(getTariffa());
        if (minimoTariffa == null) {
            throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgTariffaNonContieneImportiGruppoPeriodo"));
        } else if (getGiorniVoucher() == null) {
           return righe;
        } else if (minimoTariffa.getMinimo() > getGiorniVoucher()) {
            throw new TariffaNonValidaException(MessageFormat.format(bundle.getString("FatturaUtils.msgNoleggioPrepagatoInferiorePeriodoMinimoRichiesto0"), minimoTariffa.getMinimo()));
        }

        Date fine = FormattedDate.add(getInizio(), Calendar.DAY_OF_MONTH, (int) giorniPrepagati);

        Date dataInizio = FormattedDate.extractDate(getInizio());
        Date oraInizio = FormattedDate.extractTime(getInizio());
        Date dataFine = FormattedDate.extractDate(fine);
        Date oraFine = FormattedDate.extractTime(fine);

        for (MROldStagioneTariffa stagione : getTariffa().getStagioni()) {
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
                //fetching VAT code for oracle
                    MROldCodiciIva tempCodiceIva = findVATCodeForWorkshop(sx);
                    if(tempCodiceIva!=null){
                        codiceIva = tempCodiceIva;
                    }
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
                            getTariffa(),
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importoStagione.getImportoBase().doubleValue(),
                            importoStagione.getMinimo().doubleValue(),
                            false,
                            false);
                    importoExtra = calcolaImportoGiornaliero(
                            sx,
                            getTariffa(),
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importoStagione.getImportoExtra().doubleValue(),
                            1.0,
                            true,
                            false);
                } else {
                    importoGiornaliero = calcolaImportoGiornaliero(
                            sx,
                            getTariffa(),
                            stagione.getIvaInclusa(),
                            codiceIva.getAliquota(),
                            importoStagione.getImportoBase().doubleValue(),
                            giorniBase,
                            false,
                            false);
                    importoExtra = calcolaImportoGiornaliero(
                            sx,
                            getTariffa(),
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
                }

                MROldRigaDocumentoFiscale secondaRiga = creaRigaGiorniExtra(descrizioneTariffa, giorniExtra, importoExtra, 0.0, codiceIva, pianoDeiConti);
                if (secondaRiga != null) {
                    righe.add(secondaRiga);
                }
            }
        }
        
         /*
         Andrea
         add row about incident, if it is a rental agreeement with incident
         */
        MROldFonteCommissione rentalFonte = null;
        Double defDamageWaiver = null;
        Double defDamageWaiverToUser = null;
        Double defTheftWaiver = null;
        Double defTheftWaiverToUser = null;
        if (getContrattoNoleggio() != null) {
            if (getContrattoNoleggio().getMovimento() != null) {
                if (getContrattoNoleggio().getMovimento() != null) {
                    MROldMovimentoAuto movimentoAuto = (MROldMovimentoAuto) sx.get(MROldMovimentoAuto.class, getContrattoNoleggio().getMovimento().getId());
                    //if last movement has incident
                    if (movimentoAuto.getIsAccident()!= null &&
                            movimentoAuto.getIsAccident()) {
                        if (movimentoAuto.getMrIncidentType() != null) {
                            //CIDATTIVO
                            if (movimentoAuto.getMrIncidentType().getIsActive()) {

                            } else {

                                //CIDPASSIVO OR THEFT
                                if (getContrattoNoleggio().getCommissione() != null) {
                                    MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, getContrattoNoleggio().getCommissione().getId());

                                    if (commissione != null && commissione.getFonteCommissione() != null) {
                                        rentalFonte = commissione.getFonteCommissione();
                                        //Get AMOUNT  AND INFORMATION FOR DAMAGE AND THEFT
                                        if (rentalFonte.getDepositResSourceId() != null) {
                                            MROldDepositAndWaiverResSource depositAndWaiverResSource = (MROldDepositAndWaiverResSource)
                                                    sx.get(MROldDepositAndWaiverResSource.class, rentalFonte.getDepositResSourceId().getId());
                                            if (depositAndWaiverResSource.getDefaultTheftWaiverAmount() != null) {
                                                defTheftWaiver = depositAndWaiverResSource.getDefaultTheftWaiverAmount();
                                                defTheftWaiverToUser = depositAndWaiverResSource.getTheftPercChargedToUser();
                                            }
                                            if (depositAndWaiverResSource.getDefaultDamageWaiverAmount() != null) {
                                                defDamageWaiver = depositAndWaiverResSource.getDefaultDamageWaiverAmount();
                                                defDamageWaiverToUser = depositAndWaiverResSource.getDamagePercChargedToUser();
                                            }
                                            //check if eprd or eprdf
                                            boolean thereIsEPRD = false;
                                            boolean thereIsEPRDF = false;
                                            boolean thereIsEPRS = false;
                                            boolean aTheft = false;
                                            boolean theft = false;
                                            if(movimentoAuto.getMrIncidentType().getIsTheft()){
                                                theft = true;
                                            }
                                            if(movimentoAuto.getMrIncidentType().getIsTheft()){
                                                aTheft = true;
                                            }
                                            MROldTariffa tariffaContratto = (MROldTariffa) sx.get(MROldTariffa.class, getContrattoNoleggio().getTariffa().getId());
                                            for (Map.Entry<MROldOptional, MROldOptionalTariffa> optionalMap : tariffaContratto.getOptionalsTariffa().entrySet()) {
                                                MROldOptional optional = optionalMap.getKey();
                                                MROldOptionalTariffa optionalTarrifa = optionalMap.getValue();
                                                if (optional.getCodice().startsWith("EPRD") && !(optional.getCodice().startsWith("EPRDF"))) {
                                                    if(optionalTarrifa.getSelezionato() || optionalTarrifa.getSelezionatoFranchigia()){
                                                        thereIsEPRD = true;
                                                    }
                                                } else if (optional.getCodice().startsWith("EPRDF")) {
                                                     if(optionalTarrifa.getSelezionato() || optionalTarrifa.getSelezionatoFranchigia()){
                                                         thereIsEPRDF = true;
                                                     }
                                                    
                                                } else if (optional.getCodice().startsWith("EPRS")) {
                                                    if(optionalTarrifa.getSelezionato() || optionalTarrifa.getSelezionatoFranchigia()){
                                                        thereIsEPRS = true;
                                                    }
                                                }
                                            }
                                            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                            //FIRST CASE
                                            if (movimentoAuto.getMrIncidentType().getIsPassive() && !thereIsEPRD && !thereIsEPRDF) {
                                                Double amount = defDamageWaiver * ((100 - defDamageWaiverToUser) / 100);
                                                MROldOptional optionalDamage = null;
                                                for (Map.Entry<MROldOptional,  MROldOptionalTariffa> optionalMap : tariffaContratto.getOptionalsTariffa().entrySet()) {
                                                    MROldOptional optional = optionalMap.getKey();
                                                    MROldOptionalTariffa optionalTarrifa = optionalMap.getValue();
                                                    if (optional.getCodice().startsWith("INCIDENTNOTINS")) {
                                                        //optionalTarrifa.setSelezionato(true);
                                                        optionalDamage = optional;
                                                    }
                                                }
                                                if (optionalDamage != null) {
                                                    MROldRigaDocumentoFiscale riga = creaRigaOptional(
                                                            optionalDamage,
                                                            1.0,
                                                            amount,
                                                            null,
                                                            false,
                                                            true,
                                                            getContrattoNoleggio().getTariffa().getCodiceIvaNonSoggetto(),
                                                            getContrattoNoleggio().getTariffa().getCodiceIvaNonSoggetto(),
                                                            optionalDamage.getContoContabile());
                                                    righe.add(riga);
                                                }
                                            }
                                            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                            else if(aTheft && !thereIsEPRDF){
                                                //SECOND CASE
                                                Double amount = defTheftWaiver * ((100 - defTheftWaiverToUser) / 100);
                                                MROldOptional optionalDamage = null;
                                                for (Map.Entry<MROldOptional, MROldOptionalTariffa> optionalMap : tariffaContratto.getOptionalsTariffa().entrySet()) {
                                                    MROldOptional optional = optionalMap.getKey();
                                                    MROldOptionalTariffa optionalTarrifa = optionalMap.getValue();
                                                    if (optional.getCodice().startsWith("ATHEFTNOINS")) {
                                                        //optionalTarrifa.setSelezionato(true);
                                                        optionalDamage = optional;
                                                    }
                                                }
                                                if (optionalDamage != null) {
                                                    MROldRigaDocumentoFiscale riga = creaRigaOptional(
                                                            optionalDamage,
                                                            1.0,
                                                            amount,
                                                            null,
                                                            false,
                                                            true,
                                                            getContrattoNoleggio().getTariffa().getCodiceIvaNonSoggetto(),
                                                            getContrattoNoleggio().getTariffa().getCodiceIvaNonSoggetto(),
                                                            optionalDamage.getContoContabile());
                                                    righe.add(riga);
                                                }
                                            }
                                            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                            else if(theft && !thereIsEPRDF){
                                                //THIRD CASE
                                                Double amount = defTheftWaiver* ((100 - defTheftWaiverToUser) / 100);
                                                MROldOptional optionalDamage = null;
                                                for (Map.Entry<MROldOptional, MROldOptionalTariffa> optionalMap : tariffaContratto.getOptionalsTariffa().entrySet()) {
                                                    MROldOptional optional = optionalMap.getKey();
                                                    MROldOptionalTariffa optionalTarrifa = optionalMap.getValue();
                                                    if (optional.getCodice().startsWith("THEFTNOINS")) {
                                                        //optionalTarrifa.setSelezionato(true);
                                                        optionalDamage = optional;
                                                    }
                                                }
                                                if (optionalDamage != null) {
                                                    MROldRigaDocumentoFiscale riga = creaRigaOptional(
                                                            optionalDamage,
                                                            1.0,
                                                            amount,
                                                            null,
                                                            false,
                                                            true,
                                                            tariffaContratto.getCodiceIvaNonSoggetto(),
                                                            tariffaContratto.getCodiceIvaNonSoggetto(),
                                                            optionalDamage.getContoContabile());
                                                    righe.add(riga);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        verifyCarWashService(sx, getContrattoNoleggio(), righe);
        verifyFuelService(sx, getContrattoNoleggio(), righe);

        return aggiungiRigheOptionalsPrepagato(sx, righe, getTariffa(), giorniPrepagati);
    }

    @Override
    public List<MROldRigaDocumentoFiscale> calculateRowsSplitPayment(Session sx) {
        List<MROldRigaDocumentoFiscale> rowCalculated = null;

        try {
            rowCalculated = calcolaRighePrepagato(sx);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        return rowCalculated;
    }

    public void verifyFuelService(Session sx, MROldContrattoNoleggio contratto, List righe) {
        if (contratto == null || contratto.getId() == null) {

            return;
        }
        try {
            MROldFonteCommissione fonteCommissione = (MROldFonteCommissione) sx.get(MROldFonteCommissione.class, contratto.getCommissione().getFonteCommissione().getId());
            if (fonteCommissione != null && fonteCommissione.getDepositResSourceId() != null && fonteCommissione.getDepositResSourceId().getId() != null) {
                MROldDepositAndWaiverResSource resSourceDetails = (MROldDepositAndWaiverResSource) sx.get(MROldDepositAndWaiverResSource.class
                        , fonteCommissione.getDepositResSourceId().getId());

                if (resSourceDetails.getFuelServicePercChargedToUser() != null
                        && resSourceDetails.getFuelServicePercChargedToUser() != 100) {
                /*Iterator it = contratto.getTariffa().getOptionalsTariffa().values().iterator();
                 while (it.hasNext()) {
                 OptionalTariffa optionalTariffa = (OptionalTariffa) it.next();
                 Optional optional = optionalTariffa.getOptional();
                 */
                    Map totaliCarburanti = new HashMap();
                    double totaleKm = calcolaKmCarburanteMancante(sx, totaliCarburanti);
                    Iterator carburanti = totaliCarburanti.keySet().iterator();

                    while (carburanti.hasNext()) {
                        MROldCarburante carburante = (MROldCarburante) carburanti.next();
                        carburante = (MROldCarburante) sx.get(MROldCarburante.class, carburante.getId());
                        Integer litri = (Integer) totaliCarburanti.get(carburante);
                        aggiungiRigaCarburante(sx, righe, contratto.getTariffa(), litri, carburante, true);
                        selectServizioRifornimentoPrepagato(sx, righe, contratto, contratto.getTariffa());
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void verifyCarWashService(Session sx, MROldContrattoNoleggio contratto, List righe) {
        if (contratto==null || contratto.getId()==null){
            return;
        }
        try {
            MROldFonteCommissione fonteCommissione = (MROldFonteCommissione) sx.get(MROldFonteCommissione.class, contratto.getCommissione().getFonteCommissione().getId());
            MROldDepositAndWaiverResSource resSourceDetails =null;
            if(fonteCommissione.getDepositResSourceId()!=null){
                resSourceDetails =(MROldDepositAndWaiverResSource) sx.get(MROldDepositAndWaiverResSource.class, fonteCommissione.getDepositResSourceId().getId());
            }

            if (resSourceDetails !=null && resSourceDetails.getDefaultCarWashServAmount() != null
                    && resSourceDetails.getCarWashPercChargedToUser() != null
                    && resSourceDetails.getCarWashPercChargedToUser() != 100) {

                selectServizioCarWash(sx, righe, contratto, contratto.getTariffa());

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void selectServizioRifornimentoPrepagato(Session sx, List righe, MROldContrattoNoleggio contratto, MROldTariffa tariffa) {
        if (contratto==null || contratto.getId()==null)
            return;
        tariffa =(MROldTariffa) sx.get(MROldTariffa.class, contratto.getTariffa().getId());
        Iterator it = tariffa.getOptionalsTariffa().values().iterator();
        boolean found = false;
        while (it.hasNext() && found == false) {
            MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
            MROldOptional optional = optionalTariffa.getOptional();

            if (optional.getCarburante().equals(Boolean.TRUE)) {
                found = true;
                optionalTariffa.setSelezionato(true);
                optionalTariffa.setPrepagato(true);
                sx.merge(optionalTariffa);

            }
        }

    }

    private void selectServizioCarWash(Session sx, List righe, MROldContrattoNoleggio contratto, MROldTariffa tariffa) {
        if (contratto==null || contratto.getId()==null)
            return;
        tariffa =(MROldTariffa) sx.get(MROldTariffa.class, contratto.getTariffa().getId());
        Iterator it = tariffa.getOptionalsTariffa().values().iterator();
        boolean found = false;
        while (it.hasNext() && found == false) {
            MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
            MROldOptional optional = optionalTariffa.getOptional();

            if (optional.getLavaggio().equals(Boolean.TRUE)) {
                found = true;
                if (optionalTariffa.getSelezionato() || optionalTariffa.getSelezionatoRientro()) {
                    optionalTariffa.setSelezionato(true);
                    optionalTariffa.setPrepagato(true);
                    sx.merge(optionalTariffa);
                }
            }
        }
    }


}
