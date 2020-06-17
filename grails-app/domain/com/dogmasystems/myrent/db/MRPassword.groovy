package com.dogmasystems.myrent.db

import grails.util.Holders

class MRPassword implements Serializable{
     Long id;
     MRUser utente;
     String password;
     Date dataInserimento;
     Integer giorniValidita;
     Boolean ultima;
    static constraints = {
        utente nullable: true
        password nullable: true
        dataInserimento nullable: true
        giorniValidita nullable: true
        ultima nullable: true

    }
static mapping = {
    cache true
    utente cache: true

    sort("id")
    table name: "password"//, schema: "public"
    id generator:'sequence', params:[sequence:'password_id_seq']
    id column: "id"
    password column:"password"

    dataInserimento column:"data_inserimento", sqlType: "date"
    giorniValidita column: "giorni_validita", sqlType: "int4"
    utente column: "id_utente", sqlType: "int4"
    ultima column: "ultima"
    version false

}
    public Date getDataScadenza() {
        if (giorniValidita == 0) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(dataInserimento);
        c.add(Calendar.DATE, giorniValidita);  // number of days to add

        return c.getTime();
    }

    public Boolean isPasswordValid() {
        if (giorniValidita == 0) {
            return true;
        }

        Calendar c = Calendar.getInstance();
        if (c.getTime().after(getDataScadenza())) {
            return false;
        } else {
            return true;
        }
    }
}
