package com.demo.aireportstudio.services;

import com.demo.aireportstudio.config.XmlDataWebSocketHandler;
import org.springframework.stereotype.Service;

/**
 * Service for sending messages to console WebSocket sessions.
 *
 * Usage examples:
 *
 * @Autowired
 * private ConsoleWebSocketService consoleService;
 *
 * // Send message to specific console
 * consoleService.sendMessageToConsole("session-uuid-123", "Hello from service!");
 *
 * // Send to all consoles
 * consoleService.broadcastToAllConsoles("System message to all users");
 *
 * // Trigger auto-save
 * consoleService.triggerAutoSave("session-uuid-123");
 */
@Service
public class ConsoleWebSocketService {

    /**
     * Send a message to a specific console session
     * @param sessionId The unique session ID of the console modal
     * @param message The message to send
     */
    public void sendMessageToConsole(String sessionId, String message) {
        XmlDataWebSocketHandler.sendMessageToSession(sessionId, message);
    }

    /**
     * Send a message to all connected console sessions
     * @param message The message to broadcast
     */
    public void broadcastToAllConsoles(String message) {
        XmlDataWebSocketHandler.sendMessageToAll(message);
    }

    /**
     * Send a demo message sequence to a specific console session
     * @param sessionId The unique session ID of the console modal
     */
    public void sendDemoMessages(String sessionId) {
        // This could trigger the demo sequence if needed
        sendMessageToConsole(sessionId, "Starting demo sequence...");
    }

    /**
     * Send an auto-save command to a specific console session
     * @param sessionId The unique session ID of the console modal
     */
    public void triggerAutoSave(String sessionId) {
        sendMessageToConsole(sessionId, "AUTO_SAVE");
    }

    /**
     * Send progress update to console
     * @param sessionId The unique session ID of the console modal
     * @param progress Current progress (e.g., "Processing item 5 of 10")
     */
    public void sendProgressUpdate(String sessionId, String progress) {
        sendMessageToConsole(sessionId, "Progress: " + progress);
    }

    /**
     * Send error message to console
     * @param sessionId The unique session ID of the console modal
     * @param error The error message
     */
    public void sendErrorMessage(String sessionId, String error) {
        sendMessageToConsole(sessionId, "ERROR: " + error);
    }

    /**
     * Send success message to console
     * @param sessionId The unique session ID of the console modal
     * @param message The success message
     */
    public void sendSuccessMessage(String sessionId, String message) {
        sendMessageToConsole(sessionId, "SUCCESS: " + message);
    }
}
