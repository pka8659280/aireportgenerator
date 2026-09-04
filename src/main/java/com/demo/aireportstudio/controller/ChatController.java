package com.demo.aireportstudio.controller;

import com.demo.aireportstudio.services.DeepSeekService;
import com.demo.aireportstudio.services.DeepSeekService.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private DeepSeekService deepSeekService;

    @GetMapping("/chat")
    public String chatPage() {
        return "chat";
    }

    @RestController
    @RequestMapping("/api/chat")
    public static class ChatApiController {

        @Autowired
        private DeepSeekService deepSeekService;

        @PostMapping
        public ResponseEntity<?> chat(@RequestBody ChatRequest request) {
            if (request.getMessages() == null || request.getMessages().isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("messages must not be empty"));
            }
            try {
                String reply = deepSeekService.chat(request.getMessages());
                return ResponseEntity.ok(new ReplyResponse(reply));
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage()));
            }
        }
    }

    public static class ChatRequest {
        private List<ChatMessage> messages;

        public ChatRequest() {}

        public ChatRequest(List<ChatMessage> messages) {
            this.messages = messages;
        }

        public List<ChatMessage> getMessages() { return messages; }
        public void setMessages(List<ChatMessage> messages) { this.messages = messages; }
    }

    public record ReplyResponse(String reply) {}

    public record ErrorResponse(String error) {}
}
