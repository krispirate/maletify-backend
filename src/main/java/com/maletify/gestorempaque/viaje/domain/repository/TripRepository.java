package com.maletify.gestorempaque.viaje.domain.repository;

import com.maletify.gestorempaque.viaje.domain.model.Viaje;
import java.util.Optional;

public interface TripRepository {

    Viaje save(Viaje viaje);

    Optional<Viaje> findById(Long id);
    
    // Podrías añadir findByUsuarioId(Long idUsuario) para listar viajes de un usuario
}
