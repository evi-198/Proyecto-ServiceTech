package com.servicetech.controller;

import com.servicetech.dto.ServicioResponseDTO;
import com.servicetech.dto.TecnicoResponseDTO;
import com.servicetech.model.Servicio;
import com.servicetech.model.Usuario;
import com.servicetech.repository.ServicioRepository;
import com.servicetech.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para consultar información relacionada con técnicos.
 * Expone endpoints para listar técnicos y revisar sus órdenes activas.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class TecnicoController {

    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;

    @Value("${servicetech.tecnico.limite-ordenes-activas}")
    private int limiteOrdenesActivas;

    // Estados que cuentan como carga activa de un técnico.
    // PENDIENTE no se considera porque el servicio aún no está asignado.
    private static final List<String> ESTADOS_ACTIVOS = List.of("ASIGNADO", "EN PROCESO");
    private static final String ESTADO_FINALIZADO = "FINALIZADO";

    public TecnicoController(UsuarioRepository usuarioRepository,
                              ServicioRepository servicioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.servicioRepository = servicioRepository;
    }

    /**
     * Lista todos los usuarios con rol técnico.
     */
    @GetMapping("/tecnicos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<TecnicoResponseDTO>> listarTecnicos() {
        List<Usuario> tecnicos = usuarioRepository.findAll().stream()
                .filter(u -> u.getRol().getNombre().equalsIgnoreCase("TECNICO"))
                .toList();

        List<TecnicoResponseDTO> respuesta = tecnicos.stream()
                .map(this::construirTecnicoDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Lista las órdenes activas asignadas a un técnico específico.
     */
    @GetMapping("/tecnicos/{id}/ordenes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TECNICO')")
    public ResponseEntity<List<ServicioResponseDTO>> listarOrdenesActivasDelTecnico(
            @PathVariable Integer id) {

        List<Servicio> servicios = servicioRepository
                .findByTecnico_IdUsuarioAndEstadoServicio_NombreInOrderByFechaCreacionAsc(
                        id.longValue(),
                        ESTADOS_ACTIVOS
                );

        List<ServicioResponseDTO> respuesta = servicios.stream()
                .map(s -> new ServicioResponseDTO(
                        s.getIdServicio(),
                        s.getDescripcion(),
                        s.getFechaCreacion(),
                        s.getEstadoServicio().getNombre(),
                        s.getTipoServicio().getNombre(),
                        s.getFechaFinalizacion()
                ))
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Construye el DTO de un técnico con la información de carga y estado.
     */
    private TecnicoResponseDTO construirTecnicoDTO(Usuario tecnico) {
        long activas = servicioRepository.countByTecnico_IdUsuarioAndEstadoServicio_NombreIn(
                tecnico.getIdUsuario(), ESTADOS_ACTIVOS
        );

        long finalizadas = servicioRepository.countByTecnico_IdUsuarioAndEstadoServicio_Nombre(
                tecnico.getIdUsuario(), ESTADO_FINALIZADO
        );

        boolean saturado = activas >= limiteOrdenesActivas;

        return new TecnicoResponseDTO(
                tecnico.getIdUsuario(),
                tecnico.getNombre(),
                tecnico.getCorreo(),
                tecnico.getTelefono(),
                activas,
                finalizadas,
                limiteOrdenesActivas,
                saturado
        );
    }
}