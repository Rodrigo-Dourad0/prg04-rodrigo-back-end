package br.com.ifba.prg04_rodrigo_back_end.viagem.service;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.exception.RegraDeNegocioException;
import br.com.ifba.prg04_rodrigo_back_end.reserva.entity.Reserva;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.viagem.entity.Viagem;
import br.com.ifba.prg04_rodrigo_back_end.viagem.enums.StatusPagamento;
import br.com.ifba.prg04_rodrigo_back_end.viagem.enums.StatusViagem;
import br.com.ifba.prg04_rodrigo_back_end.reserva.repository.ReservaRepository;
import br.com.ifba.prg04_rodrigo_back_end.usuario.repository.UsuarioRepository;
import br.com.ifba.prg04_rodrigo_back_end.viagem.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViagemService implements ViagemIService {

    private final ViagemRepository viagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;

    @Override
    @Transactional
    public Viagem save(Viagem viagem, Long organizadorId) {
        Usuario usuario = usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));

        if (!usuario.isOrganizador()) {
            throw new RegraDeNegocioException("Este usuário não possui perfil de organizador.");
        }

        viagem.setStatus(StatusViagem.ABERTA);
        viagem.setOrganizador(usuario.getOrganizador());

        return viagemRepository.save(viagem);
    }

    @Override
    public Page<Viagem> findAll(Pageable pageable) {
        return viagemRepository.findAll(pageable);
    }

    @Override
    public List<Viagem> findByDestino(String destino) {
        return viagemRepository.findByDestinoContainingIgnoreCase(destino);
    }

    @Override
    public Viagem findById(Long id) {
        return viagemRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Viagem não encontrada com id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long viagemId, Long solicitanteId) { // Cancelar
        Viagem viagem = findById(viagemId);

        Long donoId = viagem.getOrganizador().getUsuario().getId();
        if (!donoId.equals(solicitanteId)) {
            throw new RegraDeNegocioException("Apenas o organizador pode cancelar esta viagem.");
        }
        if (viagem.getStatus() == StatusViagem.CANCELADA) {
            throw new RegraDeNegocioException("Esta viagem já foi cancelada.");
        }
        if (viagem.getStatus() == StatusViagem.FINALIZADA) {
            throw new RegraDeNegocioException("Não é possível cancelar uma viagem concluída.");
        }

        viagem.setStatus(StatusViagem.CANCELADA);
        viagemRepository.save(viagem);

        // Cascata de cancelamento
        List<Reserva> reservas = reservaRepository.findByViagem(viagem);
        for (Reserva r : reservas) {
            if (r.getStatus() == StatusPagamento.PAGO) {
                r.setStatus(StatusPagamento.REEMBOLSADO);
            } else {
                r.setStatus(StatusPagamento.CANCELADO);
            }
        }
        reservaRepository.saveAll(reservas);
    }

    @Override
    @Transactional
    public Viagem update(Long viagemId, Long solicitanteId, Viagem novosDados) {
        Viagem viagem = findById(viagemId);

        if (!viagem.getOrganizador().getUsuario().getId().equals(solicitanteId)) {
            throw new RegraDeNegocioException("Apenas o organizador pode editar esta viagem.");
        }

        // Validação de segurança (Trava de edição)
        List<Reserva> reservas = reservaRepository.findByViagem(viagem);
        boolean temReservas = !reservas.isEmpty();

        if (temReservas) {
            // Se tem reservas, dados críticos não podem mudar
            if (!viagem.getDestino().equals(novosDados.getDestino()) ||
                    !viagem.getDataPartida().isEqual(novosDados.getDataPartida()) ||
                    !viagem.getDataRetorno().isEqual(novosDados.getDataRetorno()) ||
                    viagem.getPreco().compareTo(novosDados.getPreco()) != 0 ||
                    !viagem.getVagasTotais().equals(novosDados.getVagasTotais())) {

                throw new RegraDeNegocioException("Não é permitido alterar Destino, Datas, Preço ou Vagas com reservas ativas.");
            }
        } else {
            // Se não tem reservas, pode atualizar tudo
            viagem.setDestino(novosDados.getDestino());
            viagem.setDataPartida(novosDados.getDataPartida());
            viagem.setDataRetorno(novosDados.getDataRetorno());
            viagem.setPreco(novosDados.getPreco());
            viagem.setVagasTotais(novosDados.getVagasTotais());

        }

        viagem.setTitulo(novosDados.getTitulo());
        viagem.setDescricao(novosDados.getDescricao());
        viagem.setImagemUrl(novosDados.getImagemUrl());

        return viagemRepository.save(viagem);
    }
}