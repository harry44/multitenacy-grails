package com.dogmasystems.myrent.db

class ReservationDetachFix {

    Integer id;
    MRLocation sede;
    Date scheduleDate;
    Integer schedulerDateTo;
    Integer schedulerDateFrom;
    Boolean isActivated;

    static mapping = {
        cache true
        table name: "reservation_detach_fix"//, schema: "public"
        id column: "id", sqlType: "int4"
        id generator:'sequence', params:[sequence:'reservation_detach_fix_seq']
        sede column: "id_sede", sqlType: "int4"
        scheduleDate column: "schedule_date"
        schedulerDateTo column: "schedule_date_to", sqlType: "int4"
        schedulerDateFrom column: "schedule_date_from", sqlType: "int4"
        isActivated column: "is_activated"
        version false
    }
    static constraints = {
        sede nullable: true
        scheduleDate nullable: true
        schedulerDateTo nullable: true
        isActivated nullable: true
        schedulerDateFrom nullable: true
    }
}
