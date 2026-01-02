package proyecto.dto;

import proyecto.model.FormularioModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

public record FormularioDto(
        Long idFormulario,
        Long usuarioId,
        String nombreEmpresa,
        String codigo,
        LocalDate fecha,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn,
        Byte logoEmpresa
) {
    public static FormularioDto of(FormularioModel f){
        return new FormularioDto(
                f.getIdFormulario(),
                (f.getUsuario()!=null ? f.getUsuario().getId() : null),
                f.getNombreEmpresa(),
                f.getCodigo(),
                f.getFecha(),
                f.getCreadoEn(),
                f.getActualizadoEn(),
                f.getLogoEmpresa() != null ? Byte.valueOf(Base64.getEncoder().encodeToString(f.getLogoEmpresa())) : null

        );
    }
}