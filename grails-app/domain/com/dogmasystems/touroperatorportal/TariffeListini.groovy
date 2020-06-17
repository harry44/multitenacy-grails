package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRGroup
import com.dogmasystems.myrent.db.MRLocation
import grails.util.Holders

class TariffeListini implements Serializable{

	Double importoBase
	Double importoExtra
	MRGroup gruppi
	Durate durate
	MRLocation location
	Listini listini
	Date date

	static belongsTo = [Durate, MRGroup]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "tariffe_listini"//, schema: "public"
		id generator:'sequence', params:[sequence:'tariffe_listini_seq']
		id column: "id", sqlType: "int4"
		gruppi column:"id_gruppo", sqlType:"int4"
		durate column:"id_durata", sqlType:"int4"


			listini column: "listini_id" , sqlType:"int4"

			location column: "location_id" , sqlType: "int4"



			date column: "date"


		version false
		sort id:'asc'
	}

	static constraints = {
		importoBase nullable: true, scale: 17
		importoExtra nullable: true, scale: 17
		date nullable: true
		listini nullable: true
		location nullable: true
		gruppi nullable: true
	}
}
