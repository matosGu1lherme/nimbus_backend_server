package com.nimbus.nimbusWebServer.security.filter;

import com.nimbus.nimbusWebServer.config.SecurityConfig;
import com.nimbus.nimbusWebServer.implementation.UserDetailsImpl;
import com.nimbus.nimbusWebServer.models.user.User;
import com.nimbus.nimbusWebServer.repositories.UserRepository;
import com.nimbus.nimbusWebServer.services.AccessTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
                String token = recoveryToken(request);

                if (token == null) {
                    System.out.println(">>> [AUTH ERROR] Cookie 'accessToken' não encontrado na requisição!");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token ausente.");
                    return;
                }

                System.out.println("Token resgatado do Cookie: " + token);
                String subject = jwtTokenService.getSubjectFromToken(token);

                User user = userRepository.findByEmail(subject)
                        .orElseThrow(() -> {
                            return new RuntimeException("Usuário não encontrado");
                        });

                UserDetailsImpl userDetails = new UserDetailsImpl(user);

                // Cria um objeto de autenticação do Spring Security
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Define o objeto de autenticação no contexto de segurança do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
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
        Cookie[] cookies = request.getCookies();

        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    // Verifica se o endpoint requer autenticação antes de processar a requisição
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !Arrays.asList(SecurityConfig.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestURI);
    }
}
