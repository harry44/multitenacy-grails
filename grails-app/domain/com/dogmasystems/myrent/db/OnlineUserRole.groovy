package com.dogmasystems.myrent.db

class OnlineUserRole {
    String description
    Date dateCreated
    Date lastUpdated
    Long id

    static auditable = true

    static mapping = {
        cache true
        table name: "online_user_role"
        id generator: 'sequence', params: [sequence: 'online_user_role_seq']
        id column: "id"
        description column: "description"
        version false
    }

    static constraints = {
        description nullable: true
    }

}
