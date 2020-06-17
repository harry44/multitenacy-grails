package com.dogmasystems

import grails.gorm.MultiTenant
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class MRUser implements Serializable, MultiTenant<MRUser> {

    private static final long serialVersionUID = 1

    String username
    String password
    String fullname
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    Integer tenantId
    Date dateCreated,lastUpdated
    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        fullname nullable: true, blank: true
    }

    static mapping = {
        id generator:'sequence', params:[sequence:'user_seq']
	    table(name:"dswp_users")
        password column: '`password`'

    }
    def beforeInsert(){
        tenantId=1
    }
}
