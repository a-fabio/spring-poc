package br.com.poc.repository;

import br.com.poc.entity.Pedido;
import br.com.poc.entity.Pedido.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByClienteId(Long clienteId);
    
    List<Pedido> findByStatus(StatusPedido status);
    
    List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status);
    
    @Query("SELECT p FROM Pedido p WHERE p.dataPedido BETWEEN :inicio AND :fim")
    List<Pedido> findByDataPedidoBetween(@Param("inicio") LocalDateTime inicio, 
                                        @Param("fim") LocalDateTime fim);
    
    @Query("SELECT p FROM Pedido p WHERE p.valor >= :valorMinimo")
    List<Pedido> findByValorGreaterThanEqual(@Param("valorMinimo") BigDecimal valorMinimo);
    
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.id = :clienteId")
    Long countPedidosByClienteId(@Param("clienteId") Long clienteId);
}