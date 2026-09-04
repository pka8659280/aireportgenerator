package com.demo.aireportstudio.controller;

import com.demo.aireportstudio.model.UserPrompt;
import com.demo.aireportstudio.services.UserPromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserPromptController {

    @Autowired
    private UserPromptService userPromptService;

    @GetMapping("/userprompt")
    public String userPromptPage(Model model) {
        model.addAttribute("userPrompt", null);
        return "userprompt";
    }

    @GetMapping("/userprompt/{id}")
    public String editUserPromptPage(@PathVariable String id, Model model) {
        Optional<UserPrompt> userPrompt = userPromptService.findById(id);
        if (userPrompt.isPresent()) {
            model.addAttribute("userPrompt", userPrompt.get());
        } else {
            model.addAttribute("userPrompt", null);
        }
        return "userprompt";
    }

    @RestController
    @RequestMapping("/api/userprompt")
    public static class UserPromptApiController {

        @Autowired
        private UserPromptService userPromptService;

        @GetMapping
        public List<UserPrompt> getAllUserPrompts() {
            return userPromptService.findAll();
        }

        @GetMapping("/{id}")
        public Optional<UserPrompt> getUserPromptById(@PathVariable String id) {
            return userPromptService.findById(id);
        }

        @PostMapping
        public UserPrompt createUserPrompt(@RequestBody UserPrompt userPrompt) {
            // Ensure ID is null for new entities
            userPrompt.setUserPromptId(null);
            return userPromptService.save(userPrompt);
        }

        @PutMapping("/{id}")
        public UserPrompt updateUserPrompt(@PathVariable String id, @RequestBody UserPrompt userPrompt) {
            Optional<UserPrompt> existingUserPrompt = userPromptService.findById(id);
            if (existingUserPrompt.isPresent()) {
                UserPrompt existing = existingUserPrompt.get();
                // Preserve createdAt and update other fields
                existing.setTitle(userPrompt.getTitle());
                existing.setContent(userPrompt.getContent());
                existing.setIsDeleted(userPrompt.getIsDeleted() != null ? userPrompt.getIsDeleted() : false);
                return userPromptService.save(existing);
            } else {
                throw new IllegalArgumentException("UserPrompt with ID '" + id + "' not found");
            }
        }

        @DeleteMapping("/{id}")
        public void deleteUserPrompt(@PathVariable String id) {
            userPromptService.deleteById(id);
        }
    }
}
