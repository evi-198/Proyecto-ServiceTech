package com.servicetech.service.impl;

import com.servicetech.dto.ServicioAdminResponseDTO;
import com.servicetech.dto.ServicioRequestDTO;
import com.servicetech.dto.ServicioResponseDTO;
import com.servicetech.model.EstadoServicio;
import com.servicetech.model.Servicio;
import com.servicetech.model.TipoServicio;
import com.servicetech.model.Usuario;
import com.servicetech.repository.EstadoServicioRepository;
import com.servicetech.repository.ServicioRepository;
import com.servicetech.repository.TipoServicioRepository;
import com.servicetech.repository.UsuarioRepository;
import com.servicetech.service.ServicioService;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de gestión de servicios.
 * Coordina las operaciones entre clientes, técnicos, tipos de servicio y estados.
 */
@Service
public class ServicioServiceImpl implements ServicioService {

    /** Repositorio para persistir y consultar servicios. */
    private final ServicioRepository servicioRepository;

    /** Repositorio para validar cliente y técnico. */
    private final UsuarioRepository usuarioRepository;

    /** Repositorio para consultar el tipo de servicio solicitado. */
    private final TipoServicioRepository tipoServicioRepository;

    /** Repositorio para consultar y actualizar el estado del servicio. */
    private final EstadoServicioRepository estadoServicioRepository;

    public ServicioServiceImpl(ServicioRepository servicioRepository,
                               UsuarioRepository usuarioRepository,
                               TipoServicioRepository tipoServicioRepository,
                               EstadoServicioRepository estadoServicioRepository) {
        this.servicioRepository = servicioRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoServicioRepository = tipoServicioRepository;
        this.estadoServicioRepository = estadoServicioRepository;
    }

    /**
     * Crea un servicio validando al cliente, el tipo de servicio y el estado inicial.
     */
    @Override
    public ServicioResponseDTO crearServicio(ServicioRequestDTO request) {

        // 1️. Buscar cliente
        Usuario cliente = usuarioRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // 2️. Validar rol
        if (!cliente.getRol().getNombre().equalsIgnoreCase("CLIENTE")) {
            throw new RuntimeException("El usuario no tiene rol CLIENTE");
        }

        // 3️. Buscar tipo servicio
        TipoServicio tipoServicio = tipoServicioRepository.findById(request.getIdTipoServicio())
                .orElseThrow(() -> new RuntimeException("Tipo de servicio no encontrado"));

        // 4️. Buscar estado PENDIENTE
        EstadoServicio estado = estadoServicioRepository.findByNombre("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no configurado"));

        // 5️. Crear servicio (tecnico = null automáticamente)
        Servicio servicio = new Servicio(
                request.getDescripcion(),
                cliente,
                tipoServicio,
                estado
        );

        Servicio servicioGuardado = servicioRepository.save(servicio);

