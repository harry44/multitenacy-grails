/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.utils;

import it.aessepi.utils.db.InformativaConsenso;
import it.aessepi.utils.db.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jamess
 */
public abstract class Parameters {

    private static ParametersAdapter adapter;
    
    static {
        String adapterProperty = System.getProperty("utils.config");
        if(adapterProperty != null && adapterProperty.equals("server")) {
            adapter = new ServerAdapter();
        } else {
            adapter = new DesktopAdapter();
        }
    }    //DESKTOP PUBLIC MEMBERS

    public static final String USER_FILENAME = "user.dat";
    public static final String MYRENT = "MyRent";
    public static final String MYRENTEE = "MyRentEE";
    public static final String MYRENTPRO = "MyRentPRO";
    public static final String MYFLEET = "MyFleet";
    public static final String PANDORA = "Pandora";
    public static final String MYMEC = "MyMec";
    public static final String MYELETTRO = "MyElettro";
    public static final String MYCONTAB = "MyContab";
    public static final String NETBANK = "NetBank";
    public static final String REGISTRY_FILENAME = "Reg.dat";
    public static final String VERSION_FILENAME = "Ver.dat";
    public static final String REGISTRATION_FILENAME = "Azienda.dat";
    public static final String FISCAL_CODE_FILENAME = "codici.csv";
    public static final String SERIAL_NUMBER_PROGRAM_WINDOWS = "smartctl.exe";
    public static final String SERIAL_NUMBER_PROGRAM_UNIX = "smartctl";
//    TODO Remove these desktop version members who are probably not used anymore.
//    public static final String REPORTS_DIRNAME = "reports";
//    public static final String CACHE_DIRNAME = "cache";
//    public static final String CURRENT_DIRNAME = "jar";
//    public static final String HIBERNATE_CFG_FILENAME = "hibernate.cfg.xml";
    public static final String BACKUPDB_CFG_FILENAME = "Backup.dat";
    public static final Boolean TIPO_STAMPANTE_LOCALE = Boolean.FALSE;
    public static final Boolean TIPO_STAMPANTE_REMOTA = Boolean.TRUE;
    public static final String UTILS_VERSION = "4.0.2";
    public static final String CONTABILITA_UTILS_VERSION = "1.1";
    public static final String KEY_JASPER_IMAGE_DIR = "jasper.image.dir";
    public static final String PARAMETER_NAME_START = "flavour";
    public static final String PARAMETER_NAME_VALUE_MYFLEET = "myfleet";

    public static String nazione;
    private static String sessionId;
    private static String systemUser;
    private static String remoteIp;

    public static boolean isDesktopVersion() {
        return adapter.isDesktopVersion();
    }

    public static byte[] checkSumSerialNumberBytes() {
        return adapter.checkSumSerialNumberBytes();
    }

    public static String serialNumberProgramPath() {
        return adapter.serialNumberProgramPath();
    }

    public static String serialNumberCommandLine() {
        return adapter.serialNumberCommandLine();
    }

    public static FileSystemView getFileSystemView() {
        return adapter.getFileSystemView();
    }

    public static Boolean getViewerPDFEsterno() {
        return adapter.getViewerPDFEsterno();
    }

    public static String getPercorsoViewerPDFEsterno() {
        return adapter.getPercorsoViewerPDFEsterno();
    }

    public static Boolean getTipoStampante() {
        return adapter.getTipoStampante();
    }

    public static String getProperty(String key) {
        return adapter.getProperty(key);
    }

    public static String userReportsDir() {
        return adapter.userReportsDir();
    }

    public static String mainReportsDir() {
        return adapter.mainReportsDir();
    }

    public static void posiziona(JDialog d) {
        adapter.posiziona(d);
    }

    public static void posizionaUsingMainFrame(JDialog d) {
        adapter.posizionaUsingMainFrame(d);
    }

    public static String mainConfigDirPath() {
        return adapter.mainConfigDirPath();
    }

    public static User getUser() {


        return adapter.getUser();
    }

    public static void setUser(User u) {
        adapter.setUser(u);
    }

