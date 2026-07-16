package com.servicetech.dto;

/**
 * DTO utilizado para cambiar el estado de un servicio desde el flujo del técnico.
 */
public class ActualizarEstadoDTO {

    private Integer idEstadoServicio;

    public ActualizarEstadoDTO() {
    }

    public Integer getIdEstadoServicio() {
        return idEstadoServicio;
    }

    public void setIdEstadoServicio(Integer idEstadoServicio) {
        this.idEstadoServicio = idEstadoServicio;
    }
}
