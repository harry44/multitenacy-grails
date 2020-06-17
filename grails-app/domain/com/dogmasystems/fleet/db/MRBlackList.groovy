package com.dogmasystems.fleet.db
/**
 * Created by kavi on 07-09-2016.
 */
class MRBlackList implements Serializable{

    String driverLicense
    Date birthdate
    String taxId
    String surname
    String firstname
    String secondname
    String note
    String country
    String reason
    Boolean isInBlackList

    Date dateCreated, lastUpdated
    Long createdBy
    Long updatedBy

    Long tenantId
    static auditable = true
    def springSecurityService

    def beforeValidate() {
        def user = springSecurityService.currentUser
        if(user != null){
            tenantId = user.tenant?.tenantId
        }
    }

    static mapping = {
        cache true
        table name: "black_list"
        id generator:'sequence', params:[sequence:'black_list_seq']
        id column: "id", sqlType: "int4"
        driverLicense column:"driver_license"
        surname column:"surname"
        firstname column:"firstname"
        birthdate column:"birthdate" ,sqlType: "date"
        note column:"note"
        reason column:"reason"
        isInBlackList column:"is_in_black_list"
        taxId column:"tax_id"
        country column:"country"
        secondname column:"middle_name"
        dateCreated column:"insert_date" ,sqlType: "date"
        tenantId column:"tenant_id"
        version false
     //   note sqlType: 'text'
    }

 static  constraints={
     driverLicense nullable: true
     birthdate nullable: true
     taxId nullable: true
     surname nullable:false
     firstname nullable: false
     secondname nullable: true
     note nullable: true
     createdBy nullable: true
     updatedBy nullable: true
     country nullable: true
     isInBlackList nullable: true
     reason nullable: true
     dateCreated nullable: true
     lastUpdated nullable: true
     tenantId nullable: true
     version nullable: true
}

}
