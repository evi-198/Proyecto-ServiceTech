package com.servicetech.dto;

/**
 * DTO utilizado para devolver la información de un técnico.
 * Incluye sus datos principales y un resumen de su carga de trabajo,
 * permitiendo conocer si puede recibir nuevas órdenes de servicio.
 */
public class TecnicoResponseDTO {

    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String telefono;
    private long ordenesActivas;
    private long ordenesFinalizadas;
    private int limiteOrdenes;
    private boolean saturado;
    
    
    /**
     * Inicializa un objeto con la información del técnico y sus estadísticas.
     */
    public TecnicoResponseDTO(Integer idUsuario, String nombre, String correo, String telefono,
                               long ordenesActivas, long ordenesFinalizadas,
                               int limiteOrdenes, boolean saturado) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.ordenesActivas = ordenesActivas;
        this.ordenesFinalizadas = ordenesFinalizadas;
        this.limiteOrdenes = limiteOrdenes;
        this.saturado = saturado;
    }

    public Integer getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public long getOrdenesActivas() { return ordenesActivas; }
    public long getOrdenesFinalizadas() { return ordenesFinalizadas; }
    public int getLimiteOrdenes() { return limiteOrdenes; }
    public boolean isSaturado() { return saturado; }
}