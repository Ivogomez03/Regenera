package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.MatrizObjetivosModel;
import java.util.List;

public interface MatrizObjetivoRepository extends JpaRepository<MatrizObjetivosModel, Long> {
    List<MatrizObjetivosModel> findByUsuario_Id(Long idUsuario);
}