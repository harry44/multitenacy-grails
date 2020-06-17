package com.dogmasystems.myrent.db

class MRVehicleTraction {

    String description

    static constraints = {
        description nullable: false
    }

    static mapping = {
        table name: "vehicle_traction"
        id generator: 'sequence', params: [sequence: 'vehicle_traction_seq']
        id column: "id", sqlType: "int4"
        version false
    }
}
