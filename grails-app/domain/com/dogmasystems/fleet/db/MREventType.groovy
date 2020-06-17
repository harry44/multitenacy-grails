package com.dogmasystems.fleet.db

import java.util.Date;

class MREventType implements Serializable{

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
	String description
	static hasMany = [ mrEventExpiration: MREventExpiration
	]
	static mapping = {
		cache true
		id generator:'sequence', params:[sequence:'mrEventType_seq']
		id column: "id", sqlType: "int4"
	}

	static constraints = {
	}
}
