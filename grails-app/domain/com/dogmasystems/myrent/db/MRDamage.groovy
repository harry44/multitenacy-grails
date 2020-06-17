package com.dogmasystems.myrent.db

import grails.util.Holders

import java.util.Date;


class MRDamage implements Serializable{

	Date detectionDate
	Date repairDate
	Boolean isRepaired
	String description
	MRVehicle vehicle
	MRVehicleMovement vehicleMovement
	///////////////////new fields
	MRDamageDictionary damageDictionary
	MRDamageType damageType
	MRDamageSeverity damageSeverity
	Double appliedPrice
	Double cost
	//Inspection inspection
	Date created
	MRUser createdBy
	Date updated
	MRUser updatedBy

	Boolean checkIn
	/////////////////////////////
	static hasMany = [dannimovimentis: MRDamageMovement,dannidip: MRDamageInPicture]
	//static hasMany = [movimentiAutos: MovimentiAuto]
	//static belongsTo = [MovimentiAuto, ParcoVeicoli]
	static belongsTo = [vehicle:MRVehicle, vehicleMovement:MRVehicleMovement]
//	static belongsTo = [movements:MRVehicleMovement]
	

	//static mappedBy = [movements: "damages"]
	
	static mapping = {
		cache true
		table name:"danni"//, schema:'public'
		id generator:'sequence', params:[sequence:'danni_seq']
		id column: "id", sqlType: "int4"
		//id generator:"sequence",params:[name:'danni_id_seq',startwith:3707]
		detectionDate column:"data_rilevamento", sqlType: "date"//, defaultValue: new Date()
		repairDate column:"data_riparazione", sqlType: "date"
		vehicle column: "id_veicolo", sqlType: "int4"
//		movimentiAuto column: "id_movimento", sqlType: "int4"
		damageDictionary  column:"id_damagedictionary",sqlType:"int4"
		damageSeverity column:"id_damageseverity",sqlType:"int4"
		damageType column:"id_damagetype",sqlType:"int4"
		vehicleMovement column:"id_movimento",sqlType:"int4"

		//	inspection column:"id_inspection",sqlType:"int4"
		created sqlType: "date", column: "created"
		updated sqlType: "date", column: "updated"
		isRepaired column:"riparato",  defaultValue: false
		//movements joinTable:[name:"danni_movimenti", key:"id_danno"]
		description column:"descrizione"
		
		version false
	}

	static constraints = {
		detectionDate nullable: true
		repairDate nullable: true
		isRepaired nullable: true
		description nullable: true
		//new field
		damageType nullable:true
		damageDictionary nullable: true
		damageSeverity nullable: true
		appliedPrice nullable: true
		cost nullable: true
		//inspection nullable: true
		created nullable:true
		createdBy nullable:true
		updated nullable:true
		updatedBy nullable:true
		vehicleMovement nullable: true
		checkIn nullable: true

	}
}
