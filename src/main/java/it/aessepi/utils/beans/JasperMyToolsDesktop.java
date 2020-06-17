/*
 *  JasperMyToolsDesktop.java
 *
 *  Created on November 8, 2003, 11:38 AM
 */
package it.aessepi.utils.beans;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.HibernateBridge;
import it.aessepi.utils.Parameters;
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
import java.awt.Desktop;


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

public class JasperMyToolsDesktop {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/utils/beans/Bundle");
    private static final Log log = LogFactory.getLog(JasperMyToolsDesktop.class);
    private static Desktop desktop;


   

   
//    private void impostaProprieta() {
//        System.setProperty(
//                "org.xml.sax.driver",
//                "org.apache.xerces.parsers.SAXParser"
//                );
//        
//        System.setProperty(
//                "jasper.reports.compilation.xml.validation",
//                "false"
//                );
//    }

    public static String[] userReportLanguages(String basename) {
        return reportLanguages(JasperMyTools.percorsoCompleto.substring(0, JasperMyTools.percorsoCompleto.length() - 1), basename);
    }

    public static String[] mainReportLanguages(String basename) {
        return reportLanguages(JasperMyTools.percorsoCompleto.substring(0, JasperMyTools.percorsoCompleto.length() - 1), basename);
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
        return JasperMyTools.percorsoCompleto + basename + ".jasper"; //NOI18N
    }

    public static String nomeFileXML(String basename) {
        return JasperMyTools.percorsoCompleto + basename + ".xml"; //NOI18N
    }

    public static String nomeFileJRPrint(String basename) {
        return JasperMyTools.percorsoCompleto + basename + ".jrprint"; //NOI18N
    }

   
   

    private static void apriPDF(Component parent,String percorsoViewerPDF, File filePDF) throws IOException, InterruptedException {
        if (percorsoViewerPDF != null) {
            File viewerPDF = new File(percorsoViewerPDF);
            if (viewerPDF.exists()) {
                run(percorsoViewerPDF, filePDF.getCanonicalPath());
                try {
                    filePDF.delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            if (isDesktopSupported(Desktop.Action.OPEN)) {
                desktop.open(filePDF);
            } else {
                String msg = "Funzionalita' non supportata.\nImpostare un programma di lettura pdf nelle preferenze del MyRent\noppure usare il visualizzatore interno";
                JOptionPane.showMessageDialog(parent, msg, "Stampa", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }

    public static int run(String cmd, String arg) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(new String[]{cmd, arg});
        process.waitFor();
        return process.exitValue();
    }

   

    private static boolean isDesktopSupported(Desktop.Action action) {
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            if (desktop.isSupported(action)) {
               return true;
            }
        }

        return false;
    }
}
