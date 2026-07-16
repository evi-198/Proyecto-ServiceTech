package com.servicetech.dto;

/**
 * DTO utilizado para registrar una observación asociada a un servicio.
 */
public class ObservacionRequestDTO {

    private String comentario;

    private Integer idServicio;

    private Integer idTecnico;

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public Integer getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Integer idTecnico) {
        this.idTecnico = idTecnico;
    }
}