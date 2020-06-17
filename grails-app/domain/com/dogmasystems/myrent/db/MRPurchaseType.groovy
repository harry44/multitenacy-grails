package com.dogmasystems.myrent.db

class MRPurchaseType implements Serializable{

    String description

    static mapping = {
        cache true
        table name: "purchase_type"//, schema: "public"
        id generator:'sequence', params:[sequence:'purchase_type_seq']
        id column: "id", sqlType: "int4"
        description column: "description"
        version false
    }

    static constraints = {
        description nullable: true
    }

    @Override
    public String toString() {
        return description != null ? description : new String();
    }
}
