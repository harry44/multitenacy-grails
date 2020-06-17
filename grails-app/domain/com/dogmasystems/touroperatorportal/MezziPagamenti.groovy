package com.dogmasystems.touroperatorportal

class MezziPagamenti implements Serializable{

	String codice
	String descrizione
	String codiceEsportazione

	static hasMany = [pagamentis: Pagamenti]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "mezzi_pagamenti"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		version false
	}

	static constraints = {
		codice nullable: true
		descrizione nullable: true
		codiceEsportazione nullable: true
	}

	public String toString() {
		return
		(getCodice() != null ? getCodice() : "<>") + // NOI18N
				", " + // NOI18N
				(getDescrizione() != null ? getDescrizione() : "<>"); // NOI18N
	}
}