        // 6️. Retornar respuesta
        return new ServicioResponseDTO(
                servicioGuardado.getIdServicio(),
                servicioGuardado.getDescripcion(),
                servicioGuardado.getFechaCreacion(),
                servicioGuardado.getEstadoServicio().getNombre(),
                servicioGuardado.getTipoServicio().getNombre(),
                servicioGuardado.getFechaFinalizacion()
        );
    }
    
    /**
     * Recupera todos los servicios asociados a un cliente.
     */
    @Override
    public List<ServicioResponseDTO> listarPorCliente(Integer idCliente) {
    	
    	System.out.println("FILTRANDO CLIENTE: " + idCliente); 

    	List<Servicio> servicios =
                servicioRepository.findByCliente_IdUsuarioOrderByFechaCreacionDesc(idCliente);

        return servicios.stream()
                .map(servicio -> new ServicioResponseDTO(
                        servicio.getIdServicio(),
                        servicio.getDescripcion(),
                        servicio.getFechaCreacion(),
                        servicio.getEstadoServicio().getNombre(),
                        servicio.getTipoServicio().getNombre(),
                        servicio.getFechaFinalizacion()
                ))
                .toList();
    }

    
    /**
     * Recupera los servicios activos de un cliente para mostrar su estado actual.
     */
    @Override
    public List<ServicioResponseDTO> listarActivosPorCliente(Integer idCliente) {

        List<String> estadosActivos = List.of("PENDIENTE", "EN PROCESO");

        List<Servicio> servicios =
                servicioRepository.findByCliente_IdUsuarioAndEstadoServicio_NombreIn(
                        idCliente,
                        estadosActivos
                );

        return servicios.stream()
                .map(servicio -> new ServicioResponseDTO(
                        servicio.getIdServicio(),
                        servicio.getDescripcion(),
                        servicio.getFechaCreacion(),
                        servicio.getEstadoServicio().getNombre(),
                        servicio.getTipoServicio().getNombre(),
                        servicio.getFechaFinalizacion()
                ))
                .toList();
    }

    
    /**
     * Recupera el historial de servicios finalizados o cancelados por cliente.
     */
    @Override
    public List<ServicioResponseDTO> listarHistorialPorCliente(Integer idCliente) {

        List<String> estadosHistorial = List.of("FINALIZADO", "CANCELADO");

        List<Servicio> servicios =
                servicioRepository.findByCliente_IdUsuarioAndEstadoServicio_NombreIn(
                        idCliente,
                        estadosHistorial
                );

        return servicios.stream()
                .map(servicio -> new ServicioResponseDTO(
                        servicio.getIdServicio(),
                        servicio.getDescripcion(),
                        servicio.getFechaCreacion(),
                        servicio.getEstadoServicio().getNombre(),
                        servicio.getTipoServicio().getNombre(),
                        servicio.getFechaFinalizacion()
                ))
                .toList();
    }
    
    
    /**
     * Recupera los servicios asignados a un técnico.
     */
    @Override
    public List<ServicioResponseDTO> obtenerServiciosPorTecnico(Long idTecnico) {
        List<Servicio> servicios = servicioRepository.findByTecnicoId(idTecnico);

        // construir ServicioResponseDTO por constructor (inmutable), sin setters.
        return servicios.stream()
                .map(servicio -> new ServicioResponseDTO(
                        servicio.getIdServicio(),
                        servicio.getDescripcion(),
                        servicio.getFechaCreacion(),
                        servicio.getEstadoServicio().getNombre(),
                        servicio.getTipoServicio().getNombre(),
                        servicio.getFechaFinalizacion()
                ))
                .toList();
    }

    /**
     * Recupera los próximos servicios programados para un técnico.
     */
    @Override
    public List<ServicioResponseDTO> obtenerProximosServiciosPorTecnico(Long idTecnico) {
        List<String> estadosProximos = List.of("PENDIENTE", "ASIGNADO", "EN PROCESO");

        List<Servicio> servicios = servicioRepository
                .findByTecnico_IdUsuarioAndEstadoServicio_NombreInOrderByFechaCreacionAsc(
                        idTecnico,
                        estadosProximos
                );

        return servicios.stream()
                .map(servicio -> new ServicioResponseDTO(
                        servicio.getIdServicio(),
                        servicio.getDescripcion(),
                        servicio.getFechaCreacion(),
                        servicio.getEstadoServicio().getNombre(),
                        servicio.getTipoServicio().getNombre(),
                        servicio.getFechaFinalizacion()
                ))
                .toList();
    }
    
    /**
     * Actualiza el estado de un servicio y registra la fecha de finalización si aplica.
     */
    @Override
    public ServicioResponseDTO actualizarEstadoServicio(
            Integer idServicio,
            Integer idEstadoServicio
    ) {

        // 1. Buscar servicio
        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // 2. Buscar nuevo estado
        EstadoServicio nuevoEstado = estadoServicioRepository.findById(idEstadoServicio)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        // 3. Actualizar estado
        servicio.setEstadoServicio(nuevoEstado);

        // 4. Si finaliza o cancela, guardar fecha finalización
        String nombreEstado = nuevoEstado.getNombre().toUpperCase();

        if (nombreEstado.equals("FINALIZADO") || nombreEstado.equals("CANCELADO")) {
            servicio.setFechaFinalizacion(java.time.LocalDateTime.now());
        }

        // 5. Guardar cambios
        Servicio servicioActualizado = servicioRepository.save(servicio);

        // 6. Retornar respuesta
        return new ServicioResponseDTO(
                servicioActualizado.getIdServicio(),
                servicioActualizado.getDescripcion(),
                servicioActualizado.getFechaCreacion(),
                servicioActualizado.getEstadoServicio().getNombre(),
                servicioActualizado.getTipoServicio().getNombre(),
                servicioActualizado.getFechaFinalizacion()
        );
    }
    
    @Override
    public List<ServicioAdminResponseDTO> listarTodas(String estado) {

        List<Servicio> servicios = (estado == null || estado.isBlank())
                ? servicioRepository.findAllByOrderByFechaCreacionDesc()
                : servicioRepository.findByEstadoServicio_NombreOrderByFechaCreacionDesc(estado.toUpperCase());

        return servicios.stream()
                .map(this::mapearAAdminDTO)
                .toList();
    }

    @Override
    public ServicioAdminResponseDTO asignarTecnico(Integer idServicio, Integer idTecnico) {

        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Usuario tecnico = usuarioRepository.findById(idTecnico)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));

        if (!tecnico.getRol().getNombre().equalsIgnoreCase("TECNICO")) {
            throw new RuntimeException("El usuario seleccionado no tiene rol TECNICO");
        }

        servicio.setTecnico(tecnico);

        // Si estaba PENDIENTE, pasa a ASIGNADO.
        // Si ya estaba EN PROCESO (reasignación), se conserva el estado actual
        // para no perder el progreso registrado por el técnico anterior.
        if (servicio.getEstadoServicio().getNombre().equalsIgnoreCase("PENDIENTE")) {
            EstadoServicio asignado = estadoServicioRepository.findByNombre("ASIGNADO")
                    .orElseThrow(() -> new RuntimeException("Estado ASIGNADO no configurado"));
            servicio.setEstadoServicio(asignado);
        }

        Servicio actualizado = servicioRepository.save(servicio);

        return mapearAAdminDTO(actualizado);
    }

    private ServicioAdminResponseDTO mapearAAdminDTO(Servicio servicio) {
        Usuario tecnico = servicio.getTecnico();

        return new ServicioAdminResponseDTO(
                servicio.getIdServicio(),
                servicio.getDescripcion(),
                servicio.getFechaCreacion(),
                servicio.getEstadoServicio().getNombre(),
                servicio.getTipoServicio().getNombre(),
                servicio.getFechaFinalizacion(),
                servicio.getCliente().getIdUsuario(),
                servicio.getCliente().getNombre(),
                tecnico != null ? tecnico.getIdUsuario() : null,
                tecnico != null ? tecnico.getNombre() : null
        );
    }

}
