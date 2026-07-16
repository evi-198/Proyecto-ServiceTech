package com.servicetech.repository;

import com.servicetech.model.Observacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para gestionar las observaciones registradas sobre los servicios.
 */
public interface ObservacionRepository extends JpaRepository<Observacion, Integer> {

    /**
     * Obtiene las observaciones de un servicio ordenadas por fecha descendente.
     */
    List<Observacion> findByServicio_IdServicioOrderByFechaDesc(Integer idServicio);
}