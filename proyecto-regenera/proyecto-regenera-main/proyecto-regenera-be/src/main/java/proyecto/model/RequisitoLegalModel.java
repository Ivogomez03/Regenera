package proyecto.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "ga", name = "requisito_legal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequisitoLegalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_requisito_legal")
    private Long idRequisitoLegal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ambito", nullable = false)
    private AmbitoModel ambito;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tipo", nullable = false)
    private TipoModel tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_resultado")
    private ResultadoModel resultado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_aspecto_ambiental", nullable = false) // nombre de la columna real en la tabla
    private AspectoAmbientalModel aspecto;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "resena")
    private String resena;

    @Column(name = "obligacion")
    private String obligacion;

    @Column(name = "punto_inspeccion")
    private String puntoInspeccion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioModel usuario;
}