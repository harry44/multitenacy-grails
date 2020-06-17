package com.dogmasystems.fleet.db

import java.util.Date;

import com.dogmasystems.myrent.db.MRDocumentManagement;

class MRRequestPicture implements Serializable{

	MRDocumentManagement gestioneDocumentale
	MRRepMaintRequest mrRepMaintRequest
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

	static belongsTo = [MRRepMaintRequest]

	static constraints = {
	}

	static mapping = {
		cache true
		//id generator: "assigned"
//		id column: "id", sqlType: "int4"
		id generator:'sequence', params:[sequence:'mrRequestPicture_seq']
		//gestioneDocumentale column:"id_gestione_documentale",sqlType: "int4"
		version false
	}
}
