package com.dogmasystems.touroperatorportal

import grails.util.Holders

//import com.dogmasystems.myrent.db.MRLocation

class AuthenticationTokenReclaim {
    String tokenValue

    Date timeStamp
    Integer rentalId
    Integer reservationId
    Integer reclaimId
    String ipAddress


    static constraints = {

        tokenValue blank: false,nullable:true


        timeStamp blank: true , nullable: true
        rentalId  nullable: true
        reservationId  nullable: true
        reclaimId  nullable: true
        ipAddress  nullable: true

    }
    static mapping = {

        ////datasource 'myrent'
        table name: "authentication_token_reclaim"//, schema: "public"
        id generator:'sequence', params:[sequence:'auth_token_reclaim_seq']
        //id generator: "assigned"
        id column: "id", sqlType: "int4"

        version false
    }
}
