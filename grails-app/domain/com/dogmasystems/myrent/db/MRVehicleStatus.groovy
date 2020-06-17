package com.dogmasystems.myrent.db

import com.dogmasystems.utils.db.PersistentInstance
import org.apache.commons.lang.builder.CompareToBuilder
import org.apache.commons.lang.builder.EqualsBuilder

class MRVehicleStatus implements Serializable, PersistentInstance, Comparable<MRVehicleStatus> {

    String description
    Boolean isOnService

    static mapping = {
        cache true
        table name: "vehicle_status"//, schema: "public"
        id generator:'sequence', params:[sequence:'vehicle_status_seq']
        id column: "id", sqlType: "int4"
        description column: "description"
        isOnService column: "is_on_service"
        version false
    }

    static constraints = {
        description nullable: true
        isOnService nullable: true
    }

    @Override
    public String toString() {
        return description != null ? description : new String();
    }

    @Override
    public int compareTo(MRVehicleStatus o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof MRVehicleStatus)) {
            return false;
        }
        MRVehicleStatus castOther = (MRVehicleStatus) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
}
