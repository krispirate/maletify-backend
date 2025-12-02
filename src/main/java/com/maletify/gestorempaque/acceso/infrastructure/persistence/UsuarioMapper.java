package com.maletify.gestorempaque.acceso.infrastructure.persistence;

import com.maletify.gestorempaque.acceso.domain.model.Usuario;

public class UsuarioMapper {

    // DE LA BASE DE DATOS -> A LA APP
    public static Usuario toDomain(UsuarioJpa entity) {
        return Usuario.builder()
                .id(entity.getIdUsuario())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .passwordHash(entity.getContrasenaHash())
                .paisResidencia(entity.getPaisResidencia())
                .rol(entity.getRol())
                .preferencias(entity.getPreferencias())
                
                // --- 1. CORRECCIÓN: AGREGAR ESTO ---
                .plan(entity.getPlan()) // <--- ¡Sin esto, React nunca sabrá que es PLUS!
                // -----------------------------------
                
                .build();
    }

    // DE LA APP -> A LA BASE DE DATOS
    public static UsuarioJpa toEntity(Usuario domain) {
        UsuarioJpa entity = new UsuarioJpa();
        entity.setIdUsuario(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setEmail(domain.getEmail());
        entity.setContrasenaHash(domain.getPasswordHash());
        entity.setPaisResidencia(domain.getPaisResidencia());
        entity.setRol(domain.getRol());
        entity.setPreferencias(domain.getPreferencias());

        // --- 2. CORRECCIÓN: NO LO FUERCES A FREE ---
        // BORRA ESTO: entity.setPlan("FREE");  <-- ESTO ERA EL ERROR GRAVE
        
        // PON ESTO EN SU LUGAR:
        entity.setPlan(domain.getPlan()); // Guarda el plan que tenga el usuario actualmente
        // -------------------------------------------

        return entity;
    }
}