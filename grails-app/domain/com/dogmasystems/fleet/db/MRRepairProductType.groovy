package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRChartAccount

import java.util.Date;

class MRRepairProductType implements Serializable{

	String description
	MRRepairType repairType

	MRChartAccount account

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

	}

	static mapping = {
		cache true
		id generator:'sequence', params:[sequence:'mrRepairProductType_seq']
	}
}
