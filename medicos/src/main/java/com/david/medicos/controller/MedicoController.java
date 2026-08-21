package com.david.medicos.controller;

import com.david.commons.controller.CommonController;
import com.david.commons.dto.medicos.MedicoRequest;
import com.david.commons.dto.medicos.MedicoResponse;
import com.david.medicos.service.MedicoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class MedicoController  extends CommonController<MedicoRequest, MedicoResponse, MedicoService> {

    public MedicoController(MedicoService service){
        super(service);
    }

    @GetMapping("/id-medico/{id}")
    public ResponseEntity<MedicoResponse> obternerMedicoPorIdSinEstado(
            @PathVariable @Positive(message =" el id debe ser positivo")Long id
    ) {
        return ResponseEntity.ok(service.obtenerMedicoPorIdSinEstado(id));
    }


    @PutMapping("/{idMedico}/disponibilidad/{idDisponibilidad}")
    public ResponseEntity<Void> actualizarDisponibilidadMedico(
            @PathVariable @Positive(message = "El id médico debe ser positivo")
            Long idMedico,

            @PathVariable @Positive(message = "El id disponibilidad debe ser positivo")
            Long idDisponibilidad) {

        service.actualizarDisponibilidadMedico(idMedico, idDisponibilidad);

        return ResponseEntity.noContent().build();
    }

}

