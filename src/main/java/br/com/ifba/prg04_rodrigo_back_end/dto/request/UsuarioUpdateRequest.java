package br.com.ifba.prg04_rodrigo_back_end.dto.request;

import lombok.Data;

@Data
public class UsuarioUpdateRequest {
    // Não usamos @NotBlank aqui porque o campo pode vir nulo (significa que não quer alterar)
    private String nome;
    private String telefone;
    private String senha;
}