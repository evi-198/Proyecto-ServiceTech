package com.servicetech.dto;

import java.time.LocalDateTime;

/**
 * DTO utilizado para resumir una observación dentro del reporte técnico.
 * Contiene el comentario y la fecha en que fue registrada.
 */
public class ObservacionResumenDTO {

    private String comentario;
    private LocalDateTime fecha;

    public ObservacionResumenDTO(String comentario, LocalDateTime fecha) {
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
