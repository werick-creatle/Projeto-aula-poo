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

    /**
     * CREATE (Criar)
     * Lógica adaptada do seu professor
     */
    @PostMapping
    public ResponseEntity<Object> criarHospede(@RequestBody Hospede hospede) {
        try {
            repository.save(hospede);

            // Retorna 201 Created com a URI e o objeto no corpo
            return ResponseEntity.created(URI.create("/hospedes/" + hospede.getId())).body(hospede);

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace(); // Loga o erro no console
            Erro erro = Erro.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .mensagem("Registro duplicado! (Ex: CPF ou Email já existe)")
                    .exception(e.getClass().getName())
                    .build();
            return new ResponseEntity<>(erro, erro.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
            Erro erro = Erro.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .mensagem(e.getMessage())
                    .exception(e.getClass().getName())
                    .build();
            return new ResponseEntity<>(erro, erro.getStatus());
        }
    }

    /**
     * READ (Listar Todos)
     */
    @GetMapping
    public ResponseEntity<List<Hospede>> listarTodosHospedes() {
        List<Hospede> hospedes = repository.findAll();
        // Retorna 200 OK com a lista (pode ser vazia)
        return ResponseEntity.ok(hospedes);
    }

    /**
     * READ (Buscar por ID)
     * Lógica adaptada do seu professor
     */
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
     * Lógica adaptada combinando a checagem de ID com o try...catch
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarHospede(@PathVariable Long id, @RequestBody Hospede hospedeDetalhes) {

        // 1. Verifica se o hospede existe
        Optional<Hospede> optional = repository.findById(id);
        if (optional.isEmpty()) {
            Erro erro = Erro.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .mensagem("Hóspede com ID " + id + " não encontrado.")
                    .exception("N/A")
                    .build();
            return new ResponseEntity<>(erro, erro.getStatus());
        }

        // 2. Tenta atualizar
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
            Erro erro = Erro.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .mensagem("Erro de integridade (Ex: CPF ou Email duplicado)")
                    .exception(e.getClass().getName())
                    .build();
            return new ResponseEntity<>(erro, erro.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
            Erro erro = Erro.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .mensagem(e.getMessage())
                    .exception(e.getClass().getName())
                    .build();
            return new ResponseEntity<>(erro, erro.getStatus());
        }
    }

    /**
     * DELETE (Deletar)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarHospede(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            Erro erro = Erro.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .mensagem("Hóspede com ID " + id + " não encontrado.")
                    .exception("N/A")
                    .build();
            return new ResponseEntity<>(erro, erro.getStatus());
        }

        try {
            repository.deleteById(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            Erro erro = Erro.builder()
                    .status(HttpStatus.CONFLICT) // 409 Conflict
                    .mensagem("Não é possível excluir o hóspede, pois ele está associado a uma reserva ou estadia.")
                    .exception(e.getClass().getName())
                    .build();
            return new ResponseEntity<>(erro, erro.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            Erro erro = Erro.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .mensagem(e.getMessage())
                    .exception(e.getClass().getName())
                    .build();
            return new ResponseEntity<>(erro, erro.getStatus());
        }
    }
}