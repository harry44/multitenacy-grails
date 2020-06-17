/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author giacomo
 */
public class MROldOptionalClass implements it.aessepi.utils.db.PersistentInstance{

    private Integer id;
    private String description;
    private Boolean isAncillary;
    private Boolean isFine;
    private Boolean isExcess;

    /**
     * @return the id
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the isAncillary
     */
    public Boolean getIsAncillary() {
        return isAncillary;
    }

    /**
     * @param isAncillary the isAncillary to set
     */
    public void setIsAncillary(Boolean isAncillary) {
        this.isAncillary = isAncillary;
    }

    /**
     * @return the isFine
     */
    public Boolean getIsFine() {
        return isFine;
    }

    /**
     * @param isFine the isFine to set
     */
    public void setIsFine(Boolean isFine) {
        this.isFine = isFine;
    }

    /**
     * @return the isExcess
     */
    public Boolean getIsExcess() {
        return isExcess;
    }

    /**
     * @param isExcess the isExcess to set
     */
    public void setIsExcess(Boolean isExcess) {
        this.isExcess = isExcess;
    }

    @Override
    public String toString() {
        return getDescription(); //NOI18N
    }

    @Override
    public boolean equals(Object other) {
        if ( !(other instanceof MROldOptionalClass) ) return false;
        MROldOptionalClass castOther = (MROldOptionalClass) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }

}
