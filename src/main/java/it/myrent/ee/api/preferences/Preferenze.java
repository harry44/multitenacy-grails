/*
 * Preferenze.java
 *
 * Created on 13 febbraio 2005, 11.20
 *
 */
package it.myrent.ee.api.preferences;

//import it.aessepi.myrentcs.utils.AbstractFatturazione;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.Parameters;
import it.myrent.ee.db.MROldSetting;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import java.text.ParseException;


/**
 *
 * @author jamess
 */
public class Preferenze {

    public static final String DATE_FORMAT_TIMESTAMP = "yyyy-MM-dd-HH:mm";
    public static final int HOUR_FROM_START_DEFAULT = 24;
    public static final String PROP_NOME_TEMA = "nome.tema"; //NOI18N
    public static final String PROP_CONTRATTO_STAMPA_CHIUSURA = "contratto.stampa_chiusura"; //NOI18N
    public static final String PROP_CALENDARIO_SEDE = "calendario.sede"; //NOI18N
    public static final String PROP_CALENDARIO_GRUPPO = "calendario.categoria"; //NOI18N
    public static final String PROP_CALENDARIO_RICORDA_SELEZIONE = "calendario.ricorda_selezione"; //NOI18N
    public static final String PROP_CONTRATTO_TIPO_VISUALIZZAZIONE = "contratto.tipo_visualizzazione"; //NOI18N    
    public static final String PROP_TIPO_STAMPANTE = "stampante.tipo"; //NOI18N
    public static final String PROP_STAMPA_PREDEF_SENZA_IMMAGINI = "stampa.predefinita.senza.immagini"; //NOI18N
    public static final String PROP_STAMPA_VIEWER_PDF_ESTERNO = "stampa.viewer.pdf.esterno"; //NOI18N
    public static final String PROP_STAMPA_PERCORSO_VIEWER_PDF = "stampa.percorso.viewer.pdf"; //NOI18N
    public static final String PROP_AVVISO_SCADENZA_ASSICURAZIONE = "assicurazione.avviso.scadenza"; //NOI18N
    public static final String PROP_AVVISO_SCADENZA_BOLLO = "bollo.avviso.scadenza"; //NOI18N
    public static final String PROP_GIORNI_AVVISO_SCADENZA_ASSICURAZIONE = "assicurazione.avviso.scadenza.giorni"; //NOI18N
    public static final String PROP_GIORNI_AVVISO_SCADENZA_BOLLO = "bollo.avviso.scadenza.giorni"; //NOI18N
    public static final String PROP_SHOW_CHANGELOG = "show.changelog"; //NOI18N
    public static final String PREFERENZE_FILENAME = "prefs.myrent"; //NOI18N
    public static final String PROP_CHANGELOG_VERSION = "changelog.version"; //NOI18N
    public static final String PROP_TABLE_BACKGROUND = "table.background"; //NOI18N
    public static final String PROP_TABLE_FOREGROUND = "table.foreground"; //NOI18N
    public static final String PROP_TABLE_SELECTION_BACKGROUND = "table.selection_background"; //NOI18N
    public static final String PROP_TABLE_SELECTION_FOREGROUND = "table.selection_foreground"; //NOI18N
    public static final String PROP_LISTINI_IMPORTI_GIORNALIERI = MROldSetting.LISTINI_IMPORTI_GIORNALIERI;
    public static final String PROP_CONTRATTO_DISABILITA_RIMBORSI = MROldSetting.CONTRATTO_DISABILITA_RIMBORSI;
    public static final String PROP_PREPAGATI_IMPORTI_NASCOSTI = MROldSetting.PREPAGATI_IMPORTI_NASCOSTI;
    public static final String PROP_PREPAGATI_FATTURE_RIEPILOGATIVE = MROldSetting.PREPAGATI_FATTURE_RIEPILOGATIVE; //NOI18N
    public static final String PROP_MAIL_SMTP_HOST = MROldSetting.MAIL_SMTP_HOST;
    public static final String PROP_MAIL_SMTP_AUTH = MROldSetting.MAIL_SMTP_AUTH;
    public static final String PROP_MAIL_SMTP_USER = MROldSetting.MAIL_SMTP_USER;
    public static final String PROP_MAIL_SMTP_PASS = MROldSetting.MAIL_SMTP_PASS;
    public static final String PROP_MAIL_FROM = MROldSetting.MAIL_FROM;
    public static final String PROP_CONTRATTO_FATTURAZIONE_MENSILE = MROldSetting.CONTRATTO_FATTURAZIONE_MENSILE;
    public static final String PROP_CONTRATTO_FORMA_PAGAMENTO_DEFAULT = MROldSetting.CONTRATTO_FORMA_PAGAMENTO_DEFAULT; //NOI18N

    public static final String VIRTUAL_POS_UNICREDIT = "UniCredit";

    public static final String VIRTUAL_POS_NEXI_PAYMENT = "Nexi";

    public static final String VIRTUAL_POS_NETS = "Nets";
    public static final String VIRTUAL_POS_INGENICO = "Ingenico";
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/preferences/Bundle");
    public static final String[] THEMES = new String[]{
            bundle.getString("JPanelPreferenze.msgPredefinita"),
            "Java", //NOI18N
            "Liquid", //NOI18N
            "Plastic", //NOI18N
            "Skin", //NOI18N
            "Tiny", //NOI18N
            "NimROD" //NOI18N
    };
    public static final String DEFAULT_THEME_NAME = bundle.getString("JPanelPreferenze.msgPredefinita");
    public static final String[] CLASSES = new String[]{
            "com.incors.plaf.kunststoff.KunststoffLookAndFeel", //NOI18N
            "javax.swing.plaf.metal.MetalLookAndFeel", //NOI18N
            "com.birosoft.liquid.LiquidLookAndFeel", //NOI18N
            "com.jgoodies.looks.plastic.Plastic3DLookAndFeel", //NOI18N
            "com.l2fprod.gui.plaf.skin.SkinLookAndFeel", //NOI18N
            "de.muntjak.tinylookandfeel.TinyLookAndFeel", //NOI18N
            "com.nilo.plaf.nimrod.NimRODLookAndFeel" //NOI18N
    };
    private static Preferenze preferenze;
    private static Log log = LogFactory.getLog(Preferenze.class);
    public static final Boolean TIPO_STAMPANTE_LOCALE = Boolean.FALSE;
    public static final Boolean TIPO_STAMPANTE_REMOTA = Boolean.TRUE;
    public static final Color DEFAULT_TABLE_BACKGROUND = new Color(239, 239, 251);
    public static final Color DEFAULT_TABLE_FOREGROUND = new Color(51, 51, 51);
    public static final Color DEFAULT_TABLE_SELECTION_BACKGROUND = new Color(204, 204, 255);
    public static final Color DEFAULT_TABLE_SELECTION_FOREGROUND = new Color(51, 51, 51);
    public static final Color DEFAULT_TABLE_GRID_COLOR = new Color(122, 138, 153);

    /** Creates a new instance of Preferenze */
    private Preferenze() {
    }

    private Preferenze(Properties p) {
        setNomeTema(p.getProperty(PROP_NOME_TEMA));
        setContrattoStampaChiusura(new Boolean(p.getProperty(PROP_CONTRATTO_STAMPA_CHIUSURA)));
        setCalendarioAutoGruppo(
                p.getProperty(PROP_CALENDARIO_GRUPPO) != null
                        ? new Integer(p.getProperty(PROP_CALENDARIO_GRUPPO))
                        : new Integer(-1));
        setCalendarioAutoSede(
                p.getProperty(PROP_CALENDARIO_SEDE) != null
                        ? new Integer(p.getProperty(PROP_CALENDARIO_SEDE))
                        : new Integer(0));
        setTipoVisualizzazioneContratto(
                p.getProperty(PROP_CONTRATTO_TIPO_VISUALIZZAZIONE) != null
                        ? new Integer(p.getProperty(PROP_CONTRATTO_TIPO_VISUALIZZAZIONE))
                        : new Integer(0));
        setCalendarioAutoRicordaSelezione(new Boolean(p.getProperty(PROP_CALENDARIO_RICORDA_SELEZIONE)));
        setTipoStampante(new Boolean(p.getProperty(PROP_TIPO_STAMPANTE)));
        setStampaPredefinitaSenzaImmagini(new Boolean(p.getProperty(PROP_STAMPA_PREDEF_SENZA_IMMAGINI)));
        setStampaViewerPDFEsterno(new Boolean(p.getProperty(PROP_STAMPA_VIEWER_PDF_ESTERNO)));
        setStampaPercorsoViewerPDF(p.getProperty(PROP_STAMPA_PERCORSO_VIEWER_PDF));
        setAvvisoScadenzaBollo(new Boolean(p.getProperty(PROP_AVVISO_SCADENZA_BOLLO)));
        setAvvisoScadenzaAssicurazione(new Boolean(p.getProperty(PROP_AVVISO_SCADENZA_ASSICURAZIONE)));
        setGiorniAvvisoScadenzaBollo(
                p.getProperty(PROP_GIORNI_AVVISO_SCADENZA_BOLLO) != null
                        ? new Integer(p.getProperty(PROP_GIORNI_AVVISO_SCADENZA_BOLLO))
                        : new Integer(15));
        setGiorniAvvisoScadenzaAssicurazione(
                p.getProperty(PROP_GIORNI_AVVISO_SCADENZA_ASSICURAZIONE) != null
                        ? new Integer(p.getProperty(PROP_GIORNI_AVVISO_SCADENZA_ASSICURAZIONE))
                        : new Integer(15));
        String changelog = p.getProperty(PROP_SHOW_CHANGELOG);
        setShowChangeLog(changelog == null ? Boolean.TRUE : new Boolean(p.getProperty(PROP_SHOW_CHANGELOG)));

        String version = p.getProperty(PROP_CHANGELOG_VERSION);
        setChangeLogVersion(version == null ? "0.0.0" : version); //NOI18N
        setMailSmtpHost(p.getProperty(PROP_MAIL_SMTP_HOST));
        setMailSmtpAuth(new Boolean(p.getProperty(PROP_MAIL_SMTP_AUTH)));
        setMailSmtpUser(p.getProperty(PROP_MAIL_SMTP_USER));
        setMailSmtpPass(p.getProperty(PROP_MAIL_SMTP_PASS));
        setMailFrom(p.getProperty(PROP_MAIL_FROM));
    }
    private String nomeTema;
    private Boolean tariffeIvaCompresa;
    private Boolean contrattoStampaChiusura;
    private Integer tipoVisualizzazioneContratto;
    private Integer calendarioAutoSede;
    private Integer calendarioAutoGruppo;
    private Boolean calendarioAutoRicordaSelezione;
    private Boolean tipoStampante;
    private Boolean stampaPredefinitaSenzaImmagini;
    private String stampaPercorsoViewerPDF;
    private Boolean stampaViewerPDFEsterno;
    private Boolean avvisoScadenzaBollo;
    private Boolean avvisoScadenzaAssicurazione;
    private Integer giorniAvvisoScadenzaBollo;
    private Integer giorniAvvisoScadenzaAssicurazione;
    private Boolean showChangeLog;
    private String changeLogVersion;
    private Color tableBackground;
    private Color tableForeground;
    private Color tableSelectionBackground;
    private Color tableSelectionForeground;
    private String mailSmtpHost;
    private Boolean mailSmtpAuth;
    private String mailSmtpUser;
    private String mailSmtpPass;
    private String mailFrom;
    private String mailName;

