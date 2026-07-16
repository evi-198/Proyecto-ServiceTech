package com.servicetech.model;

import jakarta.persistence.*;

/**
 * Modelo que representa un rol del sistema.
 * Define el tipo de acceso que tiene un usuario, como cliente, técnico o administrador.
 */
@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    // Este modelo no tiene relaciones directas, pero define el tipo de acceso de los usuarios.

    // Constructor vacío obligatorio para JPA
    public Rol() {
    }

    // Constructor útil
    public Rol(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
