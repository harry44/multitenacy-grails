package com.dogmasystems.fleet.db

import grails.util.Holders

class MRDocumentSeverity implements Serializable{

//	int severityId
	String description
	String lang
	static auditable = true
	Date dateCreated, lastUpdated
	Integer damageSeverity
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
		table name: "mrdocument_severity"//, schema: "public"
		id generator:'sequence', params:[sequence:'mrDocumentSeverity_seq']
		tenantId column: "tenant_id"//, sqlType: "int4"
		id column: "id", sqlType: "int4"
		damageSeverity column: 'severity_id', sqlType: "int4"
		version false
	}

	static constraints = {
	}

	@Override
	String toString() {
		return description;
	}
}
