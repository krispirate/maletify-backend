package com.maletify.gestorempaque.notificacion.infrastructure.rest;

import com.maletify.gestorempaque.notificacion.application.ReminderService;
import com.maletify.gestorempaque.notificacion.domain.model.Recordatorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


// DTO de ejemplo para la creación de un recordatorio
record ReminderRequest(Long idLista, Long idUsuario, String fechaHoraEnvio, String medioEnvio, String mensaje) {}

@RestController
@RequestMapping("/api/notificaciones")
public class NotificationController {
    
    private final ReminderService reminderService;

    public NotificationController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping("/crear")
    public ResponseEntity<Long> createReminder(@RequestBody ReminderRequest request) {
        try {
            LocalDateTime fechaEnvio = LocalDateTime.parse(request.fechaHoraEnvio());
            
            Recordatorio recordatorio = reminderService.createReminder(
                request.idLista(),
                request.idUsuario(),
                fechaEnvio,
                request.medioEnvio(),
                request.mensaje());

            return new ResponseEntity<>(recordatorio.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // --- NUEVOS ENDPOINTS ---

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Recordatorio>> getUserNotifications(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(reminderService.getRemindersByUser(idUsuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.noContent().build();
    }
}