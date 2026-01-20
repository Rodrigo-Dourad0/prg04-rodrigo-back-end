package br.com.ifba.prg04_rodrigo_back_end.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateRequest(
        @NotBlank(message = "O e-mail não pode estar vazio")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        // Campos detalhados de endereço para maior precisão no banco de dados
        String rua,
        String numero,
        String bairro,
        String cidade,
        String estado,
        String cep
) {
}