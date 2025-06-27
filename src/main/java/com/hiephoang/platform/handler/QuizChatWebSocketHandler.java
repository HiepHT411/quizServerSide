package com.hiephoang.platform.handler;

import com.hiephoang.platform.payload.Message;
import com.hiephoang.platform.service.SessionMonitor;
import com.hiephoang.platform.service.WSRequestProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class QuizChatWebSocketHandler extends BinaryWebSocketHandler implements SmartLifecycle {

    private AtomicBoolean running = new AtomicBoolean(false);

    @Autowired
    private SessionMonitor sessionMonitor;

    @Autowired
    private WSRequestProcessor requestProcessor;

    @Override
    public void start() {
        if(!running.get())
            running.set(true);
    }

    @Override
    public void stop() {
        if (running.get()) {
            log.info("Stop QuizChatWebSocketHandler");
            running.set(false);
        }
    }


    @Override
    public boolean isRunning() {
        return running.get();
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        super.afterConnectionEstablished(session);
        try {
            WebSocketSession webSocketSession = new ConcurrentWebSocketSessionDecorator(session, 10000, 4194304);
            sessionMonitor.registerSession(webSocketSession.getId(), webSocketSession);
        } catch (Exception e) {
            log.error("Error afterConnectionEstablished", e);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("Start handle text message: {}", message);
        try {
            Message msg = Message.parseFrom(message.getPayload());

            if (msg.getMessage().equals("JOIN")) {
                sessionMonitor.saveUserWSSession(msg.getUsername(), session.getId());
            }
            requestProcessor.handleMessage(msg);
        } catch (JsonProcessingException e) {
            log.error("Error parsing message {}", message);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("Start handle message?: {}", message);
        super.handleMessage(session, message);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.info("Start handle binary message: {}", message);
        super.handleBinaryMessage(session, message);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        super.afterConnectionClosed(session, status);
        WebSocketSession wrapSession = sessionMonitor.removeSession(session.getId());
        if (Objects.nonNull(wrapSession))
            if (wrapSession.isOpen())
                wrapSession.close();
        log.info("closed ws connection sid={}", session.getId());
    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }
}
