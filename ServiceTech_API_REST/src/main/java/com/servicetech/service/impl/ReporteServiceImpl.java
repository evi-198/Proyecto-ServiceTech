package com.servicetech.service.impl;

import com.servicetech.dto.ObservacionResumenDTO;
import com.servicetech.dto.ReporteServicioDetalleDTO;
import com.servicetech.dto.ReporteTecnicoResponseDTO;
import com.servicetech.model.Observacion;
import com.servicetech.model.Servicio;
import com.servicetech.model.Usuario;
import com.servicetech.repository.ObservacionRepository;
import com.servicetech.repository.ServicioRepository;
import com.servicetech.repository.UsuarioRepository;
import com.servicetech.service.ReporteService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de reportes.
 * Genera un resumen del desempeño de un técnico para un rango de fechas determinado.
 */
@Service
public class ReporteServiceImpl implements ReporteService {

    /**
     * Repositorio para consultar servicios del técnico.
     */
    private final ServicioRepository servicioRepository;

    /**
     * Repositorio para consultar observaciones asociadas a cada servicio.
     */
    private final ObservacionRepository observacionRepository;

    /**
     * Repositorio para validar la existencia del técnico.
     */
    private final UsuarioRepository usuarioRepository;

    public ReporteServiceImpl(
            ServicioRepository servicioRepository,
            ObservacionRepository observacionRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.servicioRepository = servicioRepository;
        this.observacionRepository = observacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Genera un reporte detallado del trabajo realizado por un técnico en un periodo dado.
     * Este proceso es útil para el administrador y para el seguimiento del equipo técnico.
     */
    @Override
    public ReporteTecnicoResponseDTO generarReporteTecnico(
            Integer idTecnico,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new RuntimeException("La fecha de inicio no puede ser posterior a la fecha fin");
        }

        Usuario tecnico = usuarioRepository.findById(idTecnico)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);

        // Servicios asignados al técnico creados dentro del periodo
        List<Servicio> servicios =
                servicioRepository.findByTecnicoAndFechaCreacionBetween(idTecnico, inicio, fin);

        int pendientes = 0;
        int asignados = 0;
        int enProceso = 0;
        int finalizados = 0;
        int cancelados = 0;

        List<ReporteServicioDetalleDTO> detalles = servicios.stream()
                .map(servicio -> {
                    List<Observacion> observaciones =
                            observacionRepository
                                    .findByServicio_IdServicioOrderByFechaDesc(servicio.getIdServicio());

                    List<ObservacionResumenDTO> obsResumen = observaciones.stream()
                            .map(obs -> new ObservacionResumenDTO(obs.getComentario(), obs.getFecha()))
                            .collect(Collectors.toList());

                    return new ReporteServicioDetalleDTO(
                            servicio.getIdServicio(),
                            servicio.getCliente().getNombre(),
                            servicio.getTipoServicio().getNombre(),
                            servicio.getEstadoServicio().getNombre(),
                            servicio.getDescripcion(),
                            servicio.getFechaCreacion(),
                            servicio.getFechaFinalizacion(),
                            observaciones.size(),
                            obsResumen
                    );
                })
                .collect(Collectors.toList());

        for (Servicio servicio : servicios) {
            String estado = servicio.getEstadoServicio().getNombre().trim().toUpperCase();
            switch (estado) {
                case "PENDIENTE" -> pendientes++;
                case "ASIGNADO" -> asignados++;
                case "EN PROCESO" -> enProceso++;
                case "FINALIZADO", "COMPLETADO" -> finalizados++;
                case "CANCELADO" -> cancelados++;
                default -> { }
            }
        }

        return new ReporteTecnicoResponseDTO(
                idTecnico,
                tecnico.getNombre(),
                fechaInicio,
                fechaFin,
                servicios.size(),
                pendientes,
                asignados,
                enProceso,
                finalizados,
                cancelados,
                detalles
        );
    }
}
