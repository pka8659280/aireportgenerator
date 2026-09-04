package com.demo.aireportstudio.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class XmlDataWebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /** Per-session single-thread executors so console lines are delivered in order
     *  without ever blocking the System.out mirror thread (a slow/stalled client
     *  can no longer freeze console output for the whole application). */
    private static final Map<String, ExecutorService> sendExecutors = new ConcurrentHashMap<>();

    /** Outgoing message queue capacity per session; overflow lines are dropped. */
    private static final int MAX_QUEUED_MESSAGES = 10000;

    private static final AtomicInteger THREAD_SEQ = new AtomicInteger();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = getSessionId(session);
        // Reconnect with the same id: drop any previous send executor for it
        ExecutorService old = sendExecutors.remove(sessionId);
        if (old != null) {
            old.shutdownNow();
        }
        sessions.put(sessionId, session);
        System.out.println("Console WebSocket connection established: " + sessionId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        String sessionId = getSessionId(session);
        sessions.remove(sessionId);
        ExecutorService executor = sendExecutors.remove(sessionId);
        if (executor != null) {
            executor.shutdownNow();
        }
        System.out.println("Console WebSocket connection closed: " + sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Echo back the message for now
        session.sendMessage(new TextMessage("Received: " + message.getPayload()));
    }

    public static void sendMessageToSession(String sessionId, String message) {
        WebSocketSession session = sessions.get(sessionId);
        if (session == null || !session.isOpen()) {
            return;
        }
        ExecutorService executor = sendExecutors.computeIfAbsent(sessionId,
                id -> new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>(MAX_QUEUED_MESSAGES),
                        r -> {
                            Thread t = new Thread(r, "console-ws-sender-" + THREAD_SEQ.incrementAndGet());
                            t.setDaemon(true);
                            return t;
                        }));
        try {
            executor.execute(() -> {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(message));
                    }
                } catch (IOException e) {
                    System.err.println("Error sending message to session " + sessionId + ": " + e.getMessage());
                }
            });
        } catch (RejectedExecutionException e) {
            // Queue full (client is not reading fast enough): drop the line rather
            // than block the System.out mirror thread.
            System.err.println("Console WS queue full for session " + sessionId + ", dropping log line");
        }
    }

    public static void sendMessageToAll(String message) {
        for (String sessionId : sessions.keySet()) {
            sendMessageToSession(sessionId, message);
        }
    }

    private String getSessionId(WebSocketSession session) {
        // Get the unique ID from the query parameters or path variables
        String query = session.getUri().getQuery();
        if (query != null && query.contains("id=")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("id=")) {
                    return param.substring(3);
                }
            }
        }
        // Fallback to session ID if no unique ID provided
        return session.getId();
    }
}
