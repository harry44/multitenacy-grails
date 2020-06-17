package com.dogmasystems.myrent.db
import com.dogmasystems.fleet.db.MRIncidentType
import com.dogmasystems.touroperatorportal.Autisti
import com.dogmasystems.utils.db.Loggable
import grails.util.Holders

class MRVehicleMovement implements Loggable,Serializable, Comparable<MRVehicleMovement> {

	MRAffiliate affiliate;
	MRMovementCausal causal;
	MRVehicle vehicle;
	MRIncidentType incident
	MRRentalAgreement rentalAgreement;
	MRVehicleMovement plannedMovement
	MRReservation reservation;
	MREnumeration enumeration;
	Integer year;
	/** date **/
	Date data;
	String prefix;
	Integer number;
    String signature1
	String signature2
	MRLocation pickupLocation;
	/** timestamp **/
	Date startDate;
	Integer startKm;
	Integer startFuel;

	MRLocation returnLocation;
	/** timestamp **/
	Date endDate;
	Integer endKm;
	Integer endFuel
	String note;
	Autisti autisti;

	MRUser userOpening;
	/** timestamp **/
	Date dateOpening;
    
	MRUser userClosing;
	/** timestamp **/
	Date dateClosing;
    MRBusinessPartner clienti;
	Double amount;
	Boolean isLast;
	Boolean isClosed;
	Boolean isNotValid;
	Boolean isAccident
	Boolean isDirtyCheckout
	Boolean isNotRentable
	Boolean isClosedByApp
	//Madhvendra
	Double damagesAmount;
	Double qtyLiter;
	String authCod;
	Boolean isDamages;
	Boolean isDamageCharged;
	Boolean isTheft;
	Boolean isTheftCharged;

	Boolean damagesAcceptedByCustomer;
	Boolean customerRefusesCheckIn;
	Boolean keyLeftInKeyBox;

	static auditable = true

	public MRVehicleMovement() {
		setIsClosed(Boolean.FALSE);
		setIsNotValid(Boolean.FALSE);
		setIsLast(Boolean.TRUE);
	}

	@Override
	public Integer getEntityId() {
		return this.entityId;
	}
	
	public MRVehicleMovement(
		MRVehicleMovement otherMovement
	) {
	this.affiliate = otherMovement.affiliate
	this.causal = otherMovement.causal
	this.vehicle = otherMovement.vehicle
	this.rentalAgreement = otherMovement.rentalAgreement
	this.enumeration = otherMovement.enumeration
	this.year = otherMovement.year
	this.data = otherMovement.data
	this.prefix = otherMovement.prefix
	this.number = otherMovement.number
	this.signature1 = otherMovement.signature1
	this.signature2 = otherMovement.signature2
	this.pickupLocation = otherMovement.pickupLocation
	this.startDate = otherMovement.startDate
	this.startKm = otherMovement.startKm
	this.startFuel = otherMovement.startFuel
	this.returnLocation = otherMovement.returnLocation
	this.endDate = otherMovement.endDate
	this.endKm = otherMovement.endKm
	this.endFuel = otherMovement.endFuel
	this.autisti = otherMovement.autisti
	this.userOpening = otherMovement.userOpening
	this.dateOpening = otherMovement.dateOpening
	this.userClosing = otherMovement.userClosing
	this.dateClosing = otherMovement.dateClosing
	this.clienti = otherMovement.clienti
	this.isLast = otherMovement.isLast
	this.isClosed = otherMovement.isClosed
	this.isNotValid = otherMovement.isNotValid
	this.isAccident = otherMovement.isAccident
	this.isNotRentable = otherMovement.isNotRentable
	this.isDirtyCheckout = otherMovement.isDirtyCheckout
	this.incident = otherMovement.incident
}
	
	public MRVehicleMovement(MRVehicle veicolo, MRLocation sedeUscita, MRLocation sedeRientro, Date inizio, Date fine, Boolean annullato) {
		setVeicolo(veicolo);
		setSedeUscita(sedeUscita);
		setSedeRientro(sedeRientro);
		setInizio(inizio);
		setFine(fine);
	}

	public String toString() {
		return getVehicle().toString();
	}

	public String[] getLoggableFields() {
		return [
				"importo", // NOI18N
				"sedeRientro", // NOI18N
				"fine", // NOI18N
				"kmFine", // NOI18N
				"combustibileFine", // NOI18N
				"chiuso" // NOI18N
		] as String[];
	}

	public String[] getLoggableLabels() {
		return [
//					bundle.getString("MovimentoAuto.logImporto"),
//					bundle.getString("MovimentoAuto.logSedeRientro"),
//					bundle.getString("MovimentoAuto.logFine"),
//					bundle.getString("MovimentoAuto.logKmFine"),
//					bundle.getString("MovimentoAuto.logCombustibileFine"),
//					bundle.getString("MovimentoAuto.logChiuso")
		] as String[];
	}

