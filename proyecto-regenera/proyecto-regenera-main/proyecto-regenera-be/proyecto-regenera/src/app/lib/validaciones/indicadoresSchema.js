import * as yup from "yup";

const indicadoresSchema = yup.object().shape({
    // --- Datos de Medición ---
    tipoIndicador: yup.string().required("El tipo de indicador es obligatorio"),
    valorMedido: yup
        .number()
        .typeError("Debe ser un número")
        .required("El valor es obligatorio"),
    fechaLineaBase: yup.date().required("La fecha de línea base es obligatoria"),
    fechaRegistro: yup.date().required("La fecha de registro es obligatoria"),
    fuenteDato: yup.string().required("La fuente del dato es obligatoria"),
    observaciones: yup.string().nullable(),

    // --- Responsable de Carga ---
    respCargaNombre: yup.string().required("El nombre es obligatorio"),
    respCargaApellido: yup.string().required("El apellido es obligatorio"),
    respCargaCargo: yup.string().required("El cargo es obligatorio"),
    respCargaSector: yup.string().required("El sector es obligatorio"),

    // --- Integración Objetivos (Matriz) ---
    objetivoAsociado: yup.string().required("El objetivo asociado es obligatorio"),
    sentidoIndicador: yup.string().required("El sentido del indicador es obligatorio"),
    metaValor: yup
        .number()
        .typeError("Debe ser un número")
        .required("El valor de la meta es obligatorio"),
    metaUnidad: yup.string().required("La unidad de medida es obligatoria"),
    responsableCumplimiento: yup.string().required("El responsable del cumplimiento es obligatorio"),
});

export default indicadoresSchema;