package proyecto.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "matriz_objetivo", schema = "ga")
@Data
public class MatrizObjetivosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemObjetivo;

    @Column(columnDefinition = "TEXT")
    private String objetivoGlobal;

    @Column(columnDefinition = "TEXT")
    private String meta;

    @ManyToOne
    @JoinColumn(name = "id_indicador")
    private IndicadorAmbientalModel indicadorAsociado;

    @Column(columnDefinition = "TEXT")
    private String responsable;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioModel usuario;
}