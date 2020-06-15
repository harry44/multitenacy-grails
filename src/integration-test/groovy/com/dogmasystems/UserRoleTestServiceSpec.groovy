package com.dogmasystems

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class UserRoleTestServiceSpec extends Specification {

    UserRoleTestService userRoleTestService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new UserRoleTest(...).save(flush: true, failOnError: true)
        //new UserRoleTest(...).save(flush: true, failOnError: true)
        //UserRoleTest userRoleTest = new UserRoleTest(...).save(flush: true, failOnError: true)
        //new UserRoleTest(...).save(flush: true, failOnError: true)
        //new UserRoleTest(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //userRoleTest.id
    }

    void "test get"() {
        setupData()

        expect:
        userRoleTestService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<UserRoleTest> userRoleTestList = userRoleTestService.list(max: 2, offset: 2)

        then:
        userRoleTestList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        userRoleTestService.count() == 5
    }

    void "test delete"() {
        Long userRoleTestId = setupData()

        expect:
        userRoleTestService.count() == 5

        when:
        userRoleTestService.delete(userRoleTestId)
        sessionFactory.currentSession.flush()

        then:
        userRoleTestService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        UserRoleTest userRoleTest = new UserRoleTest()
        userRoleTestService.save(userRoleTest)

        then:
        userRoleTest.id != null
    }
}
