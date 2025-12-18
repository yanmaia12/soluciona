package com.soluciona.soluciona.controller;

import com.soluciona.soluciona.dto.FcmTokenDTO;
import com.soluciona.soluciona.model.Profiles;
import com.soluciona.soluciona.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "*")
public class ProfilesController {

    @Autowired
    private ProfileService profileService;

    @PostMapping
    public ResponseEntity<Profiles> createProfile(@RequestBody Profiles profiles){
        try {
            Profiles newProfile = profileService.createProfile(profiles);
            return new ResponseEntity<>(newProfile, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profiles> searchProfileByID(@PathVariable UUID id){
        try {
            Profiles perfil = profileService.searchProfileById(id);
            return ResponseEntity.ok(perfil);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profiles> updateProfile(@PathVariable UUID id,
                                                  @RequestBody Profiles dadosAtualizados){
        try {
            Profiles perfilAtualizado = profileService.updateProfile(id, dadosAtualizados, id);
            return ResponseEntity.ok(perfilAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/fcm-token")
    public ResponseEntity<Void> updateTokenFCM(@RequestBody FcmTokenDTO dto) {
        return ResponseEntity.ok().build();
    }
}