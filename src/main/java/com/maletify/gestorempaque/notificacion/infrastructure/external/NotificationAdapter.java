package com.maletify.gestorempaque.notificacion.infrastructure.external;

import com.maletify.gestorempaque.notificacion.domain.model.Recordatorio;
import com.maletify.gestorempaque.notificacion.domain.services.NotificationSender;
import org.springframework.stereotype.Component;

@Component
public class NotificationAdapter implements NotificationSender {

    @Override
    public boolean send(Recordatorio recordatorio) {
        // Simulación de la lógica de envío a un servicio de terceros (Firebase, Twilio, SendGrid, etc.)
        
        System.out.println("--- ENVIANDO NOTIFICACIÓN ---");
        System.out.println("Destino (Usuario ID): " + recordatorio.getIdUsuario());
        System.out.println("Medio: " + recordatorio.getMedioEnvio());
        System.out.println("Mensaje: " + recordatorio.getPayloadJson());
        System.out.println("------------------------------");
        
        // Simular éxito
        return true; 
    }
}