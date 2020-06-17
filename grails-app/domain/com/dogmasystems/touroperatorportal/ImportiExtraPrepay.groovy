package com.dogmasystems.touroperatorportal

class ImportiExtraPrepay implements Serializable{

	Integer minimo
	Integer massimo
	Boolean importoFisso
	Double importoBase
	Double importoExtra
	Tariffe tariffe

	static belongsTo = [Tariffe]

	static mapping = {
		//datasource 'myrent'
		cache true
		table name: "importi_extra_prepay"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'importi_extra_prepay_seq']
		//uncomment for import
		id column: "id", sqlType: "int4"
		tariffe column:"id_tariffa", sqlType: "int4"
		version false
	}

	static constraints = {
		minimo nullable: true
		massimo nullable: true
		importoFisso nullable: true
		importoBase nullable: true, scale: 17
		importoExtra nullable: true, scale: 17
	}
}
