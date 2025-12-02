package com.maletify.gestorempaque.api.domain.services;

import com.maletify.gestorempaque.api.domain.model.Clima;
import com.maletify.gestorempaque.api.domain.model.ClimaRequest;
import java.util.Optional;

// Puerto que modela la interacción con un servicio de terceros
public interface ExternalWeatherProvider {

    Optional<Clima> getForecast(ClimaRequest request);
}