package com.maletify.gestorempaque.lista.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Setter
public class ListaEmpaque {
    
    public Long id;
    private Long idViaje;
    private Long idUsuarioPropietario;
    private String nombreLista;
    private LocalDateTime fechaCreacion;
    private String estado; 
    

    private List<DetalleLista> detalles;

    public static ListaEmpaque createInitial(Long idViaje, Long idUsuario, String nombre) {
        return ListaEmpaque.builder()
                .idViaje(idViaje)
                .idUsuarioPropietario(idUsuario)
                .nombreLista(nombre)
                .fechaCreacion(LocalDateTime.now())
                .estado("BORRADOR")
                .detalles(new ArrayList<>())
                .build();
    }
    
    public void addDetail(DetalleLista detalle) {
        this.detalles.add(detalle);
    }
}
