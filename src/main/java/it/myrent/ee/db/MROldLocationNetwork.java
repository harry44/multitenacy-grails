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
public class MROldLocationNetwork implements it.aessepi.utils.db.PersistentInstance, Comparable<MROldLocationNetwork> {

    private Integer id;
    private String description;
    private String familyCode;
    private Boolean isFranchisee;

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

    public Boolean getIsFranchisee() {
        return isFranchisee;
    }

    public void setIsFranchisee(Boolean isFranchisee) {
        this.isFranchisee = isFranchisee;
    }

    @Override
    public String toString() {
        return  description != null ? description : new String();
    }

    public String getFamilyCode() {
        return familyCode;
    }

    public void setFamilyCode(String familyCode) {
        this.familyCode = familyCode;
    }

    @Override
    public int compareTo(MROldLocationNetwork o) {
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
        if (!(obj instanceof MROldLocationNetwork)) {
            return false;
        }
        MROldLocationNetwork castOther = (MROldLocationNetwork) obj;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
}
