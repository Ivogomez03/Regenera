package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.SectorModel;

public interface SectorRepository extends JpaRepository<SectorModel, Integer> {

    boolean existsBySectorIgnoreCase(String sector);
}
