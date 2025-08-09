package com.example.cliente.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PedidoDTO {
    private Long id;

    @NotNull
    private Long clienteId;

    @Size(max = 200)
    private String descricao;

    @NotNull
    private BigDecimal valor;
}