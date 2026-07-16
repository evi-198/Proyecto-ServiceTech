package com.servicetech.controller;

import com.servicetech.dto.LoginRequest;
import com.servicetech.model.Usuario;
import com.servicetech.security.JwtService;
import com.servicetech.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de autenticación para el inicio de sesión de usuarios.
 * Maneja el acceso inicial para clientes, técnicos y administradores.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    /**
     * Valida las credenciales del usuario y devuelve un resumen del perfil autenticado
     * junto con el token JWT que la app Android debe reenviar en cada request
     * posterior (header "Authorization: Bearer <token>").
     * Este endpoint es el punto de entrada común para todos los roles al iniciar sesión.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioService.login(
                request.getCorreo(),
                request.getPassword()
        );

        String token = jwtService.generarToken(usuario.getCorreo());

        Map<String, Object> response = new HashMap<>();
        response.put("idUsuario", usuario.getIdUsuario());
        response.put("nombre", usuario.getNombre());
        response.put("correo", usuario.getCorreo());
        response.put("rol", usuario.getRol().getNombre());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}