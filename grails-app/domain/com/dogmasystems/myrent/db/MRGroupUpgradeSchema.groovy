package com.dogmasystems.myrent.db

import grails.util.Holders

class MRGroupUpgradeSchema implements Serializable{

    String description
   // MRReservationSource reservationSource

    static constraints = {
        description nullable :true

    }

    //static hasMany = [ reservationSource : MRReservationSource,
       //     ]

    static mapping = {
        cache true
        //id generator: "assigned"
        table name: "group_upgrade_schema"
        id generator:'sequence', params:[sequence:'group_upgrade_schema_seq']
        id column: "id", sqlType: "int4"
         description column: "description"


        version false
    }

}
