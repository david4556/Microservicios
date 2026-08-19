package com.david.servicioa.controller;


import com.david.servicioa.client.ServicioBClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaludoController {



    private final ServicioBClient servicioBClient;

    public SaludoController(ServicioBClient servicioBClient) {
        this.servicioBClient = servicioBClient;
    }

    @GetMapping
    public ResponseEntity<String>saludo(){
        return ResponseEntity.ok( "Servicio a dice: Hola mundo");
    }

    @GetMapping ("/servicio-b")
    public ResponseEntity<String>mensajeDesdeB(){
        return ResponseEntity.ok( "Servicio a dice: Hola mundo" +servicioBClient.mensajeDesdeB());
    }
}