    //firma certa
    private String cartellaSpoolFirmaCerta;
    private String templateFirmaCertaContrattoBreveTermine;
    private String templateFirmaCertaInformativaPrivacy;
    private String templateFirmaCertaPreventivoBreveTermine;
    private String templateFirmaCertaPrenotazioni;
    private String templateFirmaCertaContrattoBreveTermineChiuso;
    private String invoiceEnumerationFromUserAffiliate;

    public static boolean loadPreferenze() {
        return loadPreferenze(preferenzeFilePath());
    }

    public static boolean savePreferenze() {
        return savePreferenze(preferenzeFilePath());
    }

    public static Integer themeIndexForName(String themeName) {
        if (themeName != null) {
            for (int i = 0; i < THEMES.length; i++) {
                if (themeName.equals(THEMES[i])) {
                    return new Integer(i);
                }
            }
            log.warn("Prefrenze : no theme found for name : " + themeName); //NOI18N
        }
        return null;
    }

    public static String themeNameForIndex(Integer themeIndex) {
        if (themeIndex != null && themeIndex.intValue() >= 0 && themeIndex.intValue() < THEMES.length) {
            return THEMES[themeIndex.intValue()];
        }
        log.warn("Preferenze : Invalid index. Could not retrieve theme name."); //NOI18N
        return null;
    }

    public String classNameForThemeIndex(Integer themeIndex) {
        if (themeIndex != null && themeIndex.intValue() >= 0 && themeIndex.intValue() < CLASSES.length) {
            return CLASSES[themeIndex.intValue()];
        }
        log.warn("Preferenze : Invalid index. Will return default class name."); //NOI18N
        return CLASSES[0];
    }

    public static void initPreferenze() {
        if (getPreferenze() == null && !loadPreferenze()) {
            log.debug("Preferenze : Creating default preference file."); //NOI18N
            setPreferenze(new Preferenze());
            getPreferenze().setNomeTema(THEMES[0]);
            Preferenze.savePreferenze();
        }
    }

