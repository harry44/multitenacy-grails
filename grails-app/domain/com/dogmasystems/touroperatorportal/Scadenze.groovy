package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRBusinessPartner
import com.dogmasystems.myrent.db.MRHeaderInvoice
import grails.util.Holders;

class Scadenze implements Serializable{

	String annotazioni
	Date dataFattura
	Date dataPresentazione
	Date dataScadenza
	Boolean distinta
	Double importoFattura
	Double importoScadenza
	Integer noFattura
	Boolean saldo
	Integer tipoCliente
	Boolean tipoScadenza
	MRBusinessPartner clientiByIdCliente
	Distinte distinte
	Pagamenti pagamenti
	CoordinateBancarie coordinateBancarie
	MRBusinessPartner clientiByIdFornitore
	MRHeaderInvoice fatturaIntestazione

	static belongsTo = [MRBusinessPartner, CoordinateBancarie, Distinte, MRHeaderInvoice, Pagamenti]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "scadenze"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		dataFattura sqlType:"date"
		dataPresentazione sqlType:"date"
		dataScadenza sqlType:"date"
		clientiByIdCliente column:"id_cliente", sqlType:"int4"
		distinte column:"id_distinta", sqlType:"int4"
		pagamenti column:"id_pagamento", sqlType:"int4"
		coordinateBancarie column:"id_coordinate_bancarie", sqlType:"int4"
		clientiByIdFornitore column:"id_fornitore", sqlType:"int4"
		fatturaIntestazione column:"id_fattura", sqlType:"int4"


			annotazioni column:"annotazioni"

		version false
	}

	static constraints = {
		annotazioni nullable: true
		dataFattura nullable: true
		dataPresentazione nullable: true
		dataScadenza nullable: true
		distinta nullable: true
		importoFattura nullable: true, scale: 17
		importoScadenza nullable: true, scale: 17
		noFattura nullable: true
		saldo nullable: true
		tipoCliente nullable: true
		tipoScadenza nullable: true
	}
}
