package com.soluciona.soluciona.controller;

import com.soluciona.soluciona.dto.ServicePostRequestDTO;
import com.soluciona.soluciona.model.ServicePost;
import com.soluciona.soluciona.service.ServicePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
public class ServicePostController {

    @Autowired
    private ServicePostService servicePostService;

    @GetMapping
    public List<ServicePost> listServices() {
        return servicePostService.listActiveServices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePost> searchServiceById(@PathVariable Long id) {
        try {
            ServicePost servico = servicePostService.searchServiceById(id);
            return ResponseEntity.ok(servico);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @PostMapping
    public ResponseEntity<ServicePost> createService(
            @RequestBody ServicePostRequestDTO dto,
            Authentication authentication
    ) {
        try {
            UUID autorId = (UUID) authentication.getPrincipal();

            ServicePost novoAnuncio = servicePostService.createService(dto, autorId);
            return new ResponseEntity<>(novoAnuncio, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicePost> updateService(
            @PathVariable Long id,
            @RequestBody ServicePostRequestDTO dto,
            Authentication authentication
    ) {
        try {
            UUID autorIdDoToken = (UUID) authentication.getPrincipal();

            ServicePost anuncioAtualizado = servicePostService.updateService(id, dto, autorIdDoToken);

            return ResponseEntity.ok(anuncioAtualizado);

        } catch (RuntimeException e) {

            if (e.getMessage().startsWith("Acesso negado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            if (e.getMessage().startsWith("Anúncio não encontrado") || e.getMessage().startsWith("Nova categoria não encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteService(
            @PathVariable Long id,
            Authentication authentication
    ) {
        try {
            UUID autorIdDoToken = (UUID) authentication.getPrincipal();

            servicePostService.deleteService(id, autorIdDoToken);

            return ResponseEntity.ok("Anúncio apagado com sucesso!");

        } catch (RuntimeException e) {

            if (e.getMessage().startsWith("Acesso negado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            if (e.getMessage().startsWith("Anúncio não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}