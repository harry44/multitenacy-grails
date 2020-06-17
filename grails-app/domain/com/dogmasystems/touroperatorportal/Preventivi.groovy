package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRAffiliate;
import com.dogmasystems.myrent.db.MRBusinessPartner
import com.dogmasystems.myrent.db.MREnumeration;
import com.dogmasystems.myrent.db.MRGroup
import com.dogmasystems.myrent.db.MRLocation;

class Preventivi implements Serializable{

	String prefisso
	Integer numero
	Integer anno
	Date data
	Date inizio
	Date fine
	Double scontoPercentuale
	Integer kmTotali
	String note
	MRLocation sediByIdSedeUscita
	MRBusinessPartner clientiByIdCond3
	MRAffiliate affiliati
	MRBusinessPartner clientiByIdCond2
	MRBusinessPartner clientiByIdCond1
	MRBusinessPartner clientiByIdCliente
	MREnumeration numerazioni
	Pagamenti pagamenti
	MRGroup gruppi
	MRLocation sediByIdSedeRientroPrevisto
	Tariffe tariffe
	MRBusinessPartner agent

	static hasOne = [commissionis: Commissioni]
	static belongsTo = [MRAffiliate, MRBusinessPartner, MRGroup, MREnumeration, Pagamenti, MRLocation, Tariffe]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "preventivi"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'preventivi_seq']
		id column: "id", sqlType: "int4"
		data sqlType:"date"
		inizio sqlType:"date"
		fine sqlType:"date"
		sediByIdSedeUscita column: "id_sede_uscita", sqlType:"int4"
		clientiByIdCond3 column: "id_cond_3", sqlType:"int4"
		affiliati column: "id_affiliato", sqlType:"int4"
		clientiByIdCond2 column: "id_cond_2", sqlType:"int4"
		clientiByIdCond1 column: "id_cond_1", sqlType:"int4"
		clientiByIdCliente column: "id_cliente", sqlType:"int4"
		numerazioni column: "id_numerazione", sqlType:"int4"
		pagamenti column: "id_pagamento", sqlType:"int4"
		gruppi column: "id_gruppo", sqlType:"int4"
		sediByIdSedeRientroPrevisto column: "id_sede_rientro_previsto", sqlType:"int4"
		tariffe column: "id_tariffa", sqlType:"int4"
		agent column: "agent_id", sqlType:"int4"
		version false
		sort ([data:'desc',numero:'desc'])
	}

	static constraints = {
		prefisso nullable: true
		numero nullable: true
		anno nullable: true
		data nullable: true
		inizio nullable: true
		fine nullable: true
		scontoPercentuale nullable: true, scale: 17
		kmTotali nullable: true
		note nullable: true
		clientiByIdCond1 nullable: true
		clientiByIdCond2 nullable: true
		clientiByIdCond3 nullable: true
		commissionis nullable: true
		tariffe nullable:true
		agent nullable:true

	}
}
