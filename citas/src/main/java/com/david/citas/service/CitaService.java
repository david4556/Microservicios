package com.david.citas.service;

import com.david.citas.dto.CitaRequest;
import com.david.citas.dto.CitaResponse;
import com.david.commons.service.CrudService;

public interface CitaService  extends CrudService<CitaRequest, CitaResponse> {

    void actualizarEstadoCita(Long idCita, Long idEstadoCita);

    boolean tieneCitaConfirmadaOEnCursoMedico(Long idMedico);

}