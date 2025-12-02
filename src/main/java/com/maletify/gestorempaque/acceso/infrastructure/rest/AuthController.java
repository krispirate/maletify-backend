package com.maletify.gestorempaque.acceso.infrastructure.rest;

import com.maletify.gestorempaque.acceso.application.AuthenticationService;
import com.maletify.gestorempaque.acceso.domain.model.Credenciales;
import com.maletify.gestorempaque.acceso.domain.model.Usuario;
import com.maletify.gestorempaque.acceso.infrastructure.rest.dto.LoginRequest;
import com.maletify.gestorempaque.acceso.infrastructure.rest.dto.RegisterRequest;
import com.maletify.gestorempaque.acceso.infrastructure.rest.dto.UserResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acceso") // Endpoint para el Access Context
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        try {
            Usuario usuario = authenticationService.registerNewUser(
                request.getNombre(), 
                request.getEmail(), 
                request.getPassword()
            );
            
            // Mapeo simple a DTO de respuesta
            UserResponse response = new UserResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol());
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        try {
            Credenciales credenciales = new Credenciales(request.getEmail(), request.getPassword());
            Usuario usuario = authenticationService.authenticate(credenciales);

            UserResponse response = new UserResponse(usuario.getId(), usuario.getNombre(), usuario.getEmail(), usuario.getRol());
            
            // En un sistema real, aquí se generaría un Token JWT
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401
        }
    }
}