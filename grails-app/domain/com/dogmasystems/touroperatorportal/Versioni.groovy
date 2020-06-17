package com.dogmasystems.touroperatorportal

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class Versioni implements Serializable {

	String name
	Integer major
	Integer minor
	Integer patch

	int hashCode() {
		def builder = new HashCodeBuilder()
		builder.append name
		builder.append major
		builder.append minor
		builder.append patch
		builder.toHashCode()
	}

	boolean equals(other) {
		if (other == null) return false
		def builder = new EqualsBuilder()
		builder.append name, other.name
		builder.append major, other.major
		builder.append minor, other.minor
		builder.append patch, other.patch
		builder.isEquals()
	}

	static mapping = {
		//datasource 'myrent'
		table name: "versioni"//, schema: "public"
		id composite: ["name", "major", "minor", "patch"]
		version false
	}
}
