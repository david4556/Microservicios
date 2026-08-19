package com.david.commons.client;

import com.david.commons.dto.medicos.MedicoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "medicos")
public interface MedicoClient {

    @GetMapping("/{id}")
    MedicoResponse obternerMedicoActivarPorId(@PathVariable Long id);


    @GetMapping("/{id-medico/{id}")
    MedicoResponse obternerMedicoActivarPorIdSinEstado(@PathVariable Long id);
}
