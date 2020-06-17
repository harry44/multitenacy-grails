import com.dogmasystems.UserPasswordEncoderListener
import com.dogmasystems.CustomUserDetailsService
import com.dogmasystems.CustomPasswordEncoder
//import com.dogmasystems.CustomAuthenticationProvider
//import grails.plugin.springsecurity.SpringSecurityUtils
// Place your Spring DSL code here
beans = {
    passwordEncoder(CustomPasswordEncoder)
    userPasswordEncoderListener(UserPasswordEncoderListener)
    userDetailsService(CustomUserDetailsService)
    //daoAuthenticationProvider(CustomAuthenticationProvider)
}
