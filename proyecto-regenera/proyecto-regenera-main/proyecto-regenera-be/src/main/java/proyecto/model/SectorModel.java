package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sector", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sector")
    private Long idSector;

    @Column(name = "sector", nullable = false)
    private String sector;
}
