/*
 * Dencrypter.java
 *
 * Created on 14 decembrie 2006, 12:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.dogmasystems;

import java.io.UnsupportedEncodingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.security.auth.kerberos.KerberosKey;
import javax.security.auth.kerberos.KerberosPrincipal;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author jamess
 */
public class DESEncrypter {
    
    Cipher ecipher;
    Cipher dcipher;
    SecretKey key;
    
    public DESEncrypter(String passPhrase) {
        try {
            KerberosPrincipal KerPr = new KerberosPrincipal("pippo@pluto.com"); //NOI18N
            KerberosKey ch1 = new KerberosKey(KerPr,passPhrase.toCharArray(),"DES"); //NOI18N
            key = ch1;
            ecipher = Cipher.getInstance("DES"); //NOI18N
            dcipher = Cipher.getInstance("DES"); //NOI18N
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
            
        } catch (javax.crypto.NoSuchPaddingException e) {
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (java.security.InvalidKeyException e) {
        }
    }
    
    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8"); //NOI18N
            
            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);
            
            // Encode bytes to base64 to get a string
            //return new sun.misc.BASE64Encoder().encode(enc);
            return new String(new Base64().encode(enc));
        } catch (Exception e) {
               System.out.println("exception: "+e);
        }
        return null;
    }
    
    public String decrypt(String str) {
        try {
            // Decode base64 to get bytes
            //byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            byte[] dec = new Base64().decode(str.getBytes("UTF8")); //NOI18N
            
            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);
            
            // Decode using utf-8
            return new String(utf8, "UTF8"); //NOI18N
        } catch (Exception e) {
        }
        return null;
    }    
}
