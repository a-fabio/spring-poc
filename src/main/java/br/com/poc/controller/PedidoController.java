package br.com.poc.controller;

import br.com.poc.entity.Cliente;
import br.com.poc.entity.Pedido;
import br.com.poc.entity.Pedido.StatusPedido;
import br.com.poc.repository.ClienteRepository;
import br.com.poc.repository.PedidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna uma lista com todos os pedidos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Pedido> buscarPorId(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        return pedido.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar pedidos por cliente", description = "Retorna todos os pedidos de um cliente específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos do cliente retornada com sucesso")
    public List<Pedido> buscarPorCliente(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar pedidos por status", description = "Retorna todos os pedidos com um status específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos por status retornada com sucesso")
    public List<Pedido> buscarPorStatus(
            @Parameter(description = "Status do pedido (PENDENTE, CONFIRMADO, ENVIADO, ENTREGUE, CANCELADO)", required = true)
            @PathVariable StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }
    
    @GetMapping("/cliente/{clienteId}/status/{status}")
    @Operation(summary = "Buscar pedidos por cliente e status", description = "Retorna pedidos de um cliente com status específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos filtrada retornada com sucesso")
    public List<Pedido> buscarPorClienteEStatus(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long clienteId,
            @Parameter(description = "Status do pedido", required = true)
            @PathVariable StatusPedido status) {
        return pedidoRepository.findByClienteIdAndStatus(clienteId, status);
    }
    
    @PostMapping
    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {
        try {
            // Verifica se o cliente existe
            if (pedido.getClienteId() == null) {
                return ResponseEntity.badRequest().body("ID do cliente é obrigatório");
            }
            
            Optional<Cliente> cliente = clienteRepository.findById(pedido.getClienteId());
            if (cliente.isEmpty()) {
                return ResponseEntity.badRequest().body("Cliente não encontrado");
            }
            
            pedido.setCliente(cliente.get());
            Pedido novoPedido = pedidoRepository.save(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar pedido: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido", description = "Atualiza os dados de um pedido existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<?> atualizar(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id, 
            @RequestBody Pedido pedidoAtualizado) {
        try {
            Optional<Pedido> pedidoExistente = pedidoRepository.findById(id);
            if (pedidoExistente.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Pedido pedido = pedidoExistente.get();
            
            // Atualiza apenas os campos permitidos (não atualiza cliente nem data)
            pedido.setDescricao(pedidoAtualizado.getDescricao());
            pedido.setValor(pedidoAtualizado.getValor());
            pedido.setStatus(pedidoAtualizado.getStatus());
            
            Pedido pedidoSalvo = pedidoRepository.save(pedido);
            return ResponseEntity.ok(pedidoSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar pedido: " + e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza apenas o status de um pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Status inválido")
    })
    public ResponseEntity<?> atualizarStatus(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id,
            @Parameter(description = "Novo status do pedido", required = true)
            @RequestParam StatusPedido status) {
        try {
            Optional<Pedido> pedidoExistente = pedidoRepository.findById(id);
            if (pedidoExistente.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Pedido pedido = pedidoExistente.get();
            pedido.setStatus(status);
            
            Pedido pedidoSalvo = pedidoRepository.save(pedido);
            return ResponseEntity.ok(pedidoSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar status: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pedido", description = "Remove um pedido do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}