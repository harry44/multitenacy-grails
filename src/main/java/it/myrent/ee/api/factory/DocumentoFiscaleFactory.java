/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.api.factory;

import grails.util.Holders;
import it.aessepi.myrentcs.utils.AbstractFatturazione;
import it.aessepi.myrentcs.utils.FatturaUtils;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.Parameters;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.BusinessRuleException;
import it.myrent.ee.api.exception.DatabaseException;
import it.myrent.ee.api.exception.TitledException;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.api.utils.*;
import it.myrent.ee.db.*;
import it.myrent.ee.db.MROldAffiliato;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;

/**
 *
 * @author giacomo
 */
public class DocumentoFiscaleFactory {
    private static final Log log = LogFactory.getLog(DocumentoFiscaleFactory.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/factory/Bundle");
    /**
     * Questo metodo crea una fattura finale per la prenotazione. Esclude dalla fatturazione le voci fatturate in precedenza.
     * @param prenotazione Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La fattura creata e salvata nel database.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldDocumentoFiscale newFatturaSaldo(Session sx, Transaction tx, MROldPrenotazione prenotazione, MROldSede sedeOperativa, User user) {
        MROldDocumentoFiscale newFattura = null;
        try {
//            MROldCurrencyConversionRate ccr = null;
//            if (prenotazione.getConversionRate() != null) {
//                ccr = (MROldCurrencyConversionRate) sx.get(MROldCurrencyConversionRate.class, prenotazione.getConversionRate().getId());
//            }
            List<MROldPartita> partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, prenotazione);
            List<MROldPrimanota> contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, prenotazione);
            newFattura = PartitaFactory.newPartitaSaldo(sx, prenotazione, sedeOperativa, partiteAcconti, contropartiteAperte, user).getFattura();
            newFattura.setConversionRate(prenotazione.getConversionRate());
            newFattura.getPrimanota().setConversionRate(prenotazione.getConversionRate());
            newFattura.setContratto(null);
        } catch (Exception ex) {
            //throw new DatabaseException(ex);
            ex.printStackTrace();
        }
        return newFattura;
    }

   /**
     * Crea l'intestazione della fattura per una prenotazione, senza le righe e senza compilare i totali.
     * @param sx La sessione al database
     * @param prenotazione Il contratto di noleggio da fatturare
     * @param sedeOperativa La sede usata per la numerazione della fattura.
     * @param tipoDocumento Il tipo di documento da creare, uno tra i tipi definiti dentro MROldDocumentoFiscale
     * @return La fattura appena creata, non salvata nel database.
     */
    static MROldDocumentoFiscale newIntestazioneFattura(Session sx, MROldPrenotazione prenotazione, MROldSede sedeOperativa, String tipoDocumento) {
        //User aUser = (User) sx.get(UserImpl.class, Parameters.getUser().getId());
        prenotazione = (MROldPrenotazione) sx.get(MROldPrenotazione.class, prenotazione.getId());
        MROldAffiliato affiliato = prenotazione.getAffiliato();
        String tipoNumerazione;
        if (tipoDocumento.equals(MROldDocumentoFiscale.NC)) {
            tipoNumerazione = MROldNumerazione.NOTE_CREDITO_VENDITA;
        } else if (tipoDocumento.equals(MROldDocumentoFiscale.RF)) {
            tipoNumerazione = MROldNumerazione.CORRISPETTIVI;
        } else {
            tipoNumerazione = MROldNumerazione.VENDITE;
        }
        MROldNumerazione numerazione = NumerazioniUtils.getNumerazione(sx, sedeOperativa, tipoNumerazione);
        Date data = FormattedDate.formattedDate();
        Integer anno = FormattedDate.annoCorrente();
        Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
        String prefisso = numerazione.getPrefisso();
        MROldDocumentoFiscale newFattura = MROldDocumentoFiscale.creaDocumentoFiscale(
                affiliato,
                prenotazione.getUserApertura(),
                tipoDocumento,
                numerazione,
                prefisso,
                numero,
                data,
                anno);
        newFattura.setContratto(null);
        newFattura.setPrenotazione(prenotazione);
        if (prenotazione.getCliente() != null) {
            sx.evict(prenotazione.getCliente());
            newFattura.setCliente((MROldClienti) sx.get(MROldClienti.class, prenotazione.getCliente().getId()));
        }
        newFattura.setPagamento((MROldPagamento) sx.get(MROldPagamento.class, prenotazione.getPagamento().getId()));
        newFattura.setAnnotazioni(prenotazione.getNote());
        return newFattura;
    }

    /**
     * Questo metodo crea una ricevuta fiscale per il noleggio. Esclude dalla fatturazione le voci fatturate in precedenza.
     * @param prenotazione Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La fattura creata e salvata nel database.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    /*public static MROldDocumentoFiscale newRicevutaFiscalePagata(Session sx, MROldPrenotazione prenotazione, MROldSede sedeOperativa) throws TitledException, Exception {
        MROldDocumentoFiscale newFattura = null;
        Transaction tx = null;
        try {
            tx = sx.beginTransaction();
            prenotazione = (MROldPrenotazione) sx.get(MROldPrenotazione.class, prenotazione.getId());
            List<MROldPrimanota> contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, prenotazione);

            if (AbstractFatturazione.getRicevutaFiscaleComeFatturaDISaldo(sx)) {
                newFattura = PartitaFactory.newRicevutaFiscalePagataSaldo(sx, prenotazione, sedeOperativa, contropartiteAperte).getFattura();
            } else {
                if (contropartiteAperte.isEmpty()) {
                    throw new BusinessRuleException();
                }
                newFattura = PartitaFactory.newRicevutaFiscalePagata(sx, prenotazione, sedeOperativa, contropartiteAperte).getFattura();
            }
            newFattura.setConversionRate(prenotazione.getConversionRate());

            newFattura.setContratto(null);
            tx.commit();
        } catch (BusinessRuleException brex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception txx) {
                }
            }
            throw brex;
        } catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception txx) {
                }
            }
           
            throw new Exception();
        } finally {
            if (sx != null) {
                try {
                    sx.close();
                } catch (Exception sxx) {
                }
            }
        }
        return newFattura;
    }*/

    /**
     * Questo metodo crea una ricevuta fiscale per la prenotazione, senza la primanota e senza la partita. La fattura contiene una sola riga, dell'acconto.
     * @param sx La sessione al database.
     * @param prenotazione Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la fattura.
     * @return La fattura creata, ma non salvata nel database.
     */
    static MROldDocumentoFiscale newRicevutaFiscalePagata(Session sx, MROldPrenotazione prenotazione, MROldSede sedeOperativa, Double acconto) {
        MROldDocumentoFiscale newFattura = newIntestazioneFattura(sx, prenotazione, sedeOperativa, MROldDocumentoFiscale.RF);
        MROldPianoDeiConti contoContabile = ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.CLIENTI_C_ANTICIPI);
        newFattura.setFatturaRighe(new ArrayList());
        newFattura.getFatturaRighe().add(newRigaAcconto(bundle.getString("DocumentoFiscaleFactory.msgRicevutaFiscalePagata"), acconto, prenotazione.getTariffa().getCodiceIva(), contoContabile));
        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));

        if (prenotazione.getConversionRate() != null) {
            newFattura.setConversionRate(prenotazione.getConversionRate());
            newFattura.getPrimanota().setConversionRate(prenotazione.getConversionRate());
        }

        //assegna la fattura alla prenotazione se esiste
        newFattura.setContratto(null);

        return newFattura;
    }

    /** Crea la riga di fattura per un singolo acconto.
     *
     * @param descrizioneAcconto La descrizione della riga di acconto.
     * @param totaleAcconto Il totale dell'acconto comprensivo di Iva.
     * @param codiceIva Il codice Iva da pplicare.
     * @param clientiContoAnticipi Il conto contabile per la registrazione dell'acconto.
     * @return La riga dell'acconto.
     */
    public static MROldRigaDocumentoFiscale newRigaAcconto(
            String descrizioneAcconto,
            Double totaleAcconto,
            MROldCodiciIva codiceIva,
            MROldPianoDeiConti clientiContoAnticipi) {

        MROldRigaDocumentoFiscale rigaAcconto = new MROldRigaDocumentoFiscale();
        Double imponibile = MathUtils.round(MathUtils.scorporoIva(codiceIva.getAliquota(), totaleAcconto), 5);
        Double iva = MathUtils.round(imponibile * codiceIva.getAliquota(), 5);
        Double totale = imponibile + iva;

        rigaAcconto = new MROldRigaDocumentoFiscale();
        rigaAcconto.setDescrizione(descrizioneAcconto);
        rigaAcconto.setUnitaMisura(null);
        rigaAcconto.setQuantita(1.0);
        rigaAcconto.setPrezzoUnitario(imponibile);
        rigaAcconto.setTotaleImponibileRiga(imponibile);
        rigaAcconto.setTotaleIvaRiga(iva);
        rigaAcconto.setCodiceIva(codiceIva);
        rigaAcconto.setSconto(0.0);
        rigaAcconto.setScontoFisso(0.0);
        rigaAcconto.setCodiceSottoconto(clientiContoAnticipi);
        rigaAcconto.setTotaleRiga(totale);

        rigaAcconto.setTempoKm(Boolean.FALSE);
        rigaAcconto.setTempoExtra(Boolean.FALSE);
        rigaAcconto.setFranchigia(Boolean.FALSE);

        return rigaAcconto;
    }

    /**
     * Questo metodo crea una fattura finale per il noleggio. Esclude dalla fatturazione le voci fatturate in precedenza.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La fattura creata e salvata nel database.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldDocumentoFiscale newFatturaAcconto(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, User user) throws TitledException {
        MROldDocumentoFiscale newFattura = null;
        Transaction tx = null;
        try {
            contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contrattoNoleggio.getId());
            List<MROldPrimanota> contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, contrattoNoleggio);
            if (contropartiteAperte.isEmpty()) {
                throw new BusinessRuleException(
                        bundle.getString("DocumentoFiscaleFactory.msgNessunaFatturaAccontoDaEmettereMessage"),
                        bundle.getString("DocumentoFiscaleFactory.msgNessunaFatturaAccontoDaEmettereTitle"));
            }
            newFattura = PartitaFactory.newPartitaAcconto(sx, contrattoNoleggio, sedeOperativa, contropartiteAperte, user).getFattura();
            newFattura.setConversionRate(contrattoNoleggio.getConversionRate());

            newFattura.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));
            //tx.commit();
        } catch (BusinessRuleException brex) {
            brex.printStackTrace();

            if (tx != null) {
                try {
                    //tx.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            throw brex;
        } catch (Exception ex) {
            ex.printStackTrace();

            if (tx != null) {
                try {
                    //tx.rollback();
                } catch (Exception txx) {
                    txx.printStackTrace();
                }
            }
            log.error("Errore creazione fattura: " + ex.getMessage()); //NOI18N
            log.debug("Errore creazione fattura: ", ex); //NOI18N
            throw new DatabaseException(ex);
        } finally {
            if (sx != null) {
                try {
                    //sx.close();
                } catch (Exception sxx) {
                    sxx.printStackTrace();
                }
            }
        }
        return newFattura;
    }

    /**
     * Questo metodo crea una fattura di acconto per il noleggio, senza la primanota e senza la partita. La fattura contiene una sola riga, dell'acconto.
     * @param sx La sessione al database.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la fattura.
     * @return La fattura creata, ma non salvata nel database.
     */
    static MROldDocumentoFiscale newFatturaAcconto(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, Double acconto, User user) {
        MROldDocumentoFiscale newFattura = newIntestazioneFattura(sx, contrattoNoleggio, sedeOperativa, MROldDocumentoFiscale.FTA, user);
        MROldPianoDeiConti contoContabile = ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.CLIENTI_C_ANTICIPI);
        newFattura.setFatturaRighe(new ArrayList());
        newFattura.getFatturaRighe().add(newRigaAcconto(bundle.getString("DocumentoFiscaleFactory.msgAcconto"), acconto, contrattoNoleggio.getTariffa().getCodiceIva(), contoContabile));
        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));

        if (contrattoNoleggio.getConversionRate() != null) {
            newFattura.setConversionRate(contrattoNoleggio.getConversionRate());
            newFattura.getPrimanota().setConversionRate(contrattoNoleggio.getConversionRate());
        }

        //assegna la fattura alla prenotazione se esiste
        newFattura.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));

        return newFattura;
    }

    /**
     * Crea l'intestazione della fattura per un contratto, senza le righe e senza compilare i totali.
     * @param sx La sessione al database
     * @param contrattoNoleggio Il contratto di noleggio da fatturare
     * @param sedeOperativa La sede usata per la numerazione della fattura.
     * @param tipoDocumento Il tipo di documento da creare, uno tra i tipi definiti dentro DocumentoFiscale
     * @return La fattura appena creata, non salvata nel database.
     */
    static MROldDocumentoFiscale newIntestazioneFattura(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, String tipoDocumento, User user) {
        //User aUser =  (User)Parameters.getUser();
        contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contrattoNoleggio.getId());
        MROldAffiliato affiliato = contrattoNoleggio.getAffiliato();
        String tipoNumerazione;
        if (tipoDocumento.equals(MROldDocumentoFiscale.NC)) {
            tipoNumerazione = MROldNumerazione.NOTE_CREDITO_VENDITA;
        } else if (tipoDocumento.equals(MROldDocumentoFiscale.RF)) {
            tipoNumerazione = MROldNumerazione.CORRISPETTIVI;
        } else if(tipoDocumento.equals(MROldDocumentoFiscale.FTP)) {
            tipoNumerazione = MROldNumerazione.PROFORMA;
        }else {
            tipoNumerazione = MROldNumerazione.VENDITE;
        }
        /*
         * InvoiceEurop
         */
        MROldFonteCommissione fonteCommissione = null;
        if(contrattoNoleggio.getCommissione() != null){
            fonteCommissione = contrattoNoleggio.getCommissione().getFonteCommissione();
        }
        MROldNumerazione numerazione = null;
            numerazione = NumerazioniUtils.getNumerazione(sx, sedeOperativa, tipoNumerazione, user);
        Date data = FormattedDate.formattedDate();
        Integer anno = FormattedDate.annoCorrente();
        Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
        String prefisso = numerazione.getPrefisso();
        MROldDocumentoFiscale newFattura = MROldDocumentoFiscale.creaDocumentoFiscale(
                affiliato,
                contrattoNoleggio.getUserApertura(),
                tipoDocumento,
                numerazione,
                prefisso,
                numero,
                data,
                anno);
        newFattura.setContratto(contrattoNoleggio);
