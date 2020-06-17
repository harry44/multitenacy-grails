package com.dogmasystems.touroperatorportal

class CodiciFiscali implements Serializable{

	String luogo
	String codice
	String codice2

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "codici_fiscali"//, schema: "public"
		id name: "luogo", generator: "assigned"
		version false
	}

	static constraints = {
		codice nullable: true
		codice2 nullable: true
	}
}
