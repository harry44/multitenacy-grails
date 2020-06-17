package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRUser

class Sessioni implements Serializable{

	String sessionId
	String remoteIp
	String systemUser
	Date startTime
	Date endTime
	MRUser users

	static belongsTo = [MRUser]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "sessioni"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		startTime sqlType:"date"
		endTime sqlType:"date"
		users column:"id_user", sqlType:"int4"
		version false
	}

	static constraints = {
		sessionId nullable: true
		remoteIp nullable: true
		systemUser nullable: true
		startTime nullable: true
		endTime nullable: true
	}
}
