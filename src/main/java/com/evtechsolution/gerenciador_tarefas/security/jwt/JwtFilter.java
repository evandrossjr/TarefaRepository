package com.evtechsolution.gerenciador_tarefas.security.jwt;

import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                String username = JwtUtil.validateToken(token);
                // Aqui você pode adicionar o usuário no contexto de segurança, se necessário
            } catch (RuntimeException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token não fornecido");
            return;
        }

        filterChain.doFilter(request, response);
    }
}