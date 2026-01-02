package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_aspecto_ambiental", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaAspectoAmbientalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria_aspecto_ambiental")
    private Integer id;

    @Column(name = "categoria_aspecto_ambiental", nullable = false, unique = true)
    private String categoriaAspectoAmbiental;
}