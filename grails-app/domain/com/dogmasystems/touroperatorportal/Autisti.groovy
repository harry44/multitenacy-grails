package com.dogmasystems.touroperatorportal
import com.dogmasystems.myrent.db.MRVehicleMovement
class Autisti implements Serializable{

	String cognome
	String nome
	String via
	String numero
	String nazione
	String cap
	String citta
	String provincia
	String telefono
	String luogoNascita
	String provinciaNascita
	String nazioneNascita
	Date dataNascita
	String documento
	String numeroDocumento
	String rilasciatoDa
	Date dataRilascio
	Date dataScadenza
	String annotazioni
	byte[] foto
	Boolean isFotoSet
	String categoriaPatente
	String codiceFiscale

	static hasMany = [movimentiAutos: MRVehicleMovement]

	static mapping = {
		cache true
		////datasource 'myrent'
		table name: "autisti"//, schema: "public"
		id generator: 'sequence', params: [sequence: 'autisti_seq']
		id column: "id", sqlType: "int4"
		version false
	}

	static constraints = {
		cognome nullable: true
		nome nullable: true
		via nullable: true
		numero nullable: true
		nazione nullable: true
		cap nullable: true
		citta nullable: true
		provincia nullable: true
		telefono nullable: true
		luogoNascita nullable: true
		provinciaNascita nullable: true
		nazioneNascita nullable: true
		dataNascita nullable: true
		documento nullable: true
		numeroDocumento nullable: true
		rilasciatoDa nullable: true
		dataRilascio nullable: true
		dataScadenza nullable: true
		annotazioni nullable: true
		foto nullable: true
		isFotoSet nullable: true
		categoriaPatente nullable: true
		codiceFiscale nullable: true
	}
	public String toString() {
		String fullName=""
		fullName = getCognome()+" "+ getNome()
		return fullName
	}
}
