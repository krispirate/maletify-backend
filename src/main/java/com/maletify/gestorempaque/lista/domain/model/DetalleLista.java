package com.maletify.gestorempaque.lista.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor; 
import lombok.NoArgsConstructor;  

import java.time.LocalDateTime; 
@Getter
@Setter
@Builder
@AllArgsConstructor 
@NoArgsConstructor 
public class DetalleLista {
    
    private Long id;
    private ObjetoEmpaque item;
    private int cantidad;
    private boolean obligatorio; 
    private boolean agregadoManual; 
    private boolean empacado;
    

    private LocalDateTime fechaCreacion; 
    public void markAsPacked() {
        this.empacado = true;
    }
}