package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.model.RubroIndustrialModel;

@Repository
public interface RubroIndustrialRepository extends JpaRepository<RubroIndustrialModel, Long> {

}