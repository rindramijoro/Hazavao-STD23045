package com.my.company.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenAiService {
 private final WebClient webClient;
 @Value("${openai.api.key}")
 private String apiKey;

 @Value("${openai.api.url}")
 private String apiUrl;

 public OpenAiService(WebClient.Builder webClientBuilder) {
   this.webClient = webClientBuilder.build();
 }

 public String getDefinitionInMalagasy(String teny) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "Mamaly amin'ny teny malagasy. Omeo fanazavana tsotra sy mazava."),
                        Map.of("role", "user", "content", "Give me the meaning of the word \"" + teny + "\" in Malagasy.")
                ),
                "temperature", 0.7,
                "max_tokens", 150
        );

        try {
            Map<String, Object> response = webClient.post()
                    .uri(apiUrl)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || !response.containsKey("choices")) {
                return "No response from OpenAI.";
            }

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return message.get("content").toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Couldn't get the meaning from OpenAI.";
        }
    }
}
