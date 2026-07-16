package com.servicetech.dto;

/**
 * DTO utilizado para recibir la información necesaria al crear un servicio.
 * Normalmente lo envía un cliente al solicitar una atención.
 */
public class ServicioRequestDTO {

    private String descripcion;
    private Integer idCliente;
    private Integer idTipoServicio;

    public ServicioRequestDTO() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdTipoServicio() {
        return idTipoServicio;
    }

    public void setIdTipoServicio(Integer idTipoServicio) {
        this.idTipoServicio = idTipoServicio;
    }
}
