package com.dogmasystems.myrent.db

class MRTargetSource {

    String description
    Boolean isTourOperator
    Boolean isWeb
    Boolean isCorporate
    Boolean isWalkIn

    static constraints = {
        description(nullable: true)
        isTourOperator(nullable: true)
        isWeb(nullable: true)
        isCorporate(nullable: true)
        isWalkIn(nullable: true)

    }
    static mapping = {
        cache true
        table name: "fonte_target"
        id generator: "sequence", params: [sequence: "fonte_target_seq"]
        id columns: "id", sqlType: "int4"
        description column: "description"
        isTourOperator column: "istouroperator"
        isWeb column: "isweb"
        isCorporate column: "iscorporate"
        isWalkIn column: "iswalkin"
        version false
    }

}
