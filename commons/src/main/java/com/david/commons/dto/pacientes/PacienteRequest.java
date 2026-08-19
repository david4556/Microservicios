

package com.david.commons.dto.pacientes;

import jakarta.validation.constraints.*;

public record PacienteRequest(

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
        @Min(value = 1, message = "La edad debe ser mínimo de 1 año")
        @Max(value = 100, message = "La edad debe ser máximo de 100 años")
        Short edad,

        @NotNull(message = "El peso es requerido")
        @DecimalMin(value = "0.1", message = "El peso debe ser mínimo de 0.1 kg")
        @DecimalMax(value = "200", message = "El peso debe ser máximo de 200 kg")
        Double peso,
        @NotNull(message = "La estatura es requerida")
        @DecimalMin(value = "1.0", message = "La estatura debe ser mínimo de 1.0 metros")
        @DecimalMax(value = "2.0", message = "La estatura debe ser máximo de 2.0 metros")
        Double estatura,

        @NotBlank(message = "El email es requerido")
        @Size(min = 8, max = 100, message = "El email debe tener entre 8 y 100 caracteres")
        @Email(message = "El email debe tener un formato válido")
        String email,

        @NotBlank(message = "El teléfono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener exactamente 10 dígitos")
        String telefono,


        @NotBlank(message = "La direccion es requeridad")
        @Size(min = 1, max = 150, message = "La direccion debe de ser de 1 a 150 caracteres")
        String direccion

) {
}