package com.david.medicos.service;

import com.david.commons.dto.medicos.MedicoRequest;
import com.david.commons.dto.medicos.MedicoResponse;
import com.david.commons.enums.DisponibilidadMedico;
import com.david.commons.enums.EspecialidaMediico;
import com.david.commons.enums.EstadoRegistro;
import com.david.commons.exceptions.RecursoNoEncontradoException;
import com.david.medicos.entity.Medico;
import com.david.medicos.mapper.MedicoMapper;
import com.david.medicos.repository.MedicoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Slf4j
@Service
@AllArgsConstructor
public class MedicoServiceImpl  implements MedicoService{

    private final MedicoRepository medicoRepository;

    private final MedicoMapper medicoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<MedicoResponse> listar() {

        log.info("listando");
        return medicoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(medicoMapper::entidadAResponse).toList();
    }


    @Override
    @Transactional(readOnly = true)
    public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {

        log.info("Buscando medico sin estado:{}", id);
        return medicoMapper.entidadAResponse(medicoRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException("Medico sin estado:" +id)));
    }

    @Override

    public void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad) {


        Medico medico =obtenerMedicoActivoOException(idMedico);

        log.info("Actualizando medico:{}", idMedico);
        DisponibilidadMedico nuevaDisponibilidad = DisponibilidadMedico
                .obtenerDisponibilidadPorCodigo(idDisponibilidad);

        DisponibilidadMedico disponibilidadAnterior = medico.getDisponibilidad();

        medico.actualizarDisponibilidad(nuevaDisponibilidad);
        log.info("disponibilidad cambio", idMedico, disponibilidadAnterior, nuevaDisponibilidad);

    }


    @Override
    @Transactional(readOnly = true)
    public MedicoResponse obtenerPorId(Long id) {

        return medicoMapper.entidadAResponse(obtenerMedicoActivoOException(id));
    }

    @Override

    public MedicoResponse registrar(MedicoRequest request) {

        log.info("Registrar nuevo medico {}" , request.nombre());

        validarDatosUnicos(request);

        Medico medico = medicoMapper.requestAEntidad(request);

                medico.actualizarEspecialidad(
                        EspecialidaMediico.obtenerEspecialidadPorCodigo(request.idEspecialidad()));
                        medicoRepository.save(medico);

                        log.info("nuevo medico :{}", medico.getNombre());

                        return medicoMapper.entidadAResponse(medico);


    }

    @Override
    public MedicoResponse actualizar(MedicoRequest request, Long id) {
        Medico medico = obtenerMedicoActivoOException(id);

        log.info("Actualizando medico:{}", id);

        validarCambiosUnicos(request, id );

        medico.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad(),
                request.email(),
                request.telefono(),
                request.cedulaProfesional(),
                EspecialidaMediico. obtenerEspecialidadPorCodigo(request.idEspecialidad()));

                log.info("medico actualiado");
                return medicoMapper.entidadAResponse(medico);


    }

    @Override
    public void eliminar(Long id) {
        Medico medico =obtenerMedicoActivoOException(id);

        log.info("Eliminando medico:{}", id);
medico.eliminar();
log.info("eliminado exitoso", id);

    }

    private Medico obtenerMedicoActivoOException(Long id){

        log.info("Buscando:{}", id);

        return medicoRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("medico no encontrado :" +id));
    }



    private void validarDatosUnicos(MedicoRequest request){

        log.info("validando datos ");


        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistro(
                request.email().trim(), EstadoRegistro.ACTIVO
        )) throw new IllegalArgumentException("ya existe un medico activo" +request.email());


        log.info("validando telefono unico ");


        if (medicoRepository.existsByTelefonoAndEstadoRegistro(
                request.telefono().trim(), EstadoRegistro.ACTIVO
        )) throw new IllegalArgumentException("ya existe un telefono  activo" +request.telefono());


        log.info("validando cedula unico ");


        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistro(
                request.cedulaProfesional().trim(), EstadoRegistro.ACTIVO
        )) throw new IllegalArgumentException("ya existe un  medco con cedula profecional   activo" +request.cedulaProfesional());

    }



    private void validarCambiosUnicos(MedicoRequest request, Long id){

        log.info("validando cambio de email  ");


        if (medicoRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
                request.email().trim(), EstadoRegistro.ACTIVO, id))
            throw new IllegalArgumentException("ya existe un medico activo" +request.email());


        log.info("validando telefono unico ");


        if (medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(
                request.telefono().trim(), EstadoRegistro.ACTIVO,id
        )) throw new IllegalArgumentException("ya existe un telefono  activo" +request.telefono());


        log.info("validando cedula unico ");


        if (medicoRepository.existsByCedulaProfesionalIgnoreCaseAndEstadoRegistroAndIdNot(
                request.cedulaProfesional().trim(), EstadoRegistro.ACTIVO, id
        )) throw new IllegalArgumentException("ya existe un  medco con cedula profecional   activo" +request.cedulaProfesional());

    }
}
