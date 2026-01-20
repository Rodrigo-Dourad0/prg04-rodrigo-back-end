package br.com.ifba.prg04_rodrigo_back_end.usuario.controller;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.mapper.ObjectMapperUtil;
import br.com.ifba.prg04_rodrigo_back_end.organizador.dto.PerfilOrganizadorRequest;
import br.com.ifba.prg04_rodrigo_back_end.organizador.entity.Organizador;
import br.com.ifba.prg04_rodrigo_back_end.usuario.dto.UsuarioCreateRequest;
import br.com.ifba.prg04_rodrigo_back_end.usuario.dto.UsuarioUpdateRequest;
import br.com.ifba.prg04_rodrigo_back_end.usuario.dto.UsuarioResponse;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import br.com.ifba.prg04_rodrigo_back_end.usuario.service.UsuarioIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {


    private final UsuarioIService service;
    private final ObjectMapperUtil objectMapper;

    @GetMapping
    public ResponseEntity<Page<UsuarioResponse>> listarTodos(
            @PageableDefault(page = 0, size = 10, sort = "pessoa.nome") Pageable pageable) {

        Page<Usuario> usuariosPage = service.findAll(pageable);

     
        Page<UsuarioResponse> response = usuariosPage.map(usuario -> {

            UsuarioResponse dto = objectMapper.map(usuario, UsuarioResponse.class);

            dto.setOrganizadorAtivo(usuario.isOrganizador());


            if (usuario.getPessoa() != null) {
                dto.setNome(usuario.getPessoa().getNome());
            }

            return dto;
        });


        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody @Valid UsuarioCreateRequest request) {


        Usuario usuarioEntidade = objectMapper.map(request, Usuario.class);

        Usuario usuarioSalvo = service.save(usuarioEntidade);

        UsuarioResponse response = objectMapper.map(usuarioSalvo, UsuarioResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/tornar-organizador")
    public ResponseEntity<Void> tornarOrganizador(@RequestBody @Valid PerfilOrganizadorRequest request) {

        // Pega o usuário logado diretamente do SecurityContext (extraído do Token)
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Organizador perfilEntidade = objectMapper.map(request, Organizador.class);
        
        service.tornarOrganizador(usuarioLogado.getId(), perfilEntidade);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UsuarioResponse> atualizar(@RequestBody @Valid UsuarioUpdateRequest request) {

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        Usuario usuarioAtualizado = service.update(usuarioLogado.getId(), request);

        UsuarioResponse response = objectMapper.map(usuarioAtualizado, UsuarioResponse.class);

        return ResponseEntity.ok(response);
    }
}