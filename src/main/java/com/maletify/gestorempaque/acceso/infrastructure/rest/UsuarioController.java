package com.maletify.gestorempaque.acceso.infrastructure.rest;

import com.maletify.gestorempaque.acceso.domain.model.Usuario;
import com.maletify.gestorempaque.acceso.domain.repository.UserRepository; // <--- Usamos tu interfaz de Dominio
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UserRepository userRepository;

    // Inyectamos la interfaz del dominio (Spring buscará tu implementación JpaUserRepository automáticamente)
    public UsuarioController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Long id, @RequestBody Map<String, String> datos) {
        
        // 1. Buscar usuario usando el repositorio de dominio
        Usuario usuario = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Actualizar los campos en el objeto de Dominio
        if (datos.containsKey("nombre")) {
            usuario.setNombre(datos.get("nombre"));
        }
        
        if (datos.containsKey("pais")) {
            usuario.setPaisResidencia(datos.get("pais"));
        }

        // 3. Guardar cambios
        // Tu adaptador (JpaUserRepository) se encargará de convertir Usuario -> UsuarioJpa y hacer el UPDATE en la BD
        userRepository.save(usuario);
        
        // 4. Responder
        return ResponseEntity.ok(Map.of(
            "mensaje", "Perfil actualizado con éxito",
            "usuario", usuario
        ));

        
    }

    // Endpoint para obtener datos frescos del usuario
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id) {
        return userRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}