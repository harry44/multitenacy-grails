package com.dogmasystems
import com.dogmasystems.myrent.db.MRLocation
import com.dogmasystems.myrent.db.MRReservationSource
import com.dogmasystems.myrent.db.MRReservation

class User implements Serializable{

	transient springSecurityService

	String username
	String password
    String email
    MRLocation location
	Integer myrentUserId
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	
	static auditable = true
	Date dateCreated, lastUpdated
	SshUser sshUser

	static belongsTo = [tenant:Tenant]
	static hasMany = [dswpUsersFonti:MRReservationSource, reservationId : MRReservation ]

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: 'tenant'
		password blank: false
		sshUser nullable: true
        location nullable: true
		reservationId nullable: true
		myrentUserId nullable: true

        email email: true, nullable: true, unique: true
		//myrentUserId nullable: true
	}

	static mapping = {
		cache true
		tenant cache: true, lazy: false
		table("dswp_users")
		password column: '`password`'
		id generator:'sequence', params:[sequence:'user_seq']
		//reservationId column: "mrreservation_id",sqlType: "int4"
		//myrentUserId sqlType: "int4"
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		
		if (isDirty('password')) {
			println("Password changed")
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
