package proyecto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rubro_industrial", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
public class RubroIndustrialModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rubro_industrial")
    private Long id;

    @Column(name = "rubro_industrial", nullable = false)
    private String rubroIndustrial;

}