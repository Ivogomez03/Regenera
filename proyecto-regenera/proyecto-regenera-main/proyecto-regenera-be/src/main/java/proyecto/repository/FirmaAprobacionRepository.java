package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.model.FirmaAprobacionModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface FirmaAprobacionRepository extends JpaRepository<FirmaAprobacionModel, Long> {

    Optional<FirmaAprobacionModel> findTopByFormulario_IdFormularioOrderByIdFirmaAprobacionDesc(Long idFormulario);

    List<FirmaAprobacionModel> findByFormulario_IdFormulario(Long idFormulario);

}