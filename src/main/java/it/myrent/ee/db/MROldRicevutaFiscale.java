/*
 * RicevutaFiscale.java
 *
 * Created on 21 gennaio 2005, 17.01
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.HashCodeBuilder;
import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;

/**
 *
 * @author maio
 */
public class MROldRicevutaFiscale extends MROldDocumentoFiscale implements PersistentInstance{
    
    /** Creates a new instance of MROldRicevutaFiscale */
    public MROldRicevutaFiscale() {
    }
    
    public MROldRicevutaFiscale(
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
        return RF;
    }
    
    public String getTipoNumerazione() {
        return MROldNumerazione.CORRISPETTIVI;
    }
     
//    public boolean equals(Object other) {
//        if ( !(other instanceof MROldRicevutaFiscale)) return false;
//        MROldRicevutaFiscale castOther = (MROldRicevutaFiscale) other;
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
