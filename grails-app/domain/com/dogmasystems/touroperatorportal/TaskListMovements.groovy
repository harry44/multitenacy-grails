package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRVehicle
import com.dogmasystems.myrent.db.MRVehicleMovement
//import com.sun.org.apache.xpath.internal.operations.Bool;

class TaskListMovements implements Serializable{

	MRVehicle parcoVeicoli
	MRVehicleMovement movimentiAuto
	Boolean isChecked
	TaskListVehicle taskId
    static constraints = {
    }
	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "task_list_movements"//, schema: "public"
		//id generator: "assigned"
		id column: "id", sqlType: "int4"
		version false
	}
}
