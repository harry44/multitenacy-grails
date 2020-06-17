/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.api.factory;

import it.aessepi.myrentcs.utils.Fatturazione;
import it.aessepi.myrentcs.utils.FatturazioneFactory;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.BusinessRuleException;
import it.myrent.ee.api.exception.FatturaVuotaException;
import it.myrent.ee.api.exception.TariffaNonValidaException;
import it.myrent.ee.api.exception.TitledException;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.api.utils.*;
import it.myrent.ee.db.*;
import it.myrent.ee.db.MROldCommissione;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author giacomo
 */
public class PartitaFactory {

    private static final Log log = LogFactory.getLog(PartitaFactory.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/factory/Bundle");

    public static MROldPartita newPartita(Session sx, MROldDocumentoFiscale fattura, MROldNumerazione numerazione, Integer numero) {
        MROldPartita partita = null;
        if (fattura.getId() != null) {
            fattura = (MROldDocumentoFiscale) sx.merge(fattura);
            partita = ContabUtils.leggiPartita(sx, fattura);
        }
        if (partita == null) {
            partita = new MROldPartita();
            if (numerazione == null) {
                numerazione = NumerazioniUtils.getNumerazione(sx, fattura.getAffiliato(), MROldNumerazione.PARTITE);
            }
            partita.setNumerazione(numerazione);
            if (numero == null) {
                numero = NumerazioniUtils.aggiornaProgressivo(sx, numerazione, 0);
            }
            partita.setNumero(numero);
            partita.setData(fattura.getData());
            partita.setAffiliato(fattura.getAffiliato());
            partita.setFattura(fattura);
            partita.setContratto(fattura.getContratto());
            partita.setPrenotazione(fattura.getPrenotazione());
            partita.setPrimanota(fattura.getPrimanota());
            partita.setChiusa(false);
        }
        partita.setImporto(fattura.getTotaleFattura());
        partita.setCliente(fattura.getCliente());
        return partita;
    }

    /**
     * Questo metodo crea una fattura di saldo per la prenotazione. Esclude dalla fatturazione le voci fatturate in precedenza. Associa le contropartite degli incassi.
     * @param prenotazione Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La partita contenente la fattura creata.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldPartita newPartitaSaldo(Session sx, MROldPrenotazione prenotazione, MROldSede sedeOperativa, List<MROldPartita> partiteAcconti, List<MROldPrimanota> contropartiteAperte, User user) throws TariffaNonValidaException, FatturaVuotaException, Exception {
        MROldDocumentoFiscale newFattura = DocumentoFiscaleFactory.newIntestazioneFattura(sx, prenotazione, sedeOperativa, MROldDocumentoFiscale.FT);
        try {
            /* per test */

            Date timestampOraInizio = FormattedDate.extractTime(prenotazione.getInizio());
            Date timestampaDateFine = FormattedDate.extractDate(prenotazione.getFine());
            Date timestampOreFine = FormattedDate.extractTime(prenotazione.getFine());

            Date timestamp = null;

            if (Boolean.TRUE.equals(prenotazione.getTariffa().getOraRientroAttiva()) && prenotazione.getTariffa().getOraRientro() != null && (timestampOreFine.getTime() <= timestampOraInizio.getTime())) {
                timestampOreFine = prenotazione.getTariffa().getOraRientro();
            }

            timestamp = FormattedDate.createTimestamp(timestampaDateFine, timestampOreFine);

            /* fine test */

            /*
             * ATTENZIONE: Come data di inizio viene presaa la data di INIZIO CONTRATTO
             * e non la data di INIZIO DEL PRIMO MOVIMENTO
             *
             */
            Fatturazione fatturazione = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                    sx,
                    prenotazione,
                    prenotazione.getTariffa(),
                    prenotazione.getInizio(),
                    timestamp,
                    prenotazione.getScontoPercentuale(),
                    prenotazione.getCommissione().getGiorniVoucher());

            
            newFattura.setFatturaRighe(fatturazione.calcolaRigheProssimaFattura(sx));
            newFattura.setInizioFatturazione(fatturazione.getInizioPeriodo());
            newFattura.setFineFatturazione(fatturazione.getFinePeriodo());
            if(fatturazione.getInizioPeriodo() != null && fatturazione.getInizioPeriodo().after(FormattedDate.add(FormattedDate.formattedDate(), Calendar.DAY_OF_MONTH, 30))) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        for (MROldPartita partita : partiteAcconti) {
            imposteFattura.subtract(partita.getFattura().getFatturaImposte());
        }
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

        //if the total of the invoice is zero, stop!
        if (newFattura.getTotaleFattura() != null && newFattura.getTotaleFattura() < 0.03) {
            throw new BusinessRuleException(bundle.getString("PartitaFactory.msgFatturaImportoZero"), bundle.getString("PartitaFactory.msgFatturaImportoZeroTitle"));
        }

