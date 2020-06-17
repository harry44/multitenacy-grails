package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRLocation
import grails.util.Holders;

class Regioni implements Serializable{

	String descrizione

	static hasMany = [sedis: MRLocation]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "regioni"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"

			descrizione column:"descrizione"

		version false
	}

	static constraints = {
		descrizione nullable: true
	}
}
