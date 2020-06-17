package com.dogmasystems.myrent.db

class UploadViaApp {

    MRRentalAgreement rentalAgreement
    MRUser user
    MRAffiliate affiliate
    MRBusinessPartner businesspartner
    MRVehicle vehicle
    MRReservation reservation

    String folderName
    Date dateCreated
    RentToRent renttorent



    static constraints = {
        rentalAgreement nullable: true
        user nullable: true
        affiliate nullable: true

        dateCreated nullable: true
        renttorent nullable: true
        folderName nullable:true
        vehicle nullable:true
        businesspartner nullable:true
        reservation nullable:true
    }
    static mapping = {
        cache true
        id column: "id", sqlType: "int4"
        id generator:'sequence', params:[sequence:'upload_via_app_seq']
        user  column: "user_id", sqlType: "int4"
        rentalAgreement  column: "rental_agreement_id", sqlType: "int4"
        businesspartner  column: "businesspartner_id", sqlType: "int4"
        vehicle  column: "vehicle_id", sqlType: "int4"
        affiliate  column: "affiliate_id", sqlType: "int4"
        reservation column:"reservation_id",sqlType:"int4"

        version false
    }
}
