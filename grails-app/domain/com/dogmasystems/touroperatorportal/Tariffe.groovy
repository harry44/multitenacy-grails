package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRGroup
import com.dogmasystems.myrent.db.MRReservation
import com.dogmasystems.myrent.db.MRVatCodes
import com.dogmasystems.myrent.db.MRRentalAgreement
import grails.util.Holders

class Tariffe implements Serializable{

	String descrizione
	Boolean ivaInclusaExtraPrepay
	Boolean multistagione
	Double depositoSuperAss
	Double depositoSenzaAss
	Double depositoContanti
	MRVatCodes codiciivaByIdCodIva
	MRVatCodes codiciivaByIdCodIvaNo
	MRVatCodes codiciivaByIdCodIvaExtraPrepay
	MRGroup gruppi
	// Added field
	Boolean oraRientroAttiva
	Integer proKey


	@Override
	String toString() {
		return descrizione
	}
	static hasMany = [contrattoNoleggios : MRRentalAgreement,
					  importiExtraPrepays: ImportiExtraPrepay,
					  optionalsTariffes  : OptionalsTariffe,
					  prenotazionis      : MRReservation,
					  preventivis        : Preventivi,
					  stagioneTariffas   : StagioneTariffa]
	static belongsTo = [MRVatCodes, MRGroup]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "tariffe"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'tariffe_seq']
		id column: "id", sqlType: "int4"
		codiciivaByIdCodIva column: "id_cod_iva", sqlType:"int4"
		codiciivaByIdCodIvaNo column: "id_cod_iva_no", sqlType:"int4"
		codiciivaByIdCodIvaExtraPrepay column: "id_cod_iva_extra_prepay", sqlType:"int4"
		gruppi column: "id_gruppo", sqlType:"int4"
		descrizione column:"descrizione"

		oraRientroAttiva column: "ora_rientro_attiva"
		proKey column: "prokey"
		version false
	}

	static constraints = {
		descrizione nullable: true
		ivaInclusaExtraPrepay nullable: true
		multistagione nullable: true
		depositoSuperAss nullable: true, scale: 17
		depositoSenzaAss nullable: true, scale: 17
		depositoContanti nullable: true, scale: 17
		proKey nullable: true
		oraRientroAttiva nullable: true
		codiciivaByIdCodIvaExtraPrepay nullable: true
		gruppi nullable: true
	}
}