    public static boolean loadPreferenze(String file) {
        boolean retValue = true;
        BufferedInputStream in = null;
        Properties p = new Properties();
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            p.load(in);
            in.close();
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("Preferenze : Exception encountered while loading preference file.", ex); //NOI18N
            } else {
                log.warn("Preferenze : Exception encountered while loading preference file : " + ex.getMessage()); //NOI18N
            }
            retValue = false;
        }
        setPreferenze(new Preferenze(p));
        return retValue;
    }

    public static boolean savePreferenze(String file) {
        boolean retValue = true;
        BufferedOutputStream out = null;
        Properties p = getPreferenze().toProperties();
        try {
            out = new BufferedOutputStream(new FileOutputStream(file));
            p.store(out, null);
            out.close();
        } catch (Exception ex) {
            if (log.isDebugEnabled()) {
                log.debug("Preferenze : Exception encountered while saving preference file.", ex); //NOI18N
            } else {
                log.warn("Preferenze : Exception encountered while saving preference file : " + ex.getMessage()); //NOI18N
            }
            retValue = false;
        }
        return retValue;
    }

    private Properties toProperties() {
        Properties p = new Properties();
        if (getNomeTema() == null) {
            setNomeTema(THEMES[0]);
        }

        if (getContrattoStampaChiusura() == null) {
            setContrattoStampaChiusura(Boolean.FALSE);
        }

        if (getCalendarioAutoGruppo() == null) {
            setCalendarioAutoGruppo(new Integer(-1));
        }

        if (getCalendarioAutoSede() == null) {
            setCalendarioAutoSede(new Integer(0));
        }

        if (getTipoVisualizzazioneContratto() == null) {
            setTipoVisualizzazioneContratto(new Integer(0));
        }

        if (getCalendarioAutoRicordaSelezione() == null) {
            setCalendarioAutoRicordaSelezione(Boolean.TRUE);
        }

        if (getTipoStampante() == null) {
            setTipoStampante(TIPO_STAMPANTE_LOCALE);
        }

        if (getStampaPredefinitaSenzaImmagini() == null) {
            setStampaPredefinitaSenzaImmagini(Boolean.FALSE);
        }

        if (getStampaViewerPDFEsterno() == null) {
            setStampaViewerPDFEsterno(Boolean.FALSE);
        }

        if (getAvvisoScadenzaBollo() == null) {
            setAvvisoScadenzaBollo(Boolean.TRUE);
        }

        if (getAvvisoScadenzaAssicurazione() == null) {
            setAvvisoScadenzaAssicurazione(Boolean.TRUE);
        }

        if (getGiorniAvvisoScadenzaBollo() == null) {
            setGiorniAvvisoScadenzaBollo(new Integer(15));
        }

        if (getGiorniAvvisoScadenzaAssicurazione() == null) {
            setGiorniAvvisoScadenzaAssicurazione(new Integer(15));
        }

        if (getShowChangeLog() == null) {
            setShowChangeLog(Boolean.TRUE);
        }

        if (getChangeLogVersion() == null) {
            setChangeLogVersion(Parameters.getVersion());
        }


        p.setProperty(PROP_NOME_TEMA, getNomeTema());
        p.setProperty(PROP_CONTRATTO_STAMPA_CHIUSURA, getContrattoStampaChiusura().toString());
        p.setProperty(PROP_CALENDARIO_GRUPPO, getCalendarioAutoGruppo().toString());
        p.setProperty(PROP_CALENDARIO_SEDE, getCalendarioAutoSede().toString());
        p.setProperty(PROP_CONTRATTO_TIPO_VISUALIZZAZIONE, getTipoVisualizzazioneContratto().toString());
        p.setProperty(PROP_CALENDARIO_RICORDA_SELEZIONE, getCalendarioAutoRicordaSelezione().toString());
        p.setProperty(PROP_TIPO_STAMPANTE, getTipoStampante().toString());
        p.setProperty(PROP_STAMPA_PREDEF_SENZA_IMMAGINI, getStampaPredefinitaSenzaImmagini().toString());
        p.setProperty(PROP_STAMPA_VIEWER_PDF_ESTERNO, getStampaViewerPDFEsterno().toString());
        p.setProperty(PROP_AVVISO_SCADENZA_BOLLO, getAvvisoScadenzaBollo().toString());
        p.setProperty(PROP_AVVISO_SCADENZA_ASSICURAZIONE, getAvvisoScadenzaAssicurazione().toString());
        p.setProperty(PROP_GIORNI_AVVISO_SCADENZA_BOLLO, getGiorniAvvisoScadenzaBollo().toString());
        p.setProperty(PROP_GIORNI_AVVISO_SCADENZA_ASSICURAZIONE, getGiorniAvvisoScadenzaAssicurazione().toString());
        p.setProperty(PROP_SHOW_CHANGELOG, getShowChangeLog().toString());
        p.setProperty(PROP_CHANGELOG_VERSION, getChangeLogVersion());

        if (getStampaPercorsoViewerPDF() == null) {
            p.remove(PROP_STAMPA_PERCORSO_VIEWER_PDF);
        } else {
            p.setProperty(PROP_STAMPA_PERCORSO_VIEWER_PDF, getStampaPercorsoViewerPDF());
        }

        if (getMailSmtpHost() == null) {
            p.remove(PROP_MAIL_SMTP_HOST);
        } else {
            p.setProperty(PROP_MAIL_SMTP_HOST, getMailSmtpHost());
        }

        if (getMailSmtpAuth() == null) {
            p.remove(PROP_MAIL_SMTP_AUTH);
        } else {
            p.setProperty(PROP_MAIL_SMTP_AUTH, getMailSmtpAuth().toString());
        }

        if (getMailSmtpUser() == null) {
            p.remove(PROP_MAIL_SMTP_USER);
        } else {
            p.setProperty(PROP_MAIL_SMTP_USER, getMailSmtpUser());
        }

        if (getMailSmtpPass() == null) {
            p.remove(PROP_MAIL_SMTP_PASS);
        } else {
            p.setProperty(PROP_MAIL_SMTP_PASS, getMailSmtpPass());
        }

        if (getMailFrom() == null) {
            p.remove(PROP_MAIL_FROM);
        } else {
            p.setProperty(PROP_MAIL_FROM, getMailFrom());
        }

        return p;
    }

    public static Preferenze getPreferenze() {
        return preferenze;
    }

    public static void setPreferenze(Preferenze aPreferenze) {
        Preferenze.preferenze = aPreferenze;
    }

    private static String preferenzeFilePath() {
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) { //NOI18N
            return System.getProperty("user.home") + File.separator + "." + PREFERENZE_FILENAME; //NOI18N
        } else {
            return System.getProperty("user.home") + File.separator + PREFERENZE_FILENAME; //NOI18N
        }

    }

    public String getNomeTema() {
        return nomeTema;
    }

    public void setNomeTema(String nomeTema) {
        this.nomeTema = nomeTema;
    }

    public Boolean getContrattoStampaChiusura() {
        return contrattoStampaChiusura;
    }

    public void setContrattoStampaChiusura(Boolean contrattoStampaChiusura) {
        this.contrattoStampaChiusura = contrattoStampaChiusura;
    }

    public Integer getTipoVisualizzazioneContratto() {
        return tipoVisualizzazioneContratto;
    }

    public void setTipoVisualizzazioneContratto(Integer tipoVisualizzazioneContratto) {
        this.tipoVisualizzazioneContratto = tipoVisualizzazioneContratto;
    }

    public Integer getCalendarioAutoSede() {
        return calendarioAutoSede;
    }

    public void setCalendarioAutoSede(Integer calendarioAutoSede) {
        this.calendarioAutoSede = calendarioAutoSede;
    }

    public Integer getCalendarioAutoGruppo() {
        return calendarioAutoGruppo;
    }

    public void setCalendarioAutoGruppo(Integer aCalendarioAutoGruppo) {
        this.calendarioAutoGruppo = aCalendarioAutoGruppo;
    }

    public Boolean getCalendarioAutoRicordaSelezione() {
        return calendarioAutoRicordaSelezione;
    }

    public void setCalendarioAutoRicordaSelezione(Boolean calendarioAutoRicordaSelezione) {
        this.calendarioAutoRicordaSelezione = calendarioAutoRicordaSelezione;
    }

    public static Boolean getOperatoriSoloTariffeAbilitate(Session sx) {
        log.debug("Preferenze: Attenzione! Requesting database value operatoriSoloTariffeAbilitate."); //NOI18N
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.OPERATORI_SOLO_TARIFFE_ABILITATE));
    }

    public static void setOperatoriSoloTariffeAbilitate(Session sx, Boolean operatoriSoloTariffeAbilitate) {
        saveSetting(sx, MROldSetting.OPERATORI_SOLO_TARIFFE_ABILITATE, String.valueOf(operatoriSoloTariffeAbilitate));
    }

    public static Boolean getPrenotazioneSedeUscitaModificabile(Session sx) {
        log.debug("Preferenze: Attenzione! Requesting database value prenotazioneSedeUscitaModificabile"); //NOI18N
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.PRENOTAZIONE_SEDE_USCITA_MODIFICABILE));
    }

    public static void setPrenotazioneSedeUscitaModificabile(Session sx, Boolean prenotazioneSedeUscitaModificabile) {
        saveSetting(sx, MROldSetting.PRENOTAZIONE_SEDE_USCITA_MODIFICABILE, String.valueOf(prenotazioneSedeUscitaModificabile));
    }

    public static Boolean getOptionalsAbilitaTempoExtra(Session sx) {
        log.debug("Preferenze: Attenzione! Requesting database value optionalsAbilitaTempoExtra"); // NOI18N
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.OPTIONALS_ABILITA_TEMPO_EXTRA));
    }

    public static void setOptionalsAbilitaTempoExtra(Session sx, Boolean optionalsAbilitaTempoExtra) {
        saveSetting(sx, MROldSetting.OPTIONALS_ABILITA_TEMPO_EXTRA, String.valueOf(optionalsAbilitaTempoExtra));
    }

    public static Boolean getAvvisoScadenzaContrattoVeicolo(Session sx) {
        log.debug("Preferenze: Attenzione! Requesting database value avvisoScadenzaContrattoVeicolo."); //NOI18N        
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.VEICOLO_AVVISO_SCADENZA_CONTRATTO));
    }

    public static void setAvvisoScadenzaContrattoVeicolo(Session sx, Boolean avvisoScadenzaContrattoVeicolo) {
        saveSetting(sx, MROldSetting.VEICOLO_AVVISO_SCADENZA_CONTRATTO, String.valueOf(avvisoScadenzaContrattoVeicolo));
    }

    public static Integer getGiorniAvvisoScadenzaContrattoVeicolo(Session sx) throws HibernateException {
        String aGiorni = getSettingValue(sx, MROldSetting.VEICOLO_GIORNI_AVVISO_SCADENZA_CONTRATTO);
        if (aGiorni == null) {
            return new Integer(20);
        } else {
            return new Integer(aGiorni);
        }
    }

    public static void setGiorniAvvisoScadenzaContrattoVeicolo(Session sx, Integer giorni) {
        saveSetting(sx, MROldSetting.VEICOLO_GIORNI_AVVISO_SCADENZA_CONTRATTO, String.valueOf(giorni));
    }

    public static Integer getGiorniSemaforoScadenzaContrattoVeicolo(Session sx) throws HibernateException {
        String aGiorni = getSettingValue(sx, MROldSetting.VEICOLO_GIORNI_SEMAFORO_SCADENZA_CONTRATTO);
        if (aGiorni == null) {
            return new Integer(3);
        } else {
            return new Integer(aGiorni);
        }
    }

    public static void setGiorniSemaforoScadenzaContrattoVeicolo(Session sx, Integer aGiorniSemaforoScadenzaContrattoVeicolo) {
        saveSetting(sx, MROldSetting.VEICOLO_GIORNI_SEMAFORO_SCADENZA_CONTRATTO, aGiorniSemaforoScadenzaContrattoVeicolo.toString());
    }

    public static void setListiniImportiGiornalieri(Session sx, Boolean listiniImportiGiornalieri) {
        saveSetting(sx, MROldSetting.LISTINI_IMPORTI_GIORNALIERI, String.valueOf(listiniImportiGiornalieri));
    }

    public static Boolean getListiniImportiGiornalieri(Session sx) {
        log.debug("Preferenze: Attenzione! Requesting database value listiniImportiGiornalieri."); //NOI18N
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.LISTINI_IMPORTI_GIORNALIERI));
    }

    public static void setContrattoDisabilitaRimborsi(Session sx, Boolean contrattoDisabilitaRimborsi) {
        saveSetting(sx, MROldSetting.CONTRATTO_DISABILITA_RIMBORSI, String.valueOf(contrattoDisabilitaRimborsi));
    }

    public static Boolean getContrattoDisabilitaRimborsi(Session sx) {
        log.debug("Preferenze: Attenzione! Requesting database value contrattoDisabilitaRimborsi."); //NOI18N
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.CONTRATTO_DISABILITA_RIMBORSI));
    }

    public static void setContrattoAggiornaDataFineDaRientroEffettivo(Session sx, Boolean aggiornaDataFineDaRientroEffettivo) {
        saveSetting(sx, MROldSetting.CONTRATTO_AGGIORNA_DATA_FINE_DA_RIENTRO_EFFETTIVO, aggiornaDataFineDaRientroEffettivo.toString());
    }

    public static Boolean getContrattoAggiornaDataFineDaRientroEffettivo(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.CONTRATTO_AGGIORNA_DATA_FINE_DA_RIENTRO_EFFETTIVO));
    }

    public static void setContrattoDisabilitaVerificaSaldoChiusura(Session sx, Boolean contrattoDisabilitaVerificaSaldoChiusura) {
        saveSetting(sx, MROldSetting.CONTRATTO_DISABILITA_VERIFICA_SALDO_CHIUSURA, contrattoDisabilitaVerificaSaldoChiusura.toString());
    }

    public static Boolean getContrattoDisabilitaVerificaSaldoChiusura(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.CONTRATTO_DISABILITA_VERIFICA_SALDO_CHIUSURA));
    }

    public static void setPrepagatiImportiNascosti(Session sx, Boolean prepagatiImportiNascosti) {
        saveSetting(sx, MROldSetting.PREPAGATI_IMPORTI_NASCOSTI, String.valueOf(prepagatiImportiNascosti));
    }

    public static Boolean getPrepagatiImportiNascosti(Session sx) {
        log.debug("Preferenze: Attenzione! Requesting database value prepagatiImportiNascosti");
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.PREPAGATI_IMPORTI_NASCOSTI));
    }

    public static void setOptionalsAbilitaSconto(Session sx, Boolean optionalsAbilitaSconto) {
        saveSetting(sx, MROldSetting.OPTIONALS_ABILITA_SCONTO, optionalsAbilitaSconto.toString());
    }

    public static Boolean getOptionalsAbilitaSconto(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.OPTIONALS_ABILITA_SCONTO));
    }

    public static void setPrepagatiFattureRiepilogative(Session sx, Boolean prepagatiFattureRiepilogative) {
        saveSetting(sx, MROldSetting.PREPAGATI_FATTURE_RIEPILOGATIVE, String.valueOf(prepagatiFattureRiepilogative));
    }

    public static Boolean getPrepagatiFattureRiepilogative(Session sx) {
        log.debug("Preferenze: Attenzione! Requesting database value prepagatiFattureRiepilogative");
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.PREPAGATI_FATTURE_RIEPILOGATIVE));
    }

    public static Boolean getUnicitaCodiceFiscale(Session sx) {
        log.debug("Preferenze: Attenzione! Requesting database value unicitaCodiceFiscale");
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.CLIENTI_UNICITA_CODICE_FISCALE));
    }

    public static void setUnicitaCodiceFiscale(Session sx, Boolean unicitaCodiceFiscale) {
        saveSetting(sx, MROldSetting.CLIENTI_UNICITA_CODICE_FISCALE, String.valueOf(unicitaCodiceFiscale));
    }

    public static Boolean getClientiApplicaSconto(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.CLIENTI_APPLICA_SCONTO));
    }

    public static void setClientiApplicaSconto(Session sx, Boolean clientiApplicaSconto) {
        saveSetting(sx, MROldSetting.CLIENTI_APPLICA_SCONTO, clientiApplicaSconto.toString());
    }

    public static Boolean getContrattoModificaFonte(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.CONTRATTO_MODIFICA_FONTE));
    }

    public static void setContrattoModificaFonte(Session sx, Boolean contrattoModificaFonte) {
        saveSetting(sx, MROldSetting.CONTRATTO_MODIFICA_FONTE, contrattoModificaFonte.toString());
    }

    public static Boolean getContrattoRicevutaFiscaleUnica(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.CONTRATTO_RICEVUTA_FISCALE_UNICA));
    }

    public static void setContrattoRicevutaFiscaleUnica(Session sx, Boolean contrattoRicevutaFiscaleUnica) {
        saveSetting(sx, MROldSetting.CONTRATTO_RICEVUTA_FISCALE_UNICA, contrattoRicevutaFiscaleUnica.toString());
    }


    public static Boolean getContrattoStampaFatturaEsteso(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.CONTRATTO_STAMPA_FATTURA_ESTESO));
    }

    /*
     * Andrea
     */


    public static Boolean getRecalculateInvoice(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.RECALCULATE_INVOICE));
    }


    public static Boolean getInvoiceEnumerationFromUserAffiliate(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.INVOICE_ENUMERATION_FROM_USER_AFFILIATE));
    }
    /*
     * Andrea
     */

    public static Boolean getInvoiceListOperator(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.INVOICELIST_OPERATOR));
    }
    /*
     * Andrea
     */

    public static Boolean getOperatorCancelReservation(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.OPERATOR_CANCEL_RESERVATION));
    }
    public static Boolean getNumerazioneFatturePrefissoAnno(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.NUMERAZIONE_FATTURE_PREFISSO_ANNO));
    }

    public static Boolean getPermettiModificaManualeFatture(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.PERMETTI_MODIFICA_MANUALE_FATTURE));
    }

    public static void setContrattoStampaFatturaEsteso(Session sx,Boolean contrattoStampaFatturaEsteso) {
        saveSetting(sx,MROldSetting.CONTRATTO_STAMPA_FATTURA_ESTESO, String.valueOf(contrattoStampaFatturaEsteso));
    }

    /*
     * Andrea
     */
    public static void setRecalculateInvoice(Boolean recalculateInvoice,Session sx) {
        saveSetting(sx,MROldSetting.RECALCULATE_INVOICE, String.valueOf(recalculateInvoice));
    }

    /*
     * Andrea
     */
    public static void setInvoiceListOperator(Session sx,Boolean invoiceListOperator) {
        saveSetting(sx,MROldSetting.INVOICELIST_OPERATOR, String.valueOf(invoiceListOperator));
    }

    /*
    * Andrea
    */
    public static void setOperatorCancelReservation(Session sx,Boolean operatorCancelReservation) {
        saveSetting(sx,MROldSetting.OPERATOR_CANCEL_RESERVATION, String.valueOf(operatorCancelReservation));
    }

    public static void setInvoiceEnumerationFromUserAffiliate(Session sx,Boolean invoiceEnumerationFromUserAffiliate) {
        saveSetting(sx,MROldSetting.INVOICE_ENUMERATION_FROM_USER_AFFILIATE, String.valueOf(invoiceEnumerationFromUserAffiliate));
    }

    public static void setNumerazioneFatturePrefissoAnno(Boolean value,Session sx) {
        saveSetting(sx,MROldSetting.NUMERAZIONE_FATTURE_PREFISSO_ANNO, String.valueOf(value));
    }

    public static void setPermettiModificaManualeFatture(Boolean value,Session sx) {
        saveSetting(sx,MROldSetting.PERMETTI_MODIFICA_MANUALE_FATTURE, String.valueOf(value));
    }



    public static MROldSetting getSetting(String key,Session sx) {
        MROldSetting retValue = null;
        Session mySession = null;
        try {
            mySession = sx;//.startNewSession();
            retValue = getSetting(mySession, key);
            mySession.close();
        } catch (HibernateException ex) {
            if (mySession != null) {
                //sx.closeSession();
            }
        }
        return retValue;
    }

    public static MROldSetting getSetting(Session sx, String key) throws HibernateException {
        return (MROldSetting) sx.get(MROldSetting.class, key);
    }

    public static String getSettingValue(Session sx, String key) throws HibernateException {
        MROldSetting s = getSetting(sx, key);
        if (s != null) {
            return s.getValue();
        }
        return null;
    }

    public static void saveSetting(Session sx, String key, String value) {
        MROldSetting s = (MROldSetting) sx.get(MROldSetting.class, key);
        if (s == null) {
            s = new MROldSetting(key, value);
            sx.save(s);
        } else {
            s.setValue(value);
            sx.update(s);
        }
    }

    public void setTipoStampante(Boolean tipoStampante) {
        this.tipoStampante = tipoStampante;
    }

    public Boolean getTipoStampante() {
        return tipoStampante;
    }

    public Boolean getStampaPredefinitaSenzaImmagini() {
        return stampaPredefinitaSenzaImmagini;
    }

    public void setStampaPredefinitaSenzaImmagini(Boolean stampaPredefinitaSenzaImmagini) {
        this.stampaPredefinitaSenzaImmagini = stampaPredefinitaSenzaImmagini;
    }

    public Boolean getStampaViewerPDFEsterno() {
        return stampaViewerPDFEsterno;
    }

    public void setStampaViewerPDFEsterno(Boolean stampaViewerPDFEsterno) {
        this.stampaViewerPDFEsterno = stampaViewerPDFEsterno;
    }

    public String getStampaPercorsoViewerPDF() {
        return stampaPercorsoViewerPDF;
    }

    public void setStampaPercorsoViewerPDF(String stampaPercorsoViewerPDF) {
        this.stampaPercorsoViewerPDF = stampaPercorsoViewerPDF;
    }

    public Boolean getAvvisoScadenzaBollo() {
        return avvisoScadenzaBollo;
    }

    public void setAvvisoScadenzaBollo(Boolean avvisoScadenzaBollo) {
        this.avvisoScadenzaBollo = avvisoScadenzaBollo;
    }

    public Boolean getAvvisoScadenzaAssicurazione() {
        return avvisoScadenzaAssicurazione;
    }

    public void setAvvisoScadenzaAssicurazione(Boolean avvisoScadenzaAssicurazione) {
        this.avvisoScadenzaAssicurazione = avvisoScadenzaAssicurazione;
    }

    public Integer getGiorniAvvisoScadenzaBollo() {
        return giorniAvvisoScadenzaBollo;
    }

    public void setGiorniAvvisoScadenzaBollo(Integer giorniAvvisoScadenzaBollo) {
        this.giorniAvvisoScadenzaBollo = giorniAvvisoScadenzaBollo;
    }

    public Integer getGiorniAvvisoScadenzaAssicurazione() {
        return giorniAvvisoScadenzaAssicurazione;
    }

    public void setGiorniAvvisoScadenzaAssicurazione(Integer giorniAvvisoScadenzaAssicurazione) {
        this.giorniAvvisoScadenzaAssicurazione = giorniAvvisoScadenzaAssicurazione;
    }

    public Boolean getShowChangeLog() {
        return showChangeLog;
    }

    public void setShowChangeLog(Boolean showChangeLog) {
        this.showChangeLog = showChangeLog;
    }

    public String getChangeLogVersion() {
        return changeLogVersion;
    }

    public void setChangeLogVersion(String changeLogVersion) {
        this.changeLogVersion = changeLogVersion;
    }

    public Color getTableBackground() {
        return tableBackground;
    }

    public void setTableBackground(Color tableBackground) {
        this.tableBackground = tableBackground;
    }

    public Color getTableForeground() {
        return tableForeground;
    }

    public void setTableForeground(Color tableForeground) {
        this.tableForeground = tableForeground;
    }

    public Color getTableSelectionBackground() {
        return tableSelectionBackground;
    }

    public void setTableSelectionBackground(Color tableSelectionBackground) {
        this.tableSelectionBackground = tableSelectionBackground;
    }

    public Color getTableSelectionForeground() {
        return tableSelectionForeground;
    }

    public void setTableSelectionForeground(Color tableSelectionForeground) {
        this.tableSelectionForeground = tableSelectionForeground;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        this.mailSmtpHost = mailSmtpHost;
    }

    public Boolean getMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public void setMailSmtpAuth(Boolean mailSmtpAuth) {
        this.mailSmtpAuth = mailSmtpAuth;
    }

    public String getMailSmtpUser() {
        return mailSmtpUser;
    }

    public void setMailSmtpUser(String mailSmtpUser) {
        this.mailSmtpUser = mailSmtpUser;
    }

    public String getMailSmtpPass() {
        return mailSmtpPass;
    }

    public void setMailSmtpPass(String mailSmtpPass) {
        this.mailSmtpPass = mailSmtpPass;
    }

    public String getMailName() {
        return mailName;
    }

    public void setMailName(String mailName) {
        this.mailName = mailName;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getOggettoEmailText(Session sx) {
        return getSettingValue(sx,MROldSetting.PROP_OGGETTO_EMAIL_TEXT);
    }

    public static Boolean getOggettoEmailTextModifiableUsers(Session sx) {
        return Boolean.valueOf(getSettingValue(sx,MROldSetting.PROP_OGGETTO_EMAIL_TEXT_MODIFIABLE_USERS));
    }

    public void setOggettoEmailText(String oggettoEmailText,Session sx) {
        saveSetting(sx,MROldSetting.PROP_OGGETTO_EMAIL_TEXT, oggettoEmailText);
    }

    public static String getMessaggioEmailText(Session sx) {
        return getSettingValue(sx,MROldSetting.PROP_MESSAGGIO_EMAIL_TEXT);
    }

    public static Boolean getMessaggioEmailTextModifiableUsers(Session sx) {
        return Boolean.valueOf(getSettingValue(sx,MROldSetting.PROP_MESSAGGIO_EMAIL_TEXT_MODIFIABLE_USERS));
    }

    public void setMessaggioEmailText(String messaggioEmailText,Session sx) {
        saveSetting(sx,MROldSetting.PROP_MESSAGGIO_EMAIL_TEXT, messaggioEmailText);
    }

    public static String getEmailbccEmailText(Session sx) {
        return getSettingValue(sx,MROldSetting.PROP_EMAILBCC_EMAIL_TEXT);
    }


    public void setEmailbccEmailText(String emailbccEmailText,Session sx) {
        saveSetting(sx,MROldSetting.PROP_EMAILBCC_EMAIL_TEXT, emailbccEmailText);
    }

    public static Boolean getAllegatiEmailModifiableUsers(Session sx) {
        return Boolean.valueOf(getSettingValue(sx,MROldSetting.PROP_ALLEGATI_EMAIL_MODIFIABLE_USERS));
    }

    public static Boolean getToEmailModifiableUsers(Session sx) {
        return Boolean.valueOf(getSettingValue(sx,MROldSetting.PROP_TO_EMAIL_MODIFIABLE_USERS));
    }

    public static Boolean getCcEmailModifiableUsers(Session sx) {
        return Boolean.valueOf(getSettingValue(sx,MROldSetting.PROP_CC_EMAIL_MODIFIABLE_USERS));
    }

    public static Boolean getCcEmailSedeIngresso(Session sx) {
        return Boolean.valueOf(getSettingValue(sx,MROldSetting.PROP_CC_EMAIL_SEDE_INGRESSO));
    }

    public static Boolean getCcEmailSedeUscita(Session sx) {
        return Boolean.valueOf(getSettingValue(sx,MROldSetting.PROP_CC_EMAIL_SEDE_USCITA));
    }

    public static String getMailFromName(Session sx) {
        return getSettingValue(sx,MROldSetting.MAIL_FROM_NAME);
    }


    public static Integer getGiorniCalcoloScadenzaDaNotifica(Session sx) {
        String aGiorni = getSettingValue(sx,MROldSetting.MULTE_GIORNI_CACOLO_SCADENZA_DA_NOTIFICA);
        if (aGiorni == null) {
            /* il default e' 58 */
            return new Integer(58);
        } else {
            return new Integer(aGiorni);
        }
    }


    public static void setGiorniCalcoloScadenzaDaNotifica(Session sx,Integer aGiorniCalcoloScadenzaDaNotifica) {
        saveSetting(sx,MROldSetting.MULTE_GIORNI_CACOLO_SCADENZA_DA_NOTIFICA, aGiorniCalcoloScadenzaDaNotifica.toString());
    }

    public Boolean getCalendarioTutteSediOperatoriCapostazioni(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.PROP_CALENDARIO_TUTTE_SEDI_OPERATORI_CAPOSTAZIONI));
    }

    public void setCalendarioTutteSediOperatoriCapostazioni(Session sx,Boolean calendarioTutteSediOperatoriCapostazioni) {
        if (calendarioTutteSediOperatoriCapostazioni != null) {
            saveSetting(sx,MROldSetting.PROP_CALENDARIO_TUTTE_SEDI_OPERATORI_CAPOSTAZIONI, String.valueOf(calendarioTutteSediOperatoriCapostazioni));
        }
    }

