package com.demo.aireportstudio.services;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.pdf.JRPdfExporter;
import net.sf.jasperreports.pdf.SimplePdfExporterConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Service
public class JasperReportsCompilerService {

    @Autowired
    private JasperReportsErrorHandlerService errorHandlerService;

    @Autowired
    private JasperReportsAIFixService aiFixService;

    /** Maximum compile+AI-fix attempts for the generated JRXML (see application.properties). */
    @Value("${app.jrxml.max-fix-attempts:5}")
    private int maxFixAttempts;

    /** Give up when the same compile error repeats this many consecutive times. */
    @Value("${app.jrxml.max-same-error-attempts:3}")
    private int maxSameErrorAttempts;

    public byte[] compileAndGeneratePDF(String jrxmlContent, String xmlData, String knowledge) 
            throws JRException, InterruptedException {
        
        System.out.println("=== STARTING JASPERREPORTS PROCESSING ===");

        // Parse the selected XML data once. The whole document is always used as
        // a single record — the report's XPath query "*" selects the root element,
        // so the detail band renders exactly once.
        // When no XML data is supplied (legacy callers), the fill falls back to
        // an empty bean data source.
        Document xmlDocument = null;
        if (xmlData != null && !xmlData.trim().isEmpty()) {
            try {
                xmlDocument = parseXmlDocument(xmlData);
                System.out.println("XML data source prepared (single record).");
            } catch (Exception e) {
                System.out.println("Selected XML data is not valid: " + e.getMessage());
                throw new RuntimeException("Selected XML data is not valid: " + e.getMessage(), e);
            }
        }

        String currentJrxml = jrxmlContent;
        int maxAttempts = maxFixAttempts;
        int attempt = 0;
        String lastErrorMessage = "";
        int consecutiveSameErrorCount = 0;
        int consecutiveFixFailures = 0;

        while (attempt < maxAttempts) {
            if (Thread.currentThread().isInterrupted()) {
                throw new RuntimeException("PDF generation cancelled due to interruption");
            }

            attempt++;
            System.out.println("=== ATTEMPT " + attempt + " OF " + maxAttempts + " ===");
            System.out.println("JRXML length: " + currentJrxml.length());

            // Filter out markdown code blocks before compilation
            currentJrxml = filterOutMarkdownCodeBlocks(currentJrxml);

            try {
                // 1. Compile JRXML
                System.out.println("Compiling JRXML report...");
                long compileStart = System.currentTimeMillis();
                JasperReport jasperReport = JasperCompileManager.compileReport(
                        new ByteArrayInputStream(currentJrxml.getBytes(StandardCharsets.UTF_8)));
                System.out.println("Compile took " + (System.currentTimeMillis() - compileStart) + " ms");

                // 2. Fill report with the selected XML data source
                System.out.println("Filling report...");
                JRDataSource dataSource;
                if (xmlDocument != null) {
                    // Always treat the whole XML document as a single record so the
                    // detail band renders exactly once (the JRXML query "*" selects
                    // the root element).
                    System.out.println("Using XML data source (single record)");
                    dataSource = new JRXmlDataSource(xmlDocument);
                } else {
                    System.out.println("Using empty bean data source (no XML data provided)");
                    dataSource = new JRBeanCollectionDataSource(Collections.singletonList(new Object()));
                }
                long fillStart = System.currentTimeMillis();
                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        jasperReport,
                        null,  // ← put your real parameters map here if needed
                        dataSource
                );
                System.out.println("Fill took " + (System.currentTimeMillis() - fillStart) + " ms");

                // 3. Export to PDF (modern way - JasperReports 6.12+ / 7.x)
                System.out.println("Exporting to PDF...");
                long exportStart = System.currentTimeMillis();
                ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

                JRPdfExporter exporter = new JRPdfExporter();
                
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfOutputStream));

                // PDF settings
                SimplePdfExporterConfiguration pdfConfig = new SimplePdfExporterConfiguration();
                pdfConfig.setMetadataAuthor("AI Reporting Studio");
                pdfConfig.setMetadataTitle("Generated Report - Attempt " + attempt);
                pdfConfig.setMetadataCreator("Your Company Name");
                pdfConfig.setEncrypted(false);
                // pdfConfig.setAllowedPermissions(PdfPermissions.PRINTING | PdfPermissions.COPY); // optional
                // pdfConfig.setCompression(); // optional

                exporter.setConfiguration(pdfConfig);

                exporter.exportReport();
                System.out.println("Export took " + (System.currentTimeMillis() - exportStart) + " ms");

