package com.dogmasystems

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class CustomAuthenticationProvider implements AuthenticationProvider{
    def customUserDetailsService
    @Override
    Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        Object credentials = authentication.getCredentials();
        if (!(credentials instanceof String)) {
            return null;
        }
        MRUser user=customUserDetailsService.loadUserByUsername(name)
        GrailsUser userDetails=new CustomUserDetails(user.username,user.password,user.enabled,user.getAccountExpired(),user.passwordExpired,user.accountLocked,user.authorities,user.id)
        if (!userDetails) {
            throw new BadCredentialsException("Authentication failed for " + name);
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(userDetails.role));
        Authentication auth = new CompanyAuthentication(name, credentials, grantedAuthorities);
        return auth
    }

    @Override
    boolean supports(Class<?> authentication) {
        return false
    }
}
