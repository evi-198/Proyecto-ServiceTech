package com.servicetech.dto;

/**
 * DTO utilizado para devolver la información pública de un usuario después de una operación.
 */
public class UsuarioResponseDTO {

    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String telefono;
    private String rol;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(Integer idUsuario, String nombre, String correo, String telefono, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.rol = rol;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
