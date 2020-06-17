/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author dogma_system
 */
public class MROldGroupUpgradeSchemaRow implements it.aessepi.utils.db.PersistentInstance, Comparable<MROldGroupUpgradeSchemaRow> {

    private Integer id;
    private Integer priority;
    private MROldGroupUpgradeSchema groupUpgradeSchemaId;
    private MROldGruppo requestGroupId;
    private MROldGruppo fallbackGroupId;

    public MROldGruppo getFallbackGroupId() {
        return fallbackGroupId;
    }

    public void setFallbackGroupId(MROldGruppo fallbackGroupId) {
        this.fallbackGroupId = fallbackGroupId;
    }

    public MROldGroupUpgradeSchema getGroupUpgradeSchemaId() {
        return groupUpgradeSchemaId;
    }

    public void setGroupUpgradeSchemaId(MROldGroupUpgradeSchema groupUpgradeSchemaId) {
        this.groupUpgradeSchemaId = groupUpgradeSchemaId;
    }

    public MROldGruppo getRequestGroupId() {
        return requestGroupId;
    }

    public void setRequestGroupId(MROldGruppo requestGroupId) {
        this.requestGroupId = requestGroupId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

//@Override
//    public String toString() {
//        return description != null ? description : new String();
//    }
    @Override
    public int compareTo(MROldGroupUpgradeSchemaRow o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getId(), o.getId()).
                    toComparison();

        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MROldGroupUpgradeSchemaRow)) {
            return false;
        }
        MROldGroupUpgradeSchemaRow castOther = (MROldGroupUpgradeSchemaRow) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
}
