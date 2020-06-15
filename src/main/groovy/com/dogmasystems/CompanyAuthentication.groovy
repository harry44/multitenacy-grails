package com.dogmasystems;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class CompanyAuthentication extends UsernamePasswordAuthenticationToken{
	private static final long serialVersionUID = 1
	
	final String tenant
	
	CompanyAuthentication(principal, credentials, String company) {
		super(principal, credentials)
		tenant = company
	}

	CompanyAuthentication(principal, credentials, String company, Collection authorities) {
		super(principal, credentials, authorities)
		tenant = company
	}
}
