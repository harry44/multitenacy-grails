/*
 * Version.java
 *
 * Created on 16 februarie 2005, 10:00
 */

package it.myrent.ee.db;

import java.io.Serializable;

/**
 *
 * @author  jamess
 */
public class Version implements Serializable{
    
    /** Creates a new instance of Version */
    public Version() {
        
    }    
    
    public Version(String version) {
        this.version = version;
    }    
    private String version;
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getVersion() {
        return version;
    }
    
}
