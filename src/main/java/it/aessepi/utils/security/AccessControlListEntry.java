/*
 * AclEntryImpl.java
 *
 * Created on 06 aprilie 2005, 17:20
 */

package it.aessepi.utils.security;

import java.io.Serializable;
import java.util.Enumeration;
import java.security.acl.AclEntry;
import java.security.Principal;
import java.security.acl.Permission;

/**
 *
 * @author jamess
 */
public class AccessControlListEntry implements AclEntry, Serializable{
    private Principal principalUtente = null;
    private boolean neg = false;    
    private Object resource = null;
    private PermissionAction permessi = null;
    
    /** Creates a new instance of AclEntryImpl */
    public AccessControlListEntry(AccessControlListEntry i){
        setPrincipal(i.getPrincipal());
        permessi = new PermissionAction(0);        
        setResource(i.getResource());
        
        for (Enumeration en = i.permissions(); en.hasMoreElements();){
            addPermission((Permission)en.nextElement());
        }
        if (i.isNegative()) setNegativePermissions();
    }
    
    public AccessControlListEntry(){
        principalUtente = null;
        permessi = new PermissionAction(0);
    }
    
    public AccessControlListEntry(Principal p){
        principalUtente = p;
        permessi = new PermissionAction(0);
    }
    
    public Object clone() {
        return (Object) new AccessControlListEntry(this);
    }
    
    public boolean isNegative(){
        return neg;
    }
    
    public boolean addPermission(Permission perm){
        if (checkPermission(perm)) return false;
        permessi = permessi.or((PermissionAction)perm);
        return true;
    }
    
    
    public boolean removePermission(Permission perm){
        PermissionAction newPerm = permessi.minus((PermissionAction)perm);
        if(newPerm.equals(permessi)) {
            return false;
        } else {
            permessi = newPerm;
            return true;
        }
    }
    
    public boolean checkPermission(Permission perm){
        return permessi.and((PermissionAction)perm).equals(perm);
    }
    
    public Enumeration permissions(){
        return new Enumeration() {
            boolean hasMoreElements = true;
            public boolean hasMoreElements() {
                return hasMoreElements;
            }
            public Object nextElement() {
                hasMoreElements = false;
                return permessi;
            }
        };
    }
    
    public void setNegativePermissions(){
        neg = true;
    }
    
    public Principal getPrincipal(){
        return principalUtente;
    }
    
    public boolean setPrincipal(Principal p) {
        if (principalUtente != null)
            return false;
        principalUtente = p;
        return true;
    }
    
    public String toString(){
        return "AclEntry:"+principalUtente.toString(); //NOI18N
    }
    
    public boolean checkResource(Object resource){
        return getResource() != null && getResource().equals(resource);
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }
    
}