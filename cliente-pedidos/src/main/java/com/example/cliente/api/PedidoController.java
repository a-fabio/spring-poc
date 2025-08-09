package com.example.cliente.api;

import com.example.cliente.api.dto.PedidoDTO;
import com.example.cliente.domain.Cliente;
import com.example.cliente.domain.Pedido;
import com.example.cliente.repository.ClienteRepository;
import com.example.cliente.repository.PedidoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoController(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<Pedido> listar(@RequestParam(value = "clienteId", required = false) Long clienteId) {
        if (clienteId != null) {
            return pedidoRepository.findByClienteId(clienteId);
        }
        return pedidoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PedidoDTO dto) {
        Optional<Cliente> optCliente = clienteRepository.findById(dto.getClienteId());
        if (optCliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body("Cliente não encontrado: id=" + dto.getClienteId());
        }
        Pedido pedido = new Pedido();
        pedido.setCliente(optCliente.get());
        pedido.setDescricao(dto.getDescricao());
        pedido.setValor(dto.getValor());
        Pedido salvo = pedidoRepository.save(pedido);
        return ResponseEntity.created(URI.create("/api/pedidos/" + salvo.getId())).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody PedidoDTO dto) {
        Optional<Pedido> opt = pedidoRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Pedido pedido = opt.get();
        if (!pedido.getCliente().getId().equals(dto.getClienteId())) {
            Optional<Cliente> optCliente = clienteRepository.findById(dto.getClienteId());
            if (optCliente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body("Cliente não encontrado: id=" + dto.getClienteId());
            }
            pedido.setCliente(optCliente.get());
        }
        pedido.setDescricao(dto.getDescricao());
        pedido.setValor(dto.getValor());
        Pedido salvo = pedidoRepository.save(pedido);
        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
        }
    }
}