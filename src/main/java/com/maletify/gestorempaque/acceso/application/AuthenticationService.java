package com.maletify.gestorempaque.acceso.application;

import com.maletify.gestorempaque.acceso.domain.model.Credenciales;
import com.maletify.gestorempaque.acceso.domain.model.Usuario;
import com.maletify.gestorempaque.acceso.domain.repository.UserRepository;
import com.maletify.gestorempaque.acceso.domain.services.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Caso de Uso: Registrar un nuevo usuario
    public Usuario registerNewUser(String nombre, String email, String password) {
        
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        
       
        Usuario nuevoUsuario = Usuario.createNew(nombre, email, password, passwordEncoder);

        return userRepository.save(nuevoUsuario);
    }
    
    // Caso de Uso: Iniciar sesión
    public Usuario authenticate(Credenciales credenciales) {
        
        Usuario usuario = userRepository.findByEmail(credenciales.getEmail())
                                        .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));


        if (!usuario.checkPassword(credenciales.getRawPassword(), passwordEncoder)) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        return usuario;
    }
}