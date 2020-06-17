package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRChartAccount;

class Distinte implements Serializable{

	Integer numero
	Date dataPresentazione
	String annotazione
	MRChartAccount pianoDeiConti

	static hasMany = [scadenzes: Scadenze]
	static belongsTo = [MRChartAccount]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "distinte"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		pianoDeiConti column:"id_conto_banca", sqlType:"int4"
		version false
	}

	static constraints = {
		numero nullable: true
		dataPresentazione nullable: true
		annotazione nullable: true
	}
}
