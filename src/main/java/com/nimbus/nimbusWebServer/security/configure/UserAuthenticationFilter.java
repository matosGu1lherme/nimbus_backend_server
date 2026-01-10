package com.nimbus.nimbusWebServer.security.configure;

import com.nimbus.nimbusWebServer.implementation.UserDetailsImpl;
import com.nimbus.nimbusWebServer.models.user.User;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import com.nimbus.nimbusWebServer.services.AccessTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AccessTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(checkIfEndpointIsNotPublic(request)) {
            try {
                System.out.println(">>> [AUTH CHECK] Acessando rota privada: " + request.getRequestURI());

                String token = recoveryToken(request);
                System.out.println("Token que esta sendo resgatado: " + token);

                if (token == null) {
                    System.out.println(">>> [AUTH ERROR] Token não encontrado no Header Authorization!");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token ausente.");
                    return;
                }

                String subject = jwtTokenService.getSubjectFromToken(token);
                System.out.println(">>> [AUTH INFO] Subject extraído do token: " + subject);

                User user = userRepository.findByEmail(subject)
                        .orElseThrow(() -> {
                            System.out.println(">>> [AUTH ERROR] Usuário não existe no banco de dados: " + subject);
                            return new RuntimeException("Usuário não encontrado");
                        });

                UserDetailsImpl userDetails = new UserDetailsImpl(user);

                System.out.println(">>> [AUTH SUCCESS] Usuário encontrado: " + user.getEmail());
                System.out.println(">>> [AUTH SUCCESS] Roles do usuário: " + userDetails.getAuthorities());

                // Cria um objeto de autenticação do Spring Security
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Define o objeto de autenticação no contexto de segurança do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println(">>> [AUTH CONTEXT] Autenticação definida no SecurityContext com sucesso.");
            } catch (Exception e) {
                System.out.println(">>> [AUTH EXCEPTION] Falha crítica no filtro: " + e.getMessage());
                e.printStackTrace(); // Importante para ver o stacktrace completo no log do Render
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("Erro ao autenticar: " + e.getMessage());
                response.getWriter().flush();
                return;
            }
        } else {
            System.out.println(">>> [AUTH SKIP] Rota pública detectada: " + request.getRequestURI());
        }
        filterChain.doFilter(request, response); // Continua o processamento da requisição
    }

    // Recupera o token do cabeçalho Authorization da requisição
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Verifica se o endpoint requer autenticação antes de processar a requisição
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }
}
