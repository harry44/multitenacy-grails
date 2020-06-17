package com.dogmasystems.myrent.db

import grails.util.Holders

class MRSourceType implements Serializable{

	String description;

	public String toString() {
		return description != null ? description : new String();
	}

	static mapping = {
		cache true
		table name: "fonte_type"//, schema: "public"
		id generator:'sequence', params:[sequence:'fonte_type_seq']
		id column: "id", sqlType: "int4"
		description column:"description",type:"java.lang.String"

		version false
	}

	static constraints = {
	}
}
