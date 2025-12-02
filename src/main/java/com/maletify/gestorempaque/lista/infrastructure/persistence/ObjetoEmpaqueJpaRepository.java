package com.maletify.gestorempaque.lista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObjetoEmpaqueJpaRepository 
       extends JpaRepository<ObjetoEmpaqueJpa, Long> {

    Optional<ObjetoEmpaqueJpa> findByNombreObjeto(String nombreObjeto);
}
