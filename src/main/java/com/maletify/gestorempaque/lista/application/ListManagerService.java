package com.maletify.gestorempaque.lista.application;

import com.maletify.gestorempaque.lista.domain.model.ListaEmpaque;
import com.maletify.gestorempaque.lista.domain.model.ObjetoEmpaque;
import com.maletify.gestorempaque.lista.domain.model.DetalleLista;
import com.maletify.gestorempaque.lista.domain.repository.PackingListRepository;
import com.maletify.gestorempaque.lista.domain.repository.ItemCatalogRepository;
import com.maletify.gestorempaque.lista.infrastructure.persistence.DetalleListaRepository;
import com.maletify.gestorempaque.lista.infrastructure.persistence.ListaEmpaqueRepository;
import com.maletify.gestorempaque.lista.infrastructure.persistence.ListMapper;
import com.maletify.gestorempaque.lista.infrastructure.persistence.DetalleListaJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListManagerService {
    
    private final PackingListRepository listRepository;
    private final ItemCatalogRepository itemCatalogRepository;
    private final DetalleListaRepository detalleListaRepository;
    private final ListaEmpaqueRepository listaEmpaqueRepository;

    public ListManagerService(
            PackingListRepository listRepository, 
            ItemCatalogRepository itemCatalogRepository,
            DetalleListaRepository detalleListaRepository,
            ListaEmpaqueRepository listaEmpaqueRepository
    ) {
        this.listRepository = listRepository;
        this.itemCatalogRepository = itemCatalogRepository;
        this.detalleListaRepository = detalleListaRepository;
        this.listaEmpaqueRepository = listaEmpaqueRepository;
    }

    // --- MÉTODOS EXISTENTES ---

    public ListaEmpaque addItemManually(Long listaId, Long itemId, int cantidad) {
        ListaEmpaque lista = listRepository.findById(listaId)
            .orElseThrow(() -> new IllegalArgumentException("Lista no encontrada."));
            
        ObjetoEmpaque item = itemCatalogRepository.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Ítem de catálogo no encontrado."));

        DetalleLista nuevoDetalle = DetalleLista.builder()
            .item(item)
            .cantidad(cantidad)
            .obligatorio(false) 
            .agregadoManual(true)
            .build();

        lista.addDetail(nuevoDetalle);
        
        return listRepository.save(lista);
    }
    
    public Optional<ListaEmpaque> findById(Long id) {
        return listRepository.findById(id);
    }

    public void markItemAsPacked(Long listaId, Long detalleId, boolean nuevoEstado) {
        detalleListaRepository.actualizarEstado(detalleId, nuevoEstado);
    }

    public List<ListaEmpaque> getListasByUsuario(Long idUsuario) {
        return listaEmpaqueRepository.findAllByUsuarioPropietario_IdUsuario(idUsuario)
                .stream()
                .map(ListMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addItemByName(Long listaId, String nombreItem) {
        if (!listaEmpaqueRepository.existsById(listaId)) {
            throw new IllegalArgumentException("Lista no encontrada");
        }

        ObjetoEmpaque item = itemCatalogRepository.findByNombre(nombreItem)
            .orElseGet(() -> {
                ObjetoEmpaque newItem = ObjetoEmpaque.builder()
                        .nombre(nombreItem)
                        .descripcion("Agregado manualmente")
                        .sugeridoPorSistema(false)
                        .build();
                return itemCatalogRepository.save(newItem);
            });

        DetalleListaJpa nuevoDetalleJpa = new DetalleListaJpa();
        nuevoDetalleJpa.setIdLista(listaId);
        nuevoDetalleJpa.setIdObjeto(item.getId());
        nuevoDetalleJpa.setCantidad(1);
        nuevoDetalleJpa.setObligatorio(false);
        nuevoDetalleJpa.setAgregadoManual(true);
        nuevoDetalleJpa.setEmpacado(false);
        nuevoDetalleJpa.setTimestampCreacion(LocalDateTime.now());

        detalleListaRepository.save(nuevoDetalleJpa);
    }

    @Transactional
    public void removeItem(Long detalleId) {
        detalleListaRepository.deleteById(detalleId);
    }

    // --- CORRECCIÓN AQUI ---
    @Transactional
    public Long createCustomList(Long idUsuario, String nombreLista, List<String> itemsNombres) {
        
        // Creamos la cabecera de la lista
        // CAMBIO: idViaje(null) en lugar de 0L para evitar error de Foreign Key
        ListaEmpaque lista = ListaEmpaque.builder()
                .idViaje(null) 
                .idUsuarioPropietario(idUsuario)
                .nombreLista(nombreLista)
                .fechaCreacion(LocalDateTime.now())
                .estado("BORRADOR")
                .detalles(new ArrayList<>())
                .build();

        // Guardamos la lista primero
        ListaEmpaque listaGuardada = listRepository.save(lista);
        Long listaId = listaGuardada.getId();

        // Agregamos los ítems
        for (String nombreItem : itemsNombres) {
            addItemByName(listaId, nombreItem); 
        }

        return listaId;
    }
}