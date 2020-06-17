package com.dogmasystems

class EmailTemplate implements Serializable{

    static auditable=true

    String name
    String content
    String subject
    Long tenantId

    Date dateCreated
    Date lastUpdated

//    def springSecurityService
//    def beforeValidate() {
//        def user = springSecurityService.currentUser
//        tenantId = user.tenant?.id
//    }

    static constraints = {
        subject nullable: true
    }

    static mapping = {
        content sqlType: "Text"
    }
}
