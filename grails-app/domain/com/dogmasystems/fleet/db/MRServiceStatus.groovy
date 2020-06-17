package com.dogmasystems.fleet.db

class MRServiceStatus implements Serializable{
long id
String description
boolean isOut
	static mapping={
		cache true
		id generator:'sequence', params:[sequence:'mrServiceStatus_seq']
		id column: "id"//, sqlType: "int4"
	}
    static constraints = {
    }
}
