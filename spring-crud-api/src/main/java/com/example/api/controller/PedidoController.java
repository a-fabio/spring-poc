package com.example.api.controller;

import com.example.api.dto.PedidoDTO;
import com.example.api.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedido", description = "API para gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna uma lista com todos os pedidos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    public ResponseEntity<List<PedidoDTO>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoDTO> buscarPorId(
            @Parameter(description = "ID do pedido") @PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos por cliente", description = "Retorna todos os pedidos de um cliente específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos do cliente retornada com sucesso")
    public ResponseEntity<List<PedidoDTO>> listarPorCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
    }

    @PostMapping
    @Operation(summary = "Criar novo pedido", description = "Cria um novo pedido no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<PedidoDTO> criar(
            @Parameter(description = "Dados do pedido") @Valid @RequestBody PedidoDTO pedidoDTO) {
        PedidoDTO novoPedido = pedidoService.criar(pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido", description = "Atualiza os dados de um pedido existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Pedido ou cliente não encontrado")
    })
    public ResponseEntity<PedidoDTO> atualizar(
            @Parameter(description = "ID do pedido") @PathVariable Long id,
            @Parameter(description = "Dados atualizados do pedido") @Valid @RequestBody PedidoDTO pedidoDTO) {
        return ResponseEntity.ok(pedidoService.atualizar(id, pedidoDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar pedido", description = "Remove um pedido do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do pedido") @PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}