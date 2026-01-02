package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import proyecto.model.RequisitoLegalModel;

import java.util.List;
import java.util.Optional;


public interface RequisitoLegalRepository extends JpaRepository<RequisitoLegalModel, Long> {

    @Query("SELECT DISTINCT rl.anio FROM RequisitoLegalModel rl ORDER BY rl.anio DESC")
    List<Integer> findDistinctAniosDesc();

    Optional<RequisitoLegalModel> findByIdRequisitoLegalAndUsuario_Email(Long idRequisitoLegal, String email);

    List<RequisitoLegalModel> findByUsuario_Email(String email);

}
