package br.com.ifba.prg04_rodrigo_back_end.usuario.dto;

import lombok.Data;

@Data
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;

    // O front-end precisa saber disso para mostrar botões extras
    private boolean isOrganizador;
}