package proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "ga", name = "impacto_significado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactoSignificadoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_impacto_significado")
    private Long idImpactoSignificado;

    @NotBlank
    @Column(name = "impacto_significado", nullable = false, unique = true, length = 50)
    private String impactoSignificado;

}
