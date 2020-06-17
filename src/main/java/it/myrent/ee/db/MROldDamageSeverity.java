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
public class MROldDamageSeverity implements it.aessepi.utils.db.PersistentInstance, Comparable<MROldDamageSeverity>{

    private Integer id;
    private String description;


    private String severityImage;



    private String severityOldImage;


    private String descriptionEn;

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

    public String getSeverityImage() {
        return severityImage;
    }

    public void setSeverityImage(String severityImage) {
        this.severityImage = severityImage;
    }
    public String getSeverityOldImage() {
        return severityOldImage;
    }

    public void setSeverityOldImage(String severityOldImage) {
        this.severityOldImage = severityOldImage;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    @Override
    public String toString() {
        return description != null ? description : new String();
    }

    @Override
    public int compareTo(MROldDamageSeverity o) {
        if (o == null) {
            return 1;
        } else {
                    return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldDamageSeverity)) {
            return false;
        }
        MROldDamageSeverity castOther = (MROldDamageSeverity) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

}
