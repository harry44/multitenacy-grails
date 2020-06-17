package com.dogmasystems.myrent.db

class MRInsuranceCompany implements Serializable{

    String description

    static mapping = {
        cache true
        table name: "insurance_company"//, schema: "public"
        id generator:'sequence', params:[sequence:'insurance_company_seq']
        id column: "id", sqlType: "int4"
        description column: "description"
        version false
    }

    static constraints = {
        description nullable: true
    }
}
