/*
 * PrincipalImpl.java
 *
 * Created on 06 aprilie 2005, 16:59
 */

package it.aessepi.utils.security;

import java.security.Principal;
import java.io.Serializable;
import it.aessepi.utils.db.User;

/**
 * Principalimpl represents a user
 * @author jamess
 */
public class PrincipalUtente implements Principal, Serializable {
    private User user = null;
    /** Creates a new instance of PrincipalImpl */
    private PrincipalUtente() {
    }
    
    public PrincipalUtente(User user) {
        this.user = user;
    }
    public String getName() {
        return user.getUserName();
    }
    
    public boolean equals(Object a) {
        if (a instanceof PrincipalUtente){
            return (user.equals(((PrincipalUtente)a).getUser()));            
        } else {
            return false;
        }
    }
    
    public int hashCode(){
        return user.hashCode();
    }
    
    public String toString() {
        return ("PrincipalImpl :"+user.toString()); //NOI18N
    }
    
    public User getUser() {
        return user;
    }
    
}
