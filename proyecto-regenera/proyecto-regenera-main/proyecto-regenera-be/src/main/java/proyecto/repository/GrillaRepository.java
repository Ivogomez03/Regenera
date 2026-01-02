package proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyecto.model.GrillaModel;

import java.util.List;

@Repository
public interface GrillaRepository extends JpaRepository<GrillaModel, Long> {

    java.util.List<GrillaModel> findByFormulario_IdFormulario(Long idFormulario);

    List<GrillaModel> findByFormulario_IdFormularioAndIdSector(Long idFormulario, Long idSector);

    @Query("""
           select distinct g.idSector
           from GrillaModel g
           where g.formulario.usuario.email = :email
           order by g.idSector
           """)
    List<Long> findDistinctSectoresByUsuarioEmail(@Param("email") String email);

    @Query("""
           select distinct g.idSector
           from GrillaModel g
           where g.formulario.idFormulario = :idFormulario
             and g.formulario.usuario.email = :email
           order by g.idSector
           """)
    List<Long> findDistinctSectoresByFormularioAndEmail(@Param("idFormulario") Long idFormulario,
                                                        @Param("email") String email);

}