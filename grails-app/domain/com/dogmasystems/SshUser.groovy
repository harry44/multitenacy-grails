package com.dogmasystems

import java.util.Date;

class SshUser implements Serializable{
	
	String username, password, hostname
	
	static belongsTo = [user:User]
	
	User user
	
	static auditable = true
	Date dateCreated, lastUpdated
	
	static mapping = {
		id generator:'sequence', params:[sequence:'sshUser_seq']
	}

	String getLabel() {
		username
	}
    static constraints = {
    }
}
