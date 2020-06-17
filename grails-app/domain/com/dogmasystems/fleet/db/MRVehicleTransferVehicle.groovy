package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRVehicle;

class MRVehicleTransferVehicle implements Serializable{
	MRVehicle vehicle
	MRVehicleTransfer mrVehicleTransfer

	//static belongsTo = [MRVehicleTransfer]
	static constraints = {
	}
//    String toString(){return id}
static mapping={
	cache true
	table name:'parco_veicoli_vehicle_transfer'
	id composite: ['vehicle', 'mrVehicleTransfer']
}
}
