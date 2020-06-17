package com.dogmasystems.touroperatorportal

class NazioniIso implements Serializable{

	String codice
	String codice3
	String nome

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "nazioni_iso"//, schema: "public"
		id name: "codice", generator: "assigned"
		version false
	}

	static constraints = {
		codice maxSize: 2
		codice3 nullable: true
		nome nullable: true
	}
}
