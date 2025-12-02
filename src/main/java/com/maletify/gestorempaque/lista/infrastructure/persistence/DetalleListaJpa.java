package com.maletify.gestorempaque.lista.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime; // Incluir por si acaso

@Entity
@Table(name = "detalle_lista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleListaJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    // Columna de Clave Foránea a lista_empaque
    @Column(name = "id_lista", nullable = false)
    private Long idLista; 
    
    // Columna de Clave Foránea a objeto (ítem)
    @Column(name = "id_objeto", nullable = false)
    private Long idObjeto; 

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "obligatorio", nullable = false)
    private boolean obligatorio;
    
    @Column(name = "agregado_manual", nullable = false)
    private boolean agregadoManual;

    @Column(name = "empacado", nullable = false)
    private boolean empacado;
    
    @Column(name = "timestamp_creacion")
    private LocalDateTime timestampCreacion;
}