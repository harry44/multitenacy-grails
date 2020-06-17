package com.dogmasystems

class BootStrap {
    def hibernateDatastore
    DatabaseProvisioningService databaseProvisioningService
    def init = { servletContext ->
//        new Role(authority: 'ROLE_ADMIN').save(flush:true)
     //   new Role(authority: 'ROLE_USER').save(flush:true)
       // new Product(prodCode: 'P0001', prodName: 'iPhone', prodModel: 'XS', prodDesc: '', prodImageUrl: 'https://ibox.co.id/media/catalog/product/cache/3/image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphonexs-max-gold_3_1.png', prodPrice: 999).save(flush:true)

        for (DatabaseConfiguration databaseConfiguration : databaseProvisioningService.findAllDatabaseConfiguration() ) {
            hibernateDatastore.getConnectionSources().addConnectionSource(databaseConfiguration.dataSourceName, databaseConfiguration.configuration)
        }

        hibernateDatastore.getConnectionSources().each{
            println it.name
        }

    }
    def destroy = {
    }
}
