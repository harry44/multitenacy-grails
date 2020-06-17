/*
 * Numerazione.java
 *
 * Created on 04 iunie 2007, 18:32
 *
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author jamess
 */
public class MROldNumerazioneSede extends MROldNumerazione implements PersistentInstance {
    
    /** Creates a new instance of MROldNumerazione */
    public MROldNumerazioneSede() {
    }
    
    public MROldNumerazioneSede(String documento) {
        super(documento);
    }
    
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldNumerazioneSede) {
            return new EqualsBuilder().append(getId(), ((MROldNumerazioneSede)other).getId()).isEquals();
        }
        return false;
    }
}
