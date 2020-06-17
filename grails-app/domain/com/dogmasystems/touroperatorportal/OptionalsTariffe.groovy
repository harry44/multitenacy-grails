package com.dogmasystems.touroperatorportal

class OptionalsTariffe implements Serializable{

	Boolean incluso
	Boolean selezionato
	Boolean prepagato
	Boolean selezionatoRientro
	Boolean selezionatoFranchigia
	Double importo
	Double franchigia
	Double quantita
	Optionals optionals
	//Tariffe tariffe

	//static belongsTo = [Optionals, Tariffe]
	static belongsTo = [Optionals]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "optionals_tariffe"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		optionals column: "id_optional", sqlType: "int4"
		//tariffe column: "id_tariffa"//, sqlType: "int4"
		version false
	}

	static constraints = {
		incluso nullable: true
		selezionato nullable: true
		prepagato nullable: true
		selezionatoRientro nullable: true
		selezionatoFranchigia nullable: true
		importo nullable: true, scale: 17
		franchigia nullable: true, scale: 17
		quantita nullable: true, scale: 17
	}
}
