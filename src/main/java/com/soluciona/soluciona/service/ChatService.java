package com.soluciona.soluciona.service;

import com.soluciona.soluciona.dto.GeminiRequestDTO;
import com.soluciona.soluciona.dto.GeminiResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class ChatService {

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAIResponse(String userMessage) {
        String finalUrl = apiUrl + apiKey;

        String contexto = """
            Você é o assistente virtual oficial da plataforma 'Soluciona'.
            O Soluciona é um aplicativo que conecta clientes a prestadores de serviços domésticos (limpeza, obras, aulas, etc).
            Regras:
            1. Seja educado, útil e vá direto ao ponto.
            2. Se perguntarem preços, diga que depende do prestador.
            3. Incentive o usuário a navegar nas categorias do app.
            4. Responda sempre em Português de Portugal.
            
            Pergunta do usuário: 
            """ + userMessage;

        GeminiRequestDTO.Part part = new GeminiRequestDTO.Part(contexto);
        GeminiRequestDTO.Content content = new GeminiRequestDTO.Content(Collections.singletonList(part));
        GeminiRequestDTO request = new GeminiRequestDTO(Collections.singletonList(content));

        try {
            GeminiResponseDTO response = restTemplate.postForObject(finalUrl, request, GeminiResponseDTO.class);

            if (response != null && !response.getCandidates().isEmpty()) {
                return response.getCandidates().get(0).getContent().getParts().get(0).getText();
            }
            return "Sem resposta da IA.";
        } catch (Exception e) {
            return "Erro ao comunicar com a IA: " + e.getMessage();
        }
    }
}