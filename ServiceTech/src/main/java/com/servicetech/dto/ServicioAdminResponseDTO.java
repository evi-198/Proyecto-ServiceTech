package com.servicetech.dto;

import java.time.LocalDateTime;

/**
 * DTO utilizado para enviar la información completa de una orden
 * de servicio al panel del administrador.
 */

public class ServicioAdminResponseDTO {
	
	 // Datos generales del servicio
    private Integer idServicio;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private String estado;
    private String tipoServicio;
    private LocalDateTime fechaFinalizacion;

   // Información del cliente
    private Integer idCliente;
    private String clienteNombre;

    // Información del técnico asignado
    private Integer idTecnico;      // Puede ser null si aún no está asignado
    private String tecnicoNombre;   // Puede ser null si aún no está asignado
    
    
    /**
     * Inicializa el DTO con toda la información de la orden de servicio.
     */
    public ServicioAdminResponseDTO(Integer idServicio, String descripcion, LocalDateTime fechaCreacion,
                                     String estado, String tipoServicio, LocalDateTime fechaFinalizacion,
                                     Integer idCliente, String clienteNombre,
                                     Integer idTecnico, String tecnicoNombre) {
        this.idServicio = idServicio;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.tipoServicio = tipoServicio;
        this.fechaFinalizacion = fechaFinalizacion;
        this.idCliente = idCliente;
        this.clienteNombre = clienteNombre;
        this.idTecnico = idTecnico;
        this.tecnicoNombre = tecnicoNombre;
    }
    
 // Métodos de acceso (Getters)

    public Integer getIdServicio() { return idServicio; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public String getEstado() { return estado; }
    public String getTipoServicio() { return tipoServicio; }
    public LocalDateTime getFechaFinalizacion() { return fechaFinalizacion; }
    public Integer getIdCliente() { return idCliente; }
    public String getClienteNombre() { return clienteNombre; }
    public Integer getIdTecnico() { return idTecnico; }
    public String getTecnicoNombre() { return tecnicoNombre; }
}