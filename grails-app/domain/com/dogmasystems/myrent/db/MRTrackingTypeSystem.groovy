package com.dogmasystems.myrent.db

class MRTrackingTypeSystem {

    String description

    static mapping = {
        cache true
        table name: "tracking_type_system"//, schema: "public"
        id generator:'sequence', params:[sequence:'tracking_type_system_seq']
        id column: "id", sqlType: "int4"
        version false
    }

    static constraints = {
        description nullable: true
    }
}
