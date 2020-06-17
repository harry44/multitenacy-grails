/*
 * FatturaUtils.java
 *
 * Created on 11 octombrie 2004, 12:46
 */
package it.aessepi.myrentcs.utils;

import grails.util.Holders;
import it.myrent.ee.api.exception.FatturaVuotaException;
import it.myrent.ee.api.exception.TariffaNonValidaException;
import it.myrent.ee.api.exception.TitledException;
import it.myrent.ee.api.factory.ImpostaFactory;
import it.myrent.ee.api.utils.CastellettoIva;
import it.myrent.ee.api.utils.ContabUtils;
import it.myrent.ee.db.*;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.FileUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.aessepi.utils.beans.JasperMyTools;
import java.awt.Component;
import java.io.File;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import javax.swing.JOptionPane;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author  jamess
 */
public class FatturaUtils {

    /** Creates a new instance of FatturaUtils */
    public FatturaUtils() {
    }
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");
    private static Log log = LogFactory.getLog(FatturaUtils.class);

    /*private static MROldPianoDeiConti leggiCodiceSottoconto() {
        return ContabUtils.leggiSottoconto(new Integer(4), new Integer(10), new Integer(6));
    }*/

    private static MROldPianoDeiConti leggiCodiceSottoconto(Session sx) throws HibernateException {
        return ContabUtils.leggiSottoconto(sx, new Integer(4), new Integer(10), new Integer(6));
    }

    /*private static MROldPianoDeiConti leggiCodiceSottocontoCarburante() {
        return ContabUtils.leggiSottoconto(new Integer(4), new Integer(10), new Integer(3));
    }*/

    private static MROldPianoDeiConti leggiCodiceSottocontoCarburante(Session sx) throws HibernateException {
        return ContabUtils.leggiSottoconto(sx, new Integer(4), new Integer(10), new Integer(3));
    }

    /*private static MROldCodiciIva leggiCodiceIva() {
        return ContabUtils.leggiCodiceIva(new Integer(1));
    }*/

    private static MROldCodiciIva leggiCodiceIva(Session mySession) throws HibernateException {
        return ContabUtils.leggiCodiceIva(mySession, new Integer(1));
    }

