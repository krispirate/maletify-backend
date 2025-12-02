package com.maletify.gestorempaque.acceso.infrastructure.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data // DTOs simples usan @Data
@AllArgsConstructor
public class RegisterRequest {
    private String nombre;
    private String email;
    private String password;
}