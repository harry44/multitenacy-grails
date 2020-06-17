/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.myrentcs.utils;

import grails.util.Holders;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.TariffaNonValidaException;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.api.utils.ContabUtils;
import it.myrent.ee.api.utils.MovimentoAutoUtils;
import it.myrent.ee.api.utils.TariffeUtils;
import it.myrent.ee.db.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author bogdan
 */
public abstract class AbstractFatturazione implements Fatturazione {
    private Session sx;
    private MROldContrattoNoleggio contrattoNoleggio;
    private MROldPrenotazione prenotazione;
    private MROldPreventivo preventivo;
    private MROldTariffa tariffa;
    private Date inizio;
    private Date fine;
    private Integer km;
    private Integer litri;
    private MROldCarburante carburante;
    private Double scontoPercentuale;
    private Integer giorniVoucher;
    /*
     * Andrea
     */
    private Double giorniNoleggioApplicability;

    /*
     * Andrea
     */

    private MROldSede locationPickUp=null;



    protected AbstractFatturazione(MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        this.contrattoNoleggio = contrattoNoleggio;
        this.tariffa = tariffa;
        this.inizio = inizio;
        this.fine = fine;
        this.km = km;
        this.litri = litri;
        this.carburante = carburante;
        this.scontoPercentuale = scontoPercentuale;
        this.giorniVoucher = giorniVoucher;

        if (contrattoNoleggio!=null)
            this.locationPickUp=contrattoNoleggio.getSedeUscita();

    }
    protected AbstractFatturazione(MROldPreventivo preventivo, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        this.preventivo = preventivo;
        this.tariffa = tariffa;
        this.inizio = inizio;
        this.fine = fine;
        this.km = km;
        this.litri = litri;
        this.carburante = carburante;
        this.scontoPercentuale = scontoPercentuale;
        this.giorniVoucher = giorniVoucher;

        if (preventivo!=null)
            this.locationPickUp=preventivo.getSedeUscita();

    }


    protected AbstractFatturazione(MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        this.contrattoNoleggio = null;
        this.tariffa = tariffa;
        this.inizio = inizio;
        this.fine = fine;
        this.km = km;
        this.litri = litri;
        this.carburante = carburante;
        this.scontoPercentuale = scontoPercentuale;
        this.giorniVoucher = giorniVoucher;
    }

    protected AbstractFatturazione(MROldPrenotazione aPrenotazione, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        this.prenotazione = aPrenotazione;
        this.tariffa = tariffa;
        this.inizio = inizio;
        this.fine = fine;
        this.km = km;
        this.litri = litri;
        this.carburante = carburante;
        this.scontoPercentuale = scontoPercentuale;
        this.giorniVoucher = giorniVoucher;

        if (aPrenotazione!=null)
            this.locationPickUp=aPrenotazione.getSedeUscita();

    }

    protected AbstractFatturazione(Session sx, MROldPrenotazione aPrenotazione, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
        this.sx = sx;
        this.prenotazione = aPrenotazione;
        this.tariffa = tariffa;
        this.inizio = inizio;
        this.fine = fine;
        this.km = km;
        this.litri = litri;
        this.carburante = carburante;
        this.scontoPercentuale = scontoPercentuale;
        this.giorniVoucher = giorniVoucher;

        if (aPrenotazione!=null)
            this.locationPickUp=aPrenotazione.getSedeUscita();
    }

    

    private static Log log = LogFactory.getLog(AbstractFatturazione.class);
    protected static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");
    public static final String UNITA_MISURA_GIORNI = bundle.getString("FatturaUtils.msgGG");
    public static final String UNITA_MISURA_KM = bundle.getString("FatturaUtils.msgKM");
    public static final String DESCRIZIONE_GIORNI_TARIFFA = bundle.getString("FatturaUtils.msgListino0GiorniTariffa");
    public static final String DESCRIZIONE_GIORNI_EXTRA = bundle.getString("FatturaUtils.msgListino0GiorniExtra");
    public static final String DESCRIZIONE_COMBUSTIBILE_MANCANTE = bundle.getString("FatturaUtils.msgMaterialeConsumo0");
    /**
     * Il ritardo massimo consentito (in minuti) al rientro del mezzo. Superata
     * questa durata, il ritardo viene fatturato.
     */
    public static final int RITARDO_MASSIMO_CONSENTITO = 59; //minuti


    public List<MROldRigaDocumentoFiscale> calcolaRigheProssimaFattura(Session sx)  throws TariffaNonValidaException{
        List righeNuove = null;
        try {
            try {
                righeNuove = calcolaRigheProssimaFattura(sx);
            } catch (Exception fvex) {
                righeNuove = new ArrayList<MROldRigaDocumentoFiscale>();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (sx != null) {
                try {
                    if (sx.isOpen())
                        sx.close();
                } catch (Exception sxx) {
                }
            }
        }
        return righeNuove;
    }


    /*public List<MROldRigaDocumentoFiscale> ricalcolaRigheUltimaFattura(MROldDocumentoFiscale fattura) throws BusinessRuleException, DatabaseException {
        List righeNuove;
        Transaction tx = null;
        try {
            tx = sx.beginTransaction();
            righeNuove = ricalcolaRigheUltimaFattura(sx, fattura);
            tx.commit();
        } catch (TariffaNonValidaException tex) {
            tx.commit();
            throw new BusinessRuleException(tex.getMessage(), bundle.getString("FatturaUtils.msgTARIFFA_NON_VALIDA"), tex);
        } catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception txx) {
                }
            }
            throw new DatabaseException(bundle.getString("FatturaUtils.msgImpossibileLeggereDatiPerCalcolareFattura"), bundle.getString("FatturaUtils.msgErroreDatabase"), ex);
        } finally {
            if (sx != null) {
                try {
                    sx.close();
                } catch (Exception sxx) {
                }
            }
        }
        return righeNuove;
    }*/

    /*protected MROldPianoDeiConti leggiCodiceSottoconto() {
        return ContabUtils.leggiSottoconto(new Integer(4), new Integer(10), new Integer(6));
    }*/

    protected MROldPianoDeiConti leggiCodiceSottoconto(Session sx) throws HibernateException {
        return ContabUtils.leggiSottoconto(sx, new Integer(4), new Integer(10), new Integer(6));
    }

    /*protected MROldPianoDeiConti leggiCodiceSottocontoCarburante() {
        return ContabUtils.leggiSottoconto(new Integer(4), new Integer(10), new Integer(3));
    }*/

    protected MROldPianoDeiConti leggiCodiceSottocontoCarburante(Session sx) throws HibernateException {
        return ContabUtils.leggiSottoconto(sx, new Integer(4), new Integer(10), new Integer(3));
    }

    /*protected MROldCodiciIva leggiCodiceIva() {
        return ContabUtils.leggiCodiceIva(new Integer(1));
    }*/

    protected MROldCodiciIva leggiCodiceIva(Session mySession) throws HibernateException {
        return ContabUtils.leggiCodiceIva(mySession, new Integer(1));
    }

    protected boolean isVerificaMinimoTariffa(Session sx) {
        return true;
    }

    /**
     * Calcola l'importo giornaliero scorporando l'iva e gli optionals inclusi
     * nell'<code>importoTariffa</code> dato il numero di giorni di riferimento.
     *
     * @param tariffa la tariffa di riferimento
     * @param importoTariffa l'importo complessivo della tariffa
     * @param numeroGiorni il numero di giorni al quale e' riferito
     * l'<code>importoTariffa</code>
     * @return l'importo giornaliero del noleggio.
     **/
    protected double calcolaImportoGiornaliero(Session sx, MROldTariffa tariffa, boolean ivaInclusa, double aliquotaIva, double importoTariffa, double numeroGiorni, boolean giornoExtra, boolean extraPrePay) throws HibernateException {
        if (Preferenze.getListiniImportiGiornalieri(sx)) {
            importoTariffa *= numeroGiorni;
        }
        List optionalsInclusi = new ArrayList();
        double oneriAeroportualiIvaApplicabile = 0.0, oneriAeroportualiSenzaIva = 0.0, x = 0.0,
                s = 0.0, si = 0.0, sp = 0.0, spi = 0.0, iva = (ivaInclusa ? aliquotaIva : 0.0);
        //if (!giornoExtra) {
            if (tariffa.getOptionalsTariffa() != null) {
                Iterator it = tariffa.getOptionalsTariffa().values().iterator();
                while (it.hasNext()) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                    if (Boolean.TRUE.equals(optionalTariffa.getIncluso()) && Boolean.TRUE.equals(optionalTariffa.getSelezionato())) {
                        if (giornoExtra && optionalTariffa.getOptional().getImportoFisso()) {
                            //;
                        } else {
                            if (!Boolean.TRUE.equals(optionalTariffa.getOptional().getDepositoCauzionale())) {
                                optionalsInclusi.add(optionalTariffa);
                            }
                        }
                    }
                }
            }
        //}
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
                            s += importo;
                        }
                    }
                }
            }
        }
        //TODO - sbagliato - x = (importoTariffa - (s + si)*(1.0+sp*spi) - iva*(si + s * spi + si * spi)) / (iva+1.0) / (1.0 + spi + sp) / numeroGiorni;
        //x = (importoTariffa - si * (1.0 + spi + sp + iva * spi + iva) + s * (1.0 + spi + sp + iva * spi)) / (1.0 + spi + sp + iva * spi + iva) / numeroGiorni;

