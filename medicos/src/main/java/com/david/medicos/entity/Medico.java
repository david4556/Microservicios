package com.david.medicos.entity;

import com.david.commons.enums.DisponibilidadMedico;
import com.david.commons.enums.EspecialidaMediico;
import com.david.commons.enums.EstadoRegistro;
import com.david.commons.utils.StringCustomUtils;
import com.david.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
@Setter
@Entity
@Table(name = "MEDICOS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medico {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID_MEDICO")
        private Long id;

        @Column(name = "NOMBRE", nullable = false, length = 50)
        private String nombre;

        @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
        private String apellidoPaterno;

        @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
        private String apellidoMaterno;
         @Column(name = "EDAD", nullable = false)
        private Short edad;


         @Column(name = "EMAIL", nullable = false,  length = 100)
         private String email;

        @Column(name = "TELEFONO", nullable = false,  length = 10)
        private String telefono;

         @Column(name = "CEDULA_PROFESIONAL", nullable = false,  length = 12)
        private String cedulaProfesional;

         @Enumerated(EnumType.STRING)
         @Column(name = "ESPECIALIDAD", nullable = false)
         private EspecialidaMediico especialidad;

        @Enumerated(EnumType.STRING)
        @Column(name = "DISPONIBILIDAD", nullable = false)
        private DisponibilidadMedico disponibilidad;

        @Enumerated(EnumType.STRING)
        @Column(name = "ESTADO_REGISTRO", nullable = false)
        private EstadoRegistro estadoRegistro;












        public void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad,
                                 String email, String telefono,
                                 String cedulaProfesional, EspecialidaMediico especialidad) {

            StringCustomUtils.validarTamanio(nombre, 1, 50,
                    "El nombre es requerido y debe tener entre 1 y 50 caracteres");

            StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50,
                    "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres");

            StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50, "El apellido materno es requerido y debe tener entre 1 y 50 caracteres");

            StringCustomUtils.validarTamanio(email, 1, 100,
                    "El email es requerido y debe tener entre 8 y 100 caracteres");

            StringCustomUtils.validarTamanio(telefono, 10, 10, "El teléfono debe contener exactamente 10 dígitos");

            StringCustomUtils.validarTamanio(cedulaProfesional, 12, 12, "La cedulaa debe contener exactamente 10 dígitos");

            ValoresNumericosUtils.validarRangoShort(edad, (short) 18, (short) 100,
                    "La edad es requerida y debe tener ser entre 18 y 100 años");

            if (especialidad == null)
                throw new IllegalArgumentException("La especialidad es requerida");
        }



        private void validarNoEliminado(){
            if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
                throw new IllegalArgumentException("el medico ya esta eliminado");
        }

        public void actualizarEspecialidad(EspecialidaMediico especialidad){

            validarNoEliminado();

            if (especialidad == null)
            throw new IllegalArgumentException("la especialidad es requerida");
            this.especialidad = especialidad;
        }


    public void actualizarDisponibilidad(DisponibilidadMedico disponibilidad){

        validarNoEliminado();

        if (disponibilidad== null)
            throw new IllegalArgumentException("la disponibilidad es requerida");
        this.disponibilidad = disponibilidad;
    }

        public void eliminar(){

            validarNoEliminado();
            this.estadoRegistro = EstadoRegistro.ELIMINADO;
        }


    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, Short edad,
                             String email, String telefono,
                             String cedulaProfesional, EspecialidaMediico especialidad) {

        validarDatos(
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                edad,
                email,
                telefono,
                cedulaProfesional,
                especialidad
        );


        actualizarEspecialidad(especialidad);

        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.edad = edad;
        this.email = email.trim();
        this.telefono = telefono.trim();
        this.cedulaProfesional = cedulaProfesional.trim();
    }








}


