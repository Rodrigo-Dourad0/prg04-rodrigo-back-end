package br.com.ifba.prg04_rodrigo_back_end.reserva.service;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.exception.RegraDeNegocioException;
import br.com.ifba.prg04_rodrigo_back_end.viagem.enums.StatusPagamento;
import br.com.ifba.prg04_rodrigo_back_end.viagem.enums.StatusViagem;
import br.com.ifba.prg04_rodrigo_back_end.reserva.entity.Reserva;
import br.com.ifba.prg04_rodrigo_back_end.reserva.repository.ReservaRepository;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.usuario.repository.UsuarioRepository;
import br.com.ifba.prg04_rodrigo_back_end.viagem.entity.Viagem;
import br.com.ifba.prg04_rodrigo_back_end.viagem.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService implements ReservaIService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ViagemRepository viagemRepository;

    @Override
    @Transactional
    public Reserva save(Reserva reserva, Long usuarioId, Long viagemId) {
        if (reserva.getQuantidadeLugares() > 5) {
            throw new RegraDeNegocioException("Máximo de 5 assentos por vez.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));
        Viagem viagem = viagemRepository.findById(viagemId)
                .orElseThrow(() -> new RegraDeNegocioException("Viagem não encontrada."));

        if (viagem.getStatus() != StatusViagem.ABERTA) {
            throw new RegraDeNegocioException("Viagem não está aberta.");
        }
        if (viagem.getOrganizador().getUsuario().getId().equals(usuario.getId())) {
            throw new RegraDeNegocioException("Organizador não pode reservar vaga na própria excursão.");
        }

        // Lógica de vagas
        Integer totalVendidos = reservaRepository.totalLugaresReservados(viagem);
        Integer vagasRestantes = viagem.getVagasTotais() - totalVendidos;

        if (reserva.getQuantidadeLugares() > vagasRestantes) {
            throw new RegraDeNegocioException("Vagas insuficientes. Restam apenas: " + vagasRestantes);
        }

        BigDecimal valorTotal = viagem.getPreco().multiply(new BigDecimal(reserva.getQuantidadeLugares()));

        reserva.setUsuario(usuario);
        reserva.setViagem(viagem);
        reserva.setValorTotal(valorTotal);
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setStatus(StatusPagamento.PENDENTE);

        Reserva reservaSalva = reservaRepository.save(reserva);

        // Atualiza status se lotou
        if ((totalVendidos + reserva.getQuantidadeLugares()) == viagem.getVagasTotais()) {
            viagem.setStatus(StatusViagem.LOTADA);
            viagemRepository.save(viagem);
        }

        return reservaSalva;
    }

    @Override
    public Page<Reserva> listarReservasPorUsuario(Long usuarioId, Pageable pageable) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));


        return reservaRepository.findByUsuario(usuario, pageable);
    }

    @Override
    public Page<Reserva> listarPorViagem(Long viagemId, Long solicitanteId, Pageable pageable) {
        Viagem viagem = viagemRepository.findById(viagemId)
                .orElseThrow(() -> new RegraDeNegocioException("Viagem não encontrada."));

        Long donoId = viagem.getOrganizador().getUsuario().getId();
        if (!donoId.equals(solicitanteId)) {
            throw new RegraDeNegocioException("Acesso negado.");
        }


        return reservaRepository.findByViagem(viagem, pageable);
    }

    @Override
    public void delete(Long reservaId) {
        if (!reservaRepository.existsById(reservaId)) {
            throw new RegraDeNegocioException("Reserva não encontrada.");
        }
        reservaRepository.deleteById(reservaId);
    }

    @Override
    @Transactional
    public void confirmarPagamento(Long reservaId, Long solicitanteId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RegraDeNegocioException("Reserva não encontrada."));

        Long donoId = reserva.getViagem().getOrganizador().getUsuario().getId();
        if (!donoId.equals(solicitanteId)) {
            throw new RegraDeNegocioException("Apenas o organizador pode confirmar.");
        }
        if (reserva.getStatus() != StatusPagamento.PENDENTE) {
            throw new RegraDeNegocioException("Reserva não está pendente.");
        }

        reserva.setStatus(StatusPagamento.PAGO);
        reservaRepository.save(reserva);
    }
}