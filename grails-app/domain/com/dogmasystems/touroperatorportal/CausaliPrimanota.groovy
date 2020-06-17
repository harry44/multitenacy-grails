package com.dogmasystems.touroperatorportal
import com.dogmasystems.myrent.db.MRVatRecord

class CausaliPrimanota implements Serializable{

	String descrizione
	Boolean dataDocReq
	Boolean numDocReq
	String causaleEsterna
	Boolean iva
	Boolean segnoIva
	Boolean gestIvaIndet
	Boolean liquidazioneIva
	MRVatRecord registriIva

	static hasMany = [primenotes: Primenote,
	                  righeCausales: RigheCausale]
	static belongsTo = [MRVatRecord]

	static mapping = {
		cache true
		////datasource 'myrent'
		table name: "causali_primanota"//, schema: "public"
		id column: "codice", generator: "assigned", sqlType: 'int4'
		registriIva column: 'id_registro_iva', sqlType: "int4"
		version false
	}

	static constraints = {
		descrizione nullable: true
		dataDocReq nullable: true
		numDocReq nullable: true
		causaleEsterna nullable: true
		iva nullable: true
		segnoIva nullable: true
		gestIvaIndet nullable: true
		liquidazioneIva nullable: true
		registriIva nullable: true
	}
}
