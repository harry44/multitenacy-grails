package com.dogmasystems.touroperatorportal
import com.dogmasystems.myrent.db.MRUser
class LogEntries implements Serializable{

	String entityName
	Integer entityId
	String fieldName
	String fieldLabel
	String oldValue
	String newValue
	Date changeDate
	String sessionId
	MRUser users

	static belongsTo = [MRUser]

	static mapping = {
		cache true
		table name: "log_entries"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		changeDate sqlType:"date"
		users column:"changed_by", sqlType:"int4"
		version false
	}

	static constraints = {
		entityName nullable: true
		entityId nullable: true
		fieldName nullable: true
		fieldLabel nullable: true
		oldValue nullable: true
		newValue nullable: true
		changeDate nullable: true
		sessionId nullable: true
	}
}
