package br.com.ifba.prg04_rodrigo_back_end.viagem.service;

import br.com.ifba.prg04_rodrigo_back_end.viagem.entity.Viagem;

import java.util.List;

public interface ViagemIService {

    Viagem save(Viagem viagem, Long organizadorId);

    List<Viagem> findAll();

    List<Viagem> findByDestino(String destino);

    Viagem findById(Long id);

    void delete(Long id, Long solicitanteId); // Cancelar

    Viagem update(Long id, Long solicitanteId, Viagem viagemComNovosDados);

}