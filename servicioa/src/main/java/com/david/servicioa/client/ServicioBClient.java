package com.david.servicioa.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name= "serviciob")
public interface ServicioBClient {


    @GetMapping
    String mensajeDesdeB();
}
