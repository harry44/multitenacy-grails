package com.dogmasystems.touroperatorportal

class DocumentiContrattoNoleggio implements Serializable{

	String description
	Boolean visualizza
	Boolean modifica
	Boolean cancella
    static constraints = {
		description(nullable: true)
		visualizza(nullable: true)
		modifica(nullable: true)
		cancella(nullable: true)
    }
	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "documenti_contratto_noleggio"//, schema: "public"
		id generator:'sequence', params:[sequence:'documenti_contratto_noleggio_seq']
		id column: "id", sqlType: "int4"
		description column: "descrizione", sqlType: "varchar(255)"
		visualizza column: "visualizza"
		modifica column: "modifica"
		cancella column: "cancella"

		version false
	}
}
