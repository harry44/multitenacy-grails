package com.dogmasystems.myrent.db

//import java.awt.geom.Arc2D.Double;

class MRDamagePrice implements Serializable{

	MRDamageSeverity damageSeverity
	MRDamageDictionary damageDictionary
	MRGroup groups
	Double minPrice
	Double suggestedPrice
	Double maxPrice
	Double estimatedPrice
	Double maxDiscount

    static constraints = {
        groups nullable: true

        minPrice nullable: true
        maxPrice nullable: true
        suggestedPrice nullable: true
        estimatedPrice nullable: true
        maxDiscount nullable: true
    }

	static mapping = {
		cache true
		damageSeverity cache: true
		damageDictionary cache: true
		groups cache: true

		//table name: "damage_price", schema: "public"
        id generator:'sequence', params:[sequence:'damage_price_seq']
		id column: "id", sqlType: "int4"
		damageDictionary column:"id_damageDictionary", sqlType:"int4"
		damageSeverity column:"id_damageSeverity", sqlType:"int4"
		groups column:"id_gruppi" , sqlType:"int4"
		version false
	}
}
