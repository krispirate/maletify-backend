package com.maletify.gestorempaque.viaje.domain.model;

import lombok.Getter;
import lombok.Builder;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
public class Viaje {
    
    private Long id;
    private Long idUsuario;
    private String destino;
    private Fechas fechas;
    private String tipoViaje;
    private int duracionDias; // Se calcula en el dominio

    // Constructor privado para forzar el uso del Builder
    private Viaje(Long id, Long idUsuario, String destino, Fechas fechas, String tipoViaje, int duracionDias) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.destino = destino;
        this.fechas = fechas;
        this.tipoViaje = tipoViaje;
        this.duracionDias = duracionDias;
    }

    // Método de Dominio: Crea un nuevo viaje y calcula su duración
    public static Viaje createNew(Long idUsuario, String destino, LocalDate fechaInicio, LocalDate fechaFin, String tipoViaje) {
        Fechas fechas = new Fechas(fechaInicio, fechaFin);
        int duracion = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;

        return Viaje.builder()
                .idUsuario(idUsuario)
                .destino(destino)
                .fechas(fechas)
                .tipoViaje(tipoViaje)
                .duracionDias(duracion)
                .build();
    }
}
