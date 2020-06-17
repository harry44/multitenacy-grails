package com.dogmasystems.myrent.db


import com.dogmasystems.touroperatorportal.Garanzie
import com.dogmasystems.touroperatorportal.Tariffe
import com.dogmasystems.touroperatorportal.Pagamenti
import com.dogmasystems.touroperatorportal.Commissioni
import grails.util.Holders


class MRReservation implements Serializable{

    Date startDate
    Date endDate
    String prefix
    Integer number
    Integer year
    Date data //date
    String codice
    Double discountRate
    Double noleggioNoVat
    Boolean isRental
    Boolean isCanceled
    Boolean isRefused
    Boolean isNoShow
    Boolean isConfirm
    String note
    MRLocation locationPickupExcepted
    MRBusinessPartner clientiByIdCond3
    MRAffiliate affiliati
    MRUser users
    MRBusinessPartner clientiByIdCond2
    MRBusinessPartner clientiByIdCond1
    MRBusinessPartner clientiByIdCliente
    MREnumeration numerazioni //enumeration
	//MRVehicleMovement vehicleMovement
    Pagamenti pagamenti
    MRGroup group
    MRGroup assignedGroup
    MRLocation locationReturnExcepted
    Tariffe tariffe
    Double rentalTotal
    RentalType rentalType
	MRCancellingReason cancellingReason
    String noteGaranzia
    String nomeFirmatario;
    String luogoFirma;
    String annotazioniFirma;
    String operatorName;
    Boolean isOnRequest;
    Boolean isAccepted;
    String productNameDesc;
    Boolean isSplitPayment
    //Add the column to contain id of prenotazioni table of pro database
    Integer proKey
    Double prepaidTotal
    Garanzie garanzieByIdGaranzia1
    Garanzie garanzieByIdGaranzia2
    Double saldoAcconti
    Double saldoFatture
    Double cauzione
    Double saldoCauzione
    Double saldoNoleggio
    Boolean privacyMessage1
    Boolean privacyMessage2
    MRBusinessPartner agent
    Date paymentRequesTimestamp
    String transactionId
    String paymentLinkUnicredit
    String paymentLinkPaypal
    String xmlCreatedBy
    Date xmlCreatedWhen
    Double xmlCreatedTotalPrice;
    Double xmlCreatedPriceExtra;
    Double xmlCreatedRentalPrice;
    String xmlDeletedBy;
    Date xmlDeleted



    static hasOne = [commissionis : Commissioni,dswpUser : MRUser];

    static hasMany = [movimentiAutos: MRVehicleMovement,
                      prenotazioniRigas: MRRowReservation,
                      fatturaIntestaziones: MRHeaderInvoice,
    ]
//    static belongsTo = [MRAffiliate, MRBusinessPartner, MRGroup, MREnumeration, Pagamenti, MRLocation, Tariffe, MRUser]

