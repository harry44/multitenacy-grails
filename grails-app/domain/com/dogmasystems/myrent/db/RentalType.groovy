package com.dogmasystems.myrent.db

import grails.gorm.transactions.Transactional
import grails.util.Holders

@Transactional
class RentalType implements Serializable {

    Integer id;
    String description;
    Boolean isRental;
    Boolean isFreeLoan;
    Boolean isFranchiseRental;
    Boolean isAgreement;

    static hasMany = [fontiCommissioni: MRReservationSource]

    static mapping = {
        cache true
        table name: "rental_type"
        id generator: 'sequence', params: [sequence: 'rental_type_seq']
        id column: "id" //, sqlType: "int4"
        description column: "description"
        isAgreement column: "isagreement"

        isRental column: "is_rental"


        isFreeLoan column: "is_free_loan"

        isFranchiseRental column: "is_franchise_rental"

        version false
    }
    static constraints = {
    }
}
