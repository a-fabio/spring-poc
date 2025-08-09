package br.com.poc.service;

import br.com.poc.entity.Cliente;
import br.com.poc.entity.Pedido;
import br.com.poc.entity.Pedido.StatusPedido;
import br.com.poc.repository.ClienteRepository;
import br.com.poc.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatus(status);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorClienteEStatus(Long clienteId, StatusPedido status) {
        return pedidoRepository.findByClienteIdAndStatus(clienteId, status);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim);
    }
    
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorValorMinimo(BigDecimal valorMinimo) {
        return pedidoRepository.findByValorGreaterThanEqual(valorMinimo);
    }
    
    @Transactional(readOnly = true)
    public Long contarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.countPedidosByClienteId(clienteId);
    }
    
    public Pedido salvar(Pedido pedido) {
        validarPedido(pedido);
        return pedidoRepository.save(pedido);
    }
    
    public Pedido criarPedido(Long clienteId, String descricao, BigDecimal valor) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));
        
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDescricao(descricao);
        pedido.setValor(valor);
        pedido.setStatus(StatusPedido.PENDENTE);
        
        return salvar(pedido);
    }
    
    public Pedido atualizar(Long id, Pedido pedidoAtualizado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        
        pedido.setDescricao(pedidoAtualizado.getDescricao());
        pedido.setValor(pedidoAtualizado.getValor());
        
        // Só atualiza o cliente se foi fornecido
        if (pedidoAtualizado.getCliente() != null) {
            Cliente cliente = clienteRepository.findById(pedidoAtualizado.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            pedido.setCliente(cliente);
        }
        
        return pedidoRepository.save(pedido);
    }
    
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        
        validarMudancaStatus(pedido.getStatus(), novoStatus);
        pedido.setStatus(novoStatus);
        
        return pedidoRepository.save(pedido);
    }
    
    public void deletar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        
        // Só permite deletar pedidos pendentes ou cancelados
        if (pedido.getStatus() == StatusPedido.PROCESSANDO || pedido.getStatus() == StatusPedido.CONCLUIDO) {
            throw new RuntimeException("Não é possível deletar pedidos processando ou concluídos");
        }
        
        pedidoRepository.delete(pedido);
    }
    
    private void validarPedido(Pedido pedido) {
        if (pedido.getCliente() == null) {
            throw new RuntimeException("Cliente é obrigatório");
        }
        
        if (pedido.getValor() == null || pedido.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor deve ser maior que zero");
        }
        
        if (pedido.getDescricao() == null || pedido.getDescricao().trim().isEmpty()) {
            throw new RuntimeException("Descrição é obrigatória");
        }
    }
    
    private void validarMudancaStatus(StatusPedido statusAtual, StatusPedido novoStatus) {
        // Regras de negócio para mudança de status
        switch (statusAtual) {
            case PENDENTE:
                if (novoStatus != StatusPedido.PROCESSANDO && novoStatus != StatusPedido.CANCELADO) {
                    throw new RuntimeException("Pedido pendente só pode ir para PROCESSANDO ou CANCELADO");
                }
                break;
            case PROCESSANDO:
                if (novoStatus != StatusPedido.CONCLUIDO && novoStatus != StatusPedido.CANCELADO) {
                    throw new RuntimeException("Pedido processando só pode ir para CONCLUIDO ou CANCELADO");
                }
                break;
            case CONCLUIDO:
            case CANCELADO:
                throw new RuntimeException("Não é possível alterar status de pedidos concluídos ou cancelados");
        }
    }
}