package com.dogmasystems.myrent.db

import org.apache.commons.lang.builder.CompareToBuilder

class MRAgreement implements Comparable<MRAgreement>{

     String description
     Double discount
     Date validFrom
     Date validTo
     Boolean isValid

    static mapping = {
        cache true
        table name: "agreement"
        id generator: 'sequence', params: [sequence: 'agreement_seq']
        id column: "id", sqlType: "int4"
        description column: "description"
        discount column: "discount"
        validFrom column: "validFrom"
        validTo column: "validTo"
        isValid column: "isValid"
        version false
    }

    static constraints = {
        description(nullable: true)
        discount(nullable: true)
        validFrom(nullable: true)
        validTo(nullable: true)
        isValid(nullable: true)
    }

    @Override
    public int compareTo(MRAgreement o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }
}
