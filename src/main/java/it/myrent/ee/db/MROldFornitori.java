package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MROldFornitori extends MROldBusinessPartner implements it.aessepi.utils.db.PersistentInstance, MROldContribuente {


    /** Dati aggiuntivi fornitore **/
    private MROldPianoDeiConti codiceSottoconto;
    private Boolean ritenutaAcconto;


    private Double commission;
    
    public MROldFornitori() {
        super();
        setRitenutaAcconto(false);
        setFornitore(true);
    }
    
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE)
        .append(getRagioneSociale())
        .append(getCognome())
        .append(getNome())
        .toString().trim();
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof MROldFornitori) ) return false;
        MROldFornitori castOther = (MROldFornitori) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }

    public MROldPianoDeiConti getCodiceSottoconto() {
        return this.codiceSottoconto;
    }

    public void setCodiceSottoconto(MROldPianoDeiConti codiceSottoconto) {
        this.codiceSottoconto = codiceSottoconto;
    }

    public Boolean getRitenutaAcconto() {
        return this.ritenutaAcconto;
    }

    public void setRitenutaAcconto(Boolean ritenutaAcconto) {
        this.ritenutaAcconto = ritenutaAcconto;
    }
    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

}
