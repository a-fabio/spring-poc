package br.com.poc.controller;

import br.com.poc.entity.Pedido;
import br.com.poc.entity.Pedido.StatusPedido;
import br.com.poc.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API para gerenciamento de pedidos")
public class PedidoController {
    
    private final PedidoService pedidoService;
    
    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna uma lista com todos os pedidos cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)))
    })
    public ResponseEntity<List<Pedido>> listarTodos() {
        List<Pedido> pedidos = pedidoService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Pedido> buscarPorId(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.buscarPorId(id);
        return pedido.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos por cliente", description = "Retorna todos os pedidos de um cliente específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos do cliente retornados com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)))
    })
    public ResponseEntity<List<Pedido>> listarPorCliente(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "Listar pedidos por status", description = "Retorna todos os pedidos com um status específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos por status retornados com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)))
    })
    public ResponseEntity<List<Pedido>> listarPorStatus(
            @Parameter(description = "Status do pedido (PENDENTE, PROCESSANDO, CONCLUIDO, CANCELADO)", required = true)
            @PathVariable StatusPedido status) {
        List<Pedido> pedidos = pedidoService.buscarPorStatus(status);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/cliente/{clienteId}/status/{status}")
    @Operation(summary = "Listar pedidos por cliente e status", description = "Retorna pedidos de um cliente com status específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos filtrados retornados com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)))
    })
    public ResponseEntity<List<Pedido>> listarPorClienteEStatus(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long clienteId,
            @Parameter(description = "Status do pedido", required = true)
            @PathVariable StatusPedido status) {
        List<Pedido> pedidos = pedidoService.buscarPorClienteEStatus(clienteId, status);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/periodo")
    @Operation(summary = "Listar pedidos por período", description = "Retorna pedidos realizados em um período específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos do período retornados com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)))
    })
    public ResponseEntity<List<Pedido>> listarPorPeriodo(
            @Parameter(description = "Data de início (formato: yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @Parameter(description = "Data de fim (formato: yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        List<Pedido> pedidos = pedidoService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/valor-minimo")
    @Operation(summary = "Listar pedidos por valor mínimo", description = "Retorna pedidos com valor maior ou igual ao especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos por valor retornados com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class)))
    })
    public ResponseEntity<List<Pedido>> listarPorValorMinimo(
            @Parameter(description = "Valor mínimo para filtro", required = true)
            @RequestParam BigDecimal valorMinimo) {
        List<Pedido> pedidos = pedidoService.buscarPorValorMinimo(valorMinimo);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/cliente/{clienteId}/count")
    @Operation(summary = "Contar pedidos por cliente", description = "Retorna a quantidade de pedidos de um cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contagem retornada com sucesso")
    })
    public ResponseEntity<Long> contarPedidosPorCliente(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long clienteId) {
        Long count = pedidoService.contarPedidosPorCliente(clienteId);
        return ResponseEntity.ok(count);
    }
    
    @PostMapping
    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Pedido> criar(
            @Parameter(description = "Dados do pedido a ser criado", required = true)
            @Valid @RequestBody Pedido pedido) {
        try {
            Pedido novoPedido = pedidoService.salvar(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/cliente/{clienteId}")
    @Operation(summary = "Criar pedido para cliente", description = "Cria um novo pedido associado a um cliente específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Pedido> criarPedido(
            @Parameter(description = "ID do cliente", required = true)
            @PathVariable Long clienteId,
            @Parameter(description = "Descrição do pedido", required = true)
            @RequestParam String descricao,
            @Parameter(description = "Valor do pedido", required = true)
            @RequestParam BigDecimal valor) {
        try {
            Pedido novoPedido = pedidoService.criarPedido(clienteId, descricao, valor);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido", description = "Atualiza os dados de um pedido existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Pedido> atualizar(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id,
            @Parameter(description = "Novos dados do pedido", required = true)
            @Valid @RequestBody Pedido pedido) {
        try {
            Pedido pedidoAtualizado = pedidoService.atualizar(id, pedido);
            return ResponseEntity.ok(pedidoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza apenas o status de um pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pedido.class))),
        @ApiResponse(responseCode = "400", description = "Mudança de status inválida"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Pedido> atualizarStatus(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id,
            @Parameter(description = "Novo status do pedido", required = true)
            @RequestParam StatusPedido status) {
        try {
            Pedido pedidoAtualizado = pedidoService.atualizarStatus(id, status);
            return ResponseEntity.ok(pedidoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pedido", description = "Remove um pedido do sistema (apenas pedidos pendentes ou cancelados)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Não é possível deletar pedidos processando ou concluídos"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do pedido", required = true)
            @PathVariable Long id) {
        try {
            pedidoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}