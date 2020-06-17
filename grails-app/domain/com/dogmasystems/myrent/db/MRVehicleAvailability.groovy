package com.dogmasystems.myrent.db

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class MRVehicleAvailability implements Serializable {

	//Integer idVeicolo
	Date startDate
	Date endDate
	Boolean parkEntry
	Boolean parkExit
	MRVehicle vehicle
	MRLocation location

	
	public MRVehicleAvailability(MRVehicle vehicle, Date start, Date end, MRLocation location, Boolean pEntry, Boolean pExit){
		this.vehicle = vehicle
		this.startDate = start
		this.endDate = end
		this.location = location
		this.parkEntry = pEntry
		this.parkExit = pExit
	}
	
	
	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append vehicle
		builder.append startDate
		builder.append endDate
		builder.append location
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append vehicle, other.parcoVeicoli
		builder.append startDate, other.inizio
		builder.append endDate, other.fine
		builder.append location, other.sedi
		builder.isEquals()
	}

	static belongsTo = [MRVehicle, MRLocation]

	static mapping = {
		cache true
		table name: "disponibilita_veicoli"//, schema: "public"
		id composite: ["vehicle", "startDate", "endDate", "location"]
		startDate column: "inizio", sqlType:"timestamp"
		endDate column: "fine", sqlType:"timestamp"
		vehicle column:"id_veicolo", /*sqlType:"int4",*/ insert:"false", update:"false", sqlType:"int4"
		location column:"id_sede", sqlType:"int4"
		parkEntry column :"ingresso_parco"
		parkExit column: "uscita_parco"
		//parcoVeicoli column:"id_veicolo", sqlType:"int4", insert:"false", update:"false"
		version false
	}

	static constraints = {
		parkEntry nullable: true
		parkExit nullable: true
	}
}
