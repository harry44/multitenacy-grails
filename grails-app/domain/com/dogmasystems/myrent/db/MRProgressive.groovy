package com.dogmasystems.myrent.db

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class MRProgressive implements Serializable {

	//Integer idNumerazione
	Integer year
	Integer progressive

	/*int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append idNumerazione
		builder.append anno
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append idNumerazione, other.idNumerazione
		builder.append anno, other.anno
		builder.isEquals()
	}*/

	static belongsTo = [idNumerazione:MREnumeration]

	static mapping = {
		table name: "progressivi"//, schema: "public"
		id composite: ["idNumerazione", "year"]
		idNumerazione column:"id_numerazione", sqlType:"int4"
		year column:"anno"
		progressive column:"progressivo"
		version false
	}

	static constraints = {
		progressive nullable: true
	}
}
