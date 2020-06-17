/*
 * Messages.java
 *
 * Created on 10 octombrie 2005, 14:27
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package it.myrent.ee.api.exception;

import java.util.ResourceBundle;

/**
 *
 * @author jamess
 */
public class ErrorMessage {
    
    /** Creates a new instance of Messages */
    private ErrorMessage() {
    }
    
    public static String get(String key) {
        return ResourceBundle.getBundle("it/myrent/ee/api/exception/Bundle").getString(key);
    }
    
}
