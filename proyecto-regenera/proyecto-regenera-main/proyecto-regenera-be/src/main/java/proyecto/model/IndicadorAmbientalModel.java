package proyecto.model;

import jakarta.persistence.*;
import lombok.*;
import proyecto.enums.TipoIndicadorEnum;

import java.time.LocalDate;

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
    private Integer idIndicador;

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

    @Column(name = "objetivo_asociado", nullable = false, columnDefinition = "TEXT")
    private String objetivo;

    // Desglosamos la meta para poder calcular (Valor + Unidad)
    @Column(name = "meta_valor_objetivo")
    private Double metaValor;

    @Column(name = "meta_unidad")
    private String metaUnidad;

    @Column(name = "responsable_cumplimiento")
    private String responsableCumplimiento; // Persona asignada al cumplimiento del objetivo

    // Campo Calculado: Avance
    // No se persiste en BD necesariamente, o se puede persistir si se requiere
    // historial.
    // Aquí lo definimos como Transient para que se calcule al vuelo al pedir el
    // dato.
    @Transient
    public Double getPorcentajeAvance() {
        if (metaValor == null || metaValor == 0 || valorMedido == null) {
            return 0.0;
        }
        // Cálculo básico: (Valor Actual / Meta) * 100
        // NOTA: Dependiendo del indicador, "superar" la meta puede ser bueno o malo
        // (ej. residuos vs producción). Este es un cálculo genérico de progreso.
        double avance = (valorMedido / metaValor) * 100;
        return Math.round(avance * 100.0) / 100.0; // Redondeo a 2 decimales
    }
}