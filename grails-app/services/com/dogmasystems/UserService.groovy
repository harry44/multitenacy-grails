package com.dogmasystems

import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.services.Service

import javax.transaction.Transactional

@CurrentTenant
@Service(User)
interface UserService {

    User get(Serializable id)

    List<User> list(Map args)

    Long count()

    void delete(Serializable id)



}