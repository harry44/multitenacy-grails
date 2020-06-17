/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import java.util.Date;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author Madhvendra
 */
public class MROldIncidentType implements it.aessepi.utils.db.PersistentInstance, Comparable<MROldIncidentType> {
    private Integer id;
    private String description;
    private Boolean isActive;
    private Boolean isPassive;
    private Boolean isTheft;
    private Boolean isAttemptedTheft;
    private Date dateCreated;
    private Date lastUpdated;
    private Long tenantId;

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsTheft() {
        return isTheft;
    }

    public void setIsTheft(Boolean isTheft) {
        this.isTheft = isTheft;
    }

    public Boolean getIsAttemptedTheft() {
        return isAttemptedTheft;
    }

    public void setIsAttemptedTheft(Boolean isAttemptedTheft) {
        this.isAttemptedTheft = isAttemptedTheft;
    }

    public Boolean getIsPassive() {
        return isPassive;
    }

    public void setIsPassive(Boolean isPassive) {
        this.isPassive = isPassive;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return description != null ? description : new String();
    }


    public int compareTo(MROldIncidentType o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldIncidentType)) {
            return false;
        }
        MROldIncidentType castOther = (MROldIncidentType) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
}
