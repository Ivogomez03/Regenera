package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.model.GrillaModel;

import java.util.List;

@Repository
public interface GrillaRepository extends JpaRepository<GrillaModel, Long> {

       @Query("SELECT DISTINCT g.sector.idSector FROM GrillaModel g WHERE g.formulario.usuario.email = :email")
       List<Long> findDistinctSectoresByUsuarioEmail(String email);

       @Query("SELECT DISTINCT g.sector.idSector FROM GrillaModel g WHERE g.formulario.idFormulario = :idFormulario AND g.formulario.usuario.email = :email")
       List<Long> findDistinctSectoresByFormularioAndEmail(Long idFormulario, String email);

       List<GrillaModel> findByFormulario_IdFormulario(Long idFormulario);

       List<GrillaModel> findByFormulario_IdFormularioAndSector_IdSector(Long idFormulario, Long idSector);
}