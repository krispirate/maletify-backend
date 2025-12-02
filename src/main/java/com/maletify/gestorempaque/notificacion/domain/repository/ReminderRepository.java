package com.maletify.gestorempaque.notificacion.domain.repository;

import com.maletify.gestorempaque.notificacion.domain.model.Recordatorio;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface ReminderRepository {

    Recordatorio save(Recordatorio recordatorio);

    Optional<Recordatorio> findById(Long id);
    
    // Consulta clave para el programador (Scheduler)
    List<Recordatorio> findPendingByDate(LocalDateTime maxDate);
}