package com.david.pacientes.service;

import com.david.commons.dto.pacientes.PacienteRequest;
import com.david.commons.dto.pacientes.PacienteResponse;
import com.david.commons.enums.EstadoRegistro;
import com.david.commons.exceptions.RecursoNoEncontradoException;
import com.david.pacientes.entity.Paciente;
import com.david.pacientes.mapper.PacienteMapper;
import com.david.pacientes.repository.PacienteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Slf4j
@Service
@AllArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;


    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> listar() {

        log.info("listando");

        return pacienteRepository
                .findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(pacienteMapper::entidadAResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPacientePorIdSinEstado(Long id) {

        log.info("Buscando paciente sin estado: {}", id);

        return pacienteMapper.entidadAResponse(
                pacienteRepository.findById(id)
                        .orElseThrow(() ->
                                new RecursoNoEncontradoException(
                                        "Paciente sin estado: " + id
                                )
                        )
        );
    }


    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorId(Long id) {

        return pacienteMapper.entidadAResponse(
                obtenerPacienteActivoOException(id)
        );
    }


    @Override
    public PacienteResponse registrar(PacienteRequest request) {

        log.info(
                "Registrar nuevo paciente {}",
                request.nombre()
        );

        Paciente paciente = pacienteMapper.requestAEntidad(request);
        paciente.setEstadoRegistro(EstadoRegistro.ACTIVO);

        paciente.calcularIMC();

        paciente.generarNumeroExpediente();

        pacienteRepository.save(paciente);

        log.info(
                "Nuevo paciente: {}",
                paciente.getNombre()
        );

        return pacienteMapper.entidadAResponse(paciente);
    }


    @Override
    public PacienteResponse actualizar(
            PacienteRequest request,
            Long id) {

        Paciente paciente =
                obtenerPacienteActivoOException(id);

        log.info(
                "Actualizando paciente: {}",
                id
        );

        paciente.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.direccion(),
                request.edad(),
                request.peso(),
                request.estatura(),
                request.email(),
                request.telefono()

        );

        paciente.calcularIMC();

        paciente.generarNumeroExpediente();

        log.info("Paciente actualizado");

        return pacienteMapper.entidadAResponse(paciente);
    }


    @Override
    public void eliminar(Long id) {

        Paciente paciente =
                obtenerPacienteActivoOException(id);

        log.info(
                "Eliminando paciente: {}",
                id
        );

        paciente.eliminar();

        log.info(
                "Eliminado exitoso: {}",
                id
        );
    }


    private Paciente obtenerPacienteActivoOException(Long id) {

        log.info(
                "Buscando: {}",
                id
        );

        return pacienteRepository
                .findByIdAndEstadoRegistro(
                        id,
                        EstadoRegistro.ACTIVO
                )
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Paciente no encontrado: " + id
                        )
                );
    }
}