package com.servicetech.service;

import com.servicetech.dto.ReporteTecnicoResponseDTO;

import java.time.LocalDate;

/**
 * Contrato del servicio de reportes.
 * Define la generación de reportes técnicos basados en servicios y observaciones.
 */
public interface ReporteService {

    /**
     * Genera un reporte de desempeño del técnico para un rango de fechas.
     */
    ReporteTecnicoResponseDTO generarReporteTecnico(
            Integer idTecnico,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}
