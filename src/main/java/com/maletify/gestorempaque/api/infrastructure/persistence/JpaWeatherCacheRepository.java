package com.maletify.gestorempaque.api.infrastructure.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maletify.gestorempaque.api.domain.model.Clima;
import com.maletify.gestorempaque.api.domain.repository.WeatherCacheRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

interface JpaClimaCacheSpringRepository extends JpaRepository<ClimaCacheJpa, Long> {
    Optional<ClimaCacheJpa> findByDestinoAndFecha(String destino, LocalDate fecha);
}

@Repository
public class JpaWeatherCacheRepository implements WeatherCacheRepository {

    private final JpaClimaCacheSpringRepository jpaRepository;
    private final ObjectMapper objectMapper; 

    public JpaWeatherCacheRepository(JpaClimaCacheSpringRepository jpaRepository, ObjectMapper objectMapper) {
        this.jpaRepository = jpaRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<Clima> findByDestinoAndFecha(String destino, LocalDate fecha) {
        return jpaRepository.findByDestinoAndFecha(destino, fecha)
                .flatMap(entity -> {
                    try {
                        Clima clima = objectMapper.readValue(entity.getDatosClimaJson(), Clima.class);
                        return Optional.of(clima);
                    } catch (JsonProcessingException e) {
                        return Optional.empty();
                    }
                });
    }

    @Override
    public void save(String destino, LocalDate fecha, Clima clima) {
        try {
            ClimaCacheJpa entity = new ClimaCacheJpa();
            entity.setDestino(destino);
            entity.setFecha(fecha);
            entity.setFuente("OPEN_WEATHER"); 
            entity.setFechaCreacion(LocalDateTime.now());

            entity.setDatosClimaJson(objectMapper.writeValueAsString(clima)); 
            
            jpaRepository.save(entity);
        } catch (JsonProcessingException e) {
        
        }
    }
}