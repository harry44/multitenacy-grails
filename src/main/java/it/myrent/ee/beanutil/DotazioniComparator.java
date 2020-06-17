/*
 * DotazioniComparator.java
 *
 * Created on May 8, 2006, 08:35 PM
 */

package it.myrent.ee.beanutil;


import it.aessepi.utils.db.PersistentInstance;
import java.util.Comparator;

/**
 * Compara con <code>toString()</code>. Non ritorna mai zero.
 * @author  jamess 
 */
public class DotazioniComparator implements Comparator{
    
    /** Creates a new instance of DotazioniComparator */
    public DotazioniComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        int retValue = -1;
        if(o1 != null && o2 != null) { 
            retValue = compareToString(o1, o2);
            if(retValue == 0) {
                retValue = comparePersistentInstances((PersistentInstance)o1, (PersistentInstance)o2);
            }
        }
        
        if(retValue == 0) {
            retValue = -1;
        }        
        return retValue;
    }    
    
    public int comparePersistentInstances(PersistentInstance p1, PersistentInstance p2) {
        int retValue = -1;
        if(p1.getId() != null && p2.getId() != null) {
            retValue = p1.getId().compareTo(p2.getId());
        }
        return retValue;
    }
    
    public int compareToString(Object o1, Object o2) {
        return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
    }
}
