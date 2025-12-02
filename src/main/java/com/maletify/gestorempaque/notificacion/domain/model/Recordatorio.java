package com.maletify.gestorempaque.notificacion.domain.model;

import lombok.Getter;
import lombok.Builder;
import java.time.LocalDateTime;

@Getter
@Builder
public class Recordatorio {
    
    private Long id;
    private Long idLista;     // Fk a Lista Context
    private Long idUsuario;   // Fk a Access Context
    private LocalDateTime fechaEnvio;
    private String medioEnvio; // Ej: PUSH, EMAIL, SMS
    private String estado;     // Ej: PENDIENTE, ENVIADO, FALLIDO
    private String payloadJson; // Contenido del mensaje (Ej: "Recuerda empacar")
    private LocalDateTime fechaEnvioReal;

    // Método de Dominio: Marcar como enviado
    public void markAsSent() {
        this.estado = "ENVIADO";
        this.fechaEnvioReal = LocalDateTime.now();
    }
}