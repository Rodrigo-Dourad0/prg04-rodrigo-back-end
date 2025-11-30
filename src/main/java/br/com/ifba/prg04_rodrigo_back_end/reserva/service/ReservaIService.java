package br.com.ifba.prg04_rodrigo_back_end.reserva.service;

import br.com.ifba.prg04_rodrigo_back_end.reserva.entity.Reserva;

import java.util.List;

public interface ReservaIService {

    Reserva save(Reserva reserva, Long usuarioId, Long viagemId);

    List<Reserva> listarReservasPorUsuario(Long usuarioId);

    List<Reserva> listarPorViagem(Long viagemId, Long solicitanteId);

    void delete(Long reservaId);

    void confirmarPagamento(Long reservaId, Long solicitanteId);

}