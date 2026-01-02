package proyecto.request_response;

import java.time.LocalDateTime;

public record FirmaAprobacionCreateRequest(

        // Elaboró
        String nombreElabore,
        String apellidoElabore,
        String puestoElabore,
        String firmaElabore,        // En base64
        String aclaracionElabore,
        LocalDateTime fechaElabore,

        // Revisó
        String nombreReviso,
        String apellidoReviso,
        String puestoReviso,
        String firmaReviso,         // En base64
        String aclaracionReviso,
        LocalDateTime fechaReviso,

        // Aprobó
        String nombreAprobo,
        String apellidoAprobo,
        String puestoAprobo,
        String firmaAprobo,         // En base64
        String aclaracionAprobo,
        LocalDateTime fechaAprobo

) {
}