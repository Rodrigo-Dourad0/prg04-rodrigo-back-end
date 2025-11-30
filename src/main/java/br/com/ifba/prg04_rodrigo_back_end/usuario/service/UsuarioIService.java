package br.com.ifba.prg04_rodrigo_back_end.usuario.service;

import br.com.ifba.prg04_rodrigo_back_end.perfilorganizador.entity.PerfilOrganizador;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;

public interface UsuarioIService {

    Usuario save(Usuario usuario);

    Usuario findById(Long id);


    void tornarOrganizador(Long id, PerfilOrganizador perfil);


    Usuario update(Long id, Usuario usuarioComNovosDados);
}