package com.david.pacientes.service;

import com.david.commons.client.CitaClient;
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

    private final CitaClient citaClient;


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

        log.info("Buscando paciente sin validar estado: {}", id);

        return pacienteMapper.entidadAResponse(pacienteRepository.findById(id)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado: " + id))
        );
    }


    @Override
    @Transactional(readOnly = true)
    public PacienteResponse obtenerPorId(Long id) {

        return pacienteMapper.entidadAResponse(obtenerPacienteActivoOException(id)
        );
    }


    @Override
    public PacienteResponse registrar(
            PacienteRequest request) {

        log.info("Registrando nuevo paciente: {}",
                request.nombre()
        );

        Paciente paciente =
                pacienteMapper.requestAEntidad(request);

        paciente.validarDatos(
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

        paciente.setEstadoRegistro(EstadoRegistro.ACTIVO);

        paciente.calcularIMC();

        paciente.generarNumeroExpediente();

        pacienteRepository.save(paciente);

        log.info("Paciente registrado: {}", paciente.getId());

        return pacienteMapper.entidadAResponse(
                paciente
        );
    }


    @Override
    public PacienteResponse actualizar(PacienteRequest request, Long id) {

        Paciente paciente = obtenerPacienteActivoOException(id);


        if (citaClient.tieneCitaConfirmadaOEnCurso(id)) {
            throw new IllegalStateException(
                    "No se puede actualizar el paciente porque tiene una cita CONFIRMADA o EN_CURSO"
            );
        }
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

        log.info("Paciente actualizado: {}", id);

        return pacienteMapper.entidadAResponse(paciente);
    }


    @Override
    public void eliminar(Long id) {

        Paciente paciente = obtenerPacienteActivoOException(id);

        if (citaClient.tieneCitaConfirmadaOEnCurso(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar el paciente porque tiene una cita CONFIRMADA o EN_CURSO"
            );
        }

        paciente.eliminar();

        log.info("Paciente eliminado lógicamente: {}", id);
    }


    private Paciente obtenerPacienteActivoOException(Long id) {

        return pacienteRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() -> new RecursoNoEncontradoException("El paciente con ID " + id + " no existe o no está activo"));
    }
}