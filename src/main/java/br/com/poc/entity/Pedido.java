package br.com.poc.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String descricao;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;
    
    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;
    
    @Column(name = "cliente_id", insertable = false, updatable = false)
    private Long clienteId;
    
    @PrePersist
    protected void onCreate() {
        dataPedido = LocalDateTime.now();
        if (status == null) {
            status = StatusPedido.PENDENTE;
        }
    }
    
    public enum StatusPedido {
        PENDENTE, CONFIRMADO, ENVIADO, ENTREGUE, CANCELADO
    }
}