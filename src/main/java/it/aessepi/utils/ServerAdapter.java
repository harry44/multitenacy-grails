/*
 * Constants.java
 *
 * Created on 03 martie 2005, 15:54
 */
package it.aessepi.utils;

import it.aessepi.utils.beans.JasperMyTools;
import it.aessepi.utils.db.InformativaConsenso;
import java.io.FilenameFilter;
import java.util.Properties;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import it.aessepi.utils.db.User;
import it.aessepi.utils.security.AccessControlList;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author  jamess
 */
public class ServerAdapter extends ParametersAdapter {

    /** Creates a new instance of Constants */
    public ServerAdapter () {
        setProperties(new Properties(System.getProperties()));
        if (!loadProperties("users" + File.separator + System.getProperty("user.name") + ".conf")) { //NOI18N            
            log.debug("Parameters: Could not load properties file."); //NOI18N
        } else {
            log.debug("Parameters: Successfully loaded properties file."); //NOI18N
            createDirectories();
        }
        setCurrentDir(getProperties().getProperty("user.dir")); //NOI18N
    }
    
    private boolean createDirectories() {
        File f = null;
        boolean retValue = true;
        try {
            f = new File(getProperty(KEY_USER_CONFIG_DIR));
            if (!f.exists()) {
                f.mkdirs();
                log.debug("Parameters: Successfully created user's config dir."); //NOI18N
            }
        } catch (Exception ex) {
            log.debug("Parameters: Error creating user's config dir."); //NOI18N
            retValue = false;
        }
        try {
            f = new File(getProperty(KEY_USER_REPORTS_DIR));
            if (!f.exists()) {
                f.mkdirs();
                log.debug("Parameters: Successfully created user's reports dir."); //NOI18N
            }
        } catch (Exception ex) {
            log.debug("Parameters: Error creating user's reports dir." + ex); //NOI18N
            retValue = false;
        }
        return retValue;
    }


    /**
     *@deprecated Use <code>Parameters.getProperty(Parameters.KEY_USER_CONFIG_DIR)</code>
     */
    public String userConfigDirPath() {
        return getProperty(KEY_USER_CONFIG_DIR);
    }

    /**
     *@deprecated Use <code>Parameters.getProperty(Parameters.KEY_USER_CONFIG_DIR)</code>
     */
    public String mainConfigDirPath() {
        return getProperty(KEY_MAIN_CONFIG_DIR);
    }

    /**
     *Computes the full path of the file containing memorized password.
     */
    public String passwordFilePath() {
        return getProperty(KEY_USER_HOME) + File.separator + "." + getProperty(KEY_USER_NAME) + ".myrent"; //NOI18N
    }

    /**
     *Computes the full path of the version file.
     */
    public String versionFilePath() {
        return getProperty(KEY_USER_CONFIG_DIR) + File.separator + Parameters.VERSION_FILENAME;
    }

    

    /**
     *Computes the full path to the program used for retrieving hdd's serial number.
     */
    public String serialNumberProgramPath() {
        return getProperty(KEY_MAIN_CONFIG_DIR) + File.separator + serialNumberProgramName();
    }

    /**
     *Computes the full path of the file containing the database connection data.
     */
    public String registryFilePath() {
        return getProperty(KEY_USER_CONFIG_REG);        
    }

    /**
     *Computes the full path of the file containing registration data and company info.
     */
    public String registrationFilePath() {
        return getProperty(KEY_USER_CONFIG_AZIENDA);
    }

    /**
     *Computes the full path of the file containing DB backup data.
     */
    public String backupDBFilePath() {
        return getProperty(KEY_USER_CONFIG_DIR) + File.separator + Parameters.BACKUPDB_CFG_FILENAME;
    }
    
    /**
     * Reads the properties and searches a valid reports directory. Search order is: user's reports dir, main reports dir, <code>reports</code> dir in main install dir.
     */
    public String reportsDir() {
        String reportsDir = getProperty(KEY_USER_REPORTS_DIR);
        if (!verifyReportsDir(reportsDir)) {
            reportsDir = getProperty(KEY_MAIN_REPORTS_DIR);
            if (!verifyReportsDir(reportsDir)) {
                reportsDir = getProperty(KEY_MAIN_DIR + File.separator + "reports"); //NOI18N
            }
        }
        return reportsDir;
    }

    public String userReportsDir() {
        return getProperty(KEY_USER_REPORTS_DIR);
    }

    public String mainReportsDir() {
        return getProperty(KEY_MAIN_REPORTS_DIR);
    }

