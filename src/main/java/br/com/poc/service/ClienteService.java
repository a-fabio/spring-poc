package br.com.poc.service;

import br.com.poc.entity.Cliente;
import br.com.poc.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorTermo(String termo) {
        return clienteRepository.buscarPorTermo(termo);
    }
    
    public Cliente salvar(Cliente cliente) {
        validarCliente(cliente);
        return clienteRepository.save(cliente);
    }
    
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        
        // Validar se o email não está sendo usado por outro cliente
        if (!cliente.getEmail().equals(clienteAtualizado.getEmail()) && 
            clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
            throw new RuntimeException("Email já está em uso por outro cliente");
        }
        
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        
        return clienteRepository.save(cliente);
    }
    
    public void deletar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        
        clienteRepository.delete(cliente);
    }
    
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }
    
    private void validarCliente(Cliente cliente) {
        if (cliente.getId() == null && clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }
    }
}