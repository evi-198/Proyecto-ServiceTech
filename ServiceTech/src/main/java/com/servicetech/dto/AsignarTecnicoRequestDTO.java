package com.servicetech.dto;

/**
 * DTO utilizado para recibir el identificador del técnico
 * que será asignado a una orden de servicio.
 */

public class AsignarTecnicoRequestDTO {

    private Integer idTecnico;

    public Integer getIdTecnico() { return idTecnico; }
    public void setIdTecnico(Integer idTecnico) { this.idTecnico = idTecnico; }
}