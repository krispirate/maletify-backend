package com.maletify.gestorempaque.acceso.domain.repository;

import com.maletify.gestorempaque.acceso.domain.model.Usuario;
import java.util.Optional;

public interface UserRepository {

    Usuario save(Usuario usuario);

    Optional<Usuario> findById(Long id);
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
}