package com.maletify.gestorempaque.lista.infrastructure.recommendation;

import com.maletify.gestorempaque.lista.domain.model.DetalleLista;
import com.maletify.gestorempaque.lista.domain.model.ObjetoEmpaque;
import com.maletify.gestorempaque.lista.domain.services.RecommendationEngine;
import com.maletify.gestorempaque.lista.domain.repository.ItemCatalogRepository;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class RecommendationEngineAdapter implements RecommendationEngine {

    private final ItemCatalogRepository itemCatalogRepository;

    public RecommendationEngineAdapter(ItemCatalogRepository itemCatalogRepository) {
        this.itemCatalogRepository = itemCatalogRepository;
    }

    @Override
    public List<DetalleLista> generateRecommendations(
            Long idViaje, 
            Long idUsuario, 
            String clasificacionClima, 
            int duracionDias) {
        
        List<DetalleLista> recomendaciones = new ArrayList<>();
        
        // Simulación de la lógica del motor de reglas (Domain Service)

        // 1. Reglas de Ropa (Basadas en Clima y Duración)
        if (clasificacionClima.equals("FRIO_EXTREMO")) {
            ObjetoEmpaque abrigo = itemCatalogRepository.findByNombre("Abrigo Termico").orElse(null);
            if (abrigo != null) {
                 // 1 abrigo es obligatorio en frío extremo
                recomendaciones.add(DetalleLista.builder().item(abrigo).cantidad(1).obligatorio(true).build());
            }
            // Cantidad de ropa por día
            ObjetoEmpaque medias = itemCatalogRepository.findByNombre("Medias").orElse(null);
            if (medias != null) {
                recomendaciones.add(DetalleLista.builder().item(medias).cantidad(duracionDias + 1).obligatorio(false).build());
            }

        } else if (clasificacionClima.equals("CALIDO")) {
            ObjetoEmpaque trajeBano = itemCatalogRepository.findByNombre("Traje de Baño").orElse(null);
            if (trajeBano != null) {
                recomendaciones.add(DetalleLista.builder().item(trajeBano).cantidad(1).obligatorio(false).build());
            }
        }

        // 2. Reglas Estándar (No importa el clima)
        ObjetoEmpaque pasaporte = itemCatalogRepository.findByNombre("Pasaporte/DNI").orElse(null);
        if (pasaporte != null) {
            recomendaciones.add(DetalleLista.builder().item(pasaporte).cantidad(1).obligatorio(true).build());
        }
        
        // 3. Reglas basadas en Preferencias de Usuario (Mocked)
        // Lógica: Si el usuario tiene 'Tecnología' en sus preferencias, sugiere un cargador.
        // Esto requeriría llamar al Access Context o tener las preferencias en cache.
        ObjetoEmpaque cargador = itemCatalogRepository.findByNombre("Cargador de Móvil").orElse(null);
        if (cargador != null) {
            recomendaciones.add(DetalleLista.builder().item(cargador).cantidad(1).obligatorio(false).build());
        }
        
        return recomendaciones;
    }
}
