package com.maletify.gestorempaque.lista.infrastructure.persistence;

import com.maletify.gestorempaque.acceso.infrastructure.persistence.UsuarioJpa;
import com.maletify.gestorempaque.lista.domain.model.DetalleLista;
import com.maletify.gestorempaque.lista.domain.model.ListaEmpaque;
import com.maletify.gestorempaque.lista.domain.model.ObjetoEmpaque;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ListMapper {

    private ListMapper() {
        // Clase de utilidad
    }
    
    // --- Mapper para ObjetoEmpaque (Catálogo) ---
    public static ObjetoEmpaque toDomain(ObjetoEmpaqueJpa jpa) {
        return ObjetoEmpaque.builder()
                .id(jpa.getIdObjeto())
                .nombre(jpa.getNombreObjeto())
                .descripcion(jpa.getDescripcion())
                .sugeridoPorSistema(jpa.isSugeridoSistema()) 
                .build();
    }
    
    public static ObjetoEmpaqueJpa toJpa(ObjetoEmpaque domain) {
        ObjetoEmpaqueJpa jpa = new ObjetoEmpaqueJpa();
        jpa.setIdObjeto(domain.getId());
        jpa.setNombreObjeto(domain.getNombre());
        jpa.setDescripcion(domain.getDescripcion());
        jpa.setSugeridoSistema(domain.isSugeridoPorSistema());
        return jpa;
    }

    // --- Mapper para DetalleLista ---
    public static DetalleLista toDomain(DetalleListaJpa jpa, ObjetoEmpaque item) {
        return DetalleLista.builder()
                .id(jpa.getIdDetalle())
                .item(item)
                .cantidad(jpa.getCantidad())
                .obligatorio(jpa.isObligatorio()) 
                .agregadoManual(jpa.isAgregadoManual()) 
                .empacado(jpa.isEmpacado())
                .build();
    }
    
    public static DetalleListaJpa toJpa(DetalleLista domain, Long idLista) {
        DetalleListaJpa jpa = new DetalleListaJpa();
        jpa.setIdDetalle(domain.getId());
        jpa.setIdLista(idLista);
        jpa.setIdObjeto(domain.getItem().getId());
        jpa.setCantidad(domain.getCantidad());
        
        jpa.setObligatorio(domain.isObligatorio()); 
        jpa.setAgregadoManual(domain.isAgregadoManual()); 
        jpa.setEmpacado(domain.isEmpacado());
        
        jpa.setTimestampCreacion(LocalDateTime.now()); 

        return jpa;
    }
    
    // --- Mapper para ListaEmpaque (Agregado Raíz) ---
    public static ListaEmpaqueJpa toJpa(ListaEmpaque domain) {
        ListaEmpaqueJpa jpa = new ListaEmpaqueJpa();
        jpa.setIdLista(domain.id);

        if (domain.getIdUsuarioPropietario() != null) {
            UsuarioJpa propietarioJpa = new UsuarioJpa();
            propietarioJpa.setIdUsuario(domain.getIdUsuarioPropietario());
            jpa.setUsuarioPropietario(propietarioJpa);
        }

        jpa.setIdViaje(domain.getIdViaje());
        jpa.setNombreLista(domain.getNombreLista());
        jpa.setFechaCreacion(domain.getFechaCreacion());
        jpa.setEstado(domain.getEstado());

        return jpa;
    }

    // --- Mapper para ListaEmpaque (CORREGIDO: Ahora sí mapea los detalles) ---
    public static ListaEmpaque toDomain(ListaEmpaqueJpa jpa) {
        if (jpa == null) return null;

        // 1. Recuperamos la lista de detalles de la base de datos
        List<DetalleLista> detallesDomain = new ArrayList<>();
        
        if (jpa.getDetalles() != null) {
            detallesDomain = jpa.getDetalles().stream().map(d -> {
                // Creamos un objeto temporal solo con el ID para que no pese tanto
                // (En la vista "Mis Listas" solo necesitamos contar cuántos hay)
                ObjetoEmpaque itemTemp = ObjetoEmpaque.builder()
                        .id(d.getIdObjeto())
                        .nombre("Item") 
                        .build();
                return toDomain(d, itemTemp);
            }).collect(Collectors.toList());
        }

        return ListaEmpaque.builder()
                .id(jpa.getIdLista())
                .idViaje(jpa.getIdViaje())
                .idUsuarioPropietario(jpa.getIdUsuarioPropietario())
                .nombreLista(jpa.getNombreLista())
                .fechaCreacion(jpa.getFechaCreacion())
                .estado(jpa.getEstado())
                .detalles(detallesDomain) // <--- ¡Aquí asignamos los detalles recuperados!
                .build();
    }
}