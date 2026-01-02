package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proyecto.model.PersonaModel;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<PersonaModel, Integer> {

    Optional<PersonaModel> findByCorreoElectronico(String correoElectronico);

}
