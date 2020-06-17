package com.dogmasystems.myrent.db

import grails.util.Holders

class MRMacroClass implements Serializable{

	String description;
	String name;
	String exportCode;
	String webCode;
	String webDescrption

	static hasMany = [mrGroups: MRGroup]

	static mapping = {
		cache true
		table name:"macro_class"//, schema:"public"
		id generator:'sequence', params:[sequence:'macro_class_seq']
		id column: "id", sqlType: "int4"
		webDescrption column:"web_descrption"
		exportCode column:"exportcode"
		webCode column:"webcode"
		description column:"description"

		version false
	}

	static constraints = {
		description nullable: true
		name nullable: true
		exportCode nullable: true
		webCode nullable: true
		webDescrption nullable: true

	}
}
