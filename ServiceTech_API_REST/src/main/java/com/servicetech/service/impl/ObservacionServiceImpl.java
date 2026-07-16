package com.servicetech.service.impl;

import com.servicetech.dto.ObservacionRequestDTO;
import com.servicetech.dto.ObservacionResponseDTO;
import com.servicetech.model.Observacion;
import com.servicetech.model.Servicio;
import com.servicetech.model.Usuario;
import com.servicetech.repository.ObservacionRepository;
import com.servicetech.repository.ServicioRepository;
import com.servicetech.repository.UsuarioRepository;
import com.servicetech.service.ObservacionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de observaciones.
 * Permite al técnico registrar y consultar comentarios sobre un servicio.
 */
@Service
public class ObservacionServiceImpl implements ObservacionService {

    /**
     * Repositorio de observaciones para consultas y persistencia.
     */
    private final ObservacionRepository observacionRepository;

    /**
     * Repositorio de servicios para validar la existencia del servicio asociado.
     */
    private final ServicioRepository servicioRepository;

    /**
     * Repositorio de usuarios para validar el técnico que registra la observación.
     */
    private final UsuarioRepository usuarioRepository;

    public ObservacionServiceImpl(
            ObservacionRepository observacionRepository,
            ServicioRepository servicioRepository,
            UsuarioRepository usuarioRepository) {

        this.observacionRepository = observacionRepository;
        this.servicioRepository = servicioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Lista las observaciones asociadas a un servicio.
     * Sirve para revisar el historial de seguimiento del trabajo.
     */
    @Override
    public List<ObservacionResponseDTO> listarPorServicio(Integer idServicio) {

        List<Observacion> lista =
                observacionRepository
                        .findByServicio_IdServicioOrderByFechaDesc(idServicio);

        return lista.stream().map(obs ->
                new ObservacionResponseDTO(
                        obs.getIdObservacion(),
                        obs.getComentario(),
                        obs.getFecha(),
                        obs.getTecnico().getNombre()
                )
        ).collect(Collectors.toList());
    }

    /**
     * Registra una nueva observación para un servicio.
     * Normalmente la usa un técnico para dejar comentarios o avances.
     */
    @Override
    public ObservacionResponseDTO registrarObservacion(ObservacionRequestDTO dto) {

        Servicio servicio = servicioRepository.findById(dto.getIdServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Usuario tecnico = usuarioRepository.findById(dto.getIdTecnico())
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));

        Observacion observacion = new Observacion();
        observacion.setComentario(dto.getComentario());
        observacion.setServicio(servicio);
        observacion.setTecnico(tecnico);
        observacion.setFecha(LocalDateTime.now()); // ← corrección: asignar fecha

        Observacion guardada = observacionRepository.save(observacion);

        return new ObservacionResponseDTO(
                guardada.getIdObservacion(),
                guardada.getComentario(),
                guardada.getFecha(),
                guardada.getTecnico().getNombre()
        );
    }

    /**
     * Elimina una observación existente por su identificador.
     */
    @Override
    public void eliminarObservacion(Integer idObservacion) {
        if (!observacionRepository.existsById(idObservacion)) {
            throw new RuntimeException("Observación no encontrada");
        }
        observacionRepository.deleteById(idObservacion);
    }
}