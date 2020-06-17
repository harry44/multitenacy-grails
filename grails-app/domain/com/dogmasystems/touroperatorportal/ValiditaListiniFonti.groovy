package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRLocation
import com.dogmasystems.myrent.db.MRReservationSource

class ValiditaListiniFonti implements Serializable{

	Date iniziostagione
	Date finestagione
	Date inizioofferta
	Date fineofferta
	MRReservationSource fontiCommissioni
	Listini listini
	MRLocation applicableLocation

	static belongsTo = [ Listini, MRLocation , MRReservationSource]

	static mapping = {
		//datasource 'myrent'
		cache true
		listini cache: true
		table name: "validita_listini_fonti"//, schema: "public"
		id column: "id", sqlType: "int4"
		id generator: 'sequence', params: [sequence: 'validita_listini_fonti_seq']
		iniziostagione sqlType:"date"
		finestagione sqlType:"date"
		inizioofferta sqlType:"date"
		fineofferta sqlType:"date"
		fontiCommissioni column: "id_fonte_commissione", sqlType:"int4"
		listini column: "id_listino", sqlType:"int4"
		applicableLocation column: "applicable_location", sqlType:"int4"
		version false
	}

	static constraints = {
		iniziostagione nullable: true
		finestagione nullable: true
		inizioofferta nullable: true
		fineofferta nullable: true
		applicableLocation nullable: true
	}
}
