package com.dogmasystems.myrent.db

class MRDamageMovement implements Serializable{

	MRVehicleMovement movements 
	
    static belongsTo = [ damage : MRDamage]

    static mapping = {
        cache true
        version false
        table name:"danni_movimenti"//, schema:'public'
        damage column: 'id_danno', sqlType:"int4"
        movements column: 'id_movimento', sqlType:"int4"
        id composite: ['damage', 'movements']
    }
}
