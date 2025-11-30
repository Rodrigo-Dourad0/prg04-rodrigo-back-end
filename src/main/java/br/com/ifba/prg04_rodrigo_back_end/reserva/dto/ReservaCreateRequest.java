package br.com.ifba.prg04_rodrigo_back_end.reserva.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservaCreateRequest {

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "O ID da viagem é obrigatório")
    private Long viagemId;

    @NotNull(message = "Informe a quantidade de lugares")
    @Min(value = 1, message = "Pelo menos 1 lugar deve ser reservado")
    private Integer quantidadeLugares;
}