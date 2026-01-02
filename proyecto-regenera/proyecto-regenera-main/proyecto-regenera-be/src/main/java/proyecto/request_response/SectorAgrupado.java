package proyecto.request_response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SectorAgrupado {

    Long idSector;

    String nombreSector;

    Integer totalItems;

    List<GrillaResponse> items;

}
