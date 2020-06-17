package com.dogmasystems.myrent.db

import grails.util.Holders

//import com.dogmasystems.utils.db.PersistentInstance;

class MRSourceClass implements Serializable{

	String description;
	MRSourceTarget sourceTarget;
	MRSourceType sourceType;


	public String toString() {
		return description != null ? description : new String();
	}

	static mapping = {
		cache true
		table name: "fonte_class"//, schema: "public"
		id generator:'sequence', params:[sequence:'fonte_class_seq']
		id column: "id", sqlType: "int4"
		sourceTarget column: 'id_fonte_target', sqlType:"int4"
		sourceType column: 'id_fonte_type', sqlType:"int4"
		description column: 'description'
		version false
	}

	static constraints = {
	}


//
//	@Override
//	public Long getId() {
//		return this.id;
//	}
//	public void setId(Long id){
//		this.id = id;
//	}
}
