package com.dogmasystems.myrent.db

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class MRUserLocation implements Serializable {

	//Integer idUser
	//Integer index


	/*int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append idUser
		builder.append index
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append idUser, other.idUser
		builder.append index, other.index
		builder.isEquals()
	}*/

	static belongsTo = [idSede:MRLocation,idUser: MRUser]

	static mapping = {
		cache true
		idSede cache: true
		idUser cache: true
		table name: "sedi_user"//, schema: "public"
		id composite: ["idSede", "idUser"]
		idSede column:"id_sede"//,sqlType:"int4"
		idUser column:"id_user"//,sqlType:"int4"
		//users column:"id_user", sqlType:"int4"
		//location column:"id_sede", sqlType:"int4"
		version false
	}
}
