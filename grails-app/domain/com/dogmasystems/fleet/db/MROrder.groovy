package com.dogmasystems.fleet.db

import java.util.Date;

import com.dogmasystems.myrent.db.MRAffiliate;
import com.dogmasystems.myrent.db.MRBusinessPartner;
import com.dogmasystems.myrent.db.MRDocumentManagement;
import com.dogmasystems.myrent.db.MRLocation;
import com.dogmasystems.myrent.db.MRUser;
import com.dogmasystems.myrent.db.MRVehicle;
//import com.dogmasystems.AccCostCenter;TODO

class MROrder implements Serializable{

	MROrderType orderType	//needed
	String prefix
	Long orderNumber
	Date date
	MRBusinessPartner businessPartner
	MRDocumentStatus orderStatus	//needed
	MRDocumentType documentType	//needed
	MRVehicle vehicle
	MRUser user
	MRLocation location
	MRAffiliate affiliate
	Boolean isInspectionRequired
	MRRequestType requestType	//needed
	MRDocumentSeverity documentSeverity	//needed
	//AccCostCenter costCenter
	Date estimatedStartDate
	Date estimatedDeliveryDate
	Date deliveryDate
	Boolean isIncident
	//va bene MRDamage o creiamo MRIncident??
	MRIncident incident	//needed
	String description
	Long vehicleMileage
	Double totalLabour
	Double totalParts
	Double otherCosts
	Double grandTotal


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
//            incident:MRIncident,
maintenanceRecord: MRMaintenanceRecord
	]

	static hasMany = [orderRows:MROrderRow,
					  orderPictures: MRDocumentManagement
	]

	static constraints = {

		orderType nullable:true
		prefix nullable:true
//		orderNumber nullable:true
		date nullable:true
		businessPartner nullable:true
		orderStatus nullable:true
		documentType nullable:true
		vehicle nullable:true
		user nullable:true
		location nullable:true
		affiliate nullable:true
		isInspectionRequired nullable:true
		requestType nullable:true
		documentSeverity nullable:true
		//AccCostCenter costCenter
		estimatedStartDate nullable:true
		estimatedDeliveryDate nullable:true
		deliveryDate nullable:true
		isIncident nullable:true
		//va bene MRDamage o creiamo MRIncident??
		incident nullable:true
		description nullable:true
		vehicleMileage nullable:true
		maintenanceRecord nullable:true
		totalLabour nullable:true
		totalParts nullable:true
		otherCosts nullable:true
		grandTotal nullable:true
	}

	static mapping = {
		cache true
		id generator:'sequence', params:[sequence:'mrOrder_seq']
		description type: 'text'
		date column: "order_date"
	}
}