	public String getEntityName() {
		return "MovimentoAuto"; // NOI18N
	}
	public int compareTo(MRVehicleMovement o) {
		return getStartDate().compareTo(o.getStartDate());
	}


	static hasMany = [
		dannimovimentis: MRDamageMovement
		//damages : MRDamage
					  //multes: Multe]
	]
	static belongsTo = [MRAffiliate, MRMovementCausal,MRReservation]


	////

	// TODO you have multiple hasMany references for class(es) [Danni]
	//      so you'll need to disambiguate them with the 'mappedBy' property:
	//static mappedBy = [damages: "movements"]

	static mapping = {
//		cache true

		table name: "movimenti_auto"//, schema: "public"
		id generator:'sequence', params:[sequence:'movimenti_auto_seq']
		id column: "id", sqlType: "int4"
		data column: "data", sqlType:"date"
		signature1 type:"text"
		signature2 type:"text"
		startDate column: "inizio",sqlType:"date"
		endDate column: "fine", sqlType:"date"
		incident column:"mr_incident_type",sqlType:"int4"
		pickupLocation column:"id_sede_uscita", sqlType:"int4"
		year column: "anno"
		prefix column: "prefisso"
		affiliate column:"id_affiliato", sqlType:"int4"
		userOpening column:"id_user_apertura", sqlType:"int4"
		causal column:"id_causale", sqlType:"int4"
		vehicle column:"id_veicolo", sqlType:"int4"
		rentalAgreement column:"id_contratto", sqlType:"int4"
		enumeration column:"id_numerazione", sqlType:"int4"
		//autisti column:"autisti_id", sqlType:"int4"
		userClosing column:"id_user_chiusura", sqlType:"int4"
		returnLocation column:"id_sede_rientro", sqlType:"int4"
		isNotValid column:"annullato"
		isClosed column:"chiuso"
		//fornitore column:"id_fornitore",  sqlType:"int4"
		number column:"numero"
		dateOpening column: "data_apertura", sqlType:"date"
		dateClosing column: "data_chiusura", sqlType:"date"
		isLast column: "ultimo"
        autisti column:"id_autista", sqlType:"int4"
		clienti column:"id_fornitore", sqlType:"int4"
		endKm column: "km_fine"
		endFuel column:"combustibile_fine"
		startKm column: "km_inizio"
		startFuel column:"combustibile_inizio"
		isClosedByApp column:"is_closed_by_app"
		reservation  column: "id_prenotazione", sqlType:"int4"
		amount column:"importo"
		qtyLiter column:"qty_liter"
		damagesAmount column:"damages_amount"
		authCod column: "auth_cod"
		isDamages column: "is_damages"
		isDamageCharged column: "is_damage_charged"
		isTheft column: "is_theft"
		isTheftCharged column: "is_theft_charged"
		isDirtyCheckout column:"dirty_checkout"
		isAccident column :"is_accident"

		damagesAcceptedByCustomer column: "damages_accepted_by_customer"
		customerRefusesCheckIn column:"customer_refuses_check_in"
		keyLeftInKeyBox column :"key_left_in_key_box"
		note column :"note"
		plannedMovement column: "id_planned_mov"
		version false
	}

	static constraints = {

		//	data nullable: true
		startDate nullable: true
		endDate nullable: true
		pickupLocation nullable: true
		year nullable: true
		prefix nullable: true
		affiliate nullable: true
		userOpening nullable: true
		causal nullable: true
		vehicle nullable: true
		rentalAgreement nullable: true
		endKm nullable:true
		endFuel nullable:true
		startKm nullable:true
		startFuel nullable:true
		enumeration nullable: true
		userClosing nullable: true
		returnLocation nullable: true
		isNotValid nullable: true
		isClosed nullable: true
		clienti nullable:true
		number nullable: true
		dateOpening nullable: true
		dateClosing nullable: true
		isLast nullable: true
		autisti nullable:true
		isAccident nullable:true
		isNotRentable nullable:true
		signature1 nullable:true
		signature2 nullable:true
		isClosedByApp nullable:true
		incident nullable:true
		note nullable:true
		isDirtyCheckout nullable:true
		amount nullable:true
		reservation nullable:true
		isTheftCharged nullable: true
		isTheft nullable: true
		isDamageCharged nullable: true
		isDamages nullable: true
		authCod nullable: true
		qtyLiter nullable: true
		damagesAmount nullable: true
		damagesAcceptedByCustomer nullable: true
		customerRefusesCheckIn nullable: true
		keyLeftInKeyBox nullable: true
		plannedMovement nullable: true
	}


}
