package proyecto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "grilla", schema = "ga")
@Getter
@Setter
public class GrillaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long idItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formulario", nullable = false)
    private FormularioModel formulario;

    @Column(name = "id_sector", nullable = false)
    private Long idSector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_actividad", nullable = false)
    private ActividadModel actividad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aspecto_ambiental", nullable = false)
    private AspectoAmbientalModel aspectoAmbiental;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_impacto_ambiental", nullable = false)
    private ImpactoAmbientalModel impactoAmbiental;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_impacto_ambiental", nullable = false)
    private TipoImpactoModel tipoImpacto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_condicion_impacto", nullable = false)
    private CondicionImpactoModel condicionImpacto;

    @Column(name = "severidad", nullable = false)
    private Integer severidad;

    @Column(name = "magnitud", nullable = false)
    private Integer magnitud;

    @Column(name = "frecuencia", nullable = false)
    private Integer frecuencia;

    @Column(name = "reversibilidad", nullable = false)
    private Integer reversibilidad;

    @Column(name = "valoracion", nullable = false)
    private Integer valoracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_impacto_significativo", nullable = false)
    private ImpactoSignificadoModel impactoSignificado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_requisito_legal_asociado", nullable = false)
    private RequisitoLegalAsociadoModel requisitoLegalAsociado;

    @Column(name = "control")
    private String control;

    @Column(name = "observaciones")
    private String observaciones;

}
