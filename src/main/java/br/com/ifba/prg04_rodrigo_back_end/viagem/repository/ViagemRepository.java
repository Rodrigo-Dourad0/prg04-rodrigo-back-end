package br.com.ifba.prg04_rodrigo_back_end.viagem.repository;

import br.com.ifba.prg04_rodrigo_back_end.viagem.entity.Viagem;
import br.com.ifba.prg04_rodrigo_back_end.viagem.enums.StatusViagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViagemRepository extends JpaRepository<Viagem, Long> {

    // Busca viagens por status (ex: listar todas as "ABERTA")
    List<Viagem> findByStatus(StatusViagem status);

    // Busca viagens contendo o nome do destino (ex: "Chapada")
    List<Viagem> findByDestinoContainingIgnoreCase(String destino);
}