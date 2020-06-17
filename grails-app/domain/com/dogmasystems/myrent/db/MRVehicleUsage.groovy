package com.dogmasystems.myrent.db

import grails.util.Holders

class MRVehicleUsage {

    String shortDescription, longDescription

    static mapping = {
        cache true
        table name: "vehicleusage", schema: "public"
        id generator:'sequence', params:[sequence:'vehicle_usage_seq']
            id column: "id", sqlType: "int4"

        shortDescription column: "shortdescription"
        longDescription column: "longdescription"

        version(false)
    }


    static constraints = {
        shortDescription nullable: true
        longDescription nullable: true
    }
}
