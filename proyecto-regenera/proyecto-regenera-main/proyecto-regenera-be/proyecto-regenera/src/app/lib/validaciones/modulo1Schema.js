
import * as yup from "yup";

const modulo1Schema = yup.object().shape({
  nombreRazonSocial: yup.string().max(255, "El nombre es demasiado largo"),
  cuit: yup.string().matches(/^\d{11}$/, "CUIT inválido"),
  domicilioLegal: yup.string().max(255, "El domicilio es demasiado largo"),
  domicilioOperativo: yup.string().max(255, "El domicilio es demasiado largo"),
  correoElectronico: yup.string().max(50, "El email es demasiado largo"),
  telefono: yup.string().max(20, "El teléfono es demasiado largo"),

  declaracionAlcanceSga: yup.string().max(5000, "La declaración es demasiado larga"),
  declaracionPoliticaAmbiental: yup.string().max(5000, "La declaración es demasiado larga"),
  fechaUltimaActualizacionPolitica: yup.date().max(new Date(), "La fecha debe ser anterior o igual a hoy"),

  autoridadSocietaria: yup.object().shape({
    nombre: yup.string().max(50, "El nombre es demasiado largo"),
    apellido: yup.string().max(50, "El apellido es demasiado largo"),
    correoElectronico: yup.string().max(50, "El email es demasiado largo"),
    telefono: yup.string().max(20, "El teléfono es demasiado largo"),
    cargo: yup.string().max(50, "El cargo es demasiado largo"),
    asignacionResponsabilidades: yup.string().max(1000, "El texto es demasiado largo"),
  }),

  responsableSga: yup.object().shape({
    nombre: yup.string().max(50, "El nombre es demasiado largo"),
    apellido: yup.string().max(50, "El apellido es demasiado largo"),
    correoElectronico: yup.string().max(50, "El email es demasiado largo"),
    telefono: yup.string().max(20, "El teléfono es demasiado largo"),
    cargo: yup.string().max(50, "El cargo es demasiado largo"),
    asignacionResponsabilidades: yup.string().max(1000, "El texto es demasiado largo"),
  }),
});

export default modulo1Schema;