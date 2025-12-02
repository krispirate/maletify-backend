package com.maletify.gestorempaque.api.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "clima_cache")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClimaCacheJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClima;
    
    @Column(name = "destino_varchar")
    private String destino;
    
    @Column(name = "fecha_consulta_date")
    private LocalDate fecha;
    
    @Column(name = "datos_clima_json", columnDefinition = "JSON")
    private String datosClimaJson; 
    
    @Column(name = "fecha_creacion_ts")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fuente_varchar")
    private String fuente;
}