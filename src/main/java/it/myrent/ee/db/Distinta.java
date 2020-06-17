/*
 * Distinta.java
 *
 * Created on 15 febbraio 2005, 9.02
 */

package it.myrent.ee.db;

/**
 *
 * @author  laura
 */

import it.aessepi.utils.beans.FormattedDate;
import java.util.Date;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Distinta implements it.aessepi.utils.db.PersistentInstance,Serializable {
    
    private Integer id;
    private Integer numero;
    private MROldPianoDeiConti contoBanca;
    private Date dataPresentazione;
    private String  annotazione;
       
    
    public Distinta() {
    }
    
    public Integer getId(){
        return id;
    }
    
    public void setId(Integer id) {
        this.id=id;
    }
        
    public String toString() {
        if(getDataPresentazione() != null && getContoBanca() != null) {
            return FormattedDate.format(getDataPresentazione()) + " - " + getContoBanca().getDescrizione(); //NOI18N
        } else {
            return new String();
        }
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof Distinta) ) return false;
        Distinta castOther = (Distinta) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }
    
    public MROldPianoDeiConti getContoBanca() {
        return contoBanca;
    }

    public void setContoBanca(MROldPianoDeiConti banca) {
        this.contoBanca = banca;
    }

    public Date getDataPresentazione() {
        return dataPresentazione;
    }

    public void setDataPresentazione(Date dataPresentazione) {
        this.dataPresentazione = dataPresentazione;
    }

    public String getAnnotazione() {
        return annotazione;
    }

    public void setAnnotazione(String annotazione) {
        this.annotazione = annotazione;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
    
    
}




