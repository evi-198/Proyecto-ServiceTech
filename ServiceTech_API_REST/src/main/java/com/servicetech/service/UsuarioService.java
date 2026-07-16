package com.servicetech.service;

import com.servicetech.model.Usuario;

/**
 * Contrato del servicio de usuarios.
 * Define la lógica de autenticación para los usuarios del sistema.
 */
public interface UsuarioService {

    /**
     * Valida las credenciales de un usuario y devuelve su entidad si es correcto.
     */
    Usuario login(String correo, String password);
}
