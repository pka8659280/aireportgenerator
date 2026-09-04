package com.demo.aireportstudio.services;

import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportGenerationService {

    @Autowired
    private JRXMLGenerationService jrxmlGenerationService;

    @Autowired
    private JasperReportsService jasperReportsService;

    // Store the last generated JRXML in memory
    private String lastGeneratedJRXML;

    /**
     * Orchestrates the complete report generation process
     *
     * @param xmlData The XML data for the report
     * @param knowledge The knowledge base/template
     * @param systemPrompt The system prompt
     * @param userPrompt The user prompt
     * @return byte array containing the generated PDF
     * @throws JRException if JasperReports compilation fails
     * @throws InterruptedException if the process is interrupted
     */
    public byte[] generateReport(String xmlData, String knowledge, String systemPrompt, String userPrompt) throws JRException, InterruptedException {
        long totalStart = System.currentTimeMillis();
        System.out.println("=== STARTING COMPLETE REPORT GENERATION PROCESS ===");

        // Step 1: Generate JRXML using AI
        long step1Start = System.currentTimeMillis();
        String jrxmlContent = jrxmlGenerationService.generateJRXML(xmlData, knowledge, systemPrompt, userPrompt);
        System.out.println("Step 1 completed: JRXML generation took " + (System.currentTimeMillis() - step1Start) + " ms");

        // Step 2: Store JRXML content in memory for reference
        long step2Start = System.currentTimeMillis();
        this.lastGeneratedJRXML = jrxmlContent;
        System.out.println("Step 2 completed: JRXML stored in memory, length=" + jrxmlContent.length()
                + ", took " + (System.currentTimeMillis() - step2Start) + " ms");

        // Step 3: Compile JRXML and generate PDF
        long step3Start = System.currentTimeMillis();
        byte[] pdfBytes = jasperReportsService.compileAndGeneratePDF(jrxmlContent, xmlData, knowledge);
        System.out.println("Step 3 completed: PDF generation took " + (System.currentTimeMillis() - step3Start)
                + " ms, size=" + pdfBytes.length + " bytes");

        System.out.println("=== REPORT GENERATION PROCESS COMPLETED SUCCESSFULLY in "
                + (System.currentTimeMillis() - totalStart) + " ms ===");
        return pdfBytes;
    }

    /**
     * Get the last generated JRXML content
     *
     * @return the last generated JRXML as string, or error message if none available
     */
    public String getLastGeneratedJRXML() {
        if (lastGeneratedJRXML != null) {
            return lastGeneratedJRXML;
        } else {
            return "No JRXML generated yet";
        }
    }
}
