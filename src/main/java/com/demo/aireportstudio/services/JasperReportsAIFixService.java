package com.demo.aireportstudio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JasperReportsAIFixService {

    @Autowired
    private DeepSeekService deepSeekService;

    public String fixJrxml(String errorMessage, String jrxmlContent, String xmlData, String knowledge, int attempt, boolean sameErrorAsLast) throws InterruptedException {
        System.out.println("=== STARTING JRXML FIX PROCESS ===");
        System.out.println("Error to fix: " + errorMessage);
        System.out.println("JRXML length: " + jrxmlContent.length());
        System.out.println("XML Data length: " + (xmlData != null ? xmlData.length() : 0));
        System.out.println("Knowledge length: " + (knowledge != null ? knowledge.length() : 0));

        // Add delay before AI fix to prevent rapid successive API calls
        System.out.println("Waiting 2 seconds before calling AI fix...");
        Thread.sleep(2000); // 2 second delay

        // Get JasperReports version for context (hardcoded)
        String jasperVersion = "7.0.3";
        System.out.println("JasperReports version: " + jasperVersion);

        // Create a detailed prompt for the AI to fix the JRXML
        String attemptInfo = "";
        if (attempt > 1) {
            attemptInfo = "\n\nATTEMPT INFORMATION:\n" +
                         "This is attempt #" + attempt + " to fix this JRXML.\n";
            if (sameErrorAsLast) {
                attemptInfo += "IMPORTANT: The same error has occurred in previous attempts. You must try a completely different approach to fix this issue.\n" +
                              "Do NOT make the same changes that were tried before. Analyze the error more deeply and try alternative solutions.\n";
            }
        }

        // Include the full error details and context
        String errorContext = "";
        if (xmlData != null && !xmlData.trim().isEmpty()) {
            errorContext += "\nXML DATA CONTEXT:\n" + xmlData + "\n\n";
        }
        if (knowledge != null && !knowledge.trim().isEmpty()) {
            errorContext += "KNOWLEDGE CONTEXT:\n" + knowledge + "\n\n";
        }

        String fixPrompt = "You are an expert JasperReports developer. I have a JRXML file that is failing to compile. You need to fix it.\n\n" +
                          "COMPILATION ERROR DETAILS:\n" + errorMessage + "\n\n" +
                          "JASPERREPORTS VERSION: " + jasperVersion + "\n\n" + attemptInfo +
                          "JRXML CONTENT TO FIX:\n" + jrxmlContent + "\n\n" + errorContext +
                          "INSTRUCTIONS:\n" +
                          "1. Analyze the compilation error and identify what needs to be fixed\n" +
                          "2. Fix ONLY the problematic parts while preserving the rest of the JRXML structure\n" +
                          "3. Ensure all XML tags are properly closed and nested\n" +
                          "4. Make sure field names, variable names, and parameter names are consistent\n" +
                          "5. Verify that data types are correct (Integer, String, BigDecimal, etc.)\n" +
                          "6. Check that expression language is valid (using $F{}, $V{}, $P{}, etc.)\n" +
                          "7. Ensure band heights and element positions are reasonable\n" +
                          "8. Validate that all referenced fields exist in the data source\n\n" +
                          "COMMON JASPERREPORTS ERRORS TO WATCH FOR:\n" +
                          "- Missing or incorrect field declarations\n" +
                          "- Invalid expression syntax in textFieldExpression\n" +
                          "- Wrong data types for fields/parameters\n" +
                          "- Missing or incorrect XML namespaces\n" +
                          "- Invalid characters in element names\n" +
                          "- Incorrect band structure (detail, pageHeader, etc.)\n" +
                          "- Missing required attributes on elements\n" +
                          "- Schema validation errors (xsi:schemaLocation)\n\n" +
                          "OUTPUT REQUIREMENTS:\n" +
                          "Return ONLY the complete, corrected JRXML content.\n" +
                          "Start directly with the <jasperReport ...> root element, without any <?xml?> declaration.\n" +
                          "Do NOT include any explanations, comments, or markdown formatting.\n" +
                          "DO NOT include xsi:schemaLocation or any XML schema validation references.\n" +
                          "The output must be valid XML that can be compiled by JasperReports " + jasperVersion + ".\n" +
                          "Ensure the JRXML follows proper JasperReports structure with jasperReport as root element.";

        System.out.println("AI fix prompt length: " + fixPrompt.length());

        try {
            // Call DeepSeek API to fix the JRXML with original context
            long fixStart = System.currentTimeMillis();
            String apiResponse = deepSeekService.generate(xmlData, knowledge, "", fixPrompt);
            System.out.println("Received AI response for JRXML fix, elapsed: " + (System.currentTimeMillis() - fixStart)
                    + " ms, length: " + apiResponse.length());

            // Process the response to extract the fixed JRXML
            String fixedJrxml = extractJrxml(apiResponse);
            System.out.println("Extracted fixed JRXML, length: " + fixedJrxml.length());

            // Save the extracted JRXML that will be compiled, for debugging
            deepSeekService.saveResult(fixedJrxml);

            return fixedJrxml;

        } catch (Exception e) {
            System.out.println("Error during JRXML fix process: " + e.getMessage());
            throw new RuntimeException("Failed to fix JRXML: " + e.getMessage(), e);
        }
    }



    private String extractJrxml(String apiResponse) {
        // The AI should return only the JRXML content, but we need to extract it
        // by cutting off any prose before the <jasperReport root element and after the closing tag.
        String cleaned = apiResponse;

        // Cut off any prose before the <jasperReport root element (drops any <?xml?> declaration)
        int xmlStart = cleaned.indexOf("<jasperReport");
        if (xmlStart > 0) {
            cleaned = cleaned.substring(xmlStart);
        }

        // Cut off any trailing text after the closing tag
        int reportEnd = cleaned.lastIndexOf("</jasperReport>");
        if (reportEnd != -1) {
            cleaned = cleaned.substring(0, reportEnd + "</jasperReport>".length());
        }

        cleaned = cleaned.trim();

        System.out.println("Extracted JRXML from AI response, length: " + cleaned.length());
        return cleaned;
    }
}
