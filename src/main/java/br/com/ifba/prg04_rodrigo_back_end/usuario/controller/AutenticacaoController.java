package br.com.ifba.prg04_rodrigo_back_end.usuario.controller;

import br.com.ifba.prg04_rodrigo_back_end.infraestructure.security.TokenService;
import br.com.ifba.prg04_rodrigo_back_end.usuario.dto.AutenticacaoDto;
import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody @Valid AutenticacaoDto dados) {

        var tokenSpring = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());


        // Se a senha estiver errada, lança uma Exception automaticamente (403)
        var auth = authenticationManager.authenticate(tokenSpring);

        // Se chegou aqui, está logado. Gera o token.
        var tokenJWT = tokenService.gerarToken((Usuario) auth.getPrincipal());

        // Retorna o token em um JSON simples
        return ResponseEntity.ok(Map.of("token", tokenJWT));
    }
}