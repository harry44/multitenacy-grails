package com.dogmasystems.myrent.db

import grails.util.Holders

class MRFreeSell implements Serializable{

    Boolean isStopSell
    Date startDate
    Date endDate
    MRGroup carClass
    MRLocation location
    MRReservationSource reservationSource

    static mapping = {
        cache true
        location cache:true
        reservationSource cache:true
        carClass cache:true

        table name: "free_sell"//, schema: "public"
        id generator:'sequence', params:[sequence:'freesell_seq']
        id column: "id", sqlType: "int4"
        isStopSell column: "is_stop_sell"
        startDate column: "start_date"
        endDate column :"end_date"
        carClass column: "car_class_id", sqlType: "int4"
        location column: "location_id", sqlType: "int4"
        reservationSource column: "id_fonte", sqlType: "int4"
        version false
    }

    static constraints = {
        isStopSell nullable: true
        startDate nullable: true
        endDate nullable: true
        carClass nullable: true
        location nullable: true
        reservationSource nullable: true
    }
}
