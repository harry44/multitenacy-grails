package com.dogmasystems.myrent.db

import org.apache.commons.lang.builder.EqualsBuilder

class MRVatRecord implements Serializable{

	/** Creates a new instance of RegistroIva */
	public MRVatRecord() {
	}

	String description;
	Boolean isVatCalculate;
	Boolean segnoLiquidazioneIva;
	MRChartAccount contoIva;

	public static final Boolean CALCOLO_IVA_IMPONIBILE = Boolean.TRUE;
	public static final Boolean CALCOLO_IVA_SCORPORO = Boolean.FALSE;

	public static final Boolean SEGNO_LIQUIDAZIONE_DEBITO = Boolean.FALSE;
	public static final Boolean SEGNO_LIQUIDAZIONE_CREDITO = Boolean.TRUE;

	//add these new column for exporting data to web portal
	String exportId;
	Boolean isExported;
	Boolean isUpdated;

//	public String toString() {
//		if(getDescription() != null) {
//			return getDescription();
//		} else {
//			return new String();
//		}
//	}

	public boolean equals(Object other) {
		if (other != null && (other instanceof MRVatRecord)) {
			return new EqualsBuilder().append(this.getId(), ((MRVatRecord)other).getId()).isEquals();
		} else {
			return false;
		}
	}

	static hasMany = [//causaliPrimanotas: CausaliPrimanota,
					  numerazionis: MREnumeration]
	static belongsTo = [MRChartAccount]

	static mapping = {
		cache true
		table name: "registri_iva"//, schema: "public"
		id generator:'sequence', params:[sequence:'registri_iva_seq']
		id column: "id", sqlType: "int4"
		description column:"descrizione"
		exportId column:"exportid"
		isExported column:"isexported"
		isUpdated column:"isupdated"
		isVatCalculate column:"calcolo_iva"
		segnoLiquidazioneIva column:"segno_liquidazione_iva"
		contoIva column: "id_conto_iva", sqlType: 'int4'
		//	pianoDeiConti column:"id_conto_iva", sqlType:"int4"
		version false
	}

	static constraints = {
		description nullable: true
		isVatCalculate nullable: true
		segnoLiquidazioneIva nullable: true
		exportId nullable: true
		isExported nullable: true
		isUpdated nullable: true
		contoIva nullable: true
	}
}
