package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRDamageSeverity
import grails.util.Holders

import java.sql.Time
import java.sql.Timestamp
import java.util.Date;

import com.dogmasystems.myrent.db.MRVehicle;

class MREventExpiration implements Serializable {

    static auditable = true
    MREventType eventType

    Date dateCreated, lastUpdated,
         eventDate, eventTime
    Boolean isActive,
            isAck, isDone, isDate,
            isMeter, isFuel, isTime,
            isCustom
    Long tenantId, eventKm
    Double eventFuel
    String description
    MRVehicle vehicle
    MRDocumentSeverity severity
    MRDamageSeverity damageSeverity
    Date dateCompleted
    Long vehicleKM
    Integer version
    def springSecurityService

    def beforeValidate() {
        def user = springSecurityService.currentUser
        if (user != null) {
            tenantId = user.tenant?.tenantId
        }
    }
    static hasOne = [eventType: MREventType]
    static mapping = {
        cache true
        id generator: 'sequence', params: [sequence: 'mrEventExpiration_seq']
        id column: "id", sqlType: "int4"
        eventTime column: 'time'
        damageSeverity column: 'id_damageseverity', sqlType: "int4"
        vehicleKM column: "vehiclekm"//, sqlType: "int4"
        tenantId column: "tenant_id", sqlType: "int4"
        eventKm column: "event_km", sqlType: "int4"
        eventType column: "event_type_id", sqlType: "int4"
        severity column: "severity_id", sqlType: "int4"
        vehicle column: "vehicle_id", sqlType: "int4"
        version column: "version", sqlType: "int4"
    }
    static constraints = {
        eventType nullable: true
        eventDate nullable: true
        isActive nullable: true
        isAck nullable: true
        isDone nullable: true
        isDate nullable: true
        isMeter nullable: true
        isFuel nullable: true
        isTime nullable: true
        isCustom nullable: true
        eventKm nullable: true
        eventFuel nullable: true
        severity nullable: true
        dateCompleted nullable: true
        vehicle nullable: true
        vehicleKM nullable: true
        damageSeverity nullable: true
        eventTime nullable: true
        version nullable: true
    }
}
