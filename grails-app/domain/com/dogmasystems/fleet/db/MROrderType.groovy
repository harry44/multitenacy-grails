package com.dogmasystems.fleet.db

import java.util.Date;

class MROrderType implements Serializable{

	String description
	Boolean isRepair
	Boolean isMaintenance

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

	static constraints = {
		description unique:true
		isRepair defaultValue:false
		isMaintenance defaultValue:false
	}

	static mapping = {
		cache true
		id generator:'sequence', params:[sequence:'mrOrderType_seq']
	}

	@Override
	String toString() {
		return description
	}
}
