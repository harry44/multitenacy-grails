package com.dogmasystems.touroperatorportal

class Comuni implements Serializable{

	String nazione
	String comune
	String provincia
	String cap
	Integer prefisso
	String codicefiscale
	String codicestatistico

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "comuni"//, schema: "public"
		id generator:'sequence', params:[sequence:'comuni_seq']
		id column: "id", sqlType: "int4"
		version false
	}

	static constraints = {
		nazione nullable: true
		comune nullable: true
		provincia nullable: true
		cap nullable: true
		prefisso nullable: true
		codicefiscale nullable: true
		codicestatistico nullable: true
	}
}
