package br.com.ifba.prg04_rodrigo_back_end.perfilorganizador.repository;

import br.com.ifba.prg04_rodrigo_back_end.perfilorganizador.entity.PerfilOrganizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilOrganizadorRepository extends JpaRepository<PerfilOrganizador, Long> {

}