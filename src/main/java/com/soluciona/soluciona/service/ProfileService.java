package com.soluciona.soluciona.service;

import com.soluciona.soluciona.model.Profiles;
import com.soluciona.soluciona.repository.ProfilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Service
public class ProfileService{

    @Autowired
    private ProfilesRepository profilesRepository;

    public Profiles createProfile(Profiles profiles){

        if (profiles.getId() == null){
            throw new RuntimeException("É necessário um ID fornecido pela base de dados!");
        }

        if (profilesRepository.existsById(profiles.getId())){
            throw new RuntimeException("Já existe um usuário com esse id registrado!");
        }

        return profilesRepository.save(profiles);
    }

    public Profiles searchProfileById(@PathVariable UUID id){
        return profilesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado com esse ID: " + id));
    }

    public Profiles updateProfile(UUID id, Profiles dadosnovos, UUID idToken){
        if (!id.equals(idToken)){
            throw new RuntimeException("Acesso negado: só o dono do perfil pode atualizar-lo!");
        }

        Profiles perfilOriginal = searchProfileById(id);

        perfilOriginal.setName(dadosnovos.getName());
        perfilOriginal.setPhoneNumber(dadosnovos.getPhoneNumber());
        perfilOriginal.setProfilePicture(dadosnovos.getProfilePicture());
        perfilOriginal.setDefaultLocation(dadosnovos.getDefaultLocation());

        return profilesRepository.save(perfilOriginal);
    }

    public void atualizarTokenFCM(UUID userId, String token) {

        Profiles perfil = profilesRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado."));

        perfil.setFcmToken(token);
        profilesRepository.save(perfil);
    }
}
