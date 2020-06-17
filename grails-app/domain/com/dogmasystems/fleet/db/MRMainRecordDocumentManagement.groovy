package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRDocumentManagement;

class MRMainRecordDocumentManagement implements Serializable{
	MRDocumentManagement documentManagement
	MRMaintenanceRecord maintenanceRecord
	static belongsTo = [MRMaintenanceRecord]

	static constraints = {

		documentManagement nullable:true
	}
	static mapping = {
		cache true
		table name: "mainrecorddocmanag"//, schema: "public"
		id generator: 'sequence', params: [sequence: 'mainrecorddocmanag_seq']
		id column: "id"//, sqlType: "int4"
	}
}
