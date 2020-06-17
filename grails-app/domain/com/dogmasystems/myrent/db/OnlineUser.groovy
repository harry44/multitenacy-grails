package com.dogmasystems.myrent.db

class OnlineUser {
    String firstName
    String lastName
    String email
    String phoneNumber
    OnlineUserType onlineUserType
    OnlineUserRole roleId
    Date dateCreated
    Date lastUpdated
    static auditable = true
    static belongsTo = [OnlineUserType, OnlineUserRole]

    static mapping = {
        cache true
        table name: "online_user"
        id generator: 'sequence', params: [sequence: 'online_user_seq']
        id column: "id", sqlType: "int4"
        firstName column: "first_name"
        lastName column: "last_name"
        email column: "email"
        phoneNumber column: "phone_number"
        onlineUserType column: "online_user_type", sqlType: "int4"
        roleId column: "role_id", sqlType: "int4"
        version false
    }

    static constraints = {
        firstName nullable: true
        lastName nullable: true
        email nullable: true
        phoneNumber nullable: true
        onlineUserType nullable: true
        roleId nullable: true

    }
}