        /* se ci sono contropartite aperte => associo come forma di pagamento quello della prima contropartita aperta */
        if (!contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    newFattura.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }

        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));

        //assegna la fattura anche all'eventuale contratto
        newFattura.setContratto(null);

        MROldPrimanota primanota = PrimanotaFactory.newPrimanota(sx, newFattura, sedeOperativa, null, null, user);
        sx.saveOrUpdate(primanota);
        sx.saveOrUpdate(newFattura);
        MROldPartita partita = PartitaFactory.newPartita(sx, newFattura, null, null);
        partita.setPrimanota(primanota);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        partita.getPartiteAcconto().addAll(partiteAcconti);
        sx.saveOrUpdate(partita);

        //MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, prenotazione.getCommissione().getId());
        if (prenotazione.getCommissione() != null && prenotazione.getCommissione().getContratto() != null) {
            //MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, prenotazione.getCommissione().getPrenotazione().getId());
            prenotazione.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, prenotazione.getCommissione().getContratto(), prenotazione));
        } else {
            prenotazione.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, null, prenotazione));
        }

        sx.saveOrUpdate(prenotazione);
        return partita;
    }

    /**
     * crea una nuova ricevuta fiscale trattandola come una fattura di saldo
     * @param sx
     * @param prenotazione
     * @param sedeOperativa
     * @param contropartiteAperte
     * @return
     * @throws BusinessRuleException
     */
    public static MROldPartita newRicevutaFiscalePagataSaldo(Session sx, MROldPrenotazione prenotazione, MROldSede sedeOperativa, List<MROldPrimanota> contropartiteAperte, User user) throws BusinessRuleException, Exception {
        MROldDocumentoFiscale newFattura = DocumentoFiscaleFactory.newIntestazioneFattura(sx, prenotazione, sedeOperativa, MROldDocumentoFiscale.RF);
        try {
            /* per test */

            MROldMovimentoAuto primoMovimento = null;


            if (prenotazione.getMovimento() != null) {
                primoMovimento = prenotazione.getMovimento();
            }


            Date timestampOraInizio = FormattedDate.extractTime(primoMovimento!=null?primoMovimento.getInizio():prenotazione.getInizio());
            Date timestampaDateFine = FormattedDate.extractDate(primoMovimento!=null?primoMovimento.getFine():prenotazione.getFine());
            Date timestampOreFine = FormattedDate.extractTime(primoMovimento!=null?primoMovimento.getFine():prenotazione.getFine());

            Date timestamp = null;

            if (Boolean.TRUE.equals(prenotazione.getTariffa().getOraRientroAttiva()) && prenotazione.getTariffa().getOraRientro() != null && (timestampOreFine.getTime() <= timestampOraInizio.getTime())) {
                timestampOreFine = prenotazione.getTariffa().getOraRientro();
            }

            timestamp = FormattedDate.createTimestamp(timestampaDateFine, timestampOreFine);

            /* fine test */

            /*
             * ATTENZIONE: Come data di inizio viene presaa la data di INIZIO CONTRATTO
             * e non la data di INIZIO DEL PRIMO MOVIMENTO
             *
             */
            Fatturazione fatturazione = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                    sx,
                    prenotazione,
                    prenotazione.getTariffa(),
                    prenotazione.getInizio(),
                    timestamp,
                    prenotazione.getScontoPercentuale(),
                    prenotazione.getCommissione().getGiorniVoucher());
            newFattura.setFatturaRighe(fatturazione.calcolaRigheProssimaFattura(sx));
            newFattura.setInizioFatturazione(fatturazione.getInizioPeriodo());
            newFattura.setFineFatturazione(fatturazione.getFinePeriodo());

            //assegna la fattura anche al contratto se esiste
            if (prenotazione.getCommissione().getContratto() != null) {
                newFattura.setContratto( null);
            }
            if(fatturazione.getInizioPeriodo() != null && fatturazione.getInizioPeriodo().after(FormattedDate.add(FormattedDate.formattedDate(), Calendar.DAY_OF_MONTH, 30))) {
                throw new Exception();
            }
        } catch (TitledException tex) {
            tex.printStackTrace();
        }

        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        List<MROldPartita> partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, prenotazione);
        for (MROldPartita partita : partiteAcconti) {
            imposteFattura.subtract(partita.getFattura().getFatturaImposte());
        }
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

        /* se ci sono contropartite aperte => associo come forma di pagamento quello della prima contropartita aperta */
        if (!contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    newFattura.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }

        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));
        MROldPrimanota primanota = PrimanotaFactory.newPrimanota(sx, newFattura, sedeOperativa, null, null, user);
        sx.saveOrUpdate(primanota);
        sx.saveOrUpdate(newFattura);
        MROldPartita partita = PartitaFactory.newPartita(sx, newFattura, null, null);
        partita.setPrimanota(primanota);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        partita.getPartiteAcconto().addAll(partiteAcconti);
        sx.saveOrUpdate(partita);

        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, prenotazione.getCommissione().getId());
        if (commissione != null) {
            if (commissione.getContratto() != null) {
                MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, commissione.getContratto().getId());
                prenotazione.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, prenotazione));
            } else {
                prenotazione.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, null, prenotazione));
            }
        } else {
            prenotazione.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, null, prenotazione));
        }

        sx.saveOrUpdate(prenotazione);
        return partita;
    }

    /**
     * Crea una nuova ricevuta fiscale per gli acconti di una prenotazione, con la relativa partita.
     * @param sx
     * @param prenotazione Il contratto per il quale si desidera generare la fattura
     * @param sedeOperativa La sede operativa usata per la fatturazione.
     * @param contropartiteAperte Gli incassi per i quali generare la fattura di acconto.
     * @return La partita contenente la nuova fattura e la sua primanota (partita) non ancora salvate nel database e anche le contropartite degli incassi.
     */
    public static MROldPartita newRicevutaFiscalePagata(Session sx, MROldPrenotazione prenotazione, MROldSede sedeOperativa, List<MROldPrimanota> contropartiteAperte, User user) {
        Double totaleDocumento = 0.0;
        for (MROldPrimanota incasso : contropartiteAperte) {
            totaleDocumento = MathUtils.round(totaleDocumento + incasso.getTotaleDocumento());
        }
        MROldDocumentoFiscale ricevutaFiscale = DocumentoFiscaleFactory.newRicevutaFiscalePagata(sx, prenotazione, sedeOperativa, totaleDocumento);
        if (!contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    ricevutaFiscale.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }
        PrimanotaFactory.newPrimanota(sx, ricevutaFiscale, sedeOperativa, null, null, user);
        MROldPartita partita = newPartita(sx, ricevutaFiscale, null, null);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        sx.saveOrUpdate(partita.getPrimanota());
        sx.saveOrUpdate(partita.getFattura());
        sx.saveOrUpdate(partita);

        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, prenotazione.getCommissione().getId());
        if (commissione != null) {
            if (commissione.getContratto() != null) {
                MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, commissione.getContratto().getId());
                prenotazione.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, prenotazione));
                prenotazione.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, prenotazione));
                ricevutaFiscale.setContratto(contratto);
            } else {
                prenotazione.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, null, prenotazione));
                prenotazione.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, null, prenotazione));
            }
        } else {
            prenotazione.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, null, prenotazione));
            prenotazione.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, null, prenotazione));
        }


        return partita;
    }

    /**
     * Crea una nuova fattura di acconto per gli acconti di un contratto, con la relativa partita.
     * @param sx
     * @param contratto Il contratto per il quale si desidera generare la fattura
     * @param sedeOperativa La sede operativa usata per la fatturazione.
     * @param contropartiteAperte Gli incassi per i quali generare la fattura di acconto.
     * @return La partita contenente la nuva fattura e la sua primanota (partita) non ancora salvate nel database e anche le contropartite degli incassi.
     */
    public static MROldPartita  newPartitaAcconto(Session sx, MROldContrattoNoleggio contratto, MROldSede sedeOperativa, List<MROldPrimanota> contropartiteAperte, User user) {
        Double totaleDocumento = 0.0;
        for (MROldPrimanota incasso : contropartiteAperte) {
            totaleDocumento = MathUtils.round(totaleDocumento + incasso.getTotaleDocumento());
        }
        MROldDocumentoFiscale fatturaAcconto = DocumentoFiscaleFactory.newFatturaAcconto(sx, contratto, sedeOperativa, totaleDocumento, user);
        if (contropartiteAperte!=null && !contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    fatturaAcconto.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }
        PrimanotaFactory.newPrimanota(sx, fatturaAcconto, sedeOperativa, null, null, user);
        MROldPartita partita = newPartita(sx, fatturaAcconto, null, null);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        sx.saveOrUpdate(partita.getPrimanota());
        sx.saveOrUpdate(partita.getFattura());
        sx.saveOrUpdate(partita);

        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, contratto.getCommissione().getId());
        if (commissione != null) {
            if (commissione.getPrenotazione() != null) {
                MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, p));
                contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, p));
                fatturaAcconto.setPrenotazione(p);
            } else {
                contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, null));
                contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, null));
            }
        } else {
            contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, null));
            contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, null));
        }


        return partita;
    }


    public static MROldPartita newPartitaAccontoEA(Session sx, MROldContrattoNoleggio contratto, MROldSede sedeOperativa,
                                                   Double remainingToPay, MROldPagamento pagamento, List<MROldPrimanota> contropartiteAperte, Integer numberToApply, User user) {
        Double totaleDocumento = 0.0;
        for (MROldPrimanota incasso : contropartiteAperte) {
            totaleDocumento = MathUtils.round(totaleDocumento + incasso.getTotaleDocumento());
        }
        MROldDocumentoFiscale fatturaAcconto = DocumentoFiscaleFactory.newCreateFatturaAccontoEA(sx, contratto, sedeOperativa, remainingToPay, numberToApply);
        if (!contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    fatturaAcconto.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }
        fatturaAcconto.setPagamento(pagamento);

        MROldFonteCommissione fonte = contratto.getCommissione().getFonteCommissione();
        MROldPartita partita = null;
        if(!"Z".equals(fonte.getRentalType().getDescription())){
            //normal enumeration logic (that will result in assigning EA - 10000000 enumeration
            PrimanotaFactory.newPrimanota(sx, fatturaAcconto, sedeOperativa, null, null, user);
            partita = newPartita(sx, fatturaAcconto, null, null);
        } else {
            //logic for Z enumeration
            MROldNumerazione numerazionePrimenote  = NumerazioniUtils.findOrCreateNumerazioneSede(sx, sedeOperativa, MROldNumerazione.PRIMENOTE);
            MROldNumerazione numerazionePartite  = NumerazioniUtils.findOrCreateNumerazioneSede(sx, sedeOperativa, MROldNumerazione.PARTITE);
            Integer iAnno = DayUtils.getFieldFromDate(new Date(), Calendar.YEAR);

            PrimanotaFactory.newPrimanota(sx, fatturaAcconto, sedeOperativa, numerazionePrimenote,
                    NumerazioniUtils.aggiornaProgressivo(sx, numerazionePrimenote, iAnno), user);
            partita = newPartita(sx, fatturaAcconto, numerazionePartite,
                    NumerazioniUtils.aggiornaProgressivo(sx, numerazionePartite, iAnno));
        }

        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        sx.saveOrUpdate(partita.getPrimanota());
        sx.saveOrUpdate(partita.getFattura());
        sx.saveOrUpdate(partita);

        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, contratto.getCommissione().getId());
        if (commissione != null) {
            if (commissione.getPrenotazione() != null) {
                MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, p));
                contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, p));
                fatturaAcconto.setPrenotazione(p);
            } else {
                contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, null));
                contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, null));
            }
        } else {
            contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, null));
            contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, null));
        }


        return partita;
    }

    /**
     * Questo metodo crea una fattura di saldo per il noleggio. Esclude dalla fatturazione le voci fatturate in precedenza. Associa le contropartite degli incassi.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La partita contenente la fattura creata.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldPartita newPartitaSaldo(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, List<MROldPartita> partiteAcconti, List<MROldPrimanota> contropartiteAperte, User user) throws BusinessRuleException, TariffaNonValidaException {
        MROldDocumentoFiscale newFattura = DocumentoFiscaleFactory.newIntestazioneFattura(sx, contrattoNoleggio, sedeOperativa, MROldDocumentoFiscale.FT, user);

        try {
            /* per test */

            MROldMovimentoAuto primoMovimento = null;
            MROldMovimentoAuto ultimoMovimento = null;

            contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contrattoNoleggio.getId());

            if (contrattoNoleggio.getMovimenti() != null && contrattoNoleggio.getMovimenti().size() >= 1) {
                List<MROldMovimentoAuto> listaMovimenti = new ArrayList<MROldMovimentoAuto>();
                listaMovimenti.addAll(contrattoNoleggio.getMovimenti());
                Collections.sort(listaMovimenti);
                primoMovimento = (MROldMovimentoAuto) listaMovimenti.get(0);
            }


            if (contrattoNoleggio.getMovimento().getUltimo() && contrattoNoleggio.getMovimento().getChiuso()) {
                ultimoMovimento = contrattoNoleggio.getMovimento();
            }


