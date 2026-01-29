package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "aspecto_ambiental_tema", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AspectoAmbientalTemaModel {
    // 1 Emisiones Gaseosas 2 Efluentes Líquidos 3 Residuos Sólidos 4 Ruido y
    // Vibraciones 5 Suelos 6 Recursos Naturales 7 Biodiversidad
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aspecto_ambiental_tema")
    private Integer idAspectoAmbientalTema;

    @Column(name = "aspecto_ambiental_tema", nullable = false)
    private String aspectoAmbientalTema;
}