package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRReservation
import com.dogmasystems.myrent.db.MRReservationSource
import com.dogmasystems.myrent.db.MRRentalAgreement
import it.myrent.ee.db.MROldPreventivo
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder;

class Commissioni implements Serializable{

	Boolean prepagato
	String codiceVoucher
	Integer giorniVoucher
	MRReservationSource fontiCommissioni
	Preventivi preventivi
	MRRentalAgreement contrattoNoleggio
	MRReservation reservation

	Commissioni(Boolean prepagato, String codiceVoucher, Integer giorniVoucher, MRReservationSource fontiCommissioni, Preventivi preventivi, MRRentalAgreement contrattoNoleggio, MRReservation reservation) {
		this.prepagato = prepagato
		this.codiceVoucher = codiceVoucher
		this.giorniVoucher = giorniVoucher
		this.fontiCommissioni = fontiCommissioni
		this.preventivi = preventivi
		this.contrattoNoleggio = contrattoNoleggio
		this.reservation = reservation
	}

	static belongsTo = [MRRentalAgreement,MRReservation, Preventivi]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "commissioni"//, schema: "public"
		id generator:'sequence', params:[sequence:'commissioni_seq']
		id column: "id", sqlType: "int4"
		fontiCommissioni column: "id_fonte_commissione", sqlType: "int4"
		preventivi column: "id_preventivo", sqlType: "int4"
		contrattoNoleggio column: "id_contratto", sqlType: "int4"
		//prenotazioni column: "id_prenotazione"//, sqlType: "int4"
		reservation column: "id_prenotazione" , sqlType: "int4"

		version false
	}

	static constraints = {
		prepagato nullable: true
		codiceVoucher nullable: true
		giorniVoucher nullable: true
		reservation nullable: true
		preventivi nullable: true
		fontiCommissioni nullable: true
		contrattoNoleggio nullable: true
	}

	public String toString() {
		if (fontiCommissioni != null) {
			return fontiCommissioni.toString();
		} else {
			return new String();
		}
	}

	public boolean equals(Object other) {
		if (!(other instanceof Commissioni)) {
			return false;
		}
		Commissioni castOther = (Commissioni) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
}
