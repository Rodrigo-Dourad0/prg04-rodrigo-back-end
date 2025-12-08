package br.com.ifba.prg04_rodrigo_back_end.reserva.repository;

import br.com.ifba.prg04_rodrigo_back_end.reserva.entity.Reserva;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.viagem.entity.Viagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Busca todas as reservas de um usuário específico
    Page<Reserva> findByUsuario(Usuario usuario, Pageable pageable);


    // "Soma a quantidade de lugares de todas as reservas dessa viagem.
    // Se não tiver nenhuma reservam retorna 0
    @Query("SELECT COALESCE(SUM(r.quantidadeLugares), 0) FROM Reserva r WHERE r.viagem = :viagem")
    Integer totalLugaresReservados(@Param("viagem") Viagem viagem);


    // Buscar todas as reservas de uma viagem específica, a API com paginação
    Page<Reserva> findByViagem(Viagem viagem, Pageable pageable);

    //Versão em lista para validação interna
    List<Reserva> findByViagem(Viagem viagem);

}
