package com.dogmasystems.fleet.db

import java.util.Date;

class MRDocumentType implements Serializable{

	String description
	Boolean isSales

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
		id generator:'sequence', params:[sequence:'mrDocumentType_seq']
	}

	static constraints = {
		description unique:true
		isSales defaultValue:false
	}

	@Override
	String toString() {
		return description
	}
}
