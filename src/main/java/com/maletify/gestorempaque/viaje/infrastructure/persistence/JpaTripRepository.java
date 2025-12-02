package com.maletify.gestorempaque.viaje.infrastructure.persistence;

import com.maletify.gestorempaque.viaje.domain.model.Viaje;
import com.maletify.gestorempaque.viaje.domain.repository.TripRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

interface JpaTripSpringRepository extends JpaRepository<ViajeJpa, Long> {
}

@Repository
public class JpaTripRepository implements TripRepository {
    
    private final JpaTripSpringRepository jpaRepository;

    public JpaTripRepository(JpaTripSpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Viaje save(Viaje viaje) {
        ViajeJpa entity = ViajeMapper.toEntity(viaje);
        ViajeJpa savedEntity = jpaRepository.save(entity);
        return ViajeMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Viaje> findById(Long id) {
        return jpaRepository.findById(id).map(ViajeMapper::toDomain);
    }
}