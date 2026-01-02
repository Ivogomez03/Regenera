package proyecto.dto;

import lombok.*;
import proyecto.model.EmpresaModel;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaDto {

    private Long idEmpresa;
    private String nombre;
    private String apellido;
    private String email;
    private String numeroCelular;
    private String cargo;
    private String nombreEmpresa;

    private Integer idPais;
    private String pais;

    private Integer idTamanioEmpresa;
    private String tamanioEmpresa;

    private Integer idTipoEmpresa;
    private String tipoEmpresa;

    private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;

    public static EmpresaDto of(EmpresaModel model) {
        return EmpresaDto.builder()
                .idEmpresa(model.getIdEmpresa())
                .nombre(model.getNombre())
                .apellido(model.getApellido())
                .email(model.getEmail())
                .numeroCelular(model.getNumeroCelular())
                .cargo(model.getCargo())
                .nombreEmpresa(model.getNombreEmpresa())
                .idPais(model.getPais().getIdPais())
                .pais(model.getPais().getPais())
                .idTamanioEmpresa(model.getTamanioEmpresa().getIdTamanioEmpresa())
                .tamanioEmpresa(model.getTamanioEmpresa().getTamanioEmpresa())
                .idTipoEmpresa(model.getTipoEmpresa().getIdTipoEmpresa())
                .tipoEmpresa(model.getTipoEmpresa().getTipoEmpresa())
                .creadoEn(model.getCreadoEn())
                .actualizadoEn(model.getActualizadoEn())
                .build();
    }
}
