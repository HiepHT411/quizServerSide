package com.example.GoQuiz.service;

import com.example.GoQuiz.payload.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class WSRequestProcessor {

    private final SessionMonitor sessionMonitor;

    public void handleMessage(Message msg) {
        for (WebSocketSession session : sessionMonitor.getMapSessionId2WSSession().values()) {
            try {
                session.sendMessage(new TextMessage(msg.toString()));
            } catch (IOException e) {
                log.error("Error occurred while sending message {} by sessionID {}", msg, session.getId(), e);
            }
        }

    }
}
