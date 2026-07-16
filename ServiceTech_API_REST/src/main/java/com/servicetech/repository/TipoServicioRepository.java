package com.servicetech.repository;

import com.servicetech.model.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para consultar los tipos de servicio disponibles.
 */
public interface TipoServicioRepository extends JpaRepository<TipoServicio, Integer> {
}
