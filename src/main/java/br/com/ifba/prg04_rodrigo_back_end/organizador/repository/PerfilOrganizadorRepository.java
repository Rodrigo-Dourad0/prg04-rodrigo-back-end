package br.com.ifba.prg04_rodrigo_back_end.organizador.repository;

import br.com.ifba.prg04_rodrigo_back_end.organizador.entity.Organizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilOrganizadorRepository extends JpaRepository<Organizador, Long> {

}