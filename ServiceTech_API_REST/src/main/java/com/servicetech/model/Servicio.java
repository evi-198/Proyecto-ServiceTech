package com.servicetech.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Modelo que representa un servicio solicitado por un cliente y gestionado por un técnico.
 */
@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idServicio;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    

    // ===============================
    // RELACIONES DEL SERVICIO
    // ===============================

    // Cliente que solicita el servicio y lo inicia en el sistema.
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Usuario cliente;

    // Técnico asignado para atender el servicio. Puede quedar sin valor hasta que se asigne.
    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    private Usuario tecnico;

    // Tipo de servicio solicitado por el cliente.
    @ManyToOne
    @JoinColumn(name = "id_tipo_servicio", nullable = false)
    private TipoServicio tipoServicio;

    // Estado actual del servicio durante su ciclo de vida.
    @ManyToOne
    @JoinColumn(name = "id_estado_servicio", nullable = false)
    private EstadoServicio estadoServicio;
    
    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

 
    // Constructor vacío requerido por JPA.
    public Servicio() {
    }

    // Constructor principal para crear un servicio desde la solicitud inicial del cliente.
    public Servicio(String descripcion, Usuario cliente, TipoServicio tipoServicio, EstadoServicio estadoServicio) {
        this.descripcion = descripcion;
        this.cliente = cliente;
        this.tipoServicio = tipoServicio;
        this.estadoServicio = estadoServicio;
        this.fechaCreacion = LocalDateTime.now();
    }

    
    // Getters y Setters 

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getTecnico() {
        return tecnico;
    }

    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public EstadoServicio getEstadoServicio() {
        return estadoServicio;
    }

    public void setEstadoServicio(EstadoServicio estadoServicio) {
        this.estadoServicio = estadoServicio;
    }
    
    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

}
