package br.com.poc.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(length = 20)
    private String telefone;
    
    @Column(length = 200)
    private String endereco;
    
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos;
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
    }
}