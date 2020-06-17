package com.dogmasystems.touroperatorportal
import com.dogmasystems.myrent.db.MRLocation;
class AuthenticationToken implements Serializable{

	String tokenValue
	String username
	String timeStamp
	String companyCode
	MRLocation location
	String role
	Integer userid

	static constraints = {
		username blank: false //, unique: true
		tokenValue blank: false
		location nullable:true
		companyCode nullable:true
		timeStamp blank: true , nullable: true
		role nullable:true
		userid nullable:true
	}
    static mapping = {
		
		////datasource 'myrent'
		table name: "authentication_token"//, schema: "public"
		id generator:'sequence', params:[sequence:'auth_token_seq']
		//id generator: "assigned"
		id column: "id", sqlType: "int4"
		userid column: "userid" , sqlType: "int4"
		version false
    }
}