//            Date timestampOraInizio = FormattedDate.extractTime(contrattoNoleggio.getInizio());
//            Date timestampaDateFine = FormattedDate.extractDate(contrattoNoleggio.getFine());
//            Date timestampOreFine = FormattedDate.extractTime(contrattoNoleggio.getFine());

            Date timestampOraInizio = null;
            Date timestampaDateFine = null;
            Date timestampOreFine = null;
            if (Preferenze.getFatturaTuttaDurataNoleggio(sx)) {
                Date dataFineContratto = contrattoNoleggio.getFine();

                if (contrattoNoleggio.getMovimento().getUltimo()) {
                    ultimoMovimento = contrattoNoleggio.getMovimento();
                }
                if (ultimoMovimento != null && ultimoMovimento.getFine().getTime() > contrattoNoleggio.getFine().getTime()) {
                    dataFineContratto = ultimoMovimento.getFine();
                }
                timestampOraInizio = FormattedDate.extractTime(contrattoNoleggio.getInizio());
                timestampaDateFine = FormattedDate.extractDate(dataFineContratto);
                timestampOreFine = FormattedDate.extractTime(dataFineContratto);
            } else {
                timestampOraInizio = FormattedDate.extractTime(primoMovimento != null ? primoMovimento.getInizio() : contrattoNoleggio.getInizio());
                timestampaDateFine = FormattedDate.extractDate(ultimoMovimento != null ? ultimoMovimento.getFine() : contrattoNoleggio.getFine());
                timestampOreFine = FormattedDate.extractTime(ultimoMovimento != null ? ultimoMovimento.getFine() : contrattoNoleggio.getFine());
            }

            Date timestamp = null;

            if (Boolean.TRUE.equals(contrattoNoleggio.getTariffa().getOraRientroAttiva()) && contrattoNoleggio.getTariffa().getOraRientro() != null && (timestampOreFine.getTime() <= timestampOraInizio.getTime())) {
                timestampOreFine = contrattoNoleggio.getTariffa().getOraRientro();
            }

            timestamp = FormattedDate.createTimestamp(timestampaDateFine, timestampOreFine);

            /* fine test */

            /*
             * ATTENZIONE: Come data di inizio viene presaa la data di INIZIO CONTRATTO
             * e non la data di INIZIO DEL PRIMO MOVIMENTO
             *
             */
            //sx = HibernateBridge.refreshSessionSX(sx);
            Fatturazione fatturazione = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                    sx,
                    contrattoNoleggio,
                    contrattoNoleggio.getTariffa(),
                    contrattoNoleggio.getInizio(),
                    timestamp,
                    contrattoNoleggio.getScontoTariffa(),
                    contrattoNoleggio.getCommissione().getGiorniVoucher());
            newFattura.setFatturaRighe(fatturazione.calcolaRigheProssimaFattura(sx));
            newFattura.setInizioFatturazione(fatturazione.getInizioPeriodo());
            newFattura.setFineFatturazione(fatturazione.getFinePeriodo());

            //assegna la fattura anche alla prenotazione
            //sx = HibernateBridge.refreshSessionSX(sx);
            newFattura.setPrenotazione( PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));
            if(fatturazione.getInizioPeriodo() != null && fatturazione.getInizioPeriodo().after(FormattedDate.add(FormattedDate.formattedDate(), Calendar.DAY_OF_MONTH, 30)) && !Preferenze.getMonthlyRental(sx)) {
                throw new TitledException(bundle.getString("PartitaFactory.msgFatturazioneAnticipataSuperanteMassimoConsentito"), bundle.getString("PartitaFactory.msgPeriodoFatturatoNonValido"));
            }
        } catch (TitledException tex) {
            throw new BusinessRuleException(tex.getMessage(), tex.getTitle(), tex);
        }

        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        for (MROldPartita partita : partiteAcconti) {
            imposteFattura.subtract(partita.getFattura().getFatturaImposte());
        }
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

        /* se ci sono contropartite aperte => associo come forma di pagamento quello della prima contropartita aperta */
        if (contropartiteAperte != null && !contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            primanota = (MROldPrimanota) sx.get(MROldPrimanota.class, primanota.getId());
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    newFattura.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }

        //if the total of the invoice is zero, stop!
        if (newFattura.getTotaleFattura() != null && newFattura.getTotaleFattura() < 0.03) {
            throw new BusinessRuleException(bundle.getString("PartitaFactory.msgFatturaImportoZero"), bundle.getString("PartitaFactory.msgFatturaImportoZeroTitle"));
        }

        //if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
        if (contrattoNoleggio.getPagamento()!=null && Boolean.TRUE.equals(contrattoNoleggio.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));
        MROldPrimanota primanota = PrimanotaFactory.newPrimanota(sx, newFattura, sedeOperativa, null, null, user);
        sx.saveOrUpdate(primanota);
        sx.saveOrUpdate(newFattura);
        MROldPartita partita = PartitaFactory.newPartita(sx, newFattura, null, null);
        partita.setPrimanota(primanota);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        partita.getPartiteAcconto().addAll(partiteAcconti);
        sx.saveOrUpdate(partita);

        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, contrattoNoleggio.getCommissione().getId());
        if (commissione != null) {
            if (commissione.getPrenotazione() != null) {
                MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, p));
            } else {
                contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, null));
            }
        } else {
            contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, null));
        }

        if(contrattoNoleggio != null && contrattoNoleggio.getId() != null) {
            sx.merge(contrattoNoleggio);
        } else { //non dovrebbe mai arrivarci
            sx.saveOrUpdate(contrattoNoleggio);
        }

        return partita;
    }

    /**
     * Questo metodo crea una fattura di saldo per il noleggio. Esclude dalla fatturazione le voci fatturate in precedenza. Associa le contropartite degli incassi.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La partita contenente la fattura creata.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldPartita ricalcolaPartitaSaldo(Session sx, MROldPartita partita, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, List<MROldPartita> partiteAcconti, List<MROldPrimanota> contropartiteAperte, User user) throws BusinessRuleException, TariffaNonValidaException {
        MROldDocumentoFiscale newFattura = DocumentoFiscaleFactory.newIntestazioneFattura(sx, contrattoNoleggio, sedeOperativa, MROldDocumentoFiscale.FT, user);
        try {

             /* per test */

            MROldMovimentoAuto primoMovimento = null;
            MROldMovimentoAuto ultimoMovimento = null;

            if (contrattoNoleggio.getMovimenti() != null && contrattoNoleggio.getMovimenti().size() >= 1) {
                List<MROldMovimentoAuto> listaMovimenti = new ArrayList<MROldMovimentoAuto>();
                listaMovimenti.addAll(contrattoNoleggio.getMovimenti());
                Collections.sort(listaMovimenti);
                primoMovimento = (MROldMovimentoAuto) listaMovimenti.get(0);
            }


            if (contrattoNoleggio.getMovimento().getUltimo() && contrattoNoleggio.getMovimento().getChiuso()) {
                ultimoMovimento = contrattoNoleggio.getMovimento();
            }


