package com.dogmasystems.myrent.db

import com.dogmasystems.touroperatorportal.Documenti
import com.dogmasystems.touroperatorportal.Pagamenti
import com.dogmasystems.touroperatorportal.Comuni
import grails.util.Holders

class MRBusinessPartner implements Serializable {

	Integer idExt;
	Boolean isClient;
	Boolean isSupplier;
	Boolean isDriver;
	/** Dati anagrafici **/
	Boolean isPhysicalPerson;
	Boolean isIndividualCompany;
	Boolean isCompleted;
	String companyName;
	String surname;
	String name;
	/** Indirizzo **/
	String street;
	String number;
	String postalCode;
	String city;
	String province;
	String national;
	/** Contatti **/
	String phoneNumb1;
	String phoneNumb2;
	String cellphoneNumber;
	String email;
	/** Dati fiscali **/
	String vatNumber;
	String taxCode;
	/** Dati aggiuntivi pers. fisica **/
	Date birthDate;
	String birthPlace;
	String birthProv;
	String birthNation;
	Boolean gender;
	/** Dati contabili **/
	Pagamenti pagamenti
	//Set coordinateBancarie;
	String blackList;
	String eInvoiceCode
	String eInvoiceEmail
	/** Note **/
	String reminder;
	//add these new column for exporting data to web portal
	String customerExportId;
	String supplierExportId;
	Boolean isUpdated;

	//From Client Domain
	Boolean groupsEffect
	String barcode
	Double discount
	Boolean withholdingTax
	String document
	String documentNumb
	String issueBy
	Date releaseDate
	Date expiryDate
	String drivingLicense
	MRChartAccount chartAccount
	//MRReservationSource reservationSource
	MRReservationSource reservationSource   //Changed to hasMany
	Documenti documenti// added by sargam
	Double commission
	//Comuni comuni // added by sargam
// Added field
	Date dataInformativa
	Boolean informativaFirmata
	byte[] foto
	Boolean isFotoSet
	Integer proKey
	Integer custId, driverId
	String codiceNSA
	String typeOfCustomer
	String documentNumb1
	String licenseType
	String issueBy1
	Date releaseDate1
	Date expiryDate1
	String document1
	MRBusinessPartner clienteFattura
	MRBusinessPartner agent



	static auditable = true

	String toString() {
		def fullname = ""
		fullname += companyName != null ? companyName : ""
		fullname += surname != null ? fullname != "" ? " - " + surname : surname : ""
		fullname += name != null ? fullname != "" ? " - " + name : name : ""
		fullname +=taxCode !=null? fullname !=""?  "/"+taxCode:taxCode:""
		return fullname
	}

	//static belongsTo = [MRReservationSource]
	static hasMany = [//assicurazionis: Assicurazioni,
					 // clientis                    : MRBusinessPartner,      //Class changed to MRBusinessPartner as MRClient is deleted
					  contrattoNoleggiosForIdCliente: MRRentalAgreement,
					  contrattoNoleggiosForIdCond1: MRRentalAgreement,
					  contrattoNoleggiosForIdCond2: MRRentalAgreement,
					  contrattoNoleggiosForIdCond3: MRRentalAgreement,
					  //coordinateBancariesForIdCliente: CoordinateBancarie,
					  //coordinateBancariesForIdFornitore: CoordinateBancarie,
					  fatturaIntestaziones        : MRHeaderInvoice,
					  reservationClienti:MRReservationSource,
					  //garanzies: Garanzie,
					  movimentiAutos              : MRVehicleMovement,
					  contrattoNoleggiosForInvIdCond1: MRRentalAgreement,
					  splitPayments : MRSplitPayment,
					  location:MRLocation
	]


	// TODO you have multiple hasMany references for class(es) [RighePrimanota, Prenotazioni, CoordinateBancarie, ContrattoNoleggio, Scadenze, Preventivi]
	//      so you'll need to disambiguate them with the 'mappedBy' property:
	static mappedBy = [coordinateBancariesForIdCliente: 'clientiByIdCliente',
					   coordinateBancariesForIdFornitore: 'clientiByIdFornitore',
					   prenotazionisForIdCliente      : "clientiByIdCliente",
					   prenotazionisForIdCond1        : "clientiByIdCond1",
					   prenotazionisForIdCond2        : "clientiByIdCond2",
					   prenotazionisForIdCond3        : "clientiByIdCond3",
					   preventivisForIdCliente        : "clientiByIdCliente",
					   preventivisForIdCond1          : "clientiByIdCond1",
					   preventivisForIdCond2          : "clientiByIdCond2",
					   preventivisForIdCond3          : "clientiByIdCond3",
					   righePrimanotasForIdCliente    : "clientiByIdCliente",
					   righePrimanotasForIdFornitore  : "clientiByIdFornitore",
					   scadenzesForIdCliente          : "clientiByIdCliente",
					   scadenzesForIdFornitore        : "clientiByIdFornitore",
					   contrattoNoleggiosForIdCliente : "clientiByIdCliente",
					   contrattoNoleggiosForIdCond1   : "clientiByIdCond1",
					   contrattoNoleggiosForIdCond2   : "clientiByIdCond2",
					   contrattoNoleggiosForIdCond3   : "clientiByIdCond3",
					   contrattoNoleggiosForInvIdCond1: "clientiByInv"
	]


