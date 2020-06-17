/*
 * FileUtils.java
 *
 * Created on 08 iulie 2005, 17:17
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package it.aessepi.utils;

import it.aessepi.utils.NetworkAdapterSerial;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosPrincipal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jamess
 */
public class FileUtils {
    //estensioni ammesse per StampaClient oltre a quelle per .jrprint e firmacerta
    public static final String ESTENSIONE_HTML = "html";
    public static final String ESTENSIONE_PDF = "pdf";
    public static final String ESTENSIONE_XLS = "xls";
    public static final String ESTENSIONE_XLSX = "xlsx";
    public static final String ESTENSIONE_TXT = "txt";
    public static final String ESTENSIONE_GIF = "gif";
    public static final String ESTENSIONE_PNG = "png";
    public static final String ESTENSIONE_JPG1 = "jpg";
    public static final String ESTENSIONE_JPG2 = "jpeg";
    public static final String ESTENSIONE_BMP = "bmp";
    public static final String ESTENSIONE_TIFF = "tiff";

    /** Creates a new instance of FileUtils */
    private FileUtils() {
    }
    private static Log log = LogFactory.getLog(FileUtils.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/utils/Bundle");
    // Copies all files under srcDir to dstDir.
    // If dstDir does not exist, it will be created.
    public static void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }
            
