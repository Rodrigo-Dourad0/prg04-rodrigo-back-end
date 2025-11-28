package br.com.ifba.prg04_rodrigo_back_end.service;

import br.com.ifba.prg04_rodrigo_back_end.dto.request.ReservaCreateRequest;
import br.com.ifba.prg04_rodrigo_back_end.dto.response.ReservaResponse;
import br.com.ifba.prg04_rodrigo_back_end.exception.RegraDeNegocioException;
import br.com.ifba.prg04_rodrigo_back_end.model.entity.Reserva;
import br.com.ifba.prg04_rodrigo_back_end.model.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.model.entity.Viagem;
import br.com.ifba.prg04_rodrigo_back_end.model.enums.StatusPagamento;
import br.com.ifba.prg04_rodrigo_back_end.model.enums.StatusViagem;
import br.com.ifba.prg04_rodrigo_back_end.repository.ReservaRepository;
import br.com.ifba.prg04_rodrigo_back_end.repository.UsuarioRepository;
import br.com.ifba.prg04_rodrigo_back_end.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ViagemRepository viagemRepository;

    @Transactional
    public ReservaResponse reservar(ReservaCreateRequest request) {

        // Limite de assentos por compra
        if (request.getQuantidadeLugares() > 5) {
            throw new RegraDeNegocioException("Não é permitido reservar mais de 5 assentos por vez.");
        }

        // Encontrar Usuário e Viagem no banco
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado com id: " + request.getUsuarioId()));

        Viagem viagem = viagemRepository.findById(request.getViagemId())
                .orElseThrow(() -> new RegraDeNegocioException("Viagem não encontrada com id: " + request.getViagemId()));

        //  Status da Viagem
        if (viagem.getStatus() != StatusViagem.ABERTA) {
            throw new RegraDeNegocioException("Esta viagem não está aceitando reservas no momento. Status atual: " + viagem.getStatus());
        }

        // Organizador não compra própria viagem
        if (viagem.getOrganizador().getUsuario().getId().equals(usuario.getId())) {
            throw new RegraDeNegocioException("O organizador não pode reservar vaga na própria excursão.");
        }


        // Descobre quantos já foram vendidos
        Integer totalVendidos = reservaRepository.totalLugaresReservados(viagem);

        // Calcula quantos restam
        Integer vagasRestantes = viagem.getVagasTotais() - totalVendidos;

        // Verifica se cabe o novo pedido
        if (request.getQuantidadeLugares() > vagasRestantes) {
            throw new RegraDeNegocioException("Não há vagas suficientes. Restam apenas: " + vagasRestantes);
        }

        //CÁLCULO FINANCEIRO
        BigDecimal valorTotal = viagem.getPreco().multiply(new BigDecimal(request.getQuantidadeLugares()));


        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setViagem(viagem);
        reserva.setQuantidadeLugares(request.getQuantidadeLugares());
        reserva.setValorTotal(valorTotal);
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setStatus(StatusPagamento.PENDENTE);


        reserva = reservaRepository.save(reserva);

        // Se após essa venda as vagas acabarem, muda o status da viagem para LOTADA
        if ((totalVendidos + request.getQuantidadeLugares()) == viagem.getVagasTotais()) {
            viagem.setStatus(StatusViagem.LOTADA);
            viagemRepository.save(viagem);
        }

        // RETORNO (DTO)
        return toResponse(reserva);
    }

    // Método auxiliar para converter Entidade -> DTO
    private ReservaResponse toResponse(Reserva entity) {
        ReservaResponse response = new ReservaResponse();
        response.setId(entity.getId());

        response.setNomeUsuario(entity.getUsuario().getNome());
        response.setTituloViagem(entity.getViagem().getTitulo());

        response.setQuantidadeLugares(entity.getQuantidadeLugares());
        response.setValorTotal(entity.getValorTotal());
        response.setStatus(entity.getStatus());
        response.setDataReserva(entity.getDataReserva());

        return response;
    }


    // Método de Leitura (READ)
    public List<ReservaResponse> listarReservasPorUsuario(Long usuarioId) {

        //Verificar se o usuário existe
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));

        // Buscar no Repository
        List<Reserva> reservas = reservaRepository.findByUsuario(usuario);

        //Converter a lista de Entidades para lista de DTOs
        return reservas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    // Método DELETE (Cancelar/Excluir)
    public void cancelar(Long reservaId) {

        // Verificar se a reserva existe antes de tentar apagar
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RegraDeNegocioException("Reserva não encontrada para cancelamento."));


        // Apagar do banco
        reservaRepository.delete(reserva);
    }


    public List<ReservaResponse> listarPorViagem(Long viagemId, Long solicitanteId) {

        //Buscar a viagem
        Viagem viagem = viagemRepository.findById(viagemId)
                .orElseThrow(() -> new RegraDeNegocioException("Viagem não encontrada."));

        //  O solicitante é o dono da viagem?
        Long donoDaViagemId = viagem.getOrganizador().getUsuario().getId();

        if (!donoDaViagemId.equals(solicitanteId)) {
            throw new RegraDeNegocioException("Acesso negado. Apenas o organizador desta viagem pode ver a lista de passageiros.");
        }

        // Se passou, busca as reservas
        List<Reserva> reservas = reservaRepository.findByViagem(viagem);

        return reservas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }



    @Transactional
    public void confirmarPagamento(Long reservaId, Long solicitanteId) {

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RegraDeNegocioException("Reserva não encontrada."));

        //Quem está tentando confirmar é o dono da viagem?
        Long donoDaViagemId = reserva.getViagem().getOrganizador().getUsuario().getId();

        if (!donoDaViagemId.equals(solicitanteId)) {
            throw new RegraDeNegocioException("Apenas o organizador pode confirmar pagamentos desta viagem.");
        }

        //Só pode pagar se estiver PENDENTE
        if (reserva.getStatus() != StatusPagamento.PENDENTE) {
            throw new RegraDeNegocioException("Não é possível confirmar pagamento de uma reserva com status: " + reserva.getStatus());
        }

        reserva.setStatus(StatusPagamento.PAGO);
        reservaRepository.save(reserva);
    }


}