//            Date timestampOraInizio = FormattedDate.extractTime(contrattoNoleggio.getInizio());
//            Date timestampaDateFine = FormattedDate.extractDate(contrattoNoleggio.getFine());
//            Date timestampOreFine = FormattedDate.extractTime(contrattoNoleggio.getFine());

            Date timestampOraInizio = null;
            Date timestampaDateFine = null;
            Date timestampOreFine = null;
            if (Preferenze.getFatturaTuttaDurataNoleggio(sx)) {
                Date dataFineContratto = contrattoNoleggio.getFine();

                if (contrattoNoleggio.getMovimento().getUltimo()) {
                    ultimoMovimento = contrattoNoleggio.getMovimento();
                }
                if (ultimoMovimento != null && ultimoMovimento.getFine().getTime() > contrattoNoleggio.getFine().getTime()) {
                    dataFineContratto = ultimoMovimento.getFine();
                }
                timestampOraInizio = FormattedDate.extractTime(contrattoNoleggio.getInizio());
                timestampaDateFine = FormattedDate.extractDate(dataFineContratto);
                timestampOreFine = FormattedDate.extractTime(dataFineContratto);
            } else {
                timestampOraInizio = FormattedDate.extractTime(primoMovimento != null ? primoMovimento.getInizio() : contrattoNoleggio.getInizio());
                timestampaDateFine = FormattedDate.extractDate(ultimoMovimento != null ? ultimoMovimento.getFine() : contrattoNoleggio.getFine());
                timestampOreFine = FormattedDate.extractTime(ultimoMovimento != null ? ultimoMovimento.getFine() : contrattoNoleggio.getFine());
            }



