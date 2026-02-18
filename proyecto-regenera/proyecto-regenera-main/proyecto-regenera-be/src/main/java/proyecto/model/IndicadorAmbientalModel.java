package proyecto.model;

import jakarta.persistence.*;
import lombok.*;
import proyecto.enums.SentidoIndicadorEnum;
import proyecto.enums.TipoIndicadorEnum;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "indicador_ambiental", schema = "ga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndicadorAmbientalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_indicador")
    private Long idIndicador;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private UsuarioModel usuario;

    // Datos de la Medición

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_indicador", nullable = false)
    private TipoIndicadorEnum tipoIndicador;

    @Column(name = "valor_medido", nullable = false)
    private Double valorMedido; // Valor numérico

    @Column(name = "fecha_linea_base")
    private LocalDate fechaLineaBase;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "fuente_dato")
    private String fuenteDato;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    // Responsable de la Carga (Campos embebidos o planos)

    @Column(name = "resp_carga_nombre")
    private String respCargaNombre;

    @Column(name = "resp_carga_apellido")
    private String respCargaApellido;

    @Column(name = "resp_carga_cargo")
    private String respCargaCargo;

    @Column(name = "resp_carga_sector")
    private String respCargaSector;

    // Integración con Objetivos y Metas
    @Column(name = "objetivo_asociado", columnDefinition = "TEXT")
    private String objetivoAsociado;

    @Column(name = "meta_valor")
    private Double metaValor;

    @Column(name = "meta_unidad")
    private String metaUnidad;

    @Column(name = "sentido_indicador")
    @Enumerated(EnumType.STRING)
    private SentidoIndicadorEnum sentidoIndicador;

    @Column(name = "responsable_cumplimiento")
    private String responsableCumplimiento; // Persona asignada al cumplimiento del objetivo

}