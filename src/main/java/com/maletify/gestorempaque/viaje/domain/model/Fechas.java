package com.maletify.gestorempaque.viaje.domain.model;

import lombok.Value;
import java.time.LocalDate;

@Value
public class Fechas {
    
    LocalDate fechaInicio;
    LocalDate fechaFin;
    
    public Fechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
}