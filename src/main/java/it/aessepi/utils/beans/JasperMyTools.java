/*
 *  JasperMyTools.java
 *
 *  Created on November 8, 2003, 11:38 AM
 */
package it.aessepi.utils.beans;

import it.aessepi.utils.Parameters;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JasperMyTools {

    private static final Log log = LogFactory.getLog(JasperMyTools.class);
    public static String percorsoCompleto;

    public JasperMyTools(JFrame parent, String nomeFile, Map parameters, Object[] beanArray) {
       
    }

    public JasperMyTools(JDialog parent, String nomeFile, Map parameters, Object[] beanArray) {
       

    }

    public JasperMyTools(JFrame parent, String nomeFile1, String nomeFile2, Map parameters, Object[] beanArray) {
       
    }

    public JasperMyTools(JDialog parent, String nomeFile1, String nomeFile2, Map parameters, Object[] beanArray) {
        
    }
    public JasperMyTools(JDialog parent, String nomeFile1, String nomeFile2,String nomeFile3, Map parameters, Object[] beanArray) {
       
    }


    /* costruttori con altri tipi di files */
    public JasperMyTools(JFrame parent, File f,String fileContent, String fileExtension) {
       
    }

    public JasperMyTools(JDialog parent, File f,String fileContent, String fileExtension) {
       
    }

    public JasperMyTools(JFrame parent,String fileContent, String fileExtension) {
        
    }

    public JasperMyTools(JDialog parent, String fileContent, String fileExtension) {
       
    }
    /* end costruttori con altri tipi di files */

    public static String[] userReportLanguages(String basename) {
        if (Parameters.isDesktopVersion()) {
            return JasperMyToolsDesktop.userReportLanguages(basename);
        } else {
            return JasperMyToolsServer.userReportLanguages(basename);
        }
    }


    
    public static String nomeFileJasper(String basename) {
        if(Parameters.isDesktopVersion()) {
            return JasperMyToolsDesktop.nomeFileJasper(basename);
        } else {
            return JasperMyToolsServer.nomeFileJasper(basename);
        }
    }

    public static String nomeFileJasperUserDir(String basename) {
        if(Parameters.isDesktopVersion()) {
            return JasperMyToolsDesktop.nomeFileJasper(basename);
        } else {
            return JasperMyToolsServer.nomeFileJasperUserDir(basename);
        }
    }
}