//    public static Integer getContrattoRitardoMassimoMinuti() {
//        if (getSettingValue(MROldSetting.CONTRATTO_RITARDO_MASSIMO_MINUTI) != null) {
//            return Integer.valueOf(getSettingValue(MROldSetting.CONTRATTO_RITARDO_MASSIMO_MINUTI));
//        } else {
//            return Integer.valueOf(AbstractFatturazione.RITARDO_MASSIMO_CONSENTITO);
//        }
//    }
//
    public static Integer getContrattoRitardoMassimoMinuti(Session sx) {
        if (getSettingValue(sx, MROldSetting.CONTRATTO_RITARDO_MASSIMO_MINUTI) != null) {
            return Integer.valueOf(getSettingValue(sx, MROldSetting.CONTRATTO_RITARDO_MASSIMO_MINUTI));
        } else {
            return 59;
        }
    }

    public static void setContrattoRitardoMassimoMinuti(Session sx,Integer contrattoRitardoMassimoMinuti) {
        saveSetting(sx,MROldSetting.CONTRATTO_RITARDO_MASSIMO_MINUTI, contrattoRitardoMassimoMinuti.toString());
    }

    /*
     * Restituisce ora minuti in formato date (facendo il parsing della string delle preferenze).
     * Se l'ora e' null allora restituisce ora e minuti della data odierna
     */


    /*
     * Come getGeneraleInizioImpegnoOraMinuti() ma con la sessione
     */
    public static Date getGeneraleInizioImpegnoOraMinuti(Session sx) {
        if (getSettingValue(sx, MROldSetting.GENERALE_INIZIO_IMPEGNO_ORAMINUTI) != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_TIMESTAMP);
            Date date = null;
            try {
                date = sdf.parse(getSettingValue(sx, MROldSetting.GENERALE_INIZIO_IMPEGNO_ORAMINUTI));
            } catch (ParseException ex) {
                log.error("cannot parse: " + getSettingValue(sx, MROldSetting.GENERALE_INIZIO_IMPEGNO_ORAMINUTI) + " as date");
            }
            return date;
        } else {
            return null;
        }
    }

    /*
     * Serializza la data in stringa e la salva nella preferenza
     */
    public static void setGeneraleInizioImpegnoOraMinuti(Date date,Session sx) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_TIMESTAMP);
        if (date != null) {
            saveSetting(sx,MROldSetting.GENERALE_INIZIO_IMPEGNO_ORAMINUTI, sdf.format(date));
        } else {
            saveSetting(sx,MROldSetting.GENERALE_INIZIO_IMPEGNO_ORAMINUTI, null);
        }
    }

    /*
     * Restituisce le ore di fine impegno da sommare alla data di inizio impegno
     */
    public static Integer getGeneraleFineImpegnoOre(Session sx) {
        if (getSettingValue(sx,MROldSetting.GENERALE_FINE_IMPEGNO_ORE) != null) {
            return new Integer(getSettingValue(sx,MROldSetting.GENERALE_FINE_IMPEGNO_ORE));
        } else {
            return null;
        }
    }

    /*
     * Come getGeneraleFineImpegnoOre() ma con la sessione
     */


    public static void setGeneraleFineImpegnoOre(Session sx,Integer ore) {
        if (ore != null) {
            saveSetting(sx,MROldSetting.GENERALE_FINE_IMPEGNO_ORE, ore.toString());
        } else {
            saveSetting(sx,MROldSetting.GENERALE_FINE_IMPEGNO_ORE, null);
        }
    }

    /**
     *
     * @param value
     */
    public static void setFuelInvoicedWithVatCodeZero(Session sx, Boolean value) {
        if (value != null) {
            saveSetting(sx, MROldSetting.FUEL_INVOICED_WITH_VAT_CODE_ZERO, String.valueOf(value));
        } else {
            saveSetting(sx, MROldSetting.FUEL_INVOICED_WITH_VAT_CODE_ZERO, String.valueOf(Boolean.FALSE));
        }
    }

    /**
     *
     * @param sx
     * @return
     */
    public static Boolean getFuelInvoicedWithVatCodeZero(Session sx) {
        if (getSettingValue(sx, MROldSetting.FUEL_INVOICED_WITH_VAT_CODE_ZERO) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.FUEL_INVOICED_WITH_VAT_CODE_ZERO));
        } else {
            return Boolean.FALSE;
        }
    }

    public static Boolean getOptionalsAbilitaImportiNegativi(Session sx) {
        if (getSettingValue(sx,MROldSetting.OPTIONALS_ABILITA_IMPORTI_NEGATIVI) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.OPTIONALS_ABILITA_IMPORTI_NEGATIVI));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setOptionalsAbilitaImportiNegativi(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.OPTIONALS_ABILITA_IMPORTI_NEGATIVI, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.OPTIONALS_ABILITA_IMPORTI_NEGATIVI, String.valueOf(Boolean.FALSE));
        }
    }

    public static Integer getOptionalsImportoMassimoNegativo(Session sx) {
        if (getSettingValue(sx, MROldSetting.OPTIONALS_IMPORTO_MASSIMO_NEGATIVO) != null) {
            return Integer.valueOf(getSettingValue(sx, MROldSetting.OPTIONALS_IMPORTO_MASSIMO_NEGATIVO));
        } else {
            return null;
        }
    }

    public static void setOptionalsImportoMassimoNegativo(Integer value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.OPTIONALS_IMPORTO_MASSIMO_NEGATIVO, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.OPTIONALS_IMPORTO_MASSIMO_NEGATIVO, null);
        }
    }

    /*
     * anagraficghe incomplete
     */
    public static Boolean getPermettiAnagraficheIncomplete(Session sx) {
        if (getSettingValue(sx, MROldSetting.PERMETTI_ANAGRAFICHE_INCOMPLETE) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.PERMETTI_ANAGRAFICHE_INCOMPLETE));
        } else {
            return Boolean.FALSE;
        }
    }


    public static void setPermettiAnagraficheIncomplete(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.PERMETTI_ANAGRAFICHE_INCOMPLETE, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.PERMETTI_ANAGRAFICHE_INCOMPLETE, String.valueOf(Boolean.FALSE));
        }
    }

    /*
    * contratti senza conducenti
    */
    public static Boolean getPermettiContrattiSenzaConducenti(Session sx) {
        if (getSettingValue(sx, MROldSetting.PERMETTI_CONTRATTI_SENZA_CONDUCENTI) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.PERMETTI_CONTRATTI_SENZA_CONDUCENTI));
        } else {
            return Boolean.FALSE;
        }
    }


    public static void setPermettiContrattiSenzaConducenti(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.PERMETTI_CONTRATTI_SENZA_CONDUCENTI, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.PERMETTI_CONTRATTI_SENZA_CONDUCENTI, String.valueOf(Boolean.FALSE));
        }
    }
    /*
     * fine contratti
     */

    /*
     * email obbligatoria
     */

    public static Boolean getEmailClienteObbligatoria(Session sx) {
        if (getSettingValue(sx, MROldSetting.EMAIL_CLIENTE_OBBLIGATORIA) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.EMAIL_CLIENTE_OBBLIGATORIA));
        } else {
            return Boolean.FALSE;
        }
    }



    public static void setEmailClienteObbligatoria(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.EMAIL_CLIENTE_OBBLIGATORIA, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.EMAIL_CLIENTE_OBBLIGATORIA, String.valueOf(Boolean.FALSE));
        }
    }


    /*
     * fine email obbligatoriA
     */

    /*
     * codice fiscale per cliente straniero
     */

    public static Boolean getCodiceFiscaleClienteStraniero(Session sx) {
        if (getSettingValue(sx, MROldSetting.CODICE_FISCALE_9999999999999999) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.CODICE_FISCALE_9999999999999999));
        } else {
            return Boolean.FALSE;
        }
    }


    public static void setCodiceFiscaleClienteStraniero(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.CODICE_FISCALE_9999999999999999, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.CODICE_FISCALE_9999999999999999, String.valueOf(Boolean.FALSE));
        }
    }


    /*
     * fine codice fiscale
     */

    /*
     * cambio veicolo in prenotazione
     */

    public static Boolean getGruppoSpecialeTutteSedi(Session sx) {
        if (getSettingValue(sx, MROldSetting.CAMBIO_VEICOLO_GRUPPO_SPECIALE_MOSTRA_TUTTE_SEDI) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.CAMBIO_VEICOLO_GRUPPO_SPECIALE_MOSTRA_TUTTE_SEDI));
        } else {
            return Boolean.FALSE;
        }
    }


    public static void setGruppoSpecialeTutteSedi(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.CAMBIO_VEICOLO_GRUPPO_SPECIALE_MOSTRA_TUTTE_SEDI, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.CAMBIO_VEICOLO_GRUPPO_SPECIALE_MOSTRA_TUTTE_SEDI, String.valueOf(Boolean.FALSE));
        }
    }

    public static Boolean getGruppoSpecialeTuttiGruppi(Session sx) {
        if (getSettingValue(sx,MROldSetting.CAMBIO_VEICOLO_GRUPPO_SPECIALE_MOSTRA_TUTTI_GRUPPI) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.CAMBIO_VEICOLO_GRUPPO_SPECIALE_MOSTRA_TUTTI_GRUPPI));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setGruppoSpecialeTuttiGruppi(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.CAMBIO_VEICOLO_GRUPPO_SPECIALE_MOSTRA_TUTTI_GRUPPI, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.CAMBIO_VEICOLO_GRUPPO_SPECIALE_MOSTRA_TUTTI_GRUPPI, String.valueOf(Boolean.FALSE));
        }
    }

    /*
     * fine cambio veicolo
     */

    /*
     * numero minimo giorni optional
     */
    public static Boolean getOptionalMinimoGiorni(Session sx) {
        if (getSettingValue(sx,MROldSetting.APPLICA_NUMERO_GIORNI_MINIMO_OPTIONAL) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.APPLICA_NUMERO_GIORNI_MINIMO_OPTIONAL));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setOptionalMinimoGiorni(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.APPLICA_NUMERO_GIORNI_MINIMO_OPTIONAL, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.APPLICA_NUMERO_GIORNI_MINIMO_OPTIONAL, String.valueOf(Boolean.FALSE));
        }
    }

    /*
     * fine numero giorni minimo optional
     */

    /*
     * permetti cambio gruppo selezionato
     */

    public static Boolean getPermettiCambioGruppoSelezionatoContratto(Session sx) {
        if (getSettingValue(sx, MROldSetting.PERMETTI_CAMBIO_GRUPPO_SELEZIONATO_CONTRATTO) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.PERMETTI_CAMBIO_GRUPPO_SELEZIONATO_CONTRATTO));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setPermettiCambioGruppoSelezionatoContratto(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.PERMETTI_CAMBIO_GRUPPO_SELEZIONATO_CONTRATTO, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.PERMETTI_CAMBIO_GRUPPO_SELEZIONATO_CONTRATTO, String.valueOf(Boolean.FALSE));
        }
    }

    //ADDED BY MADHVENDRA & ANDREA RATE NUMERATION
    public static Boolean getFrenchiseeNumerationPreferences(Session sx) {
        if (getSettingValue(sx,MROldSetting.FRENCHISEE_NUMERATION_PREFERENCES) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.FRENCHISEE_NUMERATION_PREFERENCES));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setFrenchiseeNumerationPreferences(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.FRENCHISEE_NUMERATION_PREFERENCES, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.FRENCHISEE_NUMERATION_PREFERENCES, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getPermettiCambioGruppoSelezionatoPrenotazione(Session sx) {
        if (getSettingValue(sx, MROldSetting.PERMETTI_CAMBIO_GRUPPO_SELEZIONATO_PRENOTAZIONE) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.PERMETTI_CAMBIO_GRUPPO_SELEZIONATO_PRENOTAZIONE));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setPermettiCambioGruppoSelezionatoPrenotazione(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.PERMETTI_CAMBIO_GRUPPO_SELEZIONATO_PRENOTAZIONE, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.PERMETTI_CAMBIO_GRUPPO_SELEZIONATO_PRENOTAZIONE, String.valueOf(Boolean.FALSE));
        }
    }

    /*
     * fine cambio gruppo selezionato
     */

    /* firma certa */
    public void setCartellaSpoolFirmaCerta(String cartellaSpoolFirmaCerta) {
        this.cartellaSpoolFirmaCerta = cartellaSpoolFirmaCerta;
    }

    public String getCartellaSpoolFirmaCerta() {
        return cartellaSpoolFirmaCerta;
    }

    public String getTemplateFirmaCertaContrattoBreveTermine() {
        return this.templateFirmaCertaContrattoBreveTermine;
    }

    public void setTemplateFirmaCertaContrattoBreveTermine(String pathToTemplate) {
        this.templateFirmaCertaContrattoBreveTermine = pathToTemplate;
    }

    public String getTemplateFirmaCertaContrattoBreveTermineChiuso() {
        return this.templateFirmaCertaContrattoBreveTermineChiuso;
    }

    public void setTemplateFirmaCertaContrattoBreveTermineChiuso(String pathToTemplate) {
        this.templateFirmaCertaContrattoBreveTermineChiuso = pathToTemplate;
    }

    public String getTemplateFirmaCertaPreventivoBreveTermine() {
        return this.templateFirmaCertaPreventivoBreveTermine;
    }

    public void setTemplateFirmaCertaPreventivoBreveTermine(String pathToTemplate) {
        this.templateFirmaCertaPreventivoBreveTermine = pathToTemplate;
    }

    public String getTemplateFirmaCertaPrenotazione() {
        return this.templateFirmaCertaPrenotazioni;
    }

    public void setTemplateFirmaCertaPrenotazione(String pathToTemplate) {
        this.templateFirmaCertaPrenotazioni = pathToTemplate;
    }

    public String getTemplateFirmaCertaInformativaPrivacy() {
        return this.templateFirmaCertaInformativaPrivacy;
    }

    public void setTemplateFirmaCertaInformativaPrivacy(String pathToTemplate) {
        this.templateFirmaCertaInformativaPrivacy = pathToTemplate;
    }
    /* fine firma certa */

    /** GESTIONE DOCUMENTALE **/
