package com.dogmasystems.touroperatorportal

class Documenti implements Serializable{

	String descrizione

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "documenti"//, schema: "public"
		id name: "descrizione", generator: "assigned"
		version false
	}
	
	
	String toString() {
		def des = ""
		des = descrizione
		return des
	}
}