	static mapping = {
		cache true
		sort("id")
		table name: 'clienti'//, schema: 'public'
		id generator: 'sequence', params: [sequence: 'clienti_seq']
		id column: "id", sqlType: "int4"
		isClient column: "cliente"
		idExt column: "id_ext"
		isSupplier column: "fornitore"
		isDriver column: "conducente"
		isPhysicalPerson column: "pers_fisica"
		isIndividualCompany column: "ditta_ind"
		isCompleted column: "completo"
		splitPayments cascade: 'all-delete-orphan'
		typeOfCustomer column: "type_of_customer"
		street column: "via"
		number column: "numero"
		national column: "nazione"
		postalCode column: "cap"
		city column: "citta"
		province column: "provincia"
		phoneNumb1 column: "telefono1"
		phoneNumb2 column: "telefono2"
		cellphoneNumber column: "cellulare"
		email column: "email"
		vatNumber column: "partita_iva"
		taxCode column: "codice_fiscale"
		birthDate column: "data_nascita", sqlType: "date"
		birthPlace column: "luogo_nascita"
		eInvoiceCode column: "e_invoice_code"
		eInvoiceEmail column: "e_invoice_email"
		birthProv column: "prov_nascita"
		reminder column: "promemoria"
		companyName column: "ragione_sociale"
		name column: "nome"
		surname column: "cognome"
		blackList column: "lista_nera"
		gender column: "sesso"
		birthNation column: "nazione_nascita"

		customerExportId column: "cexportid"
		isUpdated column: "isupdated"
		supplierExportId column: "sexportid"

		//From Client Domain
		releaseDate column: "data_rilascio", sqlType: "date"
		expiryDate column: "data_scadenza", sqlType: "date"
		groupsEffect column: "raggruppa_effetti"
		barcode column: "barcode"
		discount column: "discount"
		withholdingTax column: "withholdingTax"
		document column: "documento"

		documentNumb column: "numero_documento"
		issueBy column: "rilasciato_da"
		releaseDate column: "data_rilascio", sqlType: "date"
		expiryDate column: "data_scadenza", sqlType: "date"
		drivingLicense column: "categoria_patente"


		pagamenti column: "id_pagamento", sqlType: "int4"
		chartAccount column: "codicesottoconto", sqlType: "int4"
	    reservationSource column: "id_fonte_commissione", sqlType: "int4"
		//reservationSource1 column:"id_fonte_commissione", sqlType: "int4"
		documenti column: "documenti" //added by sargam
		//comuni column: "comuni"

		dataInformativa column: "data_informativa" , sqlType: "date"
		informativaFirmata column: "informativa_firmata"
		foto column: "foto"
		isFotoSet column: "is_foto_set"
		custId column: "custId"
		driverId column: "driverId"
		proKey column: "prokey"

		reservationClienti joinTable: [name: "clienti_reservation_source", key: "id_cliente_res" , column: "id_cliente_reservation"]
		document1 column: "document1"
		clienteFattura column: "id_cliente_fattura",sqlType: "int4"
		agent column:"agent_id",sqlType: "int4"
//		location column:"location_id",sqlType: "int4"
//		location cascade: 'all-delete-orphan'
		version false
	}

	static constraints = {
		idExt nullable: true
		isClient nullable: true
		isSupplier nullable: true
		isDriver nullable: true
		isPhysicalPerson nullable: true
		isIndividualCompany nullable: true
		chartAccount nullable: true
		pagamenti nullable: true
		isCompleted nullable: true
		companyName nullable: true
		surname nullable: true
		name nullable: true
		street nullable: true
		number nullable: true
		national nullable: true
		postalCode nullable: true
		city nullable: true
		province nullable: true
		phoneNumb1 nullable: true
		phoneNumb2 nullable: true
		cellphoneNumber nullable: true
		email nullable: true, email: true
		vatNumber nullable: true
		taxCode nullable: true
		birthDate nullable: true
		birthPlace nullable: true
		birthProv nullable: true
		gender nullable: true
		reminder nullable: true
		birthNation nullable: true
		blackList nullable: true
		//From Client Domain
		birthNation nullable: true
		blackList nullable: true
		groupsEffect nullable: true
		barcode nullable: true
		discount nullable: true, scale: 17
		withholdingTax nullable: true
		document nullable: true
		documentNumb nullable: true
		issueBy nullable: true
		releaseDate nullable: true
		expiryDate nullable: true
		drivingLicense nullable: true
		customerExportId nullable: true
		supplierExportId nullable: true
		isUpdated nullable: true
		reservationSource nullable: true
		commission nullable: true
		documenti nullable: true
		//comuni nullable: true // added by sargam
		dataInformativa nullable: true
		informativaFirmata nullable: true
		foto nullable: true
		isFotoSet nullable: true
		proKey nullable: true
		custId nullable: true
		driverId nullable: true
		codiceNSA nullable: true
		typeOfCustomer nullable: true
		eInvoiceCode nullable: true
		eInvoiceEmail nullable: true
		documentNumb1 nullable:true
		licenseType nullable: true
		issueBy1 nullable: true
		releaseDate1 nullable: true
		expiryDate1 nullable: true
		document1 nullable: true
		clienteFattura nullable: true
		agent nullable:true
//		location nullable:true
	}
}