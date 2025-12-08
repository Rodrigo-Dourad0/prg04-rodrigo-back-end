package br.com.ifba.prg04_rodrigo_back_end.usuario.service;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.exception.RegraDeNegocioException;
import br.com.ifba.prg04_rodrigo_back_end.perfilorganizador.entity.PerfilOrganizador;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UsuarioIService {

    private final UsuarioRepository repository;

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {

        if (repository.existsByEmail(usuario.getEmail())) {
            throw new RegraDeNegocioException("Já existe um usuário cadastrado com este e-mail.");
        }

        return repository.save(usuario);
    }

    @Override
    public Usuario findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado com id: " + id));
    }

    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional
    public void tornarOrganizador(Long usuarioId, PerfilOrganizador perfil) {
        Usuario usuario = findById(usuarioId);

        if (usuario.isOrganizador()) {
            throw new RegraDeNegocioException("Este usuário já possui perfil de organizador.");
        }

        perfil.setUsuario(usuario);
        usuario.setPerfilOrganizador(perfil);


        repository.save(usuario);
    }

    @Override
    @Transactional
    public void delete(Long id) {

       //Por enquanto esta deletando sem a lógica de verificar se ele tem viagens em seu nome, etc...
        Usuario usuario = findById(id);

        repository.delete(usuario);
    }


    @Override
    @Transactional
    public Usuario update(Long id, Usuario usuarioComNovosDados) {
        Usuario usuarioExistente = findById(id);


        if (usuarioComNovosDados.getNome() != null && !usuarioComNovosDados.getNome().isBlank()) {
            usuarioExistente.setNome(usuarioComNovosDados.getNome());
        }

        if (usuarioComNovosDados.getTelefone() != null && !usuarioComNovosDados.getTelefone().isBlank()) {
            usuarioExistente.setTelefone(usuarioComNovosDados.getTelefone());
        }

        if (usuarioComNovosDados.getSenha() != null && !usuarioComNovosDados.getSenha().isBlank()) {
            if (usuarioComNovosDados.getSenha().length() < 6) {
                throw new RegraDeNegocioException("A nova senha deve ter no mínimo 6 caracteres.");
            }
            usuarioExistente.setSenha(usuarioComNovosDados.getSenha());
        }

        return repository.save(usuarioExistente);
    }
}