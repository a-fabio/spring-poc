package br.com.poc.controller;

import br.com.poc.entity.Cliente;
import br.com.poc.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    })
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Cliente> buscarPorId(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar cliente por email", description = "Retorna um cliente específico pelo seu email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Cliente> buscarPorEmail(
            @Parameter(description = "Email do cliente", required = true)
            @PathVariable String email) {
        Optional<Cliente> cliente = clienteService.buscarPorEmail(email);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes por nome", description = "Retorna clientes que contenham o nome informado (busca case-insensitive)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    })
    public ResponseEntity<List<Cliente>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome para busca", required = true)
            @RequestParam String nome) {
        List<Cliente> clientes = clienteService.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/pesquisar")
    @Operation(summary = "Pesquisar clientes", description = "Busca clientes por termo no nome ou email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pesquisa realizada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class)))
    })
    public ResponseEntity<List<Cliente>> pesquisar(
            @Parameter(description = "Termo de pesquisa (nome ou email)", required = true)
            @RequestParam String termo) {
        List<Cliente> clientes = clienteService.buscarPorTermo(termo);
        return ResponseEntity.ok(clientes);
    }
    
    @PostMapping
    @Operation(summary = "Criar novo cliente", description = "Cria um novo cliente no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "409", description = "Email já está em uso")
    })
    public ResponseEntity<Cliente> criar(
            @Parameter(description = "Dados do cliente a ser criado", required = true)
            @Valid @RequestBody Cliente cliente) {
        try {
            Cliente novoCliente = clienteService.salvar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "409", description = "Email já está em uso por outro cliente")
    })
    public ResponseEntity<Cliente> atualizar(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long id,
            @Parameter(description = "Novos dados do cliente", required = true)
            @Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente", description = "Remove um cliente do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "400", description = "Não é possível deletar cliente com pedidos associados")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long id) {
        try {
            clienteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/existe-email/{email}")
    @Operation(summary = "Verificar se email existe", description = "Verifica se já existe um cliente com o email informado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso")
    })
    public ResponseEntity<Boolean> existeEmail(
            @Parameter(description = "Email a ser verificado", required = true)
            @PathVariable String email) {
        boolean existe = clienteService.existePorEmail(email);
        return ResponseEntity.ok(existe);
    }
}