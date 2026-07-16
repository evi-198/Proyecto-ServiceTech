package com.servicetech.repository;

import com.servicetech.model.EstadoServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para consultar los estados de un servicio.
 */
public interface EstadoServicioRepository extends JpaRepository<EstadoServicio, Integer> {

    /**
     * Busca un estado por su nombre, como PENDIENTE, ASIGNADO o FINALIZADO.
     */
    Optional<EstadoServicio> findByNombre(String nombre);
}
