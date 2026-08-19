package com.david.commons.dto.pacientes;

public record DatosPaciente(

        String nombre,
        String numExpendiente,
        String edad,
        String peso,
        String estatura,
        String imc,
        String telefono
) {
}
