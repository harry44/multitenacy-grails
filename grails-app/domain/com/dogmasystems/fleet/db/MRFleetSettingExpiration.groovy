package com.dogmasystems.fleet.db

class MRFleetSettingExpiration implements Serializable{

    Double kmAlert
    Double daysAlert
    static auditable = true
    Date dateCreated, lastUpdated

    Long tenantId

    def springSecurityService
    def beforeValidate() {
        def user = springSecurityService.currentUser
        if(user != null){
            tenantId = user.tenant?.tenantId
        }
    }
    static constraints = {
        kmAlert nullable: true
        daysAlert nullable: true
    }

    static mapping = {
        cache true
        id generator:'sequence', params:[sequence:'mrFleetSettingExpiration_seq']
    }
}

