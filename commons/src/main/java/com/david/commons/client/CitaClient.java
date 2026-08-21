package com.david.commons.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "citas")
public interface CitaClient {

    @GetMapping("/paciente/{idPaciente}/tiene-cita-confirmada-en-curso")
    Boolean tieneCitaConfirmadaOEnCurso(
            @PathVariable Long idPaciente


    );
    @GetMapping("/medico/{idMedico}/tiene-cita-confirmada-en-curso")
    Boolean tieneCitaConfirmadaOEnCursoMedico(
            @PathVariable("idMedico") Long idMedico
    );


}