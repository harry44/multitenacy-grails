package com.dogmasystems

import com.dogmasystems.myrent.db.MRUser

import java.util.Date;

class Tenant implements Serializable{
	Long tenantId
	String companyName
	String  companyCode
	Boolean isMonthly
	Boolean isAlwaysOpenRepForDamage
	Boolean isOpenRepIfAccident
	Boolean isOpenRepIfNotRentable
    Boolean emailForRepMaintRequest

    Double openRepForAmtGreaterThan
	byte[] logo


	static auditable = true
	Date dateCreated, lastUpdated
	
	static hasMany = [users: MRUser, sshUsers: com.dogmasystems.SshUser]

    static constraints = {
		isMonthly nullable: true
        isAlwaysOpenRepForDamage nullable: true
        isOpenRepIfAccident nullable: true
        isOpenRepIfNotRentable nullable: true
        openRepForAmtGreaterThan nullable: true
        emailForRepMaintRequest nullable: true
		logo nullable: true
    }
	
	
	static mapping = {
		cache true
		table name: "tenant"//, schema: "public"
		id name : 'tenantId'
		id generator:'sequence', params:[sequence:'tenant_seq']
		companyName column:"company_name"
		companyCode column:"company_code"
		isMonthly  column: "is_monthly"
		isAlwaysOpenRepForDamage column: "is_always_open_rep_for_damage"
		isOpenRepIfAccident column: "is_open_rep_if_accident"
		isOpenRepIfNotRentable column: "is_open_rep_if_not_rentable"
		emailForRepMaintRequest column: "email_for_rep_maint_request"
		openRepForAmtGreaterThan column: "open_rep_for_amt_greater_than"


	}
}
