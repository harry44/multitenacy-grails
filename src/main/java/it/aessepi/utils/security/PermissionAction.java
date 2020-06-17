/*
 * PermissionImpl.java
 *
 * Created on 06 aprilie 2005, 17:25
 */

package it.aessepi.utils.security;

import it.aessepi.utils.BundleUtils;
import java.io.Serializable;
import java.security.acl.Permission;
import java.util.ResourceBundle;

/**
 *
 * @author jamess
 */
public class PermissionAction implements Permission, Serializable {
    
    private int permessi;
    private String descrizione = null;
    
    /** Creates a new instance of PermissionImpl */
    public PermissionAction(int permessi, String descrizione) {
        this.setPermessi(permessi);
        this.setDescrizione(descrizione);
    }
    
    public PermissionAction(int permessi) {
        this.setPermessi(permessi);
        this.setDescrizione(createDescrizione(permessi));
    }
    
    public int hashCode() {
        return getPermessi();
    }
    
    public boolean equals(Object p){
        if (p instanceof PermissionAction){
            return getPermessi() == ((PermissionAction)p).getPermessi();
        } else {
            return false;
        }
    }
    
    public String toString(){
        return getDescrizione();
    }
    
    public int getPermessi() {
        return permessi;
    }
    
    public void setPermessi(int permessi) {
        this.permessi = permessi;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public PermissionAction or(PermissionAction p) {
        if(p == null) {
            p = new PermissionAction(getPermessi());            
        }
        return new PermissionAction(getPermessi() | p.getPermessi());       
    }
    
    public PermissionAction and(PermissionAction p) {
        if(p == null) {
            p = new PermissionAction(0);            
        }
        return new PermissionAction(getPermessi() & p.getPermessi());
    }
    
    public PermissionAction minus(PermissionAction p) {
        if(p == null) {
            p = new PermissionAction(0);
        }
        return new PermissionAction(getPermessi() - (this.and(p)).getPermessi());
    }
    
    private String createDescrizione(int permessi) {
        String retValue = ""; //NOI18N
        if((permessi & 1) == 1) {
            retValue = retValue + READ + " "; //NOI18N
        }
        if((permessi & 2) == 2) {
            retValue = retValue + MODIFY + " "; //NOI18N
        }
        if((permessi & 4) == 4) {
            retValue = retValue + NEW + " "; //NOI18N
        }
        if((permessi & 8) == 8) {
            retValue = retValue + DELETE + " "; //NOI18N
        }
        if((permessi & 16) == 16) {
            retValue = retValue + PRINT + " "; //NOI18N
        }
        return retValue.trim();
    }
    
    public static void main(String[] args) {
        System.out.println(
                READ.or(MODIFY).or(DELETE) + " " + //NOI18N
                READ.or(MODIFY).or(DELETE).getPermessi());
    }
    
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/utils/security/Bundle");
    public static final PermissionAction READ = new PermissionAction(1, bundle.getString("PermissionAction.READ"));
    public static final PermissionAction MODIFY = new PermissionAction(2, bundle.getString("PermissionAction.MODIFY"));
    public static final PermissionAction NEW = new PermissionAction(4, bundle.getString("PermissionAction.NEW"));
    public static final PermissionAction DELETE = new PermissionAction(8, bundle.getString("PermissionAction.DELETE"));
    public static final PermissionAction PRINT = new PermissionAction(16, bundle.getString("PermissionAction.PRINT"));
    public static final PermissionAction ALL = READ.or(MODIFY).or(NEW).or(DELETE).or(PRINT);
}
