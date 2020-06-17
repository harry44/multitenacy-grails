package com.dogmasystems.myrent.db

class MRBookingOnlineStatus implements Serializable{

    String description
    Boolean disableForOnlineBooking
    //Integer color

    static mapping = {
        cache true
        table name: "booking_online_status"//, schema: "public"
        id generator:'sequence', params:[sequence:'booking_online_status_seq']
        id column: "id", sqlType: "int4"
        description column: "description"
        disableForOnlineBooking column: "disable_for_online_booking"
        version false
    }

    static constraints = {
        description nullable: true
        disableForOnlineBooking nullable: true
    }
}
