package com.dogmasystems.myrent.db

class MRCurrency implements Serializable {

    String code;
    String description;
    Boolean enabled;
    Boolean isDefault;
    Date createDate;
    Date modifyDate;
    MRUser createdBy;
    MRUser lastModifyBy;

    static mapping = {
        cache true
        table name: "currency"
        id name: "code", generator: "assigned"
        description column: "description"
        enabled column: "enabled"
        isDefault column: "is_default"
        createDate column: "create_date"
        modifyDate column: "modify_date"
        createdBy column: "id_user_created", sqlType: "int4"
        lastModifyBy column: "id_user_last_modify", sqlType: "int4"

        version false
    }
    static constraints = {
        description nullable: true
        enabled nullable: true
        isDefault nullable: true
        createDate nullable: true
        modifyDate nullable: true
        createdBy nullable: true
        lastModifyBy nullable: true

    }
}
