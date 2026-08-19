package com.david.commons.enums;

import com.david.commons.exceptions.RecursoNoEncontradoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter

public enum DisponibilidadMedico {



        DISPONIBLE  (1L, " disponible para atender"),
        EN_CONSULTA(2L, " disponible para atender"),
                FUERA_DE_TURNO(3L, " disponible para atender"),
                DE_GUARDIA(4L, " disponible para atender"),
                NO_DISOINIBLE(5L, " disponible para atender");

        private final Long codigo;
        private final String descripcion;


        public static DisponibilidadMedico obtenerDisponibilidadPorCodigo(Long codigo){

            for (DisponibilidadMedico d  : values()){
                if (Objects.equals(d.codigo, codigo))
                    return d;
            }
            throw new RecursoNoEncontradoException("codigo de disponibilidad no valida :" + codigo);
        }

    }

