package com.dogmasystems.touroperatorportal

class ImportiTariffe implements Serializable{

	Integer minimo
	Integer massimo
	Boolean importoFisso
	Double importoBase
	Double importoExtra
	StagioneTariffa stagioneTariffa

	static belongsTo = [StagioneTariffa]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "importi_tariffe"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'importi_tariffe_seq']
		id column: "id", sqlType: "int4"
		stagioneTariffa column:"id_stagione", sqlType: "int4"
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
