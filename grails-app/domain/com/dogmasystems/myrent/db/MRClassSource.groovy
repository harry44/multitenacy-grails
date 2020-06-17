package com.dogmasystems.myrent.db

class MRClassSource {

    String description
    MRTargetSource fonteTarget
    MRTypeSource fonteType


    static constraints = {
        description(nullable: true)
        fonteTarget(nullable: true)
        fonteType(nullable: true)
    }

    static mapping = {
        cache true
        table name: "fonte_class"
        id generator: 'sequence', params: [sequence: 'fonte_class_seq']
        id column: "id", sqlType: "int4"
        description column: "description"
        fonteTarget column: "id_fonte_target", sqlType: "int4"
        fonteType column: "id_fonte_type", sqlType: "int4"
        version false
    }
}
