package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRChartAccount;
import com.dogmasystems.myrent.db.MRRowInvoice;
import com.dogmasystems.myrent.db.MRVehicle
import grails.util.Holders;
class Carburanti implements Serializable{

	Double importoUnitario
	String descrizione
	String unitaMisura
	MRChartAccount pianoDeiConti
	// Added field
	Integer proKey
	String externalID
	static hasMany = [fatturaRigas: MRRowInvoice,
	                  parcoVeicolis: MRVehicle]
	static belongsTo = [MRChartAccount]

	static mappedBy = [fatturaRigas: "carburanti"]

	static mapping = {
		cache true
		////datasource 'myrent'
		table name: "carburanti"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'carburanti_seq']
		//import db pro
		id column: "id", sqlType: "int4"
		pianoDeiConti column: 'id_conto_ricavo', sqlType: "int4"

			proKey column: "prokey"

		version false
	}
	

	static constraints = {
		importoUnitario nullable: true, scale: 17
		descrizione nullable: true
		unitaMisura nullable: true
		pianoDeiConti nullable: true
		proKey nullable: true
		externalID nullable: true
	}
	String toString(){
		return descrizione 
	}
}
