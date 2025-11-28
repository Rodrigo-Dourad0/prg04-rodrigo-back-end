package br.com.ifba.prg04_rodrigo_back_end.service;

import br.com.ifba.prg04_rodrigo_back_end.dto.request.PerfilOrganizadorRequest;
import br.com.ifba.prg04_rodrigo_back_end.dto.request.UsuarioCreateRequest;
import br.com.ifba.prg04_rodrigo_back_end.dto.request.UsuarioUpdateRequest;
import br.com.ifba.prg04_rodrigo_back_end.dto.response.UsuarioResponse;
import br.com.ifba.prg04_rodrigo_back_end.exception.RegraDeNegocioException;
import br.com.ifba.prg04_rodrigo_back_end.model.entity.PerfilOrganizador;
import br.com.ifba.prg04_rodrigo_back_end.model.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;


    @Transactional
    public UsuarioResponse cadastrar(UsuarioCreateRequest request) {


        if (repository.existsByEmail(request.getEmail())) {
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com este e-mail.");
        }


        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setCpf(request.getCpf());
        usuario.setTelefone(request.getTelefone());


        usuario = repository.save(usuario);


        return toResponse(usuario);
    }

    // Método auxiliar para converter Entidade em DTO de Resposta
    private UsuarioResponse toResponse(Usuario entity) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setEmail(entity.getEmail());
        response.setOrganizador(entity.isOrganizador());
        return response;
    }


    @Transactional
    public void tornarOrganizador(Long usuarioId, PerfilOrganizadorRequest request) {


        Usuario usuario = repository.findById(usuarioId)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado com id: " + usuarioId));



        if (usuario.isOrganizador()) {
            throw new RegraDeNegocioException("Este usuário já possui perfil de organizador.");
        }

        //Criar o objeto PerfilOrganizador
        PerfilOrganizador perfil = new PerfilOrganizador();
        perfil.setBio(request.getBio());
        perfil.setChavePix(request.getChavePix());
        perfil.setLinkInstagram(request.getLinkInstagram());
        perfil.setUsuario(usuario); // Liga o perfil ao usuário


        usuario.setPerfilOrganizador(perfil);

        repository.save(usuario);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioUpdateRequest request) {

        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));

        // Só muda se vier algo novo
        if (request.getNome() != null && !request.getNome().isBlank()) {
            usuario.setNome(request.getNome());
        }

        if (request.getTelefone() != null && !request.getTelefone().isBlank()) {
            usuario.setTelefone(request.getTelefone());
        }

        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            //Senha tem que ter no mínimo 6 dígitos
            if (request.getSenha().length() < 6) {
                throw new RegraDeNegocioException("A nova senha deve ter no mínimo 6 caracteres.");
            }
            usuario.setSenha(request.getSenha());
        }


        usuario = repository.save(usuario);

        return toResponse(usuario);
    }

}