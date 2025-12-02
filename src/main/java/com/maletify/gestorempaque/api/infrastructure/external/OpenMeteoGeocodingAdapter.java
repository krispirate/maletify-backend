package com.maletify.gestorempaque.api.infrastructure.external;

import com.maletify.gestorempaque.api.domain.services.GeocodingProvider;
import com.maletify.gestorempaque.api.domain.model.Coordinates;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Component
public class OpenMeteoGeocodingAdapter implements GeocodingProvider {

    private final WebClient webClient;
    private static final String BASE_URL = "https://geocoding-api.open-meteo.com/v1/search";

    public OpenMeteoGeocodingAdapter() {
        this.webClient = WebClient.create(BASE_URL);
    }

    @Override
    public Optional<Coordinates> getCoordinates(String city, String country) {
        String name = city + (country != null ? ", " + country : "");

        try {
            GeocodingResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .queryParam("name", name)
                    .queryParam("count", 1) 
                    .queryParam("language", "es")
                    .build())
                .retrieve()
                .bodyToMono(GeocodingResponse.class)
                .block();

            if (response != null && response.results() != null && !response.results().isEmpty()) {
                Result result = response.results().get(0);
                return Optional.of(new Coordinates(result.latitude(), result.longitude()));
            }
        } catch (Exception e) {
            System.err.println("Error al obtener coordenadas para " + name + ": " + e.getMessage());
        }

        return Optional.empty();
    }
    

    record GeocodingResponse(List<Result> results) {}
    record Result(double latitude, double longitude, String name) {}
}