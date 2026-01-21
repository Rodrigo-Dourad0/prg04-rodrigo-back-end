package br.com.ifba.prg04_rodrigo_back_end.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioCreateRequest {

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String senha;

    // Dados Aninhados
    private PessoaRequest pessoa;

    @Data
    public static class PessoaRequest {
        @NotBlank private String nome;
        private String cpf;
        private String telefone;
        private EnderecoRequest endereco;
    }

    @Data
    public static class EnderecoRequest {
        private String rua;
        private String numero;
        private String bairro;
        private String cidade;
        private String cep;
        private String estado;
    }
}