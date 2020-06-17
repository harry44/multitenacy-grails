package com.dogmasystems.myrent.db

class RateImportResult {
    String fileName
    Date creationTime
    Integer createRates
    Integer updatedRates
    Integer totalExecutedLines
    Integer errorRate

    static constraints = {
        fileName nullable: false
        creationTime nullable: false
        createRates nullable: true
        updatedRates nullable: true
        totalExecutedLines nullable: true
        errorRate nullable: true
    }

    static mapping = {
        cache true
        table name: "rate_import_result"
        id generator:'sequence', params:[sequence:'rate_import_seq']
        id column: "id", sqlType: "int4"
        fileName column: "fileName", sqlType:"character varying"
        creationTime column: "creationTime", sqlType:"date"
        createRates column: "createRates"
        updatedRates column: "updatedRates"
        totalExecutedLines column: "totalExecutedLines"
        errorRate column: "errorRate"
        version false
    }
}
