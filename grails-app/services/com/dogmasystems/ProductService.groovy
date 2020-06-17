package com.dogmasystems

import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.services.Service
@CurrentTenant
@Service(Product)
interface ProductService {

    Product get(Serializable id)

    List<Product> list(Map args)

    Long count()

    void delete(Serializable id)

    Product save(Product product)

}