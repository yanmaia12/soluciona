package com.soluciona.soluciona.controller;

import com.soluciona.soluciona.dto.RequestCreateDTO;
import com.soluciona.soluciona.dto.RequestStatusUpdateDTO;
import com.soluciona.soluciona.model.Requests;
import com.soluciona.soluciona.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
public class RequestsController {

    @Autowired
    private RequestService requestService;

    @PostMapping
    public ResponseEntity<Requests> createOrder(
                                                 @RequestBody RequestCreateDTO dto,
                                                 Authentication authentication
    ) {
        try {
            UUID clienteId = (UUID) authentication.getPrincipal();

            Requests novoPedido = requestService.createOrder(dto, clienteId);
            return new ResponseEntity<>(novoPedido, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            System.err.println("ERRO AO CRIAR PEDIDO: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/myOrders")
    public ResponseEntity<Map<String, List<Requests>>> seeMyOrders(
                                                                    Authentication authentication
    ) {
        try {
            UUID userId = (UUID) authentication.getPrincipal();

            Map<String, List<Requests>> meusPedidos = requestService.seeMyOrders(userId);
            return ResponseEntity.ok(meusPedidos);
        } catch (RuntimeException e) {
            System.err.println("ERRO AO BUSCAR PEDIDOS: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Requests> updateStatusOrder(
            @PathVariable Long id,
            @RequestBody RequestStatusUpdateDTO dto,
            Authentication authentication
    ) {
        try {
            UUID userIdDoToken = (UUID) authentication.getPrincipal();

            Requests pedidoAtualizado = requestService.updateStatusOrder(id, dto, userIdDoToken);

            return ResponseEntity.ok(pedidoAtualizado);

        } catch (RuntimeException e) {

            if (e.getMessage().startsWith("Acesso negado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            if (e.getMessage().startsWith("Pedido (Request) não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}