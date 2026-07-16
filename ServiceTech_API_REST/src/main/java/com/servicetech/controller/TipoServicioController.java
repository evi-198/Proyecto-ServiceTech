package com.servicetech.controller;

import com.servicetech.model.TipoServicio;
import com.servicetech.repository.TipoServicioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para consultar y administrar los tipos de servicio disponibles.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class TipoServicioController {

    private final TipoServicioRepository tipoServicioRepository;

    public TipoServicioController(TipoServicioRepository tipoServicioRepository) {
        this.tipoServicioRepository = tipoServicioRepository;
    }

    /**
     * Lista todos los tipos de servicio registrados en el sistema.
     */
    @GetMapping("/tipos-servicio")
    public ResponseEntity<List<TipoServicio>> listarTipos() {
        List<TipoServicio> lista = tipoServicioRepository.findAll();
        return ResponseEntity.ok(lista);
    }

    /**
     * Crea un nuevo tipo de servicio (admin).
     */
    @PostMapping("/tipos-servicio")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<TipoServicio> crearTipo(@RequestBody TipoServicio request) {
        TipoServicio tipo = new TipoServicio(request.getNombre(), request.getDescripcion());
        TipoServicio guardado = tipoServicioRepository.save(tipo);
        return ResponseEntity.ok(guardado);
    }

    /**
     * Actualiza un tipo de servicio existente (admin).
     */
    @PutMapping("/tipos-servicio/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<TipoServicio> actualizarTipo(
            @PathVariable Integer id,
            @RequestBody TipoServicio request) {

        TipoServicio tipo = tipoServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de servicio no encontrado"));

        tipo.setNombre(request.getNombre());
        tipo.setDescripcion(request.getDescripcion());

        TipoServicio actualizado = tipoServicioRepository.save(tipo);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Elimina un tipo de servicio (admin). Si hay órdenes que lo usan, no se
     * permite el borrado y se informa con un 409.
     */
    @DeleteMapping("/tipos-servicio/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarTipo(@PathVariable Integer id) {
        if (!tipoServicioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            tipoServicioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar: hay órdenes que usan este tipo de servicio");
        }
    }
}