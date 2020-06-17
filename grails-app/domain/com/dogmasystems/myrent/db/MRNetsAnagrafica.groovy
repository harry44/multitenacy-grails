package com.dogmasystems.myrent.db

class MRNetsAnagrafica implements Serializable{

    String id;
    String token;
    String wsdlLink;
    String notifyUrl;
    String errorUrl;
    Boolean inUse;

    String userId;

    String shaIn;

    String refId;

    static hasMany = [
            location : MRLocation
    ]


    static mapping = {
        table name: "nets_anagrafica"//, schema: "public"
        id column: "merchant_id", generator: "assigned"
        token column: "token"
        wsdlLink column: "wsdl_link"
        notifyUrl column: "notify_url"
        errorUrl column: "error_url"
        inUse column: "in_use"
        userId column: "user_id"
        shaIn column: "sha_in"
        refId column: "ref_id"
        version false
    }
    static constraints = {
        token nullable: true
        wsdlLink nullable: true
        notifyUrl nullable: true
        errorUrl nullable: true
        inUse nullable: true
        userId nullable: true
        shaIn nullable: true
        refId nullable: true
    }
}