    private static boolean verifyReportsDir(String reportsDir) {
        boolean isValid = false;
        File reportsDirFile = null;
        if (reportsDir != null) {
            try {
                reportsDirFile = new File(reportsDir);
                if (reportsDirFile.exists() && reportsDirFile.isDirectory() && reportsDirFile.canRead()) {
                    String[] files = reportsDirFile.list(new FilenameFilter() {

                        public boolean accept(File f, String name) {
                            return name.endsWith(".jasper"); //NOI18N
                        }
                    });
                    if (files != null && files.length > 0) {
                        isValid = true;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return isValid;
    }

    public boolean loadProperties(String filename) {
        log.debug("Parameters: Will try loading properties file: " + filename); //NOI18N
        BufferedInputStream in = null;
        File propertiesFile;
        if (filename == null) {
            propertiesFile = new File(Parameters.USER_FILENAME);
        } else {
            propertiesFile = new File(filename);
        }

        try {
            in = new BufferedInputStream(new FileInputStream(propertiesFile));
            getProperties().load(in);
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveProperties(String filename) {
        BufferedOutputStream out = null;
        File propertiesFile;
        if (filename == null) {
            propertiesFile = new File(Parameters.USER_FILENAME);
        } else {
            propertiesFile = new File(filename);
        }
        try {
            out = new BufferedOutputStream(new FileOutputStream(propertiesFile));
            getProperties().store(out, null);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isDesktopVersion() {
        return false;
    }
    
    private static final Log log = LogFactory.getLog(ServerAdapter.class);    
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/utils/Bundle");
    
    /**
     *Chiavi per i valori memorizzati nel file di configurazione dell'utente.
     */
    /**
     * Nome chiave utente amministratore. L'utente che installa il programma.
     * NOT USED !!!
     * Sample value: <code>root</code> oppure <code>Administrator</code>
     */
    public static final String KEY_ROOT_USER_NAME = "root.user.name"; //NOI18N
    /**
     * Nome chiave cartella di installazione del programma.
     * Sample value: <code>C:\Programmi\MyRent</code>
     */
    public static final String KEY_MAIN_DIR = "main.dir"; //NOI18N
    /**
     * Nome chiave cartella di configurazione del programma
     * Sample value: <code>C:\Programmi\MyRent\jar\myrent</code> o <code>/home/utente/MyRent/jar/.myrent</code>
     */
    public static final String KEY_MAIN_CONFIG_DIR = "main.config.dir"; //NOI18N
    /**
     * Nome chiave file Reg.dat o comunque il file contenente i dati di connessione al DB per l'installazione.
     * Sample value: <code>C:\Programmi\MyRent\jar\myrent\Reg.dat</code> o <code>/opt/MyRent/jar/.myrent/Reg.dat</code>
     */
    public static final String KEY_MAIN_CONFIG_REG = "main.config.reg"; //NOI18N
    /**
     * Nome chiave file Azienda.dat per l'installazione. Il file contenente i dati di attivazione.
     * Sample value: <code>C:\Programmi\MyRent\jar\myrent\Azienda.dat</code> o <code>/opt/MyRent/jar/.myrent/Azienda.dat</code>
     */
    public static final String KEY_MAIN_CONFIG_AZIENDA = "main.config.azienda"; //NOI18N
    /**
     * Nome chiave percorso completo del file di configurazione di hibernate.
     * Sample value: <code>C:\Programmi\MyRent\reports\hibernate.cfg.xml</code>
     */
    public static final String KEY_MAIN_CONFIG_HIBERNATE = "main.config.hibernate"; //NOI18N
    /**
     * Nome chiave cartella dei reports del programma
     * Sample value: <code>C:\Programmi\MyRent\reports</code>
     */
    public static final String KEY_MAIN_REPORTS_DIR = "main.reports.dir"; //NOI18N
    /**
     * Nome chiave nome utente.
     */
    public static final String KEY_USER_NAME = "user.name"; //NOI18N
    /**
     * Nome chiave cartella corrente.
     */
    public static final String KEY_USER_DIR = "user.dir"; //NOI18N
    /**
     * Nome chiave home utente.
     * Sample value: <code>C:\Dati Applicazioni\<Nome utente></code> o <code>/home/<nome utente></code>
     */
    public static final String KEY_USER_HOME = "user.home"; //NOI18N
    /**
     * Nome chiave cartella di configurazione utente
     * Sample value: <code>C:\Dati Applicazioni\myrent</code> o <code>/home/utente/.myrent</code>
     */
    public static final String KEY_USER_CONFIG_DIR = "user.config.dir"; //NOI18N
    /**
     * Nome chiave file Reg.dat o comunque il file contenente i dati di connessione al DB per l'utente.
     * Sample value: <code>C:\Dati Applicazioni\myrent\Reg.dat</code> o <code>/home/utente/.myrent/Reg.dat</code>
     */
    public static final String KEY_USER_CONFIG_REG = "user.config.reg"; //NOI18N
    /**
     * Nome chiave file Azienda.dat per l'utente. Il file contenente i dati di attivazione.
     * Sample value: <code>C:\Dati Applicazioni\myrent\Azienda.dat</code> o <code>/home/utente/.myrent/Azienda.dat</code>
     */
    public static final String KEY_USER_CONFIG_AZIENDA = "user.config.azienda"; //NOI18N
    /**
     * Nome chiave cartella dei reports dell'utente.
     * Sample value: <code>C:\Dati Applicazioni\myrent\reports</code>
     */
    public static final String KEY_USER_REPORTS_DIR = "user.reports.dir"; //NOI18N
    /**
     * Nome chiave immagini per reports di stampa
     */
    public static final String KEY_JASPER_IMAGE_DIR = Parameters.KEY_JASPER_IMAGE_DIR;
    
}
