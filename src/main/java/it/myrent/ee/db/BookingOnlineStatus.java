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
public class BookingOnlineStatus implements it.aessepi.utils.db.PersistentInstance, Comparable<BookingOnlineStatus> {
    private Integer id;//	integer (sequence)
    private String description;
    private Boolean disableForOnlineBooking;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDisableForOnlineBooking() {
        return disableForOnlineBooking;
    }

    public void setDisableForOnlineBooking(Boolean disableForOnlineBooking) {
        this.disableForOnlineBooking = disableForOnlineBooking;
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
    public int compareTo(BookingOnlineStatus o) {
        if (o == null) {
            return 1;
        } else {
                    return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof BookingOnlineStatus)) {
            return false;
        }
        BookingOnlineStatus castOther = (BookingOnlineStatus) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
    
}
