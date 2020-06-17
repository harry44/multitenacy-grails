package com.dogmasystems.touroperatorportal

import grails.util.Holders

class Durate implements Serializable{

	Integer minimo
	Integer massimo
	Boolean importoFisso
	Integer durataIndex
	Listini listini

	static hasMany = [tariffeListinis: TariffeListini]
	static belongsTo = [Listini]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "durate"//, schema: "public"
		id generator:'sequence', params:[sequence:'durate_seq']
		//id generator: "assigned"
		id column: "id", sqlType: "int4"
		listini column:"id_listino", sqlType: "int4"
		durataIndex column:"index"
		version false
	}

	static constraints = {
		listini nullable: true
		minimo nullable: true
		massimo nullable: true
		importoFisso nullable: true
		durataIndex nullable: true
	}
}
