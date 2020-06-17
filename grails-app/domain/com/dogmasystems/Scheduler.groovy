package com.dogmasystems

class Scheduler implements Serializable{     //This object should be created using BOOTSTRAP for each JOB

    static auditable=true

    String jobName
    EmailTemplate template

    Date dateCreated
    Date lastUpdated
    String cronExpression
    Integer monthInterval

    static constraints = {
        template nullable: true
        cronExpression nullable: true
        monthInterval nullable: true
    }
}
