package com.example.api.service;

import com.example.api.dto.ClienteComPedidosDTO;
import com.example.api.dto.ClienteDTO;
import com.example.api.dto.PedidoDTO;
import com.example.api.entity.Cliente;
import com.example.api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        return convertToDTO(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteComPedidosDTO buscarComPedidos(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        return convertToComPedidosDTO(cliente);
    }

    @Transactional
    public ClienteDTO criar(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + clienteDTO.getEmail());
        }
        
        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());
        
        cliente = clienteRepository.save(cliente);
        return convertToDTO(cliente);
    }

    @Transactional
    public ClienteDTO atualizar(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        
        // Verifica se o email já existe para outro cliente
        if (!cliente.getEmail().equals(clienteDTO.getEmail()) && 
            clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + clienteDTO.getEmail());
        }
        
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());
        
        cliente = clienteRepository.save(cliente);
        return convertToDTO(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        return dto;
    }

    private ClienteComPedidosDTO convertToComPedidosDTO(Cliente cliente) {
        ClienteComPedidosDTO dto = new ClienteComPedidosDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        
        List<PedidoDTO> pedidos = cliente.getPedidos().stream()
                .map(pedido -> {
                    PedidoDTO pedidoDTO = new PedidoDTO();
                    pedidoDTO.setId(pedido.getId());
                    pedidoDTO.setDescricao(pedido.getDescricao());
                    pedidoDTO.setValor(pedido.getValor());
                    pedidoDTO.setDataPedido(pedido.getDataPedido());
                    pedidoDTO.setClienteId(cliente.getId());
                    return pedidoDTO;
                })
                .collect(Collectors.toList());
        
        dto.setPedidos(pedidos);
        return dto;
    }

}