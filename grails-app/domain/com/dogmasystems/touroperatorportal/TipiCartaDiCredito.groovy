package com.dogmasystems.touroperatorportal

class TipiCartaDiCredito implements Serializable{

	String descrizione

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "tipi_carta_di_credito"//, schema: "public"
		id name: "descrizione", generator: "assigned"
		version false
	}
}
