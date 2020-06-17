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
public class MROldFonteClass implements it.aessepi.utils.db.PersistentInstance,Comparable<MROldFonteClass>{
    private Integer id;
    private String description;
    private FonteTarget fonteTarget;
    private FonteType fonteType;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public String toString() {
      return description != null ? description : new String();
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
     * @return the fonteTarget
     */
    public FonteTarget getFonteTarget() {
        return fonteTarget;
    }

    /**
     * @param fonteTarget the fonteTarget to set
     */
    public void setFonteTarget(FonteTarget fonteTarget) {
        this.fonteTarget = fonteTarget;
    }

    /**
     * @return the fonteType
     */
    public FonteType getFonteType() {
        return fonteType;
    }

    /**
     * @param fonteType the fonteType to set
     */
    public void setFonteType(FonteType fonteType) {
        this.fonteType = fonteType;
    }




    @Override
    public int compareTo(MROldFonteClass o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

     public boolean equals(Object other) {
        if (!(other instanceof MROldFonteClass)) {
            return false;
        }
        MROldFonteClass castOther = (MROldFonteClass) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

}
