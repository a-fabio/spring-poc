package br.com.poc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "PEDIDO")
public class Pedido {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "DATA_PEDIDO")
    private LocalDate dataPedido;

    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID")
    private Cliente cliente;
}