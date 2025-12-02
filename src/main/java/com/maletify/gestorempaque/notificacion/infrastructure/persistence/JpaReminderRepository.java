package com.maletify.gestorempaque.notificacion.infrastructure.persistence;

import com.maletify.gestorempaque.notificacion.domain.model.Recordatorio;
import com.maletify.gestorempaque.notificacion.domain.repository.ReminderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Interfaz interna de Spring Data JPA
interface JpaReminderSpringRepository extends JpaRepository<RecordatorioJpa, Long> {
    // Buscar pendientes
    List<RecordatorioJpa> findByEstadoAndFechaEnvioPrevBefore(String estado, LocalDateTime maxDate);
    
    // NUEVO: Buscar por usuario ordenado por fecha (más reciente primero)
    List<RecordatorioJpa> findByIdUsuarioOrderByFechaEnvioPrevDesc(Long idUsuario);
}

// Mapper local para convertir entre JPA y Dominio
class ReminderMapper {
    public static Recordatorio toDomain(RecordatorioJpa jpa) {
        return Recordatorio.builder()
            .id(jpa.getIdRecordatorio())
            .idLista(jpa.getIdLista())
            .idUsuario(jpa.getIdUsuario())
            .fechaEnvio(jpa.getFechaEnvioPrev())
            .medioEnvio(jpa.getMedioEnvio())
            .estado(jpa.getEstado())
            .payloadJson(jpa.getPayloadJson())
            .fechaEnvioReal(jpa.getFechaEnvioReal())
            .build();
    }
    
    public static RecordatorioJpa toJpa(Recordatorio domain) {
        RecordatorioJpa jpa = new RecordatorioJpa();
        jpa.setIdRecordatorio(domain.getId());
        jpa.setIdLista(domain.getIdLista());
        jpa.setIdUsuario(domain.getIdUsuario());
        jpa.setFechaEnvioPrev(domain.getFechaEnvio());
        jpa.setMedioEnvio(domain.getMedioEnvio());
        jpa.setEstado(domain.getEstado());
        jpa.setPayloadJson(domain.getPayloadJson());
        jpa.setFechaEnvioReal(domain.getFechaEnvioReal());
        return jpa;
    }
}

@Repository
public class JpaReminderRepository implements ReminderRepository {

    private final JpaReminderSpringRepository jpaRepository;

    public JpaReminderRepository(JpaReminderSpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Recordatorio save(Recordatorio recordatorio) {
        RecordatorioJpa jpa = ReminderMapper.toJpa(recordatorio);
        RecordatorioJpa savedJpa = jpaRepository.save(jpa);
        return ReminderMapper.toDomain(savedJpa);
    }

    @Override
    public Optional<Recordatorio> findById(Long id) {
        return jpaRepository.findById(id).map(ReminderMapper::toDomain);
    }

    @Override
    public List<Recordatorio> findPendingByDate(LocalDateTime maxDate) {
        return jpaRepository.findByEstadoAndFechaEnvioPrevBefore("PENDIENTE", maxDate)
            .stream()
            .map(ReminderMapper::toDomain)
            .collect(Collectors.toList());
    }

    // --- MÉTODOS NUEVOS PARA SOPORTAR EL FRONTEND ---

    // Buscar notificaciones de un usuario específico
    public List<Recordatorio> findByUserId(Long userId) {
        return jpaRepository.findByIdUsuarioOrderByFechaEnvioPrevDesc(userId)
                .stream()
                .map(ReminderMapper::toDomain)
                .collect(Collectors.toList());
    }

    // Eliminar una notificación
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}