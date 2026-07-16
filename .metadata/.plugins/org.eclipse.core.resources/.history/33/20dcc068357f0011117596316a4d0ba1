package com.servicetech.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Encargado de todo lo relacionado al token JWT: generarlo al hacer login,
 * y luego leerlo/validarlo en cada request posterior.
 *
 * Un JWT tiene 3 partes (header.payload.signature). Lo importante para
 * entender este archivo:
 * - "subject" del token = el correo del usuario (su identificador único).
 * - Se firma con una clave secreta (jwt.secret) para que nadie pueda
 *   fabricar un token falso sin conocer esa clave.
 * - Tiene fecha de expiración (jwt.expiration-ms), pasado ese tiempo deja
 *   de ser válido aunque la firma sea correcta.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    /**
     * Genera un nuevo token para el usuario que acaba de loguearse.
     */
    public String generarToken(String correo) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(correo)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(getClaveFirma())
                .compact();
    }

    /**
     * Extrae el correo (subject) guardado dentro del token.
     * Se usa en el filtro para saber "quién dice ser" el que hace el request.
     */
    public String extraerCorreo(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    /**
     * Revisa que el token pertenezca al correo indicado y que no esté vencido.
     */
    public boolean esTokenValido(String token, String correoEsperado) {
        String correoDelToken = extraerCorreo(token);
        return correoDelToken.equals(correoEsperado) && !estaExpirado(token);
    }

    // ==========================
    // Métodos privados de apoyo
    // ==========================

    private boolean estaExpirado(String token) {
        Date expiracion = extraerClaim(token, Claims::getExpiration);
        return expiracion.before(new Date());
    }

    private <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    private SecretKey getClaveFirma() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}