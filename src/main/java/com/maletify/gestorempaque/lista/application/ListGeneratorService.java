package com.maletify.gestorempaque.lista.application;

import com.maletify.gestorempaque.api.application.WeatherService;
import com.maletify.gestorempaque.api.domain.model.Clima;
import com.maletify.gestorempaque.lista.domain.model.DetalleLista;
import com.maletify.gestorempaque.lista.domain.model.ListaEmpaque;
import com.maletify.gestorempaque.lista.domain.model.ObjetoEmpaque;
import com.maletify.gestorempaque.lista.domain.repository.ItemCatalogRepository;
import com.maletify.gestorempaque.lista.domain.repository.PackingListRepository;
import com.maletify.gestorempaque.lista.infrastructure.persistence.ListaEmpaqueRepository;

// --- AQUI: Usamos el Repositorio de DOMINIO (Correcto) ---
import com.maletify.gestorempaque.acceso.domain.repository.UserRepository; 
import com.maletify.gestorempaque.acceso.domain.model.Usuario; 
// ---------------------------------------------------------

import com.maletify.gestorempaque.notificacion.application.ReminderService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ListGeneratorService {

    private final PackingListRepository listRepository;
    private final ItemCatalogRepository itemCatalogRepository;
    private final WeatherService weatherService;
    private final ReminderService reminderService;
    
    // Inyectamos UserRepository (Interfaz de Dominio)
    private final UserRepository userRepository;
    
    // Inyectamos ListaEmpaqueRepository (JPA) solo para contar listas (esto es aceptable)
    private final ListaEmpaqueRepository listaEmpaqueRepository;

    public ListGeneratorService(
            PackingListRepository listRepository,
            ItemCatalogRepository itemCatalogRepository,
            WeatherService weatherService,
            ReminderService reminderService,
            UserRepository userRepository, // <--- Correcto
            ListaEmpaqueRepository listaEmpaqueRepository
    ) {
        this.listRepository = listRepository;
        this.itemCatalogRepository = itemCatalogRepository;
        this.weatherService = weatherService;
        this.reminderService = reminderService;
        this.userRepository = userRepository;
        this.listaEmpaqueRepository = listaEmpaqueRepository;
    }

    // --- VALIDACIÓN ---
    private void validarLimiteGeneracion(Long idUsuario) {
        // 1. Buscamos el Usuario de DOMINIO
        Usuario usuario = userRepository.findById(idUsuario)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // 2. Verificar el PLAN y el ROL
        // Ahora usuario.getPlan() FUNCIONARÁ porque lo agregamos al modelo
        String plan = usuario.getPlan() != null ? usuario.getPlan().toUpperCase() : "FREE";
        String rol = usuario.getRol() != null ? usuario.getRol().toUpperCase() : "USER";

        // Si es PLUS o ADMIN, tiene pase libre
        if ("PLUS".equals(plan) || "ADMIN".equals(rol)) {
            return; 
        }

        // 3. Definir ventana de tiempo (últimas 24 horas)
        LocalDateTime hace24Horas = LocalDateTime.now().minusHours(24);

        // 4. Contar listas creadas
        long listasHoy = listaEmpaqueRepository
            .countByUsuarioPropietario_IdUsuarioAndFechaCreacionAfter(idUsuario, hace24Horas);

        // 5. Validar límite
        if (listasHoy >= 3) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, 
                "LÍMITE ALCANZADO: Los usuarios FREE solo pueden generar 3 listas cada 24 horas."
            );
        }
    }

    @Transactional
    public ListaEmpaque generateAndSaveList(
            Long idViaje,
            Long idUsuario,
            String destino,
            int dias
    ) {
        
        // --- 0. VALIDAR EL LÍMITE ---
        validarLimiteGeneracion(idUsuario);

        // 1. Obtener Clima
        Optional<Clima> climaOpt = weatherService.getClima(destino, LocalDate.now());
        String clasificacion = "templado";

        if (climaOpt.isPresent()) {
            Clima clima = climaOpt.get();
            double temp = clima.getTemperaturaMedia();
            String condiciones = clima.getCondiciones();

            if (condiciones != null && condiciones.toUpperCase().contains("LLUVIA")) {
                clasificacion = "lluvia";
            } else if (temp < 10) {
                clasificacion = "frio";
            } else if (temp > 25) {
                clasificacion = "calor";
            }
        }

        // 2. Crear Lista Base
        ListaEmpaque lista = ListaEmpaque.createInitial(
                idViaje,
                idUsuario,
                "Lista para " + destino
        );

        // 3. Generar Ítems
        List<DetalleLista> detalles = new ArrayList<>();
        detalles.add(itemPersistido("Ropa interior", dias));
        detalles.add(itemPersistido("Calcetines", dias));
        detalles.add(itemPersistido("Cargador", 1));
        detalles.add(itemPersistido("Cepillo de dientes", 1));
        detalles.add(itemPersistido("Medicinas", 1));

        switch (clasificacion) {
            case "frio" -> {
                detalles.add(itemPersistido("Abrigo grueso", 1));
                detalles.add(itemPersistido("Guantes", 1));
                detalles.add(itemPersistido("Botas térmicas", 1));
            }
            case "calor" -> {
                detalles.add(itemPersistido("Gafas de sol", 1));
                detalles.add(itemPersistido("Protector solar", 1));
                detalles.add(itemPersistido("Ropa ligera", dias / 2 + 1));
            }
            case "lluvia" -> {
                detalles.add(itemPersistido("Paraguas", 1));
                detalles.add(itemPersistido("Impermeable", 1));
                detalles.add(itemPersistido("Zapatos impermeables", 1));
            }
            default -> detalles.add(itemPersistido("Chaqueta ligera", 1));
        }

        detalles.forEach(lista::addDetail);

        // 4. Guardar
        ListaEmpaque listaGuardada = listRepository.save(lista);

        // 5. Notificación
        String payload = String.format(
            "{\"titulo\": \"¡Lista para %s creada!\", \"mensaje\": \"Hemos generado tu equipaje para %d días basándonos en el clima (%s).\", \"tipo\": \"success\"}",
            destino, dias, clasificacion
        );

        reminderService.createReminder(
            listaGuardada.getId(),
            idUsuario,
            LocalDateTime.now(), 
            "SISTEMA",           
            payload              
        );

        return listaGuardada;
    }

    private DetalleLista itemPersistido(String nombre, int cantidad) {
        Optional<ObjetoEmpaque> encontrado = itemCatalogRepository.findByNombre(nombre);
        ObjetoEmpaque objeto;

        if (encontrado.isPresent()) {
            objeto = encontrado.get();
        } else {
            ObjetoEmpaque nuevo = ObjetoEmpaque.builder()
                    .nombre(nombre)
                    .descripcion(nombre)
                    .sugeridoPorSistema(true)
                    .build();
            objeto = itemCatalogRepository.save(nuevo);
        }

        return DetalleLista.builder()
                .item(objeto)
                .cantidad(cantidad)
                .obligatorio(true)
                .agregadoManual(false)
                .empacado(false)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }
}