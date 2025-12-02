package com.maletify.gestorempaque.lista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ListaEmpaqueRepository extends JpaRepository<ListaEmpaqueJpa, Long> {
    
    // Busca todas las listas donde el usuario propietario tenga el ID que le pasamos
    List<ListaEmpaqueJpa> findAllByUsuarioPropietario_IdUsuario(Long idUsuario);
    

    // NUEVO: Cuenta listas creadas por un usuario DESPUÉS de una fecha específica
    long countByUsuarioPropietario_IdUsuarioAndFechaCreacionAfter(Long idUsuario, LocalDateTime fechaLimite);
    long countByUsuarioPropietario_IdUsuario(Long idUsuario);
}