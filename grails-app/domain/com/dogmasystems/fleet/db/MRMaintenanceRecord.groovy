package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRBusinessPartner
import com.dogmasystems.myrent.db.MRDamage
import com.dogmasystems.myrent.db.MRUser
import grails.util.Holders

import java.util.Date;
import com.dogmasystems.myrent.db.MRRentalAgreement;
import com.dogmasystems.myrent.db.MRAffiliate;
import com.dogmasystems.myrent.db.MRLocation;
import com.dogmasystems.myrent.db.MRVehicle;

class MRMaintenanceRecord implements Serializable{

    Integer recordNumber
    MRDocumentStatus status
    MRVehicle vehicle
    MRLocation location
    MRAffiliate affiliate

    String prefix
    Date data
    MRUser user
    MRRequestType requestType
    MRDocumentStatus requestStatus
    MRDocumentSeverity requestSeverity


    Date repairFrom
    Date repairTo
    Boolean isIncident
    MRIncident incident
    // ** for replacement fields (not mandatory) **
    Boolean isReplacement
    MRBusinessPartner insuranceCompany
    MRBusinessPartner garage
    MRBusinessPartner lawyer
    MRBusinessPartner salesRepresentative
    MRDocumentStatus documentStatus
    String replacedVehicle
    //** group of field days **
    Double estimatedDaysByGarage
    Double actualDays
    Double daysAskedToInsurance
    Double daysByInsuranceAssessor
    Double rentToRentCost
    //** group of field Documents **
    Date completedDocumentDate
    Date sentDocumentDate
    Date negotiationDate
    Date subpoenaDate
    //** group of field Invoices & Payments from Insurance Company**
    Double approvedDays
    Double approvedRentalDayAmount
    Double approvedAmount
    Date accruedDate
    String rentalInvoiceNumber
    Date rentalInvoiceDate
    Date rentalPaymentDate
    //** group of field Invoices & Payments to Lawyer **
    String lawyerInvoiceNumber
    Date lawyerInvoiceDate
    Date lawyerPaymentDate
    Double lawyerFee
    //** group of field Invoices & Payments to Garage **
    String garageInvoiceNumber
    Date garageInvoiceDate
    Date garagePaymentDate
    Double garageFee
    //** group of field Invoices & Payments to Sales Representative **
    String salesRepInvoiceNumber
    Date salesRepInvoiceDate
    Date salesRepPaymentDate
    Double salesRepFee
    //** group of field profit**
    Double rentalProfit
    Double lawyerFeeProfit



    String description
    Long vehicleMileage
    MRRentalAgreement rentalAgreement


    static auditable = true
    Date dateCreated, lastUpdated

    Long tenantId

    def springSecurityService
    def beforeValidate() {
        def user = springSecurityService.currentUser
        if(user != null){
            tenantId = user.tenant?.tenantId
        }
    }

    static hasMany = [
            repMaintRequests:MRRepMaintRequest,
            orders:MROrder,
            mainRecordDocumentManagement:MRMainRecordDocumentManagement,
            rentalAgreement:MRRentalAgreement,
            damage:MRDamage
    ]

    static mapping = {
        cache true
        id generator:'sequence', params:[sequence:'mrMaintRecord_seq']
        insuranceCompany lazy: false
        mainRecordDocumentManagement cascade: 'all-delete-orphan'
        data column: "date"
    }

    static constraints = {
        status nullable: true
        vehicle nullable: true
        location nullable: true
        affiliate nullable: true
        user nullable: true
        prefix nullable: true
        data nullable: true
        requestType nullable: true
        requestStatus nullable: true
        requestSeverity nullable: true
        repairFrom nullable: true
        repairTo nullable: true
        isIncident nullable: true
        incident nullable: true
        isReplacement nullable:true
        insuranceCompany nullable: true
        garage nullable: true
        lawyer nullable: true
        salesRepresentative nullable: true
        documentStatus nullable: true
        replacedVehicle nullable: true
        estimatedDaysByGarage nullable: true
        actualDays nullable: true
        daysAskedToInsurance nullable: true
        daysByInsuranceAssessor nullable: true
        rentToRentCost nullable: true
        completedDocumentDate nullable: true
        sentDocumentDate nullable: true
        negotiationDate nullable: true
        subpoenaDate nullable: true
        approvedDays nullable: true
        approvedRentalDayAmount nullable: true
        approvedAmount nullable: true
        accruedDate nullable: true
        rentalInvoiceNumber nullable: true
        rentalInvoiceDate nullable: true
        rentalPaymentDate nullable: true
        lawyerInvoiceNumber nullable: true
        lawyerInvoiceDate nullable: true
        lawyerPaymentDate nullable: true
        lawyerFee nullable: true
        garageInvoiceNumber nullable: true
        garageInvoiceDate nullable: true
        garagePaymentDate nullable: true
        garageFee nullable: true
        salesRepInvoiceNumber nullable: true
        salesRepInvoiceDate nullable: true
        salesRepPaymentDate nullable: true
        salesRepFee nullable: true
        rentalProfit nullable: true
        lawyerFeeProfit nullable: true
        vehicleMileage nullable: true
        rentalAgreement nullable:true

    }
}