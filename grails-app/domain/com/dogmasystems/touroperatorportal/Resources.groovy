package com.dogmasystems.touroperatorportal

import grails.util.Holders
import it.aessepi.utils.BundleUtils

class Resources implements Serializable{


	String descrizione
	static mapping = {
	    	cache true
			table name: "resources"

			id name: "descrizione", generator: "assigned"

		version false
	}

	public String toString() {
		if (getDescrizione() != null) {
			return message(code: getDescrizione().replaceAll("\\s", "_"));
		} else {
			return new String();
		}
	}
}
