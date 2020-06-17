package com.dogmasystems.myrent.db

class MRBusinessPartnerDocument implements Serializable {
    MRBusinessPartner businessPartner
    MRDocumentManagement documentManagement

    static belongsTo = [MRBusinessPartner]
    static constraints = {
    }
}
