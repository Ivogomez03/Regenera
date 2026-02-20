import * as yup from "yup";

const matrizLegalSchema = yup.object().shape({
    // --- Campos Obligatorios (Los que validabas en el if) ---
    idAspectoAmbientalTema: yup
        .string()
        .required("Debes seleccionar un aspecto ambiental"),

    idAmbito: yup
        .string()
        .required("El ámbito es obligatorio"),

    idTipo: yup
        .string()
        .required("El tipo de normativa es obligatorio"),

    nro: yup
        .string()
        .required("El número de normativa es obligatorio"),

    anio: yup
        .number()
        .typeError("El año debe ser numérico")
        .required("El año es obligatorio")
        .min(1800, "Ingrese un año válido")
        .max(new Date().getFullYear() + 5, "Ingrese un año válido"),

    fecha: yup
        .date()
        .typeError("Debe ser una fecha válida")
        .required("La fecha de evaluación es obligatoria"),

    // --- Campos Opcionales ---
    // Usamos nullable() o no le ponemos .required() para que Yup permita que estén vacíos
    resena: yup
        .string()
        .nullable(),

    obligacion: yup
        .string()
        .nullable(),

    puntoControl: yup
        .string()
        .nullable(),

    idResultado: yup
        .string()
        .nullable()
});

export default matrizLegalSchema;