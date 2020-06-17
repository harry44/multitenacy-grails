package com.dogmasystems.myrent.db

class MRTrackingDevice implements Serializable{

    String imei
    String userName
    String password
    String mobileNumber
    MRTrackingTypeSystem trackingTypeSystemId

    static mapping = {
        cache true
        table name: "tracking_device"//, schema: "public"
        id generator:'sequence', params:[sequence:'tracking_device_seq']
        id column: "id", sqlType: "int4"
        imei column: "imei"
        userName column: "user_name"
        password column: "password"
        mobileNumber column: "mobile_number"
        trackingTypeSystemId column: "tracking_type_system_id", sqlType: "int4"
        version false
    }

    static constraints = {
        imei nullable: true
        userName nullable: true
        password nullable: true
        mobileNumber nullable: true
        trackingTypeSystemId nullable: true
    }

    String toString() {
        String trackingDeviceStr = ""
        trackingDeviceStr += trackingTypeSystemId?.description != null ? trackingTypeSystemId?.description : ""
        trackingDeviceStr = imei != null ? trackingDeviceStr != "" ? imei + " - " + trackingDeviceStr : imei : ""
        return trackingDeviceStr
    }
}
