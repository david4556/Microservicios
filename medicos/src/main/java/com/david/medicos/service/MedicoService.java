package com.david.medicos.service;

import com.david.commons.dto.medicos.MedicoRequest;
import com.david.commons.dto.medicos.MedicoResponse;
import com.david.commons.service.CrudService;

public interface MedicoService  extends CrudService<MedicoRequest, MedicoResponse> {
    MedicoResponse obtenerMedicoPorIdSinEstado(Long id);


    void actualizarDisponibilidadMedico(Long idMedico, Long idDisponibilidad);
}
