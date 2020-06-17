package com.dogmasystems.myrent.db

import grails.util.Holders

class MROptionalClass implements Serializable{

    String description;
    Boolean isAncillary;
    Boolean isFine;
    Boolean isExcess;

    static mapping = {
        cache true
        table name: "optional_class"//, schema: "public"
        id generator:'sequence', params:[sequence:'optional_class_seq']
        id column: "id", sqlType: "int4"
        description column:"description"
        isAncillary column:"is_ancillary"
        isFine column:"is_fine"
        isExcess column:"is_excess"

        version false
    }

    static constraints = {
        isAncillary nullable: true
        isFine nullable: true
        isExcess nullable: true
    }
}
