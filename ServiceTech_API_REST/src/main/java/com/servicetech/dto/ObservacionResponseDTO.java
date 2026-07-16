package com.servicetech.dto;

import java.time.LocalDateTime;

/**
 * DTO utilizado para devolver una observación registrada en el seguimiento de un servicio.
 */
public class ObservacionResponseDTO {

    private Integer idObservacion;

    private String comentario;

    private LocalDateTime fecha;

    private String tecnico;

    public ObservacionResponseDTO(
            Integer idObservacion,
            String comentario,
            LocalDateTime fecha,
            String tecnico
    ) {

        this.idObservacion = idObservacion;
        this.comentario = comentario;
        this.fecha = fecha;
        this.tecnico = tecnico;
    }

    public Integer getIdObservacion() {
        return idObservacion;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getTecnico() {
        return tecnico;
    }
}