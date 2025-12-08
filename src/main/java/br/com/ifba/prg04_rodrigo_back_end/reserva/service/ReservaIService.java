package br.com.ifba.prg04_rodrigo_back_end.reserva.service;

import br.com.ifba.prg04_rodrigo_back_end.reserva.entity.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservaIService {

    Reserva save(Reserva reserva, Long usuarioId, Long viagemId);

    Page<Reserva> listarReservasPorUsuario(Long usuarioId, Pageable pageable);

    Page<Reserva> listarPorViagem(Long viagemId, Long solicitanteId, Pageable pageable);

    void delete(Long reservaId);

    void confirmarPagamento(Long reservaId, Long solicitanteId);

}