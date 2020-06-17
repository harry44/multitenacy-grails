/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author giacomo
 */
public class FonteTarget implements it.aessepi.utils.db.PersistentInstance, Comparable<FonteTarget>{
    private Integer id;
    private String description;
    private Boolean isTourOperator;
    private Boolean isWeb;
    private Boolean isCorporate;
    private Boolean isWalkIn;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
      return description != null ? description : new String();
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
     * @return the isTourOperator
     */
    public Boolean getIsTourOperator() {
        return isTourOperator;
    }

    /**
     * @param isTourOperator the isTourOperator to set
     */
    public void setIsTourOperator(Boolean isTourOperator) {
        this.isTourOperator = isTourOperator;
    }

    /**
     * @return the isWeb
     */
    public Boolean getIsWeb() {
        return isWeb;
    }

    /**
     * @param isWeb the isWeb to set
     */
    public void setIsWeb(Boolean isWeb) {
        this.isWeb = isWeb;
    }

    /**
     * @return the isCorporate
     */
    public Boolean getIsCorporate() {
        return isCorporate;
    }

    /**
     * @param isCorporate the isCorporate to set
     */
    public void setIsCorporate(Boolean isCorporate) {
        this.isCorporate = isCorporate;
    }

    /**
     * @return the isWalkIn
     */
    public Boolean getIsWalkIn() {
        return isWalkIn;
    }

    /**
     * @param isWalkIn the isWalkIn to set
     */
    public void setIsWalkIn(Boolean isWalkIn) {
        this.isWalkIn = isWalkIn;
    }

     @Override
    public int compareTo(FonteTarget o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

     public boolean equals(Object other) {
        if (!(other instanceof FonteTarget)) {
            return false;
        }
        FonteTarget castOther = (FonteTarget) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
}
