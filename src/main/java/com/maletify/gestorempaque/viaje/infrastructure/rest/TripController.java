package com.maletify.gestorempaque.viaje.infrastructure.rest;

import com.maletify.gestorempaque.viaje.application.TripCreatorService;
import com.maletify.gestorempaque.viaje.domain.model.Viaje;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/viajes")
public class TripController {

    private final TripCreatorService tripCreatorService;

    public TripController(TripCreatorService tripCreatorService) {
        this.tripCreatorService = tripCreatorService;
    }

    @PostMapping("/crear")
    // Aquí se usaría un DTO, pero lo simplificamos a parámetros para el ejemplo
    public ResponseEntity<Long> createTrip(
            @RequestParam Long userId,
            @RequestParam String destino,
            @RequestParam String fechaInicio, // Se asume formato y conversión a LocalDate en la vida real
            @RequestParam String fechaFin,
            @RequestParam String tipoViaje) {
        try {
            // Ejemplo simple de conversión
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);

            Viaje nuevoViaje = tripCreatorService.create(userId, destino, inicio, fin, tipoViaje);
            
            return new ResponseEntity<>(nuevoViaje.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}