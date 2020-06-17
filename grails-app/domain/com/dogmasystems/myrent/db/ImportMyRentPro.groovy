package com.dogmasystems.myrent.db

class ImportMyRentPro implements Serializable{

    String databaseName
    String username
    String password
    String hostname
    Date dateCreated, lastUpdated
    Long tenantId


    static constraints = {

    }

    static auditable = true

    def springSecurityService
    def beforeValidate() {
        def user = springSecurityService.currentUser
        tenantId = user.tenant?.tenantId
    }
}
