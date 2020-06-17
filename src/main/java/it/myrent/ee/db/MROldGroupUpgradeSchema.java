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
public class MROldGroupUpgradeSchema implements it.aessepi.utils.db.PersistentInstance, Comparable<MROldGroupUpgradeSchema> {

    private Integer id;
    private String description;

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

    @Override
    public String toString() {
        return description != null ? description : new String();
    }

    @Override
    public int compareTo(MROldGroupUpgradeSchema o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldGroupUpgradeSchema)) {
            return false;
        }
        MROldGroupUpgradeSchema castOther = (MROldGroupUpgradeSchema) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
}
