package com.maletify.gestorempaque.lista.infrastructure.persistence;

import com.maletify.gestorempaque.lista.domain.model.ObjetoEmpaque;
import com.maletify.gestorempaque.lista.domain.repository.ItemCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemCatalogRepositoryImpl implements ItemCatalogRepository {

    private final JpaObjetoEmpaqueRepository jpa;

    @Override
    public Optional<ObjetoEmpaque> findById(Long id) {
        return jpa.findById(id)
                .map(ObjetoMapper::toDomain);
    }

    @Override
    public Optional<ObjetoEmpaque> findByNombre(String nombre) {
        return jpa.findByNombreObjeto(nombre) // ← NOMBRE CORRECTO
                .map(ObjetoMapper::toDomain);
    }

    @Override
    public List<ObjetoEmpaque> findAll() {
        return jpa.findAll().stream()
                .map(ObjetoMapper::toDomain)
                .toList();
    }

    @Override
    public ObjetoEmpaque save(ObjetoEmpaque item) {
        var saved = jpa.save(ObjetoMapper.toJpa(item));
        return ObjetoMapper.toDomain(saved);
    }
}
