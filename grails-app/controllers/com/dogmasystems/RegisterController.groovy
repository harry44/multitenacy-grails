package com.dogmasystems

import grails.gorm.multitenancy.CurrentTenant
import grails.validation.ValidationException
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Transactional
@CurrentTenant
@Secured('permitAll')
class RegisterController {
  def userDataService
    def userRoleTestService
    static allowedMethods = [register: "POST"]
    @CurrentTenant
    def index() {
        def roleList=Role.list()
        model:[roleList:roleList]
    }
    @Transactional
    @CurrentTenant
    def register() {
        if(!params.password.equals(params.repassword)) {
            flash.message = "Password and Re-Password not match"
            redirect action: "index"
            return
        } else {
            try {
                def user = MRUser.findByUsername(params.username)?: userDataService.save(params,new MRUser())
                def role = Role.get(params.role.id)
                if(user && role) {
                    //UserRole userRole=new UserRole()
                    //userRoleTest.user=user
                    //userRoleTest.role=role
                   // userDataService.createUserRole(user,role,userRole)
                 // println   userRoleTestService.save(userRoleTest)
                   UserRole.create user, role ,true

                   UserRole.withSession {
                      it.flush()
                      it.clear()
                    }

                    flash.message = "You have registered successfully. Please login."
                    redirect controller: "login", action: "auth"
                } else {
                    flash.message = "Register failed"
                    render view: "index"
                    return
                }
            } catch (ValidationException e) {
                flash.message = "Register Failed"
                redirect action: "index"
                return
            }
        }
    }
}
