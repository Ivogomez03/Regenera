package proyecto.request_response;

import proyecto.dto.GrillaDto;

import java.time.LocalDate;
import java.util.List;

public record FormularioCreateRequest(

        Long idSector,

        String nombreEmpresa,

        String codigo,

        LocalDate fecha,

        List<GrillaDto> items,

        String logoEmpresa
) {

}