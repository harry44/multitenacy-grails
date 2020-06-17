package com.dogmasystems.touroperatorportal

class AnagraficaAzienda implements Serializable{

	Integer cap
	Integer capRiv
	Integer capProd
	String cellRiv
	String citta
	String cittaProd
	String cittaRiv
	Integer codiceDitta
	Integer codiceRiv
	String codiceFiscale
	String codiceFiscaleProd
	String codiceFiscaleRiv
	Date dataAttivazione
	Date dataUltimoAgg
	String email
	String emailProd
	String emailRiv
	String fax
	String faxProd
	String faxRiv
	Boolean moduloCnt
	Boolean moduloNb
	Boolean moduloNl
	String nazione
	String numero
	Integer numeroAutoMezzi
	String numeroRiv
	String numeroProd
	String partitaIva
	String partitaIvaProd
	String partitaIvaRiv
	String provincia
	String provinciaProd
	String provinciaRiv
	String ragioneSociale
	String ragioneSocialeProd
	String ragioneSocialeRiv
	String telefono
	String telProd
	String telRiv
	String via
	String viaProd
	String viaRiv
	Integer calcoloTariffa

	static mapping = {
		cache true
		////datasource 'myrent'
		table name: "anagrafica_azienda"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		version false
	}

	static constraints = {
		cap nullable: true
		capRiv nullable: true
		capProd nullable: true
		cellRiv nullable: true
		citta nullable: true
		cittaProd nullable: true
		cittaRiv nullable: true
		codiceDitta nullable: true
		codiceRiv nullable: true
		codiceFiscale nullable: true
		codiceFiscaleProd nullable: true
		codiceFiscaleRiv nullable: true
		dataAttivazione nullable: true
		dataUltimoAgg nullable: true
		email nullable: true
		emailProd nullable: true
		emailRiv nullable: true
		fax nullable: true
		faxProd nullable: true
		faxRiv nullable: true
		moduloCnt nullable: true
		moduloNb nullable: true
		moduloNl nullable: true
		nazione nullable: true
		numero nullable: true
		numeroAutoMezzi nullable: true
		numeroRiv nullable: true
		numeroProd nullable: true
		partitaIva nullable: true
		partitaIvaProd nullable: true
		partitaIvaRiv nullable: true
		provincia nullable: true
		provinciaProd nullable: true
		provinciaRiv nullable: true
		ragioneSociale nullable: true
		ragioneSocialeProd nullable: true
		ragioneSocialeRiv nullable: true
		telefono nullable: true
		telProd nullable: true
		telRiv nullable: true
		via nullable: true
		viaProd nullable: true
		viaRiv nullable: true
		calcoloTariffa nullable: true
	}
}