    public static String getCurrentDir() {
        return adapter.getCurrentDir();
    }

    public static String getVersion() {
        return adapter.getVersion();
    }

    public static void setVersion(String v) {
        adapter.setVersion(v);
    }

    public static String getProgram() {
        return adapter.getProgram();
    }

    public static void setProgram(String p) {
        adapter.setProgram(p);
    }

    //public static Object getAnagraficaAzienda() {
    //    return adapter.getAnagraficaAzienda();
    //}

    //public static void setAnagraficaAzienda(Object a) {
       // adapter.setAnagraficaAzienda(a);
    //}

    public static String registryFilePath() {
        return adapter.registryFilePath();
    }

    public static String registrationFilePath() {
        return adapter.registrationFilePath();
    }

    public static InformativaConsenso getPrivacy(String titolareTratt, String indirizzoAzienda, String ragioneSocialeCliente) {
        return adapter.getPrivacy(titolareTratt, indirizzoAzienda, ragioneSocialeCliente);
    }

    public static String backupDBFilePath() {
        return adapter.backupDBFilePath();
    }

    public static void setDefaultSize(Dimension aDefaultSize) {
        adapter.setDefaultSize(aDefaultSize);
    }

    public static Dimension getDefaultSize() {
        return adapter.getDefaultSize();
    }

    public static void setDefaultBorder(Border b) {
        adapter.setDefaultBorder(b);
    }

    public static Border getDefaultBorder() {
        return adapter.getDefaultBorder();
    }

    public static void setTipoStampante(Boolean aTipoStampante) {
        adapter.setTipoStampante(aTipoStampante);
    }

    public static void setViewerPDFEsterno(Boolean aViewerPDFEsterno) {
        adapter.setViewerPDFEsterno(aViewerPDFEsterno);
    }

    public static void setPercorsoViewerPDFEsterno(String aPercorsoViewerPDFEsterno) {
        adapter.setPercorsoViewerPDFEsterno(aPercorsoViewerPDFEsterno);
    }

    public static void setDefaultLocation(Point aDefaultLocation) {
        adapter.setDefaultLocation(aDefaultLocation);
    }

    public static String userConfigDirPath() {
        return adapter.userConfigDirPath();
    }

    public static Point getDefaultLocation() {
        return adapter.getDefaultLocation();
    }
    
    public static String getGestioneAggiornamentiPath() {
        return adapter.getGestioneAggiornamentiPath();
    }
    
    public static String passwordFilePath() {
        return adapter.passwordFilePath();
    }

    public static Double getAliquotaIva() {
        return adapter.getAliquotaIVA();
    }

    public static void setAliquotaIva(Double aliquota) {
        adapter.setAliquotaIVA(aliquota);
    }

    public static List getNazioneItalia() {
        List<String> listaItalia = new ArrayList<String>();
        listaItalia.add("ITALIA");
        listaItalia.add("ITALY");
        return listaItalia;
    }

    public static String getNazione() {
        return nazione;
    }

    public static void setNazione(String aNazione) {
        nazione = aNazione;
    }

    /* session id di collegamento al MyRent, generata all'avvio e memorizzata qui */
    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String aSessionId) {
        sessionId = aSessionId;
    }

    /* nome utente di sistema, se valorizzato */
    public static String getSystemUser() {
        return systemUser;
    }

    public static void setSystemUser(String aSystemUser) {
        systemUser = aSystemUser;
    }

    /* ip remoto di collegamento (potrebbe essere l'ip fornito da nx), se valorizzato */
    public static String getRemoteIp() {
        return remoteIp;
    }

    public static void setRemoteIp(String aRemoteIp) {
        remoteIp = aRemoteIp;
    }


    /**
     * metodi per MyFleet
     */

    public static boolean isMyFleet() {
        if (getProgram() != null && getProgram().equals(MYFLEET)) {
            return true;
        }
        return false;
    }
    public static Object getAnagraficaAzienda() {
        return adapter.getAnagraficaAzienda();
    }
    public static void setAnagraficaAzienda(Object a) {
        adapter.setAnagraficaAzienda(a);
    }
}
