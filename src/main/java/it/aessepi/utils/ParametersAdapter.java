/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.utils;

import it.aessepi.utils.db.InformativaConsenso;
import it.aessepi.utils.db.User;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author jamess
 */
public abstract class ParametersAdapter {

    private String GESTIONE_AGGIORNAMENTI_PATH = "lib/GestioneAggiornamento.jar";
    private String program;
    private String version;
    private String mainVersion;
    private Object anagraficaAzienda = null;
    private Boolean tipoStampante = Parameters.TIPO_STAMPANTE_LOCALE;
    private Boolean viewerPDFEsterno = Boolean.FALSE;
    private String percorsoViewerPDFEsterno = null;
    private Border defaultBorder = null;
    private Dimension defaultSize;
    private Point defaultLocation;
    private FileSystemView fileSystemView;
    private Properties properties;
    private String currentDir;
    private User user;
    private User root;
    private Double aliquotaIVA;
    protected static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/utils/Bundle");

    abstract public String userConfigDirPath();

    abstract public String mainConfigDirPath();

    abstract public String passwordFilePath();

    abstract public String versionFilePath();

    abstract public String serialNumberProgramPath();

    abstract public String registryFilePath();

    abstract public String registrationFilePath();

    abstract public String backupDBFilePath();

    abstract public boolean isDesktopVersion();

    abstract public String reportsDir();

    abstract public String userReportsDir();

    abstract public String mainReportsDir();

    /**
     *@return String - Current program's name.
     */
    public String getProgram() {
        return program;
    }

    /**
     *Sets program's name.
     *@param p Progam's name. One of the constants defined in it.aessepi.utils.Parameters, namely MYRENT, MYMEC or MYELETTRO.
     */
    public void setProgram(String p) {
        program = p;
    }

    /**
     *@return Current directory name.
     */
    public String getCurrentDir() {
        return currentDir;
    }

    /**
     *Do not use for setting current directory as it cannot be changed. Use only for updating this field.
     *@param d Current dirname to be set.
     */
    public void setCurrentDir(String d) {
        currentDir = d;
    }

    /**
     *Updates currentDir field and returns it.
     */
    public String currentDir() {
        String d = null;
        try {
            d = new java.io.File("").getCanonicalPath(); //NOI18N
        } catch (Exception ex) {
        }
        setCurrentDir(d);
        return d;
    }

    /**
     *Computes the name of the progam used for retrieving hdd's serial number.
     */
    public String serialNumberProgramName() {
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) { //NOI18N
            return Parameters.SERIAL_NUMBER_PROGRAM_UNIX;
        }

