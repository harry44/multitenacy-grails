package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRBusinessPartner;
import com.dogmasystems.myrent.db.MRLocation
import com.dogmasystems.myrent.db.MRVehicle

class MRVehicleTransfer implements Serializable{
    enum Status {
        PLAN("Plan"),DONE("Done"),CANCEL("Cancel")

        final String status
        Status(String status){this.status=status}
    }
    enum Type {
        MOVEMENT("Movement"),TRANSPORT("Transport")
        final String type
        Type(String type) { this.type = type }

    }
    MRLocation locationPickUp
    MRLocation locationDropOff
    Date pickupDate
    Date dropoffDate
    Double estimatedCost
    Double estimatedKm
    Status status
    Type type
    String note
	MRBusinessPartner carrier

    static hasMany = [mrVehicleTransferVehicle:MRVehicleTransferVehicle]


    static constraints = {
        locationPickUp nullable: true
        locationDropOff nullable: true
        pickupDate nullable: true
        dropoffDate nullable: true
        estimatedCost nullable: true
        estimatedKm nullable: true
        note nullable: true
//        status inList: Status.values()*.status
//        type inList: Type.values()*.type

    }
    static mapping = {
        cache true
        table name: "vehicle_trans"
        id generator:'sequence', params:[sequence:'MRVehicleTransfer_seq']
        //note sqlType: 'text'
		mrVehicleTransferVehicle cascade: 'all-delete-orphan'
    }
}
