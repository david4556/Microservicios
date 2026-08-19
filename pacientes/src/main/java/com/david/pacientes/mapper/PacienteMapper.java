package com.david.pacientes.mapper;

import com.david.commons.dto.pacientes.PacienteRequest;
import com.david.commons.dto.pacientes.PacienteResponse;
import com.david.pacientes.entity.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public Paciente requestAEntidad(PacienteRequest request) {

        if (request == null) {
            return null;
        }

        return Paciente.builder()
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .direccion(request.direccion().trim())
                .edad(request.edad())
                .peso(request.peso())
                .estatura(request.estatura())
                .email(request.email().trim())
                .telefono(request.telefono().trim())
                .build();
    }

    public PacienteResponse entidadAResponse(Paciente paciente) {

        if (paciente == null) {
            return null;
        }

        String nombreCompleto = String.join(" ",
                paciente.getNombre(),
                paciente.getApellidoPaterno(),
                paciente.getApellidoMaterno()
        );

        return new PacienteResponse(
                paciente.getId(),
                nombreCompleto,
                paciente.getEdad(),
                paciente.getPeso(),
                paciente.getEstatura(),
                paciente.getImc(),
                paciente.getEmail(),
                paciente.getTelefono(),
                paciente.getDireccion(),
                paciente.getNumeroExpediente()
        );
    }
}