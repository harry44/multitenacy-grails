package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRChartAccount;

class RigheCausale implements Serializable{

	Boolean veicolo
	Boolean pagamento
	Boolean dataScadenza
	Boolean kmVeicolo
	Boolean autoCarica
	Boolean segno
	Integer numeroRiga
	MRChartAccount pianoDeiConti
	CausaliPrimanota causaliPrimanota

	static belongsTo = [CausaliPrimanota, MRChartAccount]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "righe_causale"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		pianoDeiConti column:"id_conto", sqlType:"int4"
		causaliPrimanota column:"id_causale", sqlType:"int4"
		version false
	}

	static constraints = {
		veicolo nullable: true
		pagamento nullable: true
		dataScadenza nullable: true
		kmVeicolo nullable: true
		autoCarica nullable: true
		segno nullable: true
		numeroRiga nullable: true
	}
}
