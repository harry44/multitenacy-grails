package com.dogmasystems.touroperatorportal

class Settings implements Serializable{
	String key
	String value
	static mapping = {
		//datasource 'myrent'
		cache true
		table name: "settings"//, schema: "public"
		id name: "key", generator: "assigned"
		//id generator: 'sequence', params:[sequence:'settings_seq']

			value column:"value",sqlType:"text"

		version false
	}

	static constraints = {
		value nullable: true

	}
}
