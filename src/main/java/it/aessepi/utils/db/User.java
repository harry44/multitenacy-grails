/*
 * UserInterface.java
 *
 * Created on 06 aprilie 2005, 18:00
 */

package it.aessepi.utils.db;

import java.util.Map;

/**
 *
 * @author jamess
 */
public interface User extends PersistentInstance {
    
    public void setId(Integer id);
    public void setUserName(String userName);
    public String getUserName();
    public void setPassword(String password);
    public String getPassword();
    public void setNomeCognome(String nomeCognome);
    public String getNomeCognome();
    public String toString();
    public boolean equals(Object other);
    public int hashCode();
    public Map getPermessi();
    public void setPermessi(Map permessi);
}
