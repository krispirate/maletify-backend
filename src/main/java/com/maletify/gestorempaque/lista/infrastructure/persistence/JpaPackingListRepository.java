package com.maletify.gestorempaque.lista.infrastructure.persistence;

// --- Imports de Dominio y Aplicación ---
import com.maletify.gestorempaque.lista.domain.repository.PackingListRepository; 
import com.maletify.gestorempaque.lista.domain.model.DetalleLista;
import com.maletify.gestorempaque.lista.domain.model.ListaEmpaque;
import com.maletify.gestorempaque.lista.domain.model.ObjetoEmpaque;
import com.maletify.gestorempaque.lista.domain.repository.ItemCatalogRepository;


// --- Imports de Spring y Java ---
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query; // Importar Query para la corrección
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Interfaces Spring Data
interface JpaListaEmpaqueSpringRepository extends JpaRepository<ListaEmpaqueJpa, Long> {
    
    // CORRECCIÓN CLAVE: Consulta JPQL explícita para evitar errores de nombres.
    @Query("SELECT l FROM ListaEmpaqueJpa l WHERE l.idViaje = :idViaje")
    Optional<ListaEmpaqueJpa> findListaByViajeId(Long idViaje); 
}

interface JpaDetalleListaSpringRepository extends JpaRepository<DetalleListaJpa, Long> {
    List<DetalleListaJpa> findByIdLista(Long idLista);
    void deleteByIdLista(Long idLista); 
}

@Repository
public class JpaPackingListRepository implements PackingListRepository {

    private final JpaListaEmpaqueSpringRepository listaRepository;
    private final JpaDetalleListaSpringRepository detalleRepository;
    private final ItemCatalogRepository itemCatalogRepository; // Necesario para mapear ObjetoEmpaque

    public JpaPackingListRepository(
        JpaListaEmpaqueSpringRepository listaRepository,
        JpaDetalleListaSpringRepository detalleRepository,
        ItemCatalogRepository itemCatalogRepository) {
        this.listaRepository = listaRepository;
        this.detalleRepository = detalleRepository;
        this.itemCatalogRepository = itemCatalogRepository;
    }

    @Override
    @jakarta.transaction.Transactional // Asegura que las operaciones de la lista y detalles sean atómicas
    public ListaEmpaque save(ListaEmpaque lista) {
        // 1. Guardar o actualizar la Raíz del Agregado
        ListaEmpaqueJpa listaJpa = ListMapper.toJpa(lista);
        ListaEmpaqueJpa savedListaJpa = listaRepository.save(listaJpa);
        
        // Asignar el ID generado de la Lista al objeto de Dominio (si es nuevo)
        lista.id = savedListaJpa.getIdLista(); 

        // 2. Limpiar los detalles antiguos para el Agregado (patrón: "delete all, insert new")
        detalleRepository.deleteByIdLista(savedListaJpa.getIdLista());

        // 3. Guardar los nuevos DetalleLista
        List<DetalleListaJpa> detallesJpa = lista.getDetalles().stream()
            .map(detalle -> ListMapper.toJpa(detalle, savedListaJpa.getIdLista()))
            .collect(Collectors.toList());
            
        detalleRepository.saveAll(detallesJpa);

        return lista; // Devuelve el objeto de Dominio actualizado
    }

    @Override
    public Optional<ListaEmpaque> findById(Long id) {
        return listaRepository.findById(id)
            .map(listaJpa -> {
                List<DetalleListaJpa> detallesJpa = detalleRepository.findByIdLista(listaJpa.getIdLista());
                
                // Mapear los detalles de vuelta al dominio, cargando los ítems
                List<DetalleLista> detallesDomain = detallesJpa.stream()
                    .map(detalleJpa -> {
                        ObjetoEmpaque item = itemCatalogRepository.findById(detalleJpa.getIdObjeto())
                            .orElseThrow(() -> new IllegalStateException("Ítem de catálogo no encontrado: " + detalleJpa.getIdObjeto()));
                        return ListMapper.toDomain(detalleJpa, item);
                    })
                    .collect(Collectors.toList());
                    
                // Reconstruir el Agregado Raíz
                
                // Usar el getter de la entidad JPA para obtener el ID de usuario
                // NO necesitamos importar UsuarioJpa aquí, porque ListaEmpaqueJpa ya lo importa.
                Long idUsuario = listaJpa.getUsuarioPropietario() != null 
                                     ? listaJpa.getUsuarioPropietario().getIdUsuario() 
                                     : null;
                
                return ListaEmpaque.builder()
                    .id(listaJpa.getIdLista())
                    .idUsuarioPropietario(idUsuario) // Usamos el ID de la relación
                    .idViaje(listaJpa.getIdViaje())
                    .nombreLista(listaJpa.getNombreLista())
                    .fechaCreacion(listaJpa.getFechaCreacion())
                    .estado(listaJpa.getEstado())
                    .detalles(detallesDomain)
                    .build();
            });
    }

    @Override
    public Optional<ListaEmpaque> findByViajeId(Long idViaje) {
        // Usamos el nuevo método de consulta @Query
        return listaRepository.findListaByViajeId(idViaje).flatMap(listaJpa -> findById(listaJpa.getIdLista()));
    }
}
