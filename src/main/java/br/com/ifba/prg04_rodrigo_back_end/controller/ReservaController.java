package br.com.ifba.prg04_rodrigo_back_end.controller;

import br.com.ifba.prg04_rodrigo_back_end.dto.request.ReservaCreateRequest;
import br.com.ifba.prg04_rodrigo_back_end.dto.response.ReservaResponse;
import br.com.ifba.prg04_rodrigo_back_end.service.ReservaService;
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

    private final ReservaService service;


    @PostMapping("/nova")
    public ResponseEntity<ReservaResponse> criar(@RequestBody @Valid ReservaCreateRequest request) {
        ReservaResponse novaReserva = service.reservar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaReserva);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ReservaResponse>> listarMinhasReservas(@PathVariable Long usuarioId) {

        List<ReservaResponse> lista = service.listarReservasPorUsuario(usuarioId);

        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {

        service.cancelar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/viagem/{viagemId}")
    public ResponseEntity<List<ReservaResponse>> listarPorViagem(@PathVariable Long viagemId,
                                                                 @RequestParam Long organizadorId) {

        // Passamos os dois IDs para o service validar
        List<ReservaResponse> lista = service.listarPorViagem(viagemId, organizadorId);

        return ResponseEntity.ok(lista);
    }


    @PutMapping("/{id}/confirmar-pagamento")
    public ResponseEntity<Void> confirmarPagamento(@PathVariable Long id, @RequestParam Long organizadorId) {

        service.confirmarPagamento(id, organizadorId);

        return ResponseEntity.noContent().build();
    }



}