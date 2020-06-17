package com.dogmasystems.myrent.db

import com.dogmasystems.touroperatorportal.Listini
import grails.util.Holders
import org.apache.commons.lang.builder.CompareToBuilder
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

//import com.dogmasystems.utils.db.PersistentInstance;

class MRGroup implements Serializable{

	String nationalCode;
	String internationalCode;
	String description;
	String webDescription;
	Double franchigiaDanni;
	Double franchigiaFurto;
	//Double franchigiaPerditaChiavi;
	Boolean isFoto1Set;
	Boolean isSpecial;
	Boolean isDisabled;
	//byte[] foto1;
	MRMacroClass macroClass;
    String wireframeImage
	// Added field
	Integer proKey
	String externalID


	public String toString() {
		return (getNationalCode() != null ? getNationalCode() : "") + //NOI18N
				" / " + //NOI18N
				(getInternationalCode() != null ? getInternationalCode() : "") + //NOI18N
				" / " + //NOI18N
				(getDescription() != null ? getDescription() : ""); //NOI18N
	}

	public boolean equals(Object other) {
		if (!(other instanceof MRGroup)) {
			return false;
		}
		MRGroup castOther = (MRGroup) other;
		return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}


	public int compareTo(MRGroup o) {
		if (o == null) {
			return 1;
		} else {
			return new CompareToBuilder().append(getNationalCode(), o.getNationalCode()).
					append(getInternationalCode(), o.getInternationalCode()).
					append(getDescription(), getDescription()).
					append(getId(), o.getId()).
					toComparison();
		}
	}


	static belongsTo = [Listini]
	static hasMany = [  parcoVeicolis: MRVehicle,
						grouplistinis: Listini]
	static mapping = {
		cache true
		table name: "gruppi"//, schema: "public"
		id generator:'sequence', params:[sequence:'gruppi_seq']
		id column: "id", sqlType: "int4"
		isFoto1Set column:"is_foto1_set"
		nationalCode column:"codice_nazionale"
		internationalCode column:"codice_internazionale"
		description column:"descrizione"
		wireframeImage column:"wireframe_image"
		franchigiaDanni column:"franchigia_danni"
		franchigiaFurto column: "franchigia_furto"
//		franchigiaChiavi nullable: true, scale: 17
//		isFoto1Set nullable: true
//		foto1 nullable: true
		isSpecial column:"speciale"
		macroClass column: "macro_class_id", sqlType: "int4"
		webDescription column: "web_descrizione"
		grouplistinis joinTable:[name:'listini_gruppi', key:'mrgroup_id']
		proKey column: "prokey"
		version false
	}

	static constraints = {
		nationalCode nullable: true, unique: true
		internationalCode nullable: true
		description nullable: true
		wireframeImage nullable:true
		webDescription nullable: true
		macroClass nullable:true
		franchigiaDanni nullable: true, scale: 17
		franchigiaFurto nullable: true, scale: 17
//		franchigiaChiavi nullable: true, scale: 17
		isFoto1Set nullable: true
//		foto1 nullable: true
		isSpecial nullable: true
		isDisabled nullable: true,default:false
		proKey nullable: true
		externalID nullable: true

	}

}
