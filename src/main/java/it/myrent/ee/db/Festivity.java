/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author dogma_system
 */
public class Festivity implements PersistentInstance,Comparable<Festivity> {
private Integer id;
private String description;
private MROldSede locationId;
private Date date;

    public MROldSede getLocationId() {
        return locationId;
    }

    public void setLocationId(MROldSede locationId) {
        this.locationId = locationId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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
    public int compareTo(Festivity o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    @Override
     public boolean equals(Object other) {
        if (!(other instanceof Festivity)) {
            return false;
        }
        Festivity castOther = (Festivity) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
}
