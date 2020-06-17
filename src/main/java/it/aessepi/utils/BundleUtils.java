/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.aessepi.utils;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * This class is useful to have only one instance per ResourceBundle object. 
 * 
 * @author Mauro Chiarugi
 * 
 */
public class BundleUtils {

    static HashMap<String, ResourceBundle> bundles = new HashMap<String, ResourceBundle>();
    
    public static ResourceBundle getBundle(String bundleName) {
        ResourceBundle bundle = bundles.get(bundleName);
        if(bundle == null) {
            bundle = ResourceBundle.getBundle(bundleName);
            bundles.put(bundleName, bundle);
        }
        return bundle;
    }
}
