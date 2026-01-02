package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.model.TipoEmpresaModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoEmpresaRepository extends JpaRepository<TipoEmpresaModel, Integer> {

    Optional<TipoEmpresaModel> findByTipoEmpresa(String tipoEmpresa);

    List<TipoEmpresaModel> findAllByOrderByTipoEmpresaAsc();
}