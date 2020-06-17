/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import java.io.Serializable;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author Madhvendra
 */

public class MROldCCDepositByGroup implements it.aessepi.utils.db.PersistentInstance,Serializable, Comparable<MROldCCDepositByGroup> {

    private Integer id;
    private MROldGruppo carClassId;
    private MROldFonteCommissione reservationSourceId;
    private Boolean isDepositRequired;
    private Boolean isDamageWaiverRequired;
    private Boolean isTheftWaiverRequired;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldGruppo getCarClassId() {
        return carClassId;
    }

    public void setCarClassId(MROldGruppo carClassId) {
        this.carClassId = carClassId;
    }

    
    public MROldFonteCommissione getReservationSourceId() {
        return reservationSourceId;
    }

    public void setReservationSourceId(MROldFonteCommissione reservationSourceId) {
        this.reservationSourceId = reservationSourceId;
    }

    public Boolean getIsDepositRequired() {
        return isDepositRequired;
    }

    public void setIsDepositRequired(Boolean isDepositRequired) {
        this.isDepositRequired = isDepositRequired;
    }

    public Boolean getIsDamageWaiverRequired() {
        return isDamageWaiverRequired;
    }

    public void setIsDamageWaiverRequired(Boolean isDamageWaiverRequired) {
        this.isDamageWaiverRequired = isDamageWaiverRequired;
    }

    public Boolean getIsTheftWaiverRequired() {
        return isTheftWaiverRequired;
    }

    public void setIsTheftWaiverRequired(Boolean isTheftWaiverRequired) {
        this.isTheftWaiverRequired = isTheftWaiverRequired;
    }

    @Override
    public int compareTo(MROldCCDepositByGroup o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().//append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldCCDepositByGroup)) {
            return false;
        }
        MROldCCDepositByGroup castOther = (MROldCCDepositByGroup) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

}
