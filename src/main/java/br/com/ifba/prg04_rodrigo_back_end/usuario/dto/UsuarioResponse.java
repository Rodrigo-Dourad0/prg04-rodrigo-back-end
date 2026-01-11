package br.com.ifba.prg04_rodrigo_back_end.usuario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private  String telefone;
    private String enderecoCompleto;

    @JsonProperty("isOrganizador")
    private boolean organizadorAtivo;
}