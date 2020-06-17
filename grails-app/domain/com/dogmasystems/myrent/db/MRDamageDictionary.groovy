package com.dogmasystems.myrent.db

import grails.util.Holders

class MRDamageDictionary implements Serializable{

	String description
	String descriptionEn
	MRDamageType damageType
	Boolean isEnable


	static mapping = {
		cache true
		damageType cache: true
		table name: "damage_dictionary"//, schema: "public"
		id generator:'sequence', params:[sequence:'damage_dictionary_seq']
		//id generator: "assigned"
		id column: "id", sqlType: "int4"
		description column: "description", sqlType: "varchar(255)"
		descriptionEn column: "description_en", sqlType: "varchar(255)"

		isEnable column:"is_enable"
		damageType column :"id_damagetype" , sqlType: "int4"
		version false
	}
	static constraints = {
		description nullable: true
		damageType nullable:true
		descriptionEn nullable: true
		isEnable nullable: true
		
	}
}
