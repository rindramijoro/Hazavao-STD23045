package com.my.company.endpoint.rest.controller.health;

import com.my.company.service.OpenAiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HazavaoController {
    private final OpenAiService openAiService;

    public HazavaoController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @GetMapping("/hazavao")
    public Map<String, String> getDefinition(@RequestParam String teny) {
        String definition = openAiService.getDefinitionInMalagasy(teny);
        return Map.of("teny", teny, "hevitra", definition);
    }
}
