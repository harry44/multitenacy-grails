package com.dogmasystems.fleet.db

import java.util.Date;

class MRRequestType implements Serializable{

	int typeId
	String description
	String lang

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
		id generator:'sequence', params:[sequence:'mrRequestType_seq']
	}

	static constraints = {
	}

	@Override
	String toString() {
		return description
	}
}
