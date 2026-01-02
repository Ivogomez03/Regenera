package proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "ga", name = "resultado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado")
    private Long idResultado;

    @Column(name = "resultado", nullable = false, unique = true, length = 20)
    private String resultado;

}