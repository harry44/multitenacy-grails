package com.dogmasystems.myrent.db

import grails.util.Holders

class MRNotification {

    String status
    String serviceName
    String inputParam
    String outMsg
    String commentMsg
    MRUser users
    Date lastUpdated
    Boolean notifyFlag

    static mapping = {
        cache true
        table name: "notification"//, schema: "public"
        id column: "id", sqlType: "int4"

        id generator:'sequence', params:[sequence:'notification_seq']
        status column: "status"
        serviceName column: "service_name"
        inputParam column: "input_param"
        outMsg column: "out_msg"
        commentMsg column: "comment_msg"
        users column: "id_user_created", sqlType: "int4"

        lastUpdated column: "last_updated"
        notifyFlag column: "notify_flag"
        version false
    }

    static constraints = {
        status nullable: true
        serviceName nullable: true
        inputParam nullable: true
        outMsg nullable: true
        commentMsg nullable: true
        users nullable: true
        lastUpdated nullable: true
        notifyFlag nullable: true
    }
}
