package com.maletify.gestorempaque.lista.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "objeto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjetoEmpaqueJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_objeto")
    private Long idObjeto;

    @Column(name = "nombre", nullable = false)
    private String nombreObjeto;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "sugerido_sistema", nullable = false)
    private boolean sugeridoSistema;
}