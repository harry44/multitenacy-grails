package com.dogmasystems.myrent.db

class SignatureAppRentalAgreement implements Serializable {



    MRRentalAgreement rentalAgreement
    MRUser user
    MRAffiliate affiliate

    Integer reportSelected
    Integer movementSelected
    String host
    String language
    String companyCode
    Date dateCreated
    RentToRent renttorent


    static constraints = {
        rentalAgreement nullable: true
        user nullable: true
        affiliate nullable: true
        reportSelected nullable: true
        movementSelected nullable: true
        host nullable: true
        dateCreated nullable: true
        companyCode nullable: true
        renttorent nullable: true

        language nullable: true
    }
    static mapping = {
        cache true
        id column: "id", sqlType: "int4"
        id generator:'sequence', params:[sequence:'siganture_app_seq']
        user  column: "user_id", sqlType: "int4"
        rentalAgreement  column: "rental_agreement_id", sqlType: "int4"
        affiliate  column: "affiliate_id", sqlType: "int4"
        language  column: "language"
        renttorent column: "renttorent", sqlType: "int4"
        version false
    }
}
