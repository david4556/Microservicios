package com.david.serviciob.cliente;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="servicioa")
public interface ServiceClient {


    @GetMapping
    String saludoDesdeA();
}
