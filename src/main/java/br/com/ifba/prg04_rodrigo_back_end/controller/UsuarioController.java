package br.com.ifba.prg04_rodrigo_back_end.controller;

import br.com.ifba.prg04_rodrigo_back_end.dto.request.PerfilOrganizadorRequest;
import br.com.ifba.prg04_rodrigo_back_end.dto.request.UsuarioCreateRequest;
import br.com.ifba.prg04_rodrigo_back_end.dto.request.UsuarioUpdateRequest;
import br.com.ifba.prg04_rodrigo_back_end.dto.response.UsuarioResponse;
import br.com.ifba.prg04_rodrigo_back_end.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;


    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody @Valid UsuarioCreateRequest request) {

        UsuarioResponse novoUsuario = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PostMapping("/tornar-organizador/{id}")
    public ResponseEntity<Void> tornarOrganizador(@PathVariable Long id,
                                                  @RequestBody @Valid PerfilOrganizadorRequest request) {

        service.tornarOrganizador(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id,
                                                     @RequestBody UsuarioUpdateRequest request) {

        UsuarioResponse usuarioAtualizado = service.atualizar(id, request);

        return ResponseEntity.ok(usuarioAtualizado);
    }
}