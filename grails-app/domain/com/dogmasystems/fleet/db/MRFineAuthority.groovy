package com.dogmasystems.fleet.db

import javax.xml.soap.Text

class MRFineAuthority implements Serializable{
//    int id
    String authority;
    String office;
    String district;
    String street;
    String number;
    String zipCode;
    String city;
    String region;
    String phone1;
    String phone2;
    String mobilePhone;
    String email;
    String country;
    String memo;


    static auditable = true


    static constraints = {
        authority nullable: true
        office nullable: true
        district nullable: true
        street nullable: true
        number nullable: true
        zipCode nullable: true
        city nullable: true
        region nullable: true
        phone1 nullable: true
        phone2 nullable: true
        mobilePhone nullable: true
        email email: true, blank: false
        country nullable: true
        memo nullable: true

    }
    public String toString() {
        return authority != null ? authority : new String();
    }
    static mapping = {
        cache true
        table name: "multe_enti"//,schema: "public"
        id generator:'sequence', params:[sequence:'multe_enti_seq']
        id column: "id", sqlType: 'int4'
        authority column: "organo"
        office column: "ufficio"
        district column: "comune"
        street column: "via"
        number column: "numero"
        zipCode column: "cap"
        city column: "citta"
        region column: "provincia"
        phone1 column: "telefono1"
        phone2 column: "telefono2"
        mobilePhone column: "cellulare"
        email column: "email"
        country column: "nazione"
        memo column: "promemoria"//, sqlType: 'text'


        version false;


    }
}
