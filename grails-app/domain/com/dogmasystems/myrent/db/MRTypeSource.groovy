package com.dogmasystems.myrent.db

class MRTypeSource {
    String description

    static constraints = {
        description(nullable: true)
    }
    static mapping = {
        cache true
        table name: "fonte_type"
        id generator: "sequence", params: [sequence: "fonte_type_seq"]
        id columns: "id", sqlType: "int4"
        description columns: "description"
        version false
    }
}