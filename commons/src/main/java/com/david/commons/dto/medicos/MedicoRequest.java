package com.david.commons.dto.medicos;

import jakarta.validation.constraints.*;

public record MedicoRequest(


        @NotBlank(message = "El nombre es requerido")
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido paterno es requerido")
        @Size(min = 1, max = 50, message = "El apellido paterno debe tener entre 1 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido materno es requerido")
        @Size(min = 1, max = 50, message = "El apellido materno debe tener entre 1 y 50 caracteres")
        String apellidoMaterno,



        @NotNull(message = "La edad es requerida")
        @Min(value = 18, message = "La edad debe ser mínimo de 18 año")
        @Max(value = 100, message = "La edad debe ser máximo de 100 años")
        Short edad,




        @NotBlank(message = "El email es requerido")
        @Size(min = 1, max = 100, message = "El email debe tener entre 1 y 100 caracteres")
        @Email(message = "El email debe tener un formato válido")
        String email,

        @NotBlank(message = "El teléfono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener exactamente 10 dígitos")
        String telefono,

        @NotBlank(message = "La cedula profesional es requerido")
        @Size(min = 12, max = 12, message = "La cedula requiere exactamente 12 caracteres")
        String cedulaProfesional,

        @NotNull(message = "el id de la especialidad es requeridad ")
        @Positive(message = "el id debe ser positivo")
        Long idEspecialidad
) {
}
