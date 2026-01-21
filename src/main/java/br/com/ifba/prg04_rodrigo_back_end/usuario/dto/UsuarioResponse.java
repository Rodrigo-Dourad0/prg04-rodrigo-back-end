package br.com.ifba.prg04_rodrigo_back_end.usuario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private  String telefone;

    //Para exibir formatado
    private String enderecoCompleto;

    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;



    @JsonProperty("isOrganizador")
    private boolean organizadorAtivo;
}