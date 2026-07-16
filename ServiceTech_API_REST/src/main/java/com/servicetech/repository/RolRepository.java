package com.servicetech.repository;

import com.servicetech.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para consultar los roles disponibles del sistema.
 */
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Busca un rol por su nombre, por ejemplo CLIENTE, TÉCNICO o ADMINISTRADOR.
     */
    Optional<Rol> findByNombre(String nombre);
}
