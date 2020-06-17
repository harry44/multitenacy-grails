package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/** @author Hibernate CodeGenerator */
public class MROldMezzoPagamento implements it.aessepi.utils.db.PersistentInstance {

    private Integer id;

    private String descrizione; 
    private String codice;
    private String codiceEsportazione;

    public MROldMezzoPagamento(Integer id, String descrizione) {
        this.id = id;
        this.descrizione= descrizione;
    }
    
    
    /** default constructor */
    public MROldMezzoPagamento() {
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

    public String getCodiceEsportazione() {
        return codiceEsportazione;
    }

    public void setCodiceEsportazione(String codiceEsportazione) {
        this.codiceEsportazione = codiceEsportazione;
    }
    
    public String toString() {
        return 
                (getCodice() != null ? getCodice() : "<>") + // NOI18N
                ", " + // NOI18N
                (getDescrizione() != null ? getDescrizione() : "<>"); // NOI18N
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MROldMezzoPagamento) ) return false;
        MROldMezzoPagamento castOther = (MROldMezzoPagamento) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }
}
