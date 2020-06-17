package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRAffiliate;
import com.dogmasystems.myrent.db.MREnumeration;
import com.dogmasystems.myrent.db.MRHeaderInvoice;
import com.dogmasystems.myrent.db.MRLocation;
import com.dogmasystems.myrent.db.MRRentalAgreement
import com.dogmasystems.myrent.db.MRUser

class Primenote implements Serializable{

	Date dataModifica
	Date dataCreazione
	Integer nrReg
	Date dataReg
	Integer annoCompetenza
	String nrDoc
	String nrDocExtra
	Date dataDoc
	Integer nrProtocollo
	String annotazioni
	Double totaleDocumento
	Double totaleImponibile
	Double totaleImposta
	Boolean editable
	Boolean esportato
	Integer fileId
	MRUser usersByIdUserModifica
	MRAffiliate affiliati
	MREnumeration numerazioniByIdSezionale
	MRUser usersByIdUser
	Partite partite
	MRUser usersByIdUserCreazione
	MRRentalAgreement contrattoNoleggio
	MRLocation sedi
	MREnumeration numerazioniByIdNumerazione
	CausaliPrimanota causaliPrimanota
	// Added field
	Integer proKey
	Date dataEffettuazione
	Boolean bene
	Boolean servizio

	/*static hasMany = [fatturaIntestaziones: FatturaIntestazione,
	                  partites: Partite,
	                  righeImpostas: RigheImposta,
	                  righePrimanotas: RighePrimanota]*/
	static hasMany = [fatturaIntestaziones: MRHeaderInvoice,
		righeImpostas: RigheImposta,
		righePrimanotas: RighePrimanota]
	//static belongsTo = [Affiliati, CausaliPrimanota, ContrattoNoleggio, Numerazioni, Partite, Sedi, Users]
	static belongsTo = [MRAffiliate, CausaliPrimanota, MRRentalAgreement, MREnumeration, MRLocation, MRUser]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "primenote"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'primenote_seq']
		id column: "id", sqlType: "int4"
		dataModifica sqlType:"date"
		dataCreazione sqlType:"date"
		dataReg sqlType:"date"
		dataDoc sqlType:"date"
		usersByIdUserModifica column: "id_user_modifica", sqlType:"int4"
		affiliati column: "id_affiliato", sqlType:"int4"
		numerazioniByIdSezionale column: "id_sezionale", sqlType:"int4"
		usersByIdUser column: "id_user", sqlType:"int4"
		partite column: "id_partita", sqlType:"int4"
		usersByIdUserCreazione column: "id_user_creazione", sqlType:"int4"
		contrattoNoleggio column: "id_contratto", sqlType:"int4"
		sedi column: "id_sede", sqlType:"int4"
		numerazioniByIdNumerazione column: "id_numerazione", sqlType:"int4"
		causaliPrimanota column: "id_causale", sqlType:"int4"

		fatturaIntestaziones column:"primenote_fatt_intest_id"
		proKey column: "prokey"
		dataEffettuazione column: 'data_eff',sqlType:"date"
		version false
	}

	static constraints = {
		dataModifica nullable: true
		dataCreazione nullable: true
		nrReg nullable: true
		dataReg nullable: true
		annoCompetenza nullable: true
		nrDoc nullable: true
		nrDocExtra nullable: true
		dataDoc nullable: true
		nrProtocollo nullable: true
		annotazioni nullable: true
		totaleDocumento nullable: true, scale: 17
		totaleImponibile nullable: true, scale: 17
		totaleImposta nullable: true, scale: 17
		editable nullable: true
		esportato nullable: true
		fileId nullable: true
		proKey nullable:true
		// Added constraints
		affiliati nullable: true
		contrattoNoleggio nullable: true
		numerazioniByIdNumerazione nullable: true
		numerazioniByIdSezionale nullable: true
		partite nullable: true
		sedi nullable: true
		usersByIdUser nullable: true
		usersByIdUserCreazione nullable: true
		usersByIdUserModifica nullable: true
		causaliPrimanota nullable: true
		dataEffettuazione nullable: true
		servizio nullable: true
		bene nullable: true
	}
}
