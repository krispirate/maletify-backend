package com.maletify.gestorempaque.lista.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaObjetoEmpaqueRepository extends JpaRepository<ObjetoEmpaqueJpa, Long> {

    Optional<ObjetoEmpaqueJpa> findByNombreObjeto(String nombreObjeto);
}
