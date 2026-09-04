package com.demo.aireportstudio.controller;

import com.demo.aireportstudio.services.ConsoleLogStreamService;
import com.demo.aireportstudio.services.DeepSeekService;
import com.demo.aireportstudio.services.ReportGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/ai-reporting-studio")
public class AIReportingStudioApiController {

    @Autowired
    private ReportGenerationService reportGenerationService;

    @Autowired
    private DeepSeekService deepSeekService;

    @Autowired
    private ConsoleLogStreamService consoleLogStreamService;

    @GetMapping("/jrxml")
    public ResponseEntity<String> getJRXML() {
        try {
            String jrxmlContent = reportGenerationService.getLastGeneratedJRXML();
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_XML)
                    .body(jrxmlContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Error retrieving JRXML content: " + e.getMessage());
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateReport(@RequestBody ReportRequest request) {
        String requestId = (request.getRequestId() == null || request.getRequestId().isBlank())
                ? UUID.randomUUID().toString() : request.getRequestId();
        final long requestStart = System.currentTimeMillis();
        System.out.println("=== GENERATE REQUEST RECEIVED: requestId=" + requestId + " ===");
        System.out.println("Input xmlData length: " + (request.getXmlData() == null ? 0 : request.getXmlData().length())
                + ", knowledge length: " + (request.getKnowledge() == null ? 0 : request.getKnowledge().length())
                + ", systemPrompt length: " + (request.getSystemPrompt() == null ? 0 : request.getSystemPrompt().length())
                + ", userPrompt length: " + (request.getUserPrompt() == null ? 0 : request.getUserPrompt().length()));
        deepSeekService.register(requestId);
        // Stream the backend console log of this request to the console WebSocket
        consoleLogStreamService.setSessionId(requestId);

        try {
            // Generate the report using the service
            byte[] pdfBytes = reportGenerationService.generateReport(
                    request.getXmlData(),
                    request.getKnowledge(),
                    request.getSystemPrompt(),
                    request.getUserPrompt());

            // Return PDF with appropriate headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "generated_report.pdf");
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            // Return error response with a friendly message instead of raw exception text
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(("{\"error\": \"" + friendlyGenerateError(e) + "\"}").getBytes());
        } finally {
            long elapsed = System.currentTimeMillis() - requestStart;
            System.out.println("=== GENERATE REQUEST FINISHED: requestId=" + requestId + ", elapsed=" + elapsed + " ms ===");
            deepSeekService.unregister(requestId);
            consoleLogStreamService.clearSessionId();
            Thread.interrupted(); // clear interrupt flag so the pooled thread is not left interrupted
        }
    }

    /**
     * Stops a running report generation, aborting the in-flight DeepSeek call
     * and interrupting the request thread.
     *
     * @param body JSON body with the requestId of the generation to stop
     * @return {"stopped": true/false}
     */
    @PostMapping("/stop")
    public ResponseEntity<Map<String, Object>> stopGeneration(@RequestBody Map<String, String> body) {
        String requestId = body.get("requestId");
        if (requestId == null || requestId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("stopped", false));
        }
        return ResponseEntity.ok(Map.of("stopped", deepSeekService.cancel(requestId)));
    }

    /**
     * Polishes/optimizes a user prompt into the structured report format via
     * the DeepSeek API. The selected XML data payload is forwarded so the
     * polish is based on the actual report data. Errors are classified
     * (rate limit / timeout / 5xx / auth) and returned as friendly messages.
     *
     * @param body JSON body with the "content" to optimize and optional "xmlData"
     * @return 200 {"content": "<polished text>"} or 400/503 {"error": "..."}
     */
    @PostMapping("/polish")
    public ResponseEntity<Map<String, String>> polishPrompt(@RequestBody Map<String, String> body) {
        String content = body.get("content");
        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Input content is empty, please fill in the User Prompt content first"));
        }
        String xmlData = body.get("xmlData");
        // Register the request so the frontend can stop the in-flight DeepSeek call
        String requestId = (body.get("requestId") == null || body.get("requestId").isBlank())
                ? UUID.randomUUID().toString() : body.get("requestId");
        deepSeekService.register(requestId);
        try {
            String polished = deepSeekService.polishUserPrompt(content, xmlData);
            return ResponseEntity.ok(Map.of("content", polished));
        } catch (Exception e) {
            System.out.println("AI polish failed: " + e.getMessage());
            return ResponseEntity.status(503)
                    .body(Map.of("error", friendlyPolishError(e)));
        } finally {
            deepSeekService.unregister(requestId);
            Thread.interrupted(); // clear interrupt flag so the pooled thread is not left interrupted
        }
    }

