package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRRentalAgreement

class PaymentReceipt {

     MRRentalAgreement contratto
     String token
     String cardType
     String transactionNumber
     String cardExpiration
     Double amount
     String operationId
     String authCode
     Date timestamp
     String result
     String cardPan
     String terminalId
     String prsId
     Integer type

    static constraints = {
        contratto nullable:true
        token nullable:true
        cardType nullable:true
        transactionNumber nullable:true
        cardExpiration nullable:true
        authCode nullable:true
        amount nullable:true
        operationId nullable:true
        terminalId nullable:true
        timestamp nullable:true
        prsId nullable:true
        type nullable:true
        cardPan nullable:true


    }

    static mapping = {
        cache true
        ////datasource 'myrent'
        table name: "payment_receipt"//, schema: "public"
        id generator:'sequence', params:[sequence:'payment_receipt_seq']
        id column: "id", sqlType: "int4"
        contratto column:"contractid"  , sqlType:"int4"
        token   column:"token"
        cardType    column:"card_type"
        transactionNumber column :"transaction_number"
        cardExpiration     column :"card_expiration"
        amount     column :"amount"
        operationId     column :"operation_id"
        authCode     column :"auth_code"
        timestamp     column :"timestamp"
        result     column :"result"
        terminalId     column :"terminal_id"
        prsId     column :"prs_id"
        type     column :"type" , sqlType: "int4"
        version false
    }

     String toString() {
        String retValue = "";
        if (timestamp != null) {
            retValue += "Data: " + timestamp.toString() + ", ";
        }
        if (authCode != null) {
            retValue += "Auth. Code: " + authCode + ", ";
        }
        if (contratto != null && contratto.getNumero() != null) {
            retValue += "Contratto noleggio: " + contratto.getNumero() + ", ";
        }
        retValue += "Importo = " + amount;


        return retValue;
    }
}
