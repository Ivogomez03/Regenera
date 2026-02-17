package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.model.FormularioModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormularioRepository extends JpaRepository<FormularioModel, Long> {

    Optional<FormularioModel> findByIdFormularioAndUsuario_Email(Long idFormulario, String email);

    List<FormularioModel> findByUsuario_Email(String email);

    List<FormularioModel> findByUsuario_Id(Long idUsuario);

    @Query("SELECT DISTINCT g.formulario FROM GrillaModel g WHERE g.formulario.usuario.id = :idUsuario")
    List<FormularioModel> findFormulariosConGrillas(@Param("idUsuario") Long idUsuario);

}