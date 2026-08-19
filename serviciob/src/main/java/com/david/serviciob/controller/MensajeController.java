package com.david.serviciob.controller;


import com.david.serviciob.cliente.ServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MensajeController {
    private final ServiceClient servicioACliente;

    public MensajeController(ServiceClient servicioAClient){
        this.servicioACliente = servicioAClient;
    }

    @GetMapping

    public ResponseEntity<String> mensaje(){
        return ResponseEntity.ok( "Servicio a dice: Hola mundo 2");
    }

    @GetMapping("/servicio-a")

    public ResponseEntity<String> saludoDesdeA(){
        return ResponseEntity.ok( " COnsumiendo desde Servicio a dice" +servicioACliente.saludoDesdeA());
    }
}
