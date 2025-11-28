package br.com.ifba.prg04_rodrigo_back_end.repository;

import br.com.ifba.prg04_rodrigo_back_end.model.entity.PerfilOrganizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilOrganizadorRepository extends JpaRepository<PerfilOrganizador, Long> {

}