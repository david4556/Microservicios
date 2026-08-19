package com.david.pacientes.entity;


import com.david.commons.enums.EstadoRegistro;

import com.david.commons.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Setter
@Entity
@Table(name = "PACIENTES")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "DIRECCION", nullable = false, length = 150)
    private String direccion;

    @Column(name = "EDAD", nullable = false)
    private Short edad;

    @Column(name = "PESO", nullable = false)
    private Double peso;

    @Column(name = "ESTATURA", nullable = false)
    private Double estatura;

    @Column(name = "IMC")
    private Double imc;

    @Column(name = "TELEFONO", nullable = false, unique = true, length = 10)
    private String telefono;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "NUM_EXPEDIENTE", nullable = false, unique = true, length = 50)
    private String numeroExpediente;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "ESTADO_REGISTRO")
    private EstadoRegistro estadoRegistro;


    public void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno,
                             String direccion, Short edad, Double peso, Double estatura,
                             String email, String telefono ) {

        StringCustomUtils.validarTamanio(nombre, 1, 50,
                "El nombre es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50,
                "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50, "El apellido materno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(direccion, 1, 150,
                "La dirección es requerida y debe tener entre 1 y 150 caracteres");

        StringCustomUtils.validarTamanio(email, 8, 100,
                "El email es requerido y debe tener entre 8 y 100 caracteres");

        StringCustomUtils.validarTamanio(telefono, 10, 10, "El teléfono debe contener exactamente 10 dígitos");
    }


    private void validarNoEliminado(){
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new IllegalArgumentException("el paciente ya esta eliminado");
    }


    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, String direccion, Short edad, Double peso, Double estatura, String email, String telefono) {

        validarDatos(
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                direccion,
                edad,
                peso,
                estatura,
                email,
                telefono
        );

        this.nombre = nombre.trim();
        this.apellidoPaterno = apellidoPaterno.trim();
        this.apellidoMaterno = apellidoMaterno.trim();
        this.direccion = direccion.trim();
        this.edad = edad;
        this.peso = peso;
        this.estatura = estatura;
        this.email = email.trim();
        this.telefono = telefono.trim();
    }

    public void calcularIMC() {
        Double estaturaCuadrada = estatura;

        this.imc = peso/(estatura * estatura);

    }
    public void eliminar(){

        validarNoEliminado();
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }


    public void generarNumeroExpediente() {

        StringBuilder expediente = new StringBuilder();

        for (char numero : telefono.toCharArray()) {
            expediente.append(numero).append("X");
        }

        this.numeroExpediente = expediente.toString();
    }



}