    static mapping = {
        cache true

        commissionis cache: true
        movimentiAutos cache: true
        fatturaIntestaziones cache: true

        //datasource 'myrent'
        table name: "prenotazioni"//, schema: "public"
        id generator:'sequence', params:[sequence:'mr_reservation_seq']
        //id generator: "assigned"
        id column: "id", sqlType: "int4"
        isOnRequest column: "is_on_request"
        isAccepted column: "is_accepted"
        operatorName column: "operatorname" ,type: "string"
        startDate column: "inizio" , sqlType:"date"
        endDate column: "fine" , sqlType:"date"
        prefix column: "prefisso"
        data sqlType:"date"
        number column: "numero"
        year column: "anno"
        isCanceled column: "annullata"
        isRental column:"usata"
        isNoShow column: "no_show"
        isRefused column:"rifiutata"
        isConfirm column:"confermata"
        discountRate column:"sconto_percentuale"
        locationPickupExcepted column:"id_sede_uscita", sqlType: "int4"
        clientiByIdCond3 column:"id_cond_3", sqlType: "int4"
        affiliati column:"id_affiliato", sqlType: "int4"
        users column:"id_user_apertura", sqlType: "int4"
        clientiByIdCond1 column:"id_cond_1", sqlType: "int4"
        clientiByIdCond2 column:"id_cond_2", sqlType: "int4"
        clientiByIdCliente column:"id_cliente", sqlType: "int4"
        numerazioni column:"id_numerazione", sqlType: "int4"
        pagamenti column:"id_pagamento", sqlType: "int4"
        group column:"id_gruppo", sqlType: "int4"
        assignedGroup column:"id_gruppo_assegnato" , sqlType:"int4"
        locationReturnExcepted column:"id_sede_rientro_previsto", sqlType: "int4"
        tariffe column:"id_tariffa", sqlType: "int4"
        rentalTotal column:"noleggio"
        noleggioNoVat column: "noleggio_no_vat"
        rentalType column: "id_rental_type" , sqlType: "int4"
		cancellingReason column:"cancellingreason", sqlType: "int4"
        garanzieByIdGaranzia1 column: "id_garanzia_1", sqlType: "int4"
        garanzieByIdGaranzia2 column: "id_garanzia_2", sqlType: "int4"
        isSplitPayment column: "split_payment"
        note column:"note"
        noteGaranzia column:"note_garanzia"
        nomeFirmatario column:"nome_firmarario"
        luogoFirma column:"luogo_firma"
        annotazioniFirma column:"annotazioni_firma"
        proKey column: "prokey"
        prepaidTotal column: "noleggio_ppay"
        saldoAcconti column: "saldo_acconti"
        saldoFatture column: "saldo_fatture"
        cauzione column: "cauzione"
        saldoCauzione column: "saldo_cauzione"
        saldoNoleggio column: "saldo_noleggio"
        privacyMessage1 column: "privacy1"
        privacyMessage2 column: "privacy2"
        agent column: "agent_id", sqlType: "int4"
        paymentRequesTimestamp column:"payment_request_timestamp"
        transactionId column: "transaction_id"
        paymentLinkUnicredit column: "payment_link_unicredit"
        paymentLinkPaypal column: "payment_link_paypal"
        xmlCreatedBy column: "xml_created_by"
        xmlCreatedWhen column: "xml_created"
        xmlCreatedTotalPrice column: "xml_created_total_price"
        xmlCreatedPriceExtra column: "xml_created_price_extra"
        xmlCreatedRentalPrice column: "xml_created_rental_price"
        xmlDeletedBy column: "xml_deleted_by"
        xmlDeleted column: "xml_deleted"
        version false
    }

    static constraints = {
        productNameDesc nullable: true
        operatorName nullable: true
        isAccepted nullable: true
        isOnRequest nullable: true
        annotazioniFirma nullable: true
        noleggioNoVat nullable: true
        luogoFirma nullable: true
        nomeFirmatario nullable: true
        startDate nullable: true
        endDate nullable: true
        prefix nullable: true
        number nullable: true
        year nullable: true
        data nullable: true
        codice nullable: true
        discountRate nullable: true, scale: 17
        isRental nullable: true
        isCanceled nullable: true
        isRefused nullable: true
        isNoShow nullable: true
        isConfirm nullable: true
        note nullable: true
        isSplitPayment nullable: true
        rentalType nullable: true
        tariffe nullable: true
        rentalTotal nullable: true
        affiliati nullable: true
        numerazioni nullable: true
        pagamenti nullable: true
        locationReturnExcepted nullable: true
        locationPickupExcepted nullable: true
        group nullable: true
        assignedGroup nullable: true
        clientiByIdCond3 nullable: true
        clientiByIdCliente nullable: true
        clientiByIdCond2 nullable: true
        clientiByIdCond1 nullable: true
        users nullable: true
		cancellingReason nullable:true
        dswpUser nullable:true
        noteGaranzia nullable: true
        proKey nullable: true
        commissionis nullable: true
        prepaidTotal nullable: true
        garanzieByIdGaranzia1 nullable: true
        garanzieByIdGaranzia2 nullable: true
        saldoAcconti nullable: true
        saldoFatture nullable: true
        saldoCauzione nullable: true
        cauzione nullable: true
        saldoNoleggio nullable: true
        privacyMessage1 nullable: true
        privacyMessage2 nullable: true
        agent nullable: true
        paymentRequesTimestamp nullable: true
        transactionId nullable: true
        paymentLinkUnicredit nullable: true
        paymentLinkPaypal nullable: true
        xmlCreatedBy nullable: true
        xmlCreatedWhen nullable: true
        xmlCreatedTotalPrice nullable: true
        xmlCreatedPriceExtra nullable: true
        xmlCreatedRentalPrice nullable: true
        xmlDeletedBy nullable: true
        xmlDeleted nullable: true
    }

    MRVehicleMovement getLastMovement(){
        if(movimentiAutos != null ){
            Iterator itr = movimentiAutos.iterator()
            while(itr.hasNext()){
                MRVehicleMovement mrVehicleMovement = (MRVehicleMovement)itr.next()
                if(mrVehicleMovement.isLast){
                    return mrVehicleMovement
                }
            }
        }
    }

    MRVehicle getVehicle(){
        if(movimentiAutos != null ){
            movimentiAutos.each {
                 return it.vehicle
				//return it.getVehicle() as MRVehicle
            }
        }
    }
}
