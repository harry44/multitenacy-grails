package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRChartAccount;
import com.dogmasystems.myrent.db.MRHeaderInvoice;
import com.dogmasystems.myrent.db.MRBusinessPartner;
import com.dogmasystems.myrent.db.MRRentalAgreement
import com.dogmasystems.myrent.db.MRReservation
import com.dogmasystems.myrent.db.MRReservationSource
import grails.util.Holders
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder;

class Pagamenti implements Serializable{

	String descrizione
	Boolean contanti
	Boolean assegno
	Boolean cartaCredito
	Boolean scadenziario
	Boolean differito
	Boolean prepagato
	Integer numeroRate
	Integer intervallo
	Integer giorniPrimaScadenza
	Integer primoMeseEscl
	Integer ggFisso1
	Integer secMeseEscl
	Integer ggFisso2
	Integer ggDopo
	Boolean dataFattura
	Boolean fineMese
	Boolean primaRataNormale
	Boolean primaRataIva
	Boolean primaRataIvaSpese
	String codiceEsportazione
	MezziPagamenti mezziPagamenti
	MRChartAccount pianoDeiConti
	//Add the column to contain id of tipo_pagamenti table of pro database
	Integer proKey
	String condizioniPagamento
	String modalitaPagamento
	@Override
	public boolean equals(Object other) {
		if ( !(other instanceof Pagamenti) ) return false;
		Pagamenti castOther = (Pagamenti) other;
		return new EqualsBuilder()
				.append(this.getId(), castOther.getId())
				.isEquals();
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(getId())
				.toHashCode();
	}
	@Override
	public String toString() {
		return getDescrizione() + ", " + mezziPagamenti?.getCodice() + ", "+ mezziPagamenti?.getDescrizione(); //NOI18N
	}

	static hasMany = [clientis            : MRBusinessPartner,
					  contrattoNoleggios  : MRRentalAgreement,
					  fatturaIntestaziones: MRHeaderInvoice,
					  fontiCommissionis   : MRReservationSource,
					  prenotazionis       : MRReservation,
					  preventivis         : Preventivi,
					  righePrimanotas     : RighePrimanota,
					  scadenzes           : Scadenze]
	static belongsTo = [MezziPagamenti, MRChartAccount]

	static mapping = {
		cache true
		////datasource 'myrent'
		table name: "pagamenti"//, schema: "public"
		//id generator: "assigned"
		id generator:'sequence', params:[sequence:'pagamenti_seq']
		id column: "id", sqlType: "int4"
		mezziPagamenti column:"id_mezzo_pagamento", sqlType:"int4"
		pianoDeiConti column:"conto_incasso", sqlType:"int4"
		ggFisso1 column:"gg_fisso_1"
		ggFisso2 column:"gg_fisso_2"

		fatturaIntestaziones column:"pagamenti_fatt_intstziones_id"


			descrizione column:"descrizione"


			codiceEsportazione column:"codice_esportazione"

		proKey column: "prokey"
		condizioniPagamento column: "condizioni_pagamento"
		modalitaPagamento column: "modalita_pagamento"

		version false
	}

	static constraints = {
		descrizione nullable: true
		contanti nullable: true
		assegno nullable: true
		cartaCredito nullable: true
		scadenziario nullable: true
		differito nullable: true
		prepagato nullable: true
		numeroRate nullable: true
		intervallo nullable: true
		giorniPrimaScadenza nullable: true
		primoMeseEscl nullable: true
		ggFisso1 nullable: true
		secMeseEscl nullable: true
		ggFisso2 nullable: true
		ggDopo nullable: true
		dataFattura nullable: true
		fineMese nullable: true
		primaRataNormale nullable: true
		primaRataIva nullable: true
		primaRataIvaSpese nullable: true
		codiceEsportazione nullable: true
		proKey nullable: true
		pianoDeiConti nullable: true
		condizioniPagamento nullable: true
		modalitaPagamento nullable: true

	}
}
