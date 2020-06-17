package com.dogmasystems.myrent.db

class MRPaymentSia implements Serializable{

    String idPagamento
    String valuta
    String codiceCircuito
    String pan
    String datascad
    String aliaspan
    String aliaspanrev
    String aliaspanDatascad
    String aliaspanTail
    String esitoTrans
    String esitoTrans99
    String numtrans
    String numaut
    String acqbin
    String tipopag
    String statopag
    String idLoguitran
    String idLogapixml
    String tcontab
    String idTrans
    String imptot
    String accInfo
    String accReg
    String numord
    String reqrefnum
    String idNegozio
    String importo
    String messaggio
    String mac
    String esitoMultiTrans
    Integer idWarranty
    String idRentalAgreement
    String orderDescription
    MRLocation location
    RentalType rentalType

    static constraints = {

        idPagamento nullable: true
        valuta nullable: true
        codiceCircuito nullable: true
        pan nullable: true
        datascad nullable: true
        aliaspan nullable: true
        aliaspanrev nullable: true
        aliaspanDatascad nullable: true
        aliaspanTail nullable: true
        esitoTrans nullable: true
        esitoTrans99 nullable: true
        numtrans nullable: true
        numaut nullable: true
        acqbin nullable: true
        tipopag nullable: true
        statopag nullable: true
        idLoguitran nullable: true
        idLogapixml nullable: true
        tcontab nullable: true
        idTrans nullable: true
        imptot nullable: true
        accInfo nullable: true
        accReg nullable: true
        numord nullable: true
        reqrefnum nullable: true
        idNegozio nullable: true
        importo nullable: true
        messaggio nullable: true
        mac nullable: true
        esitoMultiTrans nullable: true
        idWarranty nullable: true
        idRentalAgreement nullable: true
        orderDescription nullable: true
        rentalType nullable:true
        location nullable:true
    }
    static mapping = {
        cache true
        table name: "payment_sia"//, schema: "public"
        id generator:'sequence', params:[sequence:'mr_payment_sia_seq']
        id column: "id", sqlType: "int4"
        idPagamento column: "id_pagamento"
        valuta column: "valuta"
        codiceCircuito column: "codice_circuito"
        pan column: "pan"
        datascad column: "datascad"
        aliaspan column: "aliaspan"
        aliaspanrev column: "aliaspanrev"
        aliaspanDatascad column: "aliaspan_datascad"
        aliaspanTail column: "aliaspan_tail"
        esitoTrans column: "esito_trans"
        esitoTrans99 column: "esito_trans99"
        numtrans column:"numtrans"
        numaut column: "numaut"
        acqbin column: "acqbin"
        tipopag column: "tipopag"
        statopag column: "statopag"
        idLoguitran column: "id_loguitran"
        idLogapixml column: "id_logapixml"
        tcontab column: "tcontab"
        idTrans column: "id_trans"
        imptot column: "imptot"
        accInfo column:"acc_info"
        accReg column: "acc_reg"
        numord column: "numord"
        reqrefnum column: "reqrefnum"
        idNegozio column: "id_negozio"
        importo column: "importo"
        messaggio column: "messaggio"
        mac column: "mac"
        esitoMultiTrans column: "esito_multi_trans"
        idWarranty column: "id_warranty"
        idRentalAgreement column: "id_rental_agreement"
        orderDescription column: "order_description"
        location column:"location_id"
        rentalType column:"rentalType_id"
        version false
    }
}
