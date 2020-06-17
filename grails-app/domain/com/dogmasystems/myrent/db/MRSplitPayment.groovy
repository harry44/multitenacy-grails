package com.dogmasystems.myrent.db

class MRSplitPayment implements Serializable{

    MRBusinessPartner businessPartner
    MRVatCodes standardVATcode
    MRVatCodes splitVATcode

    static mapping = {
        cache true
        table name: "split_payment"//, schema: "public"
        id generator:'sequence', params:[sequence:'split_payment_seq']
        id column: "id", sqlType: "int4"
        businessPartner column: "business_partner"
        standardVATcode column: "standard_VATcode"
        splitVATcode column: "split_VATcode"
        version false
    }

    static constraints = {
        businessPartner nullable: true
        splitVATcode nullable: true
        standardVATcode nullable: true
    }

    //static belongsTo = [ MRBusinessPartner]

    public MRSplitPayment(MRBusinessPartner businessPartner, MRVatCodes standardVATcode, MRVatCodes splitVATcode) {
        this.businessPartner = businessPartner;
        this.standardVATcode = standardVATcode;
        this.splitVATcode = splitVATcode;
    }
}
