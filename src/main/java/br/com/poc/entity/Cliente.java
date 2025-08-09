package br.com.poc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "CLIENTE")
public class Cliente {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "EMAIL")
    private String email;
}