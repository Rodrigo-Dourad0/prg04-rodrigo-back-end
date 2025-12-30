package br.com.ifba.prg04_rodrigo_back_end.infraestructure.security;

import br.com.ifba.prg04_rodrigo_back_end.usuario.entity.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // Gera o Token
    public String gerarToken(Usuario usuario) {
        try {

            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("API Roteiro Livre") // Quem gerou (sua API)
                    .withSubject(usuario.getEmail()) // Quem é o dono do token (email)
                    .withClaim("id", usuario.getId()) // Guarda o ID também para facilitar
                    .withExpiresAt(dataExpiracao()) // Validade
                    .sign(algoritmo); // Assina digitalmente
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // Valida o Token e recupera o Email
    public String getSubject(String tokenJWT) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Roteiro Livre")
                    .build()
                    .verify(tokenJWT) // Se o token for inválido ou expirado, lança exceção
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    // Define que o token expira em 1 hora
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }
}