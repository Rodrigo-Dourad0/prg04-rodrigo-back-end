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

import java.util.HashMap;
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

        // Autentica o usuario. Se falhar, lança 403 automaticamente.
        var auth = authenticationManager.authenticate(tokenSpring);

        // Obtém o usuario autenticado
        var usuario = (Usuario) auth.getPrincipal();

        // Gera o token JWT
        var tokenJWT = tokenService.gerarToken(usuario);

        // Monta os dados de resposta
        Map<String, Object> response = new HashMap<>();
        response.put("token", tokenJWT);
        response.put("email", usuario.getEmail());

        // Extrai dados da Pessoa se existirem
        if (usuario.getPessoa() != null) {
            response.put("nome", usuario.getPessoa().getNome());
            response.put("telefone", usuario.getPessoa().getTelefone());

            // Extrai e formata o Endereço se existir
            if (usuario.getPessoa().getEndereco() != null) {
                var end = usuario.getPessoa().getEndereco();
                String enderecoFormatado = String.format("%s, %s - %s, %s/%s (CEP: %s)",
                        end.getRua(), end.getNumero(), end.getBairro(),
                        end.getCidade(), end.getEstado(), end.getCep());
                response.put("endereco", enderecoFormatado);
            } else {
                response.put("endereco", "Endereço não cadastrado");
            }
        } else {
            response.put("nome", "Não informado");
            response.put("telefone", "Não informado");
            response.put("endereco", "Não informado");
        }

        return ResponseEntity.ok(response);
    }
}