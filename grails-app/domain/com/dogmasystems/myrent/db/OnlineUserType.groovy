package com.dogmasystems.myrent.db

class OnlineUserType {
    String description;
    Integer id;
    Date dateCreated
    Date lastUpdated
    static auditable = true

    static mapping = {
        cache true
        table name: "online_user_type"
        id generator: 'sequence', params: [sequence: 'online_user_type_seq']
        id column: "id"
        description column: "description"
        version false
    }

    static constraints = {
        description nullable: true
    }
}