//    public String getPerscorsoDocumenti() {
//        return DatabaseUtils.getSettingValue(MROldSetting.PATH_SALVATAGGIO_GESTIONE_DOCUMENTALE_FILE_SYSTEM);
//    }
//
//    public String getPerscorsoDocumenti(Session sx) {
//        return DatabaseUtils.getSettingValue(sx, MROldSetting.PATH_SALVATAGGIO_GESTIONE_DOCUMENTALE_FILE_SYSTEM);
//    }
//
//    public Boolean getAbilitaGestioneDocumentale() {
//        return Boolean.valueOf(DatabaseUtils.getSettingValue(MROldSetting.ABILITA_GESTIONE_DOCUMENTALE));
//    }
//
//    public Boolean getAbilitaGestioneDocumentale(Session sx) {
//        return Boolean.valueOf(DatabaseUtils.getSettingValue(sx, MROldSetting.ABILITA_GESTIONE_DOCUMENTALE));
//    }
//
//    public String getTipoArchivizioneGestioneDocumentale() {
//        return DatabaseUtils.getSettingValue(MROldSetting.TIPO_GESTIONE_DOCUMENTALE);
//    }
//
//    public String getTipoArchivizioneGestioneDocumentale(Session sx) {
//        return DatabaseUtils.getSettingValue(sx, MROldSetting.TIPO_GESTIONE_DOCUMENTALE);
//    }
//
//    public String getUsernameGestioneDocumentaleFtps(Session sx) {
//        return DatabaseUtils.getSettingValue(sx,MROldSetting.USERNAME_GESTIONE_DOCUMENTALE_FTPS);
//    }
//
//    public String getPasswordGestioneDocumentaleFtps(Session sx) {
//        return DatabaseUtils.getSettingValue(sx,MROldSetting.PASSWORD_GESTIONE_DOCUMENTALE_FTPS);
//    }
//
//    public String getUrlGestioneDocumentaleFtps(Session sx) {
//        return DatabaseUtils.getSettingValue(sx,MROldSetting.URL_GESTIONE_DOCUMENTALE_FTPS);
//    }
//
//    public String getProtocolloGestioneDocumentaleFtps(Session sx) {
//        return DatabaseUtils.getSettingValue(sx,MROldSetting.PROTOCOLLO_GESTIONE_DOCUMENTALE_FTPS);
//    }
//
//    public String getUsernameGestioneDocumentaleFtps() {
//        return DatabaseUtils.getSettingValue(MROldSetting.USERNAME_GESTIONE_DOCUMENTALE_FTPS);
//    }
//
//    public String getPasswordGestioneDocumentaleFtps() {
//        return DatabaseUtils.getSettingValue(MROldSetting.PASSWORD_GESTIONE_DOCUMENTALE_FTPS);
//    }
//
//    public String getUrlGestioneDocumentaleFtps() {
//        return DatabaseUtils.getSettingValue(MROldSetting.URL_GESTIONE_DOCUMENTALE_FTPS);
//    }
//
//    public String getProtocolloGestioneDocumentaleFtps() {
//        return DatabaseUtils.getSettingValue(MROldSetting.PROTOCOLLO_GESTIONE_DOCUMENTALE_FTPS);
//    }

    /** FINE GESTIONE DOCUMENTALE **/



    public static Boolean getMostraTuttiContrattiAffiliatiMainWindow(Session sx) {
        if (getSettingValue(sx,MROldSetting.MOSTRA_TUTTI_CONTRATTI_AFFILIATI_MAIN_WINDOWS) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.MOSTRA_TUTTI_CONTRATTI_AFFILIATI_MAIN_WINDOWS));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setMostraTuttiContrattiAffiliatiMainWindow(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.MOSTRA_TUTTI_CONTRATTI_AFFILIATI_MAIN_WINDOWS, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.MOSTRA_TUTTI_CONTRATTI_AFFILIATI_MAIN_WINDOWS, String.valueOf(Boolean.FALSE));
        }
    }



    public static Boolean getAbilitaColoriTuttiMovimentiCalendarioAuto(Session sx) {
        if (getSettingValue(sx,MROldSetting.ABILITA_COLORI_PLANNING_TUTTI_MOVIMENTI) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.ABILITA_COLORI_PLANNING_TUTTI_MOVIMENTI));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setAbilitaColoriTuttiMovimentiCalendarioAuto(Boolean value,Session sx) {
        if (value != null) {


            saveSetting(sx,MROldSetting.ABILITA_COLORI_PLANNING_TUTTI_MOVIMENTI, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.ABILITA_COLORI_PLANNING_TUTTI_MOVIMENTI, String.valueOf(Boolean.FALSE));
        }
    }

    /*
     * inizio multi currency
     */

    public static void setAbilitaMultiCurrency(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.ABILITA_MULTI_CURRENCY, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.ABILITA_MULTI_CURRENCY, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getAbilitaMultiCurrency(Session sx) {
        if (getSettingValue(sx,MROldSetting.ABILITA_MULTI_CURRENCY) !=null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.ABILITA_MULTI_CURRENCY));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setPermettiAggiornamentoCurrency(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.PERMETTI_AGGIORNAMENTO_CURRENCY, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.PERMETTI_AGGIORNAMENTO_CURRENCY, String.valueOf(Boolean.FALSE));
        }
    }

    public static Boolean getPermettiAggiornamentoCurrency(Session sx) {
        if (getSettingValue(sx,MROldSetting.PERMETTI_AGGIORNAMENTO_CURRENCY) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.PERMETTI_AGGIORNAMENTO_CURRENCY));
        } else {
            return Boolean.FALSE;
        }
    }


    public static void setPermettiCreazioneNuovoCurrency(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.PERMETTI_CREAZIONE_NUOVO_CURRENCY, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.PERMETTI_CREAZIONE_NUOVO_CURRENCY, String.valueOf(Boolean.FALSE));
        }
    }



    public static Boolean getPermettiCreazioneNuovoCurrency(Session sx) {
        if (getSettingValue(sx,MROldSetting.PERMETTI_CREAZIONE_NUOVO_CURRENCY) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.PERMETTI_CREAZIONE_NUOVO_CURRENCY));
        } else {
            return Boolean.FALSE;
        }
    }


    public static void setPermettiModificaCurrency(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.PERMETTI_MODIFICA_CURRENCY_ESISTENTE, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.PERMETTI_MODIFICA_CURRENCY_ESISTENTE, String.valueOf(Boolean.FALSE));
        }
    }



    public static Boolean getPermettiModificaCurrency(Session sx) {
        if (getSettingValue(sx,MROldSetting.PERMETTI_MODIFICA_CURRENCY_ESISTENTE) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.PERMETTI_MODIFICA_CURRENCY_ESISTENTE));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setDefaultCurrency(String value,Session sx) {
        saveSetting(sx,MROldSetting.DEFAULT_CURRENCY, value);
    }

