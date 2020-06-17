package com.dogmasystems.myrent.db

import grails.util.Holders

//import com.dogmasystems.utils.BundleUtils

import java.text.DecimalFormat

import org.apache.commons.lang.builder.EqualsBuilder

class MREnumeration implements Serializable{

	/** Creates a new instance of Numerazione */
	public MREnumeration() {
	}

	protected MREnumeration(String documento) {
		setDocumento(documento);
	}
	String tipo
	String type;
	String vatRecordCode;
	String documentTypeCode;
	String document;
	String prefix;
	String description;

	MRVatRecord registroIva
//	MRVatRecord vatRecord;
	MRAffiliate affiliate;
	MRLocation location;
	//Set<MRProgressive> progressive;
	/**
	 * Tipi di documento per le numerazioni
	 */
	public static final String VENDITE = "Vendite"; //NOI18N
	public static final String NOTE_CREDITO_VENDITA = "Note credito vendita"; //NOI18N
	public static final String ACQUISTI = "Acquisti"; //NOI18N
	public static final String CORRISPETTIVI = "Corrispettivi"; //NOI18N
	public static final String VENDITE_MARGINE = "Vendite sist. margine"; //NOI18N
	public static final String ACQUISTI_MARGINE = "Acquisti sist. margine"; //NOI18N
	public static final String VENDITE_INTRASTAT = "Vendite da acquisti intrastat"; //NOI18N
	public static final String CONTRATTI = "Contratti"; //NOI18N
	public static final String PRENOTAZIONI = "Prenotazioni"; //NOI18N
	public static final String PREVENTIVI = "Preventivi"; //NOI18N
	public static final String PRIMENOTE = "Primenote"; //NOI18N
	public static final String CLIENTI = "Clienti"; //NOI18N
	public static final String PARTITE = "Partite"; //NOI18N


	public String getDocumentoI18N() {
		return getDocumentoI18N(getDocument());
	}


	public boolean equals(Object other) {
		if (other != null && other instanceof MREnumeration) {
			return new EqualsBuilder().append(getId(), ((MREnumeration) other).getId()).isEquals();
		}
		return false;
	}

	public static String format(String prefisso, Integer numero) {
		if (numero != null) {
			if (prefisso != null) {
				return prefisso + " " + new DecimalFormat("#0.#").format(numero); //NOI18N
			} else {
				return new DecimalFormat("#0.#").format(numero); //NOI18N
			}
		}
		//return bundle.getString("NumerazioniUtils.msgSN");
	}

	public String toString() {
//		if (getDocument() != null) {
//			if (getPrefisso() != null) {
//				return getPrefisso() + ", " + getDocumentoI18N(); //NOI18N
//			} else {
//				return getDocumentoI18N();
//			}
//		}
//		return new String();
		return description;
	}

	static hasMany = [rentalAgreements: MRRentalAgreement,
					  headerInvoices: MRHeaderInvoice,
					  vehicleMovements: MRVehicleMovement,
					  //  partites: Partite,
//	                  prenotazionis: Prenotazioni,
//	                  preventivis: Preventivi,
//	                  primenotesForIdNumerazione: Primenote,
//	                  primenotesForIdSezionale: Primenote,
					  progressives: MRProgressive]
	static belongsTo = [MRAffiliate, MRVatRecord, MRLocation]

	// TODO you have multiple hasMany references for class(es) [Primenote] 
	//      so you'll need to disambiguate them with the 'mappedBy' property:
	static mappedBy = [primenotesForIdNumerazione: "numerazioniByIdNumerazione",
					   primenotesForIdSezionale: "numerazioniByIdSezionale"]

	static mapping = {
		table name:"numerazioni"//, schema:"public"
		id generator:'sequence', params:[sequence:'numerazioni_seq']
		discriminator column: "tipo", type: "varchar"
		id column: "id", sqlType: "int4"
		vatRecordCode column:"codice_registro_iva"
		documentTypeCode column:"codice_tipo_documento"
		prefix column:"prefisso"
		description column:"descrizione"
		affiliate column:  "id_affiliato" , sqlType: "int4"
		location column: "id_sede" ,  sqlType: "int4"
		document column:"documento"

		registroIva column: "id_registro_iva" , sqlType: "int4"
		version false
	}

	static constraints = {
		document nullable: true
		prefix nullable: true
		vatRecordCode nullable: true
		documentTypeCode nullable: true
		type nullable: true
		description nullable: true
		registroIva nullable: true
		affiliate nullable: true
		location nullable: true
//		vatRecord nullable: true
	}
}
