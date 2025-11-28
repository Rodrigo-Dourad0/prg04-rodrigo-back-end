package br.com.ifba.prg04_rodrigo_back_end.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PerfilOrganizadorRequest {

    @NotBlank(message = "A bio é obrigatória para passar credibilidade")
    private String bio;

    @NotBlank(message = "A chave PIX é obrigatória para receber pagamentos")
    private String chavePix;

    private String linkInstagram; //Opcional
}