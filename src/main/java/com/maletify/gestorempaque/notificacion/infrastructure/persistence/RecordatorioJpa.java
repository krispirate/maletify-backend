package com.maletify.gestorempaque.notificacion.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "recordatorio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordatorioJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRecordatorio; // id_recordatorio
    
    @Column(name = "id_lista")
    private Long idLista;
    
    @Column(name = "id_usuario")
    private Long idUsuario;
    
    @Column(name = "fecha_envio_prev") // fecha_envio_prevista
    private LocalDateTime fechaEnvioPrev;
    
    @Column(name = "medio_envio")
    private String medioEnvio;
    
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "payload_json", columnDefinition = "JSON")
    private String payloadJson;
    
    @Column(name = "fecha_envio_real")
    private LocalDateTime fechaEnvioReal;
}