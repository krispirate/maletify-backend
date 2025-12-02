package com.maletify.gestorempaque.acceso.domain.model;
import com.maletify.gestorempaque.acceso.domain.services.PasswordEncoder; 
import lombok.Getter;
import lombok.Setter;
import lombok.Builder; 
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long id; 
    private String nombre;
    private String email; 
    private String passwordHash; 
    private String paisResidencia;
    private String rol;
    private String preferencias;
    private String plan; // "FREE" o "PLUS"

//Verifica si la contraseña suministrada coincide con el hash almacenado
    public boolean checkPassword(String rawPassword, PasswordEncoder passwordEncoder) {
      
        return passwordEncoder.matches(rawPassword, this.passwordHash);
    }
    
    // Método de dominio: Crea un nuevo usuario
    public static Usuario createNew(String nombre, String email, String rawPassword, PasswordEncoder passwordEncoder) {
        return Usuario.builder()
                .nombre(nombre)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword)) 
                .rol("USER") 
                .build();
    }
}