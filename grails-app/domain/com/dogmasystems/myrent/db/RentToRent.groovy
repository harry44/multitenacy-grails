package com.dogmasystems.myrent.db

import com.dogmasystems.touroperatorportal.Pagamenti

class RentToRent implements Serializable{


    MRLocation  location
    MRBusinessPartner customer
    String nsaExportCode
    String exportContractNumber
    MRVehicle vehicle
    Double monthlyFee
    Date raStartDate
    Date raEndDate
    Date deliveredDate
    Boolean isVehicleReturned
    Date returnedDate
    Date lastBilledDate
    Date lastBillingDate
    Double durationInMonth
    Integer kmIncluded
    Double extraKmAmount
    Double damageExcess
    Double theftExcess
    Integer tyres
    Boolean isMaintenanceIncluded
    Boolean isRoadAssistanceIncluded
    /// new Added fields : harry
    MRBusinessPartner clientiByIdCond1
    MRBusinessPartner clientiByIdCond2
    MRBusinessPartner clientiByIdCond3
    MRVehicleMovement startMovement
    MRVehicleMovement endMovement
    String publicNote;
    Pagamenti pagamento
    Boolean is30DaysInvoice;
    Boolean isCanceled
    Date contractDate
    Double kasko
    Double fineFee
   /// End new Added Fields
    String prefix
    Integer number
    Double amountFirstInstallment
    Double amountLastInstallment
    String note
    Boolean isBilledInAdvance
    MRBusinessPartner agent
    Integer period

    Boolean signedCheckOut
     Boolean signedCheckIn

    static mapping = {
        cache true
        ////datasource 'myrent'
        table name: "renttorent"//, schema: "public"
        id generator: 'sequence', params: [sequence: 'renttorent_seq']
        id column: "id", sqlType: "int4"
        agent column: 'agent_id', sqlType: "int4"
        clientiByIdCond1 column:'id_cond_1',sqlType: "int4"
        clientiByIdCond2 column:'id_cond_2',sqlType: "int4"
        clientiByIdCond3 column:'id_cond_3',sqlType: "int4"
        location column: 'location_id', sqlType: "int4"
        customer column: 'customer_id', sqlType: "int4"
        nsaExportCode column: 'nsa_export_code'
        exportContractNumber column: 'export_contract_number'
        vehicle column: 'vehicle_id', sqlType: "int4"
        monthlyFee column: 'monthly_fee'
        raStartDate column: 'ra_start_date', sqlType: "date"
        raEndDate column: 'ra_end_date', sqlType: "date"
        deliveredDate column: 'delivered_date', sqlType: "date"
        isVehicleReturned column: 'isVehicleReturned'
        returnedDate column: 'returnedDate', sqlType: "date"
        lastBilledDate column: 'last_billed_date', sqlType: "date"
        lastBillingDate column: 'last_Billing_Date', sqlType: "date"
        durationInMonth column: 'durationInMonth'
        kmIncluded column: 'kmIncluded', sqlType: "int4"
        extraKmAmount column: 'extra_km_amount'
        damageExcess column: 'damage_excess'
        theftExcess column: 'theft_excess'
        tyres column: 'tyres', sqlType: "int4"
        isMaintenanceIncluded column: 'isMaintenanceIncluded'
        isRoadAssistanceIncluded column: 'isRoadAssistanceIncluded'
        prefix column: 'prefix'
        number column: 'number'
        amountFirstInstallment column: 'amount_first_installment'
        amountLastInstallment column: 'amount_last_installment'
        note column: 'note'
        isBilledInAdvance column: 'is_billed_in_advance'
        period column: 'period', sqlType: "int4"
        contractDate column: 'contract_date', sqlType: "date"
        pagamento column:'pagamento' ,sqlType: "int4"
        startMovement column: 'id_start_movement',sqlType: "int4"
        endMovement column: 'id_end_movement',sqlType: "int4"
        is30DaysInvoice column: 'is_30_days_invoice'
        publicNote column: 'public_note'
        fineFee column: 'fine_fee'
        signedCheckOut column :'signed_check_out'
        signedCheckIn column :'signed_check_in'
        version false
    }
    static constraints = {
        location nullable: true
        customer nullable: true
        vehicle nullable: true
        nsaExportCode nullable: true
        exportContractNumber nullable: true
        monthlyFee nullable: true
        raStartDate nullable: true
        raEndDate nullable: true
        deliveredDate nullable: true
        isVehicleReturned nullable: true
        returnedDate nullable: true
        lastBilledDate nullable: true
        lastBillingDate nullable: true
        durationInMonth nullable: true
        kmIncluded nullable: true
        extraKmAmount nullable: true
        damageExcess nullable: true
        theftExcess nullable: true
        isMaintenanceIncluded nullable: true
        isRoadAssistanceIncluded nullable: true
        prefix nullable: true
        number nullable: true
        amountFirstInstallment nullable: true
        amountLastInstallment nullable: true
        note nullable: true
        isBilledInAdvance nullable: true
        agent nullable: true
        period nullable: true
        contractDate nullable: true
        tyres nullable:true
        startMovement nullable:true
        endMovement nullable: true
        clientiByIdCond1 nullable: true
        clientiByIdCond2 nullable: true
        clientiByIdCond3 nullable: true
        is30DaysInvoice nullable: true
        publicNote nullable: true
        kasko nullable: true
        pagamento nullable: true
        fineFee nullable: true
        isCanceled nullable: true,default:false
        version nullable: true
        signedCheckOut nullable: true
        signedCheckIn nullable: true
    }
}
