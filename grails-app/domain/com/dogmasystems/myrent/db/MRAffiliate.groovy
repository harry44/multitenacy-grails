package com.dogmasystems.myrent.db

import grails.util.Holders

//import com.dogmasystems.utils.db.PersistentInstance

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder


class MRAffiliate implements Serializable{

	public static final Boolean RENTER = Boolean.TRUE;
	public static final Boolean AFFILIATE = Boolean.FALSE;



	String companyName;
	String street;
	String number;
	String postalCode;
	String city;
	String province;
	String vatNumber;
	String phoneNumb1;
	String phoneNumb2;
	String cellPhoneNumb;
	String nation;
	String email;
	String annotations;
	String taxCode;
	Boolean affiliateType;
	String nomeTrasmittente;
	//private Map enumerations;
	//add these new column for exporting data to web portal
	String affiliateExportId;
	Boolean isExported;
	Boolean isUpdated;
// Added field
	Integer proKey
	String ownerCode
	String ownerDescription
	Map numerazioni
	String idCountryTrasmittente;
	String idCodiceTrasmittente;
	String emailTrasmittente;
	String telefonoTrasmittente;
	String codiceCliente;
	String passwordCodiceCliente;
	String activationCodeFE;
	Boolean invioTest;
	String pathSalvataggio;
	String taxRegime
	MRBankAccount bankAccount;
	String nomeTerzoIntermediario;
	String idPaeseTerzoIntermediario;
	String idCodiceTerzoIntermediario;
	String emailTerzoIntermediario;
	String telefonoTerzoIntermediario;

//	public MRAffiliate() {
//		getAffiliateType(AFFILIATE);
//	}

	/**
	 *constructor for dynamic query into AutoCalendar
	 */
//	public Affiliate(Integer id, Boolean affiliateType) {
//		setId(id);
//		setAffiliateType(affiliateType);
//	}


//	@Override
//	public Long getId() {
//		 return this.id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//   }

//	public static affiliate getnoleggiatore() {
//		return ((user)parameters.getuser()).getaffiliato();
//	}

	public String toString() {
		return companyName != null ? companyName : new String();
	}

	public boolean equals(Object other) {
		if ( !(other instanceof MRAffiliate) ) return false;
		MRAffiliate castOther = (MRAffiliate) other;
		return new EqualsBuilder()
				.append(this.getId(), castOther.getId())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(getId())
				.toHashCode();
	}

	static hasMany = [//rentalAgreement: MRRentalAgreement,
					  fatturaIntestaziones: MRHeaderInvoice,
					  movimentiAutos: MRVehicleMovement,
					  numerazionis: MREnumeration,
					  parcoVeicolis: MRVehicle,
					  //partites: Partite,
					  //prenotazionis: Prenotazioni,
					  //preventivis: Preventivi,
					  //primenotes: Primenote,
					  sedis: MRLocation,
					  userses: MRUser]


	//static mappedBy = [userses:'affiliate']

	static mapping = {
		cache true
		table name: "affiliati"//, schema: "public"
		id generator:'sequence', params:[sequence:'affiliati_seq']
		id column: "id", sqlType: "int4"
		companyName column: "ragione_sociale"

			annotations column: "annotazioni"
			nomeTrasmittente column: "nome_trasmittente"
			idCountryTrasmittente column: "id_paese_trasmittente"
			idCodiceTrasmittente column: "id_codice_trasmittente"
			emailTrasmittente column: "emailTrasmittente"
			telefonoTrasmittente column: "telefono_trasmittente"
			codiceCliente column: "codice_cliente"
			passwordCodiceCliente column: "password_cod_cliente"
			activationCodeFE column: "cod_attivazione_fatt_elett"
			taxRegime column: "regime_fiscale"
			pathSalvataggio column: "path_salvataggio"
			nomeTerzoIntermediario column: "nome_terzo_intermediario"
			idPaeseTerzoIntermediario column: "id_paese_terzo_intermediario"
			idCodiceTerzoIntermediario column: "id_codice_terzo_intermediario"
			emailTerzoIntermediario column: "email_terzo_intermediario"
			telefonoTerzoIntermediario column: "telefono_terzo_intermediario"

		invioTest column: "invio_test"
//		if(Holders.grailsApplication.config.com.dogmasystems.postgres==false){
//			ownerCode  column: "ownercode", sqlType: "string"
//			ownerDescription  column: "ownerdescription", sqlType: "string"
//		}else {
//			ownerCode  column: "ownercode"
//			ownerDescription  column: "ownerdescription"
//		}
		ownerCode  column: "ownercode"
		ownerDescription  column: "ownerdescription"
		street column: "via"
		number column: "numero"
		postalCode column: "cap"
		city column: "citta"
		province column: "provincia"
		vatNumber column: "partita_iva"
		phoneNumb1 column: "telefono1"
		phoneNumb2 column: "telefono2"
		cellPhoneNumb column: "cellulare"
		nation column: "nazione"
		email column: "email"
		annotations column: "annotazioni"
		taxCode column: "codice_fiscale"
		affiliateType column: "tipo_affiliato"
		affiliateExportId column: "affiliatoexportid"
		isExported column: "isexported"
		isUpdated column: "isupdated"
		bankAccount column: "id_bank_account", sqlType: "int4"
		proKey column: "prokey"
		version false
	}

	static constraints = {
		cache: true
		sedis cache: true
		userses cache: true

		proKey nullable: true
		companyName nullable: true
		street nullable: true
		number nullable: true
		postalCode nullable: true
		city nullable: true
		province nullable: true
		vatNumber nullable: true
		phoneNumb1 nullable: true
		phoneNumb2 nullable: true
		cellPhoneNumb nullable: true
		nation nullable: true
		email nullable: true
		annotations nullable: true
		taxCode nullable: true
		affiliateType nullable: true
		affiliateExportId nullable: true
		isExported nullable: true
		isUpdated nullable: true
		ownerCode nullable:true
		ownerDescription nullable: true
		idCountryTrasmittente nullable: true
		idCodiceTrasmittente nullable: true
		emailTrasmittente nullable: true
		telefonoTrasmittente nullable: true
		codiceCliente nullable: true
		passwordCodiceCliente nullable: true
		activationCodeFE nullable: true
		invioTest nullable: true
		taxRegime nullable: true
		pathSalvataggio nullable: true
		bankAccount nullable: true
		nomeTerzoIntermediario nullable: true
		idPaeseTerzoIntermediario nullable: true
		idCodiceTerzoIntermediario nullable: true
		emailTerzoIntermediario nullable: true
		telefonoTerzoIntermediario nullable: true
		nomeTrasmittente nullable: true
	}

}