//        if (contrattoNoleggio.getCliente() != null) {
//            sx.evict(contrattoNoleggio.getCliente());
//            newFattura.setCliente((MROldClienti) sx.get(MROldClienti.class, contrattoNoleggio.getCliente().getId()));
//        }
        if (contrattoNoleggio.getInvoiceTo() != null) {
            //sx.evict(contrattoNoleggio.getCliente());
            newFattura.setCliente(contrattoNoleggio.getInvoiceTo());
        } else {
            newFattura.setCliente(contrattoNoleggio.getCliente());
        }
        // sx.evict(contrattoNoleggio.getPagamento());
        // newFattura.setPagamento((Pagamento) sx.get(Pagamento.class, contrattoNoleggio.getPagamento().getId()));
        newFattura.setPagamento(contrattoNoleggio.getPagamento());
        newFattura.setContratto(contrattoNoleggio);
        newFattura.setAnnotazioni(contrattoNoleggio.getNote());
        return newFattura;
    }

    /**
     * Questo metodo crea una fattura finale per il noleggio. Esclude dalla fatturazione le voci fatturate in precedenza.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La fattura creata e salvata nel database.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldDocumentoFiscale newFatturaSaldo(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, User user) throws TitledException {
        MROldDocumentoFiscale newFattura = null;
        Transaction tx = null;

        try {
            contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contrattoNoleggio.getId());
            MROldCurrencyConversionRate ccr = null;

            if (contrattoNoleggio.getConversionRate() != null) {
                ccr = (MROldCurrencyConversionRate) sx.get(MROldCurrencyConversionRate.class, contrattoNoleggio.getConversionRate().getId());
            }

            List<MROldPartita> partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, contrattoNoleggio);
            MROldPrenotazione prenotazione=null;
            List<MROldPartita> partiteAccontiRez = null;

            if (contrattoNoleggio.getCommissione()!=null &&
                    contrattoNoleggio.getCommissione().getPrenotazione()!=null&&
                    contrattoNoleggio.getCommissione().getPrenotazione().getId()!=null)

                prenotazione = (MROldPrenotazione) sx.get(MROldPrenotazione.class, contrattoNoleggio.getCommissione().getPrenotazione().getId());

            if (prenotazione!=null)
                partiteAccontiRez = ContabUtils.leggiPartiteAperteFattureAcconto(sx, prenotazione);

            if (partiteAccontiRez!=null){
                if (partiteAcconti!=null)
                    partiteAcconti.addAll(partiteAccontiRez);
                else
                    partiteAcconti=partiteAccontiRez;
            }
            List<MROldPrimanota> contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, contrattoNoleggio);
            newFattura = PartitaFactory.newPartitaSaldo(sx, contrattoNoleggio, sedeOperativa, partiteAcconti, contropartiteAperte, user).getFattura();
            newFattura.setConversionRate(ccr);
            newFattura.getPrimanota().setConversionRate(ccr);

        } catch (BusinessRuleException brex) {
            if (tx != null) {
                try {
                    //tx.rollback();
                } catch (Exception txx) {
                    log.error(txx.getMessage());
                    txx.printStackTrace();
                }
            }
            throw brex;
        } catch (Exception ex) {
            ex.printStackTrace();

            log.error("Errore creazione fattura: " + ex.getMessage()); //NOI18N
            log.error("Errore creazione fattura: ", ex); //NOI18N
            //throw new DatabaseException(ex);
        } finally {
            if (sx != null) {
                try {
                    //sx.close();
                } catch (Exception sxx) {
                    sxx.printStackTrace();
                }
            }
        }
        return newFattura;
    }

    /**
     * Questo metodo crea una nota credito per il noleggio.
     * @param contrattoNoleggio Il contratto da stornare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La nota credito creata e salvata nel database.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldDocumentoFiscale newNotaCredito(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, List memoList, User user) throws TitledException {
        MROldDocumentoFiscale newNotaCredito = null;
        Transaction tx = null;
        try {
            List<MROldRigaDocumentoFiscale> righeFatture = new ArrayList();
            //Adding selected invoicerow to righeFatture
            Iterator itr = memoList.iterator();
            while (itr.hasNext()) {
                MROldRigaDocumentoFiscale rigaDocumentoFiscale = (MROldRigaDocumentoFiscale) sx.get(MROldRigaDocumentoFiscale.class, Integer.parseInt((String) itr.next()));
                if (rigaDocumentoFiscale != null) {
                    righeFatture.add(rigaDocumentoFiscale);
                }
            }
            if (righeFatture.isEmpty()) {
                throw new BusinessRuleException(bundle.getString("PartitaFactory.msgNessunaVoceDiFatturaSelezionata"), bundle.getString("PartitaFactory.msgNotaCreditoVuota"));
            }
            MROldDocumentoFiscale fattura = righeFatture.get(0).getFattura();
            for (MROldRigaDocumentoFiscale rigaFattura : righeFatture) {
                if (!rigaFattura.getFattura().equals(fattura)) {
                    throw new BusinessRuleException(bundle.getString("PartitaFactory.msgVociNotaCreditoDaFatturaSingola"), bundle.getString("PartitaFactory.msgErroreCreazioneNotaCredito"));
                }
            }
            newNotaCredito = DocumentoFiscaleFactory.newIntestazioneFattura(sx, contrattoNoleggio, sedeOperativa, MROldDocumentoFiscale.NC, user);
            newNotaCredito.setFatturaRighe(FatturaUtils.creaRigheNotaCredito(sx, newNotaCredito, righeFatture));
            ImpostaFactory imposteFattura = new ImpostaFactory(newNotaCredito.getFatturaRighe());
            newNotaCredito.setFatturaImposte(imposteFattura.getImposte());
            newNotaCredito.setTotaleAcconti(imposteFattura.getTotaleAcconti());
            newNotaCredito.setTotaleImponibile(imposteFattura.getTotaleImponibile());
            newNotaCredito.setTotaleRighe(imposteFattura.getTotaleImponibile());
            newNotaCredito.setTotaleIva(imposteFattura.getTotaleImposta());
            newNotaCredito.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
            newNotaCredito.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newNotaCredito.getNumerazione(), newNotaCredito.getAnno()));
            newNotaCredito.setContratto(contrattoNoleggio);
            newNotaCredito.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));

            MROldPrimanota primanota = PrimanotaFactory.newPrimanota(sx, newNotaCredito, sedeOperativa, null, null, user);

            sx.saveOrUpdate(primanota);
            sx.saveOrUpdate(newNotaCredito);

            MROldPartita partita = ContabUtils.leggiPartita(sx, fattura);
            partita.getContropartite().add(primanota);
            partita.aggiornaSaldo();
            partita.aggiornaChiusura();
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

            //tx.commit();
        } catch (BusinessRuleException brex) {
//            if (tx != null) {
//                try {
//                    tx.rollback();
//                } catch (Exception txx) {
//                }
//            }
            throw brex;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Errore creazione fattura: " + ex.getMessage()); //NOI18N
            log.debug("Errore creazione fattura: ", ex); //NOI18N
            //throw new DatabaseException(ex);
        } finally {
            if (sx != null) {
                try {
                   // sx.close();
                } catch (Exception sxx) {
                    sxx.printStackTrace();
                }
            }
        }
        return newNotaCredito;
    }

    /**
     * Questo metodo ricalcola la fattura finale del noleggio. Esclude dalla fatturazione le voci fatturate in precedenza.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La fattura creata e salvata nel database.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldDocumentoFiscale ricalcolaFatturaSaldo(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldDocumentoFiscale fattura, MROldSede sedeOperativa, User user) throws TitledException {
        MROldDocumentoFiscale newFattura = null;
        Transaction tx = null;
        try {
            List<MROldPartita> partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, contrattoNoleggio);
            List<MROldPrimanota> contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, contrattoNoleggio);
            MROldPartita partita = ContabUtils.leggiPartita(sx, fattura);
            if (fattura.getTipoDocumento().equals(MROldDocumentoFiscale.FTA)) {
                partiteAcconti.remove(partita);
            } else {
                partiteAcconti.addAll(partita.getPartiteAcconto());
            }
            contropartiteAperte.addAll(partita.getContropartite());
            MROldPartita partitaSaldo = PartitaFactory.ricalcolaPartitaSaldo(sx, partita, contrattoNoleggio, sedeOperativa, partiteAcconti, contropartiteAperte, user);
            newFattura = partitaSaldo.getFattura();
        } catch (BusinessRuleException brex) {
            throw brex;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (sx != null) {
                try {
                    //sx.close();
                } catch (Exception sxx) {
                    sxx.printStackTrace();
                }
            }
        }
        return newFattura;
    }

    /**
     * Questo metodo crea una ricevuta fiscale per il noleggio. Esclude dalla fatturazione le voci fatturate in precedenza.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La fattura creata e salvata nel database.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldDocumentoFiscale newRicevutaFiscalePagata(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, User user) throws TitledException {
        MROldDocumentoFiscale newFattura = null;
        Transaction tx = null;
        try {
            List<MROldPrimanota> contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, contrattoNoleggio);

            if (Preferenze.getRicevutaFiscaleComeFatturaSaldo(sx)) {
                newFattura = PartitaFactory.newRicevutaFiscalePagataSaldo(sx, contrattoNoleggio, sedeOperativa, contropartiteAperte, user).getFattura();
            } else {
                if (contropartiteAperte.isEmpty()) {
                    throw new BusinessRuleException(
                            bundle.getString("DocumentoFiscaleFactory.msgNessunaRicevutaFiscaleDaEmettereMessage"),
                            bundle.getString("DocumentoFiscaleFactory.msgNessunaFatturaAccontoDaEmettereTitle"));
                }
                newFattura = PartitaFactory.newRicevutaFiscalePagata(sx, contrattoNoleggio, sedeOperativa, contropartiteAperte, user).getFattura();
            }
            newFattura.setConversionRate(contrattoNoleggio.getConversionRate());

            newFattura.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));
        } catch (BusinessRuleException brex) {
            throw brex;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (sx != null) {
                try {
                    //sx.close();
                } catch (Exception sxx) {
                    sxx.printStackTrace();
                }
            }
        }
        return newFattura;
    }

    /**
     * Questo metodo crea una fattura di acconto per il noleggio, senza la primanota e senza la partita. La fattura contiene una sola riga, dell'acconto.
     * @param sx La sessione al database.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la fattura.
     * @return La fattura creata, ma non salvata nel database.
     */
    static MROldDocumentoFiscale newRicevutaFiscalePagata(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, Double acconto, User user) {
        MROldDocumentoFiscale newFattura = newIntestazioneFattura(sx, contrattoNoleggio, sedeOperativa, MROldDocumentoFiscale.RF, user);
        MROldPianoDeiConti contoContabile = ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.CLIENTI_C_ANTICIPI);
        newFattura.setFatturaRighe(new ArrayList());
        newFattura.getFatturaRighe().add(newRigaAcconto(bundle.getString("DocumentoFiscaleFactory.msgRicevutaFiscalePagata"), acconto, contrattoNoleggio.getTariffa().getCodiceIva(), contoContabile));
        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));

        if (contrattoNoleggio.getConversionRate() != null) {
            newFattura.setConversionRate(contrattoNoleggio.getConversionRate());
            newFattura.getPrimanota().setConversionRate(contrattoNoleggio.getConversionRate());
        }

        //assegna la fattura alla prenotazione se esiste
        newFattura.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));

        return newFattura;
    }

    private static Map createFatturaSaldo(Session sx, MROldContrattoNoleggio contrattoNoleggio, List<MROldDocumentoFiscale> allInvoicesMade) {
        MROldDocumentoFiscale fatturaSaldo = null;
        Map result = new HashMap();
        Double saldoAcconti = 0.0;
        if (allInvoicesMade != null) {
            for (MROldDocumentoFiscale f : allInvoicesMade) {
                if(f.getTipoDocumento().equals(MROldDocumentoFiscale.FTA)){
                    saldoAcconti += + f.getTotaleFattura();
                }
                else{
                    fatturaSaldo = f;
                }
            }
        }

        Double currentSaldo = contrattoNoleggio.getSaldoFatture() != null ? contrattoNoleggio.getSaldoFatture() : 0.0;
        Double currentAcconto = contrattoNoleggio.getSaldoAcconti() != null ? contrattoNoleggio.getSaldoAcconti() : 0.0;
        Double totaleNoleggio = contrattoNoleggio.getNoleggio() != null ? contrattoNoleggio.getNoleggio() : 0.0;
        try {

           // fatturaSaldo = InvoiceEAUtils.createInvoiceAuto(sx, contrattoNoleggio, false);
            if (fatturaSaldo != null) {

                if (currentSaldo < totaleNoleggio) {

                    Double totInvoice = fatturaSaldo.getTotaleFattura() != null ? fatturaSaldo.getTotaleFattura() : 0.0;
                    contrattoNoleggio.setSaldoFatture(currentSaldo + totInvoice);
                    contrattoNoleggio.setSaldoAcconti(currentAcconto + saldoAcconti);

                    saveInvoiceToSession(sx ,fatturaSaldo);
                    sx.merge(contrattoNoleggio);
                    result.put("invoice",fatturaSaldo);
                    result.put("msg","");
//                    stampaFattura(fatturaSaldo, true);

                } else if (currentSaldo.equals(totaleNoleggio)) {
                    //JOptionPane.showMessageDialog(this, bundle.getString("JPanelDatiContrattoBreve.nonCiSonoImportiDaSaldare"));
                    result.put("invoice",fatturaSaldo);
                    result.put("msg","There are no new charges to invoice.");
                }

            } else {
                //JOptionPane.showMessageDialog(this, bundle.getString("JPanelDatiContrattoBreve.nonCiSonoImportiDaSaldare"));
                result.put("invoice",fatturaSaldo);
                result.put("msg","There are no new charges to invoice.");
            }
        } catch (HibernateException e) {
//            if (tx != null && tx.wasCommitted()) {
//                tx.rollback();
//            }
            e.printStackTrace();
        } finally {
//            if (sx != null && sx.isOpen()) {
//                sx.close();
//            }
        }
        return result;
    }

    public static MROldDocumentoFiscale newFatturaAccontoEA(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, Double remainingToPay, MROldPagamento pagamento, Integer numeroRecalc, User user) throws TitledException {
        MROldDocumentoFiscale newFattura = null;
        try {
            contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contrattoNoleggio.getId());
            List<MROldPrimanota> contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, contrattoNoleggio);
            if (contropartiteAperte.isEmpty()) {
//                throw new BusinessRuleException(
//                        bundle.getString("DocumentoFiscaleFactory.msgNessunaFatturaAccontoDaEmettereMessage"),
//                        bundle.getString("DocumentoFiscaleFactory.msgNessunaFatturaAccontoDaEmettereTitle"));
            }

            newFattura = PartitaFactory.newPartitaAccontoEA(sx, contrattoNoleggio, sedeOperativa, remainingToPay, pagamento, contropartiteAperte, numeroRecalc, user).getFattura();
            newFattura.setConversionRate(contrattoNoleggio.getConversionRate());

            newFattura.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));
        }
        catch (Exception ex) {
            log.error("Errore creazione fattura: " + ex.getMessage()); //NOI18N
            log.debug("Errore creazione fattura: ", ex); //NOI18N
            ex.printStackTrace();
        } finally {
//            if (sx != null && sx.isOpen()) {
//                sx.close();
//            }
        }
        return newFattura;
    }

    public static MROldDocumentoFiscale newIntestazioneFatturaProforma(
            Session sx,
            MROldContrattoNoleggio contratto,
            Date dataFattura,
            MROldNumerazione numerazioneProtocollo,
            int numeroProtocollo,
            Boolean prepagato) {
        //User aUser =  (User)Parameters.getUser();
        MROldAffiliato affiliato = null;
        boolean issummaryinvoice = false;
        if(contratto != null){
            affiliato = contratto.getAffiliato();
        }
        else{
            issummaryinvoice = true;
            affiliato = (MROldAffiliato) sx.get(MROldAffiliato.class, 1);
        }
        Integer anno = FormattedDate.annoCorrente(dataFattura);
        String prefisso = numerazioneProtocollo.getPrefisso();
        MROldDocumentoFiscale newFattura = MROldDocumentoFiscale.creaDocumentoFiscale(
                affiliato,
                contratto.getUserApertura(),
                MROldDocumentoFiscale.FTP,
                numerazioneProtocollo,
                prefisso,
                numeroProtocollo,
                dataFattura,
                anno);
        newFattura.setContratto(contratto);
        //Per le fatture prepagate, l'intestazione viene presa dalla fonte commissionabile, se presente
        MROldFonteCommissione focusFonte = null;
        if(contratto != null){
            focusFonte = (MROldFonteCommissione)sx.get(MROldFonteCommissione.class, contratto.getCommissione().getFonteCommissione().getId());
        }

        if (prepagato && focusFonte != null && focusFonte.getCliente() != null) {
            newFattura.setCliente(focusFonte.getCliente());
        } else if(contratto != null){
            if(contratto.getCliente() != null){
                newFattura.setCliente(contratto.getCliente());
            }
            else if(contratto.getConducente1() != null){
                MROldBusinessPartner bp = (MROldBusinessPartner) sx.get(MROldBusinessPartner.class, contratto.getConducente1().getId());
                newFattura.setCliente(bp);
            }

        }

        newFattura.setPrepagato(prepagato);
        if(contratto != null && contratto.getPagamento() != null){
            newFattura.setPagamento(contratto.getPagamento());
        }

        if(issummaryinvoice && focusFonte != null && focusFonte.getPagamento() != null){
            newFattura.setPagamento(focusFonte.getPagamento());
        }

        return newFattura;
    }

    public static MROldDocumentoFiscale newFatturaAutomaticaProforma(Session sx, MROldDocumentoFiscale newFattura, Boolean prepagato,
                                                                     MROldNumerazione numerazionePartite, Integer numeroPartita) throws BusinessRuleException {

        List<MROldPartita> partiteAcconti = new ArrayList<MROldPartita>();
        if (!prepagato) {
            partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, newFattura.getContratto());
        }
        return PartitaFactory.newPartitaSaldoFatturaProforma(sx, newFattura, prepagato, numerazionePartite,
                numeroPartita, partiteAcconti).getFattura();
    }

    public static MROldDocumentoFiscale newFatturaAutomaticaNoSaveInSX(Session sx, MROldDocumentoFiscale newFattura, MROldSede sede, Boolean prepagato, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione, MROldNumerazione numerazionePartite, Integer numeroPartita, User user) throws BusinessRuleException {
        List<MROldPrimanota> contropartiteAperte = new ArrayList<MROldPrimanota>();
        List<MROldPartita> partiteAcconti = new ArrayList<MROldPartita>();
        if (!prepagato) {
            partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, newFattura.getContratto());
            contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, newFattura.getContratto());
        }
        return PartitaFactory.newPartitaSaldoNoSaveInSession(sx, newFattura, sede, prepagato, numerazioneRegistrazione, numeroRegistrazione, numerazionePartite, numeroPartita, partiteAcconti, contropartiteAperte, user).getFattura();
    }

    /**
     * Crea l'intestazione della fattura per un contratto, senza le righe e senza compilare i totali.
     * Il documento creato e' una Fattura di vendita, con numerazione e numero protocollo specificati.
     * Inoltre, se la fattura e' una prepagata, il cliente viene impostato considerando le impostazioni di
     * fatturazione della fonte commissionabile.
     * @param sx
     * @param contrattoNoleggio Il contratto di noleggio.
     * @param dataFattura La data da associare alla fattura.
     * @param numerazioneProtocollo La numerazione usata per il protocollo (sezionale) della fattura.
     * @param numeroProtocollo il numero da assegnare alla fattura (il prefisso e' quello della numerazione).
     * @param prepagato Indica che la fattura riguarda solamente il periodo prepagato del noleggio.
     * @return L'intestazione della fattura appena creata, non salvata nel database.
     */
    public static MROldDocumentoFiscale newIntestazioneFattura(
            Session sx,
            MROldContrattoNoleggio contrattoNoleggio,
            Date dataFattura,
            MROldNumerazione numerazioneProtocollo,
            Integer numeroProtocollo,
            Boolean prepagato) {

        //User aUser =  (User)Parameters.getUser();
        MROldAffiliato affiliato = null;
        boolean issummaryinvoice = false;
        if(contrattoNoleggio != null){
            affiliato = contrattoNoleggio.getAffiliato();
        }
        else{
            issummaryinvoice = true;
            affiliato = (MROldAffiliato) sx.get(MROldAffiliato.class, 1);
        }
        Integer anno = FormattedDate.annoCorrente(dataFattura);
        String prefisso = numerazioneProtocollo.getPrefisso();
        MROldDocumentoFiscale newFattura = MROldDocumentoFiscale.creaDocumentoFiscale(
                affiliato,
                contrattoNoleggio.getUserApertura(),
                MROldDocumentoFiscale.FT,
                numerazioneProtocollo,
                prefisso,
                numeroProtocollo,
                dataFattura,
                anno);
        newFattura.setContratto(contrattoNoleggio);
        //Per le fatture prepagate, l'intestazione viene presa dalla fonte commissionabile, se presente
        MROldFonteCommissione focusFonte = null;
        if(contrattoNoleggio != null){
            focusFonte = (MROldFonteCommissione)sx.get(MROldFonteCommissione.class, contrattoNoleggio.getCommissione().getFonteCommissione().getId());
        }

        if (prepagato && focusFonte != null && focusFonte.getCliente() != null) {
            newFattura.setCliente(focusFonte.getCliente());
        } else if(contrattoNoleggio != null){
            if(contrattoNoleggio.getCliente() != null){
                newFattura.setCliente(contrattoNoleggio.getCliente());
            }
            else if(contrattoNoleggio.getConducente1() != null){
                MROldBusinessPartner bp = (MROldBusinessPartner) sx.get(MROldBusinessPartner.class, contrattoNoleggio.getConducente1().getId());
                newFattura.setCliente(bp);
            }

        }

        newFattura.setPrepagato(prepagato);
        if(contrattoNoleggio != null && contrattoNoleggio.getPagamento() != null){
            newFattura.setPagamento(contrattoNoleggio.getPagamento());
        }

        if(issummaryinvoice && focusFonte != null && focusFonte.getPagamento() != null){
            newFattura.setPagamento(focusFonte.getPagamento());
        }

        return newFattura;
    }


    public static MROldDocumentoFiscale newIntestazioneFattura(
            Session sx,
            Rent2Rent r2r,
            Date dataFattura,
            MROldNumerazione numerazioneProtocollo,
            Integer numeroProtocollo,
            Boolean prepagato) {
        User aUser = (User) Parameters.getUser();
        MROldAffiliato affiliato = null;
        affiliato = (MROldAffiliato) sx.get(MROldAffiliato.class, 1);

        Integer anno = FormattedDate.annoCorrente(dataFattura);
        String prefisso = numerazioneProtocollo.getPrefisso();
        MROldDocumentoFiscale newFattura = MROldDocumentoFiscale.creaDocumentoFiscale(
                affiliato,
                aUser,
                MROldDocumentoFiscale.FT,
                numerazioneProtocollo,
                prefisso,
                numeroProtocollo,
                dataFattura,
                anno);
        newFattura.setRent2rent(r2r);
        //Per le fatture prepagate, l'intestazione viene presa dalla fonte commissionabile, se presente

        if (r2r != null) {
            newFattura.setCliente(r2r.getCustomer());
        }

        newFattura.setPrepagato(prepagato);
        if (r2r != null) {
            newFattura.setPagamento(r2r.getPagamento());
        }
        return newFattura;
    }


    public static Boolean deleteInvoicesFromAgreement(Session sx, MROldContrattoNoleggio contrattoNoleggio,
                                                      MROldSede sedeOperativa,
                                                      List<MROldDocumentoFiscale> invoicesToRecalculate,
                                                      Double totalAccontoToRemove,
                                                      Double totalSaldoToRemove) throws TitledException {

        boolean response= true;
        try{
            for(Object o : invoicesToRecalculate){
                if(o instanceof MROldDocumentoFiscale){
                    MROldDocumentoFiscale f = (MROldDocumentoFiscale) o;
                    deleteFattura(sx, f);
                }

            }
            Double currentAcconto = contrattoNoleggio.getSaldoAcconti() != null ? contrattoNoleggio.getSaldoAcconti() : 0.0;
            Double currentSaldo = contrattoNoleggio.getSaldoFatture() != null ? contrattoNoleggio.getSaldoFatture() : 0.0;
            contrattoNoleggio.setSaldoAcconti(currentAcconto - totalAccontoToRemove);
            contrattoNoleggio.setSaldoFatture(currentSaldo - totalSaldoToRemove);
            contrattoNoleggio =(MROldContrattoNoleggio) sx.merge(contrattoNoleggio);
        }
        catch(HibernateException e){
//            if(tx != null && tx.wasCommitted()){
//                tx.rollback();
//            }
            response = false;
            e.printStackTrace();
        }
        finally{
//            if(sx != null && sx.isOpen()){
//                sx.close();
//            }
        }
        return response;
    }

    public static void deleteFattura(Session sx, MROldDocumentoFiscale fattura) {
        Query q = sx.createQuery("from MROldPartita p where p.fattura = :fattura");
        q.setParameter("fattura", fattura);
        List<MROldPartita> allPartiteAssociated = q.list();
        if(allPartiteAssociated != null && allPartiteAssociated.size() > 0){
            for(MROldPartita p : allPartiteAssociated){
                sx.delete(p);
            }
        }
        sx.delete(fattura);
        Query q2 = sx.createQuery("from MROldRigaDocumentoFiscale r where r.fattura = :fattura");
        q2.setParameter("fattura", fattura);
        List<MROldRigaDocumentoFiscale> allRigaDocumentoFiscale = q2.list();
        if(allRigaDocumentoFiscale != null && allRigaDocumentoFiscale.size() > 0){
            for(MROldRigaDocumentoFiscale r : allRigaDocumentoFiscale){
                sx.delete(r);
            }
        }
//        sx.delete(partita);
        if(fattura.getPrimanota() != null){
            sx.delete(fattura.getPrimanota());
        }

    }

    /**
     * Questo metodo crea una fattura di acconto per la fattura, senza la primanota e senza la partita. La fattura contiene una sola riga, dell'acconto.
     * @param sx La sessione al database.
     * @param prenotazione Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la fattura.
     * @return La fattura creata, ma non salvata nel database.
     */
    static MROldDocumentoFiscale newFatturaAcconto(Session sx, MROldPrenotazione prenotazione, MROldSede sedeOperativa, Double acconto) {
        MROldDocumentoFiscale newFattura = newIntestazioneFattura(sx, prenotazione, sedeOperativa, MROldDocumentoFiscale.FTA);
        MROldPianoDeiConti contoContabile = ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.CLIENTI_C_ANTICIPI);
        newFattura.setFatturaRighe(new ArrayList());
        newFattura.getFatturaRighe().add(newRigaAcconto(bundle.getString("DocumentoFiscaleFactory.msgAcconto"), acconto, prenotazione.getTariffa().getCodiceIva(), contoContabile));
        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
        newFattura.setContratto(PrenotazioniUtils.contrattoEsistente(sx, prenotazione));
        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));

        if (prenotazione.getConversionRate() != null) {
            newFattura.setConversionRate(prenotazione.getConversionRate());
            newFattura.getPrimanota().setConversionRate(prenotazione.getConversionRate());
        }

        newFattura.setContratto(PrenotazioniUtils.contrattoEsistente(sx, prenotazione));

        return newFattura;
    }

    public static Map createFattureForAgreementEA(Session sx, MROldContrattoNoleggio contrattoNoleggio,
                               MROldSede sedeOperativa, List<Integer> numAcconto, Integer lastNumSaldo, Integer selfMadeNumeration, List invoicesToRecalculate, User aUser) throws TitledException{

        List<MROldDocumentoFiscale> allInvoicesMade = new ArrayList<MROldDocumentoFiscale>();
        MROldDocumentoFiscale fatturaAccontoCC = null;
        MROldDocumentoFiscale fatturaAccontoCash = null;
        Double currentSaldo = contrattoNoleggio.getSaldoFatture() != null ? contrattoNoleggio.getSaldoFatture() : 0.0;
        Double totaleNoleggio = contrattoNoleggio.getNoleggio() != null ? contrattoNoleggio.getNoleggio() : 0.0;
        Double totalCashpayed = contrattoNoleggio.getTotalCashPayment() != null ? contrattoNoleggio.getTotalCashPayment() : 0.0;
        Double totalCCpayed = contrattoNoleggio.getGarazia1CCPayment() != null ? contrattoNoleggio.getGarazia1CCPayment() : 0.0;
        totalCCpayed += contrattoNoleggio.getGarazia2CCPayment() != null ? contrattoNoleggio.getGarazia2CCPayment() : 0.0;

        Double remainingDue = contrattoNoleggio.getTotalDue() != null ? contrattoNoleggio.getTotalDue() : 0.0;
        Double saldoAcconti = contrattoNoleggio.getSaldoAcconti() != null ? contrattoNoleggio.getSaldoAcconti() : 0.0;

        if (sedeOperativa != null) {
            try {
                MROldPagamento pagamentoCash = (MROldPagamento) sx.get(MROldPagamento.class, 1);
                MROldPagamento pagamentoCC = (MROldPagamento) sx.get(MROldPagamento.class, 5);

                Double totaleFattureCCemesse = 0.0;
                Double totaleFattureCashemesse = 0.0;

                List<MROldDocumentoFiscale> oldCashInvoices = new ArrayList<MROldDocumentoFiscale>();
                List<MROldDocumentoFiscale> oldCCInvoices = new ArrayList<MROldDocumentoFiscale>();

                Query queryAllCashSaldo = sx.createQuery("from MROldDocumentoFiscale d where "
                        + "d.contratto = :contratto AND "
                        + "d.pagamento = :pagamento");
                queryAllCashSaldo.setParameter("contratto", contrattoNoleggio);
                queryAllCashSaldo.setParameter("pagamento", pagamentoCC);

                oldCCInvoices = queryAllCashSaldo.list();
                queryAllCashSaldo.setParameter("pagamento", pagamentoCash);
                oldCashInvoices = queryAllCashSaldo.list();


                if (oldCCInvoices != null && oldCCInvoices.size() > 0) {
                    for (MROldDocumentoFiscale d : oldCCInvoices) {
//
                        totaleFattureCCemesse += d.getTotaleFattura();
                    }
                }

                if (oldCashInvoices != null && oldCashInvoices.size() > 0) {
                    for (MROldDocumentoFiscale d : oldCashInvoices) {
                        totaleFattureCashemesse += d.getTotaleFattura();
//
                    }
                }

                List<MROldRigaDocumentoFiscale> righeFatturePrecedenti = FatturaUtils.leggiRigheDocumentiFiscaliEmessiEA(sx, contrattoNoleggio);
                Double totAlreadyInvoiced = 0.0;
                if(righeFatturePrecedenti != null){
                    for(MROldRigaDocumentoFiscale r : righeFatturePrecedenti){
                        if(r != null){
                            totAlreadyInvoiced += r.getTotaleRiga();
                        }
                    }
                }

                Double totToInvoice = totaleNoleggio - totAlreadyInvoiced;

                Double daEmettereCC = totalCCpayed - totaleFattureCCemesse;
                Double accontoCCMax = daEmettereCC;
                Double daEmettereCash = totalCashpayed - totaleFattureCashemesse;
                Double accontoCashMax = daEmettereCash;



                if(totToInvoice - accontoCCMax >= 0.0){
                    totToInvoice = totToInvoice - accontoCCMax;
                }
                else{
//                    accontoCCMax = 0.0;
//                    totToInvoice = 0.0;
                    accontoCCMax = totToInvoice - remainingDue;
                    totToInvoice = remainingDue;
                }

                if(totToInvoice - accontoCashMax >= -0.2){
                    totToInvoice = totToInvoice - accontoCashMax;
                }
                else{
//                    accontoCashMax = 0.0;
//                    totToInvoice = 0.0;
                    accontoCashMax = totToInvoice - remainingDue;
                    totToInvoice = remainingDue;
                }


                MROldPagamento pagamento = null;

                if(accontoCashMax > 0.0 && accontoCCMax > 0.0 && contrattoNoleggio.getTotalDue() <= 0.2){//payment for saldo
                    pagamento = (MROldPagamento) sx.get(MROldPagamento.class, 5);
                }
                else{
                    pagamento = InvoiceEAUtils.findPaymentForAgreement(sx, contrattoNoleggio);
                }

                //sx = HibernateBridge.startNewSession();
                //sx.close();
                /////////////////////////acconto cash
                if (accontoCashMax > 0.2 && remainingDue > 5 || accontoCashMax > 0.0 && accontoCCMax > 0.0) {
                    Integer numberToApply = numAcconto != null ? numAcconto.get(0) : null;
                    if(numberToApply != null && numberToApply == 0){
                        numberToApply = null;
                    }
                    else if(selfMadeNumeration != null){
                        numberToApply = selfMadeNumeration;
                    }
                    fatturaAccontoCash = DocumentoFiscaleFactory.newFatturaAccontoEA(sx, contrattoNoleggio, sedeOperativa, accontoCashMax, pagamentoCash, numberToApply, aUser);
                }

                ////////////////////////////acconto cc
                if (accontoCCMax > 0.2 && remainingDue > 5
                        && currentSaldo + saldoAcconti - totaleNoleggio != 0.0
                        && currentSaldo + saldoAcconti - totaleNoleggio != 0.1
                        && currentSaldo + saldoAcconti - totaleNoleggio != -0.1) {
                    Integer numberToApply = numAcconto != null ?  numAcconto.get(1)  : null;
                    if(numberToApply != null &&  numberToApply == 0){
                        numberToApply = null;
                    }
                    else if(selfMadeNumeration != null){
                        if(fatturaAccontoCash != null){
                            selfMadeNumeration++;
                        }
                        numberToApply = selfMadeNumeration;
                    }
                    fatturaAccontoCC = DocumentoFiscaleFactory.newFatturaAccontoEA(sx, contrattoNoleggio, sedeOperativa, accontoCCMax, pagamentoCC, numberToApply, aUser);
                }

                //sx = HibernateBridge.startNewSession();
                if (fatturaAccontoCC != null) {

                    allInvoicesMade.add(fatturaAccontoCC);
                }
                if (fatturaAccontoCash != null) {

                    allInvoicesMade.add(fatturaAccontoCash);
                }
                ///////////////////////////////////////////////////////////////////////////////////////////
                if(invoicesToRecalculate != null ){

                    //&& fatturaAccontoCC == null && fatturaAccontoCash == null

                    if(fatturaAccontoCash == null){
                        if(invoicesToRecalculate.size()>1 && (invoicesToRecalculate.get(1) instanceof MROldDocumentoFiscale)){ //this means that before there was an acconto invoice for cash so we have not to delete it otherwise the enumaration will have a hole
                            MROldDocumentoFiscale previousCashAcconto = (MROldDocumentoFiscale) invoicesToRecalculate.get(1);
                            MROldDocumentoFiscale fatturaAcconto = DocumentoFiscaleFactory.newFatturaAccontoEA(sx, contrattoNoleggio,
                                    sedeOperativa, 0.0 ,
                                    previousCashAcconto.getPagamento(), previousCashAcconto.getNumero(), aUser);
                            allInvoicesMade.add(fatturaAcconto);

                        }

                    }
                    if(fatturaAccontoCC == null){
                        if(invoicesToRecalculate.size()>1 && (invoicesToRecalculate.get(2) instanceof MROldDocumentoFiscale)){
                            MROldDocumentoFiscale previousCCAcconto = (MROldDocumentoFiscale) invoicesToRecalculate.get(2);
                            MROldDocumentoFiscale fatturaAcconto = DocumentoFiscaleFactory.newFatturaAccontoEA(sx, contrattoNoleggio,
                                    sedeOperativa, 0.0 ,
                                    previousCCAcconto.getPagamento(), previousCCAcconto.getNumero(), aUser);
                            allInvoicesMade.add(fatturaAcconto);

                        }
                    }

//                            for(int i = 1; i < invoicesToRecalculate.size(); i++){
//
//                                DocumentoFiscale fatturaAcconto = DocumentoFiscaleFactory.newFatturaAccontoEA(contrattoNoleggio,
//                                        sedeOperativa, invoicesToRecalculate.get(i).getTotaleFattura() ,
//                                        invoicesToRecalculate.get(i).getPagamento(), invoicesToRecalculate.get(i).getNumero());
//                                allInvoicesMade.add(fatturaAcconto);
//                            }
                }
                ///////////////////////////////////////////////////////////////////////////////////////////
                //sx = HibernateBridge.startNewSession();
                if(selfMadeNumeration != null){
                    if(fatturaAccontoCash != null){
                        selfMadeNumeration++;
                    }
                    if(fatturaAccontoCC != null){
                        selfMadeNumeration++;
                    }
                    lastNumSaldo = selfMadeNumeration;
                }
                Map<MROldPartita, MROldDocumentoFiscale> docAndPartita = InvoiceEAUtils.createInvoiceAuto(sx, contrattoNoleggio,
                        sedeOperativa, pagamento, false, lastNumSaldo, aUser);

                MROldDocumentoFiscale fatturaSaldo = null;
                for(MROldDocumentoFiscale d : docAndPartita.values()){
                    fatturaSaldo = d;
                }
                for(MROldPartita p : docAndPartita.keySet()){
                    sx.saveOrUpdate(p);
                }

                if (fatturaSaldo != null) {

                    allInvoicesMade.add(fatturaSaldo);
                }



            } catch (TitledException ex) {
                throw ex;
            }

        }
        Map documentoFiscaleMap = createFatturaSaldo(sx, contrattoNoleggio, allInvoicesMade);
        return documentoFiscaleMap;
    }

    /**
     * Questo metodo crea una fattura di acconto per il noleggio, senza la primanota e senza la partita. La fattura contiene una sola riga, dell'acconto.
     * @param sx La sessione al database.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la fattura.
     * @return La fattura creata, ma non salvata nel database.
     */
    static MROldDocumentoFiscale newCreateFatturaAccontoEA(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, Double acconto, Integer numberToApply) {
        MROldDocumentoFiscale newFattura = newIntestazioneFatturaEA(sx, contrattoNoleggio, sedeOperativa, MROldDocumentoFiscale.FTA, numberToApply);
        MROldPianoDeiConti contoContabile = ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.CLIENTI_C_ANTICIPI);
        System.out.println("eqweeqwe" + contoContabile);
        newFattura = addRigheFatturaAccontoEA(sx, newFattura, contrattoNoleggio,contoContabile ,acconto);//group by cod iva
