package com.david.citas.mapper;

import com.david.citas.dto.CitaRequest;
import com.david.citas.dto.CitaResponse;
import com.david.citas.entity.Cita;
import com.david.commons.dto.medicos.DatosMedico;
import com.david.commons.dto.medicos.MedicoResponse;
import com.david.commons.dto.pacientes.DatosPaciente;
import com.david.commons.dto.pacientes.PacienteResponse;
import com.david.commons.mapper.CommonMapper;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper implements CommonMapper<CitaRequest, CitaResponse, Cita> {

    @Override
    public Cita requestAEntidad(CitaRequest request) {

        if (request== null ) return null;

        return Cita.crear(
                request.idPaciente(),
                request.idMedico(),
                request.fechaCita(),
                request.sintomas()
        );
    }


@Override
    public CitaResponse  entidadAResponse (Cita entidad) {
       if (entidad == null)return null;

       return new CitaResponse(
               entidad.getId(),
               null,
               null,
               entidad.getFechaCita(),
               entidad.getSintomas(),
               entidad.getEstadoCita().getDescripcion()
        );
    }

    public CitaResponse  entidadAResponse (Cita entidad, PacienteResponse paciente, MedicoResponse medico) {

        if (entidad == null)return null;

        return new CitaResponse(
                entidad.getId(),
                pacienteResponseADatosPaciente(paciente),
                medicoResponseAResponseADatosMedico(medico),
                entidad.getFechaCita(),
                entidad.getSintomas(),
                entidad.getEstadoCita().getDescripcion()
        );
    }
    private DatosPaciente pacienteResponseADatosPaciente(PacienteResponse paciente){
        if (paciente== null) return null;

        return new DatosPaciente(
                paciente.nombre(),
                paciente.numExpediente(),
                paciente.edad()+ "años",
                paciente.peso()+ "kg.",
                paciente.estatura()+"m.",
                String.join("",
                        Math.round(paciente.imc() + 100.0) / 100.0+ "",
                clasificacionIMC(paciente.imc())),
                paciente.telefono()
        );
    }

    private String clasificacionIMC(double imc){
        if (imc< 18.5) return "bajo peso";
        if (imc< 25) return " peso normsl";
        if (imc< 30) return "sobrepeso";
        if (imc< 35) return "obesidad grado 1";
        if (imc< 40) return "obesidad grado 2";

        return  "obesidad grado 3";
    }

    private DatosMedico medicoResponseAResponseADatosMedico(MedicoResponse medico){
        if (medico== null) return null;
        return new DatosMedico(
          medico.nombre(),
                medico.cedulaProfesional(),
                medico.especialidad()

        );
    }
}
