/*
 * CausaleMovimento.java
 *
 * Created on 25 mai 2007, 14:19
 *
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author jamess
 */
public class MROldCausaleMovimento implements PersistentInstance {
    
    private Integer id;
    private String descrizione;
    private Boolean noleggio;
    private Boolean prenotazione;
    private Boolean trasferimento;
    private Boolean riparazione;
    private Boolean lavaggio;
    private Boolean rifornimento;
    private Boolean indisponibilitaFutura;
    private Boolean isDisabled;
    
    public static final Integer CAUSALE_NOLEGGIO = new Integer(1);
    public static final Integer CAUSALE_PRENOTAZIONE = new Integer(2);
    public static final String CAUSALE_PLANNED_RENTAL = "PLANNED RENTAL";
    /** Creates a new instance of MROldCausaleMovimento */
    public MROldCausaleMovimento() {
    }
    
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldCausaleMovimento) {
            return new EqualsBuilder().append(getId(), ((MROldCausaleMovimento)other).getId()).isEquals();
        } else {
            return false;
        }
    }
    
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getDescrizione()).toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Boolean getNoleggio() {
        return noleggio;
    }

    public void setNoleggio(Boolean noleggio) {
        this.noleggio = noleggio;
    }

    public Boolean getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Boolean prenotazione) {
        this.prenotazione = prenotazione;
    }

    public Boolean getTrasferimento() {
        return trasferimento;
    }

    public void setTrasferimento(Boolean trasferimento) {
        this.trasferimento = trasferimento;
    }

    public Boolean getRiparazione() {
        return riparazione;
    }

    public void setRiparazione(Boolean riparazione) {
        this.riparazione = riparazione;
    }

    public Boolean getLavaggio() {
        return lavaggio;
    }

    public void setLavaggio(Boolean lavaggio) {
        this.lavaggio = lavaggio;
    }

    public Boolean getRifornimento() {
        return rifornimento;
    }

    public void setRifornimento(Boolean rifornimento) {
        this.rifornimento = rifornimento;
    }

    public Boolean getIndisponibilitaFutura() {
        return indisponibilitaFutura;
    }

    public void setIndisponibilitaFutura(Boolean indisponibilitaFutura) {
        this.indisponibilitaFutura = indisponibilitaFutura;
    }
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
}
