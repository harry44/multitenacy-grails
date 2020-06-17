package com.dogmasystems.myrent.db

class MRVehicleTransmission {

    String description

    static mapping = {
        cache true
        table name: "vehicle_transmission"//, schema: "public"
        id generator:'sequence', params:[sequence:'vehicle_transmission_seq']
        id column: "id", sqlType: "int4"
        version false
    }

    static constraints = {
        description nullable: true
    }
}
