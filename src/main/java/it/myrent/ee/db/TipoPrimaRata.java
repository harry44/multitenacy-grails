package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/** @author Hibernate CodeGenerator */
public class TipoPrimaRata implements it.aessepi.utils.db.PersistentInstance {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    

    /** nullable persistent field */
    private String descrizione;

    /** full constructor */
    public TipoPrimaRata(Integer id, String descrizione) {
        this.id = id;
        this.descrizione= descrizione;
    }

    /** default constructor */
    public TipoPrimaRata() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String getUniDescription() {
        return this.descrizione;
    }

    public String toString() {
        return descrizione != null ? descrizione : null;
    }

    public boolean equals(Object other) {
        if ( !(other instanceof TipoPrimaRata) ) return false;
        TipoPrimaRata castOther = (TipoPrimaRata) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

   
  
}
