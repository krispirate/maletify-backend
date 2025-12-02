package com.maletify.gestorempaque.api.application;

import com.maletify.gestorempaque.api.domain.model.Clima;
import com.maletify.gestorempaque.api.domain.model.ClimaRequest;
import com.maletify.gestorempaque.api.domain.repository.WeatherCacheRepository;
import com.maletify.gestorempaque.api.domain.services.ExternalWeatherProvider;
import com.maletify.gestorempaque.api.domain.services.GeocodingProvider; 
import com.maletify.gestorempaque.api.domain.model.Coordinates; 

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class WeatherService {

    private final WeatherCacheRepository cacheRepository;
    private final ExternalWeatherProvider externalProvider;
    private final GeocodingProvider geocodingProvider; 


    public WeatherService(WeatherCacheRepository cacheRepository, 
                          ExternalWeatherProvider externalProvider,
                          GeocodingProvider geocodingProvider) {
        this.cacheRepository = cacheRepository;
        this.externalProvider = externalProvider;
        this.geocodingProvider = geocodingProvider;
    }

    public Optional<Clima> getClima(String destino, java.time.LocalDate fecha) {
        // 1. Intentar obtener desde la caché
        Optional<Clima> cachedClima = cacheRepository.findByDestinoAndFecha(destino, fecha);
        if (cachedClima.isPresent()) {
            return cachedClima;
        }

        // 2. Obtener coordenadas reales
        Optional<Coordinates> coordsOpt = geocodingProvider.getCoordinates(destino, null); 
        if (coordsOpt.isEmpty()) {
            return Optional.empty(); 
        }
        Coordinates coords = coordsOpt.get();

        // 3. Obtener desde el proveedor externo
        ClimaRequest request = new ClimaRequest(coords.latitude(), coords.longitude(), destino, fecha);
        Optional<Clima> climaOpt = externalProvider.getForecast(request);

        // 4. Guardar en la caché si es exitoso
        climaOpt.ifPresent(clima -> cacheRepository.save(destino, fecha, clima));

        return climaOpt;
    }
}