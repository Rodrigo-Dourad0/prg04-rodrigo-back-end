package br.com.ifba.prg04_rodrigo_back_end.service;

import br.com.ifba.prg04_rodrigo_back_end.dto.request.ViagemCreateRequest;
import br.com.ifba.prg04_rodrigo_back_end.dto.response.ViagemResponse;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViagemService {

    private final ViagemRepository viagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaRepository reservaRepository;

    @Transactional
    public ViagemResponse criar(ViagemCreateRequest request) {
        // 1. Buscar o usuário que está tentando criar a viagem
        Usuario usuario = usuarioRepository.findById(request.getOrganizadorId())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));

        // 2. Ele é organizador?
        if (!usuario.isOrganizador()) {
            throw new RegraDeNegocioException("Este usuário não possui perfil de organizador e não pode criar viagens.");
        }

        // 3. Montar a Viagem (DTO -> Entity)
        Viagem viagem = new Viagem();
        viagem.setTitulo(request.getTitulo());
        viagem.setDestino(request.getDestino());
        viagem.setDescricao(request.getDescricao());
        viagem.setDataPartida(request.getDataPartida());
        viagem.setDataRetorno(request.getDataRetorno());
        viagem.setPreco(request.getPreco());
        viagem.setVagasTotais(request.getVagasTotais());
        viagem.setStatus(StatusViagem.ABERTA);

        // Liga a viagem ao Perfil de Organizador do usuário
        viagem.setOrganizador(usuario.getPerfilOrganizador());


        viagem = viagemRepository.save(viagem);

        return toResponse(viagem);
    }

    // Método extra para Listar Todas
    public List<ViagemResponse> listarTodas() {
        return viagemRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ViagemResponse> buscarPorDestino(String destino) {

        return viagemRepository.findByDestinoContainingIgnoreCase(destino)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelar(Long viagemId, Long solicitanteId) {

        Viagem viagem = viagemRepository.findById(viagemId)
                .orElseThrow(() -> new RegraDeNegocioException("Viagem não encontrada."));

        // Validações de segurança e status
        Long donoId = viagem.getOrganizador().getUsuario().getId();
        if (!donoId.equals(solicitanteId)) {
            throw new RegraDeNegocioException("Apenas o organizador pode cancelar esta viagem.");
        }
        if (viagem.getStatus() == StatusViagem.CANCELADA) {
            throw new RegraDeNegocioException("Esta viagem já foi cancelada.");
        }
        if (viagem.getStatus() == StatusViagem.FINALIZADA) {
            throw new RegraDeNegocioException("Não é possível cancelar uma viagem que já foi concluída.");
        }

        //Muda o status da Viagem
        viagem.setStatus(StatusViagem.CANCELADA);
        viagemRepository.save(viagem);


        List<Reserva> reservasDaViagem = reservaRepository.findByViagem(viagem);

        for (Reserva reserva : reservasDaViagem) {
            if (reserva.getStatus() == StatusPagamento.PAGO) {
                // Se pagou, reembolsa
                reserva.setStatus(StatusPagamento.REEMBOLSADO);
            } else {
                // Se não pagou, só cancela
                reserva.setStatus(StatusPagamento.CANCELADO);
            }
        }


        reservaRepository.saveAll(reservasDaViagem);
    }

    public ViagemResponse buscarPorId(Long id) {
        Viagem viagem = viagemRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Viagem não encontrada com id: " + id));

        return toResponse(viagem);
    }

    @Transactional
    public ViagemResponse atualizar(Long viagemId, Long solicitanteId, ViagemCreateRequest request) {

        Viagem viagem = viagemRepository.findById(viagemId)
                .orElseThrow(() -> new RegraDeNegocioException("Viagem não encontrada."));

        //Apenas o dono mexe
        if (!viagem.getOrganizador().getUsuario().getId().equals(solicitanteId)) {
            throw new RegraDeNegocioException("Apenas o organizador pode editar esta viagem.");
        }


        // Verificamos se o usuário tentou mudar algo proibido comparando com o que já está no banco.

        if (!viagem.getDestino().equals(request.getDestino()) ||
                !viagem.getDataPartida().isEqual(request.getDataPartida()) ||
                !viagem.getDataRetorno().isEqual(request.getDataRetorno()) ||
                viagem.getPreco().compareTo(request.getPreco()) != 0 ||
                !viagem.getVagasTotais().equals(request.getVagasTotais())) {

            throw new RegraDeNegocioException("Não é permitido alterar Destino, Datas, Preço ou Vagas. Para corrigir esses dados, cancele e crie uma nova viagem.");
        }

        //Se passou pela trava, atualiza SÓ os cosméticos
        viagem.setTitulo(request.getTitulo());
        viagem.setDescricao(request.getDescricao());

        //Salva
        viagem = viagemRepository.save(viagem);

        return toResponse(viagem);
    }



    // Conversor Auxiliar (Entity -> DTO Response)
    private ViagemResponse toResponse(Viagem entity) {
        ViagemResponse response = new ViagemResponse();
        response.setId(entity.getId());
        response.setTitulo(entity.getTitulo());
        response.setDestino(entity.getDestino());
        response.setDescricao(entity.getDescricao());
        response.setDataPartida(entity.getDataPartida());
        response.setPreco(entity.getPreco());
        response.setVagasTotais(entity.getVagasTotais());
        response.setStatus(entity.getStatus());

        response.setNomeOrganizador(entity.getOrganizador().getUsuario().getNome());
        return response;
    }
}