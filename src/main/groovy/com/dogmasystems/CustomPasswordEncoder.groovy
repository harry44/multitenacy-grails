package com.dogmasystems

import org.springframework.security.crypto.password.PasswordEncoder

class CustomPasswordEncoder implements PasswordEncoder {
    @Override
    String encode(CharSequence rawPassword) {
        DESEncrypter dencrypter = new DESEncrypter("utenti_myrent");
        return  dencrypter.encrypt(rawPassword)
    }

    @Override
    boolean matches(CharSequence rawPassword, String encodedPassword) {
        DESEncrypter dencrypter = new DESEncrypter("utenti_myrent");
        if(dencrypter.encrypt(rawPassword)==encodedPassword){
            println dencrypter.encrypt(rawPassword)+"==="+encodedPassword
            return true
        }
        return false
    }
}
