package com.soluciona.soluciona.config; // 1. Garante que está no pacote "config"

// --- IMPORTS NECESSÁRIOS ---
import com.soluciona.soluciona.service.JwtService; // O "Leitor" que já fizeste
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
// --- FIM DOS IMPORTS ---

@Component // 2. Diz ao Spring que esta é uma peça (Componente)
public class JwtAuthFilter extends OncePerRequestFilter { // 3. Um filtro que corre 1x por requisição

    @Autowired
    private JwtService jwtService; // 4. Pede o "Leitor de Crachás"

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 5. Pega no cabeçalho "Authorization" (onde vem o token)
        final String authHeader = request.getHeader("Authorization");

        // 6. Se não houver token, ou não começar com "Bearer ", manda seguir e pronto.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Manda para o próximo filtro
            return; // Sai do método
        }

        // 7. Se tiver "Bearer ", pega no token (o que vem depois de "Bearer ")
        final String jwt = authHeader.substring(7);

        try {
            // 8. Usa o JwtService para validar e extrair o ID do usuário (o UUID)
            final UUID userId = jwtService.extractUserId(jwt);

            // 9. Se o token for válido...
            if (userId != null && jwtService.isTokenValid(jwt)) {

                // 10. DIZ AO SPRING SECURITY: "ESTE UTILIZADOR ESTÁ LOGADO!"
                // Criamos um "crachá" interno do Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId, // O "Principal" (quem é) é o UUID do usuário
                        null,   // Não precisamos de credenciais
                        new ArrayList<>() // Não tem "Roles" (Autoridades) por agora
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 11. Coloca o "crachá" no "bolso" do Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            // Se o token for inválido (expirado, assinatura errada), vai dar erro
            // Nós não fazemos nada, só deixamos seguir...
            // O Spring Security vai barrá-lo mais à frente por não ter autenticação
        }

        // 12. Manda a requisição continuar o seu caminho
        filterChain.doFilter(request, response);
    }
}