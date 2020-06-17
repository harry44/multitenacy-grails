package com.dogmasystems.myrent.db

class MRAgreementWebCode {

        MRAgreement agreement
        String webCode
        Boolean isValid

        static mapping = {
            cache true
            table name: "agreement_webcode"
            id generator:'sequence', params:[sequence:'agreement_webcode_seq']
            id column: "id", sqlType: "int4"
            webCode column:"webcode"
            isValid column: "isvalid"
            agreement column: "id_agreement",sqlType: "int4"

            version false
        }

        static constraints = {
            webCode(nullable:true)
            isValid(nullable: true)
            agreement(nullable: true)

        }
    }

