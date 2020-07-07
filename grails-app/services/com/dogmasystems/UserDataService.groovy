package com.dogmasystems

import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.multitenancy.Tenants
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import org.grails.orm.hibernate.HibernateDatastore
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired

@Service(User)
@Transactional
@CurrentTenant
class UserDataService {
    @Autowired
    HibernateDatastore hibernateDatastore

    def serviceMethod() {

    }
    @CurrentTenant
    @Transactional
    User save(params,User user){
        DESEncrypter dencrypter = new DESEncrypter("utenti_myrent");
        user.properties=params
        println "password: "+dencrypter.encrypt(params.password)
        def password=dencrypter.encrypt(params.password)
        //user.password=dencrypter.encrypt(params.password)
        try{
            user.save(flush:true)
            if(user.password!=password){
                user.password=password
                user.save(flush:true)
            }
        }
        catch(Exception e) {
            println e
        }


        return user
    }
//    @CurrentTenant
//    @Transactional
//    def createUserRole(User user,Role role,UserRole userRole){
////        Serializable tenantId = Tenants.currentId(HibernateDatastore)
////        SessionFactory sessionFactory = hibernateDatastore.getDatastoreForConnection(tenantId.toString()).getSessionFactory()
////        def sx=sessionFactory.openSession()
//        userRole.user=User.get(user.id)
//        userRole.role=Role.get(role.id)
//        try{
//            UserRole.create(user,role)
//        }catch(Exception e){
//            println e
//        }
//
//      //  sx.save(userRole)
//       // sx.flush()
//
//    }
}
