package com.david.citas.controller;

import com.david.citas.dto.CitaRequest;
import com.david.citas.dto.CitaResponse;
import com.david.citas.entity.Cita;
import com.david.citas.service.CitaService;
import com.david.commons.controller.CommonController;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class CitaController extends CommonController<CitaRequest, CitaResponse, CitaService> {


    public CitaController(CitaService service){
        super(service);
    }
    @PatchMapping("/{idCita}/estado/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoCita(
            @PathVariable  @Positive (message = "el idcita debe ser positvo ") Long idCita,
            @PathVariable  @Positive (message = "el idcita debe ser positvo ") Long idEstado){
        service.actualizarEstadoCita(idCita, idEstado);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/medico/{idMedico}/tiene-cita-confirmada-en-curso")
    public ResponseEntity<Boolean> tieneCitaConfirmadaOEnCursoMedico(
            @PathVariable Long idMedico) {

        return ResponseEntity.ok(
                service.tieneCitaConfirmadaOEnCursoMedico(idMedico)
        );
    }
}
