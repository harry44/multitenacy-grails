package com.dogmasystems.touroperatorportal

class AccidentType implements Serializable{

	String description
    static constraints = {
		
		description nullable:true
		
    }
	static mapping = {
		cache true
		////datasource 'myrent'
		table name: "accident_type"//, schema: "public"
 		id column: "id", sqlType: "int4"
		version false
	}

}
