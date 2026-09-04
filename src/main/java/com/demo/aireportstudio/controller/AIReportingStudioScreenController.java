package com.demo.aireportstudio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AIReportingStudioScreenController {

    @GetMapping("/ai-reporting-studio")
    public String aiReportingStudio(Model model) {
        model.addAttribute("xmlData", "");
        model.addAttribute("knowledge", "");
        model.addAttribute("systemPrompt", "");
        model.addAttribute("prompt", "");

        return "ai-reporting-studio";
    }

    @GetMapping("/aireport")
    public String redirectToAiReportingStudio() {
        return "redirect:/ai-reporting-studio";
    }
}
