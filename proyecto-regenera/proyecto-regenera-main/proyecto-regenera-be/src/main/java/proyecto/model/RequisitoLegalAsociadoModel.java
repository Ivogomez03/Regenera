package proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(schema = "ga", name = "requisito_legal_asociado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class RequisitoLegalAsociadoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_requisito_legal_asociado")
    private Long idRequisitoLegalAsociado;

    @NotBlank
    @Column(name = "requisito_legal_asociado", nullable = false, unique = true, length = 10)
    private String requisitoLegalAsociado;
}
