package com.dogmasystems.touroperatorportal

import grails.util.Holders

class VehicleAssistance implements Serializable{
	String description
	String workingHours
	String phoneNumberWorkingHours
	String phoneNumberNoWorkingHours
    static constraints = {
		description nullable:true
		workingHours nullable:true
		phoneNumberWorkingHours nullable:true
		phoneNumberNoWorkingHours nullable:true
		
    }
	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "vehicle_assistance"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		description column:"description"

		version false
    }
}
