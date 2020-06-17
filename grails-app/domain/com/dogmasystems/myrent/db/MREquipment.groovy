package com.dogmasystems.myrent.db

class MREquipment {

    String description

    static  mapping = {
        cache true
        table name: "equipment"//, schema: "public"
        id generator:'sequence', params:[sequence:'equipment_seq']
        id column: "id", sqlType: "int4"
        description column: "description"
        version false
    }

    static constraints = {
        description nullable: true
    }
}
