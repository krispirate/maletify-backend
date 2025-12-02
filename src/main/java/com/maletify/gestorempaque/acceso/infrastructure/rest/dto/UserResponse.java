package com.maletify.gestorempaque.acceso.infrastructure.rest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor; // <-- ¡CRUCIAL!
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor // <-- Genera el constructor de todos los campos
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String nombre;
    private String email;
    private String rol; // El cuarto parámetro que estás pasando
}