package com.david.citas.service;

import com.david.citas.dto.CitaRequest;
import com.david.citas.dto.CitaResponse;
import com.david.citas.entity.Cita;
import com.david.citas.enums.EstadoCita;
import com.david.citas.mapper.CitaMapper;
import com.david.citas.repository.CitaRepository;
import com.david.commons.client.MedicoClient;
import com.david.commons.dto.medicos.MedicoResponse;
import com.david.commons.dto.pacientes.PacienteResponse;
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



    @Override
    public List<CitaResponse> listar() {

        log.info("listandoo todas las citas activas ");

        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map( cita -> citaMapper.entidadAResponse(
                        cita,
                        null,
                        obtenerMedicoSinEstado(cita.getIdMedico())
                )).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponse obtenerPorId(Long id) {
        Cita cita = obtenerCitaOException(id);

        return citaMapper.entidadAResponse(
                cita,
                null,
                obtenerMedicoSinEstado(cita.getIdMedico())
        );
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {

        log.info("Registrando nueva cita");

        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());


        Cita cita = citaMapper.requestAEntidad(request);

        citaRepository.save(cita);

        log.info("Cita registrada exitosamente");

        return citaMapper.entidadAResponse(
                cita,
                null,
                medico
        );
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, Long id) {
        Cita cita = obtenerCitaOException(id);

        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());

        log.info("Actualizando cita con id {}", id);

        cita.actualizar(
                request.idPaciente(),
                request.idMedico(),
                request.fechaCita(),
                request.sintomas()
        );

        log.info("Cita actualizada con id: {}", id);

        return citaMapper.entidadAResponse(
                cita,
                null,
                medico
        );
    }

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {
        Cita cita = obtenerCitaOException(idCita);

        log.info("actualizando estado de la cita con id {}", idCita);

        cita.actualizarEstaoCita(EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita));

        log.info("Estado de la cita {} actualizado correctamente", idCita);

    }


    @Override
    public void eliminar(Long id) {
        Cita cita = obtenerCitaOException(id);

        log.info("actualizando estado de la cita con id {}", id);

        cita.eliminar();

        log.info("Estado de la cita {} actualizado correctamente", id);


    }
    private Cita obtenerCitaOException(Long id){

        log.info("Buscando cita con id :{}", id);

        return citaRepository.findById(id).orElseThrow(()->
                new RecursoNoEncontradoException("lista no encontrada" +id));
    }

    private MedicoResponse obtenerMedicoActivo(Long id){
        log.info("Buscandp medico activo con id {} en el servicio", id);

        return medicoClient.obternerMedicoActivarPorId(id);
    }


    private MedicoResponse obtenerMedicoSinEstado(Long id){
        log.info("Buscandp medico sin estado con id {} en el servicio", id);

        return medicoClient.obternerMedicoActivarPorIdSinEstado(id);
    }
}