        return Parameters.SERIAL_NUMBER_PROGRAM_WINDOWS;
    }

    /**
     *Computes the full command line and arguments to be passed to the serial number utility.
     */
    public String serialNumberCommandLine() {
        String c = null;
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) { //NOI18N
            c = "/usr/sbin/smartctl -i /dev/hda"; //NOI18N
        } else {
            c = serialNumberProgramPath() + " -i /dev/hda"; //NOI18N
        }

        return c;

    }

    public static byte[] checkSumSerialNumberBytes() {
        byte[] ck;
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) { //NOI18N
            ck = new byte[]{49, -63, 112, -127, 114, 84, -40, -67, -118, 78, -14, 13, -5, 113, -33, -90};
        } else {
            ck = new byte[]{37, 113, -41, 100, -117, -1, 41, -86, 105, -103, -54, -23, 60, 111, -122, -124};
        }

        return ck;
    }

    /**
     *Sets current version of the program.
     */
    public void setVersion(String v) {
        version = v;
    }

    /**
     *@return String - current version of the program.
     */
    public String getVersion() {
        return version;
    }

    /**
     *@return String - main version of the program.
     */
    public String getMainVersion() {
        return mainVersion;
    }

    /**
     *Sets main version of the program.
     */
    public void setMainVersion(String v) {
        mainVersion = v;
    }

    public InformativaConsenso getPrivacy(
            String titolareTratt, String indirizzoAzienda, String ragioneSocialeCliente) {
        return new InformativaConsenso(
                "della nostra azienda.", //NOI18N
                "Noleggio automezzi.", //NOI18N
                "Modalita' informatica e cartacea.", //NOI18N
                "Il conferimento e' obbligatorio per le pratiche di noleggio.", //NOI18N
                "Non sara' possibile procedere col noleggio di automezzi.", //NOI18N
                "", //NOI18N
                "I dati potranno essere comunicati a compagnie assicurative, banche, avvocati e studi legali, studi di consulenza fiscale, contabile e amministrativa, collaboratori esterni incaricati dall'azienda per il raggiungimento delle proprie attivita'", //NOI18N
                titolareTratt,
                indirizzoAzienda,
                ragioneSocialeCliente);
    }

    /**
     *@return Properties - Program wide properties.
     */
    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties p) {
        properties = p;
    }

    public String getProperty(
            String key) {
        return getProperties().getProperty(key);
    }

    public Object setProperty(
            String key, String value) {
        return getProperties().setProperty(key, value);
    }

    public void setUser(User u) {
        user = u;
    }

    public User getUser() {
        return user;
    }

    public void setRoot(User u) {
        root = u;
    }

    public User getRoot() {
        return root;
    }

    public Object getAnagraficaAzienda() {
        return anagraficaAzienda;
    }

    public void setAnagraficaAzienda(Object aAnagraficaAzienda) {
        anagraficaAzienda = aAnagraficaAzienda;
    }

    public Border getDefaultBorder() {
        return defaultBorder;
    }

    public void setDefaultBorder(Border aDefaultBorder) {
        defaultBorder = aDefaultBorder;
    }

    public Point getDefaultLocation() {
        return defaultLocation;
    }

    public void setDefaultLocation(Point aDefaultLocation) {
        defaultLocation = aDefaultLocation;
    }

    public Dimension getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(Dimension aDefaultSize) {
        defaultSize = aDefaultSize;
    }

    public void posiziona(JDialog d) {
        try {
            d.setSize(d.getParent().getSize());
            d.setLocation(getDefaultLocation());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void posizionaUsingMainFrame(JDialog d) {
        try {
            d.setSize(Frame.getFrames()[0].getSize());
            d.setLocation(getDefaultLocation());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setFileSystemView(FileSystemView fsw) {
        fileSystemView = fsw;
    }

    public FileSystemView getFileSystemView() {
        if (fileSystemView == null) {
            setFileSystemView(new FileSystemView() {

                private File systemRoot = new File(System.getProperty("user.home")) { //NOI18N

                    public File getParentFile() {
                        return null;
                    }
                };

                public File createNewFolder(
                        File dir) {
                    File newDir = new File(dir, bundle.getString("Parameters.msgNuovaCartella"));
                    newDir.mkdir();
                    return newDir;
                }

                public File[] getRoots() {
                    return new File[]{systemRoot};
                }

                public Boolean isTraversable(
                        File f) {
                    Boolean b = Boolean.FALSE;
                    try {
                        b = new Boolean(f != null && f.isDirectory() && f.getCanonicalPath().indexOf(System.getProperty("user.home")) != -1); //NOI18N
                    } catch (Exception ex) {
                    }
                    return b;
                }
            });
        }

        return fileSystemView;
    }
    
    public Boolean getTipoStampante() {
        return tipoStampante;
    }
    
    public void setTipoStampante(Boolean aTipoStampante) {
        tipoStampante = aTipoStampante;
    }
    
    public Boolean getViewerPDFEsterno() {
        return viewerPDFEsterno;
    }
    
    public void setViewerPDFEsterno(Boolean aViewerPDFEsterno) {
        viewerPDFEsterno = aViewerPDFEsterno;
    }
    
    public String getPercorsoViewerPDFEsterno() {
        return percorsoViewerPDFEsterno;
    }
    
    public void setPercorsoViewerPDFEsterno(String aPercorsoViewerPDFEsterno) {
        percorsoViewerPDFEsterno = aPercorsoViewerPDFEsterno;
    }

    public String getGestioneAggiornamentiPath() {
        return GESTIONE_AGGIORNAMENTI_PATH;
    }

    public Double getAliquotaIVA() {
        return aliquotaIVA;
    }

    public void setAliquotaIVA(Double aliquotaIVA) {
        this.aliquotaIVA = aliquotaIVA;
    }

}
