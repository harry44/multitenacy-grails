/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.utils;

import static it.aessepi.utils.FileUtils.decryptObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.crypto.SealedObject;

/**
 *
 * @author mauro
 */
public class SecureObjectInputStream extends ObjectInputStream{
    

    public SecureObjectInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
    }
    private static Object readFile(String fileName){
        File f = new File(fileName);
        return readFile(f);
    }
    
    private static Object readFile(File f){
        Object retValue = null;
        //This method is here as history
        /*
        try{
            if(f != null && f.exists()){
                ObjectInputStream in = new SecureObjectInputStream(new FileInputStream(f));
                retValue = in.readObject();
                in.close();
            }
        }catch(Exception e){
            e.printStackTrace();
            retValue = null;
        }*/
        return retValue;
    }
    public static Object readEncryptedFile(File f) {

        // This method is here only as history... it doesn't do anything - not used anymore

        Object retValue = null;
       /* Object encryptedObject = readFile(f);
        if (encryptedObject != null) {
            try {
                Set<Map.Entry<String, String>> entries = null;
                //entries = NetworkAdapterSerial.getInstance().getSerials().entrySet(); Not required for the web
                Iterator it = entries.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
                    try {
                        retValue = decryptObject((SealedObject) encryptedObject, entry.getValue());
                    } catch (Exception ex) {
                        System.out.println("FileUtils.readEncryptedFile: INTERNO DEL CICLO WHILE: Exception encountered while decrypting file " + f.getName()); //NOI18N
                        ex.printStackTrace();
                    }
                }

            } catch (Exception e) {
                System.out.println("FileUtils.readEncryptedFile: Try/Catch SCHEDA DI RETE: Exception encountered while decrypting file " + f.getName()); //NOI18N
                e.printStackTrace();
            }
            if (retValue == null) {
                try {
                    //retValue = decryptObject((SealedObject) encryptedObject, HddSerial.getInstance().getCurrentSerial()); Not required for the web
                } catch (Exception e) {
                    System.out.println("FileUtils.readEncryptedFile: PROVA A DECRIPTARE IL SERIALE DEL DISCO: Exception encountered while decrypting file " + f.getName()); //NOI18N
                    e.printStackTrace();
                }
            }
        }*/
        return retValue;
    }

    /**
     * Only deserialize instances of our expected Bicycle class
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException,
            ClassNotFoundException {
       
        return super.resolveClass(desc);
        
    }
    
    
    
}
