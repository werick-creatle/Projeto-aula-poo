package hotelaria.projeto.hotelaria.controller;

import hotelaria.projeto.hotelaria.model.Erro;
import hotelaria.projeto.hotelaria.model.Hospede;
import hotelaria.projeto.hotelaria.repository.HospedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hospedes") // Rota base
@CrossOrigin(origins = "*")
public class HospedeController {

    @Autowired // Injeção de dependência no campo
    private HospedeRepository repository;

    //CREATE (Criar)
    @PostMapping
    public ResponseEntity<Object> criarHospede(@RequestBody Hospede hospede) {
        try {
            repository.save(hospede);
            // Retorna 201 Created com a URI e o objeto no corpo
            return ResponseEntity.created(URI.create("/hospedes/" + hospede.getId())).body(hospede);

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            Erro erro = new Erro(HttpStatus.BAD_REQUEST,
                    "Registro duplicado! (Ex: CPF ou Email já existe)",
                    e.getClass().getName());
            return new ResponseEntity<>(erro, erro.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    e.getClass().getName());
            return new ResponseEntity<>(erro, erro.getStatus());
        }
    }


    @GetMapping
    public ResponseEntity<List<Hospede>> listarTodosHospedes() {
        List<Hospede> hospedes = repository.findAll();
        return ResponseEntity.ok(hospedes);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Hospede> buscarHospedePorId(@PathVariable("id") Long idHospede) {
        Optional<Hospede> optional = repository.findById(idHospede);

        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get()); // Retorna 200 OK
        }
        return ResponseEntity.notFound().build(); // Retorna 404 Not Found
    }

    /**
     * UPDATE (Atualizar)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarHospede(@PathVariable Long id, @RequestBody Hospede hospedeDetalhes) {

        Optional<Hospede> optional = repository.findById(id);
        if (optional.isEmpty()) {

            Erro erro = new Erro(HttpStatus.NOT_FOUND,
                    "Hóspede com ID " + id + " não encontrado.",
                    "N/A");
            return new ResponseEntity<>(erro, erro.getStatus());
        }

        try {
            Hospede hospedeExistente = optional.get();
            hospedeExistente.setNome(hospedeDetalhes.getNome());
            hospedeExistente.setEmail(hospedeDetalhes.getEmail());
            hospedeExistente.setCpf(hospedeDetalhes.getCpf());
            hospedeExistente.setTelefone(hospedeDetalhes.getTelefone());

            repository.save(hospedeExistente); // Salva o hospede atualizado
            return ResponseEntity.ok(hospedeExistente); // Retorna 200 OK

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            Erro erro = new Erro(HttpStatus.BAD_REQUEST,
                    "Erro de integridade (Ex: CPF ou Email duplicado)",
                    e.getClass().getName());
            return new ResponseEntity<>(erro, erro.getStatus());

        } catch (Exception e) {
            e.printStackTrace();

            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    e.getClass().getName());
            return new ResponseEntity<>(erro, erro.getStatus());
        }
    }

    /**
     * DELETE (Deletar)
     */
    @DeleteMapping("/{id}")
    // CORREÇÃO: Trocado ResponseEntity<> por ResponseEntity<Object>
    public ResponseEntity<Object> deletarHospede(@PathVariable Long id) {

        if (!repository.existsById(id)) {

            Erro erro = new Erro(HttpStatus.NOT_FOUND,
                    "Hóspede com ID " + id + " não encontrado.",
                    "N/A");
            return new ResponseEntity<>(erro, erro.getStatus());
        }

        try {
            repository.deleteById(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();

            Erro erro = new Erro(HttpStatus.CONFLICT, // 409 Conflict
                    "Não é possível excluir o hóspede, pois ele está associado a uma reserva ou estadia.",
                    e.getClass().getName());
            return new ResponseEntity<>(erro, erro.getStatus());
        } catch (Exception e) {
            e.printStackTrace();

            Erro erro = new Erro(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    e.getClass().getName());
            return new ResponseEntity<>(erro, erro.getStatus());
        }
    }
}