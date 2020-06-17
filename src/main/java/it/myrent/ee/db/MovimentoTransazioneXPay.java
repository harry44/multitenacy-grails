/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Sumit
 */
public class MovimentoTransazioneXPay implements it.aessepi.utils.db.PersistentInstance{
private Integer id;
    private String operazione;
    private String dataOperazione;
    private Double importo;
    private String statoOperazione;

    public MovimentoTransazioneXPay() {
    }
    @Override
    public Integer getId() {
        return id;
    }
 public boolean equals(Object other) {
        if (!(other instanceof MovimentoTransazioneXPay)) {
            return false;
        }
        MovimentoTransazioneXPay castOther = (MovimentoTransazioneXPay) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the operazione
     */
    public String getOperazione() {
        return operazione;
    }

    /**
     * @param operazione the operazione to set
     */
    public void setOperazione(String operazione) {
        this.operazione = operazione;
    }

    /**
     * @return the dataOperazione
     */
    public String getDataOperazione() {
        return dataOperazione;
    }

    /**
     * @param dataOperazione the dataOperazione to set
     */
    public void setDataOperazione(String dataOperazione) {
        this.dataOperazione = dataOperazione;
    }

    /**
     * @return the importo
     */
    public Double getImporto() {
        return importo;
    }

    /**
     * @param importo the importo to set
     */
    public void setImporto(Double importo) {
        this.importo = importo;
    }

    /**
     * @return the statoOperazione
     */
    public String getStatoOperazione() {
        return statoOperazione;
    }

    /**
     * @param statoOperazione the statoOperazione to set
     */
    public void setStatoOperazione(String statoOperazione) {
        this.statoOperazione = statoOperazione;
    }

    
}
