package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRUser
import grails.util.Holders

import java.util.Date;

import com.dogmasystems.myrent.db.MRRentalAgreement;
import com.dogmasystems.myrent.db.MRVehicleMovement;

class MRIncident implements Serializable{

    Date incidentDate
    MRRentalAgreement rentalAgreement//not mandatory
    MRVehicleMovement vehicleMovement//not mandatory
    String description
    MRIncidentType incidentType //tabella	//needed
    Double amountToCharge
    Double amountToPay



    String address
    String  town
    MRUser user

    Boolean isCharged
   // static auditable = true
    Date dateCreated, lastUpdated

    Long tenantId

    def springSecurityService

    def beforeValidate() {
        def user = springSecurityService.currentUser
        if (user != null) {
            tenantId = user.tenant?.tenantId
        }
    }

    static constraints = {
        rentalAgreement nullable:true
        vehicleMovement nullable:true
        incidentType nullable:true
        amountToCharge nullable:true
        amountToPay nullable:true
        description nullable:true
        isCharged nullable:true
        address nullable:true
        town nullable:true
        user nullable:true
//        order nullable:true
//        repMaintRequest nullable:true

//        isCharged defaultValue: false
    }

    static mapping = {
        cache true
        id generator: 'sequence', params: [sequence: 'mrIncident_seq']
        description type: 'text'
            address type: 'text'
    }

    @Override
    String toString() {
        return description
    }
}

