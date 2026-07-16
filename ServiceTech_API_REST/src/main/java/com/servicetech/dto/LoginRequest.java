package com.servicetech.dto;

/**
 * DTO utilizado para recibir las credenciales de inicio de sesión de un usuario.
 */
public class LoginRequest {

    private String correo;
    private String password;

    public LoginRequest() {
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
}
