package proyecto.dto;

public record GrillaDto(
                Long idItem,

                Long idActividad,

                Long idAspectoAmbiental,

                Long idImpactoAmbiental,

                Long idTipoImpacto,

                Long idCondicionImpacto,

                String sector,

                String actividad,

                String aspectoAmbiental,

                String impactoAmbiental,

                String tipoImpacto,

                String condicionImpacto,

                Integer severidad,

                Integer magnitud,

                Integer frecuencia,

                Integer reversibilidad,

                Integer valoracion,

                String impactoSignificado,
                Long idRequisitoLegalAsociado,

                String requisitoLegalAsociado,

                String control,

                String observaciones) {

}