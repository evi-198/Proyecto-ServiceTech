package com.servicetech.service;

import com.servicetech.dto.ObservacionRequestDTO;
import com.servicetech.dto.ObservacionResponseDTO;

import java.util.List;

/**
 * Contrato del servicio de observaciones.
 * Define los casos de uso para el seguimiento de comentarios técnicos sobre servicios.
 */
public interface ObservacionService {

    /**
     * Lista las observaciones de un servicio ordenadas por fecha descendente.
     */
    List<ObservacionResponseDTO> listarPorServicio(Integer idServicio);

    /**
     * Registra una observación para un servicio.
     * Normalmente la utiliza un técnico para documentar avances o notas.
     */
    ObservacionResponseDTO registrarObservacion(ObservacionRequestDTO dto);

    /**
     * Elimina una observación existente por su identificador.
     */
    void eliminarObservacion(Integer idObservacion);
}