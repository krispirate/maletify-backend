package com.maletify.gestorempaque.viaje.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "viaje")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViajeJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viaje")
    private Long idViaje;
    
    @Column(name = "id_usuario")
    private Long idUsuario;
    
    // CORRECCIÓN: El nombre de la columna en DB es 'destino', no 'destino_varchar'
    @Column(name = "destino") 
    private String destino;
    
    // CORRECCIÓN: Verifica si en DB es 'fecha_inicio' o 'fecha_inicio_date'
    // Según tu script SQL es 'fecha_inicio'
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    
    // CORRECCIÓN: Según tu SQL es 'fecha_fin'
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    
    // CORRECCIÓN: Según tu SQL es 'tipo_viaje'
    @Column(name = "tipo_viaje")
    private String tipoViaje;
}