package com.servicetech.service;

import java.util.List;

import com.servicetech.dto.ServicioAdminResponseDTO;
import com.servicetech.dto.ServicioRequestDTO;
import com.servicetech.dto.ServicioResponseDTO;

/**
 * Contrato del servicio encargado de gestionar los servicios del sistema.
 * Cubre el flujo de cliente, técnico y seguimiento de estados.
 */
public interface ServicioService {

    /**
     * Crea un nuevo servicio a partir de la solicitud de un cliente.
     */
    ServicioResponseDTO crearServicio(ServicioRequestDTO request);

    /**
     * Obtiene todos los servicios asociados a un cliente.
     */
    List<ServicioResponseDTO> listarPorCliente(Integer idCliente);

    /**
     * Obtiene los servicios activos de un cliente.
     */
    List<ServicioResponseDTO> listarActivosPorCliente(Integer idCliente);

    /**
     * Obtiene el historial de servicios finalizados o cancelados de un cliente.
     */
    List<ServicioResponseDTO> listarHistorialPorCliente(Integer idCliente);

    /**
     * Obtiene los servicios asignados a un técnico.
     */
    List<ServicioResponseDTO> obtenerServiciosPorTecnico(Long idTecnico);

    /**
     * Obtiene los próximos servicios programados para un técnico.
     */
    List<ServicioResponseDTO> obtenerProximosServiciosPorTecnico(Long idTecnico);

    /**
     * Actualiza el estado de un servicio.
     */
    ServicioResponseDTO actualizarEstadoServicio(
            Integer idServicio,
            Integer idEstadoServicio
    );
    
   /**
    * *Listar todas las órdenes (admin), opcionalmente filtradas por estado  
   */
    List<ServicioAdminResponseDTO> listarTodas(String estado);

    /**
     *  Asignar o reasignar técnico a una orden (admin)
     */
    ServicioAdminResponseDTO asignarTecnico(Integer idServicio, Integer idTecnico);
}
