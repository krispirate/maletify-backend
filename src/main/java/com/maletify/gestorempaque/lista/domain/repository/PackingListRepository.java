package com.maletify.gestorempaque.lista.domain.repository;

import com.maletify.gestorempaque.lista.domain.model.ListaEmpaque;
import java.util.Optional;

public interface PackingListRepository {

    ListaEmpaque save(ListaEmpaque lista);

    Optional<ListaEmpaque> findById(Long id);
    
    Optional<ListaEmpaque> findByViajeId(Long idViaje);
    
}
