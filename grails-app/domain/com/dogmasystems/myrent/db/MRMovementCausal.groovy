package com.dogmasystems.myrent.db

import grails.util.Holders

//import com.dogmasystems.utils.db.PersistentInstance

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.ToStringBuilder

class MRMovementCausal implements Serializable{


    String description;
    Boolean isRental;
    Boolean isReservation;
    Boolean isTransfer;
    Boolean isReparation;
    Boolean isWashing;
    Boolean isRefuel;
    Boolean futureUnavailability;

    public static final Integer CAUSALE_NOLEGGIO = new Integer(1);
    public static final Integer CAUSALE_PRENOTAZIONE = new Integer(2);

    /** Creates a new instance of CausaleMovimento */
    public MRMovementCausal() {
    }
//	@Override
//	public Long getId() {
//		// TODO Auto-generated method stub
//		return this.id;
//	}
//	public void setId(Long id){
//		this.id = id;
//	}

    public boolean equals(Object other) {
        if(other != null && other instanceof MRMovementCausal) {
            return new EqualsBuilder().append(getId(), ((MRMovementCausal)other).getId()).isEquals();
        } else {
            return false;
        }
    }

//    public String toString() {
//        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getDescription()).toString();
//    }


    static hasMany = [movimentiAutos: MRVehicleMovement]

    static mapping = {
        cache true
        table name:"causali_movimento"//, schema:"public"
        id generator:'sequence', params:[sequence:'causali_movimento_seq']
        id column: "id", sqlType: "int4"

        isRental column:"noleggio"
        isReservation column:"prenotazione"
        isTransfer column:"trasferimento"
        isReparation column:"riparazione"
        isWashing column:"lavaggio"
        isRefuel column:"rifornimento"
        description column:"descrizione"
        futureUnavailability column:"indisponibilitafutura"

        version false
    }

    static constraints = {
        //	description nullable: true
        isRental nullable: true
        isReservation nullable: true
        isTransfer nullable: true
        isReparation nullable: true
        isWashing nullable: true
        isRefuel nullable: true
        futureUnavailability nullable: true
    }


}
