package proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "institucion",
        schema = "ga",
        indexes = {
                @Index(name = "idx_institucion_nombre", columnList = "nombre_razon_social"),
                @Index(name = "idx_institucion_localidad", columnList = "id_localidad"),
                @Index(name = "idx_institucion_rubro", columnList = "id_rubro_industrial"),
                @Index(name = "idx_institucion_activo", columnList = "activo")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InstitucionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_institucion")
    private Long id;

    @NotBlank
    @Column(name = "nombre_razon_social", nullable = false)
    private String nombreRazonSocial;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "CUIT debe tener 11 dígitos")
    @Column(name = "cuit", length = 11, nullable = false, unique = true)
    private String cuit;

    @NotBlank
    @Column(name = "domicilio_legal", nullable = false)
    private String domicilioLegal;

    @Column(name = "domicilio_operativo")
    private String domicilioOperativo;

    @NotBlank
    @Email
    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;

    @Column(name = "telefono")
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_localidad",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_institucion_localidad"))
    private LocalidadModel localidad;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rubro_industrial",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_institucion_rubro"))
    private RubroIndustrialModel rubroIndustrial;

    @NotBlank
    @Lob
    @Column(name = "declaracion_alcance_sga", nullable = false)
    private String declaracionAlcanceSga;

    @NotBlank
    @Lob
    @Column(name = "declaracion_politica_ambiental", nullable = false)
    private String declaracionPoliticaAmbiental;

    @NotNull
    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(name = "fecha_ultima_actualizacion_politica", nullable = false)
    private LocalDate fechaUltimaActualizacionPolitica;

    @Column(name = "activo", nullable = false)
    private Boolean activo = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private OffsetDateTime fechaActualizacion;

    /* equals/hashCode por id */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstitucionModel other)) return false;
        return id != null && id.equals(other.id);
    }
    @Override public int hashCode() { return 31; }
}