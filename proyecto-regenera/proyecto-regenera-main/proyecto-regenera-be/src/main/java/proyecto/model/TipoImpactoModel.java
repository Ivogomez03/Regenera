package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_impacto", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoImpactoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_impacto")
    private Integer idTipoImpacto;

    @Column(name = "tipo_impacto", nullable = false, length = 50)
    private String tipoImpacto;

}
