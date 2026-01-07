package proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "ga", name = "tipo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoModel {
    // 1 Ley 2 Decreto 3 Resolución 4 Disposición
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Long idTipo;

    @Column(name = "tipo", nullable = false, unique = true, length = 20)
    private String tipo;

}
