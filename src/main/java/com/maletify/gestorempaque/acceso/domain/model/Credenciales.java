package com.maletify.gestorempaque.acceso.domain.model;

import lombok.Value; 

@Value 
public class Credenciales {
    
    String email;
    String rawPassword;

    public Credenciales(String email, String rawPassword) {
        if (email == null || email.isEmpty() || rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Email y contraseña no pueden estar vacíos.");
        }
        this.email = email.toLowerCase();
        this.rawPassword = rawPassword;
    }
}