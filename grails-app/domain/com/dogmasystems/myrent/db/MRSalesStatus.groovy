package com.dogmasystems.myrent.db

class MRSalesStatus implements Serializable{

    String description

    static mapping = {
        cache true
        table name: "sales_status"//, schema: "public"
        id generator:'sequence', params:[sequence:'vehicle_status_seq']
        id column: "id", sqlType: "int4"
        description column: "description"
        version false
    }

    static constraints = {
        description nullable: true
    }
}
