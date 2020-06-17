/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ResourceBundle;

/**
 *
 * @author dogma_system
 */
public class MROldVehicleStatus implements it.aessepi.utils.db.PersistentInstance, Comparable<MROldVehicleStatus> {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    private Integer id;
    private String description;
    private Boolean isOnService;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsOnService() {
        return isOnService;
    }

    public void setIsOnService(Boolean isOnService) {
        this.isOnService = isOnService;
    }

    @Override
    public String toString() {
        return description != null ? description : new String();
    }

    @Override
    public int compareTo(MROldVehicleStatus o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldVehicleStatus)) {
            return false;
        }
        MROldVehicleStatus castOther = (MROldVehicleStatus) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

}
