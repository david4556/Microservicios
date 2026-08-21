package com.david.citas.service;

import com.david.citas.dto.CitaRequest;
import com.david.citas.dto.CitaResponse;
import com.david.citas.entity.Cita;
import com.david.citas.enums.EstadoCita;
import com.david.citas.mapper.CitaMapper;
import com.david.citas.repository.CitaRepository;
import com.david.commons.client.MedicoClient;
import com.david.commons.client.PacienteClient;
import com.david.commons.dto.medicos.MedicoResponse;
import com.david.commons.dto.pacientes.PacienteResponse;
import com.david.commons.enums.DisponibilidadMedico;
import com.david.commons.enums.EstadoRegistro;
import com.david.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final MedicoClient medicoClient;
    private final PacienteClient pacienteClient;

    private static final List<EstadoCita> ESTADOS_ACTIVOS = List.of(
            EstadoCita.PENDIENTE,
            EstadoCita.CONFIRMADA,
            EstadoCita.EN_CURSO
    );
    private static final List<EstadoCita> ESTADOS_PENDIENTE_O_CONFIRMADA = List.of(
            EstadoCita.PENDIENTE,
            EstadoCita.CONFIRMADA
    );
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        log.info("Listando todas las citas activas");
        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(this::mapearCitaConDetalles)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponse obtenerPorId(Long id) {
        return mapearCitaConDetalles(buscarCitaActivaOExcepcion(id));
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        log.info("Registrando nueva cita");

        PacienteResponse paciente = pacienteClient.obtenerPacienteActivo(request.idPaciente());
        validarSinCitaActivaPaciente(request.idPaciente(), null);

        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());
        validarMedicoDisponible(medico);
        validarSinCitaActivaMedico(request.idMedico(), null);

        Cita cita = citaRepository.save(citaMapper.requestAEntidad(request));
        actualizarDisponibilidadMedico(medico.id(), DisponibilidadMedico.NO_DISPONIBLE.getCodigo());

        log.info("Cita registrada exitosamente");
        return citaMapper.entidadAResponse(cita, paciente, medico);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneCitaConfirmadaOEnCursoMedico(Long idMedico) {
        return citaRepository.existsByIdMedicoAndEstadoCitaIn(
                idMedico,
                List.of(EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO)
        );
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, Long id) {
        log.info("Actualizando cita con ID: {}", id);

        Cita cita = buscarCitaActivaOExcepcion(id);

        if (!cita.getEstadoCita().isActualizable()) {
            throw new IllegalStateException("La cita solo puede actualizarse si está permitida por su estado actual");
        }

        PacienteResponse paciente = pacienteClient.obtenerPacienteActivo(request.idPaciente());
        validarSinCitaActivaPaciente(request.idPaciente(), id);

        gestionarCambioMedicoSiAplica(cita, request.idMedico(), id);

        cita.actualizar(request.idPaciente(), request.idMedico(), request.fechaCita(), request.sintomas());

        log.info("Cita actualizada correctamente");
        return citaMapper.entidadAResponse(cita, paciente, obtenerMedicoActivo(request.idMedico()));
    }

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {
        Cita cita = buscarCitaActivaOExcepcion(idCita);
        EstadoCita nuevoEstado = EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita);

        cita.actualizarEstadoCita(nuevoEstado);
        gestionarDisponibilidadPorCambioEstado(cita.getIdMedico(), nuevoEstado);

        log.info("Estado de cita {} actualizado a {}", idCita, nuevoEstado);
    }

    @Override
    public void eliminar(Long id) {
        Cita cita = buscarCitaActivaOExcepcion(id);

        // Uso directo del getter provisto por tu enum
        if (!cita.getEstadoCita().isEliminable()) {
            throw new IllegalStateException("La cita no puede eliminarse en su estado actual");
        }

        if (cita.getEstadoCita() == EstadoCita.PENDIENTE) {
            actualizarDisponibilidadMedico(cita.getIdMedico(), DisponibilidadMedico.DISPONIBLE.getCodigo());
        }

        cita.eliminar();
        log.info("Cita {} eliminada lógicamente", id);
    }


    private void gestionarCambioMedicoSiAplica(Cita citaActual, Long nuevoIdMedico, Long idCita) {
        if (citaActual.getIdMedico().equals(nuevoIdMedico)) {
            return;
        }

        MedicoResponse medicoNuevo = obtenerMedicoActivo(nuevoIdMedico);
        validarMedicoDisponible(medicoNuevo);
        validarSinCitaActivaMedico(nuevoIdMedico, idCita);

        actualizarDisponibilidadMedico(citaActual.getIdMedico(), DisponibilidadMedico.DISPONIBLE.getCodigo());
        actualizarDisponibilidadMedico(nuevoIdMedico, DisponibilidadMedico.NO_DISPONIBLE.getCodigo());
    }

    private void gestionarDisponibilidadPorCambioEstado(Long idMedico, EstadoCita estado) {

        Long nuevaDisponibilidad = switch (estado) {
            case FINALIZADA, CANCELADA -> DisponibilidadMedico.DISPONIBLE.getCodigo();
            case EN_CURSO -> DisponibilidadMedico.EN_CONSULTA.getCodigo();
            default -> null;
        };

        if (nuevaDisponibilidad != null) {
            actualizarDisponibilidadMedico(idMedico, nuevaDisponibilidad);
        }
    }

    private Cita buscarCitaActivaOExcepcion(Long id) {
        return citaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cita no encontrada con ID: " + id));
    }

    private CitaResponse mapearCitaConDetalles(Cita cita) {
        return citaMapper.entidadAResponse(
                cita,
                pacienteClient.obtenerPacienteSinValidarEstado(cita.getIdPaciente()),
                medicoClient.obternerMedicoActivarPorIdSinEstado(cita.getIdMedico())
        );
    }

    private void validarSinCitaActivaPaciente(Long idPaciente, Long idCitaExcluir) {
        boolean tieneCita = (idCitaExcluir == null)
                ? citaRepository.existsByIdPacienteAndEstadoCitaIn(idPaciente, ESTADOS_ACTIVOS)
                : citaRepository.existsByIdPacienteAndEstadoCitaInAndIdNot(idPaciente, ESTADOS_PENDIENTE_O_CONFIRMADA, idCitaExcluir);

        if (tieneCita) {
            throw new IllegalStateException("El paciente ya tiene una cita activa");
        }
    }

    private void validarSinCitaActivaMedico(Long idMedico, Long idCitaExcluir) {
        boolean tieneCita = (idCitaExcluir == null)
                ? citaRepository.existsByIdMedicoAndEstadoCitaIn(idMedico, ESTADOS_ACTIVOS)
                : citaRepository.existsByIdMedicoAndEstadoCitaInAndIdNot(idMedico, ESTADOS_PENDIENTE_O_CONFIRMADA, idCitaExcluir);

        if (tieneCita) {
            throw new IllegalStateException("El médico ya tiene una cita activa");
        }
    }

    private MedicoResponse obtenerMedicoActivo(Long id) {
        return medicoClient.obternerMedicoActivarPorId(id);
    }

    private void actualizarDisponibilidadMedico(Long id, Long disponibilidad) {
        medicoClient.actualizarDisponibilidadMedico(id, disponibilidad);
    }

    private void validarMedicoDisponible(MedicoResponse medico) {
        if (medico.idDisponibilidad() != DisponibilidadMedico.DISPONIBLE.getCodigo()) {
            throw new IllegalStateException("El médico no está disponible");
        }
    }
}