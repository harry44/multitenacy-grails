package com.dogmasystems.myrent.db

class MRFetchTransaction implements Serializable{
     Integer id
     String timestamp
     String idTrans
     String numOrdine
     String stato
     String acqbin
     String tipopag
     String circuito
     String importoTrans
     String importoAutor
     String importoContab
     String importoStornato
     String esitoTrans
     String numaut
     String codiceEsercente
     String mac
     String tAutor
     String valuta
     String oldTimestamp
     String rentalAgreementId

    static  mapping ={
        cache true
        table name: "fetch_transaction"//, schema: "public"
        id generator:'sequence', params:[sequence:'fetch_transaction_seq']
        id column: "id"//, sqlType: "int4"
        timestamp column: "timestamp"
        idTrans column: "id_trans"
        valuta column: "valuta"
        numOrdine column: "num_ordine"
        tipopag column: "tipopag"
        stato column: "stato"
        circuito column: "circuito"
        importoTrans column: "importo_trans"
        importoAutor column: "importo_autor"
        importoContab column: "importo_contab"
        importoStornato column: "importo_stornato"
        esitoTrans column: "esito_trans"
        numaut column: "numaut"
        codiceEsercente column: "codice_esercente"
        mac column: "mac"
        tAutor column: "t_autor"
        oldTimestamp column: "old_timestamp"
        rentalAgreementId column: "rental_agreement_id"
        version false
    }
    static constraints = {

        timestamp nullable: true
        valuta nullable: true
        numOrdine nullable: true
        stato nullable: true
        esitoTrans nullable: true
        numaut nullable: true
        acqbin nullable: true
        tipopag nullable: true
        importoContab nullable: true
        importoTrans nullable: true
        importoAutor nullable: true
        importoStornato nullable: true
        idTrans nullable: true
        esitoTrans nullable: true
        codiceEsercente nullable: true
        tAutor nullable: true
        oldTimestamp nullable: true
        rentalAgreementId nullable: true
        mac nullable: true

    }

}
