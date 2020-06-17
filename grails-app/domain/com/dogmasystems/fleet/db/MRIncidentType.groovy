package com.dogmasystems.fleet.db

import java.util.Date;

class MRIncidentType implements Serializable{

	String description
	Boolean isActive
	Boolean isPassive
	Boolean isTheft
	Boolean isAttemptedTheft

	static auditable = true
	Date dateCreated, lastUpdated

	Long tenantId

	def springSecurityService

	def beforeValidate() {
		def user = springSecurityService.currentUser
		if(user != null){
			tenantId = user.tenant?.tenantId
		}
	}

	static mapping = {
		cache true
		table name: "mrincident_type"
		id column: "id", sqlType: "int4"
		id generator:'sequence', params:[sequence:'mrIncidentType_seq']
		isAttemptedTheft column:"is_attempted_theft",defaultValue: false
		isTheft column:"is_theft",defaultValue: false
	}

	static constraints = {
		description unique:true
		isActive defaultValue:false
		isPassive defaultValue:false
		isTheft nullable:true
//		isTheft defaultValue:false
//		isAttemptedTheft defaultValue:false
		isAttemptedTheft nullable: true

	}
}