                byte[] pdfBytes = pdfOutputStream.toByteArray();
                System.out.println("PDF generation successful - size: " + pdfBytes.length + " bytes");

                return pdfBytes;

            } catch (JRException e) {
                String currentError = errorHandlerService.getCurrentCompilationError(e);
                System.out.println("=== COMPILATION/FILL/EXPORT ERROR ON ATTEMPT " + attempt + " ===");
                System.out.println("JRXML Compilation Error: " + currentError);
                System.out.println("Full Error Details:");
                System.out.println("-------------------");
                e.printStackTrace(System.out);
                System.out.println("-------------------");

                errorHandlerService.logJasperReportsError(e, currentJrxml);

                // Give up when the same compile error repeats too many times in a
                // row: the AI fix is not making progress, so stop retrying.
                boolean sameError = currentError.equals(lastErrorMessage);
                lastErrorMessage = currentError;
                consecutiveSameErrorCount = sameError ? consecutiveSameErrorCount + 1 : 1;

                if (attempt >= maxAttempts || consecutiveSameErrorCount >= maxSameErrorAttempts) {
                    System.out.println("Maximum attempts reached (same error repeated "
                            + consecutiveSameErrorCount + "x). Giving up.");
                    throw new RuntimeException(errorHandlerService.buildJasperReportsErrorMessage(e), e);
                }

                // Try AI auto-fix
                System.out.println("Attempting AI fix...");
                try {
                    currentJrxml = aiFixService.fixJrxml(
                            currentError,
                            currentJrxml,
                            xmlData,
                            knowledge,
                            attempt,
                            sameError
                    );
                    System.out.println("AI suggested new JRXML version → retrying...");
                    consecutiveFixFailures = 0;
                } catch (Exception fixEx) {
                    System.out.println("AI fix failed: " + fixEx.getMessage());
                    consecutiveFixFailures++;
                    // Propagate cancellation immediately so Stop works during the fix phase
                    if (Thread.currentThread().isInterrupted()) {
                        throw new RuntimeException("PDF generation cancelled due to interruption", fixEx);
                    }
                    // A fix call that keeps failing (e.g. AI service busy) is not going to
                    // get better by retrying the same broken JRXML - give up with the
                    // original compile error so the user sees a real failure, not a spin.
                    if (consecutiveFixFailures >= 2) {
                        System.out.println("AI fix failed twice in a row. Giving up.");
                        throw new RuntimeException(errorHandlerService.buildJasperReportsErrorMessage(e), e);
                    }
                }

            } catch (IllegalStateException e) {
                // Handle container shutdown (typical in Spring Boot dev/restart)
                if (e.getMessage() != null && e.getMessage().contains("web application instance has been stopped")) {
                    throw new RuntimeException("PDF generation interrupted due to application restart", e);
                }
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Unexpected error during PDF generation", e);
            }
        }

        throw new RuntimeException("Failed to generate PDF after " + maxAttempts + " attempts");
    }

    /**
     * Filters out markdown code blocks from JRXML content
     *
     * @param jrxmlContent The JRXML content that may contain markdown
     * @return The cleaned JRXML content without markdown formatting
     */
    private String filterOutMarkdownCodeBlocks(String jrxmlContent) {
        if (jrxmlContent == null) {
            return null;
        }

        // Remove every markdown fence line anywhere in the content (```, ```xml, ~~~, ~~~xml)
        String cleaned = jrxmlContent.replaceAll("(?m)^\\s*(```+|~~~+)[^\\r\\n]*\\r?\\n?", "");

        // Cut off any prose before the <jasperReport root element — any <?xml?>
        // declaration is dropped too, the compile input must start at <jasperReport
        int xmlStart = cleaned.indexOf("<jasperReport");
        if (xmlStart > 0) {
            cleaned = cleaned.substring(xmlStart);
        }

        // Cut off any trailing text after the closing tag
        int reportEnd = cleaned.lastIndexOf("</jasperReport>");
        if (reportEnd != -1) {
            cleaned = cleaned.substring(0, reportEnd + "</jasperReport>".length());
        }

        return cleaned.trim();
    }

    /**
     * Parses the selected XML data into a DOM document with XXE protections enabled.
     *
     * @param xmlData the raw XML content
     * @return the parsed DOM document
     * @throws Exception if the XML cannot be parsed
     */
    private Document parseXmlDocument(String xmlData) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Basic XXE hardening - the XML data is user supplied
        try {
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        } catch (ParserConfigurationException e) {
            // Feature not supported by this JVM's parser; continue with default settings
        }
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8)));
    }
}