    /**
     * Refines only the grammar of the System Prompt content via the DeepSeek API.
     * Unlike {@link #polishPrompt(Map)}, this never rewrites the report format or
     * enriches XML data - it only fixes spelling, punctuation and grammar errors.
     *
     * @param body JSON body with the "content" whose grammar should be refined
     * @return 200 {"content": "<refined text>"} or 400/503 {"error": "..."}
     */
    @PostMapping("/polish-grammar")
    public ResponseEntity<Map<String, String>> polishGrammar(@RequestBody Map<String, String> body) {
        String content = body.get("content");
        if (content == null || content.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Input content is empty, please fill in the System Prompt content first"));
        }
        // Register the request so the frontend can stop the in-flight DeepSeek call
        String requestId = (body.get("requestId") == null || body.get("requestId").isBlank())
                ? UUID.randomUUID().toString() : body.get("requestId");
        deepSeekService.register(requestId);
        try {
            String polished = deepSeekService.polishGrammar(content);
            return ResponseEntity.ok(Map.of("content", polished));
        } catch (Exception e) {
            System.out.println("Grammar polish failed: " + e.getMessage());
            return ResponseEntity.status(503)
                    .body(Map.of("error", friendlyPolishError(e)));
        } finally {
            deepSeekService.unregister(requestId);
            Thread.interrupted(); // clear interrupt flag so the pooled thread is not left interrupted
        }
    }

    private String friendlyPolishError(Exception e) {
        String message = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
        if (message.contains("429")) {
            return "AI service request rate limit exceeded, please try again later";
        }
        if (message.contains("timed out") || message.contains("timeout")) {
            return "Network connection timed out, please check your network and try again";
        }
        if (message.contains("503")) {
            return "AI service is temporarily busy, please try again later";
        }
        if (message.contains("api key") || message.contains("401") || message.contains("403")) {
            return "AI service authentication failed, please contact the administrator to check the configuration";
        }
        return "AI optimization failed, please try again later";
    }

    /**
     * Maps a report-generation failure to a friendly, user-facing message.
     * The raw exception (including the full JasperReports compile error) is
     * always logged to the backend console for debugging.
     */
    private String friendlyGenerateError(Exception e) {
        String message = e.getMessage() == null ? "" : e.getMessage().toLowerCase();
        if (message.contains("cancelled")) {
            return "Generation stopped by user";
        }
        if (message.contains("503") || message.contains("busy")
                || message.contains("retries") || message.contains("overloaded")
                || message.contains("unresponsive")) {
            return "AI service is temporarily busy, please wait a moment and try again";
        }
        if (message.contains("timed out") || message.contains("timeout")) {
            return "Network connection timed out, please check your network and try again";
        }
        if (message.contains("api key") || message.contains("401") || message.contains("403")) {
            return "AI service authentication failed, please contact the administrator to check the configuration";
        }
        return "Report generation failed, please try again";
    }

    @PostMapping("/generate/json")
    public ResponseEntity<?> generateReportJson(
            @RequestBody ReportRequest request) {

        try {
            // Generate the report using the service
            byte[] pdfBytes = reportGenerationService.generateReport(
                    request.getXmlData(),
                    request.getKnowledge(),
                    request.getSystemPrompt(),
                    request.getUserPrompt());

            // Return PDF with appropriate headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "generated_report.pdf");
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            // Return error response with a friendly message instead of raw exception text
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"" + friendlyGenerateError(e) + "\"}");
        }
    }

    // Request DTO for JSON API
    public static class ReportRequest {
        private String xmlData;
        private String knowledge;
        private String systemPrompt;
        private String userPrompt;
        private String requestId;

        public ReportRequest() {}

        public ReportRequest(String xmlData, String knowledge, String systemPrompt, String userPrompt) {
            this.xmlData = xmlData;
            this.knowledge = knowledge;
            this.systemPrompt = systemPrompt;
            this.userPrompt = userPrompt;
        }

        public String getXmlData() { return xmlData; }
        public void setXmlData(String xmlData) { this.xmlData = xmlData; }

        public String getKnowledge() { return knowledge; }
        public void setKnowledge(String knowledge) { this.knowledge = knowledge; }

        public String getSystemPrompt() { return systemPrompt; }
        public void setSystemPrompt(String systemPrompt) { this.systemPrompt = systemPrompt; }

        public String getUserPrompt() { return userPrompt; }
        public void setUserPrompt(String userPrompt) { this.userPrompt = userPrompt; }

        public String getRequestId() { return requestId; }
        public void setRequestId(String requestId) { this.requestId = requestId; }
    }
}