//            Date timestampOraInizio = FormattedDate.extractTime(primoMovimento!=null?primoMovimento.getInizio():contrattoNoleggio.getInizio());
//            Date timestampaDateFine = FormattedDate.extractDate(ultimoMovimento!=null?ultimoMovimento.getFine():contrattoNoleggio.getFine());
//            Date timestampOreFine = FormattedDate.extractTime(ultimoMovimento!=null?ultimoMovimento.getFine():contrattoNoleggio.getFine());

            Date timestamp = null;

            if (Boolean.TRUE.equals(contrattoNoleggio.getTariffa().getOraRientroAttiva()) && contrattoNoleggio.getTariffa().getOraRientro() != null && (timestampOreFine.getTime() <= timestampOraInizio.getTime())) {
                timestampOreFine = contrattoNoleggio.getTariffa().getOraRientro();
            }

            timestamp = FormattedDate.createTimestamp(timestampaDateFine, timestampOreFine);

            /* fine test */


            Fatturazione fatturazione = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                    sx,
                    contrattoNoleggio,
                    contrattoNoleggio.getTariffa(),
                    contrattoNoleggio.getInizio(),
                    timestamp,
                    contrattoNoleggio.getScontoTariffa(),
                    contrattoNoleggio.getCommissione().getGiorniVoucher());
            newFattura.setFatturaRighe(fatturazione.ricalcolaRigheUltimaFattura(sx, partita.getFattura()));
            newFattura.setInizioFatturazione(fatturazione.getInizioPeriodo());
            newFattura.setFineFatturazione(fatturazione.getFinePeriodo());
        } catch (TitledException tex) {
            throw new BusinessRuleException(tex.getMessage(), tex.getTitle(), tex);
        }//FIXME Qui bisogna ricalcolare l'ultima fattura, quindi leggere i documenti precedenti.
        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        for (MROldPartita aPartita : partiteAcconti) {
            imposteFattura.subtract(aPartita.getFattura().getFatturaImposte());
        }
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

        /* se ci sono contropartite aperte => associo come forma di pagamento quello della prima contropartita aperta */
        if (!contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    newFattura.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }

        /* se la fattura ha le scadenza ma il pagamento non lo prevede allora vengono cancellate altrimenti vengono create */
        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        } else {
            newFattura.getScadenze().clear();
        }

        newFattura.setNumero(partita.getFattura().getNumero());
        newFattura.setPrefisso(partita.getFattura().getPrefisso());
        newFattura.setData(partita.getFattura().getData());
        MROldPrimanota primanota = PrimanotaFactory.newPrimanota(sx, newFattura, sedeOperativa, partita.getPrimanota().getNumerazione(), partita.getPrimanota().getNumeroRegistrazione(), user);
        sx.saveOrUpdate(primanota);
        sx.saveOrUpdate(newFattura);
        MROldPartita partitaSaldo = PartitaFactory.newPartita(sx, newFattura, partita.getNumerazione(), partita.getNumero());
        partitaSaldo.setPrimanota(primanota);
        partitaSaldo.getContropartite().addAll(contropartiteAperte);
        partitaSaldo.aggiornaSaldo();
        partitaSaldo.aggiornaChiusura();
        partitaSaldo.getPartiteAcconto().addAll(partiteAcconti);
        sx.saveOrUpdate(partitaSaldo);
        sx.delete(partita);
        sx.delete(partita.getFattura().getPrimanota());
        sx.delete(partita.getFattura());

        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, contrattoNoleggio.getCommissione().getId());
        if (commissione != null) {
            if (commissione.getPrenotazione() != null) {
                MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, p));
                contrattoNoleggio.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contrattoNoleggio, p));
            } else {
                contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, null));
                contrattoNoleggio.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contrattoNoleggio, null));
            }
        } else {
            contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, null));
            contrattoNoleggio.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contrattoNoleggio, null));
        }

        sx.saveOrUpdate(contrattoNoleggio);
        return partitaSaldo;
    }

    /**
     * crea una nuova ricevuta fiscale trattandola come una fattura di saldo
     * @param sx
     * @param contrattoNoleggio
     * @param sedeOperativa
     * @param contropartiteAperte
     * @return
     * @throws BusinessRuleException
     */
    public static MROldPartita newRicevutaFiscalePagataSaldo(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, List<MROldPrimanota> contropartiteAperte, User user) throws BusinessRuleException, TariffaNonValidaException {
        MROldDocumentoFiscale newFattura = DocumentoFiscaleFactory.newIntestazioneFattura(sx, contrattoNoleggio, sedeOperativa, MROldDocumentoFiscale.RF, user);
        try {
            /* per test */

            MROldMovimentoAuto primoMovimento = null;
            MROldMovimentoAuto ultimoMovimento = null;

            if (contrattoNoleggio.getMovimenti() != null && contrattoNoleggio.getMovimenti().size() >= 1) {
                List<MROldMovimentoAuto> listaMovimenti = new ArrayList<MROldMovimentoAuto>();
                listaMovimenti.addAll(contrattoNoleggio.getMovimenti());
                Collections.sort(listaMovimenti);
                primoMovimento = (MROldMovimentoAuto) listaMovimenti.get(0);
            }


            if (contrattoNoleggio.getMovimento().getUltimo() && contrattoNoleggio.getMovimento().getChiuso()) {
                ultimoMovimento = contrattoNoleggio.getMovimento();
            }


//            Date timestampOraInizio = FormattedDate.extractTime(contrattoNoleggio.getInizio());
//            Date timestampaDateFine = FormattedDate.extractDate(contrattoNoleggio.getFine());
//            Date timestampOreFine = FormattedDate.extractTime(contrattoNoleggio.getFine());

            Date timestampOraInizio = FormattedDate.extractTime(primoMovimento!=null?primoMovimento.getInizio():contrattoNoleggio.getInizio());
            Date timestampaDateFine = FormattedDate.extractDate(ultimoMovimento!=null?ultimoMovimento.getFine():contrattoNoleggio.getFine());
            Date timestampOreFine = FormattedDate.extractTime(ultimoMovimento!=null?ultimoMovimento.getFine():contrattoNoleggio.getFine());

            Date timestamp = null;

            if (Boolean.TRUE.equals(contrattoNoleggio.getTariffa().getOraRientroAttiva()) && contrattoNoleggio.getTariffa().getOraRientro() != null && (timestampOreFine.getTime() <= timestampOraInizio.getTime())) {
                timestampOreFine = contrattoNoleggio.getTariffa().getOraRientro();
            }

            timestamp = FormattedDate.createTimestamp(timestampaDateFine, timestampOreFine);

            /* fine test */

            /*
             * ATTENZIONE: Come data di inizio viene presaa la data di INIZIO CONTRATTO
             * e non la data di INIZIO DEL PRIMO MOVIMENTO
             *
             */
            Fatturazione fatturazione = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                    sx,
                    contrattoNoleggio,
                    contrattoNoleggio.getTariffa(),
                    contrattoNoleggio.getInizio(),
                    timestamp,
                    contrattoNoleggio.getScontoTariffa(),
                    contrattoNoleggio.getCommissione().getGiorniVoucher());
            newFattura.setFatturaRighe(fatturazione.calcolaRigheProssimaFattura(sx));
            newFattura.setInizioFatturazione(fatturazione.getInizioPeriodo());
            newFattura.setFineFatturazione(fatturazione.getFinePeriodo());

            //assegna la fattura anche alla prenotazione
            newFattura.setPrenotazione( PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));
            if(fatturazione.getInizioPeriodo() != null && fatturazione.getInizioPeriodo().after(FormattedDate.add(FormattedDate.formattedDate(), Calendar.DAY_OF_MONTH, 30))) {
                throw new TitledException(bundle.getString("PartitaFactory.msgFatturazioneAnticipataSuperanteMassimoConsentito"), bundle.getString("PartitaFactory.msgPeriodoFatturatoNonValido"));
            }
        } catch (TitledException tex) {
            throw new BusinessRuleException(tex.getMessage(), tex.getTitle(), tex);
        }

        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        List<MROldPartita> partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, contrattoNoleggio);
        for (MROldPartita partita : partiteAcconti) {
            imposteFattura.subtract(partita.getFattura().getFatturaImposte());
        }
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }

        /* se ci sono contropartite aperte => associo come forma di pagamento quello della prima contropartita aperta */
        if (!contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    newFattura.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }

        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));
        MROldPrimanota primanota = PrimanotaFactory.newPrimanota(sx, newFattura, sedeOperativa, null, null, user);
        sx.saveOrUpdate(primanota);
        sx.saveOrUpdate(newFattura);
        MROldPartita partita = PartitaFactory.newPartita(sx, newFattura, null, null);
        partita.setPrimanota(primanota);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        partita.getPartiteAcconto().addAll(partiteAcconti);
        sx.saveOrUpdate(partita);

        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, contrattoNoleggio.getCommissione().getId());
        if (commissione != null) {
            if (commissione.getPrenotazione() != null) {
                MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, p));
            } else {
                contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, null));
            }
        } else {
            contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, null));
        }

        sx.saveOrUpdate(contrattoNoleggio);
        return partita;
    }

    /**
     * Crea una nuova ricevuta fiscale per gli acconti di un contratto, con la relativa partita.
     * @param sx
     * @param contratto Il contratto per il quale si desidera generare la fattura
     * @param sedeOperativa La sede operativa usata per la fatturazione.
     * @param contropartiteAperte Gli incassi per i quali generare la fattura di acconto.
     * @return La partita contenente la nuova fattura e la sua primanota (partita) non ancora salvate nel database e anche le contropartite degli incassi.
     */
    public static MROldPartita newRicevutaFiscalePagata(Session sx, MROldContrattoNoleggio contratto, MROldSede sedeOperativa, List<MROldPrimanota> contropartiteAperte, User user) {
        Double totaleDocumento = 0.0;
        for (MROldPrimanota incasso : contropartiteAperte) {
            totaleDocumento = MathUtils.round(totaleDocumento + incasso.getTotaleDocumento());
        }
        MROldDocumentoFiscale ricevutaFiscale = DocumentoFiscaleFactory.newRicevutaFiscalePagata(sx, contratto, sedeOperativa, totaleDocumento, user);
        if (!contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    ricevutaFiscale.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }
        PrimanotaFactory.newPrimanota(sx, ricevutaFiscale, sedeOperativa, null, null, user);
        MROldPartita partita = newPartita(sx, ricevutaFiscale, null, null);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        sx.saveOrUpdate(partita.getPrimanota());
        sx.saveOrUpdate(partita.getFattura());
        sx.saveOrUpdate(partita);

        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, contratto.getCommissione().getId());
        if (commissione != null) {
            if (commissione.getPrenotazione() != null) {
                MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, p));
                contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, p));
                ricevutaFiscale.setPrenotazione(p);
            } else {
                contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, null));
                contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, null));
            }
        } else {
            contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, null));
            contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, null));
        }


        return partita;
    }

    public static MROldPartita newPartitaSaldoFatturaProforma(Session sx, MROldDocumentoFiscale newFattura, Boolean prepagato,
                                                              MROldNumerazione numerazionePartite, Integer numeroPartita, List<MROldPartita> partiteAcconti) throws BusinessRuleException {

//        Session sxPrimanota = HibernateBridge.startNewSession();
        if (newFattura.getFatturaRighe() == null || newFattura.getFatturaRighe().isEmpty()) {
            throw new BusinessRuleException(bundle.getString("PartitaFactory.msgNessunaVoceDaFatturare"), bundle.getString("PartitaFactory.msgFatturaVuotaTitle"));
        }
        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        for (MROldPartita partita : partiteAcconti) {
            imposteFattura.subtract(partita.getFattura().getFatturaImposte());
        }
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

        MROldPagamento refreshedPagamentoFattura = null;

        Boolean scadenzario = true;
        if (newFattura != null && newFattura.getId() != null) {
            refreshedPagamentoFattura = (MROldPagamento) sx.get(MROldPagamento.class, newFattura.getPagamento().getId());
        }
        if (refreshedPagamentoFattura == null) {
            refreshedPagamentoFattura = newFattura.getPagamento();
            scadenzario = false;
        }

        if (newFattura.getPagamento() != null && Boolean.TRUE.equals(scadenzario)) {
            ScadenzaUtils.creaScadenze(newFattura);
        }

        MROldPartita partita = null;

        partita = PartitaFactory.newPartita(sx, newFattura, numerazionePartite, numeroPartita);

        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        partita.getPartiteAcconto().addAll(partiteAcconti);

        if (!prepagato) {

            MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, newFattura.getContratto().getCommissione().getId());
            if (commissione != null) {
                if (commissione.getPrenotazione() != null) {
                    MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                    newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), p));
                } else {
                    newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), null));
                }
            } else {
                newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), null));
            }

            sx.merge(newFattura.getContratto());
        }
        return partita;
    }


    public static MROldPartita newPartitaSaldoNoSaveInSession(Session sx, MROldDocumentoFiscale newFattura, MROldSede sedeOperativa, Boolean prepagato, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione, MROldNumerazione numerazionePartite, Integer numeroPartita, List<MROldPartita> partiteAcconti, List<MROldPrimanota> contropartiteAperte, MROldParcoVeicoli veicolo) throws BusinessRuleException {

        if (newFattura.getFatturaRighe() == null || newFattura.getFatturaRighe().isEmpty()) {
            throw new BusinessRuleException(bundle.getString("PartitaFactory.msgNessunaVoceDaFatturare"), bundle.getString("PartitaFactory.msgFatturaVuotaTitle"));
        }


        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());

        for (MROldPartita partita : partiteAcconti) {

            //imposteFattura.subtract(partita.getFattura().getFatturaImposte());

        }


        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

        MROldPagamento refreshedPagamentoFattura = null;

        Boolean scadenzario = true;
        if(newFattura != null && newFattura.getId() != null){
            refreshedPagamentoFattura = (MROldPagamento) sx.get(MROldPagamento.class, newFattura.getPagamento().getId());
        }
        if(refreshedPagamentoFattura == null){
            refreshedPagamentoFattura = newFattura.getPagamento();
            scadenzario = false;
        }


        if (newFattura.getPagamento() != null && Boolean.TRUE.equals(scadenzario)) {
            ScadenzaUtils.creaScadenze(newFattura);
        }


