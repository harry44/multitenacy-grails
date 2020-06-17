package com.dogmasystems.fleet.db

class MRCouponState implements Serializable{
    String description

    Long tenantId
    static auditable = true

    def springSecurityService
    def beforeValidate() {
        def user = springSecurityService.currentUser
        if(user != null){
            tenantId = user.tenant?.tenantId
        }
    }

    static mapping = {
        cache true
        id generator:'sequence', params:[sequence:'mrCouponState_seq']

    }
    String toString(){
        return description
    }

    static constraints = {
        description unique: true

    }
}
