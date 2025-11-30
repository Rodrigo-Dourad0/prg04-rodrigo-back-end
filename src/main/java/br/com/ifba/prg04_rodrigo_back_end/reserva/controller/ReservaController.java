package br.com.ifba.prg04_rodrigo_back_end.reserva.controller;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.mapper.ObjectMapperUtil;
import br.com.ifba.prg04_rodrigo_back_end.reserva.dto.ReservaCreateRequest;
import br.com.ifba.prg04_rodrigo_back_end.reserva.dto.ReservaResponse;
import br.com.ifba.prg04_rodrigo_back_end.reserva.entity.Reserva;
import br.com.ifba.prg04_rodrigo_back_end.reserva.service.ReservaIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaIService service;
    private final ObjectMapperUtil objectMapper;

    @PostMapping("/nova")
    public ResponseEntity<ReservaResponse> criar(@RequestBody @Valid ReservaCreateRequest request) {
        // DTO -> Entidade (Mapeia a quantidade de lugares)
        Reserva entidade = objectMapper.map(request, Reserva.class);

        Reserva salva = service.save(entidade, request.getUsuarioId(), request.getViagemId());

        ReservaResponse response = objectMapper.map(salva, ReservaResponse.class);
        response.setNomeUsuario(salva.getUsuario().getNome());
        response.setTituloViagem(salva.getViagem().getTitulo());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaResponse>> listarMinhasReservas(@PathVariable Long usuarioId) {
        List<Reserva> reservas = service.listarReservasPorUsuario(usuarioId);
        return ResponseEntity.ok(objectMapper.mapAll(reservas, ReservaResponse.class));
    }

    @GetMapping("/viagem/{viagemId}")
    public ResponseEntity<List<ReservaResponse>> listarPorViagem(@PathVariable Long viagemId,
                                                                 @RequestParam Long organizadorId) {
        List<Reserva> reservas = service.listarPorViagem(viagemId, organizadorId);
        return ResponseEntity.ok(objectMapper.mapAll(reservas, ReservaResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/confirmar-pagamento")
    public ResponseEntity<Void> confirmarPagamento(@PathVariable Long id, @RequestParam Long organizadorId) {
        service.confirmarPagamento(id, organizadorId);
        return ResponseEntity.noContent().build();
    }
}