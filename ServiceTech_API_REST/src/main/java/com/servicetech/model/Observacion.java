package com.servicetech.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Modelo que representa una observación registrada por un técnico sobre un servicio.
 */
@Entity
@Table(name = "observacion")
public class Observacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_observacion")
    private Integer idObservacion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    // ===============================
    // RELACIONES DE LA OBSERVACIÓN
    // ===============================

    // Servicio al que pertenece la observación y que está siendo seguido.
    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;

    // Técnico que registra la observación y deja seguimiento del trabajo.
    @ManyToOne
    @JoinColumn(name = "id_tecnico", nullable = false)
    private Usuario tecnico;

 
    // Constructor vacío requerido por JPA.
    public Observacion() {
    }

    // Constructor útil para crear una observación con su servicio y técnico asociados.
    public Observacion(String comentario, Servicio servicio, Usuario tecnico) {
        this.comentario = comentario;
        this.servicio = servicio;
        this.tecnico = tecnico;
        this.fecha = LocalDateTime.now();
    }

    
    // Getters y setters para acceder y actualizar la información de la observación.

    public Integer getIdObservacion() {
        return idObservacion;
    }

    public void setIdObservacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }
}
