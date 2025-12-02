package com.maletify.gestorempaque.acceso.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Esta interfaz extiende JpaRepository, lo que nos da findAll(), count(), delete(), etc. GRATIS.
@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpa, Long> {
    // No necesitas escribir nada aquí, Spring lo hace todo mágicamente.
}