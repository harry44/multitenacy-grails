package com.dogmasystems

import grails.gorm.services.Service

@Service(CompanyProfile)
interface CompanyProfileService {

    CompanyProfile get(Serializable id)

    List<CompanyProfile> list(Map args)

    Long count()

    void delete(Serializable id)

    CompanyProfile save(CompanyProfile companyProfile)

}