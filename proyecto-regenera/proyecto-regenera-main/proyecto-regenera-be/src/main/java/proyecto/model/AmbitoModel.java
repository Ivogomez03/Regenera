package proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "ga", name = "ambito")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmbitoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ambito")
    private Long idAmbito;

    @Column(name = "ambito", nullable = false, unique = true, length = 20)
    private String ambito;
}
