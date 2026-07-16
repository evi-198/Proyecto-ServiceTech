package com.servicetech.repository;

import com.servicetech.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio para gestionar los servicios del sistema.
 * Expone consultas para clientes, técnicos y reportes.
 */
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    /**
     * Obtiene todos los servicios asociados a un cliente por fecha reciente.
     */
	List<Servicio> findByCliente_IdUsuarioOrderByFechaCreacionDesc(Integer idCliente);

    /**
     * Obtiene los servicios de un cliente según una lista de estados.
     * Se usa para ver servicios activos o en historial.
     */
    List<Servicio> findByCliente_IdUsuarioAndEstadoServicio_NombreIn(
            Integer idCliente,
            List<String> estados
    );

    /**
     * Obtiene los servicios asignados a un técnico.
     */
    @Query("SELECT s FROM Servicio s WHERE s.tecnico.id = :idTecnico")
    List<Servicio> findByTecnicoId(@Param("idTecnico") Long idTecnico);

    /**
     * Obtiene los servicios activos de un técnico ordenados por fecha de creación.
     */
    List<Servicio> findByTecnico_IdUsuarioAndEstadoServicio_NombreInOrderByFechaCreacionAsc(
            Long idTecnico,
            List<String> estados
    );

    /**
     * Obtiene los servicios de un técnico creados dentro de un rango de fechas.
     * Se usa para generar reportes de desempeño.
     */
    @Query("SELECT s FROM Servicio s WHERE s.tecnico.idUsuario = :idTecnico " +
           "AND s.fechaCreacion >= :inicio AND s.fechaCreacion <= :fin " +
           "ORDER BY s.fechaCreacion DESC")
    List<Servicio> findByTecnicoAndFechaCreacionBetween(
            @Param("idTecnico") Integer idTecnico,
            @Param("inicio") java.time.LocalDateTime inicio,
            @Param("fin") java.time.LocalDateTime fin
    );

    /**
     * Cuenta los servicios activos de un técnico para medir su carga de trabajo.
     */
    long countByTecnico_IdUsuarioAndEstadoServicio_NombreIn(Integer idTecnico, List<String> estados);

    /**
     * Cuenta los servicios finalizados de un técnico para su historial.
     */
    long countByTecnico_IdUsuarioAndEstadoServicio_Nombre(Integer idTecnico, String estado);
    
    /** Todas las órdenes, más recientes primero — vista admin */
    List<Servicio> findAllByOrderByFechaCreacionDesc();

    /** Todas las órdenes filtradas por estado — vista admin */
    List<Servicio> findByEstadoServicio_NombreOrderByFechaCreacionDesc(String estado);
}
