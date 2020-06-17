package com.dogmasystems.myrent.db

class MRVehicleChassis {

    String description

    static constraints = {
        description nullable: false
    }

    static mapping = {
        table name: "vehicle_chassis"
        id generator: 'sequence', params: [sequence: 'vehicle_chassis_seq']
        id column: "id", sqlType: "int4"
        version false
    }
}
