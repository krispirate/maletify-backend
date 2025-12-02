package com.maletify.gestorempaque.lista.domain.model;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class ObjetoEmpaque {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private String categoria; 
    private boolean sugeridoPorSistema; 
}