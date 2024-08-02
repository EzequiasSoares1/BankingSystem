package com.accenture.academico.bankingsystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CryptService {
    @Autowired
    private  PasswordEncoder passwordEncoder;

    public String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    public boolean matchesPassword(String password, String passwordAuth){
        return  passwordEncoder.matches(password, passwordAuth);
    }
}

