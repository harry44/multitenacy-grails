/*
 * IndirizzoUtils.java
 *
 * Created on 04 aprilie 2005, 11:02
 */
package it.myrent.ee.api.utils;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.FiscalCode;
import it.aessepi.utils.HibernateBridge;
import it.aessepi.utils.PartitaIva;

import java.text.DecimalFormat;

import it.aessepi.utils.beans.FormattedDate;
import it.aessepi.utils.beans.QueryData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.db.*;
import it.myrent.ee.db.MROldClienti;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author jamess
 */
public class AnagraficaUtils {

    /**
     * Creates a new instance of IndirizzoUtils
     */
    public AnagraficaUtils() {
    }

    private static Log log = LogFactory.getLog(MROldClienti.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");
    private static final String TABELLA_CONDUCENTI = "MROldConducenti";
    private static final String TABELLA_CLIENTI = "MROldClienti";
    private static final String TABELLA_FORNITORI = "MROldFornitori";

    public static String formatCap(Integer capNumerico) {
        String capAlfanumerico = null;
        if (capNumerico != null) {
            capAlfanumerico = new DecimalFormat("00000").format(capNumerico); //NOI18N
        }
        return capAlfanumerico;
    }

    public static Integer parseCap(String capAlfanumerico) {
        Integer capNumerico = null;
        if (capAlfanumerico != null) {
            try {
                capNumerico = new Integer(new DecimalFormat("00000").parse(capAlfanumerico).intValue()); //NOI18N
            } catch (ParseException pex) {
                log.debug("AnagraficaUtils: ParseException - Unable to parse cap: " + capAlfanumerico, pex); //NOI18N
                log.warn("AnagraficaUtils: ParseException - Unable to parse cap: " + capAlfanumerico); //NOI18N
            }
        }
        return capNumerico;
    }

    public static String parseSesso(Boolean sesso) {
        if (MROldClienti.SESSO_MASCHILE.equals(sesso)) {
            return "M";
        } else if (MROldClienti.SESSO_FEMMINILE.equals(sesso)) {
            return "F";
        } else {
            return null;
        }
    }

    public static boolean isItaliano(MROldComuni comune) {
        return comune != null && isItaliano(comune.getNazione());
    }

    public static boolean isItaliano(String nazione) {
        return "ITALIA".equals(nazione) || "ITALY".equals(nazione);
    }

    public static String getViaNumero(String via, String numero) {
        if (via != null && numero != null) {
            return MessageFormat.format("{0}, {1}", via, numero); //NOI18N
        }
        return via;
    }

    public static String getCapProvinciaCittaNazione(String cap, String provincia, String citta, String nazione) {
        if (cap != null && provincia != null && citta != null && nazione != null) {
            return MessageFormat.format("{0} {1} ({2}), {3}", cap, citta, provincia, nazione); //NOI18N
        }
        return null;
    }

    public static String getIndirizzoCompleto(String viaNumero, String capProvinciaCittaNazione) {
        if (viaNumero != null && capProvinciaCittaNazione != null) {
            return viaNumero + ", " + capProvinciaCittaNazione; //NOI18N
        } else if (viaNumero != null) {
            return viaNumero;
        }
        return capProvinciaCittaNazione;
    }

    public static String getRecapitiTelefonici(String telefono, String fax, String cellulare) {
        telefono = (telefono != null ? MessageFormat.format(bundle.getString("AnagraficaUtils.msgTel0"), telefono) : null);
        fax = (fax != null ? MessageFormat.format(bundle.getString("AnagraficaUtils.msgFax0"), fax) : null);
        cellulare = (cellulare != null ? MessageFormat.format(bundle.getString("AnagraficaUtils.msgCell0"), cellulare) : null);
        String recapitiTelefonici =
                (telefono != null ? telefono + "; " : "") + //NOI18N
                        (fax != null ? fax + "; " : "") + //NOI18N
                        (cellulare != null ? cellulare + "; " : ""); //NOI18N
        return recapitiTelefonici.length() > 0
                ? recapitiTelefonici.substring(0, recapitiTelefonici.lastIndexOf(";")) //NOI18N
                : null;
    }

    public static String getRecapitiTelefoniciSemplici(String telefono, String fax, String cellulare) {
        String recapitiTelefonici =
                (telefono != null ? telefono + "; " : new String()) + //NOI18N
                        (fax != null ? fax + "; " : new String()) + //NOI18N
                        (cellulare != null ? cellulare + "; " : new String()); //NOI18N
        return recapitiTelefonici.length() > 0
                ? recapitiTelefonici.substring(0, recapitiTelefonici.lastIndexOf(";")) //NOI18N
                : null; //NOI18N
    }

    public static String getPartitaIvaOppureCodiceFiscale(String codiceFiscale, String partitaIva) {
        if (partitaIva != null) {
            return MessageFormat.format(bundle.getString("AnagraficaUtils.msgPIVA0"), partitaIva);
        } else if (codiceFiscale != null) {
            return MessageFormat.format(bundle.getString("AnagraficaUtils.msgCF0"), codiceFiscale);
        }
        return null;
    }

    public static String getPartitaIvaOppureCodiceFiscaleSemplici(String codiceFiscale, String partitaIva) {
        if (partitaIva != null) {
            return partitaIva;
        }
        return codiceFiscale;
    }

    public String[] identificaCodiceFiscalePartitaIva(String codiceFiscale, String partitaIva) {
        String[] dati = new String[2];

        if (codiceFiscale != null) {
            codiceFiscale = codiceFiscale.trim();
            if (AnagraficaUtils.isCodiceFiscaleCorretto(codiceFiscale) ||
                    AnagraficaUtils.isPartitaIvaCorretta(codiceFiscale)) {
                dati[0] = codiceFiscale;
            }
        }

        //Cerchiamo una partita IVA valida.
        if (partitaIva != null && AnagraficaUtils.isPartitaIvaCorretta(partitaIva)) {
            dati[1] = partitaIva;
        }
        return dati;
    }

    public static String getPartitaIvaECodiceFiscale(String codiceFiscale, String partitaIva) {
        if (partitaIva != null && codiceFiscale != null) {
            return MessageFormat.format(bundle.getString("AnagraficaUtils.msgPIVA0CF1"), partitaIva, codiceFiscale);
        } else if (partitaIva != null) {
            return MessageFormat.format(bundle.getString("AnagraficaUtils.msgPIVA0"), partitaIva);
        } else if (codiceFiscale != null) {
            return MessageFormat.format(bundle.getString("AnagraficaUtils.msgCF0"), codiceFiscale);
        }
        return null;
    }

    public static String getCodice3StatoNascita(Session sx, MROldClienti c) {
        MROldNazioneISO nazione = NazioneISOUtils.searchNazioneByName(sx, c.getLuogoNascita());
        if (nazione == null) {
            if (c.getCodiceFiscale() != null && isCodiceFiscaleCorretto(c.getCodiceFiscale())) {
                if (c.getCodiceFiscale().toUpperCase().charAt(11) == 'Z') {
                    String[] luogo = CodiceFiscaleUtils.searchLuogoForCodice(sx, c.getCodiceFiscale().toUpperCase().substring(11, 15));
                    for (int i = 0; i < luogo.length && nazione == null; i++) {
                        nazione = NazioneISOUtils.searchNazioneByName(sx, luogo[i]);
                    }
                } else {
                    nazione = NazioneISOUtils.searchNazioneByISO3Code(sx, "ITA");
                }
            }
        }

        if (nazione != null) {
            return nazione.getCodice3();
        }
        return null;

    }

    public static String getIndirizzoEmail(String email) {
        if (email != null) {
            return MessageFormat.format(bundle.getString("AnagraficaUtils.msgEmail0"), email);
        }
        return null;
    }

    private static boolean isDocumentExpired(GregorianCalendar dataScadenzaDocumento, Date dataFineContratto) {
        GregorianCalendar dateToCompare = new GregorianCalendar();
        if (dataFineContratto != null) {
            dateToCompare.setTime(dataFineContratto);
        }
        return dateToCompare.after(dataScadenzaDocumento);
    }

    public static String oscuraCartaDiCredito(String carta) {
        if (carta != null) {
            char[] caratteri = carta.toCharArray();
            for (int i = 0; i < caratteri.length - 4; i++) {
                if (caratteri[i] != ' ') {
                    caratteri[i] = '*';
                }
            }
            return new String(caratteri);
        }
        return null;
    }

    /**
     * Extracts a substring of a string starting from beginning and ending at length.
     *
     * @param aString The string to extract from.
     * @param length  End index of the substring.
     * @return Returns a substring of aString. If <code>length</code> is greater than the length of the string,
     * the whole string is returned. If <code>length</code> is less than zero, an empty string is returned. If
     * <code>aString</code> is null, an empty string is returned.
     */
    public static String substring(String aString, int length) {
        if (aString != null && length >= 0) {
            return aString.substring(0, Math.min(length, aString.length()));
        } else {
            return ""; //NOI18N
        }
    }

    /*added by Saurabh */

    public static boolean verificaGaranzia(Garanzia garanzia, Date dataFineContratto) {
        if (garanzia != null && garanzia.isGaranziaCarta()) {
            return verificaCartaCredito(dataFineContratto, (GaranziaCarta) garanzia);
        }
        return true;
    }

    private static boolean verificaCartaCredito(Date dataFineContratto, GaranziaCarta garanziaCarta) {
        String message = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy"); //NOI18N
        int yearCard = 0, monthcard = 0;
        try {
            yearCard = garanziaCarta.getAnnoScadenza() != null ? garanziaCarta.getAnnoScadenza().intValue() : 3000;
            monthcard = garanziaCarta.getMeseScadenza() != null ? garanziaCarta.getMeseScadenza().intValue() - 1 : 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        GregorianCalendar gc = new GregorianCalendar(yearCard, monthcard, 1);
        gc.set(Calendar.DAY_OF_MONTH, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (isDocumentExpired(gc, null)) {
            message = MessageFormat.format(
                    bundle.getString("AnagraficaUtils.msgConfermaCartaScaduta0"), gc.getTime());
        }

        if (message == null) {
            gc = new GregorianCalendar(garanziaCarta.getAnnoScadenza() != null ? garanziaCarta.getAnnoScadenza().intValue() : 3000, garanziaCarta.getMeseScadenza() != null ? garanziaCarta.getMeseScadenza().intValue() - 1 : 1, 1);
            gc.set(Calendar.DAY_OF_MONTH, gc.getActualMaximum(Calendar.DAY_OF_MONTH));
            if (isDocumentExpired(gc, dataFineContratto)) {
                message = MessageFormat.format(
                        bundle.getString("AnagraficaUtils.msgConfermaCartaScadenza0"), gc.getTime());
            }
        }

        if (message != null) {


            //return Chiedi.conferma(parent, bundle.getString("AnagraficaUtils.msgCartaDiCredito"), message, bundle.getString("AnagraficaUtils.msgSi"), bundle.getString("AnagraficaUtils.msgAnnulla"));
        }
        return true;
    }

    public static boolean verificaDocumentoConducente(Session sx, Date dataFineContratto, MROldConducenti aConducente) {
        if (!Preferenze.getPermettiAnagraficheIncomplete(sx)) {
            if (aConducente != null && aConducente.getDataScadenza() != null) {
                String documento = aConducente.getDocumento() != null ? aConducente.getDocumento() : new String();
                Date dataScadenzaDocumento = aConducente.getDataScadenza();
                String message = null;

                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(dataScadenzaDocumento);
                if (isDocumentExpired(gc, null)) {
                    message = MessageFormat.format(
                            bundle.getString("AnagraficaUtils.msgConfermaConducente0Documento1Scaduto2"), aConducente.toString(), documento, dataScadenzaDocumento);
                }

                if (message == null) {
                    gc = new GregorianCalendar();
                    gc.setTime(dataScadenzaDocumento);
                    if (isDocumentExpired(gc, dataFineContratto)) {
                        message = MessageFormat.format(
                                bundle.getString("AnagraficaUtils.msgConfermaConducente0Documento1Scadenza2"), aConducente.toString(), documento, dataScadenzaDocumento);
                    }
                }
                if (message != null) {


                    //return Chiedi.conferma(parent, bundle.getString("AnagraficaUtils.msgDocumentoConducente"), message, bundle.getString("AnagraficaUtils.msgSi"), bundle.getString("AnagraficaUtils.msgAnnulla"));
                }
            }
        }
        return true;
    }

    ///


    public static String verificaAnagraficaCliente(MROldClienti c) {
        if (c == null) {
            return bundle.getString("AnagraficaUtils.msgClienteNonImpostato");
        }
        String datiMancanti = ""; //NOI18N
        if (Boolean.TRUE.equals(c.getPersonaFisica())) {
            if (c.getCognome() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgCognome");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getNome() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgNome");
                datiMancanti += "\n"; //NOI18N
            }
            if (Boolean.TRUE.equals(c.getDittaIndividuale()) && c.getRagioneSociale() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgRagioneSociale");
                datiMancanti += "\n"; //NOI18N
            }
        } else if (c.getRagioneSociale() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgRagioneSociale");
            datiMancanti += "\n"; //NOI18N
        }
        if (c.getVia() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgIndirizzo");
            datiMancanti += "\n"; //NOI18N
        }
        if (c.getNumero() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgNumeroCivico");
            datiMancanti += "\n"; //NOI18N
        }

        if (c.getCap() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgCAP");
            datiMancanti += "\n"; //NOI18N
        }
        if (c.getProvincia() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgProvincia");
            datiMancanti += "\n"; //NOI18N
        }
        if (c.getCitta() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgCitta");
            datiMancanti += "\n"; //NOI18N
        }
        if (c.getNazione() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgNazione");
            datiMancanti += "\n"; //NOI18N
        } else {
            if (Boolean.TRUE.equals(c.getPersonaFisica())) {
                if (c.getCodiceFiscale() == null) {
                    datiMancanti += bundle.getString("AnagraficaUtils.msgCodiceFiscale");
                    datiMancanti += "\n"; //NOI18N
                } else if (isItaliano(c.getNazione()) && !AnagraficaUtils.isCodiceFiscaleCorretto(c.getCodiceFiscale())) { //NOI18N
                    datiMancanti += bundle.getString("AnagraficaUtils.msgCodiceFiscaleNonFormalmenteCorretto");
                    datiMancanti += "\n"; //NOI18N
                }

                if (isItaliano(c.getNazione()) && Boolean.TRUE.equals(c.getDittaIndividuale())) {
                    if (c.getPartitaIva() == null) {
                        datiMancanti += bundle.getString("AnagraficaUtils.msgPartitaIVA");
                        datiMancanti += "\n"; //NOI18N
                    } else if (!AnagraficaUtils.isPartitaIvaCorretta(c.getPartitaIva())) { //NOI18N
                        datiMancanti += bundle.getString("AnagraficaUtils.msgPartitaIVANonFormalmenteCorretta");
                        datiMancanti += "\n"; //NOI18N
                    }
                }
            } else {
                if (c.getCodiceFiscale() == null) {
                    datiMancanti += bundle.getString("AnagraficaUtils.msgCodiceFiscale");
                    datiMancanti += "\n"; //NOI18N
                } else if (isItaliano(c.getNazione()) && !AnagraficaUtils.isPartitaIvaCorretta(c.getCodiceFiscale())) { //NOI18N
                    datiMancanti += bundle.getString("AnagraficaUtils.msgCodiceFiscaleNonFormalmenteCorretto");
                    datiMancanti += "\n"; //NOI18N
                }
                if (isItaliano(c.getNazione())) {
                    if (c.getPartitaIva() == null) {
                        datiMancanti += bundle.getString("AnagraficaUtils.msgPartitaIVA");
                        datiMancanti += "\n"; //NOI18N
                    } else if (!AnagraficaUtils.isPartitaIvaCorretta(c.getPartitaIva())) { //NOI18N
                        datiMancanti += bundle.getString("AnagraficaUtils.msgPartitaIVANonFormalmenteCorretta");
                        datiMancanti += "\n"; //NOI18N
                    }
                }
            }
        }

        if (datiMancanti.equals("")) { //NOI18N
            return null;
        }

        return datiMancanti;
    }

    public static boolean isCodiceFiscaleCorretto(String nazione, Boolean personaFisica, String codice) {
        if (isItaliano(nazione) && codice != null) { //NOI18N
            if (Boolean.TRUE.equals(personaFisica)) {
                return isCodiceFiscaleCorretto(codice);
            } else {
                return isPartitaIvaCorretta(codice);
            }
        }
        return true;
    }

    public static boolean isPartitaIvaCorretta(String nazione, String codice) {
        if (isItaliano(nazione) && codice != null) { //NOI18N
            return AnagraficaUtils.isPartitaIvaCorretta(codice);
        }
        return true;
    }

    public static String verificaAnagraficaVeicolo(MROldParcoVeicoli v) {
        if (v == null) {
            return bundle.getString("AnagraficaUtils.msgVeicoloNonImpostato");
        }
        String datiMancanti = ""; //NOI18N

        if (v.getKm() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgChilometri");
            datiMancanti += "\n"; //NOI18N
        }

        if (v.getCarburante() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgTipoCarburante");
            datiMancanti += "\n"; //NOI18N
        }

        if (v.getCapacitaSerbatoio() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgCapacitaSerbatoio");
            datiMancanti += "\n"; //NOI18N
        }

        if (v.getLivelloCombustibile() == null) {
            datiMancanti += bundle.getString("AnagraficaUtils.msgLivelloCombustibile");
            datiMancanti += "\n"; //NOI18N
        }

        if (datiMancanti.equals("")) { //NOI18N
            return null;
        }

        return datiMancanti;
    }

    public static String verificaAnagraficaConducente(Session sx, MROldConducenti c) {
        List<MROldSetting> settingListExpirObj=null;
        String disableInv=null;
        MROldSetting settingObj = null;
        try {
            settingListExpirObj = sx.createQuery("from MROldSetting s where s.key =:key").setParameter("key", "disable.customer.expiryDate").list();
        }catch(Exception e){
            settingListExpirObj=null;
        }
        if ((settingListExpirObj != null )&& (settingListExpirObj.size() > 0)) {
            settingObj = settingListExpirObj.get(0);
            if (settingObj != null) {
                disableInv = settingObj.getValue();
            }
        };
        if (!Preferenze.getPermettiAnagraficheIncomplete(sx)) {
            if (c == null) {
                return bundle.getString("AnagraficaUtils.msgConducenteNonImpostato");
            }

            String datiMancanti = ""; //NOI18N
            if (c.getNome() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgNome");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getCognome() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgCognome");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getVia() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgIndirizzo");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getNumero() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgNumeroCivico");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getCap() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgCAP");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getProvincia() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgProvincia");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getCitta() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgCitta");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getNazione() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgNazione");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getLuogoNascita() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgLuogoNascita");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getDataNascita() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgDataNascita");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getDocumento() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgTipoDocumento");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getNumeroDocumento() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgNumeroDocumento");
                datiMancanti += "\n"; //NOI18N
            }
            if (c.getDataRilascio() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgDataRilascio");
                datiMancanti += "\n"; //NOI18N
            }
            if(disableInv ==null || disableInv.equalsIgnoreCase("false") ) {
                if (c.getDataScadenza() == null) {
                    datiMancanti += bundle.getString("AnagraficaUtils.msgDataScadenza");
                    datiMancanti += "\n"; //NOI18N
                }
            }
            if (datiMancanti.equals("")) { //NOI18N
                return null;
            }
            String msg = bundle.getString("AnagraficaUtils.msgDriver") + datiMancanti;
            return msg;
        }
        return null;
    }

    /**
     * Verifica se l'eta' del guidatore rientra nei limiti specificati.
     *
     * @param dataNascita    Data di nascita, con i soli campi anno, mese, giorno
     * @param inizioNoleggio Data di inizio del noleggio in qualsiasi formato (anche timestamp)
     * @param anniMinimo     L'inizio del limite di eta'
     * @param anniMassimo    La fine del limite di eta'
     * @return <code>true>/code> se l'eta del conducente rientra nei limiti specificati, <code>false</code> se non rientra.<br>
     * Se almeno un parametro e' null, il valore di ritorno e' false.
     */
    public static boolean verificaEtaGuidatore(Date dataNascita, Date inizioNoleggio, Integer anniMinimo, Integer anniMassimo) {
        if (dataNascita == null || inizioNoleggio == null || anniMinimo == null || anniMassimo == null) {
            return false;
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(dataNascita.getTime());
        c.add(c.YEAR, anniMinimo.intValue());
        Date dataMinima = new Date(c.getTimeInMillis());
        c.add(c.YEAR, anniMassimo.intValue() - anniMinimo.intValue() + 1);
        Date dataMassima = new Date(c.getTimeInMillis());
        inizioNoleggio = FormattedDate.extractDate(inizioNoleggio);
        if (inizioNoleggio.getTime() >= dataMinima.getTime() && inizioNoleggio.getTime() < dataMassima.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCodiceFiscaleCorretto(String codiceFiscale) {
        boolean codiceCorretto = (codiceFiscale.length() == 16);
        if (codiceCorretto) {
            try {
                codiceCorretto = FiscalCode.perControllo(codiceFiscale.substring(0, 15)).equalsIgnoreCase(codiceFiscale.substring(15, 16));
            } catch (Exception ex) {
                codiceCorretto = false;
            }
        }
        return codiceCorretto;
    }

    public static boolean isPartitaIvaCorretta(String partitaIva) {
        return PartitaIva.controlloPartitaIva(partitaIva);
    }

    /**
     * Prova ad individuare il cognome e nome partendo dalla ragioneSociale ed un codice fiscale
     */
    public static String[] trovaCognomeNome(String codiceFiscale, String ragioneSociale) {
        if (codiceFiscale == null || codiceFiscale.length() < 16 || ragioneSociale == null) {
            return new String[]{ragioneSociale, null};
        }

        String[] parole = ragioneSociale.split(" "); //NOI18N
        String[] cognomeNome = new String[]{null, null, null};

        //Troviamo il cognome.
        for (int i = 0; i < parole.length; i++) {
            String parola = parole[i];
            String codiceFiscaleCognome = codiceFiscale.substring(0, 3);
            String verificaCodiceCognome = FiscalCode.perCognome(parola);
            if (verificaCodiceCognome.equalsIgnoreCase(codiceFiscaleCognome)) {
                cognomeNome[0] = parola;
                parole[i] = null;
                break;
            }
        }

        if (cognomeNome[0] == null) {
            //Se non troviamo il cognome mettiamo tutta la ragione sociale e finisce cosi'.
            cognomeNome[0] = ragioneSociale;
        } else {
            // Proviamo a cercare il nome.
            for (int i = 0; i < parole.length; i++) {
                String parola = parole[i];
                if (parola != null) {
                    String codiceFiscaleNome = codiceFiscale.substring(3, 6);
                    String verificaCodiceNome = FiscalCode.perNome(parola);
                    if (verificaCodiceNome.equalsIgnoreCase(codiceFiscaleNome)) {
                        cognomeNome[1] = parola;
                        parole[i] = null;
                        break;
                    }
                }
            }
        }
        return cognomeNome;
    }


    /**
     * Verifica l'unicita' di un record in base a piu' campi, della classe <code>className</code>.
     *
     * @param <code>mySession</code>       La session da usare per il collegamento al database.
     * @param <code>className</code>       Nome della classe per la quale bisogna verificare l'esistenza.
     * @param <code>fieldUnico</code>      campo dell'incrementale nella classe.
     * @param <code>fieldNames</code>      campi da verificare nella classe.
     * @param <code>parameterValues</code> valori con i quali comparare i campi <code>fieldNames</code>.
     * @param <code>objectId</code>        id dell'oggetto per il quale si verifica l'unicita'.
     * @return boolean true se unico, altrimenti false.
     */

    public static boolean verificaUnicita1(Session sx,  String fieldNames,  Integer objectId) throws HibernateException {
        String queryString = new String("select count(x) from  MROldCommissione  as x"); //NOI18N
//
                    queryString += " where lower(x.codiceVoucher)=lower(:field"+ ")"; //NOI18N
//
        if (objectId != null) {
            queryString += " and x.id <> :id"; //NOI18N
        }
        QueryData queryData = new QueryData(queryString);
        queryData.setCountQueryString(queryString);
//        for (int i = 0; i < fieldNames.length; i++) {
            queryData.addParameter("field" , fieldNames); //NOI18N
//        }
        if (objectId != null) {
            queryData.addParameter("id", objectId); //NOI18N
        }
        Integer count = queryData.count(sx);
        return (count == null || count == 0);
    }




    public static boolean verificaUnicita(Session sx, String className, String[] fieldNames, Object[] parameterValues, Integer objectId, boolean caseSensitive) throws HibernateException {
        String queryString = new String("select count(x) from " + className + " as x"); //NOI18N
        for (int i = 0; i < fieldNames.length; i++) {
            if (i == 0) {
                if (caseSensitive) {
                    queryString += " where x." + fieldNames[i] + " = :field" + i; //NOI18N
                } else {
                    queryString += " where lower(x." + fieldNames[i] + ") = lower(:field" + i + ")"; //NOI18N
                }
            } else {
                if (caseSensitive) {
                    queryString += " and x." + fieldNames[i] + " = :field" + i; //NOI18N
                } else {
                    queryString += " and lower(x." + fieldNames[i] + ") = lower(:field" + i + ")"; //NOI18N
                }
            }
        }
        if (objectId != null) {
            queryString += " and x.id <> :id"; //NOI18N
        }
        QueryData queryData = new QueryData(queryString);
        queryData.setCountQueryString(queryString);
        for (int i = 0; i < fieldNames.length; i++) {
            queryData.addParameter("field" + i, parameterValues[i]); //NOI18N
        }
        if (objectId != null) {
            queryData.addParameter("id", objectId); //NOI18N
        }
        Integer count = queryData.count(sx);
        return (count == null || count == 0);
    }

    /**
     * Verifica l'unicita' di un record in base a piu' campi, della classe <code>className</code>.
     *
     * @param <code>mySession</code>       La session da usare per il collegamento al database.
     * @param <code>className</code>       Nome della classe per la quale bisogna verificare l'esistenza.
     * @param <code>fieldUnico</code>      campo dell'incrementale nella classe.
     * @param <code>fieldNames</code>      campi da verificare nella classe.
     * @param <code>parameterValues</code> valori con i quali comparare i campi <code>fieldNames</code>.
     * @param <code>objectId</code>        id dell'oggetto per il quale si verifica l'unicita'.
     * @return boolean true se unico, altrimenti false.
     */
    public static boolean verificaUnicitaRegExp(Session sx, String className, String[] fieldNames, Object[] parameterValues, Integer objectId, boolean caseSensitive) throws HibernateException {
        String queryString = new String("select count(x) from " + className + " as x"); //NOI18N
        for (int i = 0; i < fieldNames.length; i++) {
            if (i == 0) {
                if (caseSensitive) {
                    queryString += " where x." + fieldNames[i] + " ~ :field" + i; //NOI18N
                } else {
                    queryString += " where x." + fieldNames[i] + " ~* :field" + i; //NOI18N
                }
            } else {
                if (caseSensitive) {
                    queryString += " and x." + fieldNames[i] + " ~ :field" + i; //NOI18N
                } else {
                    queryString += " and x." + fieldNames[i] + " ~* :field" + i; //NOI18N
                }
            }
        }
        if (objectId != null) {
            queryString += " and x.id <> :id"; //NOI18N
        }
        QueryData queryData = new QueryData(queryString);
        queryData.setCountQueryString(queryString);
        for (int i = 0; i < fieldNames.length; i++) {
            queryData.addParameter("field" + i, parameterValues[i]); //NOI18N
        }
        if (objectId != null) {
            queryData.addParameter("id", objectId); //NOI18N
        }
        Integer count = queryData.count(sx);
        return (count == null || count == 0);
    }

    public static String getCapProvinciaCitta(String cap, String provincia, String citta) {
        if (cap != null && provincia != null && citta != null) {
            return MessageFormat.format("{0} {1} ({2})", cap, citta, provincia); //NOI18N
        }
        return null;
    }

    public static String verificaAnagraficaBusinessPartner(Session sx, MROldBusinessPartner bp) {
        MROldClienti clienti=(MROldClienti) bp;
        String disableVatAndTax = null;
        String invoiceItalian = null;

        MROldSetting settingObj = null;
        List<MROldSetting> settingList;
        List<MROldSetting> settingListObj=null;
        try {
            settingList = sx.createQuery("from MROldSetting s where s.key =:key").setParameter("key", "disable.rental.taxAndVat").list();
            settingListObj = sx.createQuery("from MROldSetting s where s.key =:key").setParameter("key", "avviso_ditta_italiana").list();

        }catch (Exception e){
            settingList = null;
        }

        if (settingList != null && (settingList.size() > 0)) {
            settingObj = settingList.get(0);
            if (settingObj != null) {
                disableVatAndTax = settingObj.getValue();
            }
        };

        if ((settingListObj != null )&& (settingListObj.size() > 0)) {
            settingObj = settingListObj.get(0);
            if (settingObj != null) {
                invoiceItalian = settingObj.getValue();
            }
        };


        if (!Preferenze.getPermettiAnagraficheIncomplete(sx)) {
            if (bp == null) {
                return bundle.getString("AnagraficaUtils.msgClienteNonImpostato");
            }
            String datiMancanti = ""; //NOI18N
            if (Boolean.TRUE.equals(bp.getPersonaFisica())) {
                if (bp.getCognome() == null) {
                    datiMancanti += bundle.getString("AnagraficaUtils.msgCognome");
                    datiMancanti += "\n"; //NOI18N
                }
                if (bp.getNome() == null) {
                    datiMancanti += bundle.getString("AnagraficaUtils.msgNome");
                    datiMancanti += "\n"; //NOI18N
                }
                if (Boolean.TRUE.equals(bp.getDittaIndividuale()) && bp.getRagioneSociale() == null) {
                    datiMancanti += bundle.getString("AnagraficaUtils.msgRagioneSociale");
                    datiMancanti += "\n"; //NOI18N
                }
            } else if (bp.getRagioneSociale() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgRagioneSociale");
                datiMancanti += "\n"; //NOI18N
            }
            if (bp.getVia() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgIndirizzo");
                datiMancanti += "\n"; //NOI18N
            }
            if (bp.getNumero() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgNumeroCivico");
                datiMancanti += "\n"; //NOI18N
            }

            if (bp.getCap() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgCAP");
                datiMancanti += "\n"; //NOI18N
            }
            if (bp.getProvincia() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgProvincia");
                datiMancanti += "\n"; //NOI18N
            }
            if (bp.getCitta() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgCitta");
                datiMancanti += "\n"; //NOI18N
            }
            if (bp.getNazione() == null) {
                datiMancanti += bundle.getString("AnagraficaUtils.msgNazione");
                datiMancanti += "\n"; //NOI18N
            } else {
                if (Boolean.TRUE.equals(bp.getPersonaFisica())) {

                    if(disableVatAndTax == null ||  disableVatAndTax.equalsIgnoreCase("false")){
                        if (bp.getCodiceFiscale() == null) {
                            datiMancanti += bundle.getString("AnagraficaUtils.msgCodiceFiscale");
                            datiMancanti += "\n"; //NOI18N
                        } else if (isItaliano(bp.getNazione()) && !AnagraficaUtils.isCodiceFiscaleCorretto(bp.getCodiceFiscale())) { //NOI18N
                            datiMancanti += bundle.getString("AnagraficaUtils.msgCodiceFiscaleNonFormalmenteCorretto");
                            datiMancanti += "\n"; //NOI18N
                        }
                    }



//                    if (isItaliano(bp.getNazione()) && Boolean.TRUE.equals(bp.getDittaIndividuale())) {
//                        if(disableVatAndTax == null ||  disableVatAndTax.equalsIgnoreCase("false")){
//                            if (bp.getPartitaIva() == null) {
//                                datiMancanti += bundle.getString("AnagraficaUtils.msgPartitaIVA");
//                                datiMancanti += "\n"; //NOI18N
//                            } else if (!AnagraficaUtils.isPartitaIvaCorretta(bp.getPartitaIva())) { //NOI18N
//                                datiMancanti += bundle.getString("AnagraficaUtils.msgPartitaIVANonFormalmenteCorretta");
//                                datiMancanti += "\n"; //NOI18N
//                            }
//                        }
//
//                    }
                } else {
//                    if(disableVatAndTax == null ||  disableVatAndTax.equalsIgnoreCase("false")) {
//                        if (bp.getCodiceFiscale() == null) {
//                            datiMancanti += bundle.getString("AnagraficaUtils.msgCodiceFiscale");
//                            datiMancanti += "\n"; //NOI18N
//                        } else if (isItaliano(bp.getNazione()) && !AnagraficaUtils.isPartitaIvaCorretta(bp.getCodiceFiscale())) { //NOI18N
//                            datiMancanti += bundle.getString("AnagraficaUtils.msgCodiceFiscaleNonFormalmenteCorretto");
//                            datiMancanti += "\n"; //NOI18N
//                        }
//                    }
                    if (isItaliano(bp.getNazione())) {
                        if(disableVatAndTax == null ||  disableVatAndTax.equalsIgnoreCase("false")) {
                            if (bp.getPartitaIva() == null) {
                                datiMancanti += bundle.getString("AnagraficaUtils.msgPartitaIVA");
                                datiMancanti += "\n"; //NOI18N
                            } else if (!AnagraficaUtils.isPartitaIvaCorretta(bp.getPartitaIva())) { //NOI18N
                                datiMancanti += bundle.getString("AnagraficaUtils.msgPartitaIVANonFormalmenteCorretta");
                                datiMancanti += "\n"; //NOI18N
                            }
                        }
                    }
                }
            }
            if( invoiceItalian!=null && invoiceItalian.equalsIgnoreCase("true")){
                if (((isItaliano(bp.getNazione()) && bp.getRagioneSociale()!= null  )||(isItaliano(bp.getNazione()) &&  Boolean.TRUE.equals(bp.getDittaIndividuale())))&& clienti.geteInvoiceCode()== null ) {
                    datiMancanti += bundle.getString("AnagraficaUtils.msgInvoiceCode");
                    datiMancanti += "\n"; //NOI18N
                }
            }
            if (datiMancanti.equals("")) { //NOI18N
                return null;
            }
            String msg = bundle.getString("AnagraficaUtils.msgCustomer") + datiMancanti;
            return msg;
        }

        return null;
    }

    /**
     * costruisce la data di nascita partendo dal codice fiscale
     * @param codiceFiscale
     * @return la data di nascita
     */
    public static Date trovaDataNascitaDaCodiceFiscale(String codiceFiscale) {
        Date data = null;

        if (codiceFiscale == null || codiceFiscale.equals("") || !AnagraficaUtils.isCodiceFiscaleCorretto(codiceFiscale)) {
            return data;
        }

        try {
            //recupera l'anno
            String sAnno = codiceFiscale.substring(6, 8);
            String sMese = codiceFiscale.substring(8, 9);
            String sGiorno = codiceFiscale.substring(9, 11);

            if (sAnno.startsWith("0")) {
                sAnno = "20" + sAnno;
            } else {
                sAnno = "19" + sAnno;
            }

            Integer mese = null;
            if (sMese.toUpperCase().equals("A")) {
                mese = 0;
            } else if (sMese.toUpperCase().equals("B")) {
                mese = 1;
            } else if (sMese.toUpperCase().equals("C")) {
                mese = 2;
            } else if (sMese.toUpperCase().equals("D")) {
                mese = 3;
            } else if (sMese.toUpperCase().equals("E")) {
                mese = 4;
            } else if (sMese.toUpperCase().equals("H")) {
                mese = 5;
            } else if (sMese.toUpperCase().equals("L")) {
                mese = 6;
            } else if (sMese.toUpperCase().equals("M")) {
                mese = 7;
            } else if (sMese.toUpperCase().equals("P")) {
                mese = 8;
            } else if (sMese.toUpperCase().equals("R")) {
                mese = 9;
            } else if (sMese.toUpperCase().equals("S")) {
                mese = 10;
            } else if (sMese.toUpperCase().equals("T")) {
                mese = 11;
            }

            Integer giorno = new Integer(sGiorno);
            if (giorno - 40 > 0) { // vede se si tratta di una femmina
                giorno = giorno - 40;
            }

            Calendar cal = new GregorianCalendar(new Integer(sAnno).intValue(), mese, giorno);
            data = cal.getTime();

        } catch (Exception e) {
            log.error("AnagraficaUtils.trovaDataNascitaDaCodiceFiscale Error is: " + e.getMessage());
            return data;
        }

        return data;
    }

    /**
     * trova il comune di nascita partendo dal codice fiscale
     * @param codiceFiscale
     * @return il comune di nascita
     */
    public static String trovaComuneStatoEsteroDiNascitaDaCodiceFiscale(Session sx,String codiceFiscale) {
        String comune = null;

//        Session sx = null;
        try {
//            sx = HibernateBridge.startNewSession();

            if (codiceFiscale == null || codiceFiscale.equals("") || !AnagraficaUtils.isCodiceFiscaleCorretto(codiceFiscale)) {
                return comune;
            }

            String codiceComune = codiceFiscale.substring(11, 15);
            Query query = sx.createQuery("select c.luogo from MROldCodiceFiscale c where c.codice = :codice").setParameter("codice", codiceComune.toUpperCase());
            query.uniqueResult();
            List lista = query.list();

            if (lista != null && lista.size() == 1) {
                comune = lista.get(0).toString();
            }
        } catch (Exception ex) {
            log.error("AnagraficaUtils.trovaProvinciaDiNascitaDaCodiceFiscale Error is: " + ex.getMessage());
            return null;
        } finally {
            try {
                if (sx != null) {
                    //sx.close();
                }
            } catch (Exception exx) {
            }
        }

        return comune;
    }

    /**
     * prova a cercare la provincia di nacita dal codice fiscale
     * @param sx
     * @param codiceFiscale
     * @return
     */
    public static String trovaProvinciaDiNascitaDaCodiceFiscale(Session sx, String codiceFiscale) {
        String provincia = "EE";

        if (codiceFiscale == null || codiceFiscale.equals("") || !AnagraficaUtils.isCodiceFiscaleCorretto(codiceFiscale)) {
            return null;
        }

        String codiceComune = codiceFiscale.substring(11, 15);
//        Query query = sx.createQuery("select c.provincia from Comuni c where c.codicestatistico = :codice").setParameter("codice", codiceComune.toUpperCase());
//        query.uniqueResult();
//        List lista = query.list();
//
//        if (lista != null && lista.size() == 1) {
//            provincia = lista.get(0).toString();
//        }
try{
    Object obj = sx.createQuery("select cf.luogo from it.myrent.ee.db.MROldCodiceFiscale cf where cf.codice = :codice")
            .setParameter("codice", codiceComune.toUpperCase())
            .uniqueResult();
    if (obj != null) {
        List objList = sx.createQuery("select c.provincia from Comuni c where c.comune = :comune")
                .setParameter("comune", obj.toString().toUpperCase())
                .list();
        if (objList != null && !objList.isEmpty()) {
            provincia = objList.get(0).toString();
        }
    }
}catch(Exception e){
    e.printStackTrace();
}
        
        return provincia;
    }

    /**
     *
     * @param codiceFiscale
     * @return
     */
//    public static String trovaProvinciaDiNascitaDaCodiceFiscale(Session sx,String codiceFiscale) {
//        String provincia = "EE";
//
////        Session sx = null;
//
//        try {
////            sx = HibernateBridge.startNewSession();
//
//            if (codiceFiscale == null || codiceFiscale.equals("") || !AnagraficaUtils.isCodiceFiscaleCorretto(codiceFiscale)) {
//                return null;
//            }
//
//            String codiceComune = codiceFiscale.substring(11, 15);
//            Object o = sx.createQuery("select c.provincia from Comuni c where c.codicestatistico = :codice")
//                    .setParameter("codice", codiceComune.toUpperCase())
//                    .uniqueResult();
//
//
//            if (o != null) {
//                provincia = o.toString();
//            }
//
//        } catch (Exception ex) {
//            log.error("AnagraficaUtils.trovaProvinciaDiNascitaDaCodiceFiscale Error is: " + ex.getMessage());
//            return null;
//        } finally {
//            try {
//                if (sx != null) {
////                    sx.close();
//                }
//            } catch (Exception exx) {
//            }
//        }
//
//        return provincia;
//    }

    /**
     * prova a trovare il sesso dal codice fiscale. Di defaulr ritorna maschio (false)
     * @param codiceFiscale il codice fiscale
     * @return TRUE- Femmina FALSE - Maschio
     */
    public static Boolean trovaSessoDaCodiceFiscale(String codiceFiscale) {
        Boolean sesso = false;

        if (codiceFiscale == null) {
            return sesso;
        }

        if (!AnagraficaUtils.isCodiceFiscaleCorretto(codiceFiscale)) {
            return sesso;
        }

        //per determinare il sesso del codice fiscale dobbiamo vedere il giorno di nascita
        //in quanto per le donne il giorno di nascita viene incrementato di 40

        //recupera il giorno di nascita
        try {
            Integer nascita = Integer.parseInt(codiceFiscale.substring(9,11));
            if (nascita - 40 > 0) {
                sesso = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return sesso;
        }

        return sesso;
    }


    public static boolean verificaUnicitaCliente(Session sx,String codiceFiscale, String partitaIva, Boolean
            dittaIndividuale, Integer idCorrente,boolean showMessage) {
        return verificaUnicitaBusinessPartner(sx,codiceFiscale, partitaIva, dittaIndividuale, idCorrente, showMessage, TABELLA_CLIENTI);
    }


    private static boolean verificaUnicitaBusinessPartner(Session mySession,String codiceFiscale, String partitaIva,
                                                          Boolean dittaIndividuale, Integer idCorrente, boolean showMessage, String tabella) {

        if (Preferenze.getDoNotCheckDuplicateDriverAndCustomer(mySession)){
            return true;
        }
        if (codiceFiscale == null && partitaIva == null) {
            return true;
        }

        if (codiceFiscale != null && codiceFiscale.equals(CodiceFiscaleUtils.SPECIAL_STRANGER_FISCAL_CODE)) {
            return true;
        }

        boolean unico = true;
        try {
            QueryData queryData = new QueryData();

            StringBuffer queryString = new StringBuffer("select c.id from " + tabella + " c where "); //NOI18N
            queryString.append("("); //NOI18N

            if (Preferenze.getUnicitaCodiceFiscale(mySession)) {
                // Sia la partita IVA che il codice fiscale devono essere unici se presenti.
                if (partitaIva != null) {
                    queryString.append(" lower(c.partitaIva) = :partitaIva "); //NOI18N
                    queryData.addParameter("partitaIva", partitaIva.toLowerCase()); //NOI18N
                }
                if(codiceFiscale != null) {
                    queryString.append(partitaIva != null ? " or " : ""); //NOI18N
                    queryString.append(" lower(c.codiceFiscale) = :codiceFiscale ");
                    queryData.addParameter("codiceFiscale", codiceFiscale.toLowerCase()); //NOI18N
                }
            } else {
                // La partita IVA deve essere unica se presente
                // Il codice fiscale deve essere unico per tipologia di anagrafica,
                // ed insieme alla partita IVA.
                if (partitaIva != null) {
                    queryString.append(" (lower(c.partitaIva) = :partitaIva) "); //NOI18N
                    queryData.addParameter("partitaIva", partitaIva.toLowerCase()); //NOI18N
                    if (codiceFiscale != null) {
                        queryString.append(" or (lower(c.codiceFiscale) = :codiceFiscale and (c.partitaIva is not null or c.dittaIndividuale = :dittaIndividuale)) "); //NOI18N
                        queryData.addParameter("codiceFiscale", codiceFiscale.toLowerCase()); //NOI18N
                        queryData.addParameter("dittaIndividuale", dittaIndividuale); //NOI18N
                    }
                } else {
                    queryString.append(" (lower(c.codiceFiscale) = :codiceFiscale and (c.partitaIva is null or c.dittaIndividuale = :dittaIndividuale)) "); //NOI18N
                    queryData.addParameter("codiceFiscale", codiceFiscale.toLowerCase()); //NOI18N
                    queryData.addParameter("dittaIndividuale", dittaIndividuale); //NOI18N
                }
            }
            queryString.append(")"); //NOI18N
            if (idCorrente != null) {
                queryString.append(" and c.id <> :id"); //NOI18N
                queryData.addParameter("id", idCorrente); //NOI18N
            }
            queryData.setQueryString(queryString.toString());

            List results = queryData.query(mySession);
            if (results.size() > 0) {
                unico = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        if (!unico && showMessage) {
////            User aUser = (User) Parameters.getUser();
// rilassato questo vincolo, viene sempre chiesta conferma altrimenti non e' possibile inserire i codici fiscali per gli stranieri: 9999999999999
//
//            //if ( aUser.isAdministrator() || aUser.isResponsabileNazionale() ) {
//            unico = Chiedi.conferma(
//                    parent,
//                    bundle.getString("JSchedaClienti.duplicati.msgRecordDuplicato"),
//                    bundle.getString("JSchedaClienti.duplicati.titolo"),
//                    bundle.getString("JSchedaClienti.duplicati.si"),
//                    bundle.getString("JSchedaClienti.duplicati.no"));
////            } else {
////                JMessage2.showMessage(parent, bundle.getString("AnagraficaUtils.msgRecordDuplicato"), bundle.getString("AnagraficaUtils.msgRecordClienteDuplicatoDatabase"), 10, true);
////            }
//        }
        return unico;
    }

}

    
