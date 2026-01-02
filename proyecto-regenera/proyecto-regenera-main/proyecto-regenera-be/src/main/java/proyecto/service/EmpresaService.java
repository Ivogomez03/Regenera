package proyecto.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import proyecto.model.EmpresaModel;
import proyecto.repository.*;
import proyecto.request_response.EmpresaCreateRequest;
import proyecto.dto.EmpresaDto;

import java.util.List;
@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepo;
    private final PaisRepository paisRepo;
    private final TamanioEmpresaRepository tamanioRepo;
    private final TipoEmpresaRepository tipoRepo;

    public EmpresaService(EmpresaRepository empresaRepo, PaisRepository paisRepo,
                          TamanioEmpresaRepository tamanioRepo, TipoEmpresaRepository tipoRepo) {
        this.empresaRepo = empresaRepo;
        this.paisRepo = paisRepo;
        this.tamanioRepo = tamanioRepo;
        this.tipoRepo = tipoRepo;
    }

    @Transactional
    public EmpresaDto crear(EmpresaCreateRequest req) {

        var pais = paisRepo.findById(req.getIdPais())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "País no encontrado"));

        var tamanio = tamanioRepo.findById(req.getIdTamanioEmpresa())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tamanio de empresa no encontrado"));

        var tipo = tipoRepo.findById(req.getIdTipoEmpresa())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de empresa no encontrado"));

        var empresa = new EmpresaModel();
        empresa.setNombre(req.getNombre());
        empresa.setApellido(req.getApellido());
        empresa.setEmail(req.getEmail());
        empresa.setNumeroCelular(req.getNumeroCelular());
        empresa.setCargo(req.getCargo());
        empresa.setNombreEmpresa(req.getNombreEmpresa());
        empresa.setPais(pais);
        empresa.setTamanioEmpresa(tamanio);
        empresa.setTipoEmpresa(tipo);

        var guardada = empresaRepo.save(empresa);
        return EmpresaDto.of(guardada);
    }

    @Transactional
    public EmpresaDto actualizar(Long idEmpresa, String email, EmpresaCreateRequest req) {

        var empresa = empresaRepo.findById(idEmpresa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));

        if (!empresa.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para actualizar esta empresa");
        }

        empresa.setNombre(req.getNombre());
        empresa.setApellido(req.getApellido());
        empresa.setEmail(req.getEmail());
        empresa.setNumeroCelular(req.getNumeroCelular());
        empresa.setCargo(req.getCargo());
        empresa.setNombreEmpresa(req.getNombreEmpresa());

        if (req.getIdPais() != null) {
            var pais = paisRepo.findById(req.getIdPais())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "País no encontrado"));
            empresa.setPais(pais);
        }

        if (req.getIdTamanioEmpresa() != null) {
            var tamanio = tamanioRepo.findById(req.getIdTamanioEmpresa())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tamanio no encontrado"));
            empresa.setTamanioEmpresa(tamanio);
        }

        if (req.getIdTipoEmpresa() != null) {
            var tipo = tipoRepo.findById(req.getIdTipoEmpresa())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo no encontrado"));
            empresa.setTipoEmpresa(tipo);
        }

        var actualizada = empresaRepo.save(empresa);
        return EmpresaDto.of(actualizada);
    }

    @Transactional
    public void eliminar(Long idEmpresa, String email) {
        var empresa = empresaRepo.findById(idEmpresa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));

        if (!empresa.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar esta empresa");
        }

        empresaRepo.delete(empresa);
    }

    public EmpresaDto obtener(Long idEmpresa, String email) {
        var empresa = empresaRepo.findById(idEmpresa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));

        if (!empresa.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para ver esta empresa");
        }

        return EmpresaDto.of(empresa);
    }

    public List<EmpresaDto> listar() {
        return empresaRepo.findAll().stream()
                .map(EmpresaDto::of)
                .toList();
    }

    public List<EmpresaDto> listarPorEmail(String email) {
        return empresaRepo.findAll().stream()
                .filter(e -> e.getEmail().equals(email))
                .map(EmpresaDto::of)
                .toList();
    }
}