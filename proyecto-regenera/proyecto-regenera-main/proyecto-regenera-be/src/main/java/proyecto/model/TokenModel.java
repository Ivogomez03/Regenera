package proyecto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "token_recuperacion", schema = "auth")
@Getter
@Setter
@NoArgsConstructor
public class TokenModel {

    @Id
    @Column(name = "id_token")
    private UUID idToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private UsuarioModel usuario;

    @Column(name = "emitido_en", nullable = false)
    private OffsetDateTime emitidoEn;

    @Column(name = "vence_en", nullable = false)
    private OffsetDateTime venceEn;

    @Column(name = "usado", nullable = false)
    private boolean usado = false;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "tipo", nullable = false) // VERIFY | RESET
    private String tipo;

    @Column(name = "expira_en")
    private OffsetDateTime expiraEn;

    @PrePersist
    public void prePersist() {
        if (idToken == null) {
            idToken = UUID.randomUUID();
        }
        if (emitidoEn == null) {
            emitidoEn = OffsetDateTime.now();
        }
        if (tipo == null) {
            tipo = "VERIFY";
        }
        if (expiraEn == null) {
            expiraEn = venceEn;
        }
    }
}
