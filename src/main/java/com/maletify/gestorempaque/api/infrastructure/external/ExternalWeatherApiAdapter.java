package com.maletify.gestorempaque.api.infrastructure.external;

import com.maletify.gestorempaque.api.domain.model.Clima;
import com.maletify.gestorempaque.api.domain.model.ClimaRequest;
import com.maletify.gestorempaque.api.domain.services.ExternalWeatherProvider;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class ExternalWeatherApiAdapter implements ExternalWeatherProvider {

    private final WebClient webClient;
    
    // Configura la base URL de la API de clima
    private static final String BASE_URL = "https://api.open-meteo.com/v1/forecast";

    public ExternalWeatherApiAdapter() {
        this.webClient = WebClient.create(BASE_URL);
    }

    @Override
    public Optional<Clima> getForecast(ClimaRequest request) {
        
        double latitude = request.getLatitude();
        double longitude = request.getLongitude();
        
        // Parámetros para la llamada a Open-Meteo
        LocalDate startDate = request.getFecha();
        LocalDate endDate = request.getFecha();

        try {
            // Construye la URL de la API
            WeatherApiResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .queryParam("latitude", latitude)
                    .queryParam("longitude", longitude)
                    
                    .queryParam("daily", "temperature_2m_max,temperature_2m_min,relative_humidity_2m_max,weather_code")
                    .queryParam("start_date", startDate.toString())
                    .queryParam("end_date", endDate.toString())
                    .queryParam("timezone", "auto")
                    .build())
                .retrieve()
                .bodyToMono(WeatherApiResponse.class) 
                .block(); 


            if (response != null && response.daily() != null && response.daily().temperature_2m_max() != null && !response.daily().temperature_2m_max().isEmpty()) {
                
            
                double tempMax = response.daily().temperature_2m_max().get(0);
                double tempMin = response.daily().temperature_2m_min().get(0);
                double humedadMax = response.daily().relative_humidity_2m_max().get(0);
                int weatherCode = response.daily().weather_code().get(0);

                double tempMedia = (tempMax + tempMin) / 2.0;
                String condiciones = mapWeatherCodeToConditions(weatherCode);

                Clima clima = new Clima(tempMedia, condiciones, humedadMax);
                return Optional.of(clima);
            }
        } catch (Exception e) {
            System.err.println("Error al llamar a la API de Clima para lat/lon: " + latitude + "/" + longitude + ". Error: " + e.getMessage());
        }

        return Optional.empty();
    }
    
    private String mapWeatherCodeToConditions(int code) {
        if (code <= 1) return "SOLEADO";
        if (code >= 51 && code <= 67) return "LLUVIOSO";
        if (code >= 71 && code <= 77) return "NEVANDO";
        return "NUBLADO";
    }

    record WeatherApiResponse(Daily daily) {}
    record Daily(
        @com.fasterxml.jackson.annotation.JsonProperty("temperature_2m_max") java.util.List<Double> temperature_2m_max,
        @com.fasterxml.jackson.annotation.JsonProperty("temperature_2m_min") java.util.List<Double> temperature_2m_min,
        @com.fasterxml.jackson.annotation.JsonProperty("relative_humidity_2m_max") java.util.List<Double> relative_humidity_2m_max,
        @com.fasterxml.jackson.annotation.JsonProperty("weather_code") java.util.List<Integer> weather_code
    ) {}
}