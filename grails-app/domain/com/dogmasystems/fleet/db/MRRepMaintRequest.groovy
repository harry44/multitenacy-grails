package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRDamage
import grails.util.Holders

import java.util.Date;

import com.dogmasystems.myrent.db.MRAffiliate;
import com.dogmasystems.myrent.db.MRDocumentManagement;
import com.dogmasystems.myrent.db.MRLocation;
import com.dogmasystems.myrent.db.MRUser;
import com.dogmasystems.myrent.db.MRVehicle;

class MRRepMaintRequest implements Serializable {

	MRVehicle vehicle
	MRUser user
	MRLocation location
	MRAffiliate affiliate
	String prefix

//		Long number

	Long requestNumber


	Date data

	MRRequestType requestType
	MRDocumentStatus requestStatus
	MRDocumentSeverity requestSeverity

	Date repairFrom
	Date repairTo
	Boolean isIncident
	//va bene MRDamage o creiamo MRIncident??
	MRIncident incident
	String description
	Long vehicleKM

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


	static hasOne = [
//            incident: MRIncident,
maintenanceRecord: MRMaintenanceRecord
	]

	static hasMany = [repairRows:MRRepairRow,
					  requestPictures: MRDocumentManagement,
					  damage:MRDamage
	]

	static mapping = {
		cache true
		id generator:'sequence', params:[sequence:'mrRepMaintRequest_seq']

		requestNumber column: "number"
		data column: "date"
		requestPictures column: "mrrepmaintreq_reqpics", joinTable: [name: 'mrrep_maint_req_gestione_doc']
		damage cascade: "all-delete-orphan"
	}

	static constraints = {
//		idVehicle nullable: true
		vehicle nullable: true
//		descVehicle nullable: true
		user nullable: true
		location nullable: true
		affiliate nullable: true
		prefix nullable: true
		requestNumber nullable: true
		data nullable: true
		//pensare alla possibilità di farne un oggetto
		requestType nullable: true
		requestStatus nullable: true
		requestSeverity nullable: true
		repairFrom nullable: true
		repairTo nullable: true
		isIncident nullable: true
		//va bene MRDamage o creiamo MRIncident??
		incident nullable: true
		description nullable: true
		maintenanceRecord nullable: true
		vehicleKM nullable: true
//		requestNumber nullable: true
	}
}