//    public static String getDefaultCurrency() {
//        return DatabaseUtils.getSettingValue(MROldSetting.DEFAULT_CURRENCY);
//    }
//
//    public static String getDefaultCurrency(Session sx) {
//        return DatabaseUtils.getSettingValue(sx,MROldSetting.DEFAULT_CURRENCY);
//    }

    /*
     * fine multi currency
     */

    public static void setColoraContrattoRitardoImmediato(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.COLORA_RITARDO_OLTRE_LIMITE_CONSENTITO, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.COLORA_RITARDO_OLTRE_LIMITE_CONSENTITO, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getColoraContrattoRitardoImmediato(Session sx) {
        if (getSettingValue(sx,MROldSetting.COLORA_RITARDO_OLTRE_LIMITE_CONSENTITO) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.COLORA_RITARDO_OLTRE_LIMITE_CONSENTITO));
        } else {
            return Boolean.FALSE;
        }
    }

    /*
     * FATTURAZIONE BROOKER
     */
    public static void setFatturazioneBrookerPrepagati(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.FATTURAZIONE_PREPAGATI_BROKER_CONTRATTI_CHIUSI, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.FATTURAZIONE_PREPAGATI_BROKER_CONTRATTI_CHIUSI, String.valueOf(Boolean.FALSE));
        }
    }



    public static Boolean getFatturazioneBrookerPrepagati(Session sx) {
        if (getSettingValue(sx,MROldSetting.FATTURAZIONE_PREPAGATI_BROKER_CONTRATTI_CHIUSI) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.FATTURAZIONE_PREPAGATI_BROKER_CONTRATTI_CHIUSI));
        } else {
            return Boolean.FALSE;
        }
    }
     /*
      * fine fatturazione brooker
      */

    /**
     * scorporo oneri aeroportuali
     */
    public static void setScorporoOneriAeroportualiDallaTariffa(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.SCORPORO_ONERI_AEROPORTUALI_DALLA_TARIFFA, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.SCORPORO_ONERI_AEROPORTUALI_DALLA_TARIFFA, String.valueOf(Boolean.FALSE));
        }
    }



    public static Boolean getScorporoOneriAeroportualiDallaTariffa(Session sx) {
        if (getSettingValue(sx, MROldSetting.SCORPORO_ONERI_AEROPORTUALI_DALLA_TARIFFA) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.SCORPORO_ONERI_AEROPORTUALI_DALLA_TARIFFA));
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * fine scorporo
     */

    /**
     * calcolo optional percentuali
     */
    public static void setCalcoloImportoOptionalPercentualiSoloTKM(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.CALCOLA_IMPORTO_OPTIONAL_PERCENTUALE_SOLO_SU_TKM, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.CALCOLA_IMPORTO_OPTIONAL_PERCENTUALE_SOLO_SU_TKM, String.valueOf(Boolean.FALSE));
        }
    }

    public static Boolean getCalcoloImportoOptionalPercentualiSoloTKM(Session sx) {
        if (getSettingValue(sx,MROldSetting.CALCOLA_IMPORTO_OPTIONAL_PERCENTUALE_SOLO_SU_TKM) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.CALCOLA_IMPORTO_OPTIONAL_PERCENTUALE_SOLO_SU_TKM));
        } else {
            return Boolean.FALSE;
        }
    }

    //Added @Madhvendra

    public static void setRentalTypeEnableQuotationReservationNewRental(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.RENTAL_TYPE_ENABLE_QUOTATION_RESERVATION_NEW_RENTAL, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.RENTAL_TYPE_ENABLE_QUOTATION_RESERVATION_NEW_RENTAL, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getRentalTypeEnableQuotationReservationNewRental(Session sx) {
        if (getSettingValue(sx,MROldSetting.RENTAL_TYPE_ENABLE_QUOTATION_RESERVATION_NEW_RENTAL) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.RENTAL_TYPE_ENABLE_QUOTATION_RESERVATION_NEW_RENTAL));
        } else {
            return Boolean.FALSE;
        }
    }




    public static void setUseDepositRulesInsideReservationSource(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.USE_DEPOSIT_RULES_INSIDE_RESERVATION_SOURCE, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.USE_DEPOSIT_RULES_INSIDE_RESERVATION_SOURCE, String.valueOf(Boolean.FALSE));
        }
    }

    public static Boolean getUseDepositRulesInsideReservationSource(Session sx) {
        if (getSettingValue(sx,MROldSetting.USE_DEPOSIT_RULES_INSIDE_RESERVATION_SOURCE) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.USE_DEPOSIT_RULES_INSIDE_RESERVATION_SOURCE));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setOperatorCanChangeFuelLevel(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.OPERATOR_CAN_CHANGE_FUEL_LEVEL, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.OPERATOR_CAN_CHANGE_FUEL_LEVEL, String.valueOf(Boolean.FALSE));
        }
    }



    public static Boolean getOperatorCanChangeFuelLevel(Session sx) {
        if (getSettingValue(sx,MROldSetting.OPERATOR_CAN_CHANGE_FUEL_LEVEL) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.OPERATOR_CAN_CHANGE_FUEL_LEVEL));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setOperatorCanChangeKm(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.OPERATOR_CAN_CHANGE_KM, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.OPERATOR_CAN_CHANGE_KM, String.valueOf(Boolean.FALSE));
        }
    }



    public static Boolean getOperatorCanChangeKm(Session sx) {
        if (getSettingValue(sx,MROldSetting.OPERATOR_CAN_CHANGE_KM) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.OPERATOR_CAN_CHANGE_KM));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setAdminCanChangeKm(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.ADMIN_CAN_CHANGE_KM, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.ADMIN_CAN_CHANGE_KM, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getAdminCanChangeKm(Session sx) {
        if (getSettingValue(sx,MROldSetting.ADMIN_CAN_CHANGE_KM) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.ADMIN_CAN_CHANGE_KM));
        } else {
            return Boolean.FALSE;
        }
    }


    public static void setAdminCanChangeFuelLevel(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.ADMIN_CAN_CHANGE_FUEL_LEVEL, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.ADMIN_CAN_CHANGE_FUEL_LEVEL, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getAdminCanChangeFuelLevel(Session sx) {
        if (getSettingValue(sx,MROldSetting.ADMIN_CAN_CHANGE_FUEL_LEVEL) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.ADMIN_CAN_CHANGE_FUEL_LEVEL));
        } else {
            return Boolean.FALSE;
        }
    }
    ///////////////// 
    public static void setCustomerNotReqQuotationReservationRental(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.CUSTOMER_NOT_REQUIRED_QUOTATION_RESERVATION_RENTAL, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.CUSTOMER_NOT_REQUIRED_QUOTATION_RESERVATION_RENTAL, String.valueOf(Boolean.FALSE));
        }
    }

    public static Boolean getCustomerNotReqQuotationReservationRental(Session sx) {
        if (getSettingValue(sx,MROldSetting.CUSTOMER_NOT_REQUIRED_QUOTATION_RESERVATION_RENTAL) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.CUSTOMER_NOT_REQUIRED_QUOTATION_RESERVATION_RENTAL));
        } else {
            return Boolean.FALSE;
        }
    }



    public static Boolean getUserCanChangeInvoiceNumberlocation(Session sx) {
        if (getSettingValue(sx,MROldSetting.RENTAL_TYPE_ENABLE_QUOTATION_RESERVATION_NEW_RENTAL) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.RENTAL_TYPE_ENABLE_QUOTATION_RESERVATION_NEW_RENTAL));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setUserCanChangeInvoiceNumber(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.USER_CAN_CHANGE_INVOICE_NUMBER, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.USER_CAN_CHANGE_INVOICE_NUMBER, String.valueOf(Boolean.FALSE));
        }
    }



    public static Boolean getUserCanChangeInvoiceNumber(Session sx) {
        if (getSettingValue(sx,MROldSetting.USER_CAN_CHANGE_INVOICE_NUMBER) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.USER_CAN_CHANGE_INVOICE_NUMBER));
        } else {
            return Boolean.FALSE;
        }
    }

    //////////////////////////////////////////////
    /**
     * fine calcolo optional percentuali
     */

     /*
      * fattura libera
      */

    public static void setCreaFatturaLibera(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.CREA_FATTURA_LIBERA, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.CREA_FATTURA_LIBERA, String.valueOf(Boolean.FALSE));
        }
    }

    public static Boolean getCreaFatturaLibera(Session sx) {
        if (getSettingValue(sx,MROldSetting.CREA_FATTURA_LIBERA) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.CREA_FATTURA_LIBERA));
        } else {
            return Boolean.FALSE;
        }
    }

     /*
      * fine server libera
      */

     /*
      * optional deposito cauzionale
      */

    public static void setOptionalDepositoCauzionale(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.OPTIONAL_CAUZIONALE, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.OPTIONAL_CAUZIONALE, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getOptionalDepositoCauzionale(Session sx) {
        if (getSettingValue(sx,MROldSetting.OPTIONAL_CAUZIONALE) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.OPTIONAL_CAUZIONALE));
        } else {
            return Boolean.FALSE;
        }
    }

     /*
      * fine optional deposito cauzionale
      */

     /*
      * optional modifica parcheggio
      */

    public static void setPermettiModificaParcheggioVeicolo(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.MODIFICA_PARCHEGGIO_VEICOLO, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.MODIFICA_PARCHEGGIO_VEICOLO, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getPermettiModificaParcheggioVeicolo(Session sx) {
        if (getSettingValue(sx,MROldSetting.MODIFICA_PARCHEGGIO_VEICOLO) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.MODIFICA_PARCHEGGIO_VEICOLO));
        } else {
            return Boolean.FALSE;
        }
    }

     /*
      * fine modifica parcheggio
      */

     /*
      * optional lasia prenotazione collegata al veicolo
      */

    public static void setLasciaPrenotazioneAbbinataAlVeicoloAEliminazioneContratto(Session sx,Boolean value) {
        if (value != null) {
            saveSetting(sx,MROldSetting.LASCIA_PRENOTAZIONE_ABBINATA_AL_VEICOLO_CANCELLAZIONE_CONTRATTO, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.LASCIA_PRENOTAZIONE_ABBINATA_AL_VEICOLO_CANCELLAZIONE_CONTRATTO, String.valueOf(Boolean.FALSE));
        }
    }



    public static Boolean getLasciaPrenotazioneAbbinataAlVeicoloAEliminazioneContratto(Session sx) {
        if (getSettingValue(sx,MROldSetting.LASCIA_PRENOTAZIONE_ABBINATA_AL_VEICOLO_CANCELLAZIONE_CONTRATTO) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.LASCIA_PRENOTAZIONE_ABBINATA_AL_VEICOLO_CANCELLAZIONE_CONTRATTO));
        } else {
            return Boolean.TRUE;
        }
    }

     /*
      * fine lasia prenotazione collegata al veicolo
      */

     /*
     * myguardian
     */

    public static void setIstanzaMyGuardian(String value,Session sx) {
        saveSetting(sx,MROldSetting.ISTANZA_MYGUARDIAN, value);
    }

