package com.servicetech.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO utilizado para devolver el resumen de servicios atendidos por un técnico en un periodo.
 * Se construye a partir de los servicios asignados, estados y observaciones registradas.
 */
public class ReporteTecnicoResponseDTO {

    private Integer idTecnico;
    private String nombreTecnico;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int totalServicios;
    private int pendientes;
    private int asignados;
    private int enProceso;
    private int finalizados;
    private int cancelados;
    private List<ReporteServicioDetalleDTO> servicios;

    public ReporteTecnicoResponseDTO(
            Integer idTecnico,
            String nombreTecnico,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            int totalServicios,
            int pendientes,
            int asignados,
            int enProceso,
            int finalizados,
            int cancelados,
            List<ReporteServicioDetalleDTO> servicios
    ) {
        this.idTecnico = idTecnico;
        this.nombreTecnico = nombreTecnico;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.totalServicios = totalServicios;
        this.pendientes = pendientes;
        this.asignados = asignados;
        this.enProceso = enProceso;
        this.finalizados = finalizados;
        this.cancelados = cancelados;
        this.servicios = servicios;
    }

    public Integer getIdTecnico() {
        return idTecnico;
    }

    public String getNombreTecnico() {
        return nombreTecnico;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int getTotalServicios() {
        return totalServicios;
    }

    public int getPendientes() {
        return pendientes;
    }

    public int getAsignados() {
        return asignados;
    }

    public int getEnProceso() {
        return enProceso;
    }

    public int getFinalizados() {
        return finalizados;
    }

    public int getCancelados() {
        return cancelados;
    }

    public List<ReporteServicioDetalleDTO> getServicios() {
        return servicios;
    }
}
