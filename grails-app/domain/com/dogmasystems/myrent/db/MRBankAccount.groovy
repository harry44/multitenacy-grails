package com.dogmasystems.myrent.db

class MRBankAccount implements Serializable {

    String bankName
    String description
    String address1
    String address2
    String address3
    String swiftCode
    String accountNumber
    String iban
    String bban
    MRUser createdBy
    Date created
    MRUser modifiedBy
    Date modified


    static constraints = {
        bankName nullable: true
        description nullable: true
        address1 nullable: true
        address2 nullable: true
        address3 nullable: true
        accountNumber nullable: true
        iban nullable: true
        created nullable: true
        createdBy nullable: true
        modified nullable: true
        modifiedBy nullable: true
        bban nullable: true
        swiftCode nullable: true

    }

    static mapping = {
        cache true
        table name: "bank_account"//, schema: "public"
        id generator: 'sequence', params: [sequence: 'bank_account_seq']
        id column: "id", sqlType: "int4"
        description column: "description"
        bankName column: "bank_name"
        address1 column: "address1"
        address2 column: "address2"
        address3 column: "address3"
        accountNumber column: "account_number"
        createdBy column: "id_user_created", sqlType: "int4"
        created column: "created"
        modified column: "modified"
        modifiedBy column: "id_user_modified", sqlType: "int4"
        //	pianoDeiConti column:"id_conto_iva", sqlType:"int4"
        version false
    }
}
