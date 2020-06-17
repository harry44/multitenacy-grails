package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRAffiliate;
import com.dogmasystems.myrent.db.MREnumeration;
import com.dogmasystems.myrent.db.MRHeaderInvoice;
import com.dogmasystems.myrent.db.MRBusinessPartner;
import com.dogmasystems.myrent.db.MRRentalAgreement;

class Partite implements Serializable{

	Integer numero
	Date data
	Double importo
	Double saldo
	Boolean chiusa
	MRAffiliate affiliati
	Partite partite
	Primenote primenote
	MRBusinessPartner clienti
	MRRentalAgreement contrattoNoleggio
	MREnumeration numerazioni
	MRHeaderInvoice fatturaIntestazione

	static hasMany = [partites: Partite,
	                  primenotes: Primenote]
	static belongsTo = [MRAffiliate, MRBusinessPartner, MRRentalAgreement, MRHeaderInvoice, MREnumeration, Primenote]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "partite"//, schema: "public"
		id generator: "assigned"
		id column: "id", sqlType: "int4"
		data sqlType: "date"
		affiliati column:"id_affiliato", sqlType: "int4"
		partite column:"id_partita_saldo", sqlType: "int4"
		primenote column:"id_primanota", sqlType: "int4"
		clienti column:"id_cliente", sqlType: "int4"
		contrattoNoleggio column:"id_contratto", sqlType: "int4"
		numerazioni column:"id_numerazione", sqlType: "int4"
		fatturaIntestazione column:"id_fattura", sqlType: "int4"
		version false
	}

	static constraints = {
		numero nullable: true
		data nullable: true
		importo nullable: true, scale: 17
		saldo nullable: true, scale: 17
		chiusa nullable: true
	}
}
