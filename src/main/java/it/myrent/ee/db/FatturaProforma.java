package it.myrent.ee.db;

import it.myrent.ee.db.MROldNumerazione;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;

public class FatturaProforma extends MROldFatturaGenerica implements PersistentInstance{
    
    /** Creates a new instance of Fattura */
    public FatturaProforma() {
    }
    
    public FatturaProforma(
            MROldAffiliato affiliato,
            User user,
            MROldNumerazione numerazione,
            String prefisso,
            Integer numero,            
            Date data,
            Integer anno
            ) {
        super(affiliato, user, numerazione, prefisso, numero, data, anno);
    }
    
    public String getTipoDocumento() {
        return FTP;
    }

    public String getTipoNumerazione() {
        return MROldNumerazione.PROFORMA;
    }
    
//    public boolean equals(Object other) {
//        if ( !(other instanceof Fattura)) return false;
//        Fattura castOther = (Fattura) other;
//        //return this.getId()==null?castOther.getId()==null:this.getId().equals(castOther.getId());
//
//        return new EqualsBuilder()
//        .append(this.getId(), castOther.getId())
//        .isEquals();
//    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }
    
}