//        sxPrimanota = HibernateBridge.startNewSession();
        MROldPrimanota primanota = PrimanotaFactory.newPrimanota(sx, newFattura, sedeOperativa, numerazioneRegistrazione, numeroRegistrazione, veicolo);
        MROldPartita partita = null;
//        Transaction txPrimanota = null;
//        try{
//            txPrimanota = sx.beginTransaction();
//            sx.saveOrUpdate(primanota);
//            sx.saveOrUpdate(newFattura);
        partita = PartitaFactory.newPartita(sx, newFattura, numerazionePartite, numeroPartita);
        partita.setPrimanota(primanota);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        partita.getPartiteAcconto().addAll(partiteAcconti);
//            sx.saveOrUpdate(partita);

//            txPrimanota.commit();
//        }
//        catch(Exception ex){
//            if(txPrimanota != null){
//                txPrimanota.rollback();
//            }
//            ex.printStackTrace();
//        }
//        finally{
////            if(sx != null & sx.isOpen()){
////                sx.close();
////            }
//        }
//        sx.saveOrUpdate(partita);
        if (!prepagato) {

            MROldCommissione commissione=null;
            if (newFattura.getContratto()!=null)
                commissione = (MROldCommissione) sx.get(MROldCommissione.class, newFattura.getContratto().getCommissione().getId());
            if (commissione != null) {
                if (commissione.getPrenotazione() != null) {
                    MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                    newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), p));
                } else {
                    newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), null));
                }
            } else if (newFattura.getRent2rent()==null){
                newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), null));
            } else {
                //KPRE: nothing to do
            }

            if (newFattura.getRent2rent()==null)
                sx.merge(newFattura.getContratto());
        }
        return partita;
    }


    /**
     * Questo metodo crea una fattura di saldo per il noleggio (prepagato).  Esclude dalla fatturazione le voci fatturate in precedenza. Associa le contropartite degli incassi.
     * @param newFattura La fattura contenente tutti i dati necessari (intestazione e corpo).
     * @param prepagato Indica se la fattura e' una prepagata.
     * @param numerazioneRegistrazione La numerazione usata per la registrazione contabile (primanota)
     * @param numeroRegistrazione Il numero di registrazione contabile.
     * @param numerazionePartite La numerazione per la registrazione della partita.
     * @param numeroPartita Il numero della partita.
     * @param partiteAcconti Per fatture non prepagate, l'elenco delle eventuali partite di acconto
     * @param contropartiteAperte Per fatture non prepagate, l'elenco dei pagamenti aperti per questo contratto.
     * @return La partita creata e salvata nel database.
     * @throws Exception Se la fattura non ha alcuna riga.
     */
    public static MROldPartita newPartitaSaldoNoSaveInSession(Session sx, MROldDocumentoFiscale newFattura, MROldSede sedeOperativa, Boolean prepagato, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione, MROldNumerazione numerazionePartite, Integer numeroPartita, List<MROldPartita> partiteAcconti, List<MROldPrimanota> contropartiteAperte, User user) throws BusinessRuleException {

//        Session sxPrimanota = HibernateBridge.startNewSession();
        if (newFattura.getFatturaRighe() == null || newFattura.getFatturaRighe().isEmpty()) {
            throw new BusinessRuleException(bundle.getString("PartitaFactory.msgNessunaVoceDaFatturare"), bundle.getString("PartitaFactory.msgFatturaVuotaTitle"));
        }
        List<MROldRigaDocumentoFiscale> oldFattureRigheAcconto = new ArrayList<MROldRigaDocumentoFiscale>();
        Double amountAlreadySubtract = 0.0;
        List<Map<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale>> oldImposte = new ArrayList<Map<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale>>();
        Query q = sx.createQuery("from MROldDocumentoFiscale d where d.prepagato = :prepagato AND d.contratto = :contratto AND d.discriminator = :tipoFattura");
        q.setParameter("prepagato", false);
        q.setParameter("contratto",  newFattura.getContratto());
        q.setParameter("tipoFattura", "Fattura");

        List<MROldDocumentoFiscale> oldFatture = q.list();



//        List<Partita> usedPartite = new ArrayList<Partita>();
//        if(oldFatture != null && oldFatture.size() > 0){
//            for(DocumentoFiscale d : oldFatture){
//                if(d != null && d.getPrimanota() != null){
//                    Primanota primanota = (Primanota) DatabaseUtils.refreshPersistentInstanceWithSx(sx, Primanota.class, d.getPrimanota());
//                    if(primanota.getPartita() != null){
//                        Partita partita = (Partita) DatabaseUtils.refreshPersistentInstanceWithSx(sx, Partita.class, d.getPrimanota().getPartita());
//                        usedPartite.add(partita);
//                    }
//
//                }
//
//            }
//        }
//        if(oldFatture != null && oldFatture.size() > 0){
//            for(DocumentoFiscale d: oldFatture){
//                if(d != null && d.getFatturaImposte()!= null){
////                    for(RigaImpostaDocumentoFiscale rd : d.getFatturaImposte().values()){
////                        amountAlreadySubtract += rd.getAcconto();
////                    }
//
//                    oldImposte.add(d.getFatturaImposte());
//                }
//            }
//        }


        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());

        for (MROldPartita partita : partiteAcconti) {
            boolean canSubtract = true;
//            for(Partita partita2 : usedPartite){
//                if(partita.getId().equals(partita2.getId())){
//                    canSubtract = false;
//                }
//            }
//            if(canSubtract){
            imposteFattura.subtract(partita.getFattura().getFatturaImposte());
//            }
        }


        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

        MROldPagamento refreshedPagamentoFattura = null;

        Boolean scadenzario = true;
        if(newFattura != null && newFattura.getId() != null){
            refreshedPagamentoFattura = (MROldPagamento) sx.get(MROldPagamento.class, newFattura.getPagamento().getId());
        }
        if(refreshedPagamentoFattura == null){
            refreshedPagamentoFattura = newFattura.getPagamento();
            scadenzario = false;
        }


        if (newFattura.getPagamento() != null && Boolean.TRUE.equals(scadenzario)) {
            ScadenzaUtils.creaScadenze(newFattura);
        }


