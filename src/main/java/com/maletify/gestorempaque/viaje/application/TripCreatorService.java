package com.maletify.gestorempaque.viaje.application;

import com.maletify.gestorempaque.viaje.domain.model.Viaje;
import com.maletify.gestorempaque.viaje.domain.repository.TripRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class TripCreatorService {
    
    private final TripRepository tripRepository;

    public TripCreatorService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // Caso de Uso: Crear un nuevo viaje
    public Viaje create(Long idUsuario, String destino, LocalDate fechaInicio, LocalDate fechaFin, String tipoViaje) {
        
        // 1. El Dominio crea y valida el nuevo Agregado (la lógica de duración está dentro)
        Viaje nuevoViaje = Viaje.createNew(idUsuario, destino, fechaInicio, fechaFin, tipoViaje);

        // 2. La Infraestructura persiste el Agregado
        return tripRepository.save(nuevoViaje);
    }
}