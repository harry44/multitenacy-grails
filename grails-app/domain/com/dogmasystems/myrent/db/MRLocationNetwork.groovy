package com.dogmasystems.myrent.db

class MRLocationNetwork implements Serializable{

     Integer id;
     String description;
     String familyCode;
     Boolean isFranchisee;

    static constraints = {
        description nullable: true
        familyCode nullable: true
        isFranchisee nullable: true
    }
    static hasMany = [fontiCommissioni  : MRReservationSource]
    static mapping = {
        cache true
        table name: "location_network"//, schema:"public"
        id generator: 'sequence', params: [sequence: 'location_network_seq']
        id column: "id"//, sqlType: "int4"
        description column: "description"
        familyCode column: "family_code"
        isFranchisee column: "isFranchisee" ,defaultValue: "false"
        version false
    }
    public String toString() {
        return  description != null ? description : new String();
    }
}