//        newFattura =  addRigheFatturaAccontoByRowsEA(newFattura, contrattoNoleggio ,acconto);

        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        if(numberToApply != null){
            newFattura.setNumero(numberToApply);
        }
        else{
            newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));
        }


        if (contrattoNoleggio.getConversionRate() != null) {
            newFattura.setConversionRate(contrattoNoleggio.getConversionRate());
            newFattura.getPrimanota().setConversionRate(contrattoNoleggio.getConversionRate());
        }

        //assegna la fattura alla prenotazione se esiste
        newFattura.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));

        return newFattura;
    }

    public static Map<MROldPartita, MROldDocumentoFiscale> newFatturaAutomaticaSaldoAndPartita(Session sx, MROldDocumentoFiscale newFattura, MROldSede sedeOperativa, Boolean prepagato, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione, MROldNumerazione numerazionePartite, Integer numeroPartita, User user) throws BusinessRuleException {
        List<MROldPrimanota> contropartiteAperte = new ArrayList<MROldPrimanota>();
        List<MROldPartita> partiteAcconti = new ArrayList<MROldPartita>();
        if (!prepagato) {
            partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, newFattura.getContratto());
            contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, newFattura.getContratto());
        }
        Map<MROldPartita, MROldDocumentoFiscale> partitaDocumento = new HashMap<MROldPartita, MROldDocumentoFiscale>();
        MROldPartita focusPartita = PartitaFactory.newPartitaSaldoNoSaveInSession(sx, newFattura, sedeOperativa, prepagato, numerazioneRegistrazione, numeroRegistrazione, numerazionePartite, numeroPartita, partiteAcconti, contropartiteAperte, user);

        if(focusPartita != null){
            MROldDocumentoFiscale doc = focusPartita.getFattura();
            if(doc != null){
                partitaDocumento.put(focusPartita, doc);
            }
        }
        return partitaDocumento;
    }

    public static Map<MROldPartita, MROldDocumentoFiscale> newFatturaAutomaticaSaldoAndPartita(Session sx, MROldDocumentoFiscale newFattura, MROldSede sedeOperativa, Boolean prepagato, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione, MROldNumerazione numerazionePartite, Integer numeroPartita, MROldParcoVeicoli veicolo) throws BusinessRuleException {
        List<MROldPrimanota> contropartiteAperte = new ArrayList<MROldPrimanota>();
        List<MROldPartita> partiteAcconti = new ArrayList<MROldPartita>();

        //partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, newFattura.getRent2rent());
        //contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, newFattura.getRent2rent());

        Map<MROldPartita, MROldDocumentoFiscale> partitaDocumento = new HashMap<MROldPartita, MROldDocumentoFiscale>();
        MROldPartita focusPartita = PartitaFactory.newPartitaSaldoNoSaveInSession(sx, newFattura, sedeOperativa, prepagato, numerazioneRegistrazione, numeroRegistrazione, numerazionePartite, numeroPartita, partiteAcconti, contropartiteAperte, veicolo);

        if(focusPartita != null){
            MROldDocumentoFiscale doc = focusPartita.getFattura();
            if(doc != null){
                partitaDocumento.put(focusPartita, doc);
            }
        }
        return partitaDocumento;
    }


    public static Map<MROldPartita, MROldDocumentoFiscale> newFatturaAutomaticaProformaAndPartita(Session sx, MROldDocumentoFiscale newFattura, Boolean prepagato,
                                                                                                  MROldNumerazione numerazionePartite, Integer numeroPartita) throws BusinessRuleException {

        List<MROldPartita> partiteAcconti = new ArrayList<MROldPartita>();
        if (!prepagato) {
            partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, newFattura.getContratto());
        }

        Map<MROldPartita, MROldDocumentoFiscale> partitaDocumento = new HashMap<MROldPartita, MROldDocumentoFiscale>();
        MROldPartita focusPartita = PartitaFactory.newPartitaSaldoFatturaProforma(sx, newFattura, prepagato, numerazionePartite,
                numeroPartita, partiteAcconti);

        if(focusPartita != null){
            MROldDocumentoFiscale doc = focusPartita.getFattura();
            if(doc != null){
                partitaDocumento.put(focusPartita, doc);
            }
        }

        return partitaDocumento;
    }

    public static void saveInvoiceToSession(Session sx, MROldDocumentoFiscale documento) {

        if (documento != null) {
//            Hibernate.initialize(documento.getPrimanota());
            MROldPrimanota primaNota = (MROldPrimanota)documento.getPrimanota() ;
            if (primaNota != null) {

//                Hibernate.initialize(primaNota.getPartita());
                for(RigaImposta r: primaNota.getRigheImposta()){
                    sx.saveOrUpdate(r);
                }
                for(RigaPrimanota r: primaNota.getRighePrimanota()){
                    sx.saveOrUpdate(r);
                }

                sx.saveOrUpdate(primaNota);
                MROldPartita partita = primaNota.getPartita();
                if (partita != null) {
                    sx.saveOrUpdate(partita);
                }
            }
            for(MROldRigaDocumentoFiscale r :(List<MROldRigaDocumentoFiscale>) documento.getFatturaRighe()){
                sx.saveOrUpdate(r);
            }


            if(documento.getId() != null){
                sx.merge(documento);
            } else {
                sx.saveOrUpdate(documento);
            }
        }
    }

    /**
     * If numberToApply is not null than is assigned as numero otherwise it will normally use NumerazioneUtils.
     * @param sx
     * @param contrattoNoleggio
     * @param sedeOperativa
     * @param tipoDocumento
     * @return
     */
    static MROldDocumentoFiscale newIntestazioneFatturaEA(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa,
                                                     String tipoDocumento, Integer numberToApply) {
        User aUser =  (User)Parameters.getUser();
        contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contrattoNoleggio.getId());
        MROldAffiliato affiliato = contrattoNoleggio.getAffiliato();


        String tipoNumerazione;
        if (tipoDocumento.equals(MROldDocumentoFiscale.NC)) {
            tipoNumerazione = MROldNumerazione.NOTE_CREDITO_VENDITA;
        } else if (tipoDocumento.equals(MROldDocumentoFiscale.RF)) {
            tipoNumerazione = MROldNumerazione.CORRISPETTIVI;
        } else {
            tipoNumerazione = MROldNumerazione.VENDITE;
        }
        /*
         * InvoiceEurop
         */
        MROldFonteCommissione fonte = null;
        if(contrattoNoleggio.getCommissione() != null){
            fonte = contrattoNoleggio.getCommissione().getFonteCommissione() ;
        }

        MROldNumerazione numerazione = null;

        if(!"Z".equals(fonte.getRentalType().getDescription())){
            //normal enumeration logic (that will result in assigning EA - 10000000 enumeration
            numerazione = NumerazioniUtils.getNumerazione(sx, sedeOperativa, tipoNumerazione, fonte, affiliato);

        }
        else{
            //logic for Z enumeration
            numerazione = NumerazioniUtils.findOrCreateNumerazioneSede(sx, sedeOperativa, MROldNumerazione.VENDITE);
        }

        Date data = FormattedDate.formattedDate();
        Integer anno = FormattedDate.annoCorrente();
        Integer numero = null;

        if(numberToApply != null){
            numero = numberToApply;
        }
        else{
            numero = NumerazioniUtils.nuovoNumero(sx, numerazione, anno);
        }

        String prefisso = numerazione.getPrefisso();
        MROldDocumentoFiscale newFattura = MROldDocumentoFiscale.creaDocumentoFiscale(
                affiliato,
                aUser,
                tipoDocumento,
                numerazione,
                prefisso,
                numero,
                data,
                anno);
        newFattura.setContratto(contrattoNoleggio);
        sx.evict(contrattoNoleggio.getCliente());
        MROldBusinessPartner toInvoiceBp = (MROldBusinessPartner) sx.get(MROldBusinessPartner.class, contrattoNoleggio.getInvoiceTo().getId());
        newFattura.setCliente(toInvoiceBp);
        sx.evict(contrattoNoleggio.getPagamento());
        if(contrattoNoleggio != null){


            if(contrattoNoleggio.getTotalCashedG1() != null && contrattoNoleggio.getTotalCashedG1() > 0.0){
                contrattoNoleggio.setPagamento((MROldPagamento) sx.get(MROldPagamento.class, 1));
            }
            else{
                contrattoNoleggio.setPagamento((MROldPagamento) sx.get(MROldPagamento.class, 5));

            }


            if(contrattoNoleggio.getPagamento() != null && contrattoNoleggio.getPagamento().getId() != null){
                newFattura.setPagamento((MROldPagamento) sx.get(MROldPagamento.class, contrattoNoleggio.getPagamento().getId()));
            }
            newFattura.setContratto(contrattoNoleggio);
            newFattura.setAnnotazioni(contrattoNoleggio.getNote());
        }
        return newFattura;
    }

    private static MROldDocumentoFiscale addRigheFatturaAccontoEA(Session sx, MROldDocumentoFiscale newFattura, MROldContrattoNoleggio con, MROldPianoDeiConti contoContabile, Double acconto) {
        List<MROldRigaDocumentoFiscale> righe = InvoiceEAUtils.createRigheCustomerAuto(sx, con);
        //Map<MROldCodiciIva, Double> ivaTotaleRiga = new HashMap<MROldCodiciIva, Double>();
        //Map<MROldCodiciIva, Double> ivaTotaleRigaPresenti = new HashMap<MROldCodiciIva, Double>();

        Set<MROldRigaDocumentoFiscale> setRighe = new HashSet<MROldRigaDocumentoFiscale>();
        Set<MROldRigaDocumentoFiscale>  setRighePresenti = new HashSet<MROldRigaDocumentoFiscale>();
        List<MROldPartita> partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, newFattura.getContratto());
        List<MROldRigaDocumentoFiscale> righeAccontoPresenti = new ArrayList<MROldRigaDocumentoFiscale>();
        for(MROldPartita p : partiteAcconti){
            if(p != null && p.getFattura() != null){
                MROldDocumentoFiscale fatturaAcconto = (MROldDocumentoFiscale) sx.get(MROldDocumentoFiscale.class, p.getFattura().getId());
                if(fatturaAcconto != null && fatturaAcconto.getFatturaRighe() != null){
                    righeAccontoPresenti.addAll(fatturaAcconto.getFatturaRighe());
                }
            }

        }


        if(righeAccontoPresenti != null && righeAccontoPresenti.size() > 0){
            for(MROldRigaDocumentoFiscale r : righeAccontoPresenti){
                setRighePresenti.add(r);
                //Double totalIn = ivaTotaleRigaPresenti.get(r.getCodiceIva()) != null ? ivaTotaleRigaPresenti.get(r.getCodiceIva()) : 0.0;
                //ivaTotaleRigaPresenti.put(r.getCodiceIva(), r.getTotaleRiga()+totalIn);
            }
        }

        if(righe != null && righe.size() > 0){
            for(MROldRigaDocumentoFiscale r : righe){
                setRighe.add(r);
                //Double totalIn = ivaTotaleRiga.get(r.getCodiceIva()) != null ? ivaTotaleRiga.get(r.getCodiceIva()) : 0.0;
                //ivaTotaleRiga.put(r.getCodiceIva(), r.getTotaleRiga() + totalIn);
            }
        }
        Double acconto2 = acconto;
        newFattura.setFatturaRighe(new ArrayList());

        Integer numeroRiga = 0;