    private static void showMessageDialog(final Component owner, final String message, final String title, final int messageType) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.currentThread().sleep(100);
                } catch (Exception ex) {
                }
            }
        }).start();
    }

    /**
     * Crea una lista di righe da passare al motore di stampa
     */
    /*public static List creaStampeDocumentiFiscali(Session sx, List documentiFiscali, boolean cartaBianca) throws HibernateException {
        List stampe = null;
        if (documentiFiscali != null && documentiFiscali.size() > 0) {
            stampe = new ArrayList();
            Iterator it = documentiFiscali.listIterator();
            if (cartaBianca) {
                while (it.hasNext()) {
                    MROldDocumentoFiscale myFattura = (MROldDocumentoFiscale) it.next();
                    myFattura = (MROldDocumentoFiscale) sx.get(MROldDocumentoFiscale.class, myFattura.getId());
                    if (myFattura != null && myFattura.getFatturaRighe() != null) {
                        int righe = myFattura.getFatturaRighe().size();
                        for (int i = 0; i < righe; i++) {
                            MROldRigaDocumentoFiscale tmpRiga = (MROldRigaDocumentoFiscale) myFattura.getFatturaRighe().get(i);
                            tmpRiga.setFattura(myFattura);
                            tmpRiga.setNumeroRigaFattura(new Integer(i));
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
    }*/

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
            castelletto.setImponibile(castelletto.getImponibile().doubleValue());
            castelletto.setImposta(MathUtils.roundDouble(castelletto.getImponibile().doubleValue() * castelletto.getCodiceIva().getAliquota().doubleValue()));
        }

        return map.values().toArray();
    }

    public static boolean sottrazioneAcconti(Map<MROldCodiciIva, CastellettoIva> imposte, Double totaleAcconti) {
        CastellettoIva impostaTrovata = null;
        Set<MROldCodiciIva> codiciIva = imposte.keySet();
        for (MROldCodiciIva codiceIva : codiciIva) {
            CastellettoIva imposta = imposte.get(codiceIva);
            if (imposta.getImponibile() >= totaleAcconti) {
                if (impostaTrovata == null || codiceIva.getAliquota().compareTo(impostaTrovata.getCodiceIva().getAliquota()) > 0) {
                    impostaTrovata = imposta;
                }
            }
        }
        if (impostaTrovata != null && sottrazioneAcconti(impostaTrovata, totaleAcconti)) {
            return true;
        }

        return false;
    }

    public static boolean sottrazioneAcconti(CastellettoIva[] imposte, Double totaleAcconti) {
        CastellettoIva impostaTrovata = null;
        for (int i = 0; i < imposte.length; i++) {
            if (imposte[i].getImponibile() >= totaleAcconti) {
                if (impostaTrovata == null || imposte[i].getCodiceIva().getAliquota().compareTo(impostaTrovata.getCodiceIva().getAliquota()) > 0) {
                    impostaTrovata = imposte[i];
                }
            }
        }
        if (impostaTrovata != null && sottrazioneAcconti(impostaTrovata, totaleAcconti)) {
            return true;
        }

        return false;
    }

    public static boolean sottrazioneAcconti(CastellettoIva imposta, Double totaleAcconti) {
        if (totaleAcconti != null) {
            imposta.setImponibile(MathUtils.roundDouble(imposta.getImponibile() - totaleAcconti));
            if (imposta.getCodiceIva() != null) {
                imposta.setImposta(MathUtils.roundDouble(imposta.getImponibile() * imposta.getCodiceIva().getAliquota()));
            }
            return true;
        }
        return false;
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
        
        /***
         * Method modified by Mauro 2015-08-27 
         * for NoleggiareBIDevelopment
         */
        
        
        double tmpTotaleIva = 0.0, tmpTotaleImponibile = 0.0;

        double baseRate=0;
        double includedOptional=0;
        double ancillary=0;
        double deductible=0;

        Object[] castelletto = creaCastellettoIva(righeFattura);
        if (castelletto.length==0){
            System.out.println("***");
            System.out.println("Castelletto IVA | 0,00");
            System.out.println("***");
        }
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
                if (!ContabUtils.isRigaDescrittiva(tmpRiga)) {
                    imponibileScontato = imponibileScontato + tmpRiga.getTotaleImponibileRiga().doubleValue();
                }

                if (tmpRiga.getTempoExtra()||tmpRiga.getTempoKm())
                    baseRate+=tmpRiga.getTotaleImponibileRiga().doubleValue();
                if (tmpRiga.getFranchigia())
                    deductible+=tmpRiga.getTotaleImponibileRiga().doubleValue();


                ///In this check we add all optionals, also the ones that are included in the rate
                ///In the future we can check by OptionalClass and calculate them separately
                if (tmpRiga.getOptional()!=null &&
                        tmpRiga.getTempoExtra()!=true &&
                        tmpRiga.getTempoKm()!=true &&
                        tmpRiga.getFranchigia()!=true
                        )
                    ancillary+=tmpRiga.getTotaleImponibileRiga().doubleValue();

            }
        }
        Double importi[] = new Double[]{null, null, null, null,null, null, null, null};
        importi[0] = MathUtils.roundDouble(imponibileScontato, 2);
        importi[1] = MathUtils.roundDouble(tmpTotaleIva, 2);
        importi[2] = MathUtils.roundDouble(tmpTotaleDocumento);
        importi[3] = MathUtils.roundDouble(tmpTotaleImponibile);
        importi[4] = MathUtils.roundDouble(baseRate); //No VAT
        importi[5] = MathUtils.roundDouble(includedOptional); // Optional included in the rate
        importi[6] = MathUtils.roundDouble(ancillary); // Optional not included in the rate - Ancillary
        importi[7] = MathUtils.roundDouble(deductible); // Deductible
        
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
                            // Sconto proporzionale intero. Andiamo avanti cosi.
                            arrotondamento = scontoProporzionale;
                        }
                        //Minimizziamo al resto.
                        if (arrotondamento > resto) {
                            arrotondamento = resto;
                        }

                        if (isScontabile(tmpRiga, arrotondamento)) {
                            // Sconto proporzionale intero o riga scontabile dello sconto proporzionale 'arrotondato per eccesso.
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

        if (riga1.getOptional() == null || riga2.getOptional() == null) {
            return new EqualsBuilder().append(riga1.getTempoKm()==null?false:riga1.getTempoKm()
                            || riga1.getTempoExtra() ==null?false:riga1.getTempoExtra(),
                    riga2.getTempoKm() ==null?false:riga2.getTempoKm()
                            || riga2.getTempoExtra()==null ? false:riga2.getTempoExtra()
            ).
                    append(riga1.getDescrizione(), riga2.getDescrizione()).
                    append(riga1.getFranchigia(), riga2.getFranchigia()).
                    append(riga1.getCarburante(), riga2.getCarburante()).
                    isEquals();
        } else {
            return new EqualsBuilder().append(riga1.getTempoKm()==null?false:riga1.getTempoKm()
                            || riga1.getTempoExtra() ==null?false:riga1.getTempoExtra(),
                    riga2.getTempoKm() ==null?false:riga2.getTempoKm()
                            || riga2.getTempoExtra()==null ? false:riga2.getTempoExtra()
            ).
                    append(riga1.getOptional(), riga2.getOptional()).
                    append(riga1.getFranchigia(), riga2.getFranchigia()).
                    append(riga1.getCarburante(), riga2.getCarburante()).
                    isEquals();
        }
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
                } else if (rigaPrecedente.getFattura() !=null && rigaPrecedente.getFattura().getTipoDocumento().equals(MROldDocumentoFiscale.NC)) {
                    quantitaFatturata -= rigaPrecedente.getQuantita();
                } else {
                    quantitaFatturata += rigaPrecedente.getQuantita();
                }
                precedenti.remove();
            }
        }
        return quantitaFatturata;
    }

    public static Double alreadyInvoicedAmount(MROldRigaDocumentoFiscale rigaFattura, List<MROldRigaDocumentoFiscale> righeFatturePrecedenti) {
        Double importoFatturato = 0.0;
        Iterator<MROldRigaDocumentoFiscale> precedenti = righeFatturePrecedenti.iterator();
        while (precedenti.hasNext()) {
            MROldRigaDocumentoFiscale rigaPrecedente = precedenti.next();
            if (equalsTipoRiga(rigaFattura, rigaPrecedente)) {
                if (rigaPrecedente.getTotaleRiga() < 0 && rigaPrecedente.getPrezzoUnitario() == 0.0) {
                    importoFatturato -= rigaPrecedente.getPrezzoUnitario();
                } else if (rigaPrecedente.getFattura() !=null && rigaPrecedente.getFattura().getTipoDocumento().equals(MROldDocumentoFiscale.NC)) {
                    importoFatturato -= rigaPrecedente.getPrezzoUnitario();
                } else {
                    importoFatturato += rigaPrecedente.getPrezzoUnitario();
                }
                precedenti.remove();
            }
        }
        return importoFatturato;
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
/*
    public static void calcolaDifferenzaDaFatturare(List<RigaDocumentoFiscale> righeFattureEmesse, List<RigaDocumentoFiscale> righeDaFatturare, List<RigaDocumentoFiscale> righeDaStornare) {
        ArrayList righePrecedenti = new ArrayList(righeFattureEmesse);
        int i = 0;
        while (i < righeDaFatturare.size()) {
            RigaDocumentoFiscale aRigaTotale = (RigaDocumentoFiscale) righeDaFatturare.get(i);
            if (aRigaTotale.getQuantita() != null && aRigaTotale.getQuantita() > 0) {
                Double quantitaFatturata = quantitaFatturataPrecedentemente(aRigaTotale, righePrecedenti);
                if (quantitaFatturata > 0) {
                    int j = i;
                    while (j < righeDaFatturare.size() && quantitaFatturata > 0) {
                        RigaDocumentoFiscale riga = righeDaFatturare.get(j);
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
    }*/

    public static void calcolaDifferenzaDaFatturare(List<MROldRigaDocumentoFiscale> righeFatturePrecedenti1, List<MROldRigaDocumentoFiscale> righeFatturaTotale) {

        ArrayList righePrecedenti = new ArrayList(righeFatturePrecedenti1);

        int totRigheKmTempoPrecedenti = 0;
        for (MROldRigaDocumentoFiscale rigaPrecedente : righeFatturePrecedenti1){
            if (Boolean.TRUE.equals(rigaPrecedente.getTempoExtra()) || Boolean.TRUE.equals(rigaPrecedente.getTempoKm())) {
                totRigheKmTempoPrecedenti += rigaPrecedente.getQuantita();
            }
        }
        int totRigheKmTempoTotali = 0;
        for (MROldRigaDocumentoFiscale rigaPrecedente : righeFatturaTotale){
            if (Boolean.TRUE.equals(rigaPrecedente.getTempoExtra()) || Boolean.TRUE.equals(rigaPrecedente.getTempoKm())) {
                totRigheKmTempoTotali += rigaPrecedente.getQuantita();
            }
        }

        boolean isRientroAnticipato = totRigheKmTempoPrecedenti > totRigheKmTempoTotali;

        int i = 0;
        while (i < righeFatturaTotale.size()) {
            MROldRigaDocumentoFiscale aRigaTotale = (MROldRigaDocumentoFiscale) righeFatturaTotale.get(i);
            if (aRigaTotale.getQuantita() != null && aRigaTotale.getQuantita() > 0) {

                if (aRigaTotale.getOptional() != null && aRigaTotale.getOptional().getImportoPercentuale()) {
                    Double importoFatturato = alreadyInvoicedAmount(aRigaTotale, righePrecedenti);

                    if (importoFatturato > 0) {
                        int j = i;
                        while (j < righeFatturaTotale.size() && importoFatturato > 0) {
                            MROldRigaDocumentoFiscale riga = righeFatturaTotale.get(j);
                            if (equalsTipoRiga(aRigaTotale, riga)) {
                                if (riga.getPrezzoUnitario() > importoFatturato) {
                                    riga.setPrezzoUnitario(MathUtils.round(riga.getPrezzoUnitario() - importoFatturato, 5));
                                    aggiornaTotaliRiga(riga);
                                    importoFatturato = 0.0;
                                } else if (riga.getPrezzoUnitario() <= importoFatturato) {
                                    importoFatturato = MathUtils.round(importoFatturato - riga.getPrezzoUnitario(), 5);
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

                } else {
                    Double quantitaFatturata = quantitaFatturataPrecedentemente(aRigaTotale, righePrecedenti);
                    if (quantitaFatturata > 0 || (isRientroAnticipato && Boolean.TRUE.equals(aRigaTotale.getTempoExtra()))) {
                        int j = i;
                        while (j < righeFatturaTotale.size()
                                && (quantitaFatturata > 0 || (isRientroAnticipato && Boolean.TRUE.equals(aRigaTotale.getTempoExtra())))) {
                            MROldRigaDocumentoFiscale riga = righeFatturaTotale.get(j);
                            if (equalsTipoRiga(aRigaTotale, riga)) {
                                if (riga.getQuantita() > quantitaFatturata && !isRientroAnticipato) {
                                    riga.setQuantita(MathUtils.round(riga.getQuantita() - quantitaFatturata, 5));
                                    aggiornaTotaliRiga(riga);
                                    quantitaFatturata = 0.0;
                                } else if (riga.getQuantita() <= quantitaFatturata || isRientroAnticipato) {
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
            }
            i++;
        }
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
            documentoFiscale.setFatturaRighe(FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                    sx,
                    prenotazione,
                    prenotazione.getTariffa(),
                    prenotazione.getInizio(),
                    tmpFine,
                    null,
                    null,
                    null,
                    prenotazione.getScontoPercentuale(),
                    prenotazione.getCommissione().getGiorniVoucher()).calcolaRigheProssimaFattura(sx));
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
            tex.printStackTrace();
        }
        return documentoFiscale;
    }

    public static MROldDocumentoFiscale creaAnteprimaDocumentoFiscaleDaPrenotazionePP(Session sx, Integer idPrenotazione) {

        MROldPrenotazione prenotazione = (MROldPrenotazione) sx.get(MROldPrenotazione.class, idPrenotazione);
        MROldDocumentoFiscale documentoFiscale = new MROldDocumentoFiscale();

        Date tmpFine = prenotazione.getFine();
        Date tmpDataFine = FormattedDate.extractDate(prenotazione.getFine());

        if (Boolean.TRUE.equals(prenotazione.getTariffa().getOraRientroAttiva()) && prenotazione.getTariffa().getOraRientro() != null) {
            tmpFine = FormattedDate.createTimestamp(tmpDataFine, prenotazione.getTariffa().getOraRientro());
        }


        try {
            try {
                documentoFiscale.setFatturaRighe(FatturazioneFactory.PREPAGATO_FACTORY.getFatturazione(
                        sx,
                        prenotazione,
                        prenotazione.getTariffa(),
                        prenotazione.getInizio(),
                        tmpFine,
                        null,
                        null,
                        null,
                        prenotazione.getScontoPercentuale(),
                        prenotazione.getCommissione().getGiorniVoucher()).calcolaRigheProssimaFattura(sx));
            } catch (TariffaNonValidaException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < documentoFiscale.getFatturaRighe().size(); i++) {
                MROldRigaDocumentoFiscale riga = (MROldRigaDocumentoFiscale) documentoFiscale.getFatturaRighe().get(i);
                riga.setFattura(documentoFiscale);
            }
            Double[] totali = calcolaImportiRigheFattura(documentoFiscale.getFatturaRighe());
            documentoFiscale.setTotaleImponibile(totali[0]);
            documentoFiscale.setTotaleIva(totali[1]);
            documentoFiscale.setTotaleFattura(totali[2]);
        } catch (TitledException tex) {
            documentoFiscale.setFatturaRighe(new ArrayList());
            documentoFiscale.setTotaleImponibile(0.0);
            documentoFiscale.setTotaleIva(0.0);
            documentoFiscale.setTotaleFattura(0.0);
        }
        return documentoFiscale;
    }

    public static MROldDocumentoFiscale creaAnteprimaDocumentoFiscaleDaContrattoPP(Session sx, Integer idContratto) {

        MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
        MROldDocumentoFiscale documentoFiscale = new MROldDocumentoFiscale();

        Date tmpFine = contratto.getFine();
        Date tmpDataFine = FormattedDate.extractDate(contratto.getFine());

        if (Boolean.TRUE.equals(contratto.getTariffa().getOraRientroAttiva()) && contratto.getTariffa().getOraRientro() != null) {
            tmpFine = FormattedDate.createTimestamp(tmpDataFine, contratto.getTariffa().getOraRientro());
        }


        try {
            documentoFiscale.setFatturaRighe(FatturazioneFactory.PREPAGATO_FACTORY.getFatturazione(
                    sx,
                    contratto,
                    contratto.getTariffa(),
                    contratto.getInizio(),
                    tmpFine,
                    null,
                    null,
                    null,
                    contratto.getScontoTariffa(),
                    contratto.getCommissione().getGiorniVoucher()).calcolaRigheProssimaFattura(sx));
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
     * Legge dal database tutte le righe di tutte le fatture e ricevute fiscali non prepagate, emesse per
     * il contratto, escludendo le fatture di acconto.
     * @param sx
     * @param contrattoNoleggio Il contratto in causa.
     * @return
     */
    public static List<MROldRigaDocumentoFiscale> leggiRigheDocumentiFiscaliEmessi(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        return sx.createQuery(
                "select r from it.myrent.ee.db.MROldRigaDocumentoFiscale r "  +
                        "where r.fattura.class <> it.myrent.ee.db.MROldFatturaAcconto "
                + "and r.fattura.contratto = :contratto "
                + "and r.fattura.prepagato = :false ").
                setParameter("contratto", contrattoNoleggio).
                setParameter("false", Boolean.FALSE).
                list();
    }

    /**
     * Legge dal database tutte le righe di tutte le fatture e ricevute fiscali non prepagate, emesse per
     * il contratto, escludendo le fatture di acconto.
     * @param sx
     * @param prenotazione Il contratto in causa.
     * @return
     */
    public static List<MROldRigaDocumentoFiscale> leggiRigheDocumentiFiscaliEmessi(Session sx, MROldPrenotazione prenotazione) {
        return sx.createQuery(
                "select r from it.myrent.ee.db.MROldRigaDocumentoFiscale r "
                + "where r.fattura.class <> it.myrent.ee.db.MROldFatturaAcconto "
                + "and r.fattura.prenotazione = :prenotazione "
                + "and r.fattura.prepagato = :false ").
                setParameter("prenotazione", prenotazione).
                setParameter("false", Boolean.FALSE).
                list();
    }

    /**
     * Legge dal database tutte le righe di tutte le fatture e ricevute fiscali non prepagate, emesse per
     * il contratto, escludendo le fatture di acconto.
     * @param sx
     * @param prenotazione Il contratto in causa.
     * @return
     */
    public static List<MROldRigaDocumentoFiscale> leggiRigheDocumentiFiscaliEmessiPrepagati(Session sx, it.myrent.ee.db.MROldPrenotazione prenotazione) {
        return sx.createQuery(
                "select r from it.myrent.ee.db.MROldRigaDocumentoFiscale r "
                + "where r.fattura.class <> it.myrent.ee.db.MROldFatturaAcconto "
                + "and r.fattura.prenotazione = :prenotazione "
                + "and r.fattura.prepagato = :true ").
                setParameter("prenotazione", prenotazione).
                setParameter("true", Boolean.TRUE).
                list();
    }

    /**
     * Calcola il numero di giorni non prepagati che sono stati fatturati per questo noleggio.
     * @param sx
     * @param contrattoNoleggio Il contratto di noleggio.
     * @return Il numero totale di giorni non prepagati fatturati.
     */
    public static Integer leggiTotaleGiorniNonPrepagatiFatturati(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        Number totaleGiorni = 0;

        if (contrattoNoleggio != null && contrattoNoleggio.getId() != null) {
            totaleGiorni = (Number) sx.createQuery(
                    "select sum(CASE WHEN r.fattura.class = it.myrent.ee.db.MROldNotaCredito THEN -r.quantita ELSE r.quantita END) from it.myrent.ee.db.MROldRigaDocumentoFiscale r "
                    + "where r.fattura.class <> it.myrent.ee.db.MROldFatturaAcconto "
                    + "and r.fattura.contratto = :contratto "
                    + "and r.fattura.prepagato = :false "
                    + "and (r.tempoKm = :true or r.tempoExtra = :true)").
                    setParameter("contratto", contrattoNoleggio).
                    setParameter("true", Boolean.TRUE).
                    setParameter("false", Boolean.FALSE).
                    uniqueResult();
        }

        return totaleGiorni != null ? MathUtils.roundDouble(totaleGiorni.doubleValue()).intValue() : 0;
    }

    /**
     * Legge dal database tutte le fatture, note credito e ricevute fiscali non prepagate, emesse per il contratto,
     * escludendo le fatture di acconto.
     * @param sx
     * @param contrattoNoleggio Il contratto in causa.
     * @return
     */
    public static List<MROldDocumentoFiscale> leggiDocumentiFiscaliEmessi(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        return sx.createQuery(
                "select d from it.myrent.ee.db.MROldDocumentoFiscale d "
                + "where d.class <> it.myrent.ee.db.MROldFatturaAcconto "
                + "and d.class <> it.myrent.ee.db.MROldNotaCredito "
                + "and d.contratto.id = :contratto "
                + "and d.prepagato = :false ").
                setParameter("contratto", contrattoNoleggio.getId()).
                setParameter("false", Boolean.FALSE).setCacheable(true).list();
    }

    /**
     * Legge dal database tutte le fatture, note credito e ricevute fiscali non prepagate, emesse per il contratto,
     * escludendo le fatture di acconto.
     * @param sx
     * @param prenotazione Il contratto in causa.
     * @return
     */
    public static List<MROldDocumentoFiscale> leggiDocumentiFiscaliEmessi(Session sx, MROldPrenotazione prenotazione) {
        MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, prenotazione.getCommissione().getId());
        MROldContrattoNoleggio contratto = null;
        if (commissione.getContratto() != null) {
            contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, commissione.getContratto().getId());
        }

        Query query = sx.createQuery("select d from it.myrent.ee.db.MROldDocumentoFiscale d "
                + "where d.class <> it.myrent.ee.db.MROldFatturaAcconto "
                + (contratto != null ? "and d.contratto.id = :contratto " : "")
                + "and d.prenotazione.id = :prenotazione "
                + "and d.prepagato = :false ");

        query.setParameter("prenotazione", prenotazione.getId());
        query.setParameter("false", Boolean.FALSE);

        if (contratto != null) {
            query.setParameter("contratto", contratto.getId());
        }

        return query.list();
    }

    public static List<MROldDocumentoFiscale> leggiFattureAcconto(Session sx, MROldPrenotazione p) {
        MROldContrattoNoleggio contratto = null;

        if (p != null && p.getId() != null) {
            MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, p.getCommissione().getId());

            if (commissione.getContratto() != null) {
                contratto = (MROldContrattoNoleggio) sx.load(MROldContrattoNoleggio.class, commissione.getContratto().getId());
            }
        }
        Query query = sx.createQuery("select d from it.myrent.ee.db.MROldDocumentoFiscale d "
                + "where d.class = it.myrent.ee.db.MROldFatturaAcconto "
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

    public static List<MROldDocumentoFiscale> leggiFattureAcconto(Session sx, MROldContrattoNoleggio c) {
        return sx.createQuery(
                "select d from it.myrent.ee.db.MROldDocumentoFiscale d "
                + "where d.class = it.myrent.ee.db.MROldFatturaAcconto "
                + "and d.contratto.id = :contratto "
                + "and d.prepagato = :false ").
                setParameter("contratto", c.getId()).
                setParameter("false", Boolean.FALSE).
                list();
    }


    public static Integer leggiNDocumentiFiscaliEmessi(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        List lista = null;
        Integer numeroFatture = null;
        try {
            lista = sx.createQuery(
                    "select d from it.myrent.ee.db.MROldDocumentoFiscale d "
                    + "where d.class = it.myrent.ee.db.MROldFattura "
                    + "and d.contratto = :contratto "
                    + "and d.prepagato = :false ").
                    setParameter("contratto", contrattoNoleggio).
                    setParameter("false", Boolean.FALSE).
                    list();

            if (lista != null && lista.size() > 0) {
                numeroFatture = lista.size();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sx != null) {
                    sx.close();
                }
            } catch (Exception e) {
            }
        }

        return numeroFatture;
    }

    /**
     * Legge dal database tutte le righe di tutte le fatture e ricevute fiscali non prepagate, emesse per
     * il contratto, escludendo le fatture di acconto.
     * @param sx
     * @param contrattoNoleggio Il contratto in causa.
     * @return
     */
    public static List<MROldRigaDocumentoFiscale> leggiRigheFattureEmesse(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        /* ROB: come mai esclude le fatture di acconto? */
        return sx.createQuery(
                "select r from it.myrent.ee.db.MROldRigaDocumentoFiscale r "
                + "where r.fattura.class <> it.myrent.ee.db.MROldFatturaAcconto "
                + "and r.fattura.class <> it.myrent.ee.db.MROldNotaCredito "
                + "and r.fattura.contratto = :contratto "
                + "and r.fattura.prepagato = :false ").
                setParameter("contratto", contrattoNoleggio).
                setParameter("false", Boolean.FALSE).
                list();
    }

    /**
     * Legge dal database tutte le righe di tutte le fatture e ricevute fiscali non prepagate, emesse per
     * la prenotazione, escludendo le fatture di acconto.
     * @param sx
     * @param prenotazione La prenotazione in causa.
     * @return
     */
    public static List<MROldRigaDocumentoFiscale> leggiRigheFattureEmesse(Session sx, MROldPrenotazione prenotazione) {
        /* ROB: come mai esclude le fatture di acconto? */
        return sx.createQuery(
                "select r from it.myrent.ee.db.MROldRigaDocumentoFiscale r "
                + "where r.fattura.class <> it.myrent.ee.db.MROldFatturaAcconto "
                + "and r.fattura.class <> it.myrent.ee.db.MROldNotaCredito "
                + "and r.fattura.prenotazione = :prenotazione "
                + "and r.fattura.prepagato = :false ").
                setParameter("prenotazione", prenotazione).
                setParameter("false", Boolean.FALSE).
                list();
    }

    /**
     * Legge dal database tutte le righe di tutte le fatture e ricevute fiscali non prepagate, emesse per
     * la prenotazione, escludendo le fatture di acconto.
     * @param sx
     * @param prenotazione La prenotazione in causa.
     * @return
     */
    public static List<MROldRigaDocumentoFiscale> leggiRigheFattureEmessePrepagato(Session sx, MROldPrenotazione prenotazione) {
        /* ROB: come mai esclude le fatture di acconto? */
        return sx.createQuery(
                "select r from it.myrent.ee.db.MROldRigaDocumentoFiscale r "
                + "where r.fattura.class <> it.myrent.ee.db.MROldFatturaAcconto "
                + "and r.fattura.class <> it.myrent.ee.db.MROldNotaCredito "
                + "and r.fattura.prenotazione = :prenotazione "
                + "and r.fattura.prepagato = :true ").
                setParameter("prenotazione", prenotazione).
                setParameter("true", Boolean.TRUE).
                list();
    }


    public static List<MROldRigaDocumentoFiscale> leggiRigheFatturaLiberaEmessa(Session sx, MROldDocumentoFiscale fattura) {
//        return sx.createQuery(
//                "select r from RigaDocumentoFiscale r "
//                + "where r.fattura.class <> FatturaAcconto "
//                + "and r.fattura.class <> NotaCredito "
////                + "and r.fattura.id = :idFattura "
//
//                + "and r.fattura.documentoLibero = :true "
//                ).
//                setParameter("true", Boolean.TRUE).
//                list();
        return sx.createQuery(
                "select r from it.myrent.ee.db.MROldRigaDocumentoFiscale r "
                + "where r.fattura.id = :idfattura ").
                setParameter("idfattura", fattura.getId()).
                list();
    }

    /**
     * Legge dal database tutte le righe di tutte le note credito non prepagate, emesse per questo contratto,
     * escludendo le fatture di acconto.
     * @param sx
     * @param contrattoNoleggio Il contratto in causa.
     * @return
     */
    public static List<MROldRigaDocumentoFiscale> leggiRigheNoteCreditoEmesse(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        return sx.createQuery(
                "select r from it.myrent.ee.db.MROldRigaDocumentoFiscale r "
                + "where r.fattura.class = it.myrent.ee.db.MROldNotaCredito "
                + "and r.fattura.contratto = :contratto "
                + "and r.fattura.prepagato = :false ").
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
                "select r from it.myrent.ee.db.MROldRigaDocumentoFiscale r "
                + "where r.fattura.class = it.myrent.ee.db.MROldFattura "
                + "and r.fattura.contratto = :contratto "
                + "and r.fattura.prepagato = :false "
                + "and r.fattura <> :fattura ").
                setParameter("contratto", contrattoNoleggio).
                setParameter("fattura", fattura).
                setParameter("false", Boolean.FALSE).
                list();
    }

    /**
     * Calcola il numero di giorni non prepagati che sono stati fatturati per questo noleggio, escludendo la fattura in causa.
     * @param sx
     * @param contrattoNoleggio Il contratto di noleggio.
     * @param fattura La fattura da non considerare nel calcolo dei giorni fatturati.
     * @return Il numero totale di giorni non prepagati fatturati.
     */
    public static Integer leggiTotaleGiorniNonPrepagatiFatturePrecedenti(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldDocumentoFiscale fattura) {
        Number totaleGiorni = (Number) sx.createQuery(
                "select sum(CASE WHEN r.fattura.class = it.myrent.ee.db.MROldNotaCredito THEN -r.quantita ELSE r.quantita END) from it.myrent.ee.db.MROldRigaDocumentoFiscale r "
                + "where r.fattura.class <> it.myrent.ee.db.MROldFatturaAcconto "
                + "and r.fattura.contratto = :contratto "
                + "and r.fattura.prepagato = :false "
                + "and (r.tempoKm = :true or r.tempoExtra = :true) "
                + "and r.fattura <> :fattura ").
                setParameter("contratto", contrattoNoleggio).
                setParameter("fattura", fattura).
                setParameter("true", Boolean.TRUE).
                setParameter("false", Boolean.FALSE).
                uniqueResult();
        return totaleGiorni != null ? MathUtils.roundDouble(totaleGiorni.doubleValue()).intValue() : 0;
    }

    /**
     * Calcola il totale fatturato di un contratto. Siccome nelle fatture di saldo il totale imponibile e' al netto degli acconti,
     * il metodo applicato qui ricalcola il totale che deriva dalle righe dei documenti (senza gli acconti).
     * @param sx La sessione al database
     * @param contratto Il contratto 
     * @return il totale fatturato
     */
    public static Double calcolaTotaleFatturatoContratto(Session sx, MROldContrattoNoleggio contratto) {
        //TODO Introdurre nella fattura i campi necessari per evitare questo calcolo.
            Double aFatturato = 0.0;
            List<MROldDocumentoFiscale> documenti = FatturaUtils.leggiDocumentiFiscaliEmessi(sx, contratto);
        /*
        if split payment check if there is to add insurance workshop VAT amount
        IF NOT SPLIT VAT HALF
        */
            Double splitPaymentInsuranceAmountVATToAdd = 0.0;
            Double splitPaymentWorkshopAmountVATToAdd = 0.0;
            Double optionalAmountIfHalfVatSplit = 0.0;
            if(contratto != null && contratto.getSplitPayment() != null && contratto.getSplitPayment() &&
                    contratto.getSplitPaymentVat() != null && !contratto.getSplitPaymentVat().equals("Each party pays their own VAT")
                    && !contratto.getSplitPaymentVat().equals("Ogni parte paga la sua IVA")){
                for (MROldDocumentoFiscale documentoFiscale : documenti) {
                    List<MROldRigaDocumentoFiscale> listaRighe = documentoFiscale.getFatturaRighe();
                    for (MROldRigaDocumentoFiscale rigaDocumento : listaRighe) {
                        if (rigaDocumento != null && rigaDocumento.getDescrizione() != null) {
                            if (rigaDocumento.getDescrizione().equals("Paid by insurance")) {
                                if (rigaDocumento.getCodiceIva() != null) {
                                    Double aliquota = rigaDocumento.getCodiceIva().getAliquota().doubleValue();
                                    if (aliquota != 0.00) {
                                        Double imponibile = rigaDocumento.getTotaleImponibileRiga();
                                        splitPaymentInsuranceAmountVATToAdd = MathUtils.round(imponibile * aliquota * -1.00, 2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (MROldDocumentoFiscale documentoFiscale : documenti) {
                if(documentoFiscale != null && documentoFiscale.getCliente() != null
                        && documentoFiscale.getCliente().getTypeOfCustomer() != null
                        && documentoFiscale.getCliente().getTypeOfCustomer().equals(MROldClienti.WORKSHOP)){
                    List<MROldRigaDocumentoFiscale> listaRighe = documentoFiscale.getFatturaRighe();
                    for (MROldRigaDocumentoFiscale rigaDocumento : listaRighe) {
                        if(contratto != null && contratto.getTariffa() != null &&
                                contratto.getTariffa().getCodiceIva() != null &&
                                rigaDocumento != null && rigaDocumento.getTotaleRiga() != null
                                && rigaDocumento.getTotaleRiga() > 0){
                        /*
                        recuperare l'iva di defalut
                        */
                            MROldCodiciIva codIva = contratto.getTariffa().getCodiceIva();
                            Double aliquota = codIva.getAliquota().doubleValue();
                            Double imponibile = rigaDocumento.getTotaleImponibileRiga();
                            splitPaymentWorkshopAmountVATToAdd = splitPaymentWorkshopAmountVATToAdd + MathUtils.round(imponibile * aliquota, 2);
                        }
                    }
                }
            }

            for (MROldDocumentoFiscale documentoFiscale : documenti) {
                ImpostaFactory imposta = new ImpostaFactory(documentoFiscale.getFatturaRighe());
                if (documentoFiscale.getTipoDocumento().equals(MROldDocumentoFiscale.NC)) {
                    aFatturato = MathUtils.round(aFatturato - imposta.getTotaleImponibile() - imposta.getTotaleImposta());
                } else {
                    aFatturato = MathUtils.round(aFatturato + imposta.getTotaleImponibile() + imposta.getTotaleImposta());

                }
            }

            aFatturato = aFatturato + splitPaymentInsuranceAmountVATToAdd + splitPaymentWorkshopAmountVATToAdd + optionalAmountIfHalfVatSplit;

            return aFatturato;

    }

    public static Double calcolaTotaleFatturatoPrenotazione(Session sx, MROldPrenotazione prenotazione) {
        //TODO Introdurre nella fattura i campi necessari per evitare questo calcolo.
            Double aFatturato = 0.0;
            List<MROldDocumentoFiscale> documenti = FatturaUtils.leggiDocumentiFiscaliEmessi(sx, prenotazione);
        /*
        if split payment check if there is to add insurance workshop VAT amount
        IF NOT SPLIT VAT HALF
        */
            Double splitPaymentInsuranceAmountVATToAdd = 0.0;
            Double splitPaymentWorkshopAmountVATToAdd = 0.0;
            Double optionalAmountIfHalfVatSplit = 0.0;
            if(prenotazione != null && prenotazione.getSplitPayment() != null && prenotazione.getSplitPayment() &&
                    prenotazione.getSplitPaymentVat() != null && !prenotazione.getSplitPaymentVat().equals("Each party pays their own VAT")
                    && !prenotazione.getSplitPaymentVat().equals("Ogni parte paga la sua IVA")){
                for (MROldDocumentoFiscale documentoFiscale : documenti) {
                    List<MROldRigaDocumentoFiscale> listaRighe = documentoFiscale.getFatturaRighe();
                    for (MROldRigaDocumentoFiscale rigaDocumento : listaRighe) {
                        if (rigaDocumento != null && rigaDocumento.getDescrizione() != null) {
                            if (rigaDocumento.getDescrizione().equals("Paid by insurance")) {
                                if (rigaDocumento.getCodiceIva() != null) {
                                    Double aliquota = rigaDocumento.getCodiceIva().getAliquota().doubleValue();
                                    if (aliquota != 0.00) {
                                        Double imponibile = rigaDocumento.getTotaleImponibileRiga();
                                        splitPaymentInsuranceAmountVATToAdd = MathUtils.round(imponibile * aliquota * -1.00, 2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (MROldDocumentoFiscale documentoFiscale : documenti) {
                if(documentoFiscale != null && documentoFiscale.getCliente() != null
                        && documentoFiscale.getCliente().getTypeOfCustomer() != null
                        && documentoFiscale.getCliente().getTypeOfCustomer().equals(MROldClienti.WORKSHOP)){
                    List<MROldRigaDocumentoFiscale> listaRighe = documentoFiscale.getFatturaRighe();
                    for (MROldRigaDocumentoFiscale rigaDocumento : listaRighe) {
                        if(prenotazione != null && prenotazione.getTariffa() != null &&
                                prenotazione.getTariffa().getCodiceIva() != null &&
                                rigaDocumento != null && rigaDocumento.getTotaleRiga() != null
                                && rigaDocumento.getTotaleRiga() > 0){
                        /*
                        recuperare l'iva di defalut
                        */
                            MROldCodiciIva codIva = prenotazione.getTariffa().getCodiceIva();
                            Double aliquota = codIva.getAliquota().doubleValue();
                            Double imponibile = rigaDocumento.getTotaleImponibileRiga();
                            splitPaymentWorkshopAmountVATToAdd = splitPaymentWorkshopAmountVATToAdd + MathUtils.round(imponibile * aliquota, 2);
                        }
                    }
                }
            }

            for (MROldDocumentoFiscale documentoFiscale : documenti) {
                ImpostaFactory imposta = new ImpostaFactory(documentoFiscale.getFatturaRighe());
                if (documentoFiscale.getTipoDocumento().equals(MROldDocumentoFiscale.NC)) {
                    aFatturato = MathUtils.round(aFatturato - imposta.getTotaleImponibile() - imposta.getTotaleImposta());
                } else {
                    aFatturato = MathUtils.round(aFatturato + imposta.getTotaleImponibile() + imposta.getTotaleImposta());

                }
            }

            aFatturato = aFatturato + splitPaymentInsuranceAmountVATToAdd + splitPaymentWorkshopAmountVATToAdd + optionalAmountIfHalfVatSplit;

            return aFatturato;
    }

    /*public static void email(Session sx, Component parent, String reportFattura, MROldDocumentoFiscale fattura, MROldEmailDocumentoFiscale edf) throws JRException, MalformedURLException {
         rilettura di fattura dalla sessione, problemi di lazy loading
        fattura = (DocumentoFiscale) sx.get(DocumentoFiscale.class, fattura.getId());

        HashMap parameters = new HashMap();
        JDialogAnagraficaAzienda.putParameters(parameters);
        List stampe = creaStampeDocumentiFiscali(sx, Arrays.asList(new Object[]{fattura}), true);
        JasperPrint voucher = JasperMyTools.creaPagineStampa(reportFattura, null, parameters, stampe.toArray());

        String documento = fattura.getTipoDocumentoI18N().toLowerCase();
        String numero = fattura.getNumero().toString().replaceAll("[^a-zA-Z0-9]", "_");
        String data = FormattedDate.format(bundle.getString("FatturaUtils.email.date_format"), fattura.getData());
        String fileName = System.getProperty("java.io.tmpdir") + File.separator + documento + "_" + bundle.getString("FatturaUtils.email.nr") + "_" + numero + "_" + bundle.getString("FatturaUtils.email.data") + "_" + data + ".pdf";

        gestione dei cc, con anche la gestione delle sedi di noleggio e le relative preferenze email
        String cc = "";
        if (Preferenze.getCcEmailSedeIngresso() && fattura.getContratto() != null && fattura.getContratto().getSedeRientroPrevisto() != null && fattura.getContratto().getSedeRientroPrevisto().getEmail() != null) {
            cc = fattura.getContratto().getSedeRientroPrevisto().getEmail();
        }
        if (Preferenze.getCcEmailSedeUscita() && fattura.getContratto() != null && fattura.getContratto().getSedeUscita() != null && fattura.getContratto().getSedeUscita().getEmail() != null) {
            if (!cc.isEmpty()) {
                cc += " , ";
            }
            cc += fattura.getContratto().getSedeUscita().getEmail();
        }
        if (cc.isEmpty()) {
            cc = null;
        }

        JasperExportManager.exportReportToPdfFile(voucher, fileName);
        JDialogEmail.showEmailDialog(
                parent,
                true,
                null,
                fattura.getCliente().getEmail(),
                cc,
                Preferenze.getSettingValue(Setting.PROP_OGGETTO_EMAIL_TEXT),
                Preferenze.getSettingValue(Setting.PROP_MESSAGGIO_EMAIL_TEXT),
                new String[]{fileName},
                edf);
        FileUtils.deleteFile(fileName);
    }*/

    /*public static void email(Component parent, JDialogProgressBar progressBar, String oggetto, String messaggio, List fatture, String bcc[]) {
        Session sx = null;
        Set fattureNonInviate = new HashSet();

        try {
            sx = HibernateBridge.startNewSession();
            progressBar.setIndeterminate(true);
            progressBar.setLabel(bundle.getString("FatturaUtils.msgProgressBar"));
            int countInviati = 0;
            Iterator it = fatture.iterator();

            while (it.hasNext()) {
                if (sx != null && (!sx.isOpen() || !sx.isConnected())) {
                    sx = HibernateBridge.startNewSession();
                }
                DocumentoFiscale d = (DocumentoFiscale) it.next();
                List<DocumentoFiscale> listaSingoloDocumento = new ArrayList<DocumentoFiscale>();
                listaSingoloDocumento.add(d);
                Clienti c = d.getCliente();
                if (c != null && c.getEmail() != null) {
                    HashMap parameters = new HashMap();
                    JDialogAnagraficaAzienda.putParameters(parameters);
//                    List stampe = FatturaUtils.creaStampeDocumentiFiscali(Arrays.asList(new Object[]{d}));
                    List stampe = creaStampeDocumentiFiscali(sx, listaSingoloDocumento, true);
                    JasperPrint voucher = JasperMyTools.creaPagineStampa("fattura_esteso", null, parameters, stampe.toArray()); //NOI18N

                     rilettura del documento fiscale dalla sessione, problemi di lazy loading per leggere gli estremi del contratto e delle sedi
                    d = (DocumentoFiscale) sx.get(DocumentoFiscale.class, d.getId());

                    String documento = d.getTipoDocumentoI18N().toLowerCase();
                    String numero = d.getNumero().toString().replaceAll("[^a-zA-Z0-9]", "_");
                    String data = FormattedDate.format(bundle.getString("FatturaUtils.email.date_format"), d.getData());
                    String fileName = System.getProperty("java.io.tmpdir") + File.separator + documento + "_" + bundle.getString("FatturaUtils.email.nr") + "_" + numero + "_" + bundle.getString("FatturaUtils.email.data") + "_" + data + ".pdf";

                     gestione dei cc, con anche la gestione delle sedi di noleggio e le relative preferenze email
                    String cc = "";
                    if (Preferenze.getCcEmailSedeIngresso() && d.getContratto() != null && d.getContratto().getSedeRientroPrevisto() != null && d.getContratto().getSedeRientroPrevisto().getEmail() != null) {
                        cc = d.getContratto().getSedeRientroPrevisto().getEmail();
                    }
                    if (Preferenze.getCcEmailSedeUscita() && d.getContratto() != null && d.getContratto().getSedeUscita() != null && d.getContratto().getSedeUscita().getEmail() != null) {
                        if (!cc.isEmpty()) {
                            cc += " , ";
                        }
                        cc += d.getContratto().getSedeUscita().getEmail();
                    }
                    if (cc.isEmpty()) {
                        cc = null;
                    }

                    JasperExportManager.exportReportToPdfFile(voucher, fileName);

                     costruzione array dei cc
                    String[] ccRecipients = new String[]{};
                    if (cc != null) {
                        ccRecipients = getRecipientAddresses(cc);
                    }

                    String nomeMittente = Preferenze.getMailFromName();
                    if (MailUtils.sendEmail(true, nomeMittente, oggetto, messaggio, new String[]{fileName}, new String[]{c.getEmail()}, ccRecipients, bcc)) {
                        countInviati++;
                    } else {
                        fattureNonInviate.add(d.toString());
                    }

                    FileUtils.deleteFile(fileName);
                } else {
                    fattureNonInviate.add(MessageFormat.format(bundle.getString("FatturaUtils.msgClienteSenzaMail"), c.toString()));
                }
            }

             chiusura della progress bar
            progressBar.dispose();

            String msg = bundle.getString("FatturaUtils.msgInvioMassivoEmailEsito1") + " " + countInviati + " "
                    + bundle.getString("FatturaUtils.msgInvioMassivoEmailEsito2") + " " + fatture.size() + " "
                    + bundle.getString("FatturaUtils.msgInvioMassivoEmailEsito3");
            JOptionPane.showMessageDialog(parent, msg, bundle.getString("FatturaUtils.msgInvioMassivoFatture"), JOptionPane.INFORMATION_MESSAGE);

            if (fattureNonInviate != null && fattureNonInviate.size() > 0) {
                JBigMessage.showMessageDialog(parent, bundle.getString("FatturaUtils.msginvioMassivoFattureNonIviate"), fattureNonInviate, 600, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = null;
            if (e.getCause() != null) {
                errorMessage = e.getCause().toString();
            } else {
                errorMessage = e.getMessage();
            }
            log.error(errorMessage);

             chiusura della progress bar
            progressBar.dispose();
            fattureNonInviate.add(errorMessage);
            JBigMessage.showMessageDialog(parent, bundle.getString("FatturaUtils.msginvioMassivoFattureNonIviate"), fattureNonInviate, 600, 400);
        } finally {
            try {
                if (sx != null) {
                    sx.close();
                }
            } catch (Exception e) {
            }
        }
    }*/

    private static String[] getRecipientAddresses(String text) {
        String[] addresses = text.split("[,]"); //NOI18N
        List<String> filtered = new ArrayList<String>();
        for (int i = 0; i < addresses.length; i++) {
            if (addresses[i].indexOf('@') > 0) { //NOI18N
                filtered.add(addresses[i]);
            }
        }
        addresses = new String[filtered.size()];
        for (int i = 0; i < filtered.size(); i++) {
            addresses[i] = filtered.get(i);
        }
        return addresses;
    }

    /*public static List estraiRigheScontiDaPrenotazione(Session sx, Integer idPrenotazione) {
        List lista = new ArrayList();
        MROldDocumentoFiscale documentoFiscale = new MROldDocumentoFiscale();
        try {
            MROldPrenotazione prenotazione = (MROldPrenotazione) sx.get(MROldPrenotazione.class, idPrenotazione);
            documentoFiscale.setFatturaRighe(FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                    sx,
                    prenotazione,
                    prenotazione.getTariffa(),
                    prenotazione.getInizio(),
                    prenotazione.getFine(),
                    null,
                    null,
                    null,
                    prenotazione.getScontoPercentuale(),
                    prenotazione.getCommissione().getGiorniVoucher()).calcolaRigheProssimaFattura(sx));

            for (int i = 0; i < documentoFiscale.getFatturaRighe().size(); i++) {
                MROldRigaDocumentoFiscale riga = (MROldRigaDocumentoFiscale) documentoFiscale.getFatturaRighe().get(i);
                if (riga.getQuantita() < 0.0 || riga.getPrezzoUnitario() < 0.0) {
                    ScontiPrenotazione sp = new ScontiPrenotazione();
                    sp.setDescrizione(riga.getDescrizione());
                    sp.setImporto(new DecimalFormat("###0.00").format(Math.abs(riga.getTotaleRiga())) + " €");
                    lista.add(sp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }*/

    public static Double calcolaTotaliScontiinPrenotazione(Session sx, Integer idPrenotazione) {
        Double sconto = 0.0;

        MROldDocumentoFiscale documentoFiscale = new MROldDocumentoFiscale();
        try {
            MROldPrenotazione prenotazione = (MROldPrenotazione) sx.get(MROldPrenotazione.class, idPrenotazione);
            documentoFiscale.setFatturaRighe(FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                    sx,
                    prenotazione,
                    prenotazione.getTariffa(),
                    prenotazione.getInizio(),
                    prenotazione.getFine(),
                    null,
                    null,
                    null,
                    prenotazione.getScontoPercentuale(),
                    prenotazione.getCommissione().getGiorniVoucher()).calcolaRigheProssimaFattura(sx));

            for (int i = 0; i < documentoFiscale.getFatturaRighe().size(); i++) {
                MROldRigaDocumentoFiscale riga = (MROldRigaDocumentoFiscale) documentoFiscale.getFatturaRighe().get(i);
                if (riga.getTotaleRiga() < 0.0) {
                    sconto += Math.abs(riga.getTotaleRiga());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sconto;
    }

    public static List<MROldDocumentoFiscale> leggiFattureLibere(Session sx) {
        return sx.createQuery("select d from it.myrent.ee.db.MROldDocumentoFiscale d where d.documentoLibero = :true and d.class <> it.myrent.ee.db.MROldFatturaAcconto and d.class <> it.myrent.ee.db.MROldNotaCredito ").setParameter("true", Boolean.TRUE).list();
    }

    /*
     * estrae le fatture della pre
     */
    public static List<MROldDocumentoFiscale> leggiFatturePrenotazione(Session sx, MROldPrenotazione p) {
        List<MROldDocumentoFiscale> lista = sx.createQuery("SELECT d FROM it.myrent.ee.db.MROldDocumentoFiscale d WHERE d.prenotazione.id = :id").setParameter("id", p.getId()).list();

        return lista;
    }

    /**
     * legge gli incassi registrati per la prenotazione
     * @param sx
     * @param p
     * @return
     */
    public static List<MROldPrimanota> leggiIncassiPrenotazione(Session sx, MROldPrenotazione p) {
        List<MROldPrimanota> lista = null;

        lista = sx.createQuery("SELECT p FROM it.myrent.ee.db.MROldPrimanota p WHERE p.prenotazione.id = :id").setParameter("id", p.getId()).list();

        return lista;
    }

    public static List<MROldPrimanota> leggiIncassiContratto(Session sx, MROldContrattoNoleggio c) {
        List<MROldPrimanota> lista = null;

        lista = sx.createQuery("SELECT p FROM it.myrent.ee.db.MROldPrimanota p WHERE p.contratto.id = :id").setParameter("id", c.getId()).setCacheable(true).list();

        return lista;
    }

    /**
     * aggiorna gli incassi delle prenotazioni sul contratto di noleggio
     * @param sx
     * @param p
     * @param c
     * @return
     */
    public static void aggiornaIncasssiPrenotazioneSuContratto(Session sx, MROldPrenotazione p, MROldContrattoNoleggio c) {

        List<MROldPrimanota> lista = leggiIncassiPrenotazione(sx, p);

        if (lista != null && !lista.isEmpty()) {
            Iterator<it.myrent.ee.db.MROldPrimanota> it = lista.iterator();
            while (it.hasNext()) {
                MROldPrimanota aPrimanota = it.next();
                aPrimanota.setContratto(c);
                sx.saveOrUpdate(aPrimanota);
            }
        }
    }

    /**
     * aggiorna gli incassi delle prenotazioni sul contratto di noleggio
     * @param sx
     * @param p
     * @param c
     * @return
     */
    public static void aggiornaIncasssiPrenotazioneSuContratto(Session sx, MROldPrenotazione p, MROldContrattoNoleggio c, List<MROldPrimanota> lista) {

        if (lista != null && !lista.isEmpty()) {
            Iterator<MROldPrimanota> it = lista.iterator();
            while (it.hasNext()) {
                MROldPrimanota aPrimanota = it.next();
                aPrimanota.setContratto(c);
                sx.saveOrUpdate(aPrimanota);
            }
        }
    }

    /**
     * aggiorna le fatture delle prenotazioni sul contratto di noleggio
     * @param sx
     * @param p
     * @param c
     * @return
     */
    public static void aggiornaFatturePrenotazioneSuContratto(Session sx, MROldPrenotazione p, MROldContrattoNoleggio c) {

        List<MROldDocumentoFiscale> lista = leggiFatturePrenotazione(sx, p);

        if (lista != null && !lista.isEmpty()) {
            Iterator<MROldDocumentoFiscale> it = lista.iterator();
            while (it.hasNext()) {
                MROldDocumentoFiscale d = it.next();
                d.setContratto(c);
                sx.saveOrUpdate(d);
            }
        }
    }

    public static void aggiornaFatturePrenotazioneSuContratto(Session sx, MROldPrenotazione p, MROldContrattoNoleggio c, List<MROldDocumentoFiscale> lista) {

        if (lista != null && !lista.isEmpty()) {
            Iterator<MROldDocumentoFiscale> it = lista.iterator();
            while (it.hasNext()) {
                MROldDocumentoFiscale d = it.next();
                d.setContratto(c);
                sx.saveOrUpdate(d);
            }
        }
    }

    /**
     *
     * scollega fatture ed incassi della prenotazione dal contratto
     * @param sx
     * @param idContratto
     */
    public static void eliminaFattureEIncassi(Session sx, Integer idContratto) {
        List<MROldPrimanota> listaIncassi = sx.createQuery("SELECT p FROM it.myrent.ee.db.MROldPrimanota p where p.contratto.id = :id and p.prenotazione.id is not null").setParameter("id", idContratto).list();

        List<MROldDocumentoFiscale> listaFatture = sx.createQuery("SELECT d FROM it.myrent.ee.db.MROldDocumentoFiscale d where d.contratto.id = :id and d.prenotazione.id is not null").setParameter("id", idContratto).list();

        if (listaIncassi != null && !listaIncassi.isEmpty()) {
            Iterator<MROldPrimanota> itPrimanota = listaIncassi.iterator();
            while (itPrimanota.hasNext()) {
                MROldPrimanota p = itPrimanota.next();
                p.setContratto(null);
                sx.saveOrUpdate(p);
            }
        }

        if (listaFatture != null && !listaFatture.isEmpty()) {
            Iterator<MROldDocumentoFiscale> itFattura = listaFatture.iterator();
            while (itFattura.hasNext()) {
                MROldDocumentoFiscale d = itFattura.next();
                d.setContratto(null);
                sx.saveOrUpdate(d);
            }
        }
    }

    public static int emailInviate(Session sx, MROldDocumentoFiscale fattura) {
        int emailInviate = 0;
        try {
            Long l = (Long) sx.createQuery("SELECT count(e.id) FROM it.myrent.ee.db.MROldEmailDocumentoFiscale e WHERE e.documentoFiscale.id = :id").setParameter("id", fattura.getId()).uniqueResult();
            if (l != null) {
                emailInviate = l.intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailInviate;
    }

    /**
     * il totale gli incassi registrati per la prenotazione
     * @param sx
     * @param p
     * @return
     */
    public static Double getIncassiPrenotazione(Session sx, MROldPrenotazione p) {
        Double incassato = 0.0;
        List<Integer> listaCausaliIncasso = new ArrayList<Integer>();
        listaCausaliIncasso.add(6);
        listaCausaliIncasso.add(19);

        List<MROldPrimanota> lista = sx.createQuery("SELECT p FROM it.myrent.ee.db.MROldPrimanota p WHERE p.causale.codice in (:causaliIncasso) and p.prenotazione.id = :id").setParameterList("causaliIncasso", listaCausaliIncasso).setParameter("id", p.getId()).list();

        if (lista != null && !lista.isEmpty()) {
            Iterator<MROldPrimanota> it = lista.iterator();
            while (it.hasNext()) {
                MROldPrimanota primanota = it.next();
                incassato += primanota.getTotaleDocumento();
            }
        }

        return incassato;
    }

    /**
     * get the type pf document. Only for reports
     * @param discriminator
     * @return
     */
    public static String getDiscriminatorI18n(String discriminator) {
        if (discriminator.equals(MROldDocumentoFiscale.FT_DATABASE)) {
            return MROldDocumentoFiscale.FT_I18N;
        } else if (discriminator.equals(MROldDocumentoFiscale.NC_DATABASE)) {
            return MROldDocumentoFiscale.NC_I18N;
        } else if (discriminator.equals(MROldDocumentoFiscale.FTA_DATABASE)) {
            return MROldDocumentoFiscale.FTA_I18N;
        } else if (discriminator.equals(MROldDocumentoFiscale.RF_DATABASE)) {
            return MROldDocumentoFiscale.RF_I18N;
        } else {
            return discriminator;
        }
    }
    
    private static Date oraFineContrattoFatturazione(Session sx, MROldContrattoNoleggio contrattoNoleggio){
        
        MROldMovimentoAuto primoMovimento = null;
        MROldMovimentoAuto ultimoMovimento = null;
        if (contrattoNoleggio.getMovimento()!=null && contrattoNoleggio.getMovimento().getUltimo() && contrattoNoleggio.getMovimento().getChiuso()) {
                ultimoMovimento = contrattoNoleggio.getMovimento();
            }
             

//            Date timestampOraInizio = FormattedDate.extractTime(contrattoNoleggio.getInizio());
//            Date timestampaDateFine = FormattedDate.extractDate(contrattoNoleggio.getFine());
//            Date timestampOreFine = FormattedDate.extractTime(contrattoNoleggio.getFine());

            Date timestampOraInizio = null;
            Date timestampaDateFine = null;
            Date timestampOreFine = null;
            if (it.myrent.ee.api.preferences.Preferenze.getFatturaTuttaDurataNoleggio(sx)) {
                Date dataFineContratto = contrattoNoleggio.getFine();
                
                if (contrattoNoleggio.getMovimento()!=null && contrattoNoleggio.getMovimento().getUltimo()) {
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
            
            return timestamp;

    }

    public static void calcolaTotaleNoleggioNoPPay(Session sx, MROldContrattoNoleggio c) throws it.myrent.ee.api.exception.TariffaNonValidaException {
            List rows;

            if (c.getContrattoNoleggioRighe() != null) {
                rows = c.getContrattoNoleggioRighe();
                if (rows.size() > 0)
                    c.getContrattoNoleggioRighe().removeAll(c.getContrattoNoleggioRighe());
            } else {
                return;
            }
            List<MROldRigaDocumentoFiscale> righeDaFatturare = null;
            double totaleIva = 0, totaleImponibile = 0, totaleDocumento = 0;
            Fatturazione fatturazione = null;

            //if (!Boolean.TRUE.equals(c.getCommissione().getPrepagato())) {
            List<MROldRigaDocumentoFiscale> righeFattura = null;
            try {
                fatturazione = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                        sx,
                        c,
                        c.getTariffa(),
                        c.getInizio(),
                        oraFineContrattoFatturazione(sx, c),
                        c.getScontoTariffa(),
                        c.getCommissione().getPrepagato() ? c.getCommissione().getGiorniVoucher() : 0);

//            if (c.getId() == 35324 || c.getId() == 35325) {
//                righeFattura = fatturazione.anteprimaValoreFatture(sx);
//            }

                righeFattura = fatturazione.anteprimaValoreFatture(sx);
//                if(fatturazione instanceof FatturazioneBreveTermine){
//                    FatturazioneBreveTermine f = (FatturazioneBreveTermine)fatturazione;
//                    righeFattura = f.calcolaRighe(sx);
//
//                }
//                else if(fatturazione instanceof FatturazionePrepagato){
//                    FatturazionePrepagato f = (FatturazionePrepagato)fatturazione;
//                    righeFattura = f.calcolaRighe(sx);
//                }
//                else if(fatturazione instanceof FatturazionePlurimensile){
//                    FatturazionePlurimensile f = (FatturazionePlurimensile)fatturazione;
//                    righeFattura = f.calcolaRighe(sx);
//                }
            } catch (FatturaVuotaException fvex) {
               // log.error("Contratto - Errore per fattura vuota");
               // fvex.printStackTrace();
                righeFattura = new ArrayList();
            }

            if (righeFattura != null && !righeFattura.isEmpty()) {
                Double[] importi = FatturaUtils.calcolaImportiRigheFattura(righeFattura);

            /*if (fatturazione != null) {
                    righeDaFatturare = fatturazione.calcolaRigheFattura(sx);
                }*/

            /*for (RigaDocumentoFiscale riga : righeDaFatturare) {
                    totaleIva += riga.getTotaleIvaRiga();
                    totaleImponibile += riga.getTotaleImponibileRiga();
                    totaleDocumento += riga.getTotaleRiga();
                }*/
                /**
                 * *
                 * Method modified by Mauro 2015-08-27 for NoleggiareBIDevelopment
                 *
                 *
                 *
                 * c.setNoleggioPPay(importi[2]);
                 *
                 */
                /**
                 * *
                 * Method modified by Mauro 2015-08-27 for NoleggiareBIDevelopment
                 *
                 * if (importi[2]<totaleIva){ System.out.println("*** ANOMALIA:
                 * **************"); System.out.println("*** IMPORTI: " +
                 * importi[2]); System.out.println("*** VAT: "+importi[1]); }
                 *
                 *
                 */
                c.setNoleggio(importi[2]);
                c.setNoleggioNoVat(importi[3]);

            /*if (fatturazione != null) {
                    righeDaFatturare = fatturazione.calcolaRigheFattura(sx);
                }*/
                for (MROldRigaDocumentoFiscale riga : righeFattura) {
                /*
                    totaleIva+=riga.getTotaleIvaRiga() ;
                    totaleImponibile+=riga.getTotaleImponibileRiga();
                    totaleDocumento+=riga.getTotaleRiga();
                     *
                 */

                    MROldRigaContrattoNoleggio newRow = new MROldRigaContrattoNoleggio(riga, c);

                    c.getContrattoNoleggioRighe().add(newRow);

                }

            }
    }

    public static void calcolaTotaleNoleggioNoPPay(Session sx, MROldPrenotazione p) throws it.myrent.ee.api.exception.TariffaNonValidaException {


            if (p.getPrenotazioneRighe() != null) {
                p.getPrenotazioneRighe().removeAll(p.getPrenotazioneRighe());
            } else {
                return;
            }

            //List<RigaDocumentoFiscale> righeDaFatturare = null;
            List<MROldRigaPrenotazione> righePrenotazione = new ArrayList<MROldRigaPrenotazione>();
            double totaleIva = 0, totaleImponibile = 0, totaleDocumento = 0;
            Fatturazione fatturazione = null;

        /*
         * Andrea
         */
            Date inizio = null, fine = null, tmpOraFine = null;
            inizio = p.getInizio();
            fine = p.getFine();

            if (fine != null) {
                if (p.getTariffa() != null && Boolean.TRUE.equals(p.getTariffa().getOraRientroAttiva()) && p.getTariffa().getOraRientro() != null) {
                    tmpOraFine = p.getTariffa().getOraRientro();
                } else {
                    tmpOraFine = (Date) fine;
                }

                fine = FormattedDate.createTimestamp((Date) fine, tmpOraFine);
            }

            List<MROldRigaDocumentoFiscale> listaPrepagato = null;
            List<MROldRigaDocumentoFiscale> righeFattura = null;
            try {
            /*
                 * Andrea, changed from p.getFine() to fine
             */
                fatturazione = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                        sx,
                        p,
                        p.getTariffa(),
                        p.getInizio(),
                        fine,
                        p.getScontoPercentuale(),
                        p.getCommissione() != null ? (p.getCommissione().getPrepagato() ? (p.getCommissione().getGiorniVoucher() != null ? p.getCommissione().getGiorniVoucher() : 0) : 0) : 0);

                righeFattura = fatturazione.anteprimaValoreFatture(sx);
                listaPrepagato = righeFattura;
            } catch (FatturaVuotaException fvex) {
                log.error("Prenotazione - Errore per fattura vuota");
                fvex.printStackTrace();
                listaPrepagato = new ArrayList();
            }

            if (listaPrepagato != null && !listaPrepagato.isEmpty()) {
                Double[] importi = FatturaUtils.calcolaImportiRigheFattura(listaPrepagato);

                /**
                 * *
                 * Method modified by Mauro 2015-08-27 for NoleggiareBIDevelopment
                 *
                 *
                 *
                 * p.setNoleggioPPay(importi[2]);
                 *
                 */
                /**
                 * *
                 * Method modified by Mauro 2015-08-27 for NoleggiareBIDevelopment
                 */
                p.setNoleggio(importi[2]);
                p.setNoleggioNoVat(importi[3]);


                for (MROldRigaDocumentoFiscale riga : listaPrepagato) {
                    MROldRigaPrenotazione newRow = new MROldRigaPrenotazione(riga, p);
                    p.getPrenotazioneRighe().add(newRow);
                }

            }

    }

    public static void calcolaTotaleNoleggioPPay(Session sx, MROldContrattoNoleggio c) throws it.myrent.ee.api.exception.TariffaNonValidaException {
            if (c.getContrattoNoleggioRighePPay() != null) {
                c.getContrattoNoleggioRighePPay().removeAll(c.getContrattoNoleggioRighePPay());
            } else {
                return;
            }

            Fatturazione fatturazione = null;
            //List<RigaDocumentoFiscale> righeDaFatturare = null;
            double totaleIva = 0, totaleImponibile = 0, totaleDocumento = 0;

            if (Boolean.TRUE.equals(c.getCommissione().getPrepagato()) && c.getCommissione().getGiorniVoucher() != null) {
                List<MROldRigaDocumentoFiscale> listaPrepagato = null;
                try {
                    Double aScontoExtraPrepay = 0.0;
                    if(c.getScontoExtraPrepay() != null) {
                        aScontoExtraPrepay = c.getScontoExtraPrepay();
                    }

                    fatturazione = FatturazioneFactory.PREPAGATO_FACTORY.getFatturazione(
                            sx,
                            c.getTariffa(),
                            c.getInizio(),
                            c.getFine(),
                            c.getCommissione().getGiorniVoucher(),
                            aScontoExtraPrepay);
                    listaPrepagato = fatturazione.calcolaRigheProssimaFattura(sx);
                } catch (FatturaVuotaException fvex) {
                    log.error("Contratto - Errore per fattura vuota");
                    fvex.printStackTrace();
                    listaPrepagato = new ArrayList();
                }

                if (listaPrepagato != null && !listaPrepagato.isEmpty()) {
                    Double[] importi = FatturaUtils.calcolaImportiRigheFattura(listaPrepagato);
                    c.setNoleggioPPay(importi[2]);

                /*for (RigaDocumentoFiscale riga : righeDaFatturare) {
                    totaleIva += riga.getTotaleIvaRiga();
                    totaleImponibile += riga.getTotaleImponibileRiga();
                    totaleDocumento += riga.getTotaleRiga();
                }*/
                    /**
                     * *
                     * Method modified by Mauro 2015-08-27 for
                     * NoleggiareBIDevelopment
                     */
                    c.setNoleggioPPayNoVat(importi[3]);


                /*if (fatturazione != null) {
                    righeDaFatturare = fatturazione.calcolaRigheFattura(sx);
                }*/
                    for (MROldRigaDocumentoFiscale riga : listaPrepagato) {
                    /*
                    totaleIva+=riga.getTotaleIvaRiga() ;
                    totaleImponibile+=riga.getTotaleImponibileRiga();
                    totaleDocumento+=riga.getTotaleRiga();
                     *
                     */

                        MROldRigaContrattoNoleggioPPay newRow = new MROldRigaContrattoNoleggioPPay(riga, c);

                        c.getContrattoNoleggioRighePPay().add(newRow);

                    }

                }
            }
    }


    public static void calcolaTotaleNoleggioPPay(Session sx, MROldPrenotazione p) throws it.myrent.ee.api.exception.TariffaNonValidaException {
       

            if (p.getPrenotazioneRighePPay() != null) {
                p.getPrenotazioneRighePPay().removeAll(p.getPrenotazioneRighePPay());
            } else {
                return;
            }

            //List<RigaDocumentoFiscale> righeDaFatturare = null;
            //List<RigaPrenotazionePPay> righePrenotazione = new ArrayList<RigaPrenotazionePPay>();
            double totaleIva = 0, totaleImponibile = 0, totaleDocumento = 0;
            Fatturazione fatturazione = null;

        /*
         * Andrea
         */
            Date inizio = null, fine = null, tmpOraFine = null;
            inizio = p.getInizio();
            fine = p.getFine();

            if (fine != null) {
                if (p.getTariffa() != null && Boolean.TRUE.equals(p.getTariffa().getOraRientroAttiva()) && p.getTariffa().getOraRientro() != null) {
                    tmpOraFine = p.getTariffa().getOraRientro();
                } else {
                    tmpOraFine = (Date) fine;
                }

                fine = FormattedDate.createTimestamp((Date) fine, tmpOraFine);
            }

            if (Boolean.TRUE.equals(p.getCommissione().getPrepagato()) && p.getCommissione().getGiorniVoucher() != null) {
                List<MROldRigaDocumentoFiscale> listaPrepagato = null;
                try {
                /*
                 * Andrea, changed from p.getFine() to fine
                 */
                    Double aScontoExtraPrepay = 0.0;
                    if(p.getScontoExtraPrepay() != null) {
                        aScontoExtraPrepay = p.getScontoExtraPrepay();
                    }

                    fatturazione = FatturazioneFactory.PREPAGATO_FACTORY.getFatturazione(
                            sx,
                            p.getTariffa(),
                            p.getInizio(),
                            fine,
                            p.getCommissione().getGiorniVoucher(),
                            aScontoExtraPrepay);
                    listaPrepagato = fatturazione.calcolaRigheProssimaFattura(sx);
                } catch (FatturaVuotaException fvex) {
                    log.error("JDialogUpdateAmount.calcolaTotaleNoleggioPPay - Prenotazione - Errore per fattura vuota");
                    fvex.printStackTrace();
                    listaPrepagato = new ArrayList();
                }

                if (listaPrepagato != null && !listaPrepagato.isEmpty()) {
                    Double[] importi = FatturaUtils.calcolaImportiRigheFattura(listaPrepagato);
                    p.setNoleggioPPay(importi[2]);

                    /**
                     * *
                     * Method modified by Mauro 2015-08-27 for
                     * NoleggiareBIDevelopment
                     */
                    p.setNoleggioPPayNoVat(importi[3]);

                    if (fatturazione != null) {
                        listaPrepagato = fatturazione.calcolaRigheFattura(sx);
                    }

                    for (MROldRigaDocumentoFiscale riga : listaPrepagato) {
                        MROldRigaPrenotazionePPay newRow = new MROldRigaPrenotazionePPay(riga, p);

                        p.getPrenotazioneRighePPay().add(newRow);

                    }

                }
            }
//        if (p.getPrenotazioneRighePPay()!=null)
//            p.getPrenotazioneRighePPay().removeAll(p.getPrenotazioneRighePPay());
//        else
//            return;
//
//
//        double totaleIva = 0, totaleImponibile = 0, totaleDocumento = 0;
//        Fatturazione fatturazione = null;
//
//      
//        Date inizio = null, fine = null, tmpOraFine = null;
//        inizio = p.getInizio();
//        fine = p.getFine();
//
//        if (fine != null) {
//            if (p.getTariffa() != null && Boolean.TRUE.equals(p.getTariffa().getOraRientroAttiva()) && p.getTariffa().getOraRientro() != null) {
//                tmpOraFine = p.getTariffa().getOraRientro();
//            } else {
//                tmpOraFine = (Date) fine;
//            }
//
//            fine = FormattedDate.createTimestamp((Date) fine, tmpOraFine);
//        }
//
//
//        if (Boolean.TRUE.equals(p.getCommissione().getPrepagato()) && p.getCommissione().getGiorniVoucher() != null) {
//            List <RigaDocumentoFiscale>listaPrepagato = null;
//            try {
//                
//                fatturazione = FatturazioneFactory.PREPAGATO_FACTORY.getFatturazione(sx, p.getTariffa(), p.getInizio(), fine, p.getCommissione().getGiorniVoucher());
//                listaPrepagato = fatturazione.calcolaRigheProssimaFattura(sx);
//            } catch (FatturaVuotaException fvex) {
//                log.error("JDialogUpdateAmount.calcolaTotaleNoleggioPPay - Prenotazione - Errore per fattura vuota");
//                fvex.printStackTrace();
//                listaPrepagato = new ArrayList();
//            }
//
//            if (listaPrepagato != null && !listaPrepagato.isEmpty()) {
//                Double[] importi = FatturaUtils.calcolaImportiRigheFattura(listaPrepagato);
//                p.setNoleggioPPay(importi[2]);
//
//
//                p.setNoleggioPPayNoVat(importi[3]);
//
//
//                if (fatturazione != null) {
//                    listaPrepagato = fatturazione.calcolaRigheFattura(sx);
//                }
//
//                for (RigaDocumentoFiscale riga : listaPrepagato) {
//                    RigaPrenotazionePPay newRow = new RigaPrenotazionePPay(riga, p);
//
//                    p.getPrenotazioneRighePPay().add(newRow);
//
//                }
//
//            }
//        }
    }

    public static List<MROldRigaDocumentoFiscale> leggiRigheDocumentiFiscaliEmessiEA(Session sx, MROldContrattoNoleggio contrattoNoleggio) {
        return sx.createQuery(
                "select r from MROldRigaDocumentoFiscale r "
                        + "where "
                        + "r.fattura.discriminator = :TipoDocumento "
                        + "and r.fattura.contratto = :contratto "
                        + "and r.fattura.prepagato = :false ").
                setParameter("contratto", contrattoNoleggio).
                setParameter("false", Boolean.FALSE).
                setParameter("TipoDocumento", "Fattura").
                list();
    }

    public static List<MROldDocumentoFiscale> findAccontoInvoicesForSelectedSaldoEA(Session sx, MROldDocumentoFiscale doc) {

        List<MROldDocumentoFiscale> resultAccontoInvoices = new ArrayList<MROldDocumentoFiscale>();
        MROldPartita partitaSaldo = ContabUtils.leggiPartita(sx, doc);
        if (partitaSaldo != null) {
            List<MROldPartita> partiteAcconto = it.aessepi.myrentcs.utils.ContabUtils.leggiPartiteAcconto(sx, partitaSaldo);
            for (MROldPartita p : partiteAcconto) {
                resultAccontoInvoices.add(p.getFattura());
            }
        }

        return resultAccontoInvoices;
    }

    public static Object[] stampaCastellettoEA(Session sx, MROldDocumentoFiscale doc) {
        Object[] result = null;
        if (doc.getTipoDocumento().equals(MROldDocumentoFiscale.FT)) {
            if (doc.getTotaleAcconti() > 0.0) {
                List<MROldRigaDocumentoFiscale> righeDiAcconto = findRowsAccontiForSelectedSaldoEA(sx, doc);
                result = sottrazioneAccontiStampaEaCastellettoRighe(doc.getFatturaRighe(), righeDiAcconto);
            } else {
                result = creaCastellettoIva(doc.getFatturaRighe());
            }
        } else {
            result = creaCastellettoIva(doc.getFatturaRighe());
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

    public static Object[] sottrazioneAccontiStampaEaCastellettoRighe(List righeDiSaldo, List righeDiAcconto) {
        ArrayList<CastellettoIva> imposteSaldoArray = new ArrayList();
        ArrayList<CastellettoIva> imposteAccontoArray = new ArrayList();
        Object[] imposteSaldo = creaCastellettoIva(righeDiSaldo);
        for (Object o : imposteSaldo) {
            if (o instanceof CastellettoIva) {
                imposteSaldoArray.add((CastellettoIva) o);
            }
        }
        Object[] imposteAcconto = creaCastellettoIva(righeDiAcconto);
        for (Object o : imposteAcconto) {
            if (o instanceof CastellettoIva) {
                imposteAccontoArray.add((CastellettoIva) o);
            }
        }

        return sottrazioneAccontiStampaEA(imposteSaldoArray, imposteAccontoArray);
    }

    public static Object[] sottrazioneAccontiStampaEA(List<CastellettoIva> imposteSaldo, List<CastellettoIva> imposteAcconto) {

//        Object[] imposteTrovate = null;
        List<CastellettoIva> imposteTrovateArray = new ArrayList<CastellettoIva>();
        for (int s = 0; s < imposteSaldo.size(); s++) {
//            boolean accontoFound = false;
            CastellettoIva focusCastelletto = new CastellettoIva(imposteSaldo.get(s).getCodiceIva());
//            for (int a = 0; a < imposteAcconto.size(); a++) {
//                if (imposteSaldo.get(s).getImponibile() >= imposteAcconto.get(a).getImponibile()
//                        && imposteSaldo.get(s).getCodiceIva().equals(imposteAcconto.get(a).getCodiceIva())) {
//
//                    focusCastelletto.setImponibile(imposteSaldo.get(s).getImponibile() - imposteAcconto.get(a).getImponibile());
//                    focusCastelletto.setImposta(MathUtils.roundDouble(focusCastelletto.getImponibile() * focusCastelletto.getCodiceIva().getAliquota()));
//                    accontoFound = true;
//                    imposteTrovateArray.add(focusCastelletto);
//                } else if (imposteSaldo.get(s).getImponibile() < imposteAcconto.get(a).getImponibile()
//                        && imposteSaldo.get(s).getCodiceIva().equals(imposteAcconto.get(a).getCodiceIva())) {
//                    accontoFound = true;
//                    focusCastelletto.setImponibile(0.0);
//                    focusCastelletto.setImposta(0.0);
//                    imposteTrovateArray.add(focusCastelletto);
//                }
//
//            }
//            if (!accontoFound) {
            focusCastelletto.setImponibile(imposteSaldo.get(s).getImponibile());
            focusCastelletto.setImposta(MathUtils.roundDouble(focusCastelletto.getImponibile() * focusCastelletto.getCodiceIva().getAliquota()));
            imposteTrovateArray.add(focusCastelletto);
//            }

        }
        return imposteTrovateArray.toArray();
    }

    public static int numeroRigheFatturaStampaEA(Session sx, MROldDocumentoFiscale myFattura){

        int response = 0;
        if (myFattura != null && myFattura.getFatturaRighe() != null && myFattura.getFatturaRighe().size() > 0) {
            List<MROldRigaDocumentoFiscale> righeFatturaPerStampa = new ArrayList<MROldRigaDocumentoFiscale>();

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
            response = righeFatturaPerStampa.size();

        }
        return response-1;
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
            MROldFonteCommissione fonte = contratto.getCommissione().getFonteCommissione();
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
        result = mergeRowsByVATcode(findRowsAccontiForSelectedSaldoEA(sx, doc));
        for (MROldRigaDocumentoFiscale r : result) {
            r.setDescrizione("Acconto - " + r.getCodiceIva().toString());
            r.setTotaleRiga(-r.getTotaleRiga());
            r.setTotaleImponibileRiga(-r.getTotaleImponibileRiga());
            r.setTotaleIvaRiga(-r.getTotaleIvaRiga());
        }

        return result;
    }

    public static List<MROldRigaDocumentoFiscale> mergeRowsByVATcode(List<MROldRigaDocumentoFiscale> rows) {
        List<MROldRigaDocumentoFiscale> result = new ArrayList<MROldRigaDocumentoFiscale>();
        Map<MROldCodiciIva, MROldRigaDocumentoFiscale> totalRowsByVAT = new HashMap<MROldCodiciIva, MROldRigaDocumentoFiscale>();
        if (rows != null) {
            for (MROldRigaDocumentoFiscale r : rows) {
                if (totalRowsByVAT.containsKey(r.getCodiceIva())) {
                    MROldRigaDocumentoFiscale rigaInMap = totalRowsByVAT.get(r.getCodiceIva());
                    rigaInMap.setTotaleRiga(rigaInMap.getTotaleRiga() + r.getTotaleRiga());
                    rigaInMap.setTotaleImponibileRiga(rigaInMap.getTotaleImponibileRiga() + r.getTotaleImponibileRiga());
                    rigaInMap.setTotaleIvaRiga(rigaInMap.getTotaleIvaRiga() + r.getTotaleIvaRiga());
                } else {
                    MROldRigaDocumentoFiscale newRiga = new MROldRigaDocumentoFiscale();
                    newRiga.setTotaleRiga(r.getTotaleRiga());
                    newRiga.setTotaleImponibileRiga(r.getTotaleImponibileRiga());
                    newRiga.setTotaleIvaRiga(r.getTotaleIvaRiga());
                    newRiga.setCodiceIva(r.getCodiceIva());
//                    r.setPrezzoUnitario(null);
//                    r.setUnitaMisura(null);
//                    r.setCodiceSottoconto(null);
//                    r.setQuantita(null);
//                    r.setScontoFisso(null);
//                    r.setDescrizione("");
                    totalRowsByVAT.put(r.getCodiceIva(), newRiga);
                }

            }
        }
        for (MROldRigaDocumentoFiscale r : totalRowsByVAT.values()) {
            result.add(r);
        }
        return result;
    }

    public static String getIbanFromCustomer(MROldDocumentoFiscale doc){
        String response = "";
        if(doc != null && doc.getCliente() != null){
            if(doc.getCliente().getCliente()){
                MROldClienti c = (MROldClienti) doc.getCliente();
                if(c.getCoordinateBancarie() != null && c.getCoordinateBancarie().size() >0){
                    MROldCoordinateBancarie focusCoordinate = (MROldCoordinateBancarie) c.getCoordinateBancarie().iterator().next();
                    response = focusCoordinate.getIban();
                }
            }


        }
        return response;
    }

    public static String getCodiceContratto(MROldDocumentoFiscale documento){
        MROldContrattoNoleggio focusContrattoNoleggio = (MROldContrattoNoleggio) documento.getContratto();

        MROldFonteCommissione focusFonte = (MROldFonteCommissione) focusContrattoNoleggio.getCommissione().getFonteCommissione();

        String rentalType = findRentalType(focusContrattoNoleggio, documento);

        String numeroContratto = "";
        if(focusContrattoNoleggio != null){
            if (rentalType.equals("Z")) {
                numeroContratto = "4500910C";
            } else if (focusContrattoNoleggio.getCommissione() != null
                    && Boolean.TRUE.equals(rentalType.equals("S") || rentalType.equals("C"))
//                                && focusFonte != null
//                                && focusFonte.getExportCode() != null
                    && !Boolean.TRUE.equals(documento.getPrepagato())) {

                numeroContratto = "4501013C";

            } else if (focusContrattoNoleggio.getCommissione() != null
                    && Boolean.TRUE.equals(rentalType.equals("S") || rentalType.equals("C"))
//                                && focusFonte != null
//                                && focusFonte.getExportCode() != null
                    && Boolean.TRUE.equals(documento.getPrepagato())) {

//                            neededBpforNsa = (BusinessPartner) DatabaseUtils.refreshPersistentInstanceWithSx(sx, BusinessPartner.class, focusFonte.getCliente());
                if(focusFonte != null){
                    if(focusFonte.getExportCode() != null
                            &&  focusFonte.getExportCode().length() == 7
                            && !focusFonte.getExportCode().equals("")
                            && !focusFonte.getExportCode().equals("null")){
                        numeroContratto = focusFonte.getExportCode() + "C";
                    }
                    else{
                        numeroContratto = "Inserire codice esportazione corretto per la fonte "+ focusFonte.getRagioneSociale();
                    }

                }
                else{
                    System.out.println("Nessuna fonte associata per questo contratto !");
                }

            }
            else if(focusFonte != null ){ //contratti KPRE

                if(focusFonte.getCodice() != null){
                    numeroContratto = focusFonte.getCodice();
                }
                else if(focusFonte.getCliente() != null){
                    if(focusFonte.getCliente().getRagioneSociale() != null){
                        numeroContratto = focusFonte.getCliente().getRagioneSociale();
                    }
                    else if(focusFonte.getCliente().getPartitaIva() != null){
                        numeroContratto = focusFonte.getCliente().getPartitaIva();
                    }
                    else if (focusFonte.getCliente().getCognome() != null){
                        numeroContratto = focusFonte.getCliente().getCognome();
                    }

                }
            }

        }

        return numeroContratto;

    }

    public static String findRentalType(MROldContrattoNoleggio focusContratto, MROldDocumentoFiscale doc) {
        String rentalType = "";
        if (focusContratto != null) {
            try {
                rentalType = focusContratto.getCommissione().getFonteCommissione()
                        .getRentalType().getDescription();
            } catch (NullPointerException e) {
                System.out.println("rentalType not found");
            }

        }
        else if (doc != null){ //in this case we are sure that is not a Z agreement beacuse there is no Summary billing for that type and their invoices have always Agreement associated.
            MROldBusinessPartner bp = (MROldBusinessPartner)doc.getCliente();
            if(bp != null){
                if(bp.getRagioneSociale() != null){
                    if(bp.getRagioneSociale().contains("EUROP ASSISTANCE")){
                        rentalType = "S";
                    }
                    else{
                        rentalType = "C";
                    }

                }
                if(bp.getPartitaIva() != null){
                    if(bp.getPartitaIva().equals("00776030157") || bp.getPartitaIva().equals("11989340150")){//EUROP ASSISTANCE
                        rentalType = "S";
                    }
                    else{
                        rentalType = "C";
                    }
                }

            }

        }
        return rentalType;
    }

    public static String getInvoiceIvaDescriptions(MROldDocumentoFiscale doc){
        String response = "";
        HashSet<String> description = new HashSet<String>();
        for(MROldRigaDocumentoFiscale r :(List<MROldRigaDocumentoFiscale>) doc.getFatturaRighe()){
            MROldCodiciIva focusIva = (MROldCodiciIva) r.getCodiceIva();
            description.add(focusIva.getDescrizione());
        }
        for(String s : description){
            response += s+"\n";
        }


        return response;
    }

}
