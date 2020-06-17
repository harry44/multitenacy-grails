package com.dogmasystems.myrent.db

import it.aessepi.utils.BundleUtils

class TaxRegime implements Serializable {

    String description;
    String code;

//    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    static constraints = {
        description nullable: true
        code nullable: true
    }

    static mapping = {
        cache true
        table name: "tax_regime"//, schema: "public"
        id generator:'sequence', params:[sequence:'tax_regime_seq']
        id column: "id", sqlType: "int4"
        description column:"description"
        code column: "codice"
        version false
    }

    public String toString() {
        if (getCode() != null) {
            return getCode()
        } else {
            return new String();
        }
    }

}
