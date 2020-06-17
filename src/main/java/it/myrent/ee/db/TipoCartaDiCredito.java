/*
 * TipoCartaDiCredito.java
 *
 * Created on 12 noiembrie 2004, 11:00
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author  jamess
 */
public class TipoCartaDiCredito implements PersistentInstance {
    
    /** Creates a new instance of TipoCartaDiCredito */
    public TipoCartaDiCredito() {
    }
    
    @Override
    public Integer getId() {
        throw new UnsupportedOperationException("Integer ID field is not supported yet.");
    }

    public TipoCartaDiCredito(String descrizione) {        
        this.descrizione = descrizione;
    }
    
    private String descrizione;
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public String toString() {
        return getDescrizione() == null ? new String() : getDescrizione();
    }
    
    public boolean equals(Object other) {
        if(other != null && other instanceof TipoCartaDiCredito) {
            return new EqualsBuilder().append(getDescrizione(), ((TipoCartaDiCredito)other).getDescrizione()).isEquals();
        }
        return false;
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getDescrizione())
        .toHashCode();
    }    
}
