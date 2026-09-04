package com.demo.aireportstudio.controller;

import com.demo.aireportstudio.model.SystemPrompt;
import com.demo.aireportstudio.services.SystemPromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class SystemPromptController {

    @Autowired
    private SystemPromptService systemPromptService;

    @GetMapping("/systemprompt")
    public String systemPromptPage(Model model) {
        model.addAttribute("systemPrompt", null);
        return "systemprompt";
    }

    @GetMapping("/systemprompt/{id}")
    public String editSystemPromptPage(@PathVariable String id, Model model) {
        Optional<SystemPrompt> systemPrompt = systemPromptService.findById(id);
        if (systemPrompt.isPresent()) {
            model.addAttribute("systemPrompt", systemPrompt.get());
        } else {
            model.addAttribute("systemPrompt", null);
        }
        return "systemprompt";
    }

    @RestController
    @RequestMapping("/api/systemprompt")
    public static class SystemPromptApiController {

        @Autowired
        private SystemPromptService systemPromptService;

        @GetMapping
        public List<SystemPrompt> getAllSystemPrompts() {
            return systemPromptService.findAll();
        }

        @GetMapping("/{id}")
        public Optional<SystemPrompt> getSystemPromptById(@PathVariable String id) {
            return systemPromptService.findById(id);
        }

        @PostMapping
        public SystemPrompt createSystemPrompt(@RequestBody SystemPrompt systemPrompt) {
            // Ensure ID is null for new entities
            systemPrompt.setSystemPromptId(null);
            return systemPromptService.save(systemPrompt);
        }

        @PutMapping("/{id}")
        public SystemPrompt updateSystemPrompt(@PathVariable String id, @RequestBody SystemPrompt systemPrompt) {
            Optional<SystemPrompt> existingSystemPrompt = systemPromptService.findById(id);
            if (existingSystemPrompt.isPresent()) {
                SystemPrompt existing = existingSystemPrompt.get();
                // Preserve createdAt and update other fields
                existing.setTitle(systemPrompt.getTitle());
                existing.setContent(systemPrompt.getContent());
                existing.setIsDeleted(systemPrompt.getIsDeleted() != null ? systemPrompt.getIsDeleted() : false);
                return systemPromptService.save(existing);
            } else {
                throw new IllegalArgumentException("SystemPrompt with ID '" + id + "' not found");
            }
        }

        @DeleteMapping("/{id}")
        public void deleteSystemPrompt(@PathVariable String id) {
            systemPromptService.deleteById(id);
        }
    }
}
