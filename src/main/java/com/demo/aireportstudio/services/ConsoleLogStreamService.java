package com.demo.aireportstudio.services;

import com.demo.aireportstudio.config.XmlDataWebSocketHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Mirrors the backend console log (System.out) to the WebSocket console modal
 * of the request currently being processed.
 *
 * <p>A mirror stream replaces System.out at startup. While a report generation
 * request is running, its thread has a console session id set (equal to the
 * request id); every complete console line printed on that thread is forwarded
 * to the corresponding WebSocket session in addition to the real console.
 * Other threads are never forwarded because no session id is set for them.</p>
 *
 * <p>Usage:</p>
 * <pre>
 * consoleLogStreamService.setSessionId(requestId);
 * try {
 *     // ... run report generation ...
 * } finally {
 *     consoleLogStreamService.clearSessionId();
 * }
 * </pre>
 */
@Service
public class ConsoleLogStreamService {

    /** Maximum length of a single line forwarded over the WebSocket. */
    private static final int MAX_MESSAGE_LENGTH = 4000;

    /** The original console output stream captured before System.out is replaced. */
    private OutputStream originalOut;

    /** Console WebSocket session id for the current request thread. */
    private final ThreadLocal<String> consoleSessionId = new ThreadLocal<>();

    /** Per-thread buffer collecting bytes until a complete line is available. */
    private final ThreadLocal<ByteArrayOutputStream> lineBuffer =
            ThreadLocal.withInitial(ByteArrayOutputStream::new);

    @PostConstruct
    public void install() {
        originalOut = System.out;
        PrintStream mirrorStream = new PrintStream(
                new ConsoleMirrorOutputStream(originalOut), true, StandardCharsets.UTF_8);
        System.setOut(mirrorStream);
    }

    /**
     * Binds the console output of the current thread to a WebSocket session.
     *
     * @param sessionId the console session id (the report request id)
     */
    public void setSessionId(String sessionId) {
        consoleSessionId.set(sessionId);
    }

    /**
     * Unbinds the console output of the current thread and drops any buffered
     * incomplete line.
     */
    public void clearSessionId() {
        consoleSessionId.remove();
        lineBuffer.remove();
    }

    /**
     * Forwards complete console lines to the original console and, when a
     * console session is bound to the current thread, to the WebSocket session.
     */
    private class ConsoleMirrorOutputStream extends OutputStream {

        private final OutputStream original;

        ConsoleMirrorOutputStream(OutputStream original) {
            this.original = original;
        }

        @Override
        public void write(int b) throws IOException {
            original.write(b);
            processByte((byte) b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            original.write(b, off, len);
            for (int i = off; i < off + len; i++) {
                processByte(b[i]);
            }
        }

        @Override
        public void flush() throws IOException {
            original.flush();
        }

        private void processByte(byte value) {
            ByteArrayOutputStream buffer = lineBuffer.get();
            buffer.write(value);
            if (value == (byte) '\n') {
                String line = new String(buffer.toByteArray(), StandardCharsets.UTF_8).trim();
                buffer.reset();
                String sessionId = consoleSessionId.get();
                if (sessionId != null && !line.isEmpty()) {
                    forwardToWebSocket(sessionId, line);
                }
            }
        }

        private void forwardToWebSocket(String sessionId, String line) {
            for (int offset = 0; offset < line.length(); offset += MAX_MESSAGE_LENGTH) {
                int end = Math.min(offset + MAX_MESSAGE_LENGTH, line.length());
                XmlDataWebSocketHandler.sendMessageToSession(sessionId, line.substring(offset, end));
            }
        }
    }
}
