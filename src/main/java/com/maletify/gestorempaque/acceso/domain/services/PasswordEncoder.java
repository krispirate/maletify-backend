package com.maletify.gestorempaque.acceso.domain.services;


public interface PasswordEncoder {
    String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}