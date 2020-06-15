package com.dogmasystems

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

class CustomUserDetails extends GrailsUser {

   final String fullname

   CustomUserDetails(String username, String password, boolean enabled,
                 boolean accountExpired, boolean credentialsExpired,
                 boolean accountLocked,
                 Collection<GrantedAuthority> authorities,
                 long id, String fullname) {
      super(username, password, enabled, accountExpired,
            credentialsExpired, accountLocked, authorities, id)

      this.fullname = fullname
   }
}