package com.dogmasystems.myrent.db

class MRCausaleMovement implements Serializable{
    String description;
    Boolean rental;
    Boolean reservation;
    Boolean transfer;
    Boolean repair;
    Boolean wash;
    Boolean refuel;
    Boolean unavailable;
    Boolean isDisabled;
    static mapping = {
        cache true
        table name: "causali_movimento"
        id generator: 'sequence', params: [sequence: 'causali_movimento_seq']
        id column: "id", sqlType: "int4"
        description column: "descrizione"
        rental column: "noleggio"
        reservation column: "prenotazione"
        transfer column: "trasferimento"
        repair column: "riparazione"
        wash column: "lavaggio"
        refuel column: "rifornimento"
        unavailable column: "indisponibilitafutura"
        isDisabled column: "is_disabled"
        version false
    }
    static constraints = {
        description(nullable: true)
        rental(nullable: true)
        reservation(nullable: true)
        transfer(nullable: true)
        repair(nullable: true)
        wash(nullable: true)
        refuel(nullable: true)
        unavailable(nullable: true)
        isDisabled(nullable: true)
    }
}
