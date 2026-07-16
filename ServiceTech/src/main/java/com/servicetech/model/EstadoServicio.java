package com.servicetech.model;

import jakarta.persistence.*;

/**
 * Modelo que representa el estado actual de un servicio, como pendiente, asignado o finalizado.
 */
@Entity
@Table(name = "estado_servicio")
public class EstadoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_servicio")
    private Integer idEstadoServicio;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    // Este modelo define los estados posibles que puede tener un servicio durante su ciclo de vida.

    // Constructor vacío obligatorio para JPA
    public EstadoServicio() {
    }

    // Constructor útil
    public EstadoServicio(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Integer getIdEstadoServicio() {
        return idEstadoServicio;
    }

    public void setIdEstadoServicio(Integer idEstadoServicio) {
        this.idEstadoServicio = idEstadoServicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
