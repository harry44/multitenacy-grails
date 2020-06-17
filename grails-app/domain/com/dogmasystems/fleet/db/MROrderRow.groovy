package com.dogmasystems.fleet.db

import java.util.Date;

import com.dogmasystems.myrent.db.MRVatCodes;

class MROrderRow implements Serializable{

	MRChargePolicy chargePolicy		//needed	//addebitato a noi o ad altri
	MRRepairType repairType	        //needed
	MRRepairProductType repairProductType	//needed
	String description
	String um						//Unit of measure
	Double qty						//Quantity
	Double amount
	Double percentageDiscount
	Double fixedDiscount
	Double totalAmount
	Double totalVATAmount
	Double totalAmountWithVAT
	MRVatCodes vatCode
	//AccAccount account TODO
	Integer rowIndex					//Row order for View
	MRRepMaintRequest repMaintRequest
	MROrder order						//Bidirectional mapping of Order and OrderRow

	static belongsTo = [order:MROrder]

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
		description type: "text"

		chargePolicy nullable:true
		repairType nullable:true
		repairProductType nullable:true
		um nullable:true
		qty nullable:true
		amount nullable:true
		percentageDiscount nullable:true
		fixedDiscount nullable:true
		totalAmount nullable:true
		totalVATAmount nullable:true
		totalAmountWithVAT nullable:true
		vatCode nullable:true
		repMaintRequest nullable:true
	}

	static mapping = {
		cache true
		id generator:'sequence', params:[sequence:'mrOrderRow_seq']
	}

}