package com.maletify.gestorempaque.api.domain.repository;

import com.maletify.gestorempaque.api.domain.model.Clima;
import java.util.Optional;
import java.time.LocalDate;


public interface WeatherCacheRepository {

    Optional<Clima> findByDestinoAndFecha(String destino, LocalDate fecha);

    void save(String destino, LocalDate fecha, Clima clima);
}