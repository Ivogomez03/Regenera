package proyecto.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Entity;

@Entity
@Table(schema = "ga", name = "pais")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaisModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pais")
    private Integer idPais;

    @Column(name = "pais", nullable = false, unique = true)
    private String pais;
}
