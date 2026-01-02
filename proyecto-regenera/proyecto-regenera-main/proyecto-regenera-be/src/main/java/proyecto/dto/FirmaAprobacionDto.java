package proyecto.dto;

import proyecto.model.FirmaAprobacionModel;

import java.time.LocalDateTime;
import java.util.Base64;

public record FirmaAprobacionDto(

        Long idFirmaAprobacion,

        // Elaboró
        String nombreElabore,
        String apellidoElabore,
        String puestoElabore,
        String firmaElabore,
        String aclaracionElabore,
        LocalDateTime fechaElabore,

        // Revisó
        String nombreReviso,
        String apellidoReviso,
        String puestoReviso,
        String firmaReviso,
        String aclaracionReviso,
        LocalDateTime fechaReviso,

        // Aprobó
        String nombreAprobo,
        String apellidoAprobo,
        String puestoAprobo,
        String firmaAprobo,
        String aclaracionAprobo,
        LocalDateTime fechaAprobo
) {
    public static FirmaAprobacionDto of(FirmaAprobacionModel f) {
        if (f == null) {
            return null;
        }
        return new FirmaAprobacionDto(
                f.getIdFirmaAprobacion(),

                f.getNombreElabore(),
                f.getApellidoElabore(),
                f.getPuestoElabore(),
                f.getFirmaElabore() != null ? Base64.getEncoder().encodeToString(f.getFirmaElabore()) : null,
                f.getAclaracionElabore(),
                f.getFechaElabore(),

                f.getNombreReviso(),
                f.getApellidoReviso(),
                f.getPuestoReviso(),
                f.getFirmaReviso() != null ? Base64.getEncoder().encodeToString(f.getFirmaReviso()) : null,
                f.getAclaracionReviso(),
                f.getFechaReviso(),

                f.getNombreAprobo(),
                f.getApellidoAprobo(),
                f.getPuestoAprobo(),
                f.getFirmaAprobo() != null ? Base64.getEncoder().encodeToString(f.getFirmaAprobo()) : null,
                f.getAclaracionAprobo(),
                f.getFechaAprobo()
        );
    }
}