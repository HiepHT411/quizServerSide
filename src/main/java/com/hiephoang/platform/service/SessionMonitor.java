package com.hiephoang.platform.service;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
@Slf4j
@Service
public class SessionMonitor {
    private final ConcurrentMap<String, WebSocketSession> mapSessionId2WSSession = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, String> mapUsername2WSSession = new ConcurrentHashMap<>();

    public void registerSession(String sessionId, WebSocketSession wsSession) {
        mapSessionId2WSSession.put(sessionId, wsSession);
    }

    public WebSocketSession removeSession (String sessionId) {
        return mapSessionId2WSSession.remove(sessionId);
    }

    public void saveUserWSSession(String username, String sessionId) {
        mapUsername2WSSession.put(username, sessionId);
    }

    public String removeUserWSSession(String username) {
        return mapUsername2WSSession.remove(username);
    }

    @PreDestroy
    public void closeAllSessions () {
        for (WebSocketSession session : mapSessionId2WSSession.values()) {
            try {
                session.close();
            } catch (IOException e) {
                log.error("Error occurred while closing session {}", session.getId(), e);
            }
        }
        mapSessionId2WSSession.clear();
        mapUsername2WSSession.clear();
        log.info("Closed all websocket sessions");
    }
}
