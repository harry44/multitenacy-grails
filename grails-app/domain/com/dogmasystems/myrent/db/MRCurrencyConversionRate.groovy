package com.dogmasystems.myrent.db

class MRCurrencyConversionRate implements Serializable{

     Integer id;
     MRCurrency codeFrom;
     String codeTo;
     Date rateDate;
     Double rate;
     Date modifyDate;
     MRUser createdBy;
     MRUser lastModifyBy;
     Boolean rss;

    static mapping = {
        cache true
        sort "id"

        table name: "currency_conversion_rate"//, schema: "public"
        id column: "id", sqlType: "int4"
        id generator: 'sequence', params: [sequence: 'currency_conversion_rate_seq']
        codeFrom column: "code_from"
        codeTo column: "code_to"
        rateDate column: "rate_date"
        rate column: "rate"
        modifyDate column: "modify_date"
        createdBy  column: "id_user_created" , sqlType: "int4"
        lastModifyBy column: "id_user_last_modify" , sqlType: "int4"
        rss column: "rss"

        version false
    }
    static constraints = {
        codeFrom nullable: true
        codeTo nullable: true
        rateDate nullable: true
        rate nullable: true
        modifyDate nullable: true
        createdBy nullable: true
        lastModifyBy nullable: true
        rss nullable: true

    }
}
