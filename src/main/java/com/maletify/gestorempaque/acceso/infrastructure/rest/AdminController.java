package com.maletify.gestorempaque.acceso.infrastructure.rest;

// 1. IMPORTAMOS LA ENTIDAD Y EL NUEVO REPOSITORIO JPA
import com.maletify.gestorempaque.acceso.infrastructure.persistence.UsuarioJpa;
import com.maletify.gestorempaque.acceso.infrastructure.persistence.UsuarioJpaRepository; 
// ---------------------------------------------------

import com.maletify.gestorempaque.lista.infrastructure.persistence.ListaEmpaqueRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // Usamos el repositorio JPA, no el de dominio
    private final UsuarioJpaRepository usuarioRepository;
    private final ListaEmpaqueRepository listaRepository;

    public AdminController(UsuarioJpaRepository usuarioRepository, ListaEmpaqueRepository listaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.listaRepository = listaRepository;
    }

    // 1. OBTENER TODOS LOS USUARIOS
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioJpa>> getAllUsers() {
        // .findAll() ahora SÍ existe gracias a JpaRepository
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    // 2. CAMBIAR PLAN
    @PutMapping("/usuarios/{id}/cambiar-plan")
    public ResponseEntity<?> toggleUserPlan(@PathVariable Long id) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                String planActual = usuario.getPlan() == null ? "FREE" : usuario.getPlan();
                String nuevoPlan = "FREE".equals(planActual) ? "PLUS" : "FREE";
                
                usuario.setPlan(nuevoPlan);
                usuarioRepository.save(usuario); // Guarda directamente en la BD
                
                return ResponseEntity.ok(Map.of(
                    "mensaje", "Plan actualizado", 
                    "nuevoPlan", nuevoPlan
                ));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // 3. DASHBOARD STATS
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        // .count() ahora SÍ existe
        long totalUsuarios = usuarioRepository.count();
        long totalListas = listaRepository.count();
        
        long usuariosPlus = usuarioRepository.findAll().stream()
                .filter(u -> "PLUS".equalsIgnoreCase(u.getPlan()))
                .count();

        double ingresos = usuariosPlus * 9.99;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsuarios", totalUsuarios);
        stats.put("totalListas", totalListas);
        stats.put("usuariosPlus", usuariosPlus);
        stats.put("usuariosFree", totalUsuarios - usuariosPlus);
        stats.put("ingresosEstimados", ingresos);
        
        return ResponseEntity.ok(stats);
    }
}