//        sxPrimanota = HibernateBridge.startNewSession();
        MROldPrimanota primanota = PrimanotaFactory.newPrimanota(sx, newFattura, sedeOperativa, numerazioneRegistrazione, numeroRegistrazione, user);
        MROldPartita partita = null;
//        Transaction txPrimanota = null;
//        try{
//            txPrimanota = sx.beginTransaction();
//            sx.saveOrUpdate(primanota);
//            sx.saveOrUpdate(newFattura);
        partita = PartitaFactory.newPartita(sx, newFattura, numerazionePartite, numeroPartita);
        partita.setPrimanota(primanota);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        partita.getPartiteAcconto().addAll(partiteAcconti);
//            sx.saveOrUpdate(partita);

//            txPrimanota.commit();
//        }
//        catch(Exception ex){
//            if(txPrimanota != null){
//                txPrimanota.rollback();
//            }
//            ex.printStackTrace();
//        }
//        finally{
////            if(sx != null & sx.isOpen()){
////                sx.close();
////            }
//        }
        sx.saveOrUpdate(partita);
        if (!prepagato) {

            MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, newFattura.getContratto().getCommissione().getId());
            if (commissione != null) {
                if (commissione.getPrenotazione() != null) {
                    MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                    newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), p));
                } else {
                    newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), null));
                }
            } else {
                newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), null));
            }

            sx.merge(newFattura.getContratto());
        }
        return partita;
    }

    /**
     * Crea una nuova fattura di acconto per gli acconti di una prenotazione, con la relativa partita.
     * @param sx
     * @param prenotazione Il contratto per il quale si desidera generare la fattura
     * @param sedeOperativa La sede operativa usata per la fatturazione.
     * @param contropartiteAperte Gli incassi per i quali generare la fattura di acconto.
     * @return La partita contenente la nuova fattura e la sua primanota (partita) non ancora salvate nel database e anche le contropartite degli incassi.
     */
    public static MROldPartita newPartitaAcconto(Session sx, MROldPrenotazione prenotazione, MROldSede sedeOperativa, List<MROldPrimanota> contropartiteAperte, User user) {
        Double totaleDocumento = 0.0;
        for (MROldPrimanota incasso : contropartiteAperte) {
            totaleDocumento = MathUtils.round(totaleDocumento + incasso.getTotaleDocumento());
        }
        MROldDocumentoFiscale fatturaAcconto = DocumentoFiscaleFactory.newFatturaAcconto(sx, prenotazione, sedeOperativa, totaleDocumento);
        if (!contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    fatturaAcconto.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }
        PrimanotaFactory.newPrimanota(sx, fatturaAcconto, sedeOperativa, null, null, user);
        MROldPartita partita = newPartita(sx, fatturaAcconto, null, null);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        sx.saveOrUpdate(partita.getPrimanota());
        sx.saveOrUpdate(partita.getFattura());
        sx.saveOrUpdate(partita);

        if (prenotazione.getCommissione() != null && prenotazione.getCommissione().getContratto() != null) {
            prenotazione.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, prenotazione.getCommissione().getContratto(), prenotazione));
            prenotazione.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, prenotazione.getCommissione().getContratto(), prenotazione));
            fatturaAcconto.setContratto(prenotazione.getCommissione().getContratto());
        } else {
            prenotazione.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, null, prenotazione));
            prenotazione.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, null, prenotazione));
        }

        return partita;
    }

    /**
     * Questo metodo crea una fattura di saldo per il noleggio. Esclude dalla fatturazione le voci fatturate in precedenza. Associa le contropartite degli incassi.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La partita contenente la fattura creata.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldPartita newPartitaSaldoProforma(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, List<MROldPartita> partiteAcconti, List<MROldPrimanota> contropartiteAperte, User user) throws BusinessRuleException, TariffaNonValidaException {
        MROldDocumentoFiscale newFattura = DocumentoFiscaleFactory.newIntestazioneFattura(sx, contrattoNoleggio, sedeOperativa, MROldDocumentoFiscale.FTP, user);

        try {
            /* per test */

            MROldMovimentoAuto primoMovimento = null;
            MROldMovimentoAuto ultimoMovimento = null;

            contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contrattoNoleggio.getId());

            if (contrattoNoleggio.getMovimenti() != null && contrattoNoleggio.getMovimenti().size() >= 1) {
                List<MROldMovimentoAuto> listaMovimenti = new ArrayList<MROldMovimentoAuto>();
                listaMovimenti.addAll(contrattoNoleggio.getMovimenti());
                Collections.sort(listaMovimenti);
                primoMovimento = (MROldMovimentoAuto) listaMovimenti.get(0);
            }


            if (contrattoNoleggio.getMovimento().getUltimo() && contrattoNoleggio.getMovimento().getChiuso()) {
                ultimoMovimento = contrattoNoleggio.getMovimento();
            }


//            Date timestampOraInizio = FormattedDate.extractTime(contrattoNoleggio.getInizio());
//            Date timestampaDateFine = FormattedDate.extractDate(contrattoNoleggio.getFine());
//            Date timestampOreFine = FormattedDate.extractTime(contrattoNoleggio.getFine());

            Date timestampOraInizio = null;
            Date timestampaDateFine = null;
            Date timestampOreFine = null;
            if (Preferenze.getFatturaTuttaDurataNoleggio(sx)) {
                Date dataFineContratto = contrattoNoleggio.getFine();

                if (contrattoNoleggio.getMovimento().getUltimo()) {
                    ultimoMovimento = contrattoNoleggio.getMovimento();
                }
                if (ultimoMovimento != null && ultimoMovimento.getFine().getTime() > contrattoNoleggio.getFine().getTime()) {
                    dataFineContratto = ultimoMovimento.getFine();
                }
                timestampOraInizio = FormattedDate.extractTime(contrattoNoleggio.getInizio());
                timestampaDateFine = FormattedDate.extractDate(dataFineContratto);
                timestampOreFine = FormattedDate.extractTime(dataFineContratto);
            } else {
                timestampOraInizio = FormattedDate.extractTime(primoMovimento != null ? primoMovimento.getInizio() : contrattoNoleggio.getInizio());
                timestampaDateFine = FormattedDate.extractDate(ultimoMovimento != null ? ultimoMovimento.getFine() : contrattoNoleggio.getFine());
                timestampOreFine = FormattedDate.extractTime(ultimoMovimento != null ? ultimoMovimento.getFine() : contrattoNoleggio.getFine());
            }

            Date timestamp = null;

            if (Boolean.TRUE.equals(contrattoNoleggio.getTariffa().getOraRientroAttiva()) && contrattoNoleggio.getTariffa().getOraRientro() != null && (timestampOreFine.getTime() <= timestampOraInizio.getTime())) {
                timestampOreFine = contrattoNoleggio.getTariffa().getOraRientro();
            }

            timestamp = FormattedDate.createTimestamp(timestampaDateFine, timestampOreFine);

            /* fine test */

            /*
             * ATTENZIONE: Come data di inizio viene presaa la data di INIZIO CONTRATTO
             * e non la data di INIZIO DEL PRIMO MOVIMENTO
             *
             */
            //sx = HibernateBridge.refreshSessionSX(sx);
            Fatturazione fatturazione = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                    sx,
                    contrattoNoleggio,
                    contrattoNoleggio.getTariffa(),
                    contrattoNoleggio.getInizio(),
                    timestamp,
                    contrattoNoleggio.getScontoTariffa(),
                    contrattoNoleggio.getCommissione().getGiorniVoucher());
            newFattura.setFatturaRighe(fatturazione.calcolaRigheProssimaFattura(sx));
            newFattura.setInizioFatturazione(fatturazione.getInizioPeriodo());
            newFattura.setFineFatturazione(fatturazione.getFinePeriodo());

            //assegna la fattura anche alla prenotazione
            //sx = HibernateBridge.refreshSessionSX(sx);
            newFattura.setPrenotazione( PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));
            if(fatturazione.getInizioPeriodo() != null && fatturazione.getInizioPeriodo().after(FormattedDate.add(FormattedDate.formattedDate(), Calendar.DAY_OF_MONTH, 30))) {
                throw new TitledException(bundle.getString("PartitaFactory.msgFatturazioneAnticipataSuperanteMassimoConsentito"), bundle.getString("PartitaFactory.msgPeriodoFatturatoNonValido"));
            }
        } catch (TitledException tex) {
            throw new BusinessRuleException(tex.getMessage(), tex.getTitle(), tex);
        }

        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        for (MROldPartita partita : partiteAcconti) {
            imposteFattura.subtract(partita.getFattura().getFatturaImposte());
        }
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

