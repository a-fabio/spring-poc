package br.com.poc.repository;

import br.com.poc.entity.Pedido;
import br.com.poc.entity.Pedido.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByClienteId(Long clienteId);
    
    List<Pedido> findByStatus(StatusPedido status);
    
    List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status);
}