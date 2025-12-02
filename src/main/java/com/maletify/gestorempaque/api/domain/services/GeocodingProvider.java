package com.maletify.gestorempaque.api.domain.services;

import com.maletify.gestorempaque.api.domain.model.Coordinates; 
import java.util.Optional;

// Puerto para el servicio de Geocodificación
public interface GeocodingProvider {
    Optional<Coordinates> getCoordinates(String city, String country);
}