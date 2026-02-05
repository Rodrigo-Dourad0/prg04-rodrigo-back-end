package br.com.ifba.prg04_rodrigo_back_end.infraestructure.security;

import br.com.ifba.prg04_rodrigo_back_end.usuario.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Tenta recuperar o token do cabeçalho
            var token = this.recoverToken(request);

            // 2. Se o token existir, valida
            if (token != null) {
                // Tenta validar. Se o token estiver expirado ou inválido, o getSubject pode lançar erro.
                var login = tokenService.getSubject(token);

                if (login != null) {
                    // 3. Busca o usuário no banco
                    UserDetails usuario = repository.findByEmail(login).orElse(null);

                    if (usuario != null) {
                        // 4. Cria a autenticação forçada para este request
                        var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (Exception e) {

            System.out.println("Erro ao validar token no filtro (ignorando para permitir auth anônima): " + e.getMessage());
        }


        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}