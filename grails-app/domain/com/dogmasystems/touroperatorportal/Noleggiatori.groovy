package com.dogmasystems.touroperatorportal

import com.dogmasystems.fleet.db.MRFine
import com.dogmasystems.myrent.db.MRVehicle
class Noleggiatori implements Serializable{

	String ragioneSociale
	String via
	String numero
	String cap
	String citta
	String provincia
	String telefono1
	String telefono2
	String cellulare
	String email
	String partitaIva
	String codiceFiscale
	String nazione
	String promemoria
	// Added field
	Integer proKey

	static hasMany = [multes: MRFine,
	                  parcoVeicolis: MRVehicle]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "noleggiatori"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'noleggiatori_seq']
		id column: "id", sqlType: "int4"
		proKey column: "prokey"
		version false
	}

	static constraints = {
		ragioneSociale nullable: true
		via nullable: true
		numero nullable: true
		cap nullable: true
		citta nullable: true
		provincia nullable: true
		telefono1 nullable: true
		telefono2 nullable: true
		cellulare nullable: true
		email nullable: true
		partitaIva nullable: true
		codiceFiscale nullable: true
		nazione nullable: true
		promemoria nullable: true
		proKey nullable: true
	}
	String toString(){
		return ragioneSociale
	}
}
