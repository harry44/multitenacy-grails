package com.dogmasystems.myrent.db

class MRReservationDocument {
    MRReservation reservation
    MRDocumentManagement documentManagement

    static belongsTo = [MRReservation]
    static constraints = {
    }
}