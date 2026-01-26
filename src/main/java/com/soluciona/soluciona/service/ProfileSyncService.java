package com.soluciona.soluciona.service;

import com.soluciona.soluciona.model.Profiles;
import com.soluciona.soluciona.repository.ProfilesRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProfileSyncService {

    @Autowired
    private ProfilesRepository profileRepository;

    @Value("${supabase.jwt.secret}")
    private String jwtSecret;

    @Transactional
    public Profiles ensureProfileExists(UUID userId, String jwtToken) {
        return profileRepository.findById(userId)
                .orElseGet(() -> createProfileFromToken(userId, jwtToken));
    }

    private Profiles createProfileFromToken(UUID userId, String jwtToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes())
                    .build()
                    .parseClaimsJws(jwtToken.replace("Bearer ", ""))
                    .getBody();

            Profiles newProfile = new Profiles();
            newProfile.setId(userId);
            newProfile.setEmail(claims.get("email", String.class));

            String name = claims.get("name", String.class);
            if (name == null && claims.get("user_metadata") != null) {
                @SuppressWarnings("unchecked")
                var metadata = (java.util.Map<String, Object>) claims.get("user_metadata");
                name = (String) metadata.get("full_name");
            }
            newProfile.setName(name != null ? name : "Utilizador");

            return profileRepository.save(newProfile);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar perfil a partir do token: " + e.getMessage());
        }
    }
}