//    public static String getIstanzaMyGuardian() {
//        return DatabaseUtils.getSettingValue(MROldSetting.ISTANZA_MYGUARDIAN);
//    }
//
//    public static String getIstanzaMyGuardian(Session sx) {
//        return DatabaseUtils.getSettingValue(sx, MROldSetting.ISTANZA_MYGUARDIAN);
//    }

    /*
     * fine mygiardian
     */

    /*
     * codice del veicolo
     */

    public static void setCodiceVeicoloNonUnico(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.CODICE_VEICOLO_NON_UNICO, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.CODICE_VEICOLO_NON_UNICO, String.valueOf(Boolean.FALSE));
        }
    }



    public static Boolean getCodiceVeicoloNonUnico(Session sx) {
        if (getSettingValue(sx,MROldSetting.CODICE_VEICOLO_NON_UNICO) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.CODICE_VEICOLO_NON_UNICO));
        } else {
            return Boolean.FALSE;
        }
    }

    /*
     * fine codice veicolo
     */

     /*
     * no fattura acconto
     */

    public static void setDisabilitaFatturaDiAcconto(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.DISABILITA_FATTURA_ACCONTO, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.DISABILITA_FATTURA_ACCONTO, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getDisabilitaFatturaDiAcconto(Session sx) {
        if (getSettingValue(sx,MROldSetting.DISABILITA_FATTURA_ACCONTO) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.DISABILITA_FATTURA_ACCONTO));
        } else {
            return Boolean.FALSE;
        }
    }

    /*
     * fine no fattura acconto
     */

     /*
     * mostra menu multe
     */

    public static void setMostraMenuMulte(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.MOSTRA_MENU_MULTE_OPERATORE, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.MOSTRA_MENU_MULTE_OPERATORE, String.valueOf(Boolean.FALSE));
        }
    }

    public static Boolean getMostraMenuMulte(Session sx) {
        if (getSettingValue(sx,MROldSetting.MOSTRA_MENU_MULTE_OPERATORE) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.MOSTRA_MENU_MULTE_OPERATORE));
        } else {
            return Boolean.FALSE;
        }
    }


    /*
     * fine mostra menu multe
     */

     /*
     * mostra menu multe
     */

    public static void setModificaPrenotazioniPrepagate(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.ADMINISTRATOR_MODIFICA_PRENOTAZIONI_PREPAGATE, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.ADMINISTRATOR_MODIFICA_PRENOTAZIONI_PREPAGATE, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getModificaPrenotazioniPrepagate(Session sx) {
        if (getSettingValue(sx,MROldSetting.ADMINISTRATOR_MODIFICA_PRENOTAZIONI_PREPAGATE) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.ADMINISTRATOR_MODIFICA_PRENOTAZIONI_PREPAGATE));
        } else {
            return Boolean.FALSE;
        }
    }

    /*
     * fine mostra menu multe
     */

     /*
     * ricevuta fiscale
     */

    public static void setRicevutaFiscaleComeFatturaSaldo(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.RICEVUTA_FISCALE_COME_FATTURA_SALDO, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.RICEVUTA_FISCALE_COME_FATTURA_SALDO, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getRicevutaFiscaleComeFatturaSaldo(Session sx) {
        if (getSettingValue(sx,MROldSetting.RICEVUTA_FISCALE_COME_FATTURA_SALDO) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.RICEVUTA_FISCALE_COME_FATTURA_SALDO));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setAbilitaCreazioneRicevutaFiscale(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.PERMETTI_CREAZIONE_RICEVUTA_FISCALE, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.PERMETTI_CREAZIONE_RICEVUTA_FISCALE, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getAbilitaCreazioneRicevutaFiscale(Session sx) {
        if (getSettingValue(sx,MROldSetting.PERMETTI_CREAZIONE_RICEVUTA_FISCALE) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.PERMETTI_CREAZIONE_RICEVUTA_FISCALE));
        } else {
            return Boolean.FALSE;
        }
    }


    /*
     * fine ricevuta fiscale
     */


    public static void setAllowPartialDepositToBankAccount(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.PERMETTI_VERSAMENTI_PARZIALI_IN_CASSA_CENTREALE, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.PERMETTI_VERSAMENTI_PARZIALI_IN_CASSA_CENTREALE, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getAllowPartialDepositToBankAccount(Session sx) {
        if (getSettingValue(sx,MROldSetting.PERMETTI_VERSAMENTI_PARZIALI_IN_CASSA_CENTREALE) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.PERMETTI_VERSAMENTI_PARZIALI_IN_CASSA_CENTREALE));
        } else {
            return Boolean.FALSE;
        }
    }

    public static void setNoShowPdfWhenCreateInvoiceToBroker(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.NON_MOSTRARE_PDF_QUANDO_CREA_FATTURA_BROKER, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.NON_MOSTRARE_PDF_QUANDO_CREA_FATTURA_BROKER, String.valueOf(Boolean.FALSE));
        }
    }

    public static Boolean getNoShowPdfWhenCreateInvoiceToBroker(Session sx) {
        if (getSettingValue(sx,MROldSetting.NON_MOSTRARE_PDF_QUANDO_CREA_FATTURA_BROKER) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.NON_MOSTRARE_PDF_QUANDO_CREA_FATTURA_BROKER));
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * fattura tutta la durata del noleggio
     */
    public static void setFatturaTuttaDurataNoleggio(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.FATTURA_TUTTA_LA_DURATA_DEL_NOLEGGIO, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.FATTURA_TUTTA_LA_DURATA_DEL_NOLEGGIO, String.valueOf(Boolean.FALSE));
        }
    }


    public static Boolean getFatturaTuttaDurataNoleggio(Session sx) {
        if (getSettingValue(sx,MROldSetting.FATTURA_TUTTA_LA_DURATA_DEL_NOLEGGIO) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.FATTURA_TUTTA_LA_DURATA_DEL_NOLEGGIO));
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * non aggiorna automaticamente l'ora di rientro
     */
    public static void setNonAggiornareAutomaticamenteOraRientro(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.NON_AGGIORNARE_AUTOMATICAMENTE_ORA_RIENTRO_DA_PRENOTAZIONE_A_CONTRATTO, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.NON_AGGIORNARE_AUTOMATICAMENTE_ORA_RIENTRO_DA_PRENOTAZIONE_A_CONTRATTO, String.valueOf(Boolean.FALSE));
        }
    }

    public static Boolean getNonAggiornareAutomaticamenteOraRientro(Session sx) {
        if (getSettingValue(sx,MROldSetting.NON_AGGIORNARE_AUTOMATICAMENTE_ORA_RIENTRO_DA_PRENOTAZIONE_A_CONTRATTO) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.NON_AGGIORNARE_AUTOMATICAMENTE_ORA_RIENTRO_DA_PRENOTAZIONE_A_CONTRATTO));
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * enable reservation sources for the Area Manager
     */
    public static void setEnableReservationSourcesForAreaManager(Boolean value,Session sx) {
        if (value != null) {
            saveSetting(sx,MROldSetting.ENABLE_RESERVATION_SOURCES_AREA_MANAGER, String.valueOf(value));
        } else {
            saveSetting(sx,MROldSetting.ENABLE_RESERVATION_SOURCES_AREA_MANAGER, String.valueOf(Boolean.FALSE));
        }
    }

    public static Boolean getEnableReservationSourcesForAreaManager(Session sx) {
        if (getSettingValue(sx,MROldSetting.ENABLE_RESERVATION_SOURCES_AREA_MANAGER) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.ENABLE_RESERVATION_SOURCES_AREA_MANAGER));
        } else {
            return Boolean.FALSE;
        }
    }
    // use closing letter

    public static Boolean getUseClosingLetter(Session sx) {
        if (getSettingValue(sx,MROldSetting.Use_Closing_Letter) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.Use_Closing_Letter));
        } else {
            return Boolean.FALSE;
        }
    }
    public static void setUseClosingLetter(Boolean useClosingLetter,Session sx) {
        if (useClosingLetter != null) {
            saveSetting(sx,MROldSetting.Use_Closing_Letter, String.valueOf(useClosingLetter));
        } else {
            saveSetting(sx,MROldSetting.Use_Closing_Letter, String.valueOf(Boolean.FALSE));
        }
    }

    /**
     *
     * @param sx
     * @return
     */
    public static Boolean getNumerazioneUnicaFattureAffiliato(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.NUMERAZIONE_UNICA_FATTURE_AFFILIATO));
    }

    public static Boolean getNoteCreditoFattureStessaNumerazione(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.NUMERAZIONE_NOTE_CREDITO_FATTURE_UNICA_PER_SEDE));
    }

    /**
     *
     * @param sx
     * @return
     */
    public static String getVPosAbilitato(Session sx) {
        return getSettingValue(sx, MROldSetting.VPOS_ABILITATO);
    }

    public static Boolean getIngenicoAbilitato(Session sx){
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.INGENICO_ABILITATO));
    }


    public static Boolean getEnablePaymenyForAreaManager(Session sx) {
        if (getSettingValue(sx, MROldSetting.RESTRICTED_PAYMENT_TO_OPR_SM) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.RESTRICTED_PAYMENT_TO_OPR_SM));
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     *
     * @param sx
     * @return
     * @throws HibernateException
     */
    public static Integer getNotifyExpirationDays(Session sx) throws HibernateException {
        String aGiorni = getSettingValue(sx, MROldSetting.NOTIFY_EXPIRATION_DAYS);
        if (aGiorni == null) {
            return new Integer(30);
        } else {
            return new Integer(aGiorni);
        }
    }

    /**
     *
     * @param sx
     * @return
     * @throws HibernateException
     */
    public static Integer getNotifyExpirationKm(Session sx) throws HibernateException {
        String km = getSettingValue(sx, MROldSetting.NOTIFY_EXPIRATION_KM);
        if (km == null) {
            return new Integer(800);
        } else {
            return new Integer(km);
        }
    }

    public static Boolean getProformaCreation(Session sx) {
        if (getSettingValue(sx, MROldSetting.ALLOW_PROFORMA) != null) {
            return Boolean.valueOf(getSettingValue(sx, MROldSetting.ALLOW_PROFORMA));
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     *
     * @return
     */
    public static Integer getPasswordExirationDaysAdmin(Session sx) {
        String giorni = getSettingValue(sx, MROldSetting.PASSWORD_EXPIRATION_DAYS_ADMIN);
        if (giorni != null) {
            return new Integer(giorni);
        } else {
            return 0;
        }
    }

    /**
     *
     * @param sx
     * @return
     */
    public static Integer getPasswordExirationDaysUser(Session sx) {
        String giorni = getSettingValue(sx, MROldSetting.PASSWORD_EXPIRATION_DAYS_USER);
        if (giorni != null) {
            return new Integer(giorni);
        } else {
            return 0;
        }
    }

    /**
     *
     * @param sx
     * @return
     */
    public static Integer getMinCyclesChangePasswordsUser(Session sx) {
        String lenght = getSettingValue(sx, MROldSetting.MIN_CYCLES_CHANGE_PASSWORDS_USER);
        if (lenght != null) {
            return new Integer(lenght);
        } else {
            return 0;
        }
    }
    /**
     *
     * @param sx
     * @return
     */
    public static Integer getMinCyclesChangePasswordsAdmin(Session sx) {
        String lenght = getSettingValue(sx, MROldSetting.MIN_CYCLES_CHANGE_PASSWORDS_ADMIN);
        if (lenght != null) {
            return new Integer(lenght);
        } else {
            return 0;
        }
    }

    /**
     *
     * @param sx
     * @return
     */
    public static Integer getPasswordMinLenghtUser(Session sx) {
        String lenght = getSettingValue(sx, MROldSetting.PASSWORD_MIN_LENGHT_USER);
        if (lenght != null) {
            return new Integer(lenght);
        } else {
            return 5;
        }
    }

    /**
     *
     * @param sx
     * @return
     */
    public static Integer getPasswordMinLenghtAdmin(Session sx) {
        String lenght = getSettingValue(sx, MROldSetting.PASSWORD_MIN_LENGHT_ADMIN);
        if (lenght != null) {
            return new Integer(lenght);
        } else {
            return 5;
        }
    }


    /**
     *
     * @param sx
     * @return
     */
    public static Boolean getEnableUserSecurity(Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.ENABLE_USER_SECURITY));
    }

    public static Boolean getChargeFuel (Session sx) {
        return Boolean.valueOf(getSettingValue(sx, MROldSetting.CHARGE_FUEL));
    }

    public static Integer getThreshold (Session sx) {
        if (getSettingValue(sx, MROldSetting.THRESHOLD) == null) {
            return 5;
        }
        return Integer.valueOf(getSettingValue(sx, MROldSetting.THRESHOLD));
    }

    public static Boolean getMonthlyRental(Session sx) {
        if (getSettingValue(sx,MROldSetting.ENABLE_MONTLY_RENTAL) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.ENABLE_MONTLY_RENTAL));
        } else {
            return Boolean.FALSE;
        }
    }

    public static String getSelectedLocation(Session sx) {
        if (getSettingValue(sx,MROldSetting.SELECTED_LOCATION) != null) {
            return (getSettingValue(sx,MROldSetting.SELECTED_LOCATION));
        } else {
            return null;
        }
    }

    public static Integer getCodIvaFattTO(Session sx) {
        if (getSettingValue(sx, MROldSetting.PROP_COD_IVA) == null) {
            return 1;
        }
        return Integer.valueOf(getSettingValue(sx, MROldSetting.PROP_COD_IVA));
    }

    public static Boolean getDoNotCheckDuplicateDriverAndCustomer(Session sx) {
        if (getSettingValue(sx,MROldSetting.DONT_ALLOW_DUPLICATED_CUSTOMER_AND_DRIVER) != null) {
            return Boolean.valueOf(getSettingValue(sx,MROldSetting.DONT_ALLOW_DUPLICATED_CUSTOMER_AND_DRIVER));
        } else {
            return Boolean.FALSE;
        }
    }
}
