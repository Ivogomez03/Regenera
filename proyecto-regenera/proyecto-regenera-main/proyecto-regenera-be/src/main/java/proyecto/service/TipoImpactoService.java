package proyecto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proyecto.model.TipoImpactoModel;
import proyecto.repository.TipoImpactoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoImpactoService {

    private final TipoImpactoRepository tipoImpactoRepository;

    public List<TipoImpactoModel> listar() {

        return tipoImpactoRepository.findAll();
    }

    public TipoImpactoModel obtenerPorId(Integer idTipoImpacto) {
        return tipoImpactoRepository.findById(idTipoImpacto)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de Impacto no encontrado: " + idTipoImpacto));
    }

    @Transactional
    public TipoImpactoModel crear(TipoImpactoModel nuevo) {
        return tipoImpactoRepository.save(nuevo);
    }

    @Transactional
    public TipoImpactoModel actualizar(Integer id, TipoImpactoModel datos) {
        TipoImpactoModel existente = obtenerPorId(id);
        existente.setTipoImpacto(datos.getTipoImpacto());
        return tipoImpactoRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!tipoImpactoRepository.existsById(id)) {
            throw new IllegalArgumentException("Tipo de Impacto no encontrado: " + id);
        }
        tipoImpactoRepository.deleteById(id);
    }
}
