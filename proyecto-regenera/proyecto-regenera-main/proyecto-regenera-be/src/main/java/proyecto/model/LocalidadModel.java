package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "localidad", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalidadModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_localidad")
    private Integer idLocalidad;

    @Column(name = "nombre_localidad", nullable = false)
    private String nombreLocalidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_provincia", nullable = false)
    private ProvinciaModel provincia;
}