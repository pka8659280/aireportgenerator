package com.demo.aireportstudio.controller;

import com.demo.aireportstudio.model.Knowledge;
import com.demo.aireportstudio.services.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    @GetMapping("/knowledge")
    public String knowledgePage(Model model) {
        model.addAttribute("knowledge", null);
        return "knowledge";
    }

    @GetMapping("/knowledge/{id}")
    public String editKnowledgePage(@PathVariable String id, Model model) {
        Optional<Knowledge> knowledge = knowledgeService.findById(id);
        if (knowledge.isPresent()) {
            model.addAttribute("knowledge", knowledge.get());
        } else {
            model.addAttribute("knowledge", null);
        }
        return "knowledge";
    }

    @RestController
    @RequestMapping("/api/knowledge")
    public static class KnowledgeApiController {

        @Autowired
        private KnowledgeService knowledgeService;

        @GetMapping
        public List<Knowledge> getAllKnowledge() {
            return knowledgeService.findAll();
        }

        @GetMapping("/{id}")
        public Optional<Knowledge> getKnowledgeById(@PathVariable String id) {
            return knowledgeService.findById(id);
        }

        @PostMapping
        public Knowledge createKnowledge(@RequestBody Knowledge knowledge) {
            // Ensure ID is null for new entities
            knowledge.setKnowledgeId(null);
            return knowledgeService.save(knowledge);
        }

        @PutMapping("/{id}")
        public Knowledge updateKnowledge(@PathVariable String id, @RequestBody Knowledge knowledge) {
            Optional<Knowledge> existingKnowledge = knowledgeService.findById(id);
            if (existingKnowledge.isPresent()) {
                Knowledge existing = existingKnowledge.get();
                // Preserve createdAt and update other fields
                existing.setTitle(knowledge.getTitle());
                existing.setContent(knowledge.getContent());
                existing.setIsDeleted(knowledge.getIsDeleted() != null ? knowledge.getIsDeleted() : false);
                return knowledgeService.save(existing);
            } else {
                throw new IllegalArgumentException("Knowledge with ID '" + id + "' not found");
            }
        }

        @DeleteMapping("/{id}")
        public void deleteKnowledge(@PathVariable String id) {
            knowledgeService.deleteById(id);
        }
    }
}
