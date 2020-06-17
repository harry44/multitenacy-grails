package com.dogmasystems

class CronManagement implements Serializable{

    Date scheduledTime
    String cronJobName

    static constraints = {
        id generator:'sequence', params:[sequence:'cron_management_seq']
        id column: "id", sqlType: "int4"
        scheduledTime nullable: true
        cronJobName nullable: true
    }
}
