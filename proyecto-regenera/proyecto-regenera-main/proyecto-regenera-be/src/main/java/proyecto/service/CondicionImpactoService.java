package proyecto.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import proyecto.model.CondicionImpactoModel;
import proyecto.repository.CondicionImpactoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CondicionImpactoService {

    private final CondicionImpactoRepository condicionImpactoRepository;

    public List<CondicionImpactoModel> listar() {
        return condicionImpactoRepository.findAll();
    }

    public CondicionImpactoModel obtenerPorId(Integer id) {
        return condicionImpactoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Condición de impacto no encontrada: " + id));
    }

    @Transactional
    public CondicionImpactoModel crear(CondicionImpactoModel nueva) {
        return condicionImpactoRepository.save(nueva);
    }

    @Transactional
    public CondicionImpactoModel actualizar(Integer id, CondicionImpactoModel datos) {
        CondicionImpactoModel existente = obtenerPorId(id);
        existente.setCondicionImpacto(datos.getCondicionImpacto());
        return condicionImpactoRepository.save(existente);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!condicionImpactoRepository.existsById(id)) {
            throw new IllegalArgumentException("Condición de impacto no encontrada: " + id);
        }
        condicionImpactoRepository.deleteById(id);
    }
}