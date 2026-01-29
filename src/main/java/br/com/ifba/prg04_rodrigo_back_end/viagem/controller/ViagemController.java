package br.com.ifba.prg04_rodrigo_back_end.viagem.controller;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.mapper.ObjectMapperUtil;
import br.com.ifba.prg04_rodrigo_back_end.viagem.dto.ViagemCreateRequest;
import br.com.ifba.prg04_rodrigo_back_end.viagem.dto.ViagemResponse;
import br.com.ifba.prg04_rodrigo_back_end.viagem.entity.Viagem;
import br.com.ifba.prg04_rodrigo_back_end.viagem.service.ViagemIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
public class ViagemController {

    private final ViagemIService service;
    private final ObjectMapperUtil objectMapper;

    @PostMapping("/criar")
    public ResponseEntity<ViagemResponse> criar(@RequestBody @Valid ViagemCreateRequest request) {
        // DTO -> Entidade
        Viagem entidade = objectMapper.map(request, Viagem.class);

        System.out.println("URL CHEGOU? " + request.getImagemUrl());

        entidade.setId(null);

        // Service
        Viagem salva = service.save(entidade, request.getOrganizadorId());

        // Entidade -> DTO
        ViagemResponse response = objectMapper.map(salva, ViagemResponse.class);
        response.setNomeOrganizador(salva.getOrganizador().getUsuario().getPessoa().getNome());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ViagemResponse>> listarTodas(
            @PageableDefault(page = 0, size = 10, sort = "dataPartida") Pageable pageable) {
        
        Page<Viagem> viagensPage = service.findAll(pageable);


        Page<ViagemResponse> response = viagensPage.map(viagem -> {
            ViagemResponse dto = objectMapper.map(viagem, ViagemResponse.class);


            dto.setNomeOrganizador(viagem.getOrganizador().getUsuario().getPessoa().getNome());
            return dto;
        });

        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ViagemResponse>> buscarPorDestino(@RequestParam String destino) {
        List<Viagem> viagens = service.findByDestino(destino);
        return ResponseEntity.ok(objectMapper.mapAll(viagens, ViagemResponse.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViagemResponse> buscarPorId(@PathVariable Long id) {
        Viagem viagem = service.findById(id);

        ViagemResponse response = objectMapper.map(viagem, ViagemResponse.class);
        response.setNomeOrganizador(viagem.getOrganizador().getUsuario().getPessoa().getNome());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id, @RequestParam Long organizadorId) {
        service.delete(id, organizadorId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ViagemResponse> atualizar(@PathVariable Long id,
                                                    @RequestParam Long organizadorId,
                                                    @RequestBody @Valid ViagemCreateRequest request) {
        // DTO -> Entidade
        Viagem novosDados = objectMapper.map(request, Viagem.class);

        Viagem atualizada = service.update(id, organizadorId, novosDados);

        ViagemResponse response = objectMapper.map(atualizada, ViagemResponse.class);
        response.setNomeOrganizador(atualizada.getOrganizador().getUsuario().getPessoa().getNome());
        return ResponseEntity.ok(response);
    }
}