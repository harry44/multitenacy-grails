package com.dogmasystems

import groovy.transform.CompileStatic

@CompileStatic
class DatabaseProvisioningService {

    List<DatabaseConfiguration> findAllDatabaseConfiguration() {
        //List<String> usernames = //userRoleService.findAllUsernameByAuthority(VillainService.ROLE_ADMIN)
        List<CompanyProfile> usernames = CompanyProfile.executeQuery("from CompanyProfile")
        usernames.collect { findDatabaseConfigurationByUsername(it.companyName) }
    }

    DatabaseConfiguration findDatabaseConfigurationByUsername(String username) {
        new DatabaseConfiguration(dataSourceName: username, configuration: configurationByUsername(username)).save()
    }

    Map<String, Object> configurationByUsername(String username) {
        [
                'hibernate.hbm2ddl.auto':'update', // <1>
                'username': "myrent.${username=='noleggiare'?'noleggiarenet':username}", // <2>
                'password': 'myrent', // <2>
                'url':"jdbc:postgresql://localhost:5432/myrent.${username=='noleggiare'?'noleggiarenet':username}" // <2>
        ] as Map<String, Object>
    }
}

