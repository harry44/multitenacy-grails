package com.dogmasystems.myrent.db

class MRFestivity implements Serializable{
            String description
            Date date
            MRLocation locationId




        static mapping = {
            cache true
            table name:"festivity"
            id generator:'sequence', params:[sequence:'fastivity_seq']
            id column: "id", sqlType: "int4"
            description column: "description"
            date column: "fest_date" , sqlType: "date"
            locationId column: "id_sede", sqlType: "int4"
            version false
        }
    static constraints = {
        description nullable: true
        date nullable: true
        locationId nullable: true
    }
}
