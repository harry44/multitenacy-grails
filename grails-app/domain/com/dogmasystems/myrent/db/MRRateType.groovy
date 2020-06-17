package com.dogmasystems.myrent.db

class MRRateType implements Serializable{

    String description
    Boolean isCommission
    Boolean isOptionalRate

    static mapping = {
        cache true
        //datasource 'myrent'
        table name: "rate_type"//, schema: "public"
        id generator: 'sequence', params:[sequence:'rate_type_seq']
        id column: "id", sqlType: "int4"
        isCommission column: "iscommission"//, sqlType: "int4"
        isOptionalRate column: "isoptionalrate"//, sqlType: "int4"
        version false
    }

    static constraints = {
        description nullable: true
        isCommission nullable: true
        isOptionalRate nullable: true
    }
}
