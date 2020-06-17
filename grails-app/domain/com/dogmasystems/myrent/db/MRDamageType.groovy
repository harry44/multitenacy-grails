package com.dogmasystems.myrent.db

class MRDamageType implements Serializable{

	String description
	String descriptionEn


	static mapping = {
		cache true
		//table name: "mrdamage_type", schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'damage_type_seq']
		id column: "id", sqlType: "int4"
		description column: "description", sqlType: "varchar(255)"
		descriptionEn column: "description_en", sqlType: "varchar(255)"
		version false
	}
	static constraints = {
		description nullable: true
		descriptionEn nullable: true

	}
}
