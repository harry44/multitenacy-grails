/*
 * AclImpl.java
 *
 * Created on 06 aprilie 2005, 17:08
 */

package it.aessepi.utils.security;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.Parameters;
import it.aessepi.utils.db.User;
import java.awt.Component;
import java.util.Vector;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.NotOwnerException;
import java.security.Principal;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author jamess
 */
public class AccessControlList extends OwnerList implements Acl, Serializable{
    
    private Vector entryList = null;
    private String aclName = null;
    private static AccessControlList acl;
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/utils/security/Bundle");
    
    /** Creates a new instance of AclImpl */
    private AccessControlList(PrincipalUtente owner, String name) {
        super(owner);
        entryList = new Vector();
        aclName = name;
    }
    
    private AccessControlList() {
        
    }
    
    public void setName(Principal caller, String name)
    throws NotOwnerException {
        if (!isOwner(caller))
            throw new NotOwnerException();
        aclName = name;
    }
    
    public String getName(){
        return aclName;
    }
    
    public boolean addEntry(Principal caller, AclEntry entry)
    throws NotOwnerException {
        if (!isOwner(caller))
            throw new NotOwnerException();
        
        if (entryList.contains(entry))
            return false;
        
        entryList.addElement(entry);
        return true;
    }
    
    public boolean removeEntry(Principal caller, AclEntry entry)
    throws NotOwnerException {
        if (!isOwner(caller))
            throw new NotOwnerException();
        
        return (entryList.removeElement(entry));
    }
    
    public void removeAll(Principal caller)
    throws NotOwnerException {
        if (!isOwner(caller))
            throw new NotOwnerException();
        entryList.removeAllElements();
    }
    
    public Enumeration getPermissions(Principal user){
        Vector empty = new Vector();
        for (Enumeration e = entryList.elements();e.hasMoreElements();){
            AclEntry ent = (AclEntry) e.nextElement();
            if (ent.getPrincipal().equals(user))
                return ent.permissions();
        }
        return empty.elements();
    }
    
    public Enumeration entries(){
        return entryList.elements();
    }
    
    public boolean checkPermission(Principal user, Permission perm) {
        for (Enumeration e = entryList.elements();e.hasMoreElements();){
            AclEntry ent = (AclEntry) e.nextElement();
            if (ent.getPrincipal().equals(user))
                if (ent.checkPermission(perm)) return true;
        }
        return false;
    }
    
    public boolean checkPermission(Principal user, Object resource, Permission perm) {
        for (Enumeration e = entryList.elements();e.hasMoreElements();){
            AccessControlListEntry ent = (AccessControlListEntry) e.nextElement();
            if (ent.getPrincipal().equals(user))
                if (ent.checkPermission(perm) && ent.checkResource(resource)) return true;
        }
        return false;
    }
    
    public boolean checkResource(Object resource) {
        for (Enumeration e = entryList.elements();e.hasMoreElements();){
            AccessControlListEntry ent = (AccessControlListEntry) e.nextElement();
            if (ent.checkResource(resource)) return true;
        }
        return false;
    }
    
    public String toString(){
        return ("AclImpl: "+ getName()); //NOI18N
    }
    
    public static AccessControlList createAccessControlList(User u) throws NotOwnerException{
        PrincipalUtente p = new PrincipalUtente(u);
        acl = new AccessControlList(p, bundle.getString("AccessControlList.msgAccessControlList"));
        Map permessi = u.getPermessi();
        if(permessi != null) {
            Collection resources = permessi.keySet();
            Iterator it = resources.iterator();
            while(it.hasNext()) {
                Object resource = it.next();
                AccessControlListEntry aclEntry = new AccessControlListEntry(p);
                aclEntry.setResource(resource);
                Integer perms = (Integer)permessi.get(resource);
                if(perms != null) {
                    aclEntry.addPermission(new PermissionAction(perms.intValue()));
                }
                acl.addEntry(p, aclEntry);
            }
        }
        
        return acl;
    }
    
    public static boolean checkPermission(Component c, Object resource, Permission perm) {
        return checkPermission(c, resource, perm, false);
    }
    
    public static boolean checkPermission(Component c, Object resource, Permission perm, boolean silent) {
        if(!acl.checkPermission(new PrincipalUtente(Parameters.getUser()), resource, perm)) {
            if (!silent){
                showErrorMessage(c, resource, perm);
            }
            return false;
        }
        return true;
    }
    
    public static void showErrorMessage(Component c, Object resource, Permission perm) {
        JOptionPane.showMessageDialog(
                c,
                MessageFormat.format(bundle.getString("AccessControlList.msgPermessiNonPosseduti0Risorsa1"), perm, resource),
                bundle.getString("AccessControlList.msgAccessoNegato"),
                JOptionPane.ERROR_MESSAGE,
                new ImageIcon(AccessControlList.class.getResource("/it/aessepi/utils/images/access_denied_48px.png"))); //NOI18N
    }
    
}
