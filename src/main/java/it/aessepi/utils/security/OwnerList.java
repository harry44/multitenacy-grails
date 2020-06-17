/*
 * OwnerImpl.java
 *
 * Created on 06 aprilie 2005, 16:33
 */

package it.aessepi.utils.security;

import java.io.Serializable;
import java.util.Vector;
import java.security.acl.Owner;
import java.security.acl.NotOwnerException;
import java.security.acl.LastOwnerException;
import java.security.Principal;

/**
 *
 * @author jamess
 */
public class OwnerList implements Owner, Serializable {
    
    private Vector ownerList = null;
    
    /** Creates a new instance of OwnerImpl */
    public OwnerList() {
        ownerList = new Vector();
    }
    
    public OwnerList(Principal owner) {
        ownerList = new Vector();
        ownerList.addElement(owner);
    }

    public boolean addOwner(Principal caller, Principal owner) throws java.security.acl.NotOwnerException {
        
        if(!ownerList.contains(caller))
            throw new NotOwnerException();
        
        if (ownerList.contains(owner)) {
            return false;
        } else {
            ownerList.addElement(owner);
            return true;
        }
    }

    public boolean deleteOwner(Principal caller, Principal owner) throws java.security.acl.NotOwnerException, java.security.acl.LastOwnerException {
        if(!ownerList.contains(caller))
            throw new NotOwnerException();
        
        if (!ownerList.contains(owner)){
            return false;
        } else {
            if (ownerList.size() == 1)
                throw new LastOwnerException();
            
            ownerList.removeElement(owner);
            return true;
        }
    }

    public boolean isOwner(Principal owner) {
        return ownerList.contains(owner);
    }
    
}
