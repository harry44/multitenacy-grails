package com.dogmasystems.touroperatorportal
import com.dogmasystems.myrent.db.MRGroup

class OptionalsListini implements Serializable{

	Boolean incluso
	Double importo
	Double franchigia
	Listini listini
	Optionals optionals
	MRGroup gruppi

	static belongsTo = [MRGroup, Listini, Optionals]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "optionals_listini"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		listini column: "id_listino", sqlType: "int4"
		optionals column: "id_optional", sqlType: "int4"
		gruppi column: "id_gruppo", sqlType: "int4"
		version false
	}

	static constraints = {
		incluso nullable: true
		importo nullable: true, scale: 17
		franchigia nullable: true, scale: 17
	}
}
