package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRGroup
import com.dogmasystems.myrent.db.MRLocation
import com.dogmasystems.myrent.db.MRReservationSource
import com.dogmasystems.myrent.db.MRUser
import grails.util.Holders

class Sconti implements Serializable{

	Double percentuale
	Integer durataMinima
	Date inizioSconto
	Date fineSconto
	Date inizioRiferimento
	Date fineRiferimento
	MRUser users
	MRGroup group
	MRLocation location
	Integer durataMax
	MRReservationSource fontiCommissioni

	static belongsTo = [MRUser]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "sconti"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		inizioSconto sqlType:"date"
		fineSconto sqlType:"date"
		inizioRiferimento sqlType:"date"
		fineRiferimento sqlType:"date"


			fontiCommissioni column: "id_fonte_commissione", sqlType:"int4"
			users column:"id_user", sqlType:"int4"
			durataMax column: "durata_max", sqlType:"int4"
			durataMinima column: "durata_minima", sqlType:"int4"
			group column: "id_gruppo", sqlType:"int4"
			location column: "id_sede", sqlType:"int4"


		version false
	}

	static constraints = {
		percentuale nullable: true, scale: 17
		durataMinima nullable: true
		inizioSconto nullable: true
		fineSconto nullable: true
		inizioRiferimento nullable: true
		fineRiferimento nullable: true
		users nullable: true
		durataMax nullable: true
		durataMinima nullable: true
		group nullable: true
		location nullable: true
		fontiCommissioni nullable: true
	}
}
