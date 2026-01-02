package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "provincia", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProvinciaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_provincia")
    private Integer idProvincia;

    @Column(name = "nombre_provincia", nullable = false)
    private String nombreProvincia;

}