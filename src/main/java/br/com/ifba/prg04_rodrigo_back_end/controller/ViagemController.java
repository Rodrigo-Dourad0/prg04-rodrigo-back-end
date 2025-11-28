package br.com.ifba.prg04_rodrigo_back_end.controller;

import br.com.ifba.prg04_rodrigo_back_end.dto.request.ViagemCreateRequest;
import br.com.ifba.prg04_rodrigo_back_end.dto.response.ViagemResponse;
import br.com.ifba.prg04_rodrigo_back_end.service.ViagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
public class ViagemController {

    private final ViagemService service;


    @PostMapping("/criar")
    public ResponseEntity<ViagemResponse> criar(@RequestBody @Valid ViagemCreateRequest request) {
        ViagemResponse novaViagem = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaViagem);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ViagemResponse>> buscarPorDestino(@RequestParam String destino) {

        List<ViagemResponse> lista = service.buscarPorDestino(destino);

        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id, @RequestParam Long organizadorId) {

        service.cancelar(id, organizadorId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViagemResponse> buscarPorId(@PathVariable Long id) {

        ViagemResponse viagem = service.buscarPorId(id);

        return ResponseEntity.ok(viagem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViagemResponse> atualizar(@PathVariable Long id,
                                                    @RequestParam Long organizadorId,
                                                    @RequestBody @Valid ViagemCreateRequest request) {

        ViagemResponse viagemAtualizada = service.atualizar(id, organizadorId, request);

        return ResponseEntity.ok(viagemAtualizada);
    }

    @GetMapping
    public ResponseEntity<List<ViagemResponse>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }
}