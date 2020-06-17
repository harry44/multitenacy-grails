package com.dogmasystems.myrent.db

class MRRentalAgreementDocument {

    MRRentalAgreement rentalAgreement
    MRDocumentManagement documentManagement

    static belongsTo = [MRRentalAgreement]
    static constraints = {
    }
}
