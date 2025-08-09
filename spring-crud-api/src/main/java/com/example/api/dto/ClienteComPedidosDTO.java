package com.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteComPedidosDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private List<PedidoDTO> pedidos;

}