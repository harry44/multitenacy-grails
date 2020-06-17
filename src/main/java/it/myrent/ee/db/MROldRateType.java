/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author Madhvendra
 */
public class MROldRateType implements it.aessepi.utils.db.PersistentInstance, Comparable<MROldRateType> {

    private Integer id;
    private String description;
    private Boolean isCommission;
    private Boolean isOptionalRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsCommission() {
        return isCommission;
    }

    public void setIsCommission(Boolean isCommission) {
        this.isCommission = isCommission;
    }

    public Boolean getIsOptionalRate() {
        return isOptionalRate;
    }

    public void setIsOptionalRate(Boolean isOptionalRate) {
        this.isOptionalRate = isOptionalRate;
    }

    @Override
    public String toString() {
        return description != null ? description : new String();
    }

    @Override
    public int compareTo(MROldRateType o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MROldRateType)) {
            return false;
        }
        MROldRateType castOther = (MROldRateType) obj;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

}
