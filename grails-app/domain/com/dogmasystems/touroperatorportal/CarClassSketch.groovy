package com.dogmasystems.touroperatorportal
import com.dogmasystems.myrent.db.MRGroup
import com.dogmasystems.myrent.db.MRDocumentManagement
class CarClassSketch implements Serializable{

	MRGroup groupId
	MRDocumentManagement gestioneDocumentale
    static constraints = {
    }
	static belongsTo=[MRDocumentManagement]
	static mapping = {
		
		////datasource 'myrent'
		table name: "car_class_sketch"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		gestioneDocumentale column:"id_gestione_documentale" , sqlType: "int4"
		version false
	}
}
