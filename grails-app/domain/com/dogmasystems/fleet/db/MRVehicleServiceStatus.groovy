package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRVehicle

class MRVehicleServiceStatus implements Serializable {
    long id
    Date startDate, endDate
    MRServiceStatus mrServiceStatus
    MRVehicle mrVehicle

    static mapping = {
        cache true
        table name: "mrvehicle_service_status"
        id generator: 'sequence', params: [sequence: 'vehicle_service_status_seq']
        id column: "id"//, sqlType: "int4"
        mrServiceStatus sqlType: "int4"
        mrVehicle sqlType: "int4"
        startDate sqlType: "date"
        endDate sqlType: "date"
        version false
    }
    static constraints = {
        mrServiceStatus nullable: true
        mrVehicle nullable: true
        startDate nullable: true
        endDate nullable: true
    }
}
