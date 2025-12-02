package com.maletify.gestorempaque.lista.domain.services;

import com.maletify.gestorempaque.lista.domain.model.DetalleLista;
import java.util.List;

// Puerto que define la funcionalidad clave del motor de sugerencias
public interface RecommendationEngine {

    // Llama a las reglas de negocio basadas en el viaje, clima y preferencias
    List<DetalleLista> generateRecommendations(
        Long idViaje, 
        Long idUsuario, 
        String clasificacionClima, 
        int duracionDias);
}