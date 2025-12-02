package com.maletify.gestorempaque.lista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DetalleListaRepository extends JpaRepository<DetalleListaJpa, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE DetalleListaJpa d SET d.empacado = :estado WHERE d.idDetalle = :id")
    void actualizarEstado(@Param("id") Long id, @Param("estado") boolean estado);
}