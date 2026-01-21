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
        var auth = authenticationManager.authenticate(tokenSpring);
        var usuario = (Usuario) auth.getPrincipal();
        var tokenJWT = tokenService.gerarToken(usuario);

        Map<String, Object> response = new HashMap<>();
        response.put("token", tokenJWT);

        // Dados básicos
        response.put("id", usuario.getId());
        response.put("email", usuario.getEmail());
        response.put("organizadorAtivo", usuario.isOrganizador());

        if (usuario.getPessoa() != null) {
            response.put("nome", usuario.getPessoa().getNome());
            response.put("telefone", usuario.getPessoa().getTelefone());

            if (usuario.getPessoa().getEndereco() != null) {
                var end = usuario.getPessoa().getEndereco();

                response.put("rua", end.getRua());
                response.put("numero", end.getNumero());
                response.put("bairro", end.getBairro());
                response.put("cidade", end.getCidade());
                response.put("estado", end.getEstado());
                response.put("cep", end.getCep());


                String enderecoFormatado = String.format("%s, nº %s - %s. %s-%s. CEP: %s",
                        end.getRua() != null ? end.getRua() : "Rua não informada",
                        end.getNumero() != null ? end.getNumero() : "S/N",
                        end.getBairro() != null ? end.getBairro() : "",
                        end.getCidade() != null ? end.getCidade() : "",
                        end.getEstado() != null ? end.getEstado() : "",
                        end.getCep() != null ? end.getCep() : ""
                );

                enderecoFormatado = enderecoFormatado.replace(" . - .", "").trim();

                response.put("enderecoCompleto", enderecoFormatado);
                response.put("endereco", enderecoFormatado); // Mantém compatibilidade
            } else {
                response.put("enderecoCompleto", "Endereço não cadastrado");
            }
        } else {
            response.put("nome", "Não informado");
        }

        return ResponseEntity.ok(response);
    }
}