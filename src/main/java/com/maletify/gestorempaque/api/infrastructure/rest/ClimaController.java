package com.maletify.gestorempaque.api.infrastructure.rest;

import com.maletify.gestorempaque.api.application.WeatherService;
import com.maletify.gestorempaque.api.domain.model.Clima; // <-- ¡IMPORTACIÓN NECESARIA! (Resuelve el error "Clima cannot be resolved")

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

// DTO de respuesta simple para el clima
record ClimaResponse(String destino, String fecha, double temperaturaMedia, String condiciones, double humedadMax) {}

@RestController
@RequestMapping("/api/clima")
public class ClimaController {

    private final WeatherService weatherService;

    public ClimaController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/pronostico")
    public ResponseEntity<ClimaResponse> getClima(
            @RequestParam String destino, 
            @RequestParam String fecha) { // La fecha viene como String desde la URL
        try {
            // 1. Convertir el String de la URL a LocalDate
            LocalDate fechaConsulta = LocalDate.parse(fecha);
            
            // 2. Llamar al servicio con los tipos correctos (String, LocalDate)
            // Esto corrige el error: "getClima is not applicable for the arguments (String, String)"
            Optional<Clima> climaOpt = weatherService.getClima(destino, fechaConsulta);
            
            if (climaOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            Clima clima = climaOpt.get();
            
            // 3. Mapeo al DTO de respuesta
            ClimaResponse response = new ClimaResponse(
                destino,
                fecha,
                clima.getTemperaturaMedia(),
                clima.getCondiciones(),
                clima.getHumedadMax()
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (java.time.format.DateTimeParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Fecha inválida
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}