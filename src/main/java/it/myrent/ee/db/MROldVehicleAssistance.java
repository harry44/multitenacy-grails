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
public class MROldVehicleAssistance implements it.aessepi.utils.db.PersistentInstance {
    private Integer id;
    private String description;
    private String workingHours;
    private String phoneNumberWorkingHours;
    private String phoneNumberNoWorkingHours;

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

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getPhoneNumberWorkingHours() {
        return phoneNumberWorkingHours;
    }

    public void setPhoneNumberWorkingHours(String phoneNumberWorkingHours) {
        this.phoneNumberWorkingHours = phoneNumberWorkingHours;
    }

    public String getPhoneNumberNoWorkingHours() {
        return phoneNumberNoWorkingHours;
    }

    public void setPhoneNumberNoWorkingHours(String phoneNumberNoWorkingHours) {
        this.phoneNumberNoWorkingHours = phoneNumberNoWorkingHours;
    }

    @Override
    public String toString() {
        return description; //NOI18N
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MROldVehicleAssistance)) {
            return false;
        }
        MROldVehicleAssistance castOther = (MROldVehicleAssistance) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
}
