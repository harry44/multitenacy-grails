package com.dogmasystems

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class CompanyProfileController {

    CompanyProfileService companyProfileService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond companyProfileService.list(params), model:[companyProfileCount: companyProfileService.count()]
    }

    def show(Long id) {
        respond companyProfileService.get(id)
    }

    def create() {
        respond new CompanyProfile(params)
    }

    def save(CompanyProfile companyProfile) {
        if (companyProfile == null) {
            notFound()
            return
        }

        try {
            companyProfileService.save(companyProfile)
        } catch (ValidationException e) {
            respond companyProfile.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'companyProfile.label', default: 'CompanyProfile'), companyProfile.id])
                redirect companyProfile
            }
            '*' { respond companyProfile, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond companyProfileService.get(id)
    }

    def update(CompanyProfile companyProfile) {
        if (companyProfile == null) {
            notFound()
            return
        }

        try {
            companyProfileService.save(companyProfile)
        } catch (ValidationException e) {
            respond companyProfile.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'companyProfile.label', default: 'CompanyProfile'), companyProfile.id])
                redirect companyProfile
            }
            '*'{ respond companyProfile, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        companyProfileService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'companyProfile.label', default: 'CompanyProfile'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'companyProfile.label', default: 'CompanyProfile'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
