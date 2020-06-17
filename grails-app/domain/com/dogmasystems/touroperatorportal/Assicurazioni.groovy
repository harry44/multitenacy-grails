package com.dogmasystems.touroperatorportal
import com.dogmasystems.myrent.db.MRVehicle
import com.dogmasystems.myrent.db.MRBusinessPartner
class Assicurazioni implements Serializable{

	String numeroPolizza
	Integer numeroRate
	Double importoAnnuale
	Double importoRca
	Double importoFurto
	Double importoIncendio
	Double importoKasco
	Double importoServSan
	Double importoAltro
	Double massimaleAnnuale
	Double massimaleRca
	Double massimaleFurto
	Double massimaleIncendio
	Double massimaleKasco
	Double massimaleServSan
	Double massimaleAltro
	Double importoScadenza1
	Double importoScadenza2
	Double importoScadenza3
	Double importoScadenza4
	Date dataScadenza1
	Date dataScadenza2
	Date dataScadenza3
	Date dataScadenza4
	Boolean pagato1
	Boolean pagato2
	Boolean pagato3
	Boolean pagato4
	Date dataProssScad
	Double impProssScad
	Date dataInizio
	Double tasse
	String annotazioni
	String classeMeritoAttuale
	MRVehicle parcoVeicoli
	MRBusinessPartner clienti

	static belongsTo = [MRBusinessPartner, MRVehicle]

	static mapping = {
		cache true
		////datasource 'myrent'
		table name: "assicurazioni"//, schema: "public"
		id generator: 'sequence', params: [sequence: 'assicurazioni_seq']
		id column: "id", sqlType: "int4"
		clienti column: 'id_compagnia', sqlType: "int4"
		parcoVeicoli column: 'id_veicolo', sqlType: "int4"
		impProssScad sqlType: "float"
		version false
	}

	static constraints = {
		numeroPolizza nullable: true
		numeroRate nullable: true
		importoAnnuale nullable: true, scale: 17
		importoRca nullable: true, scale: 17
		importoFurto nullable: true, scale: 17
		importoIncendio nullable: true, scale: 17
		importoKasco nullable: true, scale: 17
		importoServSan nullable: true, scale: 17
		importoAltro nullable: true, scale: 17
		massimaleAnnuale nullable: true, scale: 17
		massimaleRca nullable: true, scale: 17
		massimaleFurto nullable: true, scale: 17
		massimaleIncendio nullable: true, scale: 17
		massimaleKasco nullable: true, scale: 17
		massimaleServSan nullable: true, scale: 17
		massimaleAltro nullable: true, scale: 17
		importoScadenza1 nullable: true, scale: 17
		importoScadenza2 nullable: true, scale: 17
		importoScadenza3 nullable: true, scale: 17
		importoScadenza4 nullable: true, scale: 17
		dataScadenza1 nullable: true
		dataScadenza2 nullable: true
		dataScadenza3 nullable: true
		dataScadenza4 nullable: true
		pagato1 nullable: true
		pagato2 nullable: true
		pagato3 nullable: true
		pagato4 nullable: true
		dataProssScad nullable: true
		impProssScad nullable: true, scale: 17
		dataInizio nullable: true
		tasse nullable: true, scale: 17
		annotazioni nullable: true
		classeMeritoAttuale nullable: true
	}
}
