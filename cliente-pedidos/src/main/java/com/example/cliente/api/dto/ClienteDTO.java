package com.example.cliente.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteDTO {
    private Long id;

    @NotBlank
    @Size(max = 120)
    private String nome;

    @Email
    @Size(max = 180)
    private String email;
}