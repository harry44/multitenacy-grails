package com.dogmasystems.touroperatorportal
import com.dogmasystems.myrent.db.MRBusinessPartner
class CoordinateBancarie implements Serializable{

	String banca
	String abi
	String cab
	String conto
	String cin
	String iban
	// Added field
	Integer proKey
	MRBusinessPartner clientiByIdCliente
	MRBusinessPartner clientiByIdFornitore

	static hasMany = [scadenzes: Scadenze]
	static belongsTo = [MRBusinessPartner]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "coordinate_bancarie"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'coordinate_bancarie_seq']
		//uncomment for import Pro db
		id column: "id", sqlType: "int4"
		clientiByIdCliente column: "id_cliente", sqlType: "int4"
		clientiByIdFornitore column: "id_fornitore", sqlType: "int4"
		proKey column: "prokey"
		version false
	}

	static constraints = {
		banca nullable: true
		abi nullable: true
		cab nullable: true
		conto nullable: true
		cin nullable: true
		iban nullable: true
		clientiByIdCliente nullable: true
		clientiByIdFornitore nullable: true
		proKey nullable: true
	}
}
