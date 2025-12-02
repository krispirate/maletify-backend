package com.maletify.gestorempaque.notificacion.application;

import com.maletify.gestorempaque.notificacion.domain.model.Recordatorio;
import com.maletify.gestorempaque.notificacion.domain.repository.ReminderRepository;
import com.maletify.gestorempaque.notificacion.domain.services.NotificationSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {
    
    private final ReminderRepository reminderRepository;
    private final NotificationSender notificationSender;

    public ReminderService(ReminderRepository reminderRepository, NotificationSender notificationSender) {
        this.reminderRepository = reminderRepository;
        this.notificationSender = notificationSender;
    }

    // Caso de Uso: Crear un nuevo recordatorio (desde el Lista Context, por ejemplo)
    public Recordatorio createReminder(Long idLista, Long idUsuario, LocalDateTime fechaEnvio, String medio, String payload) {
        Recordatorio nuevoRecordatorio = Recordatorio.builder()
            .idLista(idLista)
            .idUsuario(idUsuario)
            .fechaEnvio(fechaEnvio)
            .medioEnvio(medio)
            .estado("PENDIENTE")
            .payloadJson(payload)
            .build();
            
        return reminderRepository.save(nuevoRecordatorio);
    }

    // Tarea Programada: Se ejecuta periódicamente para enviar recordatorios pendientes
    // NOTA: Para que esto funcione, DEBES añadir @EnableScheduling a tu clase principal GestorEmpaqueApplication.java
    @Scheduled(fixedRate = 60000) // Se ejecuta cada 60 segundos
    @Transactional
    public void sendPendingReminders() {
        // 1. Buscar recordatorios pendientes que ya deberían haberse enviado
        List<Recordatorio> pendingReminders = reminderRepository.findPendingByDate(LocalDateTime.now());

        for (Recordatorio reminder : pendingReminders) {
            boolean success = notificationSender.send(reminder);
            
            if (success) {
                // 2. Lógica de Dominio: Marcar como enviado
                reminder.markAsSent(); 
                // 3. Persistir el cambio de estado
                reminderRepository.save(reminder); 
            }
            // Aquí se podría implementar lógica de reintento si falla (else)
        }
    }
    // --- NUEVOS MÉTODOS ---

    public List<Recordatorio> getRemindersByUser(Long userId) {
        // Nota: Asegúrate de haber agregado el método findByUserId en tu interfaz ReminderRepository
        // Si tu repositorio es la clase concreta JpaReminderRepository, puedes hacer un cast o agregarlo a la interfaz.
        // Aquí asumimos que JpaReminderRepository lo tiene.
        return ((com.maletify.gestorempaque.notificacion.infrastructure.persistence.JpaReminderRepository) reminderRepository)
                .findByUserId(userId);
    }

    public void deleteReminder(Long id) {
        ((com.maletify.gestorempaque.notificacion.infrastructure.persistence.JpaReminderRepository) reminderRepository)
                .deleteById(id);
    }
}