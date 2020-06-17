package com.dogmasystems.myrent.db

import java.io.Serializable;

class MRSourceUser implements Serializable{

    static belongsTo = [fonti : MRReservationSource, user : MRUser]

    static mapping = {
        cache true
        version false
        fonti cache: true
        user cache: true
        table name: "fonti_users"//, schema: "public"
        user column: 'id_user', sqlType:"int4"
        fonti column: 'id_fonte', sqlType:"int4"
        id composite: ['user', 'fonti']
    }
}
