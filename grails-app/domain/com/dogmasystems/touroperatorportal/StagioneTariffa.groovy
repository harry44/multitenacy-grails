package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRVatCodes
import grails.util.Holders

class StagioneTariffa implements Serializable{

	String descrizione
	Boolean ivaInclusa
	Boolean multistagione
	Date inizio
	Date fine
	MRVatCodes codiciivaByIdCodIva
	MRVatCodes codiciivaByIdCodIvaNo
	Tariffe tariffe
	// Added field
	Integer tariffeId

	static hasMany = [importiTariffes: ImportiTariffe]
	static belongsTo = [MRVatCodes, Tariffe]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "stagione_tariffa"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'stagione_tariffa_seq']
		//uncomment for import pro db
		id column: "id", sqlType: "int4"
		inizio sqlType:"date"
		fine sqlType:"date"
		codiciivaByIdCodIva column:"id_cod_iva", sqlType: "int4"
		codiciivaByIdCodIvaNo column:"id_cod_iva_no", sqlType: "int4"
		tariffe column:"id_tariffa", sqlType: "int4"

		tariffeId column: "tariffe_id", sqlType: "int4"

		version false
	}

	static constraints = {
		descrizione nullable: true
		ivaInclusa nullable: true
		multistagione nullable: true
		inizio nullable: true
		fine nullable: true
		tariffeId nullable: true
	}
}
