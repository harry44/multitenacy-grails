package com.dogmasystems.myrent.db

import grails.util.Holders

class MRDamageSeverity implements Serializable{

	String description
	String descriptionEn
	Integer priorityIndex
	String severityImage
	String severityOldImage


	static constraints = {

		priorityIndex nullable:true
		descriptionEn nullable: true
		severityImage nullable: true
		severityOldImage nullable: true
	}

	static mapping = {
		cache true
		//id generator: "assigned"
		//table name: "damage_severity", schema: "public"
		id generator:'sequence', params:[sequence:'damage_severity_seq']
		id column: "id", sqlType: "int4"
		descriptionEn column: "description_en", sqlType: "varchar(255)"
		description column: "description"



		severityImage column: "severity_image", sqlType: "text"
		severityOldImage column: "severity_old_image", sqlType: "text"
		version false
	}

}
