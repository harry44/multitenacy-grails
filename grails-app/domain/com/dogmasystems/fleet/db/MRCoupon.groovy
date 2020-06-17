package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRReservationSource

class MRCoupon implements Serializable{
    String coupon
    String voucher
    MRCouponState status
    MRReservationSource reservationSource
    Boolean moreCoupon

    Date dateCreated, lastUpdated
    Long createdBy
    Long updatedBy

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
        id generator:'sequence', params:[sequence:'mrCoupon_seq']
        reservationSource sqlType: "int4"

    }

    static constraints = {
        voucher nullable: true
        reservationSource nullable: true
        createdBy nullable: true
        updatedBy nullable: true
        moreCoupon nullable: true
        coupon nullable: true
        status nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }
}
