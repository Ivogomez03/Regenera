package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.model.EmpresaModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaModel, Long> {

    Optional<EmpresaModel> findByEmail(String email);

    List<EmpresaModel> findAll();
}