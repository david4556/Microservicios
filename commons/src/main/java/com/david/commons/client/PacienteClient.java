package com.david.commons.client;

import com.david.commons.dto.pacientes.PacienteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pacientes")
public interface PacienteClient {

    @GetMapping("/{id}")
    PacienteResponse obtenerPacienteActivo(@PathVariable("id") Long id);

    @GetMapping("/id-paciente/{id}")
    PacienteResponse obtenerPacienteSinValidarEstado(@PathVariable("id") Long id);
}