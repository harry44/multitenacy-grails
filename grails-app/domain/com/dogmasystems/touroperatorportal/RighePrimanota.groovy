package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRBusinessPartner
import com.dogmasystems.myrent.db.MRChartAccount;
import com.dogmasystems.myrent.db.MRVatCodes
import com.dogmasystems.myrent.db.MRVehicle

class RighePrimanota implements Serializable{

	Integer kmVeicolo
	Date dataScad
	String descrizione
	Double importo
	Boolean segno
	Integer numeroRiga
	MRVatCodes codiciiva
	Primenote primenote
	Garanzie garanzie
	MRBusinessPartner clientiByIdCliente
	MRVehicle parcoVeicoli
	Pagamenti pagamenti
	MRBusinessPartner clientiByIdFornitore
	MRChartAccount pianoDeiConti
	// Added new field
	Integer proKey

	static belongsTo = [MRBusinessPartner, MRVatCodes, Garanzie, Pagamenti, MRVehicle, MRChartAccount, Primenote]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "righe_primanota"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'righe_primanota_seq']
		id column: "id", sqlType: "int4"
		dataScad sqlType:"date"
		codiciiva column:"id_cod_iva", sqlType:"int4"
		primenote column:"id_primanota", sqlType:"int4"
		garanzie column:"id_garanzia", sqlType:"int4"
		clientiByIdCliente column:"id_cliente", sqlType:"int4"
		parcoVeicoli column:"id_veicolo", sqlType:"int4"
		pagamenti column:"id_pagamento", sqlType:"int4"
		clientiByIdFornitore column:"id_fornitore", sqlType:"int4"
		pianoDeiConti column:"id_conto", sqlType:"int4"
		proKey column: "prokey"
		version false
	}

	static constraints = {
		kmVeicolo nullable: true
		dataScad nullable: true
		descrizione nullable: true
		importo nullable: true, scale: 17
		segno nullable: true
		numeroRiga nullable: true
		// Added constraints
		clientiByIdFornitore nullable: true
		codiciiva nullable: true
		garanzie nullable: true
		pagamenti nullable: true
		parcoVeicoli nullable: true
		pianoDeiConti nullable: true
		clientiByIdCliente nullable: true
		proKey nullable: true
	}
}
