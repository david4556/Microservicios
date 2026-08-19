package com.david.citas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CitaRequest(
        @NotNull(message = "el id del paciente es requerida")
                          @Positive(message = "El id del paciente debe ser positivo")
                          Long idPaciente,

                          @NotNull(message = "el id del medico es requerida")
                          @Positive(message = "El id del medico debe ser positivo")
                          Long idMedico,

                          @NotNull(message = "la fecha de la cita  es requerida")
                          @FutureOrPresent(message = "la fecha de la cita debe ser futura")
                          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern =  "dd/MM/yyyy HH:mm")
                          LocalDateTime fechaCita,

                          @NotNull(message = "los sintomas del paciente  es requerida")
                          @Size( min = 20, max = 500,
                                  message = "la descripcion debe tener de 20 a 500 ")
                          String sintomas) {
}
