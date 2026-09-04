package com.demo.aireportstudio.services;

import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

@Service
public class JasperReportsErrorHandlerService {

    /**
     * Returns the current/latest JRXML compilation error as a single, readable text.
     * Only the error of the current compile attempt is returned - no accumulated
     * history. When the top-level message is missing, falls back to the first
     * meaningful message found in the cause chain.
     *
     * @param e the JRException thrown by the current compile attempt
     * @return the complete current/latest compilation error message
     */
    public String getCurrentCompilationError(JRException e) {
        String msg = e.getMessage();
        if (msg != null && !msg.trim().isEmpty()) {
            return msg.trim();
        }
        Throwable cause = e.getCause();
        int depth = 0;
        while (cause != null && depth < 10) {
            String causeMsg = cause.getMessage();
            if (causeMsg != null && !causeMsg.trim().isEmpty()) {
                return causeMsg.trim();
            }
            cause = cause.getCause();
            depth++;
        }
        return String.valueOf(msg);
    }

    public void logJasperReportsError(JRException e, String jrxmlContent) {
        System.out.println("=== JASPERREPORTS COMPILATION ERROR ===");
        System.out.println("JRXML Compilation failed with error: " + e.getMessage());
        System.out.println("Full JRXML content length: " + jrxmlContent.length());

        if (e.getMessage().contains("Invalid UUID string")) {
            System.out.println("ERROR TYPE: Invalid UUID in JRXML");
        } else if (e.getMessage().contains("SAXParseException")) {
            System.out.println("ERROR TYPE: XML parsing error in JRXML");
        } else if (e.getMessage().contains("compileReport")) {
            System.out.println("ERROR TYPE: JRXML compilation failure");
        } else if (e.getMessage().contains("fillReport")) {
            System.out.println("ERROR TYPE: Report filling failure");
        } else {
            System.out.println("ERROR TYPE: Unknown JasperReports error");
        }

        System.out.println("JRXML Preview (first 500 chars): " + jrxmlContent.substring(0, Math.min(500, jrxmlContent.length())));
    }

    public String buildJasperReportsErrorMessage(JRException e) {
        String jasperErrorMsg = "JasperReports Error: " + e.getMessage();
        if (e.getMessage().contains("Invalid UUID string")) {
            jasperErrorMsg += "\n\nCause: The generated JRXML contains invalid UUID values. Please check the UUID attributes in the XML.";
        } else if (e.getMessage().contains("SAXParseException")) {
            jasperErrorMsg += "\n\nCause: The generated JRXML has XML parsing errors. Please verify the XML structure and attributes.";
        } else if (e.getMessage().contains("compileReport")) {
            jasperErrorMsg += "\n\nCause: Failed to compile the JRXML report. Check for missing required elements or invalid attribute values.";
        } else if (e.getMessage().contains("fillReport")) {
            jasperErrorMsg += "\n\nCause: Failed to fill the report with data. Check the data source configuration.";
        }
        return jasperErrorMsg;
    }
}
