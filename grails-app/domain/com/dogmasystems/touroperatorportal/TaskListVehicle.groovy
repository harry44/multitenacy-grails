package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRVehicle

class TaskListVehicle implements Serializable{

	MRVehicle parcoVeicoli
	//MovimentiAuto movimentiAuto
	String description
	Boolean isMandatory
	Boolean isCheckin
    static constraints = {
    }
	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "task_list_vehicle"//, schema: "public"
		//id generator: "assigned"
		id column: "id", sqlType: "int4"
		version false
	}
}
