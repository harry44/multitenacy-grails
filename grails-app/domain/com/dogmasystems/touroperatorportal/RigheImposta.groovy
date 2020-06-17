package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRChartAccount;
import com.dogmasystems.myrent.db.MRVatCodes

class RigheImposta implements Serializable{

	Integer numeroRiga
	Double imponibile
	String descrizione
	Double importo
	Boolean segno
	Boolean corrIva
	MRVatCodes codiciiva
	Primenote primenote
	MRChartAccount pianoDeiConti
	// Added field
	Integer proKey
	static belongsTo = [MRVatCodes, MRChartAccount, Primenote]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "righe_imposta"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'righe_imposta_seq']
		id column: "id", sqlType: "int4"
		codiciiva column: "id_codice_iva", sqlType: "int4"
		primenote column: "id_primanota", sqlType: "int4"
		pianoDeiConti column: "id_conto", sqlType: "int4"
		proKey column: "prokey"
		version false
	}

	static constraints = {
		numeroRiga nullable: true
		imponibile nullable: true, scale: 17
		descrizione nullable: true
		importo nullable: true, scale: 17
		segno nullable: true
		corrIva nullable: true

		// Added constraint
		pianoDeiConti nullable: true
		proKey nullable:true
		codiciiva nullable: true
	}
}