            String[] children = srcDir.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(srcDir, children[i]), new File(dstDir, children[i]));
            }
        } else {
            copyFile(srcDir, dstDir);
        }
    }
    
    public static void copyDirectory(String srcDir, String dstDir) throws IOException{
        File src = new File(srcDir);
        File dst = new File(dstDir);
        copyDirectory(src, dst);
    }
    
    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    public static boolean copyFileToFileSystem(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }

        return true;
    }

    
    /**
     * conta quanti files ci sono in una partilare directory. Non vengono considerate le directory.
     * <br>
     * L'elenco dei files viene preso con il metodo listFles()
     * @param files array di files
     * @param estensione l'estensione del file da verificare. Se null allora verifica tutti i files
     * @return il numero dei SOLI files
     */
    public static int contaSoloFilesInDirectory(File[] files, String estensione) {
        int counter = 0;

        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isFile() && !f.isDirectory()) {
                if (estensione != null) {
                    //determina il punto prima dell'estensione
                    int index = f.getName().lastIndexOf(".");
                    if (index != -1) {
                        String ext = f.getName().substring(index + 1, f.getName().length());
                        if (ext.toLowerCase().equals(estensione.toLowerCase())) {
                            counter++;
                        }
                    }
                } else {
                    counter++;
                }
            }
        }

        return counter;
    }

    public static long fileSize(File dir) {
        long initSize = 0L;
        initSize = initSize + dir.length();
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                initSize = initSize + fileSize(new File(dir, children[i]));
            }
        }
        return initSize;
    }
    
    public static void compress(String outFilename, String inFilename, boolean moveFiles) throws IOException{
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(outFilename));
        
        // Open the input file
        FileInputStream in = new FileInputStream(inFilename);
        
        // Transfer bytes from the input file to the GZIP output stream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        
        // Complete the GZIP file
        out.finish();
        out.close();
        if(moveFiles) {
            File f = new File(inFilename);
            f.delete();
        }
    }
    
    public static void decompress(String inFilename, String outFilename) throws IOException{
        
        // Open the compressed file
        GZIPInputStream in = new GZIPInputStream(new FileInputStream(inFilename));
        
        // Open the output file
        OutputStream out = new FileOutputStream(outFilename);
        
        // Transfer bytes from the compressed file to the output file
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        
        // Close the file and stream
        in.close();
        out.close();
    }
    
    private static SealedObject encryptObject(Serializable data, String serial) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException {
        KerberosPrincipal KerPri=new KerberosPrincipal("pippo@pluto.com"); //NOI18N
        KerberosKey Key=new KerberosKey(KerPri, Arrays.copyOf(serial.toCharArray(),50), "DES"); //NOI18N
        Cipher ci=Cipher.getInstance("DES"); //NOI18N
        ci.init(Cipher.ENCRYPT_MODE,Key);
        SealedObject so = new SealedObject(data, ci);
        return so;
    }
    
    public static Object decryptObject(SealedObject encryptedObject, String serial) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException {
        KerberosPrincipal KerPr=new KerberosPrincipal("pippo@pluto.com"); //NOI18N
        KerberosKey ch1=new KerberosKey(KerPr, Arrays.copyOf(serial.toCharArray(), 50), "DES"); //NOI18N
        return encryptedObject.getObject(ch1);
    }

    public static Object readEncryptedFile(File f) {
        return SecureObjectInputStream.readEncryptedFile(f);
    }

    public static Object readEncryptedFile(String fileName){
        File f = new File(fileName);
        return readEncryptedFile(f);
    }



    /*public static Object readFile(String fileName){
        File f = new File(fileName);
        return readFile(f);
    }*/
    
    public static boolean saveEncryptedFile(File f, Serializable data) {
        SealedObject encryptedObject = null;
        try {
            encryptedObject = encryptObject(data, NetworkAdapterSerial.getInstance().getCurrentSerial());
        } catch (Exception e) {
            log.error("Exception encountered while encrypting file " + f.getName()); //NOI18N
            log.debug("Exception encountered while encrypting file " + f.getName(), e); //NOI18N
        }        
        if(encryptedObject != null) {
            return saveFile(f, encryptedObject);
        } else {
            return false;
        }
    }
    
    public static boolean saveEncryptedFile(String fileName, Serializable data) {
        return saveEncryptedFile(new File(fileName), data);
    }
    
    public static boolean saveFile(File f, Serializable data) {
        try{
            f.getCanonicalFile().getParentFile().mkdirs();
            ObjectOutputStream out;
            out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(data);
            out.close();
        }catch(Exception e){
            log.error("Exception encountered while saving file " + f.getName()); //NOI18N
            log.debug("Exception encountered while saving file " + f.getName(), e); //NOI18N
            return false;
        }
        return true;
    }
    
    public static boolean saveFile(String fileName, Serializable data) {
        return saveFile(new File(fileName), data);
    }
    
    public static boolean deleteFile(String fileName){
        boolean retValue = true;
        if (fileName != null && !fileName.equals("")){ //NOI18N
            try{
                File f = new File(fileName);
                if(f.exists()) {
                    f.delete();
                }
            }catch(Exception ex){
                retValue = false;
                ex.printStackTrace();
            }
        }
        return retValue;
    }
    
    public static boolean removeAllFilesFromDir(String directory, boolean creaSeNonEsiste){
        boolean retValue = true;
        if (directory != null && !directory.equals("")){ //NOI18N
            try {
                File dirFile = new File(directory);
                if (dirFile.exists() && dirFile.isDirectory()){
                    File[] fileArray = dirFile.listFiles();
                    for (int i = 0; i < fileArray.length; i++){
                        fileArray[i].delete();
                    }
                } else {
                    if (creaSeNonEsiste){
                        dirFile.mkdirs();
                    }
                }
            } catch (Exception ex) {
                retValue = false;
                ex.printStackTrace();
            }
        }
        return retValue;
    }
    
    
    public static boolean removeDir(String directory, boolean onlyIfEmpyt){
        boolean retValue = true;
        if (directory != null && !directory.equals("")){ //NOI18N
            try {
                File dirFile = new File(directory);
                if (dirFile.exists() && dirFile.isDirectory()){
                    File[] fileArray = dirFile.listFiles();
                    if (fileArray != null && fileArray.length > 0){
                        if (!onlyIfEmpyt){
                            for (int i = 0; i < fileArray.length; i++){
                                fileArray[i].delete();
                            }
                            dirFile.delete();
                        }
                    } else {
                        dirFile.delete();
                    }
                }
            } catch (Exception ex) {
                retValue = false;
                ex.printStackTrace();
            }
        }
        return retValue;
    }    

    public static List extensionFiles() {
        List extensions = new ArrayList();

        extensions.add(ESTENSIONE_HTML);
        extensions.add(ESTENSIONE_PDF);
        extensions.add(ESTENSIONE_XLS);
        extensions.add(ESTENSIONE_TXT);
        extensions.add(ESTENSIONE_GIF);
        extensions.add(ESTENSIONE_PNG);
        extensions.add(ESTENSIONE_JPG1);
        extensions.add(ESTENSIONE_JPG2);
        extensions.add(ESTENSIONE_BMP);
        extensions.add(ESTENSIONE_TIFF);
        extensions.add(ESTENSIONE_XLSX);

        return extensions;
    }


}
