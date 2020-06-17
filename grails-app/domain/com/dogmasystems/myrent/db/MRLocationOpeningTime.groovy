package com.dogmasystems.myrent.db

class MRLocationOpeningTime implements Serializable {

    Date specificDate
    Date startTime
    Date endTime
    MRLocation locationId
    Integer dayOfTheWeek
    Boolean isClosure
    Boolean isADate




    static mapping = {
        cache true
        table name:"location_opening_time"
        id generator:'sequence', params:[sequence:'location_opening_time_seq']
        id column: "id", sqlType: "int4"
        specificDate column: "specific_date" , sqlType: "date"
        startTime column: "start_time" , sqlType: "date"
        endTime column: "end_time" , sqlType: "date"
        dayOfTheWeek column: "day_of_the_week" , sqlType: "int4"
        isClosure column: "is_closure"
        isADate column: "is_a_date"
        locationId column: "id_sede" , sqlType: "int4"
        version false

    }
    static constraints = {

        specificDate nullable: true
        startTime nullable: true
        endTime nullable: true
        locationId nullable: true
        dayOfTheWeek nullable: true
        isClosure nullable: true
        isADate nullable: true
    }
}
