package com.servicetech.security;

import com.servicetech.model.Usuario;
import com.servicetech.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Puente entre Spring Security y la tabla "usuario" de la base de datos.
 *
 * Spring Security no sabe nada de tu modelo Usuario; esta clase traduce
 * un Usuario del sistema a un UserDetails (el formato que Spring Security
 * entiende) para poder autenticarlo y saber qué rol tiene.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Busca al usuario por correo (usado como "username" para Spring Security)
     * y arma un UserDetails con su contraseña hasheada y su rol como authority.
     *
     * El prefijo "ROLE_" es una convención que exige Spring Security para
     * poder usar luego cosas como hasRole("ADMINISTRADOR") en vez de
     * hasAuthority("ROLE_ADMINISTRADOR").
     */
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No existe un usuario con el correo: " + correo));

        String rolConPrefijo = "ROLE_" + usuario.getRol().getNombre();

        return new User(
                usuario.getCorreo(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority(rolConPrefijo))
        );
    }
}