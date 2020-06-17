package com.dogmasystems

import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.services.Service

@CurrentTenant
@Service(MRUser)
interface UserService {

    MRUser get(Serializable id)

    List<MRUser> list(Map args)

    Long count()

    void delete(Serializable id)



}