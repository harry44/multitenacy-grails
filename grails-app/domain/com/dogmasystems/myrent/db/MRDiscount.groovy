package com.dogmasystems.myrent.db



class MRDiscount {

    MRGroup group
    MRLocation location
    Date offerStart
    Date offerEnd
    Date seasonStart
    Date seasonEnd
    Integer minDays
    Integer maxDays
    Integer discount
    MRReservationSource fontiCommissioni

    static belongsTo = [MRReservationSource]
    static mapping = {
        cache true
        id generator:'sequence', params:[sequence:'mrDiscount_seq']
        offerStart sqlType:"date"
        offerEnd sqlType:"date"
        seasonStart sqlType:"date"
        seasonEnd sqlType:"date"
        group column: "group_id", sqlType: "int4"
        version false
    }
    static constraints = {
        offerStart nullable: true
        offerEnd nullable: true
        seasonStart nullable: true
        seasonEnd nullable: true
        minDays nullable: true
        maxDays nullable: true
        location nullable: true
        group nullable: true
        discount nullable: true
    }

}
