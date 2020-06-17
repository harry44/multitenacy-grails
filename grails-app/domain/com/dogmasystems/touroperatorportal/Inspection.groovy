package com.dogmasystems.touroperatorportal

class Inspection implements Serializable{

	String description
	Integer location_id
	Integer vehicle_id
	Date created
	String created_by
	Date updated
	String update_by
	
	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "inspection"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		description type : 'text'
		version false
	}
    static constraints = {
    }
}
