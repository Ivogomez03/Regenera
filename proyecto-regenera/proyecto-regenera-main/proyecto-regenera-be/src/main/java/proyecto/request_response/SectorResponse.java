package proyecto.request_response;

import proyecto.model.SectorModel;

public record SectorResponse(Long idSector, String sector) {
    public static SectorResponse of(SectorModel m) {
        return new SectorResponse(m.getIdSector(), m.getSector());
    }
}
