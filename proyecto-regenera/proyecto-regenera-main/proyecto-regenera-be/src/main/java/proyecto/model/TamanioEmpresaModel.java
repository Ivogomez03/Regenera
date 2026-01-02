package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "ga", name = "tamanio_empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TamanioEmpresaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tamanio_empresa")
    private Integer idTamanioEmpresa;

    @Column(name = "tamanio_empresa", nullable = false, unique = true)
    private String tamanioEmpresa;
}






























































































