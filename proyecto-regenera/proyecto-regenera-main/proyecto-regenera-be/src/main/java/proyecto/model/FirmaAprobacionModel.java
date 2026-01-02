package proyecto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "firma_aprobacion", schema = "ga")
@Getter
@Setter
public class FirmaAprobacionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_firma_aprobacion")
    private Long idFirmaAprobacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formulario", nullable = false)
    private FormularioModel formulario;

    // ========== ELABORÓ ==========
    @Column(name = "nombre_elaboro")
    private String nombreElabore;

    @Column(name = "apellido_elaboro")
    private String apellidoElabore;

    @Column(name = "puesto_elaboro")
    private String puestoElabore;

    @Column(name = "firma_elaboro")
    @Lob
    private byte[] firmaElabore;

    @Column(name = "aclaracion_elaboro")
    private String aclaracionElabore;

    @Column(name = "fecha_elaboro")
    private LocalDateTime fechaElabore;

    // ========== REVISÓ ==========
    @Column(name = "nombre_reviso")
    private String nombreReviso;

    @Column(name = "apellido_reviso")
    private String apellidoReviso;

    @Column(name = "puesto_reviso")
    private String puestoReviso;

    @Column(name = "firma_reviso")
    @Lob
    private byte[] firmaReviso;

    @Column(name = "aclaracion_reviso")
    private String aclaracionReviso;

    @Column(name = "fecha_reviso")
    private LocalDateTime fechaReviso;

    // ========== APROBÓ ==========
    @Column(name = "nombre_aprobo")
    private String nombreAprobo;

    @Column(name = "apellido_aprobo")
    private String apellidoAprobo;

    @Column(name = "puesto_aprobo")
    private String puestoAprobo;

    @Column(name = "firma_aprobo")
    @Lob
    private byte[] firmaAprobo;

    @Column(name = "aclaracion_aprobo")
    private String aclaracionAprobo;

    @Column(name = "fecha_aprobo")
    private LocalDateTime fechaAprobo;

    // ========== AUDITORÍA ==========
    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    @PrePersist
    public void prePersist() {
        var now = LocalDateTime.now();
        this.creadoEn = now;
        this.actualizadoEn = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.actualizadoEn = LocalDateTime.now();
    }
}