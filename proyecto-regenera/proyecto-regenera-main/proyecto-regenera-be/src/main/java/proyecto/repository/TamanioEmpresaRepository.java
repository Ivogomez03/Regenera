package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyecto.model.TamanioEmpresaModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface TamanioEmpresaRepository extends JpaRepository<TamanioEmpresaModel, Integer> {

    Optional<TamanioEmpresaModel> findByTamanioEmpresa(String tamañoEmpresa);

    List<TamanioEmpresaModel> findAllByOrderByTamanioEmpresaAsc();
}






























































































