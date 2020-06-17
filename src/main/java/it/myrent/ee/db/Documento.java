package it.myrent.ee.db;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/** @author Hibernate CodeGenerator */
public class Documento {
    
    private String descrizione;
    
    public Documento(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public Documento() {
    }
    
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String toString() {
        return descrizione !=null ? descrizione : new String();
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof Documento) ) return false;
        Documento castOther = (Documento) other;
        return new EqualsBuilder()
        .append(this.getDescrizione(), castOther.getDescrizione())
        .isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getDescrizione())
        .toHashCode();
    }
    
   
    
}