//        /* se ci sono contropartite aperte => associo come forma di pagamento quello della prima contropartita aperta */
//        if (contropartiteAperte != null && !contropartiteAperte.isEmpty()) {
//            MROldPrimanota primanota = contropartiteAperte.iterator().next();
//            primanota = (MROldPrimanota) sx.get(MROldPrimanota.class, primanota.getId());
//            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
//                if (riga.getPagamento() != null) {
//                    newFattura.setPagamento(riga.getPagamento());
//                    break;
//                }
//            }
//        }

        //if the total of the invoice is zero, stop!
        if (newFattura.getTotaleFattura() != null && newFattura.getTotaleFattura() < 0.03) {
            throw new BusinessRuleException(bundle.getString("PartitaFactory.msgFatturaImportoZero"), bundle.getString("PartitaFactory.msgFatturaImportoZeroTitle"));
        }

        //if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
        if (contrattoNoleggio.getPagamento()!=null && Boolean.TRUE.equals(contrattoNoleggio.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));
        //MROldPrimanota primanota = PrimanotaFactory.newPrimanota(sx, newFattura, sedeOperativa, null, null, user);
        //sx.saveOrUpdate(primanota);
        sx.saveOrUpdate(newFattura);
        MROldPartita partita = PartitaFactory.newPartita(sx, newFattura, null, null);
        //partita.setPrimanota(primanota);
        //partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        partita.getPartiteAcconto().addAll(partiteAcconti);
        sx.saveOrUpdate(partita);

        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, contrattoNoleggio.getCommissione().getId());
        if (commissione != null) {
            if (commissione.getPrenotazione() != null) {
                MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, p));
            } else {
                contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, null));
            }
        } else {
            contrattoNoleggio.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contrattoNoleggio, null));
        }

        if(contrattoNoleggio != null && contrattoNoleggio.getId() != null) {
            sx.merge(contrattoNoleggio);
        } else { //non dovrebbe mai arrivarci
            sx.saveOrUpdate(contrattoNoleggio);
        }

        return partita;
    }

    /**
     * Crea una nuova fattura di acconto per gli acconti di un contratto, con la relativa partita.
     * @param sx
     * @param contratto Il contratto per il quale si desidera generare la fattura
     * @param sedeOperativa La sede operativa usata per la fatturazione.
     * @param contropartiteAperte Gli incassi per i quali generare la fattura di acconto.
     * @return La partita contenente la nuva fattura e la sua primanota (partita) non ancora salvate nel database e anche le contropartite degli incassi.
     */
    public static MROldPartita  newPartitaAccontoProforma(Session sx, MROldContrattoNoleggio contratto, MROldSede sedeOperativa, List<MROldPrimanota> contropartiteAperte, User user) {
        Double totaleDocumento = 0.0;
//        for (MROldPrimanota incasso : contropartiteAperte) {
//            totaleDocumento = MathUtils.round(totaleDocumento + incasso.getTotaleDocumento());
//        }
        MROldDocumentoFiscale fatturaAcconto = DocumentoFiscaleFactory.newFatturaProformaAcconto(sx, contratto, sedeOperativa, totaleDocumento, user);
        if (contropartiteAperte!=null && !contropartiteAperte.isEmpty()) {
            MROldPrimanota primanota = contropartiteAperte.iterator().next();
            for (RigaPrimanota riga : primanota.getRighePrimanota()) {
                if (riga.getPagamento() != null) {
                    fatturaAcconto.setPagamento(riga.getPagamento());
                    break;
                }
            }
        }
        //PrimanotaFactory.newPrimanota(sx, fatturaAcconto, sedeOperativa, null, null, user);
        MROldPartita partita = newPartita(sx, fatturaAcconto, null, null);
        if (contropartiteAperte != null) {
            partita.getContropartite().addAll(contropartiteAperte);
        }
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        //sx.saveOrUpdate(partita.getPrimanota());
        sx.saveOrUpdate(partita.getFattura());
        sx.saveOrUpdate(partita);

        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, contratto.getCommissione().getId());
        if (commissione != null) {
            if (commissione.getPrenotazione() != null) {
                MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, p));
                contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, p));
                fatturaAcconto.setPrenotazione(p);
            } else {
                contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, null));
                contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, null));
            }
        } else {
            contratto.setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, contratto, null));
            contratto.setSaldoAcconti(ContabUtils.totaleFattureAcconto(sx, contratto, null));
        }


        return partita;
    }

    /**
     * Questo metodo crea una fattura di saldo per il noleggio (prepagato).  Esclude dalla fatturazione le voci fatturate in precedenza. Associa le contropartite degli incassi.
     * @param newFattura La fattura contenente tutti i dati necessari (intestazione e corpo).
     * @param prepagato Indica se la fattura e' una prepagata.
     * @param numerazioneRegistrazione La numerazione usata per la registrazione contabile (primanota)
     * @param numeroRegistrazione Il numero di registrazione contabile.
     * @param numerazionePartite La numerazione per la registrazione della partita.
     * @param numeroPartita Il numero della partita.
     * @param partiteAcconti Per fatture non prepagate, l'elenco delle eventuali partite di acconto
     * @param contropartiteAperte Per fatture non prepagate, l'elenco dei pagamenti aperti per questo contratto.
     * @return La partita creata e salvata nel database.
     * @throws Exception Se la fattura non ha alcuna riga.
     */
    public static MROldPartita newPartitaSaldo(Session sx, MROldDocumentoFiscale newFattura, Boolean prepagato, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione, MROldNumerazione numerazionePartite, Integer numeroPartita, List<MROldPartita> partiteAcconti, List<MROldPrimanota> contropartiteAperte, User aUser) throws BusinessRuleException {
        if (newFattura.getFatturaRighe() == null || newFattura.getFatturaRighe().isEmpty()) {
            throw new BusinessRuleException(bundle.getString("PartitaFactory.msgNessunaVoceDaFatturare"), bundle.getString("PartitaFactory.msgFatturaVuotaTitle"));
        }
        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        for (MROldPartita partita : partiteAcconti) {
            imposteFattura.subtract(partita.getFattura().getFatturaImposte());
        }
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));

        if (newFattura.getPagamento()!=null  && Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }

        MROldPrimanota primanota = PrimanotaFactory.newPrimanota(sx, newFattura, null, numerazioneRegistrazione, numeroRegistrazione, aUser);
        sx.saveOrUpdate(primanota);
        sx.saveOrUpdate(newFattura);
        MROldPartita partita = PartitaFactory.newPartita(sx, newFattura, numerazionePartite, numeroPartita);
        partita.setPrimanota(primanota);
        partita.getContropartite().addAll(contropartiteAperte);
        partita.aggiornaSaldo();
        partita.aggiornaChiusura();
        partita.getPartiteAcconto().addAll(partiteAcconti);
        sx.saveOrUpdate(partita);
        if (!prepagato) {

            MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, newFattura.getContratto().getCommissione().getId());
            if (commissione != null) {
                if (commissione.getPrenotazione() != null) {
                    MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
                    newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), p));
                } else {
                    newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), null));
                }
            } else {
                newFattura.getContratto().setSaldoFatture(ContabUtils.totaleFattureSaldo(sx, newFattura.getContratto(), null));
            }

            sx.saveOrUpdate(newFattura.getContratto());
        }
        return partita;
    }
}
