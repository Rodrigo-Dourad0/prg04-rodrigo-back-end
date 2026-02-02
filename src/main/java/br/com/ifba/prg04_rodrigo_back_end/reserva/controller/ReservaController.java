package br.com.ifba.prg04_rodrigo_back_end.reserva.controller;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.mapper.ObjectMapperUtil;
import br.com.ifba.prg04_rodrigo_back_end.reserva.dto.ReservaCreateRequest;
import br.com.ifba.prg04_rodrigo_back_end.reserva.dto.ReservaResponse;
import br.com.ifba.prg04_rodrigo_back_end.reserva.entity.Reserva;
import br.com.ifba.prg04_rodrigo_back_end.reserva.service.ReservaIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaIService service;
    private final ObjectMapperUtil objectMapper;

    @PostMapping("/nova")
    public ResponseEntity<ReservaResponse> criar(@RequestBody @Valid ReservaCreateRequest request) {
        Reserva entidade = new Reserva();
        entidade.setQuantidadeLugares(request.getQuantidadeLugares());

        Reserva salva = service.save(entidade, request.getUsuarioId(), request.getViagemId());

        ReservaResponse response = objectMapper.map(salva, ReservaResponse.class);
        response.setNomeUsuario(salva.getUsuario().getPessoa().getNome());
        response.setTituloViagem(salva.getViagem().getTitulo());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<ReservaResponse>> listarMinhasReservas(
            @PathVariable Long usuarioId,
            @PageableDefault(page = 0, size = 10, sort = "dataReserva") Pageable pageable) {

        Page<Reserva> reservas = service.listarReservasPorUsuario(usuarioId, pageable);

        Page<ReservaResponse> response = reservas.map(reserva -> {
            ReservaResponse dto = objectMapper.map(reserva, ReservaResponse.class);
            dto.setNomeUsuario(reserva.getUsuario().getPessoa().getNome());
            dto.setTituloViagem(reserva.getViagem().getTitulo());
            return dto;
        });

        return ResponseEntity.ok(response);
    }

    @GetMapping("/viagem/{viagemId}")
    public ResponseEntity<Page<ReservaResponse>> listarPorViagem(
            @PathVariable Long viagemId,
            @RequestParam Long organizadorId,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Page<Reserva> reservas = service.listarPorViagem(viagemId, organizadorId, pageable);

        Page<ReservaResponse> response = reservas.map(reserva ->
                objectMapper.map(reserva, ReservaResponse.class)
        );

        return ResponseEntity.ok(response);
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