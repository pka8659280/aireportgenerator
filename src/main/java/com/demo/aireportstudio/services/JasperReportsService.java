package com.demo.aireportstudio.services;

import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JasperReportsService {

    @Autowired
    private JasperReportsCompilerService compilerService;

    /**
     * Main entry point for JasperReports PDF generation.
     * This service now delegates to specialized services for better separation of concerns.
     *
     * @param jrxmlContent The JRXML content to compile and generate PDF from
     * @param xmlData Original XML data for context (used by AI fix service)
     * @param knowledge Original knowledge base for context (used by AI fix service)
     * @return byte array containing the generated PDF
     * @throws JRException if JasperReports compilation/filling fails
     * @throws InterruptedException if the operation is interrupted
     */
    public byte[] compileAndGeneratePDF(String jrxmlContent, String xmlData, String knowledge) throws JRException, InterruptedException {
        return compilerService.compileAndGeneratePDF(jrxmlContent, xmlData, knowledge);
    }

    /**
     * Legacy method for backward compatibility.
     * @deprecated Use compileAndGeneratePDF(String jrxmlContent, String xmlData, String knowledge) instead
     */
    @Deprecated
    public byte[] compileAndGeneratePDF(String jrxmlContent) throws JRException, InterruptedException {
        return compilerService.compileAndGeneratePDF(jrxmlContent, "", "");
    }
}
