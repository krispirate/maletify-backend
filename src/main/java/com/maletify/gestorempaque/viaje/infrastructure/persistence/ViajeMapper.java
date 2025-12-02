package com.maletify.gestorempaque.viaje.infrastructure.persistence;

import com.maletify.gestorempaque.viaje.domain.model.Viaje;
import com.maletify.gestorempaque.viaje.domain.model.Fechas;
import java.time.temporal.ChronoUnit;

public class ViajeMapper {

    public static Viaje toDomain(ViajeJpa entity) {
        Fechas fechas = new Fechas(entity.getFechaInicio(), entity.getFechaFin());
        // Recalcula la duración al reconstruir el objeto de dominio
        int duracion = (int) ChronoUnit.DAYS.between(fechas.getFechaInicio(), fechas.getFechaFin()) + 1;

        return Viaje.builder()
                .id(entity.getIdViaje())
                .idUsuario(entity.getIdUsuario())
                .destino(entity.getDestino())
                .fechas(fechas)
                .tipoViaje(entity.getTipoViaje())
                .duracionDias(duracion)
                .build();
    }

    public static ViajeJpa toEntity(Viaje domain) {
        ViajeJpa entity = new ViajeJpa();
        entity.setIdViaje(domain.getId());
        entity.setIdUsuario(domain.getIdUsuario());
        entity.setDestino(domain.getDestino());
        entity.setFechaInicio(domain.getFechas().getFechaInicio());
        entity.setFechaFin(domain.getFechas().getFechaFin());
        entity.setTipoViaje(domain.getTipoViaje());
        return entity;
    }
}
