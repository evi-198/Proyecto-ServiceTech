package com.servicetech.dto;

/**
 * DTO utilizado para recibir los datos de creación o actualización de un usuario.
 * Contiene la información básica necesaria para registrar un cliente, técnico o administrador.
 */
public class UsuarioRequestDTO {

    private String nombre;
    private String correo;
    private String password;
    private String telefono;
    private String rol;

    public UsuarioRequestDTO() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
