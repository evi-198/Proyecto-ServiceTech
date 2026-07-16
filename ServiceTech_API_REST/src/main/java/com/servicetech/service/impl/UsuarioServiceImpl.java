package com.servicetech.service.impl;

import com.servicetech.model.Usuario;
import com.servicetech.repository.UsuarioRepository;
import com.servicetech.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de autenticación de usuarios.
 * Valida el correo y la contraseña para permitir el acceso al sistema.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Valida las credenciales del usuario y devuelve la entidad si coincide la información.
     *
     * La contraseña guardada en la base de datos es un hash de BCrypt, por eso
     * no se puede comparar con equals(): passwordEncoder.matches() hashea el
     * password recibido y compara internamente contra ese hash.
     */
    @Override
    public Usuario login(String correo, String password) {

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new RuntimeException("Password incorrecto");
        }

        return usuario;
    }
}