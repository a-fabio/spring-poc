package com.example.api.service;

import com.example.api.dto.PedidoDTO;
import com.example.api.entity.Cliente;
import com.example.api.entity.Pedido;
import com.example.api.repository.ClienteRepository;
import com.example.api.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<PedidoDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        return convertToDTO(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoDTO criar(PedidoDTO pedidoDTO) {
        Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + pedidoDTO.getClienteId()));
        
        Pedido pedido = new Pedido();
        pedido.setDescricao(pedidoDTO.getDescricao());
        pedido.setValor(pedidoDTO.getValor());
        pedido.setCliente(cliente);
        
        pedido = pedidoRepository.save(pedido);
        return convertToDTO(pedido);
    }

    @Transactional
    public PedidoDTO atualizar(Long id, PedidoDTO pedidoDTO) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
        
        // Se o cliente mudou, busca o novo cliente
        if (!pedido.getCliente().getId().equals(pedidoDTO.getClienteId())) {
            Cliente novoCliente = clienteRepository.findById(pedidoDTO.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + pedidoDTO.getClienteId()));
            pedido.setCliente(novoCliente);
        }
        
        pedido.setDescricao(pedidoDTO.getDescricao());
        pedido.setValor(pedidoDTO.getValor());
        
        pedido = pedidoRepository.save(pedido);
        return convertToDTO(pedido);
    }

    @Transactional
    public void deletar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado com ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    private PedidoDTO convertToDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setDescricao(pedido.getDescricao());
        dto.setValor(pedido.getValor());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setClienteId(pedido.getCliente().getId());
        return dto;
    }

}