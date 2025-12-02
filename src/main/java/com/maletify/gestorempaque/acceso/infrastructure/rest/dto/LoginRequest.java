package com.maletify.gestorempaque.acceso.infrastructure.rest.dto; 

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}