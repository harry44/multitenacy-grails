package com.dogmasystems.myrent.db

class MRCancellingReason implements Serializable{
	String description
	static mapping={
		cache true
		table name:"cancelingreason"
		id generator:'sequence', params:[sequence:'cancelingreason_seq']
	}
    static constraints = {
		description nullable:true
    }
}
