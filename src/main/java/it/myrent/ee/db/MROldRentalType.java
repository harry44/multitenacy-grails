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
public class MROldRentalType implements it.aessepi.utils.db.PersistentInstance, Comparable<MROldRentalType> {

    private Integer id;
    private String description;
    private Boolean isRental;
    private Boolean isFreeLoan;
    private Boolean isFranchiseRental;
    private Boolean isAgreement;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsAgreement() {
        return isAgreement;
    }

    public void setIsAgreement(Boolean isAgreement) {
        this.isAgreement = isAgreement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsRental() {
        return isRental;
    }

    public void setIsRental(Boolean isRental) {
        this.isRental = isRental;
    }

    public Boolean getIsFreeLoan() {
        return isFreeLoan;
    }

    public void setIsFreeLoan(Boolean isFreeLoan) {
        this.isFreeLoan = isFreeLoan;
    }


    public Boolean getIsFranchiseRental() {
        return isFranchiseRental;
    }

    public void setIsFranchiseRental(Boolean isFranchiseRental) {
        this.isFranchiseRental = isFranchiseRental;
    }

    @Override
    public String toString() {
        return description != null ? description : new String();
    }

    @Override
    public int compareTo(MROldRentalType o) {
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
        if (!(obj instanceof MROldRentalType)) {
            return false;
        }
        MROldRentalType castOther = (MROldRentalType) obj;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
}
