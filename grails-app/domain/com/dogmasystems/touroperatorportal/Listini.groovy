package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRGroup
import com.dogmasystems.myrent.db.MRLocation
import com.dogmasystems.myrent.db.MRRateType
import com.dogmasystems.myrent.db.MRVatCodes
import com.dogmasystems.myrent.db.MRReservationSource
import grails.util.Holders

class Listini implements Serializable,Cloneable{

	String descrizione
	Integer numeroDocumento
	Boolean ivaInclusa
	Double depositoSuperAss
	Double depositoSenzaAss
	Double depositoContanti
	String annotazioni
	MRVatCodes codiciivaByIdCodIva
	MRVatCodes codiciivaByIdCodIvaNo
	MRLocation sede

	Boolean oraRientroAttiva
	Date oraRientro
	MRRateType rateTypeId
	Listini optionalRate
	Boolean isCalendar;
	Boolean isDisable;

	static hasMany = [durates: Durate,
					  fontiCommissionis: MRReservationSource,
					  optionalsListinis: OptionalsListini,
					  validitaListiniFontis: ValiditaListiniFonti,
					  listinigroups: MRGroup,
					  //listinilocations: MRLocation,
					  locations:MRLocation,
					  groups:MRGroup
	]
	static belongsTo = [MRVatCodes]

	static mapping = {
		//datasource 'myrent'
		cache true
		table name: "listini"//, schema: "public"
		id generator: 'sequence', params:[sequence:'listini_seq']
		id column: "id", sqlType: "int4"
		codiciivaByIdCodIva column: "id_cod_iva", sqlType: "int4", lazy: false
		codiciivaByIdCodIvaNo column: "id_cod_iva_no", sqlType: "int4" ,lazy:false
		rateTypeId column: "ratetypeid", sqlType: "int4"
		optionalRate sqlType: "int4"
		oraRientro column: "ora_rientro"
		oraRientroAttiva column: "ora_rientro_attiva"
		sede column: "id_sede",sqlType: "int4",lazy: false
		annotazioni column: "annotazioni"
		depositoContanti column: "deposito_contanti"
		depositoSenzaAss column: "deposito_senza_ass"
		depositoSuperAss column: "deposito_super_ass"
		ivaInclusa column: "iva_inclusa"
		numeroDocumento column: "numero_documento"
		isCalendar column: "is_calendar"
		isDisable column: "isdisable"
		optionalRate column:"optional_rate_id"
		descrizione column: "descrizione"
		annotazioni column: "annotazioni"

		//if(Holders.grailsApplication.config.com.dogmasystems.postgres==true){
			groups joinTable: [name:'listini_gruppi', key: 'listini_groups_id']
		//}else{
			listinigroups joinTable: [name:'listini_gruppi', key: 'listini_groups_id']
	//	}
	//	if(Holders.grailsApplication.config.com.dogmasystems.postgres==true){
			locations joinTable: [name:'listini_sedi', key: 'listini_locations_id']
		//}else{
		//	listinilocations joinTable: [name:'listini_sedi', key: 'listini_locations_id']
	//	}



		version false
	}

	static constraints = {
		groups nullable: true
		listinigroups nullable: true
		//listinilocations nullable: true
		locations nullable: true
		isDisable nullable: true
		descrizione nullable: true
		numeroDocumento nullable: true
		ivaInclusa nullable: true
		depositoSuperAss nullable: true, scale: 17
		depositoSenzaAss nullable: true, scale: 17
		depositoContanti nullable: true, scale: 17
		annotazioni nullable: true
		oraRientro nullable: true
		oraRientroAttiva nullable: true
		isCalendar nullable: true
		sede nullable: true
		rateTypeId nullable:true
	}
	public Object clone() {
		Listini obj = new Listini();


		if (this.getDescrizione() != null) {
			obj.setDescrizione("copy of " + this.getDescrizione());
		}

		obj.setCodiciivaByIdCodIva(this.getCodiciivaByIdCodIva());
		obj.setCodiciivaByIdCodIvaNo(this.getCodiciivaByIdCodIvaNo());
		obj.setAnnotazioni(this.getAnnotazioni());
		obj.setDepositoContanti(this.getDepositoContanti());
		obj.setDepositoSenzaAss(this.getDepositoSenzaAss());
		obj.setDepositoSuperAss(this.getDepositoSuperAss());
		obj.setIsCalendar(this.getIsCalendar());
		obj.setIsDisable(this.getIsDisable());
		obj.setOptionalRate(this.getOptionalRate());
//        obj.setNumeroDocumento(this.getMultiSeason());
		obj.setOraRientro(this.getOraRientro());
		obj.setOraRientroAttiva(this.getOraRientroAttiva());
		obj.setSede(this.getSede());
		obj.setRateTypeId(this.getRateTypeId())

		return obj;
	}
}
