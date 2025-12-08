package br.com.ifba.prg04_rodrigo_back_end.usuario.service;

import br.com.ifba.prg04_rodrigo_back_end.perfilorganizador.entity.PerfilOrganizador;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioIService {

    Usuario save(Usuario usuario);

    Usuario findById(Long id);

    Page<Usuario> findAll(Pageable pageable);

    void tornarOrganizador(Long id, PerfilOrganizador perfil);

    void delete(Long id);

    Usuario update(Long id, Usuario usuarioComNovosDados);
}