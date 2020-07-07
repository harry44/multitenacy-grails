package com.dogmasystems

import grails.gorm.multitenancy.CurrentTenant
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*
@CurrentTenant
class UserController {

    UserService userService
    def userDataService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond userService.list(params), model:[userCount: userService.count()]
    }

    def show(Long id) {
        respond userService.get(id)
    }

    def create() {

        respond new User(params),model: [roleList:Role.list()]
    }

    def save(User user) {
        if (user == null) {
            notFound()
            return
        }

        try {
            user=userDataService.save(params,user)
            def role = Role.get(params.role.id)
            if(user && role) {
                //UserRole userRole=new UserRole()
                //userRoleTest.user=user
                //userRoleTest.role=role
                // userDataService.createUserRole(user,role,userRole)
                // println   userRoleTestService.save(userRoleTest)
                UserRole.create user, role, true

                UserRole.withSession {
                    it.flush()
                    it.clear()
                }
            }
        } catch (ValidationException e) {
            respond user.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*' { respond user, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond userService.get(id)
    }

    def update(User user) {
        if (user == null) {
            notFound()
            return
        }

        try {
            userDataService.save(params,user)
        } catch (ValidationException e) {
            respond user.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*'{ respond user, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        userService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
