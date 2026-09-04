package com.demo.aireportstudio.controller;

import com.demo.aireportstudio.model.XmlData;
import com.demo.aireportstudio.services.ConsoleWebSocketService;
import com.demo.aireportstudio.services.XmlDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

/**
 * XML Data API Controller with console feedback and AI validation support.
 *
 * XML Data Operations with Console Feedback:
 *
 * POST /api/xmldata?sessionId=uuid-123
 * Body: {"title": "My XML", "content": "<xml>...</xml>"}
 * → Creates XML data and automatically sends demo messages with auto-save
 *
 * PUT /api/xmldata/123?sessionId=uuid-123
 * Body: {"title": "Updated XML", "content": "<xml>...</xml>"}
 * → Updates XML data with progress feedback to console session
 *
 * DELETE /api/xmldata/123?sessionId=uuid-123
 * → Deletes XML data with progress feedback to console session
 *
 * XML Validation with DeepSeek AI:
 *
 * POST /api/xmldata/validate
 * Body: "<xml>content</xml>"
 * → Returns {"xmlStatus": "SUCCESS|FAILURE", "xmlMessage": "description"}
 *
 * The sessionId parameter is optional for XML operations.
 * When provided, feedback messages are sent to the specified console WebSocket session.
 * POST operations automatically trigger the demo message sequence after successful creation.
 * All saves are validated for relational consistency before completion.
 */
@RestController
@RequestMapping("/api/xmldata")
public class XmlDataApiController {

    @Autowired
    private XmlDataService xmlDataService;

    @Autowired
    private ConsoleWebSocketService consoleWebSocketService;

    @GetMapping
    public List<XmlData> getAllXmlData() {
        return xmlDataService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<XmlData> getXmlDataById(@PathVariable String id) {
        return xmlDataService.findById(id);
    }

    @PostMapping
    public XmlData createXmlData(@RequestBody XmlData xmlData, @RequestParam(required = false) String sessionId) {
        try {
            // Ensure ID is null for new entities
            xmlData.setXmlDataId(null);

            if (sessionId != null && !sessionId.isEmpty()) {
                consoleWebSocketService.sendProgressUpdate(sessionId, "Creating new XML data entry...");
            }

            XmlData savedXmlData = xmlDataService.save(xmlData);

            if (sessionId != null && !sessionId.isEmpty()) {
                consoleWebSocketService.sendSuccessMessage(sessionId, "XML data created successfully with ID: " + savedXmlData.getXmlDataId());

                // Automatically start demo message sequence after creation
                new Thread(() -> {
                    try {
                        Thread.sleep(2000); // Wait 2 seconds before starting demo
                        for (int i = 1; i <= 5; i++) {
                            consoleWebSocketService.sendMessageToConsole(sessionId, "Demo message " + i + " from server");
                            Thread.sleep(10000); // 10 seconds between messages
                        }
                        // After 5 messages, trigger auto-save
                        Thread.sleep(1000); // Small delay before auto-save
                        consoleWebSocketService.triggerAutoSave(sessionId);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }

            return savedXmlData;
        } catch (Exception e) {
            if (sessionId != null && !sessionId.isEmpty()) {
                consoleWebSocketService.sendErrorMessage(sessionId, "Failed to create XML data: " + e.getMessage());
            }
            throw e;
        }
    }

    @PutMapping("/{id}")
    public XmlData updateXmlData(@PathVariable String id, @RequestBody XmlData xmlData, @RequestParam(required = false) String sessionId) {
        try {
            Optional<XmlData> existingXmlData = xmlDataService.findById(id);
            if (existingXmlData.isPresent()) {
                XmlData existing = existingXmlData.get();

                if (sessionId != null && !sessionId.isEmpty()) {
                    consoleWebSocketService.sendProgressUpdate(sessionId, "Updating XML data entry with ID: " + id);
                }

                // Preserve createdAt and update other fields
                existing.setTitle(xmlData.getTitle());
                existing.setContent(xmlData.getContent());
                existing.setIsDeleted(xmlData.getIsDeleted() != null ? xmlData.getIsDeleted() : false);

                XmlData updatedXmlData = xmlDataService.save(existing);

                if (sessionId != null && !sessionId.isEmpty()) {
                    consoleWebSocketService.sendSuccessMessage(sessionId, "XML data updated successfully for ID: " + id);
                }

                return updatedXmlData;
            } else {
                if (sessionId != null && !sessionId.isEmpty()) {
                    consoleWebSocketService.sendErrorMessage(sessionId, "XML data with ID '" + id + "' not found");
                }
                throw new IllegalArgumentException("XML data with ID '" + id + "' not found");
            }
        } catch (Exception e) {
            if (sessionId != null && !sessionId.isEmpty()) {
                consoleWebSocketService.sendErrorMessage(sessionId, "Failed to update XML data: " + e.getMessage());
            }
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteXmlData(@PathVariable String id, @RequestParam(required = false) String sessionId) {
        try {
            if (sessionId != null && !sessionId.isEmpty()) {
                consoleWebSocketService.sendProgressUpdate(sessionId, "Deleting XML data entry with ID: " + id);
            }

            xmlDataService.deleteById(id);

            if (sessionId != null && !sessionId.isEmpty()) {
                consoleWebSocketService.sendSuccessMessage(sessionId, "XML data deleted successfully for ID: " + id);
            }
        } catch (Exception e) {
            if (sessionId != null && !sessionId.isEmpty()) {
                consoleWebSocketService.sendErrorMessage(sessionId, "Failed to delete XML data: " + e.getMessage());
            }
            throw e;
        }
    }

    @PostMapping("/validate")
    public Map<String, Object> validateXml(@RequestBody String xmlContent) {
        return xmlDataService.validateXmlRelationalConsistency(xmlContent);
    }

}
