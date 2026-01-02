package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "condicion_impacto", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CondicionImpactoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_condicion_impacto")
    private Integer idCondicionImpacto;

    @Column(name = "condicion_impacto", nullable = false, length = 100)
    private String condicionImpacto;
}
