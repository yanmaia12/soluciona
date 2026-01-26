package com.soluciona.soluciona.config;

import com.soluciona.soluciona.service.JwtService;
import com.soluciona.soluciona.service.ProfileSyncService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ProfileSyncService profileSyncService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            final UUID userId = jwtService.extractUserId(jwt);

            if (userId != null && jwtService.isTokenValid(jwt)) {

                try {
                    profileSyncService.ensureProfileExists(userId, jwt);
                } catch (Exception syncError) {
                    // Se falhar a sincronização, retorna erro 500 com mensagem clara
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json");
                    response.getWriter().write(
                            "{\"error\": \"Falha ao sincronizar perfil: " +
                                    syncError.getMessage() + "\"}"
                    );
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        new ArrayList<>()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            System.err.println("⚠️ Token inválido ou expirado: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}