//        System.out.println("Vecchio valore calcolato: " + x);

       //x = FormulaCalcoloNoleggio.calcolaImportoGiornalieroOldWay(importoTariffa, numeroGiorni, s, si, sp + oneriAeroportualiSenzaIva, spi + oneriAeroportualiIvaApplicabile, iva);

//        System.out.println("Vecchio valore calcolato con nuovo metodo: " + x);

        double oneriAeroportuali, totaleQuotePercentualiSenzaIvaApplicabile, totaleQuotePercentualiConIvaApplicabile, totaleOptionalFissiEGiornalieriSenzaIvaApplicabile, totaleOptionalFissiEGiornalieriConIvaApplicabile;


        totaleOptionalFissiEGiornalieriConIvaApplicabile = si;
//        System.out.println("totaleOptionalFissiEGiornalieriConIvaApplicabile: " + totaleOptionalFissiEGiornalieriConIvaApplicabile);

        totaleOptionalFissiEGiornalieriSenzaIvaApplicabile = s;
        //System.out.println("totaleOptionalFissiEGiornalieriSenzaIvaApplicabile: " + totaleOptionalFissiEGiornalieriSenzaIvaApplicabile);


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


    protected double calcolaImportoGiornaliero(Session sx, Map optionalsTariffa, boolean ivaInclusa, double aliquotaIva, double importoTariffa, double numeroGiorni, boolean extraPrePay) throws HibernateException {
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

    protected Double calcolaKmCarburanteMancante(Session sx, Map totaliCarburanti) {
        double totaleKm = 0.0;

        if (getContrattoNoleggio() != null ) {
            Set movimentiSet = MovimentoAutoUtils.getMovementsOfAgreement(sx, getContrattoNoleggio());
            if(movimentiSet!=null && movimentiSet.size()>0){
                MROldContrattoNoleggio contrattoNol = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, getContrattoNoleggio().getId());
                Iterator movimenti = movimentiSet.iterator();
                while (movimenti.hasNext()) {
                    MROldMovimentoAuto aMovimento = (MROldMovimentoAuto) movimenti.next();
                    if (!aMovimento.getAnnullato().booleanValue()) {
                        int kmMovimento = 0, litriMovimento = 0;
                        if (aMovimento.getChiuso().booleanValue()) {
                            kmMovimento = aMovimento.getKmFine().intValue() - aMovimento.getKmInizio().intValue();
                            litriMovimento = aMovimento.getCombustibileInizio().intValue() - aMovimento.getCombustibileFine().intValue();
                        } else {
                            if (getKm() != null) {
                                kmMovimento = getKm().intValue() - aMovimento.getKmInizio().intValue();
                            }
                            if (getLitri() != null) {
                                litriMovimento = aMovimento.getCombustibileInizio().intValue() - getLitri().intValue();
                            }
                        }

                        if (kmMovimento > 0) {
                            totaleKm += (double) kmMovimento;
                        }

                        Boolean chargeFuelThreshold = Preferenze.getChargeFuel(sx);
                        Integer litriThreshold = Preferenze.getThreshold(sx);

                        if (chargeFuelThreshold ==null)
                            chargeFuelThreshold = false;

                        if (litriMovimento > 0 &&
                                chargeFuelThreshold && litriMovimento <= litriThreshold ) {
                            MROldParcoVeicoli car = (MROldParcoVeicoli)sx.get (MROldParcoVeicoli.class, aMovimento.getVeicolo().getId());
                            if (car.getCarburante()!=null)
                                setCarburante((MROldCarburante) sx.get(MROldCarburante.class, car.getCarburante().getId()));
                            else
                                setCarburante((MROldCarburante) sx.get(MROldCarburante.class, getCarburante().getId()));

                            Integer litriCarburante = (Integer) totaliCarburanti.get(getCarburante());

                            totaliCarburanti.put(getCarburante(), litriCarburante);


                        } else if (litriMovimento > 0) {
                            MROldParcoVeicoli car = (MROldParcoVeicoli)sx.get (MROldParcoVeicoli.class, aMovimento.getVeicolo().getId());
                            if (car.getCarburante()!=null){
                                setCarburante((MROldCarburante) sx.get(MROldCarburante.class, car.getCarburante().getId()));
                            } else {
                                setCarburante((MROldCarburante) sx.get(MROldCarburante.class, getCarburante().getId()));
                            }
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
                if (getKm() != null) {
                    totaleKm = getKm().doubleValue();
                }
                if (getLitri() != null && getCarburante() != null) {
                    setCarburante((MROldCarburante) sx.get(MROldCarburante.class, getCarburante().getId()));
                    totaliCarburanti.put(getCarburante(), getLitri());
                }
            }
        } else {
            if (getKm() != null) {
                totaleKm = getKm().doubleValue();
            }
            if (getLitri() != null && getCarburante() != null) {
                setCarburante((MROldCarburante) sx.get(MROldCarburante.class, getCarburante().getId()));
                totaliCarburanti.put(getCarburante(), getLitri());
            }
        }
        return totaleKm;
    }


    public List<MROldRigaDocumentoFiscale> calcolaRigheFattura() {
        try {
            return calcolaRigheProssimaFattura();
        } catch (Exception ex) {
        }
        return new ArrayList();
    }


    public List<MROldRigaDocumentoFiscale> calcolaRigheFattura(Session sx) {
        try {
            return calcolaRigheProssimaFattura(sx);
        } catch (Exception ex) {
        }
        return new ArrayList();
    }

    /**
     * Calcola le righe della fattura con i parametri specificati. Lo sconto e'
     * in percentuale.
     */
    /*protected List calcolaRighe(Component parent) {
        List righeFattura = new ArrayList();
        try {
            /*
             * Andrea, added parent
             */
            /*righeFattura = calcolaRighe(sx);
        } catch (TariffaNonValidaException tex) {
            tex.showMessage(parent, null);
            tex.printStackTrace();
        } catch (Exception ex) {
            log.debug("FatturaUtils.calcolaRighe : ", ex); //NOI18N
            log.error("FatturaUtils.calcolaRighe : Message is : " + ex.getMessage()); //NOI18N
            log.info("FatturaUtils.calcolaRighe : Message is : " + ex.getMessage()); //NOI18N
            JOptionPane.showMessageDialog(parent, bundle.getString("FatturaUtils.msgImpossibileLeggereDatiPerCalcolareFattura"), bundle.getString("FatturaUtils.msgErroreDatabase"), JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            if (sx != null) {
                try {
                    sx.close();
                } catch (Exception sxx) {
                }
            }
        }
        return righeFattura;
    }*/


    protected List calcolaRighe(Session sx) throws HibernateException, TariffaNonValidaException {

        /**
         * *
         * Andiamo ad implementare la ricerca del listino per l'eventuale Fonte
         * Extra PrePay FONTE_EXTRA_PPAY
         *
         */
        List righe = new ArrayList();

        if (getContrattoNoleggio() != null && getContrattoNoleggio().getId() != null) {
            setContrattoNoleggio((MROldContrattoNoleggio) sx.load(MROldContrattoNoleggio.class, getContrattoNoleggio().getId()));
        } else {
//            if (getContrattoNoleggio() != null){
//                sx.saveOrUpdate(getContrattoNoleggio());
//            }
            //setContrattoNoleggio(null);
        }
        if (getPrenotazione() != null && getPrenotazione().getId() != null) {
            setPrenotazione((MROldPrenotazione) sx.load(MROldPrenotazione.class, getPrenotazione().getId()));
        } else {

           
            //setPrenotazione(null);
        }

        MROldPianoDeiConti pianoDeiConti = leggiCodiceSottoconto(sx);

        if (getScontoPercentuale() == null) {
            setScontoPercentuale(new Double(0));
        }

        Map totaliCarburanti = new HashMap();
        double totaleKm = calcolaKmCarburanteMancante(sx, totaliCarburanti);

        Double giorniPrepagati = 0.0;
        if (getGiorniVoucher() != null) {
            giorniPrepagati = getGiorniVoucher().doubleValue();
        }
        //Arrotondiamo per eccesso, imputando almeno 1 giorno. Non teniamo conto dell'ora legale.
        //Ritardo massimo 59 minuti.
        double giorniEsattiSenzaRitardo = FormattedDate.numeroGiorni(getInizio(), getFine(), true);

        Integer ritardoMassimoConsentito = Preferenze.getContrattoRitardoMassimoMinuti(sx);

        double giorniEsatti = FormattedDate.numeroGiorni(getInizio(), FormattedDate.add(getFine(), Calendar.MINUTE, -ritardoMassimoConsentito.intValue()), true);
        double giorniInteriPerEccesso = Math.ceil(giorniEsatti);
        double giorniInteriPerDiffetto = Math.floor(giorniEsatti);

        double durataNoleggioPerEccesso = Math.max(1.0, giorniInteriPerEccesso);

        boolean prepagato = (giorniPrepagati > 0);
        boolean prepagatoExtra = (prepagato && durataNoleggioPerEccesso > giorniPrepagati);

        /*
         * Andrea
         */
        double durataNoleggioPerEccessoForOptional = durataNoleggioPerEccesso;
        giorniNoleggioApplicability = durataNoleggioPerEccesso;
        double giorniPrepagatiForOptional = giorniPrepagati;


        boolean mezzaGiornata = false;
        boolean giornataRidotta = false;
        boolean notteExtra = false;
        boolean giornoFestivo = false;
        double giorniFestivi = 0;

        if (prepagatoExtra) {
            aggiungiRighePrepagatoExtra(
                    sx,
                    righe,
                    getTariffa(),
                    durataNoleggioPerEccesso,
                    giorniPrepagati,
                    getScontoPercentuale(),
                    pianoDeiConti);
        } else if (!prepagato) {
            /*if (getTariffa() != null && getTariffa().getId() != null) {
                setTariffa((MROldTariffa) sx.get(MROldTariffa.class, getTariffa().getId()));
            }*/
            if (getTariffa().getOptionalsTariffa() != null && !getTariffa().getOptionalsTariffa().isEmpty()) {
                Iterator<MROldOptional> optionals = getTariffa().getOptionalsTariffa().keySet().iterator();
                Iterator<MROldOptional> optionalsRate = getTariffa().getOptionalsTariffa().keySet().iterator();
            /*
             * Check if there is Rate Optional to apply
             */
                while (optionalsRate.hasNext()) {
                    MROldOptional optional = optionalsRate.next();
                /*
                 * If tempoExtra and isRateOptional
                 */
                    if (optional.getTempoExtra() && optional.getIsRateOptional() != null && optional.getIsRateOptional()) {
                        MROldOptionalTariffa optionalTariffa = getTariffa().getOptionalsTariffa().get(optional);
                        if(optionalTariffa.getSelezionato()){
                        /*
                        * if TempoExtra and isRateOptional and Selected
                        */
                            if(checkRateOptionalApplicability(sx, optional)){
                            /*
                            * set rental days to 0 (not for optional)
                            */
                                durataNoleggioPerEccesso = 0.0;
                                giorniInteriPerDiffetto = 0.0;
                                giorniInteriPerEccesso = 0.0;
                            /*
                            * Check if fixed price or not
                            */
                                if(optionalTariffa.getOptional().getImportoGiornaliero()){
                                    optionalTariffa.setQuantita(durataNoleggioPerEccessoForOptional);
                                }
                                else{
                                    optionalTariffa.setQuantita(1.0);
                                }
                            }
                            else{
                            /*
                            * Optional was selected but not applicable -->throws error
                            */
                            /*
                            * Set optional select to false
                            */
                                optionalTariffa.setSelezionato(Boolean.FALSE);
                            }
                        }
                    }
                }
            /*
             * Check Tempo Extra Optional
             */
                while (optionals.hasNext()) {
                    MROldOptional optional = optionals.next();
                    if (optional.getTempoExtra()) {
                        MROldOptionalTariffa optionalTariffa = getTariffa().getOptionalsTariffa().get(optional);
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
                            righe, getTariffa(), getInizio(), getFine(),
                            giorniNoleggio, getScontoPercentuale(),
                            pianoDeiConti);
                }

            } else {
                // Aggiungiamo il numero di giorni arrotondato per eccesso, minimo uno.
                aggiungiRigheNonPrepagato(sx,
                        righe, getTariffa(), getInizio(), getFine(),
                        durataNoleggioPerEccesso, getScontoPercentuale(),
                        pianoDeiConti);
            }
        }

        /*
         * Andrea
         *  Include all the optionals
         */
        List<String> optionalCheckedIncluded = new ArrayList<String>();
        //Optional iterator
        if (getTariffa().getOptionalsTariffa() != null && !getTariffa().getOptionalsTariffa().isEmpty()) {
            Iterator<MROldOptional> optionalstariffa_checkinclude = getTariffa().getOptionalsTariffa().keySet().iterator();
            while (optionalstariffa_checkinclude.hasNext()) {
                MROldOptional optional = optionalstariffa_checkinclude.next();
                MROldOptionalTariffa optionalTariffa = getTariffa().getOptionalsTariffa().get(optional);
            /*
             * Check if selected
             */
                if(optionalTariffa.getSelezionato()){
                /*
                 * Check if optional is applicable
                 */
                    if(checkRateOptionalApplicability(sx, optional)){
                    /*
                     * check if not present in array list of optional tariffa checked
                     */
                        if(!optionalCheckedIncluded.contains(optional.getCodice())){
                            /*
                             * Add the optional to the list of included checked
                             * Get the included optionals with tokenizer
                             */
                            optionalCheckedIncluded.add(optional.getCodice());
                            String optionalsToIncludeString = optional.getIncludeOptionals();
                            if(optionalsToIncludeString != null){
                                StringTokenizer st = new StringTokenizer(optionalsToIncludeString, ",");
                                while (st.hasMoreElements()) {
                                    String includedOptionalCode = (String)st.nextElement();
                                    /*
                                     * add the included optionals
                                     */
                                    addIncludedOptional(sx, includedOptionalCode, optionalCheckedIncluded);
                                }
                            }
                        }//End optional included yet
                    }//End optional applicability
                    else{
                    /*
                     * Optional not applicable and not virtual, show if is not true
                     */
                        if(optional.getIsVirtual() == null || !optional.getIsVirtual()){
                            //JScrollPane scrollpane = new JScrollPane();
                            //String categories[] = { "The optional: "+optional.getCodice()+" is not applicable"};
                            //JList list = new JList(categories);

                            //scrollpane = new JScrollPane(list);

                            //JPanel panel = new JPanel();
                            //panel.add(scrollpane);

                            //scrollpane.getViewport().add(list);
                            //JOptionPane.showMessageDialog(null, scrollpane, "Optional not applicable",
                            //                                       JOptionPane.PLAIN_MESSAGE);
                        }
                    /*
                    * Set optional select to false
                    */
                        optionalTariffa.setSelezionato(Boolean.FALSE);
                    }
                }
            }//End while
        }



        /*
         * Andrea
         *  Exclude all the optionals
         */
        //Optional iterator
        if (getTariffa().getOptionalsTariffa() != null && !getTariffa().getOptionalsTariffa().isEmpty()) {
            Iterator<MROldOptional> optionalstariffa_checkexclude = getTariffa().getOptionalsTariffa().keySet().iterator();
            while (optionalstariffa_checkexclude.hasNext()) {
                MROldOptional optional = optionalstariffa_checkexclude.next();
                MROldOptionalTariffa optionalTariffa = getTariffa().getOptionalsTariffa().get(optional);
            /*
             * Check if selected
             */
                if (optionalTariffa.getSelezionato()) {
                    String optionalsToExcludeString = optional.getExcludeOptionals();
                    if (optionalsToExcludeString != null) {
                        StringTokenizer st = new StringTokenizer(optionalsToExcludeString, ",");
                        while (st.hasMoreElements()) {
                            String excludedOptionalCode = (String) st.nextElement();
                            if (!checkExcluded(sx, excludedOptionalCode)) {
                            /*
                             * GET ERROR
                             */
                                //JOptionPane.showMessageDialog(null, "Exclusive optional error", "The optional: "+optional.getCodice()+" can't be selected with optional: "+excludedOptionalCode+", the optional  "+optional.getCodice()+" and its included optionals will be deselected.", JOptionPane.INFORMATION_MESSAGE);
//                            JScrollPane scrollpane = new JScrollPane();
//                            String categories[] = {"The optional: " + optional.getCodice() + " can't be selected with optional: " + excludedOptionalCode + ", the optional  " + optional.getCodice() + " and its included optionals will be deselected."};
//                            JList list = new JList(categories);
//
//                            scrollpane = new JScrollPane(list);
//
//                            JPanel panel = new JPanel();
//                            panel.add(scrollpane);
//
//                            scrollpane.getViewport().add(list);
//                            JOptionPane.showMessageDialog(null, scrollpane, "Exclusive optional error",
//                                    JOptionPane.PLAIN_MESSAGE);
                            /*
                             * Set optional select to false
                             */
                                optionalTariffa.setSelezionato(Boolean.FALSE);
                            /*
                             * Deselect all included optional
                             */
                                deselectOptionalAndIncluded(optional);
                            }
                        }
                    }
                }//End if selected
            }
        }



        
        //FIXME Da chiedere se addebitare anche qui il noleggio contabile.
        return aggiungiRigheOptionals(sx, righe, getTariffa(), getScontoPercentuale(), durataNoleggioPerEccessoForOptional, giorniPrepagatiForOptional, totaleKm, totaliCarburanti);
    }


    /*
     * Andrea
     * Include the optional with the optional code and the optional included by it
     */
    protected void addIncludedOptional(Session sx, String includedOptionalCode, List<String> optionalCheckedIncluded){
        /*
         * Get the optionalTariffa from the optionals in the rate by optional code string
         */
        MROldOptionalTariffa optionalToInclude = null;
        MROldOptional optionalObjectToInclude = null;
        if(includedOptionalCode != null){
            Iterator<MROldOptional> itr = getTariffa().getOptionalsTariffa().keySet().iterator();
            while (itr.hasNext()) {
                MROldOptional optional = itr.next();
                if(optional.getCodice().equals(includedOptionalCode)){
                    optionalToInclude = getTariffa().getOptionalsTariffa().get(optional);
                    optionalObjectToInclude = optional;
                }
            }
        }
        /*
         * If I get the optional to include
         */
        if(optionalToInclude != null){
            /*
             * Check if is not yet in the included optional
             */
                if(!optionalCheckedIncluded.contains(includedOptionalCode)){
                    /*
                     * Check applicability
                     * Add the optional to the list of checked optional
                     */
                    optionalCheckedIncluded.add(includedOptionalCode);
                    if(checkRateOptionalApplicability(sx, optionalObjectToInclude)){
                        /*
                         * Select the optional
                         */
                        optionalToInclude.setSelezionato(Boolean.TRUE);
                        /*
                         * Add the optional included
                         */
                        String optionalsToIncludeString = optionalObjectToInclude.getIncludeOptionals();
                            if(optionalsToIncludeString != null){
                                StringTokenizer st = new StringTokenizer(optionalsToIncludeString, ",");
                                while (st.hasMoreElements()) {
                                    String optionalCodeToken = (String)st.nextElement();
                                    /*
                                     * add the included optionals
                                     */
                                    addIncludedOptional(sx, optionalCodeToken, optionalCheckedIncluded);
                                }
                            }
                    }//End not Applicable
                    else{
                        if(optionalObjectToInclude.getIsVirtual() == null || !optionalObjectToInclude.getIsVirtual()){

                               //JScrollPane scrollpane = new JScrollPane();
                               //String categories[] = {"The optional: "+optionalObjectToInclude.getCodice()+" is not applicable"};
                               //JList list = new JList(categories);

                               //scrollpane = new JScrollPane(list);

                               //JPanel panel = new JPanel();
                               //panel.add(scrollpane);

                               //scrollpane.getViewport().add(list);
                               //JOptionPane.showMessageDialog(null, scrollpane, "Object to include not applicable",
                                 //                                     JOptionPane.PLAIN_MESSAGE);
                        }
                    }
             }//End yet included
        }//Optional to include null
    }



    /*
     * Andrea
     * Get the optional and if is selected return false
     */
    protected boolean checkExcluded(Session sx, String excludedOptionalCode){
        /*
         * Get the optionalTariffa from the optionals in the rate by optional code string
         */
        boolean returnValue = new Boolean(Boolean.TRUE);
        MROldOptionalTariffa optionalToExclude = null;
        if(excludedOptionalCode != null){
            Iterator<MROldOptional> itr = getTariffa().getOptionalsTariffa().keySet().iterator();
            while (itr.hasNext()) {
                MROldOptional optional = itr.next();
                if(optional.getCodice().equals(excludedOptionalCode)){
                    optionalToExclude = getTariffa().getOptionalsTariffa().get(optional);
                }
            }
        }
        /*
         * If I get the optional to include
         */
        if(optionalToExclude != null){
            if (optionalToExclude.getSelezionato()){
                returnValue = new Boolean(Boolean.FALSE);
            }
        }//Optional to include null

        return returnValue;
    }

    /*
     * Andrea
     * deselect the optional and its included optional
     */
     protected void deselectOptionalAndIncluded(MROldOptional optional){
         if(optional != null){
             MROldOptionalTariffa optionalTariffa = getTariffa().getOptionalsTariffa().get(optional);
             if(optionalTariffa != null){
                 /*
                  * Check if is not yet deselected
                  */
                 if(optionalTariffa.getSelezionato()){
                     optionalTariffa.setSelezionato(Boolean.FALSE);
                     /*
                      * Get th eincluded optional to exclude
                      */
                     String optionalsToExcludeString = optional.getIncludeOptionals();
                     if(optionalsToExcludeString != null){
                         StringTokenizer st = new StringTokenizer(optionalsToExcludeString, ",");
                         while (st.hasMoreElements()) {
                             String optionalCodeToken = (String)st.nextElement();
                             /*
                             * get the optional from the string
                             */
                             if(optionalCodeToken != null){
                                Iterator<MROldOptional> itr = getTariffa().getOptionalsTariffa().keySet().iterator();
                                while (itr.hasNext()) {
                                    MROldOptional optionalToExcludeCheck = itr.next();
                                    if(optionalToExcludeCheck.getCodice().equals(optionalCodeToken)){
                                        deselectOptionalAndIncluded(optionalToExcludeCheck);
                                    }
                                }
                            }
                         }
                    }
                 }
             }
         }
     }


    /*
    * Andrea
    * Check if optional is applicable, if the optional applicability rule is null return true
    */
    protected boolean checkRateOptionalApplicability(Session sx, MROldOptional optional){
        boolean isRateOptionalApplicability = false;
        MROldContrattoNoleggio rentalAgreement = getContrattoNoleggio();
        MROldPrenotazione prenotazione = getPrenotazione();
        MROldPreventivo preventivo = getPreventivo();
        if(rentalAgreement != null){
            if(optional.getWebApplicability() != null && optional.getWebApplicability()!="" && optional.getWebApplicability().length()>0){
                Date inizio = getInizio();
                Date fine = getFine();
                Binding binding = new Binding();
                GroovyShell shell = new GroovyShell(getClass().getClassLoader(),binding);
                binding.setProperty("startDate", inizio);
                binding.setProperty("endDate", fine);
                binding.setProperty("rates", getTariffa());
                binding.setProperty("optional", optional);
                binding.setProperty("returnValue", false);
                binding.setProperty("rentDaysApplicability", giorniNoleggioApplicability);
                binding.setProperty("rentalAgreement", rentalAgreement);
                binding.setProperty("pickupLocation", rentalAgreement.getSedeUscita());
                //shell.evaluate("import it.myrent.ee.db.Tariffa; import it.myrent.ee.db.Optional; import java.util.Date; import java.util.Calendar; if(optional.getCodice().equals('WEEKEND')){       System.out.println('uno');     Calendar calendarInizio = Calendar.getInstance();            calendarInizio.setTime(startDate);      if(calendarInizio.get(Calendar.DAY_OF_WEEK) == 1 || calendarInizio.get(Calendar.DAY_OF_WEEK) == 7){            System.out.println('due');    Calendar calendarFine = Calendar.getInstance();                calendarFine.setTime(endDate);     System.out.println(endDate);     System.out.println(calendarFine.get(Calendar.DAY_OF_WEEK));      if(calendarFine.get(Calendar.DAY_OF_WEEK) == 1 || calendarFine.get(Calendar.DAY_OF_WEEK) == 7){                System.out.println('tre');      int numberOfDaysBeetwenDates = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));                    if(numberOfDaysBeetwenDates < 3){                System.out.println('4');        returnValue = true;                    }                }            }        }");
                shell.evaluate(optional.getWebApplicability());
                isRateOptionalApplicability = (Boolean)binding.getProperty("returnValue");
            } else{

            /*
             * Optional applicability is null, applicability is true
             */
                isRateOptionalApplicability = true;
            }
        } else if (prenotazione != null){
            if(optional.getWebApplicability() != null && optional.getWebApplicability()!="" && optional.getWebApplicability().length()>0){
                Date inizio = getInizio();
                Date fine = getFine();
                Binding binding = new Binding();
                GroovyShell shell = new GroovyShell(getClass().getClassLoader(),binding);
                binding.setProperty("startDate", inizio);
                binding.setProperty("endDate", fine);
                binding.setProperty("rates", getTariffa());
                binding.setProperty("optional", optional);
                binding.setProperty("returnValue", false);
                binding.setProperty("rentDaysApplicability", giorniNoleggioApplicability);
                binding.setProperty("rentalAgreement", prenotazione);
                binding.setProperty("pickupLocation", prenotazione.getSedeUscita());
                //shell.evaluate("import it.myrent.ee.db.Tariffa; import it.myrent.ee.db.Optional; import java.util.Date; import java.util.Calendar; if(optional.getCodice().equals('WEEKEND')){       System.out.println('uno');     Calendar calendarInizio = Calendar.getInstance();            calendarInizio.setTime(startDate);      if(calendarInizio.get(Calendar.DAY_OF_WEEK) == 1 || calendarInizio.get(Calendar.DAY_OF_WEEK) == 7){            System.out.println('due');    Calendar calendarFine = Calendar.getInstance();                calendarFine.setTime(endDate);     System.out.println(endDate);     System.out.println(calendarFine.get(Calendar.DAY_OF_WEEK));      if(calendarFine.get(Calendar.DAY_OF_WEEK) == 1 || calendarFine.get(Calendar.DAY_OF_WEEK) == 7){                System.out.println('tre');      int numberOfDaysBeetwenDates = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));                    if(numberOfDaysBeetwenDates < 3){                System.out.println('4');        returnValue = true;                    }                }            }        }");
                shell.evaluate(optional.getWebApplicability());
                isRateOptionalApplicability = (Boolean)binding.getProperty("returnValue");
            } else{

            /*
             * Optional applicability is null, applicability is true
             */
                isRateOptionalApplicability = true;
            }
        } else if(preventivo != null) {
            if(optional.getWebApplicability() != null && optional.getWebApplicability()!="" && optional.getWebApplicability().length()>0){
                Date inizio = getInizio();
                Date fine = getFine();
                Binding binding = new Binding();
                GroovyShell shell = new GroovyShell(getClass().getClassLoader(),binding);
                binding.setProperty("startDate", inizio);
                binding.setProperty("endDate", fine);
                binding.setProperty("rates", getTariffa());
                binding.setProperty("optional", optional);
                binding.setProperty("returnValue", false);
                binding.setProperty("rentDaysApplicability", giorniNoleggioApplicability);
                binding.setProperty("rentalAgreement", preventivo);
                binding.setProperty("pickupLocation", preventivo.getSedeUscita());
                //shell.evaluate("import it.myrent.ee.db.Tariffa; import it.myrent.ee.db.Optional; import java.util.Date; import java.util.Calendar; if(optional.getCodice().equals('WEEKEND')){       System.out.println('uno');     Calendar calendarInizio = Calendar.getInstance();            calendarInizio.setTime(startDate);      if(calendarInizio.get(Calendar.DAY_OF_WEEK) == 1 || calendarInizio.get(Calendar.DAY_OF_WEEK) == 7){            System.out.println('due');    Calendar calendarFine = Calendar.getInstance();                calendarFine.setTime(endDate);     System.out.println(endDate);     System.out.println(calendarFine.get(Calendar.DAY_OF_WEEK));      if(calendarFine.get(Calendar.DAY_OF_WEEK) == 1 || calendarFine.get(Calendar.DAY_OF_WEEK) == 7){                System.out.println('tre');      int numberOfDaysBeetwenDates = (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));                    if(numberOfDaysBeetwenDates < 3){                System.out.println('4');        returnValue = true;                    }                }            }        }");
                shell.evaluate(optional.getWebApplicability());
                isRateOptionalApplicability = (Boolean)binding.getProperty("returnValue");
            } else{

            /*
             * Optional applicability is null, applicability is true
             */
                isRateOptionalApplicability = true;
            }
        } else{
    
            /*
             * Optional applicability is null, applicability is true
             */
            isRateOptionalApplicability = true;
        }

        return isRateOptionalApplicability;
       
    }


    protected void aggiungiRighePrepagatoExtra(
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
        //MROldCodiciIva codiceIva = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());

        MROldCodiciIva codiceIvaExtraPrepay = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaExtraPrepay().getId());
        MROldCodiciIva tempCodiceIva = findVATCodeForWorkshop(sx);
        if(tempCodiceIva!=null){
            codiceIvaExtraPrepay = tempCodiceIva;
        }

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
                    true);
            importoExtra = calcolaImportoGiornaliero(
                    sx,
                    tariffa.getOptionalsTariffa(),
                    tariffa.getIvaInclusaExtraPrepay(),
                    codiceIvaExtraPrepay.getAliquota(),
                    importoTariffa.getImportoExtra().doubleValue(),
                    1.0,
                    true);
        } else {
            importoGiornaliero = calcolaImportoGiornaliero(
                    sx,
                    tariffa.getOptionalsTariffa(),
                    tariffa.getIvaInclusaExtraPrepay(),
                    codiceIvaExtraPrepay.getAliquota(),
                    importoTariffa.getImportoBase().doubleValue(),
                    giorniBase,
                    true);
            importoExtra = calcolaImportoGiornaliero(
                    sx,
                    tariffa.getOptionalsTariffa(),
                    tariffa.getIvaInclusaExtraPrepay(),
                    codiceIvaExtraPrepay.getAliquota(),
                    importoTariffa.getImportoExtra().doubleValue(),
                    1.0,
                    true);
        }

        MROldRigaDocumentoFiscale primaRiga = creaRigaNoleggio(
                descrizioneTariffa,
                giorniBase,
                importoGiornaliero,
                scontoPercentuale,
                codiceIvaExtraPrepay,
                contoRicavo);

        if (primaRiga != null) {
            righeFattura.add(primaRiga);
        }

        MROldRigaDocumentoFiscale secondaRiga = creaRigaGiorniExtra(
                descrizioneTariffa,
                giorniExtra,
                importoExtra,
                scontoPercentuale,
                codiceIvaExtraPrepay,
                contoRicavo);
        if (secondaRiga != null) {
            righeFattura.add(secondaRiga);
        }
    }

    protected List aggiungiRigaCarburante(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Integer combustibileMancante,
            MROldCarburante carburante,
            Boolean isPrepaid) {

        try {
            if(tariffa.getId() != null){
                tariffa = (MROldTariffa)sx.get(MROldTariffa.class, tariffa.getId());
            }
            sx.save(tariffa);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //vertifica se c'e' un optional di tipo pieno prepagatoù
        boolean pienoPrepagato = false;
        Iterator<MROldOptionalTariffa> it = tariffa.getOptionalsTariffa().values().iterator();
        while (it.hasNext()) {
            MROldOptionalTariffa o = it.next();
            if ((o.getSelezionato() || o.getSelezionatoRientro()) && Boolean.TRUE.equals(o.getOptional().getPienoPrepagato())) {
                pienoPrepagato = true;
                break;
            }
        }
        if (!pienoPrepagato) {
            if (combustibileMancante != null
                    && combustibileMancante.intValue() > 0
                    && carburante != null
                    && carburante.getImportoUnitario() != null
                    && carburante.getImportoUnitario().doubleValue() > 0) {


                MROldPianoDeiConti sottocontoCarburante = null;
                MROldCodiciIva codiceIva = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());


                if (Preferenze.getFuelInvoicedWithVatCodeZero(sx)) {
                    codiceIva= (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());
                }
                MROldCodiciIva tempCodiceIva = findVATCodeForWorkshop(sx);
                if(tempCodiceIva!=null){
                    codiceIva = tempCodiceIva;
                }
                if (carburante != null) {
                    carburante = (MROldCarburante) sx.get(MROldCarburante.class, carburante.getId());
                    if (carburante.getContoRicavo() != null) {
                        sottocontoCarburante = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, carburante.getContoRicavo().getId());
                    }
                }
                if (sottocontoCarburante == null) {
                    sottocontoCarburante = leggiCodiceSottocontoCarburante(sx);
                }
                if (codiceIva != null) {
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
            }
        }
        return righeFattura;
    }

    protected List aggiungiRigheOptionals(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Double scontoPercentuale,
            double totaleGiorni,
            double giorniVoucher,
            double totaleKm,
            Map totaliCarburanti) throws TariffaNonValidaException {
       aggiungiRigheOptionalsTempoExtra(sx, righeFattura, tariffa, scontoPercentuale);
        Iterator carburanti = totaliCarburanti.keySet().iterator();
        while (carburanti.hasNext()) {
            MROldCarburante carburante = (MROldCarburante) carburanti.next();
            carburante = (MROldCarburante) sx.get(MROldCarburante.class, carburante.getId());
            Integer litri = (Integer) totaliCarburanti.get(carburante);
            aggiungiRigaCarburante(sx, righeFattura, tariffa, litri, carburante, false);
        }


        // OPTIONALS USCITA

        boolean extraPrePay = giorniVoucher > 0 ? true : false;

        aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, totaleKm, false, extraPrePay);

        // Per applicare gli oneri agli optionals aggiunti al rientro
        // OPTIONALS RIENTRO - CON ONERI
        aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, totaleKm, true, extraPrePay);

        // ONERI
        aggiungiRigheOptionalsPercentuali(sx, righeFattura, tariffa, extraPrePay);

        // Per non applicare gli oneri agli optionals aggiunti al rientro
        // OPTIONALS RIENTRO - SENZA ONERI
        //aggiungiRigheOptionalsNonPercentuali(sx, righeFattura, tariffa, scontoPercentuale, totaleGiorni, giorniVoucher, totaleKm, true);

        // FRANCHIGIE
        aggiungiRigheOptionalsFranchigie(sx, righeFattura, tariffa, extraPrePay);
        return righeFattura;
    }


    public boolean checkIfFuelIncluded(){

        //maybe this code can result in N+1_SELECT_PROBLEM
        //if there is some issue with performance modify the lazy option in false
        boolean b = false;
        try{
            MROldDepositAndWaiverResSource focusDepWaiver = getContrattoNoleggio()
                    .getCommissione()
                    .getFonteCommissione()
                    .getDepositResSourceId();
            if(focusDepWaiver.getFuelServicePercChargedToUser() == 0){
                b = true;
            }
        }catch(Exception ex){
            ex.getLocalizedMessage();
        }
        return b;
    }


    protected List aggiungiRigheOptionalsPrepagato(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            double giorniVoucher) {
        // OPTIONALS INCLUSI
        aggiungiRigheOptionalsNonPercentualiPrepagato(sx, righeFattura, tariffa, giorniVoucher, Boolean.TRUE);

        // Per applicare gli oneri agli optionals non inclusi nella tariffa
        // OPTIONALS NON INCLUSI - CON ONERI
        aggiungiRigheOptionalsNonPercentualiPrepagato(sx, righeFattura, tariffa, giorniVoucher, Boolean.FALSE);

        // ONERI
        aggiungiRigheOptionalsPercentuali(sx, righeFattura, tariffa, false);

        // Per non applicare gli oneri agli optionals non inclusi nella tariffa
        // OPTIONALS NON INCLUSI - SENZA ONERI
        //aggiungiRigheOptionalsNonPercentualiPrepagato(sx, righeFattura, tariffa, giorniVoucher, Boolean.FALSE);

        return righeFattura;
    }

    private List aggiungiRigheOptionalsNonPercentualiPrepagato(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            double giorniVoucher,
            Boolean incluso) {

        if (tariffa.getOptionalsTariffa() != null) {

            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
            MROldCodiciIva tempCodiceIva = findVATCodeForWorkshop(sx);

                if(tempCodiceIva!=null){
                    codiceIvaImponibile = tempCodiceIva;
                }
            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());
            if(tempCodiceIva!=null){
                codiceIvaNonSoggetto = tempCodiceIva;
            }
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


                        /*
                         * Andrea, check first the min then the max
                         */
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

    protected List aggiungiRigheOptionalsFranchigie(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            boolean extraPrePay)
            throws HibernateException {

        if (tariffa.getOptionalsTariffa() != null) {

            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());

            if (tariffa.getCodiceIvaExtraPrepay()!= null && extraPrePay)
                codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaExtraPrepay().getId());

            MROldCodiciIva tempCodiceIva = findVATCodeForWorkshop(sx);
            if(tempCodiceIva!=null){
                codiceIvaImponibile = tempCodiceIva;
            }

            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());
            if(tempCodiceIva!=null){
                codiceIvaNonSoggetto = tempCodiceIva;
            }
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

    protected List aggiungiRigheOptionalsNonPercentuali(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Double scontoPercentuale,
            double totaleGiorni,
            double giorniVoucher,
            double totaleKm,
            boolean rientro,
            boolean extraPrePay) {

        if (tariffa.getOptionalsTariffa() != null) {


            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());

            if (tariffa.getCodiceIvaExtraPrepay()!= null && extraPrePay)
                codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaExtraPrepay().getId());

            MROldCodiciIva tempCodiceIva = findVATCodeForWorkshop(sx);
            if(tempCodiceIva!=null){
                codiceIvaImponibile = tempCodiceIva;
            }

            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());
            if(tempCodiceIva!=null){
                codiceIvaNonSoggetto = tempCodiceIva;
            }

            Iterator it = tariffa.getOptionalsTariffa().values().iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();
                MROldOptional optional = optionalTariffa.getOptional();

                //escludiamo i deposito cauzionali
                if (optional.getDepositoCauzionale() != null && Boolean.TRUE.equals(optional.getDepositoCauzionale())) {
                    ;
                } else {
                    if ( // Gli optionals in uscita solo all'uscita
                            ((!rientro && optionalTariffa.getSelezionato())
                            || // e' quelli in rientro solo al rientro.
                            (rientro && optionalTariffa.getSelezionatoRientro()))
                            && // Escludiamo gli optionals percentuali
                            !optional.getImportoPercentuale()
                            && // Escludiamo gli optionals di tempo extra.
                            !optional.getTempoExtra()
                            && // escludiamo i prepagati
                            (!optionalTariffa.getPrepagato() || giorniVoucher == 0)
                            ) {

                        // Gli optionals delle assicurazioni base sono scontabili quando sono inclusi nella tariffa.
                        boolean scontabile
                                = optionalTariffa.getIncluso().booleanValue()
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
                        /*
                         * Andrea, addebito minimo e massimo new, before was first the check max then the min
                         */
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
                        //here we have min or rent days if > then min, so check max
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

                        MROldSede aSede=null;

                        aSede = getLocationPickUp();

                        if (this.getPreventivo()!=null)
                            aSede=getPreventivo().getSedeUscita();

                        if (this.getPrenotazione()!=null)
                            aSede=getPrenotazione().getSedeUscita();

                        if (this.getContrattoNoleggio()!=null)
                            aSede=getContrattoNoleggio().getSedeUscita();

                        if (!Boolean.TRUE.equals(optional.getRoadTax()) && optional.getSoggIvaImporto()) {
                            if (Preferenze.getScorporoOneriAeroportualiDallaTariffa(sx)) {
                                prezzo = optionalTariffa.getImporto();
                            } else {
                                if (aSede != null && (aSede.getAeroporto())) {
                                    Double percentualeOnereAeroportuale = TariffeUtils.getPercentualeOneriAeroportuali(tariffa);
                                    prezzo = optionalTariffa.getImporto() / (percentualeOnereAeroportuale != null ? percentualeOnereAeroportuale : 1.0);
                                } else if (aSede != null && (aSede.getFerrovia())) {
                                    Double percentualeOnereAeroportuale = TariffeUtils.getPercentualeOneriFerroviari(tariffa);
                                    prezzo = optionalTariffa.getImporto() / (percentualeOnereAeroportuale != null ? percentualeOnereAeroportuale : 1.0);
                                } else {
                                    prezzo = optionalTariffa.getImporto();
                                }

                            }
                        }  else {
                            /*
                             * Andrea
                             * Insurance
                             * TODO
                             */
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

    protected List aggiungiRigheOptionalsPercentuali(
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

            MROldCodiciIva tempCodiceIva = findVATCodeForWorkshop(sx);
            if(tempCodiceIva!=null){
                codiceIvaNonSoggetto = tempCodiceIva;
                codiceIvaImponibile = tempCodiceIva;
            }

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
                 * y5 è totaleOptionalFissiEGiornalieriConIvaApplicabile y4 è
                 * OptionalFissiEGiornalieriSenzaIvaApplicabile y3 è
                 * totaleQuotePercentualiConIvaApplicabile
                 * y2totaleQuotePercentualiSenzaIvaApplicabile y1 oneri
                 * aeroportuali y0 importo tariffa
                 */
                double totaleImponibileRigheConIvaApplicabile = 0.0;

                List<MROldOptional> optionalInclusi = new ArrayList<MROldOptional>();
                MROldOptionalTariffa aOptionalTariffa;


                while (itForOptionalInclusi.hasNext()) {
                    aOptionalTariffa = itForOptionalInclusi.next();
//                    if (aOptionalTariffa.getIncluso() && !aOptionalTariffa.getPrepagato()) {
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
                         * fine jarko
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
                 * Cerco Optional Percentuali tranne gli oneri
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
//                    MROldFonteCommissione fonte = null;
//                    if (getContrattoNoleggio() != null && getContrattoNoleggio().getCommissione() != null && getContrattoNoleggio().getCommissione().getFonteCommissione() != null) {
//                        fonte = (MROldFonteCommissione) sx.get(MROldFonteCommissione.class, getContrattoNoleggio().getCommissione().getFonteCommissione().getId());
//                        if (optional.getDescrizione().contains("LAVAGGIO") || optional.getDescrizione().contains("WASH")) {
//                            if (fonte != null && fonte.getDepositResSourceId() != null && fonte.getDepositResSourceId().getCarWashPercChargedToUser() != null) {
//                                Double d = fonte.getDepositResSourceId().getCarWashPercChargedToUser();
//                                if (d == 0) {
//                                    optionalTariffa.setPrepagato(true);
//                                }
//                            }
//                        }
//                    }
//
//                    if (optional.getDescrizione().contains("RIPRISTINO") || optional.getDescrizione().contains("FUELSERVICE")) {
//                        if (fonte != null && fonte.getDepositResSourceId() != null && fonte.getDepositResSourceId().getFuelServicePercChargedToUser() != null) {
//                            Double d = fonte.getDepositResSourceId().getFuelServicePercChargedToUser();
//                            if (d == 0) {
//                                optionalTariffa.setPrepagato(true);
//                            }
//                        }
//                    }

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
                         * Bisogna assegnare il prezzo uguale a importoTariffa
                         * se l'optional è incluso
                         *
                         */
                        double prezzo = totaleImponibileRighe;

                        if (optionalTariffa.getIncluso() || optionalTariffa.getSelezionato()) {
                            // Modifica da mettere in preferenza a seconda dei due casi?
                            // 2014-08-04 - Mauro E Giacomo
                            if (Preferenze.getCalcoloImportoOptionalPercentualiSoloTKM(sx)) {
                                prezzo = totaleImponibileRighe;
                            } else {
                                prezzo = importoTariffaAnteOptionalPercentuali - y5;
                            }
                        }

//                        System.out.println("\n\n *** Importo di applicazione Optional Incluso: " + prezzo);

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

                /**
                 * Mi calcolo il totale dell'imponibile con IVA Applicabile per
                 * gli oneri
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

    protected List aggiungiRigheOptionalsTempoExtra(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Double scontoPercentuale) {

        if (tariffa.getOptionalsTariffa() != null) {

            MROldCodiciIva codiceIvaImponibile = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIva().getId());
            MROldCodiciIva codiceIvaNonSoggetto = (MROldCodiciIva) sx.get(MROldCodiciIva.class, tariffa.getCodiceIvaNonSoggetto().getId());

            MROldCodiciIva tempCodiceIva = findVATCodeForWorkshop(sx);
            if(tempCodiceIva!=null){
                codiceIvaImponibile = tempCodiceIva;
                codiceIvaNonSoggetto = tempCodiceIva;
            }

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

    protected MROldRigaDocumentoFiscale creaRigaGiorniExtra(
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

     protected MROldRigaDocumentoFiscale creaRigaNoleggio(
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

    /**
     * Crea la riga della fattura per un optional.
     *
     * @param optional L'optional da usare
     * @param quantita La quantita' di questo optional
     * @param prezzo Il prezzo del singolo optional.
     * @param scontoPercentuale Lo sconto da applicare. Richiesto se scontabile.
     * @param scontabile Indica se questo optional e' scontabile o no. Se
     * scontabile, lo sconto non puo' essere null
     * @param franchigia Indica se l'importo da addebitare e' quello della
     * franchigia.
     * @param codiceIvaImponibile Il codice IVA da usare se l'optional e'
     * sooggetto ad IVA
     * @param codiceIvaNonSoggetto Il codice IVA da usare se l'optional non e'
     * soggetto ad IVA
     * @param contoRicavo Il conto contabile di ricavo.
     * @return
     */
    protected MROldRigaDocumentoFiscale creaRigaOptional(
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

    /**
     * Crea la riga della fattura per un optional di tempo extra.
     *
     * @param optional L'optional da usare
     * @param quantita La quantita' di questo optional
     * @param prezzo Il prezzo del singolo optional.
     * @param scontoPercentuale Lo sconto da applicare. Richiesto se scontabile.
     * @param scontabile Indica se questo optional e' scontabile o no. Se
     * scontabile, lo sconto non puo' essere null
     * @param codiceIvaImponibile Il codice IVA da usare se l'optional e'
     * sooggetto ad IVA
     * @param codiceIvaNonSoggetto Il codice IVA da usare se l'optional non e'
     * soggetto ad IVA
     * @param contoRicavo Il conto contabile di ricavo.
     * @return
     */
    protected MROldRigaDocumentoFiscale creaRigaOptionalTempoExtra(
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

    /**
     * Trova l'importo della tariffa con la massima durata minima che non supera
     * la durata del noleggio. Se la durata del noleggio e' minore di tutte le
     * durate minime degli importi della tariffa, oppure se la tariffa non
     * contiene alcun importo, il valore di ritorno e' null.
     *
     * @param stagioneTariffa La tariffa contenente gli importi e le durate.
     * @param durataTotaleNoleggio La durata del noleggio, da non superare con
     * la durata minima della tariffa.
     * @return l'importo trovato, o null se non ci sono importi validi per la
     * durata specificata.
     */
    public static MROldImportoTariffa trovaImportoStagione(MROldStagioneTariffa stagioneTariffa, double durataTotaleNoleggio) {
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
     * che non supera la durata extra del prepagato. Se la durata extra del
     * prepagato e' minore di tutte le durate minime degli importi extra prepay
     * della tariffa, oppure se la tariffa non contiene alcun importo extra
     * prepay, il valore di ritorno e' null.
     *
     * @param tariffa La tariffa contenente gli importi extra prepay e le
     * durate.
     * @param durataExtraPrepay La durata del superamento del prepagato da non
     * superare con la durata minima della tariffa.
     * @return l'importo extra prepay trovato, o null se non ci sono importi
     * extra prepay validi per la durata specificata.
     */
    public static MROldImportoExtraPrepay trovaImportoExtraPrepay(MROldTariffa tariffa, double durataExtraPrepay) {
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

    public static MROldImportoExtraPrepay trovaMinimoExtraPrepay(MROldTariffa tariffa) {
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

    public static MROldImportoTariffa trovaMinimoStagione(MROldStagioneTariffa stagioneTariffa) {
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

    /**
     * @return the contrattoNoleggio
     */
    protected MROldContrattoNoleggio getContrattoNoleggio() {
        return contrattoNoleggio;
    }

    /**
     * @param contrattoNoleggio the contrattoNoleggio to set
     */
    protected void setContrattoNoleggio(MROldContrattoNoleggio contrattoNoleggio) {
        this.contrattoNoleggio = contrattoNoleggio;
    }

    /**
     * @return the tariffa
     */
    protected MROldTariffa getTariffa() {
        return tariffa;
    }

    /**
     * @param tariffa the tariffa to set
     */
    protected void setTariffa(MROldTariffa tariffa) {
        this.tariffa = tariffa;
    }

    /**
     * @return the inizio
     */
    public Date getInizio() {
        return inizio;
    }

    /**
     * @param inizio the inizio to set
     */
    protected void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    /**
     * @return the fine
     */
    public Date getFine() {
        return fine;
    }

    /**
     * @param fine the fine to set
     */
    protected void setFine(Date fine) {
        this.fine = fine;
    }

    /**
     * @return the km
     */
    protected Integer getKm() {
        return km;
    }

    /**
     * @param km the km to set
     */
    protected void setKm(Integer km) {
        this.km = km;
    }

    /**
     * @return the litri
     */
    protected Integer getLitri() {
        return litri;
    }

    /**
     * @param litri the litri to set
     */
    protected void setLitri(Integer litri) {
        this.litri = litri;
    }

    /**
     * @return the carburante
     */
    protected MROldCarburante getCarburante() {
        return carburante;
    }

    /**
     * @param carburante the carburante to set
     */
    protected void setCarburante(MROldCarburante carburante) {
        this.carburante = carburante;
    }

    /**
     * @return the scontoPercentuale
     */
    protected Double getScontoPercentuale() {
        return scontoPercentuale;
    }

    /**
     * @param scontoPercentuale the scontoPercentuale to set
     */
    protected void setScontoPercentuale(Double scontoPercentuale) {
        this.scontoPercentuale = scontoPercentuale;
    }

    /**
     * @return the giorniVoucher
     */
    protected Integer getGiorniVoucher() {
        return giorniVoucher;
    }

    /**
     * @param giorniVoucher the giorniVoucher to set
     */
    protected void setGiorniVoucher(Integer giorniVoucher) {
        this.giorniVoucher = giorniVoucher;
    }

    public void setPrenotazione(MROldPrenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public MROldPrenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPreventivo(MROldPreventivo preventivo) {
        this.preventivo = preventivo;
    }

    public MROldPreventivo getPreventivo() {
        return preventivo;
    }

    public Date getInizioPeriodo() {
        return null;
    }


    public Date getFinePeriodo() {
        return null;
    }

    protected void aggiungiRigheNonPrepagato(
            Session sx,
            List righeFattura,
            MROldTariffa tariffa,
            Date inizio,
            Date fine,
            double giorniNoleggio,
            double scontoPercentuale,
            MROldPianoDeiConti contoRicavo) throws TariffaNonValidaException {

        Date tmpOraInizio = FormattedDate.extractTime(inizio);
        boolean consideraPrimoGiorno = false;

        if (tariffa != null && Boolean.TRUE.equals(tariffa.getOraRientroAttiva()) && tariffa.getOraRientro() != null) {
            Integer ritardoMassimoConsentito = Preferenze.getContrattoRitardoMassimoMinuti(sx);

            Calendar cal = Calendar.getInstance();
            cal.setTime(tmpOraInizio);
            if (ritardoMassimoConsentito != null) {
                cal.add(Calendar.MINUTE, ritardoMassimoConsentito);
            }

            if (tariffa.getOraRientro().getTime() > cal.getTimeInMillis()) {

                    fine = FormattedDate.add(inizio, Calendar.DAY_OF_MONTH, (int) giorniNoleggio - 1);

                consideraPrimoGiorno = true;
            } else {
                fine = FormattedDate.add(inizio, Calendar.DAY_OF_MONTH, (int) giorniNoleggio);
            }
        } else {
            fine = FormattedDate.add(inizio, Calendar.DAY_OF_MONTH, (int) giorniNoleggio);
        }

        Date dataInizio = FormattedDate.extractDate(inizio);
        Date oraInizio = FormattedDate.extractTime(inizio);
        Date dataFine = FormattedDate.extractDate(fine);
        Date oraFine = FormattedDate.extractTime(fine);

         if (tariffa != null && Boolean.TRUE.equals(tariffa.getOraRientroAttiva()) && tariffa.getOraRientro() != null) {
             oraFine = tariffa.getOraRientro();
         }

        if (isVerificaMinimoTariffa(sx)) {
            MROldImportoTariffa minimoTariffa = trovaMinimoTariffa(tariffa);
            if (minimoTariffa == null) {
                throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgTariffaNonContieneImportiGruppoPeriodo"));
            }
            //else if (minimoTariffa.getMinimo() > giorniNoleggio) {
            //    throw new TariffaNonValidaException(MessageFormat.format(bundle.getString("FatturaUtils.msgNoleggioInferiorePeriodoMinimoRichiesto0"), minimoTariffa.getMinimo()));
            //}
        }


        for (MROldStagioneTariffa stagione : tariffa.getStagioni()) {

            Date inizioStagione = null, fineStagione = null;
//            if (stagione.inStagione(dataInizio) && !dataInizio.equals(dataFine)) {
            if (stagione.inStagione(dataInizio)) {
                inizioStagione = FormattedDate.createTimestamp(dataInizio, oraInizio);
                if (!stagione.getMultistagione() || stagione.inStagione(dataFine)) {
                    fineStagione = FormattedDate.createTimestamp(dataFine, oraFine);
                } else {
                    if (consideraPrimoGiorno) {
                        fineStagione = FormattedDate.createTimestamp(stagione.getFine(), oraFine);
                    } else {
                        //Per poter misurare correttamente la durata del periodo aggiungiamo un giorno a fine stagione.
                        fineStagione = FormattedDate.createTimestamp(FormattedDate.add(stagione.getFine(), Calendar.DAY_OF_MONTH, 1), oraFine);
                    }
                    dataInizio = FormattedDate.add(stagione.getFine(), Calendar.DAY_OF_MONTH, 1);
                }
            } else {
                continue;
            }

            if (inizioStagione != null && fineStagione != null) {

                Date tmpData = FormattedDate.extractDate(fineStagione);
                Date tmpOra = FormattedDate.extractTime(fineStagione);

                if (tariffa != null && Boolean.TRUE.equals(tariffa.getOraRientroAttiva()) && tariffa.getOraRientro() != null) {
                    tmpOra = tariffa.getOraRientro();
                }

                fineStagione = FormattedDate.createTimestamp(tmpData, tmpOra);

                Double giorniStagione = null;
                if (consideraPrimoGiorno) {
                    giorniStagione = Math.ceil( FormattedDate.numeroGiorni(inizioStagione, fineStagione, false));
                } else {
                    giorniStagione = Math.floor( Math.round(FormattedDate.numeroGiorni(inizioStagione, fineStagione, false)));
                }


                MROldImportoTariffa importStagione = trovaImportoStagione(stagione, giorniStagione);
                if (importStagione == null) {
                    importStagione = trovaMinimoStagione(stagione);
                }
                if (importStagione == null) {
                    throw new TariffaNonValidaException(bundle.getString("FatturaUtils.msgTariffaNonContieneImportiGruppoPeriodo"));
                }
                String descrizioneTariffa = stagione.getDescrizione();

                
                MROldCodiciIva codiceIva = (MROldCodiciIva) sx.get(MROldCodiciIva.class, stagione.getCodiceIva().getId());
                MROldCodiciIva tempCodiceIva = findVATCodeForWorkshop(sx);
                if(tempCodiceIva!=null){
                    codiceIva = tempCodiceIva;
                }

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
                if(giorniBase < importStagione.getMinimo()){
                    giorniExtra = giorniBase;
                    giorniBase = 0;
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

    /**
     * @return the locationPickUp
     */
    public MROldSede getLocationPickUp() {
        return locationPickUp;
    }

    /**
     * @param locationPickUp the locationPickUp to set
     */
    public void setLocationPickUp(MROldSede locationPickUp) {
        this.locationPickUp = locationPickUp;
    }
    public Fatturazione setLocationPickUpAndReturnFatturazione(MROldSede locationPickUp){
        this.locationPickUp = locationPickUp;
        return this;
    }

    protected MROldCodiciIva findVATCodeForSelectedTariffaAndCustomer(Session sx , MROldCodiciIva codiceDaConfrontare) {

        MROldCodiciIva foundIvaSplitted = null;
        MROldBusinessPartner businessPartner = null;

        if(getContrattoNoleggio() != null ){

            if(getContrattoNoleggio().getCliente() != null ){
                businessPartner = (MROldBusinessPartner) getContrattoNoleggio().getCliente();
            } else if(getContrattoNoleggio().getConducente1()!= null){
                businessPartner = (MROldBusinessPartner) getContrattoNoleggio().getConducente1();
            }

        } else if(getPrenotazione() != null){
            if(getPrenotazione().getCliente() != null){
                businessPartner = (MROldBusinessPartner) getPrenotazione().getCliente();
            }
            else if(getPrenotazione().getConducente1()!= null){

                businessPartner = (MROldBusinessPartner) getPrenotazione().getConducente1();
            }

        } else if(getPreventivo()!= null){
            if(getPreventivo().getCliente() != null){
                businessPartner = (MROldBusinessPartner) getPreventivo().getCliente();
            }
            else if(getPreventivo().getConducente1()!= null){

                businessPartner = (MROldBusinessPartner) getPreventivo().getConducente1();
            }

        }

        if (businessPartner != null) {
            if (businessPartner.getSplitPayments() != null && businessPartner.getSplitPayments().size() > 0) {
                for (MROldSplitPayment focusSplitPayment : businessPartner.getSplitPayments()) {
                    if (focusSplitPayment.getStandardVATcode().getId().equals(codiceDaConfrontare.getId())) {
                        foundIvaSplitted = focusSplitPayment.getSplitVATcode();
                    }
                }
            }
        }

        if(foundIvaSplitted != null){
            codiceDaConfrontare = foundIvaSplitted;
        }
        return codiceDaConfrontare;
    }

    protected MROldCodiciIva findVATCodeForWorkshop(Session sx ) {

        MROldCodiciIva codiceIva = null;
        MROldBusinessPartner businessPartner = null;

        if(getContrattoNoleggio() != null ){

            if(getContrattoNoleggio().getCliente() != null ){
                businessPartner = (MROldBusinessPartner) getContrattoNoleggio().getCliente();
            } else if(getContrattoNoleggio().getConducente1()!= null){
                businessPartner = (MROldBusinessPartner) getContrattoNoleggio().getConducente1();
            }

        } else if(getPrenotazione() != null){
            if(getPrenotazione().getCliente() != null){
                businessPartner = (MROldBusinessPartner) getPrenotazione().getCliente();
            }
            else if(getPrenotazione().getConducente1()!= null){

                businessPartner = (MROldBusinessPartner) getPrenotazione().getConducente1();
            }

        } else if(getPreventivo()!= null){
            if(getPreventivo().getCliente() != null){
                businessPartner = (MROldBusinessPartner) getPreventivo().getCliente();
            }
            else if(getPreventivo().getConducente1()!= null){

                businessPartner = (MROldBusinessPartner) getPreventivo().getConducente1();
            }

        }

        if (businessPartner != null && businessPartner.getTypeOfCustomer()!=null && (businessPartner.getTypeOfCustomer().equals("Garage") || businessPartner.getTypeOfCustomer().equals("Workshop")
                || businessPartner.getTypeOfCustomer().equals("Officina") || businessPartner.getTypeOfCustomer().equals("Werkstatt"))) {
            Query query = sx.createQuery(
                    "select x from MROldCodiciIva x where "
                            + //NOI18N
                            "x.codice = :codice ");
            query.setParameter("codice", "0"); //NOI18N
            codiceIva = (MROldCodiciIva) query.list().get(0);
        }
        return codiceIva;
    }
    @Override
    public List<MROldRigaDocumentoFiscale> calcolaRigheProssimaFatturaR2R(Session sx, Rent2Rent r2r, Date startDate, Date endDate)  {
        List righe = new ArrayList();


        /*
         if (getPreventivo() != null && getPrenotazione().getId() != null) {
         setPrenotazione((Prenotazione) sx.get(Prenotazione.class, getPrenotazione().getId()));
         }*/
        MROldPianoDeiConti pianoDeiConti = leggiCodiceSottoconto(sx);




        double giorniEsattiSenzaRitardo = FormattedDate.numeroGiorni(startDate, endDate, true);
        double giorniInteriPerEccesso = Math.ceil(giorniEsattiSenzaRitardo)+1;

        if (giorniInteriPerEccesso==0){
            System.out.println("*********R2R 0 giorni: "+ r2r.getVehicle().getTarga() + " ");

            //throw new FatturaVuotaException("Contratto R2R senza importo - " +r2r.getVehicle().getTarga());
        }
        giorniInteriPerEccesso=1;
        Double costoGiornaliero = 0d;
        Double costoMese = null;
        if (r2r.getVehicle().getTarga().equals("FN775GG")){
            System.out.println("debug");
        }
        if (r2r.getMonthlyFee() != null) {
            //costoGiornaliero = r2r.getMonthlyFee() * 12 / 365; <- it was for EuropAssistance
            costoMese= r2r.getMonthlyFee();
            costoGiornaliero= r2r.getMonthlyFee();
        }
        // start date è il primo gg del mede?
        // end date è l'ultimo?
        //costo gg = mese
        //quantita  = 1

        Calendar calStartDate = new GregorianCalendar();
        calStartDate.setTime(startDate);
        calStartDate.set(Calendar.DATE, calStartDate.getMinimum(Calendar.DAY_OF_MONTH));

        Calendar calLastDayOfTheMonth = new GregorianCalendar();
        calLastDayOfTheMonth.setTime(endDate);
        calLastDayOfTheMonth.set(Calendar.DATE, calLastDayOfTheMonth.getActualMaximum(Calendar.DAY_OF_MONTH));



        if (startDate.equals(calStartDate.getTime()) && endDate.equals( calLastDayOfTheMonth.getTime())){
            costoGiornaliero = r2r.getMonthlyFee();

        }
        if (r2r.getLastBilledDate()==null && r2r.getAmountFirstInstallment()!=null && r2r.getAmountFirstInstallment()>0d ){
            costoGiornaliero = r2r.getAmountFirstInstallment();
        }

        if ((r2r.getLastBillingDate()!=null && r2r.getLastBillingDate().equals(endDate)) ||
                (r2r.getLastBillingDate()==null && r2r.getRaEndDate().equals(endDate))
                        && r2r.getAmountLastInstallment()!=null && r2r.getAmountLastInstallment()>0d){
            costoGiornaliero = r2r.getAmountLastInstallment();
        }

        if (costoGiornaliero != null) {
            costoGiornaliero = MathUtils.round(costoGiornaliero, 5);
        }

       // sx = HibernateBridge.refreshSessionSX(sx);

        SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MM-yyyy");

        String descrizioneTariffa=null;
        if (r2r.getVehicle()!=null && r2r.getVehicle().getTarga() != null) {
            descrizioneTariffa = "Noleggio Rent 2 Rent - " + r2r.getVehicle().getTarga() + " dal " + dmyFormat.format(startDate) + " al " + dmyFormat.format(endDate);
        }

        MROldPianoDeiConti contoRicavo = leggiCodiceSottoconto(sx);

        MROldCodiciIva codiceIva = ContabUtils.leggiCodiceIva(sx, 1);

        if (scontoPercentuale==null)
            scontoPercentuale=0.0;

        MROldFornitori agent = r2r.getAgent();

        MROldRigaDocumentoFiscale primaRiga = creaRigaNoleggio(
                descrizioneTariffa,
                giorniInteriPerEccesso,
                costoGiornaliero,
                scontoPercentuale,
                codiceIva,
                contoRicavo,
                agent,
                r2r);

        if (primaRiga != null) {
            righe.add(primaRiga);
        }

        return righe;
    }
    /**
     *
     * @param descrizioneTariffa
     * @param giorniBase
     * @param importoGiornaliero
     * @param scontoPercentuale
     * @param codiceIva
     * @param contoRicavo
     * @return
     */
    protected MROldRigaDocumentoFiscale creaRigaNoleggio(
            String descrizioneTariffa,
            double giorniBase,
            double importoGiornaliero,
            double scontoPercentuale,
            MROldCodiciIva codiceIva,
            MROldPianoDeiConti contoRicavo,
            MROldFornitori agent,
            Rent2Rent r2r) {

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
            if (agent!=null){
                primaRiga.setAgent(agent);
            }
            if (r2r!=null){
                primaRiga.setR2r(r2r);
            }
        }
        return primaRiga;
    }
}