//        for(MROldCodiciIva codiceIva : ivaTotaleRiga.keySet()){
//            Double tot = ivaTotaleRiga.get(codiceIva);
//            Double totAlready = 0.0;
//
//            if(ivaTotaleRigaPresenti.containsKey(codiceIva)){
//                totAlready = ivaTotaleRigaPresenti.get(codiceIva);
//                tot = tot - totAlready;//here
//            }
        for(MROldRigaDocumentoFiscale r : setRighe){
            Double tot = r.getTotaleRiga();
            Double totAlready = 0.0;

            if(setRighePresenti.contains(r)){
                totAlready = r.getTotaleRiga();
                tot = tot - totAlready;//here
            }

            if(acconto2 > 0){
                if(tot - acconto2 > 0){
                    //newFattura.getFatturaRighe().add(newRigaAccontoEA(bundle.getString("DocumentoFiscaleFactory.msgAcconto"),acconto2, codiceIva, contoContabile, newFattura, numeroRiga));
                    newFattura.getFatturaRighe().add(newRigaAccontoEA("Acconto "+r.getDescrizione(), acconto2, r.getCodiceIva(), contoContabile, newFattura, numeroRiga));

                    acconto2 = 0.0;
                }
                else{
                    //newFattura.getFatturaRighe().add(newRigaAccontoEA(bundle.getString("DocumentoFiscaleFactory.msgAcconto"),tot, codiceIva, contoContabile, newFattura, numeroRiga));
                    newFattura.getFatturaRighe().add(newRigaAccontoEA("Acconto "+r.getDescrizione(), tot, r.getCodiceIva(), contoContabile, newFattura, numeroRiga));
                    acconto2 = acconto2 - tot;
                }

            }
            numeroRiga++;


        }
        return newFattura;
    }

    public static MROldRigaDocumentoFiscale newRigaAccontoEA(
            String descrizioneAcconto,
            Double totaleAcconto,
            MROldCodiciIva codiceIva,
            MROldPianoDeiConti contoContabile
            ,MROldDocumentoFiscale fatturaAcconto
            ,Integer numeroRigaFattura
    ) {


        MROldRigaDocumentoFiscale rigaAcconto = new MROldRigaDocumentoFiscale();
        Double imponibile = MathUtils.round(MathUtils.scorporoIva(codiceIva.getAliquota(), totaleAcconto), 5);
        Double iva = MathUtils.round(imponibile * codiceIva.getAliquota(), 5);
        Double totale = imponibile + iva;

        rigaAcconto = new MROldRigaDocumentoFiscale();
        rigaAcconto.setDescrizione(descrizioneAcconto);
        rigaAcconto.setUnitaMisura(null);
        rigaAcconto.setQuantita(1.0);
        rigaAcconto.setPrezzoUnitario(imponibile);
        rigaAcconto.setTotaleImponibileRiga(imponibile);
        rigaAcconto.setTotaleIvaRiga(iva);
        rigaAcconto.setCodiceIva(codiceIva);
        rigaAcconto.setSconto(0.0);
        rigaAcconto.setScontoFisso(0.0);
        rigaAcconto.setCodiceSottoconto(contoContabile);
        rigaAcconto.setTotaleRiga(totale);
        rigaAcconto.setFattura(fatturaAcconto);
        rigaAcconto.setNumeroRigaFattura(numeroRigaFattura);
        rigaAcconto.setTempoKm(Boolean.FALSE);
        rigaAcconto.setTempoExtra(Boolean.FALSE);
        rigaAcconto.setFranchigia(Boolean.FALSE);

        return rigaAcconto;
    }

    /**
     * Questo metodo crea una fattura finale per il noleggio. Esclude dalla fatturazione le voci fatturate in precedenza.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La fattura creata e salvata nel database.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldDocumentoFiscale newFatturaProforma(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, User user) throws TitledException {
        MROldDocumentoFiscale newFattura = null;
        Transaction tx = null;

        try {
            contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contrattoNoleggio.getId());
            MROldCurrencyConversionRate ccr = null;

            if (contrattoNoleggio.getConversionRate() != null) {
                ccr = (MROldCurrencyConversionRate) sx.get(MROldCurrencyConversionRate.class, contrattoNoleggio.getConversionRate().getId());
            }

            List<MROldPartita> partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, contrattoNoleggio);
            List<MROldPrimanota> contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, contrattoNoleggio);
            newFattura = PartitaFactory.newPartitaSaldo(sx, contrattoNoleggio, sedeOperativa, partiteAcconti, contropartiteAperte, user).getFattura();
            newFattura.setConversionRate(ccr);
            newFattura.getPrimanota().setConversionRate(ccr);

        } catch (BusinessRuleException brex) {
            if (tx != null) {
                try {
                    //tx.rollback();
                } catch (Exception txx) {
                    log.error(txx.getMessage());
                    txx.printStackTrace();
                }
            }
            throw brex;
        } catch (Exception ex) {
            ex.printStackTrace();

            log.error("Errore creazione fattura: " + ex.getMessage()); //NOI18N
            log.error("Errore creazione fattura: ", ex); //NOI18N
            //throw new DatabaseException(ex);
        } finally {
            if (sx != null) {
                try {
                    //sx.close();
                } catch (Exception sxx) {
                    sxx.printStackTrace();
                }
            }
        }
        return newFattura;
    }

    /**
     * Questo metodo crea una fattura finale per il noleggio. Esclude dalla fatturazione le voci fatturate in precedenza.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La fattura creata e salvata nel database.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldDocumentoFiscale newFatturaProformaSaldo(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, User user) throws TitledException {
        MROldDocumentoFiscale newFattura = null;
        Transaction tx = null;

        try {
            contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contrattoNoleggio.getId());
            MROldCurrencyConversionRate ccr = null;

            if (contrattoNoleggio.getConversionRate() != null) {
                ccr = (MROldCurrencyConversionRate) sx.get(MROldCurrencyConversionRate.class, contrattoNoleggio.getConversionRate().getId());
            }

            List<MROldPartita> partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, contrattoNoleggio);
            //List<MROldPrimanota> contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, contrattoNoleggio);
            newFattura = PartitaFactory.newPartitaSaldoProforma(sx, contrattoNoleggio, sedeOperativa, partiteAcconti, null, user).getFattura();
            newFattura.setConversionRate(ccr);
            //newFattura.getPrimanota().setConversionRate(ccr);

        } catch (BusinessRuleException brex) {
            if (tx != null) {
                try {
                    //tx.rollback();
                } catch (Exception txx) {
                    log.error(txx.getMessage());
                    txx.printStackTrace();
                }
            }
            throw brex;
        } catch (Exception ex) {
            ex.printStackTrace();

            log.error("Errore creazione fattura: " + ex.getMessage()); //NOI18N
            log.error("Errore creazione fattura: ", ex); //NOI18N
            //throw new DatabaseException(ex);
        } finally {
            if (sx != null) {
                try {
                    //sx.close();
                } catch (Exception sxx) {
                    sxx.printStackTrace();
                }
            }
        }
        return newFattura;
    }

    /**
     * Questo metodo crea una fattura finale per il noleggio. Esclude dalla fatturazione le voci fatturate in precedenza.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la numerazione.
     * @return La fattura creata e salvata nel database.
     * @throws Exception Se qualcosa va storto con la creazione della fattura.
     */
    public static MROldDocumentoFiscale newFatturaProformaAcconto(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, User user) throws TitledException {
        MROldDocumentoFiscale newFattura = null;
        Transaction tx = null;
        try {
            contrattoNoleggio = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, contrattoNoleggio.getId());
            //List<MROldPrimanota> contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, contrattoNoleggio);
//            if (contropartiteAperte.isEmpty()) {
//                throw new BusinessRuleException(
//                        bundle.getString("DocumentoFiscaleFactory.msgNessunaFatturaAccontoDaEmettereMessage"),
//                        bundle.getString("DocumentoFiscaleFactory.msgNessunaFatturaAccontoDaEmettereTitle"));
//            }
            //newFattura = PartitaFactory.newPartitaAcconto(sx, contrattoNoleggio, sedeOperativa, null, user).getFattura();
            newFattura = PartitaFactory.newPartitaAccontoProforma(sx, contrattoNoleggio, sedeOperativa, null, user).getFattura();
            newFattura.setConversionRate(contrattoNoleggio.getConversionRate());

            newFattura.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));
           // tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();

            if (tx != null) {
                try {
                    //tx.rollback();
                } catch (Exception txx) {
                    txx.printStackTrace();
                }
            }
            log.error("Errore creazione fattura: " + ex.getMessage()); //NOI18N
            log.debug("Errore creazione fattura: ", ex); //NOI18N
            throw new DatabaseException(ex);
        } finally {
            if (sx != null) {
                try {
                    //sx.close();
                } catch (Exception sxx) {
                    sxx.printStackTrace();
                }
            }
        }
        return newFattura;
    }

    /**
     * Questo metodo crea una fattura di acconto per il noleggio, senza la primanota e senza la partita. La fattura contiene una sola riga, dell'acconto.
     * @param sx La sessione al database.
     * @param contrattoNoleggio Il contratto da fatturare
     * @param sedeOperativa La sede operativa per la fattura.
     * @return La fattura creata, ma non salvata nel database.
     */
    static MROldDocumentoFiscale newFatturaProformaAcconto(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldSede sedeOperativa, Double acconto, User user) {
        MROldDocumentoFiscale newFattura = newIntestazioneFattura(sx, contrattoNoleggio, sedeOperativa, MROldDocumentoFiscale.FTP, user);
        MROldPianoDeiConti contoContabile = ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.CLIENTI_C_ANTICIPI);
        newFattura.setFatturaRighe(new ArrayList());

        //newFattura.getFatturaRighe().add(newRigaAcconto(bundle.getString("DocumentoFiscaleFactory.msgAcconto"), acconto, contrattoNoleggio.getTariffa().getCodiceIva(), contoContabile));
        ImpostaFactory imposteFattura = new ImpostaFactory(newFattura.getFatturaRighe());
        newFattura.setFatturaImposte(imposteFattura.getImposte());
        newFattura.setTotaleRighe(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleAcconti(imposteFattura.getTotaleAcconti());
        newFattura.setTotaleImponibile(imposteFattura.getTotaleImponibile());
        newFattura.setTotaleIva(imposteFattura.getTotaleImposta());
        newFattura.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
        if (Boolean.TRUE.equals(newFattura.getPagamento().getScadenziario())) {
            ScadenzaUtils.creaScadenze(newFattura);
        }
        newFattura.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, newFattura.getNumerazione(), newFattura.getAnno()));

        if (contrattoNoleggio.getConversionRate() != null) {
            newFattura.setConversionRate(contrattoNoleggio.getConversionRate());
            //newFattura.getPrimanota().setConversionRate(contrattoNoleggio.getConversionRate());
        }

        //assegna la fattura alla prenotazione se esiste
        newFattura.setPrenotazione(PrenotazioniUtils.prenotazioneEsistente(sx, contrattoNoleggio));

        return newFattura;
    }

    /**
     *
     * @param sx
     * @param newFattura
     * @param prepagato
     * @param numerazioneRegistrazione
     * @param numeroRegistrazione
     * @param numerazionePartite
     * @param numeroPartita
     * @return
     * @throws BusinessRuleException
     */
    public static MROldDocumentoFiscale newFatturaAutomatica(Session sx, MROldDocumentoFiscale newFattura, Boolean prepagato, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione, MROldNumerazione numerazionePartite, Integer numeroPartita, User aUser) throws BusinessRuleException {
        List<MROldPrimanota> contropartiteAperte = new ArrayList<MROldPrimanota>();
        List<MROldPartita> partiteAcconti = new ArrayList<MROldPartita>();
        if (!prepagato) {
            partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, newFattura.getContratto());
            contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, newFattura.getContratto());
        }
        return PartitaFactory.newPartitaSaldo(sx, newFattura, prepagato, numerazioneRegistrazione, numeroRegistrazione, numerazionePartite, numeroPartita, partiteAcconti, contropartiteAperte, aUser).getFattura();
    }

    public static MROldProgressivoInterface checkNumerazioneDoppelGanger(Session sx, String clasz, MROldProgressivoInterface p, MROldNumerazione numerazione, Integer anno, String tipoDocumento){
        boolean isDuplicated = true;
        List tipoList = new ArrayList();
        if ( (MROldDocumentoFiscale.FT.equals(tipoDocumento) || MROldDocumentoFiscale.FTA.equals(tipoDocumento)) || MROldDocumentoFiscale.FT_EN.equals(tipoDocumento) || MROldDocumentoFiscale.FT_ES.equals(tipoDocumento) ) {
            clasz = "MROldFattura";
        } else if (MROldDocumentoFiscale.NC.equals(tipoDocumento) || MROldDocumentoFiscale.NC_EN.equals(tipoDocumento) || MROldDocumentoFiscale.NC_ES.equals(tipoDocumento)) {
            clasz = "MROldNotaCredito";
        } else if (MROldDocumentoFiscale.RF.equals(tipoDocumento) || MROldDocumentoFiscale.RF_EN.equals(tipoDocumento) || MROldDocumentoFiscale.RF_ES.equals(tipoDocumento)) {
            clasz = "MROldRicevutaFiscale";
        } else if (MROldDocumentoFiscale.FTP.equals(tipoDocumento)) {
            clasz = "FatturaProforma";
        }
        while (isDuplicated) {
            String hql = "from "+clasz+" as c "
                    + " WHERE "
                    + "c.numero = :numero and "
                    + "c.prefisso = :prefisso";

            if (anno!=null)
                hql += " and c.anno = :anno";

            Query q= sx.createQuery(hql)
                    .setParameter("numero", p.getNumero())
                    .setParameter("prefisso", p.getPrefisso());
//                    .setParameter("tipo", tipoList);

            if (anno!=null)
                q.setParameter("anno", anno);

            List lista = q.list();

            if (p.getNumerazione() == null) {
                p.setNumerazione(NumerazioniUtils.getNumerazione(sx,
                        numerazione));
            }

            if ((p.getId() == null && lista.size() > 1)
                    || (p.getId() != null && lista.size() > 1)) {

                p.setNumero(NumerazioniUtils.aggiornaProgressivo(
                        sx,
                        p.getNumerazione(),
                        anno));
            } else {

                isDuplicated = false;
            }

        }
        sx.update(p);
        sx.flush();
        return p;
    }

    public static int getInvoicvePerContractCount(Session sx, MROldContrattoNoleggio contrattoNoleggio){
        int count = 0;

        List invList = sx.createQuery("select d from MROldDocumentoFiscale d where d.contratto.id = :contractId").setParameter("contractId", contrattoNoleggio.getId()).setCacheable(true).list();
        count = invList.size();
        return count;
    }
//    public static Map<MROldPartita, MROldDocumentoFiscale> newFatturaAutomaticaSaldoAndPartita(Session sx, MROldDocumentoFiscale newFattura, MROldSede sedeOperativa, Boolean prepagato, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione, MROldNumerazione numerazionePartite, Integer numeroPartita, MROldParcoVeicoli veicolo) throws BusinessRuleException {
//        List<MROldPrimanota> contropartiteAperte = new ArrayList<MROldPrimanota>();
//        List<MROldPartita> partiteAcconti = new ArrayList<MROldPartita>();
//
//        //partiteAcconti = ContabUtils.leggiPartiteAperteFattureAcconto(sx, newFattura.getRent2rent());
//        //contropartiteAperte = ContabUtils.leggiContropartiteAperte(sx, newFattura.getRent2rent());
//
//        Map<MROldPartita, MROldDocumentoFiscale> partitaDocumento = new HashMap<MROldPartita, MROldDocumentoFiscale>();
//        MROldPartita focusPartita = PartitaFactory.newPartitaSaldoNoSaveInSession(sx, newFattura, sedeOperativa, prepagato, numerazioneRegistrazione, numeroRegistrazione, numerazionePartite, numeroPartita, partiteAcconti, contropartiteAperte, veicolo);
//
//        if(focusPartita != null){
//            MROldDocumentoFiscale doc = focusPartita.getFattura();
//            if(doc != null){
//                partitaDocumento.put(focusPartita, doc);
//            }
//        }
//        return partitaDocumento;
//    }

}
