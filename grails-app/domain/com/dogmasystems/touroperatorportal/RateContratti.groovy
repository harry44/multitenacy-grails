package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRHeaderInvoice;
import com.dogmasystems.myrent.db.MRRentalAgreement

class RateContratti implements Serializable{

	Double totaleImponibile
	Double totaleIva
	Double totaleFattura
	MRRentalAgreement contrattoNoleggio
	MRHeaderInvoice fatturaIntestazione

	static belongsTo = [MRRentalAgreement, MRHeaderInvoice]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "rate_contratti"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		contrattoNoleggio column:"id_contratto", sqlType:"int4"
		fatturaIntestazione column:"id_fattura", sqlType:"int4"
		version false
	}

	static constraints = {
		totaleImponibile nullable: true, scale: 17
		totaleIva nullable: true, scale: 17
		totaleFattura nullable: true, scale: 17
	}
}
