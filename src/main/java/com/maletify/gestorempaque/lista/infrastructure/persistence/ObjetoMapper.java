package com.maletify.gestorempaque.lista.infrastructure.persistence;

import com.maletify.gestorempaque.lista.domain.model.ObjetoEmpaque;

public class ObjetoMapper {

    public static ObjetoEmpaque toDomain(ObjetoEmpaqueJpa jpa) {
        if (jpa == null) return null;

        return ObjetoEmpaque.builder()
                .id(jpa.getIdObjeto())
                .nombre(jpa.getNombreObjeto())     // ← mapea nombreObjeto → nombre
                .descripcion(jpa.getDescripcion())
                .categoria(null)                   // ← en la tabla no existe columna categoria
                .sugeridoPorSistema(jpa.isSugeridoSistema()) // ← nombre correcto del dominio
                .build();
    }

    public static ObjetoEmpaqueJpa toJpa(ObjetoEmpaque domain) {
        if (domain == null) return null;

        ObjetoEmpaqueJpa jpa = new ObjetoEmpaqueJpa();
        jpa.setIdObjeto(domain.getId());
        jpa.setNombreObjeto(domain.getNombre());         // ← mapea nombre → nombreObjeto
        jpa.setDescripcion(domain.getDescripcion());
        jpa.setSugeridoSistema(domain.isSugeridoPorSistema()); // ← getter real del dominio
        return jpa;
    }
}
