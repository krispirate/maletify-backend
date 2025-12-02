package com.maletify.gestorempaque.acceso.infrastructure.persistence;

import com.maletify.gestorempaque.acceso.domain.model.Usuario;
import com.maletify.gestorempaque.acceso.domain.repository.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


interface JpaUserSpringRepository extends JpaRepository<UsuarioJpa, Long> {
    Optional<UsuarioJpa> findByEmail(String email);
    boolean existsByEmail(String email);
}

@Repository
public class JpaUserRepository implements UserRepository {
    
    private final JpaUserSpringRepository jpaRepository;

    public JpaUserRepository(JpaUserSpringRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioJpa entity = UsuarioMapper.toEntity(usuario);

    // 🔥 ASIGNAR VALORES POR DEFECTO SI VIENEN NULL
    if (entity.getPlan() == null) {
        entity.setPlan("FREE");    // Default
    }
    if (entity.getRol() == null) {
        entity.setRol("USER");     // Default
    }

    UsuarioJpa savedEntity = jpaRepository.save(entity);
    return UsuarioMapper.toDomain(savedEntity);
}

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaRepository.findById(id).map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(UsuarioMapper::toDomain);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}