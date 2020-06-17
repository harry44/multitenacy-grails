/*
 *  JasperMyToolsServer.java
 *
 *  Created on November 8, 2003, 11:38 AM
 */
package it.aessepi.utils.beans;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.Parameters;
import it.aessepi.utils.ServerAdapter;
import javax.swing.JOptionPane;

/**
 * import dori.jasper.engine.data.JRBeanArrayDataSource;
 * import dori.jasper.engine.*;
 * import dori.jasper.engine.design.*;
 * import dori.jasper.engine.export.*;
 * import dori.jasper.engine.util.*;
 * import it.aessepi.utils.beans.JasperViewer;
 */
//import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import java.awt.Component;

import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JasperMyToolsServer {

    private static final Log log = LogFactory.getLog(JasperMyToolsServer.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/utils/beans/Bundle");

    

    private static void impostaProprieta() {
        //TODO Servono piu' queste cose? Chi fa il parsing anymore?
//        System.setProperty(
//                "org.xml.sax.driver", //NOI18N
//                "org.apache.xerces.parsers.SAXParser" //NOI18N
//                );

//        System.setProperty(
//                "jasper.reports.compilation.xml.validation", //NOI18N
//                "false" //NOI18N
//                );
    }

    public static String reportsDir(String basename) {
        String reportsDir = Parameters.getProperty(ServerAdapter.KEY_USER_REPORTS_DIR);
        if (!containsReport(reportsDir, basename)) {
            reportsDir = Parameters.getProperty(ServerAdapter.KEY_MAIN_REPORTS_DIR);
            if (!containsReport(reportsDir, basename)) {
                reportsDir = null;//Parameters.getProperty(Parameters.KEY_MAIN_DIR + File.separator + "reports");
            }
        }
        return reportsDir;
    }

    private static boolean containsReport(String reportsDir, final String basename) {
        boolean isValid = false;
        File reportsDirFile = null;
        if (reportsDir != null) {
            try {
                reportsDirFile = new File(reportsDir);
                if (reportsDirFile.exists() && reportsDirFile.isDirectory() && reportsDirFile.canRead()) {
                    String[] files = reportsDirFile.list(new FilenameFilter() {

                        public boolean accept(File f, String name) {
                            return name.equalsIgnoreCase(basename + ".jasper"); //NOI18N
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

    public static String[] userReportLanguages(String basename) {
        return reportLanguages(Parameters.userReportsDir(), basename);
    }

    public static String[] mainReportLanguages(String basename) {
        return reportLanguages(Parameters.mainReportsDir(), basename);
    }

    public static String[] reportLanguages(String reportsDir, final String basename) {
        String[] filenames = null;
        File reportsDirFile = null;
        if (reportsDir != null) {
            try {
                reportsDirFile = new File(reportsDir);
                if (reportsDirFile.exists() && reportsDirFile.isDirectory() && reportsDirFile.canRead()) {
                    filenames = reportsDirFile.list(new FilenameFilter() {

                        public boolean accept(File f, String name) {
                            if (name.startsWith(basename) && name.endsWith(".jasper") && name.length() == (basename.length() + 10)) { //NOI18N
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (filenames != null && filenames.length > 0) {
            for (int i = 0; i < filenames.length; i++) {
                filenames[i] = filenames[i].substring(basename.length() + 1, basename.length() + 3);
            }
        }
        return filenames;
    }

    public static String nomeFileJasper(String basename) {
        String cartellaReports = reportsDir(basename);
        if (cartellaReports != null) {
            return cartellaReports + File.separator + basename + ".jasper"; //NOI18N
        } else {
            return basename + ".jasper"; //NOI18N
        }
    }

    public static String nomeFileJasperUserDir(String basename) {
        String cartellaReports = Parameters.getProperty(ServerAdapter.KEY_USER_REPORTS_DIR);
        if (cartellaReports != null) {
            return cartellaReports + File.separator + basename + ".jasper"; //NOI18N
        } else {
            return basename + ".jasper"; //NOI18N
        }
    }

  
}
