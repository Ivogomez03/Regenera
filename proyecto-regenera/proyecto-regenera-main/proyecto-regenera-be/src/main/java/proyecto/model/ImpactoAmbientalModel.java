package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "impacto_ambiental", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactoAmbientalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_impacto_ambiental")
    private Integer idImpactoAmbiental;

    @Column(name = "impacto_ambiental", columnDefinition = "TEXT", nullable = false)
    private String impactoAmbiental;

    @Column(name = "resumido")
    private String resumido;

}
