package com.maletify.gestorempaque.lista.infrastructure.rest;

import com.maletify.gestorempaque.lista.application.ListGeneratorService;
import com.maletify.gestorempaque.lista.application.ListManagerService;
import com.maletify.gestorempaque.lista.domain.model.ListaEmpaque;
// 1. IMPORTAR EL REPOSITORIO
import com.maletify.gestorempaque.lista.infrastructure.persistence.ListaEmpaqueRepository; 

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/listas")
public class PackingListController {

    private final ListGeneratorService generatorService;
    private final ListManagerService managerService;
    
    // 2. DECLARAR LA VARIABLE DEL REPOSITORIO
    private final ListaEmpaqueRepository listaEmpaqueRepository; 

    // 3. ACTUALIZAR EL CONSTRUCTOR PARA INYECTARLO
    public PackingListController(
            ListGeneratorService generatorService,
            ListManagerService managerService,
            ListaEmpaqueRepository listaEmpaqueRepository // <--- Agregar aquí
    ) {
        this.generatorService = generatorService;
        this.managerService = managerService;
        this.listaEmpaqueRepository = listaEmpaqueRepository; // <--- Asignar aquí
    }

    @PostMapping("/generar/{idViaje}")
    public ResponseEntity<Long> generateList(
            @PathVariable Long idViaje,
            @RequestParam Long idUsuario,
            @RequestParam String destino,
            @RequestParam int duracionDias
    ) {
        try {
            ListaEmpaque lista = generatorService.generateAndSaveList(
                    idViaje,
                    idUsuario,
                    destino,
                    duracionDias
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(lista.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{listaId}")
    public ResponseEntity<ListaEmpaque> getListaById(@PathVariable Long listaId) {
        return managerService.findById(listaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{listaId}/empacar/{detalleId}")
    public ResponseEntity<String> markItemAsPacked(
            @PathVariable Long listaId,
            @PathVariable Long detalleId,
            @RequestBody Map<String, Boolean> body 
    ) {
        try {
            boolean nuevoEstado = body.getOrDefault("empacado", true);
            managerService.markItemAsPacked(listaId, detalleId, nuevoEstado);
            return ResponseEntity.ok("Ítem actualizado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ListaEmpaque>> getListasByUsuario(@PathVariable Long idUsuario) {
        try {
            List<ListaEmpaque> listas = managerService.getListasByUsuario(idUsuario);
            return ResponseEntity.ok(listas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{listaId}/agregar")
    public ResponseEntity<String> addItem(
            @PathVariable Long listaId,
            @RequestBody Map<String, String> body
    ) {
        String nombre = body.get("nombre");
        if (nombre == null || nombre.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre es requerido");
        }
        managerService.addItemByName(listaId, nombre);
        return ResponseEntity.ok("Ítem agregado");
    }

    @DeleteMapping("/{listaId}/eliminar/{detalleId}")
    public ResponseEntity<String> deleteItem(
            @PathVariable Long listaId,
            @PathVariable Long detalleId
    ) {
        managerService.removeItem(detalleId);
        return ResponseEntity.ok("Ítem eliminado");
    }

    @PostMapping("/crear-personalizada")
    public ResponseEntity<Long> createCustomList(@RequestBody Map<String, Object> payload) {
        try {
            Long idUsuario = Long.valueOf(payload.get("idUsuario").toString());
            String nombre = (String) payload.get("nombre");
            List<String> items = (List<String>) payload.get("items");

            Long listaId = managerService.createCustomList(idUsuario, nombre, items);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(listaId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // AHORA SÍ FUNCIONARÁ PORQUE 'listaEmpaqueRepository' YA EXISTE EN LA CLASE
    @GetMapping("/conteo/{idUsuario}")
    public ResponseEntity<Long> contarListasUsuario(@PathVariable Long idUsuario) {
        long conteo = listaEmpaqueRepository.countByUsuarioPropietario_IdUsuario(idUsuario);
        return ResponseEntity.ok(conteo);
    }
}