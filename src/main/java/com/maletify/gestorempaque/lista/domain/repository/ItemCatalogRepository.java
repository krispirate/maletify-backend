package com.maletify.gestorempaque.lista.domain.repository;

import com.maletify.gestorempaque.lista.domain.model.ObjetoEmpaque;
import java.util.Optional;
import java.util.List;

// Puerto para interactuar con el catálogo de objetos de empaque
public interface ItemCatalogRepository {

    Optional<ObjetoEmpaque> findById(Long id);
    
    Optional<ObjetoEmpaque> findByNombre(String nombre);
    
    List<ObjetoEmpaque> findAll();
    
    ObjetoEmpaque save(ObjetoEmpaque item); // Para precargar el catálogo
}