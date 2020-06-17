package com.dogmasystems.myrent.db

import com.dogmasystems.touroperatorportal.DocumentiContrattoNoleggio

class MRDamageInPicture implements Serializable{

    MRDocumentManagement documentManagement
	MRDamage damage
	Integer position_x
	Integer position_y

	static belongsTo = [MRDamage]
	static constraints = {

		damage nullable:true
		documentManagement nullable:true
		//position_x nullable: true
		//position_y nullable: true

	}
	static mapping = {
		cache true
		documentManagement cache: true
		damage cache: true
		//id generator: "assigned"
		id column: "id", sqlType: "int4"
		id generator:'sequence', params:[sequence:'damage_in_picture_seq']
        documentManagement column:"id_gestione_documentale",sqlType: "int4"
		damage column:"id_danno" , sqlType: "int4"/*,sqlType: "int4"*/
		version false
	}
	
}
