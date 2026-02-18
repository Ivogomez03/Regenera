package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.model.IndicadorAmbientalModel;

import java.util.List;

@Repository
public interface IndicadorAmbientalRepository extends JpaRepository<IndicadorAmbientalModel, Long> {
    // Buscar indicadores por objetivo para armar la matriz de seguimiento

    List<IndicadorAmbientalModel> findByUsuario_Id(Long idUsuario);
}