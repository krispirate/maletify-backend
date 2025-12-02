package com.maletify.gestorempaque.lista.infrastructure.persistence;

import com.maletify.gestorempaque.acceso.infrastructure.persistence.UsuarioJpa; 
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lista_empaque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListaEmpaqueJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lista")
    private Long idLista; 
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "id_usuario_propietario", referencedColumnName = "id_usuario", nullable = false)
    private UsuarioJpa usuarioPropietario;

    // CAMBIO AQUI: Se quitó 'nullable = false' para permitir listas sin viaje (personalizadas)
    @Column(name = "id_viaje") 
    private Long idViaje; 
    
    @Column(name = "nombre_lista", nullable = false)
    private String nombreLista;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "estado", nullable = false)
    private String estado;
    
    @Transient 
    private List<DetalleListaJpa> detalles;

    public Long getIdUsuarioPropietario() {
        return usuarioPropietario != null ? usuarioPropietario.getIdUsuario() : null;
    }
}