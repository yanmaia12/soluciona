package com.soluciona.soluciona.controller;

import com.soluciona.soluciona.dto.ChatRequestDTO;
import com.soluciona.soluciona.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping
    public ResponseEntity<Map<String, String>> askChatbot(@RequestBody ChatRequestDTO chatRequestDTO) {
        String response = chatService.getAIResponse(chatRequestDTO.getMessage());
        return ResponseEntity.ok(Map.of("response", response));
    }
}