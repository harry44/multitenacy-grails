package com.dogmasystems.myrent.db


class MRSourceTarget implements  Serializable{

	String description;
	Boolean isTourOperator;
	Boolean isWeb;
	Boolean isCorporate;
	Boolean isWalkIn;

	static mapping = {
		cache true
		table name: "fonte_target"//, schema: "public"
		id generator:'sequence', params:[sequence:'fonte_target_seq']
		id column: "id", sqlType: "int4"
		isTourOperator column: "istouroperator"
		isWeb column: "isweb"
		isCorporate column: "iscorporate"
		isWalkIn column: "iswalkin"
		description column: "description"

		version false
	}

	static constraints = {
	}

}
