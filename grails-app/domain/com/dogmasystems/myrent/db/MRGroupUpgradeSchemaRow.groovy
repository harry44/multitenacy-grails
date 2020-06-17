package com.dogmasystems.myrent.db

class MRGroupUpgradeSchemaRow implements Serializable{

    MRGroupUpgradeSchema groupUpgradeSchemaId
    MRGroup requestGroupId
    MRGroup fallbackGroupId
    Integer priority

    static constraints = {
        requestGroupId nullable: true
        groupUpgradeSchemaId nullable: true
        fallbackGroupId nullable: true

    }
    static mapping = {
        cache true
        table name: "group_upgrade_schema_row"//, schema:"public"
        id generator: 'sequence', params: [sequence: 'group_upgrade_schema_row_seq']
        id column: "id", sqlType: "int4"
        requestGroupId column: "id_request_group", sqlType: "int4"
        fallbackGroupId column: "id_fallback_group", sqlType: "int4"
        groupUpgradeSchemaId column: "id_group_upgrade_schema", sqlType: "int4"
        priority column: "priority"
        version false
    }
}
