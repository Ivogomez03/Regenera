package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "aspecto_ambiental", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AspectoAmbientalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aspecto_ambiental")
    private Integer idAspectoAmbiental;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria_aspecto_ambiental", nullable = false)
    private CategoriaAspectoAmbientalModel categoria;

    @Column(name = "aspecto_ambiental", nullable = false)
    private String aspectoAmbiental;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_impacto_ambiental", nullable = false)
    private ImpactoAmbientalModel impacto;
}
