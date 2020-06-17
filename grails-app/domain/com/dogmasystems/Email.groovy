package com.dogmasystems

class Email implements Serializable{

    static auditable=true

    String smtpHost
    String smtpAuth
    String smtpUser
    String smtpPass
    String mailFrom
    String mailCC
    String senderName
    String sendMethod
    Integer portNumber
    Boolean authNotProtected

    Boolean isEmailEngineEnabled

    Date dateCreated
    Date lastUpdated

    Long tenantId

    static constraints = {
        isEmailEngineEnabled blank:true, nullable:true
        smtpHost blank:true, nullable:true
        smtpAuth blank:true, nullable:true
        smtpUser blank:true, nullable:true
        smtpPass blank:true, nullable:true
        mailFrom blank:true, nullable:true
        mailCC blank:true, nullable:true
        portNumber blank:true, nullable:true
        senderName nullable: true
        authNotProtected blank:true, nullable:true
        sendMethod( inList: ["NORMAL", "SSL", "TLS" ])
    }

    def springSecurityService
    def beforeValidate() {
        def user = springSecurityService.currentUser
        tenantId = user.tenant?.tenantId
    }
}