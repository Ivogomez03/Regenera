package proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import proyecto.enums.TipoReponsabilidadEnum;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "persona",
        schema = "ga",
        indexes = {
                @Index(name = "idx_persona_apellido_nombre", columnList = "apellido, nombre"),
                @Index(name = "idx_persona_responsabilidad", columnList = "tipo_responsabilidad"),
                @Index(name = "idx_persona_activo", columnList = "activo")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PersonaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long id;

    @NotBlank
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotBlank
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotBlank
    @Email
    @Column(name = "correo_electronico", nullable = false)
    private String correoElectronico;

    @Column(name = "telefono")
    private String telefono;

    @NotBlank
    @Column(name = "cargo", nullable = false)
    private String cargo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_responsabilidad", nullable = false)
    private TipoReponsabilidadEnum tipoResponsabilidad;

    @NotBlank
    @Lob
    @Column(name = "asignacion_responsabilidades", nullable = false)
    private String asignacionResponsabilidades;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_institucion",
            foreignKey = @ForeignKey(name = "fk_persona_institucion"))
    private InstitucionModel institucion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private OffsetDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private OffsetDateTime fechaActualizacion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonaModel other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}