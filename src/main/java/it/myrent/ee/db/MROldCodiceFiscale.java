/*
 * CodiceFiscale.java
 *
 * Created on 28 iunie 2007, 14:55
 *
 */

package it.myrent.ee.db;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author jamess
 */
public class MROldCodiceFiscale implements Serializable {
    
    /** Creates a new instance of MROldCodiceFiscale */
    public MROldCodiceFiscale() {
    }
    
    private String luogo;
    private String codice;
    private String codice2;
    
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getLuogo()).toString();
    }
    
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldCodiceFiscale) {
            return new EqualsBuilder().append(getLuogo(), ((MROldCodiceFiscale)other).getLuogo()).isEquals();
        }
        return false;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getCodice2() {
        return codice2;
    }

    public void setCodice2(String codice2) {
        this.codice2 = codice2;
    }
}
