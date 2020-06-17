/*
 * Constants.java
 *
 * Created on 03 martie 2005, 15:54
 */

package it.aessepi.utils;
import it.aessepi.utils.beans.JasperMyTools;
import java.util.Properties;
import java.io.File;

/**
 *
 * @author  jamess
 */
public class DesktopAdapter extends ParametersAdapter {
    
    /** Creates a new instance of Constants */
    public DesktopAdapter () {
        try {
            setCurrentDir(new java.io.File("").getCanonicalPath());
        } catch (Exception ex) {
        }
        setProperties(new Properties(System.getProperties()));
    }
    
    
    /**
     *Computes the complete path of the config dir.
     */
    public String userConfigDirPath() {
        String d = null;
        String program = getProgram();
        if(program.equals(Parameters.MYRENTEE) || program.equals(Parameters.MYRENTPRO)) {
            program = Parameters.MYRENT;
        }
        if(System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) {
            d = getCurrentDir() + File.separator + "." + program.toLowerCase();
        } else {
            d = getCurrentDir() + File.separator + program.toLowerCase();
        }
        return d;
    }
    
    /**
     *Computes the complete path of the config dir.
     */
    public String mainConfigDirPath() {
        return userConfigDirPath();
    }
    
    /**
     *Computes the full path of the file containing memorized password.
     */
    public String passwordFilePath() {
        return userConfigDirPath() + File.separator + "User.dat";
    }
    
    /**
     *Computes the full path of the version file.
     */
    public String versionFilePath() {
        return userConfigDirPath() + File.separator + Parameters.VERSION_FILENAME;
    }
    
    
    /**
     *Computes the full path to the program used for retrieving hdd's serial number.
     */
    public String serialNumberProgramPath() {
        return userConfigDirPath() + File.separator + serialNumberProgramName();
    }
    
    /**
     *Computes the full path of the file containing the database connection data.
     */
    public String registryFilePath() {
        return userConfigDirPath() + File.separator + Parameters.REGISTRY_FILENAME;
    }
    
    /**
     *Computes the full path of the file containing registration data and company info.
     */
    public String registrationFilePath() {
        return userConfigDirPath() + File.separator + Parameters.REGISTRATION_FILENAME;
    }
    
    /**
     *Computes the full path of the file containing DB backup data.
     */
    public String backupDBFilePath() {
        return userConfigDirPath() + File.separator + Parameters.BACKUPDB_CFG_FILENAME;
    }
  
       
    public boolean isDesktopVersion() {
        return true;
    }
    
    
    public String reportsDir() {
        String reportsDir = JasperMyTools.percorsoCompleto;
        if(reportsDir != null) {
            //Faccio questo perche' nell'attivazione viene aggiunto il file separator alla fine.
            reportsDir = reportsDir.substring(0, reportsDir.length() - 1);
        } else {
            reportsDir = getCurrentDir();
            int j = reportsDir.indexOf("jar");
            if(j != -1) {
                reportsDir = reportsDir.substring(0, j) + "reports";
            } else {
                reportsDir = reportsDir + File.separator + "reports";
            }
        }
        return reportsDir;
    }
    
    public String userReportsDir() {
        return reportsDir();
    }
    
    public String mainReportsDir() {
        return reportsDir();
    }
}
