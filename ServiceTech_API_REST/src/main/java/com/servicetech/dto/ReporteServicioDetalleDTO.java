package com.servicetech.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO utilizado para detallar un servicio incluido en el reporte técnico.
 * Resume la información clave del servicio y sus observaciones asociadas.
 */
public class ReporteServicioDetalleDTO {

    private Integer idServicio;
    private String cliente;
    private String tipoServicio;
    private String estado;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaFinalizacion;
    private int cantidadObservaciones;
    private List<ObservacionResumenDTO> observaciones;

    public ReporteServicioDetalleDTO(
            Integer idServicio,
            String cliente,
            String tipoServicio,
            String estado,
            String descripcion,
            LocalDateTime fechaCreacion,
            LocalDateTime fechaFinalizacion,
            int cantidadObservaciones,
            List<ObservacionResumenDTO> observaciones
    ) {
        this.idServicio = idServicio;
        this.cliente = cliente;
        this.tipoServicio = tipoServicio;
        this.estado = estado;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaFinalizacion = fechaFinalizacion;
        this.cantidadObservaciones = cantidadObservaciones;
        this.observaciones = observaciones;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public String getCliente() {
        return cliente;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public String getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public int getCantidadObservaciones() {
        return cantidadObservaciones;
    }

    public List<ObservacionResumenDTO> getObservaciones() {
        return observaciones;
    }
}
