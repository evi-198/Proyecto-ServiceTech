package com.servicetech.dto;

import java.time.LocalDateTime;

/**
 * DTO utilizado para devolver la información de un servicio al cliente, técnico o administrador.
 */
public class ServicioResponseDTO {

    private Integer idServicio;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private String estado;
    private String tipoServicio;
    private LocalDateTime fechaFinalizacion;

    public ServicioResponseDTO(Integer idServicio,
                                String descripcion,
                                LocalDateTime fechaCreacion,
                                String estado,
                                String tipoServicio,
                                LocalDateTime fechaFinalizacion) {
        this.idServicio = idServicio;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.tipoServicio = tipoServicio;
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }
    
    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }
}
