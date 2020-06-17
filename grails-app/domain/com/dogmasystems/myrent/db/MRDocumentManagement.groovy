package com.dogmasystems.myrent.db

import com.dogmasystems.touroperatorportal.DocumentiContrattoNoleggio
import com.dogmasystems.fleet.db.MRFine
import grails.util.Holders

class MRDocumentManagement implements Serializable{

//	DocumentiContrattoNoleggio tipo
	String keywords
	String description
	Date paidOn
	Double cost
	Date due_date
	Date load_date
	MRUser created_by
	String path_file
	String archiving_protocol
	MRVehicle vehicle
	MRVehicleMovement vehicleMovement
	MRRentalAgreement rentalAgreement
	DocumentiContrattoNoleggio tipo
	MRFine fine
	MRBusinessPartner businessPartner
	MRReservation reservation
	RentToRent renttorent

//	static belongsTo =[MRUser,DocumentiContrattoNoleggio,MRVehicle]
	static belongsTo =[MRUser,MRVehicle,DocumentiContrattoNoleggio]
	static hasMany=[documents:MRVehicle]
	static mapping = {
		cache true
		vehicleMovement cache:true
		rentalAgreement cache:true
		reservation cache:true
		businessPartner cache:true
		fine cache:true
		tipo cache:true
		vehicle  cache:true
		//table name: "documents_management", schema: "public"
		//id generator: "assigned"
		table name:"gestione_documentale"//, schema:'public'
		id generator:'sequence', params:[sequence:'gestione_documentale_seq']
		id column: "id", sqlType: "int4"
		vehicleMovement column: "movimenti_auto_id", sqlType: "int4"
		rentalAgreement column:"ra_id" , sqlType: "int4"
		vehicle column: "parco_veicoli_id", sqlType: "int4"
		description type : 'text', column : "descrizione"
		keywords column : "keywords"
		due_date column : "data_scadenza", sqlType: "date"
		load_date column : "data_caricamento", sqlType: "date"
		archiving_protocol column: "protocollo_archiviazione"
		tipo column:"tipo_id", sqlType:"int4"
		created_by column:"created_by",sqlType:"int4"
		fine column:"fine_id" ,sqlType:"int4"
		reservation column:"reservation_id" ,sqlType:"int4"
		businessPartner column:"businessPartner_id" ,sqlType:"int4"
		renttorent column:"renttorent_id" ,sqlType:"int4"


		//documents joinTable:[name: "documenti_veicoli", key: 'id_documento_veicolo']

		documents column: "mrdoc_management_doc_id", joinTable: [name: 'gestione_doc_parco_veicoli']

		version false
	}

	static constraints = {

		description nullable: true
		archiving_protocol nullable: true
		keywords nullable: true
		created_by nullable: true
		due_date nullable:true
		tipo nullable: true
		fine nullable:true
		vehicleMovement nullable: true
		vehicle nullable: true
		rentalAgreement nullable:true
		businessPartner nullable:true
		reservation nullable:true
		paidOn nullable: true
		cost nullable: true
		renttorent nullable: true
	}
}
