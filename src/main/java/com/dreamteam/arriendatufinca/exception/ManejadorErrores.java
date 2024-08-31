package com.dreamteam.arriendatufinca.exception;

public class ManejadorErrores {
    private ManejadorErrores(){
        throw new IllegalStateException("Utility class");
    }

    public static final String ERROR_CALIFICADOR_NO_EXISTE = "No existe el calificador";
    public static final String ERROR_CALIFICADO_NO_EXISTE = "No existe el calificado";
    public static final String ERROR_PROPIEDAD_NO_EXISTE = "No existe la propiedad";
    public static final String ERROR_SOLICITUD_NO_EXISTE = "No existe la solicitud";
    public static final String ERROR_CALIFICACION_NO_EXISTE = "No existe la calificacion";
    public static final String ERROR_ARRENDADOR_NO_EXISTE = "No existe el arrendador";
    public static final String ERROR_ARRENDATARIO_NO_EXISTE = "No existe el arrendatario";
    public static final String ERROR_CUENTA_NO_EXISTE = "No existe la cuenta";
    public static final String ERROR_CORREO_CUENTA_NO_EXISTE = "No existe la cuenta con ese email";
    public static final String ERROR_CONTRASENA_INCORRECTA = "La contraseña ingresada es incorrecta";
    public static final String ERROR_CORREO_ARRENDATARIO_YA_EXISTE = "Ya existe un arrendatario con ese correo";
    public static final String ERROR_CORREO_ARRENDADOR_YA_EXISTE = "Ya existe un arrendador con ese correo";
    public static final String ERROR_MUNICIPIO_O_DEPARTAMENTO_INVALIDO = "El municipio o departamento es inválido";
    public static final String ERROR_ARRENDADOR_INCORRECTO = "El arrendador es incorrecto";
    public static final String ERROR_ARRENDATARIO_INCORRECTO = "El arrendatario es incorrecto";
    public static final String ERROR_PUNTAJE_INVALIDO = "La calificacion debe ser entre 0 y 5";
    public static final String ERROR_FECHA_INICIAL_SOLICITUD_INVALIDA = "La fecha de inicio debe ser mayor a la fecha actual";
    public static final String ERROR_FECHA_FINAL_SOLICITUD_INVALIDA = "La fecha final debe ser mayor a la fecha de inicio";
    public static final String ERROR_CANTIDAD_PERSONAS_SOLICITUD_INVALIDA = "La cantidad de personas debe ser mayor a 0";
    public static final String ERROR_SOLICITUD_NO_PUEDE_SER_CALIFICADA = "La solicitud no puede ser calificada";
    public static final String ERROR_TIPO_CALIFICACION_INVALIDO = "El tipo de calificacion es invalido";
    public static final String ERROR_CAMBIO_ESTADO_INVALIDO = "El cambio de estado es invalido";
    public static final String ERROR_CUENTA_REPETIDA = "Una cuenta no se puede calificar a si misma";
    public static final String ERROR_CALIFICADOR_NO_PERTENECE_SOLICITUD = "El calificador no pertenece a la solicitud";
    public static final String ERROR_CALIFICADO_NO_PERTENECE_SOLICITUD = "El calificado no pertenece a la solicitud";
    public static final String ERROR_PROPIEDAD_NO_PERTENECE_SOLICITUD = "La propiedad no pertenece a la solicitud";
}
