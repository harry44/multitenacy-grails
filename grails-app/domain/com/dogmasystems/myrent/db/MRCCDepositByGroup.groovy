package com.dogmasystems.myrent.db
class MRCCDepositByGroup implements Serializable{

    Boolean isDepositRequired
    Boolean isDamageWaiverRequired
    Boolean isTheftWaiverRequired
    MRGroup carClassId
    MRReservationSource fontiCommissioni

    /*static hasMany = [
            reservationSourceId : MRReservationSource
    ]

    static mappedBy = [
            reservationSourceId: "depositByGroups"
    ]*/

    static mapping = {
        cache true
        table name: "cc_deposit_by_group"//, schema: "public"
        id column: "id", sqlType: "int4"
        id generator: 'sequence', params: [sequence: 'cc_deposit_by_group_seq']
        isDepositRequired column: "is_deposit_required"
        isDamageWaiverRequired column: "is_damage_waiver_required"
        isTheftWaiverRequired column: "is_theft_waiver_required"
        fontiCommissioni column: "reservation_source_id", sqlType:"int4"
        carClassId column: "gruppo_Id", sqlType: "int4"
        version false
    }

    static constraints = {
        isDepositRequired nullable: true
        isDamageWaiverRequired nullable: true
        isTheftWaiverRequired nullable: true
        fontiCommissioni nullable: true
        //reservationSourceId nullable: true
        carClassId nullable: true
    }
}
