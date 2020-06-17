package com.dogmasystems.myrent.db

class MRTaxInvoice implements Serializable{

	Double deposit
	Double taxable
	Double tax
	MRVatCodes vatCodes
	MRHeaderInvoice headerInvoice

	static belongsTo = [MRVatCodes, MRHeaderInvoice]

	static mapping = {
		cache true
		table name: "fattura_imposta"//, schema: "public"
		id generator:'sequence', params:[sequence:'fattura_imposta_seq']
		id column: "id", sqlType: "int4"
		vatCodes column:"id_codice_iva", sqlType:"int4"
		headerInvoice column:"id_fattura", sqlType:"int4"
		deposit column:"acconto"
		taxable column:"imponibile"
		tax column:"imposta"
		version false
	}

	static constraints = {
		deposit nullable: true, scale: 17
		taxable nullable: true, scale: 17
		tax nullable: true, scale: 17
	}
}
