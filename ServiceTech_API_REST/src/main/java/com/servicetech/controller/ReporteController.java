package com.servicetech.controller;

import com.servicetech.dto.ReporteTecnicoResponseDTO;
import com.servicetech.service.ReporteService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controlador REST para generar reportes relacionados con el desempeño de los técnicos.
 * Su uso está orientado a funciones de administrador y seguimiento del equipo técnico.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /**
     * Genera el reporte del técnico a partir de servicios asignados,
     * estados y observaciones existentes en el periodo indicado.
     * Este endpoint es útil para que un administrador revise el desempeño
     * y el trabajo realizado por un técnico específico.
     *
     * GET /api/tecnico/{idTecnico}/reporte?fechaInicio=2024-01-01&fechaFin=2024-12-31
     */
    @GetMapping("/tecnico/{idTecnico}/reporte")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMINISTRADOR')")
    public ResponseEntity<ReporteTecnicoResponseDTO> generarReporteTecnico(
            @PathVariable Integer idTecnico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        ReporteTecnicoResponseDTO reporte =
                reporteService.generarReporteTecnico(idTecnico, fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }
}