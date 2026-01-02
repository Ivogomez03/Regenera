package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "ga", name = "tipo_empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoEmpresaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_empresa")
    private Integer idTipoEmpresa;

    @Column(name = "tipo_empresa", nullable = false, unique = true)
    private String tipoEmpresa;
}