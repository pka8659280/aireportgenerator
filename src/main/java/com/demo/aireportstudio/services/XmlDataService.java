package com.demo.aireportstudio.services;

import com.demo.aireportstudio.model.XmlData;
import com.demo.aireportstudio.repository.XmlDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class XmlDataService {

    private static final Logger logger = LoggerFactory.getLogger(XmlDataService.class);

    @Autowired
    private XmlDataRepository xmlDataRepository;

    @Autowired
    private DeepSeekService deepSeekService;

    public List<XmlData> findAll() {
        logger.debug("Finding all active XML data entries");
        List<XmlData> xmlDataList = xmlDataRepository.findAllByIsDeletedFalse();
        logger.debug("Found {} active XML data entries", xmlDataList.size());
        return xmlDataList;
    }

    public Optional<XmlData> findById(String id) {
        logger.debug("Finding XML data by ID: {}", id);
        if (!StringUtils.hasText(id)) {
            logger.warn("Attempted to find XML data with null or empty ID");
            return Optional.empty();
        }
        Optional<XmlData> xmlData = xmlDataRepository.findByXmlDataIdAndIsDeletedFalse(id);
        if (xmlData.isPresent()) {
            logger.debug("Found active XML data with ID: {}", id);
        } else {
            logger.debug("No active XML data found with ID: {}", id);
        }
        return xmlData;
    }

    public Optional<XmlData> findByTitle(String title) {
        logger.debug("Finding XML data by title: {}", title);
        if (!StringUtils.hasText(title)) {
            logger.warn("Attempted to find XML data with null or empty title");
            return Optional.empty();
        }
        Optional<XmlData> xmlData = xmlDataRepository.findByTitleAndIsDeletedFalse(title);
        if (xmlData.isPresent()) {
            logger.debug("Found active XML data with title: {}", title);
        } else {
            logger.debug("No active XML data found with title: {}", title);
        }
        return xmlData;
    }

    public XmlData save(XmlData xmlData) {
        logger.debug("Saving XML data: {}", xmlData);

        // Ensure isDeleted is set for new entities
        if (xmlData.getIsDeleted() == null) {
            xmlData.setIsDeleted(false);
        }

        // Validate input
        validateContent(xmlData);

        // Check for duplicate titles
        validateUniqueTitle(xmlData);

        XmlData savedXmlData = xmlDataRepository.save(xmlData);
        logger.info("Successfully saved XML data with ID: {}", savedXmlData.getXmlDataId());
        return savedXmlData;
    }

    public void deleteById(String id) {
        logger.debug("Deleting XML data by ID: {}", id);
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("XML data ID cannot be null or empty");
        }

        if (!xmlDataRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent XML data with ID: {}", id);
            throw new IllegalArgumentException("XML data with ID '" + id + "' does not exist");
        }

        xmlDataRepository.deleteById(id);
        logger.info("Successfully deleted XML data with ID: {}", id);
    }

    public boolean existsByTitle(String title) {
        logger.debug("Checking if active XML data exists with title: {}", title);
        if (!StringUtils.hasText(title)) {
            return false;
        }
        return xmlDataRepository.existsByTitleAndIsDeletedFalse(title);
    }

    public long count() {
        long count = xmlDataRepository.count();
        logger.debug("Total XML data entries count: {}", count);
        return count;
    }

    public List<XmlData> findByTitleContaining(String keyword) {
        logger.debug("Finding active XML data entries containing keyword: {}", keyword);
        if (!StringUtils.hasText(keyword)) {
            return List.of();
        }
        List<XmlData> results = xmlDataRepository.findAllByIsDeletedFalse().stream()
                .filter(x -> x.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                           x.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        logger.debug("Found {} active XML data entries containing keyword: {}", results.size(), keyword);
        return results;
    }

    private void validateContent(XmlData xmlData) {
        if (xmlData == null) {
            throw new IllegalArgumentException("XML data cannot be null");
        }

        if (!StringUtils.hasText(xmlData.getTitle())) {
            throw new IllegalArgumentException("XML data title cannot be null or empty");
        }

        if (!StringUtils.hasText(xmlData.getContent())) {
            throw new IllegalArgumentException("XML data content cannot be null or empty");
        }

        // Validate title length
        if (xmlData.getTitle().length() > 500) {
            throw new IllegalArgumentException("XML data title cannot exceed 500 characters");
        }
    }

    private void validateUniqueTitle(XmlData xmlData) {
        Optional<XmlData> existing = xmlDataRepository.findByTitleAndIsDeletedFalse(xmlData.getTitle());

        if (xmlData.getXmlDataId() == null || xmlData.getXmlDataId().isEmpty()) {
            // New entry
            if (existing.isPresent()) {
                throw new IllegalArgumentException("XML data with title '" + xmlData.getTitle() + "' already exists");
            }
        } else {
            // Update - check if title conflicts with other entries
            if (existing.isPresent() && !existing.get().getXmlDataId().equals(xmlData.getXmlDataId())) {
                throw new IllegalArgumentException("XML data with title '" + xmlData.getTitle() + "' already exists");
            }
        }
    }

    /**
     * Validates XML data relational consistency using DeepSeek AI
     * @param xmlContent The XML content to validate
     * @return Map containing validation results (xmlStatus and xmlMessage)
     */
    public Map<String, Object> validateXmlRelationalConsistency(String xmlContent) {
        try {
            logger.debug("Validating XML relational consistency with DeepSeek AI");

            String validationPrompt =
                "You are an expert XML validator focused on relational integrity.\n\n" +
                "XML Data to validate:\n" + xmlContent + "\n\n" +
                "Task:\n" +
                "Before saving, validate that all XML data is relationally consistent.\n" +
                "Ensure that every primary key exists, all foreign (secondary) keys correctly reference existing primary keys, and that all defined relationships are valid.\n\n" +
                "Validation rules:\n" +
                "- Detect missing primary keys\n" +
                "- Detect missing foreign keys\n" +
                "- Detect foreign keys referencing non-existent primary keys\n" +
                "- Detect orphaned or broken relationships between XML nodes\n\n" +
                "CRITICAL RESPONSE FORMAT:\n" +
                "Return ONLY a valid JSON object with exactly these fields and nothing else:\n" +
                "- xmlStatus: 'SUCCESS' if no relational issues are found, otherwise 'FAILURE'\n" +
                "- xmlMessage: a concise list identifying each XML node with missing or invalid primary keys, foreign keys, or relationships\n\n" +
                "Formatting constraints:\n" +
                "- xmlMessage must list node names and key types (PRIMARY_KEY, FOREIGN_KEY, RELATION)\n" +
                "- Do not exceed 300 characters\n" +
                "- No explanations, markdown, code blocks, or additional text\n\n" +
                "Start the response with { and end with }.";

            // Call DeepSeek AI
            String aiResponse = deepSeekService.generate("", "", "", validationPrompt);

            // Clean the AI response to remove any markdown formatting
            String cleanResponse = aiResponse.trim();

            // Remove markdown code blocks if present
            if (cleanResponse.startsWith("```json")) {
                cleanResponse = cleanResponse.substring(7);
            } else if (cleanResponse.startsWith("```")) {
                cleanResponse = cleanResponse.substring(3);
            }

            if (cleanResponse.endsWith("```")) {
                cleanResponse = cleanResponse.substring(0, cleanResponse.length() - 3);
            }

            // Remove any leading/trailing whitespace again
            cleanResponse = cleanResponse.trim();

            // If the response doesn't start with {, try to find the JSON content
            if (!cleanResponse.startsWith("{")) {
                int jsonStart = cleanResponse.indexOf("{");
                if (jsonStart != -1) {
                    cleanResponse = cleanResponse.substring(jsonStart);
                }
            }

            // If the response doesn't end with }, try to find the JSON end
            if (!cleanResponse.endsWith("}")) {
                int jsonEnd = cleanResponse.lastIndexOf("}");
                if (jsonEnd != -1) {
                    cleanResponse = cleanResponse.substring(0, jsonEnd + 1);
                }
            }

            logger.debug("Cleaned AI response: {}", cleanResponse);

            // Parse the JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(cleanResponse);

            // Extract required fields
            String xmlStatus = jsonResponse.has("xmlStatus") ? jsonResponse.get("xmlStatus").asText() : "UNKNOWN";
            String xmlMessage = jsonResponse.has("xmlMessage") ? jsonResponse.get("xmlMessage").asText() : "Analysis completed";

            // Validate that we got expected status
            if (!"SUCCESS".equals(xmlStatus) && !"FAILURE".equals(xmlStatus)) {
                xmlStatus = "UNKNOWN";
            }

            logger.info("XML validation completed with status: {}", xmlStatus);

            return Map.of(
                "xmlStatus", xmlStatus,
                "xmlMessage", xmlMessage
            );

        } catch (Exception e) {
            logger.error("Error during XML validation with DeepSeek AI", e);
            return Map.of(
                "xmlStatus", "FAILURE",
                "xmlMessage", "Validation failed due to system error: " + e.getMessage()
            );
        }
    }
}
