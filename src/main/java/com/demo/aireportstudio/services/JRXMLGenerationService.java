package com.demo.aireportstudio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JRXMLGenerationService {

    @Autowired
    private DeepSeekService deepSeekService;

    /**
     * Generates JRXML content using DeepSeek API
     *
     * @param xmlData The XML data to base the report on
     * @param knowledge The knowledge base/template for JRXML structure
     * @param systemPrompt The system prompt
     * @param userPrompt The user prompt
     * @return The generated JRXML content as a string
     * @throws InterruptedException if the API call is interrupted
     */
    public String generateJRXML(String xmlData, String knowledge, String systemPrompt, String userPrompt) throws InterruptedException {
        long start = System.currentTimeMillis();
        System.out.println("=== GENERATING JRXML VIA DEEPSEEK ===");
        System.out.println("XML Data length: " + (xmlData != null ? xmlData.length() : 0));
        System.out.println("Knowledge length: " + (knowledge != null ? knowledge.length() : 0));
        System.out.println("System Prompt length: " + (systemPrompt != null ? systemPrompt.length() : 0));
        System.out.println("User Prompt length: " + (userPrompt != null ? userPrompt.length() : 0));

        String jrxmlContent = deepSeekService.generate(xmlData, knowledge, systemPrompt, userPrompt);
        System.out.println("JRXML generation successful, content length: " + jrxmlContent.length()
                + ", elapsed: " + (System.currentTimeMillis() - start) + " ms");

        // Post-process to cut off any prose before <?xml and after </jasperReport>
        jrxmlContent = extractJrxml(jrxmlContent);
        System.out.println("JRXML post-processing completed, length after extraction: " + jrxmlContent.length());
        System.out.println("JRXML preview (first 200 chars): "
                + jrxmlContent.substring(0, Math.min(200, jrxmlContent.length())));

        // Save the extracted JRXML that will be compiled, for debugging
        deepSeekService.saveResult(jrxmlContent);

        return jrxmlContent;
    }

    /**
     * Extracts the JRXML from a DeepSeek response by cutting off any text
     * before the XML declaration and any text after the closing tag.
     *
     * @param apiResponse the raw DeepSeek reply
     * @return the trimmed JRXML content
     */
    private String extractJrxml(String apiResponse) {
        String cleaned = apiResponse;

        // Cut off any prose before the <jasperReport root element (drops any <?xml?> declaration)
        int xmlStart = cleaned.indexOf("<jasperReport");
        System.out.println("extractJrxml: <jasperReport index: " + xmlStart + " (input length: " + cleaned.length() + ")");
        if (xmlStart > 0) {
            cleaned = cleaned.substring(xmlStart);
        }

        // Cut off any trailing text after the closing tag
        int reportEnd = cleaned.lastIndexOf("</jasperReport>");
        System.out.println("extractJrxml: closing tag index: " + reportEnd);
        if (reportEnd != -1) {
            cleaned = cleaned.substring(0, reportEnd + "</jasperReport>".length());
        }

        return cleaned.trim();